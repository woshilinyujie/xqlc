package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Adapters.CommuniqueAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.CommuniqueBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oguig on 2017/7/25.
 * 官方公告
 */

public class CommuniqueActivity extends MyBaseActivity implements View.OnClickListener {
    private IconFontTextView msg_gonggao_back;
    private PullToRefreshLayout msg_gonggao_swip_layout;
    private LoadListView msg_gonggao_list_view;
    private LinearLayout msg_gonggao_nothing_ll;
    private String TAG = "CommuniqueActivity";
    private List<CommuniqueBean.DataBean> datas;
    private CommuniqueAdapter communiqueAdapter;
    private int currentBottomPageIndex=1;//已经加载的页数
    private int page = 1;//页数
    private int size = 10;//每页显示的条数
    private int totalPageCount;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_communique);
        initView();
        //网络获取数据
        RequestData(1, true);
        setOnClickListener();
    }

    private void initView() {
        msg_gonggao_back = (IconFontTextView) findViewById(R.id.msg_gonggao_back);
        msg_gonggao_swip_layout = (PullToRefreshLayout) findViewById(R.id.msg_gonggao_swip_layout);
        msg_gonggao_list_view = (LoadListView) findViewById(R.id.msg_gonggao_list_view);
        msg_gonggao_nothing_ll = (LinearLayout) findViewById(R.id.msg_gonggao_nothing_ll);
        datas = new ArrayList<>();
        communiqueAdapter = new CommuniqueAdapter(datas, CommuniqueActivity.this);
        msg_gonggao_list_view.setAdapter(communiqueAdapter);

    }

    private void setOnClickListener() {
        msg_gonggao_back.setOnClickListener(this);
        msg_gonggao_swip_layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentBottomPageIndex = 1;
                RequestData(1, true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
        msg_gonggao_list_view.setLoadMoreListener(new LoadListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //页数++
                currentBottomPageIndex++;
                //
                RequestData(currentBottomPageIndex, false);
            }
        });
        msg_gonggao_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CommuniqueBean.DataBean dataBean = datas.get(position);
                Intent intent = new Intent(CommuniqueActivity.this, ArticleActivity.class);
                intent.putExtra("id", dataBean.getId() + "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msg_gonggao_back:
                finish();
                break;
        }
    }

    private void RequestData(int page, final boolean isRefreshing) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(CommuniqueActivity.this);
            loadingDialog.show();
        }
        OkHttpUtils.post()
                .url(AppConstants.COMMUNIQUE)
                .addParams("pager.pageNumber", "" + page)
                .addParams("pager.pageSize", "" + size)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (!datas.isEmpty()) {
                            datas.clear();
                            communiqueAdapter.notifyDataSetChanged();
                        }
                        msg_gonggao_list_view.setVisibility(View.GONE);
                        msg_gonggao_nothing_ll.setVisibility(View.VISIBLE);
                        if (isRefreshing) {
                            msg_gonggao_swip_layout.delayRefreshFinish(PullToRefreshLayout.FAIL, 800);
                        } else {
                            //加载失败pager-1
                            currentBottomPageIndex--;
                            //加载失败
                            msg_gonggao_list_view.loadFail();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        String s = response.toString();
                        Log.v("sdf",s);
                        CommuniqueBean gson = new Gson().fromJson(s, CommuniqueBean.class);
                        if (gson.getRcd().equals("R0001")) {
                            totalPageCount = gson.getPageBean().getPageCount();


                            if (isRefreshing) {
                                msg_gonggao_swip_layout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                if (!datas.isEmpty()) {
                                    datas.clear();
                                }
                                if (gson.getArticleItemList() != null) {
                                    if (gson.getArticleItemList().size() > 0) {
                                        msg_gonggao_list_view.setVisibility(View.VISIBLE);
                                        msg_gonggao_nothing_ll.setVisibility(View.GONE);
                                    } else {
                                        msg_gonggao_list_view.setVisibility(View.GONE);
                                        msg_gonggao_nothing_ll.setVisibility(View.VISIBLE);
                                    }

                                    datas.addAll(gson.getArticleItemList());
                                    msg_gonggao_list_view.loadFinish(currentBottomPageIndex, totalPageCount);
                                    communiqueAdapter.notifyDataSetChanged();
                                }

                            } else {
                                datas.addAll(gson.getArticleItemList());
                                msg_gonggao_list_view.loadFinish(currentBottomPageIndex, totalPageCount);
                                communiqueAdapter.notifyDataSetChanged();
                            }

                        } else {
                            msg_gonggao_list_view.setVisibility(View.GONE);
                            msg_gonggao_nothing_ll.setVisibility(View.VISIBLE);
                            Toast.makeText(CommuniqueActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        CustomApplication.removeActivity(this);
        super.onDestroy();
    }
}
