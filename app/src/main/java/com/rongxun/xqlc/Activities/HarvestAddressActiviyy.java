package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.ReceivingAddressBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.SelfDialog;
import com.rongxun.xqlc.UI.SelfDialog1;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import okhttp3.Call;


/**
 * Created by oguig on 2017/5/15.
 * 我的收货地址
 */

public class HarvestAddressActiviyy extends MyBaseActivity implements View.OnClickListener {
    private LinearLayout myharvestaddress_ll_address;
    private TextView img_back;
    private RelativeLayout myharvestaddress_rl;
    private TextView topname, tv_no_harvest_address;
    private ImageView iv_no_harvest_address;
    private Button btn_addreceivingaddress;
    private LinearLayout myharvestaddress_btn_editor, myharvestaddress_btn_delete;
    public static final String EDIT = "000";
    private SelfDialog selfDialog;
    private SelfDialog1 selfDialog1;
    private static final int HARVESTADDRESS = 1; //用户有收货地址
    private static final int NOADDRESS = 2;       //用户没有收货地址
    private static final int REMOVEADDRESS = 3;  //删除地址
    /*编辑成功跳转 我的收货地址 界面结果码*/
    private static final int resultCodeTo_edit_succeed = 6546;
    /*添加成功跳转 我的收货地址 界面结果码*/
    private static final int resultCodeTo_add_succeed = 6547;
    private boolean REVEOK = false;

    public ReceivingAddressBean receivingAddressBean;
    private TextView myharvestaddress_name, myharvestaddress_phone, myharvestaddress_address_addressinfo;//收货人姓名，电话，地址
    private String token;
    private RelativeLayout hava_no_address_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CustomApplication.addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_harvest_address);
        initView();
        //获取用户收货地址
        new harvestAddressThread().start();

    }


    private void initView() {
        token = PreferenceUtil.getPrefString(HarvestAddressActiviyy.this, "loginToken", "");
        img_back = (TextView) findViewById(R.id.harves_black);
        topname = (TextView) findViewById(R.id.harvest_topname);
        myharvestaddress_name = (TextView) findViewById(R.id.myharvestaddress_name);
        myharvestaddress_phone = (TextView) findViewById(R.id.myharvestaddress_phone);
        myharvestaddress_address_addressinfo = (TextView) findViewById(R.id.myharvestaddress_address_addressinfo);
        tv_no_harvest_address = (TextView) findViewById(R.id.tv_no_harvest_address);
        hava_no_address_rl=(RelativeLayout)findViewById(R.id.hava_no_address_rl);
        myharvestaddress_btn_editor = (LinearLayout) findViewById(R.id.myharvestaddress_btn_editor);
        myharvestaddress_btn_delete = (LinearLayout) findViewById(R.id.myharvestaddress_btn_delete);
        iv_no_harvest_address = (ImageView) findViewById(R.id.iv_no_harvest_address);
        btn_addreceivingaddress = (Button) findViewById(R.id.btn_addreceivingaddress);
        myharvestaddress_rl = (RelativeLayout) findViewById(R.id.myharvestaddress_rl);
        myharvestaddress_ll_address = (LinearLayout) findViewById(R.id.myharvestaddress_ll_address);
        img_back.setVisibility(View.VISIBLE);
        topname.setText("我的收货地址");
        img_back.setOnClickListener(this);
        myharvestaddress_btn_editor.setOnClickListener(this);
        myharvestaddress_btn_delete.setOnClickListener(this);
        btn_addreceivingaddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.harves_black:
                finish();
                break;
            //跳转至添加收货地址（没有添加过地址）
            case R.id.btn_addreceivingaddress:
                Intent intent = new Intent(this, AddOrEditTheReceivingAddressActivity.class);
                startActivityForResult(intent, resultCodeTo_add_succeed);
                break;
            //点击收货地址编辑
            case R.id.myharvestaddress_btn_editor:
                Intent it = new Intent(this, AddOrEditTheReceivingAddressActivity.class);
                it.putExtra("edit", EDIT);
                it.putExtra("name", receivingAddressBean.getName());
                it.putExtra("phone", receivingAddressBean.getPhone());
                it.putExtra("address", receivingAddressBean.getAddress());
                it.putExtra("addressinfo", receivingAddressBean.getAddressInfo());
                startActivityForResult(it, resultCodeTo_edit_succeed);
                break;
            //点击收货地址删除
            case R.id.myharvestaddress_btn_delete:
                showNormalDialog();
                break;
        }
    }

    private void showNormalDialog() {
        selfDialog = new SelfDialog(this);
        selfDialog.setTitle("温馨提示");
        selfDialog.setMessage("确定删除收货地址吗?");
        selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {


            @Override
            public void onYesClick() {
                selfDialog.dismiss();
                //删除地址
                new deleteReceivingAddressThread().start();
            }
        });
        selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                selfDialog.dismiss();
            }
        });
        selfDialog.show();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //显示用户收货地址
                case HARVESTADDRESS:
                    iv_no_harvest_address.setVisibility(View.GONE);
                    tv_no_harvest_address.setVisibility(View.GONE);
                    btn_addreceivingaddress.setVisibility(View.GONE);
                    hava_no_address_rl.setVisibility(View.GONE);
                    myharvestaddress_ll_address.setVisibility(View.VISIBLE);
                    myharvestaddress_name.setText(receivingAddressBean.getName());
                    myharvestaddress_phone.setText(receivingAddressBean.getPhone());
                    myharvestaddress_address_addressinfo.setText(receivingAddressBean.getAddress() + receivingAddressBean.getAddressInfo());
                    break;
                //没有用户收货地址
                case NOADDRESS:
                    iv_no_harvest_address.setVisibility(View.VISIBLE);
                    tv_no_harvest_address.setVisibility(View.VISIBLE);
                    btn_addreceivingaddress.setVisibility(View.VISIBLE);
                    hava_no_address_rl.setVisibility(View.VISIBLE);
                    myharvestaddress_ll_address.setVisibility(View.GONE);
                    //todo
                    break;
                case REMOVEADDRESS:
                    myharvestaddress_ll_address.setVisibility(View.GONE);
                    iv_no_harvest_address.setVisibility(View.VISIBLE);
                    tv_no_harvest_address.setVisibility(View.VISIBLE);
                    btn_addreceivingaddress.setVisibility(View.VISIBLE);
                    hava_no_address_rl.setVisibility(View.VISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("ppp", resultCode + "");
        switch (requestCode) {
            case resultCodeTo_add_succeed:
                //获取用户收货地址
                new harvestAddressThread().start();
                break;
            case resultCodeTo_edit_succeed:
                //获取用户收货地址
                new harvestAddressThread().start();
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
//        //获取用户收货地址
        new harvestAddressThread().start();
        super.onRestart();
    }

    private class harvestAddressThread extends Thread {
        @Override
        public void run() {
            String basicUrl =AppConstants.URL_SUFFIX + "/rest/userAddressVT";

            OkHttpUtils.post()
                    .url(basicUrl)
                    .addParams("token", token)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String ss = response.toString();
                            receivingAddressBean = new Gson().fromJson(ss, ReceivingAddressBean.class);
                            if (receivingAddressBean.getRcd().equals("R0001")) {
                                //请求成功
                                mHandler.sendEmptyMessage(HARVESTADDRESS);
                            } else if (receivingAddressBean.getRcd().equals("R0003")) {
                                mHandler.sendEmptyMessage(NOADDRESS);
                            }
                        }
                    });


            super.run();
        }
    }

    private class deleteReceivingAddressThread extends Thread {
        @Override
        public void run() {
            String basicUrl =AppConstants.URL_SUFFIX+"/rest/userAddressDelVT";
            OkHttpUtils.post()
                    .url(basicUrl)
                    .addParams("token", token)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            showDelDialog();



                        }
                    });
            super.run();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
//弹出确认弹框
    private void showDelDialog() {
        selfDialog1 = new SelfDialog1(this);
        selfDialog1.setTitle("温馨提示");
        selfDialog1.setMessage("删除成功！");
        selfDialog1.setYesOnclickListener("确定", new SelfDialog1.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                REVEOK = true;
                mHandler.sendEmptyMessage(REMOVEADDRESS);
                selfDialog1.dismiss();
            }
        });
        selfDialog1.show();
    }
}




