package com.example.zzq.adapter;

import java.util.List;

import com.example.zzq.bean.UserInfo;
import com.zhang.box.ImageCallback;
import com.zhang.box.ImageLoader;
import com.zhang.box.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GallaryAdItemAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	ImageLoader mLogoImage;
	private List<UserInfo> mDatas;
	int index;

	public GallaryAdItemAdapter(Context context, List<UserInfo> mDatas) {

		this.mContext = context;
		this.mDatas = mDatas;

		inflater = LayoutInflater.from(mContext);
		mLogoImage = ImageLoader.GetObject(context);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewholder = null;
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.gallary_ad_item, null);
			viewholder.mlayout = (LinearLayout) convertView
					.findViewById(R.id.gallaryitemlayout);
			viewholder.micon = (ImageView) convertView
					.findViewById(R.id.gallary_ad_ItemImage);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}

		Drawable draw = null;
		int advDatas = UserInfo.advVideo.size();
		if (advDatas > 0) {

			String picUrl = UserInfo.advVideo.get(position % advDatas).advimg;
			draw = mLogoImage.loadNetDrawable(picUrl, new KImageCallback(
					viewholder.micon));
			if (draw != null) {
				viewholder.micon.setBackgroundDrawable(draw);
			}
		}

		return convertView;
	}

	class ViewHolder {
		LinearLayout mlayout;
		ImageView micon;
	}

	class KImageCallback implements ImageCallback {
		ImageView logImage;

		public KImageCallback(ImageView logImage) {
			this.logImage = logImage;
		}

		@Override
		public void imageLoaded(Drawable imageDrawable, String imageUrl) {
			logImage.setBackgroundDrawable(imageDrawable);

		}
	}
}
