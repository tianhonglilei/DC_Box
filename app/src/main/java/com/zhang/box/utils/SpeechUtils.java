package com.zhang.box.utils;

import java.io.IOException;
import java.util.Random;
import android.content.Context;
import android.media.MediaPlayer;
import com.zhang.box.R;

/**
 * 语音提示 工具类
 * 
 * @author wang
 * 
 */
public class SpeechUtils {

	private static int[] ids = { R.raw.s0, R.raw.s1, R.raw.s2 };

	public SpeechUtils() {
		throw new UnsupportedOperationException("SpeechUtils语音提示工具类不能实例化");
	}

	/** 单一的语音提示方法 */
	public static void setSpeechSingle(Context context, int rawPath) {
		MediaPlayer mp = MediaPlayer.create(context, rawPath);
		mp.setLooping(false);
		mp.start();
	}

	/** 随机的读取语音提示方法 */
	public static void setSpeechRandom(Context context) {
		Random random = new Random();
		int a = random.nextInt(2);
		MediaPlayer mp = MediaPlayer.create(context, ids[a]);
		mp.setLooping(false);
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mp.start();
	}
}
