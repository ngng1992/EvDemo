package com.mfinance.everjoy.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleUtility {
    @TargetApi(Build.VERSION_CODES.N)
    public static Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    public static Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    public static Locale getLanguage(SharedPreferences setting) {
        String sLocale;
        if (setting == null) {
            sLocale = getDeviceLanguage();
        } else {
            sLocale = setting.getString("LANGUAGE", getDeviceLanguage());
        }
        //System.out.println(sLocale);

        if(sLocale.equals("简体")){
            return Locale.SIMPLIFIED_CHINESE;
        }else if(sLocale.equals("繁體")){
            return Locale.TRADITIONAL_CHINESE;
        }else
            return Locale.ENGLISH;
    }

    private static Boolean isDeviceTraditionalChinese(){
        return Locale.getDefault().equals(Locale.TRADITIONAL_CHINESE) || Locale.getDefault().equals(Locale.TAIWAN) ||
                Locale.getDefault().getCountry().equals("HK");
    }

    private static Boolean isDeviceSimplifiedChinese(){
        return Locale.getDefault().equals(Locale.SIMPLIFIED_CHINESE) || Locale.getDefault().equals(Locale.PRC);
    }

    public static String getDeviceLanguage(){
        if(CompanySettings.DEFAULT_LANGUAGE!=null)
            return CompanySettings.DEFAULT_LANGUAGE;
        if (isDeviceTraditionalChinese())
            return "繁體";
        else if (isDeviceSimplifiedChinese())
            return "简体";
        else
            return "English";
    }
}
