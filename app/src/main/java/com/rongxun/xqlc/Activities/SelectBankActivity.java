package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.bank.BankCard;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class SelectBankActivity extends MyBaseActivity implements View.OnClickListener {

    private IconFontTextView back;
    private ListView listView;
    private Button button;
    private SelectBankAdapter adapter;
    private List<BankList.BankCardListBean> bankCardList;
    private int currentPosition=100;
    private BankList.BankCardListBean currentBankCard;
    private int selectPosition=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_select_bank);
        initView();
        initListener();
    }

    private void initListener() {

    }

    private void initView() {
        Intent intent = getIntent();
        selectPosition=intent.getIntExtra("selectPosition",200);
        BankList bankList= (BankList) intent.getSerializableExtra("bean");
        bankCardList = bankList.getBankCardList();

        for(BankList.BankCardListBean item : bankCardList){
            if(item.getBankId().equals("CMB")){
                bankCardList.remove(item);
            }
        }
        back = (IconFontTextView) findViewById(R.id.select_bank_back);
        button.setOnClickListener(this);
        back.setOnClickListener(this);
        adapter = new SelectBankAdapter();
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.select_bank_back:
                finish();
                break;
        }
    }

    public class SelectBankAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return bankCardList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //银行较少不需要做缓存
            final BankList.BankCardListBean bankCard = bankCardList.get(position);
            convertView=View.inflate(SelectBankActivity.this,R.layout.select_bank_list_item,null);
            TextView name= (TextView) convertView.findViewById(R.id.select_bank_list_item_bankname);
            TextView limit= (TextView) convertView.findViewById(R.id.select_bank_list_item_limit);
            ImageView iv= (ImageView) convertView.findViewById(R.id.select_bank_list_item_iv);
            final CheckBox checkBox= (CheckBox) convertView.findViewById(R.id.select_bank_list_item_checkbox);
            name.setText(bankCard.getBankName());
            Glide.with(SelectBankActivity.this).load(bankCard.getBankIcon()).into(iv);

            limit.setText(bankCard.getBankLimit());
            if(position==selectPosition){
                bankCard.isCheck=true;
                selectPosition=200;
            }
            if(bankCard.isCheck ){
                //以选择状态
                checkBox.setBackgroundResource(R.mipmap.checked_box_);
            }else{
                checkBox.setBackgroundResource(R.mipmap.box_normal);
            }
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bankCard.isCheck){
                        checkBox.setBackgroundResource(R.mipmap.box_normal);
                        bankCard.isCheck=false;
                    }else {
                        for(int x=0;x<bankCardList.size();x++){
                            if(x!=position){
                                bankCardList.get(x).isCheck=false;
                                notifyDataSetChanged();
                                currentBankCard=null;
                                currentPosition=100;
                            }
                        }
                        currentBankCard=bankCard;
                        currentPosition=position;
                        checkBox.setBackgroundResource(R.mipmap.checked_box_);
                        bankCard.isCheck=true;
                    }
                }
            });
            return convertView;
        }
    }



    public interface BankButtonListener{
        public void ButtonListener();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }
}
