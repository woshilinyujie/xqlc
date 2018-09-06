package com.rongxun.xqlc.Fragments;

import android.graphics.Color;
import android.os.Bundle;
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
import com.rongxun.xqlc.Adapters.BackMoneyListAdapter;
import com.rongxun.xqlc.Adapters.HongBaoListAdapter;
import com.rongxun.xqlc.Beans.center.UserTenderList;
import com.rongxun.xqlc.R;
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


public class BackMoneyFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private String TAG = "投资记录";
    private String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/hkmx";
    private List<UserTenderList.UserRepaymentListBean> tenderList = new ArrayList<>();

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private BackMoneyListAdapter myAdapter;
    private UserTenderList resultBean;
    private View rootView;
    private LoadListView investRecordListView;
    private PullToRefreshLayout investRecordSwipLayout;
    private ImageView investRecordNothingImg;
    private String status;
    private LinearLayout repay_ment_nothing_ll;

    public static BackMoneyFragment newInstance(String param1) {
        BackMoneyFragment fragment = new BackMoneyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public BackMoneyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_repay_ment, container, false);
        initView();
        initNetWork();
        initListener();
        return rootView;
    }

    private void initView() {
        investRecordListView = (LoadListView) rootView.findViewById(R.id.repay_ment_list_view);
        investRecordListView.getRootView().setBackgroundColor(Color.parseColor("#f6f7fb"));
        investRecordSwipLayout = (PullToRefreshLayout) rootView.findViewById(R.id.repay_ment_swip_layout);
        investRecordNothingImg = (ImageView) rootView.findViewById(R.id.repay_ment_nothing_img);
        repay_ment_nothing_ll = (LinearLayout) rootView.findViewById(R.id.repay_ment_nothing_ll);
        myAdapter = new BackMoneyListAdapter(getActivity(), tenderList);
        investRecordListView.setAdapter(myAdapter);
    }

    private void initNetWork() {
        RequestForListData(BASIC_URL, 1, PAGESIZE, true);
    }

    private void initListener() {
        investRecordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                UserTenderList.UserRepaymentListBean userTender = tenderList.get(position);
//                int a = userTender.getBorrowStatus();
//                if (userTender.getBorrowStatus() == 3 ||
//                        userTender.getBorrowStatus() == 7 || userTender.getIsNewbor().equals("1")) {
//                    Intent intent = new Intent(getActivity(), RepayMentActivity.class);
//                    if (tenderList != null) {
//                        intent.putExtra("id", userTender.getTenderid());
//                        intent.putExtra("projectId", userTender.getBorrowId());
//                        intent.putExtra("name", userTender.getBorrowName());
//                    }
//                    startActivity(intent);
//                }
            }
        });
        investRecordSwipLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentBottomPageIndex = 1;
                RequestForListData(BASIC_URL, 1, PAGESIZE, true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
        investRecordListView.setLoadMoreListener(new LoadListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //页数++
                currentBottomPageIndex++;
                //
                RequestForListData(BASIC_URL, currentBottomPageIndex, PAGESIZE, false);
            }
        });
    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean isRefreshing) {

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", ""))
                .addParams("pager.pageNumber", pageNumber + "")
                .addParams("pager.pageSize", pageSize + "")
                .addParams("status", status)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (!tenderList.isEmpty()) {
                            tenderList.clear();
                            myAdapter.notifyDataSetChanged();
                        }
                        investRecordListView.setVisibility(View.GONE);
                        repay_ment_nothing_ll.setVisibility(View.VISIBLE);
                        if (isRefreshing) {
                            investRecordSwipLayout.delayRefreshFinish(PullToRefreshLayout.FAIL, 800);
                        } else {
                            //加载失败pager-1
                            currentBottomPageIndex--;
                            //加载失败
                            investRecordListView.loadFail();
                        }
                        try{

                            Toast.makeText(getActivity(), "连接网络失败", Toast.LENGTH_SHORT).show();
                        }catch (Exception e1){

                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        resultBean = JSON.parseObject(s, UserTenderList.class);
                        Log.i(TAG, s);
                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            if (isRefreshing) {
                                investRecordSwipLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                if (!tenderList.isEmpty()) {
                                    tenderList.clear();
                                }
                                if (resultBean.getUserRepaymentList() != null){
                                    if(resultBean.getUserRepaymentList().size()>0){
                                        investRecordListView.setVisibility(View.VISIBLE);
                                        repay_ment_nothing_ll.setVisibility(View.GONE);
                                    }else{
                                        investRecordListView.setVisibility(View.GONE);
                                        repay_ment_nothing_ll.setVisibility(View.VISIBLE);
                                    }
                                    tenderList.addAll(resultBean.getUserRepaymentList());
                                    investRecordListView.loadFinish(currentBottomPageIndex, totalPageCount);
                                    myAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (resultBean.getUserRepaymentList() != null)
                                    tenderList.addAll(resultBean.getUserRepaymentList());
                                investRecordListView.loadFinish(currentBottomPageIndex, totalPageCount);
                                myAdapter.notifyDataSetChanged();
                            }
                        } else {
                            investRecordListView.setVisibility(View.GONE);
                            repay_ment_nothing_ll.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
