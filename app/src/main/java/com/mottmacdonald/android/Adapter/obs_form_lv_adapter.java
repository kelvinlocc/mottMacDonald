package com.mottmacdonald.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mottmacdonald.android.Models.obs_form_DataModel;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Utils.DeviceUtils;
import com.mottmacdonald.android.Utils.FileUtil;
import com.mottmacdonald.android.Utils.PhotoReSize;
import com.mottmacdonald.android.View.BaseActivity;
import com.mottmacdonald.android.View.ObservationFormActivity;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by KelvinLo on 6/22/2016.
 */
public class obs_form_lv_adapter extends BaseAdapter implements AbsListView.OnScrollListener {
    public static String  TAG = "obs_form_lv_adapter ";
    String[] result;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;
    private ArrayList<Integer> itemNo;
    private ArrayList<obs_form_DataModel> myArrayList_dataModel;
    obs_form_DataModel myData;
    private final int TAKE_PHOTO = 1;


    public obs_form_lv_adapter(Context context2, ArrayList<obs_form_DataModel> data01) {//
        // TODO Auto-generated constructor stub
        context = context2;//
//        itemNo = data01;
        myArrayList_dataModel = data01;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub

//        return itemNo.size();
        return myArrayList_dataModel.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public class Holder {
        TextView tv;
        ImageView img;
        TextView txt_itemNo;
        EditText editText_Observation;
        EditText toBeRemediated;
        EditText followUpAction;
        ImageButton btn_takePhoto;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Log.i("check_", " at position: " + myArrayList_dataModel.get(position).getItemNo());
        final Holder holder = new Holder();
        View view;
        view = inflater.inflate(R.layout.obs_form_lv_item, null);




        myData = new obs_form_DataModel();
        myData = myArrayList_dataModel.get(position);


        holder.txt_itemNo = (TextView) view.findViewById(R.id.item_no);
//        holder.txt_itemNo.setText(Integer.toString(myArrayList_dataModel.get(position).getItemNo()));
        holder.txt_itemNo.setText(Integer.toString(myData.getItemNo()));


        holder.editText_Observation = (EditText) view.findViewById(R.id.edit_txt_obervation);
        holder.editText_Observation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Log.i(TAG," user is finished typing");
                    String string = holder.editText_Observation.getText().toString().trim();
                    myData.setObservation(string);
                    return false;
                }
                return false;
            }
        });
        holder.editText_Observation.setText(myData.getObservation());


        holder.toBeRemediated = (EditText) view.findViewById(R.id.to_be_remediated_before);
        holder.toBeRemediated.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    String string = holder.toBeRemediated.getText().toString().trim();
                    myData.setToBeRemediated_before(string);
                    return  false;
                }
                return false;
            }
        });
        holder.toBeRemediated.setText(myData.getToBeRemediated_before());

        holder.followUpAction = (EditText) view.findViewById(R.id.follow_up_action);
        holder.followUpAction.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    String string = holder.followUpAction.getText().toString().trim();
                    myData.setFollowUpAction(string);
                    return false;
                }
                return false;
            }
        });
        holder.followUpAction.setText(myData.getFollowUpAction());

        holder.btn_takePhoto = (ImageButton) view.findViewById(R.id.add_photo);
        holder.btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"take photo!");
                takePhoto(position);
            }
        });


//        holder.tv=(TextView) view.findViewById(R.id.shop_name);
//        holder.img=(ImageView) view.findViewById(R.id.user_icon);
//        holder.tv.setText(result[position]);
//        holder.img.setImageResource(imageId[position]);


        return view;
    }
    int temp =0;
    private void takePhoto(final int position) {
        obs_form_DataModel newData = new obs_form_DataModel();
//        newData.setItemNo(arrayList_dataModel.size()+1);
//
//        arrayList_dataModel.add(newData);
//        lv_adapter.notifyDataSetChanged();
        Log.i(TAG, "@4");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(FileUtil.getFileRoot(context) + "/mottCacheImage.jpg");
//        File file = new File(FileUtil.getFileRoot(context) + "/mott_" + DeviceUtils.getCurrentTime("yyyyMMddHHmmssSSSS") + "jpg");
        Uri imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("position","test");
        ((Activity) context).startActivityForResult(intent, TAKE_PHOTO);




    }




}