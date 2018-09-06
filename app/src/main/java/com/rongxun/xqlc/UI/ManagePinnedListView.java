package com.rongxun.xqlc.UI;

import android.content.Context;
import android.util.AttributeSet;

import com.rongxun.xqlc.UI.pullableview.Pullable;

import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.UI
 * Author: HouShengLi
 * Time: 2017/07/27 09:49
 * E-mail: 13967189624@163.com
 * Description:
 */

public class ManagePinnedListView extends PinnedSectionListView implements Pullable {
    public ManagePinnedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ManagePinnedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0) {
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {

        //禁用上拉
        return false;
    }
}
