package com.rongxun.xqlc.UI;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.rongxun.xqlc.Beans.Borrow.Acount;
import com.rongxun.xqlc.R;

import java.util.List;


/**
 * PROJECT_NAME: Sample
 * PACKAGE_NAME: com.guaika.sample.utils
 * Author: HouShengLi
 * Time: 2017/05/18 19:04
 * E-mail: 13967189624@163.com
 * Description:
 */

public class TipsFlipper extends ViewFlipper {

    public static final int MSG = 0X11;
    private Context mContext;
    private LayoutInflater inflater;
    private List<Acount> datas;
    private boolean isFirst = true;

    private int mCurrPos;//当前显示的view

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG:
                    moveNext();
                    Log.d("Task", "下一个");
                    mHandler.sendEmptyMessageDelayed(MSG, 6000);
                    break;
            }
        }
    };

    public TipsFlipper(Context context) {
        super(context);
        mContext = context;
        inflater = LayoutInflater.from(context);
    }


    public TipsFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflater = LayoutInflater.from(context);
    }


    public void start() {

        if (isFirst) {
            mHandler.sendEmptyMessage(MSG);
            isFirst = false;
        }

    }

    private void moveNext() {
        setView(mCurrPos, mCurrPos + 1);
        setInAnimation(mContext, R.anim.enter_bottom);
        setOutAnimation(mContext, R.anim.leave_top);
        showNext();
    }

    private void setView(int curr, int next) {

        View noticeView = inflater.inflate(R.layout.notice_item, null);
        TextView txtTime = (TextView) noticeView.findViewById(R.id.manage_txt_time);
        TextView txtUser = (TextView) noticeView.findViewById(R.id.manage_txt_user);
        TextView txtMoney = (TextView) noticeView.findViewById(R.id.manage_txt_money);

        //空数据直接结束
        if (datas != null && datas.size() == 0) {
            return;
        }

        //防止下标越界
        if (next > (datas.size() - 1)) {
            next = 0;
        }
        try {
            Acount acount = datas.get(next);
            txtTime.setText(acount.getTime());
            txtUser.setText("用户" + acount.getUser() + "投资了");
            txtMoney.setText("¥" + acount.getMoney());

            noticeView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017/5/18 0018 监听
                    if (tipsClickListener != null) {
                        tipsClickListener.onTipsClick(mCurrPos);
                    }
                }
            });

            if (getChildCount() > 1) {
                removeViewAt(0);
            }
            addView(noticeView, getChildCount());
            mCurrPos = next;
        } catch (Exception e) {

        }
    }

    /**
     * 接口   监听回调
     */
    public interface OnTipsClickListener {

        void onTipsClick(int position);
    }

    private OnTipsClickListener tipsClickListener;

    public void setOnTipsClickListener(OnTipsClickListener tipsClickListener) {
        this.tipsClickListener = tipsClickListener;
    }

    public void setDatas(List<Acount> datas) {
        this.datas = datas;
    }
}
