package com.rongxun.xqlc.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Borrow.BorrowBean;
import com.rongxun.xqlc.Beans.Borrow.BorrowPoputMessage;
import com.rongxun.xqlc.Beans.InvestBean;
import com.rongxun.xqlc.Beans.bank.BankCard;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.Beans.projectInitBean;
import com.rongxun.xqlc.Beans.userInfo.UserBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.CancleMessageDialog;
import com.rongxun.xqlc.UI.CashDialog;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LMessageDialog;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.TopUpDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.KeepTwoUtil;
import com.rongxun.xqlc.Util.PrefUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;
import com.yintong.secure.activity.BaseActivity;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class ProjectInvestActivity extends MyBaseActivity {


    private ImageView projectInvestToolbarBack;//返回
    private TextView projectInvestToolbarTitle;//标题
    private Toolbar projectInvestToolbar;//toolbar

    private TextView name;//项目名称
    private TextView peroid;//预计年化收益率
    private TextView date;//剩余投资期限
    private TextView projectInvestAbleTender;//剩余可投金额
    private EditText projectInvestTenderMoney;//投资金额
    private TextView invest_all;//余额全投
    private TextView expect;//预期收益率
    private RelativeLayout projectInvestHbChooseLayout;//红包选择
    private TextView projectInvestHbMoney;//红包选择金额
    private IconFontTextView projectInvestHbChooseAction;//右向箭头">"
    private TextView projectInvestAbleMoney;//可用余额
    private RelativeLayout bank_layout;//银行卡信息
    private TextView bank_name;//XXX银行
    private TextView bank_limit;//单笔支付限额5万，单日支付限额20万
    private TextView top_up;//银行卡yue
    private Button projectInvestConfirmButton;//确定投资
//    private TextView projectInvestLowestTender;//?????

    private int hongBaoCount = 0;//使用的红包数
    private int projectId;
    private String lowestTender;
    private String availableTender; //剩余可投金额
    private String project;
    private String mostAccount;
    private boolean isBund;
    private ProjectInvestActivityBroadCast broadCast;
    private String projectUrl;
    private String TAG = "确认投资";
    private BorrowPoputMessage borrowPoputMessage;
    //是否让edit 通知激活改变监听
    private boolean isChange = true;
    private boolean isMost = true;
    DecimalFormat df1 = new DecimalFormat("#0.00");
    DecimalFormat df = new DecimalFormat("#0");
    private List<com.rongxun.xqlc.Beans.projectInitBean.DataBean> userHongbaoList;
    private List<com.rongxun.xqlc.Beans.projectInitBean.CouponListBean> userJiaxiList;
    private boolean isEntry = false;
    StringBuilder hongbaoArray;
    private int onSize;
    private String selectedIdArrayString = "";//以逗号分隔的红包ID
    private float keyboardF;
    private float residueF;
    private String bankUrl = AppConstants.URL_SUFFIX + "/rest/bankTo";
    private BankList banklist;
    private Gson gson = new Gson();
    public static int FINISH = 1212;
    private boolean isSelect = false;//是否选择过红包
    private boolean isSelectJiaxijuan = false;//是否选择加息卷
    private LoadingDialog loadingDialog;
    private ImageView bank_icon;
    private String tenderMoney;
    private String safePass;
    private String hbMoney;
    private String exceptS;
    private CashDialog cashDialog;
    private projectInitBean projectInitBean;
    private float bankMoney;
    private String certainMoneyResult;//选择红包后 的投资金额
    private LinearLayout ll2;
    private TextView money_limit;
    private boolean isBroadcast = false;//是否是广播 刷新 是的话 使用的红包 不重置
    private RelativeLayout jiaxi_layout;
    private TextView jiaxijuan;
    private int jiaxijuanID;
    private LinearLayout jiaxi_money_ll;
    private TextView jiaxi_moneyT;
    private double jiaxiMoney;
    private double jiaxijuanPercentage;
    private TextView invest_name;
    private TextView least_money;
    private Button recharge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        MobclickAgent.onEvent(this, "6");
        setContentView(R.layout.activity_project_invest);
        initView();
    }


    private void initView() {
        broadCast = new ProjectInvestActivityBroadCast();
        IntentFilter intentFilter = new IntentFilter("ProjectInvestActivityBroadCast");
        registerReceiver(broadCast, intentFilter);

        projectInvestToolbarBack = (ImageView) findViewById(R.id.confirm_investment_img_back);
        projectInvestToolbarTitle = (TextView) findViewById(R.id.project_invest_toolbar_title);
        projectInvestToolbar = (Toolbar) findViewById(R.id.project_invest_toolbar);
//        projectInvestLowestTender = (TextView) findViewById(R.id.project_invest_lowest_tender);
        projectInvestAbleTender = (TextView) findViewById(R.id.project_invest_able_tender);
        bank_name = (TextView) findViewById(R.id.project_invest_bank_name);
        invest_name = (TextView) findViewById(R.id.project_invest_name);
        bank_limit = (TextView) findViewById(R.id.project_invest_bank_limit);
        top_up = (TextView) findViewById(R.id.project_invest_top_up);
        invest_all = (TextView) findViewById(R.id.project_invest_all);
        least_money = (TextView) findViewById(R.id.project_invest_least_money);
        projectInvestTenderMoney = (EditText) findViewById(R.id.project_invest_tender_money);
        projectInvestHbChooseAction = (IconFontTextView) findViewById(R.id.project_invest_hb_choose_action);
        projectInvestHbMoney = (TextView) findViewById(R.id.project_invest_hb_money);
        projectInvestHbChooseLayout = (RelativeLayout) findViewById(R.id.project_invest_hb_choose_layout);
        projectInvestConfirmButton = (Button) findViewById(R.id.project_invest_confirm_button);
        projectInvestAbleMoney = (TextView) findViewById(R.id.project_invest_able_money);
        bank_layout = (RelativeLayout) findViewById(R.id.add_bank_bank_layout);
        peroid = (TextView) findViewById(R.id.project_invest_able_peroid);
        expect = (TextView) findViewById(R.id.project_invest_expect_money);
        date = (TextView) findViewById(R.id.project_invest_able_date);
        name = (TextView) findViewById(R.id.confirm_investment_txt_title);
        recharge = (Button) findViewById(R.id.project_invest_recharge);
        bank_icon = (ImageView) findViewById(R.id.project_invest_bank_icon);
        ll2 = (LinearLayout) findViewById(R.id.project_invest_ll2);//限购横条
        money_limit = (TextView) findViewById(R.id.project_invest_money_limit);//限购
        jiaxi_layout = (RelativeLayout) findViewById(R.id.project_invest_jiaxi_layout);
        jiaxijuan = (TextView) findViewById(R.id.project_invest_jiaxijuan);

        jiaxi_money_ll = (LinearLayout) findViewById(R.id.project_invest_expect_jiaxi_money_ll);
        jiaxi_moneyT = (TextView) findViewById(R.id.project_invest_expect_jiaxi_money);

        SpannableString ss = new SpannableString("请输入投资金额");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        projectId = getIntent().getIntExtra("projectId", 0);
        isBund = getIntent().getBooleanExtra("isBund", false);

        //再次拿项目数据防止数据过期
        initWork();
    }

    private void initDate() {
        invest_name.setText(getIntent().getStringExtra("name"));
        least_money.setText(getIntent().getStringExtra("leastmoney"));
        //限购
        if (TextUtils.isEmpty(projectInitBean.getMostAccount())) {
            ll2.setVisibility(View.GONE);
        } else {
            money_limit.setText(projectInitBean.getMostAccount());
        }
        peroid.setText(projectInitBean.getApr() + "");//预期年利率
        date.setText(projectInitBean.getTimeLimit() + "天");//项目期限
//        projectInvestLowestTender.setText(lowestTender);
        projectInvestAbleTender.setText(availableTender);

        name.setText(project);


        //确认投资
        projectInvestConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitButton();
            }
        });

        //返回
        projectInvestToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(projectInitBean!=null){
                    Intent Chargeintent = new Intent(ProjectInvestActivity.this, ChargeActivity.class);
                    Chargeintent.putExtra("money",projectInitBean.getAbleMoney());
                    startActivityForResult(Chargeintent, 2008);
                }
            }
        });

    }


    private void initNetwork() {
        RequestForBankData(bankUrl);
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
                    public void onError(Call call, final Exception e, int id) {
                        // TODO: 2017/6/20 0020 网络请求错误
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.i(TAG, s);

                        final BankList resultBean = JSON.parseObject(s, BankList.class);

                        if (resultBean.getRcd().equals("R0001")) {
                            banklist = resultBean;
                            for (BankList.BankCardListBean item : banklist.getBankCardList()) {
                                if (item.getBankId().equals(banklist.getBankId())) {
                                    bank_limit.setText(item.getBankLimit());
                                    String bank_limitS = item.getBankName() + "（" + "尾号" + banklist.
                                            getCardNo().substring(banklist.getCardNo().length() -
                                            4, banklist.getCardNo().length()) + "）";
                                    bank_name.setText(bank_limitS);
                                    try {
                                        Glide.with(ProjectInvestActivity.this).load(banklist.getNoIcon()).into(bank_icon);
                                    } catch (Exception e) {

                                    }


                                    if (isBund) {
                                        bank_layout.setVisibility(View.VISIBLE);
                                    } else {
                                        bank_layout.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }

                    }
                });
    }


    //获取项目剩余 可投
    public void RequestForListData(String basicUrl) {

        String url = basicUrl + "/" + projectId;
        Log.i(TAG, url);

        OkHttpUtils.post()
                .url(url)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {
                        // TODO: 2017/6/20 0020
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        final BorrowBean resultBean = JSON.parseObject(s, BorrowBean.class);

                        if (resultBean.getRcd().equals("R0001")) {
                            projectInvestAbleTender.setText(df.format(Double.parseDouble(resultBean.getBalance())));
                        } else {
                            Toast.makeText(ProjectInvestActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }


    //请求网络数据 查询该帐号号码
    public void RequestForListDataAccount(String basicUrl, String userId) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("username", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.i(TAG, "response json:" + s);
                        final UserBean resultBean = JSON.parseObject(s, UserBean.class);

                        if (resultBean.getRcd().equals("R0001")) {
                            Intent intent = new Intent();
                            intent.setClass(ProjectInvestActivity.this, ForgetSafePassActivity.class);
                            intent.putExtra("currentPhoneNo", resultBean.getPhone());
                            startActivity(intent);
                        } else if (resultBean.getRcd().equals("E0001")) {
                            startActivity(new Intent(ProjectInvestActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(ProjectInvestActivity.this, "登录已过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProjectInvestActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    //拿到项目数据后处理
    public void UpdateViews() {
        projectInvestAbleMoney.setText("现金余额："+projectInitBean.getAbleMoney() + "元");
        //余额全投
        invest_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int allmoney = (int) (Float.parseFloat(projectInitBean.getAbleMoney()) / 100);
                projectInvestTenderMoney.setText((int) (allmoney * 100) + "");
            }
        });

        projectInvestTenderMoney.addTextChangedListener(

                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        top_up.setVisibility(View.VISIBLE);
                        if (isChange) {
                            if (projectInvestTenderMoney.getText().toString().length() > 0 && availableTender.length() > 0) {
                                //输入的金额 > 剩余可投金额 ------> 设为剩余可投金额
                                if (Float.parseFloat(projectInvestTenderMoney.getText().toString()) > Float.parseFloat(availableTender + "")) {
                                    projectInvestTenderMoney.setText(availableTender);
                                    projectInvestTenderMoney.setSelection(availableTender.length());
                                    //输入的钱大于100金额
                                }

                                //预期收益
                                exceptS = df1.format(Float.parseFloat(projectInvestTenderMoney.getText().toString()) *
                                        (projectInitBean.getDayApr()) *
                                        (projectInitBean.getTimeLimit()));
                                expect.setText(exceptS + "元");

                                //选择红包后如果变动金额  标识只有选择过红包才会执行

                                if (userHongbaoList != null && isSelect && !certainMoneyResult.equals(projectInvestTenderMoney.getText().toString())) {
                                    for (int x = 0; x < userHongbaoList.size(); x++) {
                                        //所有红包设置未选择状态
                                        userHongbaoList.get(x).isright = false;
                                    }
                                    hongBaoCount = 0;
                                    //选择的红包id归0
                                    selectedIdArrayString = "";
                                    //设置红包可用个数
                                    projectInvestHbMoney.setTextColor(Color.parseColor("#fa5454"));
                                    projectInvestHbMoney.setText(userHongbaoList.size() + "个可用");
                                    isSelect = false;
                                    final LMessageDialog dialog = new LMessageDialog(ProjectInvestActivity.this, R.style.ActionSheetDialogStyle);
                                    dialog.setMessage("投资额已变更，需要重新选择红包");
                                    dialog.setCancelable(false);
                                    dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }

                                if (userJiaxiList != null && isSelectJiaxijuan && !certainMoneyResult.equals(projectInvestTenderMoney.getText().toString())) {
                                    for (int x = 0; x < userJiaxiList.size(); x++) {
                                        //所有加息卷设置未选择状态
                                        userJiaxiList.get(x).isRight = false;
                                    }
                                    jiaxi_money_ll.setVisibility(View.GONE);
                                    jiaxi_moneyT.setText("0元");
                                    jiaxijuanID = 0;
                                    jiaxijuan.setTextColor(Color.parseColor("#fa5454"));
                                    jiaxijuan.setText(userJiaxiList.size() + "张可用");
                                    isSelectJiaxijuan = false;
                                    final LMessageDialog dialog = new LMessageDialog(ProjectInvestActivity.this, R.style.ActionSheetDialogStyle);
                                    dialog.setMessage("投资额已变更，需要重新选择加息卷");
                                    dialog.setCancelable(false);
                                    dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            }
                        } else {
                            isChange = true;
                        }
                        if (projectInvestTenderMoney.getText().toString().length() == 0) {
                            expect.setText("0元");
                            jiaxi_moneyT.setText("0元");
                            top_up.setText("0元");
                        } else {
                            CalculateBankMoney(0);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );

        //设置红包可用个数
        projectInvestHbMoney.setTextColor(Color.parseColor("#fa5454"));
        if (!isBroadcast) {
            projectInvestHbMoney.setText(userHongbaoList.size() + "个可用");
            if (userJiaxiList==null || userJiaxiList.size() == 0) {
                jiaxijuan.setText("暂无可用加息卷");
            } else {
                jiaxijuan.setTextColor(Color.parseColor("#fa5454"));
                jiaxijuan.setText(userJiaxiList.size() + "张可用");
            }
        }

        //红包选择点击  0为无可用红包
        if (projectInitBean.getUseHongbao() == 0) {
            projectInvestHbMoney.setText("此项目不支持使用");
            projectInvestHbMoney.setTextColor(Color.parseColor("#cccccc"));
        } else {
            projectInvestHbChooseLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    //输入的投资金额
                    String tenderMoney = projectInvestTenderMoney.getText().toString();
                    if (TextUtils.isEmpty(tenderMoney) || Integer.parseInt(tenderMoney) < 100) {
                        Toast.makeText(ProjectInvestActivity.this, "投资金额必须大于100", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        intent.setClass(ProjectInvestActivity.this, HbChooseActivity.class);
                        //可用红包集合
                        intent.putExtra("hbListString", gson.toJson(userHongbaoList));
                        //可用加息卷集合
                        if(!projectInitBean.getIsCoupon() && userJiaxiList!=null){
                            userJiaxiList.clear();//加息卷不可用
                        }
                        intent.putExtra("jiaxiListString", gson.toJson(userJiaxiList));
                        //最大可投金额
                        intent.putExtra("mostMoney", availableTender);
                        //项目剩余天数
                        intent.putExtra("timeLimit", projectInitBean.getTimeLimit());
                        intent.setClass(ProjectInvestActivity.this, HbChooseActivity.class);
                        //投资金额
                        intent.putExtra("tenderMoney", tenderMoney);
                        //加息金额
                        intent.putExtra("jiaxiMoney",jiaxiMoney);
                        //项目剩余可投金额
                        intent.putExtra("beLeftMoney", availableTender);
                        intent.putExtra("isHongbaoEnter", true);
                        startActivityForResult(intent, 7080);
                        isSelect = false;
                        isSelectJiaxijuan = false;
                    }
                }
            });

        }
        if (!projectInitBean.getIsCoupon()) {
            jiaxijuan.setText("此项目不支持使用");
            jiaxijuan.setTextColor(Color.parseColor("#cccccc"));
        } else {
            jiaxi_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tenderMoney = projectInvestTenderMoney.getText().toString();
                    if (TextUtils.isEmpty(tenderMoney) || Integer.parseInt(tenderMoney) < 100) {
                        Toast.makeText(ProjectInvestActivity.this, "投资金额必须大于100", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Intent intent = new Intent();
                        //输入的投资金额
                        intent.setClass(ProjectInvestActivity.this, HbChooseActivity.class);
                        //可用红包集合
                        if(projectInitBean.getUseHongbao() == 0){
                            userHongbaoList.clear();
                        }
                        intent.putExtra("hbListString", gson.toJson(userHongbaoList));
                        //可用加息卷集合
                        intent.putExtra("jiaxiListString", gson.toJson(userJiaxiList));
                        intent.putExtra("isHongbaoEnter", false);
                        //项目剩余天数
                        intent.putExtra("timeLimit", projectInitBean.getTimeLimit());
                        //最大可投金额
                        intent.putExtra("mostMoney", availableTender);
                        intent.setClass(ProjectInvestActivity.this, HbChooseActivity.class);
                        //投资金额
                        intent.putExtra("tenderMoney", tenderMoney);
                        //加息金额
                        intent.putExtra("jiaxiMoney",jiaxiMoney);
                        //项目剩余可投金额
                        intent.putExtra("beLeftMoney", availableTender);
                        startActivityForResult(intent, 7080);
                        isSelect = false;
                        isSelectJiaxijuan = false;
                    }
                }
            });
        }
    }

    /**
     * 确认投资
     */
    public void SubmitButton() {
        tenderMoney = projectInvestTenderMoney.getText().toString();
        if (tenderMoney == null || tenderMoney.equals("")) {
            Toast.makeText(ProjectInvestActivity.this, "请填写投资金额", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(tenderMoney) < 100) {
            Toast.makeText(ProjectInvestActivity.this, "起投金额不得少于100", Toast.LENGTH_SHORT).show();
            return;
        } else if (Float.parseFloat(Integer.parseInt(tenderMoney) + "") / 100 != (int) Float.parseFloat(Integer.parseInt(tenderMoney) + "") / 100) {
            Toast.makeText(ProjectInvestActivity.this, "投资金额必须为100的整数倍", Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.isEmpty(mostAccount) && Integer.parseInt(mostAccount) != 0
                && !mostAccount.equals("null") && projectInvestTenderMoney.getText().toString().length() > 0
                && Integer.parseInt(projectInvestTenderMoney.getText().toString()) > Integer.parseInt(mostAccount)) {
            Toast.makeText(ProjectInvestActivity.this, "该项目标限购￥：" + mostAccount, Toast.LENGTH_SHORT).show();
            return;
        }

        if (projectInvestHbMoney.getText().toString().equals("此项目不支持使用") || projectInvestHbMoney.getText().toString().equals("未选择红包")) {
            hbMoney = "0元";
        } else {
            hbMoney = projectInvestHbMoney.getText().toString();
        }
        //判断是否绑卡过
        if (!isBund) {
            //没绑卡进入绑卡页面
            Toast.makeText(this, "您未绑卡，完成首次充值后，自动绑卡", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(this, RealNameAuthActivity.class);

            String chargeMoney;
            if (bankMoney > 3) {
                chargeMoney = bankMoney + "";
            } else {
                chargeMoney = "3";
            }
            intent.putExtra("money", chargeMoney);
            intent.putExtra("inverstMark", "inverstMark");
            intent.putExtra("tenderMoney", tenderMoney);
            intent.putExtra("selectedIdArrayString", selectedIdArrayString);
            intent.putExtra("jiaxijuanID", jiaxijuanID+"");
            intent.putExtra("project", project);
            intent.putExtra("projectId", projectId);
            intent.putExtra("exceptS", (Double.parseDouble(exceptS)+jiaxiMoney)+"");
            startActivity(intent);
        } else {

            //判断钱是否够 不够跑充值页面
            if (bankMoney > 0) {
                //资金余额不足
                TopUpDialog dialog = new TopUpDialog(ProjectInvestActivity.this, R.style.ActionSheetDialogStyle);
                if (isInteger(hbMoney.substring(0, hbMoney.length() - 1))) {
                    dialog.setData(df1.format(Integer.parseInt(tenderMoney) - Integer.parseInt(hbMoney.substring(0, hbMoney.length() - 1)) - (Double.parseDouble(projectInitBean.getAbleMoney())))
                            , ProjectInvestActivity.this, tenderMoney, selectedIdArrayString, project, projectId, (Double.parseDouble(exceptS)+jiaxiMoney)+"", hongBaoCount,jiaxijuanID+"");
                } else {
                    dialog.setData(df1.format(Integer.parseInt(tenderMoney) - (Double.parseDouble(projectInitBean.getAbleMoney())))
                            , ProjectInvestActivity.this, tenderMoney, selectedIdArrayString, project, projectId, (Double.parseDouble(exceptS)+jiaxiMoney)+"", hongBaoCount,jiaxijuanID+"");
                }
            } else {

                cashDialog = new CashDialog(ProjectInvestActivity.this, R.style.ActionSheetDialogStyle);
                cashDialog.getPrompt().setVisibility(View.INVISIBLE);
                cashDialog.getForget().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = getSharedPreferences("AppToken", MODE_PRIVATE).getString("username", "");
                        String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";
                        RequestForListDataAccount(basicUrl, id);
                    }
                });

                cashDialog.getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = AppConstants.URL_SUFFIX + "/rest/investDoH/" + projectId;
                        safePass = cashDialog.getPasswordE().getText().toString();
                        RequestToInvestDo(url);
                    }
                });
            }
        }
    }


    /**
     * 请求提交投资该项目
     *
     * @param basicUrl
     */

    public void RequestToInvestDo(String basicUrl) {
        if (selectedIdArrayString.isEmpty() && hongBaoCount > 0) {
            Toast.makeText(ProjectInvestActivity.this, "请重新勾选红包", Toast.LENGTH_SHORT).show();
            return;
        }
        if (jiaxijuanID==0 && jiaxijuanPercentage > 0) {
            Toast.makeText(ProjectInvestActivity.this, "请重新勾选加息卷", Toast.LENGTH_SHORT).show();
            return;
        }
        if (safePass == null || safePass.equals("")) {
            Toast.makeText(ProjectInvestActivity.this, "请填写交易密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ProjectInvestActivity.this);
            loadingDialog.showLoading();
        }

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("tenderMoney", tenderMoney)
                .addParams("safepwd", safePass)
                .addParams("clientType", 1 + "")
                .addParams("couponId", jiaxijuanID+"")
                .addParams("hongbaoArray", selectedIdArrayString)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loadingDialog != null && loadingDialog.isShowing()) {
                                    loadingDialog.dismissLoading();
                                    loadingDialog = null;
                                }
                                Toast.makeText(ProjectInvestActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                                if (e != null && e.getMessage() != null) {
                                    Log.i(TAG, e.getMessage());
                                }
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.i(TAG, s);
                        final InvestBean resultBean = JSON.parseObject(s, InvestBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loadingDialog != null && loadingDialog.isShowing()) {
                                    loadingDialog.dismissLoading();
                                    loadingDialog = null;
                                }
                                if (resultBean.getRcd().equals("R0001")) {
                                    cashDialog.dismiss();
                                    //转入投资成功页面
                                    Intent successIntent = new Intent(ProjectInvestActivity.this, InvestResultActivity.class);
                                    if (isInteger(hbMoney.substring(0, hbMoney.length() - 1))) {
                                        successIntent.putExtra("money",
                                                Integer.parseInt(tenderMoney));
                                    } else {
                                        successIntent.putExtra("money", Integer.parseInt(tenderMoney));
                                    }
                                    if(TextUtils.isEmpty(resultBean.getTime())&&resultBean.getRepayDateString()!=null){
                                        successIntent.putExtra("time", resultBean.getRepayDateString());
                                    }else{
                                        successIntent.putExtra("time", resultBean.getTime());
                                    }
                                    successIntent.putExtra("project", project);
                                    successIntent.putExtra("exceptS", (Double.parseDouble(exceptS)+jiaxiMoney)+"");//预期收益;

                                    startActivityForResult(successIntent, InvestResultActivity.CONTEXT_INCLUDE_CODE);
                                    setResult(FINISH);
                                    finish();
                                } else if (resultBean.getRcd().equals("E0001")) {
                                    final CancleMessageDialog dialog = new CancleMessageDialog(ProjectInvestActivity.this, R.style.ActionSheetDialogStyle);
                                    dialog.setTitle("您的账户已被其他设备登录。若非本人操作，则密码可能已泄露，建议重新登录时修改登录密码");
                                    dialog.setMessage("重新登录");
                                    dialog.setCancleText("忽略");
                                    dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent loginIntent = new Intent(ProjectInvestActivity.this, LoginActivity.class);
                                            startActivity(loginIntent);
                                            dialog.dismiss();
                                        }
                                    });
                                } else if (resultBean.getRcd().equals("S0002")) {
                                    //交易密码错误
                                    cashDialog.getPrompt().setVisibility(View.VISIBLE);
                                } else {
                                    cashDialog.dismiss();
                                    //被抢投 刷新项目
                                    Toast.makeText(ProjectInvestActivity.this, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                                    RequestForListData(AppConstants.URL_SUFFIX + "/rest/borrow");
                                }
                            }
                        });
                    }
                });
    }


    class ProjectInvestActivityBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("quickPlay") != null && intent.getStringExtra("quickPlay").equals("finish")) {
                finish();
                ProjectInvestActivity.this.setResult(FINISH);
            } else if (intent.getStringExtra("quickPlay") != null && intent.getStringExtra("quickPlay").equals("refresh")) {
                //刷新页面
                initWork();
                RequestForListData(AppConstants.URL_SUFFIX + "/rest/borrow");
                RequestForBankData(bankUrl);
                if (intent.getBooleanExtra("isBund", false) == true) {
                    isBund = true;
                }
                //是否是广播 刷新
                isBroadcast = true;
            } else {
                isBund = true;
                //刷新账户信息
                initWork();

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppConstants.ChooseHongBaoOkCode) {
            String hbListString = data.getStringExtra("hbListString");
            String jiaxiListString = data.getStringExtra("jiaxijuanListString");
            jiaxiMoney = data.getDoubleExtra("jiaxiMoney", 0);
            userHongbaoList = new Gson().fromJson(hbListString, new TypeToken<ArrayList<projectInitBean.DataBean>>() {
            }.getType());
            userJiaxiList = new Gson().fromJson(jiaxiListString, new TypeToken<ArrayList<projectInitBean.CouponListBean>>() {
            }.getType());

            //使用红包的金额总和
            hongBaoCount = data.getIntExtra("hongBaoCount", 0);
            //使用的加息卷id
            jiaxijuanID = data.getIntExtra("jiaxijuanID", 0);
            //使用的加息卷百分比
            jiaxijuanPercentage = data.getDoubleExtra("jiaxijuanPercentage", 0);


            selectedIdArrayString = data.getStringExtra("hongBaoArray");
            //选择红包后的投资金额  可能增大
            certainMoneyResult = data.getStringExtra("certainMoney");
            if (TextUtils.isEmpty(certainMoneyResult))
                certainMoneyResult = "0";
            //选择红包前 用户投资的金额
            String tenderNumberString = projectInvestTenderMoney.getText().toString();

            if (Integer.parseInt(certainMoneyResult) > 0 && hongBaoCount>0) {
                jiaxi_money_ll.setVisibility(View.GONE);
                //设置选择红包的金额
                projectInvestHbMoney.setText(hongBaoCount + "元");
                if(projectInitBean!=null && projectInitBean.getIsCoupon())
                    jiaxijuan.setText(userJiaxiList.size() + "张可用");
                //availableTender  为项目剩余 可投金额
                if (Integer.parseInt(certainMoneyResult) > Integer.parseInt(availableTender)) {
                    //如果选择红包后的投资金额 大于 目剩余可投金额
                    certainMoneyResult = availableTender;
                    Toast.makeText(this, "投资金额大于剩余可投金额", Toast.LENGTH_SHORT).show();
                }
                projectInvestTenderMoney.setText(certainMoneyResult);
                projectInvestTenderMoney.setSelection(certainMoneyResult.length());
            }

            if(jiaxijuanPercentage>0){
                jiaxijuan.setText("加息" + jiaxijuanPercentage + "%");
                if(projectInitBean!=null && projectInitBean.getUseHongbao()!=0)
                    projectInvestHbMoney.setText(userHongbaoList.size() + "个可用");

                if (Integer.parseInt(certainMoneyResult) > Integer.parseInt(availableTender)) {
                    //如果选择红包后的投资金额 大于 目剩余可投金额
                    certainMoneyResult = availableTender;
                    Toast.makeText(this, "投资金额大于剩余可投金额", Toast.LENGTH_SHORT).show();
                }
                projectInvestTenderMoney.setText(certainMoneyResult);
                projectInvestTenderMoney.setSelection(certainMoneyResult.length());
            }

            if(jiaxijuanPercentage==0 && hongBaoCount==0){
                jiaxi_money_ll.setVisibility(View.GONE);
                if(projectInitBean!=null && projectInitBean.getUseHongbao()!=0)
                    projectInvestHbMoney.setText(userHongbaoList.size() + "个可用");
                if(projectInitBean!=null && projectInitBean.getIsCoupon())
                    jiaxijuan.setText(userJiaxiList.size() + "张可用");

                if (Integer.parseInt(certainMoneyResult) > Integer.parseInt(availableTender)) {
                    //如果选择红包后的投资金额 大于 目剩余可投金额
                    certainMoneyResult = availableTender;
                    Toast.makeText(this, "投资金额大于剩余可投金额", Toast.LENGTH_SHORT).show();
                }
                projectInvestTenderMoney.setText(certainMoneyResult);
                projectInvestTenderMoney.setSelection(certainMoneyResult.length());
            }


            //计算银行卡需要充值的钱
            CalculateBankMoney(hongBaoCount);
        }
        isEntry = true;
        if (hongBaoCount > 0)
            isSelect = true;  //是否选择过红包的标识
        if (jiaxijuanID != 0) {
            isSelectJiaxijuan = true;
            jiaxi_money_ll.setVisibility(View.VISIBLE);
            jiaxi_moneyT.setText(jiaxiMoney + "元");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void initWork() {
        String initUrl = AppConstants.URL_SUFFIX + "/rest/poputInvestH/" + projectId;
        OkHttpUtils.post()
                .url(initUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.v("ProjectInverst", response);
                        projectInitBean = gson.fromJson(response, projectInitBean.class);
                        if (projectInitBean.getRcd().equals("R0001")) {
                            lowestTender = projectInitBean.getLowestAccount();
                            availableTender = (int) Float.parseFloat(projectInitBean.getBalance()) + "";
                            project = projectInitBean.getBorrowName();
                            mostAccount = projectInitBean.getMostAccount();
                            //红包集合
                            if (!isBroadcast) {
                                userHongbaoList = projectInitBean.getData();
                                userJiaxiList = projectInitBean.getCouponList();
                            }
                            UpdateViews();
                            initDate();
                            initNetwork();

                        } else {
                            Toast.makeText(ProjectInvestActivity.this, projectInitBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void CalculateBankMoney(int hongBaoCount) {
        float keyboardF;//重新获取页面上的 投资金额
        if (projectInvestTenderMoney.getText().toString().equals("")) {
            keyboardF = 0;
        } else {
            keyboardF = Float.parseFloat(projectInvestTenderMoney.getText().toString());
        }
        //账户可用余额
        float residueF = Float.parseFloat(projectInitBean.getAbleMoney());
        //计算充值的钱
        bankMoney = Float.parseFloat(df1.format((keyboardF - residueF - hongBaoCount)));
        //计算银行卡 需要充值的金额
        if (bankMoney < 0) {
            top_up.setText("0元");
        } else if (bankMoney > 0 && bankMoney < 3) {
            top_up.setText("最低充值额度3元");
        } else {
            top_up.setText(bankMoney + "元");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCast);
        CustomApplication.removeActivity(this);
    }

    //判断是否是int类型
    public boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        MobclickAgent.onResume(this);
        super.onResume();
    }

}
