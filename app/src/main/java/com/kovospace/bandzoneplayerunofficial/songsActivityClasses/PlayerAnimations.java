package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bandzoneplayerunofficial.R;
import com.example.bandzoneplayerunofficial.helpers.Misc;
import com.example.bandzoneplayerunofficial.helpers.SlideAnimation;
import com.example.bandzoneplayerunofficial.objects.Track;

public class PlayerAnimations {
    private static Context context;

    public static void init(Context c) {
        context = c;
    }

    public static void animate(ImageButton pauseHolder, SeekBar progressHolder, ProgressBar loadingHolder, Track track) {
        if (track.isPlaying()) {
            showSeekBar(true, progressHolder);
            showPauseButton(true, pauseHolder);
            showLoading(false, loadingHolder);
        } else {
            showSeekBar(false, progressHolder);
            showPauseButton(false, pauseHolder);
            showLoading(false, loadingHolder);
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
            b.animate().alpha(1.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) { b.setVisibility(View.VISIBLE); }
                @Override
                public void onAnimationEnd(Animator animation) {}
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
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

    public static View getBackCurrentView(Track currentTrack) {
        RecyclerView rv = (RecyclerView) ((Activity) context).findViewById(R.id.songsList);
        TextView tv;
        Track t;
        for (int i = 0; i < rv.getChildCount(); i++) {
            tv = rv.getChildAt(i).findViewById(R.id.trackName);
            if (tv != null) {
                t = (Track) tv.getTag();
                if (t.getHref_hash().equals(currentTrack.getHref_hash())) {
                    return rv.getChildAt(i);
                    //PlayerAnimations.showLoading(true, rv.getChildAt(i).findViewById(R.id.trackLoading));
                    //PlayerAnimations.showSeekBar(true, rv.getChildAt(i).findViewById(R.id.seekBar));
                }
            }
        }
        return null;
    }

}
