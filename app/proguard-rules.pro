# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

###############################一般使用得默认混淆#####################################


#混淆第三方库
-dontskipnonpubliclibraryclasses
#代码混淆压缩比，值在0~7之间，默认为5
-optimizationpasses 5
#不使用大小写混淆类名，混淆后的类名为小写
-dontusemixedcaseclassnames
#混淆时记录日志，有助于排查错误
-verbose
-printmapping proguardMapping.txt
#混淆时允许访问并修改有修饰符的类和类成员
-allowaccessmodification
#代码混淆使用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#不进行优化
-dontoptimize
#不进行预校验
-dontpreverify
#对注解中的参数进行保留
-keepattributes *Annotation*,InnerClasses
#指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
#不混淆泛型
-keepattributes Signature
#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable


###################################不混淆########################################


#这些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

#Native方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#自定义组件不混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#自定义控件类和类的成员不混淆（所有指定的类和类成员是要存在的）
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#自定义控件类不混淆
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

#枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#R文件的静态成员
-keepclassmembers class **.R$* {
    public static <fields>;
}

#注解继承类不混淆
-keep class * extends java.lang.annotation.Annotation {*;}

#保持Serializable实现类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#Webview不混淆内容
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}


-dontwarn com.qiyukf.**
-keep class com.qiyukf.** {*;}

#android support.v4/v7包不混淆
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }

#打包时忽略警告
-dontwarn android.support.**

##实体类不混淆
-keep class com.hzgeek.jinwanlicai.bean** { *; }
-keep class com.hzgeek.jinwanlicai.httpcallback** { *; }
-keep class com.hzgeek.jinwanlicai.okhttp.callback** { *; }



##################################第三方库的混淆######################################

#如果你的项目中用了第三方库，请参考其官方文档说明进行混淆

#####Okhttp START#####
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
#####Okhttp END#####


#####Gilde Start#####
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#####Glide END#####


#####Fastjson START#####
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
#####Fastjson END#####

#####Jpush Start#####
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#####Jpush End#####

######Google GSON Start######
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
######Google GSON End######

#####meiqia start#####

## ----------------------------------
##      OkHttp相关
## ----------------------------------
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**

## ----------------------------------
##      Okio相关
## ----------------------------------
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

## ----------------------------------
##      UIL相关
## ----------------------------------
-keep class com.nostra13.universalimageloader.** { *; }
-keepclassmembers class com.nostra13.universalimageloader.** {*;}
-dontwarn com.nostra13.universalimageloader.**

## ----------------------------------
##      Glide相关
## ----------------------------------
-keep class com.bumptech.glide.Glide { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.**

## ----------------------------------
##      Picasso相关
## ----------------------------------
-keep class com.squareup.picasso.Picasso { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn com.squareup.picasso.**

## ----------------------------------
##      xUtils3相关
## ----------------------------------
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}
-dontwarn org.xutils.**
#####meiqia end#####

#####友盟 start#####

#--统计--#
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class [com.hzgeek.jinwanlicai].R$*{
public static final int *;
}

#--分享--#
-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keep public class com.umeng.socialize.* {*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {*;}
-keep class com.tencent.mm.opensdk.** {*;}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }
-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
    }
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
    }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
       *;
    }
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
    }

-keep class com.linkedin.** { *; }
-keepattributes Signature

#####友盟 end#####

#####Banner start#####
# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }
#####Banner end#####
