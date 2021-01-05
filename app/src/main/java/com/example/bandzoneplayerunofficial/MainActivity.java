package com.example.bandzoneplayerunofficial;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bandzoneplayerunofficial.mainActivityClasses.SearchBands;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText bandSearchField = (EditText) findViewById(R.id.bandInputEditText);
        SearchBands searchBands = new SearchBands(MainActivity.this, this);

        bandSearchField.addTextChangedListener(
                searchBands.watchText()
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