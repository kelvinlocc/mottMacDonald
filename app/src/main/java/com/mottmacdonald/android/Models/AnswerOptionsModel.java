package com.mottmacdonald.android.Models;

import java.util.List;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/13 0:18
 * 备注：
 */
public class AnswerOptionsModel {

    public String status;
    public List<AnswerOptionsData> data;

    public class AnswerOptionsData{
        public String id;
        public String answer;
    }
}
