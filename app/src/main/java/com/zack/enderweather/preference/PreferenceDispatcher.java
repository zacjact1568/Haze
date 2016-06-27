package com.zack.enderweather.preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zack.enderweather.application.EnderWeatherApp;

public class PreferenceDispatcher {

    public static final String KEY_PREF_NEED_GUIDE = "need_guide";
    public static final String KEY_PREF_LOCATION_SERVICE = "location_service";
    public static final String KEY_PREF_LANGUAGE = "language";
    public static final String KEY_PREF_NIGHT_MODE = "night_mode";

    private SharedPreferences sharedPreferences;

    private static PreferenceDispatcher ourInstance = new PreferenceDispatcher();

    private PreferenceDispatcher() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EnderWeatherApp.getGlobalContext());
    }

    public static PreferenceDispatcher getInstance() {
        return ourInstance;
    }

    public boolean getBooleanPref(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    public String getStringPref(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setPref(String key, boolean newPref) {
        sharedPreferences.edit().putBoolean(key, newPref).apply();
    }

    public void setPref(String key, String newPref) {
        sharedPreferences.edit().putString(key, newPref).apply();
    }
}
