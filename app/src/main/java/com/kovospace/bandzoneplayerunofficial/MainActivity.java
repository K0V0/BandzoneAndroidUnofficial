package com.kovospace.bandzoneplayerunofficial;

import androidx.appcompat.app.AppCompatActivity;
import com.droidnet.DroidListener;

public class MainActivity extends AppCompatActivity implements DroidListener {

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {

        } else {

        }
    }
}
