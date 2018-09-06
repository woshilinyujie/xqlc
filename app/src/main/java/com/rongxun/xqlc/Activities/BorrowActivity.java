package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rongxun.xqlc.Beans.BorrowActivityBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.InvestListView;
import com.rongxun.xqlc.UI.LoadListView;
import com.rongxun.xqlc.UI.PullToRefreshLayout;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class BorrowActivity extends MyBaseActivity {

    private InvestListView list;
    private TextView balck;
    private int id;

    private int page = 1;//页数
    private int size = 10;//每页显示的条数
    private PullToRefreshLayout refreshLayout;
    private Gson gson = new Gson();
    private List<BorrowActivityBean.DataBean.ResultBean> datas;
    private int allItem;
    private BorrowAdapter borrowAdapter;
    private TextView msg;
    private int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        initView();
        initListener();
        initDate();
    }

    private void initDate() {
        RequestForListData(AppConstants.URL_SUFFIX + "/rest/loanagreementList");
    }

    private void initListener() {
        balck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //刷新监听
        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener()

        {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // TODO: 2017/6/9 0009
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //页数加1
                page++;
                //请求数据
                RequestForListData(AppConstants.URL_SUFFIX + "/rest/loanagreementList");
            }
        });
    }

    private void initView() {
        datas = new ArrayList<>();
        id = getIntent().getIntExtra("id", 0);
        projectId = getIntent().getIntExtra("projectId", 0);
        list = (InvestListView) findViewById(R.id.borrow_list);
        balck = (TextView) findViewById(R.id.borrow_back);
        msg = (TextView) findViewById(R.id.borrow_msg);
        refreshLayout = (PullToRefreshLayout) findViewById(R.id.invest_record_refresh);
        borrowAdapter = new BorrowAdapter();
        list.setAdapter(borrowAdapter);
    }


    class BorrowAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final BorrowActivityBean.DataBean.ResultBean resultBean = datas.get(position);
            ViewHolder holder;
            if (convertView == null) {

                convertView = View.inflate(BorrowActivity.this, R.layout.borrow_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.money.setText(resultBean.getMoney() + " 元");
            String codeName="*"+resultBean.getRealname().substring(1,resultBean.getRealname().length());
            holder.name.setText(codeName);
            holder.borrow_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BorrowActivity.this, MoneyNegotiateActivity.class);
                    intent.putExtra("itemId", id);
                    intent.putExtra("projectId",projectId);
                    intent.putExtra("LoanAgreementUrl", resultBean.getLoangreementUrl());
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class ViewHolder {

        TextView name;
        TextView money;
        RelativeLayout borrow_rl;

        public ViewHolder(View view) {

            name = (TextView) view.findViewById(R.id.borrow_item_name);
            money = (TextView) view.findViewById(R.id.borrow_item_money);
            borrow_rl = (RelativeLayout) view.findViewById(R.id.borrow_rl);
        }
    }

    public void RequestForListData(String basicUrl) {
        String sss = PreferenceUtil.getPrefString(this, "loginToken", "");
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("id", id + "")
                .addParams("pager.pageSize", size + "")
                .addParams("pager.pageNumber", page + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//加载失败
                        setNoRecordBg();
                        page--;
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        BorrowActivityBean borrowActivityBean = gson.fromJson(response, BorrowActivityBean.class);
                        if(page==1){
                            if (borrowActivityBean.getData() != null && borrowActivityBean.getData().getResult() != null &&
                                    borrowActivityBean.getData().getResult().size() == 0) {
                                datas.addAll(borrowActivityBean.getData().getResult());
                                list.setVisibility(View.GONE);
                                msg.setVisibility(View.VISIBLE);
                                msg.setText(borrowActivityBean.getMsg());
                                return;
                            }
                        }
                        if (borrowActivityBean.getData() != null && borrowActivityBean.getData().getResult() != null &&
                                borrowActivityBean.getData().getResult().size() > 0) {
                            datas.addAll(borrowActivityBean.getData().getResult());
                        }

                        if (datas.size() == allItem) {
                            refreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                        } else {
                            //加载成功
                            refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                        allItem = datas.size();
                        setNoRecordBg();
                        borrowAdapter.notifyDataSetChanged();
                    }
                });
    }


    /**
     * 设置无记录的背景
     */
    private void setNoRecordBg() {


    }


}
