package com.example.nobuya.getmetar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nobuya on 2014/09/04.
 */
public class Settings {
    // keys
    public final static String K_DEVELOP_MODE = "develop_mode";
    public final static String K_DEBUG_MODE = "debug_mode";
    public final static String K_DEFAULT_CCCC = "default_cccc";
    // values
    public final static String V_RJTT = "rjtt";

    private static SharedPreferences sharedPrefs = null;

    public static void init(Context context) {
        if (sharedPrefs == null) {
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static Boolean getDevelopMode() {
        return sharedPrefs.getBoolean(K_DEVELOP_MODE, true);
    }
    public static Boolean getDebugMode() {
        return sharedPrefs.getBoolean(K_DEBUG_MODE, true);
    }
    public static String getDefaultCCCC() {
        return sharedPrefs.getString(K_DEFAULT_CCCC, V_RJTT);
    }
}
