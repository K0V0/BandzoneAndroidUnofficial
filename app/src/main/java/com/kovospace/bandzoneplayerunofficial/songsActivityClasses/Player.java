package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.*;
import androidx.annotation.OptIn;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.extractor.DefaultExtractorsFactory;
import androidx.media3.extractor.mp3.Mp3Extractor;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.helpers.Connection;
import com.kovospace.bandzoneplayerunofficial.helpers.PlayerHelper;
import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Playback runs on ExoPlayer rather than MediaPlayer.
 *
 * MediaPlayer cannot seek this catalogue: an mp3 that is VBR with no Xing header gives it no
 * time-to-byte map, so a seek stalled playback for many seconds. ExoPlayer's mp3 extractor is told
 * to build a seek index for exactly those files, which also gets the duration right.
 *
 * The static shape is deliberate - TracksAdapter, PlayerAnimations and PlayerWidget all reach in
 * here, so the surface is kept exactly as it was.
 */
@OptIn(markerClass = UnstableApi.class)
public class Player {
    private static final int SEEKBAR_REFRESH_RATE = 250;
    private static final int USED_IN_BAND_PROFILE = 1;
    private static final int USED_IN_BANDS_LIST = 2;

    private static int playerUsedIn;
    private static ExoPlayer exoPlayer;
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
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    private static Runnable seekBarRunnable;
    private static int direction = 0;
    private static boolean trackLoaded;
    private static TextView currentTime;
    private static TextView totalTime;
    private static Runnable onPlayStart;
    private static Mp3File mp3File;
    private static Connection connectionTester;
    // set while a finger is on the seekbar, so the ticker stops overwriting where the user put it
    private static boolean userSeeking;

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
            stopTicker();
            attachSeekBar();
            runSeekbar();
        }
    }

    private static void createPlayer() {
        trackLoaded = false;

        // FLAG_ENABLE_INDEX_SEEKING is the whole point of the migration: for an mp3 with no
        // Xing/VBRI header the extractor builds its own index instead of guessing a constant
        // bitrate, so seeking lands where asked and the duration comes out right.
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory()
                .setMp3ExtractorFlags(Mp3Extractor.FLAG_ENABLE_INDEX_SEEKING);

        exoPlayer = new ExoPlayer.Builder(context)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(context, extractorsFactory))
                .build();
        exoPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                        .setUsage(C.USAGE_MEDIA)
                        .build(),
                true // let ExoPlayer handle audio focus
        );

        exoPlayer.addListener(new androidx.media3.common.Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == androidx.media3.common.Player.STATE_READY && !trackLoaded) {
                    onTrackReady();
                } else if (playbackState == androidx.media3.common.Player.STATE_ENDED) {
                    onTrackEnded();
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                // Ticking only while something actually moves - the old code re-posted every 250ms
                // forever, which kept the main looper from ever going idle.
                if (isPlaying) {
                    startTicker();
                } else {
                    stopTicker();
                }
            }
        });

        exoPlayer.setMediaItem(MediaItem.fromUri(uri));
        exoPlayer.prepare();
        exoPlayer.play();

        attachSeekBar();
    }

    private static void onTrackReady() {
        trackLoaded = true;
        PlayerHelper.updatePlayState(items, currentTrack);
        if (adapterThis != null) {
            adapterThis.notifyDataSetChanged();
        }
        if (onPlayStart != null) {
            onPlayStart.run();
        }
        PlayerAnimations.showLoading(false, trackLoadingWheel);
        if (!(playerUsedIn == USED_IN_BAND_PROFILE)) {
            PlayerAnimations.showSeekBar(true, progressBarHolder);
        }
        PlayerAnimations.showPauseButton(true, pauseButton);
        runSeekbar();
    }

    private static void onTrackEnded() {
        play(next());
        View w = PlayerAnimations.getBackCurrentView(currentTrack);
        if (w != null) {
            ProgressBar loading = w.findViewById(R.id.trackLoading);
            PlayerAnimations.showLoading(true, loading);
        } else {
            PlayerAnimations.showLoading(true, trackLoadingWheel);
        }
    }

    private static void attachSeekBar() {
        seekBarRunnable = new Runnable() {
            @Override
            public void run() {
                if (Player.getCurrentTrack() != null && !userSeeking) {
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
                // follow the thumb while dragging, so the time is not stuck until the finger lifts
                if (fromUser && currentTime != null) {
                    currentTime.setText(PlayerHelper.milisecondsToHuman(progress));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userSeeking = true;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userSeeking = false;
                Player.rewindTo(seekBar.getProgress());
            }
        });
    }

    private static void startTicker() {
        if (seekBarRunnable == null) {
            return;
        }
        mHandler.removeCallbacks(seekBarRunnable);
        mHandler.post(seekBarRunnable);
    }

    private static void stopTicker() {
        if (seekBarRunnable != null) {
            mHandler.removeCallbacks(seekBarRunnable);
        }
    }

    private static void runSeekbar() {
        applyDuration(durationOf(currentTrack));
        startTicker();
        resolveExactDuration(currentTrack);
    }

    // The player's own duration is right far more often now that the extractor indexes headerless
    // files, but a duration already known - read off the downloaded file, or sent by the API -
    // is still preferred: it needs no scan and is available before playback is ready.
    private static int durationOf(Track track) {
        Long known = (track == null) ? null : track.getKnownDurationMs();
        return (known != null) ? known.intValue() : Player.getDuration();
    }

    private static void applyDuration(int duration) {
        progressBar.setMax(duration);
        totalTime.setText(PlayerHelper.milisecondsToHuman(duration));
    }

    // Reading the file is real I/O, so it stays off the main thread; the seekbar is corrected once
    // the answer is in, and only while the same track is still the one playing.
    private static void resolveExactDuration(final Track track) {
        if (track == null || track.getKnownDurationMs() != null || !track.isAvailableOffline()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Long exact = track.resolveLocalDurationMs();
                if (exact == null) {
                    return;
                }
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (track == currentTrack && progressBar != null) {
                            applyDuration(exact.intValue());
                        }
                    }
                });
            }
        }).start();
    }

    private static void killPlayer() {
        if (exoPlayer != null) {
            try {
                PlayerAnimations.showPauseButton(false, pauseButton);
                if (!(playerUsedIn == USED_IN_BAND_PROFILE)) {
                    PlayerAnimations.showSeekBar(false, progressBarHolder);
                }
                exoPlayer.stop();
                exoPlayer.release();
                exoPlayer = null;
                stopTicker();
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
        if (exoPlayer != null) {
            if (exoPlayer.isPlaying()) {
                exoPlayer.pause();
                currentPosition = getCurrentPosition();
            }
        }
    }

    public static void toggle() {
        if (exoPlayer != null) {
            if (pauseState() == 1) {
                play();
            } else {
                pause();
            }
        }
    }

    public static void play() {
        if (exoPlayer != null) {
            exoPlayer.seekTo(currentPosition);
            exoPlayer.play();
        }
    }

    public static void stop() {
        if (exoPlayer != null) {
            killPlayer();
        }
    }

    public static int getDuration() {
        if (exoPlayer != null) {
            long duration = exoPlayer.getDuration();
            return (duration == C.TIME_UNSET) ? 0 : (int) duration;
        }
        return 0;
    }

    public static int getCurrentPosition() {
        return (exoPlayer == null) ? 0 : (int) exoPlayer.getCurrentPosition();
    }

    public static void rewindTo(int progress) {
        if (exoPlayer != null) {
            exoPlayer.seekTo(progress);
        }
    }

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
        // a bare local path must not go through Uri.parse - a '#' in the track
        // title would be read as a fragment and cut the path short
        uri = currentTrack.isAvailableOffline()
                ? Uri.fromFile(new File(currentTrack.getTrackFullLocalPath()))
                : Uri.parse(currentTrack.getHref());
        if (exoPlayer == null) {
            createPlayer();
        } else {
            killPlayer();
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
        if (exoPlayer == null) {
            return false;
        } else {
            return exoPlayer.isPlaying();
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
        //
        // Asks playWhenReady, not isPlaying(). isPlaying() is derived and goes false whenever the
        // player dips into STATE_BUFFERING - right after a seek, for instance - so callers that
        // read the state straight after pause()/play() (PlayerWidget does) would see "paused" for
        // a track that is merely buffering and leave the icon out of sync. playWhenReady carries
        // the intent and flips synchronously, the way MediaPlayer's isPlaying() used to.
        if (exoPlayer != null) {
            if (trackLoaded) {
                return (!exoPlayer.getPlayWhenReady() && getCurrentPosition() > 1) ? 1 : 0;
            } else {
                return -1;
            }
        }
        return -1;
    }

}
