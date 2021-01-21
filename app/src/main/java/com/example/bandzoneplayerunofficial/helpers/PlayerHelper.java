package com.example.bandzoneplayerunofficial.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;
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
        RecyclerView songsList = ((Activity) context).findViewById(R.id.songsList);
        View child;
        //Track track = (Track) v.findViewById(R.id.trackName).getTag();
        //int id = track.getOrder();
        for (int i = 0; i < songsList.getChildCount(); i++) {
            child = songsList.getChildAt(i);
            LinearLayout seekBarHolder = child.findViewById(R.id.seekbarHolder); // OK
            LinearLayout pauseButtonHolder = child.findViewById(R.id.pauseButtonHolder); // OK
            //SlideAnimation.slideView(seekBarHolder, seekBarHolder.getLayoutParams().height, 0);
            //pauseButtonHolder.animate().alpha(0.5f).setDuration(200);

            //Track track2 = (Track) child.findViewById(R.id.trackName).getTag(); // tu spadne
            //System.out.println(child);
            //int id2 = track2.getOrder();
            //if (id != id2) {
                //LinearLayout seekBarHolder = child.findViewById(R.id.seekbarHolder);
                //LinearLayout pauseButtonHolder = child.findViewById(R.id.pauseButtonHolder);
                //SlideAnimation.slideView(seekBarHolder, seekBarHolder.getLayoutParams().height, 0);
               // pauseButtonHolder.animate().alpha(0.0f).setDuration(200);
           // }
        }
    }

}
