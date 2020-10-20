package com.mfinance.everjoy.everjoy.sp;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;


/**
 * 保存到sp的数据
 */
public class AppSharedPUtils {

    /**
     * 全局sp对象，app的若干设置
     */
    private static SPUtils SPUTILS = null;

    /**
     * 调用保存方法时，必须先调用init
     */
    public static void init() {
        SPUTILS = SPUtils.getInstance("sp_app");
    }

    /**
     * 1. 是否记住了通知栏权限，不在提示
     */
    public static boolean isNotifyTip() {
        return SPUTILS.getBoolean("is_notify_tip", false);
    }

    public static void setIsNotifyTip(boolean isNotifyTip) {
        SPUTILS.put("is_notify_tip", isNotifyTip);
    }

    /**
     * 4. 语言
     */
    public static String getLanguage() {
        return SPUTILS.getString("language", "SC");
    }

    public static void setLanguage(String language) {
        SPUTILS.put("language", language);
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
