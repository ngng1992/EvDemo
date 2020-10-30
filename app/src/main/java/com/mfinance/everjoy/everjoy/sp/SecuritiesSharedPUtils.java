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

    // ============================= step1 编辑资料 个人资料 ============================= //

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
        return SPUTILS.getInt("persNationality", 3);
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
        return SPUTILS.getInt("persBandAccRegion", 3);
    }

    // ============================= step1 编辑资料 联系方式-学历及工作资料 ============================= //

    // 电邮
    public static void setPersEmailn(String persEmail) {
        SPUTILS.put("persEmail", persEmail);
    }

    public static String getPersEmail() {
        return SPUTILS.getString("persEmail", "");
    }

    // 电话
    public static void setPersTelNo(String persTelNo) {
        SPUTILS.put("persTelNo", persTelNo);
    }

    public static String getPersTelNo() {
        return SPUTILS.getString("persTelNo", "");
    }

    // 住址
    public static void setPersAddress(String persAddress) {
        SPUTILS.put("persAddress", persAddress);
    }

    public static String getPersAddress() {
        return SPUTILS.getString("persAddress", "");
    }

    // 通讯地址
    public static void setPersMailAddress(String persMailAddress) {
        SPUTILS.put("persMailAddress", persMailAddress);
    }

    public static String getPersMailAddress() {
        return SPUTILS.getString("persMailAddress", "");
    }

    // 教育程度
    public static void setPersEducationLevel(int persEducationLevel) {
        SPUTILS.put("persEducationLevel", persEducationLevel);
    }

    public static int getPersEducationLevel() {
        return SPUTILS.getInt("persEducationLevel", -1);
    }

    // 教育程度-其他
    public static void setPersEducationLevelOther(String persEducationLevelOther) {
        SPUTILS.put("persEducationLevelOther", persEducationLevelOther);
    }

    public static String getPersEducationLevelOther() {
        return SPUTILS.getString("persEducationLevelOther", "");
    }

    // 工作状况
    public static void setWorkState(int workState) {
        SPUTILS.put("workState", workState);
    }

    public static int getWorkState() {
        return SPUTILS.getInt("workState", 0);
    }

    // 雇主名称
    public static void setWorkEmployerName(String workEmployerName) {
        SPUTILS.put("workEmployerName", workEmployerName);
    }

    public static String getWorkEmployerName() {
        return SPUTILS.getString("workEmployerName", "");
    }

    // 雇主地址
    public static void setWorkAddress(String workAddress) {
        SPUTILS.put("workAddress", workAddress);
    }

    public static String getWorkAddress() {
        return SPUTILS.getString("workAddress", "");
    }

    // 公司电话
    public static void setWorkTel(String workTel) {
        SPUTILS.put("workTel", workTel);
    }

    public static String getWorkTel() {
        return SPUTILS.getString("workTel", "");
    }

    // 业务性质
    public static void setWorkType(int workType) {
        SPUTILS.put("workType", workType);
    }

    public static int getWorkType() {
        return SPUTILS.getInt("workType", -1);
    }

    // 职业
    public static void setWorkPos(int workPos) {
        SPUTILS.put("workPos", workPos);
    }

    public static int getWorkPos() {
        return SPUTILS.getInt("workPos", -1);
    }

    // 工作年资
    public static void setWorkYeas(int workYear) {
        SPUTILS.put("workYear", workYear);
    }

    public static int getWorkYear() {
        return SPUTILS.getInt("workYear", -1);
    }

    // ============================= step1 编辑资料 录制视频 ============================= //
    // 视频拍摄路径
    public static void setVideoPath(String videoPath) {
        SPUTILS.put("videoPath", videoPath);
    }

    public static String getVideoPath() {
        return SPUTILS.getString("videoPath", "");
    }

    // 视频拍摄文件名称
    public static void setVideoName(String videoName) {
        SPUTILS.put("videoName", videoName);
    }

    public static String getVideoName() {
        return SPUTILS.getString("videoName", "");
    }

    // 香港居民身份证正面
    public static void setImgHkIdFront(String imgHkIdFront) {
        SPUTILS.put("imgHkIdFront", imgHkIdFront);
    }

    public static String getImgHkIdFront() {
        return SPUTILS.getString("imgHkIdFront", "");
    }

    // 香港居民身份证正面名称
    public static void setImgHkIdFrontFileName(String imgHkIdFrontFileName) {
        SPUTILS.put("imgHkIdFrontFileName", imgHkIdFrontFileName);
    }

    public static String getImgHkIdFrontFileName() {
        return SPUTILS.getString("imgHkIdFrontFileName", "");
    }

    // 香港居民身份证反面
    public static void setImgHkIdBack(String imgHkIdBack) {
        SPUTILS.put("imgHkIdBack", imgHkIdBack);
    }

    public static String getImgHkIdBack() {
        return SPUTILS.getString("imgHkIdBack", "");
    }

    // 香港居民身份证反面名称
    public static void setImgHkIdBackFileName(String imgHkIdBackFileName) {
        SPUTILS.put("imgHkIdBackFileName", imgHkIdBackFileName);
    }

    public static String getImgHkIdBackFileName() {
        return SPUTILS.getString("imgHkIdBackFileName", "");
    }

    // 大陆居民身份证正面
    public static void setImgMainlandIdFront(String imgMainlandIdFront) {
        SPUTILS.put("imgMainlandIdFront", imgMainlandIdFront);
    }

    public static String getImgMainlandIdFront() {
        return SPUTILS.getString("imgMainlandIdFront", "");
    }

    // 大陆居民身份证正面名称
    public static void setImgMainlandIdFrontFileName(String imgMainlandIdFrontFileName) {
        SPUTILS.put("imgMainlandIdFrontFileName", imgMainlandIdFrontFileName);
    }

    public static String getImgMainlandIdFrontFileName() {
        return SPUTILS.getString("imgMainlandIdFrontFileName", "");
    }

    // 大陆居民身份证反面
    public static void setImgMainlandIdBack(String imgMainlandIdBack) {
        SPUTILS.put("imgMainlandIdBack", imgMainlandIdBack);
    }

    public static String getImgMainlandIdBack() {
        return SPUTILS.getString("imgMainlandIdBack", "");
    }

    // 大陆居民身份证反面名称
    public static void setImgMainlandIdBackFileName(String imgMainlandIdBackFileName) {
        SPUTILS.put("imgMainlandIdBackFileName", imgMainlandIdBackFileName);
    }

    public static String getImgMainlandIdBackFileName() {
        return SPUTILS.getString("imgMainlandIdBackFileName", "");
    }

    // 护照
    public static void setImgPassport(String imgPassport) {
        SPUTILS.put("imgPassport", imgPassport);
    }

    public static String getImgPassport() {
        return SPUTILS.getString("imgPassport", "");
    }

    // 护照名称
    public static void setImgPassportFileName(String imgPassportFileName) {
        SPUTILS.put("imgPassportFileName", imgPassportFileName);
    }

    public static String getImgPassportFileName() {
        return SPUTILS.getString("imgPassportFileName", "");
    }

    // 港澳通行证
    public static void setImgHkMacauPass(String imgHkMacauPass) {
        SPUTILS.put("imgHkMacauPass", imgHkMacauPass);
    }

    public static String getImgHkMacauPass() {
        return SPUTILS.getString("imgHkMacauPass", "");
    }

    // 港澳通行证名称
    public static void setImgHkMacauPassFileName(String imgHkMacauPassFileName) {
        SPUTILS.put("imgHkMacauPassFileName", imgHkMacauPassFileName);
    }

    public static String getImgHkMacauPassFileName() {
        return SPUTILS.getString("imgHkMacauPassFileName", "");
    }

    // 住址
    public static void setImgProofAddress(String imgProofAddress) {
        SPUTILS.put("imgProofAddress", imgProofAddress);
    }

    public static String getImgProofAddress() {
        return SPUTILS.getString("imgProofAddress", "");
    }

    // 住址名称
    public static void setImgProofAddressFileName(String imgProofAddressFileName) {
        SPUTILS.put("imgProofAddressFileName", imgProofAddressFileName);
    }

    public static String getImgProofAddressFileName() {
        return SPUTILS.getString("imgProofAddressFileName", "");
    }

}
