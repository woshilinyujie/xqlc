package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.ServiscUtil;
import com.umeng.analytics.MobclickAgent;

public class PlaySuccessActivity extends MyBaseActivity {

    private TextView money;
    private Button sure;
    private TextView text;
    private Intent intent;
    private boolean isSuccess;
    private ImageView iv;
    private TextView order;
    private ImageView service;
    private LinearLayout success_ll;
    private Button watch;
    private Button inverst;
    private LinearLayout success_ll1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_play_success);
        intent = getIntent();
        initView();
        initDate();
    }

    private void initView() {
        order = (TextView) findViewById(R.id.play_success_order);
        money = (TextView) findViewById(R.id.play_success_money);
        sure = (Button) findViewById(R.id.play_success_sure);
        text = (TextView) findViewById(R.id.play_success_text);
        iv = (ImageView) findViewById(R.id.play_success_iv);
        service = (ImageView) findViewById(R.id.play_success_service);
        success_ll = (LinearLayout) findViewById(R.id.play_success_ll);
        success_ll1 = (LinearLayout) findViewById(R.id.play_success_ll);
        watch = (Button) findViewById(R.id.play_success_watch);
        inverst = (Button) findViewById(R.id.play_success_inverst);
    }

    private void initDate() {
        String orderString = intent.getStringExtra("order");
        String moneyString = intent.getStringExtra("money");
        String messageString = intent.getStringExtra("message");
        isSuccess = intent.getBooleanExtra("isSuccess", false);
        if (isSuccess) {

            money.setText(moneyString);
            iv.setBackgroundResource(R.mipmap.invest_play_success);
        } else {
            success_ll1.setVisibility(View.GONE);
            success_ll.setVisibility(View.GONE);
            sure.setVisibility(View.VISIBLE);
            money.setText(messageString);
            iv.setBackgroundResource(R.mipmap.invest_defeated);
        }
        text.setText(messageString);
        order.setText(orderString);


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        service.setOnClickListener(new View.OnClickListener() {

            private ServiscUtil serviscUtil;

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                serviscUtil = new ServiscUtil(PlaySuccessActivity.this,money);
                serviscUtil.initPopuptWindowExit();
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("ChargeFragmentBroadCase");
                intent.putExtra("finish","finish");
                sendBroadcast(intent);
                finish();
            }
        });

        inverst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("ChargeFragmentBroadCase");
                intent.putExtra("finish","finish");
                sendBroadcast(intent);
                Intent intent1=new Intent("MainActivityBroadReceiver");
                intent1.putExtra("current",1);
                sendBroadcast(intent1);
                finish();
            }
        });

        Intent againIntent = new Intent("LoginContentFragmentBroadCast");
        againIntent.putExtra("refresh","refresh");
        sendBroadcast(againIntent);
        Intent cash = new Intent("CashFragmentBroadCase");
        sendBroadcast(cash);
        Intent invest = new Intent("ProjectInvestActivityBroadCast");
        sendBroadcast(invest);

    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
