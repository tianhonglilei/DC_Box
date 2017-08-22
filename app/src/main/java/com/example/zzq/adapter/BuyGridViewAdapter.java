package com.example.zzq.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;
import java.util.List;
import com.avm.serialport_142.MainHandler;
import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.DesProActivity;
import com.zhang.box.DesProAppActivity;
import com.zhang.box.R;
import com.zhang.box.utils.CommonAdapter;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.OnclickToMuch;
import com.zhang.box.utils.ViewHolder;

@SuppressWarnings("rawtypes")
public class BuyGridViewAdapter extends CommonAdapter {

	private Context context;
	private int flag;
	private int type;
	private ImageView micon;
	private ImageView iv_flags;
	private TextView down2buy;
	private List<UserInfo> mDatas;
	private String resultInfo;

	// private ImageLoaderwh mImageLoader;

	@SuppressWarnings("unchecked")
	public BuyGridViewAdapter(Context context, int flag, int type,
			List<UserInfo> mDatas) {
		super(context, flag, mDatas);
		this.context = context;
		this.flag = flag;
		this.type = type;
		this.mDatas = mDatas;
	}

	@Override
	public int getCount() {

		// Math.floor()不取小数 向上取整:Math.ceil()//只要有小数都+1
		if (flag % 2 == 0) {
			return (int) Math.floor((double) (mDatas.size()) / 2);
		} else {
			return (int) Math.ceil((double) (mDatas.size()) / 2);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,
				R.layout.buylist_item_two, position);

		micon = viewHolder.getView(R.id.ItemImage2buy);
		down2buy = viewHolder.getView(R.id.down2buy);
		iv_flags = viewHolder.getView(R.id.iv_flags);
		int pos = 0;
		if (flag % 2 == 0) {
			pos = position;
		} else {
			pos = position + (int) Math.floor((double) (mDatas.size()) / 2);
		}

		UserInfo userinfo = mDatas.get(pos);
		String str = "" + (float) userinfo.price / 100 + "0" + "元";
		down2buy.setText(str);
		int hdids = userinfo.hdid;
		String huodaoInfo = MainHandler.getGoodsInfo(11, hdids);
		resultInfo = huodaoInfo.substring(0, 1);

		final int allProPosition = pos;
		int max = userinfo.max;
		int mfinish = userinfo.mfinish;
		int maths = max - mfinish;
		int protype = userinfo.protype;

		boolean flag = false;

		if (protype == 1) {

			if (("0".equals(resultInfo))) {
				flag = true;
				if (hdids > 0 && hdids < 9) {
					if ("0".equals(SysData.leftNo)) {
						iv_flags.setBackgroundResource(R.drawable.ice_flag);
					} else if ("1".equals(SysData.leftNo)) {
						iv_flags.setBackgroundResource(R.drawable.hot_flag);
					} else {
						iv_flags.setVisibility(View.INVISIBLE);
					}
				} else if (hdids > 8) {
					if ("0".equals(SysData.rightNo)) {
						iv_flags.setBackgroundResource(R.drawable.ice_flag);
					} else if ("1".equals(SysData.rightNo)) {
						iv_flags.setBackgroundResource(R.drawable.hot_flag);
					} else {
						iv_flags.setVisibility(View.INVISIBLE);
					}
				}
			} else if ("1".equals(resultInfo)) {
				flag = false;
				down2buy.setText("已告罄");
				MulDataUtils.dealPicture(context, userinfo.logogray, micon);
				iv_flags.setVisibility(View.INVISIBLE);
			} else if ("9".equals(resultInfo)) {
				flag = false;
				down2buy.setText("已告罄.");
				MulDataUtils.dealPicture(context, userinfo.logogray, micon);
				iv_flags.setVisibility(View.INVISIBLE);
			} else {
				flag = false;
				down2buy.setText(".已告罄");
				MulDataUtils.dealPicture(context, userinfo.logogray, micon);
				iv_flags.setVisibility(View.INVISIBLE);
			}
		} else if (protype == 2) {
			// shipin
			if (maths >= 1) {
				flag = true;
			} else {
				flag = false;
				down2buy.setText("已告罄");
				MulDataUtils.dealPicture(context, userinfo.logogray, micon);
				iv_flags.setVisibility(View.INVISIBLE);
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
						UserInfo.fromActivity = 1;
						UserInfo info = mDatas.get(allProPosition);
						Intent intent = new Intent(context,DesProActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("flagss", 11);
						bundle.putSerializable("proInfos",(Serializable) info);
						intent.putExtras(bundle);
						context.startActivity(intent);
					}
				}
			});
		}
		return viewHolder.getConvertView();
	}
}
