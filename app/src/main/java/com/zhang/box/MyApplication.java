package com.zhang.box;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.avm.serialport_142.MainHandler;
import com.avm.serialport_142.service.CommService;
import com.avm.serialport_142.service.CommServiceThread;
import com.example.zzq.bean.SysData;
import com.zhang.box.catchexecpton.HttpParameters;
import com.zhang.box.catchexecpton.LogCollector;
import com.zhang.box.constants.Constants;
import com.zhang.box.utils.DeviceUtils;
import com.zhang.box.utils.FileUtils;
import com.zhang.box.utils.LogToFile;
import com.zhang.box.utils.SpUtil;
import com.zhang.box.utils.ToastTools;
import com.zhang.easymoney.net.HttpUtil;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 配置文件
 * 
 * @author wang
 * 
 */
public class MyApplication extends Application {

	private static final String LogTag = "MyApplication";

	private static MyApplication instance;
	private String secondPath;
	private NetCodeTask mNetCodeTask;
	private String code;
	//错误日志接口
	private static final String UPLOAD_URL = "http://"+ Constants.baseUrlLHL+"/box/bug.php";

	/**
	 * 获取一个Application的实例
	 * 
	 * @return
	 */
	public static synchronized MyApplication getInstance() {
		if (instance == null) {
			instance = new MyApplication();
		}
		return instance;
	}

	/**
	 * 设置一个BaseApplication的实例
	 * 
	 * @param app
	 */
	public static void setInstance(MyApplication app) {
		instance = app;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		//程序启动首次调用的
		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryClass();
		activityManager.getLargeMemoryClass();
		LogToFile.init(getApplicationContext());
		initData(this);// 初始化数据
	}

	/** 初始化数据 */
	private void initData(Context context) {
		String firstPath = Environment.getExternalStorageDirectory()
				+ File.separator + "Android" + File.separator;
		secondPath = firstPath + "data" + File.separator;
		String thirdPath = secondPath + "com.zhang.box" + File.separator;
		String forthPath = thirdPath + "set" + File.separator;

		File dirFirstPath = new File(firstPath);
		if (!dirFirstPath.exists()) { // 目录存在返回false
			dirFirstPath.mkdirs(); // 创建一个目录
		}
		File dirSecondPath = new File(secondPath);
		if (!dirSecondPath.exists()) {
			dirSecondPath.mkdirs();
		}
		File dirThirdPath = new File(thirdPath);
		if (!dirThirdPath.exists()) {
			dirThirdPath.mkdirs();
		}

		File dirForthPath = new File(forthPath);
		if (!dirForthPath.exists()) {
			dirForthPath.mkdirs();
		}

		FileUtils.assetsDataToSD(getApplicationContext(), forthPath
				+ "config.ini");
		//激活码
		code = SpUtil.getString(getApplicationContext(), "code", null);
		if (TextUtils.isEmpty(code)) {
			upDataCode(0);
		} else {
			initConfigInfo();
		}
	}

	/** 配置机器信息 */
	private void initConfigInfo() {
		int loadResult = MainHandler.load(this);
		if (loadResult == MainHandler.ERROR_NO_SDCARD) {
			ToastTools.showShort(this, "系统没有内存卡");
		} else if (loadResult == MainHandler.ERROR_EMPTY_DATA) {
			ToastTools.showShort(this, "串口信息没有配置或者读取失败");
		} else if (loadResult == MainHandler.ERROR_NET_NOT_AVAILABLE) {
			ToastTools.showShort(this, "系统没有连接网络");
		} else if (loadResult == MainHandler.LOAD_DATA_SUCCESS) {
			ToastTools.showShort(this, "加载成功");
		}

		new CommService() {

			@Override
			public void result(int res) {

				if (res == CommService.ERROR_SYSTEM_SERVICE) {
					ToastTools
							.showLong(getApplicationContext(), "数据配置或者网络调用错误");
				} else if (res == CommService.ERROR_SYSTEM_TIME) {
					ToastTools.showLong(getApplicationContext(), "系统时间不正确");
				} else if (res == CommService.ERROR_CODE_NO_EXIST) {
					ToastTools.showLong(getApplicationContext(), "激活码不存在");
				} else if (res == CommService.ERROR_SYSTEM_CODE) {
					ToastTools.showLong(getApplicationContext(), "激活码校验失败");
				} else if (res == CommService.ERROR_ACTIVATE_CHECK) {
					ToastTools.showLong(getApplicationContext(), "激活校验失败");
				} else if (res == CommService.ERROR_CODE_USED) {
					ToastTools.showLong(getApplicationContext(), "激活码已被使用");
					//测试
//					SysData.imei = DeviceUtils.getIMEI(getApplicationContext());
					Log.e(LogTag+"", "MyAppliction--->imei===" + SysData.imei);
				} else if (res == CommService.ERROR_OTHER) {
					ToastTools.showLong(getApplicationContext(), "其他错误");
				} else if (res == CommServiceThread.ERROR_IO_PROBLEM) {
					ToastTools.showLong(getApplicationContext(), "串口打开IO出错");
				} else if (res == CommServiceThread.ERROR_PERMISSION_REJECT) {
					ToastTools.showLong(getApplicationContext(), "没有打开串口的权限");
				} else if (res == CommServiceThread.ERROR_NOT_CONFIG) {
					ToastTools.showLong(getApplicationContext(),
							"系统中没有配置要打开的串口");
				} else if (res == CommServiceThread.ERROR_UNKNOWN) {
					ToastTools.showLong(getApplicationContext(), "串口打开时的未知错误");
				} else if (res == CommServiceThread.COMM_SERVICE_START) {
					ToastTools.showLong(getApplicationContext(), "激活启动成功");
					// !!!!!获取机器号
					SysData.imei = MainHandler.getMachNo();
//					SysData.imei = DeviceUtils.getIMEI(getApplicationContext());
					Log.e(LogTag+"", "MyAppliction--->imei===" + SysData.imei);
					HttpParameters params = new HttpParameters();
					params.add("imei", MainHandler.getMachNo());
					boolean isDebug = true;

					//获取debug日志
					LogCollector.setDebugMode(isDebug);
					LogCollector.init(getApplicationContext(), UPLOAD_URL,
							params);
					Log.e(LogTag+"", "MyAppliction--->imei=debug==" + SysData.imei);
				}
			}

		}.connect(this,
				SpUtil.getString(getApplicationContext(), "code", null), 1);
		Log.e("wh", "application---激活码"
						+ SpUtil.getString(getApplicationContext(), "code",
								null));

		// 判断是否在运行
		if (!DeviceUtils.isAppRunning(getApplicationContext(), "com.zhang.box")) {
			// 暂时不开放
			DeviceUtils.startAPP("com.zhang.box", getApplicationContext());
		}

	}

	private void upDataCode(int i) {
		mNetCodeTask = new NetCodeTask();
		mNetCodeTask.execute();
	}

	class NetCodeTask extends AsyncTask<Object, Integer, String> {

		private NetCodeTask() {
		}

		protected String doInBackground(Object... params) {
			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			url = "http://"+ Constants.baseUrlLHL+"/boxapp/?c=welcome&m=box_activecode";
//			String json = HttpUtil.RequestGetData(url, nameValuePairs);
			String json = "{'code':400000000806}";
			Log.i(LogTag+"_url",url+"");
			Log.i(LogTag+"_json",json+"");
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {

			if (result == null||result.equals("")) {
				ToastTools
						.showLong(getApplicationContext(), "网路连接失败,没有获取到激活码!");
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					code = jsonObject.getString("code");
					SpUtil.putString(getApplicationContext(), "code", code);
					initConfigInfo();
				} catch (JSONException e) {
					e.printStackTrace();
					ToastTools.showLong(getApplicationContext(), "连接成功,数据异常!");
				}
			}
		}


	}
}
