package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.InvestmentStatementsBean;
import com.rongxun.xqlc.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2015/10/30.
 */
public class InvestRecordListAdapter extends BaseAdapter {

    private List<InvestmentStatementsBean.UserTenderListBean> tenderList;
    private LayoutInflater mInflater;
    private Context context;
    DecimalFormat df = new DecimalFormat("#0.00");

    public InvestRecordListAdapter(Context context, List<InvestmentStatementsBean.UserTenderListBean> tenderList) {
        this.context = context;
        this.tenderList = tenderList;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<InvestmentStatementsBean.UserTenderListBean> getTenderList() {
        return tenderList;
    }

    public void setTenderList(List<InvestmentStatementsBean.UserTenderListBean> tenderList) {
        this.tenderList = tenderList;
    }

    @Override
    public int getCount() {
        return tenderList.size();
    }

    @Override
    public Object getItem(int position) {
        return tenderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InvestmentStatementsBean.UserTenderListBean entity = tenderList.get(position);
        ListItemViewHolder viewHolder = null;
        try {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.invest_record_list_view_item_layout, null);
                viewHolder = new ListItemViewHolder();
                viewHolder.tenderRecordItemBorrowName = (TextView) convertView.findViewById(R.id.tender_record_item_borrow_name);
                viewHolder.tenderRecordItemBorrowCount = (TextView) convertView.findViewById(R.id.tender_record_item_borrow_count);
                viewHolder.tenderRecordItemTime = (TextView) convertView.findViewById(R.id.tender_record_item_time);
                viewHolder.tenderRecordItemProfit = (TextView) convertView.findViewById(R.id.tender_record_item_profit);
                viewHolder.tenderRecordItemRecentTime = (TextView) convertView.findViewById(R.id.tender_record_item_recent_time);
                viewHolder.state = (TextView) convertView.findViewById(R.id.tender_record_item_borrow_state);
                viewHolder.tenderRecordItemProfit = (TextView) convertView.findViewById(R.id.tender_record_item_profit);
                viewHolder.recentRl = (RelativeLayout) convertView.findViewById(R.id.tender_record_item_recent_rl);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ListItemViewHolder) convertView.getTag();
            }


            String format = df.format(Double.parseDouble(entity.getTenderAccount()));
            viewHolder.tenderRecordItemBorrowName.setText(entity.getBorrowName());
            viewHolder.tenderRecordItemBorrowCount.setText(format);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            viewHolder.tenderRecordItemTime.setText(sdf.format(new Date(entity.getCreateDate())));
            viewHolder.tenderRecordItemProfit.setText(entity.getInterest());
            viewHolder.state.setText(entity.getBorrowStatusShow());
            if(entity.getFinalRepayDateString()!=null){
                viewHolder.recentRl.setVisibility(View.VISIBLE);
                viewHolder.tenderRecordItemRecentTime.setText(entity.getFinalRepayDateString());
            }else{
                viewHolder.recentRl.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'invest_record_list_view_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ListItemViewHolder {
        TextView tenderRecordItemBorrowName;
        TextView tenderRecordItemBorrowCount;
        TextView tenderRecordItemTime;
        TextView tenderRecordItemProfit;
        TextView tenderRecordItemRecentTime;
        TextView state;
        RelativeLayout recentRl;
    }
}
