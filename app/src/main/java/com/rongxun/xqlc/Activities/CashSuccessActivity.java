package com.rongxun.xqlc.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.R;


public class CashSuccessActivity extends MyBaseActivity implements View.OnClickListener {

    private TextView back;
    private ImageView iv;
    private TextView money;
    private TextView name;
    private Button button;
    private String icon;
    private String moneyS;
    private String bankname;
    private String cardNo;
    private TextView cash_money;
    private TextView arrive_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_cash_success);
        initView();
    }

    private void initView() {
        moneyS = getIntent().getStringExtra("money");
        icon = getIntent().getStringExtra("icon");
        cardNo = getIntent().getStringExtra("CardNo");
        bankname  = getIntent().getStringExtra("BankName");
        back = (TextView) findViewById(R.id.cash_success_bank_back);
        iv = (ImageView) findViewById(R.id.cash_success_bank_iv);
        money = (TextView) findViewById(R.id.cash_success_bank_money);
        name = (TextView) findViewById(R.id.cash_success_bank_name);
        button = (Button) findViewById(R.id.cash_success_bank_button);
        cash_money = (TextView) findViewById(R.id.cash_success_bank_cash_money);
        arrive_money = (TextView) findViewById(R.id.cash_success_bank_arrive_money);
        back.setOnClickListener(this);
        button.setOnClickListener(this);
        money.setText(moneyS+"元");
        cash_money.setText(getIntent().getStringExtra("cashMoney")+"元");
        arrive_money.setText(getIntent().getStringExtra("ArriveMoney")+"元");
        Glide.with(this).load(getIntent().getStringExtra("iconUrl")).into(iv);
        name.setText(bankname + "（尾号" + cardNo.substring(cardNo.length() - 4, cardNo.length()) + "）");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cash_success_bank_back:
                finish();
                break;
            case R.id.cash_success_bank_button:
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        CustomApplication.removeActivity(this);
        super.onDestroy();
    }
}
