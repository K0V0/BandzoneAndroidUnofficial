package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.databases.DbHelper;
import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.interfaces.DataWrapper;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;

import java.util.ArrayList;
import java.util.List;

public abstract class BandWrapper implements DataWrapper {
    protected Activity activity;
    protected Context context;
    protected String extra;

    protected Band band;
    protected List<Track> tracks;
    protected List<BandProfileItem> bandProfileItems;
    protected Mp3File mp3File;
    protected ImageFile imageFile;

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
        this.mp3File = new Mp3File(this.context);
        this.imageFile = new ImageFile(this.context);
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

    private List<Track> addExtraData(List<Track> trackList) {
        int i = 0;
        for (Track track : trackList) {
            track.setOrder(i);
            track.setBandName(band.getTitle());
            track.setBandSlug(band.getSlug());
            track.setTrackFullLocalPath(mp3File);
            track.hasOfflineCopy();
            track.convertDuration();
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
        // runs after data retrieved from wrapper (JSON or local)
        bandProfileItems.clear();
        band.setImageFullLocalPath(imageFile);
        band.hasOfflineCopy();
        bandProfileItems.add(band);
        bandProfileItems.addAll(addExtraData(tracks));
        DbHelper.rememberBandAndTracksForOffline(bandProfileItems);
        tracksAdapter.notifyItemInserted(bandProfileItems.size() - 1);
        noTracksMessage();
    }

}
