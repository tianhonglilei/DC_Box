package com.example.zzq.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zzq.bean.UserInfo;
import com.zhang.box.R;
import com.zhang.box.utils.CommonAdapter;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.ToastTools;
import com.zhang.box.utils.ViewHolder;

@SuppressWarnings("rawtypes")
public class AppDesJPGridViewAdapter extends CommonAdapter {

	private Context context;
	private List<UserInfo> jpList;
	private ImageView iv_jplogo;
	private ImageView iv_jpdown;
	private TextView tv_jp_price;

	@SuppressWarnings("unchecked")
	public AppDesJPGridViewAdapter(Context context, List<UserInfo> jpList) {
		super(context, jpList);

		this.context = context;
		this.jpList = jpList;
	}

	@Override
	public int getCount() {
		return jpList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,
				R.layout.des_app_jp_item, position);

		iv_jplogo = viewHolder.getView(R.id.iv_jplogo);
		iv_jpdown = viewHolder.getView(R.id.iv_jpdown);
		tv_jp_price = viewHolder.getView(R.id.tv_jp_price);
		if (position > 9) {
			iv_jplogo.setVisibility(View.INVISIBLE);
			iv_jpdown.setVisibility(View.INVISIBLE);
			tv_jp_price.setVisibility(View.INVISIBLE);
		} else {
			MulDataUtils.dealPicture(context, jpList.get(position).applogo,
					iv_jplogo);
			String infos = jpList.get(position).appname + "  "
					+ jpList.get(position).appprice / 100 + " ";
			tv_jp_price.setText(infos);
		}

		return viewHolder.getConvertView();
	}

}