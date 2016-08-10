package com.mottmacdonald.android.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.androidquery.AQuery;
import com.google.gson.reflect.TypeToken;
import com.mottmacdonald.android.Adapter.GeneralExpandableAdapter;
import com.mottmacdonald.android.Data.DataShared;
import com.mottmacdonald.android.Data.MySharedPref_App;
import com.mottmacdonald.android.Models.AllTemplatesModel;
import com.mottmacdonald.android.Models.FormsDataModel;
import com.mottmacdonald.android.Models.ItemData;
import com.mottmacdonald.android.Models.SectionData;
import com.mottmacdonald.android.Models.SectionsDataModel;
import com.mottmacdonald.android.Models.TemplatesData;
import com.mottmacdonald.android.Models.obs_form_DataModel;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.View.CustomView.DrawView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class testActivity extends BaseActivity {
    private String TAG = this.getClass().getName();
    private static final String PREF_NAME = "myNameList";


    private static final String CONTRACT_NAME = "contract_name";
    private static final String CONTRACT_ID = "contract_id";
    private static final String FORM_INFO_ID = "form_info_id";


    public static final String PREFS_NAME = "DataModel";
    Type listOfObjects = new TypeToken<java.util.ArrayList<obs_form_DataModel>>() {
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

    public static void start(Context context, String contractName, String contractId, String formInfoId) {
        Intent intent = new Intent(context, testActivity.class);
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
//        initView();
        initDatas();





    }
    private void initViews() {
        mAQuery.id(R.id.title_text).text(contractName + "-GENERAL SITE ACTIVITIES");

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

        //<
        mAdapter = new GeneralExpandableAdapter(mContext, groupDatas, childrenDatas);
        listView = (ExpandableListView) findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
        listView.setGroupIndicator(null);
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
        //>

        //>>
        listView.addHeaderView(headView);
        listView.addFooterView(footView);

        pm_layout.setVisibility(View.GONE);
        contractor_layout.setVisibility(View.GONE);
        et_layout.setVisibility(View.GONE);
        iec_layout.setVisibility(View.GONE);
        pmSign.setVisibility(View.GONE);

//        postRunnable_addGlobalLayout();
        postRunnable_addGlobalLayout_02();
        addOnPreDrawListener(pmSign);
        addOnPreDrawListener(contractorSign);
        addOnPreDrawListener(etSign);
        addOnPreDrawListener(iecSign);

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
//            saveFormItem();
        }
    };

    public void initView(){

        pmSign = (RelativeLayout) findViewById(R.id.pm_sign);
        contractorSign = (RelativeLayout) findViewById(R.id.contractor_sign);
        etSign = (RelativeLayout) findViewById(R.id.et_sign);
        iecSign = (RelativeLayout) findViewById(R.id.iec_sign);
        pm_layout = (LinearLayout) findViewById(R.id.pm_layout);
        contractor_layout = (LinearLayout) findViewById(R.id.contractor_layout);
        et_layout = (LinearLayout) findViewById(R.id.et_layout);
        iec_layout = (LinearLayout) findViewById(R.id.iec_layout);
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

        pm=con=et=iec = true;

        initViews();
        setSignShow_02();
//        mySharedPref_app.output_StringArrayList(mContext,string);
    }

    private void setSignShow_02() {
        if (pm) {
            pm_layout.setVisibility(View.VISIBLE);
            pmSign.setVisibility(View.VISIBLE);
//            pmSign.getViewTreeObserver();
            outputWidthHeight(pmSign);
        }

        if (con) {
            contractor_layout.setVisibility(View.VISIBLE);
            contractorSign.setVisibility(View.VISIBLE);
            outputWidthHeight(contractorSign);
        }
        if (et) {
            et_layout.setVisibility(View.VISIBLE);
            etSign.setVisibility(View.VISIBLE);
            outputWidthHeight(etSign);
        }
        if (true) {
            iec_layout.setVisibility(View.VISIBLE);
            iecSign.setVisibility(View.VISIBLE);
            outputWidthHeight(iecSign);
        }
//        addOnGlobalLayoutListener_02();
    }

    public void postRunnable_addGlobalLayout (){
        pmSign.post(new Runnable() {
            @Override
            public void run() {
                pmSign.setVisibility(View.INVISIBLE);
                pm_layout.setVisibility(View.VISIBLE);
                contractor_layout.setVisibility(View.VISIBLE);
                et_layout.setVisibility(View.VISIBLE);
                iec_layout.setVisibility(View.VISIBLE);
            }
        });
    }
    public void postRunnable_addGlobalLayout_02 (){
        ViewTreeObserver greenObserver = pmSign.getViewTreeObserver();
        greenObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                pmSign.getViewTreeObserver().removeOnPreDrawListener(this);
                Log.i(TAG, "onPreDraw: ");
                Log.i(TAG, "getMeasuredWidth,getWidth "+pmSign.getMeasuredWidth()+pmSign.getWidth());
                return true;
            }
        });
    }
    public void addOnPreDrawListener(final RelativeLayout relativeLayout){
        ViewTreeObserver viewTreeObserver = relativeLayout.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Log.i(TAG, "onPreDraw: ");
                Log.i(TAG, "onPreDraw: "+relativeLayout.toString()+": width: "+relativeLayout.getWidth());
                relativeLayout.getViewTreeObserver().removeOnPreDrawListener(this);

                return true;
            }
        });
    }

    public void addOnGlobalLayoutListener_02(){
        Log.i(TAG, "addOnGlobalLayoutListener_02: ");

        ViewTreeObserver vto1 = pmSign.getViewTreeObserver();
        vto1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onPreDraw() {
                Log.i(TAG, "pmSign.getWidth(): " + pmSign.getWidth() + " pmSign.getHeight(): " + pmSign.getHeight());
                pmSign.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        ViewTreeObserver vto2 = contractorSign.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i(TAG, "contractorSign.getWidth(): " + contractorSign.getWidth() + " contractorSign.getHeight(): " + contractorSign.getHeight());
                contractorSign.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        ViewTreeObserver vto3 = etSign.getViewTreeObserver();
        vto3.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i(TAG, "etSign.getWidth(): " + etSign.getWidth() + " etSign.getHeight(): " + etSign.getHeight());
                etSign.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        ViewTreeObserver vto4 = iecSign.getViewTreeObserver();
        vto4.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i(TAG, "iecSign.getWidth(): " + iecSign.getWidth() + " iecSign.getHeight(): " + iecSign.getHeight());
                iecSign.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    public void outputWidthHeight (RelativeLayout relativeLayout){
        Log.i(TAG, "outputWidthHeight: width: "+relativeLayout.getWidth()+" height: "+relativeLayout.getHeight());
    }

}
