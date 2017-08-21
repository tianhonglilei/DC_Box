package com.zhang.box;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.zzq.adapter.HuoDaoAdapter;
import com.example.zzq.adapter.HuoDaoAdapter2;
import com.example.zzq.bean.ConfigInfo;
import com.example.zzq.bean.SysData;
import com.zhang.box.constants.Constants;
import com.zhang.box.utils.ActivitySkipUtil;
import com.zhang.box.utils.StyleUtil;
import com.zhang.box.utils.ToastTools;
import com.zhang.easymoney.net.HttpUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * 货道测试
 * 
 * @author wang
 * 
 */
public class HuodaoActivity extends Activity {

	private Context mContext;
	private ListView lv_huodao_drink;
	private ListView lv_huodao_shanke;
	private HuoDaoNetTask mHuoDaoNetTask;
	private List<ConfigInfo> huoDaoListDrink;
	private List<ConfigInfo> huoDaoListSnake;
	private HuoDaoAdapter huoDaoAdapter;
	private HuoDaoAdapter2 huoDaoAdapter2;
	private ProgressDialog pd;
	private ImageButton ib_huodao_return;
	private Button btn_ceshi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		StyleUtil.customStyle(this, R.layout.huodao, "货道测试");
		mContext = this;
		intView();
		intData();
		initListener();
	}

	/** 初始化监听事件 */
	private void initListener() {
		ib_huodao_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btn_ceshi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivitySkipUtil.skipAnotherActivity(HuodaoActivity.this,
						WebViewCeshiActivity.class, false);
			}
		});
	}

	/** 数据源 */
	private void intData() {
		huoDaoListDrink = new ArrayList<ConfigInfo>();
		huoDaoListSnake = new ArrayList<ConfigInfo>();
		upDataHuodao(0);
	}

	private void upDataHuodao(int i) {
		pd.show();
		mHuoDaoNetTask = new HuoDaoNetTask();
		mHuoDaoNetTask.execute();
	}

	class HuoDaoNetTask extends AsyncTask<Object, Integer, String> {

		private HuoDaoNetTask() {
		}

		protected String doInBackground(Object... params) {
			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));
			url = Constants.ProActivityUrlLHL;
			String json = HttpUtil.RequestGetData(url, nameValuePairs);
			Log.e("whwh", "json==" + json);
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();
			if (result == null) {
				ToastTools.showLong(mContext, "网络异常,请检测网络情况!");
			} else {
				try {
					JSONArray jsonObj = new JSONObject(result)
							.getJSONArray("yinliao");
					if (jsonObj.length() == 0) {
						ToastTools.showShort(mContext, "尚商品补仓记录!");
					} else {
						for (int i = 0; i < jsonObj.length(); i++) {
							ConfigInfo info = new ConfigInfo();
							JSONObject listJson = jsonObj.optJSONObject(i);
							int hid = Integer.parseInt(listJson
									.getString("hid"));
							int hgid = Integer.parseInt(listJson
									.getString("hgid"));
							String logo = listJson.getString("logo");
							info.setHid(hid);
							info.setLogo(logo);
							info.setHgid(hgid);
							huoDaoListDrink.add(info);
						}
					}

					JSONArray jsonObj2 = new JSONObject(result)
							.getJSONArray("shipin");
					for (int i = 0; i < jsonObj2.length(); i++) {
						ConfigInfo info = new ConfigInfo();
						JSONObject listJson2 = jsonObj2.optJSONObject(i);
						int hid = Integer.parseInt(listJson2.getString("hid"));
						int hgid = Integer
								.parseInt(listJson2.getString("hgid"));
						String logo = listJson2.getString("logo");
						info.setHid(hid);
						info.setLogo(logo);
						info.setHgid(hgid);
						huoDaoListSnake.add(info);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// 数据添加到适配器里
				setListView();
			}
		}
	}

	/** 数据添加到适配器里 */
	public void setListView() {
		huoDaoAdapter = new HuoDaoAdapter(mContext, huoDaoListDrink);
		lv_huodao_drink.setAdapter(huoDaoAdapter);
		lv_huodao_drink.setDividerHeight(5);
		lv_huodao_drink.setSelection(ListView.FOCUS_DOWN);

		// -----------------------------------------------------------

		huoDaoAdapter2 = new HuoDaoAdapter2(mContext, huoDaoListSnake);
		lv_huodao_shanke.setAdapter(huoDaoAdapter2);
		lv_huodao_shanke.setDividerHeight(5);
		lv_huodao_shanke.setSelection(ListView.FOCUS_DOWN);
	}

	/** 初始化布局 */
	private void intView() {
		lv_huodao_drink = (ListView) findViewById(R.id.lv_huodao_drink);
		lv_huodao_shanke = (ListView) findViewById(R.id.lv_huodao_shanke);
		ib_huodao_return = (ImageButton) findViewById(R.id.ib_huodao_return);
		btn_ceshi = (Button) findViewById(R.id.btn_ceshi);
		pd = new ProgressDialog(this);
		pd.setMessage(this.getText(R.string.pd_updating));
		pd.setCancelable(true);
	}
}
