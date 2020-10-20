package com.mfinance.everjoy.everjoy.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.graphics.ColorUtils;

/**
 * 状态栏、底部导航栏
 */
public class FullStatusUtils {

    private static final String TAG_STATUS_BAR = "TAG_STATUS_BAR";
    private static final String TAG_OFFSET = "TAG_OFFSET";
    private static final int KEY_OFFSET = -123;

    /**
     * 设置状态栏为透明
     *
     * @param viewBar 靠近状态栏的view布局
     */
    public static void setStatusBarTan(Activity activity, View viewBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        ViewGroup.LayoutParams params = viewBar.getLayoutParams();
        params.height = getStatusBarHeight();
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 设置StatusBar是否隐藏
     *
     * @param isVisible true隐藏
     */
    public static void setStatusBarVisibility(Activity activity, final boolean isVisible) {
        setStatusBarVisibility(activity.getWindow(), isVisible);
    }

    private static void setStatusBarVisibility(Window window, final boolean isVisible) {
        if (isVisible) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            showStatusBarView(window);
            addMarginTopEqualStatusBarHeight(window);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            hideStatusBarView(window);
            subtractMarginTopEqualStatusBarHeight(window);
        }
    }

    private static void addMarginTopEqualStatusBarHeight(final Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        View withTag = window.getDecorView().findViewWithTag(TAG_OFFSET);
        if (withTag == null) return;
        addMarginTopEqualStatusBarHeight(withTag);
    }

    private static void addMarginTopEqualStatusBarHeight(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        view.setTag(TAG_OFFSET);
        Object haveSetOffset = view.getTag(KEY_OFFSET);
        if (haveSetOffset != null && (Boolean) haveSetOffset) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin + getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin);
        view.setTag(KEY_OFFSET, true);
    }

    private static void showStatusBarView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_STATUS_BAR);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.VISIBLE);
    }

    private static void hideStatusBarView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_STATUS_BAR);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.GONE);
    }

    private static void subtractMarginTopEqualStatusBarHeight(final Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        View withTag = window.getDecorView().findViewWithTag(TAG_OFFSET);
        if (withTag == null) return;
        subtractMarginTopEqualStatusBarHeight(withTag);
    }

    public static void subtractMarginTopEqualStatusBarHeight(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        Object haveSetOffset = view.getTag(KEY_OFFSET);
        if (haveSetOffset == null || !(Boolean) haveSetOffset) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin - getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin);
        view.setTag(KEY_OFFSET, false);
    }

    /**
     * 设置底部导航栏颜色为黑色
     */
    public static void setNavBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(Color.parseColor("#000000"));
        }
    }

    /**
     * 设置底部导航栏是否显示
     *
     * @param isVisible true显示
     */
    public static void setNavBarVisibility(Activity activity, boolean isVisible) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = activity.getResources()
                        .getResourceEntryName(id);
                if ("navigationBarBackground".equals(resourceEntryName)) {
                    child.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
                }
            }
        }
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (isVisible) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~uiOptions);
        } else {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);
        }
    }

    /**
     * Android 6.0 以上设置状态栏颜色
     * 状态栏设置白色，则字体设置黑色
     * 状态栏设置黑色，则字体设置白色
     */
    public static void setStatusBarText(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            // 设置状态栏底色颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
            // 如果亮色，设置状态栏文字为黑色
            if (isLightColor(color)) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * 判断颜色是不是亮色
     * <p>
     * https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    private static boolean isLightColor(int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }
}
