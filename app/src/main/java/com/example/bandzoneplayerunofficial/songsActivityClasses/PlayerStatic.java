package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import com.example.bandzoneplayerunofficial.helpers.PlayerHelper;
import com.example.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.example.bandzoneplayerunofficial.objects.Track;

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

    public static void init(Context c, List<BandProfileItem> i, TracksAdapter a) {
        PlayerStatic.init(c, a);
        items = i;
    }

    public static void init(Context c, TracksAdapter a) {
        context = c;
        adapterThis = a;
        System.out.println(items == null);
        if (items == null) {
            items = new ArrayList<>();
        }
        lastTrackIndex = items.size() - 1;
    }

    public static void setTracklist(List<BandProfileItem> list) {
        items = list;
    }

    public static int next() {
        return (currentTrackIndex < lastTrackIndex) ? (currentTrackIndex +1) : (0);
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
        currentTrackIndex = order;
        lastTrackIndex = items.size() - 1; // because on construction length is 0
        if (items.get(currentTrackIndex).getClass() != Track.class) {
            play(next());
        }
        currentTrack = (Track) items.get(currentTrackIndex);
        uri = Uri.parse(currentTrack.getHref());

        PlayerHelper.updatePlayState(items, currentTrack);
        adapterThis.notifyDataSetChanged();

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

    public Track getCurrentTrack() {
        return currentTrack;
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
