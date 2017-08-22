package com.example.zzq.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserInfo implements Serializable {

	/**
	 * 实体类
	 */
	private static final long serialVersionUID = 1L;

	public static String CESHI_id = "ceshiid";

	// 上传
	public static final String UPSTATUS = "status";
	public static int upstatus;
	public static String upname = "";

	// 下载 版本更新
	public static int downVersionCode;
	public static String downApkUrl;

	// 广告
	public static String ADV_id = "advid";
	public static String ADV_status = "advstatus";
	public static String ADV_img = "advimg";
	public static String ADV_video = "advvideo";
	public static String ADV_type = "advtype";

	public String video;
	public String videoLocation;
	public String type;
	public String advimg;

	public static String hdID = "1";
	// 商品
	public static final String PRO_ID = "id";

	public static final String PRO_LOGO = "logo";
	public static final String PRO_NAME = "name";
	public static final String PRO_DES = "des";
	public static final String PRO_LOGO_GRAY = "logogray";
	public static final String PRO_PRICE = "price";
	public static final String PRO_STATUS = "status";
	public static final String PRO_MAINSHOW = "mainshow";
	public static final String PRO_BIG = "big";

	public static final String PRO_PROID = "proid";
	public static final String PRO_BAI = "bai";
	public static final String PRO_HUNDREND = "hundrend";
	public static final String PRO_XIANG = "xiang";

	// 营养 proid bai  hundrend xiang
	public int proid;
	public String bai;
	public String hundrend;
	public String xiang;
	public int protype;
	public int id;
	public String logo; // 用户id
	public String name;
	public String des;
	public String logogray;
	public int price;
	public int weixinprice;
	public int zhifubaoprice;
	public int status;
	public int hdid;
	public int promainshow;
	public String probig;
	public String prohuogui;

	// App
	public static final String APPLOGO = "logo";
	public static final String APPNAME = "name";
	public static final String APPPRICE = "price";
	public static final String APPID = "id";
	public static final String APPDUAN = "duan";
	public static final String APPMAINSHOW = "mainshow";
	public static final String APPAPKURL = "apkurl";
	public static final String APPMAINIMG = "mainimg";
	public static final String APPSHIJIAN = "shijian";
	public static final String APPSYS = "sys";
	public static final String APPSTATUS = "status";
	public static final String APPTYPE = "type";
	public static final String BUYAPP = "buyapp";
	public int appid;
	public String applogo; // 用户id
	public String appname;
	public int appprice;
	public String duan;
	public int mainshow;
	public int max;
	public int mfinish;
	public String apkurl;
	public String apkBigImg;
	public String mainimg;
	public String shijian;
	public String sys;
	
	// public static final String APPSTATUS = "status";
	public int appstatus;
	public String apptype;
	public String buyapp;
	// appid,applogo,appname,appprice,duan,mainshow,apkurl,mainimg,shijian,sys,appstatus,apptype

	/**
	 * jp 表 apkurl,big,duan,id,logo,name,price,status,sys,type
	 */
	public static final String JPLOGO = "logo";
	public static final String JPNAME = "name";
	public static final String JPPRICE = "price";
	public static final String JPID = "id";
	public static final String JPDUAN = "duan";
	public static final String JPAPKURL = "apkurl";
	public static final String JPSYS = "sys";
	public static final String JPSTATUS = "status";
	public static final String JPTYPE = "type";
	public static final String JPBIG = "big";
	public int jpid;
	public String jplogo; // 用户id
	public String jpname;
	public int jpprice;
	public String jpduan;
	public String jpapkurl;
	public String jpsys;
	public int jpstatus;
	public String jptype;
	public String jpbig;

	// 主页面商品
	public static String sucessTitle;
	public static String sucessLogo;
	public static String desType;

	public static int fromActivity;

	// 所有应用商品的
	public static int desAppId;

	public static int isLoad = 0;

	//
	public static List<UserInfo> proMainlist = new ArrayList<UserInfo>();
	public static List<UserInfo> proAlllist = new ArrayList<UserInfo>();
	public static List<UserInfo> proSnakelist = new ArrayList<UserInfo>();
	public static List<UserInfo> proDrinklist = new ArrayList<UserInfo>();
	public static List<UserInfo> proAlllistById = new ArrayList<UserInfo>();
	
	public static List<UserInfo> advVideo = new ArrayList<UserInfo>();
 
	public static List<UserInfo> appMainshowOne = new ArrayList<UserInfo>();
	public static List<UserInfo> appMainshow = new ArrayList<UserInfo>();
	public static List<UserInfo> jpAllshow = new ArrayList<UserInfo>();
	public static List<UserInfo> searchApp = new ArrayList<UserInfo>();
	public static List<UserInfo> proInfos = new ArrayList<UserInfo>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getLogogray() {
		return logogray;
	}

	public void setLogogray(String logogray) {
		this.logogray = logogray;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public String getApplogo() {
		return applogo;
	}

	public void setApplogo(String applogo) {
		this.applogo = applogo;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public int getAppprice() {
		return appprice;
	}

	public void setAppprice(int appprice) {
		this.appprice = appprice;
	}

	public UserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
