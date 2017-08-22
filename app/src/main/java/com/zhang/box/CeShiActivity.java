package com.zhang.box;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.avm.serialport_142.MainHandler;
import com.avm.serialport_142.utils.Avm;
import com.zhang.box.services.MachineBrocastReceiver;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CeShiActivity extends Activity implements OnClickListener {
	private Button btn_chuhuo;
	private Button btn_shilian;
	private Button btn_huodaoshunhuai;
	private Button btn_huodao1;
	private Button btn_huodao2;
	private Button btn_huodao3;
	private Button btn_huodao4;
	private Button btn_huodao5;
	private Button btn_huodao6;
	private Button btn_huodao7;
	private Button btn_huodao8;
	private Button btn_huodao9;
	private Button btn_huodao10;
	private Button btn_huodao11;
	private Button btn_huodao12;
	private Button btn_huodao13;
	private Button btn_huodao14;
	private Button btn_huodao15;
	private Button btn_huodao16;
	private Button btn_back;
	private TextView tv_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ceshi);
		/*
		 * btn_chuhuo = (Button) findViewById(R.id.btn_chuhuo); btn_shilian =
		 * (Button) findViewById(R.id.btn_shilian); btn_huodaoshunhuai =
		 * (Button) findViewById(R.id.btn_huodaoshunhuai); btn_huodao1 =
		 * (Button) findViewById(R.id.btn_huodao1); btn_huodao2 = (Button)
		 * findViewById(R.id.btn_huodao2); btn_huodao3 = (Button)
		 * findViewById(R.id.btn_huodao3); btn_huodao4 = (Button)
		 * findViewById(R.id.btn_huodao4); btn_huodao5 = (Button)
		 * findViewById(R.id.btn_huodao5); btn_huodao6 = (Button)
		 * findViewById(R.id.btn_huodao6); btn_huodao7 = (Button)
		 * findViewById(R.id.btn_huodao7); btn_huodao8 = (Button)
		 * findViewById(R.id.btn_huodao8); btn_huodao9 = (Button)
		 * findViewById(R.id.btn_huodao9); btn_huodao10 = (Button)
		 * findViewById(R.id.btn_huodao10); btn_huodao11 = (Button)
		 * findViewById(R.id.btn_huodao11); btn_huodao12 = (Button)
		 * findViewById(R.id.btn_huodao12); btn_huodao13 = (Button)
		 * findViewById(R.id.btn_huodao13); btn_huodao14 = (Button)
		 * findViewById(R.id.btn_huodao14); btn_huodao15 = (Button)
		 * findViewById(R.id.btn_huodao15); btn_huodao16 = (Button)
		 * findViewById(R.id.btn_huodao16); btn_back = (Button)
		 * findViewById(R.id.btn_back);
		 */
		tv_result = (TextView) findViewById(R.id.tv_result);
		registerBroadcastReceive();
		// inidata();

		btn_chuhuo.setOnClickListener(this);
		btn_shilian.setOnClickListener(this);
		btn_huodaoshunhuai.setOnClickListener(this);
		btn_huodao1.setOnClickListener(this);
		btn_huodao2.setOnClickListener(this);
		btn_huodao3.setOnClickListener(this);
		btn_huodao4.setOnClickListener(this);
		btn_huodao5.setOnClickListener(this);
		btn_huodao6.setOnClickListener(this);
		btn_huodao7.setOnClickListener(this);
		btn_huodao8.setOnClickListener(this);
		btn_huodao9.setOnClickListener(this);
		btn_huodao10.setOnClickListener(this);
		btn_huodao11.setOnClickListener(this);
		btn_huodao12.setOnClickListener(this);
		btn_huodao13.setOnClickListener(this);
		btn_huodao14.setOnClickListener(this);
		btn_huodao15.setOnClickListener(this);
		btn_huodao16.setOnClickListener(this);
		btn_back.setOnClickListener(this);

	}

	//
	private void inidata(String num) {
		// MainHandler.noticeAvmOutGoods("1110200000100"+Avm.OUT_GOODS_ALIPAY,
		// "123456");
		String nums = "111" + num + "00000100";
		int numcode = (int) ((Math.random() * 9 + 1) * 100000);
		if (MainHandler.noticeAvmOutGoods(nums + Avm.OUT_GOODS_ALIPAY, numcode
				+ "")) {
			Log.e("whwh", num + "出货通知成功！");
			tv_result.setText(num + "出货通知成功！");
		} else {
			tv_result.setText(num + "出货通知失败！");
		}

	}

	private void registerBroadcastReceive() {
		MachineBrocastReceiver receiver = new MachineBrocastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.avm.serialport.OUT_GOODS");
		filter.addAction("com.avm.serialport.door_state");
		registerReceiver(receiver, filter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chuhuo:
			String machineNum = MainHandler.getMachNo();// Imei号获取
			tv_result.setText("机器型号:" + machineNum);
			break;
		case R.id.btn_shilian:
			// 设置系统状态信息
			String statusInfos = "1101145184501114518451041";
			boolean isStatusInfos = MainHandler.noticeAvmConfig(statusInfos);
			if (isStatusInfos) {
				tv_result.setText("设置参数正确！");
			} else {
				tv_result.setText("设置参数失败");
			}

			break;
		case R.id.btn_huodaoshunhuai:
			// 判断设置是否成功
			int isSuccess = MainHandler.getConfigResult();
			if (isSuccess == 0) {
				tv_result.setText("设置成功！");
			} else if (isSuccess == 1) {
				tv_result.setText("设置未完成！");
			} else {
				tv_result.setText("设置失败！");
			}

			break;
		case R.id.btn_huodao1:
			inidata("01");
			break;
		case R.id.btn_huodao2:
			inidata("02");
			break;
		case R.id.btn_huodao3:
			inidata("03");
			break;
		case R.id.btn_huodao4:
			inidata("04");
			break;
		case R.id.btn_huodao5:
			inidata("20");
			break;
		case R.id.btn_huodao6:
			inidata("21");
			break;
		case R.id.btn_huodao7:
			/** 获取售货机通信状态 */
			boolean machineStutsa = MainHandler.isAvmRunning();
			if (machineStutsa) {
				tv_result.setText("工控正在正常通信");
			} else {
				tv_result.setText("工控通信异常");
			}
			break;
		case R.id.btn_huodao8:

			/** 机器运行信息 */
			String runningInfo = MainHandler.getMachRunInfo();
			tv_result.setText("机器运行信息 :" + runningInfo);

			break;
		case R.id.btn_huodao9:
			/** 系统配置信息 */
			String SysInfo = MainHandler.getAVMConfigInfo(11);
			tv_result.setText("系统配置信息 :" + SysInfo);
			break;
		case R.id.btn_huodao10:
			/** 货道检测是否好用 */
			String huodaoInfo1 = MainHandler.getGoodsInfo(11, 1);
			tv_result.setText("检测货道1是否好用 ==" + huodaoInfo1);
			break;
		case R.id.btn_huodao11:
			/** 货道检测是否好用 */
			String huodaoInfo2 = MainHandler.getGoodsInfo(11, 2);
			tv_result.setText("检测货道2是否好用 ==" + huodaoInfo2);
			break;
		case R.id.btn_huodao12:
			/** 货道检测是否好用 */
			String huodaoInfo3 = MainHandler.getGoodsInfo(11, 3);
			tv_result.setText("检测货道3是否好用 ==" + huodaoInfo3);
			break;
		case R.id.btn_huodao13:
			/** 货道检测是否好用 */
			String huodaoInfo4 = MainHandler.getGoodsInfo(11, 4);
			tv_result.setText("检测货道4是否好用 ==" + huodaoInfo4);
			break;
		case R.id.btn_huodao14:
			/** 货道检测是否好用 */
			String huodaoInfo20 = MainHandler.getGoodsInfo(11, 20);
			tv_result.setText("检测货道20是否好用 ==" + huodaoInfo20);
			break;
		case R.id.btn_huodao15:
			/** 货道检测是否好用 */
			String huodaoInfo21 = MainHandler.getGoodsInfo(11, 21);
			tv_result.setText("检测货道21是否好用 ==" + huodaoInfo21);
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_huodao16:
			boolean isOpen = MainHandler.isDoorOpen();
			if (isOpen) {
				tv_result.setText("门已打开");
			} else {
				tv_result.setText("门已关闭");
			}
			break;

		default:
			break;
		}

	}

}
