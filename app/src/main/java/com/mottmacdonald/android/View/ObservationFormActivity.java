package com.mottmacdonald.android.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.mottmacdonald.android.Adapter.obs_form_lv_adapter;
import com.mottmacdonald.android.Models.ItemData;
import com.mottmacdonald.android.Models.obs_form_DataModel;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Utils.DeviceUtils;
import com.mottmacdonald.android.Utils.FileUtil;
import com.mottmacdonald.android.Utils.PhotoReSize;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/5/2 17:11
 * 备注：
 */
public class ObservationFormActivity extends BaseActivity {

    private final int TAKE_PHOTO = 1;
    private static final String ITEM_DATA = "item_data";
    private ImageButton addphoto;
    private TableLayout form;
    private TextView item_no;
    private ImageView obs_image;
    private EditText recommendation;
    private EditText date;
    private EditText followup;
    private static String TAG = "ObservationFormActivity";
    private obs_form_lv_adapter lv_adapter;
    private ImageButton myButton;

    private ArrayList<Integer> itemNo;
    private Integer[] tempList = {1, 2, 3};
    private EditText editText_Observation;

    public ArrayList<obs_form_DataModel> arrayList_dataModel;

    public static final String PREFS_NAME = "DataModel";

    // method of list 02:
    Type listOfObjects;
//    = new TypeToken<ArrayList<obs_form_DataModel>>() {
//    }.getType();

    public static void start(Context context, ItemData itemData) {
        Intent intent = new Intent(context, ObservationFormActivity.class);
        intent.putExtra(ITEM_DATA, itemData);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_obs_form);
        ListView form_LV = (ListView) findViewById(R.id.obs_form_lv);

        arrayList_dataModel = new ArrayList<obs_form_DataModel>();
        itemNo = new ArrayList<Integer>(Arrays.asList(tempList));


        SharedPreferences mPrefs = getSharedPreferences("myP", MODE_PRIVATE);// use JSOnM format to store the object (obs_form)
        // check whether a data model store in shared preference:
        String string = mPrefs.getString("myTest", "");
        Log.i("checking", "string " + string + ".");


        String json = mPrefs.getString("MyList", "");
        Gson gson = new Gson();
        if (!json.isEmpty()) {
            Log.i("checking", "json !null");
            Log.i("checking", "json " + json.toString());
            listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {}.getType();

            ArrayList<obs_form_DataModel> list2 = gson.fromJson(json, listOfObjects);
            obs_form_DataModel data = new obs_form_DataModel();
            if (!list2.isEmpty()) {
                arrayList_dataModel=list2;
                data = list2.get(0);
                Log.i("checking", "list2 is !empty");
                Log.i("checking", "data.getItemNo()" + data.getItemNo());
            } else {
                Log.i("checking", "list2 is empty");
            }
        } else {
            Log.i("checking", "json is null");
        }

        lv_adapter = new obs_form_lv_adapter(this, arrayList_dataModel);
        form_LV.setAdapter(lv_adapter);

        myButton = (ImageButton) findViewById(R.id.addphoto);
//        editText_Observation = (EditText) findViewById(R.id.edit_txt_obervation);

        initViews();
        takePhoto();

    }

    public void init() {

//        TableLayout form = (TableLayout) findViewById(R.id.table_layout);
        //we don;t use table layout
//        for (int i = 0; i < 0; i++) {
//
//            TableRow row = new TableRow(this);
////            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
//            Log.i(TAG, "update03");
//
//            Log.i(TAG, "TableRow.LayoutParams: " + lp);
//            row.setLayoutParams(lp);
//            item_no = new TextView(this);
//            item_no.setText("temp");
//            addphoto = new ImageButton(this);
//            obs_image = new ImageView(this);
//            recommendation = new EditText(this);
//            recommendation.setInputType(InputType.TYPE_CLASS_TEXT);
//            date = new EditText(this);
//            date.setInputType(InputType.TYPE_CLASS_DATETIME);
//            followup = new EditText(this);
//            followup.setInputType(InputType.TYPE_CLASS_TEXT);
//            row.addView(item_no);
//            row.addView(addphoto);
//            row.addView(obs_image);
//            row.addView(recommendation);
//            row.addView(date);
//            row.addView(followup);
//            form.addView(row, i);
//        }

    }

    private void initViews() {
        mAQuery.id(R.id.back_btn).clicked(clickListener);
        mAQuery.id(R.id.addphoto).clicked(clickListener);

    }

    private void takePhoto() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(FileUtil.getFileRoot(mContext) + "/mottCacheImage.jpg");
//        File file = new File(FileUtil.getFileRoot(mContext) + "/mott_" + DeviceUtils.getCurrentTime("yyyyMMddHHmmssSSSS") + "jpg");
        Uri imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((BaseActivity) mContext).startActivityForResult(intent, TAKE_PHOTO);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {

                File file = new File(FileUtil.getFileRoot(mContext) + "/mottCacheImage.jpg");
                File saveFile = new File(FileUtil.getFileRoot(mContext) + "/mott_" + DeviceUtils.getCurrentTime("yyyyMMddHHmmssSSSS") + ".jpg");
                PhotoReSize photoReSize = new PhotoReSize(mContext);
                photoReSize.reSize(file, DeviceUtils.getDisplayWidth(), saveFile);
                mAQuery.id(R.id.obs_image).image(saveFile.getAbsolutePath());

                // add item into data model
                obs_form_DataModel newData = new obs_form_DataModel();
                newData.setItemNo(arrayList_dataModel.size() + 1);
                newData.setPhotoCache(saveFile);

                arrayList_dataModel.add(newData);

//                Set<String> set = new HashSet<String>();
//                set.addAll(arrayList_dataModel);
                SharedPreferences mPrefs = getSharedPreferences("myP", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();




                // and store current object into shared preference:
                Type type = new TypeToken<List<obs_form_DataModel>>() {
                }.getType();
                Gson gson = new Gson();
//                Json json= gson.toJson(newData);

//                List<obs_form_DataModel> listObj = gson.fromJson(json);
//                prefsEditor.putString("MyObject",json);
                listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {}.getType();
                String strObject = gson.toJson(arrayList_dataModel, listOfObjects); // Here list is your List<CUSTOM_CLASS> object


                prefsEditor.putString("MyList", strObject);
                prefsEditor.putString("myTest", "test");

                Log.i("checking", "                prefsEditor.putString(\"MyList\", \"test\");\n");
                prefsEditor.commit();

//                String json = mPrefs.getString("MyList", "");
//
//                if (json != "") {
//                    Log.i("checking", "json ! null");
//                    Log.i("checking", "json " + json.toString());
//                    ArrayList<obs_form_DataModel> list2 = gson.fromJson(json, listOfObjects);
//                    obs_form_DataModel data = new obs_form_DataModel();
//                    if (!list2.isEmpty()) {
//                        data = list2.get(0);
//                        Log.i("checking", "list2 is !empty");
//                        Log.i("checking", "data.getItemNo()" + data.getItemNo());
//                    } else {
//                        Log.i("checking", "list2 is empty");
//                    }
//                } else {
//                    Log.i("checking", "json is null");
//                }



                lv_adapter.notifyDataSetChanged();
            }
            init();
        }

    }


    // convert the arrayList into set , for store in share preference

//    public void addTask(obs_form_DataModel newData) {
//        if (null == arrayList_dataModel) {
//            arrayList_dataModel = new ArrayList<obs_form_DataModel>();
//        }
//        arrayList_dataModel.add(newData);
//
//        // save the task list to preference
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        try {
//            editor.putString("task", ObjectSerializer.serialize(arrayList_dataModel));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        editor.commit();
//    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_btn:
                    finish();
                    break;
                case R.id.addphoto:
                    Log.i(TAG, " add photo button is clicked");
                    PopupMenu myPopup = new PopupMenu(mContext, myButton);
                    myPopup.getMenuInflater().inflate(R.menu.popup_menu, myPopup.getMenu());
                    myPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.capture:
                                    takePhoto();
                                    return true;
                                case R.id.album:
                                    return true;

                                default:
                                    return false;
                            }

                        }
                    });
                    myPopup.show();
                    break;
//                case R.id.addphoto:
//                    PopupMenu popup = new PopupMenu(ObservationFormActivity.this, addphoto);
//                    //Inflating the Popup using xml file
//                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
//                    //registering popup with OnMenuItemClickListener
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            // handle popup choices: capture / choose photo
//                            switch (item.getItemId()) {
//                                case R.id.capture:
//                                    //capture image
//                                    takePhoto();
//                                    return true;
//                                case R.id.album:
//                                    //select from album
//                                    Intent album = new Intent(Intent.ACTION_GET_CONTENT);
//                                    album.addCategory(Intent.CATEGORY_OPENABLE);
//                                    album.setType("image/*");
//                                    startActivityForResult(album, 0);
//                                    return true;
//                                default:
//                                    return false;
//                            }
//                        }
//                    });
//                    popup.show(); //showing popup menu
//                    break;
            }
        }
    };
}
