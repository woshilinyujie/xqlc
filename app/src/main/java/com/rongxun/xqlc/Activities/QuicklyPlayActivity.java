package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.BaseBean;
import com.rongxun.xqlc.Beans.InverstBeen;
import com.rongxun.xqlc.Beans.InvestBean;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.Beans.bank.QuickPlayBean;
import com.rongxun.xqlc.Beans.bank.RbOrderBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.BankLoading;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.ServiscUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;


public class QuicklyPlayActivity extends MyBaseActivity implements View.OnClickListener {
    DecimalFormat df = new DecimalFormat("#0.00");
    private String orderString;
    private TextView bank_name;
    private TextView phone;
    private Button sure;
    private IconFontTextView black;
    private EditText code;
    private Button code_button;
    private TextView money;
    private TextView order;
    private String TAG = "快速支付";
    private String TAG1 = "查询支付";
    private boolean isFirst = true;
    private CountDownTimer countDownTimer;
    private LoadingDialog loadingDialog;
    private int count = 0;
    private String jiaxijuanID = "0";
    private String bankUrl = AppConstants.URL_SUFFIX + "/rest/bankTo";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    bank_name.setText(quickPlayBean.getBank_name());

                    phone.setText(quickPlayBean.getPhone().substring(0, 3) + "****" +
                            quickPlayBean.getPhone().substring(7, 11));

                    try {
                        for (int x = 0; x < banklistBean.getBankCardList().size(); x++) {
                            if (banklistBean.getBankCardList().get(x).getBankId().equals(banklistBean.getBankId())) {
                                Glide.with(QuicklyPlayActivity.this).load(banklistBean.getBankCardList().get(x).getBankIcon()).into(bank_iv);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("快捷支付", e.toString());
                    }

                    bank_name_number.setText("（" + "尾号" + banklistBean.
                            getCardNo().substring(banklistBean.getCardNo().length() -
                            4, banklistBean.getCardNo().length()) + "）");
                    money.setText(quickPlayBean.getMoney() + "元");
                    order.setText(orderString);
                    sure.setEnabled(true);
                    break;
                case 1:
                    String url = AppConstants.URL_SUFFIX + "/rest/payResult";
                    //查询支付结果
                    RequestForRechargeToResult(url);
                    break;
            }

        }
    };
    private QuickPlayBean quickPlayBean;
    private String inverstMark;
    private String selectedIdArrayString;
    private String safePass;
    private String tenderMoney;
    private String project;
    private int projectId;
    private EditText safePassT;
    private String payPassword;
    private BankList banklistBean;
    private TextView forget;
    private TextView bank_name_number;
    private ImageView bank_iv;
    private LinearLayout service;
    private BankLoading BankDialog;
    private String exceptS;
    private String time;
    private LinearLayout isinverst_ll;
    private TextView hongbao_play;
    private TextView money_play;
    private TextView inverst_money;
    private LinearLayout hongbaoLL;
    private String chargeMoney;
    private String idNo;
    private String realName;
    private String userId;
    private String cardNo;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        count = 0;
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_quickly_play);
        Intent intent = getIntent();
        inverstMark = intent.getStringExtra("inverstMark");

        orderString = intent.getStringExtra("order");
        selectedIdArrayString = intent.getStringExtra("selectedIdArrayString");
        safePass = intent.getStringExtra("safePass");
        tenderMoney = intent.getStringExtra("tenderMoney");
        projectId = intent.getIntExtra("projectId", 0);
        project = intent.getStringExtra("project");

        chargeMoney = intent.getStringExtra("chargeMoney");
        cardNo = intent.getStringExtra("cardNo");
        userId = intent.getStringExtra("userId");
        realName = intent.getStringExtra("realName");
        idNo = intent.getStringExtra("idNo");

        exceptS = intent.getStringExtra("exceptS");

        initView();
        initListener();
        initDate();
        countDownTimer = new CountDownTimer(1000 * AppConstants.VerifyCodeTimeFuture, 1000 * AppConstants.VerifyCodeTimeInteral) {

            @Override
            public void onTick(long millisUntilFinished) {
                code_button.setEnabled(false);
                code_button.setText((millisUntilFinished / 1000) + "秒");
                code_button.setTextColor(Color.parseColor("#666666"));
            }

            @Override
            public void onFinish() {
                code_button.setEnabled(true);
                code_button.setText("重新获取");
                code_button.setTextColor(Color.parseColor("#3574"));
            }
        };
        code_button.setEnabled(false);
        countDownTimer.start();
    }

    private void initListener() {
        sure.setOnClickListener(this);
        black.setOnClickListener(this);
        code_button.setOnClickListener(this);
        sure.setEnabled(false);
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6) {
                    sure.setBackgroundResource(R.drawable.button_background_normal1);
                    sure.setEnabled(false);
                } else {
                    sure.setBackgroundResource(R.drawable.button_background_normal);
                    sure.setEnabled(true);
                }
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            private ServiscUtil serviscUtil;

            @Override
            public void onClick(View v) {
                serviscUtil = new ServiscUtil(QuicklyPlayActivity.this, bank_name);
                serviscUtil.initPopuptWindowExit();
            }
        });
    }

    private void initView() {


        bank_name = (TextView) findViewById(R.id.quickly_play_bank_name);
        phone = (TextView) findViewById(R.id.quickly_play_bank_phone);
        sure = (Button) findViewById(R.id.quickly_play_bank_sure);
        black = (IconFontTextView) findViewById(R.id.quickly_play_black);
        code = (EditText) findViewById(R.id.quickly_play_code);
        code_button = (Button) findViewById(R.id.quickly_play_code_button);
        money = (TextView) findViewById(R.id.quickly_play_money);
        order = (TextView) findViewById(R.id.quickly_play_order);
        safePassT = (EditText) findViewById(R.id.quickly_play_safepass);
        forget = (TextView) findViewById(R.id.quickly_play_forget);
        bank_name_number = (TextView) findViewById(R.id.quickly_play_bank_name_number);
        bank_iv = (ImageView) findViewById(R.id.quickly_play_bank_iv);
        service = (LinearLayout) findViewById(R.id.play_quickly_service);
        //是否是投资充值框
        isinverst_ll = (LinearLayout) findViewById(R.id.quickly_play_isinverst_ll);
        //使用红包支付金额
        hongbao_play = (TextView) findViewById(R.id.quickly_play_hongbao_play);
        //余额支付
        money_play = (TextView) findViewById(R.id.quickly_play_money_play);
        //投资金额
        inverst_money = (TextView) findViewById(R.id.quickly_play_inverst_money);
        hongbaoLL = (LinearLayout) findViewById(R.id.quickly_play_hongbao_ll);
    }

    private void initDate() {
        url = AppConstants.URL_SUFFIX + "/rest/payOrder";
        RequestForRechargeTo(url);
    }

    //访问表单
    public void RequestForRechargeTo(String url) {
        OkHttpUtils.post()
                .url(url)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("orderNo", orderString)
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
                        quickPlayBean = new Gson().fromJson(s, QuickPlayBean.class);
                        if (quickPlayBean.getRcd().equals("R0001")) {
                            //请求成功  查询银行卡
                            RequestForBankData(bankUrl);
                        }

                        forget.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(QuicklyPlayActivity.this, ForgetSafePassActivity.class);
                                intent.putExtra("currentPhoneNo", quickPlayBean.getPhone());
                                startActivity(intent);
                            }
                        });

                        //设置显示参数
                        if (inverstMark != null) {
                            sure.setEnabled(false);
                            isinverst_ll.setVisibility(View.VISIBLE);
                            if (getIntent().getIntExtra("hongbaoCount", 0) == 0) {
                                hongbaoLL.setVisibility(View.GONE);
                            } else {
                                hongbao_play.setText(getIntent().getIntExtra("hongbaoCount", 0) + "元");
                            }
                            inverst_money.setText(getIntent().getStringExtra("tenderMoney") + "元");
                            money_play.setText(df.format(Float.parseFloat(getIntent().getStringExtra("tenderMoney")) -
                                    getIntent().getIntExtra("hongbaoCount", 0) - quickPlayBean.getMoney()) + "元");
                        }
                    }
                });


    }


    //重发验证码
    public void RequestForRechargeToCode() {
        String chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeSaveHnew";
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.showLoading();
        }

        OkHttpUtils.post()
                .url(chargeUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("userId", userId)
                .addParams("idNo", idNo)
                .addParams("realName", realName)
                .addParams("cardNo", cardNo)
                .addParams("userAccountRecharge.money", chargeMoney)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null || e.getMessage() != null) {
                            Toast.makeText(QuicklyPlayActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, "（一）:" + s);
                        final RbOrderBean resultBean = new Gson().fromJson(s, RbOrderBean.class);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            orderString = resultBean.getOrder_no();
                            order.setText(orderString);
                            RequestForRechargeTo(url);
                            code_button.setEnabled(false);
                            countDownTimer.start();
                        } else {
                            Toast.makeText(QuicklyPlayActivity.this, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    //确认支付
    public void RequestForRechargeToPlay(String url) {

        if (!TextUtils.isEmpty(safePassT.getText().toString())) {
            payPassword = safePassT.getText().toString();
        } else if (!TextUtils.isEmpty(safePass)) {
            payPassword = safePass;
        }

        if (TextUtils.isEmpty(orderString) || TextUtils.isEmpty(payPassword)
                || TextUtils.isEmpty(code.getText().toString())) {
            return;
        }

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.showLoading();
        }

        OkHttpUtils.post()
                .url(url)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("orderNo", orderString)
                .addParams("payPassword", payPassword)
                .addParams("checkCode", code.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        Toast.makeText(QuicklyPlayActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        final BaseBean codeBean = new Gson().fromJson(s, BaseBean.class);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        if (codeBean.getRcd().equals("R0001")) {
                            //查询支付结果 支付成功
                            String url = AppConstants.URL_SUFFIX + "/rest/payResult";
                            RequestForRechargeToResult(url);
                        } else if (codeBean.getRcd().equals("S00010")) {
                            Toast.makeText(QuicklyPlayActivity.this, codeBean.getRmg(), Toast.LENGTH_SHORT).show();
                        } else {
                            //支付处理中
                            Intent intent = new Intent(QuicklyPlayActivity.this, PlaySuccessActivity.class);
                            intent.putExtra("money", quickPlayBean.getMoney() + "");
                            intent.putExtra("order", orderString);
                            intent.putExtra("message", codeBean.getRmg());
                            startActivity(intent);

                            finish();
                        }
                    }
                });


    }


    //查询支付结果
    public void RequestForRechargeToResult(String url) {
        if (isFirst) {
            if (BankDialog == null) {
                BankDialog = new BankLoading(this, R.style.BankDialog);
                BankDialog.show();
                Window window = BankDialog.getWindow();
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.FILL_PARENT;
                lp.height = WindowManager.LayoutParams.FILL_PARENT;
                window.setAttributes(lp);
            }
            isFirst = false;
        }


        OkHttpUtils.post()
                .url(url)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("orderNo", orderString)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());

                        }
                        if (BankDialog != null) {
                            BankDialog.dismiss();
                            BankDialog = null;
                        }
                        Toast.makeText(QuicklyPlayActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG1, s);
                        final BaseBean codeBean = new Gson().fromJson(s, BaseBean.class);
                        if (codeBean.getRcd().equals("R0002")) {
                            if (count < 5) {
                                count = count + 1;
                                handler.sendEmptyMessageDelayed(1, 2000);
                            } else {
                                //进入支付失败页面
                                Intent intent = new Intent(QuicklyPlayActivity.this, PlaySuccessActivity.class);
                                //isSuccess 是否是充值成功的标记
                                intent.putExtra("isSuccess", false);
                                intent.putExtra("money", quickPlayBean.getMoney() + "");
                                intent.putExtra("order", orderString);
                                intent.putExtra("message", codeBean.getRmg());
                                startActivity(intent);

                                if (BankDialog != null && BankDialog.isShowing()) {
                                    BankDialog.dismiss();
                                    BankDialog = null;
                                }

                                //
                                finish();
                            }
                        } else {
                            //通知帐号刷新
                            Intent bIntent = new Intent("LoginContentBroadCast");
                            bIntent.putExtra("Quickly", "Quickly");
                            sendBroadcast(bIntent);
                            if (BankDialog != null) {
                                BankDialog.dismiss();
                                BankDialog = null;
                            }

                            if (inverstMark != null && inverstMark.equals("inverstMark")) {
                                //自动投资
                                String url = AppConstants.URL_SUFFIX + "/rest/investDoH/" + projectId;
                                RequestToInvestDo(url);

                            } else {
                                //充值成功
                                Intent intent = new Intent(QuicklyPlayActivity.this, PlaySuccessActivity.class);
                                intent.putExtra("money", quickPlayBean.getMoney() + "");
                                intent.putExtra("order", orderString);
                                intent.putExtra("message", codeBean.getRmg());
                                intent.putExtra("isSuccess", true);
                                startActivity(intent);
                            }
                            //关闭充值页面
                            Intent intentClose = new Intent("ChargeFragmentBroadCase");
                            intentClose.putExtra("finish", "finish");
                            sendBroadcast(intentClose);
                            finish();
                        }
                    }
                });


    }

    @Override
    public void onClick(View v) {
        int ID = v.getId();
        switch (ID) {
            case R.id.quickly_play_bank_sure:
                String urlplay = AppConstants.URL_SUFFIX + "/rest/toPayRongbaoVT";
                RequestForRechargeToPlay(urlplay);
                break;
            case R.id.quickly_play_black:
                finish();
                break;
            case R.id.quickly_play_code_button:
                //发送验证码
                String url = AppConstants.URL_SUFFIX + "/rest/ajaxGetPhoneCodeRongbao";
                RequestForRechargeToCode();
                break;
        }
    }


    /**
     * 请求提交投资该项目
     *
     * @param basicUrl
     */

    public void RequestToInvestDo(String basicUrl) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(QuicklyPlayActivity.this);
            loadingDialog.showLoading();
        }


        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("tenderMoney", tenderMoney)
                .addParams("safepwd", payPassword)
                .addParams("clientType", 1 + "")
                .addParams("hongbaoArray", selectedIdArrayString)
                .addParams("coupon", jiaxijuanID)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        Toast.makeText(QuicklyPlayActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        InvestBean inverstBeen = new Gson().fromJson(s, InvestBean.class);
                        try {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismissLoading();
                                loadingDialog = null;
                            }
                        } catch (Exception e) {

                        }
                        if (inverstBeen.getRcd().equals("R0001")) {
                            Intent successIntent = new Intent(QuicklyPlayActivity.this, InvestResultActivity.class);
                            successIntent.putExtra("project", project);
                            successIntent.putExtra("exceptS", exceptS);
                            if (TextUtils.isEmpty(inverstBeen.getTime()) && inverstBeen.getRepayDateString() != null) {
                                successIntent.putExtra("time", inverstBeen.getRepayDateString());
                            } else {
                                successIntent.putExtra("time", inverstBeen.getTime());
                            }
                            //充值金额
                            successIntent.putExtra("money", (int) Float.parseFloat(tenderMoney));


                            startActivityForResult(successIntent, InvestResultActivity.CONTEXT_INCLUDE_CODE);
                            //关闭投资页面
                            Intent intent = new Intent("ProjectInvestActivityBroadCast");
                            intent.putExtra("quickPlay", "finish");
                            sendBroadcast(intent);
                            //setResult(AppConstants.projectInvestSuccess);
                        } else if (inverstBeen.getRcd().equals("M0007_211")) {
                            //被抢投 刷新项目
                            Toast.makeText(QuicklyPlayActivity.this, "" + inverstBeen.getRmg(), Toast.LENGTH_SHORT).show();
                            //充值成功 投资不成功  刷新页面  关闭页面刷新
                            Intent intent = new Intent("ProjectInvestActivityBroadCast");
                            intent.putExtra("quickPlay", "refresh");
                            sendBroadcast(intent);
                        } else {
                            //充值成功 投资不成功  刷新页面关闭 页面刷新
                            Intent intent = new Intent("ProjectInvestActivityBroadCast");
                            intent.putExtra("quickPlay", "refresh");
                            sendBroadcast(intent);
                            Toast.makeText(QuicklyPlayActivity.this, "" + inverstBeen.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });


    }


    /**
     * 获取银行卡数据
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

                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        banklistBean = JSON.parseObject(s, BankList.class);
                        if (banklistBean.getRcd().equals("R0001")) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (inverstMark != null) {
            Intent intent = new Intent("ProjectInvestActivityBroadCast");
            intent.putExtra("quickPlay", "refresh");
            sendBroadcast(intent);
        }
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
