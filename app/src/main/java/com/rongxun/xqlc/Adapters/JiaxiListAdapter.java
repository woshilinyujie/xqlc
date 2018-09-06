package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.UserJIaxiBean;
import com.rongxun.xqlc.Beans.user.UserHongbaoViews;
import com.rongxun.xqlc.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/11/5.
 */
public class JiaxiListAdapter extends BaseAdapter {
    DecimalFormat df1 = new DecimalFormat("#0.00");
    private List<UserJIaxiBean.UserCouponViewsBean> hongbaoList;
    private LayoutInflater mInflater;
    private Context context;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd");

    public JiaxiListAdapter(Context context, List<UserJIaxiBean.UserCouponViewsBean> hongbaoList, LayoutInflater mInflater) {
        this.context = context;
        this.hongbaoList = hongbaoList;
        this.mInflater = mInflater;
    }

    public List<UserJIaxiBean.UserCouponViewsBean> getHongbaoList() {
        return hongbaoList;
    }

    public void setHongbaoList(List<UserJIaxiBean.UserCouponViewsBean> hongbaoList) {
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
        UserJIaxiBean.UserCouponViewsBean entity = hongbaoList.get(position);

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.jiaxi_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        String type ="";
//        if(isApp==1&&isPc==1){
//            type ="—通用";
//        }else if(isApp==1&& isPc!=1){
//            type ="—移动端";
//        }else if(isApp!=1&& isPc==1){
//            type ="—电脑端";
//        }

//        viewHolder.money.setText("投资金额满" + entity.getInvestFullMomey() + " 元可用");

        if (entity.getAmountMin() == 0 && entity.getAmountMax() > 0) {
            viewHolder.money.setText("投资金额：小于" + entity.getAmountMax() + "元可用");
        } else if (entity.getAmountMin() > 0 && entity.getAmountMax() == 0) {
            viewHolder.money.setText("投资金额：大于" + entity.getAmountMin() + "元可用");
        } else if (entity.getAmountMin() == 0 && entity.getAmountMax() == 0) {
            viewHolder.money.setText("投资金额：无限制");
        } else {
            viewHolder.money.setText("投资金额：" + entity.getAmountMin() + "元至"+entity.getAmountMax()+"元可用");
        }

        viewHolder.hongbaoListItemType.setText(entity.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        viewHolder.hongbaoListItemDeadLine.setText("有效期至:" + sdf.format(entity.getEndTime()));

        if (entity.getTimeLimitStart() == 0 && entity.getTimeLimitEnd() == 0) {
            viewHolder.hongbaoListItemTimeLine.setText("项目期限：无限制");
        } else if (entity.getTimeLimitStart() > 0 && entity.getTimeLimitEnd() == 0) {
            viewHolder.hongbaoListItemTimeLine.setText("项目期限：大于" + entity.getTimeLimitStart() + "天可用");
        } else if (entity.getTimeLimitStart() == 0 && entity.getTimeLimitEnd() > 0) {
            viewHolder.hongbaoListItemTimeLine.setText("项目期限：小于" + entity.getTimeLimitEnd() + "天可用");
        } else {
            viewHolder.hongbaoListItemTimeLine.setText("项目期限：" + entity.getTimeLimitStart() + "天至" + entity.getTimeLimitEnd() + "天可用");
        }
        if(entity.getDays()==0){
            viewHolder.day.setText("无限制");
        }else{

            viewHolder.day.setText("加息时长："+ entity.getDays()+"天");
        }

        viewHolder.hongbaoListItemNumber.setText(df1.format(entity.getApr()* 36)  + "%");
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'hong_bao_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {

        TextView day;
        TextView hongbaoListItemNumber;
        TextView hongbaoListItemType;
        TextView hongbaoListItemTimeLine;
        TextView hongbaoListItemDeadLine;
        TextView money;

        ViewHolder(View view) {
            hongbaoListItemNumber = (TextView) view.findViewById(R.id.jiaxi_list_item_number);
            hongbaoListItemType = (TextView) view.findViewById(R.id.jiaxi_list_item_type);
            hongbaoListItemTimeLine = (TextView) view.findViewById(R.id.jiaxi_list_item_time_line);
            hongbaoListItemDeadLine = (TextView) view.findViewById(R.id.jiaxi_list_item_dead_line);
            money = (TextView) view.findViewById(R.id.jiaxi_list_item_money_line);
            day = (TextView) view.findViewById(R.id.jiaxi_list_item_day);
        }
    }
}
