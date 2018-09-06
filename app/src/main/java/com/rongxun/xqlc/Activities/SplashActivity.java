package com.rongxun.xqlc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppVersionUtils;
import com.rongxun.xqlc.Util.PrefUtils;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    public final int MSG_FINISH_GUIDE_ACTIVITY = 500;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISH_GUIDE_ACTIVITY:
                    if (isGuide()) {
                        //GuideActivity，并结束当前的SplashActivity
                        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } else {
                        //判断是不是进入广告页
                        String second = PrefUtils.getString(SplashActivity.this, "second", "");
                        if (second.equals("")) {
                            //判断是否有手势密码
                            if (isLogin() && isGestureCode()) {
                                Intent intent = new Intent();
                                intent.putExtra("from", "welcome");
                                intent.setClass(SplashActivity.this, GestureCodeActivity.class);
                                startActivity(intent);
                            }else{
                                //直接进入首页
                                Intent main = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(main);
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            }
                        } else {
                            //进入广告页
                            Intent intent = new Intent(SplashActivity.this, AdsActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                    }
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 不显示系统的标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        // 停留3秒后发送消息，跳转到MainActivity
        mHandler.sendEmptyMessageDelayed(MSG_FINISH_GUIDE_ACTIVITY, 2000);

    }

    /**
     * 是否进入引导页
     *
     * @return
     */
    private boolean isGuide() {

        //获取当前应用的版本号
        int currentVersionCode = AppVersionUtils.getVersionCode(this);
        int lastVersionCode = PrefUtils.getInt(this, "versionCode", 1);

        if (currentVersionCode == 1) {
            //初始版本
            return PrefUtils.getBoolean(this, "isFirst", true);
        } else if (currentVersionCode > lastVersionCode) {
            //app更新
            PrefUtils.putInt(this, "versionCode", currentVersionCode);
            return true;
        } else {
            //重复安装
            return PrefUtils.getBoolean(this, "isFirst", true);
        }

    }

    public void onResume() {
        super.onResume();
        //友盟api
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
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

        String phone = getSharedPreferences("UserId", Context.MODE_PRIVATE).
                getString("UserId", null);
        String code = getSharedPreferences(phone, Context.MODE_PRIVATE).
                getString(phone, null);

        if (code != null && phone != null) {
            return true;
        }
        return false;
    }

}
