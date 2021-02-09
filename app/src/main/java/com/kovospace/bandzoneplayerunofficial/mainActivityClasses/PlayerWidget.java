package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.SongsActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.Misc;
import com.kovospace.bandzoneplayerunofficial.helpers.SlideAnimation;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.Player;

public class PlayerWidget {
    private String slug;
    private int hiddenHeight;

    private Context context;
    private LinearLayout playerHolder;
    private LinearLayout playerWidgetTexts;
    private LinearLayout playerWidgetSeekbarHolder;
    private ImageButton pausePlay;
    private TextView trackName;
    private TextView albumName;
    private TextView bandName;
    private LinearLayout progressBarHolder;
    private SeekBar progressBar;
    private ImageButton prevTrack;
    private ImageButton nextTrack;
    private ImageButton stopTrack;
    private ImageButton minimize;
    private ImageButton openBand;
    private ProgressBar loadingWheel;
    private TextView currentTime;
    private TextView totalTime;

    public PlayerWidget(Context context) {
        this.context = context;
        this.playerHolder = ((Activity) context).findViewById(R.id.playerWidgetHolder);
        this.playerWidgetTexts = playerHolder.findViewById(R.id.playerWidgetTexts);
        this.playerWidgetSeekbarHolder = playerHolder.findViewById(R.id.playerWidgetSeekbarHolder);
        this.pausePlay = playerHolder.findViewById(R.id.playerWidgetPause);
        this.trackName = playerHolder.findViewById(R.id.playerWidgetTrackName);
        this.albumName = playerHolder.findViewById(R.id.playerWidgetAlbumName);
        this.bandName = playerHolder.findViewById(R.id.playerWidgetBandName);
        this.progressBarHolder = playerHolder.findViewById(R.id.playerWidgetSeekbarHolder);
        this.progressBar = progressBarHolder.findViewById(R.id.playerWidgetSeekbar);
        this.currentTime = progressBarHolder.findViewById(R.id.playerWidgetTime);
        this.totalTime = progressBarHolder.findViewById(R.id.playerWidgetTimeTotal);
        this.prevTrack = playerHolder.findViewById(R.id.widgetPlayerPrevTrack);
        this.nextTrack = playerHolder.findViewById(R.id.widgetPlayerNextTrack);
        this.stopTrack = playerHolder.findViewById(R.id.widgetPlayerStopTrack);
        this.minimize = playerHolder.findViewById(R.id.widgetPlayerMinimize);
        this.openBand = playerHolder.findViewById(R.id.widgetPlayerOpenBand);
        this.loadingWheel = playerHolder.findViewById(R.id.playerWidgetLoading);
        this.hiddenHeight = Misc.getPixels(this.context, 120);
        check();
        attachActions();
    }

    public void check() {
        if (Player.isPlaying() || Player.isPaused()) {
            playerHolder.setVisibility(View.VISIBLE);
            playerHolder.setAlpha(1.0f);
            initPlayerClass();
            update();
        } else {
            playerHolder.setVisibility(View.GONE);
        }
    }

    private void update() {
        Track track = Player.getCurrentTrack();
        Band band = Player.getCurrentBand();
        trackName.setText(track.getTitle());
        albumName.setText(track.getAlbum());
        bandName.setText(band.getTitle());
        slug = band.getSlug();
        updatePauseButton();
    }

    private void initPlayerClass() {
        Player.init(this.context);
        Player.uiInit(loadingWheel, pausePlay, progressBarHolder, progressBar, currentTime, totalTime);
    }

    private void updatePauseButton() {
        if (Player.pauseState() == 0) {
            pausePlay.setImageResource(R.mipmap.pause);
        } else if (Player.pauseState() == 1) {
            pausePlay.setImageResource(R.mipmap.play);
        } else {
            loadingWheel.setVisibility(View.VISIBLE);
        }
    }

    private void openBandProfile() {
        if (slug != null) {
            Intent myIntent = new Intent(context, SongsActivity.class);
            myIntent.putExtra("slug", slug);
            context.startActivity(myIntent);
        }
    }

    private void unfoldPlayer() {
        if (playerWidgetSeekbarHolder.getHeight() == 0) {
            SlideAnimation.slideView(playerWidgetSeekbarHolder, 0, hiddenHeight);
        } else {
            SlideAnimation.slideView(playerWidgetSeekbarHolder, hiddenHeight, 0);
        }
    }

    private void attachActions() {
        Player.setOnPlayStart(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Player.isPlaying()) {
                    Player.pause();
                } else if (Player.isPaused()) {
                    Player.play();
                }
                updatePauseButton();
            }
        });
        playerWidgetTexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfoldPlayer();
            }
        });
        openBand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBandProfile();
            }
        });
        prevTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.play(Player.prev());
                updatePauseButton();
            }
        });
        nextTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.play(Player.next());
                updatePauseButton();
            }
        });
        stopTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.stop();
                playerHolder.animate().alpha(0.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        playerHolder.setVisibility(View.GONE);
                        playerWidgetSeekbarHolder.getLayoutParams().height = 0;
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
            }
        });
        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfoldPlayer();
            }
        });
    }

}
