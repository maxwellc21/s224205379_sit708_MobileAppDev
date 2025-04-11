// utils/SharedPrefManager.java
package com.quizapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREF_NAME = "quizAppPrefs";
    private static final String KEY_NAME = "username";

    public static void saveName(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_NAME, name).apply();
    }

    public static String getName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_NAME, "");
    }
}
