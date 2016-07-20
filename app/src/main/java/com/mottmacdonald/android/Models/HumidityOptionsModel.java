package com.mottmacdonald.android.Models;

import java.util.List;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/13 0:25
 * 备注：
 */
public class HumidityOptionsModel {

    public String status;
    public List<HumidityOptionsData> data;

    public class HumidityOptionsData{
        public String id;
        public String name;
    }
}
