package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Fragments.BackMoneyFragment;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.slidingtab.SlidingTabLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;


public class BackMoneyActivity extends MyBaseActivity {

    IconFontTextView repaymentToolbarBack;
    Toolbar repaymentToolbar;

    private Fragment payedFragment;
    private Fragment unpayedFragment;
    private Fragment currentFragment;
    private SlidingTabLayout tab;
    private ViewPager pager;
    private ArrayList<Fragment> al;
    private String[] titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_black_money);
        tab = (SlidingTabLayout) findViewById(R.id.content_repay_ment_tab);
        pager = (ViewPager) findViewById(R.id.content_repay_ment_pager);
        repaymentToolbarBack  = (IconFontTextView) findViewById(R.id.repayment_toolbar_back);
        repaymentToolbar  = (Toolbar) findViewById(R.id.repayment_toolbar);
        initToolBar();
        unpayedFragment = BackMoneyFragment.newInstance("0");
        payedFragment = BackMoneyFragment.newInstance("1");
        titles = new String[]{"待回款", "已回款"};
        al = new ArrayList<Fragment>();
        al.add(unpayedFragment);
        al.add(payedFragment);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return al.get(position);
            }

            @Override
            public int getCount() {
                return al.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        //绑定 vp
        tab.setViewPager(pager);

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    public void initToolBar() {
        repaymentToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
//        repaymentToolbarRadioGroup.setOnCheckedChangeListener(this);
        setSupportActionBar(repaymentToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent("LoginContentBroadCast"));
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }


}
