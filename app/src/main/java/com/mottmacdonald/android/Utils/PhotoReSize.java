package com.mottmacdonald.android.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class PhotoReSize {

	private Context mContext;
	public PhotoReSize(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}
	
	public File reSize(File file, int width, File saveFile){
		Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
		int bmWidth = bitmap.getWidth();
		float scale = (float)(width)/(float)(bmWidth);
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap reBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
		File reFile = saveBitmap(reBitmap, saveFile);
		if(bitmap !=null && !bitmap.isRecycled()){
			bitmap.recycle();bitmap = null;
		}
		if(reBitmap !=null && !reBitmap.isRecycled()){
			reBitmap.recycle();reBitmap = null;
		}
		
		return reFile;
	}

	public  File saveBitmap(Bitmap bitmap, File saveFile){
		File file = null;
		File sdcardDir = Environment.getExternalStorageDirectory();
		if(checkPath()){
			file = saveFile;
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			FileOutputStream fileOutputStream = null;
			try
			{
				fileOutputStream = new FileOutputStream(file);
			} 
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fileOutputStream);
			try 
			{
				fileOutputStream.flush();
			} 
			catch (Exception e)
			{
			}
			try
			{
				fileOutputStream.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}else {
			
		}
		return file;
		
		
	}
	
	private boolean checkPath(){
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			String path = sdcardDir.getPath() + "/com.BrandLady/camera/reSize/";
			File path1 = new File(path);
			if (!path1.exists()) {
				path1.mkdirs();
			}
			return true;
		} else {
			return false;
		}
	}
	
	private String getTime(){
		Calendar calendar = Calendar.getInstance();
		int mYear = calendar.get(Calendar.YEAR);  
        int mMonth = calendar.get(Calendar.MONTH)+1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        int mSecond = calendar.get(Calendar.SECOND);
        int mMSecond = calendar.get(Calendar.MILLISECOND);
        String time = "reSizeImage"+mYear+mMonth+mDay+mHour+mMinute+mSecond+mMSecond;
		return time;
	}
}
