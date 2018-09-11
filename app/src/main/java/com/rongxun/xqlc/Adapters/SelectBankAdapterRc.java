package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rongxun.xqlc.Beans.bank.BankCard;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.R;

import java.util.List;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.adapter
 * Author: HouShengLi
 * Time: 2017/09/06 14:44
 * E-mail: 13967189624@163.com
 * Description:
 */

public class SelectBankAdapterRc extends RecyclerView.Adapter<SelectBankAdapterRc.BankViewHolder> {

    private BankList list;
    private Context context;
    private LayoutInflater inflater;
    private int lastSelect;

    public SelectBankAdapterRc(Context context, BankList list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public BankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.select_bank_list_item1, parent, false);
        return new BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BankViewHolder holder, final int position) {

        //设置数据
        BankList.BankCardListBean bank = list.getBankCardList().get(position);
        //银行卡Name
        holder.txtBankName.setText(bank.getBankName());
        //限额
        holder.txtBankLimit.setText(bank.getBankLimit());
        //设置银行ICON
        String resName = "bank_" + bank.getBankId().toLowerCase();
        //本地ResId
        int resId = context.getResources().getIdentifier(resName, "mipmap", context.getPackageName());
        Glide.with(context)
                .load(resId)
                .into(holder.imageBankIcon);


    }


    @Override
    public int getItemCount() {

        return list.getBankCardList() != null ? list.getBankCardList().size() : 0;
    }


    class BankViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBankIcon;
        TextView txtBankName;
        TextView txtBankLimit;

        public BankViewHolder(View itemView) {
            super(itemView);
            imageBankIcon = (ImageView) itemView.findViewById(R.id.select_bank_list_item_iv);
            txtBankName = (TextView) itemView.findViewById(R.id.select_bank_list_item_bankname);
            txtBankLimit = (TextView) itemView.findViewById(R.id.select_bank_list_item_limit);
        }
    }


    public void setLastSelect(int lastSelect) {
        this.lastSelect = lastSelect;
    }

    /**
     * 接口
     */
    public interface OnClickChoose {
        void choose(int position);
    }

    public void setClickChoose(OnClickChoose clickChoose) {
        this.clickChoose = clickChoose;
    }

    private OnClickChoose clickChoose;
}
