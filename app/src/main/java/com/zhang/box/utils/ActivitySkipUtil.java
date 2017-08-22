package com.zhang.box.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/** ��תҳ�湤���� */
public class ActivitySkipUtil {
	
	public ActivitySkipUtil() {
		throw new UnsupportedOperationException("ActivitySkipUtil����ʵ����");
	}

	/**
	 * ��������:��Activity������Activity֮�����ת(��Я���κ�����)
	 * 
	 * @param activity
	 *            ������ת��Activityʵ��
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
	 * ��������:��������������Activity֮��ĵ���ת(��Я���κ�����)
	 * 
	 * @param context
	 *            ������ת��Activityʵ��
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
	 * ���������������ݵ�Activity֮�����ת
	 * 
	 * @param activity
	 *            ������ת��Activityʵ��
	 * @param cls
	 *            Ŀ��Activityʵ��
	 * @param hashMap
	 *            Я�������ļ���
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
	 * ���������������ݵ�Activity֮�����ת
	 * 
	 * @param context
	 *            ������ת��Activityʵ��
	 * @param cls
	 *            Ŀ��Activityʵ��
	 * @param hashMap
	 *            Я�������ļ���
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
