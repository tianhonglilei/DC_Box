package com.zhang.box.services;

import com.avm.serialport_142.MainHandler;
import com.zhang.box.ProSucessActivity;
import com.zhang.box.utils.ActivitySkipUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GoodsOutBrocastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		// com.avm.serialport.OUT_GOODS
		if (action.equals("com.avm.serialport.OUT_GOODS")) {
			String getGoodsTalk = MainHandler.getTranResult();
			Log.e("whwhwh", "getGoodsTalk===" + getGoodsTalk);
			String code = getGoodsTalk.substring(17, 18);
			Log.e("whwhwh", "code===" + code);
			if ("0".equals(code)) {
				Log.e("whwhwh", "支付成功了!");
				ActivitySkipUtil.skipAnotherActivity(context,
						ProSucessActivity.class, true);
			}
		}
	}
}
