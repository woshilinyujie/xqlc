package com.rongxun.xqlc.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Activities.QuicklyPlayActivity;
import com.rongxun.xqlc.Beans.bank.RbOrderBean;
import com.rongxun.xqlc.Beans.user.UserRechargeBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.ClickEvent;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.io.IOException;

/**
 * Created by linyujie on 16/7/18.
 */
public class TopUpDialog extends Dialog {
    private Context context;
    private ImageView iv;
    private Button button;
    private TextView tv;
    private String userId;
    private TextView top_up;
    private TextView cancel;
    private TextView money;
    private String TopUpMoney;
    private String TAG = "自动充值";
    private String realName;
    private String cardNo;
    private String cardId;
    private Activity activity;
    private String tenderMoney;
    private String project;
    private String selectedIdArrayString;
    private int projectId;
    private String exceptS;
    private String jiaxijuanID="0";
    private int  hongbaoCount;
    String chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
    String url = AppConstants.URL_SUFFIX + "/rest/rechargeSave";
    public final static int REQUEST_CODE_BAOFOO_SDK = 100;
    private MessageDialog messageDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111://认证提交
                    String url = AppConstants.URL_SUFFIX + "/rest/rechargeSaveHnew";
                    RequestForRechargeData(url, TopUpMoney);
                    break;
                case 0x222:


                    break;
                default:
                    break;

            }
        }
    };
    private LoadingDialog loadingDialog;
    private TextView msg;

    public TopUpDialog(Context context) {
        super(context, 0);
    }

    public TopUpDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initData();
        initListenet();
    }

    private void initListenet() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //点击充值
        top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果快速点击了两次
                if (ClickEvent.isFastDoubleClick(R.id.charge_action_submit)) {
                    return;
                }
                dismiss();
                //调用接口，返回订单信息
                RequestForRechargeTo(chargeUrl);
            }
        });

    }


    private void initView() {
        View view = View.inflate(context, R.layout.top_up_dialog, null);
        top_up = (TextView) view.findViewById(R.id.top_up);
        cancel = (TextView) view.findViewById(R.id.top_up_cancel);
        money = (TextView) view.findViewById(R.id.top_up_money);
        msg = (TextView) view.findViewById(R.id.top_up_msg);
        setCancelable(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();


        setContentView(view);

    }

    private void initData() {

    }

    public void RequestForRechargeData(String basicUrl, final String chargeMoney) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(activity);
            loadingDialog.showLoading();
        }

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(context, "loginToken", ""))
                .addParams("userId", userId)
                .addParams("idNo", cardId)
                .addParams("realName", realName)
                .addParams("cardNo", cardNo)
                .addParams("userAccountRecharge.money", chargeMoney)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null || e.getMessage() != null) {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(activity, QuicklyPlayActivity.class);
                            intent.putExtra("order", resultBean.getOrder_no());
                            intent.putExtra("inverstMark", "inverstMark");
                            intent.putExtra("tenderMoney", tenderMoney);//投资金额
                            intent.putExtra("selectedIdArrayString", selectedIdArrayString);
                            intent.putExtra("project", project);
                            intent.putExtra("jiaxijuanID", jiaxijuanID);
                            intent.putExtra("projectId", projectId);
                            intent.putExtra("exceptS",exceptS);//收益
                            intent.putExtra("hongbaoCount",hongbaoCount);//使用的红包金额

                            intent.putExtra("chargeMoney",chargeMoney);
                            intent.putExtra("cardNo", cardNo);
                            intent.putExtra("userId", userId);
                            intent.putExtra("realName", realName);
                            intent.putExtra("idNo", cardId);

                            activity.startActivity(intent);
                        } else {
                            Toast.makeText(activity, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    /**
     * 获取银行卡信息
     *
     * @param url
     */
    public void RequestForRechargeTo(String url) {
        if (loadingDialog == null) {

            loadingDialog = new LoadingDialog(activity);
            loadingDialog.showLoading();
        }

        OkHttpUtils.post()
                .url(url)
                .addParams("token", PreferenceUtil.getPrefString(context, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        Toast.makeText(context, "银行卡信息查询失败", Toast.LENGTH_SHORT).show();
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        final UserRechargeBean recharge = new Gson().fromJson(s, UserRechargeBean.class);
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
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);

                        } else if (recharge.getRcd().equals("E0001")) {
                            Toast.makeText(context, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                        } else {
                            //请求失败
                            Toast.makeText(context, recharge.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void setData(String TopUpMoney,  Activity activity, String tenderMoney,
                         String selectedIdArrayString, String project, int projectId,String exceptS,int hongbaoCount,String jiaxijuanID) {
        this.jiaxijuanID=jiaxijuanID;
        this.activity = activity;
        this.project = project;
        this.exceptS=exceptS;
        this.projectId = projectId;
        this.tenderMoney = tenderMoney;
        this.hongbaoCount=hongbaoCount;
        this.selectedIdArrayString = selectedIdArrayString;
        if (Double.parseDouble(TopUpMoney) < 3) {
            this.TopUpMoney = 3 + "";
            money.setVisibility(View.GONE);
            msg.setText("您的账户余额不足，还需充值3元（平台最小充值额为3元）");
        } else {
            money.setVisibility(View.VISIBLE);
            this.TopUpMoney = TopUpMoney;
            money.setText(TopUpMoney + "元");
        }
//        show();
        //直接默认点击充值
        RequestForRechargeTo(chargeUrl);
    }


}
