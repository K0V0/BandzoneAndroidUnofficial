package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.bandzoneplayerunofficial.R;
import com.example.bandzoneplayerunofficial.helpers.LoadingOverlay;
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

    protected TextView bandName;
    protected TextView bandGenreAndCity;
    protected ImageView bandImage;
    protected LoadingOverlay loadingOverlay;

    //protected boolean imageLoaded;

    public BandWrapper() {}

    public BandWrapper(Activity activity, Context context) {
        this(activity, context, "");
    }

    public BandWrapper(Activity activity, Context context, String extra) {
        this.activity = activity;
        this.context = context;
        this.extra = extra;
        this.bandName = activity.findViewById(R.id.bandName);
        this.bandGenreAndCity = activity.findViewById(R.id.bandGenreAndCity);
        this.bandImage = activity.findViewById(R.id.bandImage);
        this.loadingOverlay = new LoadingOverlay(this.context, this.activity);
    }

    public void triggerShow() {
        System.out.println(band);
        System.out.println(tracks);
        renderBandInfo();
    }

    private void renderBandInfo() {
        //this.imageLoaded = false;
        bandName.setText(band.getTitle());
        bandGenreAndCity.setText(band.getGenre());
        loadingOverlay.startService();

        Glide   .with(this.activity)
                .load(band.getImage_url())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //imageLoaded = true;
                        //loadingOverlay.stopService();
                        System.out.println("imageLoaded");
                        return false;
                    }
                })
                .into(bandImage);
        System.out.println("loaded");
    }

    private void renderTracks() {

    }
}
