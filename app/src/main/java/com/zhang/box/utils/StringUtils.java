package com.zhang.box.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/** String 工具类 */
public abstract class StringUtils {

	private static DecimalFormat fmt = new DecimalFormat("##,###,###,##0.00");

	/* 地球半径 */
	private static final double EARTH_RADIUS = 6378137.0;

	private StringUtils() {
		throw new InstantiationError("工具类无法实例化");
	}

	/** 获取时间戳字符串 */
	public static String timeStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * 
	 * @return
	 */
	public static int randomNum() {
		int num = 10000000;
		int TimeNum = (int) ((Math.random() * 9 + 1) * num);
		return TimeNum;
	}

	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String getRandonInt(int lenth) {
		String str = "";
		for (int i = 0; i < lenth; i++) {
			str += String.valueOf((int) (Math.random() * 9));
		}
		return str;
	}

	/** 判断字符串是否为空，即为null或"" */
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}

	/** 判断字符串是否不为空，即不为null且不为"" */
	public static boolean isNotEmpty(String str) {
		return (!(isEmpty(str)));
	}

	/** 判断字符串是否为空白，即为null或为" " */
	public static boolean isBlank(String str) {
		return ((str == null) || (str.trim().length() == 0));
	}

	/** 判断字符串是否不为空白，即不为null且不为" " */
	public static boolean isNotBlank(String str) {
		return (!(isBlank(str)));
	}

	/** 字符串转为int */
	public static int StringToInt(String str) {
		int result = 0;
		if (null == str) {
			return 0;
		}
		try {
			result = Integer.parseInt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/** 将double按默认格式格式化为字符串 */
	public static String formateStr(Double str) {
		return fmt.format(str);
	}

	/** 将double按默认格式格式化为字符串 */
	public static String formateStr(int str) {
		return fmt.format(str);
	}

	/** 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/** 根据两个经纬度获取距离 米 */
	public static double gpsDistance(double lat_a, double lng_a, double lat_b,
			double lng_b) {
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/** 获取根目录路径 */
	public static String getSDPath() {
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		// 如果有sd卡，则返回sd卡的目录
		if (hasSDCard) {
			return Environment.getExternalStorageDirectory().getPath() + "/";
		} else
			// 如果没有sd卡，则返回存储目录
			return Environment.getDownloadCacheDirectory().getPath() + "/";
	}

	/** 判断是否是当前页面 */
	public static String getRunningActivityName(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
				.getClassName();
		return runningActivity;
	}

	/** 设置字体颜色 */
	public static void setFont(TextView textView, String textViewStr,int start,int end) {
		SpannableStringBuilder builder = new SpannableStringBuilder(textView
				.getText().toString());
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		builder.setSpan(redSpan, start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(builder);
	}
}
