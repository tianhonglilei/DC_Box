package com.example.zzq.bean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import com.zhang.box.LoadingActivity;

/**
 * 
 * @author Administrator 获取系统数据
 * 
 */

public class SysData {

	public class activity {
		// id =0 顶部图片 1 我要开拓
	}

	public static String imei = null;
	public static String appName = "";
	public static LoadingActivity loadmain;
	public static String leftNo;
	public static int leftStu = -1;
	public static int leftNum = -1;
	public static String rightNo;
	public static int rightStu = -1;
	public static int rightNum = -1;
	public static int width = 0;
	public static int height = 0;

	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	public static int upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		// public static void upZipFile() throws Exception{
		ZipFile zfile = new ZipFile(zipFile);
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				String dirstr = folderPath + ze.getName();
				// dirstr.trim();
				dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
				File f = new File(dirstr);
				f.mkdir();
				continue;
			}
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(folderPath, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zfile.close();
		return 0;
	}

	public static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		String lastDir = baseDir;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				lastDir += (dirs[i] + "/");
				File dir = new File(lastDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}
			File ret = new File(lastDir, dirs[dirs.length - 1]);
			return ret;
		} else {

			return new File(baseDir, absFileName);

		}

	}

}
