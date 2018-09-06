package com.rongxun.xqlc.Beans.Borrow;

import java.util.List;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.bean
 * Author: HouShengLi
 * Time: 2017/06/06 17:44
 * E-mail: 13967189624@163.com
 * Description:
 */

public class ManageOriginBean {


    private DataBeanX data;
    private String rcd;
    private String rmg;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
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

    public static class DataBeanX {


        private LcImgBean lcImg;
        private LctcImgBean lctcImg;
        private List<InvestsBean> invests;
        private List<BorrowsBean> borrows;

        public LcImgBean getLcImg() {
            return lcImg;
        }

        public void setLcImg(LcImgBean lcImg) {
            this.lcImg = lcImg;
        }

        public LctcImgBean getLctcImg() {
            return lctcImg;
        }

        public void setLctcImg(LctcImgBean lctcImg) {
            this.lctcImg = lctcImg;
        }

        public List<InvestsBean> getInvests() {
            return invests;
        }

        public void setInvests(List<InvestsBean> invests) {
            this.invests = invests;
        }

        public List<BorrowsBean> getBorrows() {
            return borrows;
        }

        public void setBorrows(List<BorrowsBean> borrows) {
            this.borrows = borrows;
        }

        public static class LcImgBean {
            /**
             * img : /data/upfiles/images/201703/1105b09b721747bdbdd0c396476d0826.jpg
             * url : https://www.hzjcb.com/article/content/728.htm
             */

            private String img;
            private String url;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class LctcImgBean {
            /**
             * img : /data/upfiles/images/201707/bd46000d768a41d0ac76d8d642d2f6e8.png
             * url : www.baidu.com
             */

            private String img;
            private String url;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class InvestsBean {
            /**
             * id : 2
             * tenderId : 1
             * username : 136****5239
             * createDate : 1461894552000
             * money : 10000.0
             * account : 10000.0
             * autoTenderStatus : 0
             * status : 1
             * arp : null
             * loginStatus : null
             * ableMoney : 0.00
             * clientType : 0
             * createDateStr : 1个月前
             */

            private int id;
            private int tenderId;
            private String username;
            private long createDate;
            private String money;
            private String account;
            private String autoTenderStatus;
            private String status;
            private Object arp;
            private Object loginStatus;
            private String ableMoney;
            private int clientType;
            private String createDateStr;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getTenderId() {
                return tenderId;
            }

            public void setTenderId(int tenderId) {
                this.tenderId = tenderId;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getAutoTenderStatus() {
                return autoTenderStatus;
            }

            public void setAutoTenderStatus(String autoTenderStatus) {
                this.autoTenderStatus = autoTenderStatus;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Object getArp() {
                return arp;
            }

            public void setArp(Object arp) {
                this.arp = arp;
            }

            public Object getLoginStatus() {
                return loginStatus;
            }

            public void setLoginStatus(Object loginStatus) {
                this.loginStatus = loginStatus;
            }

            public String getAbleMoney() {
                return ableMoney;
            }

            public void setAbleMoney(String ableMoney) {
                this.ableMoney = ableMoney;
            }

            public int getClientType() {
                return clientType;
            }

            public void setClientType(int clientType) {
                this.clientType = clientType;
            }

            public String getCreateDateStr() {
                return createDateStr;
            }

            public void setCreateDateStr(String createDateStr) {
                this.createDateStr = createDateStr;
            }
        }

        public static class BorrowsBean {
            private int status;
            private String key;
            private List<DataBean> data;
            /**
             * more :
             * status : 1
             * data : [{"id":2921,"name":"新手标签角标1","status":1,"type":"16","timeLimit":"10","apr":14.4,"schedule":"0","balance":"100000.0","isNewbor":1,"activityOne":"新手1","activityTwo":"新手2","cornerLable":2,"baseApr":7.2,"awardApr":7.2,"cornerLableVal":"http://192.168.0.149:8080/mobile_img/cornerLable/2.png","lowestAccount":"100","mostAccount":"","count":null},{"id":2922,"name":"新手标签角标2","status":1,"type":"16","timeLimit":"10","apr":18,"schedule":"0","balance":"100000.0","isNewbor":1,"activityOne":"新手1","activityTwo":"新手2","cornerLable":1,"baseApr":10.8,"awardApr":7.2,"cornerLableVal":"http://192.168.0.149:8080/mobile_img/cornerLable/1.png","lowestAccount":"100","mostAccount":"","count":null},{"id":2923,"name":"test","status":1,"type":"14","timeLimit":"100","apr":181.44,"schedule":"2","balance":"9800","isNewbor":1,"activityOne":null,"activityTwo":null,"cornerLable":0,"baseApr":1.3,"awardApr":180,"cornerLableVal":"","lowestAccount":"200","mostAccount":"","count":1},{"id":2831,"name":"新手标第241期","status":1,"type":"16","timeLimit":"7","apr":14.4,"schedule":"1","balance":"99500","isNewbor":0,"activityOne":null,"activityTwo":null,"cornerLable":0,"baseApr":5.4,"awardApr":9,"cornerLableVal":"","lowestAccount":"100","mostAccount":"20000","count":2}]
             * key : 新手专区
             */

            private String more;

            public String getMore() {
                return more;
            }

            public void setMore(String more) {
                this.more = more;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * id : 2921
                 * name : 新手标签角标1
                 * status : 1
                 * type : 16
                 * timeLimit : 10
                 * apr : 14.4
                 * schedule : 0
                 * balance : 100000.0
                 * isNewbor : 1
                 * activityOne : 新手1
                 * activityTwo : 新手2
                 * cornerLable : 2
                 * baseApr : 7.2
                 * awardApr : 7.2
                 * cornerLableVal : http://192.168.0.149:8080/mobile_img/cornerLable/2.png
                 * lowestAccount : 100
                 * mostAccount :
                 * count : null
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
                private int tq;
                private int useHongbao;

                public void setUseHongbao(int useHongbao){
                    this.useHongbao=useHongbao;
                }
                public int getUseHongbao(){
                    return useHongbao;
                }
                public int getTq(){
                    return tq;
                }
                public void setTq(int tq){
                    this.tq=tq;
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
    }
}
