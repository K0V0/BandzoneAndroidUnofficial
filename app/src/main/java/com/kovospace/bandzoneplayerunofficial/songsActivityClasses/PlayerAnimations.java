package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.helpers.Misc;
import com.kovospace.bandzoneplayerunofficial.helpers.SlideAnimation;
import com.kovospace.bandzoneplayerunofficial.objects.Track;

public class PlayerAnimations {
    private static Context context;
    private static final int PROGRESSBAR_HOLDER_HEIGHT = 40;

    public static void init(Context c) {
        context = c;
    }

    public static void animate(ImageButton pauseHolder, LinearLayout progressHolder, ProgressBar loadingHolder, Track track) {
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
                public void onAnimationStart(Animator animation) {
                    ViewGroup buttonContainer = (ViewGroup) b.getParent();
                    ImageView savedIcon = buttonContainer.findViewById(R.id.trackSaved);
                    if (savedIcon != null) {
                        savedIcon.setVisibility(View.INVISIBLE);
                    }
                    b.setVisibility(View.VISIBLE);
                }
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

    public static void showSeekBar(boolean start, LinearLayout s) {
        if (start) {
            if (s.getHeight() == 0) {
                SlideAnimation.slideView(s, 0, Misc.getPixels(context, 92));
            }
        } else {
            if (s.getHeight() > 0) {
                SlideAnimation.slideView(s, Misc.getPixels(context, 92),0);
            }
        }
    }

    public static View getBackCurrentView(Track currentTrack) {
        RecyclerView rv = (RecyclerView) ((Activity) context).findViewById(R.id.songsList);
        TextView tv;
        Track t;
        if (rv != null) {
            for (int i = 0; i < rv.getChildCount(); i++) {
                tv = rv.getChildAt(i).findViewById(R.id.trackName);
                if (tv != null) {
                    t = (Track) tv.getTag();
                    if (t.getHrefHash().equals(currentTrack.getHrefHash())) {
                        return rv.getChildAt(i);
                    }
                }
            }
        }
        return null;
    }

    public static void downloadComplete(ImageButton downloadButton, ProgressBar downloadLoading) {
        downloadLoading.animate().alpha(0.0f).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                downloadButton.setImageResource(R.mipmap.remove_foreground);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

}
