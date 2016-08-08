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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    public ArrayList<obs_form_DataModel> arrayList;
    public MySharedPref_App mySharedPref_app;
    public String KEY;
    public static Context mContext;
    // method of list 02:
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
        ListView form_LV = (ListView) findViewById(R.id.obs_form_lv);
        arrayList = new ArrayList<>();
        mySharedPref_app = new MySharedPref_App();

        String head =  mySharedPref_app.getHead(mContext);
//        Log.i(TAG,"create the unique ID for shared preference: head+ tail "+head+","+tail);
        //ObservationFormActivity: create the unique ID for shared preference: head+ tail P560(R)1760,00
        KEY = head+item_id;
        Log.i(TAG, "onCreate: KEY =head + item_id: "+KEY);

        MySharedPref_App mySharedPref_app = new MySharedPref_App();
        arrayList = mySharedPref_app.getArrayList(KEY,mContext);
        lv_adapter = new obs_form_lv_adapter(this, arrayList, KEY);
        form_LV.setAdapter(lv_adapter);
        myButton = (ImageButton) findViewById(R.id.addphoto);
        initViews();
    }



    private void initViews() {
        mAQuery.id(R.id.back_btn).clicked(clickListener);
        mAQuery.id(R.id.addphoto).clicked(clickListener);

    }

    private void takePhoto() {


        /*if (Build.VERSION.SDK_INT <= 19) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(FileUtil.getFileRoot(mContext) + "/mottCacheImage.jpg");
//        File file = new File(FileUtil.getFileRoot(mContext) + "/mott_" + DeviceUtils.getCurrentTime("yyyyMMddHHmmssSSSS") + "jpg");
            Uri imageUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            ((BaseActivity) mContext).startActivityForResult(intent, TAKE_PHOTO);

        } else if (Build.VERSION.SDK_INT > 19) {
*/
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, TAKE_PHOTO);
        //}
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
            // make a new bitmap from your file


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

//                Bitmap bitmap = BitmapFactory.decodeFile(saveFile.getAbsolutePath());


                mAQuery.id(R.id.obs_image).image(saveFile.getAbsolutePath());

                // add item into data model
                newData.setItemNo(arrayList.size() + 1);
                newData.setFile(saveFile);
                newData.setRecommedation("N/A");

                newData.setToBeRemediated_before("N/A");
                newData.setFollowUpAction("N/A");
//                Log.i(TAG, "saveFile: " + saveFile);
//                Log.i(TAG, "saveFile.getAbsolutePath(): " + saveFile.getAbsolutePath());

                // save photo into bitmap:
//                newData.setBitmap(bitmap);

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
//                    Log.i(TAG,"set ")

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
            prefsEditor.commit();
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


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_btn:
                    saveObsFrom();


//                    public static void saveFormItemObservation(Context context, String formItemId, File image,
//                    String recommendation, String remediated,
//                    String followup, ICallback<SaveFormItemObservationModel> callback){


//                    SaveFormApi.saveFormWeather(mContext, formInfoId, conditionId, temperatureText, humidityId,
//                            windId, remarkText, new ICallback<SaveFormWeatherModel>() {
//                                @Override
//                                public void onSuccess(SaveFormWeatherModel saveFormWeatherModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
//                                    dismissProgress();
//                                    if (saveFormWeatherModel != null) {
//                                        saveWeatherLocalData(conditionId, temperatureText, humidityId, windId, remarkText);
//                                        GeneralSiteActivity.start(mContext, contractName, contractId, formInfoId);
//                                    } else {
//                                        showRequestFailToast();
//                                    }
//                                }
//
//                                @Override
//                                public void onError(int i, String s) {
//
//                                }
//                            });
//
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
//                                    select from album
                                    selectFromAlbum();

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
