package com.zhang.box.constants;

/**
 * 
 * @author wang
 * 
 *         接口
 * 
 */
public class Constants {

	public static String NEW_NOTICE_ACTION = "com.zhang.box.action.NEW_NOTICE_ACTION";

	
	/****************************  基础网段  ***************************************/
	
	/** 基本网址 */
	public static String basesUrl = "60.205.218.33";
	
	
	/** 李鸿烈 基本接口 */
	public static String baseUrlLHL = "60.205.218.33";

	/** 张振强 基本接口 */
//	public static String baseUrlZZQ = "101.200.84.234";
	
	
	/****************************  程序使用的常用接口  ******************************/
	
	/** LoadingActivity 和 LoadingService 程序第一次进入从服务器获取所有的数据 */
	public static String LoadActivityUrlLHL = "http://"+ basesUrl+ "/boxapp/?c=heart&m=box_heart";
	
	
	/** LoadingService 程序更新下载完成后的向服务器发送一个通知 */
	public static String LoadingServiceUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_updatefinish";
	
	
	/** ConfigActivity 下载最新包 */
	public static String ConfigActivityUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=updateapk";
	
	/** ProActivity and huodaoActivity 商品补货获取商品信息接口 */
	public static String ProActivityUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=machinehuodao";
	
	/** proAdapter2 补了多少商品数量上传接口 */
	public static String proAdapter2UrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=updatehuodao";
	
	
	/*************************  点创二维码，支付，支付成功状态   *************************/
	
	/** DesProActivity之点创二维码李鸿烈接口 */
	public static String dainchaungQRUrlLHL = "http://quyingyoung.dc-box.com/?c=welcome&m=zhifu_app&tradeno=";
	
	/** DesProActivity之点创二维码张振强接口 */
	public static String dainchaungQRUrlZZQ = "http://quyingyoung.dc-box.com/?c=welcome&m=zhifu_app&tradeno=";
	
	/** DesProActivity之点创支付李鸿烈接口 */
	public static String dainchaungPayUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_apphuodaostatus";
	
	/** DesProActivity之点创支付张振强接口 */
	public static String dainchaungPayUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_apphuodaostatus";
	
	/** DesProActivity之点创支付成功后  李鸿烈  接口 */
	public static String dainchaungStatusUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_appchuhuosuccess";
	
	/** DesProActivity之点创支付成功后  张振强   接口 */
	public static String dainchaungStatusUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_appchuhuosuccess";
	
	
	/*************************  微信二维码，支付，支付成功状态   *************************/
	
	/** DesProActivity之微信二维码李鸿烈接口 */
	public static String weixinQRUrlLHL = "http://"+basesUrl+"/weixin/example/native.php";
	
	
	//TODO WHWH
	/** DesProActivity之微信二维码张振强接口 */
	public static String weixinQRUrlZZQ = "http://www.dc-box.com/weixin/example/native.php";
	
	/** DesProActivity之微信支付李鸿烈接口 */
	public static String weixinPayUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_weixinuodao";
	
	/** DesProActivity之微信支付张振强接口 */
	public static String weixinPayUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_weixinuodao";
	
	/** DesProActivity之微信支付成功后  李鸿烈  接口 */
	public static String weixinStatusUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_weixinhuodaostatus";
	
	/** DesProActivity之微信支付成功后  张振强   接口 */
	public static String weixinStatusUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_weixinhuodaostatus";
	
	
	/************************  支付宝二维码，支付，支付成功状态   ********************/
	/** DesProActivity之支付宝二维码李鸿烈接口 */
	public static String zhifubaoQRUrlLHL = "http://"+basesUrl+"/f2f/f2fpay/qrpay_test.php";
	
	/** DesProActivity之支付宝二维码张振强接口 */
	public static String zhifubaoQRUrlZZQ = "http://"+basesUrl+"/f2f/f2fpay/qrpay_test.php";
	
	/** DesProActivity之支付宝支付李鸿烈接口 */
	public static String zhifubaoPayUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_zhifubaohuodao";
	
	/** DesProActivity之支付宝支付张振强接口 */
	public static String zhifubaoPayUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_zhifubaohuodao";
	
	/** DesProActivity之支付宝支付成功后  李鸿烈  接口 */
	public static String zhifubaoStatusUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_zhifubaohuodaostatus";
	
	//TODO WHWH
	/** DesProActivity之支付宝支付成功后  张振强  接口 */
	public static String zhifubaoStatusUrlZZQ = "http://quyingyoung.dc-box.com/?c=welcome&m=box_zhifubaohuodaostatus";

}
