package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.rongxun.xqlc.Adapters.ActivityCenterAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.home.ActivityCenterBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class ActivityCenterActivity extends MyBaseActivity {


    private RelativeLayout relBack;
    private PullToRefreshLayout refreshLayout;
    private LoadListView lv;
    private List<ActivityCenterBean.ActivityListBean> data;
    private ActivityCenterBean centerBean;
    private ActivityCenterAdapter adapter;

    private int pager = 1;//第一页
    private int size = 10;//每页十条
    private int allSize;//总条数

    public static final int REFRESHING = 0;//刷新
    public static final int LOADMORE = 1;//加载更多


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_center);
        CustomApplication.addActivity(this);

        //发一个广播,隐藏活动中心红点
        Intent home = new Intent();
        home.setAction("intent.action.home.data.refresh");
        home.putExtra("home", 222);
        sendBroadcast(home);


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
        //lv监听
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (data.size() > 0) {
                    ActivityCenterBean.ActivityListBean activityListBean = data.get(position);

                    //跳有交互的web
                    if (activityListBean.getContent() != null
                            && !activityListBean.getContent().equals("")) {
                        Intent activity = new Intent(ActivityCenterActivity.this, H5JSActivity.class);
                        activity.putExtra("web_url", activityListBean.getContent());
                        startActivity(activity);
                        finish();
                    }


                }

            }
        });
    }

    private void initData() {
        //网络获取数据
        requestData(REFRESHING);
    }

    private void requestData(final int flag) {


        OkHttpUtils
                .post()
                .url(AppConstants.ACTIVITY_CENTER)
                .addParams("pager.pageNumber", pager + "")
                .addParams("pager.pageSize", size + "")
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                        // TODO: 2017/8/7 0007 网络错误提示预留

                        //请求失败
                        if (flag == REFRESHING) {
                            refreshLayout.delayRefreshFinish(PullToRefreshLayout.FAIL, 800);
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

                            centerBean = GsonUtils.GsonToBean(response, ActivityCenterBean.class);
                            allSize = centerBean.getPageBean().getTotalCount();
                            if (flag == REFRESHING) {
                                //刷新成功
                                refreshLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                //清空数据
                                if (!data.isEmpty()) {
                                    data.clear();
                                }
                                //获取数据
                                if (centerBean != null) {
                                    data.addAll(centerBean.getActivityList());
                                }
                                lv.loadFinish(data.size(), allSize);
                                adapter.notifyDataSetChanged();
                            } else if (flag == LOADMORE) {
                                if (centerBean != null) {
                                    data.addAll(centerBean.getActivityList());
                                }
                                //加载完成
                                lv.loadFinish(data.size(), allSize);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                });

    }

    private void initView() {

        relBack = (RelativeLayout) findViewById(R.id.activity_center_rel_back);
        lv = (LoadListView) findViewById(R.id.activity_center_lv);
        refreshLayout = (PullToRefreshLayout) findViewById(R.id.activity_center_refresh);

        //设置底部背景色
        lv.getRootView().setBackgroundColor(Color.parseColor("#f6f7fb"));
        data = new ArrayList<>();
        adapter = new ActivityCenterAdapter(data, this);
        lv.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
        OkHttpUtils.getInstance().cancelTag(this);
    }
}