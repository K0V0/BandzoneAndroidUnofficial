package com.kovospace.bandzoneplayerunofficial;

import android.app.Application;
import com.droidnet.DroidNet;
import com.kovospace.bandzoneplayerunofficial.databases.DbHelper;

public class Main extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DroidNet.init(this);
        DbHelper.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }
}
