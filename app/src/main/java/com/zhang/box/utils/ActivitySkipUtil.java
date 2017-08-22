package com.zhang.box.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/** 跳转页面工具类 */
public class ActivitySkipUtil {
	
	public ActivitySkipUtil() {
		throw new UnsupportedOperationException("ActivitySkipUtil不能实例化");
	}

	/**
	 * 功能描述:在Activity里两个Activity之间的跳转(不携带任何数据)
	 * 
	 * @param activity
	 *            发起跳转的Activity实例
	 */
	public static void skipAnotherActivity(Activity activity,
			Class<? extends Activity> cls, Boolean isFinish) {
		Intent intent = new Intent(activity, cls);
		activity.startActivity(intent);
		if (isFinish) {
			activity.finish();
		}
	}

	

	/**
	 * 功能描述:在适配器里两个Activity之间的的跳转(不携带任何数据)
	 * 
	 * @param context
	 *            发起跳转的Activity实例
	 */

	public static void skipAnotherActivity(Context context,
			Class<? extends Activity> cls, boolean isFinish) {

		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
		if (isFinish) {
			((Activity) context).finish();
		}
	}
	
	
	/**
	 * 功能描述：带数据的Activity之间的跳转
	 * 
	 * @param activity
	 *            发起跳转的Activity实例
	 * @param cls
	 *            目标Activity实例
	 * @param hashMap
	 *            携带参数的集合
	 */
	public static void skipAnotherActivity(Activity activity,
			Class<? extends Activity> cls,
			HashMap<String, ? extends Object> hashMap) {
		Intent intent = new Intent(activity, cls);
		Iterator<?> iterator = hashMap.entrySet().iterator();
		while (iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, Object> entry = (Entry<String, Object>) iterator
					.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				intent.putExtra(key, (String) value);
			}
			if (value instanceof Boolean) {
				intent.putExtra(key, (Boolean) value);
			}
			if (value instanceof Integer) {
				intent.putExtra(key, (Integer) value);
			}
			if (value instanceof Float) {
				intent.putExtra(key, (Float) value);
			}
			if (value instanceof Double) {
				intent.putExtra(key, (Double) value);
			}
		}

		activity.startActivity(intent);
	}

	/**
	 * 功能描述：带数据的Activity之间的跳转
	 * 
	 * @param context
	 *            发起跳转的Activity实例
	 * @param cls
	 *            目标Activity实例
	 * @param hashMap
	 *            携带参数的集合
	 */
	public static void skipAnotherActivity(Context context,
			Class<? extends Activity> cls,HashMap<String, ? extends Object> hashMap) {
		Intent intent = new Intent(context, cls);
		Iterator<?> iterator = hashMap.entrySet().iterator();
		while (iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, Object> entry = (Entry<String, Object>) iterator
					.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				intent.putExtra(key, (String) value);
			}
			if (value instanceof Boolean) {
				intent.putExtra(key, (Boolean) value);
			}
			if (value instanceof Integer) {
				intent.putExtra(key, (Integer) value);
			}
			if (value instanceof Float) {
				intent.putExtra(key, (Float) value);
			}
			if (value instanceof Double) {
				intent.putExtra(key, (Double) value);
			}
		}

		context.startActivity(intent);
	}
}
