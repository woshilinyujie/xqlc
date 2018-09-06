package com.rongxun.xqlc.Activities;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Fragments.FinancialDetailsFragment;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.slidingtab.SlidingTabLayout;

import java.util.ArrayList;

public class UserAccDetailActivity extends MyBaseActivity  {
    IconFontTextView userAccDetailToolbarBack;
    Toolbar userAccDetailToolbar;
    private String[] titles;
    private ArrayList<Fragment> al;
    private SlidingTabLayout tabLayout;
    FinancialDetailsFragment allFragment,investFragment,returnedFragment,rechargeFragment,withdrawFragment,awardFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_acc_detail);
        CustomApplication.addActivity(this);
        initView();
        initToolBar();
    }


    private void initView() {
        userAccDetailToolbarBack=(IconFontTextView)findViewById(R.id.user_acc_detail_toolbar_back);
        tabLayout=(SlidingTabLayout)findViewById(R.id.invest_record_tab);
        ViewPager viewPager= (ViewPager) findViewById(R.id.financial_detailsr_vp);
        titles = new String[]{"全部", "投资","回款","充值","提现","奖励"};
        allFragment = FinancialDetailsFragment.newInstance("");
        investFragment = FinancialDetailsFragment.newInstance("account_borrow_tender");
        returnedFragment = FinancialDetailsFragment.newInstance("account_borrow_hk");
        rechargeFragment = FinancialDetailsFragment.newInstance("account_recharge");
        withdrawFragment = FinancialDetailsFragment.newInstance("account_borrow_cash");
        awardFragment = FinancialDetailsFragment.newInstance("account_link");


        al = new ArrayList<Fragment>();
        al.add(allFragment);
        al.add(investFragment);
        al.add(returnedFragment);
        al.add(rechargeFragment);
        al.add(withdrawFragment);
        al.add(awardFragment);

        tabLayout.setViewPager(viewPager,titles,UserAccDetailActivity.this,al);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void initToolBar() {
        userAccDetailToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(userAccDetailToolbar);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }

}
