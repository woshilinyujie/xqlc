package com.rongxun.xqlc.Beans;

import java.util.List;

/**
 * Created by oguig on 2017/8/10.
 */

public class InvestmentStatementsBean {

    /**
     * rcd : R0001
     * rmg : null
     * userTenderList : [{"apr":"0.123","borrowAccount":"1000253","borrowId":1230099,"borrowName":"共享活动标","borrowStatus":0,"borrowStatusShow":"还款中","createDate":1500351701869,"interest":"0.18","isNewbor":0,"tenderAccount":"1000","tenderid":111}]
     * pageBean : {"pageNumber":1,"totalCount":10,"pageCount":10,"pageSize":10}
     */

    private String rcd;
    private Object rmg;
    private PageBeanBean pageBean;
    private List<UserTenderListBean> userTenderList;

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

    public PageBeanBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBeanBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<UserTenderListBean> getUserTenderList() {
        return userTenderList;
    }

    public void setUserTenderList(List<UserTenderListBean> userTenderList) {
        this.userTenderList = userTenderList;
    }

    public static class PageBeanBean {
        /**
         * pageNumber : 1
         * totalCount : 10
         * pageCount : 10
         * pageSize : 10
         */

        private int pageNumber;
        private int totalCount;
        private int pageCount;
        private int pageSize;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

    public static class UserTenderListBean {
        /**
         * apr : 0.123
         * borrowAccount : 1000253
         * borrowId : 1230099
         * borrowName : 共享活动标
         * borrowStatus : 0
         * borrowStatusShow : 还款中
         * createDate : 1500351701869
         * interest : 0.18
         * isNewbor : 0
         * tenderAccount : 1000
         * tenderid : 111
         */

        private String apr;
        private String borrowAccount;
        private int borrowId;
        private String borrowName;
        private int borrowStatus;
        private String borrowStatusShow;
        private long createDate;
        private String interest;
        private int isNewbor;
        private String tenderAccount;
        private int tenderid;

        public void setSumMoney(String sumMoney) {
            this.sumMoney = sumMoney;
        }

        private String finalRepayDateString;

        public String getSumMoney() {
            return sumMoney;
        }

        private String sumMoney;


        public void setFinalRepayDateString(String finalRepayDateString){
            this.finalRepayDateString=finalRepayDateString;
        }

        public String getFinalRepayDateString(){
            return finalRepayDateString;
        }
        public String getApr() {
            return apr;
        }

        public void setApr(String apr) {
            this.apr = apr;
        }

        public String getBorrowAccount() {
            return borrowAccount;
        }

        public void setBorrowAccount(String borrowAccount) {
            this.borrowAccount = borrowAccount;
        }

        public int getBorrowId() {
            return borrowId;
        }

        public void setBorrowId(int borrowId) {
            this.borrowId = borrowId;
        }

        public String getBorrowName() {
            return borrowName;
        }

        public void setBorrowName(String borrowName) {
            this.borrowName = borrowName;
        }

        public int getBorrowStatus() {
            return borrowStatus;
        }

        public void setBorrowStatus(int borrowStatus) {
            this.borrowStatus = borrowStatus;
        }

        public String getBorrowStatusShow() {
            return borrowStatusShow;
        }

        public void setBorrowStatusShow(String borrowStatusShow) {
            this.borrowStatusShow = borrowStatusShow;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public int getIsNewbor() {
            return isNewbor;
        }

        public void setIsNewbor(int isNewbor) {
            this.isNewbor = isNewbor;
        }

        public String getTenderAccount() {
            return tenderAccount;
        }

        public void setTenderAccount(String tenderAccount) {
            this.tenderAccount = tenderAccount;
        }

        public int getTenderid() {
            return tenderid;
        }

        public void setTenderid(int tenderid) {
            this.tenderid = tenderid;
        }
    }
}
