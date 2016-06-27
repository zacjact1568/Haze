package com.zack.enderweather.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.zack.enderweather.R;
import com.zack.enderweather.database.DatabaseDispatcher;
import com.zack.enderweather.preference.PreferenceDispatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class EnderWeatherApp extends Application {

    private static final int BUFFER_SIZE = 400000;

    private static Context globalContext;

    @Override
    public void onCreate() {
        super.onCreate();

        globalContext = getApplicationContext();

        //设定preferences默认值
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        initFromPreferences();

        initCityDB();
    }

    public static Context getGlobalContext() {
        return globalContext;
    }

    /** 通过Preference中的数据初始化某些设置 */
    private void initFromPreferences() {
        PreferenceDispatcher dispatcher = PreferenceDispatcher.getInstance();
        //设定运行时的默认语言
        initLocale(dispatcher.getStringPref(PreferenceDispatcher.KEY_PREF_LANGUAGE));
        //设定白天夜间模式
        initNightMode(dispatcher.getStringPref(PreferenceDispatcher.KEY_PREF_NIGHT_MODE));
    }

    /** 初始化城市数据库 */
    private void initCityDB() {
        File cityDBFile = getDatabasePath(DatabaseDispatcher.DB_CITY_CN);
        if (cityDBFile.exists()) {
            return;
        }
        File cityDBDir = cityDBFile.getParentFile();
        if (cityDBDir.exists() || cityDBDir.mkdir()) {
            //若目录不存在，exists返回false，就去创建目录，mkdir返回true，说明创建成功
            //若目录已存在，exists返回true，就不会执行mkdir了
            //若目录不存在，mkdir也返回false，不会执行下面的内容

            //导入城市数据库
            try {
                InputStream is = getResources().openRawResource(R.raw.city_cn);
                //FileOutputStream也有创建文件的功能
                FileOutputStream fos = new FileOutputStream(cityDBFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initLocale(String value) {
        if (value.equals("def")) {
            return;
        }
        Configuration config = getResources().getConfiguration();
        switch (value) {
            case "en":
                config.locale = Locale.ENGLISH;
                break;
            case "zh":
                config.locale = Locale.CHINESE;
                break;
            default:
                break;
        }
        getResources().updateConfiguration(config, null);
    }

    private void initNightMode(String value) {
        int mode = AppCompatDelegate.MODE_NIGHT_NO;
        switch (value) {
            case "off":
                mode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case "on":
                mode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case "auto":
                mode = AppCompatDelegate.MODE_NIGHT_AUTO;
                break;
            case "def":
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
            default:
                break;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}
