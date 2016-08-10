package com.mottmacdonald.android.Adapter;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mottmacdonald.android.Data.MySharedPref_App;
import com.mottmacdonald.android.Models.obs_form_DataModel;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Utils.showDatePicker;
import com.mottmacdonald.android.Utils.showTimePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by KelvinLo on 6/22/2016.
 */
public class obs_form_lv_adapter extends BaseAdapter implements AbsListView.OnScrollListener {
    public static String TAG = "obs_form_lv_adapter ";
    String[] result;
    Context context;
    public String KEY = "";
    //    ArrayList<obs_form_DataModel> arrayList;
    int[] imageId;
    private static LayoutInflater inflater = null;
    private ArrayList<Integer> itemNo;
    private ArrayList<obs_form_DataModel> arrayList;
    obs_form_DataModel myData;
    private final int TAKE_PHOTO = 1;
    Type listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {
    }.getType();
    final SharedPreferences mPrefs;
    private Calendar myCalendar;
    private DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener date;

    public obs_form_lv_adapter(Context context2, ArrayList<obs_form_DataModel> data01, String preference) {//
        // TODO Auto-generated constructor stub
        context = context2;//
//        itemNo = data01;
//        KEY = preference;
//        arrayList = data01;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPrefs = context.getSharedPreferences(preference, Context.MODE_PRIVATE);// use JSOnM format to store the object (obs_form)
        // check whether a data model store in shared preference:
        MySharedPref_App mySharedPref_app = new MySharedPref_App();
//        arrayList = mySharedPref_app.getArrayList(preference, context2);
        arrayList = data01;
        Log.i(TAG,"arrayList.size() @adapter"+arrayList.size());
        myCalendar =  Calendar.getInstance();

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayList.size();

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


        Log.i(TAG, " at position: " + arrayList.get(position).getRecommedation());
        MySharedPref_App mySharedPref_app = new MySharedPref_App();
//        arrayList = mySharedPref_app.getArrayList(KEY, context);

        myData = new obs_form_DataModel();
        myData = arrayList.get(position);
        Log.i(TAG,"arrayList.size() @adapter"+arrayList.size());


        holder.txt_itemNo = (TextView) view.findViewById(R.id.item_no);
//        holder.txt_itemNo.setText(Integer.toString(arrayList.get(position).getItemNo()));
        holder.txt_itemNo.setText(Integer.toString(myData.getItemNo()));


        holder.editText_Observation = (EditText) view.findViewById(R.id.recommendation);
        holder.editText_Observation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Log.i(TAG, " user is finished typing");
                    String string = holder.editText_Observation.getText().toString().trim();
                    Log.i(TAG, string);

                    arrayList.get(position).setRecommedation(string);
                    Log.i(TAG, "input model get observation: " + arrayList.get(position).getRecommedation());
//                    notifyDataSetChanged();
                    update(mPrefs, position);
                    return false;
                }
                return false;
            }
        });

        Log.i(TAG, "@adapter " + arrayList.get(position).getRecommedation());
        holder.editText_Observation.setText(arrayList.get(position).getRecommedation());


        holder.toBeRemediated = (EditText) view.findViewById(R.id.to_be_remediated_before);
//        holder.toBeRemediated.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    String string = holder.toBeRemediated.getText().toString().trim();
//                    myData.setToBeRemediated_before(string);
//                    return false;
//                }
//                return false;
//            }
//        });


        holder.toBeRemediated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");

                final DialogFragment timePickerFragment = new showTimePicker.TimePickerFragment(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(hourOfDay, minute);
                        Log.i(TAG, "onTimeSet: hourofday: "+hourOfDay);
                        String date_time = myData.getToBeRemediated_before();
                        date_time = date_time+" "+hourOfDay+":"+minute;
                        myData.setToBeRemediated_before(date_time);
                        Log.i(TAG, "onTimeSet: date_time "+date_time);
                        update(mPrefs, position);
                    }
                };

                DialogFragment datePickerFragment = new showDatePicker.DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Log.d(TAG, "onDateSet");
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, day);

                        String Date = Integer.toString(year)+"-"+Integer.toString(month+1)+"-"+Integer.toString(day);
                        Log.i(TAG, "onDateSet: date "+Date);
                        myData.setToBeRemediated_before(Date);

//                        editText.setText(df.format(c.getTime()));
//                        nextField.requestFocus(); //moves the focus to something else after dialog is closed
                        timePickerFragment.show(((FragmentActivity)context).getFragmentManager(),"timePicker");

                    }
                };

                ((FragmentActivity)context).getSupportFragmentManager();
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                datePickerFragment.show(((FragmentActivity) context).getFragmentManager(), "datePicker");
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
                    update(mPrefs, position);
                    return false;
                }
                return false;
            }
        });
        holder.followUpAction.setText(myData.getFollowUpAction());

        holder.obs_photo = (ImageView) view.findViewById(R.id.obs_photo);
        Bitmap bitmap = BitmapFactory.decodeFile(myData.getFile().getAbsolutePath());
        Bitmap reducedBitmap = getResizedBitmap(bitmap, 200);
        holder.obs_photo.setImageBitmap(reducedBitmap);


        return view;
    }
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        notifyDataSetChanged();
//        .setText(sdf.format(myCalendar.getTime()));
    }



    protected void update(SharedPreferences preferences, int position) {
        Log.i(TAG, "update shared preference");
        // and store current object into shared preference:
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson1 = new Gson();
        String JsonObject = gson1.toJson(arrayList, listOfObjects); // Here list is your List<CUSTOM_CLASS> object
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