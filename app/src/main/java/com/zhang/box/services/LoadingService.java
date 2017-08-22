package com.zhang.box.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.avm.serialport_142.MainHandler;
import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.BooksDB;
import com.zhang.box.ImageCallback;
import com.zhang.box.NetImageLoader;
import com.zhang.box.constants.Constants;
import com.zhang.box.utils.DeviceUtils;
import com.zhang.box.utils.ToastTools;
import com.zhang.easymoney.net.HttpGetAndPostNet;
import com.zhang.easymoney.net.HttpUtil;
import com.zhang.easymoney.net.PosetnetMonClick;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.ProgressBar;

/**
 * 定时向后台请求信息的后台服务
 */
public class LoadingService extends Service {
	public static final int SUCCESS = 100;// 成功
	private static final long CHECK_PERIOD_TWO = 1000 * 60 * 3;// 30分钟监测一次
	private Timer mTimer;
	private BooksDB mBooksDB;
	private List<String> proList = new ArrayList<String>();
	private int index;
	private Context context;
	private NetImageLoader mLogoImage;
	private NetTask mNetTask;
	private FinishNetTask mFinishNetTask;
	private boolean isHave;
	private ProgressBar mProgressBar;
	private Dialog mDownloadDialog;
	private String versionCode;
	public static final String ACTION_REQUEST_SHUTDOWN = "android.intent.action.ACTION_REQUEST_SHUTDOWN";
	public static final String ACTION_REBOOT = "android.intent.action.REBOOT";

	public LoadingService() {
		super();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * 启动定时器进行定时检查数据
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		this.context = SysData.loadmain;
		mLogoImage = NetImageLoader.GetObject(context);
		mTimer = new Timer();
		mTimer.schedule(new CheckTask(), CHECK_PERIOD_TWO, CHECK_PERIOD_TWO);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 服务销毁时取消定时器
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mTimer.cancel();
	}

	private class CheckTask extends TimerTask {
		@Override
		public void run() {

			// mLogoImage = NetImageLoader.GetObject(context);
			/** 获取当前机器的版本号 */
			PackageInfo pinfo;
			try {
				pinfo = getPackageManager().getPackageInfo("com.zhang.box",
						PackageManager.GET_CONFIGURATIONS);
				versionCode = String.valueOf(pinfo.versionCode);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			// // 判断是否在运行
			if (!DeviceUtils.isAppRunning(getApplicationContext(),
					"com.zhang.box")) {
				// 暂时不开放
				DeviceUtils.startAPP("com.zhang.box", getApplicationContext());
			}
			upData(0);
			// Calendar cal = Calendar.getInstance();// 当前日期
			// int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
			// int minute = cal.get(Calendar.MINUTE);// 获取分钟
			// int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
			// final int start = 3 * 60;
			// final int end = 3 * 60 + 3;
			// if (minuteOfDay >= start && minuteOfDay <= end) { //
			// 凌晨3点到凌晨3点31分钟
			// // 自动重启
			// // TODO WH 重新启动程序
			// restartApplication();
			// }

			//reBootMachine();
		}
	}

	public void upData(int i) {
		mNetTask = new NetTask();
		mNetTask.execute();
	}

	/** 关机重新启动机器 */
	public void reBootMachine() {
		Intent intent2 = new Intent(Intent.ACTION_REBOOT);
		intent2.putExtra("nowait", 1);
		intent2.putExtra("interval", 1);
		intent2.putExtra("window", 0);
		sendBroadcast(intent2);
	}

	class NetTask extends AsyncTask<Object, Integer, String> {

		private int update;

		private NetTask() {
		}

		protected String doInBackground(Object... params) {

			// String url =
			// "http://123.57.44.205/box/box_heart.php?imei=11111";// +
			// String json = HttpUtil.RequestGetData2(url); //
			// SharedUtil.getUserKey(mContext));
			// return json;

			String url = "";
			// SysData.imei = DeviceUtils.getIMEI(context);
			// SysData.sim = DeviceUtils.getSim(context);
			// String model = android.os.Build.MODEL;
			// String sdk = android.os.Build.VERSION.RELEASE;

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					10);
			nameValuePairs.add(new BasicNameValuePair("app", SysData.appName));
			nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));
			nameValuePairs
					.add(new BasicNameValuePair("click", UserInfo.upname));
			nameValuePairs.add(new BasicNameValuePair("ver", versionCode));
			// 根据机器内容是否第一次 1全部下发 0更新
			String path = Environment.getExternalStorageDirectory().toString()
					+ "/boxcontent/";
			File destDir = new File(path);
			// TODO WH
			if (destDir.exists()) {
				nameValuePairs.add(new BasicNameValuePair("updata", 0 + ""));
			} else {
				nameValuePairs.add(new BasicNameValuePair("updata", 1 + ""));
			}
			/** 检测是否开门 */
			boolean isOpen = MainHandler.isDoorOpen();
			if (isOpen) {
				nameValuePairs.add(new BasicNameValuePair("door", 1 + ""));
			} else {
				nameValuePairs.add(new BasicNameValuePair("door", 0 + ""));
			}

			/** 检测机器所有的货道和货道的状态 */
			String str = new String();
			for (int i = 1; i < 22; i++) {
				String huodaoInfo = MainHandler.getGoodsInfo(11, i);
				String subHuodaoInfo = huodaoInfo.substring(0, 1);
				str += (i + "|" + subHuodaoInfo + "|");
			}
			nameValuePairs.add(new BasicNameValuePair("mhd", str));

			String jia = SysData.imei + "198217";
			nameValuePairs
					.add(new BasicNameValuePair("jiami", SysData.md5(jia)));

			url = Constants.LoadActivityUrlLHL;
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
					update = 0;
					mBooksDB = new BooksDB(getApplicationContext());
					JSONArray jsonObj = new JSONObject(result)
							.getJSONArray("pro");
					for (int i = 0; i < jsonObj.length(); i++) {
						UserInfo info = new UserInfo();
						JSONObject tempJson = jsonObj.optJSONObject(i);

						info.id = Integer.parseInt(tempJson.getString("id"));
						info.logo = tempJson.getString("logo");
						info.name = tempJson.getString("name");
						info.des = tempJson.getString("des");
						info.price = Integer.parseInt(tempJson
								.getString("price"));
						info.zhifubaoprice = Integer.parseInt(tempJson
								.getString("zhifubao"));
						info.weixinprice = Integer.parseInt(tempJson
								.getString("weixin"));
						info.probig = tempJson.getString("big");
						info.prohuogui = tempJson.getString("hgid");
						info.logogray = tempJson.getString("logogray");
						info.status = Integer.parseInt(tempJson
								.getString("status"));
						info.hdid = Integer
								.parseInt(tempJson.getString("hdid"));
						info.promainshow = Integer.parseInt(tempJson
								.getString("mainshow"));

						if (i == 0) {
							if (UserInfo.proAlllist.size() > 0) {
								UserInfo.proAlllist.clear();
							}

							if (UserInfo.proMainlist.size() > 0) {
								UserInfo.proMainlist.clear();
							}
							if (UserInfo.proSnakelist.size() > 0) {
								UserInfo.proSnakelist.clear();
							}
							if (UserInfo.proDrinklist.size() > 0) {
								UserInfo.proDrinklist.clear();
							}
							mBooksDB.deleteTableInfos("prduct");
						}
						mBooksDB.insertP(info);
						proList.add(tempJson.getString("logo"));
						proList.add(tempJson.getString("big"));
						proList.add(tempJson.getString("logogray"));
						update = 1;// 更新
					}

					JSONArray jsonObjadv = new JSONObject(result)
							.getJSONArray("adv");
					for (int i = 0; i < jsonObjadv.length(); i++) {
						UserInfo info = new UserInfo();
						JSONObject tempJson = jsonObjadv.optJSONObject(i);

						info.id = Integer.parseInt(tempJson.getString("id"));
						info.advimg = tempJson.getString("img");
						info.video = tempJson.getString("video");
						info.type = tempJson.getString("type");
						info.status = Integer.parseInt(tempJson
								.getString("status"));
						if (i == 0) {
							if (UserInfo.advVideo.size() > 0) {
								UserInfo.advVideo.clear();
							}
							mBooksDB.deleteTableInfos("adv");
						}
						mBooksDB.insertAdv(info);
						proList.add(tempJson.getString("img"));
						proList.add(tempJson.getString("video"));
						update = 1;
					}

					JSONArray jsonObjapp = new JSONObject(result)
							.getJSONArray("app");
					for (int i = 0; i < jsonObjapp.length(); i++) {
						UserInfo info = new UserInfo();
						JSONObject tempJson = jsonObjapp.optJSONObject(i);

						info.appid = Integer.parseInt(tempJson.getString("id"));
						info.applogo = tempJson.getString("logo");
						info.appname = tempJson.getString("name");
						info.appprice = Integer.parseInt(tempJson
								.getString("price"));
						info.duan = tempJson.getString("duan");
						info.mainshow = Integer.parseInt(tempJson
								.getString("mainshow"));

						info.apkurl = tempJson.getString("apkurl");
						info.mainimg = tempJson.getString("mainimg");
						info.sys = tempJson.getString("sys");
						info.appstatus = Integer.parseInt(tempJson
								.getString("status"));

						info.apkBigImg = tempJson.getString("big");
						info.apptype = tempJson.getString("type");
						info.buyapp = tempJson.getString("buyapp");
						UserInfo.desType = info.apptype;
						if (i == 0) {
							if (UserInfo.appMainshowOne.size() > 0) {
								UserInfo.appMainshowOne.clear();
							}
							if (UserInfo.appMainshow.size() > 0) {
								UserInfo.appMainshow.clear();
							}
							if (UserInfo.jpAllshow.size() > 0) {
								UserInfo.jpAllshow.clear();
							}
							if (UserInfo.searchApp.size() > 0) {
								UserInfo.searchApp.clear();
							}
							mBooksDB.deleteTableInfos("apps");
						}
						mBooksDB.insertApp(info);

						proList.add(tempJson.getString("logo"));
						proList.add(tempJson.getString("big"));
						proList.add(tempJson.getString("mainimg"));
						update = 1;
					}

					// 营养 proid bai hundrend xiang
					JSONArray jsonObjinfo = new JSONObject(result)
							.getJSONArray("productinfo");// .getJSONObject("nResult");
					for (int i = 0; i < jsonObjinfo.length(); i++) {
						UserInfo info = new UserInfo();
						JSONObject tempJson = jsonObjinfo.optJSONObject(i);

						info.proid = Integer.parseInt(tempJson
								.getString("proid"));
						info.bai = tempJson.getString("bai");
						info.hundrend = tempJson.getString("hundrend");
						info.xiang = tempJson.getString("xiang");
						if (i == 0) {
							if (UserInfo.proInfos.size() > 0) {
								UserInfo.proInfos.clear();
							}
							mBooksDB.deleteTableInfos("proinfo");
						}
						mBooksDB.insertInfos(info);
						update = 1;

					}
					// 版本安装 context int serverVersion =
					// Integer.parseInt(mVersion_code);
					JSONArray jsonObjdown = new JSONObject(result)
							.getJSONArray("down");
					for (int i = 0; i < jsonObjdown.length(); i++) {

						UserInfo info = new UserInfo();
						JSONObject tempJson = jsonObjdown.optJSONObject(i);

						int serverVersion = info.downVersionCode = Integer
								.parseInt(tempJson.getString("ver"));
						String downurl = info.downApkUrl = tempJson
								.getString("apk");

						// 如果服务器的版本大于机器现有的版本则自动更新
						if (serverVersion > getVersionCode()) {
							SetPressdialog(downurl);
						}
					}

					// UserInfo.jpAllshow.clear();
					// mBooksDB.ReadAppJingPing();
					// upDataFinish(0);
					if (update == 1) {
						upDataFinish(0);
					}
					startAutoScroll();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int getVersionCode() {
		// 1,包管理者对象packageManager
		PackageManager pm = getPackageManager();
		// 2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			// 3,获取版本名称
			return packageInfo.versionCode;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void SetPressdialog(String downurl) {
		// AlertDialog.Builder builder = new Builder(context);
		// builder.setTitle("正在更新中...");
		// View view = LayoutInflater.from(context).inflate(
		// R.layout.dialog_progress, null);
		// mProgressBar = (ProgressBar) view.findViewById(R.id.id_progress);
		// builder.setView(view);
		//
		// mDownloadDialog = builder.create();
		// mDownloadDialog.show();

		HttpGetAndPostNet.HttpGetdata(downurl, new PosetnetMonClick() {

			public void Postfinish(Boolean alog) {

				// if (mDownloadDialog != null) {
				// mDownloadDialog.dismiss();
				// mDownloadDialog = null;
				// }
				if (alog) {
					InstallApk();
				}
			}

			@Override
			public void HandHttpStartnum(long anum) {

			}

			@Override
			public void HandHttpEndnum(int anum) {
				// // mProgressBar.setProgress(mProgress);
				// if (mDownloadDialog != null) {
				// mProgressBar.setProgress(anum);
				// }
			}
		});
	}

	/** 下载完成后发送的信息 */
	protected void upDataFinish(int i) {
		mFinishNetTask = new FinishNetTask();
		mFinishNetTask.execute();
	}

	class FinishNetTask extends AsyncTask<Object, Integer, String> {

		private FinishNetTask() {
		}

		protected String doInBackground(Object... params) {

			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					10);
			nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));
			url = Constants.LoadingServiceUrlLHL;
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
				ToastTools.showShort(getApplicationContext(), "更新完成!");
				restartApplication();
			}
		}
	}

	/** 重新启动程序 */
	public void restartApplication() {
		final Intent intent = getPackageManager().getLaunchIntentForPackage(
				getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void InstallApk() {
		String path = Environment.getExternalStorageDirectory()
				+ "/bailingooh/Box.apk";
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + path),
				"application/vnd.android.package-archive");
		startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public void read() {

		// TODO 清除点击商品和应用程序名字数据库
		// UserInfo.uploadName.clear();

		UserInfo.isLoad = 0;
		// BooksDB mBooksDB = new BooksDB(getApplicationContext());
		// mBooksDB.ReadProAllById();

		UserInfo.proMainlist.clear();
		mBooksDB.ReadProMain();

		UserInfo.proAlllist.clear();
		mBooksDB.ReadProAll(); // 商品所有
		if (UserInfo.proDrinklist.size() > 0) {
			UserInfo.proDrinklist.clear();
		}
		mBooksDB.ReadDrinkAll();

		if (UserInfo.proSnakelist.size() > 0) {
			UserInfo.proSnakelist.clear();
		}
		mBooksDB.ReadSnakeAll();

		UserInfo.advVideo.clear();
		mBooksDB.ReadAdvVideo();

		// mBooksDB.ReadUpload(); // 读取所有点击应用和商品的名字

		UserInfo.appMainshowOne.clear();
		mBooksDB.ReadAppByMainshowOne();// 应用3张图

		UserInfo.jpAllshow.clear();
		mBooksDB.ReadAppJingPing();

		// TODO
		UserInfo.appMainshow.clear();
		mBooksDB.ReadAllApp();
	}

	/** 加载数据 */
	public void downpro(int andex) {

		if (andex >= proList.size()) {
			read();
		} else {
			String url = proList.get(andex);
			UserInfo.isLoad = 1;
			isHave = Getphontnames(url);
			if (!isHave) {
				if (mLogoImage.loadNetDrawable(url, new KImageCallback())) {
					index++;
					if (index < proList.size()) {
						downpro(index);
					} else {
						read();
					}
				}
			} else {
				index++;
				downpro(index);
			}
		}
	}

	/** 不存在走 上面的方法 */
	public Boolean Getphontnames(String url) {

		int pos = url.lastIndexOf("/");
		String filename = url.substring(pos + 1);
		String filepath = Environment.getExternalStorageDirectory().toString()
				+ "/boxcontent/";
		String path = filepath + filename;
		File file1 = new File(path);

		if (file1.exists()) {
			return true;
		} else {
			return false;
		}
	}

	private void startAutoScroll() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Message msg = mHandler.obtainMessage(0, 0, 0);
				mHandler.sendMessage(msg);
			}

		}.start();

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			index = 0;
			downpro(index);
		}
	};

	class KImageCallback implements ImageCallback {

		public KImageCallback() {
		}

		@Override
		public void imageLoaded(Drawable imageDrawable, String imageUrl) {

			index++;
			if (index < proList.size()) {
				downpro(index);
			} else {
				read();
			}
		}
	};
}
