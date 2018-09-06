package com.rongxun.xqlc.Beans;

import java.util.List;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.bean
 * Author: HouShengLi
 * Time: 2017/06/03 18:16
 * E-mail: 13967189624@163.com
 * Description:
 */

public class CommuniqueBean {


    /**
     * rcd : R0001
     * rmg : null
     * articleItemList : [{"id":804,"title":"融宝通道各银行充值额度","content":null,"createDate":1496736102000},{"id":801,"title":"邮政储蓄银行维护通知","content":null,"createDate":1496215777000},{"id":797,"title":"兴业银行维护通知","content":null,"createDate":1495792836000},{"id":799,"title":"建设银行维护通知","content":null,"createDate":1495793005000},{"id":793,"title":"关于端午节放假安排的通知","content":null,"createDate":1495596375000},{"id":791,"title":"浦发银行维护通知","content":null,"createDate":1495595945000},{"id":789,"title":"各银行维护通知","content":null,"createDate":1495195116000},{"id":787,"title":"民生银行维护通知","content":null,"createDate":1495174542000},{"id":783,"title":"【重要】关于金储宝全面执行\u201c即投即起息\u201d计息方式的公告","content":null,"createDate":1494810660000},{"id":781,"title":"关于电信网络故障现已恢复正常通知","content":null,"createDate":1494649389000}]
     * pageBean : {"pageNumber":1,"totalCount":44,"pageCount":5,"pageSize":10}
     */

    private String rcd;
    private Object rmg;
    private PageBeanBean pageBean;
    private List<DataBean> articleItemList;

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

    public PageBeanBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBeanBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<DataBean> getArticleItemList() {
        return articleItemList;
    }

    public void setArticleItemList(List<DataBean> articleItemList) {
        this.articleItemList = articleItemList;
    }

    public static class PageBeanBean {
        /**
         * pageNumber : 1
         * totalCount : 44
         * pageCount : 5
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

    public static class DataBean {
        /**
         * id : 804
         * title : 融宝通道各银行充值额度
         * content : null
         * createDate : 1496736102000
         */

        private int id;
        private String title;
        private Object content;
        private long createDate;

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

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }
    }
}
