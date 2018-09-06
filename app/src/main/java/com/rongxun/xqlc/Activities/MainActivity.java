package com.rongxun.xqlc.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rongxun.xqlc.Adapters.MainFragmentPagerAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.home.ActivityDialogBean;
import com.rongxun.xqlc.Beans.home.SecondBean;
import com.rongxun.xqlc.Fragments.HomeFragment;
import com.rongxun.xqlc.Fragments.MainFragment;
import com.rongxun.xqlc.Fragments.ManageFragment;
import com.rongxun.xqlc.Fragments.MineFragment;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.MainTipsDialog;
import com.rongxun.xqlc.UI.MainViewPager;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.AppVersionUtils;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.Util.PrefUtils;
import com.rongxun.xqlc.Util.PreferenceUtil;
import com.rongxun.xqlc.Util.UpdateApk;
import com.rongxun.xqlc.Util.Utils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class MainActivity extends MyBaseActivity implements View.OnClickListener {

    public static final int MANAGE = 1;//理财


    private long mExitTime;//保存上次点击back键的系统时间
    public static final int PERMISSION_S = 110;//存储卡权限
    public static final int PERMISSION_P = 220;//手机权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomApplication.addActivity(this);
        String flag = getIntent().getStringExtra("flag");
        MainFragment fragment=new MainFragment(this,flag);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).commit();

        // TODO: 2017/8/21 0021  判断是不是由广告详情页跳过来的，再判断是不是有参数，若有，则显示理财列表
//        String flag = getIntent().getStringExtra("flag");
//
//        //是否显示理财界面
//        if (flag != null && flag.equals("ads")) {
//            pager.setCurrentItem(MANAGE);
//        }

        //////////////////是否显示读秒页面///////////////////
        showSecond();

        /////////////////////检测更新///////////////////////

//        // TODO: 2017/8/8 0008
//        //============方案1==========//
//        //检测更新
//        boolean noTips = PrefUtils.getBoolean(this, "update_no_tips", false);
//        //用户点击暂不更新，不再弹出
//        if (!noTips) {
//            UpdateApk.getInstance().startUpdate(this, AppConstants.UPADTE_APK, UpdateApk.MAIN);
//        }

        //============方案2=========//
        //更新完成，不在弹出
        UpdateApk.getInstance().startUpdate(this,AppConstants.UPADTE_APK,UpdateApk.MAIN,
                new UpdateApk.ShowActivityDialog() {
                    @Override
                    public void showActivity() {
                        ////////////////////活动弹窗///////////////////////
                        // TODO: 2017/8/8 0008
                        ActivityDialog();
                    }
                });

        ////////////////////上传设备信息////////////////////
        // TODO: 2017/8/8 0008
        //如果第一次上传成功，则不再上传
        boolean firstSuccess = PrefUtils.getBoolean(this, "is_first_success", false);
        if (!firstSuccess) {
            UploadDeviceInformation();
        }
    }




    /**
     * 读秒界面
     */
    private void showSecond() {

        OkHttpUtils
                .post()
                .url(AppConstants.IS_SECOND)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(final String response, int id) {

                        if (response != null) {
                            final SecondBean secondBean = GsonUtils.GsonToBean(response, SecondBean.class);
                            if (secondBean.getRcd().equals("R0001")) {

                                if (secondBean.getImageUrl() != null) {
                                    //缓存图片
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //在主线程异步加载
                                            Glide.with(MainActivity.this)
                                                    .load(secondBean.getImageUrl())
                                                    .downloadOnly(500, 500);
                                            PrefUtils.putString(MainActivity.this, "second", secondBean.getImageUrl());
                                            PrefUtils.putString(MainActivity.this, "second_detail", secondBean.getTypeTarget());
                                        }
                                    }).start();

                                } else {
                                    //清空缓存
                                    PrefUtils.putString(MainActivity.this, "second", "");
                                    PrefUtils.putString(MainActivity.this, "second_detail", "");
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }



    /**
     * @param requestCode
     * @param permissions  权限数组
     * @param grantResults 授权结果数组
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_S:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意
                    // TODO: 2017/8/8 0008 直接下载
                    UpdateApk.getInstance().update();
                } else {
                    // 权限被用户拒绝了
                    Toast.makeText(this, "没权限，无法下载，请到应用管理打开权限", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSION_P:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意
                    // TODO: 2017/8/8 0008 直接上传
                    deviceInformation();
                } else {
                    // 权限被用户拒绝了
                    Toast.makeText(this, "没权限，无法上传", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 上传设备信息
     */
    private void UploadDeviceInformation() {

        if (Utils.isAndroid6()) {

            //求情手机信息的权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // 没有权限。
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    // TODO: 2017/8/8 0008 第一次决绝了，第二次在申请时就会回调
                    Toast.makeText(this, "权限已被您拒绝，请在 设置--应用程序 中打开", Toast.LENGTH_SHORT).show();

                } else {
                    // 申请授权。
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_P);
                }
            } else {
                //有权限
                deviceInformation();
            }
        } else {

            deviceInformation();
        }


    }

    /**
     * 设备信息上传
     */
    private void deviceInformation() {
        OkHttpUtils
                .post()
                .url(AppConstants.DEVICE_INFORMATION)
                .addParams("appType", "1")
                .addParams("placeName", AppVersionUtils.getChannelName(this, "UMENG_CHANNEL") + "")
                .addParams("im", AppVersionUtils.getPhoneIMEI(this))
                .addParams("deviceToken", PreferenceUtil.getPrefString(this, "deviceToken", ""))
                .addParams("welcome", AppVersionUtils.getPhoneIMEI(this))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误
                        Toast.makeText(MainActivity.this, "设备信息上传失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {
                            // TODO: 2017/8/8 0008
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object != null && object.getString("rcd").equals("R0001")) {
                                    PrefUtils.putBoolean(MainActivity.this, "is_first_success", true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

    }

    /**
     * 设置活动弹窗
     */
    private void ActivityDialog() {

        OkHttpUtils
                .get()
                .url(AppConstants.ACTIVITY_DIALOG)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // TODO: 2017/8/8 0008
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        // TODO: 2017/8/8 0008
                        if (response != null) {
                            ActivityDialogBean dialogBean = GsonUtils.GsonToBean(response, ActivityDialogBean.class);
                            if (dialogBean != null && dialogBean.getRcd().equals("R0001")) {

                                setMainActivityDialog(dialogBean);
                            }
                        }

                    }
                });
    }


    /**
     * @param dialogBean
     */
    private void setMainActivityDialog(ActivityDialogBean dialogBean) {

        if (dialogBean != null && dialogBean.getImageUrl() != null) {

            final String currentUrl = dialogBean.getImageUrl();
            final String typeTarget = dialogBean.getTypeTarget();

            String lastUrl = PrefUtils.getString(MainActivity.this, "imgUrl", "");

            //如果还是同一个url,则不显示对话框
            if (TextUtils.equals(currentUrl, lastUrl)) {
                return;
            }

            //设置对话框
            final MainTipsDialog tipsDialog = new MainTipsDialog(MainActivity.this);
            tipsDialog.setImage(currentUrl);
            tipsDialog.setListener(new MainTipsDialog.ImageOnClickListener() {
                @Override
                public void imageOnclick() {
                    Intent h5 = new Intent(MainActivity.this, H5JSActivity.class);
                    h5.putExtra("web_url", typeTarget);
                    startActivity(h5);
                    //保存配置
                    PrefUtils.putString(MainActivity.this, "imgUrl", currentUrl);
                    tipsDialog.dismiss();
                }
            });
        }

    }


    /**
     * 再按一次退出应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //时间相隔大于2s
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                //否则退出应用
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        MyBaseActivity.isActive = true;
    }

}
