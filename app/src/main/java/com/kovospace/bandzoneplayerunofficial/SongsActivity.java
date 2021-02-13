package com.kovospace.bandzoneplayerunofficial;

import android.os.Bundle;
import com.downloader.PRDownloader;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.BandWrapper;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.BandWrapperNet;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.BandWrapperOffline;

public class SongsActivity extends Activity {
    public String slug;
    private BandWrapper bandWrapper;

    @Override
    protected void onNetworkChanged() {
        loadSongs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        slug = getIntent().getStringExtra("slug");
        PRDownloader.initialize(getApplicationContext());
        loadSongs();
    }

    private void loadSongs() {
        if (connectionTest.isConnectionAvailable()) {
            bandWrapper = new BandWrapperNet(SongsActivity.this, this, slug);
        } else {
            bandWrapper = new BandWrapperOffline(SongsActivity.this, this, slug);
        }
    }
}