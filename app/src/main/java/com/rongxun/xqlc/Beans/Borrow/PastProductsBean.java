package com.rongxun.xqlc.Beans.Borrow;

import java.util.List;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.bean
 * Author: HouShengLi
 * Time: 2017/06/07 09:49
 * E-mail: 13967189624@163.com
 * Description:
 */

public class PastProductsBean {


    /**
     * rcd : R0001
     * rmg : null
     * borrowItemList : null
     * borrowDTOList : [{"id":17,"name":"zdl测试项目01","status":3,"type":"14","timeLimit":"20","apr":21.6,"schedule":"8","balance":"9200","isNewbor":1,"activityOne":null,"activityTwo":null,"cornerLable":4,"baseApr":3.6,"awardApr":18,"cornerLableVal":"http://116.62.239.119:800 /mobile_img/cornerLable/4.png","lowestAccount":"100","mostAccount":""},{"id":18,"name":"zdl测试新手项目02","status":3,"type":"16","timeLimit":"20","apr":7.2,"schedule":"1","balance":"9900","isNewbor":1,"activityOne":"加息","activityTwo":null,"cornerLable":1,"baseApr":3.6,"awardApr":3.6,"cornerLableVal":"http://116.62.239.119:800 /mobile_img/cornerLable/1.png","lowestAccount":"100","mostAccount":""}]
     * pageBean : {"pageNumber":1,"totalCount":2,"pageCount":1,"pageSize":10}
     */

    private String rcd;
    private String rmg;
    private String borrowItemList;
    private PageBeanBean pageBean;
    private List<BorrowDTOListBean> borrowDTOList;

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

    public String getBorrowItemList() {
        return borrowItemList;
    }

    public void setBorrowItemList(String borrowItemList) {
        this.borrowItemList = borrowItemList;
    }

    public PageBeanBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBeanBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<BorrowDTOListBean> getBorrowDTOList() {
        return borrowDTOList;
    }

    public void setBorrowDTOList(List<BorrowDTOListBean> borrowDTOList) {
        this.borrowDTOList = borrowDTOList;
    }

    public static class PageBeanBean {
        /**
         * pageNumber : 1
         * totalCount : 2
         * pageCount : 1
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

    public static class BorrowDTOListBean {
        /**
         * id : 17
         * name : zdl测试项目01
         * status : 3
         * type : 14
         * timeLimit : 20
         * apr : 21.6
         * schedule : 8
         * balance : 9200
         * isNewbor : 1
         * activityOne : null
         * activityTwo : null
         * cornerLable : 4
         * baseApr : 3.6
         * awardApr : 18
         * cornerLableVal : http://116.62.239.119:800 /mobile_img/cornerLable/4.png
         * lowestAccount : 100
         * mostAccount :
         * count:3
         */

        private int id;
        private String name;
        private int status;
        private String type;
        private String timeLimit;
        private double apr;
        private String schedule;
        private String balance;
        private int isNewbor;
        private String activityOne;
        private String activityTwo;
        private int cornerLable;
        private double baseApr;
        private double awardApr;
        private String cornerLableVal;
        private String lowestAccount;
        private String mostAccount;
        private int count;
        private String more;
        private int useHongbao;

        public void setUseHongbao(int useHongbao){
            this.useHongbao=useHongbao;
        }
        public int getUseHongbao(){
            return useHongbao;
        }

        public String getMore() {
            return more;
        }

        public void setMore(String more) {
            this.more = more;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(String timeLimit) {
            this.timeLimit = timeLimit;
        }

        public double getApr() {
            return apr;
        }

        public void setApr(double apr) {
            this.apr = apr;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public int getIsNewbor() {
            return isNewbor;
        }

        public void setIsNewbor(int isNewbor) {
            this.isNewbor = isNewbor;
        }

        public String getActivityOne() {
            return activityOne;
        }

        public void setActivityOne(String activityOne) {
            this.activityOne = activityOne;
        }

        public String getActivityTwo() {
            return activityTwo;
        }

        public void setActivityTwo(String activityTwo) {
            this.activityTwo = activityTwo;
        }

        public int getCornerLable() {
            return cornerLable;
        }

        public void setCornerLable(int cornerLable) {
            this.cornerLable = cornerLable;
        }

        public double getBaseApr() {
            return baseApr;
        }

        public void setBaseApr(double baseApr) {
            this.baseApr = baseApr;
        }

        public double getAwardApr() {
            return awardApr;
        }

        public void setAwardApr(double awardApr) {
            this.awardApr = awardApr;
        }

        public String getCornerLableVal() {
            return cornerLableVal;
        }

        public void setCornerLableVal(String cornerLableVal) {
            this.cornerLableVal = cornerLableVal;
        }

        public String getLowestAccount() {
            return lowestAccount;
        }

        public void setLowestAccount(String lowestAccount) {
            this.lowestAccount = lowestAccount;
        }

        public String getMostAccount() {
            return mostAccount;
        }

        public void setMostAccount(String mostAccount) {
            this.mostAccount = mostAccount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

    }
}
