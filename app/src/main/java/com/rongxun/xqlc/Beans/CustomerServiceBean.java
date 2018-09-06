package com.rongxun.xqlc.Beans;

/**
 * Created by oguig on 2017/8/7.
 */

public class CustomerServiceBean {

    /**
     * customerMap : {"company_email":{"description":"客服接口之公司邮箱","imageUrl":"","key":"company_email","name":"公司邮箱","order":4,"value":"jcb@hzjcb.com"},"service_hours":{"description":"客服接口之服务时间","imageUrl":"","key":"service_hours","name":"服务时间","order":2,"value":"工作日 9:00-17:30"},"online_phone":{"description":"客服接口之热线电话","imageUrl":"","key":"online_phone","name":"热线电话","order":1,"value":"400-0333-113"},"wechat_account":{"description":"客服接口之微信公众号","imageUrl":"","key":"wechat_account","name":"微信公众号","order":3,"value":"my_jcb"},"hotactivity":"当西湖遇上龙井"}
     * rcd : R0001
     * rmg :
     */

    private CustomerMapBean customerMap;
    private String rcd;
    private String rmg;

    public CustomerMapBean getCustomerMap() {
        return customerMap;
    }

    public void setCustomerMap(CustomerMapBean customerMap) {
        this.customerMap = customerMap;
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

    public static class CustomerMapBean {
        /**
         * company_email : {"description":"客服接口之公司邮箱","imageUrl":"","key":"company_email","name":"公司邮箱","order":4,"value":"jcb@hzjcb.com"}
         * service_hours : {"description":"客服接口之服务时间","imageUrl":"","key":"service_hours","name":"服务时间","order":2,"value":"工作日 9:00-17:30"}
         * online_phone : {"description":"客服接口之热线电话","imageUrl":"","key":"online_phone","name":"热线电话","order":1,"value":"400-0333-113"}
         * wechat_account : {"description":"客服接口之微信公众号","imageUrl":"","key":"wechat_account","name":"微信公众号","order":3,"value":"my_jcb"}
         * hotactivity : 当西湖遇上龙井
         */

        private CompanyEmailBean company_email;
        private ServiceHoursBean service_hours;
        private OnlinePhoneBean online_phone;
        private WechatAccountBean wechat_account;
        private String hotactivity;

        public CompanyEmailBean getCompany_email() {
            return company_email;
        }

        public void setCompany_email(CompanyEmailBean company_email) {
            this.company_email = company_email;
        }

        public ServiceHoursBean getService_hours() {
            return service_hours;
        }

        public void setService_hours(ServiceHoursBean service_hours) {
            this.service_hours = service_hours;
        }

        public OnlinePhoneBean getOnline_phone() {
            return online_phone;
        }

        public void setOnline_phone(OnlinePhoneBean online_phone) {
            this.online_phone = online_phone;
        }

        public WechatAccountBean getWechat_account() {
            return wechat_account;
        }

        public void setWechat_account(WechatAccountBean wechat_account) {
            this.wechat_account = wechat_account;
        }

        public String getHotactivity() {
            return hotactivity;
        }

        public void setHotactivity(String hotactivity) {
            this.hotactivity = hotactivity;
        }

        public static class CompanyEmailBean {
            /**
             * description : 客服接口之公司邮箱
             * imageUrl :
             * key : company_email
             * name : 公司邮箱
             * order : 4
             * value : jcb@hzjcb.com
             */

            private String description;
            private String imageUrl;
            private String key;
            private String name;
            private int order;
            private String value;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class ServiceHoursBean {
            /**
             * description : 客服接口之服务时间
             * imageUrl :
             * key : service_hours
             * name : 服务时间
             * order : 2
             * value : 工作日 9:00-17:30
             */

            private String description;
            private String imageUrl;
            private String key;
            private String name;
            private int order;
            private String value;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class OnlinePhoneBean {
            /**
             * description : 客服接口之热线电话
             * imageUrl :
             * key : online_phone
             * name : 热线电话
             * order : 1
             * value : 400-0333-113
             */

            private String description;
            private String imageUrl;
            private String key;
            private String name;
            private int order;
            private String value;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class WechatAccountBean {
            /**
             * description : 客服接口之微信公众号
             * imageUrl :
             * key : wechat_account
             * name : 微信公众号
             * order : 3
             * value : my_jcb
             */

            private String description;
            private String imageUrl;
            private String key;
            private String name;
            private int order;
            private String value;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
