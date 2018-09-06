package com.rongxun.xqlc.Beans;

import java.util.List;

/**
 * Created by jcb on 2017/11/14.
 */

public class UserJIaxiBean {

    /**
     * rcd : R0001
     * rmg : null
     * cpNum : 28
     * userCouponViews : [{"name":"普通加息券","apr":0.014,"status":0,"timeLimitStart":0,"timeLimitEnd":60,"amountMin":0,"amountMax":1000,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":10},{"name":"普通加息券","apr":0.014,"status":0,"timeLimitStart":60,"timeLimitEnd":0,"amountMin":1000,"amountMax":0,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":10},{"name":"普通加息券","apr":0.014,"status":0,"timeLimitStart":30,"timeLimitEnd":90,"amountMin":10000,"amountMax":20000,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":0},{"name":"普通加息券","apr":0.1,"status":0,"timeLimitStart":0,"timeLimitEnd":0,"amountMin":0,"amountMax":0,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":20},{"name":"普通加息券","apr":0.04,"status":0,"timeLimitStart":15,"timeLimitEnd":20,"amountMin":0,"amountMax":0,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":0},{"name":"普通加息券","apr":0.014,"status":0,"timeLimitStart":15,"timeLimitEnd":20,"amountMin":15,"amountMax":20,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":0},{"name":"普通加息券","apr":0.03,"status":0,"timeLimitStart":20,"timeLimitEnd":50,"amountMin":100,"amountMax":150,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":0},{"name":"普通加息券","apr":0.014,"status":0,"timeLimitStart":0,"timeLimitEnd":30,"amountMin":0,"amountMax":1000,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":0},{"name":"普通加息券","apr":0.2,"status":0,"timeLimitStart":10,"timeLimitEnd":100,"amountMin":1000,"amountMax":1000,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":47},{"name":"普通加息券","apr":0.014,"status":0,"timeLimitStart":60,"timeLimitEnd":0,"amountMin":1000,"amountMax":0,"endTime":1512037444000,"endTimeStr":"2017年11月30日","days":10}]
     * pageBean : {"pageNumber":1,"totalCount":13,"pageCount":2,"pageSize":10}
     * expiredNum : 2
     * useNum : 6
     * hbNum : 8
     */

    private String rcd;
    private Object rmg;
    private String cpNum;
    private PageBeanBean pageBean;
    private String expiredNum;
    private String useNum;
    private String hbNum;
    private List<UserCouponViewsBean> userCouponViews;

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

    public String getCpNum() {
        return cpNum;
    }

    public void setCpNum(String cpNum) {
        this.cpNum = cpNum;
    }

    public PageBeanBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBeanBean pageBean) {
        this.pageBean = pageBean;
    }

    public String getExpiredNum() {
        return expiredNum;
    }

    public void setExpiredNum(String expiredNum) {
        this.expiredNum = expiredNum;
    }

    public String getUseNum() {
        return useNum;
    }

    public void setUseNum(String useNum) {
        this.useNum = useNum;
    }

    public String getHbNum() {
        return hbNum;
    }

    public void setHbNum(String hbNum) {
        this.hbNum = hbNum;
    }

    public List<UserCouponViewsBean> getUserCouponViews() {
        return userCouponViews;
    }

    public void setUserCouponViews(List<UserCouponViewsBean> userCouponViews) {
        this.userCouponViews = userCouponViews;
    }

    public static class PageBeanBean {
        /**
         * pageNumber : 1
         * totalCount : 13
         * pageCount : 2
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

    public static class UserCouponViewsBean {
        /**
         * name : 普通加息券
         * apr : 0.014
         * status : 0
         * timeLimitStart : 0
         * timeLimitEnd : 60
         * amountMin : 0
         * amountMax : 1000
         * endTime : 1512037444000
         * endTimeStr : 2017年11月30日
         * days : 10
         */

        private String name;
        private double apr;
        private int status;
        private int timeLimitStart;
        private int timeLimitEnd;
        private int amountMin;
        private int amountMax;
        private long endTime;
        private String endTimeStr;
        private int days;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getApr() {
            return apr;
        }

        public void setApr(double apr) {
            this.apr = apr;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTimeLimitStart() {
            return timeLimitStart;
        }

        public void setTimeLimitStart(int timeLimitStart) {
            this.timeLimitStart = timeLimitStart;
        }

        public int getTimeLimitEnd() {
            return timeLimitEnd;
        }

        public void setTimeLimitEnd(int timeLimitEnd) {
            this.timeLimitEnd = timeLimitEnd;
        }

        public int getAmountMin() {
            return amountMin;
        }

        public void setAmountMin(int amountMin) {
            this.amountMin = amountMin;
        }

        public int getAmountMax() {
            return amountMax;
        }

        public void setAmountMax(int amountMax) {
            this.amountMax = amountMax;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getEndTimeStr() {
            return endTimeStr;
        }

        public void setEndTimeStr(String endTimeStr) {
            this.endTimeStr = endTimeStr;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }
    }
}