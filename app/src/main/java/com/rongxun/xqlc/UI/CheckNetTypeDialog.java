package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rongxun.xqlc.R;


/**
 * PROJECT_NAME: Sample
 * PACKAGE_NAME: com.guaika.sample.utils
 * Author: HouShengLi
 * Time: 2017/05/24 17:55
 * E-mail: 13967189624@163.com
 * Description:
 */

public class CheckNetTypeDialog extends Dialog implements View.OnClickListener {

    private LayoutInflater inflater;
    private TextView txtNetType;//网络类型

    public CheckNetTypeDialog(Context context) {
        this(context, R.style.UpdateDialog);
    }

    public CheckNetTypeDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        // TODO: 2017/5/12 0012
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {

        View view = inflater.inflate(R.layout.check_net_type_dialog, null);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        txtNetType = (TextView) view.findViewById(R.id.txt_net_type);
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
                if (netTypeOnClick != null) {
                    netTypeOnClick.onClick();
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
    public interface NetTypeOnClick {

        void onClick();
    }

    private NetTypeOnClick netTypeOnClick;

    public void setNetTypeOnClick(NetTypeOnClick netTypeOnClick) {
        this.netTypeOnClick = netTypeOnClick;
    }

    /**
     * 设置网络类型
     * @param netType
     */
    public void setNetType(String netType) {
        txtNetType.setText(netType);
    }

}
