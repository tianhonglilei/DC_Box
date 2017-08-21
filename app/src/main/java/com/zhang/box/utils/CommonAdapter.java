package com.zhang.box.utils;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/** 通用的适配器 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected int flag;
	
	
	
	/**
	 * @param context 上下文
	 */
	public CommonAdapter(Context context){
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * context 上下文
	 * 
	 * mDatas 数据源
	 */
	public CommonAdapter(Context context, List<T> mDatas) {
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mDatas = mDatas;
	}

	/**
	 * context 上下文
	 * 
	 * flag 标记
	 * 
	 * mDatas 数据源
	 */
	public CommonAdapter(Context context, int flag, List<T> mDatas) {
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.flag = flag;
		this.mDatas = mDatas;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
