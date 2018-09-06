package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Fragments.AlreadyFragment;
import com.rongxun.xqlc.Fragments.AssetFragment;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.slidingtab.SlidingTabLayout;
import com.rongxun.xqlc.chart.DemoBase;

import java.util.ArrayList;

/**
 * Created by jcb on 2017/5/16.
 */

public class AssetstatisticsActivity extends DemoBase {
    private AssetFragment ingFragment;
    private AlreadyFragment alreadyFragment;
    private String[] titles;
    private ArrayList<Fragment> al;
    private RelativeLayout name_explain_rl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_assetstics);
        initView();
    }


    private void initView() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.Asset_viewpager);
        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.invest_record_tab);
        IconFontTextView back = (IconFontTextView) findViewById(R.id.invest_record_back);
        name_explain_rl = (RelativeLayout) findViewById(R.id.name_explain_rl);
        ingFragment = new AssetFragment();
        alreadyFragment = new AlreadyFragment();
        titles = new String[]{"我的资产", "累计收益"};
        al = new ArrayList<Fragment>();
        al.add(ingFragment);
        al.add(alreadyFragment);
        tabLayout.setViewPager(viewPager, titles, this, al);

        Intent intert = getIntent();
        String id = intert.getStringExtra("leiji");
        if(id!=null){
        if (id.equals("leiji")) {
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    viewPager.setCurrentItem(1);
                }
            }, 50);
        }
        }

        //名词解释点击事件
        name_explain_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AssetstatisticsActivity.this, ExplaneActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        CustomApplication.removeActivity(this);
        super.onDestroy();
    }
}

