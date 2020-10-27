package net.mfinance.commonlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import net.mfinance.commonlib.R;


/**
 * 底部线条滑动块
 */
public class LineStepView extends LinearLayout {

    private int lineCount;
    private int lineWidth;
    private int lineHeight;
    private int linePadding;
    // 线条未选中时的透明度
    private float lineUnSelectAlpha;
    private int lineUnSelectBgColor;
    private int lineSelectBgColor;
    private int selectIndex;

    public LineStepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LineStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineStepView);
        lineCount = a.getInt(R.styleable.LineStepView_lineCount, 4);
        lineWidth = (int) a.getDimension(R.styleable.LineStepView_lineWidth, 20);
        lineHeight = (int) a.getDimension(R.styleable.LineStepView_lineHeight, 2);
        linePadding = (int) a.getDimension(R.styleable.LineStepView_linePadding, 8);
        lineUnSelectAlpha = a.getFloat(R.styleable.LineStepView_lineUnSelectAlpha, 0.3f);
        lineUnSelectBgColor = a.getColor(R.styleable.LineStepView_lineUnSelectBgColor, Color.GRAY);
        lineSelectBgColor = a.getColor(R.styleable.LineStepView_lineSelectBgColor, Color.WHITE);
        selectIndex = a.getColor(R.styleable.LineStepView_selectIndex, 0);
        a.recycle();
        initView(context);
    }

    private void initView(Context context) {
        for (int i = 0; i < lineCount; i++) {
            View view = new View(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(lineWidth, lineHeight);
            layoutParams.bottomMargin = linePadding;
            layoutParams.topMargin = linePadding;
            layoutParams.leftMargin = linePadding;
            layoutParams.rightMargin = linePadding;
            layoutParams.gravity = Gravity.CENTER;
            view.setBackgroundColor(lineSelectBgColor);
            view.setLayoutParams(layoutParams);
            addView(view);
        }
        scrollStep(selectIndex);
    }

    /**
     * 滚动到第几个位置了
     */
    public void scrollStep(int index) {
        for (int i = 0; i < lineCount; i++) {
            View view = getChildAt(i);
            if (lineUnSelectAlpha == 0) {
                if (i == index) {
                    view.setBackgroundColor(lineSelectBgColor);
                } else {
                    view.setBackgroundColor(lineUnSelectBgColor);
                }
            }else {
                if (i == index) {
                    view.setAlpha(1f);
                } else {
                    view.setAlpha(lineUnSelectAlpha);
                }
            }
        }
    }
}
