package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.rongxun.xqlc.R;


/**
 * Created by Administrator on 2015/11/19.
 */
public class LoadingDialog extends Dialog {

    private LayoutInflater inflater;
    private ImageView img;
    private AnimationDrawable animationDrawable;

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.Loading_Dialog);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        inflater = LayoutInflater.from(context);
        init(context);
    }

    private void init(Context context) {

        View view = inflater.inflate(R.layout.loading_layout, null);
        img = (ImageView) view.findViewById(R.id.loading_dialog_image);
        animationDrawable = (AnimationDrawable) img.getBackground();
        setCanceledOnTouchOutside(false);

        setContentView(view);
    }

    /**
     * 显示加载
     */
    public void showLoading() {

        animationDrawable.start();
        show();
    }

    /**
     * 隐藏加载
     */
    public void dismissLoading() {
        animationDrawable.stop();
        dismiss();
    }



}
