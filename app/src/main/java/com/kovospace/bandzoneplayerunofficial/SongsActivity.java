package com.kovospace.bandzoneplayerunofficial;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.TestConnection;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.BandWrapper;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.BandWrapperNet;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.BandWrapperOffline;

public class SongsActivity extends AppCompatActivity {
    public String slug;
    private BandWrapper bandWrapper;
    private TestConnection testConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        slug = getIntent().getStringExtra("slug");
        testConnection = new TestConnection(this);

        if (testConnection.isActive()) {
            bandWrapper = new BandWrapperNet(SongsActivity.this, this, slug);
        } else {
            bandWrapper = new BandWrapperOffline(SongsActivity.this, this, slug);
        }
    }
}