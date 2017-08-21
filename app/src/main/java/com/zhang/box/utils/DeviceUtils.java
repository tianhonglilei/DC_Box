package com.zhang.box.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/** 获取手机信息工具类 */
public class DeviceUtils {

	/** 获取应用程序的IMEI号 */
	public static String getIMEI(Context context) {
		String imei = "";
		if (context == null) {
			imei = "867223020934767"; // 随便搞一个假的
		}
		TelephonyManager telecomManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telecomManager.getDeviceId();
		return imei;
	}
	
	/** 获取应用程序的SIM号 */
	public static String getSim(Context context) {
		String sim = "";
		if (context == null) {
			sim = "867223020934766"; // 随便搞一个假的
		}
		TelephonyManager telecomManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		sim = telecomManager.getSubscriberId();
		return sim;
	}

	/** 获取设备的系统版本号 */
	public static int getDeviceSDK() {
		int sdk = android.os.Build.VERSION.SDK_INT;
		return sdk;
	}

	/** 获取设备的型号 */
	public static String getDeviceName() {
		String model = android.os.Build.MODEL;
		return model;
	}
	
	/** 根据一个包名启动一个程序 */
	public static void startAPP(String appPackageName,Context context) {
		try {
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(
					appPackageName);
			context.startActivity(intent);
		} catch (Exception e) {
			//Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
		}
	}
	
	
	/**
	 * 方法描述：判断某一应用是否正在运行
	 * 
	 * @param context
	 *            上下文
	 * @param packageName
	 *            应用的包名
	 * @return true 表示正在运行，false表示没有运行
	 */
	public static boolean isAppRunning(Context context, String packageName) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
		if (list.size() <= 0) {
			return false;
		}
		for (ActivityManager.RunningTaskInfo info : list) {
			if (info.baseActivity.getPackageName().equals(packageName)) {
				return true;
			}
		}
		return false;
	}
	
	/** 取得操作系统版本号 */
	public static String getOSVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/** 检测输入的手机号 */
	public static boolean checkPhoneNum(String phoneNum) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNum);
		return m.matches();
	}
	
	/**  取得应用的版本号  name*/
	public static String getAPKVersionName(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					ctx.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**  取得应用的版本号 code*/
	public static int getAPKVersionCode(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					ctx.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
}