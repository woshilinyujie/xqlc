package com.rongxun.xqlc.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.rongxun.xqlc.Fragments.BaseFragment;

import java.util.List;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.adapter
 * Author: HouShengLi
 * Time: 2017/06/07 19:29
 * E-mail: 13967189624@163.com
 * Description:
 */

public class MainFragmentPagerAdapter extends PagerAdapter {
    private final FragmentManager fragmentManager;
    List<BaseFragment> fragments;


    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(fragments.get(position).getmView());
        return fragments.get(position).getmView();

    }

//
    public MainFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        this.fragments = fragments;
        fragmentManager =fm;
    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return fragments.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return fragments != null ? fragments.size() : 0;
//    }



}
