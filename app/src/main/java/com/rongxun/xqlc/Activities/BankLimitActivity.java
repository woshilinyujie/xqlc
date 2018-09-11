package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rongxun.xqlc.Adapters.SelectBankAdapterRc;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

public class BankLimitActivity extends AppCompatActivity {

    private TextView relBack;
    private RecyclerView rv;

    private BankList banklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank);
        initView();
    }

    private void initView() {

        relBack = (TextView) findViewById(R.id.select_bank_back);
        rv = (RecyclerView) findViewById(R.id.select_bank_rv);


        //创建水平布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //设置布局管理器
        rv.setLayoutManager(layoutManager);
        //垂直分割
        rv.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(this)
                .color(Color.parseColor("#f5f5f5"))
                .size(2)
                .build()
        );
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能

        relBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String url = AppConstants.URL_SUFFIX + "/rest/bankTo";
        RequestForBankData(url);
    }


    /**
     * 获取银行卡数据
     *
     * @param basicUrl
     */
    public void RequestForBankData(String basicUrl) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        final BankList resultBean = JSON.parseObject(s, BankList.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            banklist = resultBean;
                            SelectBankAdapterRc adapter = new SelectBankAdapterRc(BankLimitActivity.this, banklist);
                            rv.setHasFixedSize(true);
                            rv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(BankLimitActivity.this, resultBean.getRmg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


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
    }
}
