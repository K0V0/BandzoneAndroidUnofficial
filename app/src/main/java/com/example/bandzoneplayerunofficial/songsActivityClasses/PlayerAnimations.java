package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import com.example.bandzoneplayerunofficial.helpers.Misc;
import com.example.bandzoneplayerunofficial.helpers.SlideAnimation;
import com.example.bandzoneplayerunofficial.objects.Track;

public class PlayerAnimations {
    private static Context context;

    public static void init(Context c) {
        context = c;
    }

    public static void animate(ImageButton pauseHolder, SeekBar progressHolder, Track track) {
        if (track.isPlaying()) {
            showSeekBar(true, progressHolder);
            showPauseButton(true, pauseHolder);
        } else {
            showSeekBar(false, progressHolder);
            showPauseButton(false, pauseHolder);
        }
    }

    public static void showLoading(boolean start, ProgressBar b) {
        if (start) {
            b.setVisibility(View.VISIBLE);
        } else {
            b.setVisibility(View.INVISIBLE);
        }
    }

    public static void showPauseButton(boolean start, ImageButton b) {
        if (start) {
            b.setVisibility(View.VISIBLE);
            b.animate().alpha(1.0f).setDuration(250);
        } else {
            b.animate().alpha(0.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    b.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        }
    }

    public static void showSeekBar(boolean start, SeekBar s) {
        if (start) {
            if (s.getHeight() == 0) {
                SlideAnimation.slideView(s, 0, Misc.getPixels(context, 24));
            }
        } else {
            if (s.getHeight() > 0) {
                SlideAnimation.slideView(s, Misc.getPixels(context, 24),0);
            }
        }
    }

}
