package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import com.example.bandzoneplayerunofficial.helpers.PlayerHelper;
import com.example.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.example.bandzoneplayerunofficial.objects.Track;
import java.util.List;

public class Player {
    private static MediaPlayer mediaPlayer;
    public static List<Track> trackList;
    private static int currentTrack;
    private static int lastTrack;
    private static Uri uri;
    private static Context context;

    public static void init(Context c, List<BandProfileItem> tracks) {
        context = c;
        trackList = PlayerHelper.purifyPlaylist(tracks);
        lastTrack = trackList.size() - 1;
    }

    public static int next() {
        return (currentTrack < lastTrack) ? (currentTrack+1) : (0);
    }

    public static void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void toggle() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    public static void play(int order) {
        currentTrack = order;
        uri = Uri.parse(trackList.get(currentTrack).getHref());

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create((Activity) context, uri);
        } else {
            killMediaPlayer();
            mediaPlayer = MediaPlayer.create((Activity) context, uri);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play(next());
            }
        });
        mediaPlayer.start();
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
