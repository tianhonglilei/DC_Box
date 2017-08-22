package com.zhang.box;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;
import com.avm.serialport_142.MainHandler;
import com.example.zzq.adapter.GallaryAdItemAdapter;
import com.example.zzq.adapter.GridViewAdapter;
import com.example.zzq.adapter.IndexAdapter;
import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.services.MachineBrocastReceiver;
import com.zhang.box.utils.ActivitySkipUtil;
import com.zhang.box.utils.ImageLoaderUtils;
import com.zhang.box.utils.MulDataUtils;

@SuppressLint("SimpleDateFormat")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GallaryActivity extends Activity {
	NetImageLoader mLogoImage;
	private Context context;
	private boolean mVideo = true;
	private boolean mVideoImg = false;
	private int mPosition = 0;
	private GridView gridView;
	private Button ib_gallary_pro;
	private Button ib_gallary_app;
	private VideoView vvIntroduction;
	private IndexAdapter mIndexAdapter; // 图片适配器
	private ViewPager mindexViewPager; // 轮播图
	private int index;
	private boolean isPlaying;
	private GridViewAdapter adapter;
	private int count;
	private MachineBrocastReceiver receiver;
	private int width;
	private int height;
	private List<UserInfo> allVideoList = null;
	private ImageView iv_tui;
	private Bitmap iv_tui_bitmap;
	private ListView lv_adItem;
	private GallaryAdItemAdapter gAdapter;
	private String huoguiNo;
	private BooksDB mBooksDB;
	private ImageView iv_viewImagView;
	private Timer mTimer;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		setContentView(R.layout.activity_gallary);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏去掉状态栏

		context = this;
		// 获取屏幕长宽
		WindowManager wm = getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		count = 0;
		mBooksDB = new BooksDB(context);
		// 注册广播
		registerBroadcastReceive();
		getDegreeStu();

		mLogoImage = NetImageLoader.GetObject(this);

		iv_tui = (ImageView) findViewById(R.id.iv_tui);
		iv_tui_bitmap = ImageLoaderUtils.loadHugeBitmapFromDrawable(
				getResources(), R.drawable.gallary_tui, height, width);
		iv_tui.setImageBitmap(iv_tui_bitmap);

		allVideoList = new ArrayList<UserInfo>();
		setAdItem();
		setIndexAdapter();
		setVideo();
		gridView = (GridView) findViewById(R.id.main_grid);

		// proMainlist 数据应该清楚修改一下
		setGridView();
		Button btn4 = (Button) findViewById(R.id.btn4);
		btn4.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// finish();
				System.exit(0);
			}
		});

		Button btn_ceshi = (Button) findViewById(R.id.btn_ceshi);
		btn_ceshi.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivitySkipUtil.skipAnotherActivity(GallaryActivity.this,
						ConfigActivity.class, false);
			}
		});

		ib_gallary_app = (Button) findViewById(R.id.ib_gallary_app);
		ib_gallary_app.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivitySkipUtil.skipAnotherActivity(GallaryActivity.this,
						AppActivity.class, false);
			}
		});

		ib_gallary_pro = (Button) findViewById(R.id.ib_gallary_pro);
		ib_gallary_pro.setVisibility(View.VISIBLE);
		ib_gallary_pro.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (huoguiNo != null) {
					ActivitySkipUtil.skipAnotherActivity(GallaryActivity.this,
							BuyActivity.class, false);
				} else {
					ActivitySkipUtil.skipAnotherActivity(GallaryActivity.this,
							BuyActivity.class, true);
				}
			}
		});
	}

	private void getDegreeStu() {
		huoguiNo = MainHandler.getAVMConfigInfo(11);
		if (huoguiNo != null) {
			String leftStuNo = huoguiNo.substring(19, 20);
			SysData.leftNo = leftStuNo;
			String rightStuNo = huoguiNo.substring(21, 22);
			SysData.rightNo = rightStuNo;
		}
	}

	/** 动态的广告条 */
	private void setAdItem() {
		lv_adItem = (ListView) findViewById(R.id.lv_adItem);
		// new Timer().schedule(new com.example.zzq.adapter.TimeTaskScroll(this,
		// lv_adItem, listDatas), 20, 20);

		gAdapter = new GallaryAdItemAdapter(context, UserInfo.advVideo);
		lv_adItem.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		lv_adItem.setAdapter(gAdapter);
		lv_adItem.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int i = 0;
				i = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				int i = 0;
			}
		});

		lv_adItem.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

		lv_adItem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos = arg2 % (UserInfo.advVideo.size());
				mPosition = pos;
				String type = UserInfo.advVideo.get(mPosition).type;
				if (type.equals("video")) {
					SetVideo(mPosition);
				} else {
					SetImg(mPosition);
				}
			}
		});
	}

	/** 发送广播 判断们是否关门，开门 以及货道出货结果 */
	private void registerBroadcastReceive() {
		receiver = new MachineBrocastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.avm.serialport.door_state");
		registerReceiver(receiver, filter);
	}

	/** 加载网络视频数据 */
	private void AddVideoData() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mHandlerVideo.obtainMessage(0, 0, 0);
				mHandlerVideo.sendMessage(msg);
			}
		}.start();
	}

	private Handler mHandlerVideo = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			index = 0;
			downpro(index);
		}
	};

	protected void downpro(int andex) {

		if (allVideoList.size() < 1) {
			return;
		}
		mLogoImage.loadNetDrawable(allVideoList.get(index).video,
				new KImageCallback());

	}

	class KImageCallback implements ImageCallback {

		public KImageCallback() {

		}

		@Override
		public void imageLoaded(Drawable imageDrawable, String imageUrl) {

			index++;
			if (index < allVideoList.size()) {
				downpro(index);
			}
		}
	}

	/** activity销毁时 轮播图和视频暂停 */
	@Override
	protected void onPause() {
		super.onPause();

		// if (vvIntroduction.isPlaying()) {
		if (vvIntroduction != null) {
			vvIntroduction.pause();
		}
		isPlaying = false;
		// mHandler.removeCallbacksAndMessages(null);
		// }
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (UserInfo.proMainlist.size() == 0 || UserInfo.proMainlist == null) {
			UserInfo.proMainlist.clear();
			mBooksDB.ReadProMain();
		}
		if (huoguiNo == null) {
			getDegreeStu();
		}
		// if (!vvIntroduction.isPlaying()) {
		if (count != 0) {
			if (vvIntroduction != null) {
				vvIntroduction.start();
			}
		}
		isPlaying = true;
		count = 1;
		// }

		// mIndexAdapter.notifyDataSetChanged();
		adapter.notifyDataSetChanged();
		gAdapter.notifyDataSetChanged();
	}

	/** activity销毁时操作 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (vvIntroduction != null) {
			vvIntroduction.suspend(); // 将VideoView所占用的资源释放掉
		}
		// 释放广播
		unregisterReceiver(receiver);

		if (mTimer != null) {
			mTimer.cancel();
		}

		// 页面销毁时 清除图片资源
		if (iv_tui_bitmap != null && !iv_tui_bitmap.isRecycled()) {

			iv_tui_bitmap.recycle();
			iv_tui_bitmap = null;
		}
		System.gc();
	}

	/** 轮播大图 */
	private void setIndexAdapter() {
		iv_viewImagView = (ImageView) findViewById(R.id.iv_viewImagView);
		iv_viewImagView.setVisibility(View.INVISIBLE);

		// mindexViewPager.setOffscreenPageLimit(3);
		// mindexViewPager.setAdapter(mIndexAdapter);
		// mindexViewPager.setVisibility(View.INVISIBLE);
		// mindexViewPager.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// return true;
		// }
		// });
	}

	// TODO WHWH
	/** 设置视频 */
	private void setVideo() {
		for (int i = 0; i < UserInfo.advVideo.size(); i++) {
			String aurl = UserInfo.advVideo.get(i).video;
			int pos = aurl.lastIndexOf("/");
			String filename = aurl.substring(pos + 1);
			String name = Environment.getExternalStorageDirectory().toString()
					+ "/boxcontent/" + filename;
			File file1 = new File(name);

			if (!file1.exists()) {
				// 添加一个数组
				allVideoList.add(UserInfo.advVideo.get(i));
				// int size = allVideoList.size();
				UserInfo.advVideo.remove(i);
				i = 0;
			}
		}

		AddVideoData();
		if (UserInfo.advVideo.size() > 0) {
			mPosition = 0;
			String type = UserInfo.advVideo.get(mPosition).type;
			lv_adItem.setSelection(mPosition);
			vvIntroduction = (VideoView) findViewById(R.id.vv_introduction);
			// 设置媒体控制条
			MediaController mc = new MediaController(this);
			mc.setVisibility(View.INVISIBLE);
			vvIntroduction.setMediaController(mc);
			vvIntroduction
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						private String type;

						@Override
						public void onCompletion(MediaPlayer mp) {
							// vvIntroduction.suspend();// 将VideoView所占用的资源释放掉
							// 播放完毕后
							mPosition++;
							if (mPosition > UserInfo.advVideo.size() - 1) {
								mPosition = 0;
							}

							// TODO WHWH
							if (UserInfo.advVideo.size() >= 0
									&& mPosition < UserInfo.advVideo.size()) {
								type = UserInfo.advVideo.get(mPosition).type;
								if (type.equals("video")) {
									SetVideo(mPosition);
								} else {
									SetImg(mPosition);
								}
							}
						}
					});

			vvIntroduction.setVisibility(View.INVISIBLE);
			if (type.equals("video")) {
				SetVideo(mPosition);
			} else {
				SetImg(mPosition);
			}
		}
	}

	/** 设置GirdView参数，绑定数据 */
	private void setGridView() {
		int size = UserInfo.proMainlist.size() + 1;
		int length = 133;
		// GridView提取
		MulDataUtils.setGrideViews(GallaryActivity.this, gridView, size,
				length, -1);
		// GridViewAdapter adapter = new GridViewAdapter(GallaryActivity.this,
		// mAppList, 0, ProInstance.getInstance().proInfosList);

		adapter = new GridViewAdapter(GallaryActivity.this,
				UserInfo.proMainlist);
		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void startAutoScroll() {
		if (mVideoImg && !mVideo) {
			mTimer = new Timer();
			mTimer.schedule(new CheckTask(), 10000, 10000);
		}
	}

	private class CheckTask extends TimerTask {
		@Override
		public void run() {
			if (mPosition >= UserInfo.advVideo.size() - 1) {
				mPosition = 0;
			}
			String type = UserInfo.advVideo.get(mPosition).type;
			if (type == "video") {

			} else {
				mPosition++;
				if (mPosition >= UserInfo.advVideo.size())
					mPosition = 1;
				type = UserInfo.advVideo.get(mPosition).type;
				if (type.equals("video")) {
					SetVideo(mPosition);
				} else {
					SetImg(mPosition);
				}
			}
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			String type = UserInfo.advVideo.get(mPosition).type;
			if (type == "video") {

			} else {
				mPosition++;
				if (mPosition >= UserInfo.advVideo.size())
					mPosition = 1;
				type = UserInfo.advVideo.get(mPosition).type;

				if (type.equals("video")) {
					SetVideo(mPosition);
				} else {
					SetImg(mPosition);
				}
			}
		}
	};

	public void SetImg(final int andex) {
		Log.e("whwh", "dsdfaasdf");
		if (mTimer != null) {
			mTimer.cancel();
		}
		mVideo = false;
		mVideoImg = true;
		lv_adItem.post(new Runnable() {

			@Override
			public void run() {
				lv_adItem.setSelection(andex - 1);
			}
		});

		// mindexViewPager.setCurrentItem(mPosition);
		// mindexViewPager.setVisibility(View.VISIBLE);
		vvIntroduction.setVisibility(View.INVISIBLE);
		iv_viewImagView.post(new Runnable() {

			@Override
			public void run() {
				iv_viewImagView.setVisibility(View.VISIBLE);
				String url = UserInfo.advVideo.get(andex).advimg;
				MulDataUtils.dealPicture(context, url, iv_viewImagView);
			}
		});
		startAutoScroll();
	}

	public void SetVideo(final int andex) {
		if (mTimer != null) {
			mTimer.cancel();
		}
		mVideo = true;
		mVideoImg = false;
		iv_viewImagView.post(new Runnable() {

			@Override
			public void run() {
				iv_viewImagView.setVisibility(View.INVISIBLE);
			}
		});

		vvIntroduction.post(new Runnable() {

			@Override
			public void run() {
				vvIntroduction.setVisibility(View.VISIBLE);
			}
		});

		lv_adItem.post(new Runnable() {

			@Override
			public void run() {
				lv_adItem.setSelection(andex - 1);
			}
		});

		String url = UserInfo.advVideo.get(andex).video;
		int pos = url.lastIndexOf("/");
		String filename = url.substring(pos + 1);
		String xxx = Environment.getExternalStorageDirectory().toString()
				+ "/boxcontent/" + filename;
		final Uri videoUri = Uri.parse(xxx);
		if (vvIntroduction != null) {
			vvIntroduction.post(new Runnable() {

				@Override
				public void run() {
					vvIntroduction.setVisibility(View.VISIBLE);
					vvIntroduction.setVideoURI(videoUri);
					vvIntroduction.start();
				}
			});
		}
	}
}
