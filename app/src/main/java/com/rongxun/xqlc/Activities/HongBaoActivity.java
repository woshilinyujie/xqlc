package com.rongxun.xqlc.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.UserJIaxiBean;
import com.rongxun.xqlc.Beans.user.UserHongbaoBean;
import com.rongxun.xqlc.Fragments.HongBaoDetailFragment;
import com.rongxun.xqlc.Fragments.JiaXiDetailFragment;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.slidingtab.SlidingTabLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import okhttp3.Call;

public class HongBaoActivity extends MyBaseActivity  {

    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/hbListLooked";
    private String basicUrlJiaxi = AppConstants.URL_SUFFIX + "/rest/cpListLooked";
    HongBaoDetailFragment unusedFragment;
    HongBaoDetailFragment usedFragment;
    HongBaoDetailFragment haveExpiredFragment;
    private ArrayList<Fragment> fragmentList;
    private TextView explanation;
    private ViewPager hongbao_pager;
    //    private SlidingTabLayout hongbao_tab;
    private String unused, used, haveExpired;
    private LoadingDialog loadingDialog;
    private JiaXiDetailFragment unusedJiaxiFragment;
    private TabLayout hongbao_tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_hong_bao);
        initViews(this);
        RequestForListData(basicUrl, 1, 10);
        RequestForListDataJiaxi(basicUrlJiaxi, 1, 10);

    }

    public void initViews(final Activity activity) {
        IconFontTextView back = (IconFontTextView) findViewById(R.id.hongbao_back);
        hongbao_pager = (ViewPager) findViewById(R.id.hongbao_pager);
//        hongbao_tab = (SlidingTabLayout) findViewById(R.id.hongbao_tab);
        explanation = (TextView) findViewById(R.id.hongbao_explain);
        hongbao_tb = (TabLayout) findViewById(R.id.hongbao_tb);
        unusedFragment = HongBaoDetailFragment.newInstance(0);
        unusedJiaxiFragment = JiaXiDetailFragment.newInstance(0);
        fragmentList = new ArrayList<>();
        fragmentList.add(unusedFragment);
        fragmentList.add(unusedJiaxiFragment);
        //红包说明
        explanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ArticleActivity.class);
                intent.putExtra("id", "655");
                intent.putExtra("header", "红包说明");
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        hongbao_pager.setAdapter(new InnerPagerAdapter(getSupportFragmentManager()));
        hongbao_tb.setupWithViewPager(hongbao_pager);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("LoginContentBroadCast"));
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


    /**
     * 请求红包数据
     */
    private void RequestForListData(String basicUrl, int pageNumber, int pageSize) {

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(HongBaoActivity.this, "loginToken", ""))
                .addParams("pager.pageSize", pageSize + "")
                .addParams("pager.pageNumber", pageNumber + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        final UserHongbaoBean resultBean = JSON.parseObject(s, UserHongbaoBean.class);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }

                        if (resultBean.getRcd().equals("R0001")) {
                            unused = resultBean.getHbNum();
                            used = resultBean.getUseNum();
                            unusedFragment.setcount(unused);
                        }
                    }
                });

    }
    /**
     * 请求加息数据
     */
    private void RequestForListDataJiaxi(String basicUrl, int pageNumber, int pageSize) {

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(HongBaoActivity.this, "loginToken", ""))
                .addParams("pager.pageSize", pageSize + "")
                .addParams("pager.pageNumber", pageNumber + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        final UserJIaxiBean resultBean = JSON.parseObject(s, UserJIaxiBean.class);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }

                        if (resultBean.getRcd().equals("R0001")) {
                            unused = resultBean.getCpNum();
                            unusedJiaxiFragment.setcount(unused);
                        }
                    }
                });
    }



    String titles[]={"低折扣","加息劵"};
    class InnerPagerAdapter extends FragmentPagerAdapter {


        public InnerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


}
