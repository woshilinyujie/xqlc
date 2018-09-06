package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rongxun.xqlc.R;

import java.util.List;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Adapters
 * Author: HouShengLi
 * Time: 2017/07/17 14:23
 * E-mail: 13967189624@163.com
 * Description:
 */

public class ProjectDetailAdapter2 extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<String> data;
    private int resId;

    public ProjectDetailAdapter2(Context mContext, List<String> data, int resId) {

        this.mContext = mContext;
        this.data = data;
        this.resId = resId;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.project_detail_item_lv_in, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtContent.setText(data.get(position));
        holder.txtDot.setBackgroundResource(resId);
        

        return convertView;
    }

    class ViewHolder {

        TextView txtDot;//圆点
        TextView txtContent;//内容

        public ViewHolder(View v) {

            txtDot = (TextView) v.findViewById(R.id.project_detail_txt_dot);
            txtContent = (TextView) v.findViewById(R.id.project_detail_txt_content);
        }
    }
}
