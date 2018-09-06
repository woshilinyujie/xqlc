package com.rongxun.xqlc.Gesture.gestures.Util;

/**
 * Created by Administrator on 2015/10/22.
 */
public  class AppConstants {


      public static final String URL_SUFFIX ="https://api.hzjcb.com";
      public static final String IMG_URL_SUFFIX="https://www.hzjcb.com/mobile";

      //测试
//      public static final String URL_SUFFIX ="http://192.168.0.215:8080";
//      public static final String IMG_URL_SUFFIX="http://192.168.0.215:8080/mobile";
//
//      public static final String URL_SUFFIX ="http://160836c5u5.iok.la:9988";
//      public static final String IMG_URL_SUFFIX="http://160836c5u5.iok.la:9988/mobile";

//      public static final String URL_SUFFIX ="http://192.168.0.106:8080";
//      public static final String IMG_URL_SUFFIX="http://192.168.0.106:8080/mobile";

//      public static final String URL_SUFFIX ="http://hzgeek.imwork.net:9988";
//      public static final String IMG_URL_SUFFIX="http://hzgeek.imwork.net:9988/mobile";


      //手机号码验证的失效时间（单位：秒）
      public static final int VerifyCodeTimeFuture =60;

      //手机号码验证时候的提醒时间间隔（单位：秒）
      public static final int VerifyCodeTimeInteral =1;

      //预览页面关闭，跳到投资中心
      public static final int ChooseHongBaoOkCode =9092;

      //预览页面关闭，跳到投资中心
      public static final int IntroToInvestCode =7002;
      //预览页面关闭，跳到登录
      public static final int PreviewToLoginCode =8002;
      //预览页面关闭，跳到投资中心
      public static final int PreviewToInvestOthersCode =8001;
      //退出登录回调代码
      public static final int AppLoginEXitCode =9999;
      //邮箱验证回调代码（正在验证中）
      public static final int EmailAuthIng =7070;
      public static final int EmailAuthSuccess =7071;
      //实名认证回调代码（正在审核中）
      public static final int RealAuthIng =6060;
      public static final int RealAuthSuccess =6061;

      //银行卡充值完成后结果回调代码
      public static final int ChargeMoneyResultBack =5050;
      //银行卡绑定完成后结果回调代码
      public static final int BindBankCardResultSuccess =4040;

      //返回账户中心的回调代码
      public static final int backToUserCenterResult =3030;
      //投资成功后的回调代码
      public static final int projectInvestSuccess =2020;

      //修改登录密码成功后的回调
      public static final int modifyLoginSuccess =1010;
      //
      public static final int registerSuccessCode =1020;

}
