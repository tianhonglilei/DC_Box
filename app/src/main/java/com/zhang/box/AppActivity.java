package com.zhang.box;

import java.util.ArrayList;

import com.example.zzq.adapter.AppAdapter;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.utils.AlphaPageTransformer;
import com.zhang.box.utils.BackTime;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.fontUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppActivity extends Activity implements OnClickListener {

	private Context mContext;
	private int width;
	private int height;
	private ViewPager viewpager;
	private Button rl_btn_soft_one;
	private Button rl_btn_game_two;
	private Button rl_btn_shopping_three;
	private Button rl_btn_environment_four;
	private ListView app_lv;
	private Button appbtn3;
	private TextView tv_app_backTime;
	private AppAdapter appAdapter;
	private BooksDB mBooksDB;
	private BackTime bt;
	private int backTime;
	private ArrayList<ImageView> imageViews;

	// 图片资源ID
	// private final int[] imageIds = { R.drawable.a, R.drawable.b,
	// R.drawable.c,
	// R.drawable.d, R.drawable.e };
	/**
	 * 是否已经滚动
	 */
	private boolean isDragging = false;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			int item = viewpager.getCurrentItem() + 1;
			viewpager.setCurrentItem(item);
			// 延迟发消息
			handler.sendEmptyMessageDelayed(0, 4000);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;
		// 获取屏幕长宽
		WindowManager wm = getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		backTime = 90;
		initView();
		initData();
		initListener();
	}

	/** 初始化布局 */
	private void initView() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		rl_btn_soft_one = (Button) findViewById(R.id.rl_btn_soft_one);
		rl_btn_game_two = (Button) findViewById(R.id.rl_btn_game_two);
		rl_btn_shopping_three = (Button) findViewById(R.id.rl_btn_shopping_three);
		rl_btn_environment_four = (Button) findViewById(R.id.rl_btn_environment_four);
		app_lv = (ListView) findViewById(R.id.app_lv);
		appbtn3 = (Button) findViewById(R.id.appbtn3);
		tv_app_backTime = (TextView) findViewById(R.id.tv_app_backTime);
		bt = new BackTime(tv_app_backTime, "0", backTime, 1);
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

	/** 初始化数据 */
	private void initData() {
		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < UserInfo.appMainshowOne.size(); i++) {

			ImageView imageView = new ImageView(this);
			// imageView.setBackgroundResource(imageIds[i]);
			MulDataUtils.dealPicture(mContext,
					UserInfo.appMainshowOne.get(i).mainimg, imageView);

			// ToastTools.showShort(mContext,
			// UserInfo.appMainshowOne.get(i).mainimg);
			// 添加到集合中
			imageViews.add(imageView);
		}

		// TODO WH
		mBooksDB = new BooksDB(mContext);
		appAdapter = new AppAdapter(mContext);
		app_lv.setAdapter(appAdapter);
		app_lv.setDividerHeight(3);
		rl_btn_soft_one.setSelected(true);
		fontUtils.setFontColorAndSize(mContext, rl_btn_soft_one, R.color.blue,
				20);
		rl_btn_soft_one.setTextSize(20);
		rl_btn_game_two.setSelected(false);
		rl_btn_shopping_three.setSelected(false);
		rl_btn_environment_four.setSelected(false);
		UserInfo.appMainshow.clear();
		mBooksDB = new BooksDB(AppActivity.this);
		mBooksDB.ReadAppSort("soft");
		appAdapter.notifyDataSetChanged();

	}

	/** 初始化监听事件 */
	private void initListener() {

		// 4.设置适配器(PagerAdapter)-item布局-绑定数据
		viewpager.setPageMargin(6);
		viewpager.setOffscreenPageLimit((int) 3.3);
		viewpager.setAdapter(new MyPagerAdapter());
		viewpager.setPageTransformer(true, new AlphaPageTransformer());
		// 设置监听ViewPager页面的改变
		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());

		// TODO WH 设置中间位置

		int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% imageViews.size();// 要保证imageViews的整数倍
		viewpager.setCurrentItem(item);

		// 发消息
		handler.sendEmptyMessageDelayed(0, 2000);

		rl_btn_soft_one.setOnClickListener(this);
		rl_btn_game_two.setOnClickListener(this);
		rl_btn_shopping_three.setOnClickListener(this);
		rl_btn_environment_four.setOnClickListener(this);
		appbtn3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_btn_soft_one:
			bt.start();
			rl_btn_soft_one.setBackgroundResource(R.drawable.sortbg_press);
			fontUtils.setFontColorAndSize(mContext, rl_btn_soft_one,
					R.color.blue, 20);
			fontUtils.setFontColorAndSize(mContext, rl_btn_game_two,
					rl_btn_shopping_three, rl_btn_environment_four,
					R.color.black, 16);
			rl_btn_soft_one.setSelected(true);
			rl_btn_game_two.setSelected(false);
			rl_btn_shopping_three.setSelected(false);
			rl_btn_environment_four.setSelected(false);
			UserInfo.appMainshow.clear();
			mBooksDB.ReadAppSort("soft");
			appAdapter.notifyDataSetChanged();

			break;
		case R.id.rl_btn_game_two:
			bt.start();
			rl_btn_soft_one.setBackgroundResource(R.drawable.sortbg);
			fontUtils.setFontColorAndSize(mContext, rl_btn_game_two,
					R.color.blue, 20);
			fontUtils.setFontColorAndSize(mContext, rl_btn_soft_one,
					rl_btn_shopping_three, rl_btn_environment_four,
					R.color.black, 16);
			rl_btn_soft_one.setSelected(false);
			rl_btn_environment_four.setSelected(false);
			rl_btn_game_two.setSelected(true);
			rl_btn_shopping_three.setSelected(false);
			UserInfo.appMainshow.clear();
			mBooksDB.ReadAppSort("game");
			appAdapter.notifyDataSetChanged();

			break;
		case R.id.rl_btn_shopping_three:
			bt.start();
			rl_btn_soft_one.setBackgroundResource(R.drawable.sortbg);
			fontUtils.setFontColorAndSize(mContext, rl_btn_shopping_three,
					R.color.blue, 20);
			fontUtils
					.setFontColorAndSize(mContext, rl_btn_soft_one,
							rl_btn_game_two, rl_btn_environment_four,
							R.color.black, 16);
			rl_btn_soft_one.setSelected(false);
			rl_btn_environment_four.setSelected(false);
			rl_btn_game_two.setSelected(false);
			rl_btn_shopping_three.setSelected(true);
			UserInfo.appMainshow.clear();
			mBooksDB.ReadAppSort("buy");
			appAdapter.notifyDataSetChanged();

			break;
		case R.id.rl_btn_environment_four:
			bt.start();
			rl_btn_soft_one.setBackgroundResource(R.drawable.sortbg);
			fontUtils.setFontColorAndSize(mContext, rl_btn_environment_four,
					R.color.blue, 20);
			fontUtils.setFontColorAndSize(mContext, rl_btn_soft_one,
					rl_btn_game_two, rl_btn_shopping_three, R.color.black, 16);
			rl_btn_soft_one.setSelected(false);
			rl_btn_environment_four.setSelected(true);
			rl_btn_game_two.setSelected(false);
			rl_btn_shopping_three.setSelected(false);
			UserInfo.appMainshow.clear();
			mBooksDB.ReadAppSort("yule");
			appAdapter.notifyDataSetChanged();

			break;
		case R.id.appbtn3:
			bt.concel();
			finish();
			break;

		default:
			break;
		}
	}

	class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

		/**
		 * 当页面滚动了的时候回调这个方法
		 * 
		 * @param position
		 *            当前页面的位置
		 * @param positionOffset
		 *            滑动页面的百分比
		 * @param positionOffsetPixels
		 *            在屏幕上滑动的像数
		 */
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		/**
		 * 当某个页面被选中了的时候回调
		 * 
		 * @param position
		 *            被选中页面的位置
		 */
		@Override
		public void onPageSelected(int position) {

		}

		/**
		 * 当页面滚动状态变化的时候回调这个方法 静止->滑动 滑动-->静止 静止-->拖拽
		 */
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_DRAGGING) {
				isDragging = true;
				handler.removeCallbacksAndMessages(null);
			} else if (state == ViewPager.SCROLL_STATE_SETTLING) {

			} else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
				isDragging = false;
				handler.removeCallbacksAndMessages(null);
				handler.sendEmptyMessageDelayed(0, 4000);
			}

		}
	}

	class MyPagerAdapter extends PagerAdapter {

		/**
		 * 得到图片的总数
		 * 
		 * @return
		 */
		@Override
		public int getCount() {
			// return imageViews.size();
			return Integer.MAX_VALUE;
		}

		/**
		 * 相当于getView方法
		 * 
		 * @param container
		 *            ViewPager自身
		 * @param position
		 *            当前实例化页面的位置
		 * @return
		 */
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			int realPosition = position % imageViews.size();
			ImageView view = new ImageView(AppActivity.this);
			view.setScaleType(ImageView.ScaleType.FIT_XY);
			// TODO WH
			// view.setImageResource(imageIds[realPosition]);
			MulDataUtils.dealPicture(mContext,
					UserInfo.appMainshowOne.get(realPosition).mainimg, view);
			container.addView(view);

			view.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:// 手指按下
						handler.removeCallbacksAndMessages(null);
						break;

					case MotionEvent.ACTION_MOVE:// 手指在这个控件上移动
						break;
					case MotionEvent.ACTION_CANCEL:// 手指在这个控件上移动
						// handler.removeCallbacksAndMessages(null);
						// handler.sendEmptyMessageDelayed(0,4000);
						break;
					case MotionEvent.ACTION_UP:// 手指离开
						handler.removeCallbacksAndMessages(null);
						handler.sendEmptyMessageDelayed(0, 4000);
						break;
					}
					return false;
				}
			});

			view.setTag(position);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag() % imageViews.size();
					// String text = imageDescriptions[position];
					// Toast.makeText(AppActivity.this, "position==" + position,
					// Toast.LENGTH_SHORT).show();
				}
			});
			return view;
		}

		/**
		 * 比较view和object是否同一个实例
		 * 
		 * @param view
		 *            页面
		 * @param object
		 *            这个方法instantiateItem返回的结果
		 * @return
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		/**
		 * 释放资源
		 * 
		 * @param container
		 *            viewpager
		 * @param position
		 *            要释放的位置
		 * @param object
		 *            要释放的页面
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		bt.start();
	}
}
