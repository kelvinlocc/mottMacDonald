package com.mottmacdonald.android.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.mottmacdonald.android.MyApplication;
import com.mottmacdonald.android.R;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/5 22:04
 * 备注：
 */
public class BaseActivity extends AppCompatActivity{

    public AQuery mAQuery;
    public Context mContext;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        MyApplication.getInstance().addActivity(this);
        mAQuery = new AQuery(this);
        mProgressDialog = new ProgressDialog(this);
    }

    protected void showToast(String text){
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    protected void showRequestFailToast(){
        showToast(getString(R.string.request_fail));
    }

    protected void showProgress(){
        showProgress("Loading...");
    }

    protected void showProgress(String text){
        if (mProgressDialog != null){
            mProgressDialog.setMessage(text);
            mProgressDialog.show();
        }
    }

    protected void dismissProgress(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }
}
