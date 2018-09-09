package com.rongxun.xqlc.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.bank.BankCard;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.ServiscUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;


public class MyBankCardActivity extends MyBaseActivity {

    IconFontTextView myBankCardToolbarBack;
    TextView myBankCardToolbarTitle;
    Toolbar myBankCardToolbar;
    ImageView myBankCardBankImg;
    TextView myBankCardBankName;
    TextView myBankCardCardId;
//    TextView myBankCardUserName;

    private String TAG = "银行卡信息";
    private BankList banklist;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            UpdateViews(banklist);
        }
    };
    private ImageView service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_my_bank_card);

        initView();
        initToolBar();

        String url = AppConstants.URL_SUFFIX + "/rest/bankTo";
        RequestForBankData(url);

    }

    private void initView() {
        service = (ImageView) findViewById(R.id.account_manage_toolbar_iv);
        myBankCardToolbarBack = (IconFontTextView) findViewById(R.id.my_bank_card_toolbar_back);
        myBankCardToolbarTitle = (TextView) findViewById(R.id.my_bank_card_toolbar_title);
        myBankCardToolbar = (Toolbar) findViewById(R.id.my_bank_card_toolbar);
        myBankCardBankImg = (ImageView) findViewById(R.id.my_bank_card_bank_img);
        myBankCardBankName = (TextView) findViewById(R.id.my_bank_card_bank_name);
        myBankCardCardId = (TextView) findViewById(R.id.my_bank_card_card_id);
//        myBankCardUserName = (TextView) findViewById(R.id.my_bank_card_user_name);
        service.setOnClickListener(new View.OnClickListener() {
            private ServiscUtil serviscUtil;

            @Override
            public void onClick(View v) {
                serviscUtil = new ServiscUtil(MyBankCardActivity.this,myBankCardCardId);
                serviscUtil.initPopuptWindowExit();
            }
        });

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 请求拉取银行信息
     *
     * @param basicUrl
     */
    public void RequestForBankData(String basicUrl) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        final BankList resultBean = JSON.parseObject(s, BankList.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            banklist = resultBean;
                            if (banklist != null) {
                                Message message = new Message();
                                mHandler.sendMessage(message);
                            }
                        } else {
                            Toast.makeText(MyBankCardActivity.this, "获取银行数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void UpdateViews(BankList banklist) {
        if (banklist.getCardNo() == null)
            return;
//        myBankCardUserName.setText(banklist.getRealName());
        int length = banklist.getCardNo().length( );
        myBankCardCardId.setText("****      ****      ****      " + banklist.getCardNo().substring(length - 4, length));

        for (BankList.BankCardListBean itemn : banklist.getBankCardList()) {
            if (itemn.getBankId().equals(banklist.getBankId())) {
                myBankCardBankName.setText(itemn.getBankName());
                Glide.with(this).load(itemn.getBankIcon()).into(myBankCardBankImg);

            }
        }
    }

    public void initToolBar() {
        myBankCardToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(myBankCardToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
