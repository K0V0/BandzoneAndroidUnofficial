package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class Player {
    private static MediaPlayer mediaPlayer;

    public static void play(final Context context, final String url) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
        } else {
            killMediaPlayer();
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                killMediaPlayer();
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
