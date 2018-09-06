package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.rongxun.xqlc.R;


/**
 * PROJECT_NAME: Sample
 * PACKAGE_NAME: com.guaika.sample.utils
 * Author: HouShengLi
 * Time: 2017/05/24 17:08
 * E-mail: 13967189624@163.com
 * Description:
 */

public class UpdateDialog extends Dialog implements View.OnClickListener {

    private LayoutInflater inflater;

    public UpdateDialog(Context context) {
        this(context, R.style.UpdateDialog);
    }

    public UpdateDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        // TODO: 2017/5/12 0012
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {

        View view = inflater.inflate(R.layout.update_dialog, null);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        setContentView(view);
        //
        show();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_ok:
                if (updateOnClick != null) {
                    updateOnClick.onClick();
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    /**
     * 回调接口
     */
    public interface UpdateOnClick {

        void onClick();
    }

    private UpdateOnClick updateOnClick;

    public void setUpdateOnClick(UpdateOnClick updateOnClick) {
        this.updateOnClick = updateOnClick;
    }
}
