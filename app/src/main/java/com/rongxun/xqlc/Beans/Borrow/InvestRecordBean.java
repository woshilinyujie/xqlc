package com.rongxun.xqlc.Beans.Borrow;

import java.util.List;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Beans.Borrow
 * Author: HouShengLi
 * Time: 2017/07/11 10:24
 * E-mail: 13967189624@163.com
 * Description:
 */

public class InvestRecordBean {


    /**
     * rcd : R0001
     * rmg : null
     * borrowTenderItemList : [{"id":2823,"tenderId":64476,"username":"136****4431","createDate":1496991057000,"money":"100.0","account":"100.0","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"83744.00","clientType":0,"createDateStr":"1个月前"},{"id":2823,"tenderId":64475,"username":"150****7898","createDate":1496903826000,"money":"1000.0","account":"1000.0","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"144229.33","clientType":0,"createDateStr":"1个月前"},{"id":2823,"tenderId":64470,"username":"136****4431","createDate":1496894222000,"money":"1000.0","account":"1000.0","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"83744.00","clientType":0,"createDateStr":"1个月前"},{"id":2823,"tenderId":64469,"username":"150****7898","createDate":1496812946000,"money":"1000.0","account":"1000.0","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"144229.33","clientType":0,"createDateStr":"1个月前"},{"id":2823,"tenderId":64385,"username":"150****7898","createDate":1492743290000,"money":"1000","account":"1000","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"144229.33","clientType":1,"createDateStr":"1个月前"},{"id":2823,"tenderId":64384,"username":"150****7898","createDate":1492743225000,"money":"1000","account":"1000","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"144229.33","clientType":1,"createDateStr":"1个月前"},{"id":2823,"tenderId":64322,"username":"135****4349","createDate":1491039202000,"money":"10000","account":"10000","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"0.00","clientType":2,"createDateStr":"1个月前"},{"id":2823,"tenderId":64321,"username":"159****3988","createDate":1491039102000,"money":"10000.0","account":"10000.0","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"108.00","clientType":0,"createDateStr":"1个月前"},{"id":2823,"tenderId":64319,"username":"15088667898112","createDate":1491038970000,"money":"10000.0","account":"10000.0","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"0.00","clientType":0,"createDateStr":"1个月前"},{"id":2823,"tenderId":64318,"username":"159****8678","createDate":1491038862000,"money":"10000.0","account":"10000.0","autoTenderStatus":"0","status":"1","arp":null,"loginStatus":null,"ableMoney":"88.00","clientType":0,"createDateStr":"1个月前"}]
     * apr : 9.72
     * pageBean : {"pageNumber":1,"totalCount":19,"pageCount":2,"pageSize":10}
     */

    private String rcd;
    private Object rmg;
    private String apr;
    private PageBeanBean pageBean;
    private List<BorrowTenderItemListBean> borrowTenderItemList;

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

    public String getApr() {
        return apr;
    }

    public void setApr(String apr) {
        this.apr = apr;
    }

    public PageBeanBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBeanBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<BorrowTenderItemListBean> getBorrowTenderItemList() {
        return borrowTenderItemList;
    }

    public void setBorrowTenderItemList(List<BorrowTenderItemListBean> borrowTenderItemList) {
        this.borrowTenderItemList = borrowTenderItemList;
    }

    public static class PageBeanBean {
        /**
         * pageNumber : 1
         * totalCount : 19
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

    public static class BorrowTenderItemListBean {
        /**
         * id : 2823
         * tenderId : 64476
         * username : 136****4431
         * createDate : 1496991057000
         * money : 100.0
         * account : 100.0
         * autoTenderStatus : 0
         * status : 1
         * arp : null
         * loginStatus : null
         * ableMoney : 83744.00
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
}
