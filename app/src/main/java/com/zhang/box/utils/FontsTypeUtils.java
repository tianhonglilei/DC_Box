package com.zhang.box.utils;

import java.util.Hashtable;
import android.content.Context;
import android.graphics.Typeface;

/**
 * �޸�������ʽ
 * 
 * @author wang
 * 
 *         note: ��Ҫ��ϵͳĿ¼�ڣ�C:\Windows\Fonts��ѡ��һ�������ļ����Ƶ� ��Ŀ��assets���ʲ��ļ�Ŀ¼�£�
 */

public class FontsTypeUtils {

	public FontsTypeUtils() {
		throw new UnsupportedOperationException("FontsTypeUtils�޸�������ʽ����ʵ����");
	}

	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface get(Context c, String assetPath) {
		synchronized (cache) {
			if (!cache.containsKey(assetPath)) {
				try {
					Typeface t = Typeface.createFromAsset(c.getAssets(),
							assetPath);
					cache.put(assetPath, t);
				} catch (Exception e) {
					return null;
				}
			}
			return cache.get(assetPath);
		}
	}
}
