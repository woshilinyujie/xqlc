package com.rongxun.xqlc.Beans.Borrow;

/**
 * PROJECT_NAME: Sample
 * PACKAGE_NAME: com.guaika.sample.bean
 * Author: HouShengLi
 * Time: 2017/05/31 21:39
 * E-mail: 13967189624@163.com
 * Description:
 */

public class DistrictBean extends ManageBean {

    private String district;//区
    private String more;

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public DistrictBean() {

    }

    public DistrictBean(String district) {
        this.district = district;
    }

    public int getTypes() {
        return SECTION;
    }
}
