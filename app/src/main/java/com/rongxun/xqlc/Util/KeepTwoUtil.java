package com.rongxun.xqlc.Util;

import java.math.BigDecimal;

/**
 * Created by oguig on 2017/8/29.
 */

public class KeepTwoUtil {
    /**
     * 保留两位小数
     *
     * @param var
     * @return
     **/
    public static String keep2Decimal(double var) {
        BigDecimal decimal = new BigDecimal(var);
        BigDecimal setScale1 = decimal.setScale(2,BigDecimal.ROUND_HALF_UP);
        return String.valueOf(setScale1);
    }
}
