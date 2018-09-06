package com.rongxun.xqlc.Service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.rongxun.xqlc.Util.UpdateApk;

import java.io.File;

public class UpdateService extends Service {

    /**
     * 安卓系统下载类
     **/
    private DownloadManager manager;
    /**
     * 接收下载完的广播
     **/
    private DownloadCompleteReceiver receiver;
    /**
     * 下载地址
     */
    private String downApkUrl;
    /**
     * 下载路径，如果不定义自己的路径，6.0的手机不自动安装
     */
    private String DOWNLOADPATH = "/xqlc/";

    /**
     * 初始化下载器
     **/
    private void initDownManager() {

        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();

        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(downApkUrl));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        //移动网络情况下是否允许漫游
        down.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downApkUrl));
        //设置下载文件的mineType
        down.setMimeType(mimeString);
        // 下载时，通知栏显示途中
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 显示下载界面
        down.setVisibleInDownloadsUi(true);
        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(DOWNLOADPATH, "xqlc.apk");
        down.setTitle("金储宝正在下载中...");
        // 将下载请求放入队列
        manager.enqueue(down);
        //注册下载广播
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public UpdateService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        downApkUrl = intent.getStringExtra(UpdateApk.DOWNLOAD_APK_URL);
        //判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "xqlc.apk";
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            try {
                // 调用下载
                initDownManager();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (receiver != null)
            // 注销下载广播
            unregisterReceiver(receiver);
        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (manager.getUriForDownloadedFile(downId) != null) {
                    //自动安装apk
                    installAPK(manager.getUriForDownloadedFile(downId), context);
                    //installAPK(context);
                } else {
                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                }
                //停止服务并关闭广播
                UpdateService.this.stopSelf();
            }
        }

        /**
         * 自动安装apk
         *
         * @param apk
         * @param context
         */
        private void installAPK(Uri apk, Context context) {
            if (Build.VERSION.SDK_INT < 23) {
                Intent intents = new Intent();
                intents.setAction("android.intent.action.VIEW");
                intents.addCategory("android.intent.category.DEFAULT");
                intents.setType("application/vnd.android.package-archive");
                intents.setData(apk);
                intents.setDataAndType(apk, "application/vnd.android.package-archive");
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intents);
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "xqlc.apk");
                if (file.exists()) {
                    openFile(file, context);
                }
            }
        }

        private void installAPK(Context context) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "xqlc.apk");
            if (file.exists()) {
                openFile(file, context);
            } else {
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }
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
            var5.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }

    }

    public String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

}
