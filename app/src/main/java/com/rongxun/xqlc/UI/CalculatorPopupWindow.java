package com.rongxun.xqlc.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.DecimalFormatUtil;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * PROJECT_NAME: Sample
 * PACKAGE_NAME: com.guaika.sample.view
 * Time: 2017/08/22 12:33
 * E-mail: 13967189624@163.com
 * Description:
 */

public class

CalculatorPopupWindow extends PopupWindow implements View.OnClickListener {

    private Button btn0;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btnPoint;
    private ImageView imageDelete;
    private ImageView imgClose;//关闭
    private TextView txtEarning;
    private EditText edtMoney;
    private TextView txtPercent;//年化利率
    private TextView txtLimitTime;//收益期限
    private DecimalFormatUtil util;//数据格式化工具

    private String dayEarning;//日收益
    private String yearEarning;//年化利率
    private int calLimit;//剩余投资天数

    private Timer timer;
    private MyTimerTask mTask;
    private LayoutInflater inflater;
    private View view;
    Context mContext;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x11:
                    int index = edtMoney.getSelectionStart();
                    if (index > 0)
                        edtMoney.getText().delete(index - 1, index);
                    break;

                default:
                    break;
            }
        }
    };

    public CalculatorPopupWindow(Context context) {
        super(context);
        mContext = context;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.manage_calculator, null);
        setContentView(view);

        //设置宽与高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        /**
         * 设置可以获取集点
         */
        setFocusable(false);

        /**
         * 设置点击外边不可以消失
         */
        setOutsideTouchable(false);

        /**
         *设置可以触摸
         */
        setTouchable(true);


        /**
         * 设置进出动画
         */
        setAnimationStyle(R.style.Calculator_Popup);


        /**
         * 实例化一个ColorDrawable颜色为半透明
         * 设置SelectPicPopupWindow弹出窗体的背景
         */

        ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);

        //初始化
        init();
    }


    public void showPopup() {

        //设值
        if (yearEarning != null) {
            txtPercent.setText(yearEarning);
        }
        txtLimitTime.setText(calLimit + "");

        //设置显示位置
        if (!isShowing()) {
            showAtLocation(view, Gravity.BOTTOM, 0, 0);
            backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
        }
    }

    private void init() {

        //////////////////////键盘按键/////////////////////
        btn0 = (Button) view.findViewById(R.id.keyboard_btn_0);
        btn1 = (Button) view.findViewById(R.id.keyboard_btn_1);
        btn2 = (Button) view.findViewById(R.id.keyboard_btn_2);
        btn3 = (Button) view.findViewById(R.id.keyboard_btn_3);
        btn4 = (Button) view.findViewById(R.id.keyboard_btn_4);
        btn5 = (Button) view.findViewById(R.id.keyboard_btn_5);

        btn6 = (Button) view.findViewById(R.id.keyboard_btn_6);
        btn7 = (Button) view.findViewById(R.id.keyboard_btn_7);
        btn8 = (Button) view.findViewById(R.id.keyboard_btn_8);
        btn9 = (Button) view.findViewById(R.id.keyboard_btn_9);
        btnPoint = (Button) view.findViewById(R.id.keyboard_btn_point);
        imageDelete = (ImageView) view.findViewById(R.id.keyboard_image_delete);
        imgClose = (ImageView) view.findViewById(R.id.calculator_close);

        ///////////////////////金额 收益///////////////////
        txtEarning = (TextView) view.findViewById(R.id.manage_txt_earning);
        edtMoney = (EditText) view.findViewById(R.id.edt_money);
        txtPercent = (TextView) view.findViewById(R.id.calculator_txt_earning);
        txtLimitTime = (TextView) view.findViewById(R.id.calculator_txt_limit_time);

        util = DecimalFormatUtil.getInstance();

        //监听事件
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnPoint.setOnClickListener(this);
        imageDelete.setOnClickListener(this);
        imgClose.setOnClickListener(this);

        //设置不显示软键盘
        setKey();
        listener();
    }

    private void listener() {

        imageDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // TODO Auto-generated method stub
                if (!edtMoney.getText().toString().equals("")) {
                    timer = new Timer();
                    mTask = new MyTimerTask();
                    timer.schedule(mTask, 0, 100); //定时器启动，隔500毫秒触发一次
                }
                return false;
            }
        });

        //触摸事件，为了得到Button的松开事件，从而停止定时器
        imageDelete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //如果松开
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (timer != null)
                        timer.cancel();  //定时器停止

                }
                return false;
            }
        });

        edtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO: 2017/8/22 0022 计算收益
                // 预期收益 ＝ 该项目天化收益 ＊ 输入投资金额 ＊ 投资期限
                if (dayEarning != null
                        && s != null
                        && !s.toString().equals("")
                        && !s.toString().equals(".")
                        ) {

                    double earn = Double.parseDouble(s.toString())
                            * calLimit
                            * (Double.parseDouble(dayEarning));
                    txtEarning.setText(util.getDouble2(earn));

                } else {
                    txtEarning.setText("0.00");
                }

            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext, 1.0f);
            }
        });

    }

    /**
     * 设置不调用系统键盘
     */
    private void setKey() {

        if (android.os.Build.VERSION.SDK_INT <= 10) {
            edtMoney.setInputType(InputType.TYPE_NULL);
        } else {
            ((Activity) mContext).getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edtMoney, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        //获取当前光标的位置
        int index = edtMoney.getSelectionStart();
        //末尾
        int end = edtMoney.getSelectionEnd();

        Editable editable = edtMoney.getText();
        //获取输入框内容
        String content = editable.toString().trim();

        switch (v.getId()) {

            case R.id.keyboard_btn_0:
                //正常的数字键
                editable.insert(index, "0");
                break;
            case R.id.keyboard_btn_1:
                editable.insert(index, "1");
                break;
            case R.id.keyboard_btn_2:
                editable.insert(index, "2");
                break;
            case R.id.keyboard_btn_3:
                editable.insert(index, "3");
                break;
            case R.id.keyboard_btn_4:
                editable.insert(index, "4");
                break;
            case R.id.keyboard_btn_5:
                editable.insert(index, "5");
                break;
            case R.id.keyboard_btn_6:
                editable.insert(index, "6");
                break;
            case R.id.keyboard_btn_7:
                editable.insert(index, "7");
                break;
            case R.id.keyboard_btn_8:
                editable.insert(index, "8");
                break;
            case R.id.keyboard_btn_9:
                editable.insert(index, "9");
                break;
            case R.id.keyboard_btn_point:
                //小数点，只出现一次
                if (!content.contains(".")) {
                    //插入字符
                    editable.insert(index, ".");
                }
                break;
            case R.id.keyboard_image_delete:
                //删除键
                if (content.length() > 0) {
                    if (index == end) {
                        if (index > 0)
                            editable.delete(index - 1, index);
                    } else {
                        editable.delete(index, end);
                    }
                }
                break;
            case R.id.calculator_close:
                dismiss();
                break;


        }

    }

    //定时事件
    private class MyTimerTask extends TimerTask {
        public void run() {
            // TODO Auto-generated method stub
            Log.e("进入子线程", "");
            //如果EditText中的内容为空，定时器停止,否则继续往下执行，
            if (edtMoney.getText().toString().equals("")) {
                Log.e("为空停止定时器", "");
                timer.cancel();
            } else {

                handler.sendEmptyMessage(0x11);
            }
        }

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    public void setDayEarning(String dayEarning) {

        this.dayEarning = dayEarning;
    }

    public void setYearEarning(String yearEarning) {
        this.yearEarning = yearEarning;
    }

    public void setCalLimit(int calLimit) {
        this.calLimit = calLimit;
    }
}
