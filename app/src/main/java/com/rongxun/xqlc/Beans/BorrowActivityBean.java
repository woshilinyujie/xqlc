package com.rongxun.xqlc.Beans;

import java.util.List;

/**
 * Created by jcb on 2017/11/16.
 */

public class BorrowActivityBean {

    /**
     * data : {"pageNumber":1,"pageSize":10,"searchBy":null,"keyword":null,"orderBy":null,"order":null,"thisSize":0,"totalCount":1,"result":[{"realname":"借款人1","money":1200,"loangreementUrl":"http://hzjcb.oss-cn-hangzhou.aliyuncs.com/agreementlcjh/3070/JCB-20180108-3070-65221-5065.html"}],"pageCount":1}
     * rcd : R0001
     * msg : 温馨提示：借款协议会在项目募集完成后生成
     */

    private DataBean data;
    private String rcd;
    private String msg;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * pageNumber : 1
         * pageSize : 10
         * searchBy : null
         * keyword : null
         * orderBy : null
         * order : null
         * thisSize : 0
         * totalCount : 1
         * result : [{"realname":"借款人1","money":1200,"loangreementUrl":"http://hzjcb.oss-cn-hangzhou.aliyuncs.com/agreementlcjh/3070/JCB-20180108-3070-65221-5065.html"}]
         * pageCount : 1
         */

        private int pageNumber;
        private int pageSize;
        private Object searchBy;
        private Object keyword;
        private Object orderBy;
        private Object order;
        private int thisSize;
        private int totalCount;
        private int pageCount;
        private List<ResultBean> result;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public Object getSearchBy() {
            return searchBy;
        }

        public void setSearchBy(Object searchBy) {
            this.searchBy = searchBy;
        }

        public Object getKeyword() {
            return keyword;
        }

        public void setKeyword(Object keyword) {
            this.keyword = keyword;
        }

        public Object getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(Object orderBy) {
            this.orderBy = orderBy;
        }

        public Object getOrder() {
            return order;
        }

        public void setOrder(Object order) {
            this.order = order;
        }

        public int getThisSize() {
            return thisSize;
        }

        public void setThisSize(int thisSize) {
            this.thisSize = thisSize;
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

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * realname : 借款人1
             * money : 1200
             * loangreementUrl : http://hzjcb.oss-cn-hangzhou.aliyuncs.com/agreementlcjh/3070/JCB-20180108-3070-65221-5065.html
             */

            private String realname;
            private int money;
            private String loangreementUrl;

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public String getLoangreementUrl() {
                return loangreementUrl;
            }

            public void setLoangreementUrl(String loangreementUrl) {
                this.loangreementUrl = loangreementUrl;
            }
        }
    }
}
