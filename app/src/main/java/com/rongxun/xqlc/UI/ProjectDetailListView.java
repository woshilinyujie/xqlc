package com.rongxun.xqlc.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.UI
 * Author: HouShengLi
 * Time: 2017/07/17 14:11
 * E-mail: 13967189624@163.com
 * Description:
 */

public class ProjectDetailListView extends ListView {

    public ProjectDetailListView(Context context) {
        super(context);
    }

    public ProjectDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProjectDetailListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProjectDetailListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
