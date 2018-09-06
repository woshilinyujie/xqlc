package com.rongxun.xqlc.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liaoinstan.springview.container.BaseHeader;
import com.liaoinstan.springview.utils.DensityUtil;
import com.rongxun.xqlc.R;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.UI
 * Author: HouShengLi
 * Time: 2017/08/07 19:41
 * E-mail: 13967189624@163.com
 * Description:
 */

public class SpringHeader extends BaseHeader {

    private Context context;

    public SpringHeader(Context context) {
        this.context = context;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.spring_head_footer, viewGroup, true);
        return view;
    }

    @Override
    public int getDragLimitHeight(View rootView) {
        return DensityUtil.dip2px(context, 70);
    }

    @Override
    public int getDragMaxHeight(View rootView) {
        return rootView.getMeasuredHeight();
    }

    @Override
    public int getDragSpringHeight(View rootView) {
        return DensityUtil.dip2px(context, 70);
    }

    @Override
    public void onPreDrag(View rootView) {

    }

    @Override
    public void onDropAnim(View rootView, int dy) {

    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {

    }

    @Override
    public void onStartAnim() {

    }

    @Override
    public void onFinishAnim() {

    }
}
