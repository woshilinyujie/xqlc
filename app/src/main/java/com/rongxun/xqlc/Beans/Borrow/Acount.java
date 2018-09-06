package com.rongxun.xqlc.Beans.Borrow;

/**
 * PROJECT_NAME: Sample
 * PACKAGE_NAME: com.guaika.sample.bean
 * Author: HouShengLi
 * Time: 2017/05/18 19:54
 * E-mail: 13967189624@163.com
 * Description:
 */

public class Acount {

    private String time;
    private String user;
    private String money;

    public Acount(){

    }

    public Acount(String time, String user, String money) {
        this.time = time;
        this.user = user;
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
