package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.BaseBean;
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
import java.util.regex.Pattern;


public class ModifyLoginPassActivity extends MyBaseActivity {

    EditText loginPassModifyOld;
    EditText loginPassModifyNew;
    EditText loginPassModifyNewTwice;
    Button loginPassModifyButton;
    IconFontTextView modifyLoginPassToolbarBack;
    TextView modifyLoginPassToolbarTitle;
    Toolbar modifyLoginPassToolbar;
    private String TAG = "修改登录密码";
    private LMessageDialog messageDialog;
    private TextView forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_login_pass);
        CustomApplication.addActivity(this);

        initView();
        initToolBar();
        initListener();
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ModifyLoginPassActivity.this, ForgetLoginPassActivity.class);
                startActivity(intent);
            }
        });

        loginPassModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String basicUrl = AppConstants.URL_SUFFIX + "/rest/userLoginPwdUpdate";

                String oldPass = loginPassModifyOld.getText().toString();
                String newPass = loginPassModifyNew.getText().toString();
                String onePasstwice = loginPassModifyNewTwice.getText().toString();

                if (checkFormData(oldPass, newPass, onePasstwice)) {
                    RequestModifySafePass(basicUrl, oldPass, newPass);
                }
            }
        });
    }

    private void initListener() {
        loginPassModifyOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(loginPassModifyOld.getText().toString()) &&
                        !TextUtils.isEmpty(loginPassModifyNew.getText().toString())&&
                        !TextUtils.isEmpty(loginPassModifyNewTwice.getText().toString())){
                    loginPassModifyButton.setBackgroundResource(R.drawable.button_background_normal);
                }else{
                    loginPassModifyButton.setBackgroundResource(R.drawable.button_background_grey);
                }
            }
        });

        loginPassModifyNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(loginPassModifyOld.getText().toString()) &&
                        !TextUtils.isEmpty(loginPassModifyNew.getText().toString())&&
                        !TextUtils.isEmpty(loginPassModifyNewTwice.getText().toString())){
                    loginPassModifyButton.setBackgroundResource(R.drawable.button_background_normal);
                }else{
                    loginPassModifyButton.setBackgroundResource(R.drawable.button_background_grey);
                }
            }
        });

        loginPassModifyNewTwice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(loginPassModifyOld.getText().toString()) &&
                        !TextUtils.isEmpty(loginPassModifyNew.getText().toString())&&
                        !TextUtils.isEmpty(loginPassModifyNewTwice.getText().toString())){
                    loginPassModifyButton.setBackgroundResource(R.drawable.button_background_normal);
                }else{
                    loginPassModifyButton.setBackgroundResource(R.drawable.button_background_grey);
                }
            }
        });
    }

    private void initView() {

        forget = (TextView) findViewById(R.id.login_pass_modify_forget);

        loginPassModifyOld = (EditText) findViewById(R.id.login_pass_modify_old);
        loginPassModifyNew = (EditText) findViewById(R.id.login_pass_modify_new);
        loginPassModifyNewTwice = (EditText) findViewById(R.id.login_pass_modify_new_twice);
        loginPassModifyButton = (Button) findViewById(R.id.login_pass_modify_button);
        modifyLoginPassToolbarBack = (IconFontTextView) findViewById(R.id.modify_login_pass_toolbar_back);
        modifyLoginPassToolbarTitle = (TextView) findViewById(R.id.modify_login_pass_toolbar_title);
        modifyLoginPassToolbar = (Toolbar) findViewById(R.id.modify_login_pass_toolbar);



    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    /**
     * 请求修改登录密码
     */
    public void RequestModifySafePass(String basicUrl, String oldPassword, String newPassword) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("oldPassword", oldPassword)
                .addParams("newPassword", newPassword)
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
                            messageDialog = new LMessageDialog(ModifyLoginPassActivity.this,R.style.ActionSheetDialogStyle);
                            messageDialog.setMessage("登录密码修改成功！");
                            messageDialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                    messageDialog.dismiss();
                                }
                            });
                            messageDialog.show();

                        } else if (resultBack.getRcd().equals("E0002")) {
                            Toast.makeText(ModifyLoginPassActivity.this, "参数错误！", Toast.LENGTH_LONG).show();
                        } else if (resultBack.getRcd().equals("S0003")) {
                            Toast.makeText(ModifyLoginPassActivity.this, "登录密码输入有误！", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public boolean checkFormData(String oldSafe, String newSafe, String onewSafetwice) {

        if (oldSafe == null || oldSafe.trim().equals("")) {
            Toast.makeText(ModifyLoginPassActivity.this, "请输入原密码!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (newSafe == null || newSafe.trim().equals("") || onewSafetwice == null || onewSafetwice.trim().equals("")) {
            Toast.makeText(ModifyLoginPassActivity.this, "请输入新密码!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (isNumeric(newSafe)) {
            Toast.makeText(ModifyLoginPassActivity.this, "密码必须包含字母", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isStringLengthIngnel(newSafe)) {
            Toast.makeText(ModifyLoginPassActivity.this, "密码长度至少为6个字符", Toast.LENGTH_LONG).show();
            return false;
        }
        if (onewSafetwice == null || onewSafetwice.trim().equals("")) {
            Toast.makeText(ModifyLoginPassActivity.this, "请重复新密码！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!newSafe.equals(onewSafetwice)) {
            Toast.makeText(ModifyLoginPassActivity.this, "两次新密码不一致！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    public void initToolBar() {
        modifyLoginPassToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(modifyLoginPassToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 判断是否为纯数字字符串
     *
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否是8-16位之间
     *
     * @return
     */
    public static boolean isStringLengthIngnel(String str) {
        if (str.length() >= 6) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }


}
