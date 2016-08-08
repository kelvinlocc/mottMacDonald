package com.mottmacdonald.android.Apis;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.androidquery.AQuery;
import com.mottmacdonald.android.Models.SaveFormInfoModel;
import com.mottmacdonald.android.Models.SaveFormItemModel;
import com.mottmacdonald.android.Models.SaveFormItemObservationModel;
import com.mottmacdonald.android.Models.SaveFormWeatherModel;
import com.youxiachai.ajax.ICallback;
import com.youxiachai.ajax.NetCallback;
import com.youxiachai.ajax.NetOption;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/13 0:01
 * 备注：
 */
public class SaveFormApi {
    private static final String TAG = "SaveFormApi";

    public static void getHsiGraph(Context ctx, int graphType,
                                   String date, String time,
                                   ICallback<String> callback) {
        AQuery request = new AQuery(ctx);

//        Map<String, Object> params = ApiBase.baseMap(ctx);
//        params.put(Params.GRAPH_TYPE, graphType);
//        params.put(Params.DATE, date);
//        params.put(Params.TIME, time);
//
//        NetOption option = new NetOption(URL_HSI_DATA);
//        NetCallback<HsiGraph> response = new NetCallback<HsiGraph>(HsiGraph.class, option, callback);
//        response.params(params);
//        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void saveFormInfo(Context context, String formId, String date, String environmentalPermitNo,
                                    String siteLocation, String pm, String et, String contractor,
                                    String iec, String other, String followup, String otherObservation,
                                    ICallback<SaveFormInfoModel> callback) {

        AQuery request = new AQuery(context);

        Map<String, Object> params = new HashMap<>();
        params.put(ApiParams.FORM_ID, formId);
        params.put(ApiParams.INSPECTION_DATE, date);
        params.put(ApiParams.ENVIROMENTAL_PERMIT_NO, environmentalPermitNo);
        params.put(ApiParams.SITE_LOCATION, siteLocation);
        params.put(ApiParams.PM, pm);
        params.put(ApiParams.ET, et);
        params.put(ApiParams.CONTRACTOR, contractor);
        params.put(ApiParams.IEC, iec);
        params.put(ApiParams.OTHER, other);
        params.put(ApiParams.FOLLOWUP, followup);
        params.put(ApiParams.OTHER_OBSERVATION, otherObservation);
//
        NetOption option = new NetOption(ApiBase.API_HOST + ApiBase.SAVE_FORM_INFO);
        NetCallback<SaveFormInfoModel> response = new NetCallback<SaveFormInfoModel>(
                SaveFormInfoModel.class, option, callback);
        response.params(params);
        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void saveFormWeather(Context context, String formInfoId, String conditionId,
                                       String temperature, String humidityId, String windId,
                                       String remarks, ICallback<SaveFormWeatherModel> callback) {
        AQuery request = new AQuery(context);
        Map<String, Object> params = new HashMap<>();
        params.put(ApiParams.FORMINFO_ID, formInfoId);
        params.put(ApiParams.CONDITION_ID, conditionId);
        params.put(ApiParams.TEMPERATURE, temperature);
        params.put(ApiParams.HUMIDITY_ID, humidityId);
        params.put(ApiParams.WIND_ID, windId);
        params.put(ApiParams.REMARKS, remarks);

        NetOption option = new NetOption(ApiBase.API_HOST + ApiBase.SAVE_FORM_WEATHER);
        NetCallback<SaveFormWeatherModel> response = new NetCallback<SaveFormWeatherModel>(
                SaveFormWeatherModel.class, option, callback);
        response.params(params);

        request.transformer(new ApiTransFormer()).ajax(response);
    }


    public static void saveFormItem(Context context,
                                    List<Map<String, String>> mapList, ICallback<SaveFormItemModel> callback) {
        AQuery request = new AQuery(context);
        Map<String, Object> params = new HashMap<>();
//        params.put(ApiParams.FORMINFO_ID, formInfoId);
//        params.put(ApiParams.ITEM_ID, itemId);
//        params.put(ApiParams.CLOSE_OUT, closeOut);
//        params.put(ApiParams.ANSWER_ID, answerId);
//        params.put(ApiParams.REMARKS, remarks);
        for (int i = 0; i < mapList.size(); i++) {
            params.put("formitems[" + i + "]", JSON.toJSONString(mapList.get(i)));
            Log.i(TAG, "formitems[" + i + "] " + JSON.toJSONString(mapList.get(i)));
        }
        NetOption option = new NetOption(ApiBase.API_HOST + ApiBase.SAVE_FORM_ITEM);
        NetCallback<SaveFormItemModel> response = new NetCallback<SaveFormItemModel>(
                SaveFormItemModel.class, option, callback);
        response.params(params);


        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void saveFormItem_OnebyOne(Context context,
                                    Map<String, String> map, ICallback<SaveFormItemModel> callback) {
        AQuery request = new AQuery(context);
        Map<String, Object> params = new HashMap<>();
//        params.put("")

        //*
        // map.put("forminfo_id", formInfoId);
//        map.put("item_id", data.item_id);
//        map.put("close_out", !TextUtils.isEmpty(mAdapter.getCloseOutData().get(i).get(j)) ?
//                mAdapter.getCloseOutData().get(i).get(j) : "N/A");
//        map.put("answer_id", mAdapter.getAnswerId(i, j));
//        map.put("remarks", mAdapter.getRemarkData().get(i).get(j));
        //
        //
        // *//

        NetOption option = new NetOption(ApiBase.API_HOST + ApiBase.SAVE_FORM_ITEM);
        NetCallback<SaveFormItemModel> response = new NetCallback<SaveFormItemModel>(
                SaveFormItemModel.class, option, callback);
        response.params(map);


        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void getItemID (Context context,
                                             Map<String, String> map, ICallback<SaveFormItemModel> callback) {
        AQuery request = new AQuery(context);
        Map<String, Object> params = new HashMap<>();

        NetOption option = new NetOption(ApiBase.API_HOST + ApiBase.SAVE_FORM_ITEM);
        NetCallback<SaveFormItemModel> response = new NetCallback<SaveFormItemModel>(
                SaveFormItemModel.class, option, callback);
        response.params(map);


        request.transformer(new ApiTransFormer()).ajax(response);
    }



    public static void saveFormItemObservation(Context context, String formItemId, File image,
                                                   String recommendation, String remediated,
                                                   String followup, ICallback<SaveFormItemObservationModel> callback) {
        AQuery request = new AQuery(context);
        Map<String, Object> params = new HashMap<>();
        params.put(ApiParams.FORMITEM_ID, formItemId);
        params.put(ApiParams.IMAGE, image);
        params.put(ApiParams.RECOMMENDATION, recommendation);
        params.put(ApiParams.REMEDIATED, remediated);
        params.put(ApiParams.FOLLOWUP, followup);
        NetOption option = new NetOption(ApiBase.API_HOST + ApiBase.SAVE_FORM_ITEM_OBSERVATION);
        NetCallback<SaveFormItemObservationModel> response = new NetCallback<SaveFormItemObservationModel>(
                SaveFormItemObservationModel.class, option, callback);
        response.params(params);
        request.transformer(new ApiTransFormer()).ajax(response);
    }
}
