package com.zhang.box;

import com.avm.serialport_142.MainHandler;
import com.example.zzq.bean.SysData;
import com.zhang.box.utils.StyleUtil;
import com.zhang.box.utils.ToastTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wang 温度设置
 */
public class DegreeActivity extends Activity implements OnClickListener {

    private static final String TAG = "DegreeActivity";

    private Context mContext;
    private TextView tv_degree_now_left;
    private ImageButton ib_degree_normoal_left;
    private ImageButton ib_degree_ice_left;
    private ImageButton ib_degree_hot_left;
    private TextView tv_degree_now_right;
    private ImageButton ib_degree_normoal_right;
    private ImageButton ib_degree_ice_right;
    private ImageButton ib_degree_hot_right;
    private int btnId = 2; // 常温 2 制冷0 制热1
    private int typeId = 0;// 左室区 3 右室区 4
    private ImageButton ib_degree_conform; // 确定设置
    private ImageButton ib_degree_return; // 返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StyleUtil.customStyle(this, R.layout.degree, "温度设置");
        mContext = this;
        intView();
        initData();
        intListener();
    }

    private void initData() {

        String huoguiNo = MainHandler.getAVMConfigInfo(11);
//        String leftStuNo = huoguiNo.substring(19, 20);
//        String rightStuNo = huoguiNo.substring(21, 22);
        Log.e(TAG, "initData: " + huoguiNo);
        if (!huoguiNo.isEmpty()) {
            String degreeL = huoguiNo.substring(34, 36);
            tv_degree_now_left.setText("当前左室区温度为: " + degreeL + "°C");
            String degreeR = huoguiNo.substring(huoguiNo.length() - 2,
                    huoguiNo.length());
            tv_degree_now_right.setText("当前右室区温度为: " + degreeR + "°C");
        } else {
            tv_degree_now_left.setText("当前左室区温度为: 关闭状态");
            tv_degree_now_right.setText("当前右室区温度为: 关闭状态");
        }
    }

    /**
     * 设置监听事件
     */
    private void intListener() {
        ib_degree_normoal_left.setOnClickListener(this);
        ib_degree_ice_left.setOnClickListener(this);
        ib_degree_hot_left.setOnClickListener(this);
        ib_degree_normoal_right.setOnClickListener(this);
        ib_degree_ice_right.setOnClickListener(this);
        ib_degree_hot_right.setOnClickListener(this);
        ib_degree_conform.setOnClickListener(this);
        ib_degree_return.setOnClickListener(this);
    }

    /**
     * 初始化布局控件
     */
    private void intView() {

        // 左室区 布局控件
        tv_degree_now_left = (TextView) findViewById(R.id.tv_degree_now_left);
        ib_degree_normoal_left = (ImageButton) findViewById(R.id.ib_degree_normoal_left);
        ib_degree_ice_left = (ImageButton) findViewById(R.id.ib_degree_ice_left);
        ib_degree_hot_left = (ImageButton) findViewById(R.id.ib_degree_hot_left);

        // 右室区 布局控件
        tv_degree_now_right = (TextView) findViewById(R.id.tv_degree_now_right);
        ib_degree_normoal_right = (ImageButton) findViewById(R.id.ib_degree_normoal_right);
        ib_degree_ice_right = (ImageButton) findViewById(R.id.ib_degree_ice_right);
        ib_degree_hot_right = (ImageButton) findViewById(R.id.ib_degree_hot_right);

        // 设置成功后确定按钮
        ib_degree_conform = (ImageButton) findViewById(R.id.ib_degree_conform);
        ib_degree_return = (ImageButton) findViewById(R.id.ib_degree_return);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_degree_normoal_left:
                // TODO WH
                btnId = 2;
                typeId = 3;
                SysData.leftStu = btnId;
                tv_degree_now_left.setText("当前设置温度为:关闭状态");
                ToastTools.showShort(mContext, "左室区常温");
                break;

            case R.id.ib_degree_ice_left:
                btnId = 0;
                typeId = 3;
                SysData.leftStu = btnId;
                setDialog(tv_degree_now_left, "当前设置的温度为:", btnId, typeId);
                break;
            case R.id.ib_degree_hot_left:
                btnId = 1;
                typeId = 3;
                SysData.leftStu = btnId;
                setDialog(tv_degree_now_left, "当前设置的温度为:", btnId, typeId);
                break;
            case R.id.ib_degree_normoal_right:
                btnId = 2;
                typeId = 4;
                SysData.rightStu = btnId;
                tv_degree_now_right.setText("当前设置温度为:关闭状态");
                ToastTools.showShort(mContext, "右室区常温");
                break;
            case R.id.ib_degree_ice_right:
                btnId = 0;
                typeId = 4;
                SysData.rightStu = btnId;
                setDialog(tv_degree_now_right, "当前设置的温度为:", btnId, typeId);
                break;
            case R.id.ib_degree_hot_right:
                btnId = 1;
                typeId = 4;
                SysData.rightStu = btnId;
                setDialog(tv_degree_now_right, "当前设置的温度为:", btnId, typeId);
                break;

            case R.id.ib_degree_conform:
                // 确定设置系统状态配置的信息
                setSureInfo();
                break;
            case R.id.ib_degree_return:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 确定设置信息
     */
    private void setSureInfo() {
        Log.e("whwh", "setSureInfo---->leftStu===" + SysData.leftStu);
        Log.e("whwh", "setSureInfo---->leftNum===" + SysData.leftNum);
        Log.e("whwh", "setSureInfo---->rightStu===" + SysData.rightStu);
        Log.e("whwh", "setSureInfo---->rightNum===" + SysData.rightNum);
        // 当尚未设置温度 或者左右室均为关闭时
        if ((SysData.leftStu == -1 && SysData.rightStu == -1)
                || (SysData.leftStu == 2 && SysData.rightStu == 2)) {
            setMachineData(2, 2, "10", 40);

            // 只设置左室区时
        } else if (SysData.leftStu != -1 && SysData.rightStu == -1) {
            // 只有左室区制冷的情况
            if (SysData.leftStu == 0) {
                if (SysData.leftNum >= 0 && SysData.leftNum < 10) {
                    setMachineData(0, 2, "0" + SysData.leftNum, 41);
                } else {
                    setMachineData(0, 2, SysData.leftNum + "", 41);
                }
                // 只有左室区制热的情况
            } else if (SysData.leftStu == 1) {
                setMachineData(1, 2, "0" + SysData.leftNum, SysData.leftNum);
            } else if (SysData.leftStu == 2) {
                setMachineData(2, 2, 10 + "", 26);
            }
            // 只设置右室区时
        } else if (SysData.leftStu == -1 && SysData.rightStu != -1) {
            // 只有右室区制冷的情况
            if (SysData.rightStu == 0) {
                if (SysData.rightNum >= 0 && SysData.rightNum < 10) {
                    setMachineData(2, 0, "0" + SysData.rightNum, 40);
                } else {
                    setMachineData(2, 0, SysData.rightNum + "", 40);
                }

                // 只有右室区制热的情况
            } else if (SysData.rightStu == 1) {
                setMachineData(2, 1, SysData.rightNum + "", SysData.rightNum);
            } else if (SysData.rightStu == 2) {
                setMachineData(2, 2, 10 + "", 26);
            }

            // TODO 当左室区和右室区同时设置温度时
        } else if (SysData.leftStu != -1 && SysData.rightStu != -1) {

            // 左侧制冷 右侧制热
            if (SysData.leftStu == 0 && SysData.rightStu == 1) {

                if (SysData.leftNum >= 0 && SysData.leftNum < 10) {
                    setMachineData(0, 1, "0" + SysData.leftNum,
                            SysData.rightNum);
                } else {
                    setMachineData(0, 1, SysData.leftNum + "", SysData.rightNum);
                }

                // 左侧制冷 右侧关闭
            } else if (SysData.leftStu == 0 && SysData.rightStu == 2) {

                if (SysData.leftNum >= 0 && SysData.leftNum < 10) {
                    setMachineData(0, 2, "0" + SysData.leftNum, 40);
                } else {
                    setMachineData(0, 2, SysData.leftNum + "", 40);
                }
                // 左侧制热 右侧制冷
            } else if (SysData.leftStu == 1 && SysData.rightStu == 0) {
                if (SysData.rightNum >= 0 && SysData.rightNum < 10) {
                    setMachineData(1, 0, "0" + SysData.rightNum,
                            SysData.leftNum);
                } else {
                    setMachineData(1, 0, SysData.rightNum + "", SysData.leftNum);
                }

                // 左侧制热 右侧关闭
            } else if (SysData.leftStu == 1 && SysData.rightStu == 2) {
                setMachineData(1, 2, "00", SysData.leftNum);

                // 左侧关闭 右侧制冷
            } else if (SysData.leftStu == 2 && SysData.rightStu == 0) {
                if (SysData.rightNum >= 0 && SysData.rightNum < 10) {
                    setMachineData(2, 0, "0" + SysData.rightNum, 40);
                } else {
                    setMachineData(2, 0, SysData.rightNum + "", 40);
                }

                // 左侧关闭 右侧制热
            } else if (SysData.leftStu == 2 && SysData.rightStu == 1) {
                setMachineData(2, 1, "00", SysData.rightNum);

                // 左室区 和右室区同时制冷时
            } else if (SysData.leftStu == 0 && SysData.rightStu == 0) {

                if (SysData.leftNum >= 0 && SysData.leftNum < 10) {
                    setMachineData(0, 0, "0" + SysData.leftNum, 40);
                } else {
                    setMachineData(0, 0, SysData.leftNum + "", 40);
                }
                if (SysData.rightNum >= 0 && SysData.rightNum < 10) {
                    setMachineData(0, 0, "0" + SysData.rightNum, 40);
                } else {
                    setMachineData(0, 0, SysData.rightNum + "", 40);
                }

                // 左室区 和右室区同时制热时
            } else if (SysData.leftStu == 1 && SysData.rightStu == 1) {
                setMachineData(1, 1, "00", SysData.leftNum);
                setMachineData(1, 1, "00", SysData.rightNum);
            }
        }
    }

    /**
     * 设置机器配置信息
     */
    private void setMachineData(int leftStu, int rightStu, String ice, int hot) {

        Log.e("whwh", "setMachineData---->leftStu===" + SysData.leftStu);
        Log.e("whwh", "setMachineData---->leftNum===" + SysData.leftNum);
        Log.e("whwh", "setMachineData---->rightStu===" + SysData.rightStu);
        Log.e("whwh", "setMachineData---->rightNum===" + SysData.rightNum);
        String info = "11000000000" + leftStu + rightStu + "02000700" + ice
                + hot;

        Log.e("whwh", "setMachineData---->info===" + info);

        boolean isNoNumSet = MainHandler.noticeAvmConfig(info);
        if (isNoNumSet) {
            ToastTools.showShort(mContext, "温度配置设置成功!");
        } else {
            ToastTools.showShort(mContext, "温度配置设置失败!");
        }
    }

    /**
     * 带编辑框
     */
    protected void setDialog(final TextView tv, final String str,
                             final int btnId, final int typeId) {
        LayoutInflater factory = LayoutInflater.from(mContext);// 提示框
        final View layout = factory.inflate(R.layout.dialog, null);// 这里必须是final的
        final EditText et = (EditText) layout.findViewById(R.id.et_num);// 获得输入框对象
        final TextView tv_set_huodao = (TextView) layout
                .findViewById(R.id.tv_set_huodao);

        if (btnId == 0) {
            tv_set_huodao.setText("制冷温度可设置范围为:0°C ~ 25°C");
        } else if (btnId == 1) {
            tv_set_huodao.setText("制热温度可设置范围为:40°C ~ 63°C");
        }
        new AlertDialog.Builder(mContext)
                // .setTitle("请输入饮料数量")
                // .setIcon(android.R.drawable.ic_dialog_info)
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString().trim();
                        if (input.equals("")) {
                            Toast.makeText(mContext, "请输入设置的温度!" + input,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            int Pronum = Integer.parseInt(input);
                            if (btnId == 0) {
                                if (Pronum >= 0 && Pronum <= 25) {
                                    if (typeId == 3) {
                                        SysData.leftNum = Pronum;
                                        SysData.leftStu = btnId;
                                        tv.setText(str + SysData.leftNum + "°C");
                                        Toast.makeText(mContext, "左室区制冷温度设置!",
                                                Toast.LENGTH_SHORT).show();
                                    } else if (typeId == 4) {
                                        SysData.rightNum = Pronum;
                                        SysData.rightStu = btnId;
                                        tv.setText(str + SysData.rightNum
                                                + "°C");
                                        Toast.makeText(mContext, "右室区制冷温度设置!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, "请按照规定范围设置制冷温度!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else if (btnId == 1) {
                                if (Pronum >= 40 && Pronum <= 63) {
                                    if (typeId == 3) {
                                        SysData.leftNum = Pronum;
                                        SysData.leftStu = btnId;
                                        tv.setText(str + SysData.leftNum + "°C");
                                        Toast.makeText(mContext, "左室区制热温度设置!",
                                                Toast.LENGTH_SHORT).show();
                                    } else if (typeId == 4) {
                                        SysData.rightNum = Pronum;
                                        SysData.rightStu = btnId;
                                        tv.setText(str + SysData.rightNum
                                                + "°C");
                                        Toast.makeText(mContext, "右室区制热温度设置!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, "请按照规定范围设置制热温度!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }
}
