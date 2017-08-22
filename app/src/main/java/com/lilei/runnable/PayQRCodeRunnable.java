package com.lilei.runnable;

import android.os.Message;
import android.util.Log;

import com.example.zzq.bean.SysData;
import com.example.zzq.bean.UserInfo;
import com.zhang.box.constants.Constants;
import com.zhang.box.utils.StringUtils;
import com.zhang.easymoney.net.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class PayQRCodeRunnable implements Runnable {

    private String mchTradeNo;
    private UserInfo desInfos;
    private String url;

    @Override
    public void run() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                10);
        mchTradeNo = StringUtils.getRandonInt(20);
        String weixinprice = Float
                .toString((float) desInfos.weixinprice / 100);
        //String price = "0.01";
        nameValuePairs.add(new BasicNameValuePair("tradeAmt", weixinprice));
        nameValuePairs.add(new BasicNameValuePair("body", desInfos.des)); // desTitle
        nameValuePairs
                .add(new BasicNameValuePair("mchTradeNo", mchTradeNo));
        String subject = desInfos.des + "|" + desInfos.id + "|"
                + desInfos.hdid + "|" + SysData.imei + "|"
                + desInfos.prohuogui;
        nameValuePairs.add(new BasicNameValuePair("subject", subject));
        //TODO WHWH
        url = Constants.weixinQRUrlZZQ;
        String json = HttpUtil.RequestGetData(url, nameValuePairs);
        Log.e("wh",
                "des-->微信请求二维码地址参数-->nameValuePairs"
                        + nameValuePairs.toString());
        Log.e("wh", "des-->微信请求二维码地址-->json" + json);
        if(json!=null&&!json.equals("")){
            Message msg = new Message();
            msg.what = 2;
        }
    }
}
