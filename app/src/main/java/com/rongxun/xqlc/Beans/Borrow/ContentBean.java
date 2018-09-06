package com.rongxun.xqlc.Beans.Borrow;

/**
 * PROJECT_NAME: Sample
 * PACKAGE_NAME: com.guaika.sample.bean
 * Author: HouShengLi
 * Time: 2017/05/31 21:40
 * E-mail: 13967189624@163.com
 * Description:
 */

public class ContentBean extends ManageBean {


    /**
     * id : 8
     * name : 新手标第3期
     * status : 1
     * type : 16
     * timeLimit : 16
     * apr : 8.64
     * schedule : 0
     * balance : 66600.0
     * isNewbor : 1
     * activityOne : 满一百减30
     * activityTwo : 信不信由你
     * cornerLable : 0
     * baseApr : 8.64
     * awardApr : 1.2
     * count : 2
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
    private int count;//已售多少笔
    private boolean isFirst = true;//是不是第一次展示这个数据
    private boolean isLast = false;//是不是这个专区最后一条数据
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
    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public String getCornerLableVal() {
        return cornerLableVal;
    }

    public void setCornerLableVal(String cornerLableVal) {
        this.cornerLableVal = cornerLableVal;
    }

    private String cornerLableVal;

    @Override
    public int getTypes() {
        return ITEM;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

}
