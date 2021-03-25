package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.databases.BandEntity;
import com.kovospace.bandzoneplayerunofficial.databases.BandsDbHelper;
import com.kovospace.bandzoneplayerunofficial.databases.TrackEntity;
import com.kovospace.bandzoneplayerunofficial.databases.TracksDbHelper;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;

import java.util.List;

public class BandWrapperOffline extends BandWrapper {
    private BandEntity bandEntity;
    private List<TrackEntity> trackEntities;

    @Override
    public int setDataSourceType() {
        return DATA_SOURCE_LOCAL;
    }

    public BandWrapperOffline(Activity activity, Context context, String extra) {
        super(activity, context, extra);
        populateData();
        applyData();
        triggerShow();
    }

    private void populateData() {
        bandEntity = BandsDbHelper.findFirstBySlug(extra);
        trackEntities = TracksDbHelper.getBandTracks(extra);
    }

    private void applyData() {
        band = new Band(
                bandEntity.getTitle(),
                bandEntity.getCity(),
                bandEntity.getImage_url(),
                bandEntity.getHref(),
                extra,
                bandEntity.getGenre(),
                "bandzone"
        );
        for (TrackEntity e : trackEntities) {
            tracks.add(new Track(
                    e.getFullTitle(),
                    e.getTitle(),
                    e.getAlbum(),
                    e.getPlays_count(),
                    e.getHref(),
                    e.getHrefHash(),
                    e.getDuration()
            ));
        }
    }
}
