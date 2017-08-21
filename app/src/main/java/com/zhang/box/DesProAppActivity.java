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
import com.example.zzq.adapter.AppDesJPGridViewAdapter;
import com.example.zzq.adapter.MyDesProAppAdapter;
import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zhang.box.constants.Constants;
import com.zhang.box.services.GoodsOutBrocastReceiver;
import com.zhang.box.utils.AnimationUtils;
import com.zhang.box.utils.BackTime;
import com.zhang.box.utils.ImageLoaderUtils;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.StringUtils;
import com.zhang.box.utils.ToastTools;
import com.zhang.easymoney.net.HttpUtil;
import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint({ "HandlerLeak", "ResourceAsColor" })
public class DesProAppActivity extends Activity {

	private Context mContext;
	private int width;
	private int height;
	private ListView pro_app_lv;
	private ImageView iv_drink_center;
	private TextView tv_pro_name_right;
	private TextView tv_pro_price_right;
	private ImageView iv_qr_right;
	private ImageView iv_qrloading_right;
	private Bitmap iv_codes_loading_bitmap;
	private ImageButton despro_ib_dianchuang;
	private TextView despro_des_dianchaung;
	private ImageButton despro_ib_weichat;
	private TextView despro_des_weixin;
	private ImageButton despro_ib_alipay;
	private TextView despro_des_zhifubao;
	private Button proappbtn3;
	private TextView tv_proapp_backTime;
	private BooksDB mBooksDB;
	private String huogui;
	private Timestamp now;
	private String uptradeno;
	private int typIndex;// 1点创 2微信 3支付宝
	private Bitmap bitmap;
	private BackTime bt_main;
	private WeiXinQRNetTask mWeiXinQRNetTask; // 微信二维码请求
	private String mchTradeNo;
	private String WeiXinUrl;
	private ZiFuBaoNetTask mzhifubaoNetTask;
	private String ZhiFuBaoUrl;
	private ZhifuNetTask ZhifuNetTask;
	private zhifubaohuodaostatusNetTask zhifubaohuodaostatus;
	private String weixintradeno;
	private static final int MSG_ONE = 1;// 货道小于10
	private static final int MSG_TWO = 2;// 货道大于等于10
	private GoodsOutBrocastReceiver myReceiver;
	private MyDesProAppAdapter mDesProAppAdapter;
	private GridView desproapp_grid;
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
		setContentView(R.layout.des_pro_app);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;
		// 获取屏幕长宽
		WindowManager wm = getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		Bundle bundle = this.getIntent().getExtras();
		desInfos = (UserInfo) bundle.getSerializable("proInfos");
		// TODO WH
		initView();
		initData();
		initListener();

	}

	/** 配置信息 调用机器出货接口 */
	private void iniHuoDaoData(String huogui, String num) {
		String nums = huogui + "1" + num + "00000100";
		int numcode = (int) ((Math.random() * 9 + 1) * 100000);
		if (MainHandler.noticeAvmOutGoods(nums + Avm.OUT_GOODS_ALIPAY, numcode
				+ "")) {
			Log.e("whwhwh", num + "正在发送出货通知!");
			// 注册广播
			registerBoradcastReceiver(mContext);

		} else {
			Log.e("whwhwh", num + "发送出货通知失败!");
		}
	}

	// 注册一个广播
	private void registerBoradcastReceiver(Context mContext) {
		myReceiver = new GoodsOutBrocastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.avm.serialport.OUT_GOODS");
		registerReceiver(myReceiver, filter);
	}

	/** 初始化布局 */
	private void initView() {
		// 左侧部分
		pro_app_lv = (ListView) findViewById(R.id.pro_app_lv);

		// 中间部分
		iv_drink_center = (ImageView) findViewById(R.id.iv_drink_center);

		// 右侧部分
		tv_pro_name_right = (TextView) findViewById(R.id.tv_pro_name_right);
		tv_pro_price_right = (TextView) findViewById(R.id.tv_pro_price_right);

		iv_qr_right = (ImageView) findViewById(R.id.iv_qr_right);
		iv_qrloading_right = (ImageView) findViewById(R.id.iv_qrloading_right);
		iv_qrloading_right.post(new Runnable() {

			@Override
			public void run() {
				iv_codes_loading_bitmap = ImageLoaderUtils
						.loadHugeBitmapFromDrawable(getResources(),
								R.drawable.qr_loading, height, width);
				iv_qrloading_right.setImageBitmap(iv_codes_loading_bitmap);
			}
		});

		despro_ib_dianchuang = (ImageButton) findViewById(R.id.despro_ib_dianchuang);
		despro_ib_dianchuang
				.setBackgroundResource(R.drawable.payment_wailian_selected2);
		despro_des_dianchaung = (TextView) findViewById(R.id.despro_des_dianchaung);

		despro_ib_weichat = (ImageButton) findViewById(R.id.despro_ib_weichat);
		despro_des_weixin = (TextView) findViewById(R.id.despro_des_weixin);

		despro_ib_alipay = (ImageButton) findViewById(R.id.despro_ib_alipay);
		despro_des_zhifubao = (TextView) findViewById(R.id.despro_des_zhifubao);

		proappbtn3 = (Button) findViewById(R.id.proappbtn3);
		tv_proapp_backTime = (TextView) findViewById(R.id.tv_proapp_backTime);
		bt_main = new BackTime(tv_proapp_backTime, "0", 90, 1);
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

		desproapp_grid = (GridView) findViewById(R.id.desproapp_grid);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 左侧的数据
		mBooksDB = new BooksDB(this);
		search(desInfos.price);
	}

	// TODO WHWH
	/** 初始化数据 */
	private void initData() {

		// 左侧的数据
		mBooksDB = new BooksDB(this);
		search(desInfos.price);

		// 中间的数据
		MulDataUtils.dealPicture(mContext, desInfos.probig, iv_drink_center);
		AnimationUtils.translateAnimations(iv_drink_center);

		// 右侧的数据
		tv_pro_name_right.setText(desInfos.name + "   ");
		int desPrice = desInfos.price / 100;
		float desPrices = (float) desInfos.price / 100;
		tv_pro_price_right.setText("¥" + desPrices + "0" + "   " + "或" + "   "
				+ desPrice + " ");

		if (desInfos.prohuogui != null && (desInfos.prohuogui).length() == 1) {
			huogui = "0" + desInfos.prohuogui;
		} else {
			huogui = desInfos.prohuogui;
		}

		// 右侧一块 商品价格
		now = new Timestamp(System.currentTimeMillis());
		uptradeno = now + "," + Integer.toString(desInfos.id) + ","
				+ SysData.imei + "," + desInfos.hdid;

		// 点创支付生成的 二维码
		updataDianChuangPayQR(0);// 点创支付生成的二维码
	}

	/** 点创生成二维码 */
	private void updataDianChuangPayQR(int i) {
		iv_qr_right.setVisibility(View.INVISIBLE);
		iv_qrloading_right.setVisibility(View.VISIBLE);
		clearTask();
		Message msg = mHandler_dianchuangQR.obtainMessage(0, 0, 0);
		mHandler_dianchuangQR.sendMessage(msg);
	}

	// TODO
	/** 生成二维码请求 */
	private Handler mHandler_dianchuangQR = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			String tradeno = now + "," + Integer.toString(desInfos.id) + ","
					+ SysData.imei + "," + desInfos.hdid + ","
					+ desInfos.prohuogui;
			String title = desInfos.name;
			String upprice = Float.toString((float) desInfos.price / 100);
			String url = Constants.dainchaungQRUrlLHL
					+ tradeno + "&title=" + title + "&price=" + upprice;
			Log.e("whwh", "des--点创二维码地址--->" + url);
			createQRImage(url);
			iv_qr_right.setVisibility(View.VISIBLE);
			iv_qrloading_right.setVisibility(View.INVISIBLE);
			typIndex = 1;
			// 支付
			upZhifu(1);
		}
	};

	// 清除任务栈
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
				url = "http://"+ Constants.baseUrlLHL+"/boxapp/?c=welcome&m=box_zhifubaohuodao";
			} else if (typIndex == 1) {
				url = "http://"+ Constants.baseUrlLHL+"/boxapp/?c=welcome&m=box_apphuodaostatus";
			} else if (typIndex == 2) {
				nameValuePairs.add(new BasicNameValuePair("weixintradeno",
						weixintradeno));
				nameValuePairs.add(new BasicNameValuePair("mchTradeNo",
						mchTradeNo));
				url = "http://"+ Constants.baseUrlLHL+"/boxapp/?c=welcome&m=box_weixinuodao";
			}
			json = HttpUtil.RequestGetData(url, nameValuePairs);
			Log.e("whwh", "des--->支付--->" + typIndex + "nameValuePairs=="
					+ nameValuePairs.toString());
			Log.e("whwh", "des--->支付--->" + typIndex + "json==" + json);
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
						// TODO WHWH
						if (desInfos.hdid < 10) {
							UserInfo.sucessTitle = desInfos.name;
							UserInfo.sucessLogo = desInfos.logo;
							
							mHandlerNo.obtainMessage(MSG_ONE).sendToTarget();

						} else {
							UserInfo.sucessTitle = desInfos.name;
							UserInfo.sucessLogo = desInfos.logo;
							mHandlerNo.obtainMessage(MSG_TWO).sendToTarget();
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

		private zhifubaohuodaostatusNetTask() {
		}

		protected String doInBackground(Object... params) {
			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));

			pid = Integer.toString(desInfos.id);
			nameValuePairs.add(new BasicNameValuePair("pid", pid));
			nameValuePairs.add(new BasicNameValuePair("tradeno", uptradeno));
			String hdid = Integer.toString(desInfos.hdid);
			nameValuePairs.add(new BasicNameValuePair("hdid", hdid));
			nameValuePairs.add(new BasicNameValuePair("hgid",desInfos.prohuogui));
			nameValuePairs.add(new BasicNameValuePair("title",
					desInfos.name));
			nameValuePairs.add(new BasicNameValuePair("sum", 1 + ""));

			// typIndex ;//1点创 2微信 3支付宝
			if (typIndex == 3) {
				String zhifubaoprice = Float
						.toString((float) desInfos.zhifubaoprice / 100);
				nameValuePairs.add(new BasicNameValuePair("price",
						zhifubaoprice));
				//url = Constants.baseUrl + Constants.deszhifubaozhifustatsUrl;
				url = Constants.zhifubaoStatusUrlZZQ;
			} else if (typIndex == 1) {
				String dianchaungprice = Float
						.toString((float) desInfos.price / 100);
				// String price = "0.01";
				nameValuePairs.add(new BasicNameValuePair("price",
						dianchaungprice));
				url = "http://"+ Constants.baseUrlLHL+"/boxapp/?c=welcome&m=box_appchuhuosuccess";
			} else if (typIndex == 2) {
				String weixinprice = Float
						.toString((float) desInfos.weixinprice / 100);
				nameValuePairs
						.add(new BasicNameValuePair("price", weixinprice));
				nameValuePairs.add(new BasicNameValuePair("mchTradeNo",
						mchTradeNo));
				nameValuePairs.add(new BasicNameValuePair("weixintradeno",
						weixintradeno));
				url = "http://"+ Constants.baseUrlLHL+"/boxapp/?c=welcome&m=box_weixinhuodaostatus";
			}
			String json = HttpUtil.RequestGetData(url, nameValuePairs);
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
					// 处理成功后进行处理
					if (resultCode == 0) {
						uptradeno = "";
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

	// TODO WH
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
			iv_qr_right.post(new Runnable() {
				@Override
				public void run() {

					iv_qr_right.setImageBitmap(bitmap);
				}
			});
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	/** 根据价格显示出大于或者等于该商品的应用软件 */
	public void search(int price) {
		UserInfo.searchApp.clear();
		mBooksDB.SearchApp(price);
	}

	//TODO WH
	/** 初始化监听事件 */
	private void initListener() {

		// 左侧数据的添加设置 根据价格显示
		mDesProAppAdapter = new MyDesProAppAdapter(mContext);
		pro_app_lv.setAdapter(mDesProAppAdapter);
		mDesProAppAdapter.notifyDataSetChanged();

		// 精品推荐应用
		setGridView();

		// 点击点创按钮生成的二维码
		despro_ib_dianchuang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bt_main.concel();
				bt_main.start();
				typIndex = 1;
				tv_pro_price_right.setText("价格:" + (float) desInfos.price
						/ 100 + "0" + "元");
				despro_ib_dianchuang
						.setBackgroundResource(R.drawable.payment_wailian_selected2);
				despro_ib_dianchuang.setSelected(true);
				despro_ib_weichat.setSelected(false);
				despro_ib_alipay.setSelected(false);
				despro_des_dianchaung.setTextColor(getResources().getColor(
						R.color.red));
				despro_des_weixin.setTextColor(getResources().getColor(
						R.color.black));
				despro_des_zhifubao.setTextColor(getResources().getColor(
						R.color.black));
				updataDianChuangPayQR(0);
			}
		});

		// 点击微信按钮生成的二维码
		despro_ib_weichat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bt_main.concel();
				bt_main.start();
				typIndex = 2;
				tv_pro_price_right.setText("价格:"
						+ ((float) desInfos.weixinprice) / 100 + "0" + "元");
				despro_ib_dianchuang
						.setBackgroundResource(R.drawable.payment_wailian_selected2);
				despro_ib_dianchuang.setSelected(false);
				despro_ib_weichat.setSelected(true);
				despro_ib_alipay.setSelected(false);
				despro_des_dianchaung.setTextColor(getResources().getColor(
						R.color.black));
				despro_des_weixin.setTextColor(getResources().getColor(
						R.color.red));
				despro_des_zhifubao.setTextColor(getResources().getColor(
						R.color.black));
				//updataDianChuangPayQR(0);
				updataWeiChatPayQR(2);// 微信支付生成的二维码
			}
		});

		// 点击支付宝按钮生成的二维码
		despro_ib_alipay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bt_main.concel();
				bt_main.start();
				typIndex = 3;
				tv_pro_price_right
						.setText("价格:"
								+ ((float) desInfos.zhifubaoprice / 100)
								+ "0" + "元");
				despro_ib_dianchuang
						.setBackgroundResource(R.drawable.payment_wailian_selected2);
				despro_ib_dianchuang.setSelected(false);
				despro_ib_weichat.setSelected(false);
				despro_ib_alipay.setSelected(true);
				despro_des_dianchaung.setTextColor(getResources().getColor(
						R.color.black));
				despro_des_weixin.setTextColor(getResources().getColor(
						R.color.black));
				despro_des_zhifubao.setTextColor(getResources().getColor(
						R.color.red));
				upDataZhiFuBaoQR(3);// 支付宝支付
			}
		});

		/** 返回按钮 */
		proappbtn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	// TODO
	private void setGridView() {
		int size = UserInfo.jpAllshow.size();
		int length = 116;
		
		MulDataUtils.setGrideViews(DesProAppActivity.this, desproapp_grid,
				size, length, 4);
		AppDesJPGridViewAdapter adapter = new AppDesJPGridViewAdapter(
				DesProAppActivity.this, UserInfo.jpAllshow);
		adapter.notifyDataSetChanged();
		desproapp_grid.setAdapter(adapter);

		desproapp_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int positions, long id) {

				UserInfo uInfos = UserInfo.jpAllshow.get(positions - 1);
				MulDataUtils.appItemData(mContext, uInfos);
			}
		});
	}

	// TODO 生成的微信二维码
	protected void updataWeiChatPayQR(int i) {
		iv_qr_right.setVisibility(View.INVISIBLE);
		iv_qrloading_right.setVisibility(View.VISIBLE);
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
			// TODO WHWHW
			// String weixinprice = Float
			// .toString((float) UserInfo.desWeiXinPrice / 100);
			//String price = "0.01";
			String weixinprice = Float
					.toString((float) desInfos.weixinprice / 100);
			nameValuePairs.add(new BasicNameValuePair("tradeAmt", weixinprice));
			nameValuePairs
					.add(new BasicNameValuePair("body", desInfos.name));
			nameValuePairs
					.add(new BasicNameValuePair("mchTradeNo", mchTradeNo));
			String subject = desInfos.name + "|" + desInfos.id + "|"
					+ desInfos.hdid + "|" + SysData.imei + "|"
					+ desInfos.prohuogui;
			nameValuePairs.add(new BasicNameValuePair("subject", subject));
			url = "http://www.dc-box.com/weixin/example/native.php";
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
					JSONObject jsonObject = new JSONObject(result);
					int resultCode = jsonObject.getInt("error");
					if (resultCode == 0) {
						WeiXinUrl = jsonObject.getString("url");
						createQRImage(WeiXinUrl);
						weixintradeno = jsonObject.getString("tradeno");
						Log.e("whwhwh", "weixintradeno====" + weixintradeno);
						iv_qr_right.setVisibility(View.VISIBLE);
						iv_qrloading_right.setVisibility(View.INVISIBLE);
						Log.e("wh", "微信的二维码生成了。。。。。");
						typIndex = 2;
						upZhifu(typIndex);
					} else {
						Toast.makeText(DesProAppActivity.this, "微信二维码出现异常!",
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
		iv_qr_right.setVisibility(View.INVISIBLE);
		iv_qrloading_right.setVisibility(View.VISIBLE);
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
			//String zhiprice = "0.01";
			String zhiprice =((float) desInfos.zhifubaoprice / 100) + "";
			nameValuePairs.add(new BasicNameValuePair("price", zhiprice));
			nameValuePairs.add(new BasicNameValuePair("title",
					desInfos.name));
			nameValuePairs
					.add(new BasicNameValuePair("des", desInfos.name));
			url = "http://"+ Constants.baseUrlLHL+"/f2f/f2fpay/qrpay_test.php";
			String json = HttpUtil.RequestGetData(url, nameValuePairs);
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
						iv_qr_right.setVisibility(View.VISIBLE);
						iv_qrloading_right.setVisibility(View.INVISIBLE);
						Log.e("wh", "支付宝的二维码生成了。。。。。");
						typIndex = 3;
						upZhifu(typIndex);
					} else {
						Toast.makeText(DesProAppActivity.this, "支付宝二维码出现异常!",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
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
	}
}
