package com.kovospace.bandzoneplayerunofficial;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.BandsSearch;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText bandSearchField = findViewById(R.id.bandInput);
        BandsSearch bandsSearch = new BandsSearch(MainActivity.this, this);

        bandSearchField.addTextChangedListener(
                bandsSearch.watchText()
        );
    }

    @Override
    public void onUserInteraction() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}