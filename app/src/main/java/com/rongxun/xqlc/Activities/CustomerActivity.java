package com.rongxun.xqlc.Activities;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFUserInfo;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.QiyuBean;
import com.rongxun.xqlc.Beans.QiyuUserBean;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.CustomerDialog;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.AppVersionUtils;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerActivity extends MyBaseActivity implements View.OnClickListener {

    private RelativeLayout customr_phone;
    private RelativeLayout customer;
    private IconFontTextView back;
    private String TAG = "客户服务";
    private LoadingDialog loadingDialog;
    private BankList resultBean;
    private TextView fankui;
    private TextView server_time;
    private String time;
    private RelativeLayout wx_rl, yx_rl;
    List<QiyuUserBean> list = new ArrayList<QiyuUserBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_customer);


        initView();
    }

    private void initView() {
        customer = (RelativeLayout) findViewById(R.id.customer);
        customr_phone = (RelativeLayout) findViewById(R.id.customer_phone);
        fankui = (TextView) findViewById(R.id.fankui);
        server_time = (TextView) findViewById(R.id.server_time);
        wx_rl = (RelativeLayout) findViewById(R.id.wx_rl);
        yx_rl = (RelativeLayout) findViewById(R.id.yx_rl);
        fankui.setOnClickListener(this);
        back.setOnClickListener(this);
        customer.setOnClickListener(this);
        customr_phone.setOnClickListener(this);
        time = getIntent().getStringExtra("time");
        if (time != null)
            server_time.setText("服务时间：" + time);
        wx_rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText("my_jcb");
                Toast.makeText(CustomerActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        yx_rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText("jcb@hzjcb.com");
                Toast.makeText(CustomerActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_back:
                finish();
                break;
            case R.id.customer_phone:
                CustomerDialog dialog = new CustomerDialog(this, R.style.ActionSheetDialogStyle);
                dialog.show();
                break;
            case R.id.fankui:
                Intent intent = new Intent(CustomerActivity.this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.customer:
                //读取银行卡信息
                String url = AppConstants.URL_SUFFIX + "/rest/bankTo";
                RequestForBankData(url);
                break;
        }
    }


    /**
     * 获取银行卡数据
     *
     * @param basicUrl
     */
    public void RequestForBankData(String basicUrl) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.showLoading();
        }

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        list.clear();
                        String s = response.toString();
                        Log.i(TAG, s);
                        ConsultSource source = new ConsultSource("", "", "");
                        YSFUserInfo userInfo = new YSFUserInfo();
                        userInfo.userId = "uid";
                        userInfo.authToken = "auth-token-from-user-server";
                        final String currentPhone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
                        resultBean = JSON.parseObject(s, BankList.class);
                        QiyuUserBean beanName = new QiyuUserBean();
                        QiyuUserBean beanPhone = new QiyuUserBean();
                        QiyuBean beanVersion=new QiyuBean();
                        beanName.setKey("real_name");
                        beanPhone.setKey("mobile_phone");
                        beanVersion.setKey("appversion");
                        beanVersion.setIndex(0);
                        beanVersion.setLabel("APP版本");
                        beanVersion.setValue( AppVersionUtils.getVersionName(CustomerActivity.this));
                        if (resultBean.getRcd().equals("R0001")) {
                            if (resultBean.getRealName() != null && resultBean.getPhone() != null) {
                                String name= resultBean.getRealName();
                                beanName.setValue(resultBean.getRealName());
                                beanPhone.setValue(resultBean.getPhone());
                            } else if (currentPhone != null) {
                                beanName.setValue("");
                                beanPhone.setValue(resultBean.getPhone());
                            } else if(resultBean.getRealName() != null){
                                beanName.setValue(resultBean.getRealName());
                                beanPhone.setValue("");
                            }else {
                                beanName.setValue("游客");
                                beanPhone.setValue("");
                            }
                        } else {
                            beanName.setValue("游客");
                            beanPhone.setValue("");
                        }

                        if (Unicorn.isServiceAvailable()) {
                            list.add(beanName);
                            list.add(beanPhone);
                            list.add(beanVersion);
                            Gson gson = new Gson();
                            String s1 = gson.toJson(list);
                            userInfo.data = s1;
                            Unicorn.setUserInfo(userInfo);
                            Unicorn.openServiceActivity(CustomerActivity.this, "金储宝", source);
                        } else {
                            Toast.makeText(CustomerActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                        }
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }

                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
