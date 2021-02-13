package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.BandsActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.Connection;
import com.kovospace.bandzoneplayerunofficial.helpers.OnFinishTypingHelper;
import com.kovospace.bandzoneplayerunofficial.helpers.SearchFieldProgress;

public class BandsSearch extends OnFinishTypingHelper {

    private Connection conectionTester;
    private BandsWrapper bandsWrapper;
    private Context context;
    private BandsActivity bandsActivity;

    public BandsSearch(BandsActivity bandsActivity, Context context) {
        super();
        this.bandsActivity = bandsActivity;
        this.context = context;
        this.conectionTester = new Connection(this.context);
        SearchFieldProgress.init(this.context);
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

    private void search(String search) {
        SearchFieldProgress.start();
        decideWrapperOnConnection();
        // ^ dat do nejakeho eventu po zmene siete, debilne riesenie toto, blbnu toast messages
        // OK RESOLVED
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
