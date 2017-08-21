package com.zhang.box.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.TextView;

/**
 * 字体修改 工具类
 * 
 * @author wang
 * 
 */
public class fontUtils {

	public fontUtils() {
		throw new UnsupportedOperationException("字体大小和颜色修改工具类不能实例化");
	}

	public static void setFontColor(Context context, TextView textView,
			int color) {
		Resources resource = (Resources) context.getResources();
		ColorStateList csl = (ColorStateList) resource.getColorStateList(color);
		if (csl != null) {
			textView.setTextColor(csl);
		}
		textView.setTextSize(color);
	}

	public static void setFontSize(TextView textView, float size) {
	}

	/** 设置字体的颜色和 大小 */
	public static void setFontColorAndSize(Context context, Button btn,
			int color, float size) {

		Resources resource = (Resources) context.getResources();
		ColorStateList csl = (ColorStateList) resource.getColorStateList(color);
		if (csl != null) {
			btn.setTextColor(csl);
		}

		btn.setTextSize(size);
	}

	/** 设置字体的颜色和 大小 多种按钮的设置 */
	public static void setFontColorAndSize(Context context, Button btn1,
			Button btn2, Button btn3, int color, float size) {

		Resources resource = (Resources) context.getResources();
		ColorStateList csl = (ColorStateList) resource.getColorStateList(color);
		if (csl != null) {
			btn1.setTextColor(csl);
			btn2.setTextColor(csl);
			btn3.setTextColor(csl);
		}

		btn1.setTextSize(size);
		btn2.setTextSize(size);
		btn3.setTextSize(size);
	}
}
