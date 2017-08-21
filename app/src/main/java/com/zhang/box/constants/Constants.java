package com.zhang.box.constants;

/**
 * 
 * @author wang
 * 
 *         �ӿ�
 * 
 */
public class Constants {

	public static String NEW_NOTICE_ACTION = "com.zhang.box.action.NEW_NOTICE_ACTION";

	
	/****************************  ��������  ***************************************/
	
	/** ������ַ */
	public static String basesUrl = "60.205.218.33";
	
	
	/** ����� �����ӿ� */
	public static String baseUrlLHL = "60.205.218.33";

	/** ����ǿ �����ӿ� */
//	public static String baseUrlZZQ = "101.200.84.234";
	
	
	/****************************  ����ʹ�õĳ��ýӿ�  ******************************/
	
	/** LoadingActivity �� LoadingService �����һ�ν���ӷ�������ȡ���е����� */
	public static String LoadActivityUrlLHL = "http://"+ basesUrl+ "/boxapp/?c=heart&m=box_heart";
	
	
	/** LoadingService �������������ɺ�������������һ��֪ͨ */
	public static String LoadingServiceUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_updatefinish";
	
	
	/** ConfigActivity �������°� */
	public static String ConfigActivityUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=updateapk";
	
	/** ProActivity and huodaoActivity ��Ʒ������ȡ��Ʒ��Ϣ�ӿ� */
	public static String ProActivityUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=machinehuodao";
	
	/** proAdapter2 ���˶�����Ʒ�����ϴ��ӿ� */
	public static String proAdapter2UrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=updatehuodao";
	
	
	/*************************  �㴴��ά�룬֧����֧���ɹ�״̬   *************************/
	
	/** DesProActivity֮�㴴��ά������ҽӿ� */
	public static String dainchaungQRUrlLHL = "http://quyingyoung.dc-box.com/?c=welcome&m=zhifu_app&tradeno=";
	
	/** DesProActivity֮�㴴��ά������ǿ�ӿ� */
	public static String dainchaungQRUrlZZQ = "http://quyingyoung.dc-box.com/?c=welcome&m=zhifu_app&tradeno=";
	
	/** DesProActivity֮�㴴֧������ҽӿ� */
	public static String dainchaungPayUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_apphuodaostatus";
	
	/** DesProActivity֮�㴴֧������ǿ�ӿ� */
	public static String dainchaungPayUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_apphuodaostatus";
	
	/** DesProActivity֮�㴴֧���ɹ���  �����  �ӿ� */
	public static String dainchaungStatusUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_appchuhuosuccess";
	
	/** DesProActivity֮�㴴֧���ɹ���  ����ǿ   �ӿ� */
	public static String dainchaungStatusUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_appchuhuosuccess";
	
	
	/*************************  ΢�Ŷ�ά�룬֧����֧���ɹ�״̬   *************************/
	
	/** DesProActivity֮΢�Ŷ�ά������ҽӿ� */
	public static String weixinQRUrlLHL = "http://"+basesUrl+"/weixin/example/native.php";
	
	
	//TODO WHWH
	/** DesProActivity֮΢�Ŷ�ά������ǿ�ӿ� */
	public static String weixinQRUrlZZQ = "http://www.dc-box.com/weixin/example/native.php";
	
	/** DesProActivity֮΢��֧������ҽӿ� */
	public static String weixinPayUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_weixinuodao";
	
	/** DesProActivity֮΢��֧������ǿ�ӿ� */
	public static String weixinPayUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_weixinuodao";
	
	/** DesProActivity֮΢��֧���ɹ���  �����  �ӿ� */
	public static String weixinStatusUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_weixinhuodaostatus";
	
	/** DesProActivity֮΢��֧���ɹ���  ����ǿ   �ӿ� */
	public static String weixinStatusUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_weixinhuodaostatus";
	
	
	/************************  ֧������ά�룬֧����֧���ɹ�״̬   ********************/
	/** DesProActivity֧֮������ά������ҽӿ� */
	public static String zhifubaoQRUrlLHL = "http://"+basesUrl+"/f2f/f2fpay/qrpay_test.php";
	
	/** DesProActivity֧֮������ά������ǿ�ӿ� */
	public static String zhifubaoQRUrlZZQ = "http://"+basesUrl+"/f2f/f2fpay/qrpay_test.php";
	
	/** DesProActivity֧֮����֧������ҽӿ� */
	public static String zhifubaoPayUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_zhifubaohuodao";
	
	/** DesProActivity֧֮����֧������ǿ�ӿ� */
	public static String zhifubaoPayUrlZZQ = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_zhifubaohuodao";
	
	/** DesProActivity֧֮����֧���ɹ���  �����  �ӿ� */
	public static String zhifubaoStatusUrlLHL = "http://"+basesUrl+"/boxapp/?c=welcome&m=box_zhifubaohuodaostatus";
	
	//TODO WHWH
	/** DesProActivity֧֮����֧���ɹ���  ����ǿ  �ӿ� */
	public static String zhifubaoStatusUrlZZQ = "http://quyingyoung.dc-box.com/?c=welcome&m=box_zhifubaohuodaostatus";

}
