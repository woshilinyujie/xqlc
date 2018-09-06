package com.rongxun.xqlc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Bundle bundle = intent.getExtras();

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

                // TODO: 2017/5/23 0023 取得RegistrationID并保存与Appuid的关系到开发者自己的应用服务器上
                // TODO: 2017/5/23 0023 ：然后就可以像指定的用户1对1发送消息
                // TODO: 2017/5/23 0023 :RegistrationID  SDK向JPush Server注册所得到的注册ID

                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

                // TODO: 2017/5/23 0023 ://接收自定义消息
                // TODO：注：自定义消息SDK只是传递，不会有任何界面上的展示(通知栏不显示)，所以必须在此处处理

                //获取服务器 推送内容的标题
                String title = bundle.getString(JPushInterface.EXTRA_TITLE);
                //推送的内容
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                //保存服务器推送下来的附加字段。这是个 JSON 字符串
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);


            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

                // TODO: 2017/5/23 0023 : 接收通知，如果通知为空，则在通知栏上不会展示通知
                // TODO: 2017/5/23 0023 : 但是，这个广播 Intent 还是会有,开发者可以取到通知内容外的其他信息

                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//标题
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);//对应于推送框内的信息
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//字段json

                //通知栏的Notification ID，可以用于清除Notification
                //如果服务端内容（alert）字段为空，则notification id 为0
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

                //富媒体通知推送下载的HTML的文件路径,用于展现WebView。
                String fileHtml = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_PATH);

                //富媒体通知推送下载的图片资源的文件名,多个文件名用 “，” 分开。
                //与 “JPushInterface.EXTRA_RICHPUSH_HTML_PATH” 位于同一个路径。
                String fileStr = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_RES);
                String[] fileNames = fileStr.split(",");

                //.........还有好多可以接受的内容，详见api


            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

                // TODO: 2017/5/23 0023 : 用户点击了通知栏的通知
                // TODO: 2017/5/23 0023 : 没注册广播，默认打开应用程序的主Activity，相当于用户点击桌面图标的效果
                // TODO: 2017/5/23 0023 : 注册了，就不会默认打开主Activity，在这里处理逻辑

//				//可以打开自定义的Activity处理逻辑
//				Intent i = new Intent(context, MainActivity.class);
//				i.putExtras(bundle);
//				context.startActivity(i);


            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {

                // TODO: 2017/5/23 0023 此处尽然也可以处理富消息
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..


            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {

                // TODO: 2017/5/23 0023 JPush 服务的连接状态发生变化。（注：不是指 Android 系统的网络连接状态。）
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);

            } else {

                // TODO: 2017/5/23 0023
            }
        } catch (Exception e) {

        }

    }
}
