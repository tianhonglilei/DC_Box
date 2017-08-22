package com.zhang.box;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.example.zzq.bean.UserInfo;
import com.zhang.easymoney.net.HttpUtil;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//下载网络图标并读取本地图
public class ImageLoader extends Thread {
	private static ImageLoader m_object = new ImageLoader();
	private Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	private Context m_ctx;
	private Thread mThread = null;

	private ImageLoader() {
		mThread = new Thread(this);
		mThread.start();
	}

	public static ImageLoader GetObject(Context ctx) {
		m_object.m_ctx = ctx;
		return m_object;
	}

	/**
	 * 清除全部数据
	 */
	public void RemoveAll() {
		imageCache.clear();
	}

	public Drawable loadApp(Context context, final String packageName) {
		Drawable image = null;
		// final String path = fileBean.getFilePath();
		// final String fileEnds = fileBean.getFileEnds();
		if (imageCache.containsKey(packageName))// 从列表中获取数据
		{
			SoftReference<Drawable> softReference = imageCache.get(packageName);
			if (softReference != null) {
				image = softReference.get();
				if (image != null) {
					return image;
				}
			}
			imageCache.remove(packageName);
		}
		try {
			image = context.getPackageManager().getApplicationIcon(packageName);
			imageCache.put(packageName, new SoftReference<Drawable>(image));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return image;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				try {
					LoadNetIcoEvent event = (LoadNetIcoEvent) msg.obj;
					if (event != null) {
						event.callback.imageLoaded(event.image, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	public Drawable loadNetDrawable(final String url,
			final ImageCallback callback) {
		Drawable image = null;
		if (imageCache.containsKey(url))// 从列表中获取数据
		{
			SoftReference<Drawable> softReference = imageCache.get(url);
			if (softReference != null) {
				image = softReference.get();
				if (image != null) {
					return image;
				}
			}
			imageCache.remove(url);
		}
		image = LoadLocalImg(url);
		if (image == null) {
			LoadNetIcoEvent event = new LoadNetIcoEvent();
			event.path = url;
			event.callback = callback;
			addEvent(event);
			return null;
		} else {
			return image;
		}

	}

	private Drawable LoadLocalImg(String path) {
		Bitmap tempbitmap = Getphontnames(path);
		if (tempbitmap != null) {
			BitmapDrawable image = new BitmapDrawable(tempbitmap);
			if (image != null) {
				SoftReference<Drawable> softReferece = new SoftReference<Drawable>(
						image);
				imageCache.put(path, softReferece);
				return image;
			}
		}
		return null;
	}

	private boolean isrun = true;
	private Vector<LoadNetIcoEvent> vec = new Vector<LoadNetIcoEvent>();

	public void cancel() {
		try {
			// this.isrun = false;
			synchronized (vec) {
				if (vec.size() != 0) {
					vec.removeAllElements();
				}
			}
		} catch (Exception e) {
			Log.v("event", "cancel ex-" + e.toString());
		}
	}

	public void addEvent(LoadNetIcoEvent event) {
		synchronized (vec) {
			vec.add(event);
			vec.notify();
			Log.v("event", "add event unblock " + event.getClass().getName());
		}
	}

	public void run() {
		try {
			while (isrun) {
				synchronized (vec) {
					if (vec.size() <= 0) {
						vec.wait();
						continue;
					}
				}
				if (vec.size() > 0) {
					LoadNetIcoEvent event = vec.remove(0);
					Log.v("event", "[exce start]<" + event.getClass().getName()
							+ ">");
					event.ok();
					Log.v("event", "[exce end]<" + event.getClass().getName()
							+ ">");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.v("event", "run " + e.toString());
		}
	}

	public Bitmap Getphontnames(String url) {
		Bitmap imagebitmap = null;
		if (url == null)
			return null;
		int pos = url.lastIndexOf("/");
		String filename = url.substring(pos + 1);
		String filepath = Environment.getExternalStorageDirectory().toString()
				+ "/boxcontent/";
		String path = filepath + filename;
		File file1 = new File(path);
		if (file1.exists()) {
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = true;
			// Bitmap imagebitmap = BitmapFactory.decodeFile(path);//
			// BitmapFactory.decodeStream(fis,options);
			// return imagebitmap;
			// --------------------------------------------------------------------------
			BitmapFactory.Options bfOptions = new BitmapFactory.Options();
			bfOptions.inDither = false;
			bfOptions.inPurgeable = true;
			bfOptions.inInputShareable = true;
			bfOptions.inTempStorage = new byte[32 * 1024];
			FileInputStream fs = null;
			try {
				fs = new FileInputStream(file1);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				if (fs != null)
					imagebitmap = BitmapFactory.decodeFileDescriptor(
							fs.getFD(), null, bfOptions);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fs != null) {
					try {
						fs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return imagebitmap;
		} else {
			return null;
		}
	}

	Bitmap drawableToBitmap(Drawable drawable) // drawable 转换?bitmap
												// /mnt/sdcard/easy/img/android1.jpg
	{
		int width = drawable.getIntrinsicWidth(); // ?drawable 的长?
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // ?drawable 的颜色格?
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应
		// bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画?
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // ?drawable 内容画到画布?
		return bitmap;
	}

	public class LoadNetIcoEvent {
		// public String path, fileEnds;
		public String path;
		public ImageCallback callback;
		public Drawable image = null;

		public void ok() {
			Message msg = new Message();
			msg.what = 0;
			if (imageCache.containsKey(path))// 从列表中获取数据
			{
				SoftReference<Drawable> softReference = imageCache.get(path);
				if (softReference != null) {
					image = softReference.get();
					if (image != null) {
						handler.sendMessage(handler.obtainMessage(0, this));
						return;
					}
				}
			}
			Bitmap tempbitmap = Getphontnames(path);
			if (tempbitmap == null) {
				int pos = path.lastIndexOf("/");
				String filename = path.substring(pos + 1);
				String data = HttpUtil.GetPhoto5(path, filename);//
				tempbitmap = Getphontnames(path);
			}

			image = new BitmapDrawable(tempbitmap);
			if (image != null) {
				SoftReference<Drawable> softReferece = new SoftReference<Drawable>(
						image);
				imageCache.put(path, softReferece);
				msg.obj = this;
				handler.sendMessage(msg);
			}
		}

	}
}
