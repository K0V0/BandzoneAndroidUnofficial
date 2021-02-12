package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.databases.BandEntity;
import com.kovospace.bandzoneplayerunofficial.databases.BandsDbHelper;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Page;

import java.util.ArrayList;
import java.util.List;

public class BandsWrapperOffline extends BandsWrapper {
    private final int ITEMS_PER_PAGE = 100;
    private Page page;
    private List<Band> bandsList;
    private List<BandEntity> bandEntities;

    @Override
    public int setDataSourceType() {
        return DATA_SOURCE_LOCAL;
    }

    public BandsWrapperOffline(Activity activity, Context context) {
        super(activity, context);
        this.bandsList = new ArrayList<>();
        this.bandEntities = new ArrayList<>();
    }

    private void populateData() {
        BandEntity bandEntity;
        for (int i = 0; i < bandEntities.size(); i++) {
            bandEntity = bandEntities.get(i);
            bandsList.add(new Band(
                    bandEntity.getTitle(),
                    bandEntity.getCity(),
                    bandEntity.getImage_url(),
                    bandEntity.getHref(),
                    bandEntity.getSlug(),
                    bandEntity.getGenre()
            ));
            bandsList.get(i).setImageFullLocalPath(bandEntity.getImageFullLocalPath());
        }
    }

    private void applyData() {
        page = new Page(
                1,
                bandsList.size(),
                bandsList.size(),
                1,
                bandsList.size(),
                bandsList
        );
        System.out.println(page);
        update(page);
    }

    @Override
    protected void performSearch(String s) {
        this.bandEntities = BandsDbHelper.findByName(s);
        populateData();
        applyData();
    }

    @Override
    protected void loadNextContent() {}

}
