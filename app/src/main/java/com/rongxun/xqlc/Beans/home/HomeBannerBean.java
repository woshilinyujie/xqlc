package com.rongxun.xqlc.Beans.home;


import java.util.List;

public class HomeBannerBean {


    /**
     * indexHomeImage : [{"id":345,"title":"活动中心","linkUrl":"#","imageUrl":"#","order":1,"blankTarget":null,"description":"活动中心","type":10},{"id":346,"title":"新手福利","linkUrl":"#","imageUrl":"#","order":2,"blankTarget":null,"description":"新手福利","type":10},{"id":347,"title":"推荐有礼","linkUrl":"#","imageUrl":"#","order":3,"blankTarget":null,"description":"推荐有礼","type":10},{"id":349,"title":"安全保障","linkUrl":"#","imageUrl":"#","order":4,"blankTarget":null,"description":"安全保障","type":10}]
     * indexBid : {"id":3209,"name":"普通项目009","status":1,"type":"16","timeLimit":"30","apr":15.84,"schedule":"0","balance":"20000.0","isNewbor":3,"activityOne":null,"activityTwo":null,"cornerLable":2,"baseApr":8.64,"awardApr":7.2,"cornerLableVal":"http://192.168.0.149:8080/img/cornerLable/2.png","lowestAccount":"100","mostAccount":"","count":0,"useHongbao":0,"rongXunFlg":"2","account":"20000"}
     * articleList : [{"id":973,"title":"官方公告2app","content":null,"createDate":null},{"id":972,"title":"官方公告app","content":null,"createDate":null}]
     * indexImageItem : [{"rcd":"R0001","rmg":null,"imageUrl":"","type":1,"typeTarget":"","typetz":null},{"rcd":"R0001","rmg":null,"imageUrl":"http://hzjcb.oss-cn-hangzhou.aliyuncs.com/Appimage/Home%20icons/tuijian.png","type":1,"typeTarget":"https://m.hzjcb.com/html/invite/invite.html","typetz":null}]
     * rcd : R0001
     * rmg : 首页数据
     */

    private IndexBidBean indexBid;
    private String rcd;
    private String rmg;
    private List<IndexHomeImageBean> indexHomeImage;
    private List<ArticleListBean> articleList;
    private List<IndexImageItemBean> indexImageItem;

    public IndexBidBean getIndexBid() {
        return indexBid;
    }

    public void setIndexBid(IndexBidBean indexBid) {
        this.indexBid = indexBid;
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

    public List<IndexHomeImageBean> getIndexHomeImage() {
        return indexHomeImage;
    }

    public void setIndexHomeImage(List<IndexHomeImageBean> indexHomeImage) {
        this.indexHomeImage = indexHomeImage;
    }

    public List<ArticleListBean> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleListBean> articleList) {
        this.articleList = articleList;
    }

    public List<IndexImageItemBean> getIndexImageItem() {
        return indexImageItem;
    }

    public void setIndexImageItem(List<IndexImageItemBean> indexImageItem) {
        this.indexImageItem = indexImageItem;
    }

    public static class IndexBidBean {
        /**
         * id : 3209
         * name : 普通项目009
         * status : 1
         * type : 16
         * timeLimit : 30
         * apr : 15.84
         * schedule : 0
         * balance : 20000.0
         * isNewbor : 3
         * activityOne : null
         * activityTwo : null
         * cornerLable : 2
         * baseApr : 8.64
         * awardApr : 7.2
         * cornerLableVal : http://192.168.0.149:8080/img/cornerLable/2.png
         * lowestAccount : 100
         * mostAccount :
         * count : 0
         * useHongbao : 0
         * rongXunFlg : 2
         * account : 20000
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
        private Object activityOne;
        private Object activityTwo;
        private int cornerLable;
        private double baseApr;
        private double awardApr;
        private String cornerLableVal;
        private String lowestAccount;
        private String mostAccount;
        private int count;
        private int useHongbao;
        private String rongXunFlg;
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

        public Object getActivityOne() {
            return activityOne;
        }

        public void setActivityOne(Object activityOne) {
            this.activityOne = activityOne;
        }

        public Object getActivityTwo() {
            return activityTwo;
        }

        public void setActivityTwo(Object activityTwo) {
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

        public int getUseHongbao() {
            return useHongbao;
        }

        public void setUseHongbao(int useHongbao) {
            this.useHongbao = useHongbao;
        }

        public String getRongXunFlg() {
            return rongXunFlg;
        }

        public void setRongXunFlg(String rongXunFlg) {
            this.rongXunFlg = rongXunFlg;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }

    public static class IndexHomeImageBean {
        /**
         * id : 345
         * title : 活动中心
         * linkUrl : #
         * imageUrl : #
         * order : 1
         * blankTarget : null
         * description : 活动中心
         * type : 10
         */

        private int id;
        private String title;
        private String linkUrl;
        private String imageUrl;
        private int order;
        private Object blankTarget;
        private String description;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public Object getBlankTarget() {
            return blankTarget;
        }

        public void setBlankTarget(Object blankTarget) {
            this.blankTarget = blankTarget;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class ArticleListBean {
        /**
         * id : 973
         * title : 官方公告2app
         * content : null
         * createDate : null
         */

        private int id;
        private String title;
        private Object content;
        private Object createDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public Object getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Object createDate) {
            this.createDate = createDate;
        }
    }

    public static class IndexImageItemBean {
        /**
         * rcd : R0001
         * rmg : null
         * imageUrl :
         * type : 1
         * typeTarget :
         * typetz : null
         */

        private String rcd;
        private Object rmg;
        private String imageUrl;
        private int type;
        private String typeTarget;
        private int typetz;

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

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeTarget() {
            return typeTarget;
        }

        public void setTypeTarget(String typeTarget) {
            this.typeTarget = typeTarget;
        }

        public int getTypetz() {
            return typetz;
        }

        public void setTypetz(int typetz) {
            this.typetz = typetz;
        }
    }
}
