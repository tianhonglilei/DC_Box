package com.example.zzq.adapter;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.zzq.bean.ConfigInfo;
import com.example.zzq.bean.SysData;
import com.zhang.box.R;
import com.zhang.box.constants.Constants;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.StringUtils;
import com.zhang.box.utils.ToastTools;
import com.zhang.easymoney.net.HttpUtil;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class proAdapter3 extends BaseAdapter {

	private Context context;
	private List<ConfigInfo> proList;
	private ProSucessNetTask mProSucessNetTask;
	private int pronum;

	// private ConfigInfo info;

	public proAdapter3(Context context, List<ConfigInfo> proList) {
		this.context = context;
		this.proList = proList;
	}

	@Override
	public int getCount() {
		int size = proList.size();
		if (size % 6 == 0) {
			return size / 6;
		} else {
			return (size / 6 + 1);
		}
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.pro_item2, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// TODO
		position = position * 6;
		if (position < proList.size()) {
			final ConfigInfo infos = proList.get(position);
			viewHolder.rlayout_one.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, infos.getLogo(),
					viewHolder.iv_pro_logo_one);
			viewHolder.iv_pro_name_one.setText(infos.getName());
			String pro_pro_num = "需要补仓" + infos.getMfinish()+ "瓶";
			viewHolder.iv_pro_num_one.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_one, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "货道" + infos.getHid();
			viewHolder.iv_pro_hid_one.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_one, iv_pro_hid, 2,
					iv_pro_hid.length());

			// 把info 与输入框进行绑定
			viewHolder.et_pro_num_one.setTag(infos);
			// 清除焦点
			viewHolder.et_pro_num_one.clearFocus();
			viewHolder.et_pro_num_one.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// 获得Edittext所在position里面的Bean，并设置数据
					ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_one
							.getTag();
					info.setHaveNum(s + "");
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

			// 大部分情况下，Adapter里面有if必须有else
			if (!TextUtils.isEmpty(infos.getHaveNum())) {
				viewHolder.et_pro_num_one.setText(infos.getHaveNum());
			} else {
				viewHolder.et_pro_num_one.setText("");
			}

			// TODO WHWH
			if (infos.getMfinish() > 0) {
				viewHolder.ib_pro_finish_one
						.setBackgroundResource(R.drawable.buhuobtn2);
				// 补仓
				final int pos = position;
				viewHolder.ib_pro_finish_one
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = infos.getHaveNum();
								Log.e("whwh", "获取的数据--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (infos.getMfinish())) {
										updataSucess(pos);
									} else {
										ToastTools.showShort(context,
												"最大只能补仓数为"+ infos.getMfinish()
														+ "瓶");
									}
								} else {
									ToastTools.showShort(context, "空值,请重新出入!");
								}
								notifyDataSetChanged();
							}
						});
			} else {
				viewHolder.ib_pro_finish_one
						.setBackgroundResource(R.drawable.buhuobtn);
			}

		} else {

			viewHolder.rlayout_one.setVisibility(View.INVISIBLE);
		}

		if ((position + 1) < proList.size()) {
			final ConfigInfo info = proList.get(position + 1);
			viewHolder.rlayout_two.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, info.getLogo(),
					viewHolder.iv_pro_logo_two);
			viewHolder.iv_pro_name_two.setText(info.getName());
			String pro_pro_num = "需要补仓" + (info.getMfinish())
					+ "瓶";
			viewHolder.iv_pro_num_two.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_two, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "货道" + info.getHid();
			viewHolder.iv_pro_hid_two.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_two, iv_pro_hid, 2,
					iv_pro_hid.length());
			// 把info 与输入框进行绑定
			viewHolder.et_pro_num_two.setTag(info);
			// 清除焦点
			viewHolder.et_pro_num_two.clearFocus();

			viewHolder.et_pro_num_two.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// 获得Edittext所在position里面的Bean，并设置数据
					ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_two
							.getTag();
					info.setHaveNum(s + "");
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});

			// 大部分情况下，Adapter里面有if必须有else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_two.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_two.setText("");
			}

			// TODO WHWHW
			if (info.getMfinish() > 0) {

				viewHolder.ib_pro_finish_two
						.setBackgroundResource(R.drawable.buhuobtn2);
				// 补仓
				final int pos = position;
				viewHolder.ib_pro_finish_two
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "获取的数据--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <=  info.getMfinish()) {
										updataSucess(pos + 1);
									} else {
										ToastTools.showShort(
												context,
												"最大只能补仓数为"+info.getMfinish()
														+ "瓶");
									}
								} else {
									ToastTools.showShort(context, "空值,请重新出入!");
								}
								notifyDataSetChanged();
							}
						});

			} else {

				viewHolder.ib_pro_finish_two
						.setBackgroundResource(R.drawable.buhuobtn);
			}

		} else {
			viewHolder.rlayout_two.setVisibility(View.INVISIBLE);
		}
		if ((position + 2) < proList.size()) {
			final ConfigInfo info = proList.get(position + 2);
			viewHolder.rlayout_three.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, info.getLogo(),
					viewHolder.iv_pro_logo_three);
			viewHolder.iv_pro_name_three.setText(info.getName());

			String pro_pro_num = "需要补仓" + (info.getMfinish())
					+ "瓶";
			viewHolder.iv_pro_num_three.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_three, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "货道" + info.getHid();
			viewHolder.iv_pro_hid_three.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_three, iv_pro_hid, 2,
					iv_pro_hid.length());

			// 把info 与输入框进行绑定
			viewHolder.et_pro_num_three.setTag(info);
			// 清除焦点
			viewHolder.et_pro_num_three.clearFocus();

			viewHolder.et_pro_num_three
					.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// 获得Edittext所在position里面的Bean，并设置数据
							ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_three
									.getTag();
							info.setHaveNum(s + "");
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});

			// 大部分情况下，Adapter里面有if必须有else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_three.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_three.setText("");
			}

			// TODO WHWH
			if ((info.getMfinish()) > 0) {
				viewHolder.ib_pro_finish_three
						.setBackgroundResource(R.drawable.buhuobtn2);
				// 补仓
				final int pos = position;
				viewHolder.ib_pro_finish_three
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "获取的数据--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (info.getMfinish())) {
										updataSucess(pos + 2);
									} else {
										ToastTools.showShort(
												context,
												"最大只能补仓数为"+  info.getMfinish()+ "瓶");
									}
								} else {
									ToastTools.showShort(context, "空值,请重新出入!");
								}
								notifyDataSetChanged();
							}
						});

			} else {
				viewHolder.ib_pro_finish_three
						.setBackgroundResource(R.drawable.buhuobtn);
			}

		} else {
			viewHolder.rlayout_three.setVisibility(View.INVISIBLE);
		}

		if ((position + 3) < proList.size()) {
			final ConfigInfo info = proList.get(position + 3);
			viewHolder.rlayout_four.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, info.getLogo(),
					viewHolder.iv_pro_logo_four);
			viewHolder.iv_pro_name_four.setText(info.getName());

			String pro_pro_num = "需要补仓" + (info.getMfinish())
					+ "瓶";
			viewHolder.iv_pro_num_four.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_four, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "货道" + info.getHid();
			viewHolder.iv_pro_hid_four.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_four, iv_pro_hid, 2,
					iv_pro_hid.length());

			// 把info 与输入框进行绑定
			viewHolder.et_pro_num_four.setTag(info);
			// 清除焦点
			viewHolder.et_pro_num_four.clearFocus();

			viewHolder.et_pro_num_four
					.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// 获得Edittext所在position里面的Bean，并设置数据
							ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_four
									.getTag();
							info.setHaveNum(s + "");
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});

			// 大部分情况下，Adapter里面有if必须有else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_four.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_four.setText("");
			}

			// TODO H
			if ((info.getMfinish()) > 0) {
				viewHolder.ib_pro_finish_four
						.setBackgroundResource(R.drawable.buhuobtn2);
				// 补仓
				final int pos = position;
				viewHolder.ib_pro_finish_four
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "获取的数据--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (info.getMfinish())) {
										updataSucess(pos + 3);
									} else {
										ToastTools.showShort(
												context,
												"最大只能补仓数为"+ info.getMfinish()
														+ "瓶");
									}
								} else {
									ToastTools.showShort(context, "空值,请重新出入!");
								}
								notifyDataSetChanged();
							}
						});
			} else {
				viewHolder.ib_pro_finish_four
						.setBackgroundResource(R.drawable.buhuobtn);
			}

		} else {
			viewHolder.rlayout_four.setVisibility(View.INVISIBLE);
		}
		if ((position + 4) < proList.size()) {
			final ConfigInfo info = proList.get(position + 4);
			viewHolder.rlayout_five.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, info.getLogo(),
					viewHolder.iv_pro_logo_five);
			viewHolder.iv_pro_name_five.setText(info.getName());

			String pro_pro_num = "需要补仓" + (info.getMfinish())
					+ "瓶";
			viewHolder.iv_pro_num_five.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_five, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "货道" + info.getHid();
			viewHolder.iv_pro_hid_five.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_five, iv_pro_hid, 2,
					iv_pro_hid.length());

			// 把info 与输入框进行绑定
			viewHolder.et_pro_num_five.setTag(info);
			// 清除焦点
			viewHolder.et_pro_num_five.clearFocus();

			viewHolder.et_pro_num_five
					.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// 获得Edittext所在position里面的Bean，并设置数据
							ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_five
									.getTag();
							info.setHaveNum(s + "");
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});

			// 大部分情况下，Adapter里面有if必须有else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_five.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_five.setText("");
			}

			if ((info.getMfinish()) > 0) {
				viewHolder.ib_pro_finish_five
						.setBackgroundResource(R.drawable.buhuobtn2);
				// 补仓
				final int pos = position;
				viewHolder.ib_pro_finish_five
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "获取的数据--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (info.getMfinish())) {
										updataSucess(pos + 4);
									} else {
										ToastTools.showShort(
												context,
												"最大只能补仓数为"
														+ (info.getMfinish())
														+ "瓶");
									}
								} else {
									ToastTools.showShort(context, "空值,请重新出入!");
								}
								notifyDataSetChanged();
							}
						});
			} else {
				viewHolder.ib_pro_finish_five
						.setBackgroundResource(R.drawable.buhuobtn);
			}

		} else {
			viewHolder.rlayout_five.setVisibility(View.INVISIBLE);
		}

		if ((position + 5) < proList.size()) {
			final ConfigInfo info = proList.get(position + 5);
			viewHolder.rlayout_six.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, info.getLogo(),
					viewHolder.iv_pro_logo_six);
			viewHolder.iv_pro_name_six.setText(info.getName());

			String pro_pro_num = "需要补仓" + (info.getMfinish())
					+ "瓶";
			viewHolder.iv_pro_num_six.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_six, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "货道" + info.getHid();
			viewHolder.iv_pro_hid_six.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_six, iv_pro_hid, 2,
					iv_pro_hid.length());

			// 把info 与输入框进行绑定
			viewHolder.et_pro_num_six.setTag(info);
			// 清除焦点
			viewHolder.et_pro_num_six.clearFocus();

			viewHolder.et_pro_num_six.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// 获得Edittext所在position里面的Bean，并设置数据
					ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_six
							.getTag();
					info.setHaveNum(s + "");
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

			// 大部分情况下，Adapter里面有if必须有else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_six.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_six.setText("");
			}
			if ((info.getMfinish()) > 0) {
				viewHolder.ib_pro_finish_six
						.setBackgroundResource(R.drawable.buhuobtn2);
				// 补仓
				final int pos = position;
				viewHolder.ib_pro_finish_six
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "获取的数据--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (info.getMfinish())) {
										updataSucess(pos + 5);
									} else {
										ToastTools.showShort(
												context,
												"最大只能补仓数为"
														+ ( info.getMfinish())
														+ "瓶");
									}
								} else {
									ToastTools.showShort(context, "空值,请重新出入!");
								}
								notifyDataSetChanged();
							}
						});
			} else {
				viewHolder.ib_pro_finish_six
						.setBackgroundResource(R.drawable.buhuobtn);
			}

		} else {
			viewHolder.rlayout_six.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	protected void updataSucess(int i) {
		mProSucessNetTask = new ProSucessNetTask(i);
		mProSucessNetTask.execute();
	}

	class ProSucessNetTask extends AsyncTask<Object, Integer, String> {
		private int i;

		private ProSucessNetTask(int i) {
			this.i = i;
		}

		protected String doInBackground(Object... params) {
			String url = "";
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));
			nameValuePairs.add(new BasicNameValuePair("proid", proList.get(i)
					.getProid() + ""));
			nameValuePairs.add(new BasicNameValuePair("hid", proList.get(i)
					.getHid() + ""));
			nameValuePairs.add(new BasicNameValuePair("hgid", proList.get(i)
					.getHgid() + ""));
			nameValuePairs.add(new BasicNameValuePair("buhuo", pronum + ""));
			url = "http://"+ Constants.baseUrlLHL+"/boxapp/?c=welcome&m=updatehuodao";
			String json = HttpUtil.RequestGetData(url, nameValuePairs);
			Log.e("whwh", "ProActivity==" + nameValuePairs.toString()
					+ "json===" + json);
			return json;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				ToastTools.showLong(context, "网络异常,请检测网络情况!");
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					int stutes = jsonObject.getInt("err");
					Log.e("whwh", "stutes=" + stutes);
					if (stutes == 0) {
						ToastTools.showShort(context, "商品补仓成功!");

					} else {
						ToastTools.showShort(context, "商品补仓失败,请重新点击!");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class ViewHolder {
		RelativeLayout rlayout_one;
		TextView iv_pro_name_one;// 商品名称
		ImageView iv_pro_logo_one;// 商品LOGO
		ImageButton ib_pro_finish_one;// 商品LOGO
		TextView iv_pro_num_one; // 商品需要补充的数量
		EditText et_pro_num_one;// 输入只补充的数量
		TextView iv_pro_hid_one;// 商品的货道

		RelativeLayout rlayout_two;
		TextView iv_pro_name_two;// 商品名称
		ImageView iv_pro_logo_two;// 商品LOGO
		ImageButton ib_pro_finish_two;// 商品LOGO
		TextView iv_pro_num_two; // 商品需要补充的数量
		EditText et_pro_num_two;// 输入只补充的数量
		TextView iv_pro_hid_two;// 商品的货道

		RelativeLayout rlayout_three;
		TextView iv_pro_name_three;// 商品名称
		ImageView iv_pro_logo_three;// 商品LOGO
		ImageButton ib_pro_finish_three;// 商品LOGO
		TextView iv_pro_num_three; // 商品需要补充的数量
		EditText et_pro_num_three;// 输入只补充的数量
		TextView iv_pro_hid_three;// 商品的货道

		RelativeLayout rlayout_four;
		TextView iv_pro_name_four;// 商品名称
		ImageView iv_pro_logo_four;// 商品LOGO
		ImageButton ib_pro_finish_four;// 商品LOGO
		TextView iv_pro_num_four; // 商品需要补充的数量
		EditText et_pro_num_four;// 输入只补充的数量
		TextView iv_pro_hid_four;// 商品的货道

		RelativeLayout rlayout_five;
		TextView iv_pro_name_five;// 商品名称
		ImageView iv_pro_logo_five;// 商品LOGO
		ImageButton ib_pro_finish_five;// 商品LOGO
		TextView iv_pro_num_five; // 商品需要补充的数量
		EditText et_pro_num_five;// 输入只补充的数量
		TextView iv_pro_hid_five;// 商品的货道

		RelativeLayout rlayout_six;
		TextView iv_pro_name_six;// 商品名称
		ImageView iv_pro_logo_six;// 商品LOGO
		ImageButton ib_pro_finish_six;// 商品LOGO
		TextView iv_pro_num_six; // 商品需要补充的数量
		EditText et_pro_num_six;// 输入只补充的数量
		TextView iv_pro_hid_six;// 商品的货道

		public ViewHolder(View convertView) {

			rlayout_one = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_one); // 单个布局
			iv_pro_name_one = (TextView) convertView
					.findViewById(R.id.tv_pro_name_one); // 商品名字
			iv_pro_logo_one = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_one); // 商品图片
			ib_pro_finish_one = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_one); // 商品是否补仓
			iv_pro_num_one = (TextView) convertView
					.findViewById(R.id.iv_pro_num_one); // 需要补仓的数
			et_pro_num_one = (EditText) convertView
					.findViewById(R.id.et_pro_num_one); // 现补货多少
			iv_pro_hid_one = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_one); // 货道

			rlayout_two = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_two); // 单个布局
			iv_pro_name_two = (TextView) convertView
					.findViewById(R.id.tv_pro_name_two); // 商品名字
			iv_pro_logo_two = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_two); // 商品图片
			ib_pro_finish_two = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_two); // 商品是否补仓
			iv_pro_num_two = (TextView) convertView
					.findViewById(R.id.iv_pro_num_two); // 需要补仓的数
			et_pro_num_two = (EditText) convertView
					.findViewById(R.id.et_pro_num_two); // 现补货多少
			iv_pro_hid_two = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_two); // 货道

			rlayout_three = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_three); // 单个布局
			iv_pro_name_three = (TextView) convertView
					.findViewById(R.id.tv_pro_name_three); // 商品名字
			iv_pro_logo_three = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_three); // 商品图片
			ib_pro_finish_three = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_three); // 商品是否补仓
			iv_pro_num_three = (TextView) convertView
					.findViewById(R.id.iv_pro_num_three); // 需要补仓的数
			et_pro_num_three = (EditText) convertView
					.findViewById(R.id.et_pro_num_three); // 现补货多少
			iv_pro_hid_three = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_three); // 货道

			rlayout_four = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_four); // 单个布局
			iv_pro_name_four = (TextView) convertView
					.findViewById(R.id.tv_pro_name_four); // 商品名字
			iv_pro_logo_four = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_four); // 商品图片
			ib_pro_finish_four = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_four); // 商品是否补仓
			iv_pro_num_four = (TextView) convertView
					.findViewById(R.id.iv_pro_num_four); // 需要补仓的数
			et_pro_num_four = (EditText) convertView
					.findViewById(R.id.et_pro_num_four); // 现补货多少
			iv_pro_hid_four = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_four); // 货道

			rlayout_five = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_five); // 单个布局
			iv_pro_name_five = (TextView) convertView
					.findViewById(R.id.tv_pro_name_five); // 商品名字
			iv_pro_logo_five = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_five); // 商品图片
			ib_pro_finish_five = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_five); // 商品是否补仓
			iv_pro_num_five = (TextView) convertView
					.findViewById(R.id.iv_pro_num_five); // 需要补仓的数
			et_pro_num_five = (EditText) convertView
					.findViewById(R.id.et_pro_num_five); // 现补货多少
			iv_pro_hid_five = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_five); // 货道

			rlayout_six = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_six); // 单个布局
			iv_pro_name_six = (TextView) convertView
					.findViewById(R.id.tv_pro_name_six); // 商品名字
			iv_pro_logo_six = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_six); // 商品图片
			ib_pro_finish_six = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_six); // 商品是否补仓
			iv_pro_num_six = (TextView) convertView
					.findViewById(R.id.iv_pro_num_six); // 需要补仓的数
			et_pro_num_six = (EditText) convertView
					.findViewById(R.id.et_pro_num_six); // 现补货多少
			iv_pro_hid_six = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_six); // 货道
		}
	}
}
