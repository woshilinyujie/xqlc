package com.rongxun.xqlc.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rongxun.xqlc.Activities.HistoryHongbaoActivity;
import com.rongxun.xqlc.Activities.MainActivity;
import com.rongxun.xqlc.Adapters.HongBaoListAdapter;
import com.rongxun.xqlc.Beans.user.UserHongbaoBean;
import com.rongxun.xqlc.Beans.user.UserHongbaoViews;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.LoadListViewHongbao;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.PreventFastClickUtils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class HongBaoDetailFragment extends Fragment {
    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/hbListLooked";
    private final int PAGESIZE = 10;//一页的条目数
    private static final String ARG_PARAM = "hb_type";
    private View rootView;
    private PullToRefreshLayout repay_ment_swip_layout;
    private LoadListViewHongbao hongbao_list_view;
    private LinearLayout hongbao_nothing_ll;
    private ImageView hongbao_nothing_img;

    public int currentStatus;//红包类型
    private String TAG = "我的红包列表";
    private int totalPageCount;//总页数
    private List<UserHongbaoViews> hongbaoList = new ArrayList<UserHongbaoViews>();
    private HongBaoListAdapter myAdapter;
    private int currentBottomPageIndex = 1;//已经加载的页数

    public static HongBaoDetailFragment newInstance(int param) {
        HongBaoDetailFragment fragment = new HongBaoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentStatus = getArguments().getInt(ARG_PARAM);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hong_bao_detail_hongbao, container, false);
        final String flag = getActivity().getIntent().getStringExtra("flag");
        final String current = getActivity().getIntent().getStringExtra("current");
        initView();
        initNetWork();
        initListener();
        if (currentStatus == 0) {
            hongbao_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (current != null && current.equals("h5")) {
                        //发送广播到H5js界面,结束自己
                        Intent h5 = new Intent();
                        h5.setAction("intent.action.h5js.refresh.token");
                        h5.putExtra("h5_js", 222);
                        getActivity().sendBroadcast(h5);
                    }

                    if (flag != null && flag.equals("ads")) {
                        if (!PreventFastClickUtils.isFastClick()) {
                            //由广告页进入H5,此时MainActivity还没有启动，先启动然后显示理财列表
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("flag", "asd");
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                    }


                }
            });
        }

        return rootView;
    }

    private void initListener() {
        hongbao_list_view.getHistory().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), HistoryHongbaoActivity.class));
            }
        });
    }

    private void initNetWork() {
        RequestForListData(basicUrl, 1, 10, currentStatus, true);
    }


    private void initView() {
        repay_ment_swip_layout = (PullToRefreshLayout) rootView.findViewById(R.id.repay_ment_swip_layout);
        hongbao_nothing_ll = (LinearLayout) rootView.findViewById(R.id.hongbao_nothing_ll);
        hongbao_nothing_img = (ImageView) rootView.findViewById(R.id.hongbao_nothing_img);
        hongbao_list_view = (LoadListViewHongbao) rootView.findViewById(R.id.hongbao_list_view);
        hongbao_list_view.getRootView().setBackgroundColor(Color.parseColor("#f6f7fb"));
        myAdapter = new HongBaoListAdapter(getActivity(), hongbaoList, getActivity().getLayoutInflater());
        hongbao_list_view.setAdapter(myAdapter);

        repay_ment_swip_layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentBottomPageIndex = 1;
                RequestForListData(basicUrl, 1, PAGESIZE, currentStatus, true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });

        hongbao_list_view.setLoadMoreListener(new LoadListViewHongbao.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //页数++
                currentBottomPageIndex++;
                //
                RequestForListData(basicUrl, currentBottomPageIndex, PAGESIZE, currentStatus, false);
            }
        });


    }

    /**
     * 请求红包数据
     */
    private void RequestForListData(String basicUrl, int pageNumber, int pageSize, int status, final boolean isRefreshing) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", ""))
                .addParams("status", status + "")
                .addParams("pager.pageSize", pageSize + "")
                .addParams("pager.pageNumber", pageNumber + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (!hongbaoList.isEmpty()) {
                            hongbaoList.clear();
                            myAdapter.notifyDataSetChanged();
                        }
                        hongbao_list_view.setVisibility(View.GONE);
                        hongbao_nothing_ll.setVisibility(View.VISIBLE);
                        if (isRefreshing) {
                            repay_ment_swip_layout.delayRefreshFinish(PullToRefreshLayout.FAIL, 800);
                        } else {
                            //加载失败pager-1
                            currentBottomPageIndex--;
                            //加载失败
                            hongbao_list_view.loadFail();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.i(TAG, s);
                        final UserHongbaoBean resultBean = JSON.parseObject(s, UserHongbaoBean.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();

                            if (isRefreshing) {
                                repay_ment_swip_layout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                if (!hongbaoList.isEmpty()) {
                                    hongbaoList.clear();
                                }
                                if (resultBean.getUserHongbaoViews() != null) {
                                    if (resultBean.getUserHongbaoViews().size() > 0) {
                                        hongbao_list_view.setVisibility(View.VISIBLE);
                                    } else {
                                        hongbao_list_view.setVisibility(View.GONE);
                                    }
                                    hongbaoList.addAll(resultBean.getUserHongbaoViews());
                                    myAdapter.notifyDataSetChanged();
                                }
//
                                hongbao_list_view.loadFinish(currentBottomPageIndex, totalPageCount);
                            } else {
                                if (resultBean.getUserHongbaoViews() != null)
                                    hongbaoList.addAll(resultBean.getUserHongbaoViews());
                                hongbao_list_view.loadFinish(currentBottomPageIndex, totalPageCount);
                                myAdapter.notifyDataSetChanged();
                            }
                        } else {
                            hongbao_list_view.setVisibility(View.GONE);
//                            hongbao_nothing_ll.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
