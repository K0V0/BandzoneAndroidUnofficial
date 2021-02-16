package com.kovospace.bandzoneplayerunofficial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.BandsSearch;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.PlayerWidget;


public class BandsActivity extends Activity {
    private EditText bandSearchField;
    private ImageButton networkStatusButton;
    private BandsSearch bandsSearch;
    private PlayerWidget playerWidget;

    @Override
    protected void onNetworkChanged() {
        if (connectionTest.isConnectionChanged()) {
            refreshActivity();
            updateNetworkStatusIcon();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkStatusButton = findViewById(R.id.networkStatusButton);
        updateNetworkStatusIcon();

        bandSearchField = findViewById(R.id.bandInput);
        bandsSearch = new BandsSearch(BandsActivity.this, this);
        bandSearchField.addTextChangedListener(
                bandsSearch.watchText()
        );

        playerWidget = new PlayerWidget(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connectionTest.isConnectionChanged()) {
            refreshActivity();
            updateNetworkStatusIcon();
        }
        bandsSearch.onResumeChecks();
        playerWidget.check();
    }

    @Override
    public void onUserInteraction() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void refreshActivity() {
        Intent intent = new Intent(this, BandsActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateNetworkStatusIcon() {
        if (connectionTest.isConnectionAvailable()) {
            networkStatusButton.setImageResource(R.mipmap.net_ok_foreground);
        } else {
            networkStatusButton.setImageResource(R.mipmap.no_net_foreground);
        }
    }

}