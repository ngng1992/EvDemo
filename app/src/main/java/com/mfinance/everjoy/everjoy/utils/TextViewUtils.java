package com.mfinance.everjoy.everjoy.utils;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.TextView;

/**
 * TextView设置图片和取消图片
 */

public class TextViewUtils {

    /**
     * 中划线
     *
     * @param textView tv
     */
    public static void paintText(TextView textView) {
        TextPaint paint = textView.getPaint();
        // 抗锯齿
        paint.setAntiAlias(true);
        paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 下划线
     */
    public static void paintUnderLine(TextView textView) {
        TextPaint paint = textView.getPaint();
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        paint.setAntiAlias(true);
    }

    /**
     * 取消TextView的Drawable
     *
     * @param textView tv
     */
    public static void drawableTextCancel(TextView textView) {
        textView.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 设置TextView的Drawable
     *
     * @param textView tv
     */
    public static void drawableText(TextView textView, int gravity, int resId) {
        if (textView == null) {
            return;
        }
        Drawable drawable = textView.getContext().getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (gravity == Gravity.TOP) {
            textView.setCompoundDrawables(null, drawable, null, null);
        } else if (gravity == Gravity.BOTTOM) {
            textView.setCompoundDrawables(null, null, null, drawable);
        } else if (gravity == Gravity.START) {
            textView.setCompoundDrawables(drawable, null, null, null);
        } else if (gravity == Gravity.END) {
            textView.setCompoundDrawables(null, null, drawable, null);
        }
    }
}
