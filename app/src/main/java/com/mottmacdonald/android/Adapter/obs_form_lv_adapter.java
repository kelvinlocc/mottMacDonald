package com.mottmacdonald.android.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mottmacdonald.android.Models.obs_form_DataModel;
import com.mottmacdonald.android.R;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by KelvinLo on 6/22/2016.
 */
public class obs_form_lv_adapter extends BaseAdapter implements AbsListView.OnScrollListener {
    public static String TAG = "obs_form_lv_adapter ";
    String[] result;
    Context context;
    public String PREFS_NAME = "";
    //    ArrayList<obs_form_DataModel> myArrayList_dataModel;
    int[] imageId;
    private static LayoutInflater inflater = null;
    private ArrayList<Integer> itemNo;
    private ArrayList<obs_form_DataModel> myArrayList_dataModel;
    obs_form_DataModel myData;
    private final int TAKE_PHOTO = 1;
    Type listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {
    }.getType();
    final SharedPreferences mPrefs;


    public obs_form_lv_adapter(Context context2, ArrayList<obs_form_DataModel> data01, String preference) {//
        // TODO Auto-generated constructor stub
        context = context2;//
//        itemNo = data01;
        PREFS_NAME = preference;
        myArrayList_dataModel = data01;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);// use JSOnM format to store the object (obs_form)
        // check whether a data model store in shared preference:

        String json = mPrefs.getString("MyList", "");
        Gson gson = new Gson();
        if (!json.isEmpty()) {
            Log.i(TAG, "json !null");
            Log.i(TAG, "json " + json.toString());
            myArrayList_dataModel = gson.fromJson(json, listOfObjects);
            obs_form_DataModel data = new obs_form_DataModel();
            if (!myArrayList_dataModel.isEmpty()) {
//                arrayList = myArrayList_dataModel;
                data = myArrayList_dataModel.get(0);
                Log.i(TAG, "myArrayList_dataModel is !empty");
                Log.i(TAG, "data.getItemNo()" + data.getItemNo());
            } else {
                Log.i(TAG, "myArrayList_dataModel is empty");
            }
        } else {
            Log.i(TAG, "json is null");
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (myArrayList_dataModel != null) {
//        return itemNo.size();
            return myArrayList_dataModel.size();
        } else return 0;

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
        ImageView obs_photo;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder = new Holder();
        View view;
        view = inflater.inflate(R.layout.obs_form_lv_item, null);


        Log.i(TAG, " at position: " + myArrayList_dataModel.get(position).getObservation());


        myData = new obs_form_DataModel();
        myData = myArrayList_dataModel.get(position);


        holder.txt_itemNo = (TextView) view.findViewById(R.id.item_no);
//        holder.txt_itemNo.setText(Integer.toString(myArrayList_dataModel.get(position).getItemNo()));
        holder.txt_itemNo.setText(Integer.toString(myData.getItemNo()));


        holder.editText_Observation = (EditText) view.findViewById(R.id.recommendation);
        holder.editText_Observation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Log.i(TAG, " user is finished typing");
                    String string = holder.editText_Observation.getText().toString().trim();
                    Log.i(TAG, string);

                    myArrayList_dataModel.get(position).setObservation(string);
                    Log.i(TAG, "input model get observation: " + myArrayList_dataModel.get(position).getObservation());
//                    notifyDataSetChanged();
                    update(mPrefs, position);
                    return false;
                }
                return false;
            }
        });

        Log.i(TAG, "@adapter " + myArrayList_dataModel.get(position).getObservation());
        holder.editText_Observation.setText(myArrayList_dataModel.get(position).getObservation());


        holder.toBeRemediated = (EditText) view.findViewById(R.id.to_be_remediated_before);
        holder.toBeRemediated.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String string = holder.toBeRemediated.getText().toString().trim();
                    myData.setToBeRemediated_before(string);
                    return false;
                }
                return false;
            }
        });
        holder.toBeRemediated.setText(myData.getToBeRemediated_before());

        holder.followUpAction = (EditText) view.findViewById(R.id.follow_up_action);
        holder.followUpAction.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String string = holder.followUpAction.getText().toString().trim();
                    myData.setFollowUpAction(string);
                    return false;
                }
                return false;
            }
        });
        holder.followUpAction.setText(myData.getFollowUpAction());

        holder.obs_photo = (ImageView) view.findViewById(R.id.obs_photo);
        // get image absolute path from data model
        Bitmap bitmap = BitmapFactory.decodeFile(myData.getPhotoCache().getAbsolutePath());
//        bitmap = myData.getBitmap();
//        Log.i(TAG,"bitmap size: "+sizeOf(bitmap));
        Bitmap reducedBitmap = getResizedBitmap(bitmap, 200);
//        Log.i(TAG,"reducedBitmap; "+sizeOf(reducedBitmap));

        holder.obs_photo.setImageBitmap(reducedBitmap);
//        holder.obs_photo.setImageBitmap(reducedBitmap);


        return view;
    }

    protected void update(SharedPreferences preferences, int position) {
        Log.i(TAG, "update shared preference");
        // and store current object into shared preference:
        SharedPreferences.Editor prefsEditor = preferences.edit();

        Gson gson1 = new Gson();
        String JsonObject = gson1.toJson(myArrayList_dataModel, listOfObjects); // Here list is your List<CUSTOM_CLASS> object
//        Log.i(TAG,"put data model in myList");
//        Log.i(TAG,"myArrayList_dataModel getObservation"+myArrayList_dataModel.get(position).getObservation());

        prefsEditor.putString("MyList", JsonObject);

        prefsEditor.commit();
        notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}