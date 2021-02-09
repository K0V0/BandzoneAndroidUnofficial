package com.kovospace.bandzoneplayerunofficial;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.downloader.PRDownloader;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.BandsSearch;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.PlayerWidget;

public class MainActivity extends AppCompatActivity {
    private EditText bandSearchField;
    private BandsSearch bandsSearch;
    private PlayerWidget playerWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bandSearchField = findViewById(R.id.bandInput);
        bandsSearch = new BandsSearch(MainActivity.this, this);
        playerWidget = new PlayerWidget(this);

        bandSearchField.addTextChangedListener(
                bandsSearch.watchText()
        );

        PRDownloader.initialize(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerWidget.check();
    }

    @Override
    public void onUserInteraction() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}