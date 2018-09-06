package com.rongxun.xqlc.Beans;

import java.util.List;

/**
 * PROJECT_NAME: jyjf_android
 * PACKAGE_NAME: com.jcb.jinyilicai.Beans
 * Author: HouShengLi
 * Time: 2017/05/27 16:18
 * E-mail: 13967189624@163.com
 * Description:
 */

public class LoginBgBean {


    /**
     * rcd : R0001
     * rmg : null
     * homeActivityList : [{"id":138,"title":"登录注册","linkUrl":"1","imageUrl":"/data/upfiles/images/201705/c39140c7b22c400195c9a98b4f14f6a6.png","order":1,"blankTarget":null,"description":"app_register_img","type":8}]
     * pageBean : null
     */

    private String rcd;
    private Object rmg;
    private Object pageBean;
    private List<HomeActivityListBean> homeActivityList;

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

    public Object getPageBean() {
        return pageBean;
    }

    public void setPageBean(Object pageBean) {
        this.pageBean = pageBean;
    }

    public List<HomeActivityListBean> getHomeActivityList() {
        return homeActivityList;
    }

    public void setHomeActivityList(List<HomeActivityListBean> homeActivityList) {
        this.homeActivityList = homeActivityList;
    }

    public static class HomeActivityListBean {
        /**
         * id : 138
         * title : 登录注册
         * linkUrl : 1
         * imageUrl : /data/upfiles/images/201705/c39140c7b22c400195c9a98b4f14f6a6.png
         * order : 1
         * blankTarget : null
         * description : app_register_img
         * type : 8
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
}
