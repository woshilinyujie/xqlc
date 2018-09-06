package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.BaseBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LMessageDialog;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PrefUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import okhttp3.Call;


public class SafePassWordActivity extends MyBaseActivity {

    private IconFontTextView back;
    private EditText password1;
    private EditText password2;
    private Button sure;
    private String TAG="SafePassWordActivity";
    String basicUrl = AppConstants.URL_SUFFIX + "/rest/userSafePwdUpdateVT";
    private LMessageDialog messageDialog;
    private LoadingDialog loadingDialog;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_pass_word);
        CustomApplication.addActivity(this);
        initView();
        initListener();
    }

    private void initListener() {
        password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(password1.getText().toString())&&
                        !TextUtils.isEmpty(password2.getText().toString())){
                    sure.setBackgroundResource(R.drawable.button_background_normal);
                }else{
                    sure.setBackgroundResource(R.drawable.button_background_grey);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(password1.getText().toString())&&
                        !TextUtils.isEmpty(password2.getText().toString())){
                    sure.setBackgroundResource(R.drawable.button_background_normal);
                }else{
                    sure.setBackgroundResource(R.drawable.button_background_grey);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void initView() {
        flag = getIntent().getStringExtra("MineFragment");
        back = (IconFontTextView) findViewById(R.id.safe_password_back);
        password1 = (EditText) findViewById(R.id.safe_password_password1);
        password2 = (EditText) findViewById(R.id.safe_password_password2);
        sure = (Button) findViewById(R.id.safe_password_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNet();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initNet() {

        //校验密码是否一致
        if(checkFormData( password1.getText().toString(),password2.getText().toString())){
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(this);
                loadingDialog.showLoading();
            }

            OkHttpUtils.post()
                    .url(basicUrl)
                    .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                    .addParams("newPassword",password1.getText().toString())
                    .addParams("newPassword1",password1.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (e != null && e.getMessage() != null) {
                                Log.i(TAG, e.getMessage());
                            }
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismissLoading();
                                loadingDialog = null;
                            }
                        }

                        @Override
                        public void onResponse(String s, int id) {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismissLoading();
                                loadingDialog = null;
                            }
                            Log.i(TAG, s);
                            final BaseBean resultBack = new Gson().fromJson(s, BaseBean.class);
                            if (resultBack.getRcd().equals("R0001")) {
                                if(flag!=null && flag.equals("MineFragment")){
                                    Intent intent=new Intent("MineFragmentBroadCast");
                                    intent.putExtra("refresh","refresh");
                                    sendBroadcast(intent);
                                }
                                //存储交易密码
                                PrefUtils.putBoolean(SafePassWordActivity.this,"SafePassWord",true);
                                messageDialog = new LMessageDialog(SafePassWordActivity.this,R.style.ActionSheetDialogStyle);
                                messageDialog.setTitle("交易密码设置成功！");
                                messageDialog.setMessage("交易密码设置成功！");
                                messageDialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent("LoginContentBroadCast");
                                        intent.putExtra("Quickly","cash");
                                        sendBroadcast(intent);
                                        finish();
                                        messageDialog.dismiss();
                                        messageDialog = null;
                                    }
                                });

                                messageDialog.show();
                            } else{
                                Toast.makeText(SafePassWordActivity.this, resultBack.getRmg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });



        }else{
            Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT);
        }
    }


    public boolean checkFormData(String password1, String password2) {


        if ( password1 == null ||  password1.trim().equals("")) {
            Toast.makeText(this, "请输入交易密码!", Toast.LENGTH_LONG).show();
            return false;
        }
        if ( password2 == null ||  password2.trim().equals("")) {
            Toast.makeText(this, "请输入交易密码!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!password1.equals(password2)) {
            Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isStringLengthIngnel(password1)) {
            Toast.makeText(this, "交易密码长度至少为6个字符", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }




    /**
     * 判断字符串是否是6-16位之间
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
