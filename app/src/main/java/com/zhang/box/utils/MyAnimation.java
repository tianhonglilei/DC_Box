package com.zhang.box.utils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class MyAnimation extends Animation {
	int centerX, centerY;
	Camera camera = new Camera();

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		// ��ȡ���ĵ�����
		centerX = width / 2;
		centerY = height / 2;
		// ����ִ��ʱ�� ���ж���
		setDuration(1400);
		setInterpolator(new DecelerateInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final Matrix matrix = t.getMatrix();
		camera.save();
		// ��������Y����ת ���������������X�� Y�� Z��
		camera.rotateY(360 * interpolatedTime);
		// �����ǵ�����ͷ���ڱ任������
		camera.getMatrix(matrix);
		// ���÷�ת���ĵ�
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
		camera.restore();
	}
}