package com.zhang.box.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.zzq.bean.UserInfo;
import com.zhang.box.DesAppActivity;
import com.zhang.box.DesProActivity;
import com.zhang.box.ImageLoader;

/**
 * �ظ����������
 * 
 * @author wang
 * 
 */
public class MulDataUtils {

	/** �����ȡͼƬ·��ת��ͼƬ�����л��� */
	public static void dealPicture(Context context, String picPath,
			ImageView imageView) {
		ImageLoader mLogoImage = ImageLoader.GetObject(context);
		Drawable draw1 = null;

		draw1 = mLogoImage.loadNetDrawable(picPath, new KImageCallback(
				imageView));
		if (draw1 != null) {
			imageView.setImageDrawable(draw1);
			// imageView.setBackgroundDrawable(draw1);
		}
	}

	/** Ӧ��APP֮item */
	public static void appItemData(Context context, UserInfo userInfo) {
		/*
		UserInfo.fromActivity = 0;
		UserInfo.desPrice = userInfo.appprice;
		UserInfo.desBigImg = userInfo.apkBigImg;
		UserInfo.desTitle = userInfo.appname;
		UserInfo.desDuan = userInfo.duan;
		UserInfo.desId = userInfo.appid;
		UserInfo.desLogo = userInfo.applogo;

		// TODO WH
		// BooksDB mBooksDB = new BooksDB(context);
		// mBooksDB.insertUpload(UserInfo.desTitle);

		// Intent intent = new Intent(context, DesActivity.class);
		// intent.putExtra("flag", 1);
		// // ����Data����
		// context.startActivity(intent);

		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		hashMap.put("flag", 1);
		ActivitySkipUtil.skipAnotherActivity(context, DesAppActivity.class,
				hashMap);
		*/
		UserInfo.fromActivity = 0;
		Intent intent = new Intent(context,DesAppActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("flag", 1);
		bundle.putSerializable("proInfos",(Serializable) userInfo);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/** GridView����������ȡ�� */
	public static void setGrideViews(Activity activity, GridView gridView,
			int size, int length, int space) {
		int sizes = size;
		int lengths = length;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (sizes * (lengths + 4) * density);
		int itemWidth = (int) (lengths * density);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		gridView.setLayoutParams(params); // ����GirdView���ֲ���,���򲼾ֵĹؼ�
		gridView.setColumnWidth(itemWidth); // �����б����
		gridView.setHorizontalSpacing(space); // �����б���ˮƽ���
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setNumColumns(sizes); // ����������=�б�����
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	public static void getVideoFile(final List<UserInfo> list, File file) {// �����Ƶ�ļ�

		file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				// sdCard�ҵ���Ƶ����
				String name = file.getName();

				int i = name.indexOf('.');
				if (i != -1) {
					name = name.substring(i);
					if (name.equalsIgnoreCase(".mp4")
							|| name.equalsIgnoreCase(".3gp")
							|| name.equalsIgnoreCase(".wmv")
							|| name.equalsIgnoreCase(".rmvb")
							|| name.equalsIgnoreCase(".mov")
							|| name.equalsIgnoreCase(".avi")
							|| name.equalsIgnoreCase(".mkv")
							|| name.equalsIgnoreCase(".flv")
							|| name.equalsIgnoreCase(".f4v")) {
						UserInfo infoVideo = new UserInfo();
						// vi.setDisplayName(file.getName());
						// vi.setPath(file.getAbsolutePath());
						infoVideo.videoLocation = file.getName();
						list.add(infoVideo);
						return true;
					} else if (file.isDirectory()) {
						getVideoFile(list, file);
					}
				}
				return false;
			}
		});
	}

}
