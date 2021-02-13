package com.kovospace.bandzoneplayerunofficial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.kovospace.bandzoneplayerunofficial.eventBus.ReloadBandsList;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.BandsSearch;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.PlayerWidget;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BandsActivity extends Activity {
    private EditText bandSearchField;
    private BandsSearch bandsSearch;
    private PlayerWidget playerWidget;

    @Override
    protected void onNetworkChanged() {
        if (connectionTest.isConnectionChanged()) {
            refreshActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bandSearchField = findViewById(R.id.bandInput);
        bandsSearch = new BandsSearch(BandsActivity.this, this);
        playerWidget = new PlayerWidget(this);

        bandSearchField.addTextChangedListener(
                bandsSearch.watchText()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connectionTest.isConnectionChanged()) {
            refreshActivity();
        }
        playerWidget.check();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onUserInteraction() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ReloadBandsList reload) {
        if (reload.isReload()) {
            refreshActivity();
        }
        EventBus.getDefault().removeStickyEvent(reload);
    }

    private void refreshActivity() {
        Intent intent = new Intent(this, BandsActivity.class);
        startActivity(intent);
        finish();
    }
}