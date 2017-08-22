package com.zhang.box;

import com.example.zzq.bean.UserInfo;
import com.zhang.box.utils.ActivitySkipUtil;
import com.zhang.box.utils.BackTime;
import com.zhang.box.utils.BackTime.OnFinishListener;
import com.zhang.box.utils.ImageLoaderUtils;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.MyAnimation;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProSucessActivity extends Activity {

	private Context mContext;
	private int width;
	private int height;
	private ImageView iv_result_dianchaung;
	private Bitmap iv_result_dianchaung_bitmap;
	private ImageView iv_result_shopping;
	private Bitmap iv_result_shopping_bitmap;
	private ImageView show_pro;
	private Bitmap show_pro_bitmap;
	private ImageView iv_result_pro;
	private TextView des_result_pro_del;
	private Button des_btn_result_return;
	private TextView des_time_result_return;
	private ImageView iv_result_kefuhaoma;
	private Bitmap iv_result_kefuhaoma_bitmap;
	private ImageView iv_worning2;
	private Bitmap iv_worning2_bitmap;
	private BackTime btResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pro_sucess);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;
		// 获取屏幕长宽
		WindowManager wm = getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		initView();
	}

	/** 初始化控件 */
	private void initView() {
		
		// 商品详情支付成功后页面控件
		iv_result_dianchaung = (ImageView) findViewById(R.id.iv_result_dianchaung);
		iv_result_dianchaung_bitmap = ImageLoaderUtils
				.loadHugeBitmapFromDrawable(getResources(),
						R.drawable.dianchaung_title, height, width);
		iv_result_dianchaung.setImageBitmap(iv_result_dianchaung_bitmap);

		iv_result_shopping = (ImageView) findViewById(R.id.iv_result_shopping);
		iv_result_shopping_bitmap = ImageLoaderUtils
				.loadHugeBitmapFromDrawable(getResources(),
						R.drawable.des_shop_car, height, width);
		iv_result_shopping.setImageBitmap(iv_result_shopping_bitmap);

		show_pro = (ImageView) findViewById(R.id.iv_result_stutas);
		show_pro_bitmap = ImageLoaderUtils.loadHugeBitmapFromDrawable(
				getResources(), R.drawable.show_pro, height, width);
		show_pro.setImageBitmap(show_pro_bitmap);

		iv_result_pro = (ImageView) findViewById(R.id.iv_result_pro);
		des_result_pro_del = (TextView) findViewById(R.id.des_result_pro_del);// 商品名称
		des_btn_result_return = (Button) findViewById(R.id.des_btn_result_return);
		des_btn_result_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivitySkipUtil.skipAnotherActivity(ProSucessActivity.this,
						GallaryActivity.class, true);
				if (btResult != null) {
					btResult.concel();
				}
			}
		});

		des_time_result_return = (TextView) findViewById(R.id.des_time_result_return);
		btResult = new BackTime(des_time_result_return, "0", 15, 1);
		btResult.setOnFinishListener(new OnFinishListener() {

			@Override
			public void finish() {
				ActivitySkipUtil.skipAnotherActivity(ProSucessActivity.this,
						GallaryActivity.class, true);
			}

			@Override
			public void center(long time) {
				des_result_pro_del.setText(UserInfo.sucessTitle + "一件");
				MulDataUtils.dealPicture(mContext, UserInfo.sucessLogo,
						iv_result_pro);
				MyAnimation animation = new MyAnimation();
				animation.setRepeatCount(1);
				show_pro.startAnimation(animation);

			}
		});
		btResult.start();

		// 客服
		iv_result_kefuhaoma = (ImageView) findViewById(R.id.iv_result_kefuhaoma);
		iv_result_kefuhaoma_bitmap = ImageLoaderUtils
				.loadHugeBitmapFromDrawable(getResources(),
						R.drawable.des_kefu, height, width);
		iv_result_kefuhaoma.setImageBitmap(iv_result_kefuhaoma_bitmap);

		// 提示
		iv_worning2 = (ImageView) findViewById(R.id.iv_worning2);
		iv_worning2_bitmap = ImageLoaderUtils.loadHugeBitmapFromDrawable(
				getResources(), R.drawable.des_result_worn, height, width);
		iv_worning2.setImageBitmap(iv_worning2_bitmap);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (show_pro_bitmap != null && !show_pro_bitmap.isRecycled()) {
			show_pro_bitmap.recycle();
			show_pro_bitmap = null;
		}

		if (iv_result_dianchaung_bitmap != null
				&& !iv_result_dianchaung_bitmap.isRecycled()) {
			iv_result_dianchaung_bitmap.recycle();
			iv_result_dianchaung_bitmap = null;
		}

		if (iv_result_shopping_bitmap != null
				&& !iv_result_shopping_bitmap.isRecycled()) {
			iv_result_shopping_bitmap.recycle();
			iv_result_shopping_bitmap = null;
		}

		if (iv_result_kefuhaoma_bitmap != null
				&& !iv_result_kefuhaoma_bitmap.isRecycled()) {
			iv_result_kefuhaoma_bitmap.recycle();
			iv_result_kefuhaoma_bitmap = null;
		}

		if (iv_worning2_bitmap != null && !iv_worning2_bitmap.isRecycled()) {
			iv_worning2_bitmap.recycle();
			iv_worning2_bitmap = null;
		}

		System.gc();
	}
}
