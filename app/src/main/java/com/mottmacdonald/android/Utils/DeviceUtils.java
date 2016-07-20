package com.mottmacdonald.android.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.mottmacdonald.android.MyApplication;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DeviceUtils {

	/**
	 * 获取设备屏幕宽度
	 * @return 宽度像素
	 */
	public static int getDisplayWidth() {
		DisplayMetrics dm = MyApplication.getInstance().
				getResources().getDisplayMetrics();
		return dm.widthPixels;
	}
	
	/**
	 * 获取设备屏幕高度
	 * @return 高度像素
	 */
	public static int getDisplayHeight() {
		DisplayMetrics dm = MyApplication.getInstance().
				getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	
	/**
	 * 获取文件夹大小（byte）
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 如果遇到目录则通过递归调用继续统计
			}
		}
		return dirSize;
	}
	
	/**
	 * 获取文件夹大小（Mbyte）
	 * @param dir
	 * @return
	 */
	public static String getDirSizeMb(File dir) {
		double size = 0;
		long dirSize = getDirSize(dir);
		size = (dirSize + 0.0) / (1024 * 1024);
		DecimalFormat df = new DecimalFormat("0.00");//	以Mb为单位保留两位小数
		String filesize = df.format(size);

		return filesize + " mb";
	}
	
	public static float dp2px(float dpValue) {
		final float scale = MyApplication.getInstance()
				.getResources().getDisplayMetrics().scaledDensity;
		return dpValue * scale;
	}
	
	public static float px2dp(float pxValue) {
		final float scale = MyApplication.getInstance()
				.getResources().getDisplayMetrics().scaledDensity;
		return pxValue / scale;
	}
	
	// 获取手机设备的ID
	@SuppressWarnings("static-access")
	public static String getDeviceID(Context ctx) {
		String string = ((TelephonyManager) ctx
				.getSystemService(ctx.TELEPHONY_SERVICE)).getDeviceId();
		return string;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate(){
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前日期
		String date = formatter.format(curDate);
		return date;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String checkDayOfWeek(){
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE");
		Date curDate = new Date(System.currentTimeMillis());
		String dayOfWeek = formatter.format(curDate);
		return dayOfWeek;
	}
	
	public static Calendar getCalendar(){
		Calendar c = Calendar.getInstance();  
		return c;
	}
	
	
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTime(){
		SimpleDateFormat formatter = new SimpleDateFormat(
				"HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTime(String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}
	
	/** 
	 * 验证手机格式 
	 */  
	public static boolean isMobileNO(String mobiles) {  
	    /* 
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
	    联通：130、131、132、152、155、156、185、186 
	    电信：133、153、180、189、（1349卫通） 
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
	    */  
	    String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
	    if (TextUtils.isEmpty(mobiles)) return false;  
	    else return mobiles.matches(telRegex);  
	   }  
}
