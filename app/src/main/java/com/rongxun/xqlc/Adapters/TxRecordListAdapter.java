package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.funds.UserCash;
import com.rongxun.xqlc.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/2.
 */
public class TxRecordListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflate;
    private List<UserCash> txList;
    private SimpleDateFormat simpleDateFormat;
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");


    public TxRecordListAdapter(Context context, List<UserCash> txList, LayoutInflater inflate) {

        this.context = context;
        this.txList = txList;
        this.inflate = inflate;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    }

    public List<UserCash> getTxList() {
        return txList;
    }

    public void setTxList(List<UserCash> txList) {
        this.txList = txList;
    }

    @Override
    public int getCount() {
        return txList.size();
    }

    @Override
    public Object getItem(int position) {
        return txList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        UserCash entity = txList.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflate.inflate(R.layout.tx_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txListItemStatus.setText(entity.getStatusShow());
        if (entity.getCreateDate() != null) {
            viewHolder.txListItemDate.setText(simpleDateFormat.format(new Date(entity.getCreateDate())));
        }
        viewHolder.txListItemMoney.setText(df.format((Float.parseFloat(entity.getMoney()) - Float.parseFloat(entity.getFee()))) + "");
        viewHolder.txListItemBankname.setText(entity.getBankName() + "(尾号" + entity.getCardNo().substring(entity.getCardNo().length() - 4, entity.getCardNo().length()) + ")");

        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'tx_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {

        TextView txListItemBankname;
        TextView txListItemMoney;
        TextView txListItemDate;
        TextView txListItemStatus;

        ViewHolder(View view) {
            txListItemBankname = (TextView) view.findViewById(R.id.tx_list_item_bankname);
            txListItemMoney = (TextView) view.findViewById(R.id.tx_list_item_money);
            txListItemDate = (TextView) view.findViewById(R.id.tx_list_item_date);
            txListItemStatus = (TextView) view.findViewById(R.id.tx_list_item_status);
        }
    }
}
