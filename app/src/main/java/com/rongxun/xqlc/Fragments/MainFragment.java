package com.rongxun.xqlc.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rongxun.xqlc.Activities.MainActivity;
import com.rongxun.xqlc.Adapters.MainFragmentPagerAdapter;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.MainViewPager;
import com.rongxun.xqlc.Util.AppConstants;

import java.util.ArrayList;

/**
 * Created by jcb on 2017/10/25.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener{

    private View rootView;
    private LinearLayout liHome;
    private LinearLayout liManage;
    private LinearLayout liMine;
    private ImageView imgHome;
    private ImageView imgManage;
    private ImageView imgMine;
    private TextView txtHome;
    private TextView txtManage;
    private TextView txtMine;
    private MainViewPager pager;
    private ArrayList<BaseFragment> fragments;
    private HomeFragment homeFragment;
    private ManageFragment manageFragment;
    private MineFragment mineFragment;
    private MainActivity mActivity;
    public static final int HOME = 0;//首页
    public static final int MANAGE = 1;//理财
    public static final int MINE = 2;//我的
    private MainActivityBroadReceiver broadReceiver;
    private Activity activity;
    private String flag;

//    public static MainFragment newInstance(){
//        Bundle args = new Bundle();
//        MainFragment fragment=new MainFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    public MainFragment(){

    }

    @SuppressLint({"NewApi", "ValidFragment"})
   public  MainFragment(Activity activity,String flag){
       rootView = View.inflate(activity,R.layout.fragment_main,null);
       this.activity =activity;
       this.flag=flag;
       initView();
       listener();
       initViewPager();
   }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    private void initView() {
        //初始化广播
        broadReceiver = new MainActivityBroadReceiver();
        liHome = (LinearLayout) rootView.findViewById(R.id.main_line_home_tab);
        IntentFilter intentFilter = new IntentFilter("HomeFragmentBroadCast");
        activity.registerReceiver(broadReceiver, intentFilter);
        liManage = (LinearLayout) rootView.findViewById(R.id.main_line_manage_tab);
        liMine = (LinearLayout) rootView.findViewById(R.id.main_line_mine_tab);

        imgHome = (ImageView) rootView.findViewById(R.id.main_home_image);
        imgManage = (ImageView) rootView.findViewById(R.id.main_manage_image);
        imgMine = (ImageView) rootView.findViewById(R.id.main_mine_image);
        txtHome = (TextView) rootView.findViewById(R.id.main_home_text);
        txtManage = (TextView) rootView.findViewById(R.id.main_manage_text);
        txtMine = (TextView)rootView. findViewById(R.id.main_mine_text);
        pager = (MainViewPager)rootView. findViewById(R.id.main_vp);
    }

    private void initViewPager() {
        mActivity= (MainActivity) activity;
        fragments = new ArrayList<BaseFragment>();
        homeFragment =new HomeFragment(activity);
        manageFragment = new ManageFragment(activity);
        mineFragment = new MineFragment(activity);

        fragments.add(homeFragment);
        fragments.add(manageFragment);
        fragments.add(mineFragment);

        //预加载3页，缓存
//        pager.setOffscreenPageLimit(2);
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(mActivity.getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);
        if (flag != null && flag.equals("ads")) {
            pager.setCurrentItem(MANAGE);
        }
    }



    private void listener() {

        liHome.setOnClickListener(this);
        liManage.setOnClickListener(this);
        liMine.setOnClickListener(this);

        //viewpager的监听
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case HOME:
                        setHomeTab();
                        break;
                    case MANAGE:
                        setManageTab();
                        //数据赖加载
                        if (manageFragment.isFrist) {
                            manageFragment.requestData(AppConstants.MANAGE);
                            manageFragment.isFrist = false;
                        }
                        break;
                    case MINE:
                        //进一次，刷一次数据
                        mineFragment.CustomerServiceNet();
                        mineFragment.RequestForMoney(activity,null);
                        setMineTab();
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_line_home_tab:
                //显示首页
                pager.setCurrentItem(HOME);
                break;
            case R.id.main_line_manage_tab:
                //显示理财页
                pager.setCurrentItem(MANAGE);
                break;
            case R.id.main_line_mine_tab:
                //显示 我的页面
                pager.setCurrentItem(MINE);
                break;
        }
    }


    /**
     * 首页tab
     */
    private void setHomeTab() {

        imgHome.setBackgroundResource(R.mipmap.shouye_selected);
        txtHome.setTextColor(Color.parseColor("#3574fa"));
        imgManage.setBackgroundResource(R.mipmap.list);
        txtManage.setTextColor(Color.parseColor("#666666"));
        imgMine.setBackgroundResource(R.mipmap.zhanghu);
        txtMine.setTextColor(Color.parseColor("#666666"));
    }

    /**
     * 理财页tab
     */
    private void setManageTab() {

        imgHome.setBackgroundResource(R.mipmap.shouye);
        txtHome.setTextColor(Color.parseColor("#666666"));
        imgManage.setBackgroundResource(R.mipmap.list_selected);
        txtManage.setTextColor(Color.parseColor("#3574fa"));
        imgMine.setBackgroundResource(R.mipmap.zhanghu);
        txtMine.setTextColor(Color.parseColor("#666666"));
    }

    /**
     * 我的 tab
     */
    private void setMineTab() {

        imgHome.setBackgroundResource(R.mipmap.shouye);
        txtHome.setTextColor(Color.parseColor("#666666"));
        imgManage.setBackgroundResource(R.mipmap.list);
        txtManage.setTextColor(Color.parseColor("#666666"));
        imgMine.setBackgroundResource(R.mipmap.zhanghu_selected);
        txtMine.setTextColor(Color.parseColor("#3574fa"));
    }

    @Override
    public View getmView() {
        return rootView;
    }


    /**
     * 广播
     */
    class MainActivityBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getIntExtra("current", 100) == 1) {
                //显示理财页
                pager.setCurrentItem(MANAGE);
            } else if (intent.getIntExtra("Login", 0) == 101) {
                //显示我的
                pager.setCurrentItem(MINE);
            } else if (intent.getIntExtra("Exit", 0) == 103) {
                try {
                    mineFragment.exit();
                }catch (Exception e){

                }
            } else if (intent.getIntExtra("gesture", 100) == 200) {
                //显示首页
                pager.setCurrentItem(HOME);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(broadReceiver!=null)
            activity.unregisterReceiver(broadReceiver);
    }
}
