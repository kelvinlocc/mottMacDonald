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
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by KelvinLo on 7/27/2016.
 */
public class MySharedPref_App extends Application {
    public static final String TAG = "MySharedPref_App";
    SharedPreferences sharedPreferences;
    ArrayList<String> arrayList;
    Type listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {
    }.getType();


    public void putString(SharedPreferences sharedPreferences, String key, String string) {
        Log.i(TAG, "putString KEY,string; " + key + "," + string);

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(key, string);
        editor.commit();
    }

    public String getString(Context context, String Pkey,String key) {
        Log.i(TAG, "getString Pkey; " + Pkey);
        SharedPreferences sharedPreferences1 =  context.getSharedPreferences(Pkey,MODE_PRIVATE);
        Log.i(TAG,"check "+sharedPreferences1.getString(key,"no value"));
        return sharedPreferences1.getString(key,"no value");
//        return this.sharedPreferences.getString(KEY,"no value");

    }

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

    public void input_StringArrayList(ArrayList<String> text_lines, Context context, String fileName) {

//        wrtieFileOnInternalStorage(context,"example","boday");

//        String outputString = "new test/\n";
        for (int i = 0; i < text_lines.size(); i++) {
            Log.i(TAG, "arrayList i: "+i+"," + text_lines.get(i).toString());
        }
        try {

            File secondFile = new File(FileUtil.getFileRoot(context) + "/text/", fileName);
            if (secondFile.getParentFile().mkdirs()) {
                secondFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(secondFile);
                DataOutputStream dout = new DataOutputStream(fos);
                dout.writeInt(text_lines.size()); // Save line count
                for (String line : text_lines) // Save lines
                    dout.writeUTF(line);
                dout.flush(); // Flush stream ...
                dout.close(); // ... and close.
//
//                fos.write(outputString.getBytes());
//                fos.flush();
//                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> output_StringArrayList(Context context,String fileName){
        arrayList = new ArrayList<String>();
        File file = new File(FileUtil.getFileRoot(context) + "/text/", fileName);
//        fileName;
        try {
            FileInputStream input = context.openFileInput(fileName); // Open input stream
            DataInputStream din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) { // Read lines
                String line = din.readUTF();
                arrayList.add(line);
            }
            din.close();
        }catch (Exception e ){e.printStackTrace();}
        for (int i = 0; i < arrayList.size(); i++) {
            Log.i(TAG, "output_StringArrayList arrayList i: "+i+"," + arrayList.get(i).toString());
        }

        return  arrayList;

    }

    public void wrtieFileOnInternalStorage(Context context, String sFileName, String sBody) {
        Log.i(TAG, " write to disk,wrtieFileOnInternalStorage");

//        File file = new File(context.getFilesDir(),"mydir");
        File file = new File(FileUtil.getFileRoot(context) + "/file");

        if (!file.exists()) {
            file.mkdir();
        }
        Log.i(TAG, " write to disk,wrtieFileOnInternalStorage / file" + file);

//        try {
//            //Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
//            FileOutputStream output = context.openFileOutput("lines.txt", context.MODE_WORLD_READABLE);
//            DataOutputStream dout = new DataOutputStream(output);
//            dout.writeInt(text_lines.size()); // Save line count
//            for (String line : text_lines) // Save lines
//                dout.writeUTF(line);
//            dout.flush(); // Flush stream ...
//            dout.close(); // ... and close.
//        } catch (IOException exc) {
//            exc.printStackTrace();
//        }


        try {
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        } catch (Exception e) {

        }
    }

    public ArrayList<String> getString_ArrayList(String key) {

        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            FileInputStream input = openFileInput("lines.txt"); // Open input stream
            DataInputStream din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) { // Read lines
                String line = din.readUTF();
                arrayList.add(line);
            }
            din.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

}
