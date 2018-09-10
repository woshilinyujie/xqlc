package com.rongxun.xqlc.UI;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.pullableview.Pullable;


/**
 * PROJECT_NAME: Sample
 * PACKAGE_NAME: com.guaika.sample.view
 * Author: HouShengLi
 * Time: 2017/07/10 15:00
 * E-mail: 13967189624@163.com
 * Description:
 */

public class LoadListViewJiaxi extends ListView implements OnScrollListener, Pullable {

    private View rootView;
    private Context mContext;
    private ProgressBar bar;
    private TextView txtTip;
    private boolean isLoading = false;//是否正在加载
    private boolean isLastPager = false;//是不是最后一页
    private LinearLayout history;

    @Override
    public View getRootView() {
        return rootView;
    }

    public LoadListViewJiaxi(Context context) {
        super(context);

        mContext = context;
        init();
    }

    public LoadListViewJiaxi(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public LoadListViewJiaxi(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        rootView = LayoutInflater.from(mContext).inflate(R.layout.list_load_more_footer_jiaxi, null);
        bar = (ProgressBar) rootView.findViewById(R.id.progress_bar_loading);
        txtTip = (TextView) rootView.findViewById(R.id.txt_loading_tip);
        history = (LinearLayout) rootView.findViewById(R.id.load_more_hongbao_history);
        rootView.setVisibility(GONE);
        //以尾部 加入
        addFooterView(rootView, null, false);
        //设置滚动箭筒
        setOnScrollListener(this);

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (mScrollListene != null) {
            mScrollListene.onScrollStateChanged(view, scrollState);
        }

        // 滑到底部了
        if (getLastVisiblePosition() == (getCount() - 1)
                && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight()
                && !isLoading
                && !isLastPager
                && loadMoreListener != null) {

            isLoading = true; // 将加载更多进行时状态设置为true
            bar.setVisibility(VISIBLE);
            txtTip.setText("正在加载中...");
            rootView.setVisibility(VISIBLE); // 显示加载更多布局
            //速度太快，延时300,体现效果
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadMoreListener.loadMore(); // 加载更多
                }
            }, 300);

        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (mScrollListene != null) {
            mScrollListene.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }


    /**
     * 加载完成
     */
    public void loadFinish(int currentData, int allData) {

        //不处于刷新状态
        isLoading = false;

        if (currentData == allData) {
            //没有更多了
            //底部可见
//            rootView.setVisibility(VISIBLE);
            //
            txtTip.setText("");
            //隐藏bar
            bar.setVisibility(GONE);
            //设置最后一页
            isLastPager = true;

        } else {
            //还可以上拉
            txtTip.setText("");
//            rootView.setVisibility(VISIBLE);
            bar.setVisibility(GONE);
            //不是最后一页
            isLastPager = false;
        }


    }

    /**
     * 加载失败
     */
    public void loadFail() {

        isLoading = false;
        isLastPager = false;
        //加载失败
        txtTip.setText("加载失败，上拉重试");
        //
        bar.setVisibility(GONE);
        rootView.setVisibility(VISIBLE);
    }

    @Override
    public boolean canPullDown() {
        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0) {
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {
        //禁用上拉
        return false;
    }


    /**
     * 滚动状态的接口
     */
    public interface OnScrollListene {

        void onScrollStateChanged(AbsListView view, int scrollState);

        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    private OnScrollListene mScrollListene;

    public void setmScrollListener(OnScrollListene mScrollListene) {
        this.mScrollListene = mScrollListene;
    }


    /**
     * 上拉加载更多的回调接口
     */
    public interface OnLoadMoreListener {
        void loadMore();
    }

    private OnLoadMoreListener loadMoreListener;

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public LinearLayout getHistory(){
        return history;
    }
}