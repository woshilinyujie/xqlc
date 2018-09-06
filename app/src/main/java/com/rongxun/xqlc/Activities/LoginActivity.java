package com.rongxun.xqlc.Activities;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.UrlBean;
import com.rongxun.xqlc.Beans.reguser.RegUserBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.Utils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.FormBody;

public class LoginActivity extends MyBaseActivity {

    private final String TAG = "用户登录";
    //    private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    private ImageView back;
    private Intent intent;
    private String from;
    private EditText phone;
    private boolean isAnimator = true;
    private ObjectAnimator animator;
    private StringBuilder resultText;
    public static final String LOGIN_BG_URL = AppConstants.URL_SUFFIX +
            "/rest/indexActivityVT?type=8";//登录背景图url
    private LoadingDialog loaginDialog;
    private LoginActivityBroadcast broadcast;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_login);
        IntentFilter intentFilter = new IntentFilter("LoginActivityBroadcast");
        broadcast = new LoginActivityBroadcast();
        registerReceiver(broadcast, intentFilter);
        intent = getIntent();
        from = intent.getStringExtra("from");
        back = (ImageView) findViewById(R.id.login_regist_back);
        final Button next = (Button) findViewById(R.id.login_regist_next);
        phone = (EditText) findViewById(R.id.login_regist_phone);
        imageView = (ImageView) findViewById(R.id.login_imageView);
        //设置登录页背景图
        setLoginBackground();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String basicUrl = AppConstants.URL_SUFFIX + "/rest/sendPCode";
                String phoneNumberString = phone.getText().toString();
                try {
                    RequestForVerifyCode(basicUrl, phoneNumberString);
                } catch (Exception e) {

                }
            }
        });


    }


    public void RequestForVerifyCode(String basicUrl, final String phoneNoString) {
        loaginDialog = new LoadingDialog(this);
        loaginDialog.showLoading();

        okhttp3.OkHttpClient mOkHttpClient = new okhttp3.OkHttpClient();
        okhttp3.RequestBody formBody = new FormBody.Builder()
                .add("phoneReg", phoneNoString)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(basicUrl)
                .post(formBody)
                .build();
        okhttp3.Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (e != null && e.getMessage() != null) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, s);
                if (loaginDialog != null && loaginDialog.isShowing()) {
                    loaginDialog.dismissLoading();
                    loaginDialog = null;
                }
                final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String cookieval = response.header("Set-Cookie");
                        if (resultBack.getRcd().equals("M0008_7")) {
                            //进入登入密码页面
                            Intent intent = new Intent(LoginActivity.this, LoginPasswordActivity.class);
                            intent.putExtra("phone_number", phoneNoString);
                            intent.putExtra("from", from);
                            startActivityForResult(intent, 200);
                        } else if (resultBack.getRcd().equals("R0001")) {
                            //进入注册页面
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("phone_number", phoneNoString);
                            intent.putExtra("from", from);
                            intent.putExtra("sessionid", cookieval.substring(0, cookieval.indexOf(";")));
                            startActivityForResult(intent, 200);
                        } else {
                            Toast.makeText(LoginActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


    //注册图片
    private void setLoginBackground() {

        OkHttpUtils.post()
                .url(LOGIN_BG_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String s, int id) {
                        UrlBean bean = new Gson().fromJson(s, UrlBean.class);
                        try {
                            Glide.with(LoginActivity.this)
                                    .load(bean.getHomeActivityList().get(0).getImageUrl())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .skipMemoryCache(false)
                                    .into(imageView);
                        } catch (Exception e) {
                            Log.e("注册图片", e.toString());
                        }

                    }
                });

    }


//    public static boolean isMobile(String mobile) {
//        return Pattern.matches(REGEX_MOBILE, mobile);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            finish();
        } else if (resultCode == 10086) {
            setResult(10086, data);
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcast);
        super.onDestroy();
        CustomApplication.removeActivity(this);

    }

    class LoginActivityBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("finish").equals("finish")) {
                finish();
            }
        }
    }
}
