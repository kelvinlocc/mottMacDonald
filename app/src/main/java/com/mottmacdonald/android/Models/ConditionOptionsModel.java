package com.mottmacdonald.android.Models;

import java.util.List;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/13 0:15
 * 备注：
 */
public class ConditionOptionsModel {

    public String status;
    public List<ConditionOptionsData> data;

    public class ConditionOptionsData{
        public String id;
        public String name;
    }
}
