package com.rongxun.xqlc.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Article.ArticleBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import okhttp3.Call;

public class ArticleContentActivity extends MyBaseActivity {

    private String TAG = "详情内容";
    private WebView webView;//内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "直接显示内容页");
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.content_article_content);
        String header = getIntent().getStringExtra("header");
        initActionBar(header);

        webView = (WebView) findViewById(R.id.content_article_web_view);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        if ("安全保障".equals(header)) {
            webView.loadUrl("file:///android_asset/subject/subject_safe.html");
        } else {
            String basicUrl = AppConstants.URL_SUFFIX + "/rest/article/";
            String id = getIntent().getIntExtra("id", 0) + "";
            RequestForListData(basicUrl, id);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    public void initActionBar(String title) {

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.more_action_bar_layout);
        TextView moreBack = (TextView) actionBar.getCustomView().findViewById(R.id.more_actionbar_back);
        TextView moreTitle = (TextView) actionBar.getCustomView().findViewById(R.id.more_actionbar_textview);
        moreBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        moreTitle.setText(title);
    }

    //请求网络数据
    public void RequestForListData(String basicUrl, String id) {

        String url = basicUrl + id;
        Log.i(TAG, url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.i(TAG, "response json:" + s);
                        final ArticleBean article = JSON.parseObject(s, ArticleBean.class);

                        Log.i(TAG, "rcd===" + article.getRcd());
                        if (article.getRcd().equals("R0001")) {
                            Log.i(TAG, "文章内容=" + article.getContent());
                            webView.loadData(article.getContent(), "text/html; charset=UTF-8", null);//这种写法可以正确解码
                        } else {
                            Toast.makeText(ArticleContentActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
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
