package com.zack.enderweather.application;

import android.app.Application;
import android.content.Context;

import com.zack.enderweather.R;
import com.zack.enderweather.database.EnderWeatherDB;
import com.zack.enderweather.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EnderWeatherApp extends Application {

    private static final int BUFFER_SIZE = 400000;

    private static Context globalContext;

    @Override
    public void onCreate() {
        super.onCreate();

        globalContext = getApplicationContext();

        //初始化城市数据库
        initCityDB();
    }

    public static Context getGlobalContext() {
        return globalContext;
    }

    private void initCityDB() {
        File cityDBFile = getDatabasePath(EnderWeatherDB.DB_CITY_CN);
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
}
