package com.rongxun.xqlc.Fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.AccruedBean;
import com.rongxun.xqlc.Beans.AssetBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.KeepTwoUtil;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.chart.ColorTemplate;
import com.rongxun.xqlc.chart.DemoBase;
import com.rongxun.xqlc.chart.Easing;
import com.rongxun.xqlc.chart.Entry;
import com.rongxun.xqlc.chart.Highlight;
import com.rongxun.xqlc.chart.IDataSet;
import com.rongxun.xqlc.chart.Legend;
import com.rongxun.xqlc.chart.OnChartValueSelectedListener;
import com.rongxun.xqlc.chart.PercentFormatter;
import com.rongxun.xqlc.chart.PieChart;
import com.rongxun.xqlc.chart.PieData;
import com.rongxun.xqlc.chart.PieDataSet;
import com.rongxun.xqlc.chart.PieEntry;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import okhttp3.Call;


public class AssetFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnChartValueSelectedListener {
    private static final String ARG_PARAM = "param";
    private String TAG = "我的资产";
    private LoadingDialog loadingDialog;
    private TextView dongjieMoney;
    private TextView dongjieBaifenbi;
    private View rootView;
    private String status;

    private int type;
    private PieChart mChart;
    private ImageView no_cash;
    private TextView red_percent_tv, blue_percent_tv, yellow_percent_tv, purple_percent_tv, purple_percent_tv1, benjin_tv, tixian_tv, yue_tv, p1, p2, p3;
    private RelativeLayout investment_incentive_rl;
    private AssetBean asssetbean;
    private AccruedBean accruedBean;
    private DemoBase demoBase;
    private ImageView point_rect_purple;
    private TextView money;
    private String token;
    private TextView cash;

    public AssetFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.v(TAG, "setUserVisibleHint " + isVisibleToUser);
        if (isVisibleToUser == true) {
            if (mChart != null) {
                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_asset, container, false);
        initView();
        initListener();
        cash.setText("总资产(元)");
        benjin_tv.setText("待收本金(元)");
        tixian_tv.setText("提现冻结金额(元)");
        yue_tv.setText("现金余额(元)");
        investment_incentive_rl.setVisibility(View.GONE);
        //显示百分比
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(true);
        //设置图的位置
        mChart.setExtraOffsets(5, 5, 5, 5);
        //设置摩擦系数（惯性）
        mChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中心字体 类型
        demoBase = new DemoBase() {
            @Override
            public String[] getmParties() {
                return super.getmParties();
            }

            @Override
            public Typeface getmTfRegular() {
                return super.getmTfRegular();
            }

            @Override
            public Typeface getmTfLight() {
                return super.getmTfLight();
            }
        };
        mChart.setCenterTextTypeface(demoBase.getmTfLight());

        //是否画中心孔
        mChart.setDrawHoleEnabled(true);
        //中心孔颜色
        mChart.setHoleColor(Color.WHITE);
        //半透明圈颜色
        mChart.setTransparentCircleColor(Color.parseColor("#f5f5f5"));
        mChart.setTransparentCircleAlpha(255);
        //设置外圆的粗细
        mChart.setHoleRadius(70f);
        mChart.setTransparentCircleRadius(65f);

        mChart.setDrawCenterText(true);
        //旋转角度起点
        mChart.setRotationAngle(-90);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        //是否显示高亮点击
        mChart.setHighlightPerTapEnabled(false);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);


        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(demoBase.getmTfRegular());
        mChart.setEntryLabelTextSize(12f);


        return rootView;
    }


    private void initView() {
        mChart = (PieChart) rootView.findViewById(R.id.pie_chart);
        no_cash = (ImageView) rootView.findViewById(R.id.no_cash);
        red_percent_tv = (TextView) rootView.findViewById(R.id.red_percent_tv);
        blue_percent_tv = (TextView) rootView.findViewById(R.id.blue_percent_tv);
        yellow_percent_tv = (TextView) rootView.findViewById(R.id.yellow_percent_tv);
        purple_percent_tv = (TextView) rootView.findViewById(R.id.purple_percent_tv);
        purple_percent_tv1 = (TextView) rootView.findViewById(R.id.purple_percent_tv_1);
        p1 = (TextView) rootView.findViewById(R.id.property_p1);
        p2 = (TextView) rootView.findViewById(R.id.property_p2);
        p3 = (TextView) rootView.findViewById(R.id.property_p3);
        benjin_tv = (TextView) rootView.findViewById(R.id.benjin_tv);
        tixian_tv = (TextView) rootView.findViewById(R.id.tixian_tv);
        dongjieMoney = (TextView) rootView.findViewById(R.id.property_p5);
        dongjieBaifenbi = (TextView) rootView.findViewById(R.id.purple_percent_tv1);
        yue_tv = (TextView) rootView.findViewById(R.id.yue_tv);
        money = (TextView) rootView.findViewById(R.id.money);

        point_rect_purple = (ImageView) rootView.findViewById(R.id.point_rect_purple);
        investment_incentive_rl = (RelativeLayout) rootView.findViewById(R.id.investment_incentive_rl);
        cash = (TextView) rootView.findViewById(R.id.investment_incentive_cash);
        token = PreferenceUtil.getPrefString(getActivity(), "loginToken", "");
        RequestForMoney();
    }

    private void initListener() {
    }


    public void RequestForMoney() {
        String url = AppConstants.URL_SUFFIX + "/rest/ajaxIndex";
        OkHttpUtils.post()
                .url(url)
                .addParams("token", token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                        Toast.makeText(getActivity(), "连接网络失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s = response.toString();
                        Log.i(TAG, s);
                        asssetbean = new Gson().fromJson(s, AssetBean.class);
                        if (asssetbean.getRcd().equals("R0001")) {
                            if (asssetbean != null) {
                                setData(4, 100);
                                String red, blue, yellow,purple;
                                if (asssetbean.getTotal() > 0) {
                                    red = KeepTwoUtil.keep2Decimal(asssetbean.getInvestorCollectionCapital() / asssetbean.getTotal() * 100);
                                    blue = KeepTwoUtil.keep2Decimal(asssetbean.getUnableMoneyTx() / asssetbean.getTotal() * 100);
                                    yellow = KeepTwoUtil.keep2Decimal(asssetbean.getAbleMoney() / asssetbean.getTotal() * 100);
                                    purple = KeepTwoUtil.keep2Decimal(asssetbean.getUnableMoneyTz() / asssetbean.getTotal() * 100);
                                    dongjieBaifenbi.setText(purple);
                                    red_percent_tv.setText(red);
                                    blue_percent_tv.setText(blue);
                                    yellow_percent_tv.setText(yellow);
                                    dongjieBaifenbi.setText(purple);
                                } else {
                                    red_percent_tv.setText(KeepTwoUtil.keep2Decimal(0));
                                    blue_percent_tv.setText(KeepTwoUtil.keep2Decimal(0));
                                    yellow_percent_tv.setText(KeepTwoUtil.keep2Decimal(0));
                                    dongjieBaifenbi.setText(KeepTwoUtil.keep2Decimal(0));
                                }


                                p1.setText(KeepTwoUtil.keep2Decimal(asssetbean.getInvestorCollectionCapital()) + "元");
                                p2.setText(KeepTwoUtil.keep2Decimal(asssetbean.getUnableMoneyTx()) + "元");
                                p3.setText(KeepTwoUtil.keep2Decimal(asssetbean.getAbleMoney()) + "元");
                                dongjieMoney.setText(KeepTwoUtil.keep2Decimal(asssetbean.getUnableMoneyTz()));
                                //设置中心的String
                                money.setText(KeepTwoUtil.keep2Decimal(asssetbean.getTotal()));
//                                mChart.setCenterText(asssetbean.getTotal() + "\n  总资产（元）");
                            }
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.pie, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IDataSet<?> set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHole: {
                if (mChart.isDrawHoleEnabled())
                    mChart.setDrawHoleEnabled(false);
                else
                    mChart.setDrawHoleEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionDrawCenter: {
                if (mChart.isDrawCenterTextEnabled())
                    mChart.setDrawCenterText(false);
                else
                    mChart.setDrawCenterText(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleXVals: {

                mChart.setDrawEntryLabels(!mChart.isDrawEntryLabelsEnabled());
                mChart.invalidate();
                break;
            }
            case R.id.actionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
                mChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.actionTogglePercent:
                mChart.setUsePercentValues(!mChart.isUsePercentValuesEnabled());
                mChart.invalidate();
                break;
            case R.id.animateX: {
                mChart.animateX(1400);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(1400);
                break;
            }
            case R.id.animateXY: {
                mChart.animateXY(1400, 1400);
                break;
            }
            case R.id.actionToggleSpin: {
                mChart.spin(1000, mChart.getRotationAngle(), mChart.getRotationAngle() + 360, Easing.EasingOption
                        .EaseInCubic);
                break;
            }
        }
        return true;
    }

    private void setData(int count, float range) {
        //arg2 百分比
        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        float countAll = (float) asssetbean.getInvestorCollectionCapital() + (float) asssetbean.getAbleMoney()
                + (float) Math.abs(asssetbean.getUnableMoneyTz() + (float) asssetbean.getUnableMoneyTx());
        if (countAll == 0) {
            entries.add(new PieEntry(1f, demoBase.getmParties()[0 % demoBase.getmParties().length]));
            String dd = demoBase.getmParties().length + "";
        } else {
            for (int i = 0; i < count; i++) {
                switch (i) {
                    case 0:
                        entries.add(new PieEntry((float) asssetbean.getInvestorCollectionCapital(), demoBase.getmParties()[i % demoBase.getmParties().length]));
                        break;
                    case 1:
                        entries.add(new PieEntry((float) asssetbean.getUnableMoneyTx(), demoBase.getmParties()[i % demoBase.getmParties().length]));
                        break;
                    case 2:
                        entries.add(new PieEntry((float) Math.abs(asssetbean.getAbleMoney()), demoBase.getmParties()[i % demoBase.getmParties().length]));
                        break;
                    case 3:
                        entries.add(new PieEntry((float) Math.abs(asssetbean.getUnableMoneyTz()), demoBase.getmParties()[i % demoBase.getmParties().length]));
                        break;
                }
            }
        }


        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(0.5f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        //添加各种颜色
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

//        colors.add(Color.rgb(255, 255, 255));
        if (countAll == 0) {
//            colors.add(Color.rgb(184, 184, 184));
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChart.setVisibility(View.GONE);
                    no_cash.setVisibility(View.VISIBLE);
                }
            });

        } else {
            colors.add(Color.parseColor("#f65353"));
            colors.add(Color.parseColor("#72aaff"));
            colors.add(Color.parseColor("#ff9b4b"));
            colors.add(Color.parseColor("#ce63fb"));

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    no_cash.setVisibility(View.GONE);
                }
            });

        }

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(demoBase.getmTfLight());
        mChart.setData(data);

        // undo all highlights

        mChart.highlightValues(null);
        mChart.invalidate();

    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(getActivity());
    }
}
