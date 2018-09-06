package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rongxun.xqlc.Beans.home.ActivityCenterBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.DateUtil;
import com.rongxun.xqlc.Util.DecimalFormatUtil;

import java.util.List;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Adapters
 * Author: HouShengLi
 * Time: 2017/07/25 14:16
 * E-mail: 13967189624@163.com
 * Description:
 */

public class ActivityCenterAdapter extends BaseAdapter {

    private List<ActivityCenterBean.ActivityListBean> datas;
    private Context mContext;
    private LayoutInflater inflater;


    public ActivityCenterAdapter(List<ActivityCenterBean.ActivityListBean> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_activity_center, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //////////////////////设置数据///////////////////////
        DateUtil dateUtil = DateUtil.getInstance();
        DecimalFormatUtil util = DecimalFormatUtil.getInstance();

        ActivityCenterBean.ActivityListBean bean = datas.get(position);

        holder.txtTitle.setText(bean.getTitle());
        holder.txtStartTime.setText(dateUtil.getDayOrMonthOrYear4(bean.getStartTime()));
        holder.txtEndTime.setText(dateUtil.getDayOrMonthOrYear4(bean.getEndTime()));
        if (bean.getStatus() == 1) {
            //活动进行中
            holder.relEnd.setVisibility(View.GONE);
        } else {
            //活动结束
            holder.relEnd.setVisibility(View.VISIBLE);
        }

        //设置图片
        Glide.with(mContext)
                .load(bean.getImgApp())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

        return convertView;
    }


    class ViewHolder {

        TextView txtTitle;
        TextView txtStartTime;
        TextView txtEndTime;
        ImageView image;
        RelativeLayout relEnd;

        public ViewHolder(View view) {

            txtTitle = (TextView) view.findViewById(R.id.activity_center_item_title);
            txtStartTime = (TextView) view.findViewById(R.id.activity_center_item_start_time);
            txtEndTime = (TextView) view.findViewById(R.id.activity_center_item_end_time);
            image = (ImageView) view.findViewById(R.id.activity_center_item_image);
            relEnd = (RelativeLayout) view.findViewById(R.id.activity_center_item_end);

        }
    }
}
