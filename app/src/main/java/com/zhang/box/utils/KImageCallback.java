package com.zhang.box.utils;

import com.zhang.box.ImageCallback;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * �ص� ����ͼƬ
 * 
 * @author wang
 * 
 */
public class KImageCallback implements ImageCallback {
	ImageView logImage;

	public KImageCallback(ImageView logImage) {
		this.logImage = logImage;
	}

	@Override
	public void imageLoaded(Drawable imageDrawable, String imageUrl) {
		logImage.setBackgroundDrawable(imageDrawable);
	}
}
