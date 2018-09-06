package com.rongxun.xqlc.Util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rongxun.xqlc.Activities.CustomerActivity;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.SelfDialog;


/**
 * Created by jcb on 2017/6/9.
 */

public class ServiscUtil {

    PopupWindow popupWindow;
    private Activity context;
    private View view;
    private SelfDialog selfDialog;
    private TextView phone_num;
    private TextView title;

    public ServiscUtil(Activity context, View view) {
        this.context = context;
        this.view = view;
    }


    public void initPopuptWindowExit() {
        if (popupWindow == null) {
            // 获取自定义布局文件activity_popupwindow_left.xml的视图
            View popupWindow_view = context.getLayoutInflater().inflate(R.layout.exit_popup_confirm_layout, null,
                    false);
            final Button cancle = (Button) popupWindow_view.findViewById(R.id.popu_service_cancle);
            Button online = (Button) popupWindow_view.findViewById(R.id.popu_service_online);
            Button phone = (Button) popupWindow_view.findViewById(R.id.popu_service_phone);

            online.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                                popupWindow = null;
                            }
                            //转入客服服务页面
                            context.startActivity(new Intent(context,CustomerActivity.class));
                        }
                    }
            );


            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    //电话窗口
                    showNormalDialog();
                }
            });
            cancle.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                                popupWindow = null;
                            }
                        }
                    }

            );

            // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
            popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // 设置动画效果
            popupWindow.setAnimationStyle(R.style.AnimationFade);
            popupWindow.setOnDismissListener(
                    new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            SetBackgroundAlpha(1f);
                        }
                    }

            );
            popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            SetBackgroundAlpha(0.5f);
        }
    }

    public void SetBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }


    private void showNormalDialog() {
        selfDialog = new SelfDialog(context);
        phone_num = (TextView) selfDialog.findViewById(R.id.message);
        title = (TextView) selfDialog.findViewById(R.id.title);
        selfDialog.setMessage("400-782-0088");
        selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-782-0088"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                selfDialog.dismiss();

            }
        });
        selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                selfDialog.dismiss();
            }
        });
        selfDialog.show();

    }
}
