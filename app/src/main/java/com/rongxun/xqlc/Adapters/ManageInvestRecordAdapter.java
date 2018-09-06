package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rongxun.xqlc.Beans.Borrow.InvestRecordBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.DateUtil;
import com.rongxun.xqlc.Util.DecimalFormatUtil;

import java.util.List;


/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.adapter
 * Author: HouShengLi
 * Time: 2017/06/02 18:58
 * E-mail: 13967189624@163.com
 * Description:
 */

public class ManageInvestRecordAdapter extends BaseAdapter {

    private List<InvestRecordBean.BorrowTenderItemListBean> datas;
    private Context mContext;
    private LayoutInflater inflater;


    public ManageInvestRecordAdapter(List<InvestRecordBean.BorrowTenderItemListBean> datas, Context mContext) {
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

            convertView = inflater.inflate(R.layout.manage_item_investment_records, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        DateUtil dateUtil = DateUtil.getInstance();
        DecimalFormatUtil util = DecimalFormatUtil.getInstance();

        InvestRecordBean.BorrowTenderItemListBean bean = datas.get(position);

        holder.txtPhone.setText(bean.getUsername());
        holder.txtDate.setText(dateUtil.dateFormat2(bean.getCreateDate()));
        //取整数部分
        String a = util.getInt(Double.parseDouble(bean.getAccount()));
        holder.txtMoney.setText("¥"+util.getLong(Long.parseLong(a)));

        return convertView;
    }


    class ViewHolder {

        TextView txtPhone;
        TextView txtDate;
        TextView txtMoney;

        public ViewHolder(View view) {

            txtPhone = (TextView) view.findViewById(R.id.manage_investment_txt_phone);
            txtDate = (TextView) view.findViewById(R.id.manage_investment_txt_date);
            txtMoney = (TextView) view.findViewById(R.id.manage_investment_txt_money);
        }
    }
}
