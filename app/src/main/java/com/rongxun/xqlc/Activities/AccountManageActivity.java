package com.rongxun.xqlc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qiyukf.unicorn.api.Unicorn;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.BaseBean;
import com.rongxun.xqlc.Beans.EvaluateBean;
import com.rongxun.xqlc.Beans.userInfo.UserBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.CancleMessageDialog;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LMessageDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.Util.PrefUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.ServiscUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import okhttp3.Call;

public class AccountManageActivity extends MyBaseActivity {
    String exitUrl = AppConstants.URL_SUFFIX + "/rest/logout";
    private String userId;
    private final String TAG = "账户管理";
    private PopupWindow popupWindow;
    private TableRow realNameRow;
    private TableRow loginPassRow;
    private TableRow safePassRow;
    private TableRow gestureRow;
    private TableRow account_manage_table_row_shipping_address;
    private TextView realNameText;

    private String gesture;
    private ImageView slide_button;
    private boolean isCheck = false;
    private String code;
    private int CLOSECODE = 200;
    private RelativeLayout modify_code;
    private String phone;
    private Button account_manage_exit;
    private RelativeLayout account_manage_bank;
    private ImageView service;
    private TextView safeText;
    private RelativeLayout evaluate_rl;
    private TextView evaluate_tx;
    private String evaluateUrl;
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_account_manage);
        userId = getIntent().getStringExtra("userId");
        initToolBar();
        initViews();
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";
        RequestForListData(basicUrl, userId);
        EvaluateDate();

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.account_manage_toolbar);
        IconFontTextView toolBarBack = (IconFontTextView) findViewById(R.id.account_manage_toolbar_back);
        toolBarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void initViews() {
        phone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        if (phone == null)
            phone = "0";
        code = getSharedPreferences(phone, Context.MODE_PRIVATE).
                getString(phone, null);
        account_manage_table_row_shipping_address = (TableRow) findViewById(R.id.account_manage_table_row_shipping_address);
        realNameRow = (TableRow) findViewById(R.id.account_manage_table_row_real_name);
        loginPassRow = (TableRow) findViewById(R.id.account_manage_table_row_set_login);
        safePassRow = (TableRow) findViewById(R.id.account_manage_table_row_set_safe);
        gestureRow = (TableRow) findViewById(R.id.account_manage_table_row_modify_gesture);
        realNameText = (TextView) findViewById(R.id.account_manage_real_name_text);
        slide_button = (ImageView) findViewById(R.id.account_manage_slide_button);
        modify_code = (RelativeLayout) findViewById(R.id.account_manage_table_row_modify_code);
        account_manage_bank = (RelativeLayout) findViewById(R.id.account_manage_bank);
        account_manage_exit = (Button) findViewById(R.id.account_manage_exit);
        safeText = (TextView) findViewById(R.id.safe_pass_word_text);
        evaluate_rl = (RelativeLayout) findViewById(R.id.account_manage_evaluate_rl);
        evaluate_tx = (TextView) findViewById(R.id.account_manage_evaluate_text);
        name = (TextView) findViewById(R.id.account_manage_name);

        account_manage_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CancleMessageDialog dialog = new CancleMessageDialog(AccountManageActivity.this, R.style.ActionSheetDialogStyle);
                dialog.setTitle("确定退出登录");
                dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestForLogout(exitUrl);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }

    public void UpdateViews(final UserBean userBean) {
        final String phoneNo = userBean.getPhone();
        final int realStatus = userBean.getRealStatus() == null ? 0 : userBean.getRealStatus().intValue();
        name.setText("*"+userBean.getRealName().substring(1,userBean.getRealName().length()));

        //修改手势密码
        modify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountManageActivity.this, GestureCodeActivity.class);
                intent.putExtra("cancle", "modify");
                startActivityForResult(intent, CLOSECODE);
            }
        });

        //手势密码
        slide_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isCheck) {
                            code = getSharedPreferences(phone, Context.MODE_PRIVATE).
                                    getString(phone, null);
                            //如果关闭手势密码
                            if (code != null) {
                                //进入手势密码页面 输一次手势密码
                                System.out.println("手势密码关闭");
                                slide_button.setBackgroundResource(R.mipmap.slide_button);
                                new Handler().postDelayed(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                Intent intent = new Intent(AccountManageActivity.this, GestureCodeActivity.class);
                                                intent.putExtra("cancle", "sure");
                                                startActivityForResult(intent, CLOSECODE);
                                            }
                                        }, 200);
                                isCheck = false;
                            }
                        } else {
                            slide_button.setBackgroundResource(R.mipmap.slide_button_check);
                            System.out.println("手势密码打开");
                            isCheck = true;
                            final LMessageDialog dialog = new LMessageDialog(AccountManageActivity.this, R.style.ActionSheetDialogStyle);
                            dialog.setMessage("进入app时，需要输入正确的手势密码");
                            dialog.setMtitle("我知道了");
                            dialog.show();
                            dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    //进入密码设置页面
                                    Intent intent = new Intent(AccountManageActivity.this, GestureCodeActivity.class);
                                    startActivityForResult(intent, 200);
                                }
                            });
                        }
                    }
                });

        loginPassRow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(AccountManageActivity.this, ModifyLoginPassActivity.class);
                        startActivityForResult(intent, 1010);
                    }
                }
        );
        account_manage_table_row_shipping_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountManageActivity.this, HarvestAddressActiviyy.class));
            }
        });
        if (realStatus == 1) {
            realNameText.setText("已绑定");
            realNameRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AccountManageActivity.this,MyBankCardActivity.class));
                }
            });
        } else {
            realNameText.setText("未绑定");
            account_manage_bank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CancleMessageDialog dialog = new CancleMessageDialog(AccountManageActivity.this, R.style.ActionSheetDialogStyle);
                    dialog.setTitle("您的银行卡将在完成一笔投资或充值后添加");
                    dialog.setMessage("立即投资");
                    dialog.setCancleColour("#999999");
                    dialog.setCancleText("暂不考虑");
                    dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent("HomeFragmentBroadCast");
                            intent.putExtra("current", 1);
                            AccountManageActivity.this.sendBroadcast(intent);
                            dialog.dismiss();
                            AccountManageActivity.this.finish();
                        }
                    });
                    dialog.show();
                }
            });

        }
        safePassRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userBean.getSafePwdStatus()==1) {
                    Intent intentSafePassWord = new Intent();
                    intentSafePassWord.putExtra("currentPhoneNo", phoneNo);
                    intentSafePassWord.setClass(AccountManageActivity.this, ModifySafePassActivity.class);
                    startActivity(intentSafePassWord);
                } else {
                    startActivity(new Intent(AccountManageActivity.this, SafePassWordActivity.class));
                }
            }
        });
        evaluate_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(evaluateUrl)){
                    Intent h5 = new Intent(AccountManageActivity.this, H5JSActivity.class);
                    h5.putExtra("web_url", evaluateUrl);
                    h5.putExtra("EvaluateFlag", "2");

                    AccountManageActivity.this.startActivityForResult(h5,0x0001);
                }
            }
        });
        if(userBean.getSafePwdStatus()==1){
            safeText.setText("修改交易密码");
        }else{
            safeText.setText("交易密码设置");
        }

    }


    //请求网络数据
    public void RequestForListData(String basicUrl, String userId) {


        OkHttpUtils
                .post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {
                            UserBean resultBean = JSON.parseObject(response, UserBean.class);

                            if (resultBean.getRcd().equals("R0001")) {
                                UpdateViews(resultBean);
                            } else if (resultBean.getRcd().equals("E0001")) {
                                startActivityForResult(new Intent(AccountManageActivity.this, LoginActivity.class), 1400);
                                overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                                Toast.makeText(AccountManageActivity.this, "登录已过期，请重新登录!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AccountManageActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //请求退出登录
    public void RequestForLogout(String basicUrl) {

        OkHttpUtils
                .post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {

                            BaseBean resultBean = JSON.parseObject(response, BaseBean.class);

                            if (resultBean.getRcd().equals("R0001")) {

                                SharedPreferences preferences = AccountManageActivity.this.getSharedPreferences("AppToken", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                //清除Token数据
                                editor.clear().commit();
                                AccountManageActivity.this.getSharedPreferences("UserId", Context.MODE_PRIVATE).edit().clear().commit();
                                PrefUtils.putBoolean(AccountManageActivity.this, "SafePassWord", false);
                                sendBroadcast(new Intent("LoginContentBroadCast").putExtra("Quickly","exit"));
                                finish();
                            } else {
                                Toast.makeText(AccountManageActivity.this, "网络异常，请重试！", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == AppConstants.RealAuthIng) {
            String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";
            RequestForListData(basicUrl, userId);
        } else if (resultCode == AppConstants.RealAuthSuccess) {
            String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";
            RequestForListData(basicUrl, userId);
        } else if (resultCode == AppConstants.backToUserCenterResult) {
            String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";
            RequestForListData(basicUrl, userId);
        } else if (resultCode == 100) {
            Intent intent = new Intent("HomeFragmentBroadCast");
            intent.putExtra("gesture", 200);
            sendBroadcast(intent);
            finish();
        } else if (resultCode == 200) {
            Intent intent = new Intent(AccountManageActivity.this, GestureCodeActivity.class);
            startActivityForResult(intent, 200);

        }else if(requestCode==0x0001){
            EvaluateDate();
            return;
        }

        if (getSharedPreferences(phone, Context.MODE_PRIVATE).
                getString(phone, null) != null &&
                !getSharedPreferences("GestureAgain", Context.MODE_PRIVATE).getBoolean("isAgain", false)) {
            slide_button.setBackgroundResource(R.mipmap.slide_button_check);
            System.out.println("手势密码回调打开");
            isCheck = true;
        } else {
            slide_button.setBackgroundResource(R.mipmap.slide_button);
            System.out.println("手势密码回调关闭");
            isCheck = false;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public  void EvaluateDate(){
        OkHttpUtils
                .post()
                .url(AppConstants.URL_SUFFIX+"/rest/appraisalShow")
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        EvaluateBean evaluateBean = GsonUtils.GsonToBean(response, EvaluateBean.class);
                        if(evaluateBean.getRcd().equals("R0001")){
                            evaluate_tx.setText(evaluateBean.getLast());
                            evaluateUrl = evaluateBean.getAppraisalURL();
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
