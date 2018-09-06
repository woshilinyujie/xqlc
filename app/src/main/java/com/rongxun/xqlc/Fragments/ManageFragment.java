package com.rongxun.xqlc.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rongxun.xqlc.Activities.H5JSActivity;
import com.rongxun.xqlc.Activities.LoginActivity;
import com.rongxun.xqlc.Activities.ManageDetailActivity;
import com.rongxun.xqlc.Activities.PastProductsActivity;
import com.rongxun.xqlc.Adapters.MangeAdapter;
import com.rongxun.xqlc.Beans.Borrow.Acount;
import com.rongxun.xqlc.Beans.Borrow.ContentBean;
import com.rongxun.xqlc.Beans.Borrow.DistrictBean;
import com.rongxun.xqlc.Beans.Borrow.ManageBean;
import com.rongxun.xqlc.Beans.Borrow.ManageOriginBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.ManagePinnedListView;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.UI.TipsFlipper;
import com.rongxun.xqlc.UI.fab.FloatingView;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品
 */
public class ManageFragment extends BaseFragment {

    private View rootView;//根布局
    public boolean isFrist = true;

    private PullToRefreshLayout refreshLayout;
    private ManagePinnedListView lv;
    private ImageView imgBanner;//顶部广告栏
    private TipsFlipper tips;//消息提示
    private FloatingView fv;//悬浮框
    private List<ManageBean> markData;//标的数据
    private MangeAdapter mangeAdapter;
    private ImageView imageFloat;//悬浮框
    private ArrayList<Acount> tipData;//tips的数据
    private TextView txtPast;//往期产品
    private RelativeLayout relError;
    private ManageReceiver manageReceiver;
    private Activity mActivity;


    public ManageFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public ManageFragment(Activity activity) {
        mActivity = activity;
        rootView = View.inflate(mActivity,R.layout.fragment_manage,null);
        initView();
//        initData();
        listener();
    }

//    public static ManageFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        ManageFragment fragment = new ManageFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void listener() {

        //刷新监听
        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

                requestData(AppConstants.MANAGE);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                // TODO: 2017/7/27 0027 禁用
            }
        });

        //刷新状态
        // TODO: 2017/8/10 0010 :listview滑到顶部，触发下拉，导致悬浮图隐藏不出现的问题解决
        refreshLayout.setStateListener(new PullToRefreshLayout.StateListener() {
            @Override
            public void startDown() {
                fv.hide();
            }

            @Override
            public void finishDown() {
                fv.show();
            }
        });

        //listview监听
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * 因为添加头部，所以真实数据位置-1
                 * 因为设置头尾不可点击所以不需要考虑下标越界
                 */
                int relPosition = position - 1;

                if (relPosition <= 0 || relPosition == markData.size()) {
                    return;
                }
                if (markData.get(position - 1).getTypes() == ManageBean.ITEM) {
                    //如果是item类型则响应点击事件
                    ContentBean bean = (ContentBean) markData.get(relPosition);
                    Intent intent = new Intent(mActivity, ManageDetailActivity.class);
                    intent.putExtra("id", bean.getId());
                    mActivity.startActivity(intent);
                }
            }
        });



    }

    private void initData() {
        //网络请求
        requestData( AppConstants.MANAGE);
    }

    private void initView() {

        //注册广播
        manageReceiver = new ManageReceiver();
        mActivity.registerReceiver(manageReceiver, new IntentFilter("intent.action.home.data.refresh"));
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.manage_refresh);
        lv = (ManagePinnedListView) rootView.findViewById(R.id.manage_pinned_lv);
        relError = (RelativeLayout) rootView.findViewById(R.id.manage_rel_net_error);

        View head = LayoutInflater.from(mActivity).inflate(R.layout.manage_lv_head, null);
        View footer = LayoutInflater.from(mActivity).inflate(R.layout.manage_lv_footer, null);
        imgBanner = (ImageView) head.findViewById(R.id.manage_img_banner);
        tips = (TipsFlipper) head.findViewById(R.id.manage_fp_tips);
        txtPast = (TextView) footer.findViewById(R.id.manage_lv_past);

        /////////初始化悬浮框//////////
        imageFloat = (ImageView) rootView.findViewById(R.id.manage_float_image);
        fv = (FloatingView) rootView.findViewById(R.id.manage_fab);
        fv.attachToListView(lv);

        //初始化listview
        markData = new ArrayList<>();
        tipData = new ArrayList<>();
        mangeAdapter = new MangeAdapter(mActivity, markData);
//        lv.addHeaderView(head, null, false);
        lv.addFooterView(footer, null, false);
        lv.setAdapter(mangeAdapter);
    }


    public void requestData(String basicUrl) {

        SharedPreferences preferences = mActivity.getSharedPreferences("AppToken", Context.MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");

        OkHttpUtils
                .post()
                .url(basicUrl)
                .addParams("token", token)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误提示预留
                        //刷新失败
                        try {
                            if (refreshLayout != null)
                                refreshLayout.delayRefreshFinish(PullToRefreshLayout.FAIL, 400);
                        } catch (Exception e1) {

                        }
                        //判断是否显示背景图
                        setNetErrorBg();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //刷新成功
                        if (refreshLayout != null)
                            refreshLayout.delayRefreshFinish(PullToRefreshLayout.SUCCEED, 400);

                        if (response != null) {
                            //解析、添加数据
                            doJsonData(response);
                            //判断是否显示背景图
                            setNetErrorBg();
                        }
                    }
                });

    }

    /**
     * 数据整理
     *
     * @param s
     */
    private void doJsonData(String s) {

        ManageOriginBean origin = GsonUtils.GsonToBean(s, ManageOriginBean.class);

        switch (origin.getRcd()) {
            case "R0001":

                ManageOriginBean.DataBeanX originData = origin.getData();
                if (originData != null) {
                    //整理标的数据
                    setListData(originData);
                    //设置轮播文字
                    try {
                        setTips(originData);
                    } catch (Exception e) {

                    }
                }
                break;
            case "E0001":
                //抢登
                Intent login = new Intent(mActivity, LoginActivity.class);
                mActivity.startActivity(login);
                break;
            case "R0003":
                //
                Toast.makeText(mActivity, "账号被锁定", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * 滚动消息
     *
     * @param originData
     */
    private void setTips(ManageOriginBean.DataBeanX originData) {

        List<ManageOriginBean.DataBeanX.InvestsBean> invests = originData.getInvests();

        if (invests != null && invests.size() > 0) {
            for (int i = 0, len = invests.size(); i < len; i++) {

                ManageOriginBean.DataBeanX.InvestsBean investsBean = invests.get(i);
                tipData.add(new Acount(investsBean.getCreateDateStr(),
                        investsBean.getUsername(),
                        investsBean.getMoney()));
            }

            //设置数据源
            tips.setDatas(tipData);
            //开始滚动
            tips.start();
        }


    }

    /**
     * 标列表
     *
     * @param originData
     */
    private void setListData(ManageOriginBean.DataBeanX originData) {

        List<ManageOriginBean.DataBeanX.BorrowsBean> borBeen = originData.getBorrows();
        List<ManageBean> mark = new ArrayList<>();

        if (borBeen != null && borBeen.size() > 0)
            for (int i = 0, len = borBeen.size(); i < len; i++) {

                ManageOriginBean.DataBeanX.BorrowsBean bean = borBeen.get(i);

                DistrictBean districtBean = new DistrictBean();
                districtBean.setDistrict(bean.getKey());
                districtBean.setMore(bean.getMore());
                mark.add(districtBean);//获取 “区”

                List<ManageOriginBean.DataBeanX.BorrowsBean.DataBean> dataBeen = bean.getData();
                for (int j = 0, len2 = dataBeen.size(); j < len2; j++) {

                    ManageOriginBean.DataBeanX.BorrowsBean.DataBean bor = dataBeen.get(j);
                    ContentBean contentBean = new ContentBean();
                    contentBean.setId(bor.getId());
                    contentBean.setName(bor.getName());
                    contentBean.setStatus(bor.getStatus());
                    contentBean.setType(bor.getType());
                    contentBean.setTimeLimit(bor.getTimeLimit());
                    contentBean.setApr(bor.getApr());
                    contentBean.setSchedule(bor.getSchedule());
                    contentBean.setBalance(bor.getBalance());
                    contentBean.setIsNewbor(bor.getIsNewbor());
                    contentBean.setActivityOne(bor.getActivityOne());
                    contentBean.setActivityTwo(bor.getActivityTwo());
                    contentBean.setCornerLable(bor.getCornerLable());
                    contentBean.setBaseApr(bor.getBaseApr());
                    contentBean.setAwardApr(bor.getAwardApr());
                    contentBean.setCornerLableVal(bor.getCornerLableVal());
                    contentBean.setCount(bor.getCount());
                    contentBean.setTq(bor.getTq());
                    contentBean.setUseHongbao(bor.getUseHongbao());
                    if (j == len2 - 1) {
                        //最后一条数据
                        contentBean.setLast(true);
                    }
                    if (contentBean != null) ;
                    mark.add(contentBean);//获取具体“标”
                }
            }
        if (markData != null) {
            //刷新
            if (!markData.isEmpty()) {
                markData.clear();
            }
            //添加
            markData.addAll(mark);
        }
        //唤醒
        if (mangeAdapter != null)
            mangeAdapter.notifyDataSetChanged();

    }

    /**
     * 设置网络错误的背景
     */
    private void setNetErrorBg() {

        if (markData != null && markData.isEmpty()) {
            relError.setVisibility(View.VISIBLE);
            lv.setVisibility(View.INVISIBLE);
            fv.setVisibility(View.INVISIBLE);
        } else {

            relError.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            fv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public View getmView() {
        return rootView;
    }

    /**
     * 理财页广播
     */
    class ManageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getIntExtra("home", 0)) {

                case 110:
                    // TODO: 2017/7/25 0025 刷新列表数据
                    initData();
                    break;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
        mActivity.unregisterReceiver(manageReceiver);
    }

}

