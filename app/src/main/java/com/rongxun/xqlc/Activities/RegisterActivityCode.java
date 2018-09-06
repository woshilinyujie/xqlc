package com.rongxun.xqlc.Activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.login.LoginBean;
import com.rongxun.xqlc.Beans.reguser.RegUserBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.MessageDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.AppVersionUtils;
import com.rongxun.xqlc.Util.ClickEvent;
import com.rongxun.xqlc.Util.PrefUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.Utils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.builder.PostFormBuilder;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.FormBody;


public class RegisterActivityCode extends MyBaseActivity implements View.OnClickListener {


    private TextView idT;
    private Button register;
    private Button code_button;
    private EditText codeE;
//    private CheckBox check;
    private String id;
    private String sessionid;
    private String TAG = "注册";
    private CountDownTimer countDownTimer;
    private TextView negotiate;
    private IconFontTextView back;
    private String from;
    private TextView book;
    private BroadcastReceiver broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);
        initViews();
        initListener();
    }

    private void initViews() {
        IntentFilter intentFilter = new IntentFilter("LoginActivityBroadcast");
        broadcast = new LoginActivityBroadcast();
        registerReceiver(broadcast, intentFilter);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        sessionid = intent.getStringExtra("sessionid");
        id = intent.getStringExtra("phone_number");
        idT = (TextView) findViewById(R.id.register_id);
        negotiate = (TextView) findViewById(R.id.regiest_negotiate);
        register = (Button) findViewById(R.id.register);
        code_button = (Button) findViewById(R.id.register_code_button);
        codeE = (EditText) findViewById(R.id.register_code);
        back = (IconFontTextView) findViewById(R.id.register_back);
        book = (TextView) findViewById(R.id.regiest_book);
        idT.setText(Html.fromHtml("注册验证码将送至您的手机 " + "<font color='#ef3e44'>" + id + "</font>" +
                "发送短信"));

        //倒计时
        countDownTimer = new CountDownTimer(1000 * AppConstants.VerifyCodeTimeFuture, 1000 * AppConstants.VerifyCodeTimeInteral) {

            @Override
            public void onTick(long millisUntilFinished) {
                code_button.setEnabled(false);
                code_button.setText((millisUntilFinished / 1000) + "秒");
                code_button.setTextColor(Color.parseColor("#999999"));
            }

            @Override
            public void onFinish() {
                code_button.setEnabled(true);
                code_button.setText("重新发送");
                code_button.setTextColor(Color.parseColor("#3574fa"));
            }
        };
        countDownTimer.start();
    }

    private void initListener() {
        code_button.setOnClickListener(this);
        negotiate.setOnClickListener(this);
        book.setOnClickListener(this);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
        register.setEnabled(false);


        codeE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ( !TextUtils.isEmpty(codeE.getText().toString() )|| codeE.getText().toString().length()<6) {
                    register.setBackgroundResource(R.drawable.button_background_normal);
                    register.setEnabled(false);
                } else if(!TextUtils.isEmpty(codeE.getText().toString() )|| codeE.getText().toString().length()>=6){
                    register.setBackgroundResource(R.drawable.button_background_grey);
                    register.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_code_button:
                if (!ClickEvent.isFastDoubleClick()) {
                    String basicUrl = AppConstants.URL_SUFFIX + "/rest/sendPCode";
                    RequestForVerifyCode(basicUrl, id);
                }
                break;
            case R.id.regiest_negotiate:
//                startActivity(new Intent(this, RegisterNegotiateActivity.class));
                //跳转到详情页
                Intent intentNegotiate = new Intent(RegisterActivityCode.this, ArticleActivity.class);
                intentNegotiate.putExtra("id", "2");
                intentNegotiate.putExtra("header", "服务协议");
                startActivity(intentNegotiate);
                break;
            case R.id.regiest_book:
//                startActivity(new Intent(this, RegisterNegotiateActivity.class));
                //跳转到详情页
                Intent intentBook = new Intent(RegisterActivityCode.this, ArticleActivity.class);
                intentBook.putExtra("id", "875");
                intentBook.putExtra("header", "风险提示书");
                startActivity(intentBook);
                break;


            case R.id.register:
                if(code_button.getText().toString().length()==6){
                    Intent intent =new Intent(this,RegisterActivityCode.class);
                    intent.putExtra("from",from);
                    intent.putExtra("sessionid",sessionid);
                    intent.putExtra("phone_number",id);
                    intent.putExtra("code",code_button.getText().toString());
                }

                break;

            case R.id.register_back:
                finish();
                break;
        }
    }


    /**
     * 请求获取验证码
     *
     * @param basicUrl
     */
    public void RequestForVerifyCode(String basicUrl, String phoneNoString) {

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
            public void onFailure(okhttp3.Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String cookieval = response.header("Set-Cookie");
                if (cookieval != null) {
                    sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                    String s = response.body().string();
                    Log.i(TAG, s);
                    final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resultBack.getRcd().equals("R0001")) {
                                //成功
                                countDownTimer.start();//开始倒计时

                            } else {
                                Toast.makeText(RegisterActivityCode.this, "" + resultBack.getRmg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


    }






    /**
     * 判断字符串是否是6位之间
     *
     * @return
     */
    public static boolean isStringLengthIngnel(String str) {
        if (str.length() > 6) {
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
