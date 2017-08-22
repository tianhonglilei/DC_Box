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

/** �ļ����� */
public class FileUtils {

	public static final String SDCARD = StringUtils.getSDPath();

	/** ��SD���ϴ����ļ� */
	public static File creatSDFile(String fileName) throws IOException {
		File file = new File(SDCARD + fileName);
		file.createNewFile();
		return file;
	}

	/** ��SD���ϴ���Ŀ¼ */
	public static File creatSDDir(String dirName) {
		File dir = new File(SDCARD + dirName);
		dir.mkdir();
		return dir;
	}

	/** �ж�SD���ϵ��ļ����Ƿ���� */
	public static boolean isFileExist(String fileName) {
		File file = new File(SDCARD + fileName);
		return file.exists();
	}

	/**
	 * ����ʡ�ڴ�ķ�ʽ��ȡ������Դ��ͼƬ(��Сԭ���Ķ���֮һ)
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
		opt.inSampleSize = 2;// ��Сԭ����1/2
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * ����ʡ�ڴ�ķ�ʽ��ȡ������Դ��ͼƬ
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
		// opt.inSampleSize = 2;// ��Сԭ����1/2
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/** �ж��ļ��Ƿ���� */
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
	 * �����ļ�
	 * 
	 * @param path
	 *            �ļ�·��
	 * @param filename
	 *            �ļ���
	 * @return
	 */
	public static boolean createFile(String path, String filename) {
		File file = new File(path);
		Boolean createFlg = false;
		// ����ָ����·�������ļ���
		if (!file.exists()) {
			file.mkdirs();
		}
		String local_file = file.getAbsolutePath() + "/" + filename;
		file = new File(local_file);
		if (!file.exists()) {
			try {
				// �������ļ�
				createFlg = file.createNewFile();
			} catch (Exception e) {
			}
		}
		return createFlg;
	}

	/** SD���Ƿ�׼�������� */
	public static boolean isSDCardPresent() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/** SDcard�Ƿ��д */
	public static boolean isSdCardWrittenable() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/** ��ɾ��ָ��·���ļ��� */
	public static void deleteFile(String path) {
		File file = new File(path);
		// �ж��ļ��Ƿ����
		if (file.exists()) {
			// �ж��Ƿ����ļ�
			if (file.isFile()) {
				// ɾ��
				file.delete();
			}
		}
	}

	/** ��Դ���� */
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

	/** ����ļ��� �Լ��ļ����ڵ����е��ļ� */
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

	/** ��eclipse��assets��ԴĿ¼ini�ļ����Ƶ�ָ��Ŀ¼�� */
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

	/** ����ԴĿ¼�µ�ͼƬ���浽SDcard�� ��ҪĿ�� ������ͼ�� */
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

	/** ���ַ���д��ָ��·���е��ı���ȥ */
	public static void writeFile(String str) {
		String filePath = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (hasSDCard) { // SD����Ŀ¼��hello.text
			filePath = Environment.getExternalStorageDirectory().toString()
					+ File.separator + "jiqibianma.txt";
		} else
			// ϵͳ���ػ����Ŀ¼��hello.text
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
	 * ��ȡ�ı��ļ��е�����
	 * 
	 * @param strFilePath
	 *            ·��
	 * @return
	 */
	public static String ReadTxtFile(String strFilePath, Context context) {
		String path = strFilePath;
		String content = ""; // �ļ������ַ���
		// ���ļ�
		File file = new File(path);
		// ���path�Ǵ��ݹ����Ĳ�����������һ����Ŀ¼���ж�
		if (file.isDirectory()) {
			ToastTools.showShort(context, "��·���������������,�뵽���ó����������!");
		} else {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(
							instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					// ���ж�ȡ
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
