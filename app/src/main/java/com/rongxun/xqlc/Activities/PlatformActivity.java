package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Adapters.PlatformEventAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.PlatformEventBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadMoreListView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by oguig on 2017/7/27.
 */
public class PlatformActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    public String TAG = "活动中心";
    private int page = 1;//页数
    private int size = 10;//每页显示的条数
    private List<PlatformEventBean.ActivityListBean> datas;
    private SwipeRefreshLayout discover_platform_refresh;
    private LoadMoreListView discover_platform_lv;
    private LoadingDialog loadingDialog;
    private PlatformEventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_platform);
        initView();
        initData();


    }


    private void initView() {
        discover_platform_refresh = (SwipeRefreshLayout) findViewById(R.id.discover_platform_refresh);
        discover_platform_lv = (LoadMoreListView) findViewById(R.id.discover_platform_lv);
        datas = new ArrayList<>();

        eventAdapter = new PlatformEventAdapter(datas, PlatformActivity.this);
        discover_platform_lv.setAdapter(eventAdapter);
        discover_platform_lv.setOnItemClickListener(this);

        discover_platform_refresh.setOnRefreshListener(this);
    }

    private void initData() {
        RequestData();
    }

    private void RequestData() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(PlatformActivity.this);
                }
                loadingDialog.showLoading();
            }
        });


        String url = AppConstants.URL_SUFFIX + "/rest/activity";

        OkHttpUtils.post()
                .url(url)
                .addParams("pager.pageNumber", "" + page)
                .addParams("pager.pageSize", "" + size)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (discover_platform_refresh.isRefreshing()) {
                            discover_platform_refresh.setRefreshing(false);
                        }

                        //关闭dialog
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        Toast.makeText(PlatformActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.v(TAG, s);
                        final PlatformEventBean bean = new Gson().fromJson(s, PlatformEventBean.class);
                        if (bean.getRcd().equals("R0001")) {
                            if (discover_platform_refresh.isRefreshing()) {
                                discover_platform_refresh.setRefreshing(false);
                            }
                            //关闭dialog
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismissLoading();
                                loadingDialog = null;
                            }
                            if (datas != null) {
                                datas.clear();
                            }
                            datas.addAll(bean.getActivityList());
                            discover_platform_lv.onLoadComplete();
                            eventAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {

        RequestData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlatformEventBean.ActivityListBean bean = datas.get(position);
        Intent intent = new Intent(PlatformActivity.this, ArticleActivity.class);
        intent.putExtra("id", bean.getId() + "");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
