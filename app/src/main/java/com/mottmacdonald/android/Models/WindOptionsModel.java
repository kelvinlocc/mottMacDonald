package com.mottmacdonald.android.Models;

import java.util.List;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/13 0:16
 * 备注：
 */
public class WindOptionsModel {

    public String status;
    public List<WindOptionsData> data;

    public class WindOptionsData{
        public String id;
        public String name;
    }
}
