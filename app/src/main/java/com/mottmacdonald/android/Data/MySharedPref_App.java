package com.mottmacdonald.android.Data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mottmacdonald.android.Models.obs_form_DataModel;

import java.lang.reflect.Type;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by KelvinLo on 7/27/2016.
 */
public class MySharedPref_App extends Application {
    public static final String TAG = "MySharedPref_App";
    SharedPreferences sharedPreferences;
    ArrayList<obs_form_DataModel> arrayList;
    Type listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {
    }.getType();



    public void putString(SharedPreferences sharedPreferences, String key, String string) {
        Log.i(TAG, "putString key,string; " + key + "," + string);

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(key, string);
        editor.commit();
    }

    public String getString(SharedPreferences sharedPreferences, String key) {
        Log.i(TAG, "getString key; " + key);
//        sharedPreferences.getString(key)
        return "";
//        return this.sharedPreferences.getString(key,"no value");

    }

    public ArrayList<obs_form_DataModel> getArrayList(String key,Context context) {
        Log.i(TAG, "getArrayList key(context.MODE_PRIVATE);!!! " + key);
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(key, context.MODE_PRIVATE);
        ArrayList<obs_form_DataModel> obsFormDataModelArrayList = new ArrayList<obs_form_DataModel>();
        // check whether a data model store in shared preference:
        String string = sharedPreferences.getString("myTest", "");
        Log.i(TAG, "string " + string + ".");
        String json = sharedPreferences.getString("MyList", "");
        Gson gson = new Gson();
        if (!json.isEmpty()) {
            Log.i(TAG, "json !null");
            Log.i(TAG, "json " + json.toString());
            obsFormDataModelArrayList = gson.fromJson(json, listOfObjects);
            obs_form_DataModel data = new obs_form_DataModel();
            if (!obsFormDataModelArrayList.isEmpty()) {
//                arrayList = obsFormDataModelArrayList;
                data = obsFormDataModelArrayList.get(0);
                Log.i(TAG, "obsFormDataModelArrayList is !empty");
                Log.i(TAG, "data.getItemNo()" + data.getItemNo());
                Log.i(TAG, "data.getObservation()" + data.getObservation());
                return obsFormDataModelArrayList;
            } else {
                Log.i(TAG, "obsFormDataModelArrayList is empty");
            }
        } else {
            Log.i(TAG, "json is null");
        }
        return obsFormDataModelArrayList;
    }
}
