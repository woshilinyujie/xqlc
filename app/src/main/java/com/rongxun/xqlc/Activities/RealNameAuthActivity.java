package com.rongxun.xqlc.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rongxun.xqlc.Adapters.BankListAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.JsonBean;
import com.rongxun.xqlc.Beans.bank.BankCard;
import com.rongxun.xqlc.Beans.bank.BankList;
import com.rongxun.xqlc.Beans.bank.RbOrderBean;
import com.rongxun.xqlc.Beans.bank.ZSBeen;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.MessageDialog;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.ClickEvent;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.builder.PostFormBuilder;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
//绑卡认证

public class RealNameAuthActivity extends MyBaseActivity {
    private final String TAG = "绑卡认证";
    IconFontTextView realNameAuthToolbarBack;
    TextView realNameAuthToolbarTitle;
    Toolbar realNameAuthToolbar;
    EditText realNameAuthNameInput;
    EditText realNameAuthIdInput;
    LinearLayout realNameAuthFormLayout;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    TextView addBankBankName;
    RelativeLayout addBankBankLayout;
    EditText addBankCardCardId;
    TextView bankCardPopupText;
    Button addBankCardButton;
    CardView cardView;
    ScrollView realNameAuthScrollView;
    private Gson gson = new Gson();
    private String userId;
    private MessageDialog messageDialog;
    private BankList banklist;//银行列表
    private BankList.BankCardListBean selectedBank;
    private LoadingDialog loadingDialog;
    private PopupWindow popupWindow;
    private int selectPosition = 200;//进入银行页面默认选择position
    private final static int REQUEST_CODE_BAOFOO_SDK = 100;
    private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111://认证提交
                    if (banklist.getRealStatus() != null && banklist.getRealStatus().equals("2")) {
                        realNameAuthNameInput.setEnabled(false);
                        realNameAuthIdInput.setEnabled(false);
                    }
                    if (banklist.getRealName() != null) {
                        realNameAuthNameInput.setText(banklist.getRealName());
                    }
                    if (banklist.getIdNo() != null) {
                        realNameAuthIdInput.setText(banklist.getIdNo());
                    }
                    if (banklist.getCardNo() != null) {
                        addBankCardCardId.setText(banklist.getCardNo());
                    }
                    if (banklist.getBankId() != null) {
                        addBankCardButton.setText("签约中，再次绑定");
                        for (int x = 0; x < banklist.getBankCardList().size(); x++) {
                            if (banklist.getBankCardList().get(x).getBankId().equals(banklist.getBankId())) {
                                selectedBank = banklist.getBankCardList().get(x);
                                addBankBankName.setText(banklist.getBankCardList().get(x).getBankName());
                                selectPosition = x;
                            }
                        }

                    }
                    initJsonData(banklist);
                    addBankBankLayout.setOnClickListener(

                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //进入银行选择页面
                                    hintKbTwo();
                                    ShowPickerView();

                                }
                            }

                    );

                    break;
                case 0x222:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                        loadingDialog = null;
                    }
                    break;
                default:
                    break;

            }
        }
    };
    private EditText phone;
    private String order;
    private String type;
    private String cardIdText;
    private String bankNameText;
    private String realNameText;
    private String realIdText;
    private String inverstMark;
    private String selectedIdArrayString;
    private String safePass;
    private String tenderMoney;
    private int projectId;
    private String project;
    private String exceptS;
    private String jiaxijuanID = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_real_name_auth);
        initView();
        initToolBar();
        initListener();


        userId = getIntent().getStringExtra("userId");
        String url = AppConstants.URL_SUFFIX + "/rest/bankTo";
        RequestForBankData(url);


    }

    private void initListener() {


        addBankCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBank();
            }
        });
    }


    private void initView() {
        jiaxijuanID = getIntent().getStringExtra("jiaxijuanID");
        inverstMark = getIntent().getStringExtra("inverstMark");
        exceptS = getIntent().getStringExtra("exceptS");
        selectedIdArrayString = getIntent().getStringExtra("selectedIdArrayString");
        tenderMoney = getIntent().getStringExtra("tenderMoney");
        projectId = getIntent().getIntExtra("projectId", 0);
        project = getIntent().getStringExtra("project");


        phone = (EditText) findViewById(R.id.real_name_auth_name_phone);
        realNameAuthToolbarBack = (IconFontTextView) findViewById(R.id.real_name_auth_toolbar_back);
        realNameAuthToolbarTitle = (TextView) findViewById(R.id.real_name_auth_toolbar_title);
        realNameAuthToolbar = (Toolbar) findViewById(R.id.real_name_auth_toolbar);
        realNameAuthNameInput = (EditText) findViewById(R.id.real_name_auth_name_input);
        realNameAuthIdInput = (EditText) findViewById(R.id.real_name_auth_id_input);
        realNameAuthFormLayout = (LinearLayout) findViewById(R.id.real_name_auth_form_layout);
        addBankBankName = (TextView) findViewById(R.id.add_bank_bank_name);
        addBankBankLayout = (RelativeLayout) findViewById(R.id.add_bank_bank_layout);
        addBankCardCardId = (EditText) findViewById(R.id.add_bank_card_card_id);
        bankCardPopupText = (TextView) findViewById(R.id.bank_card_popup_text);
        addBankCardButton = (Button) findViewById(R.id.add_bank_card_button);
        cardView = (CardView) findViewById(R.id.card_view);
        realNameAuthScrollView = (ScrollView) findViewById(R.id.real_name_auth_scroll_view);

        String userPhone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        if (userPhone != null) {
            phone.setText(userPhone);
        }
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initToolBar() {
        realNameAuthToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(realNameAuthToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    public void AddBank() {
        //如果快速点击了两次
        if (ClickEvent.isFastDoubleClick(R.id.add_bank_card_button)) {
            return;
        }

        cardIdText = addBankCardCardId.getText().toString();
        bankNameText = addBankBankName.getText().toString();
//        String bankbranchText = addBankBankBranch.getText().toString();
        realNameText = realNameAuthNameInput.getText().toString();
        realIdText = realNameAuthIdInput.getText().toString();
        if (cardIdText == null || cardIdText.trim().equals("")) {
            Toast.makeText(this, "请填写银行卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bankNameText == null || bankNameText.trim().equals("")) {
            Toast.makeText(this, "请选择银行", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.getText().toString() == null || !isMobile(phone.getText().toString())) {
            Toast.makeText(RealNameAuthActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (realNameText == null || realNameText.trim().equals("")) {
            Toast.makeText(RealNameAuthActivity.this, "请填写姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (realIdText == null || realIdText.trim().equals("")) {
            Toast.makeText(RealNameAuthActivity.this, "请填写身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }


        if (selectedBank == null) {
            Toast.makeText(this, "绑定失败,请刷新数据后重试！", Toast.LENGTH_SHORT).show();
            return;
        }
        //0-表示增加，1表示修改
        type = "0";

        if (banklist.getBankId() != null && !banklist.getBankId().equals("")) {
            type = "1";
        }

        //请求绑定英航卡
        String url = AppConstants.URL_SUFFIX + "/rest/bankSignSaveHnewVT";
        RequestForRechargeToBand(url);
        addBankCardButton.setEnabled(false);
    }

    /**
     * 获取银行卡数据
     *
     * @param basicUrl
     */
    public void RequestForBankData(String basicUrl) {
        OkHttpUtils.post()
                .url(basicUrl)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        final BankList resultBean = JSON.parseObject(s, BankList.class);
                        if (resultBean.getRcd().equals("R0001")) {
                            banklist = resultBean;
                            if (banklist != null) {

                                Message message = new Message();
                                message.what = 0x111;
                                message.obj = "";
                                mHandler.sendMessage(message);
                            }
                        } else {
                            Toast.makeText(RealNameAuthActivity.this, resultBean.getRmg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    //绑卡
    public void RequestForRechargeToBand(String url) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.showLoading();
        }

        PostFormBuilder post = OkHttpUtils.post();
        post.url(url)
                .addParams("token", PreferenceUtil.getPrefString(this, "loginToken", ""))
                .addParams("realName", realNameText)
                .addParams("bankId", selectedBank.getBankId())
                .addParams("idNo", realIdText)
                .addParams("cardNo", addBankCardCardId.getText().toString())
                .addParams("phone", phone.getText().toString())
                .addParams("money", getIntent().getStringExtra("money"));

        post.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                            Toast.makeText(RealNameAuthActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                            loadingDialog = null;
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        Log.i(TAG, s);
                        String rcd = null;
                        try {
                            JSONObject object = new JSONObject(s);
                            rcd = object.getString("rcd");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (rcd != null && rcd.equals("R0002")) {
                            //招行卡
                            ZSBeen zsBeen = gson.fromJson(s, ZSBeen.class);
                            Intent intent = new Intent(RealNameAuthActivity.this, ZsPlayActivity.class);
                            intent.putExtra("zsBeen", zsBeen);
                            if (inverstMark != null)
                                intent.putExtra("inverstMark", inverstMark);
                            intent.putExtra("tenderMoney", tenderMoney);
                            intent.putExtra("selectedIdArrayString", selectedIdArrayString);
                            intent.putExtra("project", project);
                            intent.putExtra("projectId", projectId);
                            intent.putExtra("safePass", safePass);

                            startActivityForResult(intent, 100);
                            mHandler.sendEmptyMessage(0x222);
                        } else {
                            final RbOrderBean bean = gson.fromJson(s, RbOrderBean.class);
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismissLoading();
                                loadingDialog = null;
                            }
                            if (bean.getRcd().equals("R0001")) {
                                //进入支付页面
                                order = bean.getOrder_no();
                                Intent intent = new Intent(RealNameAuthActivity.this, QuicklyPlayActivity.class);
                                intent.putExtra("order", order);
                                if (inverstMark != null) {
                                    intent.putExtra("inverstMark", inverstMark);
                                    intent.putExtra("tenderMoney", tenderMoney);
                                    intent.putExtra("selectedIdArrayString", selectedIdArrayString);
                                    intent.putExtra("project", project);
                                    intent.putExtra("jiaxijuanID", jiaxijuanID);
                                    intent.putExtra("projectId", projectId);
                                    intent.putExtra("safePass", safePass);
                                    if (exceptS != null)
                                        intent.putExtra("exceptS", exceptS);

                                }
                                intent.putExtra("chargeMoney", getIntent().getStringExtra("money"));
                                intent.putExtra("cardNo", addBankCardCardId.getText().toString());
                                intent.putExtra("userId", userId);
                                intent.putExtra("realName", realNameText);
                                intent.putExtra("idNo", realIdText);


                                RealNameAuthActivity.this.startActivity(intent);
                                finish();
                            } else {
                                addBankCardButton.setEnabled(true);
                                Toast.makeText(RealNameAuthActivity.this, bean.getRmg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }

    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 222) {
            finish();
        }
    }

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void initJsonData(BankList banklist) {
        List<String> list = new ArrayList<String>();
        list.add(" ");
        JsonBean addressBean = new JsonBean();
        addressBean.setName(" ");
        for (int x = 0; x < banklist.getBankCardList().size(); x++) {
            JsonBean.CityBean cityBean = new JsonBean.CityBean();
            cityBean.setName(banklist.getBankCardList().get(x).getBankName());
            cityBean.setArea(list);
        }
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        ArrayList<JsonBean> jsonBean = new ArrayList<JsonBean>();
        jsonBean.add(addressBean);
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String address_name = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                address_name=address_name.trim();
                if (address_name != null && address_name.length() > 0) {
                    addBankBankName.setText(address_name);
                }
//                Toast.makeText(AddTheReceivingAddressActivity.this, address_name, Toast.LENGTH_SHORT).show();
            }
        })

//                .setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

//        pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

}
