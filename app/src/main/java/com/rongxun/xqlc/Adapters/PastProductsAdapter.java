package com.rongxun.xqlc.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rongxun.xqlc.Activities.PastProductsActivity;
import com.rongxun.xqlc.Beans.Borrow.PastProductsBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.tipprogress.TipsProgressBar;
import com.rongxun.xqlc.Util.DecimalFormatUtil;

import java.util.List;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Adapters
 * Author: HouShengLi
 * Time: 2017/07/11 16:28
 * E-mail: 13967189624@163.com
 * Description:
 */

public class PastProductsAdapter extends BaseAdapter {

    private List<PastProductsBean.BorrowDTOListBean> list;
    private Context context;
    private LayoutInflater inflater;
    private String productFlag;//产品标识

    public PastProductsAdapter(Context context, List<PastProductsBean.BorrowDTOListBean> list, String productFlag) {
        this.list = list;
        this.context = context;
        this.productFlag = productFlag;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_past_product, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        ///////////////////////设置控件//////////////////////
        PastProductsBean.BorrowDTOListBean bean = list.get(position);

        DecimalFormatUtil util = DecimalFormatUtil.getInstance();

        //顶部分割线
        if (position == 0) {
            //显示
            holder.split.setVisibility(View.VISIBLE);
        } else {
            holder.split.setVisibility(View.GONE);
        }

        holder.txtMarkNum.setText(bean.getName());//设置 xxx标第xxx期
        holder.txtEarings.setText(util.getDouble2(bean.getApr()));//年化收益率
        if(bean.getAwardApr()!=0){
               holder.add.setVisibility(View.VISIBLE);
        }else{
            holder.add.setVisibility(View.INVISIBLE);
        }
        holder.txtAddRote.setText(util.getDouble2(bean.getAwardApr()));//增加的利率
        holder.txtInvestmentLimit.setText(bean.getTimeLimit());//投资期限
        holder.txtRemain.setText("剩余募集金额"+util.getInt(Double.parseDouble(bean.getBalance()))+"元");//剩余额度
        holder.txtBuyNum.setText(bean.getCount() + "");

        //设置售罄还是还款
        if (productFlag == PastProductsActivity.SHOUQIN) {
            //募集
            holder.imgSellorRepay.setBackgroundResource(R.drawable.shape_manage_gay);
            holder.imgSellorRepay.setText("满标");
            holder.bar.setVisibility(View.GONE);
            holder.quan.setVisibility(View.GONE);
            holder.txtRemain.setVisibility(View.GONE);
            holder.txtEarings.setTextColor(Color.parseColor("#999999"));
            holder.baifenhao.setTextColor(Color.parseColor("#999999"));
            holder.txtInvestmentLimit.setTextColor(Color.parseColor("#999999"));
            holder.txtMarkNum.setTextColor(Color.parseColor("#999999"));
            holder.tian.setTextColor(Color.parseColor("#999999"));

        } else {
            //售罄
            holder.imgSellorRepay.setBackgroundResource(R.drawable.shape_manage_blue);
            holder.imgSellorRepay.setText("抢购");
            holder.bar.setProgress(Integer.parseInt(bean.getSchedule()) * 10);
            if(bean.getUseHongbao()==0){
                holder.quan.setText("不可使用优惠券");
            }else{
                holder.quan.setText("可使用优惠券");
            }
        }

        //设置角标
        if (!bean.getCornerLableVal().equals("")) {
            holder.imgCorner.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(bean.getCornerLableVal())
                    .into(holder.imgCorner);
        } else {
            holder.imgCorner.setVisibility(View.GONE);
        }

        //设置 用户专享
        if (bean.getActivityOne() != null && !bean.getActivityOne().equals("")) {
            holder.txtExclusive.setVisibility(View.VISIBLE);
            holder.txtExclusive.setText(bean.getActivityOne());
        } else {
            holder.txtExclusive.setVisibility(View.GONE);
        }

        //设置 春天礼
        if (bean.getActivityTwo() != null && !bean.getActivityTwo().equals("")) {

            holder.txtSpringGift.setVisibility(View.VISIBLE);
            holder.txtSpringGift.setText(bean.getActivityTwo());
        } else {
            holder.txtSpringGift.setVisibility(View.GONE);
        }

        return convertView;

    }


    class ViewHolder {
        TextView txtMarkNum;//xxx标第xxx期
        TextView txtExclusive;//用户专享
        TextView txtSpringGift;//春天礼
        ImageView imgCorner;//角标
        TextView txtEarings;//年化收益率
        TextView txtAddRote;//增加的利率
        TextView txtInvestmentLimit;//投资期限
        TextView txtRemain;//剩余额度
        TextView txtBuyNum;//120人已购
        TextView imgSellorRepay;
        TextView quan;
        TextView baifenhao;
        TextView tian;
        View split;
        LinearLayout add;
        private final TipsProgressBar bar;

        public ViewHolder(View v) {

            txtMarkNum = (TextView) v.findViewById(R.id.past_product_txt_time);
            txtExclusive = (TextView) v.findViewById(R.id.past_product_txt_exclusive);
            txtSpringGift = (TextView) v.findViewById(R.id.past_product_txt_spring_gift);
            imgCorner = (ImageView) v.findViewById(R.id.past_product_img_corner);
            txtEarings = (TextView) v.findViewById(R.id.past_product_txt_rote_num);
            txtAddRote = (TextView) v.findViewById(R.id.past_product_txt_add_rote);
            txtInvestmentLimit = (TextView) v.findViewById(R.id.past_product_txt_limit_num);
            txtRemain = (TextView) v.findViewById(R.id.past_product_txt_remain_money_num);
            txtBuyNum = (TextView) v.findViewById(R.id.past_product_txt_buy_num);
            quan = (TextView) v.findViewById(R.id.past_product_quan);
            baifenhao = (TextView) v.findViewById(R.id.past_product_txt_percent);
            tian = (TextView) v.findViewById(R.id.past_product_tian);
            imgSellorRepay = (TextView) v.findViewById(R.id.past_product_img_sell_or_repay);
            split = v.findViewById(R.id.past_product_view_split);
            add = (LinearLayout) v.findViewById(R.id.past_product_li_add_rote);
            bar = (TipsProgressBar) v.findViewById(R.id.past_product_progress);

        }

    }
}
