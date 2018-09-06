package com.rongxun.xqlc.Beans;

import java.util.List;

/**
 * Created by oguig on 2017/7/27.
 */

public class PlatformDynamicBean {

    /**
     * rcd : R0001
     * rmg : null
     * articleItemList : null
     * mediaReportDTOList : [{"rcd":"R0001","rmg":null,"id":727,"title":"和平影视战略控股金储宝","author":"金储宝 2017年2月26日","coverImg":"/data/upfiles/images/201703/46bea66b2f524a699b1e981afa89e0bf.jpg"},{"rcd":"R0001","rmg":null,"id":515,"title":"法律法规","author":"金储宝","coverImg":"/data/upfiles/images/201701/5c4c5b050176488c8b20ff98da3111b4.png"},{"rcd":"R0001","rmg":null,"id":666,"title":"宝付支付宣称关闭非国资、无背景互金平台  金储宝不受任何影响","author":"东方头条   2016年11月3日","coverImg":"/data/upfiles/images/201702/0fb005eac1b74066a64d80c3a8967492.jpg"},{"rcd":"R0001","rmg":null,"id":667,"title":"中国互联网金融协会监管升级 金储宝坚持合规运营引领发展","author":"搜狐网   2016年11月9日","coverImg":"/data/upfiles/images/201702/5c933b2404914cfdb1c11ccbc6a0ab0d.jpg"},{"rcd":"R0001","rmg":null,"id":668,"title":"互联网金融行业合规发展 金储宝行稳致远共进步","author":"金储宝   2016年11月16日","coverImg":"/data/upfiles/images/201702/15ba5f4d61fc4ed6824704914c8b1b61.jpg"},{"rcd":"R0001","rmg":null,"id":669,"title":"金储宝\u2014\u2014在合规发展中前行","author":"搜狐网   2016年11月9日","coverImg":"/data/upfiles/images/201702/4475ef391a1240ec8cbf2eed176c3770.jpg"},{"rcd":"R0001","rmg":null,"id":670,"title":"金储宝上线不到两年却获大量用户青睐的原因是什么？","author":"凤凰网  2016年11月21日","coverImg":"/data/upfiles/images/201702/bc7222b244bb4575845cdb1bdee4bd5b.png"},{"rcd":"R0001","rmg":null,"id":662,"title":"如何放心理财？金储宝带你解读行业趋势","author":"中国新闻网   2016年11月02日","coverImg":"/data/upfiles/images/201612/81f7a951366e4947bf2b3e78c5f55538.jpg"},{"rcd":"R0001","rmg":null,"id":663,"title":"中国互联网监管升级 金储宝规范发展响应监管","author":"网易新闻   2016年11月9日","coverImg":"/data/upfiles/images/201612/f518ba6dd3224484a3e4e621d7544502.jpg"},{"rcd":"R0001","rmg":null,"id":664,"title":"金储宝：安全合规、健康稳健的秘诀是什么？","author":"财经新闻网   2016年10月31日","coverImg":"/data/upfiles/images/201612/9c2fd0d5e47f46069332bb1907b17396.jpg"},{"rcd":"R0001","rmg":null,"id":665,"title":"面对洗牌元年，互金行业应该何去何从！！","author":"凤凰网   2016年10月26日","coverImg":"/data/upfiles/images/201612/bd9320e430094c9699851902850cfa0f.jpg"},{"rcd":"R0001","rmg":null,"id":548,"title":"信托收益率开年四连降直破7％ 政信项目最热门","author":"金储宝","coverImg":"/data/upfiles/images/201612/f1f1953b05284b4a8da8b3a92583209e.jpg"},{"rcd":"R0001","rmg":null,"id":543,"title":"两年发展厘清行业规则 P2P重归牌照路","author":"金储宝","coverImg":"/data/upfiles/images/201612/769422843354457086bb6e0ce8acec7c.jpg"},{"rcd":"R0001","rmg":null,"id":551,"title":"互联网金融第三方支付讲何去何从","author":"金储宝","coverImg":"/data/upfiles/images/201612/b56621afa8a44426a138a0c9fb19ecdc.jpg"},{"rcd":"R0001","rmg":null,"id":552,"title":"监管高压风险释放 P2P不会毙命只会抗体更强","author":"金储宝   2016年10月21日","coverImg":"/data/upfiles/images/201612/345514f0484c404fa92952a6aca17f9e.jpg"}]
     * pageBean : {"pageNumber":1,"totalCount":17,"pageCount":2,"pageSize":15}
     */

    private String rcd;
    private Object rmg;
    private Object articleItemList;
    private PageBeanBean pageBean;
    private List<MediaReportDTOListBean> mediaReportDTOList;

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

    public Object getArticleItemList() {
        return articleItemList;
    }

    public void setArticleItemList(Object articleItemList) {
        this.articleItemList = articleItemList;
    }

    public PageBeanBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBeanBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<MediaReportDTOListBean> getMediaReportDTOList() {
        return mediaReportDTOList;
    }

    public void setMediaReportDTOList(List<MediaReportDTOListBean> mediaReportDTOList) {
        this.mediaReportDTOList = mediaReportDTOList;
    }

    public static class PageBeanBean {
        /**
         * pageNumber : 1
         * totalCount : 17
         * pageCount : 2
         * pageSize : 15
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

    public static class MediaReportDTOListBean {
        /**
         * rcd : R0001
         * rmg : null
         * id : 727
         * title : 和平影视战略控股金储宝
         * author : 金储宝 2017年2月26日
         * coverImg : /data/upfiles/images/201703/46bea66b2f524a699b1e981afa89e0bf.jpg
         */

        private String rcd;
        private Object rmg;
        private int id;
        private String title;
        private String author;
        private String coverImg;

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

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }
    }
}
