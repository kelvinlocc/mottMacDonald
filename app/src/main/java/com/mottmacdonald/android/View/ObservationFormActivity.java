package com.mottmacdonald.android.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mottmacdonald.android.Adapter.obs_form_lv_adapter;
import com.mottmacdonald.android.Data.MySharedPref_App;
import com.mottmacdonald.android.Models.ItemData;
import com.mottmacdonald.android.Models.obs_form_DataModel;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Utils.DeviceUtils;
import com.mottmacdonald.android.Utils.FileUtil;
import com.mottmacdonald.android.Utils.PhotoReSize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/5/2 17:11
 * 备注：
 */
public class ObservationFormActivity extends BaseActivity {


    private final int TAKE_PHOTO = 1;
    private final int PICK_IMAGE_REQUEST = 2;
    private static final String ITEM_DATA = "item_data";
    private static String TAG = "ObservationFormActivity";
    private obs_form_lv_adapter lv_adapter;
    private ImageButton myButton;

    public ArrayList<obs_form_DataModel> arrayList;
    public MySharedPref_App mySharedPref_app;
    public String KEY;
    public boolean enable_delete_action;

    public static Context mContext;
    Type listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {
    }.getType();
    public static String item_id;

    public static void start(Context context, ItemData itemData) {
        Intent intent = new Intent(context, ObservationFormActivity.class);
        intent.putExtra(ITEM_DATA, itemData);
        mContext = context;
        context.startActivity(intent);
        item_id = itemData.item_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_form);
        final ListView form_LV = (ListView) findViewById(R.id.obs_form_lv);
        arrayList = new ArrayList<>();
        mySharedPref_app = new MySharedPref_App();

        String head =  mySharedPref_app.getHead(mContext);
//        Log.i(TAG,"create the unique ID for shared preference: head+ tail "+head+","+tail);
        //ObservationFormActivity: create the unique ID for shared preference: head+ tail P560(R)1760,00
        KEY = head+item_id;
        enable_delete_action =false;
        Log.i(TAG, "onCreate: KEY =head + item_id: "+KEY);

        MySharedPref_App mySharedPref_app = new MySharedPref_App();
        arrayList = mySharedPref_app.getArrayList(KEY,mContext);
        lv_adapter = new obs_form_lv_adapter(this, arrayList, KEY,enable_delete_action);
        form_LV.setAdapter(lv_adapter);
        myButton = (ImageButton) findViewById(R.id.addphoto);

        ToggleButton toggle_remove = (ToggleButton) findViewById(R.id.toggleButton);
        toggle_remove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked ) {
                    lv_adapter.enable_delete_action =true;
                    lv_adapter.notifyDataSetChanged();
                    Toast.makeText(ObservationFormActivity.this, "enable delete", Toast.LENGTH_SHORT).show();
                }
                else {
                    lv_adapter.enable_delete_action =false;
                    lv_adapter.notifyDataSetChanged();
                    Toast.makeText(ObservationFormActivity.this, "disable delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initViews();
    }

    private void initViews() {
        mAQuery.id(R.id.back_btn).clicked(clickListener);
        mAQuery.id(R.id.addphoto).clicked(clickListener);
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_btn:
                    saveObsFrom();
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
                                    selectFromAlbum();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    myPopup.show();
                    break;


            }
        }
    };

    private void takePhoto() {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, TAKE_PHOTO);
    }

    private void selectFromAlbum(){
        if (Build.VERSION.SDK_INT <= 19) {
            Intent album = new Intent(Intent.ACTION_GET_CONTENT);
            album.addCategory(Intent.CATEGORY_OPENABLE);
            album.setType("image/*");
            startActivityForResult(album, PICK_IMAGE_REQUEST);
        } else if (Build.VERSION.SDK_INT > 19) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }
    private File savebitmap(Bitmap bitmap) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(FileUtil.getFileRoot(mContext) + "/mottCacheImage.png");

        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        obs_form_DataModel newData = new obs_form_DataModel();
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                //File file = new File(FileUtil.getFileRoot(mContext) + "/mottCacheImage.jpg");
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                File file = savebitmap(photo);
                File saveFile = new File(FileUtil.getFileRoot(mContext) + "/mott_" + DeviceUtils.getCurrentTime("yyyyMMddHHmmssSSSS") + ".jpg");
                PhotoReSize photoReSize = new PhotoReSize(mContext);
                photoReSize.reSize(file, DeviceUtils.getDisplayWidth(), saveFile);
                mAQuery.id(R.id.obs_image).image(saveFile.getAbsolutePath());
                // add item into data model
                newData.setItemNo(arrayList.size() + 1);
                newData.setFile(saveFile);
                newData.setRecommedation("N/A");

                newData.setToBeRemediated_before("N/A");
                newData.setFollowUpAction("N/A");

            } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Log.i(TAG, "select photo from album");
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    Uri selectedImageUri = data.getData();
                    String imagePath = getRealPathFromURI(selectedImageUri);
                    Log.i(TAG, "imagePath: " + imagePath);

                    File file = new File(imagePath);
                    ImageView imageView = (ImageView) findViewById(R.id.obs_image);
                    Bitmap bitmap1 = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.setImageBitmap(bitmap1);

                    newData.setItemNo(arrayList.size() + 1);
                    newData.setFile(file);
                    newData.setRecommedation("N/A");
                    newData.setToBeRemediated_before("N/A");
                    newData.setFollowUpAction("N/A");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            arrayList.add(newData);
            SharedPreferences mPrefs = getSharedPreferences(KEY, MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();


            Log.i(TAG,"notifyDataSetChanged");
            Log.i(TAG,"arrayList.size() "+arrayList.size());
            // and store current object into shared preference:
            Gson gson = new Gson();
            String JsonObject = gson.toJson(arrayList, listOfObjects); // Here list is your List<CUSTOM_CLASS> object


            prefsEditor.putString("MyList", JsonObject);
            prefsEditor.putString("myTest", "test");
            prefsEditor.apply();
            lv_adapter.notifyDataSetChanged();
        }

    }



    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }




    private void saveObsFrom() {
//        final String temperatureText = mAQuery.id(R.id.temperature_text).getText().toString();
//        final String remarkText = mAQuery.id(R.id.remarks_text).getText().toString();
//        if (TextUtils.isEmpty(conditionId)) {
//            showToast("Please select condition");
//            return;
//        }

//        showProgress();
//        SaveFormApi.saveFormItemObservation(mContext, formItemId, conditionId, temperatureText, humidityId,
//                windId, remarkText, new ICallback<SaveFormWeatherModel>() {
//                    @Override
//                    public void onSuccess(SaveFormWeatherModel saveFormWeatherModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
//                        dismissProgress();
//                        if (saveFormWeatherModel != null) {
//                            saveWeatherLocalData(conditionId, temperatureText, humidityId, windId, remarkText);
//                            GeneralSiteActivity.start(mContext, contractName, contractId, formInfoId);
//                        } else {
//                            showRequestFailToast();
//                        }
//                    }
//
//                    @Override
//                    public void onError(int i, String s) {
//
//                    }
//                });
    }

}
