package com.mottmacdonald.android.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.androidquery.callback.AjaxStatus;
import com.mottmacdonald.android.Apis.SaveFormApi;
import com.mottmacdonald.android.Data.DataShared;
import com.mottmacdonald.android.Data.MySharedPref_App;
import com.mottmacdonald.android.Models.ConditionOptionsModel;
import com.mottmacdonald.android.Models.HumidityOptionsModel;
import com.mottmacdonald.android.Models.SaveFormInfoModel;
import com.mottmacdonald.android.Models.SaveFormWeatherModel;
import com.mottmacdonald.android.Models.WindOptionsModel;
import com.mottmacdonald.android.MyApplication;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Report;
import com.mottmacdonald.android.Utils.DeviceUtils;
import com.mottmacdonald.android.Utils.ValueUtil;
import com.mottmacdonald.android.Weather;
import com.mottmacdonald.android.WeatherDao;
import com.youxiachai.ajax.ICallback;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/5 23:40
 * 备注：
 */
public class WeatherActivity extends BaseActivity {
    private static String TAG = "WeatherActivity ";
    MySharedPref_App mySharedPreferenceApplication;
    private static final String CONTRACT_NAME = "contract_name";
    private static final String CONTRACT_ID = "contract_id";
    private static final String FORM_INFO_DATA = "form_info_data";
    private String contractName;
    private String contractId;
    private RadioGroup conditionGroup, humidityGroup, windGroup;
    private String conditionId = "";
    private String humidityId = "";
    private String windId = "";
    private String formInfoId;
    private Weather weatherLocalData;
    private RadioGroup conditionGroup_02; //
    private Boolean toggleKey;
    public MySharedPref_App mySharedPref_app;
    public static Context mContext;
    TextView info;
    TextView report, weather, general_site;

    public static void start(Context context, String contractName, String contractId,
                             SaveFormInfoModel saveFormInfoModel) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(CONTRACT_NAME, contractName);
        intent.putExtra(CONTRACT_ID, contractId);
        intent.putExtra(FORM_INFO_DATA, saveFormInfoModel);
        context.startActivity(intent);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initViews();
        searchData();
        mySharedPref_app = new MySharedPref_App(); // get instance
        if (myPref.isReport_submission(mContext)) {
            Log.i(TAG, "onCreate: foormid" + getFormId());
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        String currentContractId = mySharedPref_app.getCurrentContractId(mContext);
        String contractNumber = mySharedPref_app.getContractNumber(mContext);

        ReportActivity.start(mContext, currentContractId, contractNumber);
        Log.i(TAG, "onClick: c c from p " + myPref.getCurrentContractId(mContext) + "," + myPref.getContractNumber(mContext));

        Toast.makeText(WeatherActivity.this, "back!", Toast.LENGTH_SHORT).show();

    }

    private void initViews() {

//        if (getIntent().getExtras() != null) {
//            contractName = getIntent().getExtras().getString(CONTRACT_NAME);
//            contractId = getIntent().getExtras().getString(CONTRACT_ID);
//            mAQuery.id(R.id.title_text).text(contractName + "-" + "WEATHER");
//            SaveFormInfoModel formInfoModel = (SaveFormInfoModel) getIntent()
//                    .getExtras().getSerializable(FORM_INFO_DATA);
//            formInfoId = formInfoModel.forminfo_id;
//        }
        contractName = myPref.getContractNumber(mContext);
        contractId = myPref.getCurrentContractId(mContext);
        mAQuery.id(R.id.title_text).text(contractName + "-" + "WEATHER");


        conditionGroup = (RadioGroup) findViewById(R.id.condition_group);
        conditionGroup_02 = (RadioGroup) findViewById(R.id.condition_group_02); //
        humidityGroup = (RadioGroup) findViewById(R.id.humidity_group);
        windGroup = (RadioGroup) findViewById(R.id.wind_group);

        conditionGroup.setOnCheckedChangeListener(conditionChangeListener);
        conditionGroup_02.setOnCheckedChangeListener(conditionChangeListener_02);

        humidityGroup.setOnCheckedChangeListener(humidityChangeListener);
        windGroup.setOnCheckedChangeListener(windChangeListener);


        ConditionOptionsModel conditionOptionsModel = JSON.parseObject(DataShared.getConditionData(),
                ConditionOptionsModel.class);
        for (int i = 0; i < conditionOptionsModel.data.size(); i++) {
            ConditionOptionsModel.ConditionOptionsData data = conditionOptionsModel.data.get(i);
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_radiobutton, null);
            RadioButton tempRadio = (RadioButton) itemView.findViewById(R.id.temp_radiobutton);
//            Log.i(TAG, "tempRadio.setText: " + data.name);

            tempRadio.setText(data.name);
            tempRadio.setTag(data.id);
            tempRadio.setId(100 + i);
            if (i < 4) {
                conditionGroup.addView(itemView);
            } else {
                conditionGroup_02.addView(itemView);
            }
        }

        // uncheck all radio:
        for (int i = 0; i < conditionGroup.getChildCount(); i++) {
            ((RadioButton) conditionGroup.getChildAt(i)).setChecked(false);
            if (((RadioButton) conditionGroup.getChildAt(i)).isChecked() == false) {
//                Log.i(TAG, "conditionGroup.getChildAt: " + i + " is unchecked!");
            }
        }

        for (int i = 0; i < conditionGroup_02.getChildCount(); i++) {
            ((RadioButton) conditionGroup_02.getChildAt(i)).setChecked(false);
//            Log.i(TAG, "conditionGroup.getChildAt: " + i + " is unchecked!");
        }

        HumidityOptionsModel humidityModel = JSON.parseObject(DataShared.getHumidityData(),
                HumidityOptionsModel.class);
        for (int i = 0; i < humidityModel.data.size(); i++) {
            HumidityOptionsModel.HumidityOptionsData data = humidityModel.data.get(i);
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_radiobutton, null);
            RadioButton tempRadio = (RadioButton) itemView.findViewById(R.id.temp_radiobutton);
            tempRadio.setText(data.name);
            tempRadio.setTag(data.id);
            tempRadio.setId(200 + i);
            humidityGroup.addView(itemView);
        }

        WindOptionsModel windModel = JSON.parseObject(DataShared.getWindData(), WindOptionsModel.class);
        for (int i = 0; i < windModel.data.size(); i++) {
            WindOptionsModel.WindOptionsData data = windModel.data.get(i);
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_radiobutton, null);
            RadioButton tempRadio = (RadioButton) itemView.findViewById(R.id.temp_radiobutton);
            tempRadio.setText(data.name);
            tempRadio.setTag(data.id);
            tempRadio.setId(300 + i);
            windGroup.addView(itemView);
        }

        mAQuery.id(R.id.general_btn).clicked(clickListener);
    }

    private void setWeatherDataShow(Weather weather) {
        if (ValueUtil.StringToInt(weather.getCondition()) > 0)
            conditionGroup.check(100 + ValueUtil.StringToInt(weather.getCondition()) - 1);
        if (ValueUtil.StringToInt(weather.getHumidity()) > 0)
            humidityGroup.check(200 + ValueUtil.StringToInt(weather.getHumidity()) - 1);
        if (ValueUtil.StringToInt(weather.getWind()) > 0)
            windGroup.check(300 + ValueUtil.StringToInt(weather.getWind()) - 1);
        mAQuery.id(R.id.temperature_text).text(weather.getTemperature());
        mAQuery.id(R.id.remarks_text).text(weather.getRemarks());
    }


    private RadioGroup.OnCheckedChangeListener conditionChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

//            Log.i(TAG, "checkedID: " + checkedId);

            if (checkedId != -1 &&
                    ((RadioButton) findViewById(checkedId)).isChecked()) {
                conditionGroup_02.clearCheck();
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                conditionId = (String) radioButton.getTag();
            }
        }
    };


    private RadioGroup.OnCheckedChangeListener conditionChangeListener_02 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.i(TAG, "checkedID: " + checkedId);
            if (checkedId != -1 &&
                    ((RadioButton) findViewById(checkedId)).isChecked()) {
                conditionGroup.clearCheck();

                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                conditionId = (String) radioButton.getTag();
            }
        }
    };


    private RadioGroup.OnCheckedChangeListener humidityChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            humidityId = (String) radioButton.getTag();
        }
    };

    private RadioGroup.OnCheckedChangeListener windChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            windId = (String) radioButton.getTag();
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.general_btn:
                    if (isConnected()) {
                        Toast.makeText(WeatherActivity.this, "connected", Toast.LENGTH_SHORT).show();
                        if (report_submission) {
                            formInfoId = mySharedPref_app.getFormInfoId(mContext);
                            Log.i(TAG, "report is submitted");
                            saveWeather();
                        } else {
                            Log.i(TAG, "report is not submitted");
                            saveReport();
                        }
                    } else {
                        Toast.makeText(WeatherActivity.this, "device is offline, save data to local", Toast.LENGTH_SHORT).show();
                        GeneralSiteActivity.start(mContext, contractName, contractId, formInfoId);
                        finish();
                    }
                    break;
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
                            Toast.makeText(WeatherActivity.this, "report saved successfully", Toast.LENGTH_SHORT).show();
                            // go to save weather
                            myPref.setFormInfoId(saveFormInfoModel.forminfo_id,mContext);
                            saveWeather();
                            // set report submission status to true!
                            myPref.setReport_submission(true, mContext);
                        } else {
                            showRequestFailToast();
                            Log.i(TAG, "save failed" + "");
                        }

                    }
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(WeatherActivity.this, "report save error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveWeatherLocalData(String conditionId, String temperatureText, String humidityId,
                                      String windId, String remarkText) {
        if (weatherLocalData != null) {
            getWeatherDao().delete(weatherLocalData);
        }
        Weather weather = new Weather();
        weather.setCondition(conditionId);
        weather.setTemperature(temperatureText);
        weather.setHumidity(humidityId);
        weather.setWind(windId);
        weather.setRemarks(remarkText);
        weather.setSaveDate(DeviceUtils.getCurrentDate());
        weather.setDate(new Date());
        getWeatherDao().insert(weather);
    }

    private WeatherDao getWeatherDao() {
        return MyApplication.getInstance().getDaoSession().getWeatherDao();
    }

    private void searchData() {
        weatherLocalData = getWeather();
        setWeatherDataShow(weatherLocalData);
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    private void saveWeather() {
        final String temperatureText = mAQuery.id(R.id.temperature_text).getText().toString();
        final String remarkText = mAQuery.id(R.id.remarks_text).getText().toString();
        if (TextUtils.isEmpty(conditionId)) {
            showToast("Please select condition");
            return;
        }
        if (TextUtils.isEmpty(humidityId)) {
            showToast("Please select humidity");
            return;
        }
        if (TextUtils.isEmpty(windId)) {
            showToast("Please select wind");
            return;
        }
        Log.i(TAG, "saveWeather: formInfoId: "+formInfoId);
        formInfoId = myPref.getFormInfoId(mContext);
        Log.i(TAG, "saveWeather: formInfoId: "+formInfoId);
        showProgress();
        SaveFormApi.saveFormWeather(mContext, formInfoId, conditionId, temperatureText, humidityId,
                windId, remarkText, new ICallback<SaveFormWeatherModel>() {
                    @Override
                    public void onSuccess(SaveFormWeatherModel saveFormWeatherModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                        dismissProgress();
                        if (saveFormWeatherModel != null) {
                            saveWeatherLocalData(conditionId, temperatureText, humidityId, windId, remarkText);


                            GeneralSiteActivity.start(mContext, contractName, contractId, formInfoId);
                            Log.i(TAG, "create the unique ID for shared preference: contractName, contractId, formInfoId: " + contractName + "," + contractId + "," + formInfoId);
                            String head = contractName + contractId + formInfoId;
                            Log.i(TAG, "head " + head);
                            // // TODO: 8/8/2016
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

}
