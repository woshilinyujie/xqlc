package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rongxun.xqlc.Beans.PlatformDynamicBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;

import java.util.List;

/**
 * Created by oguig on 2017/7/27.
 */

public class PlatformDynamicAdapter extends BaseAdapter {
    private Context mContext;
    private List<PlatformDynamicBean.MediaReportDTOListBean> datas;
    private LayoutInflater inflater;

    public PlatformDynamicAdapter(Context mContext, List<PlatformDynamicBean.MediaReportDTOListBean> datas){
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
        PlatformDynamicAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_platform_dynamic, parent, false);
            holder = new PlatformDynamicAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PlatformDynamicAdapter.ViewHolder) convertView.getTag();
        }
        holder.dynamic_tv_title.setText(datas.get(position).getTitle());
        Glide.with(mContext)
                .load(datas.get(position).getCoverImg())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.dynamic_image);
        holder.dynamic_author.setText(datas.get(position).getAuthor());
        return convertView;
    }
    class ViewHolder {
        TextView dynamic_tv_title;
        ImageView dynamic_image;
        TextView dynamic_author;
        public ViewHolder(View view) {
            dynamic_tv_title=(TextView)view.findViewById(R.id.dynamic_tv_title);
            dynamic_image=(ImageView)view.findViewById(R.id.dynamic_image);
            dynamic_author=(TextView)view.findViewById(R.id.dynamic_author);


        }
    }
}
