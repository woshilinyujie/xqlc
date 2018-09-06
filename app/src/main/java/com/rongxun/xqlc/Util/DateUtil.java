package com.rongxun.xqlc.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * PROJECT_NAME: jwlc_android
 * PACKAGE_NAME: com.hzgeek.jinwanlicai.utils
 * Author: HouShengLi
 * Time: 2017/06/06 14:15
 * E-mail: 13967189624@163.com
 * Description: 对不同日期和时间的格式方法的封装
 */

public class DateUtil {


    private static DateUtil instance;

    private DateUtil() {

    }

    public static DateUtil getInstance() {
        if (instance == null) {
            synchronized (DateUtil.class) {
                if (instance == null) {
                    instance = new DateUtil();
                }
            }
        }
        return instance;
    }


    /**
     * 通过long类型的值返回当前的 星期几
     *
     * @param time
     * @return
     */
    public String getWeekday(long time) {
        Calendar calen = Calendar.getInstance();
        calen.setTimeInMillis(time);
        int week = calen.get(Calendar.DAY_OF_WEEK);
        String result = "星期一";
        switch (week) {
            case Calendar.SUNDAY:
                result = "星期日";
                break;
            case Calendar.MONDAY:
                result = "星期一";
                break;
            case Calendar.TUESDAY:
                result = "星期二";
                break;
            case Calendar.WEDNESDAY:
                result = "星期三";
                break;
            case Calendar.THURSDAY:
                result = "星期四";
                break;
            case Calendar.FRIDAY:
                result = "星期五";
                break;
            case Calendar.SATURDAY:
                result = "星期六";
                break;
            default:
                result = "星期一";
                break;
        }
        return result;
    }

    /**
     * 判断两个时间是否属于同一天
     *
     * @param time1
     * @param time2
     * @return
     */
    public boolean isSameDay(long time1, long time2) {
        Calendar calen = Calendar.getInstance();
        calen.setTimeInMillis(time1);
        int day1 = calen.get(Calendar.DAY_OF_YEAR);
        calen.setTimeInMillis(time2);
        int day2 = calen.get(Calendar.DAY_OF_YEAR);
        return day1 == day2;
    }

    /**
     * @param time
     * @return
     * @描述: 2017-02-23
     */
    public String getDayOrMonthOrYear(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(time));
    }

    /**
     * @param time
     * @return
     * @描述: 2017年02月23日
     */
    public String getDayOrMonthOrYear2(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年-MM月-dd日");
        return sdf.format(new Date(time));
    }

    /**
     * @param time
     * @return
     * @描述: 2017/02/23
     */
    public String getDayOrMonthOrYear3(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date(time));
    }

    /**
     * @param time
     * @return
     * @描述: 2017.02.23
     */
    public String getDayOrMonthOrYear4(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(time));
    }

    /**
     * @param time
     * @return
     * @描述: 2017-02-23 06:26:12
     */
    public String dateFormat2(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date(time));
    }

    /**
     * @param time
     * @return
     * @描述: 2017年02月23日06点26分12秒
     */
    public String dateFormat3(long time) {
        return new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒", Locale.getDefault())
                .format(new Date(time));
    }


}
