package com.example.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bandzoneplayerunofficial.R;
import com.example.bandzoneplayerunofficial.helpers.ToastMessage;
import com.example.bandzoneplayerunofficial.objects.Band;
import com.example.bandzoneplayerunofficial.objects.Page;

import java.util.ArrayList;
import java.util.List;

public abstract class BandsWrapper implements BandsWrapperInterface {
    public static final int DATA_SOURCE_INTERNET = 1;
    public static final int DATA_SOURCE_LOCAL = 2;

    protected Context context;
    protected Activity activity;
    protected RecyclerView bandsRecyclerView;
    protected RecyclerView.Adapter bandsAdapter;
    protected RecyclerView.LayoutManager bandsLayoutManager;
    protected ToastMessage toastMessage;

    protected int currentPage;
    protected int itemsPerPage;
    protected int itemsOnCurrentPage;
    protected int pages;
    protected int itemsTotal;
    protected List<Band> bands = new ArrayList<>();

    protected String searchString;
    protected int dataSourceType;
    protected int nextPageToLoad;
    protected boolean preventMultipleLoads;

    public BandsWrapper() {}

    public BandsWrapper(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.bandsRecyclerView = this.activity.findViewById(R.id.bandsList);
        this.bandsLayoutManager = new LinearLayoutManager(this.activity);
        this.bandsRecyclerView.setLayoutManager(bandsLayoutManager);
        this.bandsRecyclerView.setHasFixedSize(true);
        this.bandsAdapter = new BandsAdapter(this.context, bands);
        this.bandsRecyclerView.setAdapter(bandsAdapter);
        this.toastMessage = new ToastMessage(context);
        this.dataSourceType = setDataSourceType();
        afterConstruction();
    }

    private void calculateNextPage() {
        if (currentPage + 1 <= pages) {
            nextPageToLoad = currentPage + 1;
        } else {
            nextPageToLoad = 0;
        }
    }

    private List<Band> selectiveAdd(List<Band> current, List<Band> neew) {
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
        return result;
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
        this.bands.clear();
        this.bands.addAll(selectiveAdd(this.bands, bands));
        this.bandsAdapter.notifyDataSetChanged();
    }

    private void addBands(List<Band> bands) {
        this.bands.addAll(bands);
        this.bandsAdapter.notifyItemInserted(this.bands.size() - 1);
    }

    public void update(Page page) {
        updateData(page);
        updateBands(page.getBands());
    }

    public void add(Page page) {
        updateData(page);
        addBands(page.getBands());
    }

    public void search(String searchString) {
        this.searchString = searchString;
    }

    public void loadNextContent() {}

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
                            loadNextContent();
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
