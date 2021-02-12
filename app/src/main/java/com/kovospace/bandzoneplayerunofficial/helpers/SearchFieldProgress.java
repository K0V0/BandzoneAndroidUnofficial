package com.kovospace.bandzoneplayerunofficial.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.kovospace.bandzoneplayerunofficial.R;

public class SearchFieldProgress {
    private static Context context;
    private static FrameLayout spinWheelContainer;
    private static ProgressBar spinWheel;

    public SearchFieldProgress(Context context) {
        this.context = context;
        init(context);
    }

    public static void init(Context c) {
        context = c;
        spinWheelContainer = ((Activity) context).findViewById(R.id.placeForSearchStatusIcon);
        spinWheel = spinWheelContainer.findViewById(R.id.loadingSearch);
    }

    public static void start() {
        spinWheel.setVisibility(View.VISIBLE);
    }

    public static void stop() {
        spinWheel.setVisibility(View.INVISIBLE);
    }

}
