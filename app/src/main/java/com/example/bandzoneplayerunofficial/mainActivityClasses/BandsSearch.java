package com.example.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.example.bandzoneplayerunofficial.MainActivity;
import com.example.bandzoneplayerunofficial.helpers.*;

public class BandsSearch extends OnFinishTypingHelper {

    private TestConnection testConnection;
    private BandsWrapper bandsWrapper;
    private String searchText;
    private Context context;
    private Activity mainActivity;

    public BandsSearch(MainActivity mainActivity, Context context) {
        super();
        this.mainActivity = mainActivity;
        this.context = context;
        this.testConnection = new TestConnection(this.context);
        if (testConnection.isActive()) {
            this.bandsWrapper = new BandsWrapperNet(mainActivity, context);
        } else {
            this.bandsWrapper = new BandsWrapperOffline();
        }
        bandsWrapper.search("");
    }

    @Override
    public void doStuffNotOften() {
        searchText = getText();
        if (searchText.length() == 0) {
            bandsWrapper.clear();
        } else {
            bandsWrapper.search(searchText);
        }
    }

    @Override
    public void doStuffOnZero() {
        bandsWrapper.clear();
    }
}
