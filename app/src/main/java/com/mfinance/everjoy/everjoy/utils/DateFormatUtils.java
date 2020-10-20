package com.mfinance.everjoy.everjoy.utils;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 根据时间戳获取不同的时间格式
 */
public class DateFormatUtils {

    /**
     * 文章列表时间
     */
    public static final String DATE_FORMAT1 = "yyyy-MM-dd HH:mm";

    public static SimpleDateFormat getDateFormat1() {
        return new SimpleDateFormat(DATE_FORMAT1, Locale.getDefault());
    }


    /**
     * 节目列表时间
     */
    public static final String DATE_FORMAT2 = "HH:mm";

    public static SimpleDateFormat getDateFormat2() {
        return new SimpleDateFormat(DATE_FORMAT2, Locale.getDefault());
    }

    /**
     * 节目列表时间
     */
    public static final String DATE_FORMAT3 = "yyyy-MM-dd";

    public static SimpleDateFormat getDateFormat3() {
        return new SimpleDateFormat(DATE_FORMAT3, Locale.getDefault());
    }


    /**
     * 节目列表时间
     */
    public static final String DATE_FORMAT4 = "MM-dd HH:mm";

    public static SimpleDateFormat getDateFormat4() {
        return new SimpleDateFormat(DATE_FORMAT4, Locale.getDefault());
    }

    /**
     * 节目列表时间
     */
    public static final String DATE_FORMAT5 = "yyyy-MM-dd HH:mm:ss";

    public static SimpleDateFormat getDateFormat5() {
        return new SimpleDateFormat(DATE_FORMAT5, Locale.getDefault());
    }

    /**
     * Return the friendly time span by now.
     *
     * @param millis The milliseconds.
     * @return the friendly time span by now
     * <ul>
     * <li>如果小于 1 秒钟内，显示刚刚</li>
     * <li>如果在 1 分钟内，显示 XXX秒前</li>
     * <li>如果在 1 小时内，显示 XXX分钟前</li>
     * <li>如果在 1 小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(final long millis) {
//        // 本地时间：毫秒
//        long now = System.currentTimeMillis();
//        // 接收或发送消息时间：1539847880026
//        long span = now - millis;
//        if (span < 0) {
//            // U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
////            return String.format("%tc", millis);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//            return simpleDateFormat.format(new Date(millis));
//        }
//        if (span < 1000) {
//            return Utils.getApp().getResources().getString(R.string.just_now);
//        } else if (span < TimeConstants.MIN) {
//            long sec = span / TimeConstants.SEC;
//            return String.format(Locale.getDefault(), Utils.getApp().getResources().getString(R.string.sec_before), sec);
//        } else if (span < TimeConstants.HOUR) {
//            long min = span / TimeConstants.MIN;
//            return String.format(Locale.getDefault(), Utils.getApp().getResources().getString(R.string.min_before), min);
//        }
//        // 获取当天 00:00
//        long wee = getWeeOfToday();
//        if (millis >= wee) {
//            return String.format(Utils.getApp().getResources().getString(R.string.time_today), millis);
//        } else if (millis >= wee - TimeConstants.DAY) {
//            return String.format(Utils.getApp().getResources().getString(R.string.time_yesterday), millis);
//        } else {
//            return String.format("%tF", millis);
//        }
        return "";
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
