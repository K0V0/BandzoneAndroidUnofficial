package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;

public class Misc {

    public static int getPixels(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int calculatePagesCount(int count, int perPage) {
        int result = count / perPage;
        if (count % perPage > 0) {
            return result + 1;
        }
        return result;
    }
}
