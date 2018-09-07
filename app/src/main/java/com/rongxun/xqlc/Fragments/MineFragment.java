package com.rongxun.xqlc.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.liaoinstan.springview.widget.SpringView;
import com.rongxun.xqlc.Activities.AccountManageActivity;
import com.rongxun.xqlc.Activities.ActivityCenterActivity;
import com.rongxun.xqlc.Activities.ArticleListActivity;
import com.rongxun.xqlc.Activities.AssetstatisticsActivity;
import com.rongxun.xqlc.Activities.BackMoneyActivity;
import com.rongxun.xqlc.Activities.CashActivity;
import com.rongxun.xqlc.Activities.ChargeActivity;
import com.rongxun.xqlc.Activities.CustomerActivity;
import com.rongxun.xqlc.Activities.FeedBackActivity;
import com.rongxun.xqlc.Activities.H5JSActivity;
import com.rongxun.xqlc.Activities.HongBaoActivity;
import com.rongxun.xqlc.Activities.InvestRecordActivity;
import com.rongxun.xqlc.Activities.InvitedToRecordActivity;
import com.rongxun.xqlc.Activities.LoginActivity;
import com.rongxun.xqlc.Activities.MessageCenterActivity;
import com.rongxun.xqlc.Activities.MoreActivity;
import com.rongxun.xqlc.Activities.RewardActivity;
import com.rongxun.xqlc.Activities.SafePassWordActivity;
import com.rongxun.xqlc.Activities.TxRecordActivity;
import com.rongxun.xqlc.Activities.UserAccDetailActivity;
import com.rongxun.xqlc.Beans.CustomerServiceBean;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.Beans.userInfo.PrincipalBeen;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.CancleMessageDialog;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.SpringHeader;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.AppVersionUtils;
import com.rongxun.xqlc.Util.KeepTwoUtil;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private Gson gson = new Gson();
    private final String TAG = "我的账户";
    private TextView userName;//账号
    private TextView investMOney;//投资记录
    private TextView payBackMoney;//回款记录
    private TextView shouyiMoney;//累计收益
    private TextView chargeAndCash;
    private TextView chargeAndcharge;
    private RelativeLayout moneyRecord;
    private RelativeLayout investRecordLayout;
    private LinearLayout moneyBackRecordLayout;
    private boolean isClose = true;
    private RelativeLayout hot_activity_rl, recommend_courteous_rl, help_center_rl, server_rl,  more_rl;
    private TextView setting_rl;
    private double totalIncome = 0.00;
    private Activity mActivity;
    private View rootView;
    private IconFontTextView icon;
    private SharedPreferences preferences;
    private TextView today;
    private LoginContentBroadCast broadCast;
    private LinearLayout allProrert;
    private PrincipalBeen principalBeen;
    private RelativeLayout hongbao;
    private RelativeLayout reward;
    private TextView back_money_count;
    private TextView login_content_invest_invest_count, login_content_invest_money_count, login_content_invest_hongbao_count, login_content_invest_award_count, login_content_recommend_award_count;
    //登录注册
    private RelativeLayout login_content_login_registration;
    //未登录显示登录注册
    private LinearLayout login_content_login_registration_ll;
    //已登录显示的手机号码
    private TextView login_content_account_name;
    //已登录显示各种金额
    private RelativeLayout login_content_islogin;
    //已登录显示小黄条
    private RelativeLayout login_context_message;
    //账户管理
    private LinearLayout mine_ll_already_logged_in;
    private String userName1;
    //总资产显示和隐藏
    private ImageView login_content_cash_show_hide;

    String touzi = "<font color='#FF0000'>" + "--" + "</font>" + "笔在投";
    String huikuan = "<font color='#FF0000'>" + "--" + "</font>" + "天后回款";
    String hb = "<font color='#FF0000'>" + "--" + "</font>" + "元";
    String jiangli = "已获得" + "<font color='#FF0000'>" + "--" + "</font>" + "元";
    String yaoqing = "已获得" + "<font color='#FF0000'>" + "--" + "</font>" + "元";
    private String userId;
    private String currentPhoneNo;
    private TextView txz_cash;
    private String txz_cashS;
    private BankList resultBean;
    private TextView hot_activity;
    private TextView my_work_time;
    private TextView version_num;
    private LinearLayout login_content_invest_layout;
    private LinearLayout login_content_money_back_layout;
    private LinearLayout login_content_my_recommend;
    private CustomerServiceBean bean;
    private LoadingDialog loadingDialog;
    private String hongbao_countS;
    private TextView phone;


    public MineFragment() {
    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public MineFragment(Activity activity) {
        mActivity=activity;
        rootView = View.inflate(mActivity,R.layout.fragment_mine,null);
        initViews();
    }

//    public static MineFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        MineFragment fragment = new MineFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void initViews() {
        progressDialog = new ProgressDialog(mActivity);
        preferences = mActivity.getSharedPreferences("AppToken", mActivity.MODE_PRIVATE);
        broadCast = new LoginContentBroadCast();
        IntentFilter intentFilter = new IntentFilter("LoginContentBroadCast");
        mActivity.registerReceiver(broadCast, intentFilter);
        today = (TextView) rootView.findViewById(R.id.login_content_invest_today);
        userName = (TextView) rootView.findViewById(R.id.login_content_account_name);
        investMOney = (TextView) rootView.findViewById(R.id.login_content_invest_money);
        payBackMoney = (TextView) rootView.findViewById(R.id.login_content_repay_money);
        shouyiMoney = (TextView) rootView.findViewById(R.id.login_content_all_profit);
        investRecordLayout = (RelativeLayout) rootView.findViewById(R.id.login_content_invest_record);
        moneyBackRecordLayout = (LinearLayout) rootView.findViewById(R.id.login_content_money_back);
        chargeAndcharge = (TextView) rootView.findViewById(R.id.login_content_table_row_charge);
        hongbao = (RelativeLayout) rootView.findViewById(R.id.login_content_invest_hongbao);
        chargeAndCash = (TextView) rootView.findViewById(R.id.login_content_table_row_cash);
        back_money_count = (TextView) rootView.findViewById(R.id.login_content_back_money_count);
        moneyRecord = (RelativeLayout) rootView.findViewById(R.id.login_content_table_row_money_record);
        allProrert = (LinearLayout) rootView.findViewById(R.id.login_content_today_layout);
        reward = (RelativeLayout) rootView.findViewById(R.id.login_content_invest_reward);
        login_content_invest_layout = (LinearLayout) rootView.findViewById(R.id.login_content_invest_layout);
        login_content_money_back_layout = (LinearLayout) rootView.findViewById(R.id.login_content_money_back_layout);
        login_content_my_recommend = (LinearLayout) rootView.findViewById(R.id.login_content_my_recommend);
        phone = (TextView) rootView.findViewById(R.id.login_content_account_phone);
        login_content_account_name = (TextView) rootView.findViewById(R.id.login_content_account_name);
        mine_ll_already_logged_in = (LinearLayout) rootView.findViewById(R.id.mine_ll_already_logged_in);
        login_content_login_registration_ll = (LinearLayout) rootView.findViewById(R.id.login_content_login_registration_ll);
        login_content_islogin = (RelativeLayout) rootView.findViewById(R.id.login_content_islogin);
        login_context_message = (RelativeLayout) rootView.findViewById(R.id.login_context_message);
        txz_cash = (TextView) rootView.findViewById(R.id.txz_cash);
        login_content_login_registration = (RelativeLayout) rootView.findViewById(R.id.login_content_login_registration);


        login_content_invest_invest_count = (TextView) rootView.findViewById(R.id.login_content_invest_invest_count);
        login_content_invest_money_count = (TextView) rootView.findViewById(R.id.login_content_invest_money_count);
        login_content_invest_hongbao_count = (TextView) rootView.findViewById(R.id.login_content_invest_hongbao_count);
        login_content_invest_award_count = (TextView) rootView.findViewById(R.id.login_content_invest_award_count);
        login_content_recommend_award_count = (TextView) rootView.findViewById(R.id.login_content_recommend_award_count);

        hot_activity_rl = (RelativeLayout) rootView.findViewById(R.id.hot_activity_rl);
        recommend_courteous_rl = (RelativeLayout) rootView.findViewById(R.id.recommend_courteous_rl);
        help_center_rl = (RelativeLayout) rootView.findViewById(R.id.help_center_rl);
        server_rl = (RelativeLayout) rootView.findViewById(R.id.server_rl);
        setting_rl = (TextView) rootView.findViewById(R.id.setting_rl);
        more_rl = (RelativeLayout) rootView.findViewById(R.id.more_rl);

        login_content_cash_show_hide = (ImageView) rootView.findViewById(R.id.login_content_cash_show_hide);


        hot_activity = (TextView) rootView.findViewById(R.id.hot_activity);
        my_work_time = (TextView) rootView.findViewById(R.id.my_work_time);
        version_num = (TextView) rootView.findViewById(R.id.version_num);
        version_num.setText(AppVersionUtils.getVersionName(mActivity) + "");

        /////////////////////////////////
        // TODO: 2017/8/7 0007 初始化弹性控件  测试
        initSpring();


        hot_activity_rl.setOnClickListener(this);
        recommend_courteous_rl.setOnClickListener(this);
        help_center_rl.setOnClickListener(this);
        server_rl.setOnClickListener(this);
        setting_rl.setOnClickListener(this);
        more_rl.setOnClickListener(this);
        login_content_cash_show_hide.setOnClickListener(this);

        mine_ll_already_logged_in.setOnClickListener(this);
        chargeAndCash.setOnClickListener(this);
        chargeAndcharge.setOnClickListener(this);
        moneyRecord.setOnClickListener(this);
        investRecordLayout.setOnClickListener(this);
        moneyBackRecordLayout.setOnClickListener(this);
        allProrert.setOnClickListener(this);
        hongbao.setOnClickListener(this);
        reward.setOnClickListener(this);
        login_content_my_recommend.setOnClickListener(this);
        login_content_invest_layout.setOnClickListener(this);
        today.setOnClickListener(this);
        login_content_money_back_layout.setOnClickListener(this);

        login_content_cash_show_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClose) {
                    isClose = false;
                    login_content_cash_show_hide.setBackgroundResource(R.mipmap.login_close);
                    today.setText("****");
                    investMOney.setText("****");
                    shouyiMoney.setText("****");
                    payBackMoney.setText("****");
                } else {
                    isClose = true;
                    login_content_cash_show_hide.setBackgroundResource(R.mipmap.login_open);
                    if (principalBeen != null) {
                        today.setText(KeepTwoUtil.keep2Decimal(principalBeen.getTotal()));
                        shouyiMoney.setText(KeepTwoUtil.keep2Decimal(Double.parseDouble(principalBeen.getLjsy())));
                        investMOney.setText(KeepTwoUtil.keep2Decimal(principalBeen.getInvestorCollectionCapital() ));
                        payBackMoney.setText(KeepTwoUtil.keep2Decimal(principalBeen.getAbleMoney()));
                    }
                }
            }
        });


        login_content_login_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                mActivity.startActivity(intent);
            }
        });

    }

    /**
     * 弹性头
     */
    private void initSpring() {

        SpringView springView = (SpringView) rootView.findViewById(R.id.mine_spring);
        springView.setGive(SpringView.Give.NONE);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadmore() {
            }
        });
        springView.setHeader(new SpringHeader(mActivity));
        springView.setFooter(new SpringHeader(mActivity));
    }

    public void isornologin(Activity activity) {
        preferences = activity.getSharedPreferences("AppToken", Context.MODE_PRIVATE);
        if (preferences.getString("loginToken", null) == null) {
            //TODO 未登录
            login_content_account_name.setVisibility(View.GONE);
            login_context_message.setVisibility(View.GONE);
            login_content_invest_invest_count.setText(Html.fromHtml(touzi));
            login_content_invest_money_count.setText(Html.fromHtml(huikuan));
            login_content_invest_hongbao_count.setText(Html.fromHtml(hb));
            login_content_invest_award_count.setText(Html.fromHtml(jiangli));
            login_content_recommend_award_count.setText(Html.fromHtml(yaoqing));


        } else {
            if(login_content_login_registration_ll==null){
                //防止空指针
                initViews();
            }

            login_content_account_name.setVisibility(View.VISIBLE);
            login_content_islogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        RequestForMoney(null, v);
        switch (v.getId()) {

            //热门活动
            case R.id.hot_activity_rl:
                Intent it = new Intent(mActivity, ActivityCenterActivity.class);
                it.putExtra("title", "活动中心");
                mActivity.startActivity(it);

                break;
            case R.id.recommend_courteous_rl:
                Intent h5 = new Intent(mActivity, H5JSActivity.class);
                h5.putExtra("flag", "yaoqing");
                h5.putExtra("web_url", "https://m.hzjcb.com/html/invite/invite.html");
                mActivity.startActivity(h5);
                break;
            case R.id.help_center_rl:
                Intent intent = new Intent(mActivity, ArticleListActivity.class);
                intent.putExtra("title", "帮助中心");
                intent.putExtra("type", "app_help_center");
                mActivity.startActivity(intent);
                break;
            case R.id.server_rl:
                Intent it1 = new Intent(mActivity, CustomerActivity.class);
                try {
                    if (bean.getCustomerMap().getService_hours().getValue() != null)
                        it1.putExtra("time", bean.getCustomerMap().getService_hours().getValue());
                    mActivity.startActivity(it1);
                } catch (Exception e) {

                }
                break;

            case R.id.more_rl:
//                mActivity.startActivity(new Intent(mActivity, MoreActivity.class));
                Intent feedBackIntent = new Intent(mActivity, FeedBackActivity.class);
                startActivity(feedBackIntent);
                break;

        }


    }

    public void exit() {
        login_content_account_name.setVisibility(View.GONE);
        login_content_islogin.setVisibility(View.GONE);
        login_context_message.setVisibility(View.GONE);
        login_content_invest_invest_count.setText(Html.fromHtml(touzi));
        login_content_invest_money_count.setText(Html.fromHtml(huikuan));
        login_content_invest_hongbao_count.setText(Html.fromHtml(hb));
        login_content_invest_award_count.setText(Html.fromHtml(jiangli));
        login_content_recommend_award_count.setText(Html.fromHtml(yaoqing));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
        mActivity.unregisterReceiver(broadCast);
    }


    public void CustomerServiceNet() {
        String url = AppConstants.URL_SUFFIX + "/rest/customerService";
        OkHttpUtils.post()
                .url(url)
                .addParams("key", "customer_service")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
//                            Toast.makeText(mActivity, "请求数据失败，请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        bean = gson.fromJson(s, CustomerServiceBean.class);
                        try {
                            if (bean != null && bean.getRcd().equals("R0001")) {
                                hot_activity.setText(bean.getCustomerMap().getHotactivity());
                                my_work_time.setText(bean.getCustomerMap().getService_hours().getValue());
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }

    //本金利息
    public void RequestForMoney(Activity activity, final View v) {
        final Activity mmActivity;
        if (mActivity == null && activity != null) {
            mmActivity = activity;
            mActivity = activity;
        } else {
            mmActivity = mActivity;
        }
        String url = AppConstants.URL_SUFFIX + "/rest/ajaxIndexVT";
        try{
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(mmActivity);
                loadingDialog.showLoading();
            } else if (loadingDialog != null && !loadingDialog.isShowing()) {
                loadingDialog.showLoading();
            }
        }
        catch (Exception e){

        }

        OkHttpUtils.post()
                .url(url)
                .addParams("token", PreferenceUtil.getPrefString(mmActivity, "loginToken", ""))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    private String award_countS;
                    String invest_money_countS;

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                            Toast.makeText(mActivity, "请求数据失败，请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismissLoading();
                                loadingDialog = null;
                            }
                        } catch (Exception e1) {

                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismissLoading();
                                loadingDialog = null;
                            }
                        } catch (Exception e) {

                        }
                        String s = response.toString();
                        Log.v(TAG, s);
                        principalBeen = gson.fromJson(s, PrincipalBeen.class);
                        if (principalBeen.getRcd().equals("R0001")) {

                            userName1 = principalBeen.getUsername();
                            //根据存在sp中站好的tocken去判断是否进入注册页面
                            isornologin(mmActivity);
                            //请求成功，通知activity
                            if (principalBeen.getUsername() != null && principalBeen.getUsername().length() == 11) {
                                phone.setText(principalBeen.getUsername().substring(0, 3) + "****" + principalBeen.getUsername().substring(7));
                            }
                            userName.setText("*"+principalBeen.getRealName().substring(1,principalBeen.getRealName().length()));
                            if (isClose == true) {
                                today.setText(KeepTwoUtil.keep2Decimal(principalBeen.getTotal()));
                                if (principalBeen.getLjsy() != null) {
                                    shouyiMoney.setText(KeepTwoUtil.keep2Decimal(Double.parseDouble(principalBeen.getLjsy())));
                                }
                                investMOney.setText(KeepTwoUtil.keep2Decimal(principalBeen.getInvestorCollectionCapital() ));
                                payBackMoney.setText(KeepTwoUtil.keep2Decimal(principalBeen.getAbleMoney()));
                            }
                            if (principalBeen.getHbkyye() != null) {
                                hongbao_countS = "<font color='#FF0000'>" + principalBeen.getKqNum()+ "</font>" + "张可用";
                            }
                            if (principalBeen.getJljl() != null) {
                                award_countS = "已获得" + "<font color='#FF0000'>" + KeepTwoUtil.keep2Decimal(Double.parseDouble(principalBeen.getJljl())) + "</font>" + "元";
                            }
                            String invest_countS = "<font color='#FF0000'>" + principalBeen.getTzNum() + "</font>" + "笔在投";
                            if (Integer.parseInt(principalBeen.getDhkjlNum()) == 0) {
                                //没有待汇款记录
                                invest_money_countS = "暂无待收项目";
                            } else {
                                if (Integer.parseInt(principalBeen.getZjhkts()) < 0) {
                                    invest_money_countS = "回款中";
                                } else if (Integer.parseInt(principalBeen.getZjhkts()) == 0) {
                                    invest_money_countS = "今日有回款";
                                } else {
                                    invest_money_countS = "<font color='#FF0000'>" + principalBeen.getZjhkts() + "</font>" + "天后有回款";
                                }
                            }
                            String login_content_recommend_award_countS = "已获得" + "<font color='#FF0000'>" + principalBeen.getTjfriendjl() + "</font>" + "元";

                            if (principalBeen.getTxz() != null) {
                                double cash = Double.parseDouble(principalBeen.getTxz());
                                if (cash > 0) {
                                    login_context_message.setVisibility(View.VISIBLE);
                                    txz_cashS = "您有" + "<font color='#ffc56a'>" + principalBeen.getTxz()  + "</font>"+ "元资金正在提现中...";
                                    txz_cash.setText(Html.fromHtml(txz_cashS));
                                    login_context_message.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(mActivity, TxRecordActivity.class);
                                            mActivity.startActivity(intent);
                                        }
                                    });
                                } else {
                                    login_context_message.setVisibility(View.GONE);
                                }
                            } else {
                                login_context_message.setVisibility(View.GONE);
                            }
                            login_content_invest_hongbao_count.setText(Html.fromHtml(hongbao_countS));
                            login_content_invest_award_count.setText(Html.fromHtml(award_countS));
                            login_content_invest_invest_count.setText(Html.fromHtml(invest_countS));
                            login_content_invest_money_count.setText(Html.fromHtml(invest_money_countS));


                            login_content_recommend_award_count.setText(Html.fromHtml(login_content_recommend_award_countS));
                            if (v != null) {
                                switch (v.getId()) {
                                    case R.id.mine_ll_already_logged_in:
                                        if (userName1 != null) {
                                            Intent intent = new Intent(mActivity, AccountManageActivity.class);
                                            intent.putExtra("userId", userName1);
                                            mActivity.startActivity(intent);
                                        }
                                        break;
                                    case R.id.login_content_invest_today:
                                        Intent AllProperty = new Intent(mActivity, AssetstatisticsActivity.class);
                                        if (principalBeen != null)
                                            AllProperty.putExtra("principalBeen", principalBeen);
                                        mActivity.startActivity(AllProperty);
                                        break;

                                    case R.id.login_content_money_back_layout:
                                        Intent UserAccDetail = new Intent();
                                        UserAccDetail.setClass(mActivity, UserAccDetailActivity.class);
                                        if (userId != null)
                                            UserAccDetail.putExtra("userId", userId);
                                        mActivity.startActivity(UserAccDetail);
                                        break;

                                    case R.id.login_content_table_row_cash:
                                        if (principalBeen != null && principalBeen.getPayPasswordStatus() == 0) {
                                            final CancleMessageDialog dialog = new CancleMessageDialog(mActivity, R.style.ActionSheetDialogStyle);
                                            dialog.setTitle("为了您的账户资金安全，请先设置交易密码");
                                            dialog.setCancleText("暂不考虑");
                                            dialog.setMessage("立即设置");
                                            dialog.show();
                                            dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(mActivity, SafePassWordActivity.class);
                                                    intent.putExtra("MineFragment", "MineFragment");
                                                    mActivity.startActivity(intent);
                                                }
                                            });
                                        } else {
                                            //查询银行卡是否绑定银行卡
                                            RequestForBankData(AppConstants.URL_SUFFIX + "/rest/rechargeTo");
                                        }
                                        break;
                                    case R.id.login_content_table_row_charge:
                                        if (principalBeen != null && principalBeen.getPayPasswordStatus() == 0) {
                                            final CancleMessageDialog dialog = new CancleMessageDialog(mActivity, R.style.ActionSheetDialogStyle);
                                            dialog.setTitle("为了您的账户资金安全，请先设置交易密码");
                                            dialog.setCancleText("暂不考虑");
                                            dialog.setMessage("立即设置");
                                            dialog.show();
                                            dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(mActivity, SafePassWordActivity.class);
                                                    intent.putExtra("MineFragment", "MineFragment");
                                                    mActivity.startActivity(intent);
                                                }
                                            });
                                        } else {
                                            Intent Chargeintent = new Intent(mActivity, ChargeActivity.class);
                                            Chargeintent.putExtra("money", KeepTwoUtil.keep2Decimal(principalBeen.getAbleMoney()));
                                            mActivity.startActivityForResult(Chargeintent, 2008);
                                        }
                                        break;
                                    case R.id.login_content_table_row_money_record:
                                        Intent UserAccDetailintent = new Intent();
                                        UserAccDetailintent.setClass(mActivity, UserAccDetailActivity.class);
                                        if (userId != null)
                                            UserAccDetailintent.putExtra("userId", userId);
                                        mActivity.startActivity(UserAccDetailintent);
                                        break;
                                    case R.id.login_content_invest_record:
                                        Intent InvestRecordintent = new Intent();
                                        InvestRecordintent.setClass(mActivity, InvestRecordActivity.class);
                                        if (userId != null)
                                            InvestRecordintent.putExtra("userId", userId);
                                        mActivity.startActivity(InvestRecordintent);
                                        break;
                                    case R.id.login_content_money_back:
                                        Intent BackMoneyintent = new Intent();
                                        BackMoneyintent.setClass(mActivity, BackMoneyActivity.class);
                                        BackMoneyintent.putExtra("userId", userId);
                                        mActivity.startActivity(BackMoneyintent);
                                        break;
                                    case R.id.login_content_today_layout:
                                        Intent AllPropertyintent = new Intent(mActivity, AssetstatisticsActivity.class);
                                        if (principalBeen != null)
                                            AllPropertyintent.putExtra("principalBeen", principalBeen);
                                        mActivity.startActivity(AllPropertyintent);
                                        break;
                                    //累计收益
                                    case R.id.login_content_invest_layout:
                                        Intent AllPropertyintent1 = new Intent(mActivity, AssetstatisticsActivity.class);
                                        if (principalBeen != null)
                                            AllPropertyintent1.putExtra("principalBeen", principalBeen);
                                        AllPropertyintent1.putExtra("leiji", "leiji");
                                        mActivity.startActivity(AllPropertyintent1);
                                        break;
                                    case R.id.login_content_invest_hongbao:
                                        mActivity.startActivity(new Intent(mActivity, HongBaoActivity.class));
                                        break;
                                    case R.id.login_content_invest_reward:
                                        mActivity.startActivity(new Intent(mActivity, RewardActivity.class));
                                        break;
                                    case R.id.login_content_my_recommend:
                                        Intent h5 = new Intent(mActivity, InvitedToRecordActivity.class);
                                        mActivity.startActivity(h5);
                                        break;
                                    case R.id.setting_rl:
                                        if (userName1 != null) {
                                            Intent accountManageintent = new Intent(mActivity, AccountManageActivity.class);
                                            accountManageintent.putExtra("userId", userName1);
                                            mActivity.startActivity(accountManageintent);
                                        }
                                        break;
                                }
                            }

                        } else {
                            //TODO 未登录
                            if(login_content_login_registration_ll==null){//防止空指针
                                initViews();
                            }

                            login_content_invest_invest_count.setText(Html.fromHtml(touzi));
                            login_content_invest_money_count.setText(Html.fromHtml(huikuan));
                            login_content_invest_hongbao_count.setText(Html.fromHtml(hb));
                            login_content_invest_award_count.setText(Html.fromHtml(jiangli));
                            login_content_recommend_award_count.setText(Html.fromHtml(yaoqing));
                            if (v != null) {
                                switch (v.getId()) {
                                    case R.id.mine_ll_already_logged_in:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.login_content_table_row_cash:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.login_content_table_row_charge:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.login_content_table_row_money_record:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.login_content_invest_record:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.login_content_money_back:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.login_content_today_layout:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.login_content_invest_hongbao:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.login_content_invest_reward:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.login_content_my_recommend:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                    case R.id.setting_rl:
                                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                                        break;
                                }
                            }
                        }
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
                .addParams("token", PreferenceUtil.getPrefString(mActivity, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        resultBean = JSON.parseObject(s, BankList.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            Intent Cashintent = new Intent(mActivity, CashActivity.class);
                            Cashintent.putExtra("currentPhoneNo", currentPhoneNo);
                            Cashintent.putExtra("totalIncome", totalIncome + "");
                            mActivity.startActivityForResult(Cashintent, 2008);
                        } else if (resultBean.getRcd().equals("M00010")) {
                            final CancleMessageDialog dialog = new CancleMessageDialog(mActivity, R.style.ActionSheetDialogStyle);
                            dialog.setTitle("您的银行卡将在完成一笔投资或充值后添加");
                            dialog.setMessage("立即投资");
                            dialog.setCancleText("暂不考虑");
                            dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent("HomeFragmentBroadCast");
                                    intent.putExtra("current", 1);
                                    mActivity.sendBroadcast(intent);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }
                });
    }

    @Override
    public View getmView() {
        return rootView;
    }


    class LoginContentBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String quickly = intent.getStringExtra("Quickly");
            if (!TextUtils.isEmpty(quickly) && quickly.equals("Quickly")) {
                CustomerServiceNet();
                RequestForMoney(null, null);
                return;
            } else if (!TextUtils.isEmpty(quickly) && quickly.equals("cash")) {
                RequestForMoney(null, null);
                CustomerServiceNet();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
