package com.rongxun.xqlc.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.rongxun.xqlc.Activities.ManageDetailActivity;
import com.rongxun.xqlc.Activities.PastProductsActivity;
import com.rongxun.xqlc.Adapters.PastProductsAdapter;
import com.rongxun.xqlc.Beans.Borrow.PastProductsBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 已售罄
 */
public class PastProductsFragment extends Fragment {


    public static final int REFRESHING = 0;//刷新
    public static final int LOADMORE = 1;//加载更多

    private static final String PRODUCR_FLAG = "flag";

    private View rootView;
    private Activity mActivity;
    private PullToRefreshLayout refreshLayout;
    private RelativeLayout relNetError;
    private LoadListView lv;
    private Dialog loadingDialog;
    private String productFlag;
    private PastProductsAdapter productsAdapter;
    private PastProductsBean productsBean;
    private List<PastProductsBean.BorrowDTOListBean> data;

    private int pager = 1;//第一页
    private int size = 10;//每页十条
    private int allSize;//总条数
    private String rongXunFlg;

    public PastProductsFragment() {
        // Required empty public constructor
    }


    /**
     * 工厂模式
     *
     * @param productFlag
     * @return
     */
    public static PastProductsFragment newInstance(String productFlag,String rongXunFlg) {
        PastProductsFragment fragment = new PastProductsFragment();
        Bundle args = new Bundle();
        args.putString(PRODUCR_FLAG, productFlag);
        args.putString("rongXunFlg", rongXunFlg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productFlag = getArguments().getString(PRODUCR_FLAG);
            rongXunFlg = getArguments().getString("rongXunFlg");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_past_products, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
        listener();
    }

    private void listener() {


        //刷新
        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(mActivity, ManageDetailActivity.class);
                int markId = data.get(position).getId();
                intent.putExtra("id", markId);
                startActivity(intent);
//                mActivity.finish();

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
                .url(AppConstants.PAST_PRODUCT)
                .addParams("status", productFlag + "")
                .addParams("pager.pageNumber", pager + "")
                .addParams("pager.pageSize", size + "")
                .addParams("rongXunFlg",rongXunFlg)
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
                            //解析
                            productsBean = GsonUtils.GsonToBean(response, PastProductsBean.class);

                            if (productsBean != null && productsBean.getPageBean() != null) {

                                allSize = productsBean.getPageBean().getTotalCount();
                            }

                            if (flag == REFRESHING) {

                                //刷新成功
                                refreshLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 400);
                                //清空数据
                                if (!data.isEmpty()) {
                                    data.clear();
                                }
                                //获取数据
                                if (productsBean != null) {
                                    data.addAll(productsBean.getBorrowDTOList());
                                }
                                //网络错误背景图
                                setNetErrorBg();
                                lv.loadFinish(data.size(), allSize);
                                productsAdapter.notifyDataSetChanged();

                            } else if (flag == LOADMORE) {
                                if (productsBean != null) {
                                    data.addAll(productsBean.getBorrowDTOList());
                                }
                                //加载完成
                                lv.loadFinish(data.size(), allSize);
                                productsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void initView() {

        relNetError = (RelativeLayout) rootView.findViewById(R.id.past_product_rel_rate_rel_net_error);
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.sell_out_refresh);
        lv = (LoadListView) rootView.findViewById(R.id.sell_out_lv);

        data = new ArrayList<>();
        productsAdapter = new PastProductsAdapter(mActivity, data, productFlag);
        lv.setAdapter(productsAdapter);

    }

    /**
     * 设置网络错误的背景
     */
    private void setNetErrorBg() {

        if (data.isEmpty()) {
            relNetError.setVisibility(View.VISIBLE);
            lv.setVisibility(View.INVISIBLE);
        } else {

            relNetError.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }

    }

}
