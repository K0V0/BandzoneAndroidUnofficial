package com.kovospace.bandzoneplayerunofficial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.Connection;

public abstract class Activity extends AppCompatActivity {
    protected Connection connectionTest;

    protected abstract void onNetworkChanged();

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            connectionTest.getConnectionMethod();
            onNetworkChanged();
        }
    };

    /*@Override
    protected void onStart() {
        super.onStart();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionTest = new Connection(this);
        connectionTest.getConnectionMethod();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionTest.getConnectionMethod();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    /*@Override
    protected void onStop() {
        super.onStop();
    }*/

}
