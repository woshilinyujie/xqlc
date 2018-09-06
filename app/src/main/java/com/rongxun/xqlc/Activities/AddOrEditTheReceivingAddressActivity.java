package com.rongxun.xqlc.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.JsonBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.GetJsonDataUtil;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;


/**
 * Created by oguig on 2017/5/15.
 * 添加收货地址
 */

public class AddOrEditTheReceivingAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private IconFontTextView img_back;
    private RelativeLayout AddTheReceivingAddressActivity_rl_selectarea;
    private TextView topname, tv_save;
    private EditText AddTheReceivingAddressActivity_et_consignee, AddTheReceivingAddressActivity_et_contactphone, AddTheReceivingAddressActivity_et_detailedaddress;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static final int resultCodeTo_edit_succeed = 6546;//编辑成功跳转 我的收货地址 界面结果码
    private static final int resultCodeTo_add_succeed = 6547;//添加成功跳转 我的收货地址 界面结果码

    private boolean isLoaded = false;
    private boolean isedit = false;
    private String address_name;
    private TextView tv_address_name;
    private String ISEDIT;
    private Intent it;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_add_the_receiving_address);

        it = getIntent();
        ISEDIT = it.getStringExtra("edit");

        initView();
        //编辑收货地址
        if (ISEDIT != null && ISEDIT.equals("000")) {
            init();
        }
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    private void initView() {
        token = PreferenceUtil.getPrefString(this, "loginToken", "");
        img_back = (IconFontTextView) findViewById(R.id.harves_black);
        topname = (TextView) findViewById(R.id.harvest_topname);
        tv_save = (TextView) findViewById(R.id.harvest_save);
        tv_address_name = (TextView) findViewById(R.id.tv_address_name);
        AddTheReceivingAddressActivity_rl_selectarea = (RelativeLayout) findViewById(R.id.AddTheReceivingAddressActivity_rl_selectarea);


        AddTheReceivingAddressActivity_et_consignee = (EditText) findViewById(R.id.AddTheReceivingAddressActivity_et_consignee);
        AddTheReceivingAddressActivity_et_contactphone = (EditText) findViewById(R.id.AddTheReceivingAddressActivity_et_contactphone);
        AddTheReceivingAddressActivity_et_detailedaddress = (EditText) findViewById(R.id.AddTheReceivingAddressActivity_et_detailedaddress);
        img_back.setVisibility(View.VISIBLE);
        tv_save.setVisibility(View.VISIBLE);
        topname.setText("添加收货地址");
        img_back.setOnClickListener(this);
        AddTheReceivingAddressActivity_rl_selectarea.setOnClickListener(this);
        tv_save.setOnClickListener(this);

    }

    private void init() {
        String name, phone, address, addressinfo;
        topname.setText("编辑收货地址");
        isedit = true;
        name = it.getStringExtra("name");
        phone = it.getStringExtra("phone");
        address = it.getStringExtra("address");
        addressinfo = it.getStringExtra("addressinfo");

        AddTheReceivingAddressActivity_et_consignee.setText(name);
        AddTheReceivingAddressActivity_et_contactphone.setText(phone);
        tv_address_name.setText(address);
        tv_address_name.setTextColor(Color.rgb(0, 0, 0));
        AddTheReceivingAddressActivity_et_detailedaddress.setText(addressinfo);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        Log.e("aa", "开始解析数据");
//                        Toast.makeText(AddTheReceivingAddressActivity.this, "开始解析数据", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Log.e("aa", "解析数据成功");
//                    Toast.makeText(AddTheReceivingAddressActivity.this, "解析数据成功", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Log.e("aa", "解析数据失败");
//                    Toast.makeText(AddTheReceivingAddressActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
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

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                address_name = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                if (address_name != null && address_name.length() > 0) {
                    tv_address_name.setText(address_name);
                    tv_address_name.setTextColor(Color.rgb(0, 0, 0));
                }
                Log.e("aa", "选中的省/市/区：" + address_name);
//                Toast.makeText(AddTheReceivingAddressActivity.this, address_name, Toast.LENGTH_SHORT).show();
            }
        })

//                .setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //点击选择收货地址
            case R.id.AddTheReceivingAddressActivity_rl_selectarea:
                if (isLoaded) {
                    hintKbTwo();
                    ShowPickerView();
                } else {
                    Log.e("aa", "数据暂未解析成功，请等待");
                    Toast.makeText(AddOrEditTheReceivingAddressActivity.this, "数据暂未解析成功，请等待", Toast.LENGTH_SHORT).show();
                }
                break;
            //点击保存收货信息 上传到服务器
            case R.id.harvest_save:
                String phone = AddTheReceivingAddressActivity_et_contactphone.getText().toString();
                if (AddTheReceivingAddressActivity_et_consignee.getText().length() <= 0) {
                    Toast.makeText(this, "请输入收货人姓名", Toast.LENGTH_SHORT).show();
                } else if (!isMobile(phone)) {
                    Toast.makeText(this, "您输入的号码无效", Toast.LENGTH_SHORT).show();
                } else if (tv_address_name.getText().equals("请选择")) {
                    Toast.makeText(this, "请选择所在地区", Toast.LENGTH_SHORT).show();
                } else if (AddTheReceivingAddressActivity_et_detailedaddress.getText().length() <= 5) {
                    Toast.makeText(this, "请填写详细地址，不少于5个字", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    if (isedit == true) {
                        //编辑保存收货地址
                        new editTheReceivingAddressThread().start();
                    } else {
                        //添加收货地址
                        new addTheReceivingAddressThread().start();
                    }


                }
                break;
            case R.id.harves_black:
                finish();
                break;
        }
    }


    private class editTheReceivingAddressThread extends Thread {
        @Override
        public void run() {
            String basicUrl = AppConstants.URL_SUFFIX + "/rest/userAddressSaveVT";
            OkHttpUtils.post()
                    .url(basicUrl)
                    .addParams("token", token)
                    .addParams("editStatus", "1")
                    .addParams("userAddress.phone", AddTheReceivingAddressActivity_et_contactphone.getText().toString())
                    .addParams("userAddress.address", tv_address_name.getText().toString())
                    .addParams("userAddress.addressInfo", AddTheReceivingAddressActivity_et_detailedaddress.getText().toString())
                    .addParams("userAddress.name", AddTheReceivingAddressActivity_et_consignee.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }
                        @Override
                        public void onResponse(String response, int id) {
                            setResult(resultCodeTo_edit_succeed, null);
                            finish();
                        }
                    });
            super.run();
        }
    }
    private class addTheReceivingAddressThread extends Thread {
        @Override
        public void run() {
            String basicUrl = AppConstants.URL_SUFFIX + "/rest/userAddressSaveVT";

            OkHttpUtils.post()
                    .url(basicUrl)
                    .addParams("token", token)
                    .addParams("editStatus", "0")
                    .addParams("userAddress.phone", AddTheReceivingAddressActivity_et_contactphone.getText().toString())
                    .addParams("userAddress.address", tv_address_name.getText().toString())
                    .addParams("userAddress.addressInfo", AddTheReceivingAddressActivity_et_detailedaddress.getText().toString())
                    .addParams("userAddress.name", AddTheReceivingAddressActivity_et_consignee.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            setResult(resultCodeTo_add_succeed, null);
                            finish();
                        }
                    });
            super.run();
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


    /**
     * 手机号验证
     *
     * @return 验证通过返回true
     */
    public static boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

}
