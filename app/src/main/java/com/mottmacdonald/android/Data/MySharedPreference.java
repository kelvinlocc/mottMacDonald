package com.mottmacdonald.android.Data;

import android.content.SharedPreferences;
import android.util.Log;

import java.security.PublicKey;

/**
 * Created by KelvinLo on 7/27/2016.
 */
public class MySharedPreference {
    public static final String TAG = "MySharedPreference";
    SharedPreferences sharedPreferences;

    public SharedPreferences getSharedPreferences(String key) {
        //default to be all mode_private;
        Log.i(TAG,"getSharedPreferences key; "+key);
        this.sharedPreferences = getSharedPreferences(key);
        return this.sharedPreferences;


    }

    public void putString(String key,String string){
        Log.i(TAG,"putString key,string; "+key+","+string);

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(key,string);
        editor.commit();
    }

    public String getString (String key){
        Log.i(TAG,"getString key; "+key);
        return this.sharedPreferences.getString(key,"no value");

    }
}
