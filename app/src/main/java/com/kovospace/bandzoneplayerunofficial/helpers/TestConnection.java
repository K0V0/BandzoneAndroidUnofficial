package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class TestConnection {
    private Context context;
    private boolean isConnected;

    public TestConnection(Context context) {
        this.context = context;
    }

    // controls only if is wifi or 3g turned on, dorobiť v budúcnosti
    public boolean isActive() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return isConnected;
    }

}