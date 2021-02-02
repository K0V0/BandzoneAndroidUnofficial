package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

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
    private SeekBar seekBar;
    private ImageButton prevTrack;
    private ImageButton nextTrack;
    private ImageButton stopTrack;
    private ImageButton minimize;
    private ImageButton openBand;

    public PlayerWidget(Context context) {
        this.context = context;
        this.playerHolder = ((Activity) context).findViewById(R.id.playerWidgetHolder);
        this.playerWidgetTexts = playerHolder.findViewById(R.id.playerWidgetTexts);
        this.playerWidgetSeekbarHolder = playerHolder.findViewById(R.id.playerWidgetSeekbarHolder);
        this.pausePlay = playerHolder.findViewById(R.id.playerWidgetPause);
        this.trackName = playerHolder.findViewById(R.id.playerWidgetTrackName);
        this.albumName = playerHolder.findViewById(R.id.playerWidgetAlbumName);
        this.bandName = playerHolder.findViewById(R.id.playerWidgetBandName);
        this.seekBar = playerHolder.findViewById(R.id.playerWidgetSeekbar);
        this.prevTrack = playerHolder.findViewById(R.id.widgetPlayerPrevTrack);
        this.nextTrack = playerHolder.findViewById(R.id.widgetPlayerNextTrack);
        this.stopTrack = playerHolder.findViewById(R.id.widgetPlayerStopTrack);
        this.minimize = playerHolder.findViewById(R.id.widgetPlayerMinimize);
        this.openBand = playerHolder.findViewById(R.id.widgetPlayerOpenBand);
        this.hiddenHeight = Misc.getPixels(this.context, 120);
        check();
        attachActions();
    }

    public void check() {
        if (Player.isPlaying() || Player.isPaused()) {
            playerHolder.setVisibility(View.VISIBLE);
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

    private void initPlayer() {
        Player.uiInit(ProgressBar trackProgress, pausePlay, SeekBar seekBar);
    }

    private void updatePauseButton() {
        if (Player.isPlaying()) {
            pausePlay.setImageResource(R.mipmap.pause);
        } else if (Player.isPaused()) {
            pausePlay.setImageResource(R.mipmap.play);
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
                update();
            }
        });
        nextTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.play(Player.next());
                update();
            }
        });
        stopTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.stop();
            }
        });
        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
