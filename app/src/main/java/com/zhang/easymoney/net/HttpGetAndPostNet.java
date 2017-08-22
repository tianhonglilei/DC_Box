package com.zhang.easymoney.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class HttpGetAndPostNet {
	private static PosetnetMonClick mObject;
	private static String mTdata;
	private static String mKdata;
	private static String mPdata;
	private static int mNum;
	private static byte[] imagebody;
	private static String mData;
	private static String murl;
	private static Thread myThread;
	private static boolean mlog = false;

	/**
	 *
	 * 
	 * @author zhouxl
	 */
	public static void HttpGetdata(String aUrl,
			PosetnetMonClick aPosetnetMonClick)// , MonClick aMonClick, int
												// aNUm)
	{
		mObject = aPosetnetMonClick;
		murl = aUrl;
		String path = Environment.getExternalStorageDirectory() + "/bailingooh";
		String name = Environment.getExternalStorageDirectory()
				+ "/bailingooh/Box.apk";

		File file = new File(path);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		File dir = new File(name);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}
		} else {
			dir.delete();
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}
		}
		mlog = false;
		new Thread(new Runnable() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();

					HttpGet get = new HttpGet(murl);
					HttpResponse response = client.execute(get);

					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						long length = entity.getContentLength();
						mObject.HandHttpStartnum(length);
						InputStream is = entity.getContent();
						String s = null;
						if (is != null) {
							// ByteArrayOutputStream baos = new
							// ByteArrayOutputStream();
							byte[] buf = new byte[128];
							int ch = -1;
							int count = 0;// String path =
											// Environment.getExternalStorageDirectory()
											// + "/shanbopic";
							String path = Environment
									.getExternalStorageDirectory()
									+ "/bailingooh/Box.apk";
							File ApkFile = new File(path);

							FileOutputStream fos = new FileOutputStream(ApkFile);

							while ((ch = is.read(buf)) != -1) {
								count += ch;

								fos.write(buf, 0, ch);

								mData = new String(buf);
								//mObject.HandHttpEndnum((int) ((count * 100) / length));
								if (mlog) {
									return;
								}
							}
							fos.close();
							mObject.Postfinish(true);

							// s = new String(baos.toByteArray());
						}
					}

				} catch (Exception e) {
					// e.printStackTrace();
					mObject.Postfinish(false);
				}
			}
		}).start();

	}

	public static void stop() {
		mlog = true;
		// if(myThread != null)
		// {
		// aaa =1;
		// myThread.stop();
		// myThread = null;
		// }
	}

	public static void Httppostdata(String aUrl, String aKdata, String aTdata,
			PosetnetMonClick aMonClick, int aNUm) {// t= get_wifi_nearby_lbs& k
													// =
													// 129ab7de655ad4aa0f0f9127c82f64fe&p={" latitude":"***"," longitude":锟斤拷***锟斤拷
													// , " distance":*** }
		mObject = aMonClick;
		mTdata = aTdata;
		mKdata = aKdata;
		murl = aUrl;
		mNum = aNUm;
		new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost httpRequest = new HttpPost(murl);
					List<NameValuePair> paramslist = new ArrayList<NameValuePair>();

					paramslist.add(new BasicNameValuePair("log1", mKdata)); // post锟斤拷锟斤拷锟接??
					paramslist.add(new BasicNameValuePair("log2", mTdata)); // post锟斤拷锟斤拷锟接??

					HttpEntity httpentity = new UrlEncodedFormEntity(
							paramslist, "UTF_8");
					httpRequest.setEntity(httpentity);
					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse httpResponse = httpclient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						HttpEntity entity = httpResponse.getEntity();
						long length = entity.getContentLength();
						InputStream is = entity.getContent();
						String s = null;
						if (is != null) {
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							byte[] buf = new byte[128];
							int ch = -1;
							int count = 0;
							while ((ch = is.read(buf)) != -1) {
								baos.write(buf, 0, ch);
								count += ch;

								mData = new String(buf);
								Thread.sleep(100);
							}
							s = new String(baos.toByteArray());
						}
					}
				} catch (Exception e) {
					//
				}
			}
		}).start();
	}

}
