package com.mottmacdonald.android.Utils;

import java.text.DecimalFormat;

public class FloatUtil {

	
	public static float twofloatResult(float f1,float f2){
		  int m = 0;
		  String s1 = ""+f1;
		  String s2 = ""+f2;
		  m += s1.split("\\.")[1].length();
		  m += s2.split("\\.")[1].length();
		  return (float) (Float.parseFloat(s1.replace(".", ""))*Float.parseFloat(s2.replace(".", ""))/Math.pow(10,m));
		 }
	
	public static String setFormatFloat2(float f){
		DecimalFormat   fnum  =   new  DecimalFormat("##0.00");   
		return fnum.format(f);
	}
	
	public static String setFormatFloat2(String string){
		DecimalFormat   fnum  =   new  DecimalFormat("##0.00"); 
		float temp = 0;
		try {
			temp = Float.valueOf(string);
		} catch (NumberFormatException e) {
			temp = 0;
		}
		return fnum.format(temp);
	}
	
	public static String setFormatFloat1(float f){
		DecimalFormat   fnum  =   new  DecimalFormat("##0.0");   
		return fnum.format(f);
	}
	
	
	public static String setFormatInt(float f){
		DecimalFormat   fnum  =   new  DecimalFormat("##0");   
		return fnum.format(f);
	}
	
	
}
