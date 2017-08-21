package com.example.zzq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zzq.bean.UserInfo;
import com.zhang.box.R;
import com.zhang.box.utils.CommonAdapter;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.ViewHolder;

/**
 * 根据商品价格显示 对应的应用
 * 
 * @author wang
 * 
 */
@SuppressWarnings("rawtypes")
public class MyDesProAppAdapter extends CommonAdapter {

	private Context mContext;
	private ViewHolder viewHolder;
	private TextView desproappitem_tv_name_one;
	private TextView desproappitem_tv_price_one;
	private ImageView desproappitem_iv_logo_one;
	private TextView desproappitem_tv_name_two;
	private TextView desproappitem_tv_price_two;
	private ImageView desproappitem_iv_logo_two;
	private TextView desproappitem_tv_name_three;
	private TextView desproappitem_tv_price_three;
	private ImageView desproappitem_iv_logo_three;
	private RelativeLayout desproappitem_layone;
	private RelativeLayout desproappitem_laytwo;
	private RelativeLayout desproappitem_laythree;

	public MyDesProAppAdapter(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	public int getCount() {

		int size = UserInfo.searchApp.size();
		if (size % 3 == 0) {
			return size / 3;
		} else {
			return (size / 3 + 1);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.des_pro_app_item, position);

		desproappitem_tv_name_one = viewHolder
				.getView(R.id.desproappitem_tv_name_one);
		desproappitem_tv_price_one = viewHolder
				.getView(R.id.desproappitem_tv_price_one);
		desproappitem_iv_logo_one = viewHolder
				.getView(R.id.desproappitem_iv_logo_one);
		desproappitem_layone = viewHolder.getView(R.id.desproappitem_layone);

		desproappitem_tv_name_two = viewHolder
				.getView(R.id.desproappitem_tv_name_two);
		desproappitem_tv_price_two = viewHolder
				.getView(R.id.desproappitem_tv_price_two);
		desproappitem_iv_logo_two = viewHolder
				.getView(R.id.desproappitem_iv_logo_two);
		desproappitem_laytwo = viewHolder.getView(R.id.desproappitem_laytwo);

		desproappitem_tv_name_three = viewHolder
				.getView(R.id.desproappitem_tv_name_three);
		desproappitem_tv_price_three = viewHolder
				.getView(R.id.desproappitem_tv_price_three);
		desproappitem_iv_logo_three = viewHolder
				.getView(R.id.desproappitem_iv_logo_three);
		desproappitem_laythree = viewHolder
				.getView(R.id.desproappitem_laythree);

		position = position * 3;
		if (position < UserInfo.searchApp.size()) {
			desproappitem_layone.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(mContext,
					UserInfo.searchApp.get(position).applogo,
					desproappitem_iv_logo_one);

			// 加载文字
			desproappitem_tv_name_one
					.setText(UserInfo.searchApp.get(position).appname);

			String str = (UserInfo.searchApp.get(position).appprice) / 100
					+ " ";
			desproappitem_tv_price_one.setText(str);

			final int itempositions = position;
			desproappitem_layone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					UserInfo info = UserInfo.searchApp.get(itempositions);
					MulDataUtils.appItemData(mContext, info);
				}
			});
		} else {
			desproappitem_layone.setVisibility(View.INVISIBLE);
		}

		if ((position + 1) < UserInfo.searchApp.size()) {
			desproappitem_laytwo.setVisibility(View.VISIBLE);

			MulDataUtils.dealPicture(mContext,
					UserInfo.searchApp.get(position + 1).applogo,
					desproappitem_iv_logo_two);

			// 加载文字
			desproappitem_tv_name_two.setText(UserInfo.searchApp
					.get(position + 1).appname);
			String str = (UserInfo.searchApp.get(position + 1).appprice) / 100
					+ "";
			desproappitem_tv_price_two.setText(str);
			final int itempositions = position + 1;
			desproappitem_laytwo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					UserInfo info = UserInfo.searchApp.get(itempositions);
					MulDataUtils.appItemData(mContext, info);
				}
			});
		} else {
			desproappitem_laytwo.setVisibility(View.INVISIBLE);

		}
		if ((position + 2) < UserInfo.searchApp.size()) {
			desproappitem_laythree.setVisibility(View.VISIBLE);

			MulDataUtils.dealPicture(mContext,
					UserInfo.searchApp.get(position + 2).applogo,
					desproappitem_iv_logo_three);

			// 加载文字
			desproappitem_tv_name_three.setText(UserInfo.searchApp
					.get(position + 2).appname);
			String str = (UserInfo.searchApp.get(position + 2).appprice) / 100
					+ "";
			desproappitem_tv_price_three.setText(str);
			final int itempositions = position + 2;
			desproappitem_laythree.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int size =UserInfo.searchApp.size();
					UserInfo info = UserInfo.searchApp.get(itempositions);
					MulDataUtils.appItemData(mContext, info);
				}
			});
		} else {
			desproappitem_laythree.setVisibility(View.INVISIBLE);
		}

		return viewHolder.getConvertView();
	}
}
