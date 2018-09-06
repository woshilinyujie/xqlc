package com.rongxun.xqlc.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.bumptech.glide.Glide;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.SmoothImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class EnlargeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
        int mPosition = getIntent().getIntExtra("position", 0);
        int mLocationX = getIntent().getIntExtra("locationX", 0);
        int mLocationY = getIntent().getIntExtra("locationY", 0);
        int mWidth = getIntent().getIntExtra("width", 0);
        int mHeight = getIntent().getIntExtra("height", 0);

        SmoothImageView imageView = new SmoothImageView(this);
        CustomApplication.addActivity(this);
        setContentView(imageView);
        Glide.with(this).load(mDatas.get(mPosition)).into(imageView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                finish();
                break;
        }
        return super.onTouchEvent(event);
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
