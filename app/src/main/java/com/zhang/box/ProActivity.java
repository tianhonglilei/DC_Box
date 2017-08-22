package com.zhang.box;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.zzq.bean.ConfigInfo;
import com.example.zzq.bean.SysData;
import com.zhang.box.constants.Constants;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * 补货页面
 * 
 * @author wang
 * 
 */
public class ProActivity extends Activity {

	private Context mContext;
	private ListView lv_pro_drinke;
	private ListView lv_pro_snake;
	private ProNetTask mProNetTask;
	private List<ConfigInfo> proListDrinke;
	private List<ConfigInfo> proListSnake;
	private com.example.zzq.adapter.proAdapter2 proAdapter2;
	private com.example.zzq.adapter.proAdapter3 proAdapter3;
	private ProgressDialog pd;
	private ImageButton ib_pro_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		StyleUtil.customStyle(this, R.layout.pro, "补仓商品");
		mContext = this;
		intView();
		intData();
		initListener();
	}

	/** 设置监听事件 */
	private void initListener() {
		ib_pro_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/** 初始化数据 */
	private void intData() {
		proListDrinke = new ArrayList<ConfigInfo>();
		proListSnake = new ArrayList<ConfigInfo>();
		upData(0);
	}

	/** 向服务器请求数据 */
	private void upData(int i) {
		pd.show();
		mProNetTask = new ProNetTask();
		mProNetTask.execute();
	}

	class ProNetTask extends AsyncTask<Object, Integer, String> {

		private ProNetTask() {
		}

		protected String doInBackground(Object... params) {
			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));
			url = Constants.ProActivityUrlLHL;
			String json = HttpUtil.RequestGetData(url, nameValuePairs);
			Log.e("whwh",
					"ProActivity: nameValuePairs==="
							+ nameValuePairs.toString() + "json==" + json);
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

							int proid = Integer.parseInt(listJson
									.getString("id"));
							int hid = Integer.parseInt(listJson
									.getString("hid"));
							int hgid = Integer.parseInt(listJson
									.getString("hgid"));
							String logo = listJson.getString("logo");
							String name = listJson.getString("name");
							int max = Integer.parseInt(listJson.getString("max"));
							int mfinish = Integer.parseInt(listJson.getString("mfinish"));
							info.setProid(proid);
							info.setHid(hid);
							info.setLogo(logo);
							info.setName(name);
							info.setMax(max);
							info.setMfinish(mfinish);
							info.setHgid(hgid);
							proListDrinke.add(info);
						}
					}

					JSONArray jsonObj2 = new JSONObject(result)
							.getJSONArray("shipin");
					if (jsonObj2.length() == 0) {
						ToastTools.showShort(mContext, "尚商品补仓记录!");
					} else {
						for (int i = 0; i < jsonObj2.length(); i++) {
							ConfigInfo info = new ConfigInfo();
							JSONObject listJson2 = jsonObj2.optJSONObject(i);

							int proid = Integer.parseInt(listJson2
									.getString("id"));
							int hid = Integer.parseInt(listJson2
									.getString("hid"));
							int hgid = Integer.parseInt(listJson2
									.getString("hgid"));
							String logo = listJson2.getString("logo");
							String name = listJson2.getString("name");
							int max = Integer.parseInt(listJson2.getString("max"));
							int mfinish = Integer.parseInt(listJson2.getString("mfinish"));
							info.setProid(proid);
							info.setHid(hid);
							info.setLogo(logo);
							info.setName(name);
							info.setMax(max);
							info.setMfinish(mfinish);
							info.setHgid(hgid);
							proListSnake.add(info);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// 数据添加到适配器里
				setListView();
			}
		}
	}

	/** 初始化控件 */
	private void intView() {
		lv_pro_drinke = (ListView) findViewById(R.id.lv_pro_drinke);
		lv_pro_snake = (ListView) findViewById(R.id.lv_pro_snake);
		ib_pro_return = (ImageButton) findViewById(R.id.ib_pro_return);
		pd = new ProgressDialog(this);
		pd.setMessage(this.getText(R.string.pd_updating));
		pd.setCancelable(true);
	}

	/** 数据添加到适配器里 */
	public void setListView() {
		proAdapter2 = new com.example.zzq.adapter.proAdapter2(mContext,
				proListDrinke);
		lv_pro_drinke.setAdapter(proAdapter2);
		lv_pro_drinke.setDividerHeight(0);
		lv_pro_drinke.setSelection(ListView.FOCUS_DOWN);
		lv_pro_drinke.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当停止滚动时
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // 滚动时
					HideKeyBoard();
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_FLING: // 手指抬起，但是屏幕还在滚动状态
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		proAdapter3 = new com.example.zzq.adapter.proAdapter3(mContext,
				proListSnake);
		lv_pro_snake.setAdapter(proAdapter3);
		lv_pro_snake.setDividerHeight(0);
		lv_pro_snake.setSelection(ListView.FOCUS_DOWN);
		lv_pro_snake.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当停止滚动时
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // 滚动时
					HideKeyBoard();
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_FLING: // 手指抬起，但是屏幕还在滚动状态
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	/** 隐藏虚拟键盘 */
	protected void HideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(ProActivity.this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
