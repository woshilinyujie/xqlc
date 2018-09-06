package com.rongxun.xqlc.Beans.home;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.bean
 * Author: HouShengLi
 * Time: 2017/06/12 11:06
 * E-mail: 13967189624@163.com
 * Description:
 */

public class UpdateBean {


    /**
     * update : 0
     * enabled : true
     * description : 本次更新说明
     1、修复充值时无法输入小数点问题

     2、首页新增网站公告轮播及在线客服功能

     3、充值模块新增限额提醒，提现模块新增“全部提现”功能

     4、“我的”模块页面重新设计，展示更多用户数据
     * versionCode : 1
     * versionName : 1.0
     * rcd : R0001
     * rmg : 响应成功
     * url : http://dl.hzjcb.com/2.2.1_original.apk
     */

    private String update;
    private boolean enabled;
    private String description;
    private int versionCode;
    private String versionName;
    private String rcd;
    private String rmg;
    private String url;

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getRcd() {
        return rcd;
    }

    public void setRcd(String rcd) {
        this.rcd = rcd;
    }

    public String getRmg() {
        return rmg;
    }

    public void setRmg(String rmg) {
        this.rmg = rmg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
