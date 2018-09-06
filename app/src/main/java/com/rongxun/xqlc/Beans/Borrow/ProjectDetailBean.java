package com.rongxun.xqlc.Beans.Borrow;

import java.util.List;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Beans.Borrow
 * Author: HouShengLi
 * Time: 2017/07/17 14:33
 * E-mail: 13967189624@163.com
 * Description:
 */

public class ProjectDetailBean {


    /**
     * rcd : R0001
     * rmg : null
     * debtMess : 杭州****有限公司 注册号：9133*************** 地址：杭州市滨江区****************** 借款期限：30天
     * content : 我公司正式进入电子产品消费金融领域，为本地大型电子产品经销商提供供应链融资服务。据我公司调查，该企业经营状况良好，账户流水正常，其年销售额已达融资标准。以其应收账款及其库存质押， 给予一定比例的融资。
     * realName : 胡文超
     * useReason : 增加企业流动资金，缓解库存压力。
     * paymentSource : ①企业以不低于870万的应收账款作为质押，以不低于800万元的库存作为质押。 ② 我公司派专人监管企业账户及仓库，风险可控。
     * orderNum : 0
     * interest : 2430.00
     * capital : 300000.00
     * account : 302430.00
     * repaymentDateInt : null
     * isdayString : 天
     * debtorInfoArr : ["杭州****有限公司","注册号：9133***************","地址：杭州市滨江区******************","借款期限：30天"]
     * contentArr : ["我公司正式进入电子产品消费金融领域，为本地大型电子产品经销商提供供应链融资服务。据我公司调查，该企业经营状况良好，账户流水正常，其年销售额已达融资标准。以其应收账款及其库存质押， 给予一定比例的融资。"]
     * useReasonArr : ["增加企业流动资金，缓解库存压力。"]
     * paymentSourceArr : ["①企业以不低于870万的应收账款作为质押，以不低于800万元的库存作为质押。 ② 我公司派专人监管企业账户及仓库，风险可控。"]
     * borImage : [{"name":"合同一","url":"/data/upfiles/images/201703/8cdf76e0c3714ba98c130cb5e9f9b74a.jpg"},{"name":"合同二","url":"/data/upfiles/images/201703/d4256ebfbe1e44c49b37dad5d8ccb911.jpg"},{"name":"合同三","url":"/data/upfiles/images/201703/cd63d4f886db42fb8d4db286d866cb17.jpg"},{"name":"购销合同一","url":"/data/upfiles/images/201703/621921e84e084cd4888a321c8e671366.jpg"},{"name":"购销合同二","url":"/data/upfiles/images/201703/e02dd180d7074486861cb42ef153db6f.jpg"},{"name":"购销合同三","url":"/data/upfiles/images/201703/5914c625e87940dd899b61956f3e500e.jpg"},{"name":"营业执照","url":"/data/upfiles/images/201703/a9c9712029df405d98f4ea8c339ddad6.jpg"}]
     * repaymentDetailList : [{"orderNum":1,"interest":"2430.00","capital":"300000.00","account":"302430.00","repaymentDateInt":30}]
     */

    private String rcd;
    private Object rmg;
    private String debtMess;
    private String content;
    private String realName;
    private String useReason;
    private String paymentSource;
    private int orderNum;
    private String interest;
    private String capital;
    private String account;
    private Object repaymentDateInt;
    private String isdayString;
    private String borImage;
    private List<String> debtorInfoArr;
    private List<String> contentArr;
    private List<String> useReasonArr;
    private List<String> paymentSourceArr;
    private List<RepaymentDetailListBean> repaymentDetailList;

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

    public String getDebtMess() {
        return debtMess;
    }

    public void setDebtMess(String debtMess) {
        this.debtMess = debtMess;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUseReason() {
        return useReason;
    }

    public void setUseReason(String useReason) {
        this.useReason = useReason;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Object getRepaymentDateInt() {
        return repaymentDateInt;
    }

    public void setRepaymentDateInt(Object repaymentDateInt) {
        this.repaymentDateInt = repaymentDateInt;
    }

    public String getIsdayString() {
        return isdayString;
    }

    public void setIsdayString(String isdayString) {
        this.isdayString = isdayString;
    }

    public String getBorImage() {
        return borImage;
    }

    public void setBorImage(String borImage) {
        this.borImage = borImage;
    }

    public List<String> getDebtorInfoArr() {
        return debtorInfoArr;
    }

    public void setDebtorInfoArr(List<String> debtorInfoArr) {
        this.debtorInfoArr = debtorInfoArr;
    }

    public List<String> getContentArr() {
        return contentArr;
    }

    public void setContentArr(List<String> contentArr) {
        this.contentArr = contentArr;
    }

    public List<String> getUseReasonArr() {
        return useReasonArr;
    }

    public void setUseReasonArr(List<String> useReasonArr) {
        this.useReasonArr = useReasonArr;
    }

    public List<String> getPaymentSourceArr() {
        return paymentSourceArr;
    }

    public void setPaymentSourceArr(List<String> paymentSourceArr) {
        this.paymentSourceArr = paymentSourceArr;
    }

    public List<RepaymentDetailListBean> getRepaymentDetailList() {
        return repaymentDetailList;
    }

    public void setRepaymentDetailList(List<RepaymentDetailListBean> repaymentDetailList) {
        this.repaymentDetailList = repaymentDetailList;
    }

    public static class RepaymentDetailListBean {
        /**
         * orderNum : 1
         * interest : 2430.00
         * capital : 300000.00
         * account : 302430.00
         * repaymentDateInt : 30
         */

        private int orderNum;
        private String interest;
        private String capital;
        private String account;
        private int repaymentDateInt;

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getRepaymentDateInt() {
            return repaymentDateInt;
        }

        public void setRepaymentDateInt(int repaymentDateInt) {
            this.repaymentDateInt = repaymentDateInt;
        }
    }
}
