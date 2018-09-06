package com.rongxun.xqlc.Beans.Borrow;

import java.util.List;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.bean
 * Author: HouShengLi
 * Time: 2017/06/08 17:39
 * E-mail: 13967189624@163.com
 * Description:
 */

public class LastPrizeBean {


    /**
     * rcd : R0001
     * rmg : null
     * hadInvested : true
     * separator : #
     * tuhaoStr : #用户在该项目的单笔最大投资额高达#元，他将有机会获得#元土豪红包。超过他，红包就是您的。
     * tuhaoVals : ["136*****430","200.0","10"]
     * shouguanStr : 最后一位投资用户有机会获得最高#元收官大哥红包，项目剩余可投金额#元，赶快来投资。
     * shouguanVals : ["30","9800"]
     */

    private String rcd;
    private Object rmg;
    private boolean hadInvested;
    private String separator;
    private String tuhaoStr;
    private String shouguanStr;
    private List<String> tuhaoVals;
    private List<String> shouguanVals;

    public String getRcd() {
        return rcd;
    }

    public void setRcd(String rcd) {
        this.rcd = rcd;
    }

    public Object getRmg() {
        return rmg;
    }

    public void setRmg(Object rmg) {
        this.rmg = rmg;
    }

    public boolean isHadInvested() {
        return hadInvested;
    }

    public void setHadInvested(boolean hadInvested) {
        this.hadInvested = hadInvested;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getTuhaoStr() {
        return tuhaoStr;
    }

    public void setTuhaoStr(String tuhaoStr) {
        this.tuhaoStr = tuhaoStr;
    }

    public String getShouguanStr() {
        return shouguanStr;
    }

    public void setShouguanStr(String shouguanStr) {
        this.shouguanStr = shouguanStr;
    }

    public List<String> getTuhaoVals() {
        return tuhaoVals;
    }

    public void setTuhaoVals(List<String> tuhaoVals) {
        this.tuhaoVals = tuhaoVals;
    }

    public List<String> getShouguanVals() {
        return shouguanVals;
    }

    public void setShouguanVals(List<String> shouguanVals) {
        this.shouguanVals = shouguanVals;
    }
}
