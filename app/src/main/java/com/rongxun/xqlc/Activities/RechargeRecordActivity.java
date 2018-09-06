package com.rongxun.xqlc.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rongxun.xqlc.Adapters.RechargeRecordListAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.funds.UserRecharge;
import com.rongxun.xqlc.Beans.funds.UserRechargeList;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadMoreListView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.ServiscUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class RechargeRecordActivity extends MyBaseActivity implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {


    LoadMoreListView rechargeRecordListView;
    SwipeRefreshLayout rechargeRecordSwipLayout;
    IconFontTextView rechargeRecordToolbarBack;
    TextView rechargeRecordToolbarTitle;
    Toolbar rechargeRecordToolbar;

    private String TAG = "充值记录";
    private String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/rechargeList";

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private RechargeRecordListAdapter myAdapter;
    private List<UserRecharge> rechargeList = new ArrayList<UserRecharge>();
    private LoadingDialog loadingDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    if (rechargeList.size() > 0) {
                        nothing_img.setVisibility(View.GONE);
                    }
                    //刷新数据
                    myAdapter = new RechargeRecordListAdapter(RechargeRecordActivity.this, rechargeList, getLayoutInflater());
                    rechargeRecordListView.setAdapter(myAdapter);
                    if (rechargeRecordSwipLayout.isShown()) {
                        rechargeRecordSwipLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    if (rechargeList.size() > 0) {
                        nothing_img.setVisibility(View.GONE);
                    }
                    //直接添加数据
                    rechargeRecordListView.onLoadComplete();
                    myAdapter.setChargeList(rechargeList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private ImageView nothing_img;
    private ImageView service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_recharge_record);

        initView();
        initToolBar();


        myAdapter = new RechargeRecordListAdapter(this, rechargeList, getLayoutInflater());
        rechargeRecordListView.setAdapter(myAdapter);
        rechargeRecordListView.setLoadMoreListen(this);
        rechargeRecordSwipLayout.setOnRefreshListener(this);
        rechargeRecordSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        RequestForListData(BASIC_URL, 1, PAGESIZE, false);
        if (loadingDialog == null) {

            loadingDialog = new LoadingDialog(this);
            loadingDialog.showLoading();
        }

        service.setOnClickListener(new View.OnClickListener() {
            private ServiscUtil serviscUtil;

            @Override
            public void onClick(View v) {
                serviscUtil = new ServiscUtil(RechargeRecordActivity.this,nothing_img);
                serviscUtil.initPopuptWindowExit();
            }
        });
    }

    private void initView() {

        nothing_img = (ImageView) findViewById(R.id.recharge_record_nothing_img);
        rechargeRecordListView = (LoadMoreListView) findViewById(R.id.recharge_record_list_view);
        rechargeRecordSwipLayout = (SwipeRefreshLayout) findViewById(R.id.recharge_record_swip_layout);
        rechargeRecordToolbarBack = (IconFontTextView) findViewById(R.id.recharge_record_toolbar_back);
        rechargeRecordToolbarTitle = (TextView) findViewById(R.id.recharge_record_toolbar_title);
        rechargeRecordToolbar = (Toolbar) findViewById(R.id.recharge_record_toolbar);
        service = (ImageView) findViewById(R.id.recharge_record_service);
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
        rechargeRecordToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(rechargeRecordToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean refreshing) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("pager.pageNumber", pageNumber + "")
                .addParams("pager.pageSize", pageSize + "")
                .addParams("type", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        if (refreshing) {
                            if (rechargeRecordSwipLayout.isShown()) {
                                rechargeRecordSwipLayout.setRefreshing(false);
                            }
                        } else {
                            rechargeRecordListView.onLoadComplete();
                        }
                        Toast.makeText(RechargeRecordActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        final UserRechargeList resultBean = JSON.parseObject(s, UserRechargeList.class);
                        Log.i(TAG, s);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            rechargeList.addAll(resultBean.getUserRechargesList());
                            if (refreshing) {
                                Message msg = new Message();
                                msg.what = 0x111;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = 0x222;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Toast.makeText(RechargeRecordActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    @Override
    public void loadMore() {
        if (currentBottomPageIndex < totalPageCount) {
            currentBottomPageIndex++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RequestForListData(BASIC_URL, currentBottomPageIndex, PAGESIZE, false);
                }
            }, 500);
        } else {
            rechargeRecordListView.onLoadComplete();
            return;
        }
    }

    @Override
    public void onRefresh() {
        currentBottomPageIndex = 1;
        rechargeList.clear();
        myAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(BASIC_URL, 1, PAGESIZE, true);
            }

        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
