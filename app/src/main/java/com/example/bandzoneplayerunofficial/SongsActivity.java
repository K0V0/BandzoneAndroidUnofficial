package com.example.bandzoneplayerunofficial;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bandzoneplayerunofficial.songsActivityClasses.LoadBand;

public class SongsActivity extends AppCompatActivity {
    private String slug;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        intent = getIntent();
        slug = intent.getStringExtra("slug");

        LoadBand loadBand = new LoadBand(SongsActivity.this,this);
        loadBand.setQuery(slug);
        System.out.println(slug);
        loadBand.load();
    }
}