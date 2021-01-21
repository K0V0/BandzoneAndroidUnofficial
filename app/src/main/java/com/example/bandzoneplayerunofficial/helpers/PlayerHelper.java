package com.example.bandzoneplayerunofficial.helpers;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.example.bandzoneplayerunofficial.R;
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

    public void openAnimations(View v) {
        LinearLayout seekBarHolder = v.findViewById(R.id.seekbarHolder);
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (24 * scale + 0.5f);
        SlideAnimation.slideView(seekBarHolder, seekBarHolder.getLayoutParams().height, pixels);
        LinearLayout pauseButtonHolder = v.findViewById(R.id.pauseButtonHolder);
        pauseButtonHolder.animate().alpha(1.0f).setDuration(250);
    }

    public void closeAnimations(View v) {
        
    }

}
