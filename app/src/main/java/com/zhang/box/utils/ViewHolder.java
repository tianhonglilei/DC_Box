package com.zhang.box.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder {
	private  int mPosition = 0;
	private final SparseArray<View> mViews;
	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);

	}

	/**
	 * �õ�һ��ViewHolder����
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder(context, parent, layoutId, position);
			return viewHolder;
		}else{
			viewHolder.mPosition = position;
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return viewHolder;
	}

	/**
	 * ͨ���ؼ���Id��ȡ���ڵĿؼ������û�������views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {

		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

}
