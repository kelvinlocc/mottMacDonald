package com.mottmacdonald.android.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.mottmacdonald.android.Data.MySharedPref_App;
import com.mottmacdonald.android.Models.AllTemplatesModel;
import com.mottmacdonald.android.Models.FormsDataModel;
import com.mottmacdonald.android.Models.ItemData;
import com.mottmacdonald.android.Models.SaveFormInfoModel;
import com.mottmacdonald.android.Models.SaveFormItemModel;
import com.mottmacdonald.android.Models.SaveFormItemObservationModel;
import com.mottmacdonald.android.Models.SaveFormWeatherModel;
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
import com.mottmacdonald.android.Weather;
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
    private String TAG = this.getClass().getName();
    private static final String PREF_NAME = "myNameList";


    private static final String CONTRACT_NAME = "contract_name";
    private static final String CONTRACT_ID = "contract_id";
    private static final String FORM_INFO_ID = "form_info_id";


    public static final String PREFS_NAME = "DataModel";
    Type listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {
    }.getType();
    ArrayList<obs_form_DataModel> save_obs_form_dataModels;
    ArrayList<String> ArrayList;

    public static final int TAKE_PHOTO = 1;

    MySharedPref_App mySharedPref_app;
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
    private static Context mContext;

    TextView info;
    TextView report, weather, general_site;

    public static void start(Context context, String contractName, String contractId, String formInfoId) {
        Intent intent = new Intent(context, GeneralSiteActivity.class);
        intent.putExtra(CONTRACT_NAME, contractName);
        intent.putExtra(CONTRACT_ID, contractId);
        intent.putExtra(FORM_INFO_ID, formInfoId);
        context.startActivity(intent);
        mContext = context;
    }

    boolean pm, con, et, iec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_general_site);
        ArrayList = new ArrayList<String>();
        mySharedPref_app = new MySharedPref_App();
        initDatas();
        initViews();

        pm = con = et = iec = true;
        searchData();
    }


    private void searchData() {
//        String noteText = DeviceUtils.getCurrentDate();
//        Log.i(TAG, "searchData");
//
//        // Query 类代表了一个可以被重复执行的查询
//        Query query = MyApplication.getInstance().getDaoSession().getReportDao().queryBuilder()
//                .where(ReportDao.Properties.SaveDate.eq(noteText))
//                .orderAsc(ReportDao.Properties.Date)
//                .build();
//        // 查询结果以 List 返回
//        List<Report> reports = query.list();
//        if (reports.size() > 0) {
//            Log.i(TAG, "report.size is >0");
//            setSignShow(reports.get(0));
//
//        } else {
//            Log.i(TAG, "report.size is !>0");
//        }
        setSignShow(getReport());

        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    private void initDatas() {
//        if (getIntent().getExtras() != null) {
//            contractName = getIntent().getExtras().getString(CONTRACT_NAME);
//            contractId = getIntent().getExtras().getString(CONTRACT_ID);
//            formInfoId = getIntent().getExtras().getString(FORM_INFO_ID);
//        }
        contractName =myPref.getContractNumber(mContext);
        contractId =myPref.getCurrentContractId(mContext);



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
        SharedPreferences myPreference_UniqueCode = getSharedPreferences("uniqueCode", MODE_PRIVATE);
        String head = myPreference_UniqueCode.getString("code_head", "no head");

        for (int i = 0; i < sections.size(); i++) {
            groupDatas.add(sections.get(i).section);
            childrenDatas.add(sections.get(i).items);
            for (int j = 0; j < childrenDatas.get(i).size(); j++) {
                Log.i(TAG, "childrenDatas.get(i) i+j" + i + "+" + j);
                Log.i(TAG, "childrenDatas.get(i).get(j).item_id; " + childrenDatas.get(i).get(j).item_id);
                String string = head + Integer.toString(i) + Integer.toString(j);

                ArrayList.add(string);


            }

        }
        MySharedPref_App mySharedPref_app = new MySharedPref_App();

        //<temp
        String string = mySharedPref_app.getString(mContext, "uniqueCode", "code_head") + ".txt";
        mySharedPref_app.write_StringArrayList(ArrayList, mContext, string);
        mySharedPref_app.read_StringArrayList(mContext, string);
        //temp>
    }

    LinearLayout sign_layout;

    private void initViews() {
        mAQuery.id(R.id.title_text).text(contractName + "-GENERAL SITE ACTIVITIES");
        mAdapter = new GeneralExpandableAdapter(mContext, groupDatas, childrenDatas);
        listView = (ExpandableListView) findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
        listView.setGroupIndicator(null);
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
        View headView = LayoutInflater.from(mContext).inflate(R.layout.item_general_head, null);
        View footView = LayoutInflater.from(mContext).inflate(R.layout.item_general_foot, null);
        footAq = new AQuery(footView);
        footAq.id(R.id.save_btn).clicked(clickListener);
        // // TODO: 8/8/2016  pmSign;
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

        //**
        sign_layout = (LinearLayout) footView.findViewById(R.id.sign_layout);
        //**
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
        EditText editText_date_02 = (EditText) footView.findViewById(R.id.date_02);
        EditText editText_date_03 = (EditText) footView.findViewById(R.id.date_03);
        EditText editText_date_04 = (EditText) footView.findViewById(R.id.date_04);

        editText_date.setText(sharedPreferences.getString("date", ""));
        editText_date_02.setText(sharedPreferences.getString("date", ""));
        editText_date_03.setText(sharedPreferences.getString("date", ""));
        editText_date_04.setText(sharedPreferences.getString("date", ""));

        //>>
        listView.addHeaderView(headView);
        listView.addFooterView(footView);
        pm_layout.setVisibility(View.GONE);
        contractor_layout.setVisibility(View.GONE);
        et_layout.setVisibility(View.GONE);
        iec_layout.setVisibility(View.GONE);


        //submission status:
        info = (TextView) findViewById(R.id.info);
        String string;
        if (isConnected()) {
            string = "connected";
        } else {
            string = "discounted";

        }
        string = string + " ID: " + getFormId();
        info.setText(string);
        report = (TextView) findViewById(R.id.report_status);
        if (report_submission) {
            report.setText("done");
        }
        weather = (TextView) findViewById(R.id.weather_status);
        if (weather_submission) {
            weather.setText("done");
        }
        general_site = (TextView) findViewById(R.id.general_site_status);
        if (general_site_submission) {
            general_site.setText("done");
        }

    }

    private DrawView getDrawView(int w, int h) {
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
        Log.i(TAG, "setSignShow: ");
        if (!TextUtils.isEmpty(report.getPm())) {
            pm_layout.setVisibility(View.VISIBLE);
            Log.i(TAG, "report.getPm() " + report.getPm());
            pmSign.setVisibility(View.VISIBLE);
            pmTitle.setVisibility(View.VISIBLE);
            ViewTreeObserver vt01 = pmSign.getViewTreeObserver();
            vt01.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    Log.i(TAG, "onPreDraw: ");
                    Log.i(TAG, "onPreDraw: " + pmSign.toString() + ": width: " + pmSign.getWidth());
                    pmSign.getViewTreeObserver().removeOnPreDrawListener(this);
                    pmView = getDrawView(pmSign.getWidth(), pmSign.getHeight());
                    pmSign.addView(pmView);
                    return true;
                }
            });
        }

        if (!TextUtils.isEmpty(report.getContractor())) {
            contractor_layout.setVisibility(View.VISIBLE);
            Log.i(TAG, "report.getContractor() " + report.getContractor());
            contractorSign.setVisibility(View.VISIBLE);
            contractorTitle.setVisibility(View.VISIBLE);
            ViewTreeObserver vt02 = contractorSign.getViewTreeObserver();
            vt02.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    Log.i(TAG, "onPreDraw: ");
                    Log.i(TAG, "onPreDraw: " + contractorSign.toString() + ": width: " + contractorSign.getWidth());
                    contractorSign.getViewTreeObserver().removeOnPreDrawListener(this);
                    contractorView = getDrawView(contractorSign.getWidth(), contractorSign.getHeight());
                    contractorSign.addView(contractorView);
                    return true;
                }
            });
        }
        if (!TextUtils.isEmpty(report.getEt())) {
            et_layout.setVisibility(View.VISIBLE);
            Log.i(TAG, "report.getEt() " + report.getEt());
            etSign.setVisibility(View.VISIBLE);
            etTitle.setVisibility(View.VISIBLE);
            ViewTreeObserver vt03 = etSign.getViewTreeObserver();
            vt03.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    Log.i(TAG, "onPreDraw: ");
                    Log.i(TAG, "onPreDraw: " + etSign.toString() + ": width: " + etSign.getWidth());
                    etSign.getViewTreeObserver().removeOnPreDrawListener(this);
                    etView = getDrawView(etSign.getWidth(), etSign.getHeight());
                    etSign.addView(etView);
                    return true;
                }
            });
        }
        if (!TextUtils.isEmpty(report.getIec())) {
            iec_layout.setVisibility(View.VISIBLE);
            Log.i(TAG, "report.getIec() " + report.getIec());
            iecSign.setVisibility(View.VISIBLE);
            iecTitle.setVisibility(View.VISIBLE);
            ViewTreeObserver vt04 = iecSign.getViewTreeObserver();
            vt04.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    Log.i(TAG, "onPreDraw: ");
                    Log.i(TAG, "onPreDraw: " + iecSign.toString() + ": width: " + iecSign.getWidth());
                    iecSign.getViewTreeObserver().removeOnPreDrawListener(this);
                    iecView = getDrawView(iecSign.getWidth(), iecSign.getHeight());
                    iecSign.addView(iecView);
                    return true;
                }
            });
        }

        if (!TextUtils.isEmpty(report.getPm()) || !TextUtils.isEmpty(report.getContractor()) ||
                !TextUtils.isEmpty(report.getEt()) || !TextUtils.isEmpty(report.getIec())) {
            Toast.makeText(GeneralSiteActivity.this, "Exception!!!", Toast.LENGTH_SHORT).show();
            signLeftTitleLayout.setVisibility(View.VISIBLE);
            Confirmation_layout.setVisibility(View.VISIBLE);
        }

        Log.i(TAG, "report.getPm() "+report.getPm());

        Report report2 = getReport();
        Log.i(TAG, "onClick: report2.getpm "+report2.getPm());
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
            boolean gate = false;
            if (pmSign.getVisibility() == View.VISIBLE) {
                if (pmView.getDrawStatus()) {
                    gate = true;
                }
            }
            if (contractorSign.getVisibility() == View.VISIBLE) {
                if (contractorView.getDrawStatus()) {
                    gate = true;
                }
            }
            if (etSign.getVisibility() == View.VISIBLE) {
                if (etView.getDrawStatus()) {
                    gate = true;
                }
            }
            if (iecSign.getVisibility() == View.VISIBLE) {
                if (iecView.getDrawStatus()) {
                    gate = true;
                }
            }

            if (gate) {
                if (isConnected())
                    //// TODO: 8/22/2016  checking
                {

                    if(report_submission){
                        if(weather_submission){
                            saveFormItem();
                        }
                        else {
                            saveWeather();
                        }
                    }
                    else {
                        saveReport();
                    }
                    Toast.makeText(GeneralSiteActivity.this, "connected", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(GeneralSiteActivity.this, "Device is offline, please try later", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(GeneralSiteActivity.this, "at least one signature required!", Toast.LENGTH_SHORT).show();
            }

        }
    };


    private void saveReport() {
        showProgress();
        Log.i(TAG, "saveReport: ");
        Log.i(TAG, "saveFormInfo: formid: " + getFormId());
        String formId = getFormId();
        Report report = getReport();
        String date = report.getInspectionDate() + " " + report.getTime();

        SaveFormApi.saveFormInfo(mContext, formId, date, report.getEnvironmentalPermitNo(), report.getSiteLocation(), report.getPm(), report.getEt(),
                report.getContractor(), report.getIec(), report.getOthers(), "", "", new ICallback<SaveFormInfoModel>() {
                    @Override
                    public void onSuccess(SaveFormInfoModel saveFormInfoModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                        dismissProgress();
                        if (saveFormInfoModel != null) {
                            Log.i(TAG, "onSuccess: ");
                            // go to save weather
                            myPref.setFormInfoId(saveFormInfoModel.forminfo_id,mContext);

                            // set report submission status to true!
                            myPref.setReport_submission(true, mContext);
                            if (!weather_submission){
                                saveWeather();
                            }
                            if(!general_site_submission){
                                saveFormItem();
                            }
                        } else {
                            showRequestFailToast();
                            Log.i(TAG, "save failed" + "");
                        }

                    }
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(GeneralSiteActivity.this, "report save error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveWeather() {
        Log.i(TAG, "saveWeather: formInfoId: "+formInfoId);
        formInfoId = myPref.getFormInfoId(mContext);
        Log.i(TAG, "saveWeather: formInfoId: "+formInfoId);
        Weather weather =getWeather();
        showProgress();
        SaveFormApi.saveFormWeather(mContext, formInfoId, weather.getCondition(), weather.getTemperature(),weather.getHumidity(),
                weather.getWind(), weather.getRemarks(), new ICallback<SaveFormWeatherModel>() {
                    @Override
                    public void onSuccess(SaveFormWeatherModel saveFormWeatherModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                        dismissProgress();
                        if (saveFormWeatherModel != null) {
//                            GeneralSiteActivity.start(mContext, contractName, contractId, formInfoId);
//                            Log.i(TAG, "create the unique ID for shared preference: contractName, contractId, formInfoId: " + contractName + "," + contractId + "," + formInfoId);
//                            String head = contractName + contractId + formInfoId;
//                            Log.i(TAG, "head " + head);
//                            mySharedPref_app.setHead(mContext, head);

                            myPref.setWeather_submission(true, mContext);
                        } else {
                            showRequestFailToast();
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
    }


    private void saveFormItem() {
        myPref.setGeneral_site_submission(true,mContext);
        formInfoId = myPref.getFormInfoId(mContext);
        showProgress("Saving");
        Log.i(TAG, "saveFormItem; ");
        SharedPreferences preferences = getSharedPreferences("uniqueCode", MODE_PRIVATE);
        String head = preferences.getString("code_head", "no head");
        String tail = preferences.getString(head, "no tail");

        Log.i(TAG, "create the unique ID for shared preference: head,tail " + head + "," + tail);
        String PREFS_NAME = head + tail;

        SharedPreferences preferences1 = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = preferences1.getString("MyList", "");
        Gson gson = new Gson();
        if (!json.isEmpty()) {
            Log.i(TAG, "json is !empty");
            listOfObjects = new TypeToken<ArrayList<obs_form_DataModel>>() {
            }.getType();

            save_obs_form_dataModels = gson.fromJson(json, listOfObjects);

        } else {
            Log.i(TAG, "json is empty");
        }

        //// TODO: get the item.id
        List<Map<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < groupDatas.size(); i++) {
            for (int j = 0; j < childrenDatas.get(i).size(); j++) {
                ItemData data = childrenDatas.get(i).get(j);
                Map<String, String> map = new HashMap<>();


                map.put("forminfo_id", formInfoId);
                map.put("item_id", data.item_id);
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
            Log.i(TAG, "mapList i " + i);

            final Map<String, String> map = mapList.get(i);
            SaveFormApi.saveFormItem_OnebyOne(mContext, map, new ICallback<SaveFormItemModel>() {
                @Override
                public void onSuccess(SaveFormItemModel saveFormItemModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                    dismissProgress();
                    if (saveFormItemModel != null) {
                        if (saveFormItemModel.status == 1) {
                            Log.i(TAG, "saveFormItemModel.status ;" + saveFormItemModel.status);
                            Log.i(TAG, "onSuccess:  formitem_id;" + saveFormItemModel.formitem_id);
                            Log.i(TAG, "onSuccess:  item_id; " + map.get("item_id"));
                            checker_save_obs_form(saveFormItemModel.formitem_id, map.get("item_id"));
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
                    Log.i(TAG, "onError: i,s " + i + s);
                    Log.i(TAG, "failed to save and synchronise");
                }
            });
        }
        if (mapList.size() == 0) {
            Toast.makeText(GeneralSiteActivity.this, "no data", Toast.LENGTH_SHORT).show();
            dismissProgress();
        }
    }

    public void checker_save_obs_form(String formItem_id, String item_id) {
        // set head to constant value```
        String KEY = mySharedPref_app.getHead(mContext) + item_id;
        ArrayList<obs_form_DataModel> temp = mySharedPref_app.getArrayList(KEY, mContext);
        for (int j = 0; j < temp.size(); j++) {
            Log.i(TAG, "temp.getRecommedation " + temp.get(j).getRecommedation());
            saveObsFrom(formItem_id, temp.get(j).getFile(), temp.get(j).getRecommedation(), temp.get(j).getToBeRemediated_before(), temp.get(j).getFollowUpAction());
        }
    }

    private void saveObsFrom(String formitem_id, File file, String recommendation, String remediated,
                             String followup) {
        SaveFormApi.saveFormItemObservation(mContext, formitem_id, file, recommendation, remediated, followup, new ICallback<SaveFormItemObservationModel>() {
            @Override
            public void onSuccess(SaveFormItemObservationModel resultData, Enum<?> anEnum, AjaxStatus ajaxStatus) {
//                dismissProgress();
                Log.i(TAG, "saveFormItemObs successful");
                showToast("Save Form Complete");
                myPref.setGeneral_site_submission(true,mContext);
                MyApplication.getInstance().removeActivityExcept(MainActivity.class);
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError");
                Log.i(TAG, "i:" + i + " string: " + s);

            }
        });

    }
}
