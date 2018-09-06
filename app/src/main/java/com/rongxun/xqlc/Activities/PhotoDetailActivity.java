package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Borrow.MaterialShowBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetailActivity extends AppCompatActivity {

    private ViewPager vp;
    private int currentPag;
    private int allPag;
    private List<MaterialShowBean> data;
    private List<String> img;

    private TextView txtName;
    private TextView txtCurrentPag;
    private TextView txtAllPag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_photo_detail);
        initData();
        initView();

    }

    private void initData() {


        Intent intent = getIntent();
        int position = intent.getIntExtra("p", 0);
        data = (List<MaterialShowBean>) intent.getSerializableExtra("url");

        currentPag = position + 1;
        allPag = data.size();

        img = new ArrayList<>();
        for (int i = 0, len = data.size(); i < len; i++) {
            img.add( data.get(i).getImageUrl());
        }


    }

    private void initView() {

        vp = (ViewPager) findViewById(R.id.vp_photo);
        txtName = (TextView) findViewById(R.id.txt_photo_name);
        txtCurrentPag = (TextView) findViewById(R.id.txt_current_pag);
        txtAllPag = (TextView) findViewById(R.id.txt_all_pag);

        txtName.setText(data.get(currentPag - 1).getTitle());
        txtCurrentPag.setText("" + currentPag);
        txtAllPag.setText("" + allPag);
        SwitchPagerAdapter pagerAdapter = new SwitchPagerAdapter(img);
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(currentPag - 1);//设置显示当前页

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int rel = position + 1;
                txtCurrentPag.setText("" + rel);
                txtName.setText(data.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    class SwitchPagerAdapter extends PagerAdapter {
        private List<String> imgs;

        public SwitchPagerAdapter(List<String> imgs) {
            this.imgs = imgs;
        }

        @Override
        public int getCount() {
            return imgs != null ? imgs.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PhotoView view = new PhotoView(PhotoDetailActivity.this);
            view.enable();
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);

            Glide.with(PhotoDetailActivity.this)
                    .load(imgs.get(position))
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
