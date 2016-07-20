package com.mottmacdonald.android.Apis;

import android.content.Context;

import com.androidquery.AQuery;
import com.mottmacdonald.android.Models.AllContractModel;
import com.mottmacdonald.android.Models.AllTemplatesModel;
import com.mottmacdonald.android.Models.AnswerOptionsModel;
import com.mottmacdonald.android.Models.ConditionOptionsModel;
import com.mottmacdonald.android.Models.HumidityOptionsModel;
import com.mottmacdonald.android.Models.WindOptionsModel;
import com.youxiachai.ajax.ICallback;
import com.youxiachai.ajax.NetCallback;
import com.youxiachai.ajax.NetOption;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/13 0:01
 * 备注：
 */
public class GetDataApi {

    public void get(){
//        AQuery request = new AQuery(ctx);
//
//        Bundle params = ApiBase.newBaseBundle();
//        params.putString(Params.CITY, city);
//        params.putString(Params.PHONE, phone);
//        params.putString(Params.PWD, pwd);
//
//        NetOption options = new NetOption(URL_LOGIN +
//                ApiCommon.encodeUrl(params));
//
//        NetCallback<Login> response = new NetCallback<Login>(Login.class, options, callback);
//
//        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void getAllContract(Context context, ICallback<AllContractModel> callback){
        AQuery request = new AQuery(context);
        NetOption options = new NetOption(ApiBase.API_HOST + ApiBase.GET_ALL_CONTRACT);
        NetCallback<AllContractModel> response = new NetCallback<AllContractModel>(
                AllContractModel.class, options, callback);
        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void getAllTemplates(Context context, ICallback<AllTemplatesModel> callback){
        AQuery request = new AQuery(context);
        NetOption options = new NetOption(ApiBase.API_HOST + ApiBase.GET_ALL_TEMPLATES);
        NetCallback<AllTemplatesModel> response = new NetCallback<AllTemplatesModel>(
                AllTemplatesModel.class, options, callback);
        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void getConditionOptions(Context context, ICallback<ConditionOptionsModel> callback){
        AQuery request = new AQuery(context);
        NetOption options = new NetOption(ApiBase.API_HOST + ApiBase.GET_CONDITION_OPTIONS);
        NetCallback<ConditionOptionsModel> response = new NetCallback<ConditionOptionsModel>(
                ConditionOptionsModel.class, options, callback);
        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void getHumidityOptions(Context context, ICallback<HumidityOptionsModel> callback){
        AQuery request = new AQuery(context);
        NetOption options = new NetOption(ApiBase.API_HOST + ApiBase.GET_HUMIDITY_OPTIONS);
        NetCallback<HumidityOptionsModel> response = new NetCallback<HumidityOptionsModel>(
                HumidityOptionsModel.class, options, callback);
        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void getWindOptions(Context context, ICallback<WindOptionsModel> callback){
        AQuery request = new AQuery(context);
        NetOption options = new NetOption(ApiBase.API_HOST + ApiBase.GET_WIND_OPTIONS);
        NetCallback<WindOptionsModel> response = new NetCallback<WindOptionsModel>(
                WindOptionsModel.class, options, callback);
        request.transformer(new ApiTransFormer()).ajax(response);
    }

    public static void getAnswerOptions(Context context, ICallback<AnswerOptionsModel> callback){
        AQuery request = new AQuery(context);
        NetOption options = new NetOption(ApiBase.API_HOST + ApiBase.GET_ANSWER_OPTIONS);
        NetCallback<AnswerOptionsModel> response = new NetCallback<AnswerOptionsModel>(
                AnswerOptionsModel.class, options, callback);
        request.transformer(new ApiTransFormer()).ajax(response);
    }
}
