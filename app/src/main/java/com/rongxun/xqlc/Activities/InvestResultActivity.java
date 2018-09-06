package com.rongxun.xqlc.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InvestResultActivity extends MyBaseActivity {

    public static  int INVESTRESULTCODE=10010;
    private Button again;
    private TextView Tmoney;
    private TextView Tproject;
    private Button watch;
    private Intent intent;
    private int money;
    private String project;
    private IconFontTextView toolBarBack;
    private String userId;
    private TextView excepts;
    private TextView result_time;
    private String exceptS;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_invest_result);
        initView();
        initListener();
        initDate();
    }

    private void initView() {
        userId = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        intent = getIntent();
        exceptS = intent.getStringExtra("exceptS");
        time = intent.getStringExtra("time");
        money = intent.getIntExtra("money", 0);
        project = intent.getStringExtra("project");
        again = (Button) findViewById(R.id.invest_result_again);
        Tmoney = (TextView) findViewById(R.id.invest_result_money);
        Tproject = (TextView) findViewById(R.id.invest_result_project);
        toolBarBack = (IconFontTextView) findViewById(R.id.project_invest_toolbar_back);
        watch = (Button) findViewById(R.id.invest_result_watch);
        excepts = (TextView) findViewById(R.id.invest_result_excepts);
        result_time = (TextView) findViewById(R.id.invest_result_time);
    }

    private void initListener() {
        toolBarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent againIntent=new Intent("HomeFragmentBroadCast");
                againIntent.putExtra("current",1);
                sendBroadcast(againIntent);
                finish();
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InvestResultActivity.this, InvestRecordActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initDate() {
        Tmoney.setText(money+"元");
        Tproject.setText(project);
        excepts.setText(exceptS+"元");
        result_time.setText(time);

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
