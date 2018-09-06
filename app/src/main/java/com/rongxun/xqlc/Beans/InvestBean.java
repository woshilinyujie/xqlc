package com.rongxun.xqlc.Beans;

/**
 * Created by jcb on 2017/5/24.
 */

public class InvestBean {
    private String rcd;// 返回编码 return code
    private String rmg;// 返回信息 return message
    private String time;
    private String repayDateString;

    public void setRepayDateString(String repayDateString){
        this.repayDateString=repayDateString;
    }

    public String getRepayDateString(){
        return repayDateString;
    }

    public void setTime(String time){
        this.time=time;
    }

    public String getTime(){
        return time;
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
