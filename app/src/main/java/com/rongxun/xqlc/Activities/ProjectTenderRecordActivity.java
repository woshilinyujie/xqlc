package com.rongxun.xqlc.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rongxun.xqlc.Adapters.ProjectTenderRecordListAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Borrow.BorrowTenderItem;
import com.rongxun.xqlc.Beans.Borrow.BorrowTenderList;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadMoreListView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

//投资记录页面
public class ProjectTenderRecordActivity extends MyBaseActivity implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {

    LoadMoreListView tenderRecordListView;
    SwipeRefreshLayout tenderRecordSwipLayout;
    ImageView tenderRecordNothingImg;

    private String TAG = "投资记录";
    String basicUrl = AppConstants.URL_SUFFIX + "/rest/borrowTenderList";

    private BorrowTenderList resultBean;
    private ProjectTenderRecordListAdapter mAdapter;
    private List<BorrowTenderItem> tenderList = new ArrayList<BorrowTenderItem>();
    private LoadingDialog loadingDialog;

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数
    private int borrowId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_project_tender_record);

        borrowId = getIntent().getIntExtra("borrowId", 0);

        initView();
//        initToolBar();

        mAdapter = new ProjectTenderRecordListAdapter(this, new ArrayList<BorrowTenderItem>(), getLayoutInflater(), "0");
        tenderRecordListView.setAdapter(mAdapter);
        tenderRecordSwipLayout.setOnRefreshListener(this);
        tenderRecordListView.setLoadMoreListen(this);
        tenderRecordSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        RequestForListData(basicUrl, borrowId, 1, PAGESIZE, true);
        if (loadingDialog == null) {

            loadingDialog = new LoadingDialog(this);
            loadingDialog.showLoading();
        }

    }

    private void initView() {

        tenderRecordListView = (LoadMoreListView) findViewById(R.id.tender_record_list_view);
        tenderRecordSwipLayout = (SwipeRefreshLayout) findViewById(R.id.tender_record_swip_layout);
        tenderRecordNothingImg = (ImageView) findViewById(R.id.tender_record_nothing_img);
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

//    public void initToolBar() {
//        projectTenderRecordToolbarBack.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        finish();
//                    }
//                }
//        );
//        setSupportActionBar(projectTenderRecordToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//    }

    public void RequestForListData(String basicUrl, int projectId, int pageNumber, int pageSize, final boolean refreshing) {

        String url = basicUrl + "/" + projectId;
        OkHttpUtils.post()
                .url(url)
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

                        if (tenderRecordSwipLayout.isShown()) {
                            tenderRecordSwipLayout.setRefreshing(false);
                        }

                        Toast.makeText(ProjectTenderRecordActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        resultBean = JSON.parseObject(s, BorrowTenderList.class);
                        Log.i(TAG, "response json:" + s);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        if (tenderRecordSwipLayout.isShown()) {
                            tenderRecordSwipLayout.setRefreshing(false);
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            tenderList.addAll(resultBean.getBorrowTenderItemList());
                            if (refreshing) {
                                if (resultBean.getBorrowTenderItemList() != null && resultBean.getBorrowTenderItemList().size() > 0) {
                                    //刷新数据
                                    tenderRecordNothingImg.setVisibility(View.GONE);
                                    mAdapter = new ProjectTenderRecordListAdapter(ProjectTenderRecordActivity.this, tenderList, getLayoutInflater(), "0");
                                    tenderRecordListView.setAdapter(mAdapter);
                                    if (tenderRecordSwipLayout.isShown()) {
                                        tenderRecordSwipLayout.setRefreshing(false);
                                    }
                                } else {
                                    tenderRecordNothingImg.setVisibility(View.VISIBLE);
                                    if (tenderRecordSwipLayout.isShown()) {
                                        tenderRecordSwipLayout.setRefreshing(false);
                                    }
                                }
                            } else {
                                //直接添加数据
                                tenderRecordNothingImg.setVisibility(View.GONE);
                                tenderRecordListView.onLoadComplete();
                                mAdapter.setTenderList(tenderList);
                                mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (refreshing) {
                                if (tenderRecordSwipLayout.isShown()) {
                                    tenderRecordSwipLayout.setRefreshing(false);
                                }
                            } else {
                                tenderRecordListView.onLoadComplete();
                            }
                            Toast.makeText(ProjectTenderRecordActivity.this, resultBean.getRmg() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    @Override
    public void onRefresh() {
        currentBottomPageIndex = 1;
        tenderList.clear();

        mAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(basicUrl, borrowId, 1, PAGESIZE, true);
            }

        }, 500);
    }


    @Override
    public void loadMore() {
        if (currentBottomPageIndex < totalPageCount) {
            currentBottomPageIndex++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RequestForListData(basicUrl, borrowId, currentBottomPageIndex, PAGESIZE, false);
                }
            }, 500);
        } else {
            tenderRecordListView.onLoadComplete();
            return;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
