package com.rongxun.xqlc.Beans;

/**
 * Created by jcb on 2017/8/10.
 */

public class InverstBeen {

    /**
     * earnings : 0.72
     * time : 1503907433000
     * borrowName : 普通标
     * account : 100
     * rcd : R0001
     * msg : 投资成功
     */

    private String earnings;
    private long time;
    private String borrowName;
    private String account;
    private String rcd;
    private String msg;
    private String repayDateString;

    public String getRepayDateString(){
        return repayDateString;
    }

    public void setRepayDateString(String repayDateString){
        this.repayDateString=repayDateString;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRcd() {
        return rcd;
    }

    public void setRcd(String rcd) {
        this.rcd = rcd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
