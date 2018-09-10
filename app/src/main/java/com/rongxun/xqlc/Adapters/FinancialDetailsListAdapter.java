package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.CashBean;
import com.rongxun.xqlc.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 */
public class FinancialDetailsListAdapter extends BaseAdapter
{
    private List<CashBean.UserAccDetailListBean> tenderList;
    private LayoutInflater mInflater;
    private Context context;

    public FinancialDetailsListAdapter(Context context, List<CashBean.UserAccDetailListBean> tenderList)
    {
        this.context = context;
        this.tenderList = tenderList;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<CashBean.UserAccDetailListBean> getTenderList()
    {
        return tenderList;
    }

    public void setTenderList(List<CashBean.UserAccDetailListBean> tenderList)
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
        CashBean.UserAccDetailListBean entity = tenderList.get(position);
        ListItemViewHolder viewHolder = null;
        try{
            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.financial_details_list_layout, null);
                viewHolder = new ListItemViewHolder();
                viewHolder.financial_name= (TextView) convertView.findViewById(R.id.financial_name);
                viewHolder.financial_add_and_subtract =(TextView)convertView.findViewById(R.id.financial_add_and_subtract);
                viewHolder.financial_cash= (TextView) convertView.findViewById(R.id.financial_cash);
                viewHolder.financial_time= (TextView) convertView.findViewById(R.id.financial_time);
                viewHolder.financial_balance= (TextView) convertView.findViewById(R.id.financial_balance);
                convertView.setTag(viewHolder);
            } else
            {
                viewHolder = (ListItemViewHolder) convertView.getTag();
            }
            viewHolder.financial_name.setText(entity.getTypeShow());
            if(entity.getSign().equals("1")){
                viewHolder.financial_add_and_subtract.setText("+");
            }else {
                viewHolder.financial_add_and_subtract.setText("-");
            }
            viewHolder.financial_cash.setText(entity.getMoney());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            viewHolder.financial_time.setText(entity.getCreateDate());
            viewHolder.financial_balance.setText(entity.getAbleMoney());
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
        TextView financial_name;
        TextView financial_add_and_subtract;
        TextView financial_cash;
        TextView financial_time;
        TextView financial_balance;
    }
}
