package com.zhang.box;

import java.util.ArrayList;
import java.util.List;
import com.example.zzq.bean.UserInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BooksDB extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "BOOK0804.db";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "books_table";
	public final static String BOOK_ID = "book_id";
	public final static String BOOK_NAME = "book_name";
	public final static String BOOK_AUTHOR = "book_author";

	public BooksDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 创建table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + "prduct" + "(" + "pid"
				+ " INTEGER( 11 )," + "logo" + " varchar( 255 )," + "logogray"
				+ " varchar( 255 )," + "name" + " varchar( 255 )," + "des"
				+ " varchar( 255 )," + "price" + " INTEGER( 11 )," + "weixin"
				+ " INTEGER( 11 )," + "zhifubao" + " INTEGER( 11 )," + "status"
				+ " INTEGER( 11 )," + "hdid" + " INTEGER( 11 )," + "type"
				+ " INTEGER( 11 )," + "max " + " INTEGER( 11 )," + "mfinish "
				+ " INTEGER( 11 )," + "big" + " varchar( 255 )," + "hgid"
				+ " varchar( 255 )," + "promainshow" + " INTEGER( 11 ));";
		db.execSQL(sql);

		sql = "CREATE TABLE IF NOT EXISTS " + "adv" + "(" + "aid"
				+ " INTEGER( 11 )," + "img" + " varchar( 255 )," + "status"
				+ " INTEGER( 11 )," + "type" + " varchar( 255 )," + "video"
				+ " varchar( 255 ));";

		db.execSQL(sql);

		sql = "CREATE TABLE IF NOT EXISTS " + "apps" + "(" + "appid"
				+ " INTEGER( 11 )," + "applogo" + " varchar( 255 ),"
				+ "appname" + " varchar( 255 )," + "appprice"
				+ " INTEGER( 11 )," + "duan" + " varchar( 255 )," + "mainshow"
				+ " INTEGER( 11 )," + "apkurl" + " varchar( 255 )," + "mainimg"
				+ " varchar( 255 )," + "sys" + " varchar( 255 )," + "appstatus"
				+ " INTEGER( 11 )," + "big" + " varchar( 255 )," + "buyapp"
				+ " varchar( 255 )," + "apptype" + " varchar( 255 ));";

		db.execSQL(sql);

		// 营养 proid bai hundrend xiang
		sql = "CREATE TABLE IF NOT EXISTS " + "proinfo" + "(" + "proid"
				+ " INTEGER( 11 )," + "bai" + " varchar( 255 )," + "hundrend"
				+ " varchar( 255 )," + "xiang" + " varchar( 255 ));";
		db.execSQL(sql);

		// sql = "CREATE TABLE IF NOT EXISTS " + "uploads" + "(" + "upname"
		// + " varchar( 255 ));";
		// db.execSQL(sql);
		/** 创建一个上传数据的表 */
		db.execSQL("create table uploads(upname varchar(255))");
	}

	/** 商品信息插入 */
	public long insertInfos(UserInfo info) {
		// deleteProinfo(info.proid);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("proid", info.proid);
		cv.put("bai", info.bai);
		cv.put("hundrend", info.hundrend);
		cv.put("xiang", info.xiang);
		long row = db.insert("proinfo", null, cv);
		db.close();
		return row;
	}

	/** 商品信息读取 */
	// public void ReadProInfo(int pid, String xiang) {
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor cursor = db.query("proinfo", new String[] { "bai", "hundrend" },
	// "proid=? and xiang=?", new String[] { pid + "", xiang }, null,
	// null, null);
	// while (cursor.moveToNext()) {
	// UserInfo info = new UserInfo();
	// info.bai = cursor.getString(cursor.getColumnIndex("bai"));
	// info.hundrend = cursor.getString(cursor.getColumnIndex("hundrend"));
	// UserInfo.proInfos.add(info);
	// }
	// }

	/** 商品信息读取 */
	public void ReadProInfo(int pid) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query("proinfo", new String[] { "xiang", "hundrend", "bai" },
						"proid=?", new String[] { pid + "" }, null, null, null);
		while (cursor.moveToNext()) {
			UserInfo info = new UserInfo();
			info.bai = cursor.getString(cursor.getColumnIndex("bai"));
			info.hundrend = cursor.getString(cursor.getColumnIndex("hundrend"));
			info.xiang = cursor.getString(cursor.getColumnIndex("xiang"));
			UserInfo.proInfos.add(info);
		}
		cursor.close();
		db.close();
	}

	/** upload插入 HaveUpInfo */
	public void insertUpload(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("upname", name);
		db.insert("uploads", null, cv);
		db.close();
	}

	/** 根据app 查询所有的APP */
	public void ReadUpload() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("uploads", new String[] { "upname" }, null,
				null, null, null, null);
		while (cursor.moveToNext()) {

			UserInfo.upname = UserInfo.upname + ","
					+ cursor.getString(cursor.getColumnIndex("upname"));
		}
		cursor.close();
		db.close();
	}

	/** 根据id判断 */

	public Boolean HaveAppInfo(int appid) {

		SQLiteDatabase db = this.getReadableDatabase();
		Boolean b = false;
		String whereValue = Integer.toString(appid);
		Cursor cursor = db.query("apps", null, "appid" + "=?",
				new String[] { whereValue }, null, null, null);
		b = cursor.moveToFirst();
		cursor.close();
		db.close();
		return b;
	}

	/** app 插入 */

	public long insertApp(UserInfo info) {
		// if (HaveAppInfo(info.appid)) {
		// SQLiteDatabase db = this.getWritableDatabase();
		// String where ="appid" + " = ?";
		// String[] whereValue = { Integer.toString(info.appid)};
		// ContentValues cv = new ContentValues();
		// cv.put("appstatus",info.appstatus);
		// db.update("apps", cv, where, whereValue);
		// return 0;
		// deleteApps(info.appid);
		// }

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("appid", info.appid);
		cv.put("applogo", info.applogo);
		cv.put("appname", info.appname);
		cv.put("appprice", info.appprice);
		cv.put("duan", info.duan);
		cv.put("mainshow", info.mainshow);
		cv.put("apkurl", info.apkurl);
		cv.put("mainimg", info.mainimg);
		cv.put("sys", info.sys);
		cv.put("appstatus", info.appstatus);
		cv.put("big", info.apkBigImg);
		cv.put("buyapp", info.buyapp);
		cv.put("apptype", info.apptype);
		long row = db.insert("apps", null, cv);
		db.close();
		return row;
	}

	/*
	 * //
	 * appid,applogo,appname,appprice,duan,mainshow,apkurl,mainimg,shijian,sys
	 * ,appstatus,apptype
	 *//** 根据app 查询 mainshow 三张 */

	public void ReadAppByMainshowOne() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("apps", new String[] { "appid", "applogo",
				"appname", "appprice", "duan", "apkurl", "mainimg", "big",
				"sys", "apptype" }, "mainshow=? and appstatus=?", new String[] {
				"1", "1" }, null, null, null);
		while (cursor.moveToNext()) {

			UserInfo info = new UserInfo();
			info.appid = cursor.getInt(cursor.getColumnIndex("appid"));
			info.applogo = cursor.getString(cursor.getColumnIndex("applogo"));
			info.appname = cursor.getString(cursor.getColumnIndex("appname"));
			info.appprice = cursor.getInt(cursor.getColumnIndex("appprice"));
			info.duan = cursor.getString(cursor.getColumnIndex("duan"));
			info.apkurl = cursor.getString(cursor.getColumnIndex("apkurl"));
			info.mainimg = cursor.getString(cursor.getColumnIndex("mainimg"));
			info.apkBigImg = cursor.getString(cursor.getColumnIndex("big"));
			info.sys = cursor.getString(cursor.getColumnIndex("sys"));
			info.apptype = cursor.getString(cursor.getColumnIndex("apptype"));

			UserInfo.appMainshowOne.add(info);
		}
		cursor.close();
		db.close();
	}

	public void SearchApp(int price) {
		SQLiteDatabase db = this.getReadableDatabase();
		String whereValue = Integer.toString(price);
		Cursor cursor = db.query("apps", new String[] { "appid", "applogo",
				"appname", "appprice", "duan", "apkurl", "mainimg", "big",
				"sys", "appstatus", "apptype" }, "appprice>=?",
				new String[] { whereValue }, null, null, null);
		while (cursor.moveToNext()) {

			UserInfo info = new UserInfo();
			info.appid = cursor.getInt(cursor.getColumnIndex("appid"));
			info.applogo = cursor.getString(cursor.getColumnIndex("applogo"));
			info.appname = cursor.getString(cursor.getColumnIndex("appname"));
			info.appprice = cursor.getInt(cursor.getColumnIndex("appprice"));
			info.duan = cursor.getString(cursor.getColumnIndex("duan"));
			info.apkurl = cursor.getString(cursor.getColumnIndex("apkurl"));
			info.mainimg = cursor.getString(cursor.getColumnIndex("mainimg"));
			info.sys = cursor.getString(cursor.getColumnIndex("sys"));
			info.appstatus = cursor.getInt(cursor.getColumnIndex("appstatus"));
			info.apptype = cursor.getString(cursor.getColumnIndex("apptype"));
			info.apkBigImg = cursor.getString(cursor.getColumnIndex("big"));
			UserInfo.searchApp.add(info);
		}
		cursor.close();
		db.close();
	}

	/** 根据app 查询 mainshow APP精品推荐 */

	public void ReadAppJingPing() {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("apps", new String[] { "appid", "applogo",
				"appname", "appprice", "duan", "apkurl", "mainimg", "big",
				"sys" }, "buyapp=?", new String[] { "tui" }, null, null, null);

		while (cursor.moveToNext()) {
			UserInfo info = new UserInfo();
			info.appid = cursor.getInt(cursor.getColumnIndex("appid"));
			info.applogo = cursor.getString(cursor.getColumnIndex("applogo"));
			info.appname = cursor.getString(cursor.getColumnIndex("appname"));
			info.appprice = cursor.getInt(cursor.getColumnIndex("appprice"));
			info.duan = cursor.getString(cursor.getColumnIndex("duan"));
			info.apkurl = cursor.getString(cursor.getColumnIndex("apkurl"));
			info.mainimg = cursor.getString(cursor.getColumnIndex("mainimg"));
			info.apkBigImg = cursor.getString(cursor.getColumnIndex("big"));
			info.sys = cursor.getString(cursor.getColumnIndex("sys"));
			UserInfo.jpAllshow.add(info);
		}
		cursor.close();
		db.close();
	}

	/** 根据app 查询 mainshow APP 软件 */

	public void ReadAppSort(String apptype) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("apps", new String[] { "appid", "applogo",
				"appname", "appprice", "duan", "apkurl", "mainimg", "big",
				"sys" }, "apptype=? and appstatus=?", new String[] { apptype,
				"1" }, null, null, null);
		while (cursor.moveToNext()) {

			UserInfo info = new UserInfo();
			info.appid = cursor.getInt(cursor.getColumnIndex("appid"));
			info.applogo = cursor.getString(cursor.getColumnIndex("applogo"));
			info.appname = cursor.getString(cursor.getColumnIndex("appname"));
			info.appprice = cursor.getInt(cursor.getColumnIndex("appprice")); // 价格
																				// info.duan
																				// =
			cursor.getString(cursor.getColumnIndex("duan"));
			info.apkurl = cursor.getString(cursor.getColumnIndex("apkurl"));
			info.mainimg = cursor.getString(cursor.getColumnIndex("mainimg"));
			info.sys = cursor.getString(cursor.getColumnIndex("sys"));
			info.apkBigImg = cursor.getString(cursor.getColumnIndex("big"));
			UserInfo.appMainshow.add(info);
		}
		cursor.close();
		db.close();
	}

	/** 根据app 查询所有的APP */

	public void ReadAllApp() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("apps", new String[] { "appid", "applogo",
				"appname", "appprice", "duan", "mainshow", "apkurl", "mainimg",
				"big", "sys", "apptype" }, "apptype=? and appstatus=?",
				new String[] { "soft", "1" }, null, null, null);
		while (cursor.moveToNext()) {

			UserInfo info = new UserInfo();
			info.appid = cursor.getInt(cursor.getColumnIndex("appid"));
			info.applogo = cursor.getString(cursor.getColumnIndex("applogo"));
			info.appname = cursor.getString(cursor.getColumnIndex("appname"));
			info.appprice = cursor.getInt(cursor.getColumnIndex("appprice"));
			info.duan = cursor.getString(cursor.getColumnIndex("duan"));
			info.apkurl = cursor.getString(cursor.getColumnIndex("apkurl"));
			info.mainimg = cursor.getString(cursor.getColumnIndex("mainimg"));
			info.sys = cursor.getString(cursor.getColumnIndex("sys")); // info.apptype
																		// = //
			cursor.getString(cursor.getColumnIndex("apptype"));
			info.apkBigImg = cursor.getString(cursor.getColumnIndex("big"));
			UserInfo.appMainshow.add(info);
		}
		cursor.close();
		db.close();
	}

	public Boolean HaveAdvInfo(int aid) {
		SQLiteDatabase db = this.getReadableDatabase();
		Boolean b = false;
		String whereValue = Integer.toString(aid);
		Cursor cursor = db.query("adv", null, "aid" + "=?",
				new String[] { whereValue }, null, null, null);
		b = cursor.moveToFirst();
		cursor.close();
		db.close();
		return b;
	}

	// TODO WH
	public long insertAdv(UserInfo info) {

		// if (HaveAdvInfo(info.id)) {
		// SQLiteDatabase db = this.getWritableDatabase();
		// String where = "appid" + " = ?";
		// String[] whereValue = { Integer.toString(info.appid) };
		// ContentValues cv = new ContentValues();
		// cv.put("appstatus", info.appstatus);
		// db.update("apps", cv, where, whereValue);
		// return 0;

		// deleteAdv(info.id);
		// }

		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("aid", info.id);
		cv.put("img", info.advimg);
		cv.put("status", info.status);
		cv.put("type", info.type);
		cv.put("video", info.video);
		long row = db.insert("adv", null, cv);
		db.close();
		return row;
	}

	/** 读取视频Video */
	public void ReadAdvVideo() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("adv", new String[] { "aid", "img", "video",
				"type" }, "status=?", new String[] { "1" }, null, null, null);
		while (cursor.moveToNext()) {

			UserInfo info = new UserInfo();
			info.id = cursor.getInt(0);
			info.advimg = cursor.getString(1);
			info.video = cursor.getString(2);
			info.type = cursor.getString(3);
			UserInfo.advVideo.add(info);
		}

		db.close();
		cursor.close();
	}

	// 判断users表中的是否包含某个UserID的记录
	public Boolean HaveProductInfo(int pid) {
		SQLiteDatabase db = this.getReadableDatabase();
		Boolean b = false;
		String whereValue = Integer.toString(pid);
		Cursor cursor = db.query("prduct", null, "pid" + "=?",
				new String[] { whereValue }, null, null, null);
		b = cursor.moveToFirst();
		cursor.close();
		db.close();
		return b;
	}

	// TODO wh
	public long insertP(UserInfo info) {
		// if (HaveProductInfo(info.id)) {
		// SQLiteDatabase db = this.getWritableDatabase();
		// String where = "pid" + " = ?";
		// String[] whereValue = { Integer.toString(info.id) };
		// ContentValues cv = new ContentValues();
		// cv.put("status", info.status);
		// db.update("prduct", cv, where, whereValue);
		// return 0;
		// deletePrduct(info.id, info.hdid);
		// }
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("pid", info.id);
		cv.put("logo", info.logo);
		cv.put("logogray", info.logogray);
		cv.put("name", info.name);
		cv.put("des", info.des);
		cv.put("price", info.price);

		cv.put("weixin", info.weixinprice);
		cv.put("zhifubao", info.zhifubaoprice);
		cv.put("status", info.status);
		cv.put("hdid", info.hdid);
		cv.put("type", info.protype);
		cv.put("max", info.max);
		cv.put("mfinish", info.mfinish);

		cv.put("big", info.probig);
		cv.put("hgid", info.prohuogui);
		cv.put("promainshow", info.promainshow);
		long row = db.insert("prduct", null, cv);
		db.close();
		return row;
	}

	public Cursor selectP() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("prduct", null, null, null, null, null, null);
		cursor.close();
		db.close();
		return cursor;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		cursor.close();
		db.close();
		return cursor;
	}

	// 增加操作
	public long insert(String bookname, String author) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(BOOK_NAME, bookname);
		cv.put(BOOK_AUTHOR, author);
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		return row;
	}

	/** 广告的删除操作 */
	public void deleteAdv(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "aid = ?";
		String[] whereValue = { id + "" };
		db.delete("adv", where, whereValue);
		db.close();
	}

	/** 商品的删除操作 */
	public void deletePrduct(int id, int hdid) {
		SQLiteDatabase db = this.getWritableDatabase();
		// Cursor cursor = db.query("proinfo", new String[] { "bai", "hundrend"
		// },
		// "proid=? and xiang=?", new String[] { pid + "", xiang }, null,
		// null, null);

		String where = "pid = ? and hdid = ?";
		String[] whereValue = { id + "", hdid + "" };
		db.delete("prduct", where, whereValue);
		db.close();
	}

	/*	*//** 应用的删除操作 */
	/*
	 * public void deleteApps(int id) { SQLiteDatabase db =
	 * this.getWritableDatabase(); String where = "appid = ?"; String[]
	 * whereValue = { id + "" }; db.delete("apps", where, whereValue);
	 * db.close(); }
	 */

	/** 删除表中所有的信息 */
	public void deleteTableInfos(String tableName) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(tableName, null, null);
		db.close();
	}

	/** 商品的营养删除操作 */
	public void deleteProinfo(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "proid = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete("proinfo", where, whereValue);
		db.close();
	}

	// 所有面商品表数据 删除操作
	public void deletePrd() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("prduct", null, null);
		db.close();
	}

	// 所有视频表数据 删除操作
	public void deleteAdv() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("adv", null, null);
		db.close();
	}

	// 所有视频表数据 删除操作
	public void deletePinfo() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("proinfo", null, null);
		db.close();
	}

	// 所有视频表数据 删除操作
	public void deleteApl() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("uploads", null, null);
		db.close();
	}

	// 修改操作
	public void update(int id, String bookname, String author) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = BOOK_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put(BOOK_NAME, bookname);
		cv.put(BOOK_AUTHOR, author);
		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
	}

	// TODO WH 修改操作
	public void updateFinish(int id, int hgid, int hdid, int mfinish) {
		SQLiteDatabase db = this.getWritableDatabase();

		String where = "pid = ? and " + "hgid = ? and " + "hdid = ?";
		String[] whereValue = { Integer.toString(id), Integer.toString(hgid),
				Integer.toString(hdid) };
		ContentValues cv = new ContentValues();
		cv.put("mfinish", mfinish);
		db.update("prduct", cv, where, whereValue);
		db.close();
	}

	/** 主页面九个 */
	public void ReadProMain() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("prduct", new String[] { "pid", "name",
				"logo", "logogray", "price", "max", "mfinish", "weixin",
				"zhifubao", "big", "hgid", "des", "hdid", "type" },
				"promainshow=? and status=?", new String[] { "1", "1" }, null,
				null, null);
		// int i =0;
		while (cursor.moveToNext()) {
			// info.id = cursor.getInt(cursor.getColumnIndex("pid"));
			// info.name = cursor.getString(cursor.getColumnIndex("name"));
			UserInfo info = new UserInfo();
			info.id = cursor.getInt(cursor.getColumnIndex("pid"));
			info.name = cursor.getString(cursor.getColumnIndex("name"));
			info.logo = cursor.getString(cursor.getColumnIndex("logo"));
			info.logogray = cursor.getString(cursor.getColumnIndex("logogray"));
			info.des = cursor.getString(cursor.getColumnIndex("des"));
			info.price = cursor.getInt(cursor.getColumnIndex("price"));
			info.max = cursor.getInt(cursor.getColumnIndex("max"));
			info.mfinish = cursor.getInt(cursor.getColumnIndex("mfinish"));
			info.weixinprice = cursor.getInt(cursor.getColumnIndex("weixin"));
			info.zhifubaoprice = cursor.getInt(cursor
					.getColumnIndex("zhifubao"));
			info.probig = cursor.getString(cursor.getColumnIndex("big"));
			info.prohuogui = cursor.getString(cursor.getColumnIndex("hgid"));
			info.hdid = cursor.getInt(cursor.getColumnIndex("hdid"));
			info.protype = cursor.getInt(cursor.getColumnIndex("type"));
			UserInfo.proMainlist.add(info);
			// i++;
			// if(i>3)break;
		}
		cursor.close();
		db.close();
	}

	/** 读取所有的商品 */
	public void ReadProAll() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query("prduct", new String[] { "pid", "name",
				"logo", "logogray", "price", "max", "mfinish", "weixin",
				"zhifubao", "big", "hgid", "hdid", "des","type" }, "status=?",
				new String[] { "1" }, null, null, null);
		while (cursor.moveToNext()) {

			UserInfo info = new UserInfo();
			info.id = cursor.getInt(cursor.getColumnIndex("pid"));
			info.name = cursor.getString(cursor.getColumnIndex("name"));
			info.logo = cursor.getString(cursor.getColumnIndex("logo"));
			info.logogray = cursor.getString(cursor.getColumnIndex("logogray"));
			info.price = cursor.getInt(cursor.getColumnIndex("price"));
			info.max = cursor.getInt(cursor.getColumnIndex("max"));
			info.mfinish = cursor.getInt(cursor.getColumnIndex("mfinish"));
			info.weixinprice = cursor.getInt(cursor.getColumnIndex("weixin"));
			info.zhifubaoprice = cursor.getInt(cursor
					.getColumnIndex("zhifubao"));
			info.probig = cursor.getString(cursor.getColumnIndex("big"));
			info.prohuogui = cursor.getString(cursor.getColumnIndex("hgid"));
			info.hdid = cursor.getInt(cursor.getColumnIndex("hdid"));
			info.des = cursor.getString(cursor.getColumnIndex("des"));
			info.protype = cursor.getInt(cursor.getColumnIndex("type"));
			UserInfo.proAlllist.add(info);
		}
		cursor.close();
		db.close();
	}

	/** 读取饮料的商品 */
	public void ReadDrinkAll() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query("prduct", new String[] { "pid", "name",
				"logo", "logogray", "price", "weixin", "zhifubao", "big",
				"max", "mfinish", "hgid", "hdid","des", "type" },
				"status=? and type=?", new String[] { "1", "1" }, null, null,
				null);
		while (cursor.moveToNext()) {

			UserInfo info = new UserInfo();
			info.id = cursor.getInt(cursor.getColumnIndex("pid"));
			info.name = cursor.getString(cursor.getColumnIndex("name"));
			info.logo = cursor.getString(cursor.getColumnIndex("logo"));
			info.logogray = cursor.getString(cursor.getColumnIndex("logogray"));
			info.price = cursor.getInt(cursor.getColumnIndex("price"));
			info.weixinprice = cursor.getInt(cursor.getColumnIndex("weixin"));
			info.zhifubaoprice = cursor.getInt(cursor
					.getColumnIndex("zhifubao"));
			info.probig = cursor.getString(cursor.getColumnIndex("big"));
			info.max = cursor.getInt(cursor.getColumnIndex("max"));
			info.mfinish = cursor.getInt(cursor.getColumnIndex("mfinish"));
			info.prohuogui = cursor.getString(cursor.getColumnIndex("hgid"));
			info.hdid = cursor.getInt(cursor.getColumnIndex("hdid"));
			info.des = cursor.getString(cursor.getColumnIndex("des"));
			info.protype = cursor.getInt(cursor.getColumnIndex("type"));
			UserInfo.proDrinklist.add(info);
		}
		cursor.close();
		db.close();
	}

	/** 读取所有的商品 */
	public void ReadSnakeAll() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query("prduct", new String[] { "pid", "name",
				"logo", "logogray", "price", "max", "mfinish", "weixin",
				"zhifubao", "big", "hgid", "hdid","des", "type" },
				"status=? and type=?", new String[] { "1", "2" }, null, null,
				null);
		while (cursor.moveToNext()) {

			UserInfo info = new UserInfo();
			info.id = cursor.getInt(cursor.getColumnIndex("pid"));
			info.name = cursor.getString(cursor.getColumnIndex("name"));
			info.logo = cursor.getString(cursor.getColumnIndex("logo"));
			info.logogray = cursor.getString(cursor.getColumnIndex("logogray"));
			info.price = cursor.getInt(cursor.getColumnIndex("price"));
			info.max = cursor.getInt(cursor.getColumnIndex("max"));
			info.mfinish = cursor.getInt(cursor.getColumnIndex("mfinish"));
			info.weixinprice = cursor.getInt(cursor.getColumnIndex("weixin"));
			info.zhifubaoprice = cursor.getInt(cursor
					.getColumnIndex("zhifubao"));
			info.probig = cursor.getString(cursor.getColumnIndex("big"));
			info.prohuogui = cursor.getString(cursor.getColumnIndex("hgid"));
			info.hdid = cursor.getInt(cursor.getColumnIndex("hdid"));
			info.des = cursor.getString(cursor.getColumnIndex("des"));
			info.protype = cursor.getInt(cursor.getColumnIndex("type"));
			UserInfo.proSnakelist.add(info);
		}
		cursor.close();
		db.close();
	}

	// info.appid= cursor.getInt(cursor.getColumnIndex("appid"));
	public List<UserInfo> ReadProAllById() {

		SQLiteDatabase db = this.getReadableDatabase();
		List<UserInfo> userInfoProList = new ArrayList<UserInfo>();
		Cursor cursor = db.query("prduct", new String[] { "pid", "name",
				"logo", "logogray", "price", "max", "mfinish", "weixin",
				"zhifubao", "img", "hgid", "hdid","des"}, null, null, null, null,
				null);
		while (cursor.moveToNext()) {
			UserInfo info = new UserInfo();
			info.id = cursor.getInt(cursor.getColumnIndex("pid"));
			info.name = cursor.getString(cursor.getColumnIndex("name"));
			info.logo = cursor.getString(cursor.getColumnIndex("logo"));
			info.logogray = cursor.getString(cursor.getColumnIndex("logogray"));
			info.price = cursor.getInt(cursor.getColumnIndex("price"));
			info.max = cursor.getInt(cursor.getColumnIndex("max"));
			info.mfinish = cursor.getInt(cursor.getColumnIndex("mfinish"));
			info.weixinprice = cursor.getInt(cursor.getColumnIndex("weixin"));
			info.zhifubaoprice = cursor.getInt(cursor
					.getColumnIndex("zhifubao"));
			info.probig = cursor.getString(cursor.getColumnIndex("big"));
			info.prohuogui = cursor.getString(cursor.getColumnIndex("hgid"));
			info.hdid = cursor.getInt(cursor.getColumnIndex("hdid"));
			info.protype = cursor.getInt(cursor.getColumnIndex("type"));
			info.des = cursor.getString(cursor.getColumnIndex("des"));
			userInfoProList.add(info);
		}
		cursor.close();
		db.close();
		return userInfoProList;
	}

	public Boolean HaveUpInfo(int upstatus) {
		SQLiteDatabase db = this.getReadableDatabase();
		Boolean b = false;
		String whereValue = Integer.toString(1);

		Cursor cursor = db.query("uploads", null, "upstatus" + "=?",
				new String[] { whereValue }, null, null, null);
		b = cursor.moveToFirst();
		cursor.close();
		db.close();
		return b;
	}

}