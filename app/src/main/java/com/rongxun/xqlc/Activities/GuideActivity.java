package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.PrefUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * 引导页
 */
public class GuideActivity extends MyBaseActivity {

    private Button btnGo;
    private ViewPager vp;
    private List<View> views;//引导页图片
    private CircleIndicator indicator;//指示器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //設為全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);

        //记录引导页
        PrefUtils.putBoolean(this, "isFirst", false);
        initData();
        initView();
        listener();

    }


    private void listener() {

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == views.size() - 1) {
                    indicator.setVisibility(View.GONE);
                } else {
                    indicator.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //立即体验
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是不是进入广告页
                String second = PrefUtils.getString(GuideActivity.this, "second", "");
                if (second.equals("")) {
                    Intent main = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(main);
                } else {
                    Intent ads = new Intent(GuideActivity.this, AdsActivity.class);
                    startActivity(ads);
                }

                finish();

            }
        });
    }

    private void initData() {

        views = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(this);
        ImageView guide1 = (ImageView) inflater.inflate(R.layout.guide_1, null);
        ImageView guide2 = (ImageView) inflater.inflate(R.layout.guide_2, null);
        ImageView guide3 = (ImageView) inflater.inflate(R.layout.guide_3, null);
        View guide4 = inflater.inflate(R.layout.guide_4, null);
        ImageView guide4iv = (ImageView) guide4.findViewById(R.id.guide_iv);
        scaleImage(R.mipmap.guide1, guide1);
        scaleImage(R.mipmap.guide2, guide2);
        scaleImage(R.mipmap.guide3, guide3);
        scaleImage(R.mipmap.guide4, guide4iv);
        btnGo = (Button) guide4.findViewById(R.id.guide_btn_go);
        views.add(guide1);
        views.add(guide2);
        views.add(guide3);
        views.add(guide4);

    }

    private void initView() {

        indicator = (CircleIndicator) findViewById(R.id.guide_indicator);
        vp = (ViewPager) findViewById(R.id.guide_vp);
        vp.setAdapter(new GuideAdapter());
        indicator.setViewPager(vp);
    }


    /**
     * vp适配器
     */
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = views.get(position);
            container.addView(view);
            return view;
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


    public void scaleImage(int id, ImageView iv) {
        //缩放图片和设置
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //只解析大图片的宽高信息，不用耗费内存解析图片像素
//        opt.inJustDecodeBounds = true;
        //传入一个opt对象
//        BitmapFactory.decodeResource(getResources(),id, opt);
        //拿到大图片的宽高
        int imageWidth = opt.outWidth;
        int imageHeight = opt.outHeight;
        Display dp = getWindowManager().getDefaultDisplay();
        //获取屏幕宽高
        int screenWidth = dp.getWidth();
        int screenHeight = dp.getHeight();

        //计算缩放比例
        int scale = 1;
//        int scaleWidth = imageWidth / screenWidth;
//        int scaleHeight = imageHeight / screenHeight;
//        if (scaleHeight >= scaleWidth && scaleHeight >= 1) {
//            scale = scaleHeight;
//        } else if (scaleWidth >= scaleHeight && scaleWidth >= 1) {
//            scale = scaleWidth;
//        }
        //设置缩放比例
        opt.inSampleSize = scale;
        opt.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeResource(getResources(),id, opt);
        Drawable drawable =new BitmapDrawable(bm);
        iv.setBackgroundDrawable(drawable);

    }

}
