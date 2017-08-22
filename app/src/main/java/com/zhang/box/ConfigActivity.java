package com.zhang.box;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.avm.serialport_142.MainHandler;
import com.example.zzq.bean.SysData;
import com.zhang.box.constants.Constants;
import com.zhang.box.utils.ActivitySkipUtil;
import com.zhang.box.utils.DeviceUtils;
import com.zhang.box.utils.SpUtil;
import com.zhang.box.utils.StringUtils;
import com.zhang.box.utils.ToastTools;
import com.zhang.easymoney.net.HttpGetAndPostNet;
import com.zhang.easymoney.net.HttpUtil;
import com.zhang.easymoney.net.PosetnetMonClick;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ConfigActivity extends Activity implements OnClickListener {

	private Context mContext;
	private VersinNetTask mVersinNetTask;
	private Button cid;
	private Button ctime;
	private ImageButton ib_pro_activity;
	private ImageButton ib_degree_activity;
	private ImageButton ib_ceshi_activity;
	private ImageButton ib_version_activity;
	private ImageButton ib_sold_activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.config);
		mContext = this;
		// 获取屏幕长宽
		intView();// 初始化控件
		initData();// 数据源
		intListener();// 设置监听事件

	}

	/** 监听事件 */
	private void intListener() {
		ib_pro_activity.setOnClickListener(this);
		ib_degree_activity.setOnClickListener(this);
		ib_ceshi_activity.setOnClickListener(this);
		ib_version_activity.setOnClickListener(this);
		ib_sold_activity.setOnClickListener(this);
	}

	/** 数据源 */
	private void initData() {
//		SysData.imei = MainHandler.getMachNo();
//		SysData.imei = DeviceUtils.getIMEI(mContext);
		cid.setText("机器编号: " + SysData.imei);// 获取机器号
		ctime.setText("当前时间: " + StringUtils.timeStr());// 获取时间
	}

	/** 布局控件初始化 */
	private void intView() {
		cid = (Button) findViewById(R.id.cid);
		ctime = (Button) findViewById(R.id.ctime);
		ib_pro_activity = (ImageButton) findViewById(R.id.ib_pro_activity);
		ib_degree_activity = (ImageButton) findViewById(R.id.ib_degree_activity);
		ib_ceshi_activity = (ImageButton) findViewById(R.id.ib_ceshi_activity);
		ib_version_activity = (ImageButton) findViewById(R.id.ib_version_activity);
		ib_sold_activity = (ImageButton) findViewById(R.id.ib_sold_activity);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_pro_activity: // 补仓商品
			ActivitySkipUtil.skipAnotherActivity(ConfigActivity.this,
					ProActivity.class, false);
			break;
		case R.id.ib_degree_activity: // 温度设置
			ActivitySkipUtil.skipAnotherActivity(ConfigActivity.this,
					DegreeActivity.class, false);
			break;
		case R.id.ib_ceshi_activity: // 货道测试
			ActivitySkipUtil.skipAnotherActivity(ConfigActivity.this,
					HuodaoActivity.class, false);
			break;

		case R.id.ib_version_activity: // 版本更新
			upDataVersion(0);
			break;

		case R.id.ib_sold_activity: // 继续销售
			SpUtil.putBoolean(getApplicationContext(),
					LoadingActivity.IS_APP_FIRST_OPEN, false);
			boolean isFirstOpen = SpUtil.getBoolean(getApplicationContext(),
					LoadingActivity.IS_APP_FIRST_OPEN, true);
			if (isFirstOpen) {
				ActivitySkipUtil.skipAnotherActivity(ConfigActivity.this,
						GallaryActivity.class, true);
			} else {
				ActivitySkipUtil.skipAnotherActivity(ConfigActivity.this,
						LoadingActivity.class, true);
			}
			break;

		default:
			break;
		}

	}


	/** 获取box版本 */
	private int getVersionCode() {
		// 1,包管理者对象packageManager
		PackageManager pm = getPackageManager();
		// 2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
		try {
			PackageInfo packageInfo = pm.getPackageInfo("com.zhang.box",
					PackageManager.GET_CONFIGURATIONS);
			// 3,获取版本名称
			return packageInfo.versionCode;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	protected void upDataVersion(int i) {
		mVersinNetTask = new VersinNetTask();
		mVersinNetTask.execute();
	}

	class VersinNetTask extends AsyncTask<Object, Integer, String> {

		private VersinNetTask() {
		}

		protected String doInBackground(Object... params) {
			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));
			nameValuePairs.add(new BasicNameValuePair("ver", getVersionCode()
					+ ""));
			url = "http://"+Constants.baseUrlLHL+"/boxapp/?c=welcome&m=updateapk";
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
				ToastTools.showLong(mContext, "网络异常,请检测网络情况!");
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					int version = jsonObject.getInt("ver");
					String apkUrl = jsonObject.getString("apk");
					Log.e("whwh", "version=" + version);
					if (version > getVersionCode()) {
						SetPressdialog(apkUrl);
					} else {
						ToastTools.showShort(mContext, "已是最新版本,无需更新!");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void SetPressdialog(String apkUrl) {

		HttpGetAndPostNet.HttpGetdata(apkUrl, new PosetnetMonClick() {

			public void Postfinish(Boolean alog) {

				if (alog) {
					InstallApk();
				}
			}

			@Override
			public void HandHttpStartnum(long anum) {

			}

			@Override
			public void HandHttpEndnum(int anum) {

			}
		});
	}

	/** 安装程序 */
	protected void InstallApk() {
		String path = Environment.getExternalStorageDirectory()
				+ "/bailingooh/Box.apk";
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + path),
				"application/vnd.android.package-archive");
		startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
