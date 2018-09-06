package com.rongxun.xqlc.Beans;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.bean
 * Author: HouShengLi
 * Time: 2017/06/08 14:38
 * E-mail: 13967189624@163.com
 * Description:
 */

public class EventDetailBean {


    /**
     * rcd : R0001
     * rmg : null
     * activity : {"id":43,"createDate":1494777702000,"modifyDate":1495266866000,"title":"只想\u201c粽\u201d意你","startTime":1494777600000,"endTime":1495123200000,"imgWeb":"/data/upfiles/images/201705/eb7873f1a8a541429b22aa5804e8122d.jpg","imgApp":"/data/upfiles/images/201705/47f2b436da7b487197e19cd0dad21614.jpg","content":"https://www.hzjcb.com/web/data/upfiles/images/201705/9532507bc05c4154beb380381ccbf720.jpg","status":2,"orderList":772,"array":null}
     */

    private String rcd;
    private Object rmg;
    private ActivityBean activity;

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

    public ActivityBean getActivity() {
        return activity;
    }

    public void setActivity(ActivityBean activity) {
        this.activity = activity;
    }

    public static class ActivityBean {
        /**
         * id : 43
         * createDate : 1494777702000
         * modifyDate : 1495266866000
         * title : 只想“粽”意你
         * startTime : 1494777600000
         * endTime : 1495123200000
         * imgWeb : /data/upfiles/images/201705/eb7873f1a8a541429b22aa5804e8122d.jpg
         * imgApp : /data/upfiles/images/201705/47f2b436da7b487197e19cd0dad21614.jpg
         * content : https://www.hzjcb.com/web/data/upfiles/images/201705/9532507bc05c4154beb380381ccbf720.jpg
         * status : 2
         * orderList : 772
         * array : null
         */

        private int id;
        private long createDate;
        private long modifyDate;
        private String title;
        private long startTime;
        private long endTime;
        private String imgWeb;
        private String imgApp;
        private String content;
        private int status;
        private int orderList;
        private Object array;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public long getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(long modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getImgWeb() {
            return imgWeb;
        }

        public void setImgWeb(String imgWeb) {
            this.imgWeb = imgWeb;
        }

        public String getImgApp() {
            return imgApp;
        }

        public void setImgApp(String imgApp) {
            this.imgApp = imgApp;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getOrderList() {
            return orderList;
        }

        public void setOrderList(int orderList) {
            this.orderList = orderList;
        }

        public Object getArray() {
            return array;
        }

        public void setArray(Object array) {
            this.array = array;
        }
    }
}
