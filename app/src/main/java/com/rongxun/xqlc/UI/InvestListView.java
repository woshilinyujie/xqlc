package com.rongxun.xqlc.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.rongxun.xqlc.UI.pullableview.Pullable;


/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.view
 * Author: HouShengLi
 * Time: 2017/06/09 14:57
 * E-mail: 13967189624@163.com
 * Description:
 */

public class InvestListView extends ListView implements Pullable {

    public InvestListView(Context context) {
        super(context);
    }

    public InvestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InvestListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canPullDown() {
        //不可下拉
        return false;
    }

    @Override
    public boolean canPullUp() {
        if (getCount() == 0) {
            // 没有item的时候bu可以上拉加载
            return false;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部了
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(
                    getLastVisiblePosition()
                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }

        return false;
    }
}
