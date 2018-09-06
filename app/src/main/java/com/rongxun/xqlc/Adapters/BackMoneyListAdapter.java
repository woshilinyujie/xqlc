package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.center.UserTender;
import com.rongxun.xqlc.Beans.center.UserTenderList;
import com.rongxun.xqlc.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2015/10/30.
 */
public class BackMoneyListAdapter extends BaseAdapter
{

    private List<UserTenderList.UserRepaymentListBean> tenderList;
    private LayoutInflater mInflater;
    private Context context;
    DecimalFormat df = new DecimalFormat("#0.00");

    public BackMoneyListAdapter(Context context, List<UserTenderList.UserRepaymentListBean> tenderList)
    {
        this.context = context;
        this.tenderList = tenderList;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<UserTenderList.UserRepaymentListBean> getTenderList()
    {
        return tenderList;
    }

    public void setTenderList(List<UserTenderList.UserRepaymentListBean> tenderList)
    {
        this.tenderList = tenderList;
    }

    @Override
    public int getCount()
    {
        return tenderList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return tenderList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        UserTenderList.UserRepaymentListBean entity = tenderList.get(position);
        ListItemViewHolder viewHolder = null;
            try{
                if (convertView == null)
                {
                    convertView = mInflater.inflate(R.layout.returned_money_list_view_item_layout, null);
                    viewHolder = new ListItemViewHolder();
                    viewHolder.tenderRecordItemBorrowName= (TextView) convertView.findViewById(R.id.tender_record_item_borrow_name);
                    viewHolder.tenderRecordItemBorrowCount= (TextView) convertView.findViewById(R.id.tender_record_item_borrow_count);
                    viewHolder.tenderRecordItemTime= (TextView) convertView.findViewById(R.id.tender_record_item_time);
                    viewHolder.tenderRecordItemProfit= (TextView) convertView.findViewById(R.id.tender_record_item_profit);
                    viewHolder.return_day_tv=(TextView)convertView.findViewById(R.id.return_day_tv);
                    viewHolder.return_time_tv=(TextView) convertView.findViewById(R.id.return_time_tv);
                    viewHolder.return_day_rl=(RelativeLayout)convertView.findViewById(R.id.return_day_rl);
                    convertView.setTag(viewHolder);
                } else
                {
                    viewHolder = (ListItemViewHolder) convertView.getTag();
                }
                String format = df.format(Double.parseDouble(entity.getWaitAccount()));
                viewHolder.tenderRecordItemBorrowName.setText(entity.getBorrowName());
                viewHolder.tenderRecordItemBorrowCount.setText(format);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                viewHolder.return_time_tv.setText(sdf.format(new Date(entity.getRepaymentDate())));
                viewHolder.tenderRecordItemTime.setText(sdf.format(new Date(entity.getTenderTime())));
                if(entity.getRepaymentStatus()==0){
                    viewHolder.return_day_rl.setVisibility(View.VISIBLE);
                }else if(entity.getRepaymentStatus()==1){
                    viewHolder.return_day_rl.setVisibility(View.GONE);
                }
                viewHolder.tenderRecordItemProfit.setText(entity.getWaitInterest());
                if(entity.getRefunDays()<=0){
                    viewHolder.return_day_tv.setText("回款中");
                }else {
                    viewHolder.return_day_tv.setText(entity.getRefunDays()+"天后回款");
                }







            }catch (Exception e){
            }
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'invest_record_list_view_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ListItemViewHolder
    {
        TextView tenderRecordItemBorrowName;
        TextView tenderRecordItemBorrowCount;
        TextView tenderRecordItemTime;
        TextView tenderRecordItemProfit;
        TextView return_day_tv;
        TextView return_time_tv;
        RelativeLayout return_day_rl;
    }
}
