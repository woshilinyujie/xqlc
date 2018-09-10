package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.userInfo.RepaBeen;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.MyListView;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.KeepTwoUtil;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

public class RepayMentActivity extends MyBaseActivity {


    private IconFontTextView repaymentToolbarBack;
    private Intent intent;
    private int id;//号码
    private String name;
    private TextView textName;
    private TextView money;
    private TextView profit;
    private TextView time;
    private TextView limit;
    private Gson gson = new Gson();
    private String url;
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (repaBeen != null) {
                url = repaBeen.getLoanAgreementUrl();
                textName.setText(name);

                time.setText(repaBeen.getTimeLimit());
                money.setText(KeepTwoUtil.keep2Decimal(Double.parseDouble(repaBeen.getAccount()))+"元");
                profit.setText(KeepTwoUtil.keep2Decimal(Double.parseDouble(repaBeen.getInterest()))+"元");
                ment_year.setText(KeepTwoUtil.keep2Decimal(repaBeen.getApr())+"%");
                if(TextUtils.isEmpty(repaBeen.getRepayTime())&& repaBeen.getRepayTimeShow()!=null){
                    limit.setText(repaBeen.getRepayTimeShow());
                }else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    long backtime=Long.parseLong(repaBeen.getRepayTime());
                    limit.setText(sdf.format(new Date(backtime)));
                }
            }
        }
    };
    private RepaBeen repaBeen;
    private TextView ment_year;
    private Button detail;
    private Button negotiate;
    private int projectId;
    private String stateFlag;
    private ImageView stateIv;
    private TextView tv;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_repay_ment);
        initView();
        initNetWork();
        initListener();
    }

    private void initListener() {
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RepayMentActivity.this, ManageDetailActivity.class);
                intent.putExtra("id", projectId);
                RepayMentActivity.this.startActivity(intent);
            }
        });

        negotiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repaBeen != null) {
                    if (repaBeen.getJumpStatus().equals("0")) {
                        Toast.makeText(RepayMentActivity.this, repaBeen.getUserRepDetailStringX(), Toast.LENGTH_LONG).show();
                    } else if (repaBeen.getJumpStatus().equals("1")) {
                        Intent intent = new Intent(RepayMentActivity.this, MoneyNegotiateActivity.class);
                        intent.putExtra("itemId", id);
                        intent.putExtra("projectId", projectId);
                        if (url != null) {
                            intent.putExtra("LoanAgreementUrl", url);
                        }
                        startActivity(intent);
                    } else if (repaBeen.getJumpStatus().equals("2")) {
//                        //跳列表
                        Intent intent = new Intent(RepayMentActivity.this, BorrowActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                }
            }
        });
    }


    private void initView() {
        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        projectId = intent.getIntExtra("projectId", 0);
        name = intent.getStringExtra("name");
        stateFlag =intent.getStringExtra("state");
        repaymentToolbarBack = (IconFontTextView) findViewById(R.id.repayment_toolbar_back);
        textName = (TextView) findViewById(R.id.repay_ment_name);
        money = (TextView) findViewById(R.id.repay_ment_money);
        profit = (TextView) findViewById(R.id.repay_ment_profit);
        time = (TextView) findViewById(R.id.repay_ment_time);
        stateIv = (ImageView) findViewById(R.id.repay_ment_state);
        limit = (TextView) findViewById(R.id.repay_ment_time_limit);
        tv = (TextView) findViewById(R.id.repay_ment_tv);
        ment_year = (TextView) findViewById(R.id.repay_ment_year);
        tv1 = (TextView) findViewById(R.id.repay_ment_tv1);
        detail = (Button) findViewById(R.id.repay_ment_detail);
        negotiate = (Button) findViewById(R.id.repay_ment_negotiate);
        initToolBar();

        if(stateFlag.equals("1") ||stateFlag.equals("5") ){
            stateIv.setBackgroundResource(R.mipmap.mujizhong);
            tv.setText("到期回款");
            tv1.setText("投资进度");
        }else if(stateFlag.equals("3")){ //持有
            stateIv.setBackgroundResource(R.mipmap.chiyouzhong);
            tv.setText("待收回款");
        }else if(stateFlag.equals("7")){
            stateIv.setBackgroundResource(R.mipmap.yihuikuang);
            tv.setText("已回款");
        }//已回
        stateIv.measure(0,0);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (stateIv.getMeasuredWidth()/8));
        stateIv.setLayoutParams(params);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    public void initToolBar() {
        repaymentToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

    }


    private void initNetWork() {
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/userCenterBorrowRepList";
        RequestForListData(basicUrl);
    }

    public void RequestForListData(String basicUrl) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("id", id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String s, int id) {
                        String ss=s.toString();
                        Log.i("aa",ss);
                        repaBeen = gson.fromJson(s, RepaBeen.class);
                        if (repaBeen.getRcd().equals("R0001")) {
                            hander.sendEmptyMessage(0);
                        } else {
                            Toast.makeText(RepayMentActivity.this, repaBeen.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }


}
