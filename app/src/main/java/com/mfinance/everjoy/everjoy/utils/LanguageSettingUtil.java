package com.mfinance.everjoy.everjoy.utils;

import android.app.Application;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import com.mfinance.everjoy.everjoy.sp.AppSharedPUtils;

import net.mfinance.commonlib.language.MultiLanguageUtil;

import java.util.Locale;

/**
 * 语言设置
 */
public class LanguageSettingUtil {

    /**
     * 简体
     */
    public static final String SIMPLIFIED_CHINESE = "ZH_CN";
    /**
     * 繁体
     */
    public static final String TRADITIONAL_CHINESE = "ZH_TW";
    /**
     * 英语
     */
    public static final String ENGLISH = "EN";

    public static void init(Application application) {
        int selectedLanguage = 3;
        String userLanguage = AppSharedPUtils.getUserLanguage();
        if (userLanguage.equals(LanguageSettingUtil.ENGLISH)) {
            selectedLanguage = 1;
        } else if (userLanguage.equals(LanguageSettingUtil.SIMPLIFIED_CHINESE)) {
            selectedLanguage = 2;
        } else if (userLanguage.equals(LanguageSettingUtil.TRADITIONAL_CHINESE)) {
            selectedLanguage = 3;
        }
        // 工具集
        MultiLanguageUtil.init(application, selectedLanguage);
    }

    /**
     * 获取系统的默认语言，转成app设置的语言
     *
     * @return en、tc、sc
     */
    public static String getLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        // zhCN 和 zhHK/zhTW
        String language = (locale.getLanguage() + locale.getCountry()).toUpperCase();
        Log.e("lang", "手机系统默认语言 = " + language);
        if (language.contains("CN")) {
            language = SIMPLIFIED_CHINESE;
        } else if (language.contains("HK") || language.contains("TW")) {
            language = TRADITIONAL_CHINESE;
        } else {
            language = ENGLISH;
        }
        return language;
    }
}
