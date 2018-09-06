package com.rongxun.xqlc.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class HongBaoActivity extends MyBaseActivity implements View.OnClickListener {

    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/hbListLooked";
    private String basicUrlJiaxi = AppConstants.URL_SUFFIX + "/rest/cpListLooked";
    HongBaoDetailFragment unusedFragment;
    HongBaoDetailFragment usedFragment;
    HongBaoDetailFragment haveExpiredFragment;
    private ArrayList<Fragment> fragmentList;
    private String[] titles;
    private TextView explanation;
    private ViewPager hongbao_pager;
    //    private SlidingTabLayout hongbao_tab;
    private String unused, used, haveExpired;
    private LoadingDialog loadingDialog;
    private Button jiaxi_jiaxi;
    private Button jiaxi_hongbao;
    private JiaXiDetailFragment unusedJiaxiFragment;

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
        jiaxi_hongbao = (Button) findViewById(R.id.hongbao_jiaxi_hongbao);
        jiaxi_jiaxi = (Button) findViewById(R.id.hongbao_jiaxi_jiaxi);
        unusedFragment = HongBaoDetailFragment.newInstance(0);
        unusedJiaxiFragment = JiaXiDetailFragment.newInstance(0);
//        usedFragment = HongBaoDetailFragment.newInstance(1);
//        haveExpiredFragment = HongBaoDetailFragment.newInstance(2);
        fragmentList = new ArrayList<>();
//        fragmentList.add(unusedFragment);
        fragmentList.add(unusedFragment);
        fragmentList.add(unusedJiaxiFragment);
//        fragmentList.add(haveExpiredFragment);
        jiaxi_hongbao.setOnClickListener(this);
        jiaxi_jiaxi.setOnClickListener(this);

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
        hongbao_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    jiaxi_hongbao.setBackgroundColor(Color.parseColor("#FA5454"));
                    jiaxi_jiaxi.setBackgroundColor(Color.parseColor("#cccccc"));
                }else{
                    jiaxi_hongbao.setBackgroundColor(Color.parseColor("#cccccc"));
                    jiaxi_jiaxi.setBackgroundColor(Color.parseColor("#FA5454"));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("LoginContentBroadCast"));
        CustomApplication.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hongbao_jiaxi_hongbao:
                jiaxi_hongbao.setBackgroundColor(Color.parseColor("#FA5454"));
                jiaxi_jiaxi.setBackgroundColor(Color.parseColor("#cccccc"));
                hongbao_pager.setCurrentItem(0);
                break;
            case R.id.hongbao_jiaxi_jiaxi:
                jiaxi_hongbao.setBackgroundColor(Color.parseColor("#cccccc"));
                jiaxi_jiaxi.setBackgroundColor(Color.parseColor("#FA5454"));
                hongbao_pager.setCurrentItem(1);
                break;
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


    /**
     * 请求红包数据
     */
    private void RequestForListData(String basicUrl, int pageNumber, int pageSize) {
//        if (loadingDialog == null) {
//            loadingDialog = new LoadingDialog(HongBaoActivity.this);
//        }
//        loadingDialog.show();

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(HongBaoActivity.this, "loginToken", ""))
                .addParams("pager.pageSize", pageSize + "")
                .addParams("pager.pageNumber", pageNumber + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        if (loadingDialog != null && loadingDialog.isShowing()) {
//                            loadingDialog.dismiss();
//                            loadingDialog = null;
//                        }
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
                            haveExpired = resultBean.getExpiredNum();

//                            String a = "未使用 (" + unused + ")";
//                            String b = "已使用 (" + used + ")";
//                            String c = "已过期 (" + haveExpired + ")";
//                            titles = new String[]{a, b, c};
//                            hongbao_tab.setViewPager(hongbao_pager, titles, HongBaoActivity.this, fragmentList);
                            jiaxi_hongbao.setText("红包"+unused+"个");
                        }
                    }
                });
    }
    /**
     * 请求加息数据
     */
    private void RequestForListDataJiaxi(String basicUrl, int pageNumber, int pageSize) {
//        if (loadingDialog == null) {
//            loadingDialog = new LoadingDialog(HongBaoActivity.this);
//        }
//        loadingDialog.show();

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
//                            used = resultBean.getUseNum();
//                            haveExpired = resultBean.getExpiredNum();

//                            String a = "未使用 (" + unused + ")";
//                            String b = "已使用 (" + used + ")";
//                            String c = "已过期 (" + haveExpired + ")";
//                            titles = new String[]{a, b, c};
//                            hongbao_tab.setViewPager(hongbao_pager, titles, HongBaoActivity.this, fragmentList);
                            jiaxi_jiaxi.setText("加息卷"+unused+"张");
                        }
                    }
                });
    }


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
    }


}
