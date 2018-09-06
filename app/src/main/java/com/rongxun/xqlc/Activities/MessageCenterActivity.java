package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.MessageBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.socialize.utils.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;


/**
 * Created by oguig on 2017/7/25.
 * 消息中心
 */

public class MessageCenterActivity extends MyBaseActivity implements View.OnClickListener {
    private IconFontTextView msg_back;
    private TextView tv_communique_title, tv_communique_time, tv_platform_dynamic_title, tv_platform_dynamic_time;
    private RelativeLayout rl_communique, rl_platform_dynamic;
    private MessageBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        CustomApplication.addActivity(this);
        initView();
        initData();

    }


    private void initView() {
        msg_back = (IconFontTextView) findViewById(R.id.msg_back);
        //公告标题
        tv_communique_title = (TextView) findViewById(R.id.tv_communique_title);
        //公告时间
        tv_communique_time = (TextView) findViewById(R.id.tv_communique_time);
        //动态标题
        tv_platform_dynamic_title = (TextView) findViewById(R.id.tv_platform_dynamic_title);
        //动态时间
        tv_platform_dynamic_time = (TextView) findViewById(R.id.tv_platform_dynamic_time);
        rl_communique = (RelativeLayout) findViewById(R.id.rl_communique);
        rl_platform_dynamic = (RelativeLayout) findViewById(R.id.rl_platform_dynamic);


        msg_back.setOnClickListener(this);
        rl_communique.setOnClickListener(this);
        rl_platform_dynamic.setOnClickListener(this);
    }

    private void initData() {
        String url= AppConstants.URL_SUFFIX +"/rest/messageCenter";
        OkHttpUtils.post()
                .url(url)
                .addParams("deviceType","android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                    String s=response.toString();
                        Log.v("dsf",s);
                        bean=new Gson().fromJson(s, MessageBean.class);
                        if(bean.getRcd().equals("R0001")){
                            tv_communique_title.setText(bean.getNoticeTitle());
                            long time=Long.parseLong(bean.getNoticeModifyTime());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            tv_communique_time.setText(sdf.format(new Date(time)));
                            if(bean.getDynamicTitle()!=null){
                                tv_platform_dynamic_title.setText(bean.getDynamicTitle());
                            }
                            if(bean.getDynamicModifyTime()!=null){
                                long time1=Long.parseLong(bean.getDynamicModifyTime());
                                tv_platform_dynamic_time.setText(sdf.format(new Date(time1)));
                            }
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msg_back:
                finish();
                break;
            case R.id.rl_communique:
                //跳转至官方公告
                startActivity(new Intent(this, CommuniqueActivity.class));

                break;
            case R.id.rl_platform_dynamic:
                //跳转至平台动态
                startActivity(new Intent(MessageCenterActivity.this, PlatformDynamicActivity.class));
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
