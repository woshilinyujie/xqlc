package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rongxun.xqlc.Adapters.CashRewardListAdapter;
import com.rongxun.xqlc.Beans.user.AwawdCashBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.Utils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class RewardActivity extends MyBaseActivity {

    LoadListView cashAwardListView;
    PullToRefreshLayout cashAwardSwipeLayout;
    LinearLayout cashAwardNothingImg;
    IconFontTextView black;
    private String TAG = "现金奖励";
    private CashRewardListAdapter myAdapter;
    private List<AwawdCashBean.CashListBean> awardList = new ArrayList<AwawdCashBean.CashListBean>();
    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private final int PAGENUB = 1;
    private int currentBottomPageIndex = 1;//已经加载的页数
    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/centerJL";
    private TextView explanation;
    private TextView allmoney;
    private LinearLayout ll;
    private ImageView iv;
    private TextView allText;
    private TextView popu_all;
    private TextView popu_putong;
    private TextView popu_yaoqing;
    private TextView popu_jiaxi;
    private LinearLayout popu_bg;
    private String keyHbType="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reward);

        initView();
        RequestForListData(basicUrl, 1, PAGESIZE, true);

        myAdapter = new CashRewardListAdapter(this, awardList, getLayoutInflater());
        cashAwardListView.setAdapter(myAdapter);
        cashAwardListView.setLoadMoreListener(new LoadListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //页数++
                currentBottomPageIndex++;
                //
                RequestForListData(basicUrl, currentBottomPageIndex, PAGESIZE, false);
            }
        });
        cashAwardSwipeLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentBottomPageIndex = 1;
                RequestForListData(basicUrl, 1, PAGESIZE, true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });




        //奖励说明
        explanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardActivity.this, ArticleActivity.class);
                intent.putExtra("id", "653");
                intent.putExtra("header", "奖励说明");
                startActivity(intent);
            }
        });

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        explanation = (TextView) findViewById(R.id.reward_explanation);
        cashAwardListView = (LoadListView) findViewById(R.id.cash_award_list_view);
        cashAwardListView.getRootView().setBackgroundColor(Color.parseColor("#f6f7fb"));
        cashAwardSwipeLayout = (PullToRefreshLayout) findViewById(R.id.cash_award_swipe_layout);
        cashAwardNothingImg = (LinearLayout) findViewById(R.id.cash_award_nothing_ll);
        black = (IconFontTextView) findViewById(R.id.award_black);

        allmoney = (TextView) findViewById(R.id.cash_award_all_money);
        ll = (LinearLayout) findViewById(R.id.cash_award_ll);
        iv = (ImageView) findViewById(R.id.cash_award_all_iv);
        allText = (TextView) findViewById(R.id.cash_award_all_text);

        View contentView = View.inflate(this, R.layout.reward_popu, null);
        popu_all = (TextView) contentView.findViewById(R.id.reward_popu_all);
        popu_putong = (TextView) contentView.findViewById(R.id.reward_popu_putong);
        popu_yaoqing = (TextView) contentView.findViewById(R.id.reward_popu_yaoqing);
        popu_jiaxi = (TextView) contentView.findViewById(R.id.reward_popu_jiaxi);



        final PopupWindow window = new PopupWindow(contentView, Utils.dip2px(this, 102), Utils.dip2px(this, 165), true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setFocusable(true);
        window.setBackgroundDrawable(new PaintDrawable());

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.showAsDropDown(ll, Utils.dip2px(RewardActivity.this, -60), Utils.dip2px(RewardActivity.this, 5));
            }
        });

        popu_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popu_all.setBackgroundColor(Color.parseColor("#fa5454"));
                popu_putong.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_yaoqing.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_jiaxi.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_all.setTextColor(Color.parseColor("#ffffff"));
                popu_putong.setTextColor(Color.parseColor("#333333"));
                popu_yaoqing.setTextColor(Color.parseColor("#333333"));
                popu_jiaxi.setTextColor(Color.parseColor("#333333"));
                allText.setText("全部");
                currentBottomPageIndex=1;
                keyHbType="0";
                awardList.clear();
                RequestForListData(basicUrl, PAGENUB, PAGESIZE, true);
                window.dismiss();
            }
        });
        popu_putong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popu_all.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_putong.setBackgroundColor(Color.parseColor("#fa5454"));
                popu_yaoqing.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_jiaxi.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_all.setTextColor(Color.parseColor("#333333"));
                popu_putong.setTextColor(Color.parseColor("#ffffff"));
                popu_yaoqing.setTextColor(Color.parseColor("#333333"));
                popu_jiaxi.setTextColor(Color.parseColor("#333333"));
                allText.setText("普通奖励");
                currentBottomPageIndex=1;
                keyHbType="2";
                awardList.clear();
                RequestForListData(basicUrl, PAGENUB, PAGESIZE, true);
                window.dismiss();
            }
        });
        popu_yaoqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popu_all.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_putong.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_yaoqing.setBackgroundColor(Color.parseColor("#fa5454"));
                popu_jiaxi.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_all.setTextColor(Color.parseColor("#333333"));
                popu_putong.setTextColor(Color.parseColor("#333333"));
                popu_yaoqing.setTextColor(Color.parseColor("#ffffff"));
                popu_jiaxi.setTextColor(Color.parseColor("#333333"));
                allText.setText("邀请奖励");
                currentBottomPageIndex=1;
                keyHbType="1";
                awardList.clear();
                RequestForListData(basicUrl, PAGENUB, PAGESIZE, true);
                window.dismiss();
            }
        });
        popu_jiaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popu_all.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_putong.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_yaoqing.setBackgroundColor(Color.parseColor("#ffffff"));
                popu_jiaxi.setBackgroundColor(Color.parseColor("#fa5454"));
                popu_all.setTextColor(Color.parseColor("#333333"));
                popu_putong.setTextColor(Color.parseColor("#333333"));
                popu_yaoqing.setTextColor(Color.parseColor("#333333"));
                popu_jiaxi.setTextColor(Color.parseColor("#ffffff"));
                allText.setText("加息卷奖励");
                currentBottomPageIndex=1;
                keyHbType="3";
                awardList.clear();
                RequestForListData(basicUrl, PAGENUB, PAGESIZE, true);
                window.dismiss();
            }
        });

    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean isRefreshing) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("keyHbType", "0")
                .addParams("pager.pageNumber", pageNumber + "")
                .addParams("pager.pageSize", pageSize + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (!awardList.isEmpty()) {
                            awardList.clear();
                            myAdapter.notifyDataSetChanged();
                        }
                        cashAwardListView.setVisibility(View.GONE);
                        cashAwardNothingImg.setVisibility(View.VISIBLE);

                        if (isRefreshing) {
                            cashAwardSwipeLayout.delayRefreshFinish(PullToRefreshLayout.FAIL, 800);
                        } else {
                            //加载失败pager-1
                            currentBottomPageIndex--;
                            //加载失败
                            cashAwardListView.loadFail();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.i(TAG, s);
                        final AwawdCashBean resultBean = JSON.parseObject(s, AwawdCashBean.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            allmoney.setText(resultBean.getSumTzMoney()+"");
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            if (isRefreshing) {
                                cashAwardSwipeLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                if (!awardList.isEmpty()) {
                                    awardList.clear();
                                }
                                if (resultBean.getCashList() != null) {
                                    if (resultBean.getCashList().size() > 0) {
                                        cashAwardListView.setVisibility(View.VISIBLE);
                                        cashAwardNothingImg.setVisibility(View.GONE);
                                    } else {
                                        cashAwardListView.setVisibility(View.GONE);
                                        cashAwardNothingImg.setVisibility(View.VISIBLE);
                                    }

                                    awardList.addAll(resultBean.getCashList());
                                    cashAwardListView.loadFinish(currentBottomPageIndex, totalPageCount);
                                    myAdapter.notifyDataSetChanged();
                                }
                                else{
                                    cashAwardListView.setVisibility(View.GONE);
                                    cashAwardNothingImg.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (resultBean.getCashList() != null)
                                    awardList.addAll(resultBean.getCashList());
                                cashAwardListView.loadFinish(currentBottomPageIndex, totalPageCount);
                                myAdapter.notifyDataSetChanged();
                            }
                        } else {
                            cashAwardListView.setVisibility(View.GONE);
                            cashAwardNothingImg.setVisibility(View.VISIBLE);
                            Toast.makeText(RewardActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("LoginContentBroadCast"));
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
