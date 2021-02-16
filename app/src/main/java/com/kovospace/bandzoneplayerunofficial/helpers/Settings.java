package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.kovospace.bandzoneplayerunofficial.objects.Band;

public class Settings {
    protected static Context context;
    protected static SharedPreferences prefs;
    protected static Editor editor;

    public static void init(Context c) {
        context = c;
        prefs = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // allowance by user in app
    public static void setAllowConnection(boolean status) {
        editor.putBoolean("allowConnection", status);
        editor.commit();
    }

    public static boolean getAllowConnection() {
        return prefs.getBoolean("allowConnection", true);
    }

    public static void sendBandDowloadsRemoved(Band band) {
        editor.putString("bandWithDownloadsRemoved", band.getSlug());
        editor.commit();
    }

    public static String getBandsDownloadsRemoved() {
        return prefs.getString("bandWithDownloadsRemoved", "");
    }

    public static void removeBandDowloadsRemoved() {
        editor.putString("bandWithDownloadsRemoved", "");
        editor.commit();
    }

    public static void triggerBandTrackDowloaded(Band band) {
        editor.putString("bandTriggersDownload", band.getSlug());
        editor.commit();
    }

    public static String getBandThatTriggeredDownload() {
        return prefs.getString("bandTriggersDownload", "");
    }

    public static void removeBandTrackDownloadTrigger() {
        editor.putString("bandTriggersDownload", "");
        editor.commit();
    }

}
