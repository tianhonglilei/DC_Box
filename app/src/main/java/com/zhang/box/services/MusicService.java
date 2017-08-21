package com.zhang.box.services;

import java.util.Random;
import com.zhang.box.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

	private MediaPlayer mp;
	private static int[] ids = { R.raw.s0, R.raw.s1, R.raw.s2 };

	public MusicService() {
		super();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Random random = new Random();
		int a = random.nextInt(2);
		mp = MediaPlayer.create(this, ids[a]);
		mp.setLooping(false);
		Log.e("wh", "MusicService--->onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mp.start();
		Log.e("wh", "MusicService--->onStart");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.stop();
		Log.e("wh", "MusicService--->onDestroy");
	}

}
