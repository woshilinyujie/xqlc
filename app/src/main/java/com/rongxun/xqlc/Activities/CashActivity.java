package com.rongxun.xqlc.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
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
import com.rongxun.xqlc.Beans.CashSuccessBeen;
import com.rongxun.xqlc.Beans.Freebean;
import com.rongxun.xqlc.Beans.funds.UserCashBean;
import com.rongxun.xqlc.Beans.user.UserRechargeBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.CashDialog;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LMessageDialog;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.MessageDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.ClickEvent;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;


public class CashActivity extends MyBaseActivity {

    TextView cashAbleMoney;
    EditText cashMoneyNumber;
    TextView cashBank;
    TextView count;
    Button cashActionSubmit;
    TextView cashChargedTimes;
    ImageView cashBankIcon;
    IconFontTextView back;

    //提现后是否重新访问
//    public static boolean isAgain = false;
    private final String TAG = "提现";
    private UserCashBean userCashBean;
    private MessageDialog messageDialog;
    private String currentPhoneNo;
    private String currentMoney;
    private int clickCount = 0;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    bund.setVisibility(View.VISIBLE);
                    updateViews(userCashBean);
                    break;
                case 0x333:
                    dialog.dismiss();
                    Intent intentSuccess=new Intent();
                    intentSuccess.setClass(CashActivity.this,CashSuccessActivity.class);
                    intentSuccess.putExtra("money",cashMoneyNumber.getText().toString());
                    intentSuccess.putExtra("icon",userCashBean.getBankId());
                    intentSuccess.putExtra("CardNo",userCashBean.getCardNo());
                    intentSuccess.putExtra("BankName",userCashBean.getBankName());
                    intentSuccess.putExtra("cashMoney",resultBean.getTxsxf()+"");
                    intentSuccess.putExtra("ArriveMoney",resultBean.getSjdzje()+"");
                    intentSuccess.putExtra("iconUrl",resultBean.getIcon());

                    Intent intent = new Intent("LoginContentBroadCast");
                    intent.putExtra("Quickly", "cash");
                    CashActivity.this.sendBroadcast(intent);
                    startActivity(intentSuccess);
                    finish();
                    break;

                case 0x444:
                    commissionNet();
                    break;
                default:
                    break;
            }


        }
    };
    private TextView charge;
    private LinearLayout bund;
    private String userId;
    //请求可体现数据
    String basicUrl = AppConstants.URL_SUFFIX + "/rest/cashTo";
    String chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
    private CashActivity.CashFragmentBroadCase broadCase;
    private LoadingDialog loadingDialog;
    private TextView freeMoney;
    private CashDialog dialog;
    private CashSuccessBeen resultBean;
    private int commission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_cash);
        Intent intent = getIntent();
        currentPhoneNo = intent.getStringExtra("currentPhoneNo");
        currentMoney = intent.getStringExtra("totalIncome");
        IntentFilter intentFilter = new IntentFilter("CashFragmentBroadCase");
        broadCase = new CashActivity.CashFragmentBroadCase();
        registerReceiver(broadCase, intentFilter);

        //初始化控件
        initView();
        //网络请求
        RequestForRechargeTo(chargeUrl);

        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CashActivity.this, TxRecordActivity.class);
                startActivity(intent);
            }
        });

        cashChargedTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LMessageDialog dialog=new LMessageDialog(CashActivity.this, R.style.ActionSheetDialogStyle);
                dialog.setHead("提现规则说明");
                dialog.setMessage("1、免费提现额度=项目回款金额（本金+利息）+已获得的平台现金奖励。\n" +
                        "2、提现手续费=超出免费提现额度的金额*0.2%，最低为1元。\n" +
                        "3、提现到账时间：工作日下午16:00之前申请的提现会在当日18:00左右到账；16:00后申请的提现将在下个工作日到账；周末及法定假日提现，将在下个工作日到帐。");
                dialog.show();
                dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cashMoneyNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(cashMoneyNumber.getText().toString()) && cashMoneyNumber.getText().toString().length() > 2) {
                    mHandler.removeCallbacksAndMessages(null);
                    cashActionSubmit.setEnabled(false);
                    mHandler.sendEmptyMessageDelayed(0x444, 1000);

                } else {
                    String  Scommission="提现手续费："+ "<font color='#fa5454'>"+"0"+ "</font>"+" 元";
                    count.setText(Html.fromHtml(Scommission));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initView() {

        bund = (LinearLayout) findViewById(R.id.cash_bund);
        freeMoney = (TextView) findViewById(R.id.cash_free_money);
        charge = (TextView) findViewById(R.id.charge_and_cash_toolbar_title);
        cashAbleMoney = (TextView) findViewById(R.id.cash_able_money);
        cashMoneyNumber = (EditText) findViewById(R.id.cash_money_number);
        cashBank = (TextView) findViewById(R.id.cash_bank);
        count = (TextView) findViewById(R.id.cash_count);
        cashActionSubmit = (Button) findViewById(R.id.cash_action_submit);
        cashChargedTimes = (TextView) findViewById(R.id.cash_charged_times);
        cashBankIcon = (ImageView) findViewById(R.id.cash_bank_icon);
        back = (IconFontTextView) findViewById(R.id.cash_back);
        //默认提现手续费0元
        String  Scommission="提现手续费："+ "<font color='#fa5454'>"+"0"+ "</font>"+" 元";
        count.setText(Html.fromHtml(Scommission));
    }


    public void forgetSafePassOnClick() {
        Intent intent = new Intent();
        intent.setClass(this, ForgetSafePassActivity.class);
        intent.putExtra("currentPhoneNo", currentPhoneNo);
        startActivityForResult(intent, 8078);

    }

    public void updateViews(final UserCashBean entity) {
        int resId;
        cashBank.setText(entity.getBankName() + "（尾号" + entity.getCardNo().substring(entity.getCardNo().length() - 4, entity.getCardNo().length()) + "）");
        int wholeTimes = Integer.parseInt(entity.getCashChargeTimes());
        int userTimes = Integer.parseInt(entity.getUserCashChargeTimes());

        SpannableStringBuilder usedTimeText = new SpannableStringBuilder(userTimes + "笔");
        usedTimeText.setSpan(new ForegroundColorSpan(Color.RED), 0, usedTimeText.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        cashAbleMoney.setText(entity.getAbleMoney());
        freeMoney.setText(entity.getCashFeeMoney() + "");
        cashActionSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (cashMoneyNumber.getText().toString() == null || cashMoneyNumber.getText().toString().equals("")) {
                            Toast.makeText(CashActivity.this, "请输入提现金额", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (Float.parseFloat(cashMoneyNumber.getText().toString()) < 100) {
                            Toast.makeText(CashActivity.this, "提现金额必须大于100", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (clickCount > 0) {
                            Toast.makeText(CashActivity.this, "数据处理中。。", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //如果快速点击了两次
                        if (ClickEvent.isFastDoubleClick(R.id.cash_action_submit)) {
                            Toast.makeText(CashActivity.this, "请勿重复点击", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        cashActionSubmit.setEnabled(false);
                        clickCount += 1;
                        dialog = new CashDialog(CashActivity.this, R.style.ActionSheetDialogStyle);
                        dialog.getPrompt().setVisibility(View.INVISIBLE);
                        dialog.setTextVisibility(cashMoneyNumber.getText().toString(),commission);
                        dialog.setCancelable(false);
                        dialog.getForget().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                forgetSafePassOnClick();
                            }
                        });
                        final String basicUrl = AppConstants.URL_SUFFIX + "/rest/cashSave";

                        dialog.getTextView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RequestForTX(basicUrl, cashMoneyNumber.getText().toString(), dialog.getPasswordE().getText().toString());
                            }
                        });
                        dialog.getCancle().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                cashActionSubmit.setEnabled(true);
                                clickCount=0;
                            }
                        });
                    }
                }
        );


    }

    /**
     * 请求可提现数据
     *
     * @param basicUrl
     */
    public void RequestForListData(String basicUrl) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Toast.makeText(CashActivity.this, "请求数据失败，请检查网络！", Toast.LENGTH_SHORT).show();
                        if (e != null && e.getMessage() != null) {
                            if (e != null && e.getMessage() != null) {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, "response json:" + s);
                        final UserCashBean resultBean = JSON.parseObject(s, UserCashBean.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            userCashBean = resultBean;
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);
                        } else if (resultBean.getRcd().equals("E0001")) {
                            startActivityForResult(new Intent(CashActivity.this, LoginActivity.class), 1400);
                            CashActivity.this.overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(CashActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            CashActivity.this.finish();
                        } else if (resultBean.getRcd().equals("M00015_3")) {

                        } else {
                            Toast.makeText(CashActivity.this, resultBean.getRmg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * 请求提现
     *
     * @param basicUrl
     */
    public void RequestForTX(String basicUrl, String cashMoney, String safePass) {

        if (CheckFormData(cashMoney, safePass)) {
            if (loadingDialog == null) {

                loadingDialog = new LoadingDialog(CashActivity.this);
                loadingDialog.showLoading();
            }
            String url = basicUrl;

            OkHttpUtils.post()
                    .url(url)
                    .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                    .addParams("cashMoney", cashMoney)
                    .addParams("safepwd", safePass)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismissLoading();
                                loadingDialog = null;
                            }
                            cashActionSubmit.setEnabled(true);
                            clickCount = 0;
                            Toast.makeText(CashActivity.this, "操作失败，请检查网络！", Toast.LENGTH_SHORT).show();
                            if (e != null && e.getMessage() != null) {
                                Log.i(TAG, e.getMessage());
                            }
                        }

                        @Override
                        public void onResponse(String s, int id) {
                            Log.i(TAG, "response json:" + s);
                            resultBean = JSON.parseObject(s, CashSuccessBeen.class);
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismissLoading();
                                loadingDialog = null;
                            }
                            cashActionSubmit.setEnabled(true);
                            clickCount = 0;
                            if (resultBean.getRcd().equals("R0001")) {
                                Message msg = new Message();
                                msg.what = 0x333;
                                mHandler.sendMessage(msg);
                            } else {
                                Toast.makeText(CashActivity.this, resultBean.getRmg() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }
    }

    /**
     * 检验数据完整
     *
     * @param cashMoney
     * @param safePass
     * @return
     */
    public boolean CheckFormData(String cashMoney, String safePass) {
        if (cashMoney == null || cashMoney.equals("")) {
            Toast.makeText(this, "请输入提现金额", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (safePass == null || safePass.equals("")) {
            Toast.makeText(this, "请输入交易密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * 获取银行卡信息
     * u
     *
     * @param url
     */
    public void RequestForRechargeTo(String url) {

        OkHttpUtils.post()
                .url(url)
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
                        final UserRechargeBean recharge = new Gson().fromJson(s, UserRechargeBean.class);
                        if (recharge.getRcd().equals("R0001")) {
                            Glide.with(CashActivity.this).load(recharge.getBankIcon()).into(cashBankIcon);
                            //请求成功
                            userId = recharge.getUserId();
                            RequestForListData(basicUrl);

                        } else if (recharge.getRcd().equals("E0001")) {
                            CashActivity.this.startActivityForResult(new Intent(CashActivity.this, LoginActivity.class), 1400);
                            CashActivity.this.overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(CashActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            CashActivity.this.finish();
                        } else if (recharge.getRcd().equals("M00010")) {

                        } else {
                            //请求失败
                            Toast.makeText(CashActivity.this, recharge.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void commissionNet() {
        String commissionUrl = AppConstants.URL_SUFFIX + "/rest/cashChange";


        OkHttpUtils.post()
                .url(commissionUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("cashMoney", cashMoneyNumber.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Toast.makeText(CashActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        final Freebean freebean = new Gson().fromJson(s, Freebean.class);
                        if (freebean.getRcd().equals("R0001")) {
                            commission = freebean.getFee();
                            String  Scommission="提现手续费："+ "<font color='#fa5454'>"+freebean.getFee()+ "</font>"+" 元";
                            count.setText(Html.fromHtml(Scommission));
                            cashActionSubmit.setEnabled(true);
                        } else {
                            cashActionSubmit.setEnabled(false);
                            Toast.makeText(CashActivity.this, freebean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCase);
        Intent intent = new Intent("LoginContentBroadCast");
        intent.putExtra("Quickly", "cash");
        sendBroadcast(intent);
        CustomApplication.removeActivity(this);
    }

    class CashFragmentBroadCase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            RequestForRechargeTo(chargeUrl);
        }
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
