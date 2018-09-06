package com.rongxun.xqlc.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rongxun.xqlc.Adapters.HongBaoChooseListAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.projectInitBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.Util.AppConstants;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class HbChooseActivity extends MyBaseActivity implements HongBaoChooseListAdapter.OneItemClickListener, HongBaoChooseListAdapter.OneJiaxiItemClickListener {

    IconFontTextView hbChooseToolbarBack;
    TextView hbChooseToolbarTitle;
    TextView hbChooseToolbarOk;
    Toolbar hbChooseToolbar;
    ListView hbChooseListView;
    private Gson gson = new Gson();
    private String TAG = "项目投资";
    private int hongBaoCount = 0;//被选中的红包总数值
    private String hongBaoArray = "";//被选中的红包ID数组
    private List<projectInitBean.DataBean> hongbaoList = new ArrayList<>();
    private List<projectInitBean.CouponListBean> jiaxiList = new ArrayList<>();
    private HongBaoChooseListAdapter myAdapter;
    DecimalFormat df1 = new DecimalFormat("#0.00");
    private String hongBaoArrayString = "";
    StringBuilder hongbaoArray = new StringBuilder();
    int selectedHbSum = 0;
    private TextView choose_hb;
    private HbBroadCast broadCast;
    int AllHBmoney = 0; //所选中红包金额的 总额
    private String certainMoney;
    private TextView explain;
    private TextView count;
    private TextView increase;
    private String certainMoneyLast;//记录上一次投资的钱
    private Button hongbao_bt;
    private Button jiaxi_bt;
    private LinearLayout hongbao_ll;
    private LinearLayout jiaxi_ll;
    private ListView list_jiaxi;
    private TextView jiaxi_count;
    private TextView jiaxi_percentage;
    private TextView jiaxi_profit;
    private TextView jiaxi_increase;
    private boolean isHongbaoEnter = true;//入口是否是红包按钮
    private HongBaoChooseListAdapter jiaxiAdapter;
    private String jiaxiInverstMoney;
    private double jiaxiMoney;
    private int timeLimit;
    private View point_one;
    private View point_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_hb_choose);
        MobclickAgent.onEvent(this, "7");
        initView();

        String hbListString = getIntent().getStringExtra("hbListString");
        String jiaxiListString = getIntent().getStringExtra("jiaxiListString");
        jiaxiMoney = getIntent().getDoubleExtra("jiaxiMoney",0);

        //项目剩余时间
        timeLimit = getIntent().getIntExtra("timeLimit", 0);
        isHongbaoEnter = getIntent().getBooleanExtra("isHongbaoEnter", true);
        hongbaoList = new Gson().fromJson(hbListString, new TypeToken<ArrayList<projectInitBean.DataBean>>() {
        }.getType());
        jiaxiList = new Gson().fromJson(jiaxiListString, new TypeToken<ArrayList<projectInitBean.CouponListBean>>() {
        }.getType());
        //初始化所选择的 红包总额
        for (projectInitBean.DataBean item : hongbaoList) {
            //是否是选择的红包
            if (item.isright) {
                AllHBmoney = AllHBmoney + (int) item.getMoney();
            }
        }
        //用户投资的金额
        certainMoneyLast = getIntent().getStringExtra("tenderMoney");
        if (TextUtils.isEmpty(certainMoneyLast)) {
            certainMoneyLast = "0";
        }
        choose_hb.setText(AllHBmoney + "");
        increase.setText(certainMoneyLast + "");
        jiaxi_increase.setText(certainMoneyLast + "");
        //红包默认使用数目
        int hongbaoCount = 0;
        for (int x = 0; x < hongbaoList.size(); x++) {
            if (hongbaoList.get(x).isright) {
                hongbaoCount++;
            }
        }
        count.setText(hongbaoCount + "");

        if(jiaxiList!=null){
            for(projectInitBean.CouponListBean item:jiaxiList){
                if(item.isRight){
                    jiaxi_count.setText("1");
                    jiaxi_percentage.setText(Double.parseDouble(df1.format(item.getApr()* 36))  + "");
                    if(item.getDays()==0  || item.getDays()>timeLimit){
                        jiaxi_profit.setText(Double.parseDouble(df1.format(item.getApr()*Integer.parseInt(certainMoneyLast)/1000*timeLimit)) + "");

                    }else{
                        jiaxi_profit.setText(Double.parseDouble(df1.format(item.getApr()*Integer.parseInt(certainMoneyLast)/1000*(item.getDays()))) + "");
                    }
                }
            }
        }


        //集合以投资金额多少 排序 抽取到最上层
        for (int x = 0; x < hongbaoList.size(); x++) {
            if (hongbaoList.get(x).isright) {
                projectInitBean.DataBean userHongbaoListBean = hongbaoList.get(x);
                hongbaoList.remove(userHongbaoListBean);
                hongbaoList.add(0, userHongbaoListBean);
            }
        }

//        Collections.sort(hongbaoList, new HongbaoComparator());
        if (hongbaoList != null && hongbaoList.size() > 0) {
            myAdapter = new HongBaoChooseListAdapter(this, hongbaoList, jiaxiList, getLayoutInflater(),
                    getIntent().getStringExtra("tenderMoney"), getIntent().getStringExtra("beLeftMoney"),timeLimit);
            myAdapter.setType(true);
            myAdapter.setOneItemClickListener(this);
            hbChooseListView.setAdapter(myAdapter);
        }

        if (jiaxiList != null && jiaxiList.size() > 0) {
            jiaxiAdapter = new HongBaoChooseListAdapter(this, hongbaoList, jiaxiList, getLayoutInflater(),
                    getIntent().getStringExtra("tenderMoney"), getIntent().getStringExtra("beLeftMoney"),timeLimit);
            jiaxiAdapter.setType(false);
            jiaxiAdapter.setOneJiaxiItemClickListener(this);
            list_jiaxi.setAdapter(jiaxiAdapter);
        }

        if (myAdapter != null)
            myAdapter.setJiaxiAdapter(jiaxiAdapter);
        if (jiaxiAdapter != null)
            jiaxiAdapter.setHongbaoAdapter(myAdapter);

        if (isHongbaoEnter) {
            jiaxi_ll.setVisibility(View.GONE);
            hongbao_ll.setVisibility(View.VISIBLE);
            hbChooseListView.setVisibility(View.VISIBLE);
            list_jiaxi.setVisibility(View.GONE);
            hongbao_bt.setBackgroundColor(Color.parseColor("#fa5454"));
            jiaxi_bt.setBackgroundColor(Color.parseColor("#cccccc"));
        } else {

            hbChooseListView.setVisibility(View.GONE);
            list_jiaxi.setVisibility(View.VISIBLE);
            jiaxi_ll.setVisibility(View.VISIBLE);
            hongbao_ll.setVisibility(View.GONE);
            jiaxi_bt.setBackgroundColor(Color.parseColor("#fa5454"));
            hongbao_bt.setBackgroundColor(Color.parseColor("#cccccc"));
        }
    }

    private void initView() {
        hbChooseToolbarBack = (IconFontTextView) findViewById(R.id.hb_choose_toolbar_back);
        hbChooseToolbarTitle = (TextView) findViewById(R.id.hb_choose_toolbar_title);
        hbChooseToolbarOk = (TextView) findViewById(R.id.hb_choose_toolbar_ok);
        hbChooseToolbar = (Toolbar) findViewById(R.id.hb_choose_toolbar);
        hbChooseListView = (ListView) findViewById(R.id.hb_choose_list_view);
        choose_hb = (TextView) findViewById(R.id.choose_hb);
        explain = (TextView) findViewById(R.id.hb_choose_toolbar_explain);
        count = (TextView) findViewById(R.id.hb_choose_count);
        increase = (TextView) findViewById(R.id.choose_hb_increase);
        //红包按钮
        hongbao_bt = (Button) findViewById(R.id.hb_choose_hongbao_bt);
        //加息按钮
        jiaxi_bt = (Button) findViewById(R.id.hb_choose_jiaxi_bt);
        //加息list
        hongbao_ll = (LinearLayout) findViewById(R.id.hb_choose_hongbao_ll);
        jiaxi_ll = (LinearLayout) findViewById(R.id.hb_choose_jiaxi_ll);
        list_jiaxi = (ListView) findViewById(R.id.hb_choose_list_jiaxi);

        //加息卷数目
        jiaxi_count = (TextView) findViewById(R.id.hb_choose_jiaxi_count);
        //加息%
        jiaxi_percentage = (TextView) findViewById(R.id.hb_choose_jiaxi_percentage);
        //加息具体数额
        jiaxi_profit = (TextView) findViewById(R.id.hb_choose_jiaxi_profit);
        //加息总投资额
        jiaxi_increase = (TextView) findViewById(R.id.hb_choose_jiaxi_increase);

        point_one = findViewById(R.id.hb_choose_point_one);
        point_two = findViewById(R.id.hb_choose_point_two);

        broadCast = new HbBroadCast();
        IntentFilter intentFilter = new IntentFilter("HbBroadCast");
        registerReceiver(broadCast, intentFilter);

        initToolBar();

    }

    public void initToolBar() {
        hbChooseToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );


        explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HbChooseActivity.this, ArticleActivity.class);
                intent.putExtra("id", "655");
                intent.putExtra("header", "红包说明");
                startActivity(intent);
            }
        });


        hbChooseToolbarOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (projectInitBean.DataBean item : hongbaoList) {
                            if (item.isright) {
                                //统计红包总额
                                selectedHbSum += item.getMoney();
                                //使用的 红包id
                                hongbaoArray.append(item.getId() + "" + ",");

                            }
                        }
                        Log.i(TAG, hongbaoArray.toString() + "   ...........");
                        if (hongbaoArray.length() > 0) {
                            hongBaoArrayString = hongbaoArray.substring(0, hongbaoArray.length() - 1);
                        } else {
                            hongBaoArrayString = "";
                        }


                        System.out.println("--------------------" + hongBaoArrayString);
                        Intent intent = new Intent();
                        //使用红包的金额总和
                        intent.putExtra("hongBaoCount", selectedHbSum);
                        //使用红包的ID
                        intent.putExtra("hongBaoArray", hongBaoArrayString);
                        //红包集合回传 确保数据统一
                        intent.putExtra("hbListString", gson.toJson(hongbaoList));
                        //加息卷集合回传 确保数据统一
                        intent.putExtra("jiaxijuanListString", gson.toJson(jiaxiList));
                        intent.putExtra("jiaxiMoney", jiaxiMoney);
                        if (certainMoney == null) {
                            intent.putExtra("certainMoney", getIntent().getStringExtra("tenderMoney"));
                            certainMoneyLast = getIntent().getStringExtra("tenderMoney");
                        } else {
                            //certainMoney 投资的金额   回传 当前certainMoney是选择红包后 累加的
                            intent.putExtra("certainMoney", certainMoney);
                            certainMoneyLast = certainMoney;
                        }
                        if(jiaxiList!=null){
                            for(projectInitBean.CouponListBean item :jiaxiList){
                                if(item.isRight){
                                    //使用加息卷的ID
                                    intent.putExtra("jiaxijuanID", item.getId());
                                    //加息卷百分比
                                    intent.putExtra("jiaxijuanPercentage",Double.parseDouble(df1.format(item.getApr()* 36)) );
                                    if(jiaxiInverstMoney==null){
                                        intent.putExtra("certainMoney", getIntent().getStringExtra("tenderMoney"));
                                        certainMoneyLast = getIntent().getStringExtra("tenderMoney");
                                    }else{
                                        intent.putExtra("certainMoney", jiaxiInverstMoney);
                                        certainMoneyLast=jiaxiInverstMoney;
                                    }
                                }
                            }
                        }


                        setResult(AppConstants.ChooseHongBaoOkCode, intent);
                        finish();
                    }
                }
        );

        //红包按钮
        hongbao_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiaxi_ll.setVisibility(View.GONE);
                hongbao_ll.setVisibility(View.VISIBLE);
                hbChooseListView.setVisibility(View.VISIBLE);
                list_jiaxi.setVisibility(View.GONE);
                hongbao_bt.setBackgroundColor(Color.parseColor("#3574FA"));
                jiaxi_bt.setBackgroundColor(Color.parseColor("#999999"));
                point_one.setVisibility(View.VISIBLE);
                point_two.setVisibility(View.GONE);
            }

        });

        //加息按钮
        jiaxi_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point_one.setVisibility(View.GONE);
                point_two.setVisibility(View.VISIBLE);
                hbChooseListView.setVisibility(View.GONE);
                list_jiaxi.setVisibility(View.VISIBLE);
                jiaxi_ll.setVisibility(View.VISIBLE);
                hongbao_ll.setVisibility(View.GONE);
                jiaxi_bt.setBackgroundColor(Color.parseColor("#3574FA"));
                hongbao_bt.setBackgroundColor(Color.parseColor("#999999"));
            }
        });

        setSupportActionBar(hbChooseToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void OnCheckBoxClick(int position, boolean isChecked, List<projectInitBean.DataBean> hongbaoList) {
        if (isChecked) {
            //取消选择
            hongbaoList.get(position).isright = false;
        } else {
            //选择
            hongbaoList.get(position).isright = true;
        }
    }

    @Override
    public void OnCheckBoxJiaxiClick(int position, boolean isChecked, List<projectInitBean.CouponListBean> jiaxiList) {
        if (isChecked) {
            //取消选择
            jiaxiList.get(position).isRight = false;
        } else {
            //选择
            jiaxiList.get(position).isRight = true;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadCast);
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }

    class HbBroadCast extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            //投资金额
            certainMoney = intent.getStringExtra("money");
            jiaxiInverstMoney = intent.getStringExtra("jiaxiInverstMoney");
            //红包金额
            AllHBmoney = (int) intent.getIntExtra("HBmoney", 0);
            //红包个数
            int hbCount = intent.getIntExtra("hbCount", 0);
            //加息卷张数
            int jiaxijuanCount = intent.getIntExtra("jiaxijuanCount", 0);
            //加息卷百分比
            double jiaxiPercentage = intent.getDoubleExtra("jiaxiPercentage", 0);
            //加息卷收益
            jiaxiMoney = intent.getDoubleExtra("jiaxiMoney", 0);
            //是否超过可投资金额
            boolean isAllmost = intent.getBooleanExtra("isAllmost", false);
            boolean isJiaxi = intent.getBooleanExtra("isJiaxi", false);
            choose_hb.setText(AllHBmoney + "");
            count.setText(hbCount + "");

            jiaxi_count.setText(jiaxijuanCount + "");
            jiaxi_percentage.setText(jiaxiPercentage + "");
            jiaxi_profit.setText(jiaxiMoney + "");
            if (certainMoney != null)
                increase.setText(certainMoney);
            if (jiaxiInverstMoney != null)
                jiaxi_increase.setText(jiaxiInverstMoney);
            if (!isJiaxi) {
                if (certainMoney != null && certainMoneyLast != null &&!isAllmost) {
                    if ((Integer.parseInt(certainMoney) - Integer.parseInt(certainMoneyLast)) > 0) {
                        //选择红包的才能使用的金额大于 用户投资的金额
                        Toast toast = Toast.makeText(HbChooseActivity.this, "在原投资额上需增加投资额¥" + (Integer.parseInt(certainMoney) - Integer.parseInt(certainMoneyLast)), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }

            }
        }
    }

//    class HongbaoComparator implements Comparator<HbPriorityBean.UserHongbaoListBean> {
//
//        @Override
//        public int compare(HbPriorityBean.UserHongbaoListBean bean, HbPriorityBean.UserHongbaoListBean bean1) {
//            if (bean.getInvestFullMomey() > bean1.getInvestFullMomey()) {
//                return 1;
//            } else if (bean.getInvestFullMomey() == bean1.getInvestFullMomey()) {
//                return 0;
//            } else {
//                return -1;
//            }
//        }
//    }


    @Override
    public void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        MobclickAgent.onResume(this);
        super.onResume();
    }
}