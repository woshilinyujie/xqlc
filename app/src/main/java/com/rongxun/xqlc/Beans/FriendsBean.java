package com.rongxun.xqlc.Beans;

import java.util.List;

/**
 * Created by oguig on 2017/8/11.
 */

public class FriendsBean {

    /**
     * rcd : R0001
     * rmg : null
     * awardTotalMoneyTj : 44.20
     * friends : 7
     * friendList : [{"id":193425,"username":"15858242019","createDate":1495106460000,"uadSumMoney":44.2},{"id":193449,"username":"13811111112","createDate":1496198793000,"uadSumMoney":null}]
     * pageBean : {"pageNumber":1,"totalCount":7,"pageCount":1,"pageSize":10}
     */

    private String rcd;
    private Object rmg;
    private String awardTotalMoneyTj;
    private int friends;
    private PageBeanBean pageBean;
    private List<FriendListBean> friendList;

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

    public String getAwardTotalMoneyTj() {
        return awardTotalMoneyTj;
    }

    public void setAwardTotalMoneyTj(String awardTotalMoneyTj) {
        this.awardTotalMoneyTj = awardTotalMoneyTj;
    }

    public int getFriends() {
        return friends;
    }

    public void setFriends(int friends) {
        this.friends = friends;
    }

    public PageBeanBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBeanBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<FriendListBean> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<FriendListBean> friendList) {
        this.friendList = friendList;
    }

    public static class PageBeanBean {
        /**
         * pageNumber : 1
         * totalCount : 7
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

    public static class FriendListBean {
        /**
         * id : 193425
         * username : 15858242019
         * createDate : 1495106460000
         * uadSumMoney : 44.2
         */

        private int id;
        private String username;
        private long createDate;
        private double uadSumMoney;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public double getUadSumMoney() {
            return uadSumMoney;
        }

        public void setUadSumMoney(double uadSumMoney) {
            this.uadSumMoney = uadSumMoney;
        }
    }
}
