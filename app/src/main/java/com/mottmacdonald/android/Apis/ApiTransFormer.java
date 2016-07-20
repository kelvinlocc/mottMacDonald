package com.mottmacdonald.android.Apis;

import com.alibaba.fastjson.JSON;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.mottmacdonald.android.Utils.LogUtils;

/**
 * 
 * @author Cipher
 *
 */
public class ApiTransFormer implements Transformer {

	@SuppressWarnings("unchecked")
	public <T> T transform(String url, Class<T> type, String encoding, 
			byte[] data, AjaxStatus status) {
		System.out.println("获得的数据"+new String(data).toString());
		LogUtils.d("Transform data", new String(data).toString());
		return (T)JSON.parseObject(data, type);
	}
}
