package com.rongxun.xqlc.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.reguser.RegUserBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;
public class FeedBackActivity extends MyBaseActivity {


    IconFontTextView feedbackToolbarBack;
    TextView feedbackToolbarTitle;
    Toolbar feedbackToolbar;
    EditText feedbackEditText;
    Button feedbackSubmit;

    private final String TAG = "意见反馈";
    private EditText emal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_feed_back);

        initView();
        initToolBar();
        listener();

        feedbackEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void listener() {

        feedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailString = emal.getText().toString();
                String contentString = feedbackEditText.getText().toString();
                if (contentString.length() == 0) {
                    Toast.makeText(FeedBackActivity.this, "反馈内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (emailString.length() == 0) {
                    Toast.makeText(FeedBackActivity.this, "请填写您的联系方式", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    feedBack(contentString, emailString);
                }

            }
        });
    }

    private void initView() {
        emal = (EditText) findViewById(R.id.feedback_edit_emal);
        feedbackToolbarBack = (IconFontTextView) findViewById(R.id.feedback_toolbar_back);
        feedbackToolbarTitle = (TextView) findViewById(R.id.feedback_toolbar_title);
        feedbackToolbar = (Toolbar) findViewById(R.id.feedback_toolbar);
        feedbackEditText = (EditText) findViewById(R.id.feedback_edit_text);
        feedbackSubmit = (Button) findViewById(R.id.feedback_submit);
        String userPhone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        if (userPhone != null) {
            emal.setText(userPhone);
        }
    }

    public void initToolBar() {
        feedbackToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(feedbackToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void feedBack(String content, String emailString) {
        final String basicUrl = AppConstants.URL_SUFFIX + "/rest/adFeedbackNew";

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("content", content)
                .addParams("contact", emailString)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.i(TAG, s);
                        final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                                if (resultBack.getRcd().equals("R0001")) {
                                    Toast.makeText(FeedBackActivity.this, "提交成功，谢谢您的反馈", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(FeedBackActivity.this, "" + resultBack.getRmg(), Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
