package com.rongxun.xqlc.Adapters;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.rongxun.xqlc.Activities.HbChooseActivity;
import com.rongxun.xqlc.Beans.projectInitBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.CancleMessageDialog;
import com.rongxun.xqlc.UI.CancleMessageDialogOne;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by Administrator on 2015/11/5.
 */
public class HongBaoChooseListAdapter extends BaseAdapter {

    private List<projectInitBean.DataBean> hongbaoList;
    private List<projectInitBean.CouponListBean> jiaxiList;
    private LayoutInflater mInflater;
    private HbChooseActivity context;
    private String TAG = "项目投资列表";
    private OneItemClickListener oneItemClickListener;
    private OneJiaxiItemClickListener oneJiaxiItemClickListener;
    private String money;
    private int MaxInvestFullMomey;//所选红包需投资的金额
    private int lastInvestFullMomey;
    private int hbCount = 0;//红包个数
    private String beLeftMoney;
    private boolean isHongBaoCount = true;
    private int HONGBAOTYPE = 0;
    private int JIAXITYPE = 1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DecimalFormat df1 = new DecimalFormat("#0.00");
    //加息卷 数目
    private int jiaxijuanCount = 0;
    //加息%
    private double jiaxiPercentage = 0f;
    //加息收益 元
    private double jiaxiMoney = 0f;
    //加息总投资额
//    private int jiaxiAllMoney = 0;
    //用户自己投资的钱
    private int userMoney;

    int HBmoney;

    DecimalFormat df = new DecimalFormat("#0");
    private int intMoney;
    private CancleMessageDialogOne dialog = null;
    private HongBaoChooseListAdapter hongBaoChooseListAdapter;
    private HongBaoChooseListAdapter jiaxiAdapter;
    private int timeLimit;

    public HongBaoChooseListAdapter(HbChooseActivity context, List<projectInitBean.DataBean> hongbaoList,
                                    List<projectInitBean.CouponListBean> jiaxiList,
                                    LayoutInflater mInflater,
                                    String money, String beLeftMoney,int timeLimit) {
        this.jiaxiList = jiaxiList;
        this.context = context;
        this.timeLimit=timeLimit;
        //红包集合
        this.hongbaoList = hongbaoList;
        this.mInflater = mInflater;
        //项目可投金额
        this.beLeftMoney = beLeftMoney;
        //用户投资金额
        if (TextUtils.isEmpty(money))
            money = "0";
        this.money = money;
        userMoney=Integer.parseInt(money);
        for (projectInitBean.DataBean item : hongbaoList) {
            if (item.isright) {
                //红包使用的钱
                HBmoney = (int) (HBmoney + item.getMoney());
                //所选红包需投资的金额
                MaxInvestFullMomey = MaxInvestFullMomey + item.getInvestFullMomey();
            }
        }
        lastInvestFullMomey = MaxInvestFullMomey;
        if(MaxInvestFullMomey==0 ||  MaxInvestFullMomey<Integer.parseInt(money)){
            MaxInvestFullMomey=Integer.parseInt(money);
        }

    }

    public List<projectInitBean.DataBean> getHongbaoList() {
        return hongbaoList;
    }

    public void setType(boolean isHongBaoCount) {
        this.isHongBaoCount = isHongBaoCount;
    }

    public void setHongbaoList(List<projectInitBean.DataBean> hongbaoList) {
        this.hongbaoList = hongbaoList;
    }

    public OneItemClickListener getOneItemClickListener() {
        return oneItemClickListener;
    }

    public void setOneItemClickListener(OneItemClickListener oneItemClickListener) {
        this.oneItemClickListener = oneItemClickListener;
    }

    public void setOneJiaxiItemClickListener(OneJiaxiItemClickListener oneItemClickListener) {
        this.oneJiaxiItemClickListener = oneJiaxiItemClickListener;
    }

    @Override
    public int getCount() {
        if (getItemViewType(0) != HONGBAOTYPE) {
            return jiaxiList.size();
        } else {
            return hongbaoList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return hongbaoList.get(position);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        if (isHongBaoCount) {
            return HONGBAOTYPE;
        } else {
            return JIAXITYPE;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (getItemViewType(position) == HONGBAOTYPE) {
            final projectInitBean.DataBean HongbaoDateBean = hongbaoList.get(position);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.hong_bao_list_choose_item_layout, null);
                viewHolder = new ViewHolder();
//                convertView.findViewById(R.id.hongbao_img);;
                viewHolder.hongbaoListItemNumber = (TextView) convertView.findViewById(R.id.hongbao_list_item_number);
                viewHolder.hongbaoChooseBox = (CheckBox) convertView.findViewById(R.id.hongbao_choose_box);
                viewHolder.hongbaoListItemDeadLine = (TextView) convertView.findViewById(R.id.hongbao_list_item_dead_line);
                viewHolder.hongbaoListItemTimeLine = (TextView) convertView.findViewById(R.id.hongbao_list_item_time_line);
                viewHolder.money_line = (TextView) convertView.findViewById(R.id.hongbao_list_item_money_line);
                viewHolder.type = (TextView) convertView.findViewById(R.id.hongbao_list_item_type);
                viewHolder.type1 = (TextView) convertView.findViewById(R.id.hongbao_list_item_type1);
                viewHolder.hb_bg_rl = (RelativeLayout) convertView.findViewById(R.id.hb_bg_rl);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.type1.setText(HongbaoDateBean.getSourceStringMore()+"");
            viewHolder.hongbaoListItemDeadLine.setText("该优惠券将于" + sdf.format(HongbaoDateBean.getEndTime())+"到期");
            viewHolder.hongbaoListItemTimeLine.setText("项目期限满" + HongbaoDateBean.getLimitStart() + "天可用");
            viewHolder.money_line.setText("单笔投资满" + HongbaoDateBean.getInvestFullMomey() + "元可用");
            viewHolder.hongbaoListItemNumber.setText(df.format(HongbaoDateBean.getMoney()) + "");
            viewHolder.type.setText(HongbaoDateBean.getName());
            System.out.println(HongbaoDateBean.isright + "+++++++++++");
            if (HongbaoDateBean.isright) {
                viewHolder.hongbaoChooseBox.setChecked(true);
            } else {
                viewHolder.hongbaoChooseBox.setChecked(false);
            }

            final ViewHolder finalViewHolder = viewHolder;

            viewHolder.hb_bg_rl.setOnClickListener(new View.OnClickListener() {

                Intent intent = new Intent("HbBroadCast");
                int InvestFullMomey = 0;

                @Override
                public void onClick(View v) {
                    if (!HongbaoDateBean.isright) {
                        InvestFullMomey = 0;
                        for (int x = 0; x < hongbaoList.size(); x++) {
                            if (hongbaoList.get(x).isright) {
                                hongbaoList.get(x).isright=false;
                            }
                        }
                        HongbaoDateBean.isright = true;
                        if (oneItemClickListener != null) {
                            for (int x = 0; x < hongbaoList.size(); x++) {
                                //循环添加所有使用条件money的合
                                if (hongbaoList.get(x).isright)
                                    //所有勾选红包需要 投资的金额
                                    InvestFullMomey = InvestFullMomey + hongbaoList.get(x).getInvestFullMomey();
                            }

                            //判断是否大于项目可投金额
                            if (InvestFullMomey > Integer.parseInt(beLeftMoney)) {
                                InvestFullMomey =InvestFullMomey-HongbaoDateBean.getInvestFullMomey();
                                if (InvestFullMomey < Integer.parseInt(money)) {
                                    InvestFullMomey = Integer.parseInt(money);
                                    lastInvestFullMomey = Integer.parseInt(money);
                                }
                                MaxInvestFullMomey = InvestFullMomey;
                                finalViewHolder.hongbaoChooseBox.setChecked(false);
                                HongbaoDateBean.isright = false;
                                Toast.makeText(context, "红包使用金额大于项目可投剩余金额", Toast.LENGTH_SHORT).show();
                                intent.putExtra("HBmoney", HBmoney);
                                intent.putExtra("money", InvestFullMomey + "");
                                intent.putExtra("isAllmost",true);
                            } else {
                                finalViewHolder.hongbaoChooseBox.setChecked(true);
                                // MaxInvestFullMomey 勾选过的最大值
//                                if (InvestFullMomey > MaxInvestFullMomey) {
                                    //如果勾选的红包所需金额少于用户投资金额 那么使用的钱为用户投资的钱
                                    if (InvestFullMomey < Integer.parseInt(money)) {
                                        InvestFullMomey = Integer.parseInt(money);
                                        lastInvestFullMomey = Integer.parseInt(money);
                                    }
                                    MaxInvestFullMomey = InvestFullMomey;

//                                }
                                lastInvestFullMomey = InvestFullMomey;
                                //点击使用红包钱的数目
                                HBmoney = (int) (HBmoney + HongbaoDateBean.getMoney());
                                intent.putExtra("money", InvestFullMomey + "");
                                intent.putExtra("HBmoney", HBmoney);
                                intent.putExtra("jiaxiInverstMoney",money);
                                //加息卷数据清0
                                intent.putExtra("jiaxijuanCount",0);
                                intent.putExtra("jiaxiPercentage",0);
                                intent.putExtra("jiaxiMoney",0);
                                intent.putExtra("isAllmost",false);
                                if(jiaxiList!=null){
                                    for (int x = 0; x < jiaxiList.size(); x++) {
                                        if (jiaxiList.get(x).isRight) {
                                            jiaxiList.get(x).isRight=false;
                                            //通知加息卷刷新
                                            if(jiaxiAdapter!=null)
                                                jiaxiAdapter.notifyDataSetChanged();
                                        }
                                    }

                                }
                                oneItemClickListener.OnCheckBoxClick(position, false, hongbaoList);
                            }

                        }
                    } else {
                        HongbaoDateBean.isright = false;
                        finalViewHolder.hongbaoChooseBox.setChecked(false);
                        if (oneItemClickListener != null) {
                            //点击使用红包钱的数目
                            HBmoney = (int) (HBmoney - HongbaoDateBean.getMoney());
                            intent.putExtra("HBmoney", HBmoney);
                            MaxInvestFullMomey = MaxInvestFullMomey - HongbaoDateBean.getInvestFullMomey();
                            if (MaxInvestFullMomey < Integer.parseInt(money)) {
                                MaxInvestFullMomey = Integer.parseInt(money);
                            }
                            intent.putExtra("money", MaxInvestFullMomey + "");
                            lastInvestFullMomey = MaxInvestFullMomey;
                        }
                        oneItemClickListener.OnCheckBoxClick(position, true, hongbaoList);
                    }
                    //红包个数
                    hbCount = 0;
                    for (int x = 0; x < hongbaoList.size(); x++) {
                        if (hongbaoList.get(x).isright) {
                            hbCount++;
                        }

                    }
                    intent.putExtra("hbCount", hbCount);
                    intent.putExtra("jiaxiInverstMoney",money);
                    context.sendBroadcast(intent);

                }

            });
        } else {

            final projectInitBean.CouponListBean jiaxiListBean = jiaxiList.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.hong_bao_list_choose_item_jiaxi_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.jiaxi_number = (TextView) convertView.findViewById(R.id.hongbao_list_item_jiaxi_number);
                viewHolder.jiaxi_dead_line = (TextView) convertView.findViewById(R.id.hongbao_list_item_jiaxi_dead_line);
                viewHolder.jiaxi_limit = (TextView) convertView.findViewById(R.id.hongbao_list_item_jiaxi_limit);
                viewHolder.jiaxi_time = (TextView) convertView.findViewById(R.id.hongbao_list_item_jiaxi_time);
                viewHolder.jiaxi_type = (TextView) convertView.findViewById(R.id.hongbao_list_item_jiaxi_type);
                viewHolder.jiaxi_money_line = (TextView) convertView.findViewById(R.id.hongbao_list_item_jiaxi_money_line);
                viewHolder.jiaxiBox = (ImageView) convertView.findViewById(R.id.hongbao_choose_jiaxi_box);
                viewHolder.hb_bg_rl = (RelativeLayout) convertView.findViewById(R.id.hb_bg_rl);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.jiaxi_dead_line.setText("该优惠券将于" + sdf.format(jiaxiListBean.getEndTime())+"到期");
            viewHolder.jiaxi_number.setText(Double.parseDouble(df1.format(jiaxiListBean.getApr()* 36))  + "%");
            if (jiaxiListBean.getTimeLimitStart() == 0 && jiaxiListBean.getTimeLimitEnd() == 0) {
                viewHolder.jiaxi_limit.setText("无限制");
            } else if (jiaxiListBean.getTimeLimitStart() > 0 && jiaxiListBean.getTimeLimitEnd() == 0) {
                viewHolder.jiaxi_limit.setText("期限满" + jiaxiListBean.getTimeLimitStart() + "天可用");
            } else if (jiaxiListBean.getTimeLimitStart() == 0 && jiaxiListBean.getTimeLimitEnd() > 0) {
                viewHolder.jiaxi_limit.setText("期限小于" + jiaxiListBean.getTimeLimitEnd() + "天可用");
            } else {
                viewHolder.jiaxi_limit.setText("期限" + jiaxiListBean.getTimeLimitStart() + "天至" + jiaxiListBean.getTimeLimitEnd() + "天可用");
            }

            viewHolder.jiaxi_time.setVisibility(View.VISIBLE);
            if (jiaxiListBean.getDays() != 0) {
                viewHolder.jiaxi_time.setText("可加息" + jiaxiListBean.getDays() + "天");
            } else {
                viewHolder.jiaxi_time.setText("无限期");
            }
            viewHolder.jiaxi_type.setText(jiaxiListBean.getSourceStringMore()+"");

            if (jiaxiListBean.getAmountMin() == 0 && jiaxiListBean.getAmountMax() > 0) {
                viewHolder.jiaxi_money_line.setText("单笔投资小于" + jiaxiListBean.getAmountMax() + "元可用");
            } else if (jiaxiListBean.getAmountMin() > 0 && jiaxiListBean.getAmountMax() == 0) {
                viewHolder.jiaxi_money_line.setText("单笔投资满" + jiaxiListBean.getAmountMin() + "元可用");
            } else if (jiaxiListBean.getAmountMin() == 0 && jiaxiListBean.getAmountMax() == 0) {
                viewHolder.jiaxi_money_line.setText("单笔投资：无限制");
            } else {
                viewHolder.jiaxi_money_line.setText("单笔投资" + jiaxiListBean.getAmountMin() + "元至"+jiaxiListBean.getAmountMax()+"元可用");
            }

            if (jiaxiListBean.isRight) {
                viewHolder.jiaxiBox.setBackgroundResource(R.mipmap.checked_box_);
            } else {
                viewHolder.jiaxiBox.setBackgroundResource(R.mipmap.box_normal);
            }

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.hb_bg_rl.setOnClickListener(new View.OnClickListener() {
                Intent intent = new Intent("HbBroadCast");

                @Override
                public void onClick(View v) {
                    intMoney = Integer.parseInt(money);
                    if (jiaxiListBean.isRight) {
                        jiaxijuanCount = 0;
                        jiaxiPercentage = 0f;
                        jiaxiMoney = 0f;
                        intMoney = userMoney;
                        // money=intMoney+"";
                        jiaxiListBean.isRight = false;
                        finalViewHolder.jiaxiBox.setBackgroundResource(R.mipmap.box_normal);
                        intent.putExtra("money",money);
                        intent.putExtra("jiaxiInverstMoney",money);
                        intent.putExtra("HBmoney", "0");
                        intent.putExtra("hbCount", 0);
                        intent.putExtra("jiaxijuanCount",jiaxijuanCount);
                        intent.putExtra("jiaxiPercentage",jiaxiPercentage);
                        intent.putExtra("jiaxiMoney",jiaxiMoney);
                        intent.putExtra("isJiaxi",true);
                        context.sendBroadcast(intent);



                    } else {

                        //判断加息卷使用区间
                        if (dialog == null) {
                            dialog = new CancleMessageDialogOne(context, R.style.ActionSheetDialogStyle);
                            dialog.setCancelable(false);
                        }

                        //投资金额在x元至y元之间才能使用该加息券，是否将当前投资额设置为x元
                        if (intMoney < jiaxiListBean.getAmountMin() && jiaxiListBean.getAmountMax() != 0 &&jiaxiListBean.getAmountMin()!=0) {
                            dialog.setTitle("投资金额在"+jiaxiListBean.getAmountMin()+"元至"+jiaxiListBean.getAmountMax()+"元之间才能使用该加息券，是否将当前投资额设置为"+jiaxiListBean.getAmountMin()+"元");
                            dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intMoney =jiaxiListBean.getAmountMin();
                                    jiaxiClickHongbao(hongbaoList);
                                    jiaxiClickJixaxi(jiaxiList,finalViewHolder,jiaxiListBean,intent);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else if (intMoney > jiaxiListBean.getAmountMax() && jiaxiListBean.getAmountMin() != 0 &&jiaxiListBean.getAmountMax()!=0) {        //投资金额在x元至y元之间才能使用该加息券，是否将当前投资额设置为y元
                            dialog.setTitle("投资金额在"+jiaxiListBean.getAmountMin() +"元至"+jiaxiListBean.getAmountMax()+"元之间才能使用该加息券，是否将当前投资额设置为"+jiaxiListBean.getAmountMax()+"元");
                            dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intMoney =jiaxiListBean.getAmountMax();
                                    jiaxiClickHongbao(hongbaoList);
                                    jiaxiClickJixaxi(jiaxiList,finalViewHolder,jiaxiListBean,intent);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else if (jiaxiListBean.getAmountMax() == 0 && jiaxiListBean.getAmountMin()!=0 && intMoney < jiaxiListBean.getAmountMin()  ) {    //投资金额满X元才能使用该加息券，是否将当前投资额设置为x元
                            dialog.setTitle("投资金额满"+jiaxiListBean.getAmountMin()+"元才能使用该加息券，是否将当前投资额设置为"+jiaxiListBean.getAmountMin()+"元");
                            dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intMoney =jiaxiListBean.getAmountMin();
                                    jiaxiClickHongbao(hongbaoList);
                                    jiaxiClickJixaxi(jiaxiList,finalViewHolder,jiaxiListBean,intent);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else if (jiaxiListBean.getAmountMin() == 0 && jiaxiListBean.getAmountMax() < intMoney &&jiaxiListBean.getAmountMax()>0) {   //投资金额小于X元才能使用该加息券，是否将当前投资额设置为x元
                            dialog.setTitle("投资金额小于"+jiaxiListBean.getAmountMax()+"元才能使用该加息券，是否将当前投资额设置为"+jiaxiListBean.getAmountMax()+"元");
                            dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intMoney =jiaxiListBean.getAmountMax();
                                    jiaxiClickHongbao(hongbaoList);
                                    jiaxiClickJixaxi(jiaxiList,finalViewHolder,jiaxiListBean,intent);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else {
                            jiaxiClickJixaxi(jiaxiList,finalViewHolder,jiaxiListBean,intent);
                        }



                    }
                }
            });



        }

        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'hong_bao_list_choose_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        ImageView hongbaoImg;
        TextView hongbaoListItemNumber;
        CheckBox hongbaoChooseBox;
        TextView hongbaoListItemDeadLine;
        TextView hongbaoListItemTimeLine;
        TextView type1;
        public TextView money_line;
        TextView jiaxi_number;
        TextView jiaxi_dead_line;
        TextView jiaxi_limit;
        TextView jiaxi_time;
        TextView jiaxi_type;
        TextView jiaxi_money_line;
        TextView type;
        ImageView jiaxiBox;
        RelativeLayout hb_bg_rl;

    }

    public interface OneItemClickListener {
        void OnCheckBoxClick(int position, boolean isChecked, List<projectInitBean.DataBean> hongbaoList);
    }

    public interface OneJiaxiItemClickListener {
        void OnCheckBoxJiaxiClick(int position, boolean isChecked, List<projectInitBean.CouponListBean> hongbaoList);
    }

    public void setHongbaoAdapter(HongBaoChooseListAdapter hongBaoChooseListAdapter){
        this.hongBaoChooseListAdapter =hongBaoChooseListAdapter;
    }
    public void setJiaxiAdapter(HongBaoChooseListAdapter jiaxiAdapter){
        this.jiaxiAdapter=jiaxiAdapter;
    }

    public void setAllMoney(int money){
        MaxInvestFullMomey=money;
    }
    public void jiaxiClickHongbao(List<projectInitBean.DataBean> hongbaoList ){

        //红包个数
        hbCount = 0;
        //红包金额
        HBmoney = 0;
        //使用红包所需的金额
        MaxInvestFullMomey = 0;
        //点击加息卷时红包归0
        if (hongbaoList.size() > 0) {
            for (int x = 0; x < hongbaoList.size(); x++) {
                hongbaoList.get(x).isright = false;
            }

        }
    }

    public void jiaxiClickJixaxi(List<projectInitBean.CouponListBean> jiaxiList,ViewHolder finalViewHolder,
                                 projectInitBean.CouponListBean jiaxiListBean,Intent intent){
        for (int x = 0; x < jiaxiList.size(); x++) {
            if (jiaxiList.get(x).isRight) {
                jiaxiList.get(x).isRight=false;
            }
        }
        jiaxiListBean.isRight = true;
        notifyDataSetChanged();

        if(hongBaoChooseListAdapter!=null){
            for(int x=0;x<hongbaoList.size();x++){
                if(hongbaoList.get(x).isright){
                    hongbaoList.get(x).isright=false;
                }
            }
            hongBaoChooseListAdapter.notifyDataSetChanged();
        }
        jiaxijuanCount = 1;//加息卷张数
        jiaxiPercentage = Double.parseDouble(df1.format(jiaxiListBean.getApr()*36)) ;//加息卷百分比

        if(jiaxiListBean.getDays()==0  || jiaxiListBean.getDays()>timeLimit){
            jiaxiMoney=Double.parseDouble(df1.format(jiaxiListBean.getApr()/1000*timeLimit*intMoney));

        }else{
            jiaxiMoney=Double.parseDouble(df1.format(jiaxiListBean.getApr()*intMoney/1000*(jiaxiListBean.getDays())));
        }


//        if(MaxInvestFullMomey<intMoney)
//            MaxInvestFullMomey=intMoney;
        intent.putExtra("money", money);
        intent.putExtra("jiaxiInverstMoney",intMoney+"");
        intent.putExtra("HBmoney", "0");
        intent.putExtra("hbCount", 0);
        intent.putExtra("jiaxijuanCount",jiaxijuanCount);
        intent.putExtra("jiaxiPercentage",jiaxiPercentage);
        intent.putExtra("jiaxiMoney",jiaxiMoney);
        intent.putExtra("isJiaxi",true);
        context.sendBroadcast(intent);
    }

}

