package com.rongxun.xqlc.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by oguig on 2017/6/8.
 */

public class CashBean implements Serializable {

    /**
     * 资金明细bean
     * rcd : R0001
     * rmg : null
     * pageBean : {"pageNumber":1,"totalCount":5,"pageCount":1,"pageSize":10}
     * userAccDetailList : [{"type":"recharge_offline_reward","typeShow":"奖励","createDate":1496818592000,"sign":"1","money":"10000.00","ableMoney":"31554.00","tasteMoney":null,"remark":"新消息"},{"type":"recharge_offline","typeShow":"充值","createDate":1496818592000,"sign":"1","money":"10000.00","ableMoney":"21554.00","tasteMoney":null,"remark":"新消息"},{"type":"recharge_offline_reward","typeShow":"奖励","createDate":1496818442000,"sign":"1","money":"888.00","ableMoney":"11554.00","tasteMoney":null,"remark":"充值奖励"},{"type":"recharge_offline","typeShow":"充值","createDate":1496818442000,"sign":"1","money":"666.00","ableMoney":"10666.00","tasteMoney":null,"remark":"充值奖励"},{"type":"recharge","typeShow":"充值","createDate":1496815208000,"sign":"1","money":"10000.00","ableMoney":"10000.00","tasteMoney":null,"remark":"线上充值:10000.00元"}]
     */

    private String rcd;
    private Object rmg;
    private CashBean.PageBeanBean pageBean;
    private List<UserAccDetailListBean> userAccDetailList;

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

    public CashBean.PageBeanBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(CashBean.PageBeanBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<UserAccDetailListBean> getUserAccDetailList() {
        return userAccDetailList;
    }

    public void setUserAccDetailList(List<UserAccDetailListBean> userAccDetailList) {
        this.userAccDetailList = userAccDetailList;
    }

    public static class PageBeanBean {
        /**
         * pageNumber : 1
         * totalCount : 5
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

    public static class UserAccDetailListBean {
        /**
         * type : recharge_offline_reward
         * typeShow : 奖励
         * createDate : 1496818592000
         * sign : 1
         * money : 10000.00
         * ableMoney : 31554.00
         * tasteMoney : null
         * remark : 新消息
         */

        private String type;
        private String typeShow;
        private long createDate;
        private String sign;
        private String money;
        private String ableMoney;
        private Object tasteMoney;
        private String remark;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeShow() {
            return typeShow;
        }

        public void setTypeShow(String typeShow) {
            this.typeShow = typeShow;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getAbleMoney() {
            return ableMoney;
        }

        public void setAbleMoney(String ableMoney) {
            this.ableMoney = ableMoney;
        }

        public Object getTasteMoney() {
            return tasteMoney;
        }

        public void setTasteMoney(Object tasteMoney) {
            this.tasteMoney = tasteMoney;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

}
