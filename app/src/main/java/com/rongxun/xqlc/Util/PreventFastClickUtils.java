package com.rongxun.xqlc.Util;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.utils
 * Author: HouShengLi
 * Time: 2017/06/15 13:52
 * E-mail: 13967189624@163.com
 * Description:
 */

public class PreventFastClickUtils {

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
