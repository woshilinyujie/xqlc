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
    private TextView hb_play;
    private TextView money;
    private TextView profit;
    private TextView state;
    private TextView time;
    private TextView limit;
    private Gson gson = new Gson();
    private String url;
    private TextView text;
    private TextView textCount;
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (repaBeen != null) {
                url = repaBeen.getLoanAgreementUrl();
                textName.setText(name);
                if (!TextUtils.isEmpty(repaBeen.getHongbaoAmount())) {
                    hb_play.setText(KeepTwoUtil.keep2Decimal(Double.parseDouble(repaBeen.getHongbaoAmount()))+"元");
                }
                if (repaBeen.getBorrowStatus() == 3) {
                    state.setText(repaBeen.getBorrowStatusVal());
                } else {
                    state.setText(repaBeen.getBorrowStatusVal());
                }
                if(repaBeen.getJumpStatus().equals("0") &&repaBeen.getUserRepDetailList().size()==0){
                    listView.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    text.setText(repaBeen.getUserRepDetailString());
                }

                if(repaBeen.getJumpStatus().equals("2") &&repaBeen.getUserRepDetailList().size()==0){
                    listView.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    text.setText(repaBeen.getUserRepDetailString());
                }

                if(repaBeen.getUserRepDetailList().size()>0){
                    textCount.setText("("+repaBeen.getUserRepDetailList().size()+")");
                }else{
                    textCount.setVisibility(View.GONE);
                }
                time.setText(repaBeen.getCreateTime());
                money.setText(KeepTwoUtil.keep2Decimal(Double.parseDouble(repaBeen.getAccount()))+"元");
                profit.setText(KeepTwoUtil.keep2Decimal(Double.parseDouble(repaBeen.getInterest()))+"元");
                ment_year.setText(KeepTwoUtil.keep2Decimal(repaBeen.getApr())+"%");
                listView.setAdapter(new RepayMentAdapter());
                if(repaBeen.getCouponInterest().equals("0")){
                    repay_ment_rl.setVisibility(View.GONE);
                }else{
                    jiaxi.setText(repaBeen.getCouponInterest()+"");
                }
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
    private MyListView listView;
    private Button detail;
    private Button negotiate;
    private int projectId;
    private RelativeLayout repay_ment_rl;
    private TextView jiaxi;

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
        repaymentToolbarBack = (IconFontTextView) findViewById(R.id.repayment_toolbar_back);
        textName = (TextView) findViewById(R.id.repay_ment_name);
        hb_play = (TextView) findViewById(R.id.repay_ment_hb_play);
        money = (TextView) findViewById(R.id.repay_ment_money);
        profit = (TextView) findViewById(R.id.repay_ment_profit);
        state = (TextView) findViewById(R.id.repay_ment_state);
        time = (TextView) findViewById(R.id.repay_ment_time);
        text = (TextView) findViewById(R.id.repay_ment_text);
        textCount = (TextView) findViewById(R.id.textView10);
        limit = (TextView) findViewById(R.id.repay_ment_time_limit);
        ment_year = (TextView) findViewById(R.id.repay_ment_year);
        listView = (MyListView) findViewById(R.id.repay_ment_list);
        detail = (Button) findViewById(R.id.repay_ment_detail);
        negotiate = (Button) findViewById(R.id.repay_ment_negotiate);
        repay_ment_rl = (RelativeLayout) findViewById(R.id.repay_ment_rl);
        jiaxi = (TextView) findViewById(R.id.repay_ment_jiaxi);
        initToolBar();
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


    class RepayMentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return repaBeen.getUserRepDetailList().size();
        }

        @Override
        public Object getItem(int position) {
            return repaBeen.getUserRepDetailList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RepaBeen.UserRepDetailListBean userRepDetailListBean = repaBeen.getUserRepDetailList().get(position);
            convertView = View.inflate(RepayMentActivity.this, R.layout.repay_ment_item, null);
            TextView time = (TextView) convertView.findViewById(R.id.repay_ment_item_time);
            TextView principal = (TextView) convertView.findViewById(R.id.repay_ment_item_principal);
            TextView interest = (TextView) convertView.findViewById(R.id.repay_ment_item_interest);
            time.setText(userRepDetailListBean.getRepaymentDate());
            interest.setText(userRepDetailListBean.getWaitInterest());
            principal.setText(KeepTwoUtil.keep2Decimal(Double.parseDouble(userRepDetailListBean.getWaitAccount())));
            if(!userRepDetailListBean.getWaitCouponInterest().equals("0")){
                interest.setText(userRepDetailListBean.getWaitInterest()+"+"+userRepDetailListBean.getWaitCouponInterest());
            }else{
                interest.setText(userRepDetailListBean.getWaitInterest());
            }
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }


}
