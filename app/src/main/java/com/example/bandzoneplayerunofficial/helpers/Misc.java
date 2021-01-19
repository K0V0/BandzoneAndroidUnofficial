package com.example.bandzoneplayerunofficial.helpers;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Misc {
    private Context context;

    public Misc(Context context) {
        this.context = context;
    }

    public static int getVisibleItemCount(RecyclerView rv) {
        final int firstVisiblePos = getFirstVisiblePosition(rv);
        final int lastVisiblePos = getLastVisiblePosition(rv);
        return Math.max(0, lastVisiblePos - firstVisiblePos);
    }

    private static int getFirstVisiblePosition(RecyclerView rv) {
        if (rv != null) {
            final RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }
        }
        return 0;
    }

    private static int getLastVisiblePosition(RecyclerView rv) {
        if (rv != null) {
            final RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
        }
        return 0;
    }

}
