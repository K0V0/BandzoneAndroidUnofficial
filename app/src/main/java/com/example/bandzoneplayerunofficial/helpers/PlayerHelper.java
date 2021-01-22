package com.example.bandzoneplayerunofficial.helpers;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.example.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.example.bandzoneplayerunofficial.objects.Track;
import java.util.ArrayList;
import java.util.List;

public class PlayerHelper {
    private Context context;

    public PlayerHelper() {}

    public PlayerHelper(Context context) {
        this.context = context;
    }

    public static List<Track> purifyPlaylist(List<BandProfileItem> trackList) {
        List<Track> result = new ArrayList<>();
        for (BandProfileItem item : trackList) {
            if (item.getClass() == Track.class) {
                result.add((Track) item);
            }
        }
        return result;
    }

    public static void updatePlayState(List<BandProfileItem> itemsList, Track currentTrack) {
        for (int i = 0; i < itemsList.size(); i++) {
            BandProfileItem item = itemsList.get(i);
            if (item.getClass() == Track.class) {
                if (((Track) item).getOrder() == currentTrack.getOrder()) {
                    ((Track) item).setPlaying(true);
                } else {
                    ((Track) item).setPlaying(false);
                }
            }
        }
    }

    public static void updateUIanimation(Context context, LinearLayout pauseHolder, SeekBar progressHolder, Track track) {
        if (track.isPlaying()) {
            pauseHolder.animate().alpha(1.0f).setDuration(250);
            if (progressHolder.getHeight() == 0) {
                SlideAnimation.slideView(
                        progressHolder,
                        0,
                        Misc.getPixels(context, 24)
                );
            }
        } else {
            pauseHolder.animate().alpha(0.0f).setDuration(250);
            if (progressHolder.getHeight() > 0) {
                SlideAnimation.slideView(
                        progressHolder,
                        Misc.getPixels(context, 24),
                        0
                );
            }
        }
    }

}
