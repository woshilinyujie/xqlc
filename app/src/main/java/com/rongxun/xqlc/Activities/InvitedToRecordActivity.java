package com.rongxun.xqlc.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Adapters.FriendsAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.FriendsBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by oguig on 2017/8/11.
 */

public class InvitedToRecordActivity extends MyBaseActivity implements View.OnClickListener {
    private IconFontTextView invited_toolbar_back;
    private PullToRefreshLayout invited_record_refresh_layout;
    private String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/tjfriendsListVT";
    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数
    private List<FriendsBean.FriendListBean> tenderList = new ArrayList<>();
    private LoadListView invited_record_list_view;
    private LinearLayout invited_record_nothing_ll;
    private FriendsBean resultBean;
    private FriendsAdapter myAdapter;
    private TextView all_cash, all_people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.invited_to_record);
        initView();
        RequestForListData(BASIC_URL, 1, PAGESIZE, true);
    }

    private void initView() {
        invited_toolbar_back = (IconFontTextView) findViewById(R.id.invited_toolbar_back);
        invited_record_refresh_layout = (PullToRefreshLayout) findViewById(R.id.invited_record_refresh_layout);
        invited_record_list_view = (LoadListView) findViewById(R.id.invited_record_list_view);
        invited_record_list_view.getRootView().setBackgroundColor(Color.parseColor("#f6f7fb"));
        invited_record_nothing_ll = (LinearLayout) findViewById(R.id.invited_record_nothing_ll);
        all_cash = (TextView) findViewById(R.id.all_cash);
        all_people = (TextView) findViewById(R.id.all_people);
        myAdapter = new FriendsAdapter(InvitedToRecordActivity.this, tenderList);
        invited_record_list_view.setAdapter(myAdapter);
        invited_toolbar_back.setOnClickListener(this);
        invited_record_refresh_layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentBottomPageIndex = 1;
                RequestForListData(BASIC_URL, 1, PAGESIZE, true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });

        invited_record_list_view.setLoadMoreListener(new LoadListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //页数++
                currentBottomPageIndex++;
                //
                RequestForListData(BASIC_URL, currentBottomPageIndex, PAGESIZE, false);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invited_toolbar_back:
                finish();
                break;
        }
    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean isRefreshing) {

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(InvitedToRecordActivity.this, "loginToken", ""))
                .addParams("pager.pageNumber", pageNumber + "")
                .addParams("pager.pageSize", pageSize + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (!tenderList.isEmpty()) {
                            tenderList.clear();
                            myAdapter.notifyDataSetChanged();
                        }
                        invited_record_list_view.setVisibility(View.GONE);
                        invited_record_nothing_ll.setVisibility(View.VISIBLE);
                        if (isRefreshing) {
                            invited_record_refresh_layout.delayRefreshFinish(PullToRefreshLayout.FAIL, 800);
                        } else {
                            //加载失败pager-1
                            currentBottomPageIndex--;
                            //加载失败
                            invited_record_list_view.loadFail();
                        }
                        Toast.makeText(InvitedToRecordActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        resultBean = new Gson().fromJson(s, FriendsBean.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            all_cash.setText(resultBean.getAwardTotalMoneyTj() + "");
                            all_people.setText(resultBean.getFriends() + "");
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            if (isRefreshing) {
                                invited_record_refresh_layout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                if (!tenderList.isEmpty()) {
                                    tenderList.clear();
                                }
                                if (resultBean.getFriendList() != null) {
                                    if (resultBean.getFriendList().size() > 0) {
                                        invited_record_list_view.setVisibility(View.VISIBLE);
                                        invited_record_nothing_ll.setVisibility(View.GONE);
                                    } else {
                                        invited_record_list_view.setVisibility(View.GONE);
                                        invited_record_nothing_ll.setVisibility(View.VISIBLE);
                                    }
                                    tenderList.addAll(resultBean.getFriendList());
                                    invited_record_list_view.loadFinish(currentBottomPageIndex, totalPageCount);
                                    myAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (resultBean.getFriendList() != null)
                                    tenderList.addAll(resultBean.getFriendList());
                                invited_record_list_view.loadFinish(currentBottomPageIndex, totalPageCount);
                                myAdapter.notifyDataSetChanged();
                            }
                        } else {
                            invited_record_list_view.setVisibility(View.GONE);
                            invited_record_nothing_ll.setVisibility(View.VISIBLE);
                            Toast.makeText(InvitedToRecordActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
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
