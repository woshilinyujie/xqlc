package com.rongxun.xqlc.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baofoo.sdk.vip.BaofooPayActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.InnerTransBeans.ActivityToFragmentMessage;
import com.rongxun.xqlc.Beans.bank.RbOrderBean;
import com.rongxun.xqlc.Beans.user.UserRechargeBean;
import com.rongxun.xqlc.R;
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

public class
ChargeActivity extends MyBaseActivity {
    TextView chargeAbleMoney;
    EditText chargeMoneyNumber;
    //    TextView chargeSafePassForget;
    TextView chargeBank;
    Button chargeActionSubmit;
    ImageView chargeBankIcon;

    private String TAG = "充值";
    private MessageDialog messageDialog;
    private final static int REQUEST_CODE_BAOFOO_SDK = 100;
    private int flag = 0;

    private String userId = "";
    private String realName = "";
    private String cardNo = "";
    private String cardId = "";
    private String registerTime = "";
    private String ableMoney = "";
    private String bankId = "";
    private String bankName = "";
    private String phone = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111://认证提交
//                    safr_rl.setVisibility(View.VISIBLE);
                    bund.setVisibility(View.VISIBLE);
                    if (bankName.equals("")) {
                        flag = 4;
                    } else {
                        flag = 2;
                    }
                    chargeBank.setText(bankName + "（尾号" + cardNo.substring(cardNo.length() - 4, cardNo.length()) + "）");
                    chargeAbleMoney.setText(ableMoney + "元");
                    Glide.with(ChargeActivity.this).load(recharge.getBankIcon()).into(chargeBankIcon);
                    upper_limit.setText(upper_limitS);


                    break;
                case 0x222:
                    Intent payintent = new Intent(ChargeActivity.this, BaofooPayActivity.class);
                    // 通过业务流水请求报文获得的交易号
                    payintent.putExtra(BaofooPayActivity.PAY_TOKEN, msg.obj.toString());
                    // 标记是否为测试，传True为正式环境，不传或者传False则为测试调用
                    payintent.putExtra(BaofooPayActivity.PAY_BUSINESS, false);
                    startActivityForResult(payintent, REQUEST_CODE_BAOFOO_SDK);

                    break;
                default:
                    break;

            }
        }
    };

    private MessageDialog payResultDialog;//支付结果
    private TextView charge;
    private ChargeActivity.ChargeFragmentBroadCase broadCase;
    private String chargeUrl;
    private LinearLayout bund;
    private LoadingDialog loadingDialog;
    private String money;
    private IconFontTextView back;
    private TextView upper_limit;
    private String upper_limitS;
    private TextView chargeProblem;
    private UserRechargeBean recharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_charge);
        flag = 0;
        broadCase = new ChargeActivity.ChargeFragmentBroadCase();
        IntentFilter intentFilter = new IntentFilter("ChargeFragmentBroadCase");
        registerReceiver(broadCase, intentFilter);
        chargeAbleMoney = (TextView) findViewById(R.id.charge_able_money);
        upper_limit = (TextView) findViewById(R.id.upper_limit);
        chargeMoneyNumber = (EditText) findViewById(R.id.charge_money_number);
        chargeBank = (TextView) findViewById(R.id.charge_bank);
        chargeActionSubmit = (Button) findViewById(R.id.charge_action_submit);
        chargeBankIcon = (ImageView) findViewById(R.id.charge_bank_icon);
        back = (IconFontTextView) findViewById(R.id.charge_back);
        chargeProblem = (TextView) findViewById(R.id.charge_problem);
        bund = (LinearLayout) findViewById(R.id.charge_and_cash_bund);
        charge = (TextView) findViewById(R.id.charge_and_cash_toolbar_title);
        chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
        chargeAbleMoney.setText(getIntent().getStringExtra("money"));
        RequestForRechargeTo(chargeUrl);
        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeActivity.this, RechargeRecordActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chargeActionSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClick();
            }
        });
        chargeProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                            startActivity(new Intent(ChargeActivity.this, SafeControlActivity.class));
                final LMessageDialog dialog=new LMessageDialog(ChargeActivity.this,R.style.ActionSheetDialogStyle);
                dialog.setMessage("1、资金同卡进出，只支持绑定一张银行卡。如需更换银行卡，请联系客服400-033-3113\n 2、若遇到银行卡充值受限，建议您在PC端通过网银支付充值\n 3、支付上限会随时变更，实际按银行规定额度而定");
                dialog.show();
                dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void onSubmitClick() {
        //如果快速点击了两次
        if (ClickEvent.isFastDoubleClick(R.id.charge_action_submit)) {
            return;
        }

        money = chargeMoneyNumber.getText().toString();
        if (TextUtils.isEmpty(money.toString())) {
            Toast.makeText(ChargeActivity.this, "请输入充值金额", Toast.LENGTH_LONG).show();
            return;
        }
        if (Float.parseFloat(money) < 3) {
            Toast.makeText(ChargeActivity.this, "充值金额最少为3元", Toast.LENGTH_LONG).show();
            return;
        }

        if (flag == 0) {
            return;
        } else if (flag == 2) {
            //调用接口，返回订单信息
            String url = AppConstants.URL_SUFFIX + "/rest/rechargeSaveHnew";
            RequestForRechargeData(url, money, "");
            chargeActionSubmit.setEnabled(false);
        } else if (flag == 1) {
            //进入绑卡页面
            Intent intent = new Intent();
            intent.setClass(ChargeActivity.this, RealNameAuthActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("money", money + "");
            ChargeActivity.this.startActivity(intent);
        } else if (flag == 4) {
            //进入绑卡页面
            Toast.makeText(ChargeActivity.this, "招商银行暂不支持快捷支付，请您联系客服", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取银行卡信息
     *
     * @param url
     */
    public void RequestForRechargeTo(String url) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ChargeActivity.this);
            loadingDialog.showLoading();
        }
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
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        recharge = new Gson().fromJson(s, UserRechargeBean.class);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        if (recharge.getRcd().equals("R0001")) {
                            //请求成功

                            userId = recharge.getUserId();
                            realName = recharge.getRealName();
                            cardNo = recharge.getCardNo();
                            cardId = recharge.getCardId();
                            registerTime = recharge.getRegisterTime();
                            ableMoney = recharge.getAbleMoney();
                            bankId = recharge.getBnakId();
                            bankName = recharge.getBankName();
                            phone = recharge.getPhone();
                            upper_limitS = recharge.getBankLimit();
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);

                        } else if (recharge.getRcd().equals("E0001")) {
                            ChargeActivity.this.startActivityForResult(new Intent(ChargeActivity.this, LoginActivity.class), 1400);
                            ChargeActivity.this.overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(ChargeActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            ChargeActivity.this.finish();
                        } else if (recharge.getRcd().equals("M00010")) {
                            //没有绑定银行卡 点击确定进入绑卡页面
                            flag = 1;
//                            safr_rl.setVisibility(View.GONE);
                        } else {
                            if (recharge != null)
                                //请求失败
                                Toast.makeText(ChargeActivity.this, recharge.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    /**
     * 请求充值（一）
     */

    public void RequestForRechargeData(String basicUrl, final String chargeMoney, String safePwd) {

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ChargeActivity.this);
            loadingDialog.showLoading();
        }

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("userId", userId)
                .addParams("idNo", cardId)
                .addParams("realName", realName)
                .addParams("cardNo", cardNo)
                .addParams("safepwd", safePwd)
                .addParams("userAccountRecharge.money", chargeMoney)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        chargeActionSubmit.setEnabled(true);
                        if (e != null || e.getMessage() != null) {
                            Toast.makeText(ChargeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        final RbOrderBean resultBean = new Gson().fromJson(s, RbOrderBean.class);
                        Log.i(TAG, "（一）:" + s);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        chargeActionSubmit.setEnabled(true);
                        if (resultBean.getRcd().equals("R0001")) {

                            Intent intent = new Intent(ChargeActivity.this, QuicklyPlayActivity.class);
                            intent.putExtra("order", resultBean.getOrder_no());

                            intent.putExtra("chargeMoney",chargeMoney);
                            intent.putExtra("cardNo", cardNo);
                            intent.putExtra("userId", userId);
                            intent.putExtra("realName", realName);
                            intent.putExtra("idNo", cardId);
                            ChargeActivity.this.startActivity(intent);
                        } else if (resultBean.getRcd().equals("R0003")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ChargeActivity.this);
                            dialog.setCancelable(false);
                            dialog.setTitle(resultBean.getRmg())
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setClass(ChargeActivity.this, RealNameAuthActivity.class);
                                            intent.putExtra("userId", userId);
                                            intent.putExtra("money", money + "");
                                            ChargeActivity.this.startActivity(intent);

                                        }
                                    }).show();

                        } else {
                            Toast.makeText(ChargeActivity.this, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BAOFOO_SDK) {
            String result = "", msg = "";
            if (data == null || data.getExtras() == null) {
                msg = "支付已被取消";
            } else {
                //result返回值判断 -1:失败  0:取消  1:成功  10:处理中
                result = data.getExtras().getString(BaofooPayActivity.PAY_RESULT);
                msg = data.getExtras().getString(BaofooPayActivity.PAY_MESSAGE);
            }

            //刷新btype
            String chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
            RequestForRechargeTo(chargeUrl);
            //CashFragment.isAgain = true;

            //通知用户中心刷新
            ActivityToFragmentMessage message = new ActivityToFragmentMessage();
            message.setTag(2004);
            // TODO: 2017/8/8 0008 通知用户中心刷新
            // TODO: 2017/8/8 0008 @祁正伟
            // TODO: 2017/8/8 0008 .....

            messageDialog = new MessageDialog(this);
            messageDialog.setCanceledOnTouchOutside(false);
            messageDialog.setOnPositiveListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            messageDialog.dismiss();
                            messageDialog = null;

                        }
                    }
            );
            messageDialog.setMessage(msg);
            messageDialog.show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCase);
        sendBroadcast(new Intent("LoginContentBroadCast"));
        CustomApplication.removeActivity(this);
    }

    class ChargeFragmentBroadCase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("finish").equals("finish")){
                finish();
            }else{
                RequestForRechargeTo(chargeUrl);
            }
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
