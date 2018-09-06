package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StyleRes;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rongxun.xqlc.R;


/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.view
 * Author: HouShengLi
 * Time: 2017/06/05 17:34
 * E-mail: 13967189624@163.com
 * Description:
 */

public class UpdateWaitingDialog extends Dialog {

    private LayoutInflater inflater;
    private TextView txtTips;
    private ProgressWheel bar;

    public UpdateWaitingDialog(Context context) {
        this(context, R.style.UpdateDialog);
    }

    public UpdateWaitingDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        // TODO: 2017/5/12 0012
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {

        View view = inflater.inflate(R.layout.update_waiting_dialog, null);
        txtTips = (TextView) view.findViewById(R.id.tips);
        bar = (ProgressWheel) view.findViewById(R.id.update_progress_bar);
        setContentView(view);
        //返回键不能取消dialog
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        //触点在dialog外围不会消失
        setCancelable(false);


    }

    private void showDialog() {

        //
        show();
    }

    public TextView getTxtTips() {
        return txtTips;
    }

    public void setTxtTips(TextView txtTips) {
        this.txtTips = txtTips;
    }

    public ProgressWheel getBar() {
        return bar;
    }

    public void setBar(ProgressWheel bar) {
        this.bar = bar;
    }
}
