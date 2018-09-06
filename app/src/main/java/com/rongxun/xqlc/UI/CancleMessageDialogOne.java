package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.rongxun.xqlc.R;


/**
 * Created by linyujie on 16/7/18.
 */
public class CancleMessageDialogOne extends Dialog {
    private Context context;
    private Button message;
    private TextView title;
    private Button cancle;

    public CancleMessageDialogOne(Context context) {
        super(context, 0);
    }

    public CancleMessageDialogOne(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
    }


    private void initView() {
        View view = View.inflate(context, R.layout.canclemessage_dialog, null);
        title = (TextView) view.findViewById(R.id.imessage_title);
        message = (Button) view.findViewById(R.id.imessage_message);
        cancle = (Button) view.findViewById(R.id.imessage_message1);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        setContentView(view);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    public TextView getMessage() {
        return message;
    }

    public void setTitle(String titles) {
        title.setText(titles);
    }

    public void setMessage(String messages) {
        message.setText(messages);
    }

    public void setTitleSize(int size) {
        title.setTextSize(size);
    }

    public void setMessageSize(int size) {
        message.setTextSize(size);
    }

    public void setTitleColour(String colour) {
        title.setTextColor(Color.parseColor(colour));
    }

    public void setMessageColour(String colour) {
        message.setTextColor(Color.parseColor(colour));
    }

    public void setCancleColour(String colour) {
        cancle.setTextColor(Color.parseColor(colour));
    }

    public void setCancleText(String s) {
        cancle.setText(s);
    }

}