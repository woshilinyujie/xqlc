package com.rongxun.xqlc.Beans.userInfo;

import java.util.List;

/**
 * Created by linyujie on 17/1/14.
 */

public class RepaBeen {

    /**
     * timeLimit : 20
     * createTime : 2017-08-10 16:57:53
     * borrowStatus : 1
     * apr : 14.4
     * borrowStatusVal : 投资中
     * borrowName : 普通标
     * interest : 3.60
     * account : 500
     * userRepDetailList : [{"waitAccount":"100","repaymentDate":"2017-08-28","waitInterest":"0.72"},{"waitAccount":"100","repaymentDate":"2017-08-28","waitInterest":"0.72"},{"waitAccount":"500","repaymentDate":"2017-08-28","waitInterest":"3.60"}]
     * rcd : R0001
     * rmg :
     * hongbaoAmount : 0
     */

    private String timeLimit;
    private String createTime;
    private int borrowStatus;
    private double apr;
    private String borrowStatusVal;
    private String borrowName;
    private String interest;
    private String account;
    private String rcd;
    private String rmg;
    private String hongbaoAmount;
    private String repayTime;
    private String couponInterest;
    private String isNewBor;
    private String repayTimeShow;
    public void setRepayTimeShow(String repayTimeShow){
        this.repayTimeShow=repayTimeShow;
    }
    public String getRepayTimeShow(){
        return repayTimeShow;
    }


    public void setIsNewBor(String isNewBor){
        this.isNewBor=isNewBor;
    }

    public String getIsNewBor(){
        return isNewBor;
    }

    public void setCouponInterest(String couponInterest){
        this.couponInterest=couponInterest;
    }

    public String getCouponInterest(){
        return couponInterest;
    }
    private String loanAgreementUrl;
    private String userRepDetailStringX;
    private String jumpStatus;
    private String userRepDetailString;

    public void setUserRepDetailString(String userRepDetailString){
        this.userRepDetailString=userRepDetailString;
    }

    public String getUserRepDetailString(){
        return userRepDetailString;
    }

    public void setJumpStatus(String jumpStatus){
        this.jumpStatus=jumpStatus;

    }
    public String getJumpStatus(){
        return jumpStatus;
    }

    public String getUserRepDetailStringX(){
        return userRepDetailStringX;
    }

    public void setUserRepDetailStringX(String userRepDetailStringX){
        this.userRepDetailStringX=userRepDetailStringX;
    }
    public String getLoanAgreementUrl() {
        return loanAgreementUrl;
    }

    public void setLoanAgreementUrl(String loanAgreementUrl) {
        this.loanAgreementUrl = loanAgreementUrl;
    }


    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    private List<UserRepDetailListBean> userRepDetailList;

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(int borrowStatus) {
        this.borrowStatus = borrowStatus;
    }

    public double getApr() {
        return apr;
    }

    public void setApr(double apr) {
        this.apr = apr;
    }

    public String getBorrowStatusVal() {
        return borrowStatusVal;
    }

    public void setBorrowStatusVal(String borrowStatusVal) {
        this.borrowStatusVal = borrowStatusVal;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
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

    public String getRmg() {
        return rmg;
    }

    public void setRmg(String rmg) {
        this.rmg = rmg;
    }

    public String getHongbaoAmount() {
        return hongbaoAmount;
    }

    public void setHongbaoAmount(String hongbaoAmount) {
        this.hongbaoAmount = hongbaoAmount;
    }

    public List<UserRepDetailListBean> getUserRepDetailList() {
        return userRepDetailList;
    }

    public void setUserRepDetailList(List<UserRepDetailListBean> userRepDetailList) {
        this.userRepDetailList = userRepDetailList;
    }

    public static class UserRepDetailListBean {
        /**
         * waitAccount : 100
         * repaymentDate : 2017-08-28
         * waitInterest : 0.72
         */

        private String waitAccount;
        private String repaymentDate;
        private String waitInterest;
        private String waitCouponInterest;

        public String getWaitCouponInterest(){
            return waitCouponInterest;
        }

        public void setWaitCouponInterest(String waitCouponInterest){
            this.waitCouponInterest=waitCouponInterest;
        }

        public String getWaitAccount() {
            return waitAccount;
        }

        public void setWaitAccount(String waitAccount) {
            this.waitAccount = waitAccount;
        }

        public String getRepaymentDate() {
            return repaymentDate;
        }

        public void setRepaymentDate(String repaymentDate) {
            this.repaymentDate = repaymentDate;
        }

        public String getWaitInterest() {
            return waitInterest;
        }

        public void setWaitInterest(String waitInterest) {
            this.waitInterest = waitInterest;
        }
    }
}
