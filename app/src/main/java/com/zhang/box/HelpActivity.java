package com.zhang.box;

import com.zhang.box.utils.BackTime;
import com.zhang.box.utils.ImageLoaderUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends Activity {

	TextView t1;
	int timeclick;
	private BackTime bt;
	private Context mContext;
	private ImageView itemImage3;
	private Bitmap bitmap;
	private int width;
	private int height;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;
		timeclick = 60;
		WindowManager wm = getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		Button btn4 = (Button) findViewById(R.id.btn3);
		btn4.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				bt.concel();
				finish();
			}
		});

		// 图片本地资源加载
		itemImage3 = (ImageView) findViewById(R.id.ItemImage3);
		bitmap = ImageLoaderUtils.loadHugeBitmapFromDrawable(getResources(),
				R.drawable.helplogo, height, width);
		itemImage3.setImageBitmap(bitmap);

		t1 = (TextView) findViewById(R.id.t1);
		bt = new BackTime(t1, "0", timeclick, 1);
		bt.setOnFinishListener(new BackTime.OnFinishListener() {
			@Override
			public void finish() {
				((Activity) mContext).finish();
			}

			@Override
			public void center(long time) {
			}
		});

		bt.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 页面销毁时 清除图片资源
		if (itemImage3 != null && itemImage3.getDrawable() != null) {
			Bitmap oldBitmap = ((BitmapDrawable) itemImage3.getDrawable())
					.getBitmap();
			itemImage3.setImageDrawable(null);
			if (oldBitmap != null) {
				oldBitmap.recycle();
				oldBitmap = null;
			}
		}
		System.gc();
	}
}
