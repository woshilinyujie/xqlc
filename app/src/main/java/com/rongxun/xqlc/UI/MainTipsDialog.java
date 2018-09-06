package com.rongxun.xqlc.UI;

import android.app.Dialog;
import android.content.Context;
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
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.Utils;


/**
 * Created by linyujie on 16/7/18.
 */
public class MainTipsDialog extends Dialog {

    private Context context;
    private ImageView x;
    private ImageView iv;
    private int with;
    private int height;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try{
                show();

            }catch (Exception e){

            }
        }
    };

    public MainTipsDialog(Context context) {
        this(context, R.style.ActionSheetDialogStyle);
    }

    public MainTipsDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initListenet();
    }

    private void initListenet() {

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.imageOnclick();
                }
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
        params1.leftMargin = (int) (with / 1.25);
        x.setLayoutParams(params1);
        setContentView(view);

    }

    public void setImage(String imageUrl) {

        Transformation transformation = new Transformation() {

            @Override
            public Resource transform(Resource resource, int outWidth, int outHeight) {
                if (outHeight > outWidth) {
                    int p = outHeight / outWidth;
                    RelativeLayout.LayoutParams params =
                            new RelativeLayout.LayoutParams(with,
                                    with * p);
                    params.leftMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 10);
                    params.rightMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 10);
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
                    params.leftMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 10);
                    params.rightMargin = Utils.dip2px(context, Utils.px2dip(context, with) / 10);
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

        try{
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                    .skipMemoryCache(true)//跳过内存缓存
                    .transform(transformation)
                    .into(iv);
        }catch (Exception e){

        }
    }


    /**
     * 接口
     */
    private ImageOnClickListener listener;

    public void setListener(ImageOnClickListener listener) {
        this.listener = listener;
    }

    public interface ImageOnClickListener {

        void imageOnclick();
    }


}
