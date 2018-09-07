package com.rongxun.xqlc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.widget.SpringView;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Borrow.LastPrizeBean;
import com.rongxun.xqlc.Beans.Borrow.ManageDetailBean;
import com.rongxun.xqlc.Beans.home.BankBean;
import com.rongxun.xqlc.Beans.home.DealPasswordBean;
import com.rongxun.xqlc.Fragments.ManageInvestRecordActivity;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.CalculatorPopupWindow;
import com.rongxun.xqlc.UI.CancleMessageDialog;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.SpringHeader;
import com.rongxun.xqlc.UI.TipsDialog;
import com.rongxun.xqlc.UI.tipprogress.TipsProgressBar;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.DateUtil;
import com.rongxun.xqlc.Util.DecimalFormatUtil;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.Util.PreventFastClickUtils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 理财详情界面
 */
public class ManageDetailActivity extends MyBaseActivity implements View.OnClickListener {


    private RelativeLayout relBack;//返回
    private TextView txtTitle;//标题

    private TextView txtYearRote;//年化收益
    private TextView txtPlus;//加号
    private TextView txtAddRote;//加息
    private TextView txtLockDay;//锁定期
    private TextView txtLowMoney;//起投金额
    private TextView txtAllMoney;//总金额
    private TextView txtRemain;//剩余可投
    private TipsProgressBar bar;//进度条
    private TextView txtPublishDate;//发布时间
    private TextView txtRoteDate;//起息日期
    private TextView txtRemitDate;//汇款时间
    private RelativeLayout relRepayType;//还款方式
    private TextView txtRepayType;//还款方式
    private RelativeLayout relWelfare;//投资福利
    private TextView txtLastPrize;//收官奖
    private RelativeLayout liProjectDetail;//项目详情
    private RelativeLayout liInvestmentRecord;//投资记录
    private RelativeLayout liSafety;//安全保障

    private Button btnInvestmentOK;//确认投资
    private LoadingDialog loadingDialog;
    private int markId;//标的ID
    private String type;//标的类型 16新手标
    private ManageDetailBean detailBean;
    private Button imageCalculator;
    private CalculatorPopupWindow calculatorPopupWindow;
    private String timeLimit;//标的时间
    private String yearEarning;//年化收益
    private String dayEarning;
    private LinearLayout shouguanLL;
    private int expireDate;
    private String baseApr;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_detail);
        CustomApplication.addActivity(this);
        //获取id
        markId = getIntent().getIntExtra("id", 0);

        initView();
        initData();
        listener();

    }

    private void listener() {
        imageCalculator.setOnClickListener(this);
        relBack.setOnClickListener(this);
        relRepayType.setOnClickListener(this);
        relWelfare.setOnClickListener(this);
        liProjectDetail.setOnClickListener(this);
        liInvestmentRecord.setOnClickListener(this);
        liSafety.setOnClickListener(this);
        btnInvestmentOK.setOnClickListener(this);
    }

    private void initData() {

        //网络主页数据
        requestData();
        //获取土豪奖励数据
        requestThPrizeData();
    }

    /**
     * 请求土豪奖、收官奖
     */
    private void requestThPrizeData() {

        SharedPreferences preferences = getSharedPreferences("AppToken", Context.MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");

        OkHttpUtils
                .post()
                .url(AppConstants.TH_LAST_PRIZE + "/" + markId)
                .addParams("token", token)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //关闭dialog
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                        }

                        if (response != null) {

                            LastPrizeBean lastPrizeBean = GsonUtils.GsonToBean(response, LastPrizeBean.class);

                            switch (lastPrizeBean.getRcd()) {
                                case "R0001":
                                    //设置土豪奖、收官奖
                                    setPrize(lastPrizeBean);
                                    break;
                                case "E0001":
                                    //抢登
                                    Intent login = new Intent(ManageDetailActivity.this, LoginActivity.class);
                                    startActivity(login);
                                    break;
                                case "R0003":
                                    //
                                    Toast.makeText(ManageDetailActivity.this, "账号被锁定", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                });

    }

    /**
     * 请求标的数据
     */
    private void requestData() {

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.showLoading();

        SharedPreferences preferences = getSharedPreferences("AppToken", Context.MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");

        OkHttpUtils
                .post()
                .url(AppConstants.MARK_DETAIL + "/" + markId)
                .addParams("token", token)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                        //关闭dialog
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                        }
                        // TODO: 2017/8/7 0007 网络错误
                        Toast.makeText(ManageDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //关闭dialog
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                        }

                        if (response != null) {
                            detailBean = GsonUtils.GsonToBean(response, ManageDetailBean.class);

                            if (detailBean != null) {

                                switch (detailBean.getRcd()) {
                                    case "R0001":
                                        //设置标的数据
                                        setMarkData(detailBean);
                                        //设置底部投资button的状态
                                        setButtonStatus(detailBean);
                                        break;
                                    case "E0001":
                                        //抢登
                                        Intent login = new Intent(ManageDetailActivity.this, LoginActivity.class);
                                        startActivity(login);
                                        break;
                                    case "R0003":
                                        //
                                        Toast.makeText(ManageDetailActivity.this, "账号被锁定", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 判断是否投资过
     */
    private void requestNew() {

        SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");

        OkHttpUtils
                .post()
                .url(AppConstants.HAS_INVEST)
                .addParams("token", token)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //关闭dialog
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                        }

                        if (response != null) {
                            try {
                                JSONObject object = new JSONObject(response);


                                //未投资(新手) false
                                boolean isNew = object.getBoolean("tenderStatus");
                                if (!isNew) {
                                    // 是新手(有购买权限)，跳转到购买界面
                                    toBuy();
                                } else {
                                    //弹出对话框---->你非新手不可投资
                                    showNoNewDialog();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 获取银行卡信息
     */
    public void RequestForRechargeTo() {

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.showLoading();

        SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");

        OkHttpUtils
                .post()
                .url(AppConstants.IS_BANK)
                .addParams("token", token)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误
                        //结束dialog
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //结束dialog
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                        }

                        if (response != null) {
                            BankBean bankBean = GsonUtils.GsonToBean(response, BankBean.class);
                            if (bankBean != null) {
                                switch (bankBean.getRcd()) {
                                    case "R0001":
                                        confrimInvestment(bankBean);
                                        break;
                                    case "E0001":
                                        //抢登
                                        Intent login = new Intent(ManageDetailActivity.this, LoginActivity.class);
                                        startActivity(login);
                                        break;
                                    case "R0003":
                                        //
                                        Toast.makeText(ManageDetailActivity.this, "账号被锁定", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                        }
                    }
                });
    }

    /**
     * 进入确认投资页面
     */
    private void confrimInvestment(BankBean bankBean) {

        boolean isBound;
        int bankStatus = bankBean.getBankStatus();

        if (bankStatus == 1) {
            //绑卡
            isBound = true;
        } else {
            isBound = false;
        }

        Intent intent = new Intent(ManageDetailActivity.this, ProjectInvestActivity.class);
        intent.putExtra("projectId", markId);
        intent.putExtra("isBund", isBound);
        intent.putExtra("name", detailBean.getName());
        intent.putExtra("leastmoney", detailBean.getLowestAccount());
        startActivity(intent);

    }

    /**
     * 是否设置交易密码
     */
    private void checkPassword() {

        SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");

        OkHttpUtils
                .post()
                .url(AppConstants.IS_DEAL_PASSWORD)
                .addParams("token", token)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误
                        Toast.makeText(ManageDetailActivity.this, "交易密码网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response;
                        if (response != null) {
                            DealPasswordBean dealPasswordBean = GsonUtils.GsonToBean(response, DealPasswordBean.class);
                            if (dealPasswordBean != null) {
                                switch (dealPasswordBean.getRcd()) {
                                    case "R0001":
                                        int dealStatus = dealPasswordBean.getPayPasswordStatus();
                                        //判断是否设置交易密码
                                        if (dealStatus == 1) {
                                            //已设置交易密码
                                            isInvsetment2();
                                        } else {
                                            //弹出设置交易密码窗口
                                            setDealPassword();
                                        }
                                        break;
                                    case "E0001":
                                        //抢登
                                        Intent login = new Intent(ManageDetailActivity.this, LoginActivity.class);
                                        startActivity(login);
                                        break;
                                    case "R0003":
                                        //
                                        Toast.makeText(ManageDetailActivity.this, "账号被锁定", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    }
                });
    }


    private void initView() {

        relBack = (RelativeLayout) findViewById(R.id.manage_detail_rel_back);
        view = findViewById(R.id.manage_detail_view);
        txtTitle = (TextView) findViewById(R.id.manage_detail_txt_title);
        imageCalculator = (Button) findViewById(R.id.manage_image_calculator);
        txtYearRote = (TextView) findViewById(R.id.manage_detail_txt_rote_num);
        txtPlus = (TextView) findViewById(R.id.manage_detail_txt_plus);
        txtAddRote = (TextView) findViewById(R.id.manage_detail_txt_add_rote_num);
        txtLockDay = (TextView) findViewById(R.id.manage_detail_txt_lock_day);
        txtLowMoney = (TextView) findViewById(R.id.manage_detail_txt_low_money);
        txtAllMoney = (TextView) findViewById(R.id.manage_detail_txt_all_money);
        txtRemain = (TextView) findViewById(R.id.manage_detail_txt_remain);
        bar = (TipsProgressBar) findViewById(R.id.manage_detail_tip_bar);
        txtPublishDate = (TextView) findViewById(R.id.manage_detail_txt_release_date);
        txtRoteDate = (TextView) findViewById(R.id.manage_detail_txt_rote_date);
        txtRemitDate = (TextView) findViewById(R.id.manage_detail_txt_remit_date);
        relRepayType = (RelativeLayout) findViewById(R.id.manage_detail_rel_repay_type);
        txtRepayType = (TextView) findViewById(R.id.manage_detail_txt_repay_type);
        relWelfare = (RelativeLayout) findViewById(R.id.manage_detail_rel_welfare);
        txtLastPrize = (TextView) findViewById(R.id.manage_detail_txt_last_prize);
        liProjectDetail = (RelativeLayout) findViewById(R.id.manage_detail_li_project_detail);
        liInvestmentRecord = (RelativeLayout) findViewById(R.id.manage_detail_li_investment_record);
        liSafety = (RelativeLayout) findViewById(R.id.manage_detail_li_safety);
        shouguanLL = (LinearLayout) findViewById(R.id.manage_detail_shouguanLL);
        btnInvestmentOK = (Button) findViewById(R.id.manage_detail_btn_ok);

        /////////////////////////////////
        // TODO: 2017/8/7 0007 初始化弹性控件  测试

    }


    /**
     * 设置标的数据
     */
    private void setMarkData(ManageDetailBean detailBean) {

        DecimalFormatUtil df = DecimalFormatUtil.getInstance();
        DateUtil date = DateUtil.getInstance();
        if (detailBean.getType().equals("16")) {
            relWelfare.setVisibility(View.GONE);
            shouguanLL.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
        //标题
        txtTitle.setText(detailBean.getName());
        //年化收益
        yearEarning = df.getDouble2(detailBean.getYearApr());
        //基础收益
        baseApr = df.getDouble2(detailBean.getBaseApr());
        txtYearRote.setText(detailBean.getYearApr() + "");
//日收益
//        dayEarning = detailBean.getApr();
//        //加息
//        if (detailBean.getAwardApr() == 0) {
//            //隐藏
//            txtPlus.setVisibility(View.GONE);
//            txtAddRote.setVisibility(View.GONE);
//        } else {
//            //显示
//            txtPlus.setVisibility(View.VISIBLE);
//            txtAddRote.setVisibility(View.VISIBLE);
//            txtAddRote.setText(df.getDouble2(detailBean.getAwardApr()));
//        }

        //剩余期限
        expireDate = detailBean.getExpireDate();
        timeLimit = detailBean.getTimeLimit();
        //锁定期
        txtLockDay.setText(timeLimit + "天");
        //起投金额
        txtLowMoney.setText(detailBean.getLowestAccount() + "元");
        //总金额
        if (detailBean.getAccount() != null && !detailBean.getAccount().equals("")) {
            txtAllMoney.setText(df.getInt(Double.parseDouble(detailBean.getAccount())) + "元");
        } else {
            txtAllMoney.setText("--");
        }
        //剩余可投
        txtRemain.setText(df.getInt(Double.parseDouble(detailBean.getBalance())) + "元");
        //进度条
        bar.animateProgress(detailBean.getShowSchedule() * 10);
        //发布时间
        txtPublishDate.setText(date.getDayOrMonthOrYear(detailBean.getVerifyTime()));
        //起息日期
        if (detailBean.getIsNewbor().equals("1")) {
            txtRoteDate.setText("投资日起息");
        } else {
            txtRoteDate.setText("募集完成日起息");
        }
        //回款时间
        txtRemitDate.setText(date.getDayOrMonthOrYear(detailBean.getOverDate()));
        //还款方式
        txtRepayType.setText(detailBean.getStyleName() + ", 支持提前还款>");
        //设置标的类型
        type = detailBean.getType();
        //新手隐藏收官
        if (type.equals("16")) {
            shouguanLL.setVisibility(View.GONE);
        }
    }

    /**
     * 设置土豪奖、收官奖
     */
    private void setPrize(LastPrizeBean lastPrizeBean) {

        if (lastPrizeBean != null && lastPrizeBean.getRcd().equals("R0001")) {

            String separator = lastPrizeBean.getSeparator();//分隔符

            /////////////土豪奖////////////////////
            String tHPrize = lastPrizeBean.getTuhaoStr();//土豪奖描述
            List<String> type = lastPrizeBean.getTuhaoVals();
            String[] split = tHPrize.split(separator);
            String tHDesc = null;
            if (type != null && type.size() > 0) {

                tHDesc = split[0]//恭喜
                        + "<font color='#fa5454'>" + type.get(0) + "</font>"//888*****888
                        + split[1]//用户在该项目的单笔最大投资额高达
                        + "<font color='#fa5454'>" + type.get(1) + "</font>"//531400.0
                        + split[2]//元，获得了
                        + "<font color='#fa5454'>" + type.get(2) + "</font>"//25.00
                        + split[3];//元土豪红包。
            }

            //////////////收官奖//////////////////////
            String lastPrize = lastPrizeBean.getShouguanStr();//收官奖描述
            List<String> tpye2 = lastPrizeBean.getShouguanVals();
            String[] split2 = lastPrize.split(separator);
            String lsDesc = null;
            if (tpye2 != null && tpye2.size() > 0) {

                lsDesc = split2[0]////恭喜
                        + "<font color='#fa5454'>" + tpye2.get(0) + "</font>"//888*****888
                        + split2[1]//用户完成该项目的最后一笔投资，获得了
                        + "<font color='#fa5454'>" + tpye2.get(1) + "</font>"//15.00
                        + split2[2];//元收官红包。
            }

            if (lastPrizeBean.isHadInvested()) {

                //有投资
                txtLastPrize.setText(Html.fromHtml(lsDesc));
            } else {
                // 没投资  默认设置
                txtLastPrize.setText(lastPrize);
            }
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.manage_detail_rel_back:
                //返回
                finish();
                break;
            case R.id.manage_detail_rel_repay_type:
                //还款方式
                Intent intent = new Intent(ManageDetailActivity.this, ArticleActivity.class);
                intent.putExtra("id", "876");
                intent.putExtra("header", "提前还款说明");
                startActivity(intent);
                break;
            case R.id.manage_detail_rel_welfare:
                //投资福利
                Intent investWelfare = new Intent(this, H5JSActivity.class);
                investWelfare.putExtra("web_url", AppConstants.INVEST_WELFARE);
                startActivity(investWelfare);
                break;
            case R.id.manage_detail_li_project_detail:
                //项目详情
                Intent projectInent = new Intent(this, ProjectDetailActivity.class);
                projectInent.putExtra("id", markId);
                startActivity(projectInent);
                break;
            case R.id.manage_detail_li_investment_record:
                //投资记录
                Intent recordInent = new Intent(this, ManageInvestRecordActivity.class);
                recordInent.putExtra("id", markId);
                startActivity(recordInent);
                break;
            case R.id.manage_detail_li_safety:
                //安全保障
                Intent safe = new Intent(this, H5JSActivity.class);
                safe.putExtra("web_url", AppConstants.SAFETY_H5);
                startActivity(safe);
                break;
            case R.id.manage_detail_btn_ok:
                //确认投资 || 已还完
                String invest = btnInvestmentOK.getText().toString();
                if (!PreventFastClickUtils.isFastClick()) {

//                    if (invest.equals("立即购买")) {
//                        if(detailBean.getLogo().equals("0")){
                    isInvsetment();
//                        }else{
//                            //风险测评
//                            Intent h5 = new Intent(this, H5JSActivity.class);
//                            h5.putExtra("web_url", detailBean.getAppraisalURL());
//                            h5.putExtra("EvaluateFlag", "1");
//                            this.startActivityForResult(h5,0x0001);
//                        }
                } else {
                    //已还完 ——————>返回投资列表
                    finish();
                }

        break;

        case R.id.manage_image_calculator:
        // TODO: 2017/8/22 0022
        if (calculatorPopupWindow == null) {
            calculatorPopupWindow = new CalculatorPopupWindow(this);
        }
        //设置计算器年化收益
        if (txtYearRote != null) {
            calculatorPopupWindow.setYearEarning(yearEarning);
        }
        //设置计算器投资期限
        if (expireDate != 0)
            calculatorPopupWindow.setCalLimit(expireDate);
        //设置日收益率
        if (dayEarning != null) {
            calculatorPopupWindow.setDayEarning(dayEarning);
        }
        //显示
        calculatorPopupWindow.showPopup();
        break;

        default:
        break;
    }

}

    private void setButtonStatus(ManageDetailBean detailBean) {


        if (detailBean.getStatus() == 3) {
            btnInvestmentOK.setText("还款中");
            btnInvestmentOK.setBackgroundResource(R.drawable.shape_manage_detail_btn_c);
            imageCalculator.setBackgroundResource(R.mipmap.calculategrey);
        } else if (detailBean.getStatus() == 7) {
            btnInvestmentOK.setText("已还款");
            imageCalculator.setBackgroundResource(R.mipmap.calculategrey);
            btnInvestmentOK.setBackgroundResource(R.drawable.shape_manage_detail_btn_c);
        } else {
            btnInvestmentOK.setText("立即购买");
            btnInvestmentOK.setBackgroundResource(R.drawable.shape_manage_detail_btn_ok);
        }
    }

    /**
     * 判断 立即投资 是否可以点击跳转
     * 1.未登录
     * 直接去登陆页面
     * 2.已经登录
     * （1）新手标：
     * 是新手------>跳转购买界面
     * <p>
     * 非新手------>提示：你非新手
     * （2）非新手标：
     * 都可以购买
     *
     * @return
     */
    private void isInvsetment() {

        if (!isLogin()) {
            //没有登录，先登录
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            //判断是否设置交易密码
            checkPassword();
        }
    }

    /**
     * 设置交易密码的后续判断
     */
    private void isInvsetment2() {
        if (type.equals("16")) {
            //判断是不是新手
            requestNew();
        } else {
            //非新手标任何人都可以投资
            toBuy();
        }
    }


    /**
     * 到确认投资页面
     */
    private void toBuy() {
        //是否绑定银行卡
        RequestForRechargeTo();

    }

    /**
     * 判断是否登录
     *
     * @return
     */
    private boolean isLogin() {

        SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");
        if (TextUtils.isEmpty(token)) {
            return false;
        }

        return true;
    }

    /**
     * 设置交易密码的弹窗
     */
    private void setDealPassword() {

        final CancleMessageDialog dialog = new CancleMessageDialog(this, R.style.ActionSheetDialogStyle);
        dialog.setTitle("请设置交易密码");
        dialog.setCancleColour("#cccccc");
        dialog.setCancleText("暂不考虑");
        dialog.setMessage("立即设置");
        dialog.show();
        dialog.getMessage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ManageDetailActivity.this, SafePassWordActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 非新手dialog
     */
    private void showNoNewDialog() {

        TipsDialog tipsDialog = new TipsDialog(this);
        //设置标题
        tipsDialog.getTxtTitle().setText("温馨提示");
        tipsDialog.getTxtTitle().setTextColor(Color.parseColor("#fa5454"));
        tipsDialog.getTxtTitle().setTextSize(14);
        //设置提示语
        tipsDialog.getTxtTip().setText("仅允许新手投资");
        tipsDialog.getTxtTip().setTextColor(Color.parseColor("#888888"));
        tipsDialog.getTxtTip().setTextSize(12);

        //显示
        tipsDialog.show();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //网络主页数据
        requestData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0x0001) {
            requestData();
        }
    }
}
