package com.example.bandzoneplayerunofficial;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bandzoneplayerunofficial.helpers.TestConnection;
import com.example.bandzoneplayerunofficial.songsActivityClasses.BandWrapper;
import com.example.bandzoneplayerunofficial.songsActivityClasses.BandWrapperNet;
import com.example.bandzoneplayerunofficial.songsActivityClasses.BandWrapperOffline;

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
            bandWrapper = new BandWrapperOffline();
        }

    }
}