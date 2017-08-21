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
	 * ç½???¨å?
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
				// °´ÕÕÖ¸¶¨µÄÂ·¾¶´´½¨ÎÄ¼þ¼Ð
				file.mkdirs();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		File dir = new File(name);
		if (!dir.exists()) {
			try {
				// ÔÚÖ¸¶¨µÄÎÄ¼þ¼ÐÖÐ´´½¨ÎÄ¼þ
				dir.createNewFile();
			} catch (Exception e) {
			}
		} else {
			dir.delete();
			try {
				// ÔÚÖ¸¶¨µÄÎÄ¼þ¼ÐÖÐ´´½¨ÎÄ¼þ
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

								// ï¿½ì²½ï¿½ï¿½ï¿?? ï¿½Í½ï¿½ï¿??
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
													// 129ab7de655ad4aa0f0f9127c82f64fe&p={" latitude":"***"," longitude":ï¿½ï¿½***ï¿½ï¿½
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
					// ï¿½ï¿½ï¿½Ò?¿½ï¿½ï¿½ÝµÄ²ï¿½ï¿½ï¿??
					paramslist.add(new BasicNameValuePair("log1", mKdata)); // postï¿½ï¿½ï¿½ï¿½ï¿½Ó??
					paramslist.add(new BasicNameValuePair("log2", mTdata)); // postï¿½ï¿½ï¿½ï¿½ï¿½Ó??

					// ï¿½ï¿½ï¿½ï¿½ï¿½Ö·ï¿½
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

								// ï¿½ï¿½ï¿½Ö?¿½ï¿½Ïµï¿½Ç¸ï¿½ï¿½ÐµÄ³ï¿½ï¿½È£ï¿½ï¿½ï¿½ï¿½ï¿?ublishprogressï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Â½ï¿½ï¿??
								mData = new String(buf);
								// publishProgress((int)((count/(float)100)*100));
								// ï¿½ï¿½Ó½ï¿½ï¿½ ï¿½ï¿½ ï¿½ï¿½Ý´ï¿½ï¿½ï¿??

								// ï¿½ï¿½ï¿½ß³ï¿½ï¿½ï¿½ï¿½ï¿½100ms
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
