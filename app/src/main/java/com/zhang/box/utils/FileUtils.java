package com.zhang.box.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/** 文件管理 */
public class FileUtils {

	public static final String SDCARD = StringUtils.getSDPath();

	/** 在SD卡上创建文件 */
	public static File creatSDFile(String fileName) throws IOException {
		File file = new File(SDCARD + fileName);
		file.createNewFile();
		return file;
	}

	/** 在SD卡上创建目录 */
	public static File creatSDDir(String dirName) {
		File dir = new File(SDCARD + dirName);
		dir.mkdir();
		return dir;
	}

	/** 判断SD卡上的文件夹是否存在 */
	public static boolean isFileExist(String fileName) {
		File file = new File(SDCARD + fileName);
		return file.exists();
	}

	/**
	 * 以最省内存的方式读取本地资源的图片(缩小原来的二分之一)
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = 2;// 缩小原来的1/2
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap2(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// opt.inSampleSize = 2;// 缩小原来的1/2
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/** 判断文件是否存在 */
	public static Boolean exist(String path) {
		File file = new File(path);
		Boolean exist = false;
		try {
			exist = file.exists();
			file = null;
		} catch (Exception e) {
			// Log.w("FileUtil", "file exists(" + path + ") error!");
		}
		return exist;
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @param filename
	 *            文件名
	 * @return
	 */
	public static boolean createFile(String path, String filename) {
		File file = new File(path);
		Boolean createFlg = false;
		// 按照指定的路径创建文件夹
		if (!file.exists()) {
			file.mkdirs();
		}
		String local_file = file.getAbsolutePath() + "/" + filename;
		file = new File(local_file);
		if (!file.exists()) {
			try {
				// 创建新文件
				createFlg = file.createNewFile();
			} catch (Exception e) {
			}
		}
		return createFlg;
	}

	/** SD卡是否准备就绪　 */
	public static boolean isSDCardPresent() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/** SDcard是否可写 */
	public static boolean isSdCardWrittenable() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/** 　删除指定路径文件　 */
	public static void deleteFile(String path) {
		File file = new File(path);
		// 判断文件是否存在
		if (file.exists()) {
			// 判断是否是文件
			if (file.isFile()) {
				// 删除
				file.delete();
			}
		}
	}

	/** 资源清理 */
	public static void cache(String path, byte[] data) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			os.write(data);
		} catch (IOException e) {
			// Log.w("FileUtil", "file cache(" + path + ") error!");
		} finally {
			if (null != os)
				os.close();
			os = null;
		}
	}

	/** 清除文件夹 以及文件夹内的所有的文件 */
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	/** 将eclipse里assets资源目录ini文件复制到指定目录下 */
	public static void assetsDataToSD(Context context, String fileName) {
		InputStream myInput;
		try {
			OutputStream myOutput = new FileOutputStream(fileName);
			myInput = context.getAssets().open("config.ini");
			byte[] buffer = new byte[1024];
			int length = myInput.read(buffer);
			while (length > 0) {
				myOutput.write(buffer, 0, length);
				length = myInput.read(buffer);
			}

			myOutput.flush();
			myInput.close();
			myOutput.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 将资源目录下的图片保存到SDcard内 主要目的 分享功能图标 */
	public static void saveBitmap(Bitmap bitmap, String picture_path_name) {

		File file = new File(picture_path_name);
		if (!file.exists()) {
			FileOutputStream out;
			try {
				out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 将字符串写入指定路径中的文本中去 */
	public static void writeFile(String str) {
		String filePath = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (hasSDCard) { // SD卡根目录的hello.text
			filePath = Environment.getExternalStorageDirectory().toString()
					+ File.separator + "jiqibianma.txt";
		} else
			// 系统下载缓存根目录的hello.text
			filePath = Environment.getDownloadCacheDirectory().toString()
					+ File.separator + "jiqibianma.txt";

		try {
			File file = new File(filePath);
			if (!file.exists()) {
				File dir = new File(file.getParent());
				dir.mkdirs();
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(str.getBytes());
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本文件中的内容
	 * 
	 * @param strFilePath
	 *            路径
	 * @return
	 */
	public static String ReadTxtFile(String strFilePath, Context context) {
		String path = strFilePath;
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(path);
		// 如果path是传递过来的参数，可以做一个非目录的判断
		if (file.isDirectory()) {
			ToastTools.showShort(context, "该路径不存在相关内容,请到配置程序进行配置!");
		} else {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(
							instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					// 分行读取
					while ((line = buffreader.readLine()) != null) {
						content += line + "\n";
					}
					instream.close();
				}
			} catch (java.io.FileNotFoundException e) {
				Log.d("TestFile", "The File doesn't not exist.");
			} catch (IOException e) {
				Log.d("TestFile", e.getMessage());
			}
		}
		return content;
	}
}
