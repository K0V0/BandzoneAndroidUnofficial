package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.example.bandzoneplayerunofficial.interfaces.DataWrapper;
import com.example.bandzoneplayerunofficial.objects.Band;
import com.example.bandzoneplayerunofficial.objects.Track;

import java.util.List;

public abstract class BandWrapper implements DataWrapper {
    protected Activity activity;
    protected Context context;
    protected String extra;

    protected Band band;
    protected List<Track> tracks;

    public BandWrapper() {}

    public BandWrapper(Activity activity, Context context) {
        this(activity, context, "");
    }

    public BandWrapper(Activity activity, Context context, String extra) {
        this.activity = activity;
        this.context = context;
        this.extra = extra;
    }

    public void triggerShow() {
        System.out.println(band);
        System.out.println(tracks);
    }

    private void renderBandInfo() {

    }

    private void renderTracks() {

    }
}
