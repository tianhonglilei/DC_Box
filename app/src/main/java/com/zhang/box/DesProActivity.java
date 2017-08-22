package com.zhang.box;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.avm.serialport_142.MainHandler;
import com.avm.serialport_142.utils.Avm;
import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zhang.box.constants.Constants;
import com.zhang.box.services.GoodsOutBrocastReceiver;
import com.zhang.box.utils.ActivitySkipUtil;
import com.zhang.box.utils.AnimationUtils;
import com.zhang.box.utils.BackTime;
import com.zhang.box.utils.ImageLoaderUtils;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.StringUtils;
import com.zhang.box.utils.ToastTools;
import com.zhang.easymoney.net.HttpUtil;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 商品详情页面
 * 
 * @author wang
 * 
 */
public class DesProActivity extends Activity {

	private ImageView iv_des_worn_down;
	private Bitmap iv_des_worn_down_bitmap;
	private ImageView iv_codes_qr;
	private ImageView des_iv_num;
	private Bitmap des_iv_num_bitmap;
	private ImageView iv_codes_loading;
	private Bitmap iv_codes_loading_bitmap;
	private TextView des_tv_nengliang_2;
	private TextView des_tv_nengliang_3;
	private TextView des_tv_danbaizhi_2;
	private TextView des_tv_danbaizhi_3;
	private TextView des_tv_na_2;
	private TextView des_tv_na_3;
	private TextView des_tv_zhifang_2;
	private TextView des_tv_zhifang_3;
	private TextView des_tv_tanshuihuahewu_2;
	private TextView des_tv_tanshuihuahewu_3;
	private Button ib_des_pro_name;
	private ImageView des_pro;
	private TextView tv_codes_price;
	private ImageButton des_ib_dianchuang;
	private ImageButton des_ib_weichat;
	private ImageButton des_ib_alipay;
	private Button des_btn_return;
	private ImageView iv_kefuhaoma;
	private Bitmap iv_kefuhaoma_bitmap;
	private ImageView iv_worning;
	private Bitmap iv_worning_bitmap;
	private Context mContext;
	private BooksDB mBooksDB;
	private int width;
	private int height;
	private BackTime bt_main;
	private TextView des_time_return;
	private Timestamp now;
	private String uptradeno;
	private Bitmap bitmap;
	private Bitmap iv_zhuanpan_bitmap;
	private int typIndex;// 1点创 2微信 3支付宝
	private WeiXinQRNetTask mWeiXinQRNetTask; // 微信二维码请求
	private String mchTradeNo;
	private String WeiXinUrl;
	private ZiFuBaoNetTask mzhifubaoNetTask;
	private String ZhiFuBaoUrl;
	private ZhifuNetTask ZhifuNetTask;
	private zhifubaohuodaostatusNetTask zhifubaohuodaostatus;
	private ImageView iv_zhuanpan;
	private String huogui;
	private String weixintradeno;
	private GoodsOutBrocastReceiver myReceiver;
	private static final int MSG_ONE = 1;// 货道小于10
	private static final int MSG_TWO = 2;// 货道大于等于10
	private UserInfo desInfos;

	private Handler mHandlerNo = new Handler() {

		public void handleMessage(Message msg) { // 该方法是在UI主线程中执行
			switch (msg.what) {
			case MSG_ONE:
				iniHuoDaoData(huogui, "0" + desInfos.hdid);
				break;
			case MSG_TWO:
				iniHuoDaoData(huogui, String.valueOf(desInfos.hdid));
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.des_pro);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;
		// 获取屏幕长宽
		WindowManager wm = getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		mBooksDB = new BooksDB(mContext);
		Bundle bundle = this.getIntent().getExtras();
		desInfos = (UserInfo) bundle.getSerializable("proInfos");

		InitView();// 初始化布局
		InitData();// 初始化数据
		initListener();// 初始化监听事件
	}

	// 注册一个广播
	private void registerBoradcastReceiver(Context mContext) {
		myReceiver = new GoodsOutBrocastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.avm.serialport.OUT_GOODS");
		registerReceiver(myReceiver, filter);
	}

	/** 初始化监听事件 */
	private void initListener() {

		// 点击点创按钮生成的二维码
		des_ib_dianchuang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bt_main.concel();
				bt_main.start();
				typIndex = 1;
				tv_codes_price.setText("价格:" + (float) desInfos.price / 100
						+ "0" + "元");
				des_ib_dianchuang
						.setBackgroundResource(R.drawable.payment_wailian_selected);
				des_ib_dianchuang.setSelected(true);
				des_ib_weichat.setSelected(false);
				des_ib_alipay.setSelected(false);
				updataDianChuangPayQR(0);
			}
		});

		//TODO WHWH点击微信按钮生成的二维码
		des_ib_weichat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bt_main.concel();
				bt_main.start();
				typIndex = 2;
				tv_codes_price.setText("价格:" + ((float) desInfos.weixinprice)
						/ 100 + "0" + "元");
				des_ib_weichat
						.setBackgroundResource(R.drawable.payment_wechat_selected);
				des_ib_dianchuang.setSelected(false);
				des_ib_weichat.setSelected(true);
				des_ib_alipay.setSelected(false);
				updataWeiChatPayQR(2);// 微信支付生成的二维码
			}
		});

		// 点击支付宝按钮生成的二维码
		des_ib_alipay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bt_main.concel();
				bt_main.start();
				typIndex = 3;
				tv_codes_price.setText("价格:"
						+ ((float) desInfos.zhifubaoprice / 100) + "0" + "元");
				des_ib_weichat
						.setBackgroundResource(R.drawable.payment_wechat_selector);
				des_ib_dianchuang.setSelected(false);
				des_ib_weichat.setSelected(false);
				des_ib_alipay.setSelected(true);
				upDataZhiFuBaoQR(3);// 支付宝支付
			}
		});

		/** 返回按钮 */
		des_btn_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/** 初始化数据 */
	private void InitData() {

		// 左侧数据添加
		UserInfo.proInfos.clear();
		// 营养成分数据
		mBooksDB.ReadProInfo(desInfos.id);
		if (UserInfo.proInfos != null && UserInfo.proInfos.size() > 0) {

			UserInfo userInfo = UserInfo.proInfos.get(0);
			des_tv_nengliang_2.setText(userInfo.hundrend);
			des_tv_nengliang_3.setText(userInfo.bai);
			UserInfo userInfo1 = UserInfo.proInfos.get(1);
			des_tv_danbaizhi_2.setText(userInfo1.hundrend);
			des_tv_danbaizhi_3.setText(userInfo1.bai);
			UserInfo userInfo2 = UserInfo.proInfos.get(2);
			des_tv_zhifang_2.setText(userInfo2.hundrend);
			des_tv_zhifang_3.setText(userInfo2.bai);
			UserInfo userInfo3 = UserInfo.proInfos.get(3);
			des_tv_tanshuihuahewu_2.setText(userInfo3.hundrend);
			des_tv_tanshuihuahewu_3.setText(userInfo3.bai);
			UserInfo userInfo4 = UserInfo.proInfos.get(4);
			des_tv_na_2.setText(userInfo4.hundrend);
			des_tv_na_3.setText(userInfo4.bai);
		}

		// 中间一块 商品展示LOGO和名称
		ib_des_pro_name.setText(desInfos.name);
		MulDataUtils.dealPicture(mContext, desInfos.probig, des_pro);
		AnimationUtils.translateAnimations(des_pro);

		if (desInfos.prohuogui != null && (desInfos.prohuogui).length() == 1) {
			huogui = "0" + desInfos.prohuogui;
		} else {
			huogui = desInfos.prohuogui;
		}

		// 右侧一块 商品价格
		now = new Timestamp(System.currentTimeMillis());
		uptradeno = now + "," + Integer.toString(desInfos.id) + ","
				+ SysData.imei + "," + desInfos.hdid;

		tv_codes_price
				.setText("价格:" + (float) desInfos.price / 100 + "0" + "元");

		// 点创支付生成的 二维码
		// updataDianChuangPayQR(0);// 点创支付生成的二维码
		updataWeiChatPayQR(2);// 微信支付生成的二维码
	}

	/** 初始化布局控件 */
	private void InitView() {

		iv_zhuanpan = (ImageView) findViewById(R.id.iv_zhuanpan);

		iv_zhuanpan_bitmap = ImageLoaderUtils.loadHugeBitmapFromDrawable(
				getResources(), R.drawable.des_yy_pan, height, width);
		iv_zhuanpan.setImageBitmap(iv_zhuanpan_bitmap);

		des_tv_nengliang_2 = (TextView) findViewById(R.id.des_tv_nengliang_2);
		des_tv_nengliang_3 = (TextView) findViewById(R.id.des_tv_nengliang_3);

		des_tv_danbaizhi_2 = (TextView) findViewById(R.id.des_tv_danbaizhi_2);
		des_tv_danbaizhi_3 = (TextView) findViewById(R.id.des_tv_danbaizhi_3);

		des_tv_na_2 = (TextView) findViewById(R.id.des_tv_na_2);
		des_tv_na_3 = (TextView) findViewById(R.id.des_tv_na_3);

		des_tv_zhifang_2 = (TextView) findViewById(R.id.des_tv_zhifang_2);
		des_tv_zhifang_3 = (TextView) findViewById(R.id.des_tv_zhifang_3);

		des_tv_tanshuihuahewu_2 = (TextView) findViewById(R.id.des_tv_tanshuihuahewu_2);
		des_tv_tanshuihuahewu_3 = (TextView) findViewById(R.id.des_tv_tanshuihuahewu_3);

		ib_des_pro_name = (Button) findViewById(R.id.ib_des_pro_name);// 商品名称
																		// 动画效果
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(1000);
		ib_des_pro_name.startAnimation(sa);

		des_pro = (ImageView) findViewById(R.id.des_pro);// 商品logo

		// 商品详情支付页面控件之支付展示

		iv_des_worn_down = (ImageView) findViewById(R.id.iv_des_worn_down);// 二维码
																			// 手指标
		iv_des_worn_down.post(new Runnable() {

			@Override
			public void run() {
				iv_des_worn_down_bitmap = ImageLoaderUtils
						.loadHugeBitmapFromDrawable(getResources(),
								R.drawable.des_worn_down, height, width);
				iv_des_worn_down.setImageBitmap(iv_des_worn_down_bitmap);
			}
		});

		iv_codes_qr = (ImageView) findViewById(R.id.iv_codes_qr);

		iv_codes_loading = (ImageView) findViewById(R.id.iv_codes_loading);// 正在加载的二维码
		iv_codes_loading.post(new Runnable() {

			@Override
			public void run() {
				iv_codes_loading_bitmap = ImageLoaderUtils
						.loadHugeBitmapFromDrawable(getResources(),
								R.drawable.qr_loading, height, width);
				iv_codes_loading.setImageBitmap(iv_codes_loading_bitmap);
			}
		});

		des_iv_num = (ImageView) findViewById(R.id.des_iv_num); // 商品数量图片
		des_iv_num.post(new Runnable() {

			@Override
			public void run() {
				des_iv_num_bitmap = ImageLoaderUtils
						.loadHugeBitmapFromDrawable(getResources(),
								R.drawable.des_pro_num, height, width);
				des_iv_num.setImageBitmap(des_iv_num_bitmap);
			}
		});

		tv_codes_price = (TextView) findViewById(R.id.tv_codes_price);
		des_ib_dianchuang = (ImageButton) findViewById(R.id.des_ib_dianchuang);// 点创支付按钮
		// des_ib_dianchuang.setBackgroundResource(R.drawable.payment_wailian_selected);

		des_ib_weichat = (ImageButton) findViewById(R.id.des_ib_weichat); // 微信支付按钮
		des_ib_weichat
				.setBackgroundResource(R.drawable.payment_wechat_selected);
		des_ib_alipay = (ImageButton) findViewById(R.id.des_ib_alipay); // 支付宝支付按钮

		des_btn_return = (Button) findViewById(R.id.des_btn_return);

		iv_kefuhaoma = (ImageView) findViewById(R.id.iv_kefuhaoma);
		iv_kefuhaoma.post(new Runnable() {

			@Override
			public void run() {
				iv_kefuhaoma_bitmap = ImageLoaderUtils
						.loadHugeBitmapFromDrawable(getResources(),
								R.drawable.des_kefu, height, width);
				iv_kefuhaoma.setImageBitmap(iv_kefuhaoma_bitmap);
			}
		});

		iv_worning = (ImageView) findViewById(R.id.iv_worning);
		iv_worning.post(new Runnable() {

			@Override
			public void run() {
				iv_worning_bitmap = ImageLoaderUtils
						.loadHugeBitmapFromDrawable(getResources(),
								R.drawable.des_worn_bg, height, width);
				iv_worning.setImageBitmap(iv_worning_bitmap);
			}
		});

		// 倒计时按钮
		des_time_return = (TextView) findViewById(R.id.des_time_return);
		bt_main = new BackTime(des_time_return, "0", 90, 1);
		bt_main.setOnFinishListener(new BackTime.OnFinishListener() {
			@Override
			public void finish() {
				((Activity) mContext).finish();
			}

			@Override
			public void center(long time) {
			}
		});
		bt_main.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		clearTask();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (myReceiver != null) {
			unregisterReceiver(myReceiver);
		}

		// 释放图片
		if (iv_zhuanpan_bitmap != null && !iv_zhuanpan_bitmap.isRecycled()) {
			iv_zhuanpan_bitmap.recycle();
			iv_zhuanpan_bitmap = null;
		}

		if (iv_des_worn_down_bitmap != null
				&& !iv_des_worn_down_bitmap.isRecycled()) {

			iv_des_worn_down_bitmap.recycle();
			iv_des_worn_down_bitmap = null;
		}

		if (des_iv_num_bitmap != null && !des_iv_num_bitmap.isRecycled()) {

			des_iv_num_bitmap.recycle();
			des_iv_num_bitmap = null;
		}

		// 释放内存
		ImageLoaderUtils.releaseImageViewResouce(iv_codes_qr);

		if (iv_codes_loading_bitmap != null
				&& !iv_codes_loading_bitmap.isRecycled()) {

			iv_codes_loading_bitmap.recycle();
			iv_codes_loading_bitmap = null;
		}

		if (iv_kefuhaoma_bitmap != null && !iv_kefuhaoma_bitmap.isRecycled()) {

			iv_kefuhaoma_bitmap.recycle();
			iv_kefuhaoma_bitmap = null;
		}

		if (iv_worning_bitmap != null && !iv_worning_bitmap.isRecycled()) {

			iv_worning_bitmap.recycle();
			iv_worning_bitmap = null;
		}

		System.gc();
	}

	// TODO 点创支付生成的 二维码
	private void updataDianChuangPayQR(int i) {
		iv_codes_qr.setVisibility(View.INVISIBLE);
		iv_codes_loading.setVisibility(View.VISIBLE);
		clearTask();
		Message msg = mHandler_dianchuangQR.obtainMessage(0, 0, 0);
		mHandler_dianchuangQR.sendMessage(msg);
	}

	/** 生成二维码请求 */
	private Handler mHandler_dianchuangQR = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			String tradeno = now + "," + Integer.toString(desInfos.id) + ","
					+ SysData.imei + "," + desInfos.hdid + ","
					+ desInfos.prohuogui;
			String title = desInfos.name;
			String upprice = Float.toString((float) desInfos.price / 100);
			// String upprice = "0.01";
			String url = Constants.dainchaungQRUrlLHL+ tradeno + "&title=" + title + "&price=" + upprice;
			Log.e("whwh", "des--点创二维码地址--->" + url);
			createQRImage(url);
			iv_codes_qr.setVisibility(View.VISIBLE);
			iv_codes_loading.setVisibility(View.INVISIBLE);
			typIndex = 1;
			// 支付
			upZhifu(1);
		}
	};

	// TODO 生成的微信二维码
	protected void updataWeiChatPayQR(int i) {
		iv_codes_qr.setVisibility(View.INVISIBLE);
		iv_codes_loading.setVisibility(View.VISIBLE);
		clearTask();
		mWeiXinQRNetTask = new WeiXinQRNetTask();
		mWeiXinQRNetTask.execute();
	}

	// 网络请求生成 微信二维码
	class WeiXinQRNetTask extends AsyncTask<Object, Integer, String> {

		private WeiXinQRNetTask() {
		}

		protected String doInBackground(Object... params) {
			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					10);
			mchTradeNo = StringUtils.getRandonInt(20);
			String weixinprice = Float
					.toString((float) desInfos.weixinprice / 100);
			//String price = "0.01";
			nameValuePairs.add(new BasicNameValuePair("tradeAmt", weixinprice));
			nameValuePairs.add(new BasicNameValuePair("body", desInfos.des)); // desTitle
			nameValuePairs
					.add(new BasicNameValuePair("mchTradeNo", mchTradeNo));
			String subject = desInfos.des + "|" + desInfos.id + "|"
					+ desInfos.hdid + "|" + SysData.imei + "|"
					+ desInfos.prohuogui;
			nameValuePairs.add(new BasicNameValuePair("subject", subject));
			//TODO WHWH
			url = Constants.weixinQRUrlZZQ;
			String json = HttpUtil.RequestGetData(url, nameValuePairs);
			Log.e("wh",
					"des-->微信请求二维码地址参数-->nameValuePairs"
							+ nameValuePairs.toString());
			Log.e("wh", "des-->微信请求二维码地址-->json" + json);
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				ToastTools.showShort(mContext, "网络加载失败,请重新点击!");
			} else {
				try {
					//{"error":0 ,"url":----}
					JSONObject jsonObject = new JSONObject(result);
					int resultCode = jsonObject.getInt("error");
					if (resultCode == 0) {
						WeiXinUrl = jsonObject.getString("url");
						createQRImage(WeiXinUrl);
						weixintradeno = jsonObject.getString("tradeno");
						Log.e("whwhwh", "weixintradeno====" + weixintradeno);
						iv_codes_qr.setVisibility(View.VISIBLE);
						iv_codes_loading.setVisibility(View.INVISIBLE);
						Log.e("wh", "微信的二维码生成了。。。。。");
						typIndex = 2;
						upZhifu(typIndex);
					} else {
						Toast.makeText(DesProActivity.this, "微信二维码出现异常!",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** 支付宝二维码生成 */
	// TODO 支付宝二维码生成
	protected void upDataZhiFuBaoQR(int i) {
		iv_codes_qr.setVisibility(View.INVISIBLE);
		iv_codes_loading.setVisibility(View.VISIBLE);
		clearTask();
		mzhifubaoNetTask = new ZiFuBaoNetTask();
		mzhifubaoNetTask.execute();
	}

	// 网络请求生成 支付宝二维码
	class ZiFuBaoNetTask extends AsyncTask<Object, Integer, String> {

		private ZiFuBaoNetTask() {

		}

		protected String doInBackground(Object... params) {
			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

			uptradeno = now + "," + Integer.toString(desInfos.id) + ","
					+ SysData.imei + "," + desInfos.hdid + ","
					+ desInfos.prohuogui;
			nameValuePairs.add(new BasicNameValuePair("tradeno", uptradeno));
			 String zhiprice = ((float) desInfos.zhifubaoprice / 100) + "";
//			String zhiprice = 0.01 + "";
			nameValuePairs.add(new BasicNameValuePair("price", zhiprice));
			nameValuePairs.add(new BasicNameValuePair("title", desInfos.des));
			nameValuePairs.add(new BasicNameValuePair("des", desInfos.des));
			url = Constants.zhifubaoQRUrlLHL;
			String json = HttpUtil.RequestGetData(url, nameValuePairs);
			Log.e("whwhwh", "支付宝二维码生成结果:" + json);
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				ToastTools.showShort(mContext, "网络加载失败,请重新点击!");
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					int resultCode = jsonObject.getInt("err");
					if (resultCode == 0) {
						ZhiFuBaoUrl = jsonObject.getString("url");
						createQRImage(ZhiFuBaoUrl);
						iv_codes_qr.setVisibility(View.VISIBLE);
						iv_codes_loading.setVisibility(View.INVISIBLE);
						Log.e("wh", "支付宝的二维码生成了。。。。。");
						typIndex = 3;
						upZhifu(typIndex);
					} else {
						Toast.makeText(DesProActivity.this, "支付宝二维码出现异常!",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** 扫描二维码后进行支付 */
	protected void upZhifu(int i) {

		clearTask();
		ZhifuNetTask = new ZhifuNetTask();
		ZhifuNetTask.execute();
	}

	class ZhifuNetTask extends AsyncTask<Object, Integer, String> {

		private ZhifuNetTask() {
		}

		protected String doInBackground(Object... params) {
			String url = "";
			String json = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);

			nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));
			String pid = Integer.toString(desInfos.id);

			nameValuePairs.add(new BasicNameValuePair("pid", pid));
			uptradeno = now + "," + Integer.toString(desInfos.id) + ","
					+ SysData.imei + "," + desInfos.hdid + ","
					+ desInfos.prohuogui;

			nameValuePairs.add(new BasicNameValuePair("tradeno", uptradeno));
			String hdid = Integer.toString(desInfos.hdid);
			nameValuePairs.add(new BasicNameValuePair("hdid", hdid));
			nameValuePairs.add(new BasicNameValuePair("hgid",
					desInfos.prohuogui));
			nameValuePairs.add(new BasicNameValuePair("sum", 1 + ""));

			// typIndex ;//1点创 2微信 3支付宝
			if (typIndex == 3) {
				url = Constants.zhifubaoPayUrlLHL;
			} else if (typIndex == 1) {
				url = Constants.dainchaungPayUrlLHL;
			} else if (typIndex == 2) {
				nameValuePairs.add(new BasicNameValuePair("weixintradeno",
						weixintradeno));
				nameValuePairs.add(new BasicNameValuePair("mchTradeNo",
						mchTradeNo));
				url = Constants.weixinPayUrlLHL;
			}
			json = HttpUtil.RequestGetData(url, nameValuePairs);
			Log.e("whwh", "des--->支付--->" + typIndex + "nameValuePairs=="
					+ nameValuePairs.toString());
			Log.e("whwhwh", "des--->支付--->" + typIndex + "json==" + json);
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {

			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					int resultCode = jsonObject.getInt("error");
					if (resultCode == 0) {
						if (desInfos.hdid < 10) {
							UserInfo.sucessTitle = desInfos.name;
							UserInfo.sucessLogo = desInfos.logo;
							mHandlerNo.obtainMessage(MSG_ONE).sendToTarget();

						} else {
							mHandlerNo.obtainMessage(MSG_TWO).sendToTarget();
							UserInfo.sucessTitle = desInfos.name;
							UserInfo.sucessLogo = desInfos.logo;
						}

						if (zhifubaohuodaostatus != null) {
							zhifubaohuodaostatus.cancel(true);
						}
						zhifubaohuodaostatus(typIndex);

					} else if (resultCode != 0) {
						if (!uptradeno.isEmpty()) {
							upZhifu(typIndex);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
	}

	// TODO 支付成功后的接口
	private void zhifubaohuodaostatus(int i) {
		zhifubaohuodaostatus = new zhifubaohuodaostatusNetTask();
		zhifubaohuodaostatus.execute();
	}

	// 支付成功后返回的信息
	class zhifubaohuodaostatusNetTask extends
			AsyncTask<Object, Integer, String> {

		private String pid;
		private int resultMax;
		private int resultMfinish;

		private zhifubaohuodaostatusNetTask() {
		}

		protected String doInBackground(Object... params) {
			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));
			// pid = Integer.toString(UserInfo.desId);
			pid = Integer.toString(desInfos.id);
			nameValuePairs.add(new BasicNameValuePair("pid", pid));
			nameValuePairs.add(new BasicNameValuePair("tradeno", uptradeno));
			// String hdid = Integer.toString(UserInfo.desHdid);
			String hdid = Integer.toString(desInfos.hdid);
			nameValuePairs.add(new BasicNameValuePair("hdid", hdid));
			// nameValuePairs.add(new
			// BasicNameValuePair("hgid",UserInfo.deshuogui));
			nameValuePairs.add(new BasicNameValuePair("hgid",
					desInfos.prohuogui));
			// nameValuePairs.add(new BasicNameValuePair("title",
			// UserInfo.desInfo));
			nameValuePairs.add(new BasicNameValuePair("title", desInfos.des));

			nameValuePairs.add(new BasicNameValuePair("sum", 1 + ""));

			// typIndex ;//1点创 2微信 3支付宝
			//TODO WHWH
			if (typIndex == 3) {
				// String zhifubaoprice = Float.toString((float)
				// UserInfo.desZhiFuBaoPrice / 100);
				String zhifubaoprice = Float
						.toString((float) desInfos.zhifubaoprice / 100);
				nameValuePairs.add(new BasicNameValuePair("price",
						zhifubaoprice));
				url = Constants.zhifubaoStatusUrlLHL;

			} else if (typIndex == 1) {
				// String dianchaungprice = Float.toString((float)
				// UserInfo.desPrice / 100);
				String dianchaungprice = Float
						.toString((float) desInfos.price / 100);

				// String price = "0.01";
				nameValuePairs.add(new BasicNameValuePair("price",
						dianchaungprice));
				
				url =Constants.dainchaungStatusUrlLHL;
			} else if (typIndex == 2) {
				// String weixinprice = Float
				// .toString((float) UserInfo.desWeiXinPrice / 100);

				String weixinprice = Float
						.toString((float) desInfos.weixinprice / 100);
				nameValuePairs
						.add(new BasicNameValuePair("price", weixinprice));
				nameValuePairs.add(new BasicNameValuePair("mchTradeNo",
						mchTradeNo));
				nameValuePairs.add(new BasicNameValuePair("weixintradeno",
						weixintradeno));
				url = Constants.weixinStatusUrlLHL;
			}
			String json = HttpUtil.RequestGetData(url, nameValuePairs);
			Log.e("whwh", "支付成功的状态==json" + "typIndex---" + typIndex + "json"
					+ json);
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {

			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					int resultCode = jsonObject.getInt("error");
					resultMax = jsonObject.getInt("max");
					resultMfinish = jsonObject.getInt("mfinish");
					// 处理成功后进行处理
					if (resultCode == 0) {
						uptradeno = "";
						// int huogui = Integer.parseInt(UserInfo.deshuogui);
						// Log.e("ww", "修改的数据:id==" + UserInfo.desId +
						// "huogui=="
						// + huogui + "hdid==" + UserInfo.desHdid
						// + "resultMfinish==" + resultMfinish);

						// mBooksDB.updateFinish(UserInfo.desId, huogui,
						// UserInfo.desHdid, resultMfinish);

						int huogui = Integer.parseInt(desInfos.prohuogui);
						Log.e("ww", "修改的数据:id==" + desInfos.id + "huogui=="
								+ huogui + "hdid==" + desInfos.hdid
								+ "resultMfinish==" + resultMfinish);
						mBooksDB.updateFinish(desInfos.id, huogui,
								desInfos.hdid, resultMfinish);

					} else {
						zhifubaohuodaostatus(typIndex);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// log
			}
		}
	}

	/** 生成创建二维码 */
	public void createQRImage(String url) {
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
			iv_codes_qr.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	/** 配置信息 调用机器出货接口 */
	private void iniHuoDaoData(String huogui, String num) {
		String nums = huogui + "1" + num + "00000100";
		int numcode = (int) ((Math.random() * 9 + 1) * 100000);
		// Avm.OUT_GOODS_ALIPAY "50"
		if (desInfos.protype == 1) {
			if (MainHandler.noticeAvmOutGoods(nums + Avm.OUT_GOODS_ALIPAY,
					numcode + "")) {
				Log.e("whwhwh", num + "正在发送出货通知!");
				// 注册广播
				registerBoradcastReceiver(mContext);

			} else {
				Log.e("whwhwh", num + "发送出货通知失败!");
			}
		} else {
			if (MainHandler.noticeAvmOutGoods(nums + "50", numcode + "")) {
				Log.e("whwhwh", num + "正在发送出货通知!");
				ActivitySkipUtil.skipAnotherActivity(DesProActivity.this,
						ProSucessActivity.class, true);
			} else {
				Log.e("whwhwh", num + "发送出货通知失败!");
			}
		}
	}

	/** 取消点创 、微信、支付宝 二维码和支付线程 */
	private void clearTask() {

		if (ZhifuNetTask != null) {
			if (!ZhifuNetTask.isCancelled()) {
				ZhifuNetTask.cancel(true);
			}
		}

		if (mHandler_dianchuangQR != null) {
			mHandler_dianchuangQR.removeCallbacks(null);
		}

		if (mzhifubaoNetTask != null) {
			if (!mzhifubaoNetTask.isCancelled()) {
				mzhifubaoNetTask.cancel(true);
			}
		}

		if (mWeiXinQRNetTask != null) {
			if (!mWeiXinQRNetTask.isCancelled()) {
				mWeiXinQRNetTask.cancel(true);
			}
		}
	}
}
