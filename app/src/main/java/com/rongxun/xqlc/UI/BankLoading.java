package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rongxun.xqlc.R;


/**
 * Created by jcb on 2017/5/15.
 */

public class BankLoading extends Dialog{

    private ImageView iv;
    private Context context;
    private AnimationDrawable animation;
    private ImageView mLoadingImageView;
    private AnimationDrawable mLoadingAnimationDrawable;

    public BankLoading(@NonNull Context context) {
        super(context);
    }

    public BankLoading(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context=context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View loadingView= LayoutInflater.from(getContext()).inflate(R.layout.bank_loading, null);
        mLoadingImageView =(ImageView) loadingView.findViewById(R.id.bank_loading_iv);
//        mLoadingImageView.setImageResource(R.drawable.bank_loading);
        setContentView(loadingView);
        Glide.with(context).load(R.drawable.bankgif).into(mLoadingImageView);
    }

    @Override
    public void show() {
        super.show();
        //注意将动画的启动放置在Handler中.否则只可看到第一张图片
//        new Handler(){}.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mLoadingAnimationDrawable =(AnimationDrawable) mLoadingImageView.getDrawable();
//                mLoadingAnimationDrawable.start();
//            }
//        }, 10);

    }
    @Override
    public void dismiss() {
        super.dismiss();
        //结束帧动画
//        mLoadingAnimationDrawable =(AnimationDrawable) mLoadingImageView.getDrawable();
//        mLoadingAnimationDrawable.stop();
    }

}
