package com.zhang.box;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.zzq.adapter.BuyGridViewAdapter;
import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.utils.ActivitySkipUtil;
import com.zhang.box.utils.BackTime;
import com.zhang.box.utils.MulDataUtils;

@SuppressLint("SimpleDateFormat")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BuyActivity extends Activity implements OnClickListener {

	boolean mAutoScroll = true;
	int mPosition = 0;
	GridView gridView;
	HorizontalScrollView scrollview;
	int gridviewWidth;
	int x1 = 0;
	int x2 = 0;
	int downX;
	int upX;
	int moveX;
	private TextView tv_buy_backTime;
	GridView gridView2;
	HorizontalScrollView scrollview2;
	boolean isLeft;
	boolean isRight;
	boolean isTouch;
	private Context mContext;
	private int backTime;
	private BackTime bt;
	private BuyGridViewAdapter adapter;
	private BuyGridViewAdapter adapter2;
	private ImageButton ib_pro_one;
	private ImageButton ib_pro_two;
	private ImageButton ib_pro_three;
	private int proType;
	private int type;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.buy);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;
		mBooksDB = new BooksDB(mContext);
		isLeft = true;
		isRight = true;
		backTime = 90;
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat format3 = new SimpleDateFormat("EEEE");
		TextView buytv_sfm = (TextView) findViewById(R.id.buytv_sfm);
		buytv_sfm.setText(format2.format(date));
		TextView buytv_xq = (TextView) findViewById(R.id.buytv_xq);
		buytv_xq.setText(format3.format(date));
		TextView buytv_nyr = (TextView) findViewById(R.id.buytv_nyr);
		buytv_nyr.setText(format.format(date));

		TextView tv_num = (TextView) findViewById(R.id.buytv_num);
		tv_num.setText("机器编号:" + SysData.imei); // 添加IMEI号
		ib_pro_one = (ImageButton) findViewById(R.id.ib_pro_one);
		ib_pro_one.setBackgroundResource(R.drawable.category_0_pressed);
		ib_pro_two = (ImageButton) findViewById(R.id.ib_pro_two);
		ib_pro_three = (ImageButton) findViewById(R.id.ib_pro_three);
		if (UserInfo.proSnakelist.size() == 0) {
			ib_pro_one.setVisibility(View.GONE);
			ib_pro_two.setVisibility(View.GONE);
			ib_pro_three.setVisibility(View.GONE);
		} else {
			ib_pro_one.setVisibility(View.VISIBLE);
			ib_pro_two.setVisibility(View.VISIBLE);
			ib_pro_three.setVisibility(View.VISIBLE);
		}

		ib_pro_two.setOnClickListener(this);
		ib_pro_one.setOnClickListener(this);
		ib_pro_three.setOnClickListener(this);
		gridView = (GridView) findViewById(R.id.buygrid);
		scrollview = (HorizontalScrollView) findViewById(R.id.buyscrollgrid);
		scrollview.setHorizontalScrollBarEnabled(false);
		scrollview.setVerticalScrollBarEnabled(false);
		scrollview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					isTouch = true;
					bt.concel();
					downX = (int) event.getX();
					break;
				case MotionEvent.ACTION_MOVE:
					isTouch = true;
					break;
				case MotionEvent.ACTION_UP:
					isTouch = false;
					bt.start();
					x1 = scrollview.getScrollX();
					break;
				}
				return false;
			}
		});

		gridView2 = (GridView) findViewById(R.id.buygrid2);
		scrollview2 = (HorizontalScrollView) findViewById(R.id.buyscrollgrid2);
		scrollview2.setHorizontalScrollBarEnabled(false);
		scrollview2.setVerticalScrollBarEnabled(false);
		scrollview2.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					isTouch = true;
					bt.concel();
					break;
				case MotionEvent.ACTION_MOVE:
					isTouch = true;
					break;
				case MotionEvent.ACTION_UP:
					isTouch = false;
					bt.start();
					x2 = scrollview2.getScrollX();
					break;
				}
				return false;
			}
		});
		proType = 2;
		type = 7;
		if (UserInfo.proAlllist.size() > 0) {
			UserInfo.proAlllist.clear();
			mBooksDB.ReadProAll();
		}
		setGridView(proType, type, UserInfo.proAlllist);

		startAutoScroll1();
		startAutoScroll2();

		Button btn4 = (Button) findViewById(R.id.buybtn3);
		btn4.setVisibility(View.VISIBLE);
		btn4.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				bt.concel();
				if (SysData.leftNo != null) {
					finish();
				} else {
					ActivitySkipUtil.skipAnotherActivity(BuyActivity.this,
							GallaryActivity.class, true);
				}
			}
		});

		Button btn1 = (Button) findViewById(R.id.buybtn1);
		btn1.setVisibility(View.VISIBLE);
		btn1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				bt.concel();
				ActivitySkipUtil.skipAnotherActivity(BuyActivity.this,
						HelpActivity.class, false);
			}
		});

		tv_buy_backTime = (TextView) findViewById(R.id.tv_buy_backTime);
		bt = new BackTime(tv_buy_backTime, "0", backTime, 1);
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
	protected void onResume() {
		super.onResume();
		bt.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	/** 设置GirdView参数，绑定数据 */
	private void setGridView(int proType, int type, List<UserInfo> mList) {
		double aboveMaths = (double) (mList.size()) / 2;
		int size = (int) Math.floor(aboveMaths);
		int length = 160;
		// GridView提取
		MulDataUtils
				.setGrideViews(BuyActivity.this, gridView, size, length, -2);
		adapter = new BuyGridViewAdapter(BuyActivity.this, proType, type,
				mList);
		gridView.setAdapter(adapter);

		// GridView提取
		double aboveMaths2 = (double) (mList.size()) / 2;
		int size2 = (int) Math.ceil(aboveMaths2);
		int length2 = 160;
		MulDataUtils.setGrideViews(BuyActivity.this, gridView2, size2, length2,
				-2);
		adapter2 = new BuyGridViewAdapter(BuyActivity.this, proType + 1, type,
				mList);
		gridView2.setAdapter(adapter2);
	}

	private void startAutoScroll1() {
		new Thread() {
			@Override
			public void run() {
				while (mAutoScroll) {
					try {
						Thread.sleep(70);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Message msg = mHandler.obtainMessage(1, mPosition, 0);
					mHandler.sendMessage(msg);
				}
			}

		}.start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (isTouch)
					return;

				if (isLeft) {
					x1 = x1 + 1;
				} else {
					x1 = x1 - 1;
				}
				if (x1 > 360) {
					isLeft = false;
				}
				if (x1 < -10) {
					isLeft = true;
				}

				scrollview.scrollTo(x1, 400);
				break;
			}
		}
	};

	private void startAutoScroll2() {
		new Thread() {
			@Override
			public void run() {
				while (mAutoScroll) {
					try {
						Thread.sleep(70);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Message msg = mHandler2.obtainMessage(2, mPosition, 0);
					mHandler2.sendMessage(msg);
				}
			}

		}.start();
	}

	private Handler mHandler2 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				if (isTouch)
					return;

				if (isRight) {
					x2 = x2 + 1;
				} else {
					x2 = x2 - 1;
				}
				if (x2 > 500) {
					isRight = false;
				}
				if (x2 < -10) {
					isRight = true;
				}

				scrollview2.scrollTo(x2, 400);
				break;
			}
		}
	};
	private BooksDB mBooksDB;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_pro_one:
			bt.concel();
			bt.start();
			ib_pro_one.setBackgroundResource(R.drawable.category_0_pressed);
			ib_pro_one.setSelected(true);
			ib_pro_two.setSelected(false);
			ib_pro_three.setSelected(false);
			proType = 2;
			type = 7;

			if (UserInfo.proAlllist.size() > 0) {
				UserInfo.proAlllist.clear();
				mBooksDB.ReadProAll();
			}
			setGridView(proType, type, UserInfo.proAlllist);
			break;
		case R.id.ib_pro_two:
			bt.concel();
			bt.start();
			ib_pro_one.setBackgroundResource(R.drawable.category_0_normal);
			ib_pro_one.setSelected(false);
			ib_pro_two.setSelected(true);
			ib_pro_three.setSelected(false);
			proType = 4;
			type = 8;

			if (UserInfo.proDrinklist.size() > 0) {
				UserInfo.proDrinklist.clear();
				mBooksDB.ReadDrinkAll();
			}
			setGridView(proType, type, UserInfo.proDrinklist);// 饮料

			break;
		case R.id.ib_pro_three:
			bt.concel();
			bt.start();
			ib_pro_one.setBackgroundResource(R.drawable.category_0_normal);
			ib_pro_one.setSelected(false);
			ib_pro_two.setSelected(false);
			ib_pro_three.setSelected(true);
			proType = 6;
			type = 9;

			if (UserInfo.proSnakelist.size() > 0) {
				UserInfo.proSnakelist.clear();
				mBooksDB.ReadSnakeAll();
			}
			setGridView(proType, type, UserInfo.proSnakelist);// 零食
			break;

		default:
			break;
		}
	}
}
