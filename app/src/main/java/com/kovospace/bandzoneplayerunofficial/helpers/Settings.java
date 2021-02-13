package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {
    protected static Context context;
    protected static SharedPreferences prefs;
    protected static Editor editor;

    public static void init(Context c) {
        context = c;
        prefs = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // status by conditions (wifi/data on/off on phone)
    /*public static void setConnectionStatus(boolean status) {
        editor.putBoolean("connectionStatus", status);
        editor.commit();
    }

    public static boolean getConnectionStatus() {
        return prefs.getBoolean("connectionStatus", false);
    }*/

    // allowance by user in app
    public static void setAllowConnection(boolean status) {
        editor.putBoolean("allowConnection", status);
        editor.commit();
    }

    public static boolean getAllowConnection() {
        return prefs.getBoolean("allowConnection", true);
    }
}
