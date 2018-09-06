package com.rongxun.xqlc.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.widget.SpringView;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.SpringHeader;
import com.rongxun.xqlc.Util.PrefUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.PreventFastClickUtils;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import static android.view.KeyEvent.KEYCODE_BACK;

public class H5JSActivity extends AppCompatActivity {

    private RelativeLayout relBack;//返回
    private WebView webView;
    private LoadingDialog dialog;
    private String h5Url;
    private String flag;//标识
    private TextView txtTitle;
    private boolean isFristWelfare = true;//是不是新手福利第一次进入
    private H5JSReceiver h5JSReceiver;
    private TextView yaoqing;
    private ImageView relShare;
    private String evaluateFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_js);
        CustomApplication.addActivity(this);
        //////////////注册广播////////////////
        //注册广播
        h5JSReceiver = new H5JSReceiver();
        registerReceiver(h5JSReceiver, new IntentFilter("intent.action.h5js.refresh.token"));

        ///////////////接收参数///////////////
        h5Url = getIntent().getStringExtra("web_url");
//        h5Url = "http://120.27.160.157:82/rest/borrowAgreement?borrow.id=3281&id=65874&token=d3fc38bccf2d577125e7104b08f07f96";
        flag = getIntent().getStringExtra("flag");
        evaluateFlag=getIntent().getStringExtra("EvaluateFlag");
        // TODO: 2017/8/4 0004 若有其他需求，像新手福利这种，依次向后添加

        initView();
        initData();
        listener();

        /////////////////新手福利///////////////
        if (flag != null && flag.equals("welfare") && isFristWelfare) {
            //保存配置文件
            PrefUtils.putBoolean(this, "welfare", false);
            isFristWelfare = false;
            //发一个广播,隐藏新手福利红点
            Intent home = new Intent();
            home.setAction("intent.action.home.data.refresh");
            home.putExtra("home", 111);
            sendBroadcast(home);
            yaoqing.setVisibility(View.GONE);
        }
        /////////////////新手福利///////////////

        /////////////////邀请好友///////////////
        if (flag != null && flag.equals("yaoqing")) {
            //如果是点击推荐有礼进入
            yaoqing.setVisibility(View.VISIBLE);
            relShare.setVisibility(View.GONE);
        } else {
            yaoqing.setVisibility(View.GONE);
        }
        /////////////////邀请好友///////////////

    }


    private void listener() {

        relBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag != null && flag.equals("ads")) {
                    //由读秒广告页进来，按返回键，则进入主页
                    Intent main = new Intent(H5JSActivity.this, MainActivity.class);
                    startActivity(main);
                    finish();
                } else {
                    //其他情况均结束当前页面即可
                    finish();

                }
            }
        });

        yaoqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = PreferenceUtil.getPrefString(H5JSActivity.this, "loginToken", "");
                if (ss != null && ss.length() > 0) {
                    startActivity(new Intent(H5JSActivity.this, InvitedToRecordActivity.class));
                } else {
                    startActivity(new Intent(H5JSActivity.this, LoginActivity.class));
                }

            }
        });

    }

    private void initData() {
        // TODO: 2017/7/11 0011
    }

    private void initView() {

        txtTitle = (TextView) findViewById(R.id.h5_js_txt_title);
        relBack = (RelativeLayout) findViewById(R.id.h5_js_rel_back);
        webView = (WebView) findViewById(R.id.h5_js_web);
        yaoqing = (TextView) findViewById(R.id.yaoqing);
        relShare = (ImageView) findViewById(R.id.share_is_no_iv);
        /////////////////////////////////
        // TODO: 2017/8/7 0007 初始化弹性控件  测试
        initSpring();

        initWebView();


    }

    private void initSpring() {

        //设置上拉到webView顶部时，可以回到上一页
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
    }

    private void initWebView() {

        WebSettings webSettings = webView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//        h5Url = "http://192.168.0.176:18080/";
        webView.loadUrl(h5Url);


        //是否可以后退
        webView.canGoBack();
        //是否可以前进
        webView.canGoForward();

        //下面方法去掉右边滚动
        IX5WebViewExtension ix5 = webView.getX5WebViewExtension();
        if (null != ix5) {
            ix5.setScrollBarFadingEnabled(false);
        }

        //////////////////JS调用Android//////////////////////
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                try {
                    //开启加载框
                    if (dialog == null ) {
                        dialog = new LoadingDialog(H5JSActivity.this);
                    }
                    dialog.showLoading();
                }catch (Exception e){

                }
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                try {
                    //关闭对话框
                    if(dialog!=null){
                        if (dialog.isShowing()) {
                            dialog.dismissLoading();
                        }
                    }
                }catch (Exception e){

                }
                //Android调用JS方法，传Token
                AndroidDoJs();
                //有前端决定是否显示
                if(!TextUtils.isEmpty(evaluateFlag)){
                    Evaluate(evaluateFlag);
                }
                // 分享安钮
                isShowShareButton();
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Toast.makeText(H5JSActivity.this, "加载失败...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //js://webview?markdetail=111&mark=222
                /**
                 * gkand://web?status=0&&activity=类名&&id=
                 * gkand://web?status=1&&twoDimensionalUrl=url
                 * gkand://web?status=2&&shareTitle=名称&&shareUrl=url&&shareDescription=描述
                 * gkand://web?status=3&&fragment= 名称
                 */


                Uri uri = Uri.parse(url);

                if (uri.getScheme().equals("gkand")) {
                    if (uri.getAuthority().equals("web")) {
                        switch (uri.getQueryParameter("status")) {

                            case "0":
                                ///////////页面跳转//////////
                                //获取类的全名
                                String activityName = getPackageName() + "." + "Activities" + "."
                                        + uri.getQueryParameter("activity");
                                //获取参数
                                String id = uri.getQueryParameter("id");
                                //calss对象
                                Class clazz = null;
                                try {
                                    clazz = Class.forName(activityName);
                                    Intent intent = new Intent(H5JSActivity.this, clazz);
                                    if (id != null && !id.equals("undefined")) {
                                        // TODO: 2017/8/3 0003  参数为整形
                                        intent.putExtra("id", Integer.parseInt(id));
                                    }
                                    //标识H5页面是由读秒广告页打开，如果跳转到其他Activity，然后在显示main的
                                    //三个Fragment，则会出现Main没有启动报错的问题
                                    if (flag != null && flag.equals("ads")) {
                                        intent.putExtra("flag", "ads");
                                    }
                                    //标识是由H5页面跳转的，以便发广播结束此页面
                                    intent.putExtra("current", "h5");
                                    startActivity(intent);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case "1":
                                /////////二维码dialog///////
                                String shareTitle = uri.getQueryParameter("twoDimensionalUrl");
                                // TODO: 2017/8/4 0004

                                break;
                            case "2":
                                ////////////友盟分享////////
                                //权限
                                jurisdiction();
                                share(uri);
                                break;
                            case "4"://风险测评关闭web
                                setResult(0x0001);
                                finish();
                                break;
                            case "3":

                                ///////////跳转首页或者理财页////////////
                                String fragment = uri.getQueryParameter("fragment");
                                if (fragment.equals("440")) {
                                    //进入理财列表
                                    if (flag != null && flag.equals("ads")) {
                                        if (!PreventFastClickUtils.isFastClick()) {
                                            //由广告页进入H5,此时MainActivity还没有启动，先启动然后显示理财列表
                                            Intent intent = new Intent(H5JSActivity.this, MainActivity.class);
                                            intent.putExtra("flag", "ads");
                                            startActivity(intent);
                                        }
                                    } else {
                                        Intent manageIntent = new Intent();
                                        manageIntent.setAction("HomeFragmentBroadCast");
                                        manageIntent.putExtra("current", 1);
                                        sendBroadcast(manageIntent);
                                    }

                                    finish();
                                } else if (fragment.equals("550")) {
                                    //进入首页
                                    // TODO: 2017/8/3 0003
                                }
                                break;
                        }
                    }

                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);


            }
        });

        //获取标题
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView webView, String s) {

                //设置标题
                txtTitle.setText(s);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按返回键，网页可以后退
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
        unregisterReceiver(h5JSReceiver);
        // TODO: 2017/8/4 0004
    }

    /**
     * android调用js
     */
    private void AndroidDoJs() {

        //////////////////////Android调Js的方法////////////////////////////
        SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");

        //腾讯X5,版本已做了兼容
        webView.evaluateJavascript("javascript:getToken('" + token + "')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //// TODO: 2017/7/19 0019 处理JS结果
                Log.d("hsl", "onReceiveValue:----> " + value);
            }
        });

    }

    //区分风险测评入口
    private void Evaluate(String type){
        webView.evaluateJavascript("javascript:getIntoType('" + type + "')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //// TODO: 2017/7/19 0019 处理JS结果
                Log.d("hsl", "onReceiveValue:----> " + value);
            }
        });
    }

    private void jurisdiction() {
        if (ContextCompat.checkSelfPermission(H5JSActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(H5JSActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(H5JSActivity.this, "再次获取该权限需去设置打开权限许可", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(H5JSActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

    }


    /**
     * 首页广播
     */
    class H5JSReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getIntExtra("h5_js", 0)) {

                case 111:
                    //调用JS方法传Token
                    AndroidDoJs();
                    break;
                case 222:
                    finish();
                    break;

            }
        }
    }

    public void share(Uri uri) {
        String shareTitle = uri.getQueryParameter("shareTitle");
        String shareUrl = uri.getQueryParameter("shareUrl");
        String shareDescription = uri.getQueryParameter("shareDescription");

        if (shareTitle.equals("undefined") != true) {
            UMWeb web = new UMWeb(shareUrl);
            web.setTitle(shareTitle);//标题
            web.setDescription(shareDescription);//描述

            UMImage thumb = new UMImage(H5JSActivity.this, R.mipmap.share_logo);
            thumb.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            thumb.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
            thumb.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
            web.setThumb(thumb);  //缩略图
            new ShareAction(H5JSActivity.this).withMedia(web)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                    .setCallback(umShareListener).open();
        } else {
            Toast.makeText(H5JSActivity.this, shareTitle, Toast.LENGTH_SHORT).show();
        }
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.d("llk", "回调：" + platform);
            //分享开始的回调
        }

        @Override
        public void onResult(final SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(H5JSActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            });


        }

        @Override
        public void onError(final SHARE_MEDIA platform, Throwable t) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(H5JSActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                }
            });
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(final SHARE_MEDIA platform) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(H5JSActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * android调用js
     * 是否显示分享按钮
     */
    private void isShowShareButton() {

        webView.evaluateJavascript("javascript:andriodIsShow()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //// TODO: 2017/7/19 0019 处理JS结果
                Log.d("dl", "onReceiveValue:----> " + value);
                if (value != null
                        && !value.equals("undefined")
                        && value.equals("1")) {

                    relShare.setVisibility(View.VISIBLE);
                    relShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            webView.evaluateJavascript("javascript:appshare()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    // TODO: 2017/8/22 0022
                                }
                            });

                        }
                    });
                } else {
                    //隐藏
                    relShare.setVisibility(View.GONE);
                }
            }
        });

    }


}
