package com.rongxun.xqlc.Activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
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


public class RegisterActivity extends MyBaseActivity implements View.OnClickListener {


    private Button register;
    private EditText imagine_number;
    private EditText password;
//    private CheckBox check;
    private String id;
    private String sessionid;
    private String TAG = "注册";
    private boolean isClose = true;
    private RelativeLayout register_rl;
    private boolean isImagineClose = true;
    private TextView negotiate;
    private LoadingDialog loaginDialog;
    private SharedPreferences preferences;
    private ImageView back;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            register.setEnabled(true);
        }
    };
    private String from;
    private TextView book;
    private String code;
    private ImageView back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initListener();
    }

    private void initViews() {
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        sessionid = intent.getStringExtra("sessionid");
        code = intent.getStringExtra("code");
        id = intent.getStringExtra("phone_number");
        negotiate = (TextView) findViewById(R.id.regiest_negotiate);
        register = (Button) findViewById(R.id.register);
        imagine_number = (EditText) findViewById(R.id.register_imagine_number);
        password = (EditText) findViewById(R.id.register_password);
        register_rl = (RelativeLayout) findViewById(R.id.register_rl);
        book = (TextView) findViewById(R.id.regiest_book);
        back = (ImageView) findViewById(R.id.register_toolbar_back);

    }

    private void initListener() {
        negotiate.setOnClickListener(this);
        book.setOnClickListener(this);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(password.getText().toString())) {
                    register.setBackgroundResource(R.drawable.button_background_normal);
                } else {
                    register.setBackgroundResource(R.drawable.button_background_grey);
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
            case R.id.regiest_negotiate:
//                startActivity(new Intent(this, RegisterNegotiateActivity.class));
                //跳转到详情页
                Intent intentNegotiate = new Intent(RegisterActivity.this, ArticleActivity.class);
                intentNegotiate.putExtra("id", "2");
                intentNegotiate.putExtra("header", "服务协议");
                startActivity(intentNegotiate);
                break;
            case R.id.regiest_book:
//                startActivity(new Intent(this, RegisterNegotiateActivity.class));
                //跳转到详情页
                Intent intentBook = new Intent(RegisterActivity.this, ArticleActivity.class);
                intentBook.putExtra("id", "875");
                intentBook.putExtra("header", "风险提示书");
                startActivity(intentBook);
                break;


            case R.id.register:
                    String basicUrl = AppConstants.URL_SUFFIX + "/rest/reg";
                    String passWordString = password.getText().toString();
                    if (checkForm() && register.isEnabled()) {
                        //防重复
                        register.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0, 3000);
                        RequestForRegister(basicUrl, id, passWordString, code);
                    }
                break;

            case R.id.register_toolbar_back:
                finish();
                break;
        }
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                StringBuilder stringBuilder = new StringBuilder();
                for (int x = 0; x < contacts[1].length(); x++) {
                    if (isInteger(contacts[1].substring(x, x + 1))) {
                        stringBuilder.append(contacts[1].substring(x, x + 1));
                    }
                }
                imagine_number.setText(stringBuilder.toString());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
//            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    public static boolean isInteger(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    public boolean checkForm() {
        String passWordString = password.getText().toString();
        if (passWordString == null || passWordString.trim().equals("")) {
            Toast.makeText(this, "请输入登录密码", Toast.LENGTH_LONG).show();
            return false;
        } else if (passWordString.contains(" ")) {
            Toast.makeText(this, "登录密码包含了空格", Toast.LENGTH_LONG).show();
            return false;
        } else if (isInteger(passWordString)) {
            Toast.makeText(this, "登录密码必须包含字母", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isStringLengthIngnel(passWordString)) {
            Toast.makeText(this, "登录密码长度至少8个字符", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    /**
     * 判断字符串是否是6位之间
     *
     * @return
     */
    public static boolean isStringLengthIngnel(String str) {
        if (str.length() >= 8) {
            return true;
        }
        return false;
    }


    /**
     * 请求注册
     *
     * @param basicUrl
     */
    public void RequestForRegister(String basicUrl, String phoneNoString, String passWord, String verifyCode) {
        PostFormBuilder post = OkHttpUtils.post();
        post.url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("placeName", AppVersionUtils.getChannelName(RegisterActivity.this, "UMENG_CHANNEL") + "")
                .addParams("im", AppVersionUtils.getPhoneIMEI(RegisterActivity.this))
                .addParams("deviceToken", PreferenceUtil.getPrefString(RegisterActivity.this, "deviceToken", ""))
                .addParams("user.password", passWord)
                .addParams("user.phone", phoneNoString)
                .addParams("codeReg", verifyCode)
                .addHeader("cookie", sessionid);
        if (!TextUtils.isEmpty(imagine_number.getText().toString())) {
            if (imagine_number.getText().toString().length() < 11) {
                Toast.makeText(this, "请输入正确的推荐人号码", Toast.LENGTH_SHORT).show();
                return;
            } else {
                post.addParams("referrer", imagine_number.getText().toString());
            }
        }
        post.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Toast.makeText(RegisterActivity.this, "连接网络失败", Toast.LENGTH_LONG).show();
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                        if (resultBack.getRcd().equals("R0001")) {
                            SharedPreferences preferences = RegisterActivity.this.getSharedPreferences("AppToken", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            //存入数据
                            editor.putString("loginToken", resultBack.getToken());
                            //提交修改
                            editor.commit();
                            //自动登入
                            VoluntaryLogin();

                        } else {
                            Toast.makeText(RegisterActivity.this, "" + resultBack.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    //自动登入
    private void VoluntaryLogin() {
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/login";


        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(password.getWindowToken(), 0);


        if (loaginDialog == null) {

            loaginDialog = new LoadingDialog(this);
            loaginDialog.showLoading();
        }
        RequestForLogin(basicUrl);
    }


    public void RequestForLogin(String basicUrl) {
        preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String deviceToken = preferences.getString("deviceToken", "");

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("username", id).
                addParams("password", password.getText().toString()).
                addParams("deviceToken", deviceToken)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Toast.makeText(RegisterActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
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
                    public void onResponse(String s, int idd) {
                        Log.i(TAG, s);
                        final LoginBean resultBack = new Gson().fromJson(s, LoginBean.class);
                        if (loaginDialog != null && loaginDialog.isShowing()) {
                            loaginDialog.dismissLoading();
                            loaginDialog = null;
                        }
                        if (resultBack.getRcd().equals("R0001")) {

                            //登录成功
                            if (from != null && from.equals("gesture")) {
                                //清除手势密码
                                RegisterActivity.
                                        this.getSharedPreferences(RegisterActivity.this.getSharedPreferences("UserId",
                                        Context.MODE_PRIVATE).getString("UserId", null),
                                        Context.MODE_PRIVATE).edit()
                                        .clear().commit();
                                CustomApplication.clearActivity();
                                Intent intentMain = new Intent(RegisterActivity.this, MainActivity.class);
                                RegisterActivity.this.startActivity(intentMain);
                                Intent intent2 = new Intent("GesturebroadCast");
                                GestureCodeActivity.from = null;
                                intent2.putExtra("value", "finish");
                                RegisterActivity.this.sendBroadcast(intent2);
                            } else {
                                Intent refreshBroadCast = new Intent("LoginContentBroadCast");
                                refreshBroadCast.putExtra("Quickly", "Quickly");
                                RegisterActivity.this.sendBroadcast(refreshBroadCast);


                                startActivity(new Intent(RegisterActivity.this, RegisterSuccessActivity.class));
                            }
                            //通知登录注册页面关闭
                            Intent finishIntent = new Intent("LoginActivityBroadcast");
                            finishIntent.putExtra("finish", "finish");
                            RegisterActivity.this.sendBroadcast(finishIntent);
                            SharedPreferences.Editor editor = preferences.edit();
                            //存入数据
                            editor.putString("loginToken", resultBack.getToken());
                            //提交修改
                            editor.commit();
                            RegisterActivity.this.getSharedPreferences("UserId", Context.MODE_PRIVATE).
                                    edit().putString("UserId", id)
                                    .commit();
                            PrefUtils.putBoolean(RegisterActivity.this, "SafePassWord", false);

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

                            RegisterActivity.this.finish();

                        } else if (resultBack.getRcd().equals("M0001")) {
                            //用户名不存在或密码错误
                            Toast.makeText(RegisterActivity.this, "用户名不存在或密码错误", Toast.LENGTH_SHORT).show();

                        } else if (resultBack.getRcd().equals("M0002")) {
                            //您的账号已被禁用,无法登录，如有疑问请联系客服人员
                            Toast.makeText(RegisterActivity.this, "您的账号已被禁用,无法登录", Toast.LENGTH_SHORT).show();
                        } else if (resultBack.getRcd().equals("M0003")) {
                            //您的账号已被锁定，如有疑问请联系客服人员

                            Toast.makeText(RegisterActivity.this, "您的账号将被锁定!", Toast.LENGTH_SHORT).show();


                        } else if (resultBack.getRcd().equals("M0004")) {
                            //若连续N次密码输入错误,您的账号将被锁定!

                            Toast.makeText(RegisterActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(RegisterActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions[0].equals(Manifest.permission.READ_CONTACTS)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            Intent intent = new Intent(Intent.ACTION_PICK,
                    uri);

            startActivityForResult(intent, 0);
        } else {

        }
    }

}
