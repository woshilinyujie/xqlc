package com.rongxun.xqlc.Activities;

import android.content.Intent;
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
import com.rongxun.xqlc.Adapters.TxRecordListAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.funds.UserCash;
import com.rongxun.xqlc.Beans.funds.UserCashList;
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


public class TxRecordActivity extends MyBaseActivity implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {


    LoadMoreListView txRecordListView;
    SwipeRefreshLayout txRecordSwipLayout;
    IconFontTextView txRecordToolbarBack;
    TextView txRecordToolbarTitle;
    Toolbar txRecordToolbar;


    private String TAG = "提现记录";
    private String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/cashList";

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private TxRecordListAdapter myAdapter;
    private List<UserCash> txList = new ArrayList<UserCash>();
    private LoadingDialog loadingDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    if (txList.size() > 0) {
                        nothing_img.setVisibility(View.GONE);
                    }
                    //刷新数据
                    myAdapter = new TxRecordListAdapter(TxRecordActivity.this, txList, getLayoutInflater());
                    txRecordListView.setAdapter(myAdapter);
                    if (txRecordSwipLayout.isShown()) {
                        txRecordSwipLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    if (txList.size() > 0) {
                        nothing_img.setVisibility(View.GONE);
                    }
                    //直接添加数据
                    txRecordListView.onLoadComplete();
                    myAdapter.setTxList(txList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private ImageView nothing_img;
    private UserCashList resultBean;
    private ImageView service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_tx_record);

        initView();
        initToolBar();

        myAdapter = new TxRecordListAdapter(this, txList, getLayoutInflater());
        txRecordListView.setAdapter(myAdapter);
        txRecordListView.setLoadMoreListen(this);
        txRecordSwipLayout.setOnRefreshListener(this);
        txRecordSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        RequestForListData(BASIC_URL, 1, PAGESIZE, false);
        if (loadingDialog == null) {

            loadingDialog = new LoadingDialog(this);
            loadingDialog.showLoading();
        }

        service.setOnClickListener(new View.OnClickListener() {
            private ServiscUtil serviscUtil;

            @Override
            public void onClick(View v) {
                serviscUtil = new ServiscUtil(TxRecordActivity.this,nothing_img);
                serviscUtil.initPopuptWindowExit();
            }
        });
    }

    private void initView() {

        nothing_img = (ImageView) findViewById(R.id.tx_record_nothing_img);

        txRecordListView = (LoadMoreListView) findViewById(R.id.tx_record_list_view);
        txRecordSwipLayout = (SwipeRefreshLayout) findViewById(R.id.tx_record_swip_layout);
        txRecordToolbarBack = (IconFontTextView) findViewById(R.id.tx_record_toolbar_back);
        txRecordToolbarTitle = (TextView) findViewById(R.id.tx_record_toolbar_title);
        service = (ImageView) findViewById(R.id.tx_record_service);
        txRecordToolbar = (Toolbar) findViewById(R.id.tx_record_toolbar);

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
        txRecordToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(txRecordToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean refreshing) {

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("pager.pageNumber", pageNumber + "")
                .addParams("pager.pageSize", pageSize + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        if (refreshing) {
                            if (txRecordSwipLayout.isShown()) {
                                txRecordSwipLayout.setRefreshing(false);
                            }
                        } else {
                            txRecordListView.onLoadComplete();
                        }
                        Toast.makeText(TxRecordActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        resultBean = JSON.parseObject(s, UserCashList.class);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            //只显示成功，审核中，处理中的数据
                            for (UserCash item : resultBean.getUserCashList()) {
                                int status = item.getStatus() == null ? -1 : item.getStatus().intValue();
                                if (status == 1 || status == 0 || status == 4 || status == 2) {
                                    txList.add(item);
                                }
                            }
                            totalPageCount = resultBean.getPageBean().getPageCount();

                            if (refreshing) {
                                Message msg = new Message();
                                msg.what = 0x111;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = 0x222;
                                mHandler.sendMessage(msg);
                            }
                        } else if (resultBean.getRcd().equals("E0001")) {
                            startActivityForResult(new Intent(TxRecordActivity.this, LoginActivity.class), 1400);
                            overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(TxRecordActivity.this, "登录已过期，请重新登录!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(TxRecordActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();

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
            txRecordListView.onLoadComplete();
            return;
        }
    }

    @Override
    public void onRefresh() {
        currentBottomPageIndex = 1;
        txList.clear();
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
