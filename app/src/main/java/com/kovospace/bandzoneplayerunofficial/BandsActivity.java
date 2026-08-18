package com.kovospace.bandzoneplayerunofficial;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.recyclerview.widget.RecyclerView;
import com.kovospace.bandzoneplayerunofficial.helpers.SearchFieldProgress;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.BandsSearch;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.PlayerWidget;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.PopupMenuMain;

import java.util.Optional;


public class BandsActivity extends Activity {
    private EditText bandSearchField;
    private ImageButton networkStatusButton;
    private BandsSearch bandsSearch;
    private PlayerWidget playerWidget;
    private Optional<String> searchString;

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
        askForBatteryOptimization();

        bandSearchField = findViewById(R.id.bandInput);
        searchString = Optional.ofNullable(getIntent().getStringExtra("searchString"));
        SearchFieldProgress.init(this);
        networkStatusButton = findViewById(R.id.networkStatusButton);
        updateNetworkStatusIcon();
        networkStatusButton.setOnClickListener(
                new PopupMenuMain(this)
        );

        bandsSearch = new BandsSearch(BandsActivity.this, this);
        bandSearchField.addTextChangedListener(
                bandsSearch.watchText()
        );
        searchString.ifPresent(ss -> {
            bandsSearch.pauseTextListenerOnce();
            bandSearchField.setText(ss);
            bandsSearch.search(ss);
        });

        hideKeyboardOnListScroll();

        playerWidget = new PlayerWidget(this);
    }

    // Keyboard used to be torn down from onUserInteraction(), which fires for
    // every dispatched key event - and IMEs send KEYCODE_DEL while deleting,
    // so wiping out the search field killed the keyboard halfway through and
    // the leftover text got searched. Dismiss on scrolling the results instead.
    private void hideKeyboardOnListScroll() {
        RecyclerView bandsList = findViewById(R.id.bandsList);
        bandsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard();
                }
            }
        });
    }

    private void hideKeyboard() {
        View focused = getCurrentFocus();
        if (focused != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
            focused.clearFocus();
        }
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

    public void refreshActivity() {
        Intent intent = new Intent(this, BandsActivity.class);
        String extra = bandSearchField.getText().toString();
        if (extra.length() > 0) {
            intent.putExtra("searchString", extra);
        }
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

    private void askForBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }

}