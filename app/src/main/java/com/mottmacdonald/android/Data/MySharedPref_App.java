package com.mottmacdonald.android.Data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mottmacdonald.android.Models.obs_form_DataModel;
import com.mottmacdonald.android.Utils.FileUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by KelvinLo on 7/27/2016.
 */
public class MySharedPref_App extends Application {
    public static final String TAG = "MySharedPref_App";
    ArrayList<String> arrayList;
    Type listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {
    }.getType();
    static String submission_status = "submission_status";
    private String report_submission="report_submission";
    private String weather_submission="weather_submission";
    private String general_site_submission = "general_site_submission";
    private String contract_key = "contract_key";
    private String FormInfoId = "FormInfoId";
    private String contractNumber = "contractNumber";
    private String currentContractId = "currentContractId";


    public String getFormInfoId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(contract_key,MODE_PRIVATE);
        return sharedPreferences.getString(FormInfoId,"null");
    }

    public void setFormInfoId(String value,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(contract_key,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FormInfoId,value);
        editor.apply();
    }





    public String getContractNumber(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(contract_key,MODE_PRIVATE);
        return sharedPreferences.getString(contractNumber,"null");
    }

    public void setContractNumber(String value,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(contract_key,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(contractNumber,value);
        editor.apply();
    }

    public String getCurrentContractId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(contract_key,MODE_PRIVATE);
        return sharedPreferences.getString(currentContractId,"null");
    }

    public void setCurrentContractId(String value,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(contract_key,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(currentContractId,value);
        editor.apply();
    }






    public boolean isReport_submission(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(submission_status,MODE_PRIVATE);
        return sharedPreferences.getBoolean(report_submission,false);
    }

    public void setReport_submission(boolean bool, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(submission_status,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(report_submission,bool);
        editor.apply();
    }

    public boolean isWeather_submission(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(submission_status,MODE_PRIVATE);
        return sharedPreferences.getBoolean(weather_submission,false);
    }

    public void setWeather_submission(boolean bool,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(submission_status,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(weather_submission,bool);
        editor.apply();
    }

    public boolean isGeneral_site_submission(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(submission_status,MODE_PRIVATE);
        return sharedPreferences.getBoolean(general_site_submission,false);
    }

    public void setGeneral_site_submission(boolean bool,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(submission_status,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(general_site_submission,bool);
        editor.apply();
    }



    public void setHead (Context context,String string){
        SharedPreferences sharedPreferences = context.getSharedPreferences("head",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("head",string);
        editor.apply();
        Log.i(TAG, "setHead: string:"+string);

    }

    public String getHead (Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("head",MODE_PRIVATE);

        return sharedPreferences.getString("head","null");
    }

    public String getString(Context context, String Pkey, String key) {
        Log.i(TAG, "getString Pkey; " + Pkey);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Pkey, MODE_PRIVATE);
        Log.i(TAG, "check " + sharedPreferences.getString(key, "no value"));
        return sharedPreferences.getString(key, "no value");
//        return this.sharedPreferences.getString(KEY,"no value");

    }

    // get data from sharedPreference
    public ArrayList<obs_form_DataModel> getArrayList(String key, Context context) {
        Log.i(TAG, "getArrayList KEY(context.MODE_PRIVATE);!!! " + key);
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
                Log.i(TAG, "data.getRecommedation()" + data.getRecommedation());
                return obsFormDataModelArrayList;
            } else {

                Log.i(TAG, "obsFormDataModelArrayList is empty");
                return  null;
            }
        } else {
            Log.i(TAG, "json is null");
        }
        return obsFormDataModelArrayList;
    }

    // write data into internal memory
    public void write_StringArrayList(ArrayList<String> arrayList, Context context, String fileName) {
        File secondFile = new File(FileUtil.getFileRoot(context) + "/data_file/");
        Log.i(TAG, "new input");
        boolean success = true;
        if (!secondFile.exists()) {
            Log.i(TAG, " file exist");
            success = secondFile.mkdir();
        }
        if (success) {
            File file = new File(secondFile, fileName);
            try {
                Log.i(TAG, "file; " + file);
                Log.i(TAG, "file.getAbsolutePath() " + file.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(file);
                DataOutputStream dout = new DataOutputStream(fos);
                dout.writeInt(arrayList.size()); // Save line count
                for (String line : arrayList) // Save lines
                    dout.writeUTF(line);
                dout.flush(); // Flush stream ...
                dout.close(); // ... and close.}

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, " make file failed");
        }
    }

    // read internal memory data
    public ArrayList<String> read_StringArrayList(Context context, String fileName) {
        arrayList = new ArrayList<String>();
        Log.i(TAG, "fileName " + fileName);
        File secondFile = new File(FileUtil.getFileRoot(context) + "/data_file/");
        Log.i(TAG, "new read");
        boolean success = true;
        if (!secondFile.exists()) {
            Log.i(TAG, " file !exist");
            success = secondFile.mkdir();
        }
        if (success) {
            File file = new File(secondFile, fileName);
            Log.i(TAG, " file path "+file.getAbsolutePath());

            try {
                FileInputStream input = new FileInputStream(file); // Open input stream
                Log.i(TAG, " open file success!");

                DataInputStream din = new DataInputStream(input);
                int sz = din.readInt(); // Read line count
                for (int i = 0; i < sz; i++) { // Read lines
                    String line = din.readUTF();
                    arrayList.add(line);
                }
                din.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        for (int i = 0; i < arrayList.size(); i++) {
            Log.i(TAG, "arrayList " + i + ": " + arrayList.get(i).toString());
        }
        return arrayList;

    }


}
