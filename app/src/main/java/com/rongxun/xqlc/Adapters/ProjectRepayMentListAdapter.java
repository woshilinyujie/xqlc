package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.Borrow.BorrowRepaymentDetailList;
import com.rongxun.xqlc.R;

import java.util.List;

/**
 * Created by Administrator on 2015/11/17.
 */
public class ProjectRepayMentListAdapter extends BaseAdapter {
    private List<BorrowRepaymentDetailList> repayList;
    private LayoutInflater mInflater;
    private Context context;


    public ProjectRepayMentListAdapter(Context context, List<BorrowRepaymentDetailList> repayList, LayoutInflater mInflater) {
        this.context = context;
        this.repayList = repayList;
        this.mInflater = mInflater;
    }

    public List<BorrowRepaymentDetailList> getRepayList() {
        return repayList;
    }

    public void setRepayList(List<BorrowRepaymentDetailList> repayList) {
        this.repayList = repayList;
    }

    @Override
    public int getCount() {
        return repayList.size();
    }

    @Override
    public Object getItem(int position) {
        return repayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BorrowRepaymentDetailList entity = repayList.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.project_repay_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.projectRepaymentItemOrder.setText(entity.getOrderNum() + "");
        viewHolder.projectRepaymentItemDate.setText("第" + entity.getRepaymentDateInt() + "天");
        viewHolder.projectRepaymentItemCapital.setText("￥" + entity.getCapital() + "");
        viewHolder.projectRepaymentItemInterest.setText("￥" + entity.getInterest() + "");

        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'project_repay_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {

        TextView projectRepaymentItemOrder;
        TextView projectRepaymentItemDate;
        TextView projectRepaymentItemCapital;
        TextView projectRepaymentItemInterest;

        ViewHolder(View view) {
            projectRepaymentItemOrder = (TextView) view.findViewById(R.id.project_repayment_item_order);
            projectRepaymentItemDate = (TextView) view.findViewById(R.id.project_repayment_item_date);
            projectRepaymentItemCapital = (TextView) view.findViewById(R.id.project_repayment_item_capital);
            projectRepaymentItemInterest = (TextView) view.findViewById(R.id.project_repayment_item_interest);
        }
    }
}
