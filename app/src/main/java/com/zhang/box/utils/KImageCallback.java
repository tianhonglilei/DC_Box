package com.zhang.box.utils;

import com.zhang.box.ImageCallback;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * 回调 处理图片
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
