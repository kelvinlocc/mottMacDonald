package com.mottmacdonald.android.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/21 0:25
 * 备注：
 */
public class FileUtil {

    //文件存储根目录
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalCacheDir();
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getCacheDir().getAbsolutePath();
    }
}
