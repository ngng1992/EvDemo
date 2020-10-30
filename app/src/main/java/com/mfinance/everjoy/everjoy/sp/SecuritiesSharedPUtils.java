package com.mfinance.everjoy.everjoy.sp;

import com.blankj.utilcode.util.SPUtils;

/**
 * 证券开户的资料填写保存在本地
 * 如果提交成功，清空
 * 如果未提交，或提交失败，不清空，以备下次直接使用
 * <p>
 * key为控件的id
 * value为选择项或选择项的内容
 */
public class SecuritiesSharedPUtils {

    /**
     * 全局sp对象，app的若干设置
     */
    private static SPUtils SPUTILS;

    static {
        SPUTILS = SPUtils.getInstance("securities_app");
    }

    // ============================= step1 编辑资料 ============================= //

    // 称谓
    public static void setPersNameTitle(int persNameTitle) {
        SPUTILS.put("persNameTitle", persNameTitle);
    }
    public static int getPersNameTitle() {
        return SPUTILS.getInt("persNameTitle", -1);
    }

    // 中文名称
    public static void setPersNameChinese(String persNameChinese) {
        SPUTILS.put("persNameChinese", persNameChinese);
    }
    public static String getPersNameChinese() {
        return SPUTILS.getString("persNameChinese", "");
    }

    // 英文文名称
    public static void setPersNameEnglish(String persNameEnglish) {
        SPUTILS.put("persNameEnglish", persNameEnglish);
    }
    public static String getPersNameEnglish() {
        return SPUTILS.getString("persNameEnglish", "");
    }

    // 出生地区
    public static void setPersBirthRegion(int persBirthRegion) {
        SPUTILS.put("persBirthRegion", persBirthRegion);
    }
    public static int getPersBirthRegion() {
        return SPUTILS.getInt("persBirthRegion", 3);
    }

    // 出生日期
    public static void setPersBirthDate(long persBirthDate) {
        SPUTILS.put("persBirthDate", persBirthDate);
    }
    public static long getPersBirthDate() {
        return SPUTILS.getLong("persBirthDate", 0L);
    }

    // 婚姻
    public static void setPersMaritalStatus(int persMaritalStatus) {
        SPUTILS.put("persMaritalStatus", persMaritalStatus);
    }
    public static int getPersMaritalStatus() {
        return SPUTILS.getInt("persMaritalStatus", -1);
    }

    // 性别
    public static void setPersGender(int persGender) {
        SPUTILS.put("persGender", persGender);
    }
    public static int getPersGender() {
        return SPUTILS.getInt("persGender", -1);
    }

    // 国籍地区
    public static void setPersNationality(int persNationality) {
        SPUTILS.put("persNationality", persNationality);
    }
    public static int getPersNationalit() {
        return SPUTILS.getInt("persNationality", -1);
    }

    // 证件类型
    public static void setPersIdType(int persIdType) {
        SPUTILS.put("persIdType", persIdType);
    }
    public static int getPersIdType() {
        return SPUTILS.getInt("persIdType", -1);
    }

    // 证件号码
    public static void setPersIdNo(String persIdNo) {
        SPUTILS.put("persIdNo", persIdNo);
    }
    public static String getPersIdNo() {
        return SPUTILS.getString("persIdNo", "");
    }

    // 银行账户所属地区
    public static void setPersBandAccRegion(int persBandAccRegion) {
        SPUTILS.put("persBandAccRegion", persBandAccRegion);
    }
    public static int getPersBandAccRegion() {
        return SPUTILS.getInt("persBandAccRegion", -1);
    }
}
