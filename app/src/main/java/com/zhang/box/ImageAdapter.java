package com.zhang.box;

import com.example.zzq.bean.UserInfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	ImageLoader mLogoImage;

	public ImageAdapter(Context context) {

		mContext = context;
		inflater = LayoutInflater.from(mContext);
		mLogoImage = ImageLoader.GetObject(context);

	}

	@Override
	public int getCount() {
		return UserInfo.advVideo.size();
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
			convertView = inflater.inflate(R.layout.list_items, null);
			viewholder.micon = (ImageView) convertView
					.findViewById(R.id.ItemImage);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}

		Drawable draw = null;
		UserInfo info = UserInfo.advVideo.get(position);
		draw = mLogoImage.loadNetDrawable(info.advimg, new KImageCallback(
				viewholder.micon));
		if (draw != null) {
			viewholder.micon.setBackgroundDrawable(draw);
		}

		return convertView;
	}

	class ViewHolder {
		RelativeLayout mlayout;
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
