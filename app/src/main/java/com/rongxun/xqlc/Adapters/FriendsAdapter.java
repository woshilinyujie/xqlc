package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.FriendsBean;
import com.rongxun.xqlc.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 */
public class FriendsAdapter extends BaseAdapter {
    private List<FriendsBean.FriendListBean> tenderList;
    private LayoutInflater mInflater;
    private Context context;

    public FriendsAdapter(Context context, List<FriendsBean.FriendListBean> tenderList) {
        this.context = context;
        this.tenderList = tenderList;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<FriendsBean.FriendListBean> getTenderList() {
        return tenderList;
    }

    public void setTenderList(List<FriendsBean.FriendListBean> tenderList) {
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
        FriendsBean.FriendListBean entity = tenderList.get(position);
        ListItemViewHolder viewHolder = null;
        try {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.friends_item, null);
                viewHolder = new ListItemViewHolder();
                viewHolder.friends_phone = (TextView) convertView.findViewById(R.id.friends_phone);
                viewHolder.friends_time = (TextView) convertView.findViewById(R.id.friends_time);
                viewHolder.friends_get_cash = (TextView) convertView.findViewById(R.id.friends_get_cash);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ListItemViewHolder) convertView.getTag();
            }
            viewHolder.friends_phone.setText(entity.getUsername());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            viewHolder.friends_time.setText(sdf.format(new Date(entity.getCreateDate())));
            java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
            viewHolder.friends_get_cash.setText(df.format(entity.getUadSumMoney()));
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
        TextView friends_phone;
        TextView friends_time;
        TextView friends_get_cash;
    }
}
