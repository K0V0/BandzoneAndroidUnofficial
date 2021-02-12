package com.kovospace.bandzoneplayerunofficial;

import android.os.Bundle;
import com.downloader.PRDownloader;
import com.kovospace.bandzoneplayerunofficial.helpers.TestConnection;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.BandWrapper;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.BandWrapperNet;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.BandWrapperOffline;

public class SongsActivity extends MainActivity {
    public String slug;
    private BandWrapper bandWrapper;
    private TestConnection testConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        slug = getIntent().getStringExtra("slug");

        PRDownloader.initialize(getApplicationContext());

        testConnection = new TestConnection(this);

        if (testConnection.isActive()) {
            bandWrapper = new BandWrapperNet(SongsActivity.this, this, slug);
        } else {
            bandWrapper = new BandWrapperOffline(SongsActivity.this, this, slug);
        }
    }
}