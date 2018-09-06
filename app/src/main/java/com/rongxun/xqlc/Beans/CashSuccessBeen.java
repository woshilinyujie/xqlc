package com.rongxun.xqlc.Beans;

/**
 * Created by jcb on 2017/8/14.
 */

public class CashSuccessBeen {

    /**
     * icon : http://120.27.160.157:82/img/bankIcon/icon_CCB.png
     * bankaccount : 6217001460006883200
     * bankname : 建设银行
     * txje : 100
     * rcd : R0001
     * sjdzje : 99.0
     * rmg : 请求成功
     * txsxf : 1
     */

    private String icon;
    private String bankaccount;
    private String bankname;
    private int txje;
    private String rcd;
    private double sjdzje;
    private String rmg;
    private int txsxf;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public int getTxje() {
        return txje;
    }

    public void setTxje(int txje) {
        this.txje = txje;
    }

    public String getRcd() {
        return rcd;
    }

    public void setRcd(String rcd) {
        this.rcd = rcd;
    }

    public double getSjdzje() {
        return sjdzje;
    }

    public void setSjdzje(double sjdzje) {
        this.sjdzje = sjdzje;
    }

    public String getRmg() {
        return rmg;
    }

    public void setRmg(String rmg) {
        this.rmg = rmg;
    }

    public int getTxsxf() {
        return txsxf;
    }

    public void setTxsxf(int txsxf) {
        this.txsxf = txsxf;
    }
}
