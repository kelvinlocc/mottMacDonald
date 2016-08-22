package com.mottmacdonald.android.Data;

import android.content.SharedPreferences;

import com.mottmacdonald.android.MyApplication;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/14 22:41
 * 备注：
 */
public class DataShared {

    private static final String CONTRACT_JSON = "contract_json";
    private static final String TEMPLATES_JSON = "templates_json";
    private static final String CONDITION_JSON = "condition_json";
    private static final String HUMIDITY_JSON = "humidity_json";
    private static final String WIND_JSON = "wind_json";
    private static final String IS_CONTRACT_EXIST = "";
    private static final String IS_TEMPLATES_EXIST = "";
    private static final String LAST_SYNCHRONIZE_DATE = "last_synchronize_date";
    private boolean report_submission=false;
    private boolean weather_submission=false;
    private boolean general_site_submission = false;



    public static void saveContractData(String jsonString){
        SharedPreferences.Editor editor = MyApplication.getSharedPreferences().edit();
        editor.putString(CONTRACT_JSON, jsonString);
        editor.commit();
    }

    public static void saveTemplatesData(String jsonString){
        SharedPreferences.Editor editor = MyApplication.getSharedPreferences().edit();
        editor.putString(TEMPLATES_JSON, jsonString);
        editor.commit();
    }

    public static void saveConditionData(String jsonString){
        SharedPreferences.Editor editor = MyApplication.getSharedPreferences().edit();
        editor.putString(CONDITION_JSON, jsonString);
        editor.commit();
    }

    public static void saveHumidityData(String jsonString){
        SharedPreferences.Editor editor = MyApplication.getSharedPreferences().edit();
        editor.putString(HUMIDITY_JSON, jsonString);
        editor.commit();
    }

    public static void saveWindData(String jsonString){
        SharedPreferences.Editor editor = MyApplication.getSharedPreferences().edit();
        editor.putString(WIND_JSON, jsonString);
        editor.commit();
    }

    public static String getContractData(){
        return MyApplication.getSharedPreferences().getString(CONTRACT_JSON, "");
    }

    public static String getTemplatesData(){
        return MyApplication.getSharedPreferences().getString(TEMPLATES_JSON, "");
    }

    public static String getConditionData(){
        return MyApplication.getSharedPreferences().getString(CONDITION_JSON, "");
    }

    public static String getHumidityData(){
        return MyApplication.getSharedPreferences().getString(HUMIDITY_JSON, "");
    }

    public static String getWindData(){
        return MyApplication.getSharedPreferences().getString(WIND_JSON, "");
    }

    public static void saveContractStatus(Boolean b){
        SharedPreferences.Editor editor = MyApplication.getSharedPreferences().edit();
        editor.putBoolean(IS_CONTRACT_EXIST, b);
        editor.commit();
    }

    public static void saveTemplatesStatus(Boolean b){
        SharedPreferences.Editor editor = MyApplication.getSharedPreferences().edit();
        editor.putBoolean(IS_TEMPLATES_EXIST, b);
        editor.commit();
    }

    public static boolean getContractStatus(){
        return MyApplication.getSharedPreferences().getBoolean(IS_CONTRACT_EXIST, false);
    }

    public static boolean getTemplatesStatus(){
        return MyApplication.getSharedPreferences().getBoolean(IS_TEMPLATES_EXIST, false);
    }

    public static void saveLastSyncDate(String dateStr){
        SharedPreferences.Editor editor = MyApplication.getSharedPreferences().edit();
        editor.putString(LAST_SYNCHRONIZE_DATE, dateStr);
        editor.commit();
    }

    public static String getLastSyncDate(){
        return MyApplication.getSharedPreferences().getString(LAST_SYNCHRONIZE_DATE, "");
    }
}
