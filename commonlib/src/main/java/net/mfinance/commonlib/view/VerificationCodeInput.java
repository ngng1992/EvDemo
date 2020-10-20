/*
 * Copyright (C) 2013 UCWeb Inc. All rights reserved
 * 本代码版权归UC优视科技所有。
 * UC游戏交易平台为优视科技（UC）旗下的手机游戏交易平台产品
 *
 *
 */

package net.mfinance.commonlib.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;

import net.mfinance.commonlib.R;

import java.lang.reflect.Field;

/**
 * 自定义验证码输入框
 * 具体看 https://github.com/liuguangli/VerificationCodeInput}
 * TODO 3处地方修改 以下代码是修改背景色后的代码
 */
public class VerificationCodeInput extends ViewGroup {

    private final static String TYPE_NUMBER = "number";
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_PASSWORD = "password";
    private final static String TYPE_PHONE = "phone";

    private static final String TAG = "VerificationCodeInput";
    private int box = 4;
    private int boxWidth = 120;
    private int boxHeight = 120;
    private int childHPadding = 14;
    private int childVPadding = 14;
    private String inputType = TYPE_PASSWORD;
    private boolean isNeedBgLine = true;
    private int lineColor = Color.BLACK;
    private float txtSize = 24f;
    private int txtColor = Color.BLACK;
    private Drawable boxBgFocus = null;
    private Drawable boxBgNormal = null;
    private OnCompleteListener listener;


    public VerificationCodeInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInput);
        box = a.getInt(R.styleable.VerificationCodeInput_box, 4);
        childHPadding = (int) a.getDimension(R.styleable.VerificationCodeInput_child_h_padding, 0);
        childVPadding = (int) a.getDimension(R.styleable.VerificationCodeInput_child_v_padding, 0);
        boxBgFocus = a.getDrawable(R.styleable.VerificationCodeInput_box_bg_focus);
        boxBgNormal = a.getDrawable(R.styleable.VerificationCodeInput_box_bg_normal);
        inputType = a.getString(R.styleable.VerificationCodeInput_inputType);
        isNeedBgLine = a.getBoolean(R.styleable.VerificationCodeInput_isNeedBgLine, true);
        lineColor = a.getColor(R.styleable.VerificationCodeInput_lineColor, Color.BLACK);
        txtSize = a.getFloat(R.styleable.VerificationCodeInput_txtSize, 24f);
        txtColor = a.getColor(R.styleable.VerificationCodeInput_txtColor, Color.BLACK);
        // TODO 这里设置屏幕的4分之一，高度是宽度的2分之一，可以容纳20sp的字体大小
        int width = getAppScreenWidth() / box;
        boxWidth = (int) a.getDimension(R.styleable.VerificationCodeInput_child_width, width);
        boxHeight = (int) a.getDimension(R.styleable.VerificationCodeInput_child_height, width / 2);
        a.recycle();
        initViews(context);
    }

    private int getAppScreenWidth() {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();


    }


    private void initViews(Context context) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                } else {
                    focus();
                    checkAndCommit();
                }

            }

        };


        OnKeyListener onKeyListener = new OnKeyListener() {
            @Override
            public synchronized boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    backFocus();
                }
                return false;
            }
        };


        for (int i = 0; i < box; i++) {
            if (isNeedBgLine) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                LinearLayout.LayoutParams llLinearLayoutLayoutParams =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(llLinearLayoutLayoutParams);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                EditText editText = new EditText(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(boxWidth, boxHeight);
                layoutParams.bottomMargin = childVPadding;
                layoutParams.topMargin = childVPadding;
                layoutParams.leftMargin = childHPadding;
                layoutParams.rightMargin = childHPadding;
                layoutParams.gravity = Gravity.CENTER;

                editText.setOnKeyListener(onKeyListener);
                setBg(editText, false);
                // TODO 设置光标的颜色，与下标的线条颜色一致，设置背景色之后调用
                try {
                    Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(editText, R.drawable.edit_cursor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editText.setTextColor(txtColor);
                editText.setTextSize(txtSize);
                editText.setLayoutParams(layoutParams);
                editText.setGravity(Gravity.CENTER);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

                if (TYPE_NUMBER.equals(inputType)) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (TYPE_PASSWORD.equals(inputType)) {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else if (TYPE_TEXT.equals(inputType)) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (TYPE_PHONE.equals(inputType)) {
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);

                }
                editText.setId(i);
                editText.setEms(1);
                editText.addTextChangedListener(textWatcher);

                View view = new View(getContext());
                // 与et宽度相等，高度一般不高
                LinearLayout.LayoutParams viewLayoutParams = new LinearLayout.LayoutParams(boxWidth, 2);
                viewLayoutParams.bottomMargin = childVPadding;
                viewLayoutParams.topMargin = childVPadding;
                viewLayoutParams.leftMargin = childHPadding;
                viewLayoutParams.rightMargin = childHPadding;
                viewLayoutParams.gravity = Gravity.CENTER;
                view.setLayoutParams(viewLayoutParams);
                view.setBackgroundColor(lineColor);

                linearLayout.addView(editText);
                linearLayout.addView(view);

                addView(linearLayout, i);
            }else {
                EditText editText = new EditText(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(boxWidth, boxHeight);
                layoutParams.bottomMargin = childVPadding;
                layoutParams.topMargin = childVPadding;
                layoutParams.leftMargin = childHPadding;
                layoutParams.rightMargin = childHPadding;
                layoutParams.gravity = Gravity.CENTER;

                editText.setOnKeyListener(onKeyListener);
                setBg(editText, false);
                // TODO 设置光标的颜色，与下标的线条颜色一致，设置背景色之后调用
                try {
                    Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(editText, R.drawable.edit_cursor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editText.setTextColor(txtColor);
                editText.setTextSize(txtSize);
                editText.setLayoutParams(layoutParams);
                editText.setGravity(Gravity.CENTER);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

                if (TYPE_NUMBER.equals(inputType)) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (TYPE_PASSWORD.equals(inputType)) {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else if (TYPE_TEXT.equals(inputType)) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (TYPE_PHONE.equals(inputType)) {
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);

                }
                editText.setId(i);
                editText.setEms(1);
                editText.addTextChangedListener(textWatcher);
                addView(editText, i);
            }
        }
    }

    private void backFocus() {
        int count = getChildCount();
        EditText editText;
        for (int i = count - 1; i >= 0; i--) {
            // 如果et设置的底部线条，这里需要修改
            if (isNeedBgLine) {
                LinearLayout linearLayout = (LinearLayout) getChildAt(i);
                editText = (EditText) linearLayout.getChildAt(0);
            }else {
                editText = (EditText) getChildAt(i);
            }
            if (editText.getText().length() == 1) {
                editText.requestFocus();
                editText.setSelection(1);
                return;
            }
        }
    }

    private void focus() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            // 如果et设置的底部线条，这里需要修改
            if (isNeedBgLine) {
                LinearLayout linearLayout = (LinearLayout) getChildAt(i);
                editText = (EditText) linearLayout.getChildAt(0);
            }else {
                editText = (EditText) getChildAt(i);
            }
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    private void setBg(EditText editText, boolean focus) {
        // TODO 不要背景
        if (isNeedBgLine) {
            editText.setBackground(null);
        } else {
            if (boxBgNormal != null && !focus) {
                editText.setBackground(boxBgNormal);
            } else if (boxBgFocus != null && focus) {
                editText.setBackground(boxBgFocus);
            }
        }
    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < box; i++) {
            EditText editText;
            if (isNeedBgLine) {
                LinearLayout linearLayout = (LinearLayout) getChildAt(i);
                editText = (EditText) linearLayout.getChildAt(0);
            }else {
                editText = (EditText) getChildAt(i);
            }
            String content = editText.getText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }

        }
        Log.d(TAG, "checkAndCommit:" + stringBuilder.toString());
        if (full) {

            if (listener != null) {
                listener.onComplete(stringBuilder.toString());
                setEnabled(false);
            }

        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        this.listener = listener;
    }

    @Override

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = getMeasuredWidth();
        if (parentWidth == LayoutParams.MATCH_PARENT) {
            parentWidth = getScreenWidth();
        }
        Log.d(getClass().getName(), "onMeasure width " + parentWidth);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        if (count > 0) {
            View child = getChildAt(0);
            int cWidth = child.getMeasuredWidth();
            if (parentWidth != LayoutParams.WRAP_CONTENT) {
                // 重新计算padding
                childHPadding = (parentWidth - cWidth * count) / (count + 1);
            }

            int cHeight = child.getMeasuredHeight();

            int maxH = cHeight + 2 * childVPadding;
            int maxW = (cWidth) * count + childHPadding * (count + 1);
            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec),
                    resolveSize(maxH, heightMeasureSpec));
        }


    }

    private int getScreenWidth() {

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        return dm.widthPixels;


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(getClass().getName(), "onLayout width = " + getMeasuredWidth());

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            child.setVisibility(View.VISIBLE);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int cl = childHPadding + (i) * (cWidth + childHPadding);
            int cr = cl + cWidth;
            int ct = childVPadding;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }


    }

    public int getBox() {
        return box;
    }

    public interface OnCompleteListener {
        void onComplete(String content);
    }

}

