package com.rongxun.xqlc.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Adapters.FinancialDetailsListAdapter;
import com.rongxun.xqlc.Adapters.HongBaoListAdapter;
import com.rongxun.xqlc.Beans.CashBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.LoadMoreListView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class FinancialDetailsFragment extends Fragment {
    private static final String ARG_PARAM = "param";
    private String TAG = "资金明细";
    private List<CashBean.UserAccDetailListBean> tenderList = new ArrayList<CashBean.UserAccDetailListBean>();

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private FinancialDetailsListAdapter myAdapter;
    private View rootView;
    private LoadListView investRecordListView;
    private PullToRefreshLayout investRecordSwipLayout;
    private String status;
    private LinearLayout no_data_ll;


    public static FinancialDetailsFragment newInstance(String param1) {
        FinancialDetailsFragment fragment = new FinancialDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public FinancialDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            status = getArguments().getString(ARG_PARAM);
        }
        rootView = inflater.inflate(R.layout.fragment_repay_ment, container, false);
        initView();
        initNetWork();


        return rootView;
    }

    private void initView() {
        investRecordListView = (LoadListView) rootView.findViewById(R.id.repay_ment_list_view);
        investRecordListView.getRootView().setBackgroundColor(Color.parseColor("#f6f7fb"));
        investRecordSwipLayout = (PullToRefreshLayout) rootView.findViewById(R.id.repay_ment_swip_layout);
        no_data_ll = (LinearLayout) rootView.findViewById(R.id.repay_ment_nothing_ll);

        myAdapter = new FinancialDetailsListAdapter(getActivity(), tenderList);
        investRecordListView.setAdapter(myAdapter);

        investRecordSwipLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentBottomPageIndex=1;
                cashdedetails(1,true);
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
                cashdedetails(currentBottomPageIndex,false);
            }
        });


    }


    private void initNetWork() {

        cashdedetails(1,true);


    }

    private void cashdedetails(int pageNumber,final boolean isRefreshing) {

        String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/accountDetailList";

        OkHttpUtils.post()
                .url(BASIC_URL)
                .addParams("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", ""))
                .addParams("pager.pageNumber", currentBottomPageIndex+"")
                .addParams("pager.pageSize", "10")
                .addParams("recodeType", status)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (!tenderList.isEmpty()) {
                            tenderList.clear();
                            myAdapter.notifyDataSetChanged();
                        }
                        no_data_ll.setVisibility(View.VISIBLE);
                        investRecordListView.setVisibility(View.GONE);
                        if (isRefreshing) {
                            investRecordSwipLayout.delayRefreshFinish(PullToRefreshLayout.FAIL, 800);
                        } else {
                            //加载失败pager-1
                            currentBottomPageIndex--;
                            //加载失败
                            investRecordListView.loadFail();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String ss = response.toString();
                        Log.i("ss",ss);
                        CashBean resultBean = new Gson().fromJson(ss, CashBean.class);

                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            if (isRefreshing) {
                                investRecordSwipLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                if (!tenderList.isEmpty()) {
                                    tenderList.clear();
                                }

                                if (resultBean.getUserAccDetailList() != null){
                                    if(resultBean.getUserAccDetailList().size()>0){
                                        no_data_ll.setVisibility(View.GONE);
                                        investRecordListView.setVisibility(View.VISIBLE);
                                    }else{
                                        investRecordListView.setVisibility(View.GONE);
                                        no_data_ll.setVisibility(View.VISIBLE);
                                    }
                                    tenderList.addAll(resultBean.getUserAccDetailList());
                                    investRecordListView.loadFinish(currentBottomPageIndex, totalPageCount);
                                    myAdapter.notifyDataSetChanged();

                                }

                            } else {
                                if (resultBean.getUserAccDetailList() != null)
                                    tenderList.addAll(resultBean.getUserAccDetailList());
                                investRecordListView.loadFinish(currentBottomPageIndex, totalPageCount);
                                myAdapter.notifyDataSetChanged();
                            }

                        } else {
                            investRecordListView.setVisibility(View.GONE);
                            no_data_ll.setVisibility(View.VISIBLE);
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
