package com.mottmacdonald.android.Utils;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/28 23:40
 * 备注：
 */
public class ValueUtil {

    public static int StringToInt(String s){
        try {
            return Integer.valueOf(s);
        }catch (Exception e){return -1;}
    }
}
