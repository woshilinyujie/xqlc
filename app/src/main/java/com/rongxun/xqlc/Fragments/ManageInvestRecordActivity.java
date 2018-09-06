package com.rongxun.xqlc.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rongxun.xqlc.Activities.LoginActivity;
import com.rongxun.xqlc.Adapters.ManageInvestRecordAdapter;
import com.rongxun.xqlc.Beans.Borrow.InvestRecordBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class ManageInvestRecordActivity extends AppCompatActivity {

    private RelativeLayout relBack;
    private PullToRefreshLayout refreshLayout;
    private RelativeLayout relNetError;//网络错误占位图
    private View split;//分割线
    private LoadListView lv;
    private LoadingDialog loadingDialog;
    private List<InvestRecordBean.BorrowTenderItemListBean> data;
    private ManageInvestRecordAdapter recordAdapter;
    private InvestRecordBean recordBean;
    private int pager = 1;//第一页
    private int size = 10;//每页十条
    private int allSize;//总条数
    private int id;//项目id

    public static final int REFRESHING = 0;//刷新
    public static final int LOADMORE = 1;//加载更多

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invest_record);

        id = getIntent().getIntExtra("id", 0);

        initView();
        initData();
        listener();
    }

    private void listener() {

        //返回
        relBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //刷新
        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //第一页
                pager = 1;
                requestData(REFRESHING);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });

        //上拉加载更多
        lv.setLoadMoreListener(new LoadListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //页数++
                pager++;
                //
                requestData(LOADMORE);
            }
        });
    }

    private void initData() {
        //网络获取数据
        requestData(REFRESHING);
    }

    private void requestData(final int flag) {

        String token = PreferenceUtil.getPrefString(this, "loginToken", "");

        OkHttpUtils
                .post()
                .url(AppConstants.INVESTMENT_RECORD + "/" + id)
                .addParams("token", token)
                .addParams("pager.pageNumber", pager + "")
                .addParams("pager.pageSize", size + "")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误提示预留

                        setNetErrorBg();

                        //请求失败
                        if (flag == REFRESHING) {
                            refreshLayout.delayRefreshFinish(PullToRefreshLayout.FAIL, 400);
                        } else {
                            //加载失败pager-1
                            pager--;
                            //加载失败
                            lv.loadFail();
                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {

                            recordBean = GsonUtils.GsonToBean(response, InvestRecordBean.class);
                            if (recordBean != null) {

                                switch (recordBean.getRcd()) {
                                    case "R0001":
                                        // TODO: 2017/8/10 0010
                                        allSize = recordBean.getPageBean().getTotalCount();

                                        if (flag == REFRESHING) {

                                            //刷新成功
                                            refreshLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 400);
                                            //清空数据
                                            if (!data.isEmpty()) {
                                                data.clear();
                                            }
                                            //获取数据
                                            if (recordBean != null) {
                                                data.addAll(recordBean.getBorrowTenderItemList());
                                            }
                                            //判断是否还有更多数据
                                            lv.loadFinish(data.size(), allSize);
                                            recordAdapter.notifyDataSetChanged();

                                        } else if (flag == LOADMORE) {

                                            if (recordBean != null) {
                                                data.addAll(recordBean.getBorrowTenderItemList());
                                            }

                                            //加载完成
                                            lv.loadFinish(data.size(), allSize);
                                            recordAdapter.notifyDataSetChanged();
                                        }
                                        //网络错误
                                        setNetErrorBg();

                                        break;
                                    case "M0006":
                                        //刷新成功
                                        refreshLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                        //无数据
                                        setNetErrorBg();
                                        break;
                                    case "E0001":
                                        //抢登
                                        Intent login = new Intent(ManageInvestRecordActivity.this, LoginActivity.class);
                                        startActivity(login);
                                        break;
                                    case "R0003":
                                        //
                                        Toast.makeText(ManageInvestRecordActivity.this, "账号被锁定", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            }
                        }
                    }
                });

    }

    private void initView() {

        relNetError = (RelativeLayout) findViewById(R.id.manage_investment_rel_net_error);
        split = findViewById(R.id.manage_investment_view_split);
        relBack = (RelativeLayout) findViewById(R.id.invest_record_rel_back);
        refreshLayout = (PullToRefreshLayout) findViewById(R.id.manage_invest_record_refresh);
        lv = (LoadListView) findViewById(R.id.invest_record_lv);

        data = new ArrayList<>();
        recordAdapter = new ManageInvestRecordAdapter(data, this);
        lv.setAdapter(recordAdapter);
    }

    /**
     * 设置网络错误的背景
     */
    private void setNetErrorBg() {

        if (data.isEmpty()) {
            relNetError.setVisibility(View.VISIBLE);
            lv.setVisibility(View.INVISIBLE);
            split.setVisibility(View.INVISIBLE);
        } else {

            relNetError.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            split.setVisibility(View.VISIBLE);
        }

    }
}
