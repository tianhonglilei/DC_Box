package com.zhang.box;

import java.util.ArrayList;
import java.util.Hashtable;

import com.example.zzq.adapter.AppDesJPGridViewAdapter;
import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zhang.box.utils.BackTime;
import com.zhang.box.utils.CommonAdapter;
import com.zhang.box.utils.ImageLoaderUtils;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.ViewHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DesAppActivity extends Activity {

	private Context mContext;
	private int width;
	private int height;
	private ImageView iv_appdes_center_logo;
	private TextView tv_appdes_center_name;
	private TextView tv_appdes_center_des;
	private TextView tv_appdes_center_price;
	private ViewPager viewpager;
	private LinearLayout ll_point_group;
	private ImageView iv_appdes_right_logo;
	private TextView tv_appdes_right_name;
	private TextView tv_appdes_right_des;
	private TextView tv_appdes_right_price;
	private TextView tv_appdes_right_getfen;
	private ImageView iv_appdes_right_ewm_normal;
	private ImageView iv_appdes_right_ewm_err;
	private Bitmap iv_appdes_right_ewm_err_bitmap;
	private GridView appdes_grid;
	private ListView lv_appdes_left;
	private BooksDB mBooksDB;
	private MyDesAppAdapter mDesAppAdapter;
	private ArrayList<ImageView> imageViews;
	private Button desappbtn3;
	private TextView tv_desapp_backTime;
	private int backTime;
	private BackTime bt;
	private UserInfo desInfos;

	// 图片资源ID
	private final int[] imageIds = { R.drawable.aa, R.drawable.aaa,
			R.drawable.aaaa };
	/**
	 * 上一次高亮显示的位置
	 */
	private int prePosition = 0;
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
			handler.sendEmptyMessageDelayed(0, 2000);
		}
	};
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.des_app);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;
		// 获取屏幕长宽
		WindowManager wm = getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		Bundle bundle = this.getIntent().getExtras();
		desInfos = (UserInfo) bundle.getSerializable("proInfos");
		backTime = 90;
		initView();
		initData();
		initListener();
	}

	/** 初始化布局 */
	private void initView() {

		// 左侧
		lv_appdes_left = (ListView) findViewById(R.id.lv_appdes_left);

		// 中间部分
		iv_appdes_center_logo = (ImageView) findViewById(R.id.iv_appdes_center_logo);
		tv_appdes_center_name = (TextView) findViewById(R.id.tv_appdes_center_name);
		tv_appdes_center_des = (TextView) findViewById(R.id.tv_appdes_center_des);
		tv_appdes_center_price = (TextView) findViewById(R.id.tv_appdes_center_price);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);

		// 右侧部分
		iv_appdes_right_logo = (ImageView) findViewById(R.id.iv_appdes_right_logo);
		tv_appdes_right_name = (TextView) findViewById(R.id.tv_appdes_right_name);
		tv_appdes_right_des = (TextView) findViewById(R.id.tv_appdes_right_des);
		tv_appdes_right_price = (TextView) findViewById(R.id.tv_appdes_right_price);
		tv_appdes_right_getfen = (TextView) findViewById(R.id.tv_appdes_right_getfen);
		iv_appdes_right_ewm_normal = (ImageView) findViewById(R.id.iv_appdes_right_ewm_normal);
		iv_appdes_right_ewm_err = (ImageView) findViewById(R.id.iv_appdes_right_ewm_err);
		iv_appdes_right_ewm_err.post(new Runnable() {

			@Override
			public void run() {
				iv_appdes_right_ewm_err_bitmap = ImageLoaderUtils
						.loadHugeBitmapFromDrawable(getResources(),
								R.drawable.qr_loading, height, width);
				iv_appdes_right_ewm_err
						.setImageBitmap(iv_appdes_right_ewm_err_bitmap);
			}
		});
		// 下部分 应用推荐
		appdes_grid = (GridView) findViewById(R.id.appdes_grid);

		desappbtn3 = (Button) findViewById(R.id.desappbtn3);
		tv_desapp_backTime = (TextView) findViewById(R.id.tv_desapp_backTime);
	}

	// TODO
	/** 初始化数据 */
	private void initData() {

		// 左侧的数据
		mBooksDB = new BooksDB(this);
		search(desInfos.appprice);

		// 中间的数据
		MulDataUtils.dealPicture(mContext, desInfos.applogo,
				iv_appdes_center_logo);
		tv_appdes_center_name.setText(desInfos.appname);
		tv_appdes_center_des.setText(desInfos.duan);
		tv_appdes_center_price.setText(desInfos.appprice / 100 + " ");

		setViewPager();

		// 右侧的数据
		MulDataUtils.dealPicture(mContext, desInfos.applogo,
				iv_appdes_right_logo);
		tv_appdes_right_name.setText(desInfos.appname);
		tv_appdes_right_des.setText(desInfos.duan);
		tv_appdes_right_price.setText(desInfos.appprice / 100 + " ");
		// 得到的分数
		String desfen = "扫描安装应用可免费获取" + (desInfos.appprice / 100) + "积分";
		tv_appdes_right_getfen.setText(desfen);
		String appid = Integer.toString(desInfos.appid);
		upDataDianChuangQR(appid);

	}

	// TODO WH
	/** 生成应用的二维码 */
	private void upDataDianChuangQR(String appid) {

		iv_appdes_right_ewm_normal.setVisibility(View.INVISIBLE);
		iv_appdes_right_ewm_err.setVisibility(View.VISIBLE);
		HandlerThread handlerThread = new HandlerThread("handlerThread");
		handlerThread.start();
		MyHandler handler = new MyHandler(handlerThread.getLooper());
		Message msg = handler.obtainMessage();
		// 利用bundle对象来传值
		Bundle b = new Bundle();
		b.putString("appid", appid);
		msg.setData(b);
		msg.sendToTarget();
	}

	class MyHandler extends Handler {

		public MyHandler() {
			super();
		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 获取bundle对象的值
			Bundle b = msg.getData();
			String appid = b.getString("appid");
			String url = "http://quyingyoung.dc-box.com/?c=welcome&m=app_wap&machineid="
					+ SysData.imei + "&appid=" + appid + "&id=" + "";
			// 生成二维码
			createQRImage(url);
			iv_appdes_right_ewm_normal.post(new Runnable() {

				@Override
				public void run() {
					iv_appdes_right_ewm_normal.setVisibility(View.VISIBLE);

				}
			});

			iv_appdes_right_ewm_err.post(new Runnable() {

				@Override
				public void run() {
					iv_appdes_right_ewm_err.setVisibility(View.INVISIBLE);

				}
			});
		}
	}

	/** 轮播的布局和监听事件 */
	private void setViewPager() {
		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageIds[i]);
			// 添加到集合中
			imageViews.add(imageView);

			// 添加点
			ImageView point = new ImageView(this);
			point.setBackgroundResource(R.drawable.point_selector);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8,
					8);
			if (i == 0) {
				point.setEnabled(true); // 显示红色
			} else {
				point.setEnabled(false);// 显示灰色
				params.leftMargin = 8;
			}

			point.setLayoutParams(params);

			ll_point_group.addView(point);
		}
		// 4.设置适配器(PagerAdapter)-item布局-绑定数据

		viewpager.setAdapter(new MyPagerAdapter());
		// 设置监听ViewPager页面的改变
		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());

		// 设置中间位置
		int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% imageViews.size();// 要保证imageViews的整数倍
		viewpager.setCurrentItem(item);
		// 发消息
		handler.sendEmptyMessageDelayed(0, 2000);
	}

	/** 生成二维码 */
	protected void createQRImage(String url) {
		int QR_WIDTH = 150;
		int QR_HEIGHT = 150;
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						// pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888

			bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			// 显示到一个ImageView上面
			// qrcode.setBackgroundColor(Color.RED);
			iv_appdes_right_ewm_normal.post(new Runnable() {

				@Override
				public void run() {
					iv_appdes_right_ewm_normal.setImageBitmap(bitmap);
				}
			});

		} catch (WriterException e) {
			e.printStackTrace();
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
			int realPosition = position % imageViews.size();

			// 把上一个高亮的设置默认-灰色
			ll_point_group.getChildAt(prePosition).setEnabled(false);
			// 当前的设置为高亮-红色
			ll_point_group.getChildAt(realPosition).setEnabled(true);
			prePosition = realPosition;
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
				handler.sendEmptyMessageDelayed(0, 2000);
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
			final ImageView imageView = imageViews.get(realPosition);
			imageView.setTag(position);
			ViewGroup parent = (ViewGroup) imageView.getParent();
			if (parent != null) {
				parent.removeAllViews();
			}
			container.addView(imageView);// 添加到ViewPager中
			return imageView;
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

	/** 根据价格显示出大于或者等于该商品的应用软件 */
	public void search(int price) {
		UserInfo.searchApp.clear();
		mBooksDB.SearchApp(price);// 首页9个
	}

	/** 初始化监听事件 */
	private void initListener() {
		// 左侧数据的添加设置 根据价格显示
		mDesAppAdapter = new MyDesAppAdapter(mContext);
		lv_appdes_left.setDividerHeight(5);
		lv_appdes_left.setAdapter(mDesAppAdapter);

		// 精品推荐应用
		setGridView();

		desappbtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		bt = new BackTime(tv_desapp_backTime, "0", backTime, 1);
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

	// TODO
	private void setGridView() {
		int size = UserInfo.jpAllshow.size();
		int length = 116;

		MulDataUtils.setGrideViews(DesAppActivity.this, appdes_grid, size,
				length, 4);
		AppDesJPGridViewAdapter adapter = new AppDesJPGridViewAdapter(
				DesAppActivity.this, UserInfo.jpAllshow);
		adapter.notifyDataSetChanged();
		appdes_grid.setAdapter(adapter);

		appdes_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int positions, long id) {
				
				UserInfo uInfos = UserInfo.jpAllshow.get(positions-1);
				MulDataUtils.dealPicture(mContext, uInfos.applogo,
						iv_appdes_center_logo);
				tv_appdes_center_name.setText(uInfos.appname);
				tv_appdes_center_des.setText(uInfos.duan);
				tv_appdes_center_price.setText(uInfos.appprice / 100 + " ");

				// 右侧的数据
				MulDataUtils.dealPicture(mContext, uInfos.applogo,
						iv_appdes_right_logo);
				tv_appdes_right_name.setText(uInfos.appname);
				tv_appdes_right_des.setText(uInfos.duan);
				tv_appdes_right_price.setText(uInfos.appprice / 100 + " ");
				// 得到的分数
				String desfen = "扫描安装应用可免费获取" + (uInfos.appprice / 100) + "积分";
				tv_appdes_right_getfen.setText(desfen);
				String appid = Integer.toString(uInfos.appid);
				upDataDianChuangQR(appid);
			}
		});
	}

	// TODO WH
	@SuppressWarnings("rawtypes")
	public class MyDesAppAdapter extends CommonAdapter {
		private Context mContext;
		private ViewHolder viewHolder;
		private TextView desappitem_tv_name_one;
		private TextView desappitem_tv_price_one;
		private ImageView desappitem_iv_logo_one;
		private TextView desappitem_tv_name_two;
		private TextView desappitem_tv_price_two;
		private ImageView desappitem_iv_logo_two;
		private TextView desappitem_tv_name_three;
		private TextView desappitem_tv_price_three;
		private ImageView desappitem_iv_logo_three;
		private RelativeLayout desappitem_layone;
		private RelativeLayout desappitem_laytwo;
		private RelativeLayout desappitem_laythree;

		public MyDesAppAdapter(Context context) {

			super(context);
			this.mContext = context;
		}

		@Override
		public int getCount() {

			int size = UserInfo.searchApp.size();
			if (size % 3 == 0) {
				return size / 3;
			} else {
				return (size / 3 + 1);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			viewHolder = ViewHolder.get(mContext, convertView, parent,
					R.layout.des_app_item, position);

			desappitem_tv_name_one = viewHolder
					.getView(R.id.desappitem_tv_name_one);
			desappitem_tv_price_one = viewHolder
					.getView(R.id.desappitem_tv_price_one);
			desappitem_iv_logo_one = viewHolder
					.getView(R.id.desappitem_iv_logo_one);
			desappitem_layone = viewHolder.getView(R.id.desappitem_layone);

			desappitem_tv_name_two = viewHolder
					.getView(R.id.desappitem_tv_name_two);
			desappitem_tv_price_two = viewHolder
					.getView(R.id.desappitem_tv_price_two);
			desappitem_iv_logo_two = viewHolder
					.getView(R.id.desappitem_iv_logo_two);
			desappitem_laytwo = viewHolder.getView(R.id.desappitem_laytwo);

			desappitem_tv_name_three = viewHolder
					.getView(R.id.desappitem_tv_name_three);
			desappitem_tv_price_three = viewHolder
					.getView(R.id.desappitem_tv_price_three);
			desappitem_iv_logo_three = viewHolder
					.getView(R.id.desappitem_iv_logo_three);
			desappitem_laythree = viewHolder.getView(R.id.desappitem_laythree);

			position = position * 3;
			if (position < UserInfo.searchApp.size()) {
				desappitem_layone.setVisibility(View.VISIBLE);
				MulDataUtils.dealPicture(mContext,
						UserInfo.searchApp.get(position).applogo,
						desappitem_iv_logo_one);

				// 加载文字
				desappitem_tv_name_one
						.setText(UserInfo.searchApp.get(position).appname);

				String str = (UserInfo.searchApp.get(position).appprice) / 100
						+ " ";
				desappitem_tv_price_one.setText(str);

				final int itemposition = position;
				desappitem_layone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						UserInfo uInfo = UserInfo.searchApp.get(itemposition);

						MulDataUtils.dealPicture(mContext, uInfo.applogo,
								iv_appdes_center_logo);
						tv_appdes_center_name.setText(uInfo.appname);
						tv_appdes_center_des.setText(uInfo.duan);
						tv_appdes_center_price.setText(uInfo.appprice / 100
								+ " ");

						// 右侧的数据
						MulDataUtils.dealPicture(mContext, uInfo.applogo,
								iv_appdes_right_logo);
						tv_appdes_right_name.setText(uInfo.appname);
						tv_appdes_right_des.setText(uInfo.duan);
						tv_appdes_right_price.setText(uInfo.appprice / 100
								+ " ");
						// 得到的分数
						String desfen = "扫描安装应用可免费获取" + (uInfo.appprice / 100)
								+ "积分";
						tv_appdes_right_getfen.setText(desfen);
						String appid = Integer.toString(uInfo.appid);
						upDataDianChuangQR(appid);
					}
				});
			} else {
				desappitem_layone.setVisibility(View.INVISIBLE);
			}

			if ((position + 1) < UserInfo.searchApp.size()) {
				desappitem_laytwo.setVisibility(View.VISIBLE);

				MulDataUtils.dealPicture(mContext,
						UserInfo.searchApp.get(position + 1).applogo,
						desappitem_iv_logo_two);

				// 加载文字
				desappitem_tv_name_two.setText(UserInfo.searchApp
						.get(position + 1).appname);
				String str = (UserInfo.searchApp.get(position + 1).appprice)
						/ 100 + "";
				desappitem_tv_price_two.setText(str);
				final int itemposition = position + 1;
				desappitem_laytwo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						UserInfo uInfo = UserInfo.searchApp.get(itemposition);

						MulDataUtils.dealPicture(mContext, uInfo.applogo,
								iv_appdes_center_logo);
						tv_appdes_center_name.setText(uInfo.appname);
						tv_appdes_center_des.setText(uInfo.duan);
						tv_appdes_center_price.setText(uInfo.appprice / 100
								+ " ");

						// 右侧的数据
						MulDataUtils.dealPicture(mContext, uInfo.applogo,
								iv_appdes_right_logo);
						tv_appdes_right_name.setText(uInfo.appname);
						tv_appdes_right_des.setText(uInfo.duan);
						tv_appdes_right_price.setText(uInfo.appprice / 100
								+ " ");
						// 得到的分数
						String desfen = "扫描安装应用可免费获取" + (uInfo.appprice / 100)
								+ "积分";
						tv_appdes_right_getfen.setText(desfen);
						String appid = Integer.toString(uInfo.appid);
						upDataDianChuangQR(appid);

					}
				});
			} else {
				desappitem_laytwo.setVisibility(View.INVISIBLE);

			}
			if ((position + 2) < UserInfo.searchApp.size()) {
				desappitem_laythree.setVisibility(View.VISIBLE);

				MulDataUtils.dealPicture(mContext,
						UserInfo.searchApp.get(position + 2).applogo,
						desappitem_iv_logo_three);

				// 加载文字
				desappitem_tv_name_three.setText(UserInfo.searchApp
						.get(position + 2).appname);
				String str = (UserInfo.searchApp.get(position + 2).appprice)
						/ 100 + "";
				desappitem_tv_price_three.setText(str);
				final int itemposition = position + 2;
				desappitem_laythree.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						UserInfo uInfo = UserInfo.searchApp.get(itemposition);

						MulDataUtils.dealPicture(mContext, uInfo.applogo,
								iv_appdes_center_logo);
						tv_appdes_center_name.setText(uInfo.appname);
						tv_appdes_center_des.setText(uInfo.duan);
						tv_appdes_center_price.setText(uInfo.appprice / 100
								+ " ");

						// 右侧的数据
						MulDataUtils.dealPicture(mContext, uInfo.applogo,
								iv_appdes_right_logo);
						tv_appdes_right_name.setText(uInfo.appname);
						tv_appdes_right_des.setText(uInfo.duan);
						tv_appdes_right_price.setText(uInfo.appprice / 100
								+ " ");
						// 得到的分数
						String desfen = "扫描安装应用可免费获取" + (uInfo.appprice / 100)
								+ "积分";
						tv_appdes_right_getfen.setText(desfen);
						String appid = Integer.toString(uInfo.appid);
						upDataDianChuangQR(appid);

					}
				});
			} else {
				desappitem_laythree.setVisibility(View.INVISIBLE);
			}

			return viewHolder.getConvertView();
		}
	}
}
