package com.mfinance.everjoy.everjoy.sp;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.mfinance.everjoy.everjoy.config.Constants;
import com.mfinance.everjoy.everjoy.utils.LanguageSettingUtil;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;


/**
 * 保存到sp的数据
 */
public class AppSharedPUtils {

    /**
     * 全局sp对象，app的若干设置
     */
    private static SPUtils SPUTILS;

    static {
        SPUTILS = SPUtils.getInstance("sp_app");
    }

    /**
     * 是否记住了通知栏权限，不在提示
     */
    public static boolean isNotifyTip() {
        return SPUTILS.getBoolean("is_notify_tip", false);
    }
    public static void setIsNotifyTip(boolean isNotifyTip) {
        SPUTILS.put("is_notify_tip", isNotifyTip);
    }

    /**
     * 默认App系统的语言
     */
    public static String getUserLanguage() {
        return SPUtils.getInstance().getString(Constants.LANGUAGE, LanguageSettingUtil.getLanguage());
    }
    public static void setUserLanguage(String language) {
        SPUtils.getInstance().put(Constants.LANGUAGE, language);
    }


    /**
     * 是否设置了推送通知，默认开启
     */
    public static boolean isPushNotification() {
        // 默认是app是否有通知栏权限
        return SPUTILS.getBoolean("push_notification", ToolsUtils.isNotificationEnabled(Utils.getApp()));
    }
    public static void setPushNotification(boolean isNotifyPermission) {
        SPUTILS.put("push_notification", isNotifyPermission);
    }

    /**
     * 是否设置了推送提示音效
     */
    public static boolean isLoudspeakerMute() {
        return SPUTILS.getBoolean("loudspeaker_mute", true);
    }
    public static void setLoudspeakerMute(boolean isLoudspeakerMute) {
        SPUTILS.put("loudspeaker_mute", isLoudspeakerMute);
    }


}
