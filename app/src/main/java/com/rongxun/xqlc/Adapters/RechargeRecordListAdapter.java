package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.funds.UserRecharge;
import com.rongxun.xqlc.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/2.
 */
public class RechargeRecordListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflate;
    private List<UserRecharge> chargeList;
    private SimpleDateFormat simpleDateFormat;


    public RechargeRecordListAdapter(Context context, List<UserRecharge> chargeList, LayoutInflater inflate) {

        this.context = context;
        this.chargeList = chargeList;
        this.inflate = inflate;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    }

    public List<UserRecharge> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<UserRecharge> chargeList) {
        this.chargeList = chargeList;
    }

    @Override
    public int getCount() {
        return chargeList.size();
    }

    @Override
    public Object getItem(int position) {
        return chargeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserRecharge entity = chargeList.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflate.inflate(R.layout.recharge_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.rechargeListItemStatus.setText(entity.getStatusShow());
        if (entity.getCreateDate() != null) {
            viewHolder.rechargeListItemDate.setText(simpleDateFormat.format(new Date(entity.getCreateDate())));
        }
        viewHolder.rechargeListItemMoney.setText(entity.getMoney());
        viewHolder.rechargeListItemBankname.setText(entity.getName());
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'recharge_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {

        TextView rechargeListItemBankname;
        TextView rechargeListItemMoney;
        TextView rechargeListItemDate;
        TextView rechargeListItemStatus;

        ViewHolder(View view) {
            rechargeListItemBankname = (TextView) view.findViewById(R.id.recharge_list_item_bankname);
            rechargeListItemMoney = (TextView) view.findViewById(R.id.recharge_list_item_money);
            rechargeListItemDate = (TextView) view.findViewById(R.id.recharge_list_item_date);
            rechargeListItemStatus = (TextView) view.findViewById(R.id.recharge_list_item_status);
        }
    }
}
