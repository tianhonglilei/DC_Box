package com.example.zzq.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;
import java.util.List;
import com.avm.serialport_142.MainHandler;
import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.DesProActivity;
import com.zhang.box.R;
import com.zhang.box.utils.CommonAdapter;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.OnclickToMuch;
import com.zhang.box.utils.ToastTools;
import com.zhang.box.utils.ViewHolder;

@SuppressWarnings("rawtypes")
public class GridViewAdapter extends CommonAdapter {

	private Context context;
	private ImageView micon;
	private ImageView btn;
	private ImageView degreeFlag;
	private TextView tv_gallary_price;
	private String resultInfo;

	@SuppressWarnings("unchecked")
	public GridViewAdapter(Context context, List<UserInfo> mDatas) {
		super(context, mDatas);
		this.context = context;
	}

	@Override
	public int getCount() {

		if (UserInfo.proMainlist.size() == 0)

			return 0;
		else
			return UserInfo.proMainlist.size() + 1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,
				R.layout.list_item, position);

		micon = viewHolder.getView(R.id.iv_gridpro);
		btn = viewHolder.getView(R.id.btn_girddown);
		degreeFlag = viewHolder.getView(R.id.iv_flag);
		tv_gallary_price = viewHolder.getView(R.id.tv_gallary_price);

		if (UserInfo.proMainlist.size() > 0) {
			if (position >= 9) {
				micon.setVisibility(View.INVISIBLE);
				btn.setVisibility(View.INVISIBLE);
				tv_gallary_price.setVisibility(View.INVISIBLE);
			} else {

				UserInfo userinfo = UserInfo.proMainlist.get(position);
				float price = (float) userinfo.price;
				tv_gallary_price.setText("¥" + price / 100 + "0");

				int huogui = Integer.parseInt(userinfo.prohuogui);
				int hdids = userinfo.hdid;
				//
				String huodaoInfo = MainHandler.getGoodsInfo(huogui, hdids);
				resultInfo = huodaoInfo.substring(0, 1);

				int max = userinfo.max;
				int mfinish = userinfo.mfinish;
				int maths = max - mfinish;
				int protype = userinfo.protype;
				boolean flag = false;
				if (protype == 1) {

					if (("0".equals(resultInfo))) {
						flag = true;
						micon.setClickable(true);
						degreeFlag.setVisibility(View.VISIBLE);
						if (hdids > 0 && hdids < 9) {
							if ("0".equals(SysData.leftNo)) {
								degreeFlag
										.setBackgroundResource(R.drawable.ice_flag);
							} else if ("1".equals(SysData.leftNo)) {
								degreeFlag
										.setBackgroundResource(R.drawable.hot_flag);
							} else {
								degreeFlag.setVisibility(View.INVISIBLE);
							}
						} else if (hdids > 8) {
							if ("0".equals(SysData.rightNo)) {
								degreeFlag
										.setBackgroundResource(R.drawable.ice_flag);
							} else if ("1".equals(SysData.rightNo)) {
								degreeFlag
										.setBackgroundResource(R.drawable.hot_flag);
							} else {
								degreeFlag.setVisibility(View.INVISIBLE);
							}
						}
					} else if ("1".equals(resultInfo)) {
						flag = false;
						micon.setClickable(false);
						tv_gallary_price.setText("已售罄");
						degreeFlag.setVisibility(View.INVISIBLE);
						MulDataUtils.dealPicture(context, userinfo.logogray,
								micon);

					} else if ("9".equals(resultInfo)) {
						flag = false;
						micon.setClickable(false);
						tv_gallary_price.setText("已售罄.");
						degreeFlag.setVisibility(View.INVISIBLE);
						MulDataUtils.dealPicture(context, userinfo.logogray,
								micon);

					} else {
						flag = false;
						tv_gallary_price.setText(".已售罄");
						micon.setClickable(false);
						degreeFlag.setVisibility(View.INVISIBLE);
						MulDataUtils.dealPicture(context, userinfo.logogray,
								micon);
					}

				} else if (protype == 2) {
					// shipin
					if (maths >= 1) {
						flag = true;
						micon.setClickable(true);
					} else {

						flag = false;
						tv_gallary_price.setText("已售罄");
						micon.setClickable(false);
						degreeFlag.setVisibility(View.INVISIBLE);
						MulDataUtils.dealPicture(context, userinfo.logogray,
								micon);
					}
				}
				if (flag) {
					MulDataUtils.dealPicture(context, userinfo.logo, micon);
					micon.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (OnclickToMuch.isFastClick()) {

								// 启动服务
								// context.startService(new Intent(context,
								// MusicService.class));
								// 传递Data对象
								UserInfo.fromActivity = 1;
								UserInfo info = UserInfo.proMainlist
										.get(position);

								Intent intent = new Intent(context,
										DesProActivity.class);
								Bundle bundle = new Bundle();
								bundle.putInt("flag", 11);
								bundle.putSerializable("proInfos",
										(Serializable) info);
								intent.putExtras(bundle);
								context.startActivity(intent);
							}
						}
					});
				}
			}

		} else {
			ToastTools.showShort(mContext, "主页面商品没有数据,请重新检测!");
			micon.setVisibility(View.INVISIBLE);
			btn.setVisibility(View.INVISIBLE);
			tv_gallary_price.setVisibility(View.INVISIBLE);
		}
		return viewHolder.getConvertView();
	}
}
