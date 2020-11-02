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

    // ============================= step1 编辑资料 录制视频 证件上传 ============================= //

    // 视频拍摄路径
    public static void setVidSelfie(String videoPath) {
        SPUTILS.put("vidSelfie", videoPath);
    }

    public static String getVidSelfie() {
        return SPUTILS.getString("vidSelfie", "");
    }

    // 视频拍摄文件名称
    public static void setVideoName(String videoName) {
        SPUTILS.put("vidSelfieFileName", videoName);
    }

    public static String getVidSelfieFileName() {
        return SPUTILS.getString("vidSelfieFileName", "");
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



    // ============================= step1 编辑资料 财政收入-投资经验 ============================= //

    // 年收入
    public static void setFinYearRevenue(int finYearRevenueIndex) {
        SPUTILS.put("finYearRevenueIndex", finYearRevenueIndex);
    }
    public static int getFinYearRevenue() {
        return SPUTILS.getInt("finYearRevenueIndex", -1);
    }

    // 年收入
    public static void setFinNetAssetValueIndex(int finNetAssetValueIndex) {
        SPUTILS.put("finNetAssetValueIndex", finNetAssetValueIndex);
    }
    public static int getFinNetAssetValueIndex() {
        return SPUTILS.getInt("finNetAssetValueIndex", -1);
    }

    // 住宅类别
    public static void setFinResidTypeIndex(int finResidTypeIndex) {
        SPUTILS.put("finResidTypeIndex", finResidTypeIndex);
    }
    public static int getFinResidTypeIndex() {
        return SPUTILS.getInt("finResidTypeIndex", -1);
    }

    // 住宅类别其他
    public static void setFinResidTypeOther(String finResidTypeOther) {
        SPUTILS.put("finResidTypeOther", finResidTypeOther);
    }
    public static String getFinResidTypeOther() {
        return SPUTILS.getString("finResidTypeOther", "");
    }

    // 资金来源
    public static void setFinCapitalSourceIndex(int finCapitalSourceIndex) {
        SPUTILS.put("finCapitalSourceIndex", finCapitalSourceIndex);
    }
    public static int getFinCapitalSourceIndex() {
        return SPUTILS.getInt("finCapitalSourceIndex", -1);
    }

    // 资金来源其他
    public static void setFinCapitalSourceOther(String finCapitalSourceOther) {
        SPUTILS.put("finCapitalSourceOther", finCapitalSourceOther);
    }
    public static String getFinCapitalSourceOther() {
        return SPUTILS.getString("finCapitalSourceOther", "");
    }

    // 资产类别
    public static void setFinAssetTypeIndex(int finAssetTypeIndex) {
        SPUTILS.put("finAssetTypeIndex", finAssetTypeIndex);
    }
    public static int getFinAssetTypeIndex() {
        return SPUTILS.getInt("finAssetTypeIndex", -1);
    }

    // 资产类别其他
    public static void setFinAssetTypeOther(String finAssetTypeOther) {
        SPUTILS.put("finAssetTypeOther", finAssetTypeOther);
    }
    public static String getFinAssetTypeOther() {
        return SPUTILS.getString("finAssetTypeOther", "");
    }

    // 投资目标
    public static void setFinInvestTargetIndex(int finInvestTargetIndex) {
        SPUTILS.put("finInvestTargetIndex", finInvestTargetIndex);
    }
    public static int getFinInvestTargetIndex() {
        return SPUTILS.getInt("finInvestTargetIndex", -1);
    }

    // 投资目标其他
    public static void setFinInvestTargetOther(String finInvestTargetOther) {
        SPUTILS.put("finInvestTargetOther", finInvestTargetOther);
    }
    public static String getFinInvestTargetOther() {
        return SPUTILS.getString("finInvestTargetOther", "");
    }

    // 可承受风险
    public static void setFinTolerateRiskIndex(int finTolerateRiskIndex) {
        SPUTILS.put("finTolerateRiskIndex", finTolerateRiskIndex);
    }
    public static int getFinTolerateRiskIndex() {
        return SPUTILS.getInt("finTolerateRiskIndex", -1);
    }

    // 平均年投資金額
    public static void setFinInvestAvgYearValueIndex(int finInvestAvgYearValueIndex) {
        SPUTILS.put("finInvestAvgYearValueIndex", finInvestAvgYearValueIndex);
    }
    public static int getFinInvestAvgYearValueIndex() {
        return SPUTILS.getInt("finInvestAvgYearValueIndex", -1);
    }

    // 股票投资经验
    public static void setFinExpStockYearIndex(int finExpStockYearIndex) {
        SPUTILS.put("finExpStockYearIndex", finExpStockYearIndex);
    }
    public static int getFinExpStockYearIndex() {
        return SPUTILS.getInt("finExpStockYearIndex", -1);
    }

    // 衍生工具投资经验
    public static void setFinExpDerivativeYearIndex(int finExpDerivativeYearIndex) {
        SPUTILS.put("finExpDerivativeYearIndex", finExpDerivativeYearIndex);
    }
    public static int getFinExpDerivativeYearIndex() {
        return SPUTILS.getInt("finExpDerivativeYear", -1);
    }

    // 期货投资经验
    public static void setFinExpFutureYearIndex(int finExpFutureYearIndex) {
        SPUTILS.put("finExpFutureYearIndex", finExpFutureYearIndex);
    }
    public static int getFinExpFutureYearIndex() {
        return SPUTILS.getInt("finExpFutureYearIndex", -1);
    }

    // 外汇投资经验
    public static void setFinExpForexYearIndex(int finExpForexYearIndex) {
        SPUTILS.put("finExpForexYearIndex", finExpForexYearIndex);
    }
    public static int getFinExpForexYearIndex() {
        return SPUTILS.getInt("finExpFutureYearIndex", -1);
    }

    // 债券投资经验
    public static void setFinExpBondYearIndex(int finExpBondYearIndex) {
        SPUTILS.put("finExpBondYearIndex", finExpBondYearIndex);
    }
    public static int getFinExpBondYearIndex() {
        return SPUTILS.getInt("finExpBondYearIndex", -1);
    }

    // 基金投资经验
    public static void setFinExpFoundYearIndex(int finExpFoundYearIndex) {
        SPUTILS.put("finExpFoundYearIndex", finExpFoundYearIndex);
    }
    public static int getFinExpFoundYearIndex() {
        return SPUTILS.getInt("finExpFoundYearIndex", -1);
    }

    // 其他投资经验
    public static void setFinExpOtherYearIndex(int finExpOtherYearIndex) {
        SPUTILS.put("finExpOtherYearIndex", finExpOtherYearIndex);
    }
    public static int getFinExpOtherYearIndex() {
        return SPUTILS.getInt("finExpOtherYearIndex", -1);
    }


    // ============================= step2 选择账户 ============================= //

    // 账户
    public static void setCreateAccTypeIndex(int createAccTypeIndex) {
        SPUTILS.put("createAccTypeIndex", createAccTypeIndex);
    }
    public static int getCreateAccTypeIndex() {
        return SPUTILS.getInt("createAccTypeIndex", 0);
    }

    // 港股
    public static void setCreateAccStockHk(boolean createAccStockHk) {
        SPUTILS.put("createAccStockHk", createAccStockHk);
    }
    public static boolean getCreateAccStockHk() {
        return SPUTILS.getBoolean("createAccStockHk", true);
    }

    // 美股
    public static void setCreateAccStockUs(boolean createAccStockUs) {
        SPUTILS.put("createAccStockUs", createAccStockUs);
    }
    public static boolean getCreateAccStockUs() {
        return SPUTILS.getBoolean("createAccStockUs", true);
    }

    // A股
    public static void setCreateAccStockA(boolean createAccStockA) {
        SPUTILS.put("createAccStockA", createAccStockA);
    }
    public static boolean getCreateAccStockA() {
        return SPUTILS.getBoolean("createAccStockA", true);
    }

    // 税务信息
    public static void setTaxInfoIndex(int taxInfoIndex) {
        SPUTILS.put("taxInfoIndex", taxInfoIndex);
    }
    public static int getTaxInfoIndex() {
        return SPUTILS.getInt("taxInfoIndex", -1);
    }

    // 税务信息-国家地区
    public static void setTaxRegionIndex(int taxRegionIndex) {
        SPUTILS.put("taxRegionIndex", taxRegionIndex);
    }
    public static int getTaxRegionIndex() {
        return SPUTILS.getInt("taxRegionIndex", -1);
    }

    // 税务编号
    public static void setTaxTin(String taxTin) {
        SPUTILS.put("taxTin", taxTin);
    }
    public static String getTaxTin() {
        return SPUTILS.getString("taxTin", "");
    }

    // W-8BEN國家或地區
    public static void setTaxRegionW8benIndex(int taxRegionW8benIndex) {
        SPUTILS.put("taxRegionW8benIndex", taxRegionW8benIndex);
    }
    public static int getTaxRegionW8benIndex() {
        return SPUTILS.getInt("taxRegionW8benIndex", -1);
    }


    // ============================= step3 风险披露 ============================= //
    // 电子签名
    public static void setApplySign(String applySign) {
        SPUTILS.put("applySign", applySign);
    }
    public static String getApplySign() {
        return SPUTILS.getString("applySign", "");
    }
}
