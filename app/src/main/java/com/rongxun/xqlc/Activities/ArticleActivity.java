package com.rongxun.xqlc.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.widget.TextView;
import android.widget.Toast;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Article.ArticleBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;

import okhttp3.Call;

/**
 * 文章详情
 * 注：获取网页配置的文字内容后，用webview显示出来
 */
public class ArticleActivity extends MyBaseActivity {

    private IconFontTextView relBack;
    private TextView txtTitle;
    private WebView web;
    private String id;//
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_article);

        id = getIntent().getStringExtra("id");

        initView();
        getHttpData();
        listener();
    }

    private void listener() {

        relBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {

        relBack = (IconFontTextView) findViewById(R.id.article_rel_back);
        txtTitle = (TextView) findViewById(R.id.article_txt_title);
        web = (WebView) findViewById(R.id.web_article);

        WebSettings webSettings = web.getSettings();

        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(false);//不可缩放

        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
//                //开启加载框
//                if (dialog == null) {
//                    dialog = new LoadingDialog(ArticleActivity.this);
//                }
//                dialog.showLoading();
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                //关闭对话框
//                if (dialog.isShowing()) {
//                    dialog.dismissLoading();
//                }
            }

        });

    }


    public void getHttpData() {

        OkHttpUtils
                .get()
                .url(AppConstants.ARTICLE + "/" + id)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Toast.makeText(ArticleActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {
                            ArticleBean bean = GsonUtils.GsonToBean(response, ArticleBean.class);
                            if (bean.getRcd().equals("R0001")) {
                                txtTitle.setText(bean.getTitle());
                                web.loadDataWithBaseURL(null, getHtmlData(bean.getContent()), "text/html", "utf-8", null);
                            } else {
                                Toast.makeText(ArticleActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
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
