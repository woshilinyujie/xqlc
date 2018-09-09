package com.rongxun.xqlc.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.BaseBean;
import com.rongxun.xqlc.Beans.reguser.RegUserBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LMessageDialog;
import com.rongxun.xqlc.UI.MessageDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

import okhttp3.FormBody;

public class ForgetSafePassActivity extends MyBaseActivity {


    IconFontTextView forgetSafePassToolbarBack;
    TextView forgetSafePassToolbarTitle;
    Toolbar forgetSafePassToolbar;
    TextView forgetSafePassVerifyButton;
    EditText forgetSafePassVerifyCode;
    EditText forgetSafePassNewPass;
    Button forgetSafePassActionButton;
    private String TAG = "忘记交易密码";
    private String currentPhoneNo;//当前手机号
    private CountDownTimer countDownTimer;
    private String sessionid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_forget_safe_pass);
        currentPhoneNo = getIntent().getStringExtra("currentPhoneNo");

        initView();
        initToolBar();
        listenter();

//        forgetSafePassCurrentPhone.setText(currentPhoneNo);
        countDownTimer = new CountDownTimer(1000 * AppConstants.VerifyCodeTimeFuture, 1000 * AppConstants.VerifyCodeTimeInteral) {
            @Override
            public void onTick(long millisUntilFinished) {
                forgetSafePassVerifyButton.setEnabled(false);
                forgetSafePassVerifyButton.setText((millisUntilFinished / 1000) + "秒");
                forgetSafePassVerifyButton.setTextColor(Color.parseColor("#cccccc"));
            }

            @Override
            public void onFinish() {
                forgetSafePassVerifyButton.setEnabled(true);
                forgetSafePassVerifyButton.setText("重新发送");
                forgetSafePassVerifyButton.setTextColor(Color.parseColor("#3574fa"));
            }
        };
    }

    private void listenter() {

        forgetSafePassVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String basicUrl = AppConstants.URL_SUFFIX + "/rest/sendPCodeUser";
                RequestForVerifyCode(basicUrl);
            }
        });

        forgetSafePassActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String basicUrl = AppConstants.URL_SUFFIX + "/rest/lostSafePwdVT";
                String verifyCode = forgetSafePassVerifyCode.getText().toString();
                String newPass = forgetSafePassNewPass.getText().toString();


                if (verifyCode == null || verifyCode.trim().equals("")) {
                    Toast.makeText(ForgetSafePassActivity.this, "请填写手机验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPass == null || newPass.equals("")) {
                    Toast.makeText(ForgetSafePassActivity.this, "请填写新的交易密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sessionid!=null){
                    RequestToSetNewSafePass(basicUrl, newPass, verifyCode);
                }else{
                    Toast.makeText(ForgetSafePassActivity.this,"请点击发送验证码",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void initView() {

        forgetSafePassToolbarBack = (IconFontTextView) findViewById(R.id.forget_safe_pass_toolbar_back);
        forgetSafePassToolbarTitle = (TextView) findViewById(R.id.forget_safe_pass_toolbar_title);
        forgetSafePassToolbar = (Toolbar) findViewById(R.id.forget_safe_pass_toolbar);
        forgetSafePassVerifyButton = (TextView) findViewById(R.id.forget_safe_pass_verify_button);
        forgetSafePassVerifyCode = (EditText) findViewById(R.id.forget_safe_pass_verify_code);
        forgetSafePassNewPass = (EditText) findViewById(R.id.forget_safe_pass_new_pass);
        forgetSafePassActionButton = (Button) findViewById(R.id.forget_safe_pass_action_button);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    public void initToolBar() {
        forgetSafePassToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(forgetSafePassToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    /**
     * 请求获取验证码
     *
     * @param basicUrl
     */
    public void RequestForVerifyCode(String basicUrl) {

        okhttp3.OkHttpClient mOkHttpClient = new okhttp3.OkHttpClient();
        okhttp3.RequestBody formBody = new FormBody.Builder()
                .add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                        build();
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
                        if (e == null || e.getMessage() == null) {
                            Toast.makeText(ForgetSafePassActivity.this, "访问服务器出错！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (e != null && e.getMessage() != null) {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String cookieval = response.header("Set-Cookie");
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
                            Toast.makeText(ForgetSafePassActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }


    public void RequestToSetNewSafePass(String basicUrl, String loginPass, String verifyCode) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("newPassword", loginPass)
                .addParams("codeReq", verifyCode)
                .addHeader("cookie", sessionid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        final BaseBean resultBack = new Gson().fromJson(s, BaseBean.class);
                        if (resultBack.getRcd().equals("R0001")) {

                            final LMessageDialog messageDialog = new LMessageDialog(ForgetSafePassActivity.this, R.style.ActionSheetDialogStyle);
                            messageDialog.setMessage("交易密码已经重置成功");
                            messageDialog.show();
                            messageDialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    messageDialog.dismiss();
                                    finish();
                                }
                            });

                        } else {
                            Toast.makeText(ForgetSafePassActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
