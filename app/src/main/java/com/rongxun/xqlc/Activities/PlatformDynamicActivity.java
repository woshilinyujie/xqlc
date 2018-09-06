package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Adapters.PlatformDynamicAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.PlatformDynamicBean;
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
 * Created by oguig on 2017/7/27.
 */

public class PlatformDynamicActivity extends AppCompatActivity {
    private PullToRefreshLayout platform_dynamic_refresh;
    private LoadListView platform_dynamic_lv;
    private IconFontTextView repayment_toolbar_back;
    private LoadingDialog loadingDialog;
    private List<PlatformDynamicBean.MediaReportDTOListBean> datas;
    private PlatformDynamicAdapter mAdapter;
    private LinearLayout platformdynamic_ll;
    private int currentBottomPageIndex=1;//已经加载的页数
    private int totalPageCount;
    private PlatformDynamicBean resultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_dynamic);
        CustomApplication.addActivity(this);
        initView();
        initData(1, true);

    }


    private void initView() {

        repayment_toolbar_back = (IconFontTextView) findViewById(R.id.repayment_toolbar_back);
        platform_dynamic_refresh = (PullToRefreshLayout) findViewById(R.id.platform_dynamic_refresh);
        platformdynamic_ll = (LinearLayout) findViewById(R.id.platformdynamic_ll);
        platform_dynamic_lv = (LoadListView) findViewById(R.id.platform_dynamic_lv);
        datas = new ArrayList<>();
        mAdapter = new PlatformDynamicAdapter(PlatformDynamicActivity.this, datas);
        platform_dynamic_lv.setAdapter(mAdapter);
        platform_dynamic_refresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentBottomPageIndex = 1;
                initData(1, true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
        platform_dynamic_lv.setLoadMoreListener(new LoadListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //页数++
                currentBottomPageIndex++;
                //
                initData(currentBottomPageIndex, false);
            }
        });
        repayment_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        platform_dynamic_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it=new Intent(PlatformDynamicActivity.this,ArticleActivity.class);
                it.putExtra("id",resultBean.getMediaReportDTOList().get(position).getId()+"");
                startActivity(it);
            }
        });


    }

    private void initData(int pageNumber, final boolean isRefreshing) {

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(PlatformDynamicActivity.this);
        }
        loadingDialog.show();


        String basicUrl = AppConstants.URL_SUFFIX + "/rest/articleList/media_report";

        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("way", "" + 2)
                .addParams("pager.pageNumber", "" + pageNumber)
                .addParams("pager.pageSize", "" + 10)
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
                            mAdapter.notifyDataSetChanged();
                        }
                        platform_dynamic_lv.setVisibility(View.GONE);
                        platformdynamic_ll.setVisibility(View.VISIBLE);
                        if (isRefreshing) {
                            platform_dynamic_refresh.delayRefreshFinish(PullToRefreshLayout.FAIL, 800);
                        } else {
                            //加载失败pager-1
                            currentBottomPageIndex--;
                            //加载失败
                            platform_dynamic_lv.loadFail();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        String ss = response.toString();
                        Log.i("qq",ss);
                         resultBean = new Gson().fromJson(ss, PlatformDynamicBean.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            if (isRefreshing) {
                                platform_dynamic_refresh.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                if (!datas.isEmpty()) {
                                    datas.clear();
                                }
                                if (resultBean.getMediaReportDTOList() != null) {
                                    if(resultBean.getMediaReportDTOList().size()>0){
                                        platform_dynamic_lv.setVisibility(View.VISIBLE);
                                        platformdynamic_ll.setVisibility(View.GONE);
                                    }else{
                                        platform_dynamic_lv.setVisibility(View.GONE);
                                        platformdynamic_ll.setVisibility(View.VISIBLE);
                                    }

                                    datas.addAll(resultBean.getMediaReportDTOList());
                                    platform_dynamic_lv.loadFinish(currentBottomPageIndex, totalPageCount);
                                    mAdapter.notifyDataSetChanged();
                                }

                            } else {
                                if (resultBean.getMediaReportDTOList() != null)
                                    datas.addAll(resultBean.getMediaReportDTOList());
                                platform_dynamic_lv.loadFinish(currentBottomPageIndex, totalPageCount);
                                mAdapter.notifyDataSetChanged();
                            }

                        } else {
                            platform_dynamic_lv.setVisibility(View.GONE);
                            platformdynamic_ll.setVisibility(View.VISIBLE);
                            Toast.makeText(PlatformDynamicActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();

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
