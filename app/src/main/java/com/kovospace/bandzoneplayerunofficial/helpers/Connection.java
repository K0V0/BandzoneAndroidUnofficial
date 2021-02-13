package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class Connection {
    public static final int CONNECTION_OFFLINE = 1;
    public static final int CONNECTION_ONLINE = 2;
    private Context context;
    private int currentConnection;
    private int newConnection;
    private boolean connectionChanged;

    public Connection(Context context) {
        this.context = context;
        this.currentConnection = 0;
        this.newConnection = 0;
    }

    private Boolean getStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }


    public int getConnectionMethod() {
        if (getStatus() && Settings.getAllowConnection()) {
            newConnection = CONNECTION_ONLINE;
        } else {
            newConnection = CONNECTION_OFFLINE;
        }
        connectionChanged = newConnection != currentConnection;
        currentConnection = newConnection;
        return newConnection;
    }

    public boolean isConnectionChanged() {
        return connectionChanged;
    }

    public boolean isConnectionAvailable() {
        return currentConnection == CONNECTION_ONLINE;
    }

}