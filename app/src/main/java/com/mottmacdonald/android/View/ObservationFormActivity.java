package com.mottmacdonald.android.View;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
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

import com.mottmacdonald.android.Adapter.obs_form_lv_adapter;
import com.mottmacdonald.android.Models.ItemData;
import com.mottmacdonald.android.Models.obs_form_DataModel;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Utils.DeviceUtils;
import com.mottmacdonald.android.Utils.FileUtil;
import com.mottmacdonald.android.Utils.PhotoReSize;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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

    private ArrayList<obs_form_DataModel> arrayList_dataModel;

    public static void start(Context context, ItemData itemData) {
        Intent intent = new Intent(context, ObservationFormActivity.class);
        intent.putExtra(ITEM_DATA, itemData);
        context.startActivity(intent);
        Log.i(TAG, "@1" + " update");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "@2");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_form);
        ListView form_LV = (ListView) findViewById(R.id.obs_form_lv);

        arrayList_dataModel = new ArrayList<obs_form_DataModel>();
        itemNo = new ArrayList<Integer>(Arrays.asList(tempList));
        // workable: lv_adapter = new obs_form_lv_adapter(this, itemNo);

        lv_adapter = new obs_form_lv_adapter(this, arrayList_dataModel);
        form_LV.setAdapter(lv_adapter);
        Log.i(TAG, "update");

        myButton = (ImageButton) findViewById(R.id.addphoto);
//        editText_Observation = (EditText) findViewById(R.id.edit_txt_obervation);


//        addPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG," add phot button is clicked");
//                PopupMenu popup = new PopupMenu(ObservationFormActivity.this, addphoto);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        // handle popup choices: capture / choose photo
//                        switch (item.getItemId()) {
//                            case R.id.capture:
//                                //capture image
//                                takePhoto();
//                                return true;
//                            case R.id.album:
//                                //select from album
//                                Intent album = new Intent(Intent.ACTION_GET_CONTENT);
//                                album.addCategory(Intent.CATEGORY_OPENABLE);
//                                album.setType("image/*");
//                                startActivityForResult(album, 0);
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
//                popup.show(); //showing popup menu
//            }
//        });

        initViews();
        takePhoto();

    }

    public void init() {
        Log.i(TAG, "@6");
//        TableLayout form = (TableLayout) findViewById(R.id.table_layout);
        //we don;t use table layout
        for (int i = 0; i < 0; i++) {

            TableRow row = new TableRow(this);
//            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            Log.i(TAG, "update03");

            Log.i(TAG, "TableRow.LayoutParams: " + lp);
            row.setLayoutParams(lp);
            item_no = new TextView(this);
            item_no.setText("temp");
            addphoto = new ImageButton(this);
            obs_image = new ImageView(this);
            recommendation = new EditText(this);
            recommendation.setInputType(InputType.TYPE_CLASS_TEXT);
            date = new EditText(this);
            date.setInputType(InputType.TYPE_CLASS_DATETIME);
            followup = new EditText(this);
            followup.setInputType(InputType.TYPE_CLASS_TEXT);
            row.addView(item_no);
            row.addView(addphoto);
            row.addView(obs_image);
            row.addView(recommendation);
            row.addView(date);
            row.addView(followup);
            form.addView(row, i);
        }
    }

    private void initViews() {
        mAQuery.id(R.id.back_btn).clicked(clickListener);
        mAQuery.id(R.id.addphoto).clicked(clickListener);
        Log.i(TAG, "@3");

    }

    private void takePhoto() {
        obs_form_DataModel newData = new obs_form_DataModel();
        newData.setItemNo(arrayList_dataModel.size() + 1);

        arrayList_dataModel.add(newData);
        lv_adapter.notifyDataSetChanged();
        Log.i(TAG, "@4");
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File file = new File(FileUtil.getFileRoot(mContext) + "/mottCacheImage.jpg");
////        File file = new File(FileUtil.getFileRoot(mContext) + "/mott_" + DeviceUtils.getCurrentTime("yyyyMMddHHmmssSSSS") + "jpg");
//        Uri imageUri = Uri.fromFile(file);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        ((BaseActivity) mContext).startActivityForResult(intent, TAKE_PHOTO);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


//        Log.i(TAG, "onActivityResult called !"+"at position "+position);
        super.onActivityResult(requestCode, resultCode, data);
//        int position = data.getIntExtra("position", 0);
        if (data.hasExtra("position")) {
                    Log.i(TAG, " has intent");
        }
        else {
            Log.i(TAG, " NO intent");

        }
//        String string = data.getExtras().getString("position");
//        Log.i(TAG, "onActivityResult called !" + "string " + string);

        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {

                File file = new File(FileUtil.getFileRoot(mContext) + "/mottCacheImage.jpg");
                File saveFile = new File(FileUtil.getFileRoot(mContext) + "/mott_" + DeviceUtils.getCurrentTime("yyyyMMddHHmmssSSSS") + ".jpg");
                PhotoReSize photoReSize = new PhotoReSize(mContext);
                photoReSize.reSize(file, DeviceUtils.getDisplayWidth(), saveFile);
                mAQuery.id(R.id.obs_image).image(saveFile.getAbsolutePath());
            }
            init();
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_btn:
                    finish();
                    break;
                case R.id.addphoto:
                    Log.i(TAG, " add phot button is clicked");
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
