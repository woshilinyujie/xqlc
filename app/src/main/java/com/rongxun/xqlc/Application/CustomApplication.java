package com.rongxun.xqlc.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;
import com.rongxun.xqlc.Util.GlideImageLoader;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2015/10/26.
 */
public class CustomApplication extends Application {


    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "JSESSIONID";

    private static CustomApplication instance;
    private SharedPreferences preferences;

    public static CustomApplication newInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
//        MobclickAgent.setDebugMode( true );
        instance = this;


        ///////////////////////////////////////////////////////////////
        //初始化X5内核
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
                Log.d("hsl", "onCoreInitFinished: -------->x5");

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("hsl", "加载内核是否成功:" + b);
            }
        });
        //////////////////////////////////////////////////////////////

        /////////////////////////okhttp的一些基础配置 start/////////////////////
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)//设置10s连接超时
                .readTimeout(10000L, TimeUnit.MILLISECONDS)//设置10s读取超时
                .writeTimeout(10000L, TimeUnit.MILLISECONDS)//设置10s写入超时
                // TODO: 2017/5/31 0031 其他更多配置
                .build();



        //////////////////////////友盟分享 start////////////////////////
//        Config.DEBUG = false;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wx584a74ca4412ecef", "47dd53611b864b4024f1cb486b5d5afc");
        PlatformConfig.setQQZone("1105338543", "1OQl1ddjmBCm2hy4");
//////////////////////////友盟分享 end////////////////////////
        OkHttpUtils.initClient(okHttpClient);
        /////////////////////////okhttp的一些基础配置 end/////////////////////
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), "9ad181e2ef", true);

        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        //        CrashReport.initCrashReport(context, "注册时申请的APPID", isDebug, strategy);
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        CrashReport.initCrashReport(context, strategy);


        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //网易七鱼初始化
        Unicorn.init(this, "2fccf295ba76c77271d12635dd6cc3ec", options(), new GlideImageLoader(this));

    }

    /**
     * 检查返回的Response header中有没有session
     *
     * @param responseHeaders Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> responseHeaders) {
        if (responseHeaders.containsKey(SET_COOKIE_KEY)
                && responseHeaders.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = responseHeaders.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.commit();
            }
        }
    }

    /**
     * 添加session到Request header中
     */
    public final void addSessionCookie(Map<String, String> requestHeaders) {
        String sessionId = preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (requestHeaders.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(requestHeaders.get(COOKIE_KEY));
            }
            requestHeaders.put(COOKIE_KEY, builder.toString());
        }
    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


    private static List<Activity> lists = new ArrayList<>();

    public static void addActivity(Activity activity) {
        lists.add(activity);
    }

    public static void removeActivity(Activity activity) {
        lists.remove(activity);
    }

    public static void clearActivity() {
        if (lists != null) {
            for (Activity activity : lists) {
                activity.finish();
            }

            lists.clear();
        }
    }

    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        options.savePowerConfig = new SavePowerConfig();
        options.uiCustomization=new UICustomization();
        options.uiCustomization.titleCenter=true;
        options.savePowerConfig.customPush=true;
        return options;
    }

}
