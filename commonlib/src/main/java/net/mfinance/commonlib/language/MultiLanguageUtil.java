package net.mfinance.commonlib.language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

import androidx.annotation.RequiresApi;

/**
 * 多语言切换的帮助类
 *
 * http://blog.csdn.net/finddreams
 *
 * https://github.com/finddreams/AndroidMultiLanguage
 *
 */
public class MultiLanguageUtil {

    private static final String TAG = "MultiLanguageUtil";
    private static MultiLanguageUtil instance;
    private Context mContext;
    public static final String SAVE_LANGUAGE = "save_language";

    public static void init(Context mContext, int language) {
        // 保存一个默认语言
        CommSharedUtil.getInstance(mContext).putInt(MultiLanguageUtil.SAVE_LANGUAGE, language);
        if (instance == null) {
            synchronized (MultiLanguageUtil.class) {
                if (instance == null) {
                    instance = new MultiLanguageUtil(mContext);
                }
            }
        }
    }

    public static MultiLanguageUtil getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You must be init MultiLanguageUtil first");
        }
        return instance;
    }

    private MultiLanguageUtil(Context context) {
        this.mContext = context;
    }

    /**
     * 设置语言
     */
    public void setConfiguration() {
        Locale targetLocale = getLanguageLocale();
        Configuration configuration = mContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
        } else {
            configuration.locale = targetLocale;
        }
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }

    /**
     * 如果不是英文、简体中文、繁体中文，默认返回英文
     */
    private Locale getLanguageLocale() {
        int languageType = CommSharedUtil.getInstance(mContext).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_FOLLOW_SYSTEM) {
            return getSysLocale();
        } else if (languageType == LanguageType.LANGUAGE_EN) {
            return Locale.ENGLISH;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            return Locale.TRADITIONAL_CHINESE;
        }
        getSystemLanguage(getSysLocale());
        Log.e(TAG, "getLanguageLocale = " + languageType);
        return Locale.ENGLISH;
    }

    private String getSystemLanguage(Locale locale) {
//        return locale.getLanguage() + "btn_pause" + locale.getCountry();
        return locale.getLanguage() + locale.getCountry();

    }

    //以上获取方式需要特殊处理一下
    public Locale getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * 更新语言
     */
    public void updateLanguage(int languageType) {
        CommSharedUtil.getInstance(mContext).putInt(MultiLanguageUtil.SAVE_LANGUAGE, languageType);
        MultiLanguageUtil.getInstance().setConfiguration();
        // TODO 语言改变了 可通知其他activity
//        EventBus.getDefault().post(new OnChangeLanguageEvent(languageType));
    }

    /**
     * 获取到用户保存的语言类型
     */
    public int getLanguageType() {
        int languageType = CommSharedUtil.getInstance(mContext).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_CHINESE_SIMPLIFIED);
        Log.e(TAG, "getLanguageType" + languageType);
        return languageType;
    }

    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context);
        } else {
            MultiLanguageUtil.getInstance().setConfiguration();
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getInstance().getLanguageLocale();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }


    // ==============================================================================


    /**
     * 显示toast时，改变context的语言环境
     *
     * @param context 某个activity
     */
    public static Context showToastChangeContext(Context context) {
        // 8.0及以上使用createConfigurationContext设置configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return updateResources(context);
        } else {
            return context;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Context updateResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(getLocale(context));
        configuration.setLocales(new LocaleList(getLocale(context)));
        return context.createConfigurationContext(configuration);
    }

    private static Locale getLocale(Context context) {
        // 系统语言
        int languageType = CommSharedUtil.getInstance(context).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        Locale locale;
        if (languageType == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = LocaleList.getDefault().get(0);
            } else {
                locale = Locale.getDefault();
            }
        } else {
            if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if (languageType == LanguageType.LANGUAGE_EN) {
                locale = Locale.ENGLISH;
            } else {
                locale = Locale.SIMPLIFIED_CHINESE;
            }
        }
        return locale;
    }
}
