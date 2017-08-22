package com.zhang.box.services;

import com.avm.serialport_142.MainHandler;
import com.zhang.box.ConfigActivity;
import com.zhang.box.GallaryActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MachineBrocastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		/** 门开关信息 */
		if (action.equals("com.avm.serialport.door_state")) {

			boolean isOpen = MainHandler.isDoorOpen();//获取机器开门信息
			if (isOpen) {
				Log.e("whwh", "门已打开");
				Intent intentActivity = new Intent(context,
						ConfigActivity.class);
				intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intentActivity);
			} else {
				Log.e("whwh", "门已关闭");
				Intent intentActivity = new Intent(context,
						GallaryActivity.class);
				intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intentActivity);
			}
		}
	}
}
