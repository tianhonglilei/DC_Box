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
			String pro_pro_num = "��Ҫ����" + infos.getMfinish()+ "ƿ";
			viewHolder.iv_pro_num_one.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_one, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "����" + infos.getHid();
			viewHolder.iv_pro_hid_one.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_one, iv_pro_hid, 2,
					iv_pro_hid.length());

			// ��info ���������а�
			viewHolder.et_pro_num_one.setTag(infos);
			// �������
			viewHolder.et_pro_num_one.clearFocus();
			viewHolder.et_pro_num_one.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// ���Edittext����position�����Bean������������
					ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_one
							.getTag();
					info.setHaveNum(s + "");
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

			// �󲿷�����£�Adapter������if������else
			if (!TextUtils.isEmpty(infos.getHaveNum())) {
				viewHolder.et_pro_num_one.setText(infos.getHaveNum());
			} else {
				viewHolder.et_pro_num_one.setText("");
			}

			// TODO WHWH
			if (infos.getMfinish() > 0) {
				viewHolder.ib_pro_finish_one
						.setBackgroundResource(R.drawable.buhuobtn2);
				// ����
				final int pos = position;
				viewHolder.ib_pro_finish_one
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = infos.getHaveNum();
								Log.e("whwh", "��ȡ������--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (infos.getMfinish())) {
										updataSucess(pos);
									} else {
										ToastTools.showShort(context,
												"���ֻ�ܲ�����Ϊ"+ infos.getMfinish()
														+ "ƿ");
									}
								} else {
									ToastTools.showShort(context, "��ֵ,�����³���!");
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
			String pro_pro_num = "��Ҫ����" + (info.getMfinish())
					+ "ƿ";
			viewHolder.iv_pro_num_two.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_two, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "����" + info.getHid();
			viewHolder.iv_pro_hid_two.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_two, iv_pro_hid, 2,
					iv_pro_hid.length());
			// ��info ���������а�
			viewHolder.et_pro_num_two.setTag(info);
			// �������
			viewHolder.et_pro_num_two.clearFocus();

			viewHolder.et_pro_num_two.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// ���Edittext����position�����Bean������������
					ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_two
							.getTag();
					info.setHaveNum(s + "");
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});

			// �󲿷�����£�Adapter������if������else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_two.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_two.setText("");
			}

			// TODO WHWHW
			if (info.getMfinish() > 0) {

				viewHolder.ib_pro_finish_two
						.setBackgroundResource(R.drawable.buhuobtn2);
				// ����
				final int pos = position;
				viewHolder.ib_pro_finish_two
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "��ȡ������--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <=  info.getMfinish()) {
										updataSucess(pos + 1);
									} else {
										ToastTools.showShort(
												context,
												"���ֻ�ܲ�����Ϊ"+info.getMfinish()
														+ "ƿ");
									}
								} else {
									ToastTools.showShort(context, "��ֵ,�����³���!");
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

			String pro_pro_num = "��Ҫ����" + (info.getMfinish())
					+ "ƿ";
			viewHolder.iv_pro_num_three.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_three, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "����" + info.getHid();
			viewHolder.iv_pro_hid_three.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_three, iv_pro_hid, 2,
					iv_pro_hid.length());

			// ��info ���������а�
			viewHolder.et_pro_num_three.setTag(info);
			// �������
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
							// ���Edittext����position�����Bean������������
							ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_three
									.getTag();
							info.setHaveNum(s + "");
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});

			// �󲿷�����£�Adapter������if������else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_three.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_three.setText("");
			}

			// TODO WHWH
			if ((info.getMfinish()) > 0) {
				viewHolder.ib_pro_finish_three
						.setBackgroundResource(R.drawable.buhuobtn2);
				// ����
				final int pos = position;
				viewHolder.ib_pro_finish_three
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "��ȡ������--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (info.getMfinish())) {
										updataSucess(pos + 2);
									} else {
										ToastTools.showShort(
												context,
												"���ֻ�ܲ�����Ϊ"+  info.getMfinish()+ "ƿ");
									}
								} else {
									ToastTools.showShort(context, "��ֵ,�����³���!");
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

			String pro_pro_num = "��Ҫ����" + (info.getMfinish())
					+ "ƿ";
			viewHolder.iv_pro_num_four.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_four, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "����" + info.getHid();
			viewHolder.iv_pro_hid_four.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_four, iv_pro_hid, 2,
					iv_pro_hid.length());

			// ��info ���������а�
			viewHolder.et_pro_num_four.setTag(info);
			// �������
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
							// ���Edittext����position�����Bean������������
							ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_four
									.getTag();
							info.setHaveNum(s + "");
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});

			// �󲿷�����£�Adapter������if������else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_four.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_four.setText("");
			}

			// TODO H
			if ((info.getMfinish()) > 0) {
				viewHolder.ib_pro_finish_four
						.setBackgroundResource(R.drawable.buhuobtn2);
				// ����
				final int pos = position;
				viewHolder.ib_pro_finish_four
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "��ȡ������--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (info.getMfinish())) {
										updataSucess(pos + 3);
									} else {
										ToastTools.showShort(
												context,
												"���ֻ�ܲ�����Ϊ"+ info.getMfinish()
														+ "ƿ");
									}
								} else {
									ToastTools.showShort(context, "��ֵ,�����³���!");
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

			String pro_pro_num = "��Ҫ����" + (info.getMfinish())
					+ "ƿ";
			viewHolder.iv_pro_num_five.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_five, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "����" + info.getHid();
			viewHolder.iv_pro_hid_five.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_five, iv_pro_hid, 2,
					iv_pro_hid.length());

			// ��info ���������а�
			viewHolder.et_pro_num_five.setTag(info);
			// �������
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
							// ���Edittext����position�����Bean������������
							ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_five
									.getTag();
							info.setHaveNum(s + "");
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});

			// �󲿷�����£�Adapter������if������else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_five.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_five.setText("");
			}

			if ((info.getMfinish()) > 0) {
				viewHolder.ib_pro_finish_five
						.setBackgroundResource(R.drawable.buhuobtn2);
				// ����
				final int pos = position;
				viewHolder.ib_pro_finish_five
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "��ȡ������--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (info.getMfinish())) {
										updataSucess(pos + 4);
									} else {
										ToastTools.showShort(
												context,
												"���ֻ�ܲ�����Ϊ"
														+ (info.getMfinish())
														+ "ƿ");
									}
								} else {
									ToastTools.showShort(context, "��ֵ,�����³���!");
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

			String pro_pro_num = "��Ҫ����" + (info.getMfinish())
					+ "ƿ";
			viewHolder.iv_pro_num_six.setText(pro_pro_num);
			StringUtils.setFont(viewHolder.iv_pro_num_six, pro_pro_num, 4,
					pro_pro_num.length() - 1);

			String iv_pro_hid = "����" + info.getHid();
			viewHolder.iv_pro_hid_six.setText(iv_pro_hid);
			StringUtils.setFont(viewHolder.iv_pro_hid_six, iv_pro_hid, 2,
					iv_pro_hid.length());

			// ��info ���������а�
			viewHolder.et_pro_num_six.setTag(info);
			// �������
			viewHolder.et_pro_num_six.clearFocus();

			viewHolder.et_pro_num_six.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// ���Edittext����position�����Bean������������
					ConfigInfo info = (ConfigInfo) viewHolder.et_pro_num_six
							.getTag();
					info.setHaveNum(s + "");
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

			// �󲿷�����£�Adapter������if������else
			if (!TextUtils.isEmpty(info.getHaveNum())) {
				viewHolder.et_pro_num_six.setText(info.getHaveNum());
			} else {
				viewHolder.et_pro_num_six.setText("");
			}
			if ((info.getMfinish()) > 0) {
				viewHolder.ib_pro_finish_six
						.setBackgroundResource(R.drawable.buhuobtn2);
				// ����
				final int pos = position;
				viewHolder.ib_pro_finish_six
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pro_num_info = info.getHaveNum();
								Log.e("whwh", "��ȡ������--->pro_num_info==="
										+ pro_num_info);
								if (!pro_num_info.isEmpty()) {
									pronum = Integer.parseInt(pro_num_info);
									Log.e("whwh", "pro_num==" + pronum);
									if (pronum <= (info.getMfinish())) {
										updataSucess(pos + 5);
									} else {
										ToastTools.showShort(
												context,
												"���ֻ�ܲ�����Ϊ"
														+ ( info.getMfinish())
														+ "ƿ");
									}
								} else {
									ToastTools.showShort(context, "��ֵ,�����³���!");
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
				ToastTools.showLong(context, "�����쳣,�����������!");
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					int stutes = jsonObject.getInt("err");
					Log.e("whwh", "stutes=" + stutes);
					if (stutes == 0) {
						ToastTools.showShort(context, "��Ʒ���ֳɹ�!");

					} else {
						ToastTools.showShort(context, "��Ʒ����ʧ��,�����µ��!");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class ViewHolder {
		RelativeLayout rlayout_one;
		TextView iv_pro_name_one;// ��Ʒ����
		ImageView iv_pro_logo_one;// ��ƷLOGO
		ImageButton ib_pro_finish_one;// ��ƷLOGO
		TextView iv_pro_num_one; // ��Ʒ��Ҫ���������
		EditText et_pro_num_one;// ����ֻ���������
		TextView iv_pro_hid_one;// ��Ʒ�Ļ���

		RelativeLayout rlayout_two;
		TextView iv_pro_name_two;// ��Ʒ����
		ImageView iv_pro_logo_two;// ��ƷLOGO
		ImageButton ib_pro_finish_two;// ��ƷLOGO
		TextView iv_pro_num_two; // ��Ʒ��Ҫ���������
		EditText et_pro_num_two;// ����ֻ���������
		TextView iv_pro_hid_two;// ��Ʒ�Ļ���

		RelativeLayout rlayout_three;
		TextView iv_pro_name_three;// ��Ʒ����
		ImageView iv_pro_logo_three;// ��ƷLOGO
		ImageButton ib_pro_finish_three;// ��ƷLOGO
		TextView iv_pro_num_three; // ��Ʒ��Ҫ���������
		EditText et_pro_num_three;// ����ֻ���������
		TextView iv_pro_hid_three;// ��Ʒ�Ļ���

		RelativeLayout rlayout_four;
		TextView iv_pro_name_four;// ��Ʒ����
		ImageView iv_pro_logo_four;// ��ƷLOGO
		ImageButton ib_pro_finish_four;// ��ƷLOGO
		TextView iv_pro_num_four; // ��Ʒ��Ҫ���������
		EditText et_pro_num_four;// ����ֻ���������
		TextView iv_pro_hid_four;// ��Ʒ�Ļ���

		RelativeLayout rlayout_five;
		TextView iv_pro_name_five;// ��Ʒ����
		ImageView iv_pro_logo_five;// ��ƷLOGO
		ImageButton ib_pro_finish_five;// ��ƷLOGO
		TextView iv_pro_num_five; // ��Ʒ��Ҫ���������
		EditText et_pro_num_five;// ����ֻ���������
		TextView iv_pro_hid_five;// ��Ʒ�Ļ���

		RelativeLayout rlayout_six;
		TextView iv_pro_name_six;// ��Ʒ����
		ImageView iv_pro_logo_six;// ��ƷLOGO
		ImageButton ib_pro_finish_six;// ��ƷLOGO
		TextView iv_pro_num_six; // ��Ʒ��Ҫ���������
		EditText et_pro_num_six;// ����ֻ���������
		TextView iv_pro_hid_six;// ��Ʒ�Ļ���

		public ViewHolder(View convertView) {

			rlayout_one = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_one); // ��������
			iv_pro_name_one = (TextView) convertView
					.findViewById(R.id.tv_pro_name_one); // ��Ʒ����
			iv_pro_logo_one = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_one); // ��ƷͼƬ
			ib_pro_finish_one = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_one); // ��Ʒ�Ƿ񲹲�
			iv_pro_num_one = (TextView) convertView
					.findViewById(R.id.iv_pro_num_one); // ��Ҫ���ֵ���
			et_pro_num_one = (EditText) convertView
					.findViewById(R.id.et_pro_num_one); // �ֲ�������
			iv_pro_hid_one = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_one); // ����

			rlayout_two = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_two); // ��������
			iv_pro_name_two = (TextView) convertView
					.findViewById(R.id.tv_pro_name_two); // ��Ʒ����
			iv_pro_logo_two = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_two); // ��ƷͼƬ
			ib_pro_finish_two = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_two); // ��Ʒ�Ƿ񲹲�
			iv_pro_num_two = (TextView) convertView
					.findViewById(R.id.iv_pro_num_two); // ��Ҫ���ֵ���
			et_pro_num_two = (EditText) convertView
					.findViewById(R.id.et_pro_num_two); // �ֲ�������
			iv_pro_hid_two = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_two); // ����

			rlayout_three = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_three); // ��������
			iv_pro_name_three = (TextView) convertView
					.findViewById(R.id.tv_pro_name_three); // ��Ʒ����
			iv_pro_logo_three = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_three); // ��ƷͼƬ
			ib_pro_finish_three = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_three); // ��Ʒ�Ƿ񲹲�
			iv_pro_num_three = (TextView) convertView
					.findViewById(R.id.iv_pro_num_three); // ��Ҫ���ֵ���
			et_pro_num_three = (EditText) convertView
					.findViewById(R.id.et_pro_num_three); // �ֲ�������
			iv_pro_hid_three = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_three); // ����

			rlayout_four = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_four); // ��������
			iv_pro_name_four = (TextView) convertView
					.findViewById(R.id.tv_pro_name_four); // ��Ʒ����
			iv_pro_logo_four = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_four); // ��ƷͼƬ
			ib_pro_finish_four = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_four); // ��Ʒ�Ƿ񲹲�
			iv_pro_num_four = (TextView) convertView
					.findViewById(R.id.iv_pro_num_four); // ��Ҫ���ֵ���
			et_pro_num_four = (EditText) convertView
					.findViewById(R.id.et_pro_num_four); // �ֲ�������
			iv_pro_hid_four = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_four); // ����

			rlayout_five = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_five); // ��������
			iv_pro_name_five = (TextView) convertView
					.findViewById(R.id.tv_pro_name_five); // ��Ʒ����
			iv_pro_logo_five = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_five); // ��ƷͼƬ
			ib_pro_finish_five = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_five); // ��Ʒ�Ƿ񲹲�
			iv_pro_num_five = (TextView) convertView
					.findViewById(R.id.iv_pro_num_five); // ��Ҫ���ֵ���
			et_pro_num_five = (EditText) convertView
					.findViewById(R.id.et_pro_num_five); // �ֲ�������
			iv_pro_hid_five = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_five); // ����

			rlayout_six = (RelativeLayout) convertView
					.findViewById(R.id.rlayout_six); // ��������
			iv_pro_name_six = (TextView) convertView
					.findViewById(R.id.tv_pro_name_six); // ��Ʒ����
			iv_pro_logo_six = (ImageView) convertView
					.findViewById(R.id.iv_pro_logo_six); // ��ƷͼƬ
			ib_pro_finish_six = (ImageButton) convertView
					.findViewById(R.id.ib_pro_finish_six); // ��Ʒ�Ƿ񲹲�
			iv_pro_num_six = (TextView) convertView
					.findViewById(R.id.iv_pro_num_six); // ��Ҫ���ֵ���
			et_pro_num_six = (EditText) convertView
					.findViewById(R.id.et_pro_num_six); // �ֲ�������
			iv_pro_hid_six = (TextView) convertView
					.findViewById(R.id.iv_pro_hid_six); // ����
		}
	}
}
