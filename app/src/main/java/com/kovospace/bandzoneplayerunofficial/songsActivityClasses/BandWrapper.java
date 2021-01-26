package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bandzoneplayerunofficial.R;
import com.example.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.example.bandzoneplayerunofficial.interfaces.DataWrapper;
import com.example.bandzoneplayerunofficial.objects.Band;
import com.example.bandzoneplayerunofficial.objects.Track;

import java.util.ArrayList;
import java.util.List;

public abstract class BandWrapper implements DataWrapper {
    protected Activity activity;
    protected Context context;
    protected String extra;

    protected Band band;
    protected List<Track> tracks;
    protected List<BandProfileItem> bandProfileItems;

    protected RecyclerView tracksRecyclerView;
    protected RecyclerView.Adapter tracksAdapter;
    protected RecyclerView.LayoutManager tracksLayoutManager;
    protected int dataSourceType;
    protected TextView noTracksText;

    public BandWrapper() {}

    public BandWrapper(Activity activity, Context context, String extra) {
        this.activity = activity;
        this.context = context;
        this.extra = extra;
        inits();
        loadStaticUI();
        loadTracksUI();
    }

    private void inits() {
        this.dataSourceType = setDataSourceType();
        this.tracks = new ArrayList<>();
        this.bandProfileItems = new ArrayList<>();
    }

    private void loadStaticUI() {
        this.noTracksText = activity.findViewById(R.id.noTracksText);
    }

    private void loadTracksUI() {
        this.tracksRecyclerView = this.activity.findViewById(R.id.songsList);
        this.tracksLayoutManager = new LinearLayoutManager(activity);
        this.tracksRecyclerView.setLayoutManager(tracksLayoutManager);
        this.tracksRecyclerView.setHasFixedSize(true);
        this.tracksAdapter = new TracksAdapter(this.context, bandProfileItems);
        this.tracksRecyclerView.setAdapter(tracksAdapter);
        this.dataSourceType = setDataSourceType();
    }

    private List<Track> addIndexes(List<Track> trackList) {
        int i = 0;
        for (Track track : trackList) {
            track.setOrder(i);
            i++;
        }
        return trackList;
    }

    private void noTracksMessage() {
        int trackCount = 0;
        for (BandProfileItem item : bandProfileItems) {
            if (item.getClass() == Track.class) {
                trackCount++;
            }
        }
        if (trackCount == 0) {
            noTracksText.setVisibility(View.VISIBLE);
        }
    }

    public void triggerShow() {
        bandProfileItems.clear();
        bandProfileItems.add(band);
        bandProfileItems.addAll(addIndexes(tracks));
        tracksAdapter.notifyItemInserted(bandProfileItems.size() - 1);
        noTracksMessage();
    }

}
