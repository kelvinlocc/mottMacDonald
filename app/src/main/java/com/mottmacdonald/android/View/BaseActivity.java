package com.mottmacdonald.android.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.mottmacdonald.android.Data.DataShared;
import com.mottmacdonald.android.Data.MySharedPref_App;
import com.mottmacdonald.android.MyApplication;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Report;
import com.mottmacdonald.android.ReportDao;
import com.mottmacdonald.android.Utils.DeviceUtils;

import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/5 22:04
 * 备注：
 */
public class BaseActivity extends AppCompatActivity {

    String TAG = "BaseActivity";
    public AQuery mAQuery;
    public Context mContext;
    private ProgressDialog mProgressDialog;
    NetworkInfo activeNetwork;
    protected boolean report_submission = false;
    protected boolean weather_submission = false;
    protected boolean general_site_submission = false;
    MySharedPref_App myPref;
    String formID;

    private Report reportLocalData;

    private ReportDao getReportDao() {
        return MyApplication.getInstance().getDaoSession().getReportDao();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        MyApplication.getInstance().addActivity(this);
        mAQuery = new AQuery(this);
        mProgressDialog = new ProgressDialog(this);
        myPref = new MySharedPref_App();
        update();

    }

    protected void showToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    protected void showRequestFailToast() {
        showToast(getString(R.string.request_fail));
    }

    protected void showProgress() {
        showProgress("Loading...");
    }

    protected void showProgress(String text) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(text);
            mProgressDialog.show();
        }
    }

    protected void showDeviceOffline_msn() {
        Toast.makeText(BaseActivity.this, "Device is offline, please try later", Toast.LENGTH_SHORT).show();
    }

    protected boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void reset_submission_status() {
        myPref.setReport_submission(false, this);
        myPref.setWeather_submission(false, this);
        myPref.setGeneral_site_submission(false, this);
    }

    protected String getFormId() {
        String noteText = DeviceUtils.getCurrentDate();
        Log.i(TAG, "onClick: notetext "+noteText);
        Query query = getReportDao().queryBuilder()
                .where(ReportDao.Properties.SaveDate.eq(noteText))
                .orderAsc(ReportDao.Properties.Date)
                .build();
        // 查询结果以 List 返回
        List<Report> reports = query.list();
        if (reports.size() > 0) {
            reportLocalData = reports.get(0);
        }
        return reportLocalData.getFormId();

}

    protected void update() {
        if (myPref.isReport_submission(this)) {
            report_submission = true;
        }
        if (myPref.isWeather_submission(this)) {
            weather_submission = true;

        }
        if (myPref.isGeneral_site_submission(this)) {
            general_site_submission = true;
        }
    }
}
