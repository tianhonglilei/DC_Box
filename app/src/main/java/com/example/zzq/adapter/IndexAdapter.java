package com.example.zzq.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.ImageCallback;
import com.zhang.box.ImageLoader;
import com.zhang.box.R;

public class IndexAdapter extends PagerAdapter {

	/**
	 * PagerAdapter
	 */
	private LayoutInflater inflater;
	public Context mContext;
	private ViewHolder viewHolder = null;
	ImageLoader mLogoImage;

	public IndexAdapter(Context context) {
		super();
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		mLogoImage = ImageLoader.GetObject(context);

	}

	@Override
	public int getCount() {

		return UserInfo.advVideo.size();
	}

	/**
     *  
     */
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj; // key
	}

	/**
     *  
     */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		container.removeView((View) object);

	}

	class ViewHolder {
		RelativeLayout mlayout;
		ImageView micon;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View view = null;
		view = container.findViewById(position + 0);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.index_view_pager, null);
			viewHolder.mlayout = (RelativeLayout) view
					.findViewById(R.id.iv_layout);
			viewHolder.micon = (ImageView) view.findViewById(R.id.iv_icon);
			view.setTag(viewHolder);
			container.addView(view);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		Drawable draw = null;
		String url = UserInfo.advVideo.get(position).advimg;
		draw = mLogoImage.loadNetDrawable(url, new KImageCallback(
				viewHolder.micon));
		if (draw != null) {
			viewHolder.micon.setBackgroundDrawable(draw);
		}

		return view;
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
