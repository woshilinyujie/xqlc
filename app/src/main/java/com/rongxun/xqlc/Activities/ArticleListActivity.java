package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rongxun.xqlc.Adapters.ArticleListAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Article.ArticleItem;
import com.rongxun.xqlc.Beans.Article.ArticleList;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.Utils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class ArticleListActivity extends MyBaseActivity implements AdapterView.OnItemClickListener {

    String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/articleList/";
    private final String TAG = "文章列表";
    private String title;
    private String type;
    private LoadListView atricleListView;
    private LinearLayout nothingImg;
    private Toolbar atricleListToolBar;
    private IconFontTextView atriclelistToolBarBack;
    private TextView atriclelistToolTitle;
    private PullToRefreshLayout articleSwipeLayout;
    private final int PAGESIZE = 20;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private ArticleListAdapter myAdapter;
    private List<ArticleItem> articleItemList = new ArrayList<>();
    private boolean isTime;
    private TextView fankui;
    private ImageView line1;


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleItem item = articleItemList.get(position);
        if (type.equals("app_help_center")) {
            TextView help = (TextView) view.findViewById(R.id.article_list_help_tx);
            ImageView iv = (ImageView) view.findViewById(R.id.article_list_iv);
            line1=(ImageView)view.findViewById(R.id.line1);
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(Utils.dip2px(ArticleListActivity.this, 15),
                            Utils.dip2px(ArticleListActivity.this, 8));
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.rightMargin = Utils.dip2px(ArticleListActivity.this, 15);
            iv.setLayoutParams(params);
            if (item.isOpen) {
                //关闭
                item.isOpen = false;
                help.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);
                iv.setBackgroundResource(R.mipmap.drow_icon);
                help = null;
                iv = null;
            } else {
                //打开
                item.isOpen = true;
                help.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                iv.setBackgroundResource(R.mipmap.up_icon);
                help = null;
                iv = null;
            }
        } else {
            Intent intent = new Intent(ArticleListActivity.this, ArticleActivity.class);
            intent.putExtra("id", item.getId() + "");
            intent.putExtra("header", title);
            startActivity(intent);
        }


    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_article_list);
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        isTime = getIntent().getBooleanExtra("isTime", false);
        initToolBar(title);

        atricleListView = (LoadListView) findViewById(R.id.article_list_list_view);
        nothingImg = (LinearLayout) findViewById(R.id.article_list_nothing_ll);
        articleSwipeLayout = (PullToRefreshLayout) findViewById(R.id.article_list_swipe_layout);
        fankui = (TextView) findViewById(R.id.fankui);
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticleListActivity.this, FeedBackActivity.class);
                startActivity(intent);
            }
        });
        myAdapter = new ArticleListAdapter(ArticleListActivity.this, articleItemList);
        myAdapter.setType(type);
        atricleListView.setAdapter(myAdapter);
        articleSwipeLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentBottomPageIndex = 1;
                RequestForListData(BASIC_URL, type, 1, PAGESIZE, true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
        atricleListView.setLoadMoreListener(new LoadListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //页数++
                currentBottomPageIndex++;
                //
                RequestForListData(BASIC_URL, type, currentBottomPageIndex, PAGESIZE, false);
            }
        });

        atricleListView.setOnItemClickListener(this);

        RequestForListData(BASIC_URL, type, 1, PAGESIZE, true);

    }

    public void initToolBar(String title) {

        atricleListToolBar = (Toolbar) findViewById(R.id.article_list_toolbar);
        atriclelistToolTitle = (TextView) findViewById(R.id.article_list_toolbar_title);
        atriclelistToolBarBack = (IconFontTextView) findViewById(R.id.article_list_toolbar_back);
        atriclelistToolBarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        atriclelistToolTitle.setText(title);
        setSupportActionBar(atricleListToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    //请求网络数据
    public void RequestForListData(String basicUrl, final String type, int pageNumber, int pageSize, final boolean isRefreshing) {
        String url = basicUrl + type;
        OkHttpUtils.post()
                .url(url)
                .addParams("pager.pageNumber", pageNumber + "")
                .addParams("pager.pageSize", pageSize + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                        if (!articleItemList.isEmpty()) {
                            articleItemList.clear();
                            myAdapter.notifyDataSetChanged();
                        }
                        atricleListView.setVisibility(View.GONE);
                        nothingImg.setVisibility(View.VISIBLE);
                        if (isRefreshing) {
                            articleSwipeLayout.delayRefreshFinish(PullToRefreshLayout.FAIL, 800);
                        } else {
                            //加载失败pager-1
                            currentBottomPageIndex--;
                            //加载失败
                            atricleListView.loadFail();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.i(TAG, "response json:" + s);
                        final ArticleList resultBean = JSON.parseObject(s, ArticleList.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            if (isRefreshing) {
                                articleSwipeLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 800);
                                if (!articleItemList.isEmpty()) {
                                    articleItemList.clear();
                                }
                                if (resultBean.getArticleItemList() != null) {
                                    if (resultBean.getArticleItemList().size() > 0) {
                                        atricleListView.setVisibility(View.VISIBLE);
                                        nothingImg.setVisibility(View.GONE);
                                    } else {
                                        atricleListView.setVisibility(View.GONE);
                                        nothingImg.setVisibility(View.VISIBLE);
                                    }

                                    articleItemList.addAll(resultBean.getArticleItemList());
                                    atricleListView.loadFinish(currentBottomPageIndex, totalPageCount);
                                    myAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (resultBean.getArticleItemList() != null)
                                    articleItemList.addAll(resultBean.getArticleItemList());
                                atricleListView.loadFinish(currentBottomPageIndex, totalPageCount);
                                myAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(ArticleListActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                            nothingImg.setVisibility(View.VISIBLE);
                            atricleListView.setVisibility(View.GONE);
                        }

                    }
                });
    }


    public boolean isTime() {
        return isTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }

}
