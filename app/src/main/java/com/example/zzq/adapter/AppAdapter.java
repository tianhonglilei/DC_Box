package com.example.zzq.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.R;
import com.zhang.box.utils.CommonAdapter;
import com.zhang.box.utils.MulDataUtils;
import com.zhang.box.utils.ViewHolder;

@SuppressWarnings("rawtypes")
public class AppAdapter extends CommonAdapter {

	private Context context;
	private ImageView ivPicOne;
	private ImageView ivPicTwo;
	private ImageView ivPicThree;
	private ImageView ivPicFour;
	private RelativeLayout layoutone;
	private RelativeLayout layouttwo;
	private RelativeLayout layoutthree;
	private RelativeLayout layoutfour;
	private TextView tvTitleOne;
	private TextView tvTitleTwo;
	private TextView tvTitleThree;
	private TextView tvTitleFour;
	private TextView tvDescOne;
	private TextView tvDescTwo;
	private TextView tvDescThree;
	private TextView tvDescFour;
	private TextView tvPriceOne;
	private TextView tvPriceTwo;
	private TextView tvPriceThree;
	private TextView tvPriceFour;

	public AppAdapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		if (UserInfo.appMainshow.size() % 4 == 0)
			return UserInfo.appMainshow.size() / 4;
		else
			return (UserInfo.appMainshow.size() / 4 + 1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,
				R.layout.app_item, position);
		ivPicOne = viewHolder.getView(R.id.ivPicOne);
		ivPicTwo = viewHolder.getView(R.id.ivPicTwo);
		ivPicThree = viewHolder.getView(R.id.ivPicThree);
		ivPicFour = viewHolder.getView(R.id.ivPicFour);

		layoutone = viewHolder.getView(R.id.layone);
		layouttwo = viewHolder.getView(R.id.laytwo);
		layoutthree = viewHolder.getView(R.id.laythree);
		layoutfour = viewHolder.getView(R.id.layfour);

		tvTitleOne = viewHolder.getView(R.id.tvTitleOne);
		tvTitleTwo = viewHolder.getView(R.id.tvTitleTwo);
		tvTitleThree = viewHolder.getView(R.id.tvTitleThree);
		tvTitleFour = viewHolder.getView(R.id.tvTitleFour);

		tvDescOne = viewHolder.getView(R.id.tvDescOne);
		tvDescTwo = viewHolder.getView(R.id.tvDescTwo);
		tvDescThree = viewHolder.getView(R.id.tvDescThree);
		tvDescFour = viewHolder.getView(R.id.tvDescFour);

		tvPriceOne = viewHolder.getView(R.id.tvPriceOne);
		tvPriceTwo = viewHolder.getView(R.id.tvPriceTwo);
		tvPriceThree = viewHolder.getView(R.id.tvPriceThree);
		tvPriceFour = viewHolder.getView(R.id.tvPriceFour);

		// btnLoadOne = viewHolder.getView(R.id.btnLoadOne);
		// btnLoadTwo = viewHolder.getView(R.id.btnLoadTwo);
		// btnLoadThree = viewHolder.getView(R.id.btnLoadThree);
		// btnLoadFour = viewHolder.getView(R.id.btnLoadFour);

		position = position * 4;
		if (position < UserInfo.appMainshow.size()) {
			layoutone.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context,
					UserInfo.appMainshow.get(position).applogo, ivPicOne);

			tvTitleOne.setText(UserInfo.appMainshow.get(position).appname);
			tvDescOne.setText(UserInfo.appMainshow.get(position).duan);
			String str = (UserInfo.appMainshow.get(position).appprice) / 100
					+ " ";
			tvPriceOne.setText(str);
			final int proPosition = position;
			layoutone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UserInfo info = UserInfo.appMainshow.get(proPosition);
					MulDataUtils.appItemData(context, info);
				}
			});
		} else {
			layoutone.setVisibility(View.INVISIBLE);
		}

		if ((position + 1) < UserInfo.appMainshow.size()) {
			layouttwo.setVisibility(View.VISIBLE);
			MulDataUtils.dealPicture(context,
					UserInfo.appMainshow.get(position + 1).applogo, ivPicTwo);

			tvTitleTwo.setText(UserInfo.appMainshow.get(position + 1).appname);
			tvDescTwo.setText(UserInfo.appMainshow.get(position + 1).duan);
			String str = (UserInfo.appMainshow.get(position + 1).appprice)
					/ 100 + " ";
			tvPriceTwo.setText(str);
			final int proPosition = position + 1;
			layouttwo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					UserInfo info = UserInfo.appMainshow.get(proPosition);
					MulDataUtils.appItemData(context, info);
				}
			});
		} else {
			layouttwo.setVisibility(View.INVISIBLE);
		}

		if ((position + 2) < UserInfo.appMainshow.size()) {
			layoutthree.setVisibility(View.VISIBLE);

			MulDataUtils.dealPicture(context,
					UserInfo.appMainshow.get(position + 2).applogo, ivPicThree);

			tvTitleThree
					.setText(UserInfo.appMainshow.get(position + 2).appname);
			tvDescThree.setText(UserInfo.appMainshow.get(position + 2).duan);
			String str = (UserInfo.appMainshow.get(position + 2).appprice)
					/ 100 + " ";
			tvPriceThree.setText(str);
			final int proPosition = position + 2;
			layoutthree.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					UserInfo info = UserInfo.appMainshow.get(proPosition);
					MulDataUtils.appItemData(context, info);
				}
			});
		} else {
			layoutthree.setVisibility(View.INVISIBLE);
		}

		if ((position + 3) < UserInfo.appMainshow.size()) {
			layoutfour.setVisibility(View.VISIBLE);

			MulDataUtils.dealPicture(context,
					UserInfo.appMainshow.get(position + 3).applogo, ivPicFour);

			tvTitleFour

			.setText(UserInfo.appMainshow.get(position + 3).appname);
			tvDescFour.setText(UserInfo.appMainshow.get(position + 3).duan);
			String str = (UserInfo.appMainshow.get(position + 3).appprice)
					/ 100 + " ";
			tvPriceFour.setText(str);
			final int proPosition = position + 3;
			layoutfour.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					UserInfo info = UserInfo.appMainshow.get(proPosition);
					MulDataUtils.appItemData(context, info);
				}
			});
		} else {
			layoutfour.setVisibility(View.INVISIBLE);
		}

		return viewHolder.getConvertView();
	}

}
