package net.mfinance.chatlib.utils;

import android.util.Log;

import java.text.DecimalFormat;

public class NumberPriceUtil {

    /**
     * 转成以万为单位
     * @param num 整数
     */
    public static String toThou(int num) {
        // 设置保留位数
        DecimalFormat df = new DecimalFormat("0.00");
        float pp = (float)num / 10000;
        String format = df.format(pp);
        Log.e("num", "format = " + format);
        String point = removePoint(format);
        return point;
    }

    /**
     * 去掉小数点后面的0   22.010 -> 22.01
     */
    public static String removePoint(String num) {
        if(num.indexOf(".") > 0){
            // 去掉后面无用的零
            num = num.replaceAll("0+?$", "");
            // 如小数点后面全是零则去掉小数点
            num = num.replaceAll("[.]$", "");
        }
        return num;
    }

}
