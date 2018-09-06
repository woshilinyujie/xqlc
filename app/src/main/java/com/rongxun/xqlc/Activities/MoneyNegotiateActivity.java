package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

public class MoneyNegotiateActivity extends MyBaseActivity {

    private String TAG = "借款协议";
    private int projectId;
    private int itemId;
    private WebView web;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_money_negotiate);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        projectId = intent.getIntExtra("projectId", 0);
        itemId = intent.getIntExtra("itemId", 0);
        if(TextUtils.isEmpty(intent.getStringExtra("LoanAgreementUrl"))){
            url = AppConstants.URL_SUFFIX + "/rest/borrowAgreement?borrow.id=" + projectId + "&id=" + itemId + "&token=" + PreferenceUtil.getPrefString(this, "loginToken", "");
        }else {
            url =intent.getStringExtra("LoanAgreementUrl");
        }
        web = (WebView) findViewById(R.id.money_negotiate_web);
        IconFontTextView back= (IconFontTextView) findViewById(R.id.money_negotiate_back);
        WebSettings settings = web.getSettings();
        web.getSettings().setJavaScriptEnabled(true);
// 设置可以支持缩放
        web.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        web.getSettings().setBuiltInZoomControls(true);
//扩大比例的缩放
        web.getSettings().setUseWideViewPort(true);
//自适应屏幕
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web.getSettings().setLoadWithOverviewMode(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        web.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
        web.loadUrl(url);
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
