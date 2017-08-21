package com.zhang.box.utils;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * 动画工具类
 * 
 * @author wang
 * 
 */
public class AnimationUtils {

	/** 按中心点旋转动画 */
	public static void rotateAnimations(ImageView imageView) {

		RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setInterpolator(new LinearInterpolator());// 不停顿
		rotateAnimation.setRepeatCount(-1);
		imageView.startAnimation(rotateAnimation);
	}

	/** 垂直下落动画 */
	public static void translateAnimations(ImageView imageView) {
		TranslateAnimation mAnimation = new TranslateAnimation(0, 0, -600, 0);
		mAnimation.setDuration(800);
		mAnimation.setFillAfter(true);
		imageView.startAnimation(mAnimation);
	}
}
