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

import java.util.regex.Pattern;


public class ModifySafePassActivity extends MyBaseActivity {

    EditText safePassModifyOld;
    EditText safePassModifyNew;
    EditText safePassModifyNewTwice;
    Button safePassModifySubmit;
    TextView safePassModifyForget;
    IconFontTextView modifySafePassToolbarBack;
    TextView modifySafePassToolbarTitle;
    Toolbar modifySafePassToolbar;

    private String TAG = "修改交易密码";
    private LMessageDialog messageDialog;
    private String currentPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_modify_safe_pass);
        currentPhoneNo = getIntent().getStringExtra("currentPhoneNo");

        initView();
        initListener();
        initToolBar();
    }

    private void initView() {

        safePassModifyOld = (EditText) findViewById(R.id.safe_pass_modify_old);
        safePassModifyNew = (EditText) findViewById(R.id.safe_pass_modify_new);
        safePassModifyNewTwice = (EditText) findViewById(R.id.safe_pass_modify_new_twice);
        safePassModifySubmit = (Button) findViewById(R.id.safe_pass_modify_submit);
        safePassModifyForget = (TextView) findViewById(R.id.safe_pass_modify_forget);
        modifySafePassToolbarBack = (IconFontTextView) findViewById(R.id.modify_safe_pass_toolbar_back);
        modifySafePassToolbarTitle = (TextView) findViewById(R.id.modify_safe_pass_toolbar_title);
        modifySafePassToolbar = (Toolbar) findViewById(R.id.modify_safe_pass_toolbar);


    }

    private void initListener() {
        safePassModifyOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(safePassModifyOld.getText().toString()) &&
                        !TextUtils.isEmpty(safePassModifyNew.getText().toString()) &&
                        !TextUtils.isEmpty(safePassModifyNewTwice.getText().toString())) {
                    safePassModifySubmit.setBackgroundResource(R.drawable.button_background_normal);
                } else {
                    safePassModifySubmit.setBackgroundResource(R.drawable.button_background_grey);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        safePassModifyNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(safePassModifyOld.getText().toString()) &&
                        !TextUtils.isEmpty(safePassModifyNew.getText().toString()) &&
                        !TextUtils.isEmpty(safePassModifyNewTwice.getText().toString())) {
                    safePassModifySubmit.setBackgroundResource(R.drawable.button_background_normal);
                } else {
                    safePassModifySubmit.setBackgroundResource(R.drawable.button_background_grey);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        safePassModifyNewTwice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(safePassModifyOld.getText().toString()) &&
                        !TextUtils.isEmpty(safePassModifyNew.getText().toString()) &&
                        !TextUtils.isEmpty(safePassModifyNewTwice.getText().toString())) {
                    safePassModifySubmit.setBackgroundResource(R.drawable.button_background_normal);
                } else {
                    safePassModifySubmit.setBackgroundResource(R.drawable.button_background_grey);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        safePassModifySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String basicUrl = AppConstants.URL_SUFFIX + "/rest/userSafePwdUpdate";

                String oldSafe = safePassModifyOld.getText().toString();
                String newSafe = safePassModifyNew.getText().toString();
                String onewSafetwice = safePassModifyNewTwice.getText().toString();

                if (checkFormData(oldSafe, newSafe, onewSafetwice)) {
                    RequestModifySafePass(basicUrl, oldSafe, newSafe);
                }
            }
        });

        safePassModifyForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ModifySafePassActivity.this, ForgetSafePassActivity.class);
                intent.putExtra("currentPhoneNo", currentPhoneNo);
                startActivity(intent);
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

    /**
     * 请求修改安全密码
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
                            messageDialog = new LMessageDialog(ModifySafePassActivity.this,R.style.ActionSheetDialogStyle);
                            messageDialog.setMessage("交易密码修改成功！");
                            messageDialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                    messageDialog.dismiss();
                                }
                            });
                            messageDialog.show();
                        } else if (resultBack.getRcd().equals("S0002")) {
                            Toast.makeText(ModifySafePassActivity.this, "交易密码输入有误！", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public boolean checkFormData(String oldSafe, String newSafe, String onewSafetwice) {


        if (oldSafe == null || oldSafe.trim().equals("")) {
            Toast.makeText(ModifySafePassActivity.this, "请输入原始密码!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (newSafe == null || newSafe.trim().equals("")) {
            Toast.makeText(ModifySafePassActivity.this, "请输入新密码!", Toast.LENGTH_LONG).show();
            return false;
        }
         if (!isStringLengthIngnel(newSafe)) {
            Toast.makeText(ModifySafePassActivity.this, "交易密码长度至少为6个字符", Toast.LENGTH_LONG).show();
            return false;
        }
        if (onewSafetwice == null || onewSafetwice.trim().equals("")) {
            Toast.makeText(ModifySafePassActivity.this, "新密码不一致！", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!newSafe.equals(onewSafetwice)) {
            Toast.makeText(ModifySafePassActivity.this, "两次新密码不一致！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }


    public void initToolBar() {
        modifySafePassToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(modifySafePassToolbar);
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
