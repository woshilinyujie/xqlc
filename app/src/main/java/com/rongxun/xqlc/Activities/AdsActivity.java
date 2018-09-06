package com.rongxun.xqlc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PrefUtils;
import com.rongxun.xqlc.Util.PreventFastClickUtils;

import java.util.Timer;
import java.util.TimerTask;

public class AdsActivity extends AppCompatActivity {

    private ImageView imgBg;//广告
    private TextView txtTimer;//读秒倒计时
    private LinearLayout lineSkip;//跳过
    public static final int TIMER = 0;
    private Timer timer;//计时器
    private int counter = 3;
    private String phone;
    private int flag;//标识 1：详情页  2：主页

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == TIMER) {
                if (counter >= 1) {
                    txtTimer.setText(counter + "");
                    counter--;
                } else {
                    //读秒完毕
                    //跳转到主页
                    flag = 2;
                    startSkip(MainActivity.class);
                }
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        // 不显示系统的标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ads);

        initView();
        initData();
        listener();
    }

    private void listener() {

        lineSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止快速点击
                if (!PreventFastClickUtils.isFastClick()) {
                    //跳转到主页
                    flag = 2;
                    startSkip(MainActivity.class);
                }
            }
        });

        imgBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PreventFastClickUtils.isFastClick()) {
                    //跳转到广告详情
                    flag = 1;
                    startSkip(H5JSActivity.class);
                }

            }
        });
    }

    private void initData() {

        //获取图片
        String second = PrefUtils.getString(AdsActivity.this, "second", "");
        Glide.with(this)
                .load( second)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgBg);

    }

    private void initView() {
        phone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        imgBg = (ImageView) findViewById(R.id.welcome_img);
        txtTimer = (TextView) findViewById(R.id.welcome_txt_timer);
        lineSkip = (LinearLayout) findViewById(R.id.welcome_rel_skip);

        //开始倒计时
        startTime();
    }

    private void startTime() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //每隔一秒发一条空消息
                mHandler.sendEmptyMessage(TIMER);
            }
        }, 0, 1000);

    }

    /**
     * 判断是否登录
     *
     * @return
     */
    private boolean isLogin() {

        SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");
        if (TextUtils.isEmpty(token)) {
            return false;
        }

        return true;
    }

    /**
     * 是否开启手势密码
     *
     * @return
     */
    private boolean isGestureCode() {

        String code = getSharedPreferences(phone, Context.MODE_PRIVATE).
                getString(phone, null);
        String userId = getSharedPreferences("UserId", Context.MODE_PRIVATE).
                getString("UserId", null);

        if (code != null && userId != null) {
            return true;
        }
        return false;
    }

    /**
     * 开启界面
     */
    private void startSkip(Class cls) {

        //登录且开启了手势密码----》进入手势密码验证界面
        if (isLogin() && isGestureCode()) {

//            BaseActivity.isActive = true;
            Intent intent = new Intent();
            //跳转广告详情页参数
            if (flag == 1) {
                intent.putExtra("web_url", PrefUtils.getString(AdsActivity.this, "second_detail", ""));
                intent.putExtra("flag", "ads");
                intent.putExtra("from", "ads");
            } else if (flag == 2) {
                intent.putExtra("from", "MainActivity");
            }
            intent.setClass(this, GestureCodeActivity.class);
            //取消定时器
            timer.cancel();
            startActivity(intent);
            finish();

        } else {
            //正常跳转
            goActivity(cls);
        }

    }

    /**
     * 跳转
     *
     * @param cls
     */

    private void goActivity(Class cls) {

        Intent intent = new Intent(this, cls);
        if (flag == 1) {
            //进入详情页
            String second_detail = PrefUtils.getString(AdsActivity.this, "second_detail", "");
            intent.putExtra("flag", "ads");
            intent.putExtra("web_url", PrefUtils.getString(AdsActivity.this, "second_detail", ""));
        }
        //取消定时器
        timer.cancel();
        startActivity(intent);
        finish();
    }

}
