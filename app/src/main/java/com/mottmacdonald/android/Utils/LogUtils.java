package com.mottmacdonald.android.Utils;

import android.util.Log;

public class LogUtils {
	private static boolean debug = true;
	
	public static void d(String tag, String msg) {
		if(debug) {
			Log.d(tag, msg + "");
		}
	}
	
	public static void d(String tag, long msg) {
		if(debug) {
			Log.d(tag, String.valueOf(msg));
		}
	}
	
	public static void d(String tag, int msg) {
		if(debug) {
			Log.d(tag, String.valueOf(msg));
		}
	}
	
	public static void d(String tag, float msg) {
		if(debug) {
			Log.d(tag, String.valueOf(msg));
		}
	}
	
	public static void d(String tag, double msg) {
		if(debug) {
			Log.d(tag, String.valueOf(msg));
		}
	}
	
	public static void d(String tag, boolean msg) {
		if(debug) {
			Log.d(tag, String.valueOf(msg));
		}
	}
	
	public static void e(String tag, String msg) {
		if(debug) {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String tag, long msg) {
		if(debug) {
			Log.e(tag, String.valueOf(msg));
		}
	}
	
	public static void e(String tag, int msg) {
		if(debug) {
			Log.e(tag, String.valueOf(msg));
		}
	}
	
	public static void e(String tag, float msg) {
		if(debug) {
			Log.e(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void e(String tag, double msg) {
		if(debug) {
			Log.e(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void e(String tag, boolean msg) {
		if(debug) {
			Log.e(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void i(String tag, String msg) {
		if(debug) {
			Log.i(tag, msg + "");
		}
	}
	
	public static void i(String tag, long msg) {
		if(debug) {
			Log.i(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void i(String tag, int msg) {
		if(debug) {
			Log.i(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void i(String tag, float msg) {
		if(debug) {
			Log.i(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void i(String tag, double msg) {
		if(debug) {
			Log.i(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void i(String tag, boolean msg) {
		if(debug) {
			Log.i(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void w(String tag, String msg) {
		if(debug) {
			Log.w(tag, msg + "");
		}
	}
	
	public static void w(String tag, long msg) {
		if(debug) {
			Log.w(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void w(String tag, int msg) {
		if(debug) {
			Log.w(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void w(String tag, float msg) {
		if(debug) {
			Log.w(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void w(String tag, double msg) {
		if(debug) {
			Log.w(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void w(String tag, boolean msg) {
		if(debug) {
			Log.w(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void v(String tag, String msg) {
		if(debug) {
			Log.v(tag, msg + "");
		}
	}
	
	public static void v(String tag, long msg) {
		if(debug) {
			Log.v(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void v(String tag, int msg) {
		if(debug) {
			Log.v(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void v(String tag, float msg) {
		if(debug) {
			Log.v(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void v(String tag, double msg) {
		if(debug) {
			Log.v(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void v(String tag, boolean msg) {
		if(debug) {
			Log.v(tag, String.valueOf(msg) + "");
		}
	}
	
	public static void out(String msg) {
		if(debug) {
			System.out.println(msg + "");
		}
	}
	
	public static void out(int msg) {
		out(String.valueOf(msg));
	}
}