package com.rongxun.xqlc.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.rongxun.xqlc.R;

import java.util.List;

/**
 * PROJECT_NAME: Sample
 * PACKAGE_NAME: com.guaika.sample
 * Author: HouShengLi
 * Time: 2017/05/09 14:36
 * E-mail: 13967189624@163.com
 * Description:
 */

public class SwitchText extends TextSwitcher {

    private Context mContext;
    private List<String> data;//显示的文本数据
    private static final int AD_FALG = 110;
    private int index = -1;//下标
    private long delayMillis = 3000;//默认轮播时长3S
    private boolean isFirst = true;

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                // 广告
                case AD_FALG:
                    //setText的内容与当前下标保持一致
                    if (data != null && data.size() > 0) {
                        ++index;
                        if (index == data.size()) {
                            index = 0;
                        }
                        setText(data.get(index));
                        mHandler.sendEmptyMessageDelayed(AD_FALG, delayMillis);
                    }

                    break;
            }
        }
    };

    public SwitchText(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public SwitchText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {

        setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(mContext);
                // 设置文字的显示单位以及文字的大小
                tv.setTextSize(11);
                tv.setTextColor(Color.parseColor("#666666"));
                tv.setLines(1);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                //设置文字垂直居中
                LayoutParams lp = new LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                lp.gravity = Gravity.CENTER_VERTICAL;
                tv.setLayoutParams(lp);
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener2 != null) {
                            onClickListener2.onClick(index);
                        }
                    }
                });
                return tv;
            }
        });

        setInAnimation(mContext, R.anim.enter_bottom);
        setOutAnimation(mContext, R.anim.leave_top);

    }

    public void start() {

        if (isFirst) {
            mHandler.sendEmptyMessage(AD_FALG);
            isFirst = false;
        }

    }

    /**
     * 监听接口
     */
    public interface OnClickListener2 {

        void onClick(int position);
    }

    private OnClickListener2 onClickListener2;

    public void setOnClickListener2(OnClickListener2 onClickListener2) {
        this.onClickListener2 = onClickListener2;
    }
}
