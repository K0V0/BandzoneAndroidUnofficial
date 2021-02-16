package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.kovospace.bandzoneplayerunofficial.Activity;
import com.kovospace.bandzoneplayerunofficial.R;

public class PopupMenu implements View.OnClickListener, android.widget.PopupMenu.OnMenuItemClickListener {
    private Context context;
    private ImageButton networkStatusButton;

    public PopupMenu(Context context) {
        this.context = context;
        this.networkStatusButton = ((Activity) context).findViewById(R.id.networkStatusButton);
    }

    @Override
    public void onClick(View v) {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(context, networkStatusButton);
        popup.getMenuInflater()
                .inflate(R.menu.popup_main_setings, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        System.out.println("------------- klikaj kurwaaaa");
        System.out.println(item.getItemId());
        return true;
    }
}
