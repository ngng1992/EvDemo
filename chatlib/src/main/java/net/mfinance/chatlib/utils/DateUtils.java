package net.mfinance.chatlib.utils;

import android.content.Context;

import com.hyphenate.util.TimeInfo;
import net.mfinance.chatlib.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 消息时间计算
 */
public class DateUtils {

    /**
     * 时间间隔10分钟
     */
    private static final long INTERVAL_IN_MILLISECONDS = 10 * 60 * 1000;

    /**
     * 当前时间格式化
     * @param context 上下文
     * @param messageDate date日期
     * @return str时间格式
     */
    public static String getTimestampString(Context context, Date messageDate) {
        Locale curLocale = context.getResources().getConfiguration().locale;
        String languageCode = curLocale.getLanguage();
        boolean isChinese = languageCode.contains("zh");
        String format;
        long messageTime = messageDate.getTime();
        if (isSameDay(messageTime)) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(messageDate);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            format = "HH:mm";
            if (hour > 17) {                
                if(isChinese){
                    format = context.getString(R.string.date_night);
                }
            }else if(hour >= 0 && hour <= 6){                
                if(isChinese){
                    format = context.getString(R.string.date_wee_hours);
                }
            } else if (hour > 11) {
                if(isChinese){
                    format = context.getString(R.string.date_afternoon);
                }
            } else {
                if(isChinese){
                    format = context.getString(R.string.date_forenoon);
                }
            }
        } else if (isYesterday(messageTime)) {
            if(isChinese){
                format = context.getString(R.string.date_yesterday);
            }else{
                format = "MM-dd HH:mm";
            }
        } else {
            if(isChinese){
                format = context.getString(R.string.date_month_day);
            }else{
                format = "MM-dd HH:mm";
            }
        }
        if(isChinese){
            return new SimpleDateFormat(format, Locale.CHINA).format(messageDate);
        }else{
            return new SimpleDateFormat(format, Locale.US).format(messageDate);
        }
    }

    /**
     * 两者的时间小于30s
     */
    public static boolean isCloseEnough(long time1, long time2) {
        long delta = time1 - time2;
        if (delta < 0) {
            delta = -delta;
        }
        return delta < INTERVAL_IN_MILLISECONDS;
    }

    private static boolean isSameDay(long inputTime) {
        TimeInfo tStartAndEndTime = getTodayStartAndEndTime();
        return inputTime > tStartAndEndTime.getStartTime() && inputTime < tStartAndEndTime.getEndTime();
    }

    private static boolean isYesterday(long inputTime) {
        TimeInfo yStartAndEndTime = getYesterdayStartAndEndTime();
        return inputTime > yStartAndEndTime.getStartTime() && inputTime < yStartAndEndTime.getEndTime();
    }

    private static TimeInfo getYesterdayStartAndEndTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, -1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, -1);
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    private static TimeInfo getTodayStartAndEndTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}