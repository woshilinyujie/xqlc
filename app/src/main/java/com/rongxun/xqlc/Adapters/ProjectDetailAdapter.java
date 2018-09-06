package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.ProjectDetailListView;

import java.util.List;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Adapters
 * Author: HouShengLi
 * Time: 2017/07/17 13:50
 * E-mail: 13967189624@163.com
 * Description:
 */

public class ProjectDetailAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<List<String>> data;

    public ProjectDetailAdapter(Context mContext, List<List<String>> data) {
        this.mContext = mContext;
        this.data = data;
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
            convertView = inflater.inflate(R.layout.project_detail_item_lv_out, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ///////////设置数据//////////

        switch (position) {

            case 0:
                //图标
                holder.img.setBackgroundResource(R.mipmap.project_debtor);
                //标题
                holder.txtTitle.setText("债务人信息");
                holder.txtTitle.setTextColor(Color.parseColor("#ff7977"));
                //嵌套LV
                holder.lv.setAdapter(new ProjectDetailAdapter2(mContext, data.get(0),
                        R.drawable.shape_project_detail_debtor));
                break;
            case 1:
                //图标
                holder.img.setBackgroundResource(R.mipmap.project_desc);
                //标题
                holder.txtTitle.setText("项目描述");
                holder.txtTitle.setTextColor(Color.parseColor("#a5d579"));
                //嵌套LV
                holder.lv.setAdapter(new ProjectDetailAdapter2(mContext, data.get(1),
                        R.drawable.shape_project_detail_desc));
                break;
            case 2:
                //图标
                holder.img.setBackgroundResource(R.mipmap.project_money_use);
                //标题
                holder.txtTitle.setText("资金用途");
                holder.txtTitle.setTextColor(Color.parseColor("#56c3f5"));
                //嵌套LV
                holder.lv.setAdapter(new ProjectDetailAdapter2(mContext, data.get(2),
                        R.drawable.shape_project_detail_money_use));
                break;
            case 3:
                //图标
                holder.img.setBackgroundResource(R.mipmap.project_repay_source);
                //标题
                holder.txtTitle.setText("还款来源");
                holder.txtTitle.setTextColor(Color.parseColor("#ff9d46"));
                //嵌套LV
                holder.lv.setAdapter(new ProjectDetailAdapter2(mContext, data.get(3),
                        R.drawable.shape_project_detail_repay_source));
                break;
        }

        return convertView;
    }


    class ViewHolder {

        ImageView img;
        TextView txtTitle;
        ProjectDetailListView lv;

        public ViewHolder(View v) {

            img = (ImageView) v.findViewById(R.id.project_detail_img_out);
            txtTitle = (TextView) v.findViewById(R.id.project_detail_txt_out);
            lv = (ProjectDetailListView) v.findViewById(R.id.project_detail_in_lv);
        }

    }
}
