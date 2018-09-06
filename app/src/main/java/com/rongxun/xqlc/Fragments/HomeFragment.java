package com.rongxun.xqlc.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rongxun.xqlc.Activities.ActivityCenterActivity;
import com.rongxun.xqlc.Activities.ArticleActivity;
import com.rongxun.xqlc.Activities.CommuniqueActivity;
import com.rongxun.xqlc.Activities.CustomerActivity;
import com.rongxun.xqlc.Activities.H5JSActivity;
import com.rongxun.xqlc.Activities.LoginActivity;
import com.rongxun.xqlc.Activities.ManageDetailActivity;
import com.rongxun.xqlc.Activities.ProjectInvestActivity;
import com.rongxun.xqlc.Activities.SafePassWordActivity;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.Beans.home.BankBean;
import com.rongxun.xqlc.Beans.home.DealPasswordBean;
import com.rongxun.xqlc.Beans.home.HomeBannerBean;
import com.rongxun.xqlc.Beans.home.HomeMarkBean;
import com.rongxun.xqlc.Beans.home.TipBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.CancleMessageDialog;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.UI.SwitchText;
import com.rongxun.xqlc.UI.tipprogress.TipsProgressBar;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.AppVersionUtils;
import com.rongxun.xqlc.Util.DecimalFormatUtil;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.Util.PrefUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.PreventFastClickUtils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    private Banner banner;//广告栏
    private SwitchText tip;//轮播的文字
    private RelativeLayout relMore;//更多

    private ImageView imgActivityCenter;//活动中心
    private LinearLayout liActivityCenter;//活动中心
    private ImageView imgNewWelfare;//新手福利
    private LinearLayout liNewWelfare;//新手福利
    private ImageView imgRecommendGift;//推荐有礼
    private LinearLayout liRecommendGift;//推荐有礼
    private ImageView imgCustomer;//在线客服
    private LinearLayout liCustomer;//在线客服

    private TextView txtMarkNumber;//标的期数
    private TextView txtYearRote;//年化收益
    private TextView txtLimitDay;//项目限期
    private Button btnBuy;//立即购买
    private ImageView imgKnow;//了解金储宝

    private LinearLayout liMarkContent;//标展示的区域
    private View welfareDot;//新手福利红点
    private View activityDot;//活动中心的红点

    private int markId;//标的 markId
    private PullToRefreshLayout refreshLayout;
    private View rootView;//根布局

    List<HomeBannerBean.IndexImageItemBean> bannerBeanList;//轮播图数据
    private List<String> bannerData;//轮播图url集合

    private List<String> tipData;//滚动文字内容
    private List<HomeBannerBean.ArticleListBean> tipBeanList;//滚动文字数据

    private HomeReceiver homeReceiver;//广播
    private LoadingDialog loadingDialog;//加载框
    private Activity activity;
    private TextView centerT1;
    private TextView centerT2;
    private TextView centerT3;
    private TextView centerT4;
    private TextView number;


    public HomeFragment() {

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public HomeFragment(Activity activity) {
        this.activity = activity;
        rootView = View.inflate(activity, R.layout.fragment_home, null);
        getActivity();
        initView();
        initData();
        listener();
    }

//    public static HomeFragment newInstance(Activity activity) {
//
//        Bundle args = new Bundle();
//        HomeFragment fragment = new HomeFragment(activity);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private void initView() {
        homeReceiver = new HomeReceiver();
        activity.registerReceiver(homeReceiver, new IntentFilter("intent.action.home.data.refresh"));
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.home_refresh);
        banner = (Banner) rootView.findViewById(R.id.home_banner);
        tip = (SwitchText) rootView.findViewById(R.id.home_switch_text);
        relMore = (RelativeLayout) rootView.findViewById(R.id.home_rel_more);

        imgActivityCenter = (ImageView) rootView.findViewById(R.id.home_img_activity_center);
        liActivityCenter = (LinearLayout) rootView.findViewById(R.id.home_li_activity_center);
        imgNewWelfare = (ImageView) rootView.findViewById(R.id.home_img_new_welfare);
        liNewWelfare = (LinearLayout) rootView.findViewById(R.id.home_li_new_welfare);
        imgRecommendGift = (ImageView) rootView.findViewById(R.id.home_img_recommend_gift);
        liRecommendGift = (LinearLayout) rootView.findViewById(R.id.home_li_recommend_gift);
        imgCustomer = (ImageView) rootView.findViewById(R.id.home_img_customer);
        liCustomer = (LinearLayout) rootView.findViewById(R.id.home_li_customer);
        number = (TextView) rootView.findViewById(R.id.home_txt_number);
        txtMarkNumber = (TextView) rootView.findViewById(R.id.home_txt_mark_number);
        txtYearRote = (TextView) rootView.findViewById(R.id.home_txt_rote_num);
        txtLimitDay = (TextView) rootView.findViewById(R.id.home_txt_limit_day);
        btnBuy = (Button) rootView.findViewById(R.id.home_btn_buy);
        liMarkContent = (LinearLayout) rootView.findViewById(R.id.home_li_mark_content);
        imgKnow = (ImageView) rootView.findViewById(R.id.home_img_know);
        centerT1 = (TextView) rootView.findViewById(R.id.home_center1);
        centerT2 = (TextView) rootView.findViewById(R.id.home_center2);
        centerT3 = (TextView) rootView.findViewById(R.id.home_center3);
        centerT4 = (TextView) rootView.findViewById(R.id.home_center4);

        activityDot = rootView.findViewById(R.id.home_activity_red_dot);
        welfareDot = rootView.findViewById(R.id.home_welfare_red_dot);


        //设置
        initTip();
        //banner
        initBanner();
//
//        //=====================test底部图=======================//
//        Glide.with(mActivity)
//                .load("http://images.missyuan.com/attachments/day_110506/20110506_057a83d5f8484ac77fd37QAeup1S36Bs.gif")
//                .asGif()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .skipMemoryCache(false)
//                .into(imgKnow);
//        //=====================test底部图=======================//

    }

    private void initBanner() {

        class GlideImageLoader extends ImageLoader {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                try {
                    Glide.with(context).load(path).into(imageView);
                } catch (Exception e) {
                }
            }
        }

        /**
         * Transformer.Accordion 挤压
         * Transformer.BackgroundToForeground 由小变大
         * Transformer.ForegroundToBackground 有大变小
         * Transformer.CubeIn 矩形翻转
         * Transformer.CubeOut 缩小的矩形翻转
         * Transformer.DepthPage 由浅到深
         * Transformer.FlipHorizontal 水平旋转
         * Transformer.FlipVertical 垂直旋转
         * Transformer.RotateDown 大风车
         * Transformer.RotateUp 大风车
         * Transformer.ScaleInOut 一小一大
         * Transformer.Stack 滑走
         * Transformer.Tablet 矩形翻转
         * Transformer.ZoomIn 逐渐走远
         * Transformer.ZoomOut 逐渐靠近
         * Transformer.ZoomOutSlide 连环画
         */

        bannerData = new ArrayList<>();
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Stack);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
    }

    private void initTip() {
        //数据源
        tipData = new ArrayList<>();
        tip.setDelayMillis(6000);

    }

    private void listener() {

        //刷新控件的监听

        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                requestBannerData();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                // TODO: 2017/7/26 0026 已禁用上拉加载
            }
        });

        //轮播文字点击事件
        tip.setOnClickListener2(new SwitchText.OnClickListener2() {
            @Override
            public void onClick(int position) {

                HomeBannerBean.ArticleListBean bean = tipBeanList.get(position);
                //跳转到详情页
                Intent intent = new Intent(activity, ArticleActivity.class);
                intent.putExtra("id", bean.getId() + "");
                intent.putExtra("header", bean.getTitle());
                activity.startActivity(intent);
            }
        });
        //轮播文字 "更多"
        relMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/7/18 0018 跳转到消息列表
                Intent intent = new Intent(activity, CommuniqueActivity.class);
                activity.startActivity(intent);
            }
        });

        //banner点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                HomeBannerBean.IndexImageItemBean bean = bannerBeanList.get(position);
                String typeTarget = bean.getTypeTarget();
                int type = bean.getType();// 1:新活动 2：老活动
                //跳转类型
                int typetz = bean.getTypetz();
                //逻辑跳转处理
                switch (typetz) {

                    case 1:
                        /////////////跳URL//////////////
                        Intent h5 = new Intent(activity, H5JSActivity.class);
                        h5.putExtra("web_url", typeTarget);
                        activity.startActivity(h5);
                        break;
                    case 2:
                        ///////////进入理财列表//////////
                        Intent manage = new Intent();
                        manage.setAction("HomeFragmentBroadCast");
                        manage.putExtra("current", 1);
                        activity.sendBroadcast(manage);
                        break;
                    case 3:
                        ///////////推荐有礼（有推荐记录，所以独立出来）/////
                        // TODO: 2017/8/10 0010
                        Intent gift = new Intent(activity, H5JSActivity.class);
                        gift.putExtra("flag", "yaoqing");
                        gift.putExtra("web_url", "https://m.hzjcb.com/html/invite/invite.html");
                        activity.startActivity(gift);
                        break;
                    case 4:
                        ///////////活动/////////////
                        Intent ac = new Intent(activity, H5JSActivity.class);
                        ac.putExtra("web_url", typeTarget);
                        activity.startActivity(ac);
                        break;
                    case 5:
                        //////////////文章//////////////
                        // TODO: 2017/8/16 0016  typeTarget: "https://www.hzjcb.com/article/content/736.htm";
                        if (typeTarget.contains("article")) {
                            int index = typeTarget.lastIndexOf("/");
                            String id = typeTarget.substring(index + 1, typeTarget.lastIndexOf("."));
                            Intent article = new Intent(activity, ArticleActivity.class);
                            article.putExtra("id", id);
                            activity.startActivity(article);
                        }
                        break;
                    case 6:
                        ///////////////其他////////////
                        // TODO: 2017/8/10 0010
                        break;
                }
            }
        });

        //进入理财详情页
        liMarkContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentManageDetail = new Intent(activity, ManageDetailActivity.class);
                intentManageDetail.putExtra("id", markId);
                activity.startActivity(intentManageDetail);
            }
        });
        //立即购买
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止快速点击
                if (!PreventFastClickUtils.isFastClick()) {
                    if (!isLogin()) {
                        //登录去go
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    } else {
                        //判断是否设置交易密码
                        checkPassword();
                    }
                }
            }
        });


    }

    private void initData() {

        //请求Banner、活动图片、底部图片数据
        requestBannerData();
    }

    /**
     * 请求banner的数据
     */
    private void requestBannerData() {


//        if (loadingDialog == null) {
//            loadingDialog = new LoadingDialog(mActivity);
//        }
//        loadingDialog.show();

        OkHttpUtils
                .post()
                .url(AppConstants.HOME)
                .addParams("way", "2")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误
                        //请求失败
                        refreshLayout.delayRefreshFinish(PullToRefreshLayout.FAIL, 400);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //刷新完成
                        refreshLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 400);

                        if (response != null) {
                            HomeBannerBean bannerBean = GsonUtils.GsonToBean(response, HomeBannerBean.class);
                            if (bannerBean != null && bannerBean.getRcd().equals("R0001")) {
                                //设置Banner
                                setBannerData(bannerBean);
                                //标
                                setMarkData(bannerBean.getIndexBid());
                                //公告
                                setTipData(bannerBean);

                            }
                        }

                    }
                });
    }

    /**
     * 设置 BANNER、活动栏、底部图片
     * 以及其监听事件
     */
    private void setBannerData(final HomeBannerBean bannerBean) {

        DecimalFormatUtil util = DecimalFormatUtil.getInstance();

        ///////////////////设置banner//////////////////////////

        bannerBeanList = bannerBean.getIndexImageItem();

        if (bannerBeanList != null && bannerBeanList.size() > 0) {

            //清空一次数据
            if (!bannerData.isEmpty()) {
                bannerData.clear();
            }

            for (int i = 0, len = bannerBeanList.size(); i < len; i++) {
                HomeBannerBean.IndexImageItemBean listBean = bannerBeanList.get(i);
                bannerData.add(listBean.getImageUrl());
            }
            //设置数据源
            banner.setImages(bannerData);
            //开始
            banner.start();
        }

        ///////////////////////////活动、底部图片///////////////////////
        /**
         * 安全保障
         * 新手福利
         * 推荐有礼
         * 活动中心
         */
        List<HomeBannerBean.IndexHomeImageBean> activityList = bannerBean.getIndexHomeImage();

        if (activityList == null) {
            return;
        }

        for (int i = 0, len = activityList.size(); i < len; i++) {
            final HomeBannerBean.IndexHomeImageBean bean = activityList.get(i);

            switch (bean.getOrder()
                    ) {

                //====活动中心====//
                case 1:
//                    Glide.with(activity)
//                            .load(bean.getImageUrl())
//                            .skipMemoryCache(false)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(imgActivityCenter);

                    //获取上一次活动中心消息得数量
                    int lastActivityNum = PrefUtils.getInt(activity, "lastActivityNum", 0);


                    centerT1.setText(bean.getDescription());

                    //监听事件
                    liActivityCenter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ActivityCenterActivity.class);
                            intent.putExtra("title", bean.getDescription());
                            activity.startActivity(intent);
                        }
                    });

                    break;

                //====新手福利====//
                case 2:
//                    Glide.with(activity)
//                            .load(bean.getImageUrl())
//                            .skipMemoryCache(false)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(imgNewWelfare);
                    centerT2.setText(bean.getDescription());
                    //红点设置
                    if (PrefUtils.getBoolean(activity, "welfare", true)) {
                        //新手福利红点，初次安装显示，点击后不再显示
                        //设置红点显示
                        welfareDot.setVisibility(View.VISIBLE);
                    }

                    //监听事件
                    liNewWelfare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent welfare = new Intent(activity, H5JSActivity.class);
                            welfare.putExtra("web_url", bean.getLinkUrl());
                            welfare.putExtra("flag", "welfare");
                            activity.startActivity(welfare);
                        }
                    });
                    break;

                //====推荐有礼====//
                case 3:
//                    Glide.with(activity)
//                            .load(bean.getImageUrl())
//                            .skipMemoryCache(false)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(imgRecommendGift);
                    centerT3.setText(bean.getDescription());
                    //监听事件
                    liRecommendGift.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent welfare = new Intent(activity, H5JSActivity.class);
                            welfare.putExtra("web_url", bean.getLinkUrl());
                            welfare.putExtra("flag", "yaoqing");
                            activity.startActivity(welfare);
                        }
                    });
                    break;

                //====在线客服====//
                case 4:
//                    Glide.with(activity)
//                            .load(bean.getImageUrl())
//                            .skipMemoryCache(false)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(imgCustomer);
                    centerT4.setText(bean.getDescription());
                    //监听事件
                    liCustomer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //启动在线客服
                            activity.startActivity(new Intent(activity, CustomerActivity.class));
                        }
                    });

                    break;

                //====底部====//
                case 5:
                    Glide.with(activity)
                            .load(bean.getImageUrl())
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgKnow);
                    //监听事件
                    imgKnow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent aboutUs = new Intent(activity, H5JSActivity.class);
                            aboutUs.putExtra("web_url", AppConstants.ABOUT_US);
                            activity.startActivity(aboutUs);
                        }
                    });

                    break;

                default:

            }

        }
    }

    /**
     * 设置滚动文字数据
     */
    private void setTipData(HomeBannerBean tipBean) {

        tipBeanList = tipBean.getArticleList();

        ////////////////////设置tips////////////////////////////////

        //清空一次数据
        if (!tipData.isEmpty()) {
            tipData.clear();
        }

        if (tipBeanList != null && tipBeanList.size() > 0) {

            for (int i = 0, len = tipBeanList.size(); i < len; i++) {
                HomeBannerBean.ArticleListBean listBean = tipBeanList.get(i);
                tipData.add(listBean.getTitle());
            }
            //设置数据源
            tip.setData(tipData);
            tip.start();
        }

    }


    /**
     * 设置标的数据
     */
    private void setMarkData(HomeBannerBean.IndexBidBean bean) {
        DecimalFormatUtil util = DecimalFormatUtil.getInstance();
        //全局id
        markId = bean.getId();
        //标的期数
        txtMarkNumber.setText(bean.getName());
        //年化收益
        txtYearRote.setText(util.getDouble2(bean.getApr()));
        number.setText("已售"+bean.getSchedule()+"%");

        //项目期限
        txtLimitDay.setText(bean.getTimeLimit() + "天");
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

        Intent intent = new Intent(activity, ManageDetailActivity.class);
        intent.putExtra("id", markId);
//        intent.putExtra("isBund", isBound);
        activity.startActivity(intent);
    }

    /**
     * 是否设置交易密码
     */
    private void checkPassword() {

        SharedPreferences preferences = activity.getSharedPreferences("AppToken", Context.MODE_PRIVATE);
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
                        Toast.makeText(activity, "交易密码网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {
                            DealPasswordBean dealPasswordBean = GsonUtils.GsonToBean(response, DealPasswordBean.class);

                            switch (dealPasswordBean.getRcd()) {
                                case "R0001":
                                    int dealStatus = dealPasswordBean.getPayPasswordStatus();
                                    //判断是否设置交易密码
                                    if (dealStatus == 1) {
                                        //已设置交易密码
                                        toBuy();
                                    } else {
                                        //弹出设置交易密码窗口
                                        setDealPassword();
                                    }
                                    break;
                                case "E0001":
                                    //抢登
                                    Intent login = new Intent(activity, LoginActivity.class);
                                    activity.startActivity(login);
                                    break;
                                case "R0003":
                                    //
                                    Toast.makeText(activity, "账号被锁定", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                });
    }

    /**
     * 到确认投资页面
     */
    private void toBuy() {
        //是否绑定银行卡
        RequestForRechargeTo();
    }


    /**
     * 获取银行卡信息
     */
    public void RequestForRechargeTo() {

        SharedPreferences preferences = activity.getSharedPreferences("AppToken", Context.MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");


        final LoadingDialog loadingDialog = new LoadingDialog(activity);
        loadingDialog.showLoading();

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
                                        Intent login = new Intent(activity, LoginActivity.class);
                                        activity.startActivity(login);
                                        break;
                                    case "R0003":
                                        //
                                        Toast.makeText(activity, "账号被锁定", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                        }
                    }
                });
    }

    @Override
    public View getmView() {
        return rootView;
    }

    /**
     * 首页广播
     */
    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getIntExtra("home", 0)) {

            }
        }
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    private boolean isLogin() {

        SharedPreferences preferences = activity.getSharedPreferences("AppToken", Context.MODE_PRIVATE);
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

        final CancleMessageDialog dialog = new CancleMessageDialog(activity, R.style.ActionSheetDialogStyle);
        dialog.setTitle("请设置交易密码");
        dialog.setCancleColour("#cccccc");
        dialog.setCancleText("暂不考虑");
        dialog.setMessage("立即设置");
        dialog.show();
        dialog.getMessage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(activity, SafePassWordActivity.class);
                activity.startActivity(intent);
            }
        });

    }




    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
//        banner.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(homeReceiver);
        //取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
