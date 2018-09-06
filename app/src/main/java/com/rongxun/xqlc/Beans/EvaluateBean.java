package com.rongxun.xqlc.Beans;

/**
 * Created by jcb on 2018/4/11.
 */

public class EvaluateBean {

    /**
     * last : 未评估
     * appraisalURL : https://jwlcm.guaikatech.com/riskAssessment/index.html
     * rcd : R0001
     * rmg : 获取成功
     */

    private String last;
    private String appraisalURL;
    private String rcd;
    private String rmg;

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getAppraisalURL() {
        return appraisalURL;
    }

    public void setAppraisalURL(String appraisalURL) {
        this.appraisalURL = appraisalURL;
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
}
