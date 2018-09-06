package com.rongxun.xqlc.UI;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.UI
 * Author: HouShengLi
 * Time: 2017/07/13 11:53
 * E-mail: 13967189624@163.com
 * Description:
 */

public class HomeSwipeRefreshLayout extends SwipeRefreshLayout {

    private float lastX;
    private float lastY;
    boolean ismovepic = false;

    public HomeSwipeRefreshLayout(Context context) {
        super(context);
    }

    public HomeSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {



        if (ev.getAction() ==MotionEvent.ACTION_DOWN){
            lastX = ev.getX();
            lastY = ev.getY();
            ismovepic = false;
            return super.onInterceptTouchEvent(ev);
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        int x2 = (int) Math.abs(ev.getX() - lastX);
        int y2 = (int) Math.abs(ev.getY() - lastY);

        //滑动图片最小距离检查
        if (x2>y2){
            if (y2>=100)ismovepic = true;
            return false;
        }

        //是否移动图片(下拉刷新不处理)
        if (ismovepic){
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
