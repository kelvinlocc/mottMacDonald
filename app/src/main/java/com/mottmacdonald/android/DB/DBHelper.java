//package com.mottmacdonald.android.DB;
//
//import com.cantonhouse.app.common.utils.LogUtils;
//import com.cantonhouse.model.api.entity.GuideContent;
//import com.cantonhouse.model.api.entity.GuideHouse;
//import com.cantonhouse.model.api.entity.House;
//import com.cantonhouse.model.api.entity.Login;
//import com.cantonhouse.model.api.entity.PushListContent;
//import com.cantonhouse.model.api.entity.Subscribe;
//import com.cantonhouse.model.api.entity.WeekendHouse;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
///**
// * Android数据库SQLiteOpenHelper类
// * @author Lam
// */
//public class DBHelper extends SQLiteOpenHelper {
//
//	private static final int VERSION = 1;
//	private static final String DATABASE_NAME = "house.db";
//
//	public static final String TABLE_HOUSES = "houses";
//	public static final String TABLE_GUIDES = "guides";
//	public static final String TABLE_WEEKENDS = "weekends";
//	public static final String TABLE_USERINFO = "userinfo";
//	public static final String TABLE_SEARCHS_RECORDS = "records";
//	public static final String TABLE_SUBSCRIBE = "subscribe";
//	public static final String TABLE_PUSH_LIST = "pushlist";
//
//	public static final String AUTO_ID = "_id";
//	public static final String RECORD = "record";
//	public static final String  HOUSE_NAME = "house_name";
//
//	public DBHelper(Context context) {
//		super(context, DATABASE_NAME, null, VERSION);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		db.execSQL(sqlCreateTableHouses());
//		db.execSQL(sqlCreateTableWeekends());
//		db.execSQL(sqlCreateTableGuides());
//		db.execSQL(sqlCreateTableUserInfo());
//		db.execSQL(sqlCreateTableSearchRecords());
//		db.execSQL(sqlCreateTableSubscribe());
//		db.execSQL(sqlCreateTablePushList());
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 *
//	 * @return
//	 */
//	private static String sqlCreateTableHouses() {
//		String sql = "CREATE TABLE " + TABLE_HOUSES + " (" +
//				AUTO_ID + " integer " + " PRIMARY KEY " + "," +
//				House.JUN_JIA + " nvarchar " + "," +
//				House.SHENG_YU + " nvarchar " + "," +
//				House.ZAN + " nvarchar " + "," +
//				House.TUAN_GOU + " nvarchar " + "," +
//				House.SHOU_CANG + " nvarchar " + "," +
//				House.DIGEST + " nvarchar " + "," +
//				House.HOUSE_ID + " nvarchar " + "," +
//				House.HOUSE_NAME + " nvarchar " + "," +
//				House.CITY + " nvarchar " + "," +
//				House.DISTRICT + " nvarchar " + "," +
//				House.ADDRESS + " nvarchar " + "," +
//				House.SALE_STATUS + " nvarchar " + "," +
//				House.SALE_PHONE + " nvarchar " + "," +
//				House.SALE_POINT + " nvarchar " + "," +
//				House.IP + " nvarchar " + "," +
//				House.DEVELOPER + " nvarchar " + "," +
//				House.CUBAGERATE + " nvarchar " + "," +
//				House.FITMENT + " nvarchar " + "," +
//				House.CUSTODIAN_FEE + " nvarchar " + "," +
//				House.PROPERTY_TYPE + " nvarchar " + "," +
//				House.BUILDING_TYPE + " nvarchar " + "," +
//				House.IMG_Url + " nvarchar " + ");";
//		LogUtils.i("SQL_CREATE_TABLE_HOUSES", sql);
//		return sql;
//	}
//
//	private static String sqlCreateTableWeekends() {
//		String sql = "CREATE TABLE " + TABLE_WEEKENDS + " (" +
//				AUTO_ID + " integer " + " PRIMARY KEY " + "," +
//				WeekendHouse.HOUSE_ID + " nvarchar " + "," +
//				WeekendHouse.HOUSE_NAME + " nvarchar " + "," +
//				WeekendHouse.TAG + " nvarchar " + "," +
//				WeekendHouse.REGION + " nvarchar " + "," +
//				WeekendHouse.ACTION + " nvarchar " + ");";
//		LogUtils.i("SQL_CREATE_TABLE_WEEKENDS", sql);
//		return sql;
//	}
//
//	private static String sqlCreateTableGuides() {
//		String sql = "CREATE TABLE " + TABLE_GUIDES + " (" +
//				AUTO_ID + " integer " + " PRIMARY KEY " + ", " +
//				GuideContent.REC_PIC + " nvarchar " + ", " +
//				GuideContent.TITLE + " nvarchar " + ", " +
//				GuideContent.SUMMARY + " nvarchar " + ", " +
//				GuideContent.TAG + " nvarchar " + ", " +
//				GuideContent.CREATIONTIME + " nvarchar " + ", " +
//				GuideContent.NEWS_ID + " nvarchar " + ");";
//		LogUtils.i("SQL_CREATE_TABLE_GUIDES", sql);
//		return sql;
//	}
//
//	private static String sqlCreateTableUserInfo(){
//		String sql = "CREATE TABLE " + TABLE_USERINFO + " (" +
//				AUTO_ID + " integer " + " PRIMARY KEY " + ", " +
//				Login.USER_ID + " nvarchar " + ", " +
//				Login.USER_NAME + " nvarchar " + ", " +
//				Login.PHONE + " nvarchar " + ", " +
//				Login.EMAIL + " nvarchar " + ", " +
//				Login.PICTURE + " nvarchar " + ");";
//		LogUtils.i("SQL_CREATE_TABLE_USERINFO", sql);
//		return sql;
//	}
//	private static String sqlCreateTableSearchRecords() {
//		String sql = "CREATE TABLE " + TABLE_SEARCHS_RECORDS + " (" +
//				AUTO_ID + " integer " + " PRIMARY KEY " + ", " +
//				RECORD + " nvarchar " + ");";
//		LogUtils.i("SQL_CREATE_TABLE_RECORDS", sql);
//		return sql;
//	}
//	private static String sqlCreateTableSubscribe(){
//		String sql = "CREATE TABLE " + TABLE_SUBSCRIBE + " (" +
//				AUTO_ID + " integer " + " PRIMARY KEY " + ", " +
//				Subscribe.TAG_NAME + " nvarchar " + ", " +
//				Subscribe.TAG_TYPE + " nvarchar " + ", " +
//				Subscribe.NUM + " nvarchar " + ", " +
//				Subscribe.STATUS + " nvarchar " + ");";
//		LogUtils.i("SQL_CREATE_TABLE_USERINFO", sql);
//		return sql;
//	}
//
//	private static String sqlCreateTablePushList(){
//		String sql = "CREATE TABLE " + TABLE_PUSH_LIST + " (" +
//				AUTO_ID + " integer " + " PRIMARY KEY " + ", " +
//				PushListContent.TYPE + " nvarchar " + ", " +
//				PushListContent.CONTENT_ID + " nvarchar " + ", " +
//				PushListContent.CONTENT + " nvarchar " + ", " +
//				PushListContent.DATE + " nvarchar " + ");";
//		LogUtils.i("SQL_CREATE_TABLE_USERINFO", sql);
//		return sql;
//	}
//}
