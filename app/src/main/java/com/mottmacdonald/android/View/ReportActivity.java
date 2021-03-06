package com.mottmacdonald.android.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.androidquery.callback.AjaxStatus;
import com.mottmacdonald.android.Apis.SaveFormApi;
import com.mottmacdonald.android.Data.DataShared;
import com.mottmacdonald.android.Models.AllTemplatesModel;
import com.mottmacdonald.android.Models.FormsDataModel;
import com.mottmacdonald.android.Models.SaveFormInfoModel;
import com.mottmacdonald.android.Models.TemplatesData;
import com.mottmacdonald.android.MyApplication;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Report;
import com.mottmacdonald.android.ReportDao;
import com.mottmacdonald.android.Utils.DeviceUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.youxiachai.ajax.ICallback;

import java.security.PrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/5 22:04
 * 备注：
 */
public class ReportActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    static String TAG = "ReportActivity";


    private static final String CONTRACT_ID = "contract_id";
    private static final String CONTRACT_NUMBER = "contract_number";
    private static final String PREF_NAME = "myNameList";
    private Calendar currentCalendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    //    private AppCompatSpinner spinner;
//    private List<ContractData> contractDatas;
    private List<TemplatesData> templatesDatas;
    //    private int contractPosition = 0;
    private String sendDate = "", sendTime = "";
    private String currentContractId = "";
    private String currentContractNumber = "";
    private Cursor cursor;
    private Report reportLocalData;
    String formId;
    TextView report, weather, general_site;

    public static void start(Context context, String contractId, String contractNumber) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra(CONTRACT_ID, contractId);
        Log.i(TAG, "start: contractId " + contractId);
        intent.putExtra(CONTRACT_NUMBER, contractNumber);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        currentCalendar = Calendar.getInstance();
        initDatas();
        initViews();
        searchData();
    }


    private void initDatas() {
        if (getIntent().getExtras() != null) {
            currentContractId = getIntent().getExtras().getString(CONTRACT_ID);
            currentContractNumber = getIntent().getExtras().getString(CONTRACT_NUMBER);
        }
        templatesDatas = JSON.parseObject(DataShared.getTemplatesData(), AllTemplatesModel.class).data;
        Log.i(TAG, "initDatas: " + DataShared.getTemplatesData());
//        AllContractModel contractModel = JSON.parseObject(DataShared.getContractData(), AllContractModel.class);
//        contractDatas = contractModel.data;
    }

    private void initViews() {
        Log.i(TAG, "initViews: ");
        mAQuery.id(R.id.start_btn).clicked(clickListener);
        mAQuery.id(R.id.date_select).clicked(clickListener);
        mAQuery.id(R.id.time_select).clicked(clickListener);

        //update submission status
        update();

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

    private void setReportDataShow(Report report) {
        sendDate = report.getInspectionDate();
        sendTime = report.getTime();
        mAQuery.id(R.id.date_select).text(sendDate);
        mAQuery.id(R.id.time_select).text(sendTime);
        mAQuery.id(R.id.environmental_permit_no).text(report.getEnvironmentalPermitNo());
        mAQuery.id(R.id.sit_location).text(report.getSiteLocation());
        mAQuery.id(R.id.pm).text(report.getPm());
        mAQuery.id(R.id.et).text(report.getEt());
        mAQuery.id(R.id.contractor).text(report.getContractor());
        mAQuery.id(R.id.iec).text(report.getIec());
        mAQuery.id(R.id.other).text(report.getOthers());
    }

    private AdapterView.OnItemSelectedListener itemSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start_btn:
                    if (report_submission) {
                        update();
                        Toast.makeText(ReportActivity.this, "report has already saved before!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: already saved");
                    }
                    //save to local
                    saveReportDataLocal();

                    if (isConnected()) {
                        saveFormInfo();
                    }
                    else {
                        Toast.makeText(ReportActivity.this, "device is offline, save data to local", Toast.LENGTH_SHORT).show();
                        //fake data;
                        SaveFormInfoModel saveFormInfoModel = new SaveFormInfoModel();
                        finish();
                        WeatherActivity.start(mContext, currentContractNumber, currentContractId, saveFormInfoModel);

                    }
                    break;

                case R.id.date_select:
                    if (datePickerDialog == null) {
                        datePickerDialog = DatePickerDialog.newInstance(
                                ReportActivity.this,
                                currentCalendar.get(Calendar.YEAR),
                                currentCalendar.get(Calendar.MONTH),
                                currentCalendar.get(Calendar.DAY_OF_MONTH)
                        );
                    }
                    datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                    break;

                case R.id.time_select:
                    if (timePickerDialog == null) {
                        timePickerDialog = TimePickerDialog.newInstance(
                                ReportActivity.this,
                                currentCalendar.get(Calendar.HOUR_OF_DAY),
                                currentCalendar.get(Calendar.MINUTE),
                                currentCalendar.get(Calendar.SECOND),
                                true
                        );
                    }
                    timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
                    break;
            }
        }
    };

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

        String yearStr = year + "";
        String monthStr = (monthOfYear + 1) + "";
        String dayStr = dayOfMonth + "";

        if ((monthOfYear + 1) < 10) {
            monthStr = "0" + monthStr;
        }
        if (dayOfMonth < 10) {
            dayStr = "0" + dayStr;
        }

        sendDate = year + "-" + monthStr + "-" + dayStr;

        mAQuery.id(R.id.date_select).text(dayStr + " " + getResources()
                .getStringArray(R.array.month_simple_names)[monthOfYear] + " " + year);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourStr = hourOfDay + "";
        String minuteStr = minute + "";
        String secondStr = second + "";
        if (hourOfDay < 10) {
            hourStr = "0" + hourOfDay;
        }
        if (minute < 10) {
            minuteStr = "0" + minute;
        }
        if (second < 10) {
            secondStr = "0" + second;
        }
        mAQuery.id(R.id.time_select).text(hourStr + ":" + minuteStr);
        sendTime = hourStr + ":" + minuteStr;
    }

    private void saveReportDataLocal() {
        if (reportLocalData != null) {
            getReportDao().delete(reportLocalData);
        }
        String environmentalPermitNo = mAQuery.id(R.id.environmental_permit_no).getText().toString();
        String siteLocation = mAQuery.id(R.id.sit_location).getText().toString();
        String pm = mAQuery.id(R.id.pm).getText().toString();
        String et = mAQuery.id(R.id.et).getText().toString();
        String contractor = mAQuery.id(R.id.contractor).getText().toString();
        String iec = mAQuery.id(R.id.iec).getText().toString();
        String others = mAQuery.id(R.id.other).getText().toString();

        //ignore the others,
        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("pm", pm);
        prefsEditor.putString("et", et);
        prefsEditor.putString("contractor", contractor);
        prefsEditor.putString("iec", iec);
        prefsEditor.putString("date", sendDate);
        prefsEditor.apply();


        // // TODO: 8/22/2016  
        Report report = new Report();
        report.setInspectionDate(sendDate);
        report.setTime(sendTime);
        report.setEnvironmentalPermitNo(environmentalPermitNo);
        report.setSiteLocation(siteLocation);
        report.setPm(pm);
        Log.i(TAG, "saveReportDataLocal: report.set pm:"+pm);
        report.setEt(et);
        report.setContractor(contractor);
        report.setIec(iec);
        report.setOthers(others);
        report.setContractId(currentContractId);
        report.setContractNumber(currentContractNumber);
        report.setDate(new Date());
        report.setSaveDate(DeviceUtils.getCurrentDate());
        report.setFormId(formId);

        getReportDao().insert(report);
        Toast.makeText(ReportActivity.this, "report saved to local", Toast.LENGTH_SHORT).show();
        Report report2;
        report2 = getReport();
        Log.i(TAG, "onClick: report.getPm()"+report2.getPm());

        String noteText = DeviceUtils.getCurrentDate();
        Log.i(TAG, "onClick: notetext " + noteText);
        Query query = getReportDao().queryBuilder()
                .where(ReportDao.Properties.SaveDate.eq(noteText))
                .orderAsc(ReportDao.Properties.Date)
                .build();
        // 查询结果以 List 返回
        List<Report> reports = query.list();
        if (reports.size() > 0) {
            reportLocalData = reports.get(0);
        }
        Log.i(TAG, "saveReportDataLocal: reportlocaldate.getPm() = "+reportLocalData.getPm());

    }

    private ReportDao getReportDao() {
        return MyApplication.getInstance().getDaoSession().getReportDao();
    }

    private void searchData() {
        String noteText = DeviceUtils.getCurrentDate();
        Log.i(TAG, "onClick: notetext " + noteText);
        // Query 类代表了一个可以被重复执行的查询
        Query query = getReportDao().queryBuilder()
                .where(ReportDao.Properties.SaveDate.eq(noteText))
                .orderAsc(ReportDao.Properties.Date)
                .build();
        // 查询结果以 List 返回
        List<Report> reports = query.list();
        if (reports.size() > 0) {
            reportLocalData = reports.get(0);
            setReportDataShow(reportLocalData);
        }


        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        setInfo_window();
    }

    public void setInfo_window() {
        TextView info_window = (TextView) findViewById(R.id.info);
        formId = "/";
        Log.i(TAG, "setInfo_window: c c id " + currentContractId);
        formId = getFormId();
        String string;
        if (isConnected()) {
            string = "connected";
        } else {
            string = "discounted";

        }
        string = string + " form id: " + formId;
        info_window.setText(string);
    }

    private void saveFormInfo() {
        String date = sendDate + " " + sendTime;
        String environmentalPermitNo = mAQuery.id(R.id.environmental_permit_no).getText().toString();
        String siteLocation = mAQuery.id(R.id.sit_location).getText().toString();
        String pm = mAQuery.id(R.id.pm).getText().toString();
        String et = mAQuery.id(R.id.et).getText().toString();
        String contractor = mAQuery.id(R.id.contractor).getText().toString();
        String iec = mAQuery.id(R.id.iec).getText().toString();
        String others = mAQuery.id(R.id.other).getText().toString();
        String formId = "";
        if (TextUtils.isEmpty(sendDate)) {
            showToast("Please Select Date");
            return;
        }
        if (TextUtils.isEmpty(sendTime)) {
            showToast("Please Select Time");
            return;
        }
        formId = getFormId();
        showProgress();
        Log.i(TAG, "saveFormInfo: formid: " + formId);

        SaveFormApi.saveFormInfo(mContext, formId, date, environmentalPermitNo, siteLocation, pm, et,
                contractor, iec, others, "", "", new ICallback<SaveFormInfoModel>() {
                    @Override
                    public void onSuccess(SaveFormInfoModel saveFormInfoModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                        dismissProgress();
                        if (saveFormInfoModel != null) {
                            Log.i(TAG, "onSuccess: ");
                            Toast.makeText(ReportActivity.this, "saved to server successfully", Toast.LENGTH_SHORT).show();
                            saveReportDataLocal();
                            // set report submission status to true!
                            myPref.setReport_submission(true, mContext);
                            //kill this activity
                            finish();
                            WeatherActivity.start(mContext, currentContractNumber, currentContractId, saveFormInfoModel);

                            myPref.setFormInfoId(saveFormInfoModel.forminfo_id,mContext);
                        } else {
                            showRequestFailToast();
                            Log.i(TAG, "save failed" +"");
                        }

                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(ReportActivity.this, "error", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
