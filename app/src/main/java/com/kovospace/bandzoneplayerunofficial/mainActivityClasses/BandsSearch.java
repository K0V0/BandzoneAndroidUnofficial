package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.MainActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.OnFinishTypingHelper;
import com.kovospace.bandzoneplayerunofficial.helpers.SearchFieldProgress;
import com.kovospace.bandzoneplayerunofficial.helpers.TestConnection;

public class BandsSearch extends OnFinishTypingHelper {

    private TestConnection testConnection;
    private BandsWrapper bandsWrapper;
    private Context context;
    private MainActivity mainActivity;
    protected SearchFieldProgress searchFieldProgress;

    public BandsSearch(MainActivity mainActivity, Context context) {
        super();
        this.mainActivity = mainActivity;
        this.context = context;
        this.searchFieldProgress = new SearchFieldProgress(this.context);
        this.testConnection = new TestConnection(this.context);
        search("");
    }

    private void decideWrapperOnConnection() {
        if (testConnection.isActive()) {
            this.bandsWrapper = new BandsWrapperNet(mainActivity, context);
        } else {
            this.bandsWrapper = new BandsWrapperOffline(mainActivity, context);
        }
    }

    private void search(String search) {
        searchFieldProgress.start();
        decideWrapperOnConnection();
        bandsWrapper.search(search);
    }

    @Override
    public void doStuffNotOften() {
        if (getText().length() > 0) {
            search(getText());
        }
    }

    @Override
    public void doStuffOnZero() {
        search("");
    }
}
