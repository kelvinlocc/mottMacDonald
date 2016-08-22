package com.mottmacdonald.android.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.androidquery.callback.AjaxStatus;
import com.mottmacdonald.android.Apis.GetDataApi;
import com.mottmacdonald.android.Data.DataShared;
import com.mottmacdonald.android.Models.AllContractModel;
import com.mottmacdonald.android.Models.AllTemplatesModel;
import com.mottmacdonald.android.Models.AnswerOptionsModel;
import com.mottmacdonald.android.Models.ConditionOptionsModel;
import com.mottmacdonald.android.Models.HumidityOptionsModel;
import com.mottmacdonald.android.Models.WindOptionsModel;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Utils.DeviceUtils;
import com.youxiachai.ajax.ICallback;

public class MainActivity extends BaseActivity {

    private boolean isInitiativeSync = false;
    private AlertDialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        if (TextUtils.isEmpty(DataShared.getContractData())) {
            getAllContract();
        } else if (TextUtils.isEmpty(DataShared.getTemplatesData())) {
            getAllTemplates();
        } else if (TextUtils.isEmpty(DataShared.getConditionData())) {
            getConditionOptions();
        } else if (TextUtils.isEmpty(DataShared.getHumidityData())) {
            getHumidityOptions();
        } else if (TextUtils.isEmpty(DataShared.getWindData())) {
            getWindOptions();
        }

    }

    private void initViews() {
        mAQuery.id(R.id.report_btn).clicked(clickListener);
        mAQuery.id(R.id.sync_btn).clicked(clickListener);
        mAQuery.id(R.id.info_btn).clicked(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.report_btn:
                    startActivity(new Intent(mContext, SelectContractActivity.class));
                    break;
                case R.id.sync_btn:
                    if (isConnected()) {
                        isInitiativeSync = true;
                        getAllContract();
                    } else {
                        showDeviceOffline_msn();
                    }
                    break;
                case R.id.info_btn:
                    if (infoDialog == null) {
                        infoDialog = new AlertDialog.Builder(mContext)
                                .setMessage("Last Sync Date：" + DataShared.getLastSyncDate())
                                .setPositiveButton("OK", null).show();
                    }
                    infoDialog.show();
                    break;
            }

        }
    };

    private void getAllContract() {
        showProgress(getString(R.string.sync));
        GetDataApi.getAllContract(mContext, new ICallback<AllContractModel>() {
            @Override
            public void onSuccess(AllContractModel allContractModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                dismissProgress();
                if (allContractModel != null) {
                    String jsonString = JSON.toJSONString(allContractModel);
                    DataShared.saveContractData(jsonString);
                    DataShared.saveLastSyncDate(DeviceUtils.getCurrentTime("yyyy-MM-dd HH:mm"));
                    if (TextUtils.isEmpty(DataShared.getTemplatesData()) || isInitiativeSync)
                        getAllTemplates();
                } else {
                    showRequestFailToast();
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getAllTemplates() {
        showProgress(getString(R.string.sync));
        GetDataApi.getAllTemplates(mContext, new ICallback<AllTemplatesModel>() {
            @Override
            public void onSuccess(AllTemplatesModel allTemplatesModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                dismissProgress();
                if (allTemplatesModel != null) {
                    String jsonString = JSON.toJSONString(allTemplatesModel);
                    System.out.println("变回Json:" + jsonString);
                    DataShared.saveTemplatesData(jsonString);
                    if (TextUtils.isEmpty(DataShared.getConditionData()) || isInitiativeSync)
                        getConditionOptions();
                } else {
                    showRequestFailToast();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getConditionOptions() {
        showProgress(getString(R.string.sync));
        GetDataApi.getConditionOptions(mContext, new ICallback<ConditionOptionsModel>() {
            @Override
            public void onSuccess(ConditionOptionsModel conditionOptionsModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                dismissProgress();
                if (conditionOptionsModel != null) {
                    String jsonString = JSON.toJSONString(conditionOptionsModel);
                    DataShared.saveConditionData(jsonString);
                    if (TextUtils.isEmpty(DataShared.getHumidityData()) || isInitiativeSync)
                        getHumidityOptions();
                } else {
                    showRequestFailToast();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getHumidityOptions() {
        showProgress(getString(R.string.sync));
        GetDataApi.getHumidityOptions(mContext, new ICallback<HumidityOptionsModel>() {
            @Override
            public void onSuccess(HumidityOptionsModel humidityOptionsModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                dismissProgress();
                if (humidityOptionsModel != null) {
                    String jsonString = JSON.toJSONString(humidityOptionsModel);
                    DataShared.saveHumidityData(jsonString);
                    if (TextUtils.isEmpty(DataShared.getWindData()) || isInitiativeSync)
                        getWindOptions();
                } else {
                    showRequestFailToast();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getWindOptions() {
        showProgress(getString(R.string.sync));
        GetDataApi.getWindOptions(mContext, new ICallback<WindOptionsModel>() {
            @Override
            public void onSuccess(WindOptionsModel windOptionsModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {
                dismissProgress();
                if (windOptionsModel != null) {
                    String jsonString = JSON.toJSONString(windOptionsModel);
                    DataShared.saveWindData(jsonString);
                } else {
                    showRequestFailToast();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getAnswerOptions() {
        showProgress(getString(R.string.sync));
        GetDataApi.getAnswerOptions(mContext, new ICallback<AnswerOptionsModel>() {
            @Override
            public void onSuccess(AnswerOptionsModel answerOptionsModel, Enum<?> anEnum, AjaxStatus ajaxStatus) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


}
