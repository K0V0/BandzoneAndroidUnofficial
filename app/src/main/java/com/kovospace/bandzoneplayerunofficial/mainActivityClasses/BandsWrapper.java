package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.helpers.Settings;
import com.kovospace.bandzoneplayerunofficial.helpers.ToastMessage;
import com.kovospace.bandzoneplayerunofficial.interfaces.DataWrapper;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Page;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.ImageFile;

import java.util.ArrayList;
import java.util.List;

public abstract class BandsWrapper implements DataWrapper {
    protected Context context;
    protected Activity activity;
    protected ImageFile imageFile;
    protected RecyclerView bandsRecyclerView;
    protected RecyclerView.Adapter bandsAdapter;
    protected RecyclerView.LayoutManager bandsLayoutManager;
    protected ToastMessage toastMessage;
    protected TextView noBandsText;

    protected int currentPage;
    protected int itemsPerPage;
    protected int itemsOnCurrentPage;
    protected int pages;
    protected int itemsTotal;
    protected int currentOperation;
    protected List<Band> bands = new ArrayList<>();

    protected String searchString;
    protected int dataSourceType;
    protected int nextPageToLoad;
    protected boolean preventMultipleLoads;

    public BandsWrapper() {}

    public BandsWrapper(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.imageFile = new ImageFile(this.context);
        this.bandsRecyclerView = this.activity.findViewById(R.id.bandsList);
        this.bandsLayoutManager = new LinearLayoutManager(this.activity);
        this.bandsRecyclerView.setLayoutManager(bandsLayoutManager);
        this.bandsRecyclerView.setHasFixedSize(true);
        this.bandsAdapter = new BandsAdapter(this.context, bands);
        this.bandsRecyclerView.setAdapter(bandsAdapter);
        this.toastMessage = new ToastMessage(context);
        this.noBandsText = activity.findViewById(R.id.noBandsText);
        this.dataSourceType = setDataSourceType();
        afterConstruction();
    }

    protected abstract void performSearch(String s);
    protected abstract void loadNextContent();

    private void calculateNextPage() {
        if (currentPage + 1 <= pages) {
            nextPageToLoad = currentPage + 1;
        } else {
            nextPageToLoad = 0;
        }
    }

    private void selectiveAdd(List<Band> current, List<Band> neew) {
        List<Band> result = new ArrayList<>();
        for (Band neewBand : neew) {
            if (!current.contains(neewBand)) {
                result.add(neewBand);
            }
        }
        this.bands.addAll(result);
    }

    private void selectiveSort(List<Band> current, List<Band> neew) {
        List<Band> result = new ArrayList<>();
        for (Band neewBand : neew) {
            if (!current.contains(neewBand)) {
                result.add(neewBand);
            }
        }
        for (Band ooldBand : current) {
            if (neew.contains(ooldBand)) {
                result.add(ooldBand);
            }
        }
        this.bands.clear();
        this.bands.addAll(result);
    }

    private void updateData(Page page) {
        this.currentPage = page.getCurrentPage();
        this.itemsPerPage = page.getItemsPerPage();
        this.itemsOnCurrentPage = page.getItemsOnCurrentPage();
        this.pages = page.getPages();
        this.itemsTotal = page.getItemsTotal();
        calculateNextPage();
        this.preventMultipleLoads = false;
        if (dataSourceType == DATA_SOURCE_INTERNET) {
            removeLoadingDialog();
        }
    }

    private void updateBands(List<Band> bands) {
        selectiveSort(this.bands, bands);
        this.bandsAdapter.notifyDataSetChanged();
        checkIfNotEmpty();
    }

    private void addBands(List<Band> bands) {
        selectiveAdd(this.bands, bands);
        //this.bandsAdapter.notifyItemInserted(this.bands.size() - 1);
        this.bandsAdapter.notifyDataSetChanged(); // islo ok notify inset blblo
        checkIfNotEmpty();
    }

    private void loadNext() {
        currentOperation = OPERATION_NEXTPAGE;
        loadNextContent();
    }

    private void checkIfNotEmpty() {
        if (this.bands.size() <= 0) {
           noBandsText.animate().alpha(1.0F).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                   noBandsText.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationEnd(Animator animation) {}
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        } else {
            activity.findViewById(R.id.noBandsText).setVisibility(View.GONE);
        }
    }

    private List<Band> insertLocalImagePath(List<Band> bands) {
        for (int i = 0; i < bands.size(); i++) {
            Band band = bands.get(i);
            band.setImageFullLocalPath(imageFile);
            band.hasOfflineCopy();
        }
        return bands;
    }

    public void update(Page page) {
        updateData(page);
        updateBands(
                insertLocalImagePath(page.getBands())
        );
    }

    public void add(Page page) {
        updateData(page);
        addBands(
                insertLocalImagePath(page.getBands())
        );
    }

    public void onResumeChecks() {
        String bandAllDownloadsRemoved = Settings.getBandsDownloadsRemoved();
        String bandDownloadDone = Settings.getBandThatTriggeredDownload();
        String bandSlug;

        for (int i = 0; i < this.bands.size(); i++) {
            bandSlug = this.bands.get(i).getSlug();
            if (bandSlug.equals(bandAllDownloadsRemoved)) {
                this.bands.get(i).setFromDb(false);
                this.bandsAdapter.notifyItemChanged(i);
                Settings.removeBandDowloadsRemoved();
            }
            if (bandSlug.equals(bandDownloadDone)) {
                this.bands.get(i).setFromDb(true);
                this.bandsAdapter.notifyItemChanged(i);
                Settings.removeBandTrackDownloadTrigger();
            }
        }
    }

    public void handle(Page page) {
        switch(currentOperation) {
            case OPERATION_NEXTPAGE:
                add(page);
                break;
            case OPERATION_SEARCH:
                update(page);
                break;
        }
    }

    public void search (String searchString) {
        this.searchString = searchString;
        this.currentOperation = OPERATION_SEARCH;
        performSearch(this.searchString);
    }

    public void clear() {
        this.preventMultipleLoads = false;
        this.bands.clear();
        this.bandsAdapter.notifyDataSetChanged();
    }

    private void afterConstruction() {
        bandsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    if (nextPageToLoad != 0) {
                        if (!preventMultipleLoads) {
                            preventMultipleLoads = true;
                            loadNext();
                            if (dataSourceType == DATA_SOURCE_INTERNET) {
                                displayLoadingDialog();
                            }
                        }
                    } else {
                        if (currentPage == pages) {
                            toastMessage.send(R.string.noMoreBands);
                        }
                    }
                }
            }
        });
    }

    private void displayLoadingDialog() {
        this.bands.add(null);
        this.bandsAdapter.notifyItemInserted(this.bands.size() - 1);
    }

    private void removeLoadingDialog() {
        for (int i = 0; i < this.bands.size(); i++) {
            if (this.bands.get(i) == null) {
                this.bands.remove(i);
                this.bandsAdapter.notifyItemRemoved(i);
            }
        }
    }

}
