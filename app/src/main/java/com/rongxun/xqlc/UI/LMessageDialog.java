package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.rongxun.xqlc.R;

/**
 * Created by linyujie on 16/7/18.
 */
public class LMessageDialog extends Dialog {
    private Context context;
    private TextView message;
    private TextView title;
    private TextView head;

    public LMessageDialog(Context context) {
        super(context, 0);
    }

    public LMessageDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initData();
    }



    private void initView() {
        View view = View.inflate(context, R.layout.lmessage_dialog, null);
        title = (TextView) view.findViewById(R.id.imessage_title);
        message = (TextView) view.findViewById(R.id.imessage_message);
        head = (TextView) view.findViewById(R.id.imessage_head);
        setCancelable(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        setContentView(view);

    }

    private void initData() {

    }


    public void setHead(String s){
        head.setText(s);
    }
    public TextView getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        title.setText(msg);
    }

    public void setMtitle(String mtitle) {
        message.setText(mtitle);
    }
}
