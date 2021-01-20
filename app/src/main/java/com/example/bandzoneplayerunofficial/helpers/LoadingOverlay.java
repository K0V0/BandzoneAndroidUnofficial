package com.example.bandzoneplayerunofficial.helpers;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class LoadingOverlay {
    private Context context;
    private Activity activity;
    private Intent intent;

    public LoadingOverlay() {}

    public LoadingOverlay(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.intent = new Intent(this.context, LoadingOverlay.OverlayService.class);
    }

    public class OverlayService extends Service {
        /**
         * Service to overlay a translucent red View
         * @author Hathibelagal
         */

        LinearLayout oView;

        @Override
        public IBinder onBind(Intent i) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            oView = new LinearLayout(context);
            oView.setBackgroundColor(0x88ff0000); // The translucent red color
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                    0 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.addView(oView, params);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if(oView!=null){
                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                wm.removeView(oView);
            }
        }
    }

    public void toggleService() {
        if(!activity.stopService(intent)){
            activity.startService(intent);
        }
    }

    public void startService() {
        activity.startService(intent);
    }

    public void stopService() {
        activity.stopService(intent);
    }

}
