package com.zhang.box.utils;

import java.util.Hashtable;
import android.content.Context;
import android.graphics.Typeface;

/**
 * 修改字体样式
 * 
 * @author wang
 * 
 *         note: 需要到系统目录在：C:\Windows\Fonts）选择一份字体文件复制到 项目的assets（资产文件目录下）
 */

public class FontsTypeUtils {

	public FontsTypeUtils() {
		throw new UnsupportedOperationException("FontsTypeUtils修改字体样式不能实例化");
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
