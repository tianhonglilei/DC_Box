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

		/** �ſ�����Ϣ */
		if (action.equals("com.avm.serialport.door_state")) {

			boolean isOpen = MainHandler.isDoorOpen();//��ȡ����������Ϣ
			if (isOpen) {
				Log.e("whwh", "���Ѵ�");
				Intent intentActivity = new Intent(context,
						ConfigActivity.class);
				intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intentActivity);
			} else {
				Log.e("whwh", "���ѹر�");
				Intent intentActivity = new Intent(context,
						GallaryActivity.class);
				intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intentActivity);
			}
		}
	}
}
