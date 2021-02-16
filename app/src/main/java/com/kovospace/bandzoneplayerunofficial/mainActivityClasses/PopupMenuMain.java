package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import com.kovospace.bandzoneplayerunofficial.Activity;
import com.kovospace.bandzoneplayerunofficial.BandsActivity;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.helpers.Settings;

public class PopupMenuMain implements View.OnClickListener {
    private Context context;
    private ImageButton networkStatusButton;
    private PopupMenu popupMenu;
    private Context wrapper;

    public PopupMenuMain(Context context) {
        this.context = context;
        this.networkStatusButton = ((Activity) context).findViewById(R.id.networkStatusButton);
        wrapper = new ContextThemeWrapper(context, R.style.MyPopupStyle);
        popupMenu = new android.widget.PopupMenu(wrapper, networkStatusButton);
        popupMenu.getMenuInflater()
                .inflate(R.menu.popup_main_setings, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenuListener());
    }

    @Override
    public void onClick(View v) {
        init(popupMenu);
        popupMenu.show();
    }

    private void init(PopupMenu popupMenu) {
        Menu menu = popupMenu.getMenu();
        menu.findItem(R.id.offlineMode).setChecked(!Settings.getAllowConnection());
    }

    public class PopupMenuListener implements android.widget.PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            item.setActionView(new View(context));
            item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return false;
                }
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    return false;
                }
            });
            switch(item.getItemId()) {
                case R.id.offlineMode:
                    switchNetwork(item);
                    break;
            }
            return false;
        }

        private void switchNetwork(MenuItem item) {
            // pozor, ked odskrtavam checkbox vrati hodnotu zaskrtnuteho a naopak
            item.setChecked(!item.isChecked());
            Settings.setAllowConnection(!item.isChecked());
            ((BandsActivity) context).refreshActivity();
        }
    }


}
