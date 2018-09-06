package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StyleRes;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rongxun.xqlc.R;


/**
 * Author: HouShengLi
 * Time: 2017/05/24 18:18
 * E-mail: 13967189624@163.com
 * Description:
 */

public class MainUpdateDialog extends Dialog implements View.OnClickListener {

    private LayoutInflater inflater;
    private TextView txtContent;
    private Button btnCancel;

    public MainUpdateDialog(Context context) {
        this(context, R.style.UpdateDialog);
    }

    public MainUpdateDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        // TODO: 2017/5/12 0012
        inflater = LayoutInflater.from(context);
        init();
    }


    private void init() {

        View view = inflater.inflate(R.layout.main_update_dialog, null);
        Button btnOk = (Button) view.findViewById(R.id.btn_update_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_update_cancel);
        txtContent = (TextView) view.findViewById(R.id.txt_update_content);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        setContentView(view);
        //
        show();
        //强制更新，dialog低级返回键不会消失
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
        setCancelable(false);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_update_ok:
                if (mainUpdateOnClick != null) {
                    mainUpdateOnClick.onOkClick();
                }
                break;
            case R.id.btn_update_cancel:
                if (mainUpdateOnClick != null) {
                    mainUpdateOnClick.onCancelClick();
                }
                dismiss();
                break;
        }
    }

    /**
     * 回调接口
     */
    public interface MainUpdateOnClick {

        void onOkClick();

        void onCancelClick();
    }

    private MainUpdateOnClick mainUpdateOnClick;

    public void setMainUpdateOnClick(MainUpdateOnClick mainUpdateOnClick) {
        this.mainUpdateOnClick = mainUpdateOnClick;
    }

    /**
     * 设置更新的内容
     *
     * @param msg
     */
    public void setContent(String msg) {
        txtContent.setText(msg);
    }

    public Button getBtnCancel() {
        return btnCancel;
    }
}
