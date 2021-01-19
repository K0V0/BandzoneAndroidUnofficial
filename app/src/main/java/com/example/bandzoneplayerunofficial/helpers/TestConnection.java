package com.example.bandzoneplayerunofficial.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestConnection {
    private Context context;
    private boolean isConnected;

    public TestConnection(Context context) {
        this.context = context;
    }

    // controls only if is wifi or 3g turned on
    public boolean isActive() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}