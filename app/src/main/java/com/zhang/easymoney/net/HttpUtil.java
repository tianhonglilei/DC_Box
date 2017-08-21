package com.zhang.easymoney.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
 

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

 
 


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


/**
 * 缂傚偊鎷烽柨鐔烘櫕閿熸枻鎷烽柨鐔绘閿熻姤鎮堕敓濮愬劵閹峰嚖鎷烽敓濮愬?ч幏锟???
 * @author zhou
 *
 */
public class HttpUtil {

	/**
	 * get 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽柨鐔告灮閹凤拷
	 * @param context
	 * @param url
	 * @return
	 */
	
 
	public static String requestByGet(Context context, String url) {
		if(!checkNet(context)) {
			return null;
		}
		String result = null;
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = request(context, request);		 
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				 
			}
		} catch (Exception ex) {
			if(ex instanceof IOException){
//				if(context instanceof BaseActivity){
//					((BaseActivity)context).onException(ex);
//				}
			}
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * post 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽柨鐔告灮閹凤拷
	 * @param context
	 * @param url
	 * @return
	 */
	public static String requestByPost(Context context, String url, ArrayList<NameValuePair> params) {
		if(!checkNet(context)) {
			return null;
		}
		String result = null;
		HttpPost request = new HttpPost(url);
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			 
			HttpResponse response = request(context, request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			 
			}
		} catch (Exception ex) {
			try{
				if(ex instanceof IOException){
//					if(context instanceof BaseActivity){
//						((BaseActivity)context).onException(ex);
//						
////					} else if(context instanceof MapLuXianActivity){
////						((MapLuXianActivity)context).onException(ex);
////						
////					} else if(context instanceof RockActionActivity){
////						((RockActionActivity)context).onException(ex);
////						
////					} else if(context instanceof RockDiscountActivity){
////						((RockDiscountActivity)context).onException(ex);
////						
//					}
				}
			}catch(Exception ex1)
			{
				ex1.printStackTrace();
			}
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * get 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽柨鐔告灮閹凤拷
	 * @param context
	 * @param url
	 * @return
	 */
	public static String requestByPost(Context context, String url, 
			ArrayList<NameValuePair> params, HashMap<String, byte[]> dataMap) {
		if(!checkNet(context)) {
			return null;
		}
		String result = null;
		HttpPost request = new HttpPost(url);
		try {
			MultipartEntity entity = new MultipartEntity();
			for(int i = 0; i < params.size(); i++) {
				NameValuePair param = params.get(i);
				entity.addPart(param.getName(),  new StringBody(param.getValue(), Charset.forName(HTTP.UTF_8)));
			}
			for(Entry<String, byte[]> entry : dataMap.entrySet()) {
				if(entry.getValue() != null) {
					String name = entry.getKey();
					name = name.substring(0, name.indexOf("."));
					entity.addPart(name, new ByteArrayBody(entry.getValue(), entry.getKey()));
				}
			}
			request.setEntity(entity);
			 
			HttpResponse response = request(context, request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			 
			}
		} catch (Exception ex) {
			if(ex instanceof IOException){
//				if(context instanceof BaseActivity){
//					((BaseActivity)context).onException(ex);
//				}
			}
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 闁跨喐鏋婚幏鍑ゆ嫹閿熷鍎婚幏鐑芥晸閼恒儻鎷烽敓浠嬫晸閺傘倖瀚归柨鐔告灮閹凤拷
	 * @param params
	 * @return
	 */
	private static String getParamsString(ArrayList<NameValuePair> params) {
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < params.size(); i++) {
			NameValuePair pair = params.get(i);
			result.append("---");
			result.append(pair.getName());
			result.append(":");
			result.append(pair.getValue());
		}
		return result.toString();
	}
	
	/**
	 * 婵炶揪鎷烽柨婵撴嫹闁跨喐鏋婚幏鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹瑜忛敓鏂ゆ嫹
	 * @param context
	 * @return
	 */
	private static HttpResponse request(Context context, HttpRequestBase request) throws ClientProtocolException, IOException {
		HttpResponse response = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		if (isCMWAP(context)) {
			try{
				HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
				response = httpClient.execute(request);
			}catch(Exception ex)
			{
				
			}
		} else {
			response = httpClient.execute(request);
		}
		return response;
	}
	
	/**
	 * 闁跨喐鏋婚幏鐑芥晸閻ｅ矉鎷烽敓浠嬫晸閺傘倖瀚归柨鐔凤拷鐕傛嫹鐟欏嫸鎷烽敓锟??
	 * @param context
	 * @return
	 */
	private static boolean isCMWAP(Context context) {
		boolean isCMWAP = false;
		try{
			ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = con.getActiveNetworkInfo();
			if (networkInfo != null && "WIFI".equals(networkInfo.getTypeName().toUpperCase())) {
				return isCMWAP;
			} else {
				Cursor cursor = context.getContentResolver().query(
						Uri.parse("content://telephony/carriers/preferapn"),
						new String[] { "apn" }, null, null, null);
				cursor.moveToFirst();
				if (cursor.isAfterLast()) {
					isCMWAP = false;
				}
				try {
					if("cmwap".equals(cursor.getString(0)) || "uniwap".equals(cursor.getString(0))) {
						isCMWAP = true;
					} else {
						isCMWAP = false;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}
				return isCMWAP;
			}
		}catch(Exception ex)
		{
			
		}
		return isCMWAP;
	}
	
	/**
	 * 闁跨喐鏋婚幏鐑芥晸閻ｅ矉鎷烽敓浠嬫晸閺傘倖瀚归柨鐔凤拷鐕傛嫹鐟欏嫸鎷烽敓锟??
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = con.getActiveNetworkInfo();
		if (networkInfo != null && "WIFI".equals(networkInfo.getTypeName().toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 闁跨喐鏋婚幏鐑芥晸閻ｅ矉鎷烽敓浠嬫晸娴犲鎷烽敓锟???閿熸枻鎷烽敓鏂ゆ嫹閿熷鍊栭敓鏂ゆ嫹
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {
		try
		{
			ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = con.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isAvailable()) {
				return false;
			} else {
				return true;
			}
		}catch(Exception ex)
		{
			return false;
		}
	}
	
	
	
	public   static String RequestGetData( String url,List<NameValuePair> nameValuePairs)
	{
 
		String returnStr = null;
		HttpClient httpclient = new DefaultHttpClient(); 
		HttpPost httppost = new HttpPost(url);
	    try {
	    	
	        // Add your data
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        int x = response.getStatusLine().getStatusCode();
			if (response.getStatusLine().getStatusCode() ==HttpStatus.SC_OK) {
				
				returnStr=EntityUtils.toString(response
								.getEntity());
 
					}
	    } catch (Exception e) {
		       
		    	e.printStackTrace();

		    	String str = "";
		    	
	    }
	   return returnStr;
	}
	public static String RequestGetData(String userurl ,String typedata,String param)
	{
		
		String returnStr = null;
		HttpClient httpclient = new DefaultHttpClient(); 
		HttpPost httppost = new HttpPost(userurl);
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);	       
	        nameValuePairs.add(new BasicNameValuePair("method", typedata));// 
	        nameValuePairs.add(new BasicNameValuePair("param", param));// 
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        int x = response.getStatusLine().getStatusCode();
			if (response.getStatusLine().getStatusCode() ==HttpStatus.SC_OK) {
				
				returnStr=EntityUtils.toString(response
								.getEntity());
					}
	    } catch (Exception e) {
	     
		    	e.printStackTrace();

		    	String str = "";
		    	
	    }
	   return returnStr;
	}
	
	public   static String RequestGetData2(String userurl, String typedata,String kepdata)
	{
		
		String returnStr = null;
		HttpClient httpclient = new DefaultHttpClient(); 
		HttpPost httppost = new HttpPost(userurl);
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        
	        nameValuePairs.add(new BasicNameValuePair("type", typedata));//闁跨喐鏋婚幏鐑芥晸閽樺鎷烽妷顖涘閿熸枻鎷烽敓锟???
	        nameValuePairs.add(new BasicNameValuePair("key", kepdata));//闁跨喐鏋婚幏鐑芥晸閽樺鎷烽妷顖涘閿熸枻鎷烽敓锟???
	       
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	       
	       
	        int x = response.getStatusLine().getStatusCode();
			if (response.getStatusLine().getStatusCode() ==HttpStatus.SC_OK) {
				
				returnStr=EntityUtils.toString(response
								.getEntity());
				 
					}
	    } catch (Exception e) {
	 
		    	e.printStackTrace();

		    	String str = "";
		    	
	    }
	   return returnStr;
	}
	
	public static String RequestGetData2(String aurl) {
		String result = null;
		String url = aurl;
		HttpGet request = new HttpGet(url);
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				
			}
		} catch (Exception ex) {
			if(ex instanceof IOException){
			//	
			}
		}
		return result;
	}
	
	
	public static String RequestGetData1(String data) {
		String result = null;
		String url = "http://cms.shanlink.com/shanpai3.0/index.php?r=sys/SendMail_ResetPW&email=" + data;
		HttpGet request = new HttpGet(url);
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				
			}
		} catch (Exception ex) {
			if(ex instanceof IOException){
			//	
			}
		}
		return result;
	}
	
	public static String UpDataimage(String uid,String name, String path,String url)
	{
		String returnStr = null;
		boolean   photo = true;
		
		HttpClient httpclient = new DefaultHttpClient(); 
		HttpPost httppost = new HttpPost(url);
	    try {
	    	MultipartEntity mpEntity  = new MultipartEntity();
	        // Add your data
 	    	
	    	File file = new File( path ); 
	    	if (!file.exists()) {
	    		photo = false;
		     }
 	    	ContentBody TypeMessage = new StringBody(uid);
	    	ContentBody TypeMessage2 = new StringBody(name,Charset.forName(HTTP.UTF_8));

	    	mpEntity.addPart("uid",TypeMessage);//婵炴埊鎷烽柨鐔诲Г鐎靛ジ鏁撻弬銈嗗闁跨喐鏋婚幏宄懊归敓浠嬫晸閿燂拷    
	    	mpEntity.addPart("name",TypeMessage2);//婵炴埊鎷烽柨鐔诲Г鐎靛ジ鏁撻弬銈嗗闁跨喐鏋婚幏宄懊归敓浠嬫晸閿燂拷       

	        
	        if(photo)
	        {
	        	FileBody  cbFile;
		    	cbFile = new FileBody( file);
	        	mpEntity.addPart("avatar", cbFile);//婵炴埊鎷烽柨鐔诲Г鐎靛ジ鏁撻懞銉嫹閿熸枻鎷烽敓鏂ゆ嫹闁跨喐鏋婚幏锟?
	        }
	       
	        httppost.setEntity( mpEntity ); 
	        HttpResponse response = httpclient.execute(httppost);
	        int x = response.getStatusLine().getStatusCode();
			if (response.getStatusLine().getStatusCode() ==HttpStatus.SC_OK) {
				
				returnStr=EntityUtils.toString(response.getEntity());
			
			}
	    } catch (Exception e) {
		       
	 
		    	e.printStackTrace();

		    	String str = "";
		    	
	    }
	    return returnStr;
	}
	
	public static String UpDataphotoimage(String photourl,String imagepath)
	{
		String returnStr = null;
		boolean   photo = true;
		
		HttpClient httpclient = new DefaultHttpClient(); 
		HttpPost httppost = new HttpPost(photourl);
	    try {
	    	MultipartEntity mpEntity  = new MultipartEntity();
	        // Add your data
	    	String files = imagepath;
	    	
	    	File file = new File( files ); 
	    	if (!file.exists()) {
	    		photo = false;
		     }

	    	String tempdata = "{\"command\":\"0001\",\"type\":\"advert\",\"rettype\":\"bailinshanpai\",\"api_key\":\"13b7d8e0c3456e0b3aa97714d3b7acb6\",\"platform\":\"iso.5.1\",\"img_url\":\"0\"}";
	    	ContentBody jparamsMessage = new StringBody(tempdata);
 
	    	mpEntity.addPart("jparams",jparamsMessage);     
 
	        if(photo)
	        {
	        	
	        	FileBody  cbFile;
		    		 cbFile = new FileBody( file);

	        	 mpEntity.addPart("img_binary", cbFile);
	        }
	       
	        

	       
	        httppost.setEntity( mpEntity ); 
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	       

	       
	        int x = response.getStatusLine().getStatusCode();
			if (response.getStatusLine().getStatusCode() ==HttpStatus.SC_OK) {
				
				returnStr=EntityUtils.toString(response
								.getEntity());
			 
					}
	    } catch (Exception e) {
		 
		    	e.printStackTrace();

		    	String str = "";
		    	
	    }
	    return returnStr;
	}
	
	 
 
	
	public static String GetPhoto5(String aurl,String alog)
	{
    			String returnStr = null;
    			try {
    				 
    				URL url = new URL(aurl);
        			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        			conn.setConnectTimeout(5 * 1000);
        			conn.setRequestMethod("GET");
        			int m = conn.getResponseCode();
        			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        				{
        					 
        					SaveListPic5(conn.getInputStream(),alog);
        					returnStr = "ok";
        				}
        			}
    				 
    				} catch (Exception e) {
    				 
//    				throw new RuntimeException(e);
    				 
    				} 
    			return returnStr;
	}
	
	
	public static void SaveListPic5(InputStream inputStream,String aName) {

 		String path = Environment.getExternalStorageDirectory().toString()
				+ "/boxcontent/";
 		  File destDir = new File(path);
 		  if (!destDir.exists()) {
 		   destDir.mkdirs();
 		  }
		File f = new File(path, aName);

		try {
			OutputStream out = new FileOutputStream(f);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
		} catch (Exception e) {
			
			e.printStackTrace();


		}
	}
	
	
	

	public static String  GetBox(String aurl,String alog)
	{
    			String returnStr = null;
    			try {
    				 
    				URL url = new URL(aurl);
        			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        			conn.setConnectTimeout(5 * 1000);
        			conn.setRequestMethod("GET");
        			int m = conn.getResponseCode();
        			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        				{
        					 
        					SaveBox(conn.getInputStream(),alog);
        					returnStr = "ok";
        				}
        			}
    				 
    				} catch (Exception e) {
    				 
    					e.printStackTrace();
   				        throw new RuntimeException(e);
    				 
    				} 
    			return returnStr;
	}
	
	
	public static void SaveBox(InputStream inputStream,String aName) {

 		String path = Environment.getExternalStorageDirectory().toString()
				+ "/boxcontent/";
 		  File destDir = new File(path);
 		  if (!destDir.exists()) {
 		   destDir.mkdirs();
 		  }
		File f = new File(path, aName);

		try {
			OutputStream out = new FileOutputStream(f);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
		} catch (Exception e) {
			
			e.printStackTrace();


		}
	}
	
 
	
}
