package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;

public class Misc {

    public static int getPixels(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (24 * scale + 0.5f);
    }
}
