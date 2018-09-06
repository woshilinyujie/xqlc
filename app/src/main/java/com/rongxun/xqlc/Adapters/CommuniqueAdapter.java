package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.CommuniqueBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.DateUtil;

import java.util.List;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.adapter
 * Author: HouShengLi
 * Time: 2017/06/03 18:08
 * E-mail: 13967189624@163.com
 * Description:
 */

public class CommuniqueAdapter extends BaseAdapter {

    private List<CommuniqueBean.DataBean> datas;
    private Context mContext;
    private LayoutInflater inflater;

    public CommuniqueAdapter(List<CommuniqueBean.DataBean> datas, Context mContext) {
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
            convertView = inflater.inflate(R.layout.discover_item_communique, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /////////////////////设置控件/////////////////////////
        CommuniqueBean.DataBean bean = datas.get(position);
        DateUtil dateUtil = DateUtil.getInstance();
        holder.txtTitle.setText(bean.getTitle());
        holder.txtDate.setText(dateUtil.getDayOrMonthOrYear(bean.getCreateDate()));

        return convertView;
    }

    class ViewHolder {

        TextView txtTitle;
        TextView txtDate;

        public ViewHolder(View view) {

            txtTitle = (TextView) view.findViewById(R.id.discover_communique_txt_title);
            txtDate = (TextView) view.findViewById(R.id.discover_communique_txt_date);
        }
    }
}
