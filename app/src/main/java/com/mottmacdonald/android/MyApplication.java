package com.mottmacdonald.android;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/12 22:29
 * 备注：
 */
public class MyApplication extends Application{

    public static final String DB_NAME = "MOTT";
    private List<Activity> activityList;

    private static MyApplication mInstance;
    public DaoSession daoSession;
    public SQLiteDatabase db;
    public DaoMaster.DevOpenHelper helper;
    public DaoMaster daoMaster;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        activityList = new ArrayList<>();
        setupDatabase();
    }


    public static MyApplication getInstance(){
        if (mInstance == null) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }

    public static SharedPreferences getSharedPreferences(){
        return getInstance().getSharedPreferences("MottData", getInstance().MODE_PRIVATE);
    }

    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        helper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    public void removeActivity(){
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
        activityList.clear();
    }

    public void removeActivityExcept(Class class1){
        Activity temp = null;
        for (int i = 0; i < activityList.size(); i++) {
            if (activityList.get(i).getClass().getSimpleName().equals(class1.getSimpleName())){
                temp = activityList.get(i);
            }else {
                activityList.get(i).finish();
            }
        }
        activityList.clear();
        if (temp != null)
            activityList.add(temp);
    }
}
