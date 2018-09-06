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

import com.rongxun.xqlc.Beans.PlatformEventBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.DateUtil;

import java.util.List;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.adapter
 * Author: HouShengLi
 * Time: 2017/06/03 17:18
 * E-mail: 13967189624@163.com
 * Description:
 */

public class PlatformEventAdapter extends BaseAdapter {

    private List<PlatformEventBean.ActivityListBean> datas;
    private Context mContext;
    private LayoutInflater inflater;

    public PlatformEventAdapter(List<PlatformEventBean.ActivityListBean> datas, Context mContext) {
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
            convertView = inflater.inflate(R.layout.discover_item_platform_event, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /////////////////////设置控件/////////////////////////
        PlatformEventBean.ActivityListBean bean = datas.get(position);
        DateUtil util = DateUtil.getInstance();

        if (bean.getStatus() == 1) {
            //隐藏活动结束
            holder.relFinish.setVisibility(View.GONE);
        } else {
            holder.relFinish.setVisibility(View.VISIBLE);
        }

        holder.txtTitle.setText(bean.getTitle());
        holder.txtDate.setText(util.getDayOrMonthOrYear(bean.getCreateDate()));

        Glide.with(mContext)
                .load(bean.getImgApp())
//                .placeholder(R.mipmap.error)
//                .error(R.mipmap.error)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
        return convertView;
    }

    class ViewHolder {

        TextView txtTitle;
        TextView txtDate;
        ImageView image;
        RelativeLayout relFinish;//活动是否结束

        public ViewHolder(View view) {

            txtTitle = (TextView) view.findViewById(R.id.discover_platform_txt_title);
            txtDate = (TextView) view.findViewById(R.id.discover_platform_txt_date);
            image = (ImageView) view.findViewById(R.id.discover_platform_image);
            relFinish = (RelativeLayout) view.findViewById(R.id.discover_platform_rel_finish);
        }
    }
}
