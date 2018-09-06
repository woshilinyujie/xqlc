package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rongxun.xqlc.R;


/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.view
 * Author: HouShengLi
 * Time: 2017/06/15 14:43
 * E-mail: 13967189624@163.com
 * Description:
 */

public class TipsDialog extends Dialog implements View.OnClickListener {

    private LayoutInflater inflater;
    private TextView txtTitle;
    private TextView txtTip;

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public TextView getTxtTip() {
        return txtTip;
    }

    public TipsDialog(@NonNull Context context) {
        this(context, R.style.TipDialog);
    }

    public TipsDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        inflater = LayoutInflater.from(context);
        init();
    }


    private void init() {

        View view = inflater.inflate(R.layout.tips_dialog, null);
        txtTitle = (TextView) view.findViewById(R.id.tip_title);
        txtTip = (TextView) view.findViewById(R.id.tip_message);
        Button btnCancel = (Button) view.findViewById(R.id.tips_btn_cancel);
        btnCancel.setOnClickListener(this);
        setContentView(view);


    }

    public void showDialog() {
        //
        show();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tips_btn_cancel:
                dismiss();
                break;
        }
    }
}
