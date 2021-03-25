package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.databases.BandEntity;
import com.kovospace.bandzoneplayerunofficial.databases.BandsDbHelper;
import com.kovospace.bandzoneplayerunofficial.helpers.Misc;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Page;

import java.util.ArrayList;
import java.util.List;

public class BandsWrapperOffline extends BandsWrapper {
    private int dbResultsCount;
    private Page page;
    private List<Band> bandsList;
    private List<BandEntity> bandEntities;

    @Override
    public int setDataSourceType() {
        return DATA_SOURCE_LOCAL;
    }

    public BandsWrapperOffline() {
        init();
    }

    public BandsWrapperOffline(Activity activity, Context context) {
        super(activity, context);
        init();
    }

    private void init(){
        this.bandsList = new ArrayList<>();
        this.bandEntities = new ArrayList<>();
    }

    private void populateData() {
        BandEntity bandEntity;
        bandsList.clear();

        for (int i = 0; i < bandEntities.size(); i++) {
            bandEntity = bandEntities.get(i);
            bandsList.add(new Band(
                    bandEntity.getTitle(),
                    bandEntity.getCity(),
                    bandEntity.getImage_url(),
                    bandEntity.getHref(),
                    bandEntity.getSlug(),
                    bandEntity.getGenre(),
                    "bandzone"
            ));
            bandsList.get(i).setImageFullLocalPath(bandEntity.getImageFullLocalPath());
            bandsList.get(i).setFromDb(true);
        }
    }

    private void applyData(int pageNum) {
        page = new Page(
                pageNum,
                ITEMS_PER_PAGE,
                bandsList.size(),
                Misc.calculatePagesCount(dbResultsCount, ITEMS_PER_PAGE),
                dbResultsCount,
                bandsList
        );
    }

    @Override
    protected void performSearch(String s) {
        performNonModifyingSearch(s);
        handle(page);
    }

    @Override
    protected void loadNextContent() {
        performNonModifyingSearch(searchString, nextPageToLoad);
        handle(page);
    }

    public Page performNonModifyingSearch(String s) {
        this.dbResultsCount = BandsDbHelper.getCount(s);
        return performNonModifyingSearch(s, 1);
    }

    public Page performNonModifyingSearch(String s, int pageNum) {
        this.bandEntities = BandsDbHelper.findByName(s, ITEMS_PER_PAGE, ITEMS_PER_PAGE*(pageNum-1));
        populateData();
        applyData(pageNum);
        return page;
    }

    public int getDbResultsCount() {
        return dbResultsCount;
    }
}
