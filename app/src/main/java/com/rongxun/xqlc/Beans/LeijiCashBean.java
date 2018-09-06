package com.rongxun.xqlc.Beans;

/**
 * Created by oguig on 2017/8/16.
 */

public class LeijiCashBean {

    /**
     * sumJLmoney : 0.00
     * sumhbMoneyUse : 10.00
     * summoney : 10.0
     * sumHYSY : 0.00
     * sumInterest : 0.00
     * rcd : R0001
     * rmg :
     */

    private String sumJLmoney;
    private String sumhbMoneyUse;
    private double summoney;
    private String sumHYSY;
    private String sumInterest;
    private String rcd;
    private String rmg;
    private String sumJXMoney;


    public void setSumJXMoney(String sumJXMoney){
        this.sumJXMoney=sumJXMoney;
    }

    public String getSumJXMoney(){
        return sumJXMoney;
    }

    public String getSumJLmoney() {
        return sumJLmoney;
    }

    public void setSumJLmoney(String sumJLmoney) {
        this.sumJLmoney = sumJLmoney;
    }

    public String getSumhbMoneyUse() {
        return sumhbMoneyUse;
    }

    public void setSumhbMoneyUse(String sumhbMoneyUse) {
        this.sumhbMoneyUse = sumhbMoneyUse;
    }

    public double getSummoney() {
        return summoney;
    }

    public void setSummoney(double summoney) {
        this.summoney = summoney;
    }

    public String getSumHYSY() {
        return sumHYSY;
    }

    public void setSumHYSY(String sumHYSY) {
        this.sumHYSY = sumHYSY;
    }

    public String getSumInterest() {
        return sumInterest;
    }

    public void setSumInterest(String sumInterest) {
        this.sumInterest = sumInterest;
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
