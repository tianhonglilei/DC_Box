package com.example.zzq.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.avm.serialport_142.MainHandler;
import com.example.zzq.bean.ConfigInfo;
import com.zhang.box.R;
import com.zhang.box.services.MachineBrocastReceiver;
import com.zhang.box.utils.CommonAdapter;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.ToastTools;
import com.zhang.box.utils.ViewHolder;

@SuppressWarnings("rawtypes")
public class HuoDaoAdapter extends CommonAdapter {

	private Context context;
	private List<ConfigInfo> mDatas;
	private LinearLayout llayout_huodao_one;
	private ImageView iv_huodao_one;
	private TextView tv_huodao_one;
	private Button btn_huodao_one;

	private LinearLayout llayout_huodao_two;
	private ImageView iv_huodao_two;
	private TextView tv_huodao_two;
	private Button btn_huodao_two;

	private LinearLayout llayout_huodao_three;
	private ImageView iv_huodao_three;
	private TextView tv_huodao_three;
	private Button btn_huodao_three;

	private LinearLayout llayout_huodao_four;
	private ImageView iv_huodao_four;
	private TextView tv_huodao_four;
	private Button btn_huodao_four;

	private ConfigInfo info;

	@SuppressWarnings("unchecked")
	public HuoDaoAdapter(Context context, List mDatas) {
		super(context, mDatas);
		this.context = context;
		this.mDatas = mDatas;
	}

	@Override
	public int getCount() {
		int size = mDatas.size();
		if (size % 4 == 0) {
			return size / 4;
		} else {
			return (size / 4 + 1);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,
				R.layout.huodao_item, position);

		llayout_huodao_one = viewHolder.getView(R.id.llayout_huodao_one);
		iv_huodao_one = viewHolder.getView(R.id.iv_huodao_one);
		tv_huodao_one = viewHolder.getView(R.id.tv_huodao_one);
		btn_huodao_one = viewHolder.getView(R.id.btn_huodao_one);

		llayout_huodao_two = viewHolder.getView(R.id.llayout_huodao_two);
		iv_huodao_two = viewHolder.getView(R.id.iv_huodao_two);
		tv_huodao_two = viewHolder.getView(R.id.tv_huodao_two);
		btn_huodao_two = viewHolder.getView(R.id.btn_huodao_two);

		llayout_huodao_three = viewHolder.getView(R.id.llayout_huodao_three);
		iv_huodao_three = viewHolder.getView(R.id.iv_huodao_three);
		tv_huodao_three = viewHolder.getView(R.id.tv_huodao_three);
		btn_huodao_three = viewHolder.getView(R.id.btn_huodao_three);

		llayout_huodao_four = viewHolder.getView(R.id.llayout_huodao_four);
		iv_huodao_four = viewHolder.getView(R.id.iv_huodao_four);
		tv_huodao_four = viewHolder.getView(R.id.tv_huodao_four);
		btn_huodao_four = viewHolder.getView(R.id.btn_huodao_four);

		position = position * 4;
		if (position < mDatas.size()) {
			info = mDatas.get(position);
			llayout_huodao_one.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, info.getLogo(), iv_huodao_one);
			final int hid = info.getHid();
			tv_huodao_one.setText(hid + "货道");
			final int hgid = info.getHgid();
			btn_huodao_one.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e("whwhw", "hg=="+hgid+","+hid);
					if (hid < 10) {
						if (hgid == 9 || hgid == 1 || hgid == 2) {
							inidata("0" + hgid, "0" + hid);
						} else {
							inidata(hgid + "", "0" + hid);
						}
					} else {

						if (hgid == 9 || hgid == 1 || hgid == 2) {
							inidata("0" + hgid, hid+"");
						} else {
							inidata(hgid + "",  hid+"");
						}
					}
				}
			});

		} else {
			llayout_huodao_one.setVisibility(View.INVISIBLE);
		}

		if ((position + 1) < mDatas.size()) {
			info = mDatas.get(position + 1);
			llayout_huodao_two.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, info.getLogo(), iv_huodao_two);
			final int hid = info.getHid();
			tv_huodao_two.setText(hid + "货道");
			final int hgid = info.getHgid();
			btn_huodao_two.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.e("whwhw", "hg=="+hgid+","+hid);
					if (hid < 10) {

						if (hgid == 9 || hgid == 1 || hgid == 2) {
							inidata("0" + hgid, "0" + hid);
						} else {
							inidata(hgid + "", "0" + hid);
						}
					} else {

						if (hgid == 9 || hgid == 1 || hgid == 2) {
							inidata("0" + hgid, hid+"");
						} else {
							inidata(hgid + "", hid+"");
						}
					}
				}
			});

		} else {
			llayout_huodao_two.setVisibility(View.INVISIBLE);
		}

		if ((position + 2) < mDatas.size()) {
			info = mDatas.get(position + 2);
			llayout_huodao_three.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, info.getLogo(), iv_huodao_three);
			final int hid = info.getHid();
			tv_huodao_three.setText(hid + "货道");
			final int hgid = info.getHgid();
			btn_huodao_three.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.e("whwhw", "hg=="+hgid+","+hid);
					if (hid < 10) {
						if (hgid == 9 || hgid == 1 || hgid == 2) {
							inidata("0" + hgid, "0" + hid);
						} else {
							inidata(hgid + "", "0" + hid);
						}
					} else {
						if (hgid == 9 || hgid == 1 || hgid == 2) {
							inidata("0" + hgid, hid+"");
						} else {
							inidata(hgid + "", hid+"");
						}
					}
				}
			});

		} else {
			llayout_huodao_three.setVisibility(View.INVISIBLE);
		}

		if ((position + 3) < mDatas.size()) {
			info = mDatas.get(position + 3);
			llayout_huodao_four.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context, info.getLogo(), iv_huodao_four);
			final int hid = info.getHid();
			tv_huodao_four.setText(hid + "货道");
			final int hgid = info.getHgid();
			btn_huodao_four.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.e("whwhw", "hg=="+hgid+","+hid);
					if (hid < 10) {
						if (hgid == 9 || hgid == 1 || hgid == 2) {
							inidata("0" + hgid, "0" + hid);
						} else {
							inidata(hgid + "", "0" + hid);
						}
					} else {
						if (hgid == 9 || hgid == 1 || hgid == 2) {
							inidata("0" + hgid, hid+"");
						} else {
							inidata(hgid + "", hid+"");
						}
					}
				}
			});

		} else {
			llayout_huodao_four.setVisibility(View.INVISIBLE);
		}

		return viewHolder.getConvertView();
	}

	protected void inidata(String hgid, String hid) {
		// String nums = "111" + hid + "00000100"; //饮料柜
		// String nums = "091" + "01" + "00000100"; // 食品柜
		// String nums = "011" + "01"+ "00000100"; //礼品柜
		String nums = hgid + 1 + hid + "00000100";
		Log.e("whwh", "HuodaoActivity--->nums===" + nums);
		int numcode = (int) ((Math.random() * 9 + 1) * 100000);
		if (MainHandler.noticeAvmOutGoods(nums + "50", numcode + "")) {
			Log.e("whwh", hid + "出货成功!");
			ToastTools.showShort(context, hid + "出货成功!");
			// getReceiver();
		} else {
			ToastTools.showShort(context, hid + "出货失败!");
		}
	}

	/** 接收出货判断 */
	private void getReceiver() {
		MachineBrocastReceiver mbr = new MachineBrocastReceiver();
		Intent intent = new Intent();
		String action = intent.getAction();
		// action.equals("com.avm.serialport.OUT_GOODS");
		mbr.onReceive(mContext, intent);
		if (action.equals("com.avm.serialport.OUT_GOODS")) {
			String GoodRuslt = MainHandler.getTranResult();
		}
	}
}
