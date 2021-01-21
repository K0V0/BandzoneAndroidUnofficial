package com.example.bandzoneplayerunofficial.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.example.bandzoneplayerunofficial.R;

public class SearchFieldProgress {
    private Context context;
    private LayoutInflater li;
    private RelativeLayout spinWheel;
    private LinearLayout searchFieldContainer;

    public SearchFieldProgress(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        spinWheel = (RelativeLayout) li.inflate(R.layout.search_progress, null);
        searchFieldContainer = (LinearLayout) ((Activity)context).findViewById(R.id.placeForWheel);
    }

    private boolean occupied() {
        return searchFieldContainer.getChildCount() > 0;
    }

    public void start() {
        System.out.println(occupied());
        if (!occupied()) {
            searchFieldContainer.addView(spinWheel);
        }
    }

    public void stop() {
        if (occupied()) {
            searchFieldContainer.removeView(
                    searchFieldContainer.findViewById(R.id.searchLoading)
            );
        }
    }

}
