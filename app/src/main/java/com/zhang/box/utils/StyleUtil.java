package com.zhang.box.utils;

import com.zhang.box.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * 窗口风格通用类
 * 
 * @author wang
 */
public class StyleUtil {

	private static View view;

	/** 带有标题栏的 */
	public static void customStyle(final Activity activity, int contentViewId,
			String title) {
		// 如果有父Activity，如：是TAB中的一页，则直接设置ContentView
		if (activity.getParent() != null) {
			activity.setContentView(contentViewId);
			return;
		}
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.setContentView(contentViewId);
		view = LayoutInflater.from(activity).inflate(R.layout.top, null);
		TextView text = (TextView) activity.findViewById(R.id.top_title);
		text.setText(title);

		Button m_back_button = (Button) activity
				.findViewById(R.id.gua_top_back);// gua_top_back
		m_back_button.setVisibility(View.VISIBLE);
		m_back_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}
}
