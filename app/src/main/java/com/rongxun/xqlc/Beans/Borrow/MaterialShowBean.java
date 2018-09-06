package com.rongxun.xqlc.Beans.Borrow;

import java.io.Serializable;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Beans.Borrow
 * Author: HouShengLi
 * Time: 2017/07/10 12:34
 * E-mail: 13967189624@163.com
 * Description:
 */

public class MaterialShowBean implements Serializable {

    private String title;//材料名称
    private String imageUrl;//材料url

    public MaterialShowBean() {
    }

    public MaterialShowBean(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
