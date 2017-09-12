package com.zhang.box.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhang.box.LoadingActivity;

/**
 * Created by lilei on 2017/9/12.
 */

public class AutoStartAppReceiver extends BroadcastReceiver {

    public AutoStartAppReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Intent i = new Intent(context, LoadingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }


    }
}
