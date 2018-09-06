package com.rongxun.xqlc.Beans.home;

import java.util.List;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Beans.home
 * Author: HouShengLi
 * Time: 2017/07/24 17:32
 * E-mail: 13967189624@163.com
 * Description:
 */

public class TipBean {


    /**
     * rcd : R0001
     * rmg : null
     * articleItemList : [{"id":828,"title":"融宝通道各银行充值额度","content":null,"createDate":1500874335000},{"id":826,"title":"【重要】人民银行维护通知","content":null,"createDate":1500621063000},{"id":810,"title":"关于金储宝近期股权结构调整公告","content":null,"createDate":1497862804000}]
     * pageBean : {"pageNumber":1,"totalCount":32,"pageCount":11,"pageSize":3}
     */

    private String rcd;
    private String rmg;
    private PageBeanBean pageBean;
    private List<ArticleItemListBean> articleItemList;

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

    public PageBeanBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBeanBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<ArticleItemListBean> getArticleItemList() {
        return articleItemList;
    }

    public void setArticleItemList(List<ArticleItemListBean> articleItemList) {
        this.articleItemList = articleItemList;
    }

    public static class PageBeanBean {
        /**
         * pageNumber : 1
         * totalCount : 32
         * pageCount : 11
         * pageSize : 3
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

    public static class ArticleItemListBean {
        /**
         * id : 828
         * title : 融宝通道各银行充值额度
         * content : null
         * createDate : 1500874335000
         */

        private int id;
        private String title;
        private String content;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
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
