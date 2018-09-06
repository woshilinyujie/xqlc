package com.rongxun.xqlc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.login.LoginBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PrefUtils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.net.URLEncoder;

public class LoginPasswordActivity extends MyBaseActivity implements View.OnClickListener {

    private EditText password;
    private IconFontTextView back;
    private Button next;
    private ImageView open;
    private String number;
    private boolean isClose = true;
    private TextView forget;
    private LoadingDialog loaginDialog;
    private String TAG = "登录";
    private SharedPreferences preferences;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);
        initView();
        initListener();
    }

    private void initListener() {
        password.setOnClickListener(this);
        back.setOnClickListener(this);
//        close.setOnClickListener(this);
        next.setOnClickListener(this);
        open.setOnClickListener(this);
        forget.setOnClickListener(this);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 7) {
                    next.setEnabled(true);
                    next.setBackgroundResource(R.drawable.button_background_normal);
                } else {
                    next.setEnabled(false);
                    next.setBackgroundResource(R.drawable.button_background_grey);
                }
            }
        });
    }


    private void initView() {
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        number = intent.getStringExtra("phone_number");
        password = (EditText) findViewById(R.id.login_password);
        back = (IconFontTextView) findViewById(R.id.login_password_back);
        next = (Button) findViewById(R.id.login_password_next);
        open = (ImageView) findViewById(R.id.login_password_open);
        forget = (TextView) findViewById(R.id.login_password_forget);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_password_back:
                finish();
                break;
            case R.id.login_password_forget:
                Intent intent = new Intent();
                intent.putExtra("phone",number);
                intent.setClass(this, ForgetLoginPassActivity.class);
                startActivity(intent);
                break;
            case R.id.login_password_next:
                String basicUrl = AppConstants.URL_SUFFIX + "/rest/login";

                String passwordS = password.getText().toString();

                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(password.getWindowToken(), 0);


                if (loaginDialog == null) {

                    loaginDialog = new LoadingDialog(this);
                    loaginDialog.showLoading();
                }
                RequestForLogin(basicUrl);
                break;
            case R.id.login_password_open:
                if (isClose) {
                    isClose = false;
                    open.setBackgroundResource(R.mipmap.login_open);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                } else {
                    isClose = true;
                    open.setBackgroundResource(R.mipmap.login_close);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                }
                break;
        }
    }


    public void RequestForLogin(String basicUrl) {
        String passwordS = password.getText().toString();
        String sPassword = URLEncoder.encode(passwordS);
        preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String deviceToken = preferences.getString("deviceToken", "");
        String url = basicUrl + "?" + "username=" + number + "&password=" + sPassword + "&deviceToken=" + deviceToken;
        Log.i(TAG, "" + url);


        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Toast.makeText(LoginPasswordActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        if (loaginDialog != null && loaginDialog.isShowing()) {
                            loaginDialog.dismissLoading();
                            loaginDialog = null;
                        }
                        if (e == null || e.getMessage() == null) {
                        } else {
                            if (e != null && e.getMessage() != null) {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        final LoginBean resultBack = new Gson().fromJson(s, LoginBean.class);
                        if (loaginDialog != null && loaginDialog.isShowing()) {
                            loaginDialog.dismissLoading();
                            loaginDialog = null;
                        }
                        if (resultBack.getRcd().equals("R0001")) {

                            try {
                                if (resultBack.getPayPasswordStatus().equals("0")) {
                                    PrefUtils.putBoolean(LoginPasswordActivity.this, "SafePassWord", false);
                                } else {
                                    PrefUtils.putBoolean(LoginPasswordActivity.this, "SafePassWord", true);
                                }
                            } catch (Exception e) {

                            }

                            SharedPreferences.Editor editor = preferences.edit();
                            //存入数据
                            editor.putString("loginToken", resultBack.getToken());
                            //提交修改
                            editor.commit();
                            LoginPasswordActivity.this.getSharedPreferences("UserId", Context.MODE_PRIVATE).
                                    edit().putString("UserId", number)
                                    .commit();
                            //发送广播到H5js界面，调用js方法，传Token
                            Intent h5 = new Intent();
                            h5.setAction("intent.action.h5js.refresh.token");
                            h5.putExtra("h5_js", 111);
                            sendBroadcast(h5);
                            //刷新首页标的数据、理财页数据
                            Intent mark = new Intent();
                            mark.setAction("intent.action.home.data.refresh");
                            mark.putExtra("home", 110);
                            sendBroadcast(mark);
                            //刷新 我的页面
                            Intent mine = new Intent();
                            mine.setAction("LoginContentBroadCast");
                            mine.putExtra("Quickly", "Quickly");
                            sendBroadcast(mine);
                            //结束输入账号的界面
                            Intent login = new Intent();
                            login.setAction("LoginActivityBroadcast");
                            login.putExtra("finish", "finish");
                            sendBroadcast(login);

                            if (from != null && from.equals("gesture")) {
                                //清除手势密码
                                LoginPasswordActivity.this.getSharedPreferences(number, Context.MODE_PRIVATE).edit()
                                        .clear().commit();
                                Intent intentMain = new Intent(LoginPasswordActivity.this, MainActivity.class);
                                CustomApplication.clearActivity();
                                LoginPasswordActivity.this.startActivity(intentMain);
                                Intent intent2 = new Intent("GesturebroadCast");
                                GestureCodeActivity.from = null;
                                intent2.putExtra("value", "finish");
                                LoginPasswordActivity.this.sendBroadcast(intent2);
                            }
                            //判断手势密码是否设置  没设置挑设置页面
                            String code = LoginPasswordActivity.this.getSharedPreferences(number, Context.MODE_PRIVATE).getString(number, null);
                            if(code==null){
                                Intent intent=new Intent(LoginPasswordActivity.this,GestureCodeActivity.class);
                                intent.putExtra("from","RegisterSuccess");
                                LoginPasswordActivity.this.startActivity(intent);
                            }

                            LoginPasswordActivity.this.finish();

                        } else if (resultBack.getRcd().equals("M0001")) {
                            //用户名不存在或密码错误
                            Toast.makeText(LoginPasswordActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();

                        } else if (resultBack.getRcd().equals("M0002")) {
                            //您的账号已被禁用,无法登录，如有疑问请联系客服人员
                            Toast.makeText(LoginPasswordActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();
                        } else if (resultBack.getRcd().equals("M0003")) {
                            //您的账号已被锁定，如有疑问请联系客服人员

                            Toast.makeText(LoginPasswordActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();


                        } else if (resultBack.getRcd().equals("M0004")) {
                            //若连续N次密码输入错误,您的账号将被锁定!

                            Toast.makeText(LoginPasswordActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
