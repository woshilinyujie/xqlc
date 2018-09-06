package com.rongxun.xqlc.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Article.ArticleBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

public class SafeActivity extends MyBaseActivity {

    private String TAG="SafeActivity";
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_safe);
        IconFontTextView black = (IconFontTextView) findViewById(R.id.safe_black);
        web = (WebView) findViewById(R.id.safe_web);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        String url= AppConstants.URL_SUFFIX + "/rest/article/";
        web.loadDataWithBaseURL(null,getHtmlData(url), "text/html", "utf-8", null);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RequestForListData(url,"762");
    }


    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public void RequestForListData(String basicUrl, String id) {

        String url = basicUrl + id;
        Log.i(TAG, url);

        OkHttpUtils.post()
                .url(url)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
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
                        Log.i(TAG, "response json:" + s);
                        final ArticleBean article = JSON.parseObject(s, ArticleBean.class);
                        Log.i(TAG, "rcd===" + article.getRcd());
                        if (article.getRcd().equals("R0001")) {
                            web.loadDataWithBaseURL(null,getHtmlData(article.getContent()), "text/html", "utf-8", null);
                        } else {
                            Toast.makeText(SafeActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
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
