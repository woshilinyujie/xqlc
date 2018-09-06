package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rongxun.xqlc.R;


/**
 * Created by jcb on 2017/6/9.
 */

public class CashDialog extends Dialog {
    private Context context;
    private TextView cancle;
    private TextView forget;
    private EditText passwordE;
    private TextView prompt;
    private TextView sure;
    private View view;
    private int with;
    private TextView arrive_money;
    private TextView cash_money;
    private TextView commission;
    private LinearLayout ll;
    private TextView text;

    public CashDialog(@NonNull Context context) {
        super(context);

    }

    public CashDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initDate();
    }


    private void initView() {
        view = View.inflate(context, R.layout.cash_dialog, null);
        cancle = (TextView) view.findViewById(R.id.cash_dialog_cancle);
        forget = (TextView) view.findViewById(R.id.cash_dialog_forget);
        passwordE = (EditText) view.findViewById(R.id.cash_dialog_password);
        prompt = (TextView) view.findViewById(R.id.cash_dialog_prompt);
        sure = (TextView) view.findViewById(R.id.cash_dialog_sure);
        //文字text
        text = (TextView) view.findViewById(R.id.cash_dialog_text);
        //显示框
        ll = (LinearLayout) view.findViewById(R.id.cash_dialog_ll);
        //到账金额
        arrive_money = (TextView) view.findViewById(R.id.cash_dialog_arrive_money);
        //提现金额
        cash_money = (TextView) view.findViewById(R.id.cash_dialog_cash_money);
        //手续费
        commission = (TextView) view.findViewById(R.id.cash_dialog_commission);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void initDate() {
        setContentView(view);
        //直接弹出软键盘
        setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(passwordE, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        show();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        with = defaultDisplay.getWidth();
        view.setMinimumWidth(with - with / 4);

    }

    public EditText getPasswordE() {
        return passwordE;
    }

    public TextView getTextView() {
        return sure;
    }

    public TextView getForget() {
        return forget;
    }

    public TextView getPrompt() {
        return prompt;
    }

    public TextView getCancle() {
        return cancle;
    }
    public void setTextVisibility(String cashMoney,int commissionInt){
        ll.setVisibility(View.VISIBLE);
        text.setVisibility(View.GONE);
        arrive_money.setText(Float.parseFloat(cashMoney)-commissionInt+"");
        cash_money.setText(cashMoney);
        commission.setText(commissionInt+"");
    }

}
