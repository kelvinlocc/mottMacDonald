package com.mottmacdonald.android.View;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mottmacdonald.android.Models.ItemData;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Utils.DeviceUtils;
import com.mottmacdonald.android.Utils.FileUtil;
import com.mottmacdonald.android.Utils.PhotoReSize;

import java.io.File;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/5/2 17:11
 * 备注：
 */
public class ObservationFormActivity extends BaseActivity{

    private final int TAKE_PHOTO = 1;
    private static final String ITEM_DATA = "item_data";
    private ImageButton addphoto;
    private TableLayout form;
    private TextView item_no;
    private ImageView obs_image;
    private EditText recommendation;
    private EditText date;
    private EditText followup;

    public static void start(Context context, ItemData itemData){
        Intent intent = new Intent(context, ObservationFormActivity.class);
        intent.putExtra(ITEM_DATA, itemData);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_form);
        initViews();
        takePhoto();
    }

    public void init(){
        TableLayout form = (TableLayout) findViewById(R.id.table_layout);

        for (int i = 0; i < 1; i++) {

            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
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
            form.addView(row,i);
        }
    }

    private void initViews(){
        mAQuery.id(R.id.back_btn).clicked(clickListener);
        mAQuery.id(R.id.addphoto).clicked(clickListener);
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(FileUtil.getFileRoot(mContext) + "/mottCacheImage.jpg");
//        File file = new File(FileUtil.getFileRoot(mContext) + "/mott_" + DeviceUtils.getCurrentTime("yyyyMMddHHmmssSSSS") + "jpg");
        Uri imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((BaseActivity)mContext).startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == TAKE_PHOTO){
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
            switch (v.getId()){
                case R.id.back_btn:
                    finish();
                    break;
                case R.id.addphoto:
                    PopupMenu popup = new PopupMenu(ObservationFormActivity.this, addphoto);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            // handle popup choices: capture / choose photo
                            switch (item.getItemId()) {
                                case R.id.capture:
                                    //capture image
                                    takePhoto();
                                    return true;
                                case R.id.album:
                                    //select from album
                                    Intent album = new Intent(Intent.ACTION_GET_CONTENT);
                                    album.addCategory(Intent.CATEGORY_OPENABLE);
                                    album.setType("image/*");
                                    startActivityForResult(album, 0);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show(); //showing popup menu
                    break;
            }
        }
    };
}
