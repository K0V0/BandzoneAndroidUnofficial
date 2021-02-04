package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import androidx.annotation.RequiresApi;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.helpers.PlayerHelper;
import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player {
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
    private static SeekBar progressBar;
    private static Handler mHandler;
    private static Runnable seekBarRunnable;
    private static int direction = 0;
    private static boolean trackLoaded;
    private static boolean fromBandsList;

    public static void init(Context c, TracksAdapter a) {
        context = c;
        adapterThis = a;
        if (items == null) {
            items = new ArrayList<>();
        }
        lastTrackIndex = items.size() - 1;
    }

    public static void init(Context c) {
        // used by playerwidget in bands activitiy
        context = c;
        PlayerAnimations.init(context);
        fromBandsList = true;
    }

    public static void uiInit(ProgressBar trackProgress, ImageButton pause, SeekBar seekBar) {
        trackLoadingWheel = trackProgress;
        pauseButton = pause;
        progressBar = seekBar;
        if (isPlaying()) { // player - case when returning to profile of actually played band
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
                PlayerAnimations.showLoading(false, trackLoadingWheel);
                PlayerAnimations.showSeekBar(true, progressBar);
                PlayerAnimations.showPauseButton(true, pauseButton);
                runSeekbar();
            }
        });

        attachSeekBar();
    }

    private static void attachSeekBar() {
        System.out.println("----- attach seekbar");
        mHandler = new Handler();
        seekBarRunnable = new Runnable() {
            @Override
            public void run() {
                if(Player.getCurrentTrack() != null) {
                    int mCurrentPosition = Player.getCurrentPosition();
                    progressBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
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
        System.out.println("----- rund seekbar");
        progressBar.setMax(Player.getDuration());
        ((Activity) context).runOnUiThread(seekBarRunnable);
    }

    private static void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                PlayerAnimations.showPauseButton(false, pauseButton);
                PlayerAnimations.showSeekBar(false, progressBar);
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
            mediaPlayer.stop();
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
        if (items.get(currentTrackIndex).getClass() != Track.class) {
            if (direction >= 0) {
                play(next());
            } else {
                play(prev());
            }
        }
        currentTrack = (Track) items.get(currentTrackIndex);
        uri = Uri.parse(currentTrack.getHref());
        if (mediaPlayer == null) {
            createPlayer();
        } else {
            killMediaPlayer();
            createPlayer();
        }
    }

    public static void showPlayerIfPlaying(List<BandProfileItem> list) {
        if (isPlaying()) {
            if ((currentBand != null) && (list != null)) {
                if (PlayerHelper.isBandInList(list, (Band) currentBand)) {
                    if (currentTrack != null) {
                        int pos = PlayerHelper.posOfTrackInList(list, (Track) currentTrack);
                        list.set(pos, (BandProfileItem) currentTrack); // mozno prekastovat ??
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
