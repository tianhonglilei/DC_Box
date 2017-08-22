package com.zhang.box.utils;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

/**
 * ����ʱTextView������
 * 
 */
public class BackTime {

	// ����ʱtimer
	private CountDownTimerUtil countDownTimer;
	// ��ʱ�����Ļص��ӿ�
	private OnFinishListener listener;

	// ��ʱ���ڼ�ص��ӿ�
	private TextView textView;

	private Button button;

	/**
	 * @param textView
	 *            ��Ҫ��ʾ����ʱ��Button
	 * @param defaultString
	 *            Ĭ����ʾ���ַ���
	 * @param max
	 *            ��Ҫ���е���ʱ�����ֵ,��λ����
	 * @param interval
	 *            ����ʱ�ļ������λ����
	 */
	public BackTime(final TextView textView, final String defaultString,
			int max, int interval) {

		this.textView = textView;

		// ����CountDownTimer������׼ȷ��ʱ����onTick�������õ�ʱ��time����1-10ms���ҵ�����ᵼ�����һ�벻�����onTick()
		// ��ˣ����ü����ʱ��Ĭ�ϼ�ȥ��10ms���Ӷ���ȥ��
		// �������ϵ�΢�������һ�����ʾʱ�������10ms�ӳٵĻ��ۣ�������ʾʱ���1s��max*10ms��ʱ�䣬����ʱ�����ʾ����,��ʱ������

		countDownTimer = new CountDownTimerUtil(max * 1000,
				interval * 1000 - 10) {

			@Override
			public void onTick(long time) {
				// ��һ�ε��û���1-10ms���������Ҫ+15ms����ֹ��һ��������ʾ���ڶ�������ʾ2s
				textView.setText(((time + 15) / 1000) + "s");
				Log.d("BackTime", "time = " + (time) + " text = "
						+ ((time + 15) / 1000));
				listener.center(time);

			}

			@Override
			public void onFinish() {
				textView.setEnabled(true);
				textView.setText(defaultString);
				if (listener != null) {
					listener.finish();
				}
			}
		};
	}

	/**
	 * ��ʼ����ʱ
	 */
	public void start() {
		if (textView != null) {
			textView.setEnabled(false);
		} else if (button != null) {
			button.setEnabled(false);
		}
		countDownTimer.start();
	}

	/**
	 * @param button
	 *            ��Ҫ��ʾ����ʱ��Button
	 * @param defaultString
	 *            Ĭ����ʾ���ַ���
	 * @param max
	 *            ��Ҫ���е���ʱ�����ֵ,��λ����
	 * @param interval
	 *            ����ʱ�ļ������λ����
	 */
	public BackTime(final Button button, final String defaultString, int max,
			int interval) {

		this.button = button;

		// ����CountDownTimer������׼ȷ��ʱ����onTick�������õ�ʱ��time����1-10ms���ҵ�����ᵼ�����һ�벻�����onTick()
		// ��ˣ����ü����ʱ��Ĭ�ϼ�ȥ��10ms���Ӷ���ȥ��
		// �������ϵ�΢�������һ�����ʾʱ�������10ms�ӳٵĻ��ۣ�������ʾʱ���1s��max*10ms��ʱ�䣬����ʱ�����ʾ����,��ʱ������

		countDownTimer = new CountDownTimerUtil(max * 1000,
				interval * 1000 - 10) {

			@Override
			public void onTick(long time) {
				// ��һ�ε��û���1-10ms���������Ҫ+15ms����ֹ��һ��������ʾ���ڶ�������ʾ2s
				button.setText(((time + 15) / 1000) + "s");
				Log.d("BackTime", "time = " + (time) + " text = "
						+ ((time + 15) / 1000));
				listener.center(time);

			}

			@Override
			public void onFinish() {
				button.setEnabled(true);
				button.setText(defaultString);
				if (listener != null) {
					listener.finish();
				}
			}
		};
	}

	/**
	 * ����ʱȡ��
	 */
	public void concel() {
		if (textView != null) {
			textView.setEnabled(false);
		} else if (button != null) {
			button.setEnabled(false);
		}
		countDownTimer.cancel();
	}

	/**
	 * ���õ���ʱ�����ļ�����
	 * 
	 * @param listener
	 */
	public void setOnFinishListener(OnFinishListener listener) {
		this.listener = listener;
	}

	/**
	 * ��ʱ�����Ļص��ӿ�
	 */
	public interface OnFinishListener {
		public void finish();

		void center(long time);
	}

}
