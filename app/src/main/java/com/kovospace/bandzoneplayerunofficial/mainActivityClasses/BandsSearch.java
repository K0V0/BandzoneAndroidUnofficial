package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.BandsActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.Connection;
import com.kovospace.bandzoneplayerunofficial.helpers.OnFinishTypingHelper;

public class BandsSearch extends OnFinishTypingHelper {

    private Connection conectionTester;
    private BandsWrapper bandsWrapper;
    private Context context;
    private BandsActivity bandsActivity;
    private boolean pauseSearch;

    public BandsSearch(BandsActivity bandsActivity, Context context) {
        super();
        this.bandsActivity = bandsActivity;
        this.context = context;
        this.conectionTester = new Connection(this.context);
        search("");
    }

    private void decideWrapperOnConnection() {
        conectionTester.getConnectionMethod();
        if (conectionTester.isConnectionChanged()) {
            if (conectionTester.isConnectionAvailable()) {
                this.bandsWrapper = new BandsWrapperNet(bandsActivity, context, conectionTester);
            } else {
                this.bandsWrapper = new BandsWrapperOffline(bandsActivity, context);
            }
        }
    }

    public void search(String search) {
        decideWrapperOnConnection();
        bandsWrapper.search(search);
    }

    public void pauseTextListenerOnce() {
        this.pauseSearch = true;
    }

    public void resumeTextListener() {
        this.pauseSearch = false;
    }

    @Override
    public void doStuffNotOften() {
        if (getText().length() > 0) {
            if (!pauseSearch) {
                search(getText());
            } else {
                pauseSearch = false;
            }
        }
    }

    @Override
    public void doStuffOnZero() {
        if (!pauseSearch) {
            search("");
        } else {
            pauseSearch = false;
        }
    }

    public void onResumeChecks() {
        bandsWrapper.onResumeChecks();
    }

}
