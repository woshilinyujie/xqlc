package com.rongxun.xqlc.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jcb on 2017/7/10.
 */

public class projectInitBean implements Serializable{


    /**
     * timeLimit : 109
     * apr : 14.4
     * mostAccount :
     * data : [{"id":1490263,"createDate":1491038835000,"modifyDate":1491038835000,"userId":193310,"hbNo":"2017040126027","name":"用户注册","money":8,"usedMoney":0,"status":0,"startTime":1491038835000,"endTime":1491321599000,"source":1,"sourceString":"用户注册","sourceUserId":193310,"sourceBorrowId":null,"usedBorrowId":null,"expDate":3,"limitStart":0,"limitEnd":360,"isPc":1,"isApp":1,"isHfive":1,"investFullMomey":800,"isLooked":0,"on":false,"overDays":0},{"id":1731985,"createDate":1508124870000,"modifyDate":1508124881000,"userId":193310,"hbNo":"2017101632869","name":"cs","money":400,"usedMoney":null,"status":0,"startTime":1508124881000,"endTime":1508255999000,"source":5,"sourceString":"cs","sourceUserId":null,"sourceBorrowId":null,"usedBorrowId":null,"expDate":1,"limitStart":1,"limitEnd":360,"isPc":1,"isApp":1,"isHfive":1,"investFullMomey":1000,"isLooked":0,"on":false,"overDays":0},{"id":1731986,"createDate":1508125239000,"modifyDate":1508125245000,"userId":193310,"hbNo":"2017101661499","name":"测试","money":400,"usedMoney":null,"status":0,"startTime":1508125245000,"endTime":1508255999000,"source":5,"sourceString":"cs","sourceUserId":null,"sourceBorrowId":null,"usedBorrowId":null,"expDate":1,"limitStart":1,"limitEnd":360,"isPc":1,"isApp":1,"isHfive":1,"investFullMomey":1000,"isLooked":0,"on":false,"overDays":0}]
     * baseApr : 7.2
     * lowestAccount : 100
     * isNewbor : 1
     * ableMoney : 0.00
     * dayApr : 4.0E-4
     * balance : 90000
     * couponList : [{"id":5,"userId":193310,"name":"普通加息券","apr":0.014,"status":0,"expDate":20,"timeLimitStart":0,"timeLimitEnd":60,"amountMin":0,"amountMax":1000,"startTime":1508754243000,"endTime":1512037444000,"sourceBorrowId":null,"usedBorrowId":null,"days":10,"isUse":null,"modifyDate":1512037444000,"overDays":0},{"id":8,"userId":193310,"name":"普通加息券","apr":0.1,"status":0,"expDate":50,"timeLimitStart":0,"timeLimitEnd":0,"amountMin":0,"amountMax":0,"startTime":1508754243000,"endTime":1512037444000,"sourceBorrowId":null,"usedBorrowId":null,"days":20,"isUse":null,"modifyDate":1512037444000,"overDays":0},{"id":12,"userId":193310,"name":"普通加息券","apr":0.014,"status":0,"expDate":30,"timeLimitStart":0,"timeLimitEnd":30,"amountMin":0,"amountMax":1000,"startTime":1508754243000,"endTime":1512037444000,"sourceBorrowId":null,"usedBorrowId":null,"days":0,"isUse":null,"modifyDate":1512037444000,"overDays":0},{"id":13,"userId":193310,"name":"普通加息券","apr":0.2,"status":0,"expDate":30,"timeLimitStart":10,"timeLimitEnd":100,"amountMin":1000,"amountMax":1000,"startTime":1508754243000,"endTime":1512037444000,"sourceBorrowId":null,"usedBorrowId":2863,"days":47,"isUse":null,"modifyDate":1512037444000,"overDays":0}]
     * borrowId : 2921
     * borrowName : 新手标签角标1
     * useHongbao : 0
     * awardApr : 7.2
     * rcd : R0001
     * rmg : 成功
     */

    private int timeLimit;
    private double apr;
    private String mostAccount;
    private double baseApr;
    private String lowestAccount;
    private int isNewbor;
    private String ableMoney;
    private double dayApr;
    private String balance;
    private int borrowId;
    private String borrowName;
    private int useHongbao;
    private double awardApr;
    private String rcd;
    private String rmg;
    private List<DataBean> data;
    private List<CouponListBean> couponList;
    private boolean isCoupon;

    public boolean getIsCoupon(){
        return isCoupon;
    }

    public void setIsCoupon(boolean isCoupon){
        this.isCoupon=isCoupon;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public double getApr() {
        return apr;
    }

    public void setApr(double apr) {
        this.apr = apr;
    }

    public String getMostAccount() {
        return mostAccount;
    }

    public void setMostAccount(String mostAccount) {
        this.mostAccount = mostAccount;
    }

    public double getBaseApr() {
        return baseApr;
    }

    public void setBaseApr(double baseApr) {
        this.baseApr = baseApr;
    }

    public String getLowestAccount() {
        return lowestAccount;
    }

    public void setLowestAccount(String lowestAccount) {
        this.lowestAccount = lowestAccount;
    }

    public int getIsNewbor() {
        return isNewbor;
    }

    public void setIsNewbor(int isNewbor) {
        this.isNewbor = isNewbor;
    }

    public String getAbleMoney() {
        return ableMoney;
    }

    public void setAbleMoney(String ableMoney) {
        this.ableMoney = ableMoney;
    }

    public double getDayApr() {
        return dayApr;
    }

    public void setDayApr(double dayApr) {
        this.dayApr = dayApr;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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

    public int getUseHongbao() {
        return useHongbao;
    }

    public void setUseHongbao(int useHongbao) {
        this.useHongbao = useHongbao;
    }

    public double getAwardApr() {
        return awardApr;
    }

    public void setAwardApr(double awardApr) {
        this.awardApr = awardApr;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<CouponListBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponListBean> couponList) {
        this.couponList = couponList;
    }

    public static class DataBean {
        /**
         * id : 1490263
         * createDate : 1491038835000
         * modifyDate : 1491038835000
         * userId : 193310
         * hbNo : 2017040126027
         * name : 用户注册
         * money : 8.0
         * usedMoney : 0.0
         * status : 0
         * startTime : 1491038835000
         * endTime : 1491321599000
         * source : 1
         * sourceString : 用户注册
         * sourceUserId : 193310
         * sourceBorrowId : null
         * usedBorrowId : null
         * expDate : 3
         * limitStart : 0
         * limitEnd : 360
         * isPc : 1
         * isApp : 1
         * isHfive : 1
         * investFullMomey : 800
         * isLooked : 0
         * on : false
         * overDays : 0
         */

        private int id;
        private long createDate;
        private long modifyDate;
        private int userId;
        private String hbNo;
        private String name;
        private double money;
        private double usedMoney;
        private int status;
        private long startTime;
        private long endTime;
        private int source;
        private String sourceString;
        private int sourceUserId;
        private Object sourceBorrowId;
        private Object usedBorrowId;
        private int expDate;
        private int limitStart;
        private int limitEnd;
        private int isPc;
        private int isApp;
        private int isHfive;
        private int investFullMomey;
        private int isLooked;
        private boolean on;
        private int overDays;
        public boolean isright=false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public long getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(long modifyDate) {
            this.modifyDate = modifyDate;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getHbNo() {
            return hbNo;
        }

        public void setHbNo(String hbNo) {
            this.hbNo = hbNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getUsedMoney() {
            return usedMoney;
        }

        public void setUsedMoney(double usedMoney) {
            this.usedMoney = usedMoney;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public String getSourceString() {
            return sourceString;
        }

        public void setSourceString(String sourceString) {
            this.sourceString = sourceString;
        }

        public int getSourceUserId() {
            return sourceUserId;
        }

        public void setSourceUserId(int sourceUserId) {
            this.sourceUserId = sourceUserId;
        }

        public Object getSourceBorrowId() {
            return sourceBorrowId;
        }

        public void setSourceBorrowId(Object sourceBorrowId) {
            this.sourceBorrowId = sourceBorrowId;
        }

        public Object getUsedBorrowId() {
            return usedBorrowId;
        }

        public void setUsedBorrowId(Object usedBorrowId) {
            this.usedBorrowId = usedBorrowId;
        }

        public int getExpDate() {
            return expDate;
        }

        public void setExpDate(int expDate) {
            this.expDate = expDate;
        }

        public int getLimitStart() {
            return limitStart;
        }

        public void setLimitStart(int limitStart) {
            this.limitStart = limitStart;
        }

        public int getLimitEnd() {
            return limitEnd;
        }

        public void setLimitEnd(int limitEnd) {
            this.limitEnd = limitEnd;
        }

        public int getIsPc() {
            return isPc;
        }

        public void setIsPc(int isPc) {
            this.isPc = isPc;
        }

        public int getIsApp() {
            return isApp;
        }

        public void setIsApp(int isApp) {
            this.isApp = isApp;
        }

        public int getIsHfive() {
            return isHfive;
        }

        public void setIsHfive(int isHfive) {
            this.isHfive = isHfive;
        }

        public int getInvestFullMomey() {
            return investFullMomey;
        }

        public void setInvestFullMomey(int investFullMomey) {
            this.investFullMomey = investFullMomey;
        }

        public int getIsLooked() {
            return isLooked;
        }

        public void setIsLooked(int isLooked) {
            this.isLooked = isLooked;
        }

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public int getOverDays() {
            return overDays;
        }

        public void setOverDays(int overDays) {
            this.overDays = overDays;
        }
    }

    public static class CouponListBean {
        /**
         * id : 5
         * userId : 193310
         * name : 普通加息券
         * apr : 0.014
         * status : 0
         * expDate : 20
         * timeLimitStart : 0
         * timeLimitEnd : 60
         * amountMin : 0
         * amountMax : 1000
         * startTime : 1508754243000
         * endTime : 1512037444000
         * sourceBorrowId : null
         * usedBorrowId : null
         * days : 10
         * isUse : null
         * modifyDate : 1512037444000
         * overDays : 0
         */

        private int id;
        private int userId;
        private String name;
        private double apr;
        private int status;
        private int expDate;
        private int timeLimitStart;
        private int timeLimitEnd;
        private int amountMin;
        private int amountMax;
        private long startTime;
        private long endTime;
        private Object sourceBorrowId;
        private Object usedBorrowId;
        private int days;
        private Object isUse;
        private long modifyDate;
        private int overDays;
        public boolean isRight=false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

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

        public int getExpDate() {
            return expDate;
        }

        public void setExpDate(int expDate) {
            this.expDate = expDate;
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

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public Object getSourceBorrowId() {
            return sourceBorrowId;
        }

        public void setSourceBorrowId(Object sourceBorrowId) {
            this.sourceBorrowId = sourceBorrowId;
        }

        public Object getUsedBorrowId() {
            return usedBorrowId;
        }

        public void setUsedBorrowId(Object usedBorrowId) {
            this.usedBorrowId = usedBorrowId;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public Object getIsUse() {
            return isUse;
        }

        public void setIsUse(Object isUse) {
            this.isUse = isUse;
        }

        public long getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(long modifyDate) {
            this.modifyDate = modifyDate;
        }

        public int getOverDays() {
            return overDays;
        }

        public void setOverDays(int overDays) {
            this.overDays = overDays;
        }
    }
}
