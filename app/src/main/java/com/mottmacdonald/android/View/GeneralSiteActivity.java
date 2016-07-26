package com.mottmacdonald.android.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mottmacdonald.android.Adapter.GeneralExpandableAdapter;
import com.mottmacdonald.android.Apis.SaveFormApi;
import com.mottmacdonald.android.Data.DataShared;
import com.mottmacdonald.android.Models.AllTemplatesModel;
import com.mottmacdonald.android.Models.FormsDataModel;
import com.mottmacdonald.android.Models.ItemData;
import com.mottmacdonald.android.Models.SaveFormItemModel;
import com.mottmacdonald.android.Models.SaveFormItemObservationModel;
import com.mottmacdonald.android.Models.SectionData;
import com.mottmacdonald.android.Models.SectionsDataModel;
import com.mottmacdonald.android.Models.TemplatesData;
import com.mottmacdonald.android.Models.obs_form_DataModel;
import com.mottmacdonald.android.MyApplication;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Report;
import com.mottmacdonald.android.ReportDao;
import com.mottmacdonald.android.Utils.DeviceUtils;
import com.mottmacdonald.android.Utils.FileUtil;
import com.mottmacdonald.android.Utils.PhotoReSize;
import com.mottmacdonald.android.View.CustomView.DrawView;
import com.youxiachai.ajax.ICallback;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/15 1:55
 * 备注：
 */
public class GeneralSiteActivity extends BaseActivity {
    private static final String TAG = "GeneralSiteActivity16";
    private static final String PREF_NAME = "myNameList";


    private static final String CONTRACT_NAME = "contract_name";
    private static final String CONTRACT_ID = "contract_id";
    private static final String FORM_INFO_ID = "form_info_id";


    public static final String PREFS_NAME = "DataModel";
    Type listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {
    }.getType();

    public static final int TAKE_PHOTO = 1;


    private String contractName;
    private String contractId;
    private String formInfoId;
    private ExpandableListView listView;
    private List<TemplatesData> templatesDatas;
    private List<SectionsDataModel> sections = new ArrayList<>();
    private List<SectionData> groupDatas = new ArrayList<>();
    private List<List<ItemData>> childrenDatas = new ArrayList<>();
    private GeneralExpandableAdapter mAdapter;
    private AQuery footAq;
    private RelativeLayout pmSign, contractorSign, etSign, iecSign;
    private LinearLayout pm_layout, contractor_layout, et_layout, iec_layout, Confirmation_layout;
    private TextView pmTitle, contractorTitle, etTitle, iecTitle;
    private LinearLayout signLeftTitleLayout;
    private DrawView pmView, etView, contractorView, iecView;
    private EditText PM, ET, Contractor, IEC;

    public static void start(Context context, String contractName, String contractId, String formInfoId) {
        Intent intent = new Intent(context, GeneralSiteActivity.class);
        intent.putExtra(CONTRACT_NAME, contractName);
        intent.putExtra(CONTRACT_ID, contractId);
        intent.putExtra(FORM_INFO_ID, formInfoId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_general_site);



        initDatas();
        initViews();
        Log.i(TAG, "searchData ");
        searchData();
    }

    private void initDatas() {
        if (getIntent().getExtras() != null) {
            contractName = getIntent().getExtras().getString(CONTRACT_NAME);
            contractId = getIntent().getExtras().getString(CONTRACT_ID);
            formInfoId = getIntent().getExtras().getString(FORM_INFO_ID);
        }
        templatesDatas = new ArrayList<>();
        AllTemplatesModel templatesModel = JSON.parseObject(DataShared.getTemplatesData(), AllTemplatesModel.class);
        templatesDatas = templatesModel.data;
        for (TemplatesData data : templatesDatas) {
            for (FormsDataModel formData : data.forms) {
                if (contractId.equals(formData.contract_id)) {
                    sections = formData.sections.sections;
                }
            }
        }
        for (int i = 0; i < sections.size(); i++) {
            groupDatas.add(sections.get(i).section);
            childrenDatas.add(sections.get(i).items);
        }
    }

    private void initViews() {
        mAQuery.id(R.id.title_text).text(contractName + "-GENERAL SITE ACTIVITIES");
//        mAdapter = new GeneralAdapter(mContext, sections);
        mAdapter = new GeneralExpandableAdapter(mContext, groupDatas, childrenDatas);
        listView = (ExpandableListView) findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
        listView.setGroupIndicator(null);
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
//        mAQuery.id(R.id.listview).adapter(adapter);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.item_general_head, null);
        View footView = LayoutInflater.from(mContext).inflate(R.layout.item_general_foot_02, null);
        footAq = new AQuery(footView);
        footAq.id(R.id.save_btn).clicked(clickListener);
        pmSign = (RelativeLayout) footView.findViewById(R.id.pm_sign);
        contractorSign = (RelativeLayout) footView.findViewById(R.id.contractor_sign);
        etSign = (RelativeLayout) footView.findViewById(R.id.et_sign);
        iecSign = (RelativeLayout) footView.findViewById(R.id.iec_sign);
        pmTitle = (TextView) footView.findViewById(R.id.pm_title);
        contractorTitle = (TextView) footView.findViewById(R.id.contractor_title);
        etTitle = (TextView) footView.findViewById(R.id.et_title);
        iecTitle = (TextView) footView.findViewById(R.id.iec_title);
        signLeftTitleLayout = (LinearLayout) footView.findViewById(R.id.left_title_layout);

        Confirmation_layout = (LinearLayout) footView.findViewById(R.id.confirmation_layout);

        //divide each signature into difference column;
        pm_layout = (LinearLayout) footView.findViewById(R.id.pm_layout);
        contractor_layout = (LinearLayout) footView.findViewById(R.id.contractor_layout);
        et_layout = (LinearLayout) footView.findViewById(R.id.et_layout);
        iec_layout = (LinearLayout) footView.findViewById(R.id.iec_layout);

        //auto get the name of private EditText PM,ET,Contractor,IEC;

        //<<
        PM = (EditText) footView.findViewById(R.id.pm);
        ET = (EditText) footView.findViewById(R.id.et);
        Contractor = (EditText) footView.findViewById(R.id.contractor);
        IEC = (EditText) footView.findViewById(R.id.iec);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        Log.i(TAG, "pm; " + sharedPreferences.getString("pm", ""));

        PM.setText(sharedPreferences.getString("pm", "error"));
        ET.setText(sharedPreferences.getString("et", ""));
        Contractor.setText(sharedPreferences.getString("contractor", ""));
        IEC.setText(sharedPreferences.getString("iec", ""));
        //special , date value
        EditText editText_date = (EditText) footView.findViewById(R.id.date);
        editText_date.setText(sharedPreferences.getString("date", ""));


        //>>
        listView.addHeaderView(headView);
        listView.addFooterView(footView);


    }


    private DrawView getDrawView(int w, int h) {
//        DrawView drawView = new DrawView(mContext, pmSign.getWidth(), pmSign.getHeight());
        DrawView drawView = new DrawView(mContext, w, h);
        drawView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    listView.requestDisallowInterceptTouchEvent(false);
                } else {
                    listView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        return drawView;
    }

    private void setSignShow(Report report) {
        pmSign.setVisibility(View.GONE);

        Confirmation_layout.invalidate();

        if (!TextUtils.isEmpty(report.getPm())) {


            pm_layout.setVisibility(View.VISIBLE);
            Log.i(TAG, "report.getPm() " + report.getPm());

            pmSign.setVisibility(View.VISIBLE);
            pmTitle.setVisibility(View.VISIBLE);

            pmSign.getViewTreeObserver();
            Log.i(TAG, "pmSign.getWidth(): " + pmSign.getWidth() + " pmSign.getHeight(): " + pmSign.getHeight());

            pmView = getDrawView(300, 300);
//            Log.i(TAG, "pmSign.getWidth(): " + pmSign.getWidth() + " pmSign.getHeight(): " + pmSign.getHeight());
            pmSign.addView(pmView);

//            ViewTreeObserver vto1 = pmSign.getViewTreeObserver();
            ViewTreeObserver vto1 = pmSign.getViewTreeObserver();
            vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    Log.i(TAG, "onGlobalLayout ");
                    pmSign.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    pmView = getDrawView(pmSign.getWidth(), pmSign.getHeight());
                    Log.i(TAG, "pmSign.getWidth(): " + pmSign.getWidth() + " pmSign.getHeight(): " + pmSign.getHeight());
                    pmSign.addView(pmView);
                    GeneralSiteActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "pmSign.getWidth(): " + pmSign.getWidth() + " pmSign.getHeight(): " + pmSign.getHeight());
                            pmSign.invalidate();
                        }
                    });
                }
            });
            pmSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onclick");

                    Log.i(TAG, "pmSign.getWidth(): " + pmSign.getWidth() + " pmSign.getHeight(): " + pmSign.getHeight());

                }
            });
            GeneralSiteActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "set Visibility");
                    Confirmation_layout.invalidate();
                    Confirmation_layout.setVisibility(View.VISIBLE);
                    pm_layout.setVisibility(View.VISIBLE);
                    pmSign.setVisibility(View.VISIBLE);
                    pmTitle.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Log.i(TAG, "TextUtils.isEmpty(report.getPm()));  " + TextUtils.isEmpty(report.getPm()));

        }
        if (!TextUtils.isEmpty(report.getContractor())) {
            contractor_layout.setVisibility(View.VISIBLE);
            Log.i(TAG, "report.getContractor() " + report.getContractor());

            contractorSign.setVisibility(View.VISIBLE);
            contractorTitle.setVisibility(View.VISIBLE);
            ViewTreeObserver vto2 = contractorSign.getViewTreeObserver();
            vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    contractorSign.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    contractorView = getDrawView(contractorSign.getWidth(), contractorSign.getHeight());
                    Log.i(TAG, "contractorSign.getWidth(): " + contractorSign.getWidth() + " contractorSign.getHeight(): " + contractorSign.getHeight());

                    contractorSign.addView(contractorView);
                }
            });
        }
        if (!TextUtils.isEmpty(report.getEt())) {
            et_layout.setVisibility(View.VISIBLE);
            Log.i(TAG, "report.getEt() " + report.getEt());

            etSign.setVisibility(View.VISIBLE);
            etTitle.setVisibility(View.VISIBLE);
            ViewTreeObserver vto3 = etSign.getViewTreeObserver();
            vto3.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    etSign.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    etView = getDrawView(etSign.getWidth(), etSign.getHeight());
                    Log.i(TAG, "etSign.getWidth(): " + etSign.getWidth() + " etSign.getHeight(): " + etSign.getHeight());

                    etSign.addView(etView);
                }
            });
        }
        if (!TextUtils.isEmpty(report.getIec())) {
            iec_layout.setVisibility(View.VISIBLE);
            Log.i(TAG, "report.getIec() " + report.getIec());

            iecSign.setVisibility(View.VISIBLE);
            iecTitle.setVisibility(View.VISIBLE);
            ViewTreeObserver vto4 = iecSign.getViewTreeObserver();
            vto4.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    iecSign.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    iecView = getDrawView(iecSign.getWidth(), iecSign.getHeight());
                    iecSign.addView(iecView);
                }
            });
        }

        if (!TextUtils.isEmpty(report.getPm()) || !TextUtils.isEmpty(report.getContractor()) ||
                !TextUtils.isEmpty(report.getEt()) || !TextUtils.isEmpty(report.getIec())) {
            signLeftTitleLayout.setVisibility(View.VISIBLE);
            Confirmation_layout.setVisibility(View.VISIBLE);
        }


    }

    private void searchData() {
        String noteText = DeviceUtils.getCurrentDate();
        Log.i(TAG, "searchData");

        // Query 类代表了一个可以被重复执行的查询
        Query query = MyApplication.getInstance().getDaoSession().getReportDao().queryBuilder()
                .where(ReportDao.Properties.SaveDate.eq(noteText))
                .orderAsc(ReportDao.Properties.Date)
                .build();
        // 查询结果以 List 返回
        List<Report> reports = query.list();
        if (reports.size() > 0) {
            Log.i(TAG, "report.size is >0");
            setSignShow(reports.get(0));
        } else {
            Log.i(TAG, "report.size is !>0");
        }

        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
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
                mAdapter.setImage(saveFile.getAbsolutePath());
            }
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (pmSign.getVisibility() == View.VISIBLE) {
                if (!pmView.getDrawStatus()) {
                    showToast("Please signature PM’s Representative first");
                    return;
                }
            }
            if (contractorSign.getVisibility() == View.VISIBLE) {
                if (!contractorView.getDrawStatus()) {
                    showToast("Please signature Contractor’s Representative first");
                    return;
                }
            }
            if (etSign.getVisibility() == View.VISIBLE) {
                if (!etView.getDrawStatus()) {
                    showToast("Please signature ET’s Representative first");
                    return;
                }
            }
            if (iecSign.getVisibility() == View.VISIBLE) {
                if (!iecView.getDrawStatus()) {
                    showToast("Please signature IEC’s Representative first");
                    return;
                }
            }
            saveFormItem();
        }
    };

    private void saveFormItem() {
        showProgress("Saving");
        Log.i(TAG, "saveFormItem ; ");

        List<Map<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < groupDatas.size(); i++) {
            for (int j = 0; j < childrenDatas.get(i).size(); j++) {
                ItemData data = childrenDatas.get(i).get(j);
                Map<String, String> map = new HashMap<>();


                map.put("forminfo_id", formInfoId);
                map.put("item_id ", data.item_id);
                map.put("close_out", !TextUtils.isEmpty(mAdapter.getCloseOutData().get(i).get(j)) ?
                        mAdapter.getCloseOutData().get(i).get(j) : "N/A");
                map.put("answer_id", mAdapter.getAnswerId(i, j));
                map.put("remarks", mAdapter.getRemarkData().get(i).get(j));


                Log.i(TAG, "forminfo_id " + map.get("forminfo_id"));
                Log.i(TAG, "item_id " + map.get("item_id"));
                Log.i(TAG, "close_out " + map.get("close_out"));
                Log.i(TAG, "answer_id " + map.get("answer_id"));
                Log.i(TAG, "remarks " + map.get("remarks"));
                mapList.add(map);
            }
        }

        //SaveFormApi.saveFormWeather
        for (int i = 0; i < mapList.size(); i++) {
            Log.i(TAG, "i " + i);

            Map<String, String> map = mapList.get(i);
            SaveFormApi.saveFormItem_OnebyOne(mContext, map, new ICallback<SaveFormItemModel>() {
                @Override
                public void onSuccess(SaveFormItemModel saveFormItemModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                    dismissProgress();
                    if (saveFormItemModel != null) {
                        if (saveFormItemModel.status == 1) {
                            Log.i(TAG, "saveFormItemModel.status ;" + saveFormItemModel.status);
                            Log.i(TAG, "saveFormItemModel.formitem_id ;" + saveFormItemModel.formitem_id);
                            showToast("Save Form Complete");
                            saveFormItemObs(saveFormItemModel.formitem_id);
                            MyApplication.getInstance().removeActivityExcept(MainActivity.class);
                        } else {
                            showToast("Save failure");
                        }
                    } else {
                        showRequestFailToast();
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }


//        SaveFormApi.saveFormItem(mContext, mapList, new ICallback<SaveFormItemModel>() {
//            @Override
//            public void onSuccess(SaveFormItemModel saveFormItemModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
//                dismissProgress();
//                if (saveFormItemModel != null) {
//                    if (saveFormItemModel.status == 1) {
//                        Log.i(TAG,"saveFormItemModel.status ;"+saveFormItemModel.status );
//                        Log.i(TAG,"saveFormItemModel.formitem_id ;"+saveFormItemModel.formitem_id );
//                        showToast("Save Form Complete");
//                        saveFormItemObs(saveFormItemModel.formitem_id );
//                        MyApplication.getInstance().removeActivityExcept(MainActivity.class);
//                    } else {
//                        showToast("Save failure");
//                    }
//                } else {
//                    showRequestFailToast();
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });


    }

    private void saveFormItemObs(String formitem_id) {
        System.out.println("保存图片表");
        SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);// use JSOnM format to store the object (obs_form)
        String json = mPrefs.getString("MyList", "");
        Gson gson = new Gson();
        if (!json.isEmpty()) {
            Toast.makeText(mContext, "json is ! empty", Toast.LENGTH_LONG).show();
            Log.i("checking", "json " + json.toString());
            ArrayList<obs_form_DataModel> list = gson.fromJson(json,listOfObjects);
            obs_form_DataModel dataModel = list.get(0);
            Log.i(TAG,"dataModel.getItemNo(); "+dataModel.getItemNo());
            Log.i(TAG,"dataModel.getPhotoCache(); "+dataModel.getPhotoCache());

        } else {
            Toast.makeText(mContext, "json is empty", Toast.LENGTH_LONG).show();
        }

//        showProgress("Saving");
        ///storage/emulated/0/Pictures/PokemonGO/IMG_2016-07-25-20033902.png
//        File file = new File("/storage/emulated/0/Android/data/com.mottmacdonald.android/cache/mott_201605021843579470.jpg");
        File file = new File("/storage/emulated/0/Pictures/PokemonGO/IMG_2016-07-25-20033902.png");
        SaveFormApi.saveFormItemObservation(mContext, formitem_id, file, "", "", "", new ICallback<SaveFormItemObservationModel>() {
            @Override
            public void onSuccess(SaveFormItemObservationModel resultData, Enum<?> anEnum, AjaxStatus ajaxStatus) {
//                dismissProgress();
                Log.i(TAG, "saveFormItemObs successful");
                showToast("Save Form Complete");
                MyApplication.getInstance().removeActivityExcept(MainActivity.class);
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError");
                Log.i(TAG, "i:" + i + " string: " + s);

            }
        });
    }

//    private void saveFormItemObs(String formitem_id ){
//        System.out.println("保存图片表");
//        showProgress("Saving");
//
//        File file = new File("/storage/emulated/0/Android/data/com.mottmacdonald.android/cache/mott_201605021843579470.jpg");
//        SaveFormApi.saveFormItemObservation(mContext, "28", file, "", "", "", new ICallback<SaveFormItemObservationModel>() {
//            @Override
//            public void onSuccess(SaveFormItemObservationModel resultData, Enum<?> anEnum, AjaxStatus ajaxStatus) {
//                dismissProgress();
//                showToast("Save Form Complete");
//                MyApplication.getInstance().removeActivityExcept(MainActivity.class);
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
//    }
//


}
