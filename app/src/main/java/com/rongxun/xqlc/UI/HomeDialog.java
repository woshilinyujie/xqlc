package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.rongxun.xqlc.Activities.ArticleActivity;
import com.rongxun.xqlc.Activities.HuoDongDetailActivity;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.Utils;

/**
 * Created by linyujie on 16/7/18.
 */
public class HomeDialog extends Dialog {
    private Context context;
    private ImageView x;
    private ImageView iv;
    private int with;
    private int height;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            show();
        }
    };

    public HomeDialog(Context context) {
        this(context, R.style.ActionSheetDialogStyle);
    }

    public HomeDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initData();
        initListenet();
    }

    private void initListenet() {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(
                        "Setting", Context.MODE_PRIVATE);
                String currentUrl = sharedPreferences.getString("typeTarget", "");
                sharedPreferences.edit().putBoolean("handled", true).commit();
                //进入活动或者文章页面
                Intent intent = new Intent();

                if (currentUrl.contains("article")) {
                    intent.setClass(context, ArticleActivity.class);

                    int startIndex = currentUrl.lastIndexOf("/");
                    String lastHalf = currentUrl.substring(startIndex + 1, currentUrl.length());
                    System.out.println("滚公试图--" + lastHalf);
                    String articleId = lastHalf.substring(0, lastHalf.length() - 4);
                    System.out.println("滚公试图Id--" + lastHalf);
                    intent.putExtra("header", "文章详情");
                    intent.putExtra("id", articleId);

                } else if (currentUrl.contains("activity")) {
                    intent.setClass(context, HuoDongDetailActivity.class);

                    int startIndex = currentUrl.lastIndexOf("=");
                    String articleId = currentUrl.substring(startIndex + 1, currentUrl.length());
                    System.out.println("活动Id--" + articleId);

                    intent.putExtra("header", "活动详情");
                    intent.putExtra("id", articleId);
                } else {
                    return;
                }
                context.startActivity(intent);
                dismiss();
            }
        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void initView() {
        setCancelable(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        with = defaultDisplay.getWidth();
        height = defaultDisplay.getHeight();
        View view = View.inflate(context, R.layout.home_dialog, null);

        x = (ImageView) view.findViewById(R.id.home_dialog_x);
        iv = (ImageView) view.findViewById(R.id.home_dialog_iv);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(Utils.dip2px(context, 25), Utils.dip2px(context, 25));
        params1.leftMargin = (int) (with / 1.3);
        x.setLayoutParams(params1);
        setContentView(view);

    }

    private void initData() {

    }


    public void setImage(String imageUrl) {
        String imageUrl1 =  imageUrl;
        Transformation transformation = new Transformation() {

            @Override
            public Resource transform(Resource resource, int outWidth, int outHeight) {
                if (outHeight > outWidth) {
                    int p = outHeight / outWidth;
                    RelativeLayout.LayoutParams params =
                            new RelativeLayout.LayoutParams(with,
                                    with * p);
                    params.leftMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 12);
                    params.rightMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 12);
                    params.topMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 20);

                    try {
                        iv.setLayoutParams(params);
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        Log.e("主页弹窗", e.toString());
                    }
                } else {
                    RelativeLayout.LayoutParams params =
                            new RelativeLayout.LayoutParams(with,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 12);
                    params.rightMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 12);
                    params.topMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 20);
                    try {
                        iv.setLayoutParams(params);
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        Log.e("主页弹窗", e.toString());
                    }
                }
                return resource;
            }

            @Override
            public String getId() {
                return "transformation" + " desiredWidth";
            }

        };

        Glide.with(context)
                .load(imageUrl1).asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .transform(transformation)
                .into(iv);

    }
}
