package com.demo.telenorhealthassignment.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static SharedPreferences preferences;

    private PreferencesManager() {
    }

    public static void init(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
    }

    public static boolean putString(String key, String value) {
        return preferences.edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        return preferences.getString(key, Constants.DEFAULT_ACCESS_TOKEN);
    }

    public static boolean removeString(String key) {
        return preferences.edit().remove(key).commit();
    }
}
