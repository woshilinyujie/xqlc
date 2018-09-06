package com.rongxun.xqlc.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.rongxun.xqlc.Beans.Borrow.ContentBean;
import com.rongxun.xqlc.Beans.Borrow.DistrictBean;
import com.rongxun.xqlc.Beans.Borrow.ManageBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.ProgressWheel;
import com.rongxun.xqlc.UI.tipprogress.TipsProgressBar;
import com.rongxun.xqlc.Util.DecimalFormatUtil;

import java.util.List;

import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Adapters
 * Author: HouShengLi
 * Time: 2017/07/04 13:38
 * E-mail: 13967189624@163.com
 * Description:
 */

public class MangeAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private List<ManageBean> list;
    private Activity context;
    private LayoutInflater inflater;
    private DistrictBean d;

    public MangeAdapter(Context context, List<ManageBean> list) {
        this.list = list;
        this.context = (Activity) context;
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

        ViewHolder holder = null;
        ManageBean manageBean = list.get(position);
        if (convertView == null) {

            switch (manageBean.getTypes()) {
                case ManageBean.SECTION:
                    convertView = inflater.inflate(R.layout.manage_item_out, parent, false);
                    holder = new DistrictViewHolder(convertView);
                    break;
                case ManageBean.ITEM:

                    convertView = inflater.inflate(R.layout.manage_item_in, parent, false);
                    holder = new ContentViewHolder(convertView);
                    break;
                default:
                    break;
            }

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        switch (manageBean.getTypes()) {

            case ManageBean.SECTION:
                DistrictViewHolder dHolder = (DistrictViewHolder) holder;
                d = (DistrictBean) manageBean;
                dHolder.txtDistrict.setText(d.getDistrict());
                if(d.getDistrict().equals("新手专区")){
                    dHolder.txtDistrict.setBackgroundResource(R.drawable.manage_state_blue);
                }else if(d.getDistrict().equals("尊享专区")){
                    dHolder.txtDistrict.setBackgroundResource(R.drawable.manage_state_yellow);
                }else{
                    dHolder.txtDistrict.setBackgroundResource(R.drawable.manage_state_red);
                }

                final Intent intent = new Intent(context, PastProductsActivity.class);
                switch (d.getMore()){
                    case "2":
                        intent.putExtra("rongXunFlg","2");
                        break;
                    case "1":
                        intent.putExtra("rongXunFlg","1");
                        break;
                    case "0":
                        intent.putExtra("rongXunFlg","0");
                        break;
                }
                dHolder.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(intent);
                    }
                });

                break;
            case ManageBean.ITEM:
                ContentViewHolder cHolder = (ContentViewHolder) holder;
                ContentBean c = (ContentBean) manageBean;
                DecimalFormatUtil util = DecimalFormatUtil.getInstance();
                cHolder.txtMarkNum.setText(c.getName());//设置 xxx标第xxx期
                cHolder.txtEarings.setText(util.getDouble2(c.getApr()));//年化收益率

                cHolder.txtAddRote.setText(util.getDouble2(c.getAwardApr()));//增加的利率
                cHolder.txtInvestmentLimit.setText(c.getTimeLimit());//投资期限
                cHolder.txtRemain.setText(util.getInt(Double.parseDouble(c.getBalance())));//剩余额度
                cHolder.txtSaleNum.setText(c.getCount() + "");

                //设置分割线的显示与隐藏
                if (c.isLast()) {
                    //是这个专区最后一条数据，隐藏分割线
                    cHolder.split.setVisibility(View.GONE);
                } else {
                    cHolder.split.setVisibility(View.VISIBLE);
                }

                //设置进度条
                if (c.getSchedule() != null && !c.getSchedule().equals("")) {

                    if (c.isFirst()) {
                        //动画形式加载
                        cHolder.bar.animateProgress(Integer.parseInt(c.getSchedule()) * 10);
                        c.setFirst(false);
                    } else {
                        //正常加载
                        cHolder.bar.setProgress(Integer.parseInt(c.getSchedule()) * 10);//进度条
                    }

                }



                //设置 用户专享
                if (c.getActivityOne() != null && !c.getActivityOne().equals("")) {
                    cHolder.txtExclusive.setVisibility(View.VISIBLE);
                    cHolder.txtExclusive.setText(c.getActivityOne());
                } else {
                    cHolder.txtExclusive.setVisibility(View.GONE);
                }

                //设置 春天礼
                if (c.getActivityTwo() != null && !c.getActivityTwo().equals("")) {

                    cHolder.txtSpringGift.setVisibility(View.VISIBLE);
                    cHolder.txtSpringGift.setText(c.getActivityTwo());
                } else {
                    cHolder.txtSpringGift.setVisibility(View.GONE);
                }
                cHolder.buy.setBackgroundResource(R.drawable.shape_manage_blue);
                if(d.getDistrict().equals("尊享专区")){
                    cHolder.txtEarings.setTextColor(Color.parseColor("#A17A17"));
                    cHolder.bar.setPrimaryColor(Color.parseColor("#A17A17"));
                    cHolder.baifenhao.setTextColor(Color.parseColor("#A17A17"));
                    cHolder.buy.setBackgroundResource(R.drawable.shape_manage_yellow);
                }else if(d.getDistrict().equals("新手专区")){
                    cHolder.imgCorner.setVisibility(View.VISIBLE);
                }
                if(c.getUseHongbao()==1){
                    cHolder.quan.setText("可使用优惠券");
                }else{
                    cHolder.quan.setText("不可使用优惠券");
                }
                break;
        }


        return convertView;

    }

    /**
     * 三方控件实现的方法
     *
     * @param viewType
     * @return
     */
    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == ManageBean.SECTION;//0是标题，1是内容
    }

    @Override
    public int getViewTypeCount() {
        return 2;//2种view的类型
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getTypes();
    }

    class ViewHolder {

    }

    /**
     * 区
     */
    class DistrictViewHolder extends ViewHolder {

        TextView txtDistrict;
        TextView more;
        public DistrictViewHolder(View view) {
            more= (TextView) view.findViewById(R.id.manage_btn_more);
            txtDistrict = (TextView) view.findViewById(R.id.manage_txt_district);
        }

    }

    /**
     * list标内容
     */
    class ContentViewHolder extends ViewHolder {

        TextView txtMarkNum;//xxx标第xxx期
        TextView txtExclusive;//用户专享
        TextView txtSpringGift;//春天礼
        ImageView imgCorner;//角标
        TextView txtEarings;//年化收益率
        TextView txtAddRote;//增加的利率
        TextView txtInvestmentLimit;//投资期限
        TipsProgressBar bar;//进度条
        TextView txtRemain;//剩余额度
        TextView txtSaleNum;//已售120笔
        TextView baifenhao;
        View split;//分割线
        TextView buy;
        TextView quan;
        LinearLayout add;//加息

        public ContentViewHolder(View v) {

            txtMarkNum = (TextView) v.findViewById(R.id.manage_txt_time);
            quan = (TextView) v.findViewById(R.id.manage_user_quan);
            txtExclusive = (TextView) v.findViewById(R.id.manage_txt_exclusive);
            txtSpringGift = (TextView) v.findViewById(R.id.manage_txt_spring_gift);
            imgCorner = (ImageView) v.findViewById(R.id.manage_img_corner);
            txtEarings = (TextView) v.findViewById(R.id.manage_txt_rote_num);
            txtAddRote = (TextView) v.findViewById(R.id.manage_txt_add_rote);
            txtInvestmentLimit = (TextView) v.findViewById(R.id.manage_txt_limit_num);
            bar = (TipsProgressBar) v.findViewById(R.id.manage_wheel_progress);
            txtRemain = (TextView) v.findViewById(R.id.manage_txt_remain_money_num);
            txtSaleNum = (TextView) v.findViewById(R.id.manage_txt_sale_num);
            split = v.findViewById(R.id.manage_view_in_split);
            add = (LinearLayout) v.findViewById(R.id.manage_li_add_rote);
            baifenhao = (TextView) v.findViewById(R.id.manage_txt_percent);
            buy= (TextView) v.findViewById(R.id.manage_buy);

        }

    }
}
