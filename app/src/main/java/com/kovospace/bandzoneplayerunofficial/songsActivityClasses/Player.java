package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.helpers.Connection;
import com.kovospace.bandzoneplayerunofficial.helpers.PlayerHelper;
import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private static final int SEEKBAR_REFRESH_RATE = 250;
    private static final int USED_IN_BAND_PROFILE = 1;
    private static final int USED_IN_BANDS_LIST = 2;

    private static int playerUsedIn;
    private static MediaPlayer mediaPlayer;
    private static List<BandProfileItem> items;
    private static Track currentTrack;
    private static int currentTrackIndex;
    private static int lastTrackIndex;
    private static Uri uri;
    private static Context context;
    private static TracksAdapter adapterThis;
    private static Band currentBand;
    private static int currentPosition;
    private static ProgressBar trackLoadingWheel;
    private static ImageButton pauseButton;
    private static LinearLayout progressBarHolder;
    private static SeekBar progressBar;
    private static Handler mHandler;
    private static Runnable seekBarRunnable;
    private static int direction = 0;
    private static boolean trackLoaded;
    private static TextView currentTime;
    private static TextView totalTime;
    private static Runnable onPlayStart;
    private static Mp3File mp3File;
    private static Connection connectionTester;

    public static void init(Context c, TracksAdapter a) {
        context = c;
        adapterThis = a;
        if (items == null) {
            items = new ArrayList<>();
        }
        lastTrackIndex = items.size() - 1;
        mp3File = new Mp3File(context);
        connectionTester = new Connection(context);
    }

    public static void init(Context c) {
        playerUsedIn = USED_IN_BANDS_LIST;
        context = c;
        PlayerAnimations.init(context);
        playerUsedIn = USED_IN_BANDS_LIST;
    }

    public static void uiInit(ProgressBar loading, ImageButton pause, LinearLayout progressHolder, SeekBar progress, TextView current, TextView total) {
        playerUsedIn = USED_IN_BAND_PROFILE;
        trackLoadingWheel = loading;
        pauseButton = pause;
        progressBarHolder = progressHolder;
        progressBar = progress;
        currentTime = current;
        totalTime = total;
        if (isPlaying() || isPaused()) { // player - case when returning to profile of actually played band
            mHandler.removeCallbacks(seekBarRunnable);
            attachSeekBar();
            runSeekbar();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void createPlayer() {
        trackLoaded = false;
        mHandler = new Handler();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play(next());
                View w = PlayerAnimations.getBackCurrentView(currentTrack);
                if (w != null) {
                    ProgressBar loading = w.findViewById(R.id.trackLoading);
                    PlayerAnimations.showLoading(true, loading);
                } else {
                    PlayerAnimations.showLoading(true, trackLoadingWheel);
                }
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                trackLoaded = true;
                PlayerHelper.updatePlayState(items, currentTrack);
                adapterThis.notifyDataSetChanged();
                mediaPlayer.start();
                onPlayStart.run();
                PlayerAnimations.showLoading(false, trackLoadingWheel);
                if (!(playerUsedIn == USED_IN_BAND_PROFILE)) {
                    PlayerAnimations.showSeekBar(true, progressBarHolder);
                }
                PlayerAnimations.showPauseButton(true, pauseButton);
                runSeekbar();
            }
        });

        attachSeekBar();
    }

    private static void attachSeekBar() {
        mHandler = new Handler();
        seekBarRunnable = new Runnable() {
            @Override
            public void run() {
                if(Player.getCurrentTrack() != null) {
                    int mCurrentPosition = Player.getCurrentPosition();
                    progressBar.setProgress(mCurrentPosition);
                    currentTime.setText(PlayerHelper.milisecondsToHuman(mCurrentPosition));
                }
                mHandler.postDelayed(this, SEEKBAR_REFRESH_RATE);
            }
        };
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Player.rewindTo(seekBar.getProgress());
            }
        });
    }

    private static void runSeekbar() {
        if (currentTrack.isAvailableOffline()) {
            progressBar.setMax(Player.getDuration());
            totalTime.setText(PlayerHelper.milisecondsToHuman(mediaPlayer.getDuration()));
        } else {
            progressBar.setMax((int) currentTrack.getDurationMilisecs());
            totalTime.setText(currentTrack.getDuration());
        }
        ((Activity) context).runOnUiThread(seekBarRunnable);
    }

    private static void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                PlayerAnimations.showPauseButton(false, pauseButton);
                if (!(playerUsedIn == USED_IN_BAND_PROFILE)) {
                    PlayerAnimations.showSeekBar(false, progressBarHolder);
                }
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
                mHandler.removeCallbacks(seekBarRunnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setTracklist(List<BandProfileItem> list) {
        items = list;
        currentBand = PlayerHelper.getBandFromList(items);
    }

    public static int next() {
        direction = 1;
        return (currentTrackIndex < lastTrackIndex) ? (currentTrackIndex + direction) : (0);
    }

    public static int prev() {
        direction = -1;
        return (currentTrackIndex == 0) ? (lastTrackIndex) : (currentTrackIndex + direction);
    }

    public static void pause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                currentPosition = mediaPlayer.getCurrentPosition();
            }
        }
    }

    public static void toggle() {
        if (mediaPlayer != null) {
            if (pauseState() == 1) {
                play();
            } else {
                pause();
            }
        }
    }

    public static void play() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
        }
    }

    public static void stop() {
        if (mediaPlayer != null) {
            //mediaPlayer.stop();
            killMediaPlayer();
        }
    }

    public static int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public static int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public static void rewindTo(int progress) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(progress);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void play(int order) {
        currentTrackIndex = order;
        lastTrackIndex = items.size() - 1; // because on construction length is 0
        connectionTester.getConnectionMethod();
        boolean connected = connectionTester.isConnectionAvailable();
        if (items.get(currentTrackIndex).getClass() != Track.class) {
            switchTrack();
        }
        if (!connected && !items.get(currentTrackIndex).isAvailableOffline()) {
            switchTrack();
        }
        currentTrack = (Track) items.get(currentTrackIndex);
        uri = Uri.parse(currentTrack.getLocalOrHref());
        if (mediaPlayer == null) {
            createPlayer();
        } else {
            killMediaPlayer();
            createPlayer();
        }
    }

    private static void switchTrack() {
        if (direction >= 0) {
            play(next());
        } else {
            play(prev());
        }
    }

    public static void showPlayerIfPlaying(List<BandProfileItem> list) {
        if (isPlaying() || isPaused()) {
            if ((currentBand != null) && (list != null)) {
                if (PlayerHelper.isBandInList(list, (Band) currentBand)) {
                    if (currentTrack != null) {
                        connectionTester.getConnectionMethod();
                        if (connectionTester.isConnectionAvailable() || currentTrack.isAvailableOffline()) {
                            int pos = PlayerHelper.posOfTrackInList(list, (Track) currentTrack);
                            list.set(pos, (BandProfileItem) currentTrack); // mozno prekastovat ??
                        }
                    }
                } else {
                    // pridat v buducnosti aj do cudzej kapely ako prve ??
                    // mozno v upravenom vzhlade, s moznostou vratis sa na kapelu ?
                }
            }
        }
    }

    public static Track getCurrentTrack() {
        return currentTrack;
    }

    public static Band getCurrentBand() {
        return currentBand;
    }

    public static boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        } else {
            return mediaPlayer.isPlaying();
        }
    }

    public static boolean isPaused() {
        return (pauseState() >= 0);
    }

    public static void setOnPlayStart(Runnable runnable) {
        onPlayStart = runnable;
    }

    public static int pauseState() {
        // 1 - pausing
        // 0 - playing or stopped
        // -1 - not playing or loading
        if (mediaPlayer != null) {
            if (trackLoaded) {
                return (!mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() > 1) ? 1 : 0;
            } else {
                return -1;
            }
        }
        return -1;
    }

}
