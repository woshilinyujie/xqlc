package com.rongxun.xqlc.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.user.UserHongbaoViews;
import com.rongxun.xqlc.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/11/5.
 */
public class HongBaoListAdapter extends BaseAdapter {

    private List<UserHongbaoViews> hongbaoList;
    private LayoutInflater mInflater;
    private Activity context;
    String count;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd");

    public HongBaoListAdapter(Activity context, List<UserHongbaoViews> hongbaoList, LayoutInflater mInflater) {
        this.context = context;
        this.hongbaoList = hongbaoList;
        this.mInflater = mInflater;
    }

    public List<UserHongbaoViews> getHongbaoList() {
        return hongbaoList;
    }

    public void setHongbaoList(List<UserHongbaoViews> hongbaoList) {
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
        UserHongbaoViews entity = hongbaoList.get(position);

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.hong_bao_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.money.setText("2.项目金额满" + entity.getInvestFullMomey() + " 元可用");
        viewHolder.hongbaoListItemType.setText(entity.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        viewHolder.hongbaoListItemDeadLine.setText("有效期至:" + sdf.format(entity.getEndTime()));
        viewHolder.hongbaoListItemTimeLine.setText("1.项目期限满" + entity.getLimitStart() + "天可用");
        SpannableString moneyText = new SpannableString(entity.getMoney().intValue() + "");
        viewHolder.hongbaoListItemNumber.setText(moneyText);
        if(position==hongbaoList.size()-1){
            viewHolder.count_rl.setVisibility(View.VISIBLE);
            viewHolder.jx_foot_use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("HomeFragmentBroadCast");
                    intent.putExtra("current",1);
                    context.sendBroadcast(intent);
                    context.finish();
                }
            });
            viewHolder.jx_foot_count.setText("共"+count+"张");
        }else{
            viewHolder.count_rl.setVisibility(View.GONE);
        }
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'hong_bao_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {

        RelativeLayout count_rl;
        TextView hongbaoListItemNumber;
        TextView hongbaoListItemType;
        TextView hongbaoListItemTimeLine;
        TextView hongbaoListItemDeadLine;
        TextView money;
        TextView jx_foot_count;
        View jx_foot_use;

        ViewHolder(View view) {
            hongbaoListItemNumber = (TextView) view.findViewById(R.id.hongbao_list_item_number);
            hongbaoListItemType = (TextView) view.findViewById(R.id.hongbao_list_item_type);
            hongbaoListItemTimeLine = (TextView) view.findViewById(R.id.hongbao_list_item_time_line);
            hongbaoListItemDeadLine = (TextView) view.findViewById(R.id.hongbao_list_item_dead_line);
            money = (TextView) view.findViewById(R.id.hongbao_list_item_money_line);
            jx_foot_count = (TextView) view.findViewById(R.id.jx_foot_count);
            jx_foot_use =view.findViewById(R.id.jx_foot_use);
            count_rl = (RelativeLayout) view.findViewById(R.id.count_rl);
        }
    }


    public void setCount(String count){
        this.count=count;
    }
}
