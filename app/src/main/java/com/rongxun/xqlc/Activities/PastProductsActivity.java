package com.rongxun.xqlc.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Fragments.PastProductsFragment;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.slidingtab.SlidingTabLayout;

import java.util.ArrayList;

public class PastProductsActivity extends AppCompatActivity {

    private RelativeLayout relBack;
    private SlidingTabLayout tabLayout;
    private ViewPager vp;
    public static final String MUJI = "0";//募集中
    public static final String SHOUQIN = "1";//已售罄

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_past_products);

        initView();
        //返回
        relBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {

        relBack = (RelativeLayout) findViewById(R.id.past_product_rel_back);
        tabLayout = (SlidingTabLayout) findViewById(R.id.past_product_sliding_tab);
        vp = (ViewPager) findViewById(R.id.past_product_vp);
        TextView title= (TextView) findViewById(R.id.past_product_txt_title);
        String rongXunFlg = getIntent().getStringExtra("rongXunFlg");
        switch (rongXunFlg){
            case "0":
                title.setText("精选专区");
                break;
            case "1":
                title.setText("尊享专区");
                break;
            case "2":
                title.setText("新手专区");
                break;
        }
        //设置tab
        String[] titles = new String[]{"募集中", "已售罄"};
        //添加
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(PastProductsFragment.newInstance(MUJI,rongXunFlg));
        fragments.add(PastProductsFragment.newInstance(SHOUQIN,rongXunFlg));
        //tab与vp关联
        tabLayout.setViewPager(vp, titles, this, fragments);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
