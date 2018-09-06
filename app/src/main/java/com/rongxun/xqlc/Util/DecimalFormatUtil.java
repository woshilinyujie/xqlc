package com.rongxun.xqlc.Util;

import java.text.DecimalFormat;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.utils
 * Author: HouShengLi
 * Time: 2017/06/06 14:50
 * E-mail: 13967189624@163.com
 * Description:格式化十进制数字 如：double pi = 3.1415926
 *
 */

public class DecimalFormatUtil {

    private static DecimalFormatUtil instance;

    private DecimalFormatUtil() {

    }

    public static DecimalFormatUtil getInstance() {
        if (instance == null) {
            synchronized (DecimalFormatUtil.class) {
                if (instance == null) {
                    instance = new DecimalFormatUtil();
                }
            }
        }
        return instance;
    }


    /**
     * 取所有整数部分
     * @param pi
     * @return
     */
    public String getInt(double pi) {


        return new DecimalFormat("0").format(pi);
    }


    /**
     * 取所有整数 两位小数 3.14
     * @param pi
     * @return
     */
    public String getDouble2(double pi) {

        return new DecimalFormat("0.00").format(pi);
    }

    /**
     * 取所有整数 一位小数 3.1
     * @param pi
     * @return
     */
    public String getDouble(double pi) {

        return new DecimalFormat("0.0").format(pi);
    }



    /**
     * 以百分比方式计数，并取两位小数:314.16%
     * @param pi
     * @return
     */
    public String getPercent(double pi) {

        return new DecimalFormat("#.##%").format(pi);
    }

    /**
     * 每三位以逗号进行分隔:299,792,458
     * @param pi
     * @return
     */
    public String getLong(long pi) {

        return new DecimalFormat(",###").format(pi);
    }

}
