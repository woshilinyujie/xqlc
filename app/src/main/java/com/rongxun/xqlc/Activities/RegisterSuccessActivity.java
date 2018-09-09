package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.LoginBgBean;
import com.rongxun.xqlc.Beans.UrlBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

public class RegisterSuccessActivity extends AppCompatActivity {

    private ImageView success_iv;
    private Button button;
    private Gson gson = new Gson();

    public static final String REGISTER_BG_URL = AppConstants.URL_SUFFIX +
            "/rest/indexActivityVT?type=8";//注册成功背景图url
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_login_success);
        initView();
        setRegisterBackground();
    }

    //注册图片
    private void setRegisterBackground() {

        OkHttpUtils.post()
                .url(REGISTER_BG_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String s, int id) {
                        UrlBean bean=new Gson().fromJson(s,UrlBean.class);
                        try {
                            Glide.with(RegisterSuccessActivity.this).load(bean.getHomeActivityList().get(1).getImageUrl())
                                    .into(success_iv);
                        }catch (Exception e){
                            Log.e("注册图片",e.toString());
                        }

                    }
                });

    }


    private void initView() {
        success_iv = (ImageView) findViewById(R.id.login_success_iv);
        button = (Button) findViewById(R.id.login_success_button);

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (getWindowManager().getDefaultDisplay().getHeight()/2.5));
        success_iv.setLayoutParams(params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
