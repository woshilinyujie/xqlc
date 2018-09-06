package com.rongxun.xqlc.Util;

/**
 * Created by Administrator on 2015/10/22.
 */
public class AppConstants {
//    public static final String URL_SUFFIX ="https://api.hzjcb.com";
//    public static final String IMG_URL_SUFFIX = "https://www.hzjcb.com/mobile";

    public static final String URL_SUFFIX = "http://47.99.94.144:8082";
//    public static final String IMG_URL_SUFFIX = "http://120.27.160.157:82/mobile";

    //本地测试
//    public static final String URL_SUFFIX = "http://192.168.1.93:8080";
//    public static final String IMG_URL_SUFFIX = "http://192.168.0.92:8080/mobile";


    //热门活动API
    public static final String HUODONG = URL_SUFFIX + "/rest/activity";
    //平台公告
    public static final String COMMUNIQUE = URL_SUFFIX + "/rest/articleList/app_site_notice";
    //平台公告详情(文章详情)
    public static final String ARTICLE = URL_SUFFIX + "/rest/article/";

    //测试
//      public static final String URL_SUFFIX ="http://192.168.0.215:8080";
//      public static final String IMG_URL_SUFFIX="http://192.168.0.215:8080/mobile";
//
//      public static final String URL_SUFFIX ="http://160836c5u5.iok.la:9988";
//      public static final String IMG_URL_SUFFIX="http://160836c5u5.iok.la:9988/mobile";

//      public static final String URL_SUFFIX ="http://192.168.0.140:8080";
//      public static final String IMG_URL_SUFFIX="http://192.168.0.140:8080/mobile";

//      public static final String URL_SUFFIX ="http://hzgeek.imwork.net:8181";
//      public static final String IMG_URL_SUFFIX="http://hzgeek.imwork.net:8181/mobile";


    //手机号码验证的失效时间（单位：秒）
    public static final int VerifyCodeTimeFuture = 60;

    //手机号码验证时候的提醒时间间隔（单位：秒）
    public static final int VerifyCodeTimeInteral = 1;

    //预览页面关闭，跳到投资中心
    public static final int ChooseHongBaoOkCode = 9092;

    //预览页面关闭，跳到投资中心
    public static final int IntroToInvestCode = 7002;
    //预览页面关闭，跳到登录
    public static final int PreviewToLoginCode = 8002;
    //预览页面关闭，跳到投资中心
    public static final int PreviewToInvestOthersCode = 8001;
    //退出登录回调代码
    public static final int AppLoginEXitCode = 9999;
    //邮箱验证回调代码（正在验证中）
    public static final int EmailAuthIng = 7070;
    public static final int EmailAuthSuccess = 7071;
    //实名认证回调代码（正在审核中）
    public static final int RealAuthIng = 6060;
    public static final int RealAuthSuccess = 6061;

    //银行卡充值完成后结果回调代码
    public static final int ChargeMoneyResultBack = 5050;
    //银行卡绑定完成后结果回调代码
    public static final int BindBankCardResultSuccess = 4040;

    //返回账户中心的回调代码
    public static final int backToUserCenterResult = 3030;
    //投资成功后的回调代码
    public static final int projectInvestSuccess = 2020;

    //修改登录密码成功后的回调
    public static final int modifyLoginSuccess = 1010;
    //
    public static final int registerSuccessCode = 1020;


    /////////////////////////MainActivity START///////////////

    //更新
    public static final String UPADTE_APK = URL_SUFFIX + "/rest/versionVT";
    //活动弹窗
    public static final String ACTIVITY_DIALOG = URL_SUFFIX + "/rest/scrollpic?way=4";
    //第一次运行提交设备信息
    public static final String DEVICE_INFORMATION = URL_SUFFIX + "/rest/firstStarting";
    ///////////////////////////判断是否读秒/////////////////////////
    public static final String IS_SECOND = URL_SUFFIX + "/rest/scrollpic?way=5";


    /////////////////////////MainActivity END/////////////////

    /////////////////////////首页 START////////////////////////

    //头部轮播图片、轮播文字、活动列表、底部图片
    public static final String HOME = URL_SUFFIX + "/rest/homePage";
    //标的数据
    public static final String HOME_MARK = URL_SUFFIX + "/rest/indexBorrorVT";
    //轮播文字
    public static final String HOME_TIP = URL_SUFFIX + "/rest/articleList/app_site_notice";
    //活动中心
    public static final String ACTIVITY_CENTER = URL_SUFFIX + "/rest/activity";
    //安全保障
    public static final String SAFETY_H5 = "https://m.hzjcb.com/html/safe/safe.html";
    //关于我们（底部图）
    public static final String ABOUT_US = "https://m.hzjcb.com/html/aboutus/aboutus.html";
    //是否设置交易密码
    public static final String IS_DEAL_PASSWORD = URL_SUFFIX + "/rest/ajaxIndexVT";
    //是否绑定银行卡
    public static final String IS_BANK = URL_SUFFIX + "/rest/user";

    /////////////////////////首页 END////////////////////////


    //////////////////////// 理财 START ////////////////////

    //理财列表
    public static final String MANAGE = URL_SUFFIX + "/rest/borrowVT";
    //往期产品
    public static final String PAST_PRODUCT = URL_SUFFIX + "/rest/borrowMore";
    //标的详情
    public static final String MARK_DETAIL = URL_SUFFIX + "/rest/borrow";
    //土豪奖、收官奖
    public static final String TH_LAST_PRIZE = URL_SUFFIX + "/rest/reward";
    //项目详情
    public static final String PROJECT_DETAIL = URL_SUFFIX + "/rest/repaymentInfo";
    //投资记录
    public static final String INVESTMENT_RECORD = URL_SUFFIX + "/rest/borrowTenderList";
    //投资福利（h5）
    public static final String INVEST_WELFARE = "https://m.hzjcb.com/html/investwelfare/investwelfare.html";
    //新手标判断是不是投资过
    public static final String HAS_INVEST = URL_SUFFIX + "/rest/userCheckTenderH";

    //////////////////////// 理财 END ////////////////////


}
