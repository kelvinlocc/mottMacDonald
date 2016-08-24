package com.mottmacdonald.android.View;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.alibaba.fastjson.JSON;
import com.mottmacdonald.android.Data.DataShared;
import com.mottmacdonald.android.Models.AllContractModel;
import com.mottmacdonald.android.Models.ContractData;
import com.mottmacdonald.android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/27 22:36
 * 备注：
 */
public class SelectContractActivity extends BaseActivity{

    private AppCompatSpinner spinner;
    private List<ContractData> contractDatas;
    private String contractNumber = "";
    private String currentContractId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_select);
        initDatas();
        initViews();

    }

    private void initDatas(){
        AllContractModel contractModel = JSON.parseObject(DataShared.getContractData(), AllContractModel.class);
        contractDatas = contractModel.data;
    }

    private void initViews(){
        spinner = (AppCompatSpinner) findViewById(R.id.contract_spinner);
        List<String> list = new ArrayList<String>();
        for (ContractData temp:contractDatas) {
            list.add(temp.name);
        }

        setSpinnerData(list);
        mAQuery.id(R.id.confirm_btn).clicked(clickListener);
    }

    private void setSpinnerData(List<String> list){
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, list);
        myAdapter.setDropDownViewResource(R.layout.item_dropdown);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(itemSelectListener);
        myAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.confirm_btn:
                    reset_submission_status();
                    ReportActivity.start(mContext, currentContractId, contractNumber);
                    Log.i(TAG, "onClick: c c:"+currentContractId+","+contractNumber);
                    myPref.setCurrentContractId(currentContractId,mContext);
                    myPref.setContractNumber(contractNumber,mContext);
                    Log.i(TAG, "onClick: c c from p "+myPref.getCurrentContractId(mContext)+","+myPref.getContractNumber(mContext));
                    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                    String format = s.format(new Date());
                    Log.i(TAG, "onClick: format"+format);
                    myPref.setHead(mContext, format);
                    break;
            }
        }
    };


    private AdapterView.OnItemSelectedListener itemSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            currentContractId = contractDatas.get(position).contract_id;
            contractNumber = contractDatas.get(position).contract_no;

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
