package com.zhang.box;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.avm.serialport_142.MainHandler;
import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.catchexecpton.HttpParameters;
import com.zhang.box.catchexecpton.LogCollector;
import com.zhang.box.constants.Constants;
import com.zhang.box.services.LoadingService;
import com.zhang.box.utils.ActivitySkipUtil;
import com.zhang.box.utils.FileUtils;
import com.zhang.box.utils.ImageLoaderUtils;
import com.zhang.box.utils.SpUtil;
import com.zhang.box.utils.ToastTools;
import com.zhang.easymoney.net.HttpUtil;

/**
 * 数据准备页面
 *
 * @author wang
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoadingActivity extends Activity {

    private NetTask mNetTask;
    private List<String> proList = new ArrayList<String>();
    NetImageLoader mLogoImage;
    private BooksDB mBooksDB;
    private TextView tv_version;
    private String versionCode;
    private Context mContext;
    int index;
    // 要切换的照片，放在drawable文件夹下
    int[] images = {R.drawable.open1, R.drawable.open2, R.drawable.open3,
            R.drawable.open4, R.drawable.open5,};
    private ImageView image;
    private ImageView image2;
    // Message传递标志
    int SIGN = 17;
    // 照片索引
    int num = 0;
    public static final String IS_APP_FIRST_OPEN = "is_app_first_open";
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private int width;
    private int height;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loading);
        mContext = this;
        WindowManager wm = getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        SysData.width = width;
        height = wm.getDefaultDisplay().getHeight();
        SysData.height = height;
        image = (ImageView) findViewById(R.id.splsh_iv_one);
        image2 = (ImageView) findViewById(R.id.splsh_iv_two);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == SIGN) {
                    bitmap = ImageLoaderUtils.loadHugeBitmapFromDrawable(
                            getResources(), images[num++], SysData.height,
                            SysData.width);
                    image.setImageBitmap(bitmap);
                    if (num >= images.length) {
                        num = 0;
                    }
                }
            }
        };

        bitmap2 = ImageLoaderUtils.loadHugeBitmapFromDrawable(getResources(),
                R.drawable.opentext, SysData.height, SysData.width);
        image2.setImageBitmap(bitmap2);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = SIGN;
                handler.sendMessage(msg);
            }
        }, 1000, 500);
        PackageInfo pinfo;
        try {
            //版本号
            pinfo = getPackageManager().getPackageInfo("com.zhang.box",
                    PackageManager.GET_CONFIGURATIONS);
            versionCode = String.valueOf(pinfo.versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("版本号: " + versionCode);
        SysData.imei = MainHandler.getMachNo();// 获取机器号
        // SysData.imei = "93002566";// 获取机器号
        /** 启动服务 */
        SysData.loadmain = this;
        // mBooksDB.deleteApl(); //删除上传点击应用软件和商品的名字
        mBooksDB = new BooksDB(this);
        mLogoImage = NetImageLoader.GetObject(this);
        LogCollector.upload(true);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ToastTools.showShort(mContext, "数据配置成功!");
                boolean isFirstOpen = SpUtil.getBoolean(
                        getApplicationContext(), IS_APP_FIRST_OPEN, true);
                if (isFirstOpen) {
                    ActivitySkipUtil.skipAnotherActivity(LoadingActivity.this,
                            ConfigActivity.class, true);
                } else {
                    LogCollector.upload(true);
                    SysData.imei = MainHandler.getMachNo();// 获取机器号

                    HttpParameters params = new HttpParameters();
                    params.add("imei", MainHandler.getMachNo());
                    LogCollector.setParams(params);
                    LogCollector.upload(true);
                    upData(0);
                    startService(new Intent(LoadingActivity.this,
                            LoadingService.class));
                }
            }
        }, 11000);
    }

    /**
     * 资源释放
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if (image != null && image.getDrawable() != null) {
        // Bitmap oldBitmap = ((BitmapDrawable) image.getDrawable())
        // .getBitmap();
        // image.setImageDrawable(null);
        // if (oldBitmap != null) {
        // oldBitmap.recycle();
        // oldBitmap = null;
        // }
        // }
        //
        // if (image2 != null && image2.getDrawable() != null) {
        // Bitmap oldBitmap = ((BitmapDrawable) image2.getDrawable())
        // .getBitmap();
        // image2.setImageDrawable(null);
        // if (oldBitmap != null) {
        // oldBitmap.recycle();
        // oldBitmap = null;
        // }
        // }

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        if (bitmap2 != null && !bitmap2.isRecycled()) {
            bitmap2.recycle();
            bitmap2 = null;
        }

        System.gc();
    }

    // TODO 读取数据库数据
    public void read() {
        // TODO 机器编码
        FileUtils.writeFile(SysData.imei);
        UserInfo.proAlllist.clear();
        UserInfo.appMainshowOne.clear();
        UserInfo.appMainshow.clear();
        UserInfo.isLoad = 0;
        // mBooksDB.ReadProAllById();
        UserInfo.proMainlist.clear();
        mBooksDB.ReadProMain();// 90 首页9个
        UserInfo.proAlllist.clear();
        mBooksDB.ReadProAll();
        if (UserInfo.proDrinklist.size() > 0) {
            UserInfo.proDrinklist.clear();
        }
        mBooksDB.ReadDrinkAll();
        int size = UserInfo.proDrinklist.size();

        if (UserInfo.proSnakelist.size() > 0) {
            UserInfo.proSnakelist.clear();
        }
        mBooksDB.ReadSnakeAll();

        if (UserInfo.proInfos.size() > 0) {
            UserInfo.proInfos.clear(); // 营养成分表
        }

        UserInfo.advVideo.clear();
        mBooksDB.ReadAdvVideo();

        UserInfo.jpAllshow.clear();
        mBooksDB.ReadAppJingPing();

        UserInfo.appMainshowOne.clear();
        mBooksDB.ReadAppByMainshowOne();// 应用商城表头的3张图
        ActivitySkipUtil.skipAnotherActivity(LoadingActivity.this,
                GallaryActivity.class, true);
    }

    public void upData(int index) {
        mNetTask = new NetTask();
        mNetTask.execute();
    }

    class NetTask extends AsyncTask<Object, Integer, String> {

        private NetTask() {
        }

        protected String doInBackground(Object... params) {

            String url = "";
            // SysData.sim = DeviceUtils.getSim(mContext);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    10);
            nameValuePairs.add(new BasicNameValuePair("app", SysData.appName));
            Log.e("wh", "SysData.imei==" + SysData.imei);
            nameValuePairs.add(new BasicNameValuePair("imei", SysData.imei));
            nameValuePairs
                    .add(new BasicNameValuePair("click", UserInfo.upname));

            nameValuePairs.add(new BasicNameValuePair("ver", versionCode));

            // TODO 根据机器内容是否第一次 1全部下发 0更新
            String path = Environment.getExternalStorageDirectory().toString()
                    + "/boxcontent/";
            File destDir = new File(path);

            // 判断数据库是否有数据 目前是根据商品类型进行判断
            UserInfo.proAlllist.clear();
            mBooksDB.ReadProAll();
            int proSize = UserInfo.proAlllist.size();

            if (destDir.exists() && proSize > 0) {
                nameValuePairs.add(new BasicNameValuePair("updata", 1 + ""));
            } else {
                nameValuePairs.add(new BasicNameValuePair("updata", 1 + ""));
            }

            // nameValuePairs.add(new BasicNameValuePair("updata", 1 + ""));
            /** 检测是否开门 */
            boolean isOpen = MainHandler.isDoorOpen();
            if (isOpen) {
                nameValuePairs.add(new BasicNameValuePair("door", 1 + ""));
            } else {
                nameValuePairs.add(new BasicNameValuePair("door", 0 + ""));
            }

            /** 检测机器所有的货道和货道的状态 */
            String str = new String();
            for (int i = 1; i < 22; i++) {
                String huodaoInfo = MainHandler.getGoodsInfo(11, i);
                String subHuodaoInfo = huodaoInfo.substring(0, 1);
                str += (i + "|" + subHuodaoInfo + "|");

            }

            nameValuePairs.add(new BasicNameValuePair("mhd", str));
            String jia = SysData.imei + "198217";
            nameValuePairs
                    .add(new BasicNameValuePair("jiami", SysData.md5(jia)));

            url = Constants.LoadActivityUrlLHL;
            Log.e("wh", "LoadingActivity-->nameValuePairs==" + nameValuePairs.toString());
            String json = HttpUtil.RequestGetData(url, nameValuePairs);
            Log.e("wh", "LoadingActivity-->json==" + json);
            return json;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                finish();
            } else {
                int x = 0;
                try {
                    JSONArray jsonObj = new JSONObject(result)
                            .getJSONArray("pro");
                    Log.e("projson",""+jsonObj);
                    for (int i = 0; i < jsonObj.length(); i++) {
                        UserInfo info = new UserInfo();
                        JSONObject tempJson = jsonObj.optJSONObject(i);
                        info.id = Integer.parseInt(tempJson.getString("id"));
                        info.logo = tempJson.getString("logo");
                        info.name = tempJson.getString("name");
                        info.des = tempJson.getString("des");
                        info.price = Integer.parseInt(tempJson
                                .getString("price"));
                        info.max = Integer.parseInt(tempJson.getString("max"));
                        info.mfinish = Integer.parseInt(tempJson
                                .getString("mfinish"));

                        info.zhifubaoprice = Integer.parseInt(tempJson
                                .getString("zhifubao"));
                        info.weixinprice = Integer.parseInt(tempJson
                                .getString("weixin"));

                        info.probig = tempJson.getString("big");
                        info.prohuogui = tempJson.getString("hgid");
                        info.logogray = tempJson.getString("logogray");
                        info.status = Integer.parseInt(tempJson
                                .getString("status"));
                        info.hdid = Integer
                                .parseInt(tempJson.getString("hdid"));
                        info.protype = Integer.parseInt(tempJson
                                .getString("type"));
                        info.promainshow = Integer.parseInt(tempJson
                                .getString("mainshow"));
                        if (i == 0) {
                            if (UserInfo.proAlllist.size() > 0) {
                                UserInfo.proAlllist.clear();
                            }

                            if (UserInfo.proMainlist.size() > 0) {
                                UserInfo.proMainlist.clear();
                            }
                            if (UserInfo.proSnakelist.size() > 0) {
                                UserInfo.proSnakelist.clear();
                            }
                            if (UserInfo.proDrinklist.size() > 0) {
                                UserInfo.proDrinklist.clear();
                            }
                            mBooksDB.deleteTableInfos("prduct");
                        }
                        mBooksDB.insertP(info);
                        proList.add(tempJson.getString("logo"));
                        proList.add(tempJson.getString("big"));
                        proList.add(tempJson.getString("logogray"));
                        x = 1;
                    }

                    JSONArray jsonObjadv = new JSONObject(result)
                            .getJSONArray("adv");
                    for (int i = 0; i < jsonObjadv.length(); i++) {

                        UserInfo info = new UserInfo();
                        JSONObject tempJson = jsonObjadv.optJSONObject(i);
                        info.id = Integer.parseInt(tempJson.getString("id"));
                        info.advimg = tempJson.getString("img");
                        info.video = tempJson.getString("video");
                        info.type = tempJson.getString("type");
                        info.status = Integer.parseInt(tempJson
                                .getString("status"));
                        if (i == 0) {
                            if (UserInfo.advVideo.size() > 0) {
                                UserInfo.advVideo.clear();
                            }
                            mBooksDB.deleteTableInfos("adv");
                        }
                        mBooksDB.insertAdv(info);
                        proList.add(tempJson.getString("img"));
                        proList.add(tempJson.getString("video"));
                        x = 1;
                    }

                    JSONArray jsonObjapp = new JSONObject(result)
                            .getJSONArray("app");
                    for (int i = 0; i < jsonObjapp.length(); i++) {
                        UserInfo info = new UserInfo();
                        JSONObject tempJson = jsonObjapp.optJSONObject(i);

                        info.appid = Integer.parseInt(tempJson.getString("id"));
                        info.applogo = tempJson.getString("logo");
                        info.appname = tempJson.getString("name");
                        info.appprice = Integer.parseInt(tempJson
                                .getString("price"));
                        info.duan = tempJson.getString("duan");
                        info.mainshow = Integer.parseInt(tempJson
                                .getString("mainshow"));

                        info.apkurl = tempJson.getString("apkurl");
                        info.mainimg = tempJson.getString("mainimg");
                        info.sys = tempJson.getString("sys");
                        info.appstatus = Integer.parseInt(tempJson
                                .getString("status"));
                        info.apkBigImg = tempJson.getString("big");
                        info.apptype = tempJson.getString("type");
                        info.buyapp = tempJson.getString("buyapp");
                        UserInfo.desType = info.apptype;
                        if (i == 0) {
                            if (UserInfo.appMainshowOne.size() > 0) {
                                UserInfo.appMainshowOne.clear();
                            }
                            if (UserInfo.appMainshow.size() > 0) {
                                UserInfo.appMainshow.clear();
                            }
                            if (UserInfo.jpAllshow.size() > 0) {
                                UserInfo.jpAllshow.clear();
                            }
                            if (UserInfo.searchApp.size() > 0) {
                                UserInfo.searchApp.clear();
                            }
                            mBooksDB.deleteTableInfos("apps");
                        }

                        mBooksDB.insertApp(info);
                        proList.add(tempJson.getString("logo"));
                        proList.add(tempJson.getString("big"));
                        proList.add(tempJson.getString("mainimg"));
                        x = 1;
                    }

                    JSONArray jsonObjinfo = new JSONObject(result)
                            .getJSONArray("productinfo");
                    for (int i = 0; i < jsonObjinfo.length(); i++) {
                        UserInfo info = new UserInfo();
                        JSONObject tempJson = jsonObjinfo.optJSONObject(i);

                        info.proid = Integer.parseInt(tempJson
                                .getString("proid"));
                        info.bai = tempJson.getString("bai");
                        info.hundrend = tempJson.getString("hundrend");
                        info.xiang = tempJson.getString("xiang");
                        if (i == 0) {
                            if (UserInfo.proInfos.size() > 0) {
                                UserInfo.proInfos.clear();
                            }
                            mBooksDB.deleteTableInfos("proinfo");
                        }
                        mBooksDB.insertInfos(info);
                    }

                    if (x == 1) {
                        startAutoScroll();
                    } else {
                        read();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载数据
     */
    public void downpro(int andex) {
        Log.e("andex", ""+andex+"---"+proList.size());
        if (andex >= proList.size()) {
            read();

        } else {
            String url = proList.get(andex);
            UserInfo.isLoad = 1;
            if (mLogoImage.loadNetDrawable(url, new KImageCallback())) {
                index++;
                if (index < proList.size()) {
                    downpro(index);
                } else {
                    read();
                }
            }
        }
    }

    private void startAutoScroll() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message msg = mHandler.obtainMessage(0, 0, 0);
                mHandler.sendMessage(msg);

            }

        }.start();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            index = 0;
            downpro(index);
        }
    };

    class KImageCallback implements ImageCallback {

        public KImageCallback() {

        }

        @Override
        public void imageLoaded(Drawable imageDrawable, String imageUrl) {
            index++;
            if (index < proList.size()) {
                downpro(index);
            } else {
                read();
            }
        }
    }
}
