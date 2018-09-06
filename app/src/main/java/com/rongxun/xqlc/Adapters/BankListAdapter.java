package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rongxun.xqlc.Beans.bank.BankCard;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.R;

import java.util.List;

/**
 * Created by Administrator on 2015/11/9.
 */
public class BankListAdapter extends BaseAdapter {
    private List<BankList.BankCardListBean> banList;
    private LayoutInflater mInflater;
    private Context context;

    public BankListAdapter(Context context, List<BankList.BankCardListBean> banList) {
        this.context = context;
        this.banList = banList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return banList.size();
    }

    @Override
    public BankList.BankCardListBean getItem(int position) {
        return banList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BankList.BankCardListBean entity = banList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bank_list_view_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(entity.getBankIcon()).into(viewHolder.bankListViewImg);
        viewHolder.bankListViewText.setText(entity.getBankName());
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'bank_list_view_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {

        TextView bankListViewText;
        ImageView bankListViewImg;

        ViewHolder(View view) {
            bankListViewText = (TextView) view.findViewById(R.id.bank_list_view_text);
            bankListViewImg = (ImageView) view.findViewById(R.id.bank_list_view_img);
        }
    }
}
