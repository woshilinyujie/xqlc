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
import com.rongxun.xqlc.Fragments.HistoryHongBaoDetailFragment;
import com.rongxun.xqlc.Fragments.HongBaoDetailFragment;
import com.rongxun.xqlc.Fragments.JiaXiDetailFragment;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import okhttp3.Call;

public class HistoryHongbaoActivity extends MyBaseActivity {

    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/hbListLooked";
    private String basicUrlJiaxi = AppConstants.URL_SUFFIX + "/rest/cpListLooked";
    HongBaoDetailFragment unusedFragment;
    HistoryHongBaoDetailFragment usedFragment;
    HistoryHongBaoDetailFragment haveExpiredFragment;
    private ArrayList<Fragment> fragmentList;
    private String[] titles;
    private TextView explanation;
    private ViewPager hongbao_pager;
    private JiaXiDetailFragment unusedJiaxiFragment;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_hong_bao);
        initViews(this);

    }

    public void initViews(final Activity activity) {
        IconFontTextView back = (IconFontTextView) findViewById(R.id.hongbao_back);
        hongbao_pager = (ViewPager) findViewById(R.id.hongbao_pager);
        explanation = (TextView) findViewById(R.id.hongbao_explain);
        title = (TextView) findViewById(R.id.hongbao_title);
        usedFragment = HistoryHongBaoDetailFragment.newInstance(1);
        haveExpiredFragment = HistoryHongBaoDetailFragment.newInstance(2);
        fragmentList = new ArrayList<>();
        fragmentList.add(usedFragment);
        fragmentList.add(haveExpiredFragment);

        explanation.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        hongbao_pager.setAdapter(new InnerPagerAdapter(getSupportFragmentManager()));
        title.setText("历史红包");
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
