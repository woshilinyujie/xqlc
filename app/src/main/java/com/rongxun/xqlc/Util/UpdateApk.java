package com.rongxun.xqlc.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.rongxun.xqlc.Beans.home.UpdateBean;
import com.rongxun.xqlc.Service.UpdateService;
import com.rongxun.xqlc.UI.CheckNetTypeDialog;
import com.rongxun.xqlc.UI.MainUpdateDialog;
import com.rongxun.xqlc.UI.UpdateDialog;
import com.rongxun.xqlc.UI.UpdateWaitingDialog;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.FileCallBack;
import com.rongxun.xqlc.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * Author: HouShengLi
 * Time: 2017/05/24 13:38
 * E-mail: 13967189624@163.com
 * Description: APP 版本更新
 */

public class UpdateApk {

    public static final int PERMISSION = 110;//权限请求码
    public static final String DOWNLOAD_APK_URL = "downApkUrl";
    public static final int SUCCESS = 200;//求情成功
    public static final int FAILURE = 500;//请求失败
    public static final int MAIN = 0;//主页更新
    public static final int MORE = 1;//点击按钮检测更新
    public static final String TAG = "more_update";
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xqlc/";
    private String downApkUrl;//apk下载地址
    private Activity mActivity;
    private int flag;
    private String force;//是否强制更新的标识

    private static UpdateApk update;

    private UpdateApk() {
        //空构造器
    }

    public static UpdateApk getInstance() {

        if (update == null) {
            update = new UpdateApk();
        }
        return update;
    }

    /**
     * 开启更新
     *
     * @param mActivity
     * @param updateUrl
     * @param flag      标识 是从MainActivity更新，点击检测按钮更新
     */
    public void startUpdate(Activity mActivity, String updateUrl, int flag, ShowActivityDialog dialog) {
        this.flag = flag;
        this.mActivity = mActivity;
        getNewVersionCode(updateUrl, dialog);
    }

    /**
     * 网络获取最新的版本号
     *
     * @param updateUrl
     */
    private void getNewVersionCode(String updateUrl, final ShowActivityDialog dialog) {

        //获取当前版本号
        int version = AppVersionUtils.getVersionCode(mActivity);
        //测试
//        updateUrl = "http://192.168.0.182:7070/rest/versionVT";
        OkHttpUtils
                .post()
                .url(updateUrl)
                .addParams("flag", "1")//"1":android
                .addParams("version", version + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mActivity, "连接网络失败,请检查网络！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Log.d(TAG, "onResponse: --->" + response);
                        UpdateBean updateBean = GsonUtils.GsonToBean(response, UpdateBean.class);

                        if (updateBean != null && updateBean.getRcd().equals("R0001")) {

                            //获取是否开启更新的标识 true:开启 false:关闭
                            boolean enabled = updateBean.isEnabled();
                            if (enabled) {
                                //更新准备 ready go....
                                String versionName = updateBean.getVersionName();//获取版本名称
                                int newVersionCode = updateBean.getVersionCode();//获取最新的版本号
                                force = updateBean.getUpdate();//强制非强制更新的标识：1强制 0非强制
                                downApkUrl = updateBean.getUrl();//获取下载的apk的url;

                                // TODO: 2017/8/21 0021 活动弹窗
                                if (force.equals("0")) {
                                    //非强制跟新显示活动弹窗
                                    if (dialog != null) {
                                        dialog.showActivity();
                                    }
                                }

                                //检测是否有新的版本
                                if (versionName != null && isNewVersion(newVersionCode)) {

                                    switch (flag) {

                                        case MORE:
                                            //弹出 检测更新提示对话框
                                            showIsNewVersionDialog(versionName);
                                            break;
                                        case MAIN:
                                            //弹出 主页更新对话框:强制、非强制
                                            showIsNewVersionDialog2(versionName, updateBean.getDescription());
                                            break;
                                    }

                                } else {
                                    if (flag == MORE) {
                                        Toast.makeText(mActivity, "已经是最新版本", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            // TODO: 2017/8/21 0021 显示活动弹窗
                            if (dialog != null) {
                                dialog.showActivity();
                            }
                            if (flag == MORE) {
                                Toast.makeText(mActivity, "已是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });


    }

    /**
     * 检查软件是否有更新版本
     *
     * @param newVersionCode 服务器请求的最新版本号
     * @return
     */
    private boolean isNewVersion(int newVersionCode) {
        // 获取当前软件版本
        int versionCode = AppVersionUtils.getVersionCode(mActivity);
        if (versionCode < newVersionCode) {
            return true;
        }
        return false;
    }

    /**
     * 显示检测到新版本对话框  注：点击检测更新按钮弹出的对话框
     *
     * @param versionName
     */
    private void showIsNewVersionDialog(String versionName) {

        final UpdateDialog dialog = new UpdateDialog(mActivity);
        dialog.show();
        //点击立即更新
        dialog.setUpdateOnClick(new UpdateDialog.UpdateOnClick() {
            @Override
            public void onClick() {
                //取消dialog
                dialog.dismiss();
                //请求权限
                check6Permission();
            }
        });
    }

    /**
     * 主页更新 弹出的对话框
     *
     * @param versionName
     * @param msg
     */
    private void showIsNewVersionDialog2(String versionName, String msg) {

        final MainUpdateDialog mainUpdateDialog = new MainUpdateDialog(mActivity);
        mainUpdateDialog.setContent(msg);//设置更新的内容
        if (force != null && force.equals("0")) {
            //非强制更新，显示取消按钮
            mainUpdateDialog.getBtnCancel().setVisibility(View.VISIBLE);
        } else if (force != null && force.equals("1")) {
            //强制更新，隐藏取消按钮
            mainUpdateDialog.getBtnCancel().setVisibility(View.GONE);
        }
        mainUpdateDialog.show();
        //点击更新
        mainUpdateDialog.setMainUpdateOnClick(new MainUpdateDialog.MainUpdateOnClick() {
            @Override
            public void onOkClick() {
                //如果属于强制更新则不取消dialog
                if (force != null && force.equals("1")) {
                    //请求过权限
                    check6Permission();
                } else if (force != null && force.equals("0")) {
                    //取消dialog
                    mainUpdateDialog.dismiss();
                    //请求过权限
                    check6Permission();
                }


            }

            @Override
            public void onCancelClick() {
                //点击取消，主页弹框将不再出现
                // TODO: 2017/8/4 0004 暂留
                PrefUtils.putBoolean(mActivity, "update_no_tips", true);
            }
        });

    }

    private void check6Permission() {

        //Android6.0权限
        if (Utils.isAndroid6()) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 没有权限。
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                } else {
                    // 申请授权
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, PERMISSION);
                }
            } else {
                //开启下载
                update();
            }
        } else {
            //Android6.0以下直接下载，无需动态的请求权限
            update();
        }
    }

    /**
     * 开始更新
     */
    public void update() {
        //检查网络是否可用
        if (CheckNetType.IsNetWorkEnable(mActivity)) {

            // TODO: 2017/8/2 0002 如果强更，不显示网络状态,判断语句暂留

            if (force != null && force.equals("1")) {
                startUpdateService();
            } else if (force != null && force.equals("0")) {
                //获取当前网络类型
                String currentNetworkType = CheckNetType.getCurrentNetworkType();
                if (TextUtils.equals(currentNetworkType, "Wi-Fi")) {
                    //wifi环境直接下载
                    startUpdateService();
                } else {

                    // 对话框提醒：非wifi环境是否下载
                    shownetTypeDialog(currentNetworkType);
                }
            }
        }
    }

    /**
     * 显示当前网络类型的对话框
     *
     * @param netType 网络类型
     */
    private void shownetTypeDialog(String netType) {

        final CheckNetTypeDialog netTypeDialog = new CheckNetTypeDialog(mActivity);
        netTypeDialog.setNetType(netType);
        netTypeDialog.show();
        //点击是
        netTypeDialog.setNetTypeOnClick(new CheckNetTypeDialog.NetTypeOnClick() {
            @Override
            public void onClick() {
                //开启服务下载
                startUpdateService();
                //取消对话框
                netTypeDialog.dismiss();
            }
        });
    }


    /**
     * 开启服务下载apk
     */
    private void startUpdateService() {

        if (flag == UpdateApk.MORE) {
            //手动检测更新
            normalUpdate();
        } else {

            if (force.equals("0")) {
                //主页正常更新
                normalUpdate();
            } else {
                //主页强制更新
                forceUpate();
            }

        }

    }

    /**
     * 正常更新下载apk
     */
    private void normalUpdate() {
        Intent upDateIntent = new Intent(mActivity, UpdateService.class);
        upDateIntent.putExtra(DOWNLOAD_APK_URL, downApkUrl);
        mActivity.startService(upDateIntent);
    }

    /**
     * 强制更新apk 带进度条
     */
    private void forceUpate() {

        final UpdateWaitingDialog dialog = new UpdateWaitingDialog(mActivity);
        //显示更新
        dialog.show();
        dialog.getTxtTips().setText("金储宝正在下载中...");


        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            //如果文件存在，则删除文件
            File file = new File(path, "xqlc.apk");
            if (file.exists()) {
                file.delete();
            }

            OkHttpUtils
                    .get()
                    .url(downApkUrl)
                    .build()
                    .execute(new FileCallBack(path, "xqlc.apk") {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(File response, int id) {

                        }

                        @Override
                        public void inProgress(float progress, long total, int id) {

                            dialog.getBar().setProgress(false, progress * 360);
                            dialog.getBar().setText((int) (progress * 100) + "%");
                            if (dialog.getBar().getProgress() == 360) {
                                dialog.cancel();
                                installAPK(mActivity);
                            }
                        }

                    });
        }


    }


    /**
     * 安装apk
     *
     * @param context
     */
    private void installAPK(Context context) {
        File file = new File(path + "xqlc.apk");
        if (file.exists()) {
            openFile(file, context);
        } else {
            Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        //是否是7.0版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(268435456);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context,  context.getPackageName() + ".fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }else{
            intent.addFlags(268435456);
            String type = getMIMEType(file);
            intent.setDataAndType(Uri.fromFile(file), type);
        }
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            Log.e("安装失败",var5.toString());
            var5.printStackTrace();
            Toast.makeText(context, "˙没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }

    }

    public String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    /**
     * 接口
     * 作用：首页活动弹窗的限制：如果是强制更新，不显示活动弹窗，否则正常显示
     */
    public interface ShowActivityDialog {

        void showActivity();
    }
}
