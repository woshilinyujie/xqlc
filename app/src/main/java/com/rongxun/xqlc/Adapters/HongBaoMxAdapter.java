package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.funds.UserHongbao;
import com.rongxun.xqlc.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/11/5.
 */
public class HongBaoMxAdapter extends BaseAdapter {

    private List<UserHongbao> hongbaoList;
    private LayoutInflater mInflater;
    private Context context;


    public HongBaoMxAdapter(Context context, List<UserHongbao> hongbaoList, LayoutInflater mInflater) {
        this.context = context;
        this.hongbaoList = hongbaoList;
        this.mInflater = mInflater;
    }

    public List<UserHongbao> getHongbaoList() {
        return hongbaoList;
    }

    public void setHongbaoList(List<UserHongbao> hongbaoList) {
        this.hongbaoList = hongbaoList;
    }

    @Override
    public int getCount() {
        return hongbaoList.size();
    }

    @Override
    public Object getItem(int position) {
        return hongbaoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserHongbao entity = hongbaoList.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.hong_bao_mx_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.hongBaoMxItemTypeShow.setText(entity.getTypeShow());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.hongBaoMxItemDate.setText(sdf.format(entity.getCreateDate()));

        viewHolder.hongBaoMxItemHbno.setText("红包编号：" + entity.getHbNo());
        if (entity.getSignFlg() == 1) {
            viewHolder.hongBaoMxItemMoney.setTextColor(context.getResources().getColor(R.color.colorYellow));
            viewHolder.hongBaoMxItemMoney.setText("+" + entity.getMoney());
        } else {
            viewHolder.hongBaoMxItemMoney.setTextColor(context.getResources().getColor(R.color.colorGreen));
            viewHolder.hongBaoMxItemMoney.setText("—" + entity.getMoney());
        }


        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'hong_bao_mx_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {

        TextView hongBaoMxItemTypeShow;
        TextView hongBaoMxItemDate;
        TextView hongBaoMxItemMoney;
        TextView hongBaoMxItemHbno;

        ViewHolder(View view) {
            hongBaoMxItemTypeShow = (TextView) view.findViewById(R.id.hong_bao_mx_item_type_show);
            hongBaoMxItemDate = (TextView) view.findViewById(R.id.hong_bao_mx_item_date);
            hongBaoMxItemMoney = (TextView) view.findViewById(R.id.hong_bao_mx_item_money);
            hongBaoMxItemHbno = (TextView) view.findViewById(R.id.hong_bao_mx_item_hbno);
        }
    }
}
