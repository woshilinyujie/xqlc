package com.rongxun.xqlc.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Article.ActivityDetailBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import okhttp3.Call;

/**
 * 活动详情
 * 注：简单的一个H5页面
 */
public class HuoDongDetailActivity extends MyBaseActivity {

    private RelativeLayout relBack;
    private TextView txtTitle;
    private WebView web;
    private String id;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huo_dong_detail);
        CustomApplication.addActivity(this);

        id = getIntent().getStringExtra("id");

        //初始化控件
        initView();
        //加载网页
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

        relBack = (RelativeLayout) findViewById(R.id.hd_rel_back);
        txtTitle = (TextView) findViewById(R.id.hd_txt_title);
        web = (WebView) findViewById(R.id.hd_web_view);

        WebSettings webSettings = web.getSettings();
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                //开启加载框
                if (dialog == null) {
                    dialog = new LoadingDialog(HuoDongDetailActivity.this);
                }
                dialog.showLoading();
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                //关闭对话框
                if (dialog.isShowing()) {
                    dialog.dismissLoading();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(HuoDongDetailActivity.this, "加载失败...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getHttpData() {

        OkHttpUtils
                .get()
                .url(AppConstants.HUODONG + "/" + id)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Toast.makeText(HuoDongDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {
                            ActivityDetailBean bean = GsonUtils.GsonToBean(response, ActivityDetailBean.class);
                            if (bean.getRcd().equals("R0001")) {
                                web.loadUrl(bean.getActivity().getContent());
                                txtTitle.setText(bean.getActivity().getTitle());
                            } else {
                                Toast.makeText(HuoDongDetailActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
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
