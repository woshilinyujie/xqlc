package com.rongxun.xqlc.Beans.home;

/**
 * Created by oguig on 2017/6/9.
 */

public class HomeMarkBean {


    /**
     * title : 融资项目数量
     * data : {"id":2927,"name":"新手标第一期","status":1,"type":"14","timeLimit":"10","apr":17.28,"schedule":"0","balance":"10000.0","isNewbor":1,"activityOne":"标签1","activityTwo":"标签2","cornerLable":2,"baseApr":8.64,"awardApr":8.64,"cornerLableVal":"http://192.168.0.149:8080/mobile_img/cornerLable/2.png","lowestAccount":"100","mostAccount":"","count":null,"account":"10000"}
     * rcd : R0001
     * rmg :
     * key : 基本专区
     */

    private String title;
    private DataBean data;
    private String rcd;
    private String rmg;
    private String key;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static class DataBean {
        /**
         * id : 2927
         * name : 新手标第一期
         * status : 1
         * type : 14
         * timeLimit : 10
         * apr : 17.28
         * schedule : 0
         * balance : 10000.0
         * isNewbor : 1
         * activityOne : 标签1
         * activityTwo : 标签2
         * cornerLable : 2
         * baseApr : 8.64
         * awardApr : 8.64
         * cornerLableVal : http://192.168.0.149:8080/mobile_img/cornerLable/2.png
         * lowestAccount : 100
         * mostAccount :
         * count : null
         * account : 10000
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
        private Object count;
        private String account;

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

        public Object getCount() {
            return count;
        }

        public void setCount(Object count) {
            this.count = count;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
