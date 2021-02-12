package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.BandsActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.OnFinishTypingHelper;
import com.kovospace.bandzoneplayerunofficial.helpers.SearchFieldProgress;
import com.kovospace.bandzoneplayerunofficial.helpers.TestConnection;

public class BandsSearch extends OnFinishTypingHelper {

    private TestConnection testConnection;
    private BandsWrapper bandsWrapper;
    private Context context;
    private BandsActivity bandsActivity;

    public BandsSearch(BandsActivity bandsActivity, Context context) {
        super();
        this.bandsActivity = bandsActivity;
        this.context = context;
        this.testConnection = new TestConnection(this.context);
        SearchFieldProgress.init(this.context);
        search("");
    }

    private void decideWrapperOnConnection() {
        if (testConnection.isActive()) {
            this.bandsWrapper = new BandsWrapperNet(bandsActivity, context);
        } else {
            this.bandsWrapper = new BandsWrapperOffline(bandsActivity, context);
        }
    }

    private void search(String search) {
        SearchFieldProgress.start();
        decideWrapperOnConnection();
        // ^ dat do nejakeho eventu po zmene siete, debilne riesenie toto, blbnu toast messages
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
