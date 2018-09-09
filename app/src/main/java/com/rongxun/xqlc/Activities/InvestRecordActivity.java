package com.rongxun.xqlc.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Fragments.RepayMentFragment;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.slidingtab.SlidingTabLayout;

import java.util.ArrayList;
/**
 * Created by jcb on 2017/5/16.
 */

public class InvestRecordActivity extends MyBaseActivity {

    private RepayMentFragment ingFragment;
    private RepayMentFragment alreadyFragment;
    private String[] titles;
    private ArrayList<Fragment> al;
    private TextView right;
    private RepayMentFragment allFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_inverst_record);
        initView();
    }

    private void initView() {
        right = (TextView) findViewById(R.id.project_invest_toolbar_right);
        ViewPager viewPager= (ViewPager) findViewById(R.id.invest_record_pager);
        SlidingTabLayout tabLayout= (SlidingTabLayout) findViewById(R.id.invest_record_tab);
        IconFontTextView back= (IconFontTextView) findViewById(R.id.invest_record_back);
        allFragment = RepayMentFragment.newInstance("0");
        ingFragment = RepayMentFragment.newInstance("1");
        alreadyFragment = RepayMentFragment.newInstance("2");
        titles = new String[]{"全部","持有中", "已回款"};
        al = new ArrayList<Fragment>();
        al.add(allFragment);
        al.add(ingFragment);
        al.add(alreadyFragment);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        tabLayout.setViewPager(viewPager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvestRecordActivity.this, ExplaneActivity.class));
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
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
