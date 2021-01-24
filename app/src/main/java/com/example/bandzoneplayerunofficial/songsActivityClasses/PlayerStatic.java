package com.example.bandzoneplayerunofficial.songsActivityClasses;

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
import com.example.bandzoneplayerunofficial.R;
import com.example.bandzoneplayerunofficial.helpers.PlayerHelper;
import com.example.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.example.bandzoneplayerunofficial.objects.Band;
import com.example.bandzoneplayerunofficial.objects.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerStatic {
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
    private static int currentTrackLength;
    private static ProgressBar trackLoadingWheel;
    private static ImageButton pauseButton;
    private static SeekBar progressBar;

    public static void init(Context c, List<BandProfileItem> i, TracksAdapter a) {
        PlayerStatic.init(c, a);
        items = i;
        currentBand = PlayerHelper.getBandFromList(i);
    }

    public static void init(Context c, TracksAdapter a) {
        context = c;
        adapterThis = a;
        System.out.println(items == null);
        if (items == null) {
            items = new ArrayList<>();
        }
        lastTrackIndex = items.size() - 1;
        //seekBar = ((Activity)context).findViewById(R.id.seekBar);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void createPlayer() {
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
    }

    public static void getSeekbarBack(Handler mHandler) {
        progressBar.setMax(PlayerStatic.getDuration());
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(PlayerStatic.getCurrentTrack() != null){
                    int mCurrentPosition = PlayerStatic.getCurrentPosition();
                    progressBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayerStatic.rewindTo(seekBar.getProgress());
            }
        });

    }

    public static void uiInit(ProgressBar trackProgress, ImageButton pause, SeekBar seekBar) {
        trackLoadingWheel = trackProgress;
        pauseButton = pause;
        progressBar = seekBar;
    }

    public static void setTracklist(List<BandProfileItem> list) {
        items = list;
        currentBand = PlayerHelper.getBandFromList(items);
    }

    public static int next() {
        return (currentTrackIndex < lastTrackIndex) ? (currentTrackIndex +1) : (0);
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
        //currentTrackLength =
        lastTrackIndex = items.size() - 1; // because on construction length is 0
        if (items.get(currentTrackIndex).getClass() != Track.class) {
            //currentTrackLength = items.get(currentTrackIndex).
                    play(next());
        }
        currentTrack = (Track) items.get(currentTrackIndex);
        uri = Uri.parse(currentTrack.getHref());
        //currentTrackLength = mediaPlayer.getDuration();
       // System.out.println(currentTrackLength);

        PlayerHelper.updatePlayState(items, currentTrack);
        adapterThis.notifyDataSetChanged();

        if (mediaPlayer == null) {
            //mediaPlayer = MediaPlayer.create((Activity) context, uri);
            createPlayer();
        } else {
            killMediaPlayer();
            createPlayer();
            //mediaPlayer = MediaPlayer.create((Activity) context, uri);
            //seekBar.setMax(mediaPlayer.getDuration());
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play(next());
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Handler mHandler = new Handler();
                mediaPlayer.start();
                trackLoadingWheel.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
                getSeekbarBack(mHandler);
            }
        });

        //mediaPlayer.start();
    }

    public static void showPlayerIfPlaying(List<BandProfileItem> list) {
        if (isPlaying()) {
            //System.out.println("---------- player is playing");
            if ((currentBand != null) && (list != null)) {
                //System.out.println("----------- list a band not nulll");
                //System.out.println(list);
                if (PlayerHelper.isBandInList(list, (Band) currentBand)) {
                    //System.out.println("------------- it is actual band");
                    if (currentTrack != null) {
                        int pos = PlayerHelper.posOfTrackInList(list, (Track) currentTrack);
                        //System.out.println(list.indexOf((BandProfileItem) currentTrack));
                        //System.out.println(PlayerHelper.posOfTrackInList(list, (Track) currentTrack));
                        list.set(pos, (BandProfileItem) currentTrack); // mozno prekastovat ??
                        //PlayerHelper.updatePlayState(list, currentTrack);
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

    public static Band getBandFromPlayer() {
        return currentBand;
    }

    public static boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        } else {
            return mediaPlayer.isPlaying();
        }
    }

    public static int pauseState() {
        if (mediaPlayer != null) {
            return (!mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() > 1) ? 1 : 0;
        }
        return -1;
    }

    private static void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
