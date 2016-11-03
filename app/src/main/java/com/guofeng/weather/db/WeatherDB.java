package com.guofeng.weather.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.guofeng.weather.R;
import com.guofeng.weather.base.BaseApplication;
import com.guofeng.weather.base.C;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class WeatherDB {

    private static WeatherDB weatherDB;//单例对象
    private SQLiteDatabase mSQLiteDatabase; //数据库处理对象
    private String DBFileUrl = C.DB_PATH + "/" + C.DB_NAME;
    private String TAG = "WeatherDB";

    public static WeatherDB getInstance() {
        return WeatherDBHolder.sInstance;
    }

    private static final class WeatherDBHolder {
        public static final WeatherDB sInstance = new WeatherDB();
    }

    /**
     * 打开数据库
     */
    public void openDatabase() {
        Log.e(TAG, "打开数据库");
        if (!(new File(DBFileUrl).exists())) { //不存在我的SQLite数据库文件
            //寻找我的db文件，然后复制到手机
            InputStream is = BaseApplication.getMyAppContext().getResources().openRawResource(R.raw.china_city);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(DBFileUrl);
                byte[] buffer = new byte[400000];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                is.close();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DBFileUrl, null);
    }

    public SQLiteDatabase getDatabase() {
        return mSQLiteDatabase;
    }

    public void closeDatabase() {
        if (this.mSQLiteDatabase != null) {
            this.mSQLiteDatabase.close();
        }
    }


}
