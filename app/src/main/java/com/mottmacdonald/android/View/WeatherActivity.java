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

    public static void start(Context context, String contractName, String contractId,
                             SaveFormInfoModel saveFormInfoModel) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(CONTRACT_NAME, contractName);
        intent.putExtra(CONTRACT_ID, contractId);
        intent.putExtra(FORM_INFO_DATA, saveFormInfoModel);
        context.startActivity(intent);
        mContext= context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initViews();
        searchData();
        mySharedPref_app = new MySharedPref_App(); // get instance
    }

    private void initViews() {
        if (getIntent().getExtras() != null) {
            contractName = getIntent().getExtras().getString(CONTRACT_NAME);
            contractId = getIntent().getExtras().getString(CONTRACT_ID);
            mAQuery.id(R.id.title_text).text(contractName + "-" + "WEATHER");
            SaveFormInfoModel formInfoModel = (SaveFormInfoModel) getIntent()
                    .getExtras().getSerializable(FORM_INFO_DATA);
            formInfoId = formInfoModel.forminfo_id;

        }
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
            Log.i(TAG, "tempRadio.setText: " + data.name);

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
                Log.i(TAG, "conditionGroup.getChildAt: " + i + " is unchecked!");
            }
        }

        for (int i = 0; i < conditionGroup_02.getChildCount(); i++) {
            ((RadioButton) conditionGroup_02.getChildAt(i)).setChecked(false);
            Log.i(TAG, "conditionGroup.getChildAt: " + i + " is unchecked!");
        }

//        conditionGroup.clearCheck();
//        conditionGroup_02.clearCheck();


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

            Log.i(TAG, "checkedID: " + checkedId);

            if (checkedId != -1 &&
                    ((RadioButton) findViewById(checkedId)).isChecked()) {
                conditionGroup_02.clearCheck();

                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                conditionId = (String) radioButton.getTag();
            }



        }
    };

    private void clear_condition_radioGroup(String key) {
        Log.i(TAG, "RadioGroup R.id.condition_group: \n" + R.id.condition_group);
        if (key == "group01") {
            for (int i = 0; i < conditionGroup.getChildCount(); i++) {
                ((RadioButton) conditionGroup.getChildAt(i)).setChecked(false);
                if (((RadioButton) conditionGroup.getChildAt(i)).isChecked() == false) {
                    Log.i(TAG, "conditionGroup.getChildAt: " + i + " is unchecked!");
                }
            }

        } else if (key == "group02") {

            for (int i = 0; i < conditionGroup_02.getChildCount(); i++) {
                ((RadioButton) conditionGroup_02.getChildAt(i)).setChecked(false);
                if (((RadioButton) conditionGroup_02.getChildAt(i)).isChecked() == false) {
                    Log.i(TAG, "conditionGroup.getChildAt: " + i + " is unchecked!");
                }
            }

        }
    }

    private RadioGroup.OnCheckedChangeListener conditionChangeListener_02 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

//            if (conditionGroup.getCheckedRadioButtonId() != -1) {
//
//                Log.i(TAG,"condition group 1 has checked");
//                clear_condition_radioGroup("group01");
//            }
            Log.i(TAG, "checkedID: " + checkedId);
            if (checkedId != -1 &&
                    ((RadioButton) findViewById(checkedId)).isChecked()) {
                conditionGroup.clearCheck();

                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                conditionId = (String) radioButton.getTag();
            }

//            RadioButton radioButton = (RadioButton) findViewById(checkedId);
//            conditionId = (String) radioButton.getTag();
//            conditionGroup_02.clearCheck();


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
                    saveWeather();
                    break;
            }
        }
    };

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
        String dateText = DeviceUtils.getCurrentDate();

        // Query 类代表了一个可以被重复执行的查询
        Query query = getWeatherDao().queryBuilder()
                .where(WeatherDao.Properties.SaveDate.eq(dateText))
                .orderAsc(WeatherDao.Properties.Date)
                .build();
        // 查询结果以 List 返回
        List<Weather> weathers = query.list();
        System.out.println("数据长度：" + weathers.size());
        if (weathers.size() > 0) {
            weatherLocalData = weathers.get(0);
            setWeatherDataShow(weatherLocalData);
        }

        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
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
        showProgress();
        SaveFormApi.saveFormWeather(mContext, formInfoId, conditionId, temperatureText, humidityId,
                windId, remarkText, new ICallback<SaveFormWeatherModel>() {
                    @Override
                    public void onSuccess(SaveFormWeatherModel saveFormWeatherModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                        dismissProgress();
                        if (saveFormWeatherModel != null) {
                            saveWeatherLocalData(conditionId, temperatureText, humidityId, windId, remarkText);
                            GeneralSiteActivity.start(mContext, contractName, contractId, formInfoId);
                            Log.i(TAG,"create the unique ID for shared preference: contractName, contractId, formInfoId: "+contractName+","+contractId+","+formInfoId);
                            String head = contractName+contractId+formInfoId;
                            Log.i(TAG,"head "+head);
                            // // TODO: 8/8/2016
                            mySharedPref_app.setHead(mContext,head);



//                            Log.i(TAG,"check: "+mySharedPref_app.getHead(mContext));

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
