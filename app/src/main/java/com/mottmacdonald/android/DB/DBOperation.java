//package com.mottmacdonald.android.DB;
//
//import com.cantonhouse.app.common.utils.LogUtils;
//import com.cantonhouse.model.api.entity.GuideContent;
//import com.cantonhouse.model.api.entity.House;
//import com.cantonhouse.model.api.entity.Login;
//import com.cantonhouse.model.api.entity.PushListContent;
//import com.cantonhouse.model.api.entity.Subscribe;
//import com.cantonhouse.model.api.entity.WeekendHouse;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//
///**
// * 数据库操作累
// * @author Lam
// *
// */
//public class DBOperation {
//
//	private Context mContext;
//	private SQLiteDatabase mDatabase;
//
//	public DBOperation(Context context) {
//		this.mContext = context;
//	}
//
//	//数据库通用基本方法--打开数据库(可写)
//	public synchronized void openWritable() throws SQLException {
//		if(mDatabase == null || !mDatabase.isOpen()) {
//			mDatabase = new DBHelper(mContext).getWritableDatabase();
//		}
//	}
//
//	//数据库通用基本方法--打开数据库(只读)
//	public synchronized void openReadable() {
//		if(mDatabase == null || !mDatabase.isOpen()) {
//			mDatabase = new DBHelper(mContext).getReadableDatabase();
//		}
//	}
//
//	//数据库通用基本方法--关闭数据库
//	public void close() {
//		if(mDatabase != null && mDatabase.isOpen()) {
//			mDatabase.close();
//		}
//	}
//
//	//数据库执行方法--储存楼盘详情
//	public void saveHouse(House house) {
//		if(house == null) return;
//		try {
//			openWritable();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return;
//		}
//		while(mDatabase.isDbLockedByCurrentThread()) {
//			LogUtils.w("Database",
//					"Database isLockedByCurrentThread or LockedByOtherThreads");
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				return;
//			}
//		}
//		mDatabase.beginTransaction();
//		mDatabase.execSQL(sqlInsertHouse(), new Object[]{
//			house.getJunJia(), house.getShengYu(), house.getZan(),
//			house.getTuanGou(), house.getShouCang(), house.getDigest(),
//			house.getHouseId(), house.getHouseName(), house.getCity(),
//			house.getDistrict(), house.getAddress(), house.getSaleStatus(),
//			house.getSalePhone(), house.getSalePoint(), house.getIp(),
//			house.getDeveloper(), house.getCubagerate(), house.getFitment(),
//			house.getCustodianFee(), house.getPropertyType(), house.getBuildingType(),house.getImgUrl()
//		});
//		mDatabase.setTransactionSuccessful();
//		mDatabase.endTransaction();
//		close();
//	}
//
//	//数据库执行方法--储存周末楼盘详情
//	public void saveWeekend(WeekendHouse house) {
//		if(house == null) return;
//		try {
//			openWritable();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return;
//		}
//		while(mDatabase.isDbLockedByCurrentThread()) {
//			LogUtils.w("Database",
//					"Database isLockedByCurrentThread or LockedByOtherThreads");
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				return;
//			}
//		}
//		mDatabase.beginTransaction();
//		mDatabase.execSQL(sqlInsertWeekend(), new Object[] {
//			house.getHouseId(), house.getHouseName(), house.getTag(),
//			house.getRegion(), house.getAction()
//		});
//		mDatabase.setTransactionSuccessful();
//		mDatabase.endTransaction();
//		close();
//	}
//
//	//数据库执行方法--储存导购详情
//	public void saveGuide(GuideContent content) {
//		if(content == null) return;
//		try {
//			openWritable();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return;
//		}
//		while(mDatabase.isDbLockedByCurrentThread()) {
//			LogUtils.w("Database",
//					"Database isLockedByCurrentThread or LockedByOtherThreads");
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				return;
//			}
//		}
//		mDatabase.beginTransaction();
//		mDatabase.execSQL(sqlInsertGuide(), new Object[]{
//			content.getRecPic(), content.getTitle(), content.getSummary(),
//			content.getTag(), content.getCreationTime(),content.getNewsID()
//		});
//		mDatabase.setTransactionSuccessful();
//		mDatabase.endTransaction();
//		close();
//	}
//	//数据库执行方法--储存用户资料记录
//	public void saveUserInfo(Login login){
//		if(login == null) return;
//		try {
//			openWritable();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return;
//		}
//		while(mDatabase.isDbLockedByCurrentThread()) {
//			LogUtils.w("Database",
//					"Database isLockedByCurrentThread or LockedByOtherThreads");
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				return;
//			}
//		}
//		mDatabase.beginTransaction();
//		mDatabase.execSQL(sqlInsertUserInfo(), new Object[]{
//			login.getUserId(), login.getUserName(), login.getPhone(),
//			login.getEmail(), login.getPicture()
//		});
//		mDatabase.setTransactionSuccessful();
//		mDatabase.endTransaction();
//		close();
//	}
//
//	//数据库执行方法--储存订阅内容选项记录
//	public boolean saveSubscribe(Subscribe subscribe) {
//		if (subscribe == null)
//			return false;
//		try {
//			openWritable();
//
//			while (mDatabase.isDbLockedByCurrentThread()) {
//				LogUtils.w("Database",
//						"Database isLockedByCurrentThread or LockedByOtherThreads");
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//					return false;
//				}
//			}
//			mDatabase.beginTransaction();
//			mDatabase.execSQL(
//					sqlInsertSubscribe(),
//					new Object[] { subscribe.getTagName(),
//							subscribe.getTagType(), subscribe.getNum(),
//							subscribe.getStatus() });
//			mDatabase.setTransactionSuccessful();
//			mDatabase.endTransaction();
//			close();
//			return true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//
//
//
//	//数据库执行方法--储存搜索记录
//	public void saveSearchRecord(String record) {
//		try {
//			openWritable();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return;
//		}
//		while(mDatabase.isDbLockedByCurrentThread()) {
//			LogUtils.w("Database",
//					"Database isLockedByCurrentThread or LockedByOtherThreads");
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				return;
//			}
//		}
//		mDatabase.beginTransaction();
//		Cursor cursor = mDatabase.rawQuery(sqlQuerySearchRecord(record), null);
//		if(cursor.getCount() > 0) {//已经存在记录
//			mDatabase.execSQL(sqlDeleteRecord(record));
//		}
//		mDatabase.execSQL(sqlInsertSearchRecord(), new Object[]{record});
//		mDatabase.setTransactionSuccessful();
//		mDatabase.endTransaction();
//		close();
//	}
//
//
//	//数据库执行方法--储存订阅内容选项记录
//		public boolean savePushList(PushListContent pushListContent) {
//			if (pushListContent == null)
//				return false;
//			try {
//				openWritable();
//
//				while (mDatabase.isDbLockedByCurrentThread()) {
//					LogUtils.w("Database",
//							"Database isLockedByCurrentThread or LockedByOtherThreads");
//					try {
//						Thread.sleep(10);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//						return false;
//					}
//				}
//				mDatabase.beginTransaction();
//				mDatabase.execSQL(
//						sqlInsertPushList(),
//						new Object[] { pushListContent.getType(),
//							pushListContent.getId(), pushListContent.getContent(),
//								pushListContent.getDate() });
//				mDatabase.setTransactionSuccessful();
//				mDatabase.endTransaction();
//				close();
//				return true;
//			} catch (SQLException e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//
//	//更新订阅内容数据表
//		public boolean updateSubscribe(Subscribe subscribe) {
//			if (subscribe == null)
//				return false;
//			try {
//				openWritable();
//
//				while (mDatabase.isDbLockedByCurrentThread()) {
//					LogUtils.w("Database",
//							"Database isLockedByCurrentThread or LockedByOtherThreads");
//					try {
//						Thread.sleep(10);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//						return false;
//					}
//				}
//				int isCheck = 0;
//				if(subscribe.getStatus() == true){
//					isCheck = 1;
//				}else {
//					isCheck = 0;
//				}
//				mDatabase.beginTransaction();
//				mDatabase.execSQL(
//						sqlUpdateSubscribe(subscribe),
//						new Object[]{isCheck}
//						);
//				mDatabase.setTransactionSuccessful();
//				mDatabase.endTransaction();
//				close();
//				return true;
//			} catch (SQLException e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//
//	//数据库执行方法--按顺序读取整个楼盘表
//	public Cursor readHouses() {
//		openReadable();
//		Cursor cursor = mDatabase.rawQuery(sqlQueryAllHouses(), null);
//		cursor.moveToFirst();
//		close();
//		return cursor;
//	}
//	public Cursor readHouses(String name,String id) {
//		openReadable();
//		Cursor cursor = mDatabase.rawQuery(sqlQuerySingleHouses(name,id), null);
//		cursor.moveToFirst();
//		close();
//		return cursor;
//	}
//
//	//数据库执行方法--按顺序读取整个周末看楼表
//	public Cursor readWeekends() {
//		openReadable();
//		Cursor cursor = mDatabase.rawQuery(sqlQueryAllWeekends(), null);
//		cursor.moveToFirst();
//		close();
//		return cursor;
//	}
//
//	//数据库执行方法--按顺序读取某个周末看楼表
//		public Cursor readWeekend(String id, String name) {
//			openReadable();
//			Cursor cursor = mDatabase.rawQuery(sqlQuerySingleWeekends(id,name), null);
//			cursor.moveToFirst();
//			close();
//			return cursor;
//		}
//
//	//数据库执行方法--按顺序读取整个导购表
//	public Cursor readGuides() {
//		openReadable();
//		Cursor cursor = mDatabase.rawQuery(sqlQueryAllGuides(), null);
//		cursor.moveToFirst();
//		close();
//		return cursor;
//	}
//	//数据库执行方法--按顺序读取某个导购表
//	public Cursor readGuide(String id, String tagName) {
//		openReadable();
//		Cursor cursor = mDatabase.rawQuery(sqlQuerySingleGuides(id,tagName), null);
//		cursor.moveToFirst();
//		close();
//		return cursor;
//	}
//
//	//数据库执行方法--按顺序读取整个用户资料表
//	public Cursor readUserInfo(){
//		openReadable();
//		Cursor cursor = mDatabase.rawQuery(sqlQueryAllUserInfo(), null);
//		cursor.moveToFirst();
//		close();
//		return cursor;
//	}
//
//	//数据库执行方法--按顺序读取整个订阅内容选项表
//		public Cursor readSubscribe(){
//			openReadable();
//			Cursor cursor = mDatabase.rawQuery(sqlQueryAllSubscribe(), null);
//			cursor.moveToFirst();
//			close();
//			return cursor;
//		}
//
//	//数据库执行方法--按数据读取整个搜搜记录表
//	public Cursor readRecords() {
//		openReadable();
//		Cursor cursor = mDatabase.rawQuery(sqlQueryAllSearchRecords(), null);
//		cursor.moveToFirst();
//		close();
//		return cursor;
//	}
//
//	//数据库执行方法--按数据读取整个推送列表
//	public Cursor readPushList(){
//		openReadable();
//		Cursor cursor = mDatabase.rawQuery(sqlQueryAllPushList(), null);
//		cursor.moveToFirst();
//		close();
//		return cursor;
//	}
//
//	public boolean deleteUserInfo(String userID){
//		try {
//			openWritable();
//
//			while (mDatabase.isDbLockedByCurrentThread()) {
//				LogUtils.w("Database",
//						"Database isLockedByCurrentThread or LockedByOtherThreads");
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//					return false;
//				}
//			}
//			mDatabase.beginTransaction();
//			mDatabase.execSQL(sqlDeleteUserInfo(userID));
//			mDatabase.setTransactionSuccessful();
//			mDatabase.endTransaction();
//			close();
//			return true;
//		}catch(SQLException exception){
//			return false;
//		}
//
//	}
//
//	public boolean clearSearchHistory() {
//		try {
//			openWritable();
//			while (mDatabase.isDbLockedByCurrentThread()) {
//				LogUtils.w("Database",
//						"Database isLockedByCurrentThread or LockedByOtherThreads");
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//					return false;
//				}
//			}
//			mDatabase.beginTransaction();
//			mDatabase.execSQL(sqlTruncate(DBHelper.TABLE_SEARCHS_RECORDS));
//			mDatabase.setTransactionSuccessful();
//			mDatabase.endTransaction();
//			close();
//			return true;
//		}catch(SQLException exception){
//			return false;
//		}
//	}
//
//	//T-SQL语句--储存楼盘详情
//	private String sqlInsertHouse() {
//		String sql = "INSERT INTO " + DBHelper.TABLE_HOUSES + " ( " +
//				House.JUN_JIA + ", " + House.SHENG_YU + ", " +
//				House.ZAN + ", " + House.TUAN_GOU + ", " +
//				House.SHOU_CANG + ", " + House.DIGEST + ", " +
//				House.HOUSE_ID + ", " + House.HOUSE_NAME + ", " +
//				House.CITY + ", " + House.DISTRICT + ", " +
//				House.ADDRESS + ", " + House.SALE_STATUS + ", " +
//				House.SALE_PHONE + ", " + House.SALE_POINT + ", " +
//				House.IP + ", " + House.DEVELOPER + ", " +
//				House.CUBAGERATE + ", " + House.FITMENT + ", " +
//				House.CUSTODIAN_FEE + ", " + House.PROPERTY_TYPE + ", " +  House.BUILDING_TYPE + ", " +
//				House.IMG_Url + " ) " + " VALUES " +
//				"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
//		LogUtils.i("SQL_INSERT_HOUSE", sql);
//		return sql;
//	}
//
//	//T-SQL语句--储存周末开盘信息
//	private String sqlInsertWeekend() {
//		String sql = "INSERT INTO " + DBHelper.TABLE_WEEKENDS + " ( " +
//				WeekendHouse.HOUSE_ID + ", " + WeekendHouse.HOUSE_NAME + ", " +
//				WeekendHouse.TAG + ", " + WeekendHouse.REGION + ", " +
//				WeekendHouse.ACTION + " ) " + " VALUES " +
//				"(?, ?, ?, ?, ?);";
//		LogUtils.i("SQL_INSERT_WEEKEND", sql);
//		return sql;
//	}
//
//	//T-SQL语句-储存导购详情
//	private String sqlInsertGuide() {
//		String sql = "INSERT INTO " + DBHelper.TABLE_GUIDES + " ( " +
//				GuideContent.REC_PIC + ", " + GuideContent.TITLE + ", " +
//				GuideContent.SUMMARY + ", " + GuideContent.TAG + ", " +
//				GuideContent.CREATIONTIME +", " + GuideContent.NEWS_ID + " ) " + " VALUES " +
//				"(?, ?, ?, ?, ?, ?);";
//		LogUtils.i("SQL_INSERT_GUIDE", sql);
//		return sql;
//	}
//
//	//T-SQL语句-储存导购详情
//		private String sqlInsertUserInfo() {
//			String sql = "INSERT INTO " + DBHelper.TABLE_USERINFO + " ( " +
//					Login.USER_ID + ", " + Login.USER_NAME + ", " +
//					Login.PHONE + ", " + Login.EMAIL + ", " +
//					Login.PICTURE + " ) " + " VALUES " +
//					"(?, ?, ?, ?, ?);";
//			LogUtils.i("SQL_INSERT_USERINFO", sql);
//			return sql;
//		}
//
//	// T-SQL语句-储存导购详情
//	private String sqlInsertSubscribe() {
//		String sql = "INSERT INTO " + DBHelper.TABLE_SUBSCRIBE + " ( "
//				+ Subscribe.TAG_NAME + ", " + Subscribe.TAG_TYPE + ", " + Subscribe.NUM
//				+ ", " + Subscribe.STATUS + " ) "
//				+ " VALUES " + "(?, ?, ?, ?);";
//		LogUtils.i("SQL_INSERT_USERINFO", sql);
//		return sql;
//	}
//
//	//T-SQL语句-储存搜索记录
//	private String sqlInsertSearchRecord() {
//		String sql ="INSERT INTO " + DBHelper.TABLE_SEARCHS_RECORDS + " ( " +
//				DBHelper.RECORD + " ) " + " VALUES " + "(?);";
//		LogUtils.i("SQL_INSERT_RECORD", sql);
//		return sql;
//	}
//
//	//T-SQL语句-储存搜索记录
//	private String sqlInsertSearchRecord(String content) {
//		String sql ="IF EXISTS ( SELECT COUNT ( * ) FROM " + DBHelper.TABLE_SEARCHS_RECORDS +
//				" WHERE " + DBHelper.RECORD + " = '" + content + "' )" +
//				" DELETE " + DBHelper.TABLE_SEARCHS_RECORDS + " WHERE " + DBHelper.RECORD + " = '" + content + "' " +
//				" ELSE "
//
//
//		+ "INSERT INTO " + DBHelper.TABLE_SEARCHS_RECORDS + " ( " +
//				DBHelper.RECORD + " ) " + " VALUES " + "('" + content + "');";
//		LogUtils.i("SQL_INSERT_RECORD", sql);
//		return sql;
//	}
//
//	private String sqlInsertPushList(){
//		String sql = "INSERT INTO " + DBHelper.TABLE_PUSH_LIST + " ( " +
//				PushListContent.TYPE + ", " + PushListContent.CONTENT_ID + ", " +
//				PushListContent.CONTENT + ", " + PushListContent.DATE + " ) " + " VALUES " +
//				"(?, ?, ?, ?);";
//		LogUtils.i("SQL_INSERT_USERINFO", sql);
//		return sql;
//	}
//
//	//T-SQL语句-读取所有楼盘详情
//	private String sqlQueryAllHouses() {
//		String sql = "SELECT * FROM " + DBHelper.TABLE_HOUSES +
//				" ORDER BY " + DBHelper.AUTO_ID + " ASC;";
//		LogUtils.i("READ_ALL_HOUSES", sql);
//		return sql;
//	}
//
//	//T-SQL语句-读取某个楼盘详情
//		private String sqlQuerySingleHouses(String name, String id) {
//			String sql = "SELECT * FROM " + DBHelper.TABLE_HOUSES +
//					" WHERE (" + House.HOUSE_NAME + " = '" + name + "' AND "+
//							House.HOUSE_ID + " = '" + id +"');";
//			LogUtils.i("READ_SINGLE_HOUSES", sql);
//			return sql;
//		}
//
//	//T-SQL语句-读取所有周末开盘信息
//	private String sqlQueryAllWeekends() {
//		String sql = "SELECT * FROM " + DBHelper.TABLE_WEEKENDS +
//				" ORDER BY " + DBHelper.AUTO_ID + " ASC;";
//		LogUtils.i("READ_ALL_WEEKENDS", sql);
//		return sql;
//	}
//
//	private String sqlQuerySingleWeekends(String id, String name){
//		String sql = "SELECT * FROM " + DBHelper.TABLE_WEEKENDS +
//				" WHERE (" + WeekendHouse.HOUSE_ID + " = '" + id + "' AND "+
//				WeekendHouse.HOUSE_NAME + " = '" + name+"');";
//		return sql;
//	}
//
//	//T-SQL语句-读取所有导购详情
//	private String sqlQueryAllGuides() {
//		String sql = "SELECT * FROM " + DBHelper.TABLE_GUIDES +
//				" ORDER BY " + DBHelper.AUTO_ID + " ASC;";
//		LogUtils.d("READ_ALL_GUIDES", sql);
//		return sql;
//	}
//
//	private String sqlQuerySingleGuides(String id, String tagName) {
//		String sql = "SELECT * FROM " + DBHelper.TABLE_GUIDES +
//				" WHERE (" + GuideContent.NEWS_ID + " = '" + id + "' AND "+
//				GuideContent.TAG + " = '" + tagName+"');";
//		LogUtils.d("READ_SINGLE_GUIDES", sql);
//		return sql;
//	}
//
//	//T-SQL语句-读取用户资料记录
//	private String sqlQueryAllUserInfo(){
//		String sql = "SELECT * FROM " + DBHelper.TABLE_USERINFO +
//				" ORDER BY " + DBHelper.AUTO_ID + " ASC;";
//		LogUtils.d("READ_ALL_USERINFO", sql);
//		return sql;
//	}
//	//T-SQL语句-读取前8条搜索记录
//	private String sqlQueryAllSearchRecords() {
//		String sql = "SELECT * FROM " + DBHelper.TABLE_SEARCHS_RECORDS +
//				" ORDER BY " + DBHelper.AUTO_ID + " DESC LIMIT 0,8;";
//		LogUtils.d("READ_ALL_RECORDS", sql);
//		return sql;
//	}
//
//	private String sqlQuerySearchRecord(String record) {
//		String sql = "SELECT * FROM " + DBHelper.TABLE_SEARCHS_RECORDS +
//				" WHERE " + DBHelper.RECORD + " = '" + record + "';";
//		LogUtils.d("READ_RECORD", sql);
//		return sql;
//	}
//
//	private String sqlDeleteRecord(String record) {
//		String sql = "DELETE FROM " + DBHelper.TABLE_SEARCHS_RECORDS +
//				" WHERE " + DBHelper.RECORD + " = '" + record + "';";
//		LogUtils.d("DELETE_RECORD", sql);
//		return sql;
//	}
//	private String sqlDeleteUserInfo(String userID){
//		String sql = "DELETE FROM " + DBHelper.TABLE_USERINFO +
//				" WHERE " + Login.USER_ID + " = '" + userID + "';";
//		LogUtils.d("DELETE_USERINFO", sql);
//		return sql;
//	}
//
//	//T-SQL语句-读取订阅内容选项
//	private String sqlQueryAllSubscribe(){
//		String sql = "SELECT * FROM " + DBHelper.TABLE_SUBSCRIBE +
//				" ORDER BY " + DBHelper.AUTO_ID + " ASC;";
//		LogUtils.d("READ_ALL_SUBSCRIBE", sql);
//		return sql;
//	}
//
//	private String sqlQueryAllPushList(){
//		String sql = "SELECT * FROM " + DBHelper.TABLE_PUSH_LIST +
//				" ORDER BY " + DBHelper.AUTO_ID + " ASC;";
//		LogUtils.d("READ_ALL_PUSH", sql);
//		return sql;
//	}
//
//	private String sqlUpdateSubscribe(Subscribe subscribe) {
//		String sql = "UPDATE " + DBHelper.TABLE_SUBSCRIBE +
//				" SET " +
//
//				Subscribe.STATUS + " = " + "?"  +
//
//				" WHERE (" + Subscribe.TAG_NAME + " = '" + subscribe.getTagName() + "' AND "+
//				Subscribe.TAG_TYPE + " = '" + subscribe.getTagType()+"');";
////                " WHERE " + Subscribe.TAG_NAME + " = '" + subscribe.getTagName() + "';";
//		LogUtils.i("SQL_UPDATE_SUBSCRIBE", sql);
//		return sql;
//	}
//
//	//T-SQL语句--清空表数据
//	private String sqlTruncate(String table_name) {
//		String sql = "DELETE FROM " + table_name + ";" +
//				"SELECT * FROM sqlite_sequence;" +
//				"UPDATE sqlite_sequence set seq=0 where name = '" + table_name + "';";
//		LogUtils.i("TRUNCATE TABLE", sql);
//		return sql;
//	}
//
//}
