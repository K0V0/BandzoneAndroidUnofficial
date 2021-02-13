package com.kovospace.bandzoneplayerunofficial;

import android.app.Application;
import com.kovospace.bandzoneplayerunofficial.databases.DbHelper;
import com.kovospace.bandzoneplayerunofficial.helpers.Settings;

public class Main extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Settings.init(this);
        DbHelper.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
