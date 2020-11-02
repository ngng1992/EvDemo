package com.mfinance.everjoy.everjoy.ui.mvp.presenter;

import android.app.Activity;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.TimeUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.network.URLContents;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpPresenter;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpView;
import com.mfinance.everjoy.everjoy.utils.DateFormatUtils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RegisterSuccessOpenAccountPresenter extends BaseMvpPresenter<BaseMvpView<BaseBean>> {

    private BaseMvpView<BaseBean> baseMvpView;

    public RegisterSuccessOpenAccountPresenter(BaseMvpView<BaseBean> baseMvpView) {
        this.baseMvpView = baseMvpView;
    }

    /**
     *
     */
    public void requestEmailCode(Activity activity) {
        baseMvpView.onShowLoading();

        Resources resources = activity.getResources();
        Map<String, String> map = new HashMap<>();

        // ========================== step1 =========================
        String persNameTitle = "Mr";
        int persNameTitleIndex = SecuritiesSharedPUtils.getPersNameTitle();
        if (persNameTitleIndex == 0) {
            persNameTitle = "Mr";
        } else if (persNameTitleIndex == 1) {
            persNameTitle = "Miss";
        } else if (persNameTitleIndex == 2) {
            persNameTitle = "Mrs";
        }
        map.put("persNameTitle", persNameTitle);

        String persNameChinese = SecuritiesSharedPUtils.getPersNameChinese();
        if (!TextUtils.isEmpty(persNameChinese)) {
            map.put("persNameChinese", persNameChinese);
        }

        String persNameEnglish = SecuritiesSharedPUtils.getPersNameEnglish();
        map.put("persNameEnglish", persNameEnglish);

        int persBirthRegion = SecuritiesSharedPUtils.getPersBirthRegion();
        String[] country_area_alpha3_code = resources.getStringArray(R.array.country_area_alpha3_code);
        map.put("persBirthRegion", country_area_alpha3_code[persBirthRegion]);

        long persBirthDate = SecuritiesSharedPUtils.getPersBirthDate();
        String millis2String = TimeUtils.millis2String(persBirthDate, DateFormatUtils.DATE_FORMAT3);
        map.put("persBirthDate", millis2String);

        String persMaritalStatus = "single";
        int persMaritalStatusIndex = SecuritiesSharedPUtils.getPersMaritalStatus();
        if (persMaritalStatusIndex == 0) {
            persMaritalStatus = "single";
        } else if (persMaritalStatusIndex == 1) {
            persMaritalStatus = "married";
        }
        map.put("persMaritalStatus", persMaritalStatus);

        String persGender = "male";
        int persGenderIndex = SecuritiesSharedPUtils.getPersGender();
        if (persGenderIndex == 0) {
            persGender = "male";
        } else if (persGenderIndex == 1) {
            persGender = "female";
        }
        map.put("persGender", persGender);

        int persNationalit = SecuritiesSharedPUtils.getPersNationalit();
        map.put("persNationality", country_area_alpha3_code[persNationalit]);

        String persIdType = "IDHK";
        int persIdTypeIndex = SecuritiesSharedPUtils.getPersIdType();
        if (persIdTypeIndex == 0) {
            persIdType = "IDHK";
        } else if (persIdTypeIndex == 1) {
            persIdType = "IDCN";
        } else if (persIdTypeIndex == 2) {
            persIdType = "PPCN";
        }
        map.put("persIdType", persIdType);

        String persIdNo = SecuritiesSharedPUtils.getPersIdNo();
        map.put("persIdNo", persIdNo);

        int persBandAccRegion = SecuritiesSharedPUtils.getPersBandAccRegion();
        map.put("persBandAccRegion", country_area_alpha3_code[persBandAccRegion]);

        // ========================== step2 =========================
        String persEmail = SecuritiesSharedPUtils.getPersEmail();
        map.put("persEmail", persEmail);

        String persTelNo = SecuritiesSharedPUtils.getPersTelNo();
        map.put("persTelNo", persTelNo);

        String persAddress = SecuritiesSharedPUtils.getPersAddress();
        map.put("persAddress", persAddress);

        // accept (P,S,PS,U,O)
        int persEducationLevel = SecuritiesSharedPUtils.getPersEducationLevel();
        String edu = "P";
        if (persEducationLevel == 0) {
            edu = "P";
        } else if (persEducationLevel == 1) {
            edu = "S";
        } else if (persEducationLevel == 2) {
            edu = "PS";
        } else if (persEducationLevel == 3) {
            edu = "U";
        } else if (persEducationLevel == 4) {
            edu = "O";
            String persEducationLevelOther = SecuritiesSharedPUtils.getPersEducationLevelOther();
            if (!TextUtils.isEmpty(persEducationLevelOther)) {
                map.put("persEducationLevelOther", persEducationLevelOther);
            }
        }
        map.put("persEducationLevel", edu);

        // accept (E,SE,S,R,H,U)
        String workState = "E";
        int workStateIndex = SecuritiesSharedPUtils.getWorkState();
        if (workStateIndex == 0) {
            workState = "E";
        } else if (workStateIndex == 1) {
            workState = "SE";
        } else if (workStateIndex == 2) {
            workState = "S";
        } else if (workStateIndex == 3) {
            workState = "R";
        } else if (workStateIndex == 4) {
            workState = "H";
        } else if (workStateIndex == 5) {
            workState = "U";
        }
        map.put("workState", workState);

        String workEmployerName = SecuritiesSharedPUtils.getWorkEmployerName();
        if (!TextUtils.isEmpty(workEmployerName)) {
            map.put("workEmployerName", workEmployerName);
        }

        String workAddress = SecuritiesSharedPUtils.getWorkAddress();
        if (!TextUtils.isEmpty(workAddress)) {
            map.put("workAddress", workAddress);
        }

        String workTel = SecuritiesSharedPUtils.getWorkTel();
        if (!TextUtils.isEmpty(workTel)) {
            map.put("workTel", workTel);
        }

        // accept (A, F, O)
        int workTypeIndex = SecuritiesSharedPUtils.getWorkType();
        if (workTypeIndex != -1) {
            String workType = "";
            if (workTypeIndex == 0) {
                workType = "A";
            } else if (workTypeIndex == 1) {
                workType = "F";
            } else if (workTypeIndex == 2) {
                workType = "O";
            }
            if (!TextUtils.isEmpty(workType)) {
                map.put("workType", workType);
            }
        }

        // accept (C, M, S, CEO, O)
        int workPosIndex = SecuritiesSharedPUtils.getWorkPos();
        if (workPosIndex != -1) {
            String workPos = "";
            if (workPosIndex == 0) {
                workPos = "C";
            } else if (workPosIndex == 1) {
                workPos = "M";
            } else if (workPosIndex == 2) {
                workPos = "S";
            } else if (workPosIndex == 3) {
                workPos = "CEO";
            } else if (workPosIndex == 4) {
                workPos = "O";
            }
            if (!TextUtils.isEmpty(workPos)) {
                map.put("workPos", workPos);
            }
        }

//        accept (1, 1TO3, 3TO5, 5ABOVE)
        int workYearIndex = SecuritiesSharedPUtils.getWorkYear();
        if (workYearIndex != -1) {
            String workYear = "";
            if (workYearIndex == 0) {
                workYear = "1";
            } else if (workYearIndex == 1) {
                workYear = "1TO3";
            } else if (workYearIndex == 2) {
                workYear = "3TO5";
            } else if (workYearIndex == 3) {
                workYear = "5ABOVE";
            }
            if (!TextUtils.isEmpty(workYear)) {
                map.put("workYear", workYear);
            }
        }

        // accept(BELOW200, 200TO400, 400TO800, 800TO1200, ABOVE1200)
        int finYearRevenueIndex = SecuritiesSharedPUtils.getFinYearRevenue();
        String finYearRevenue = "BELOW200";
        if (finYearRevenueIndex == 0) {
            finYearRevenue = "BELOW200";
        } else if (finYearRevenueIndex == 1) {
            finYearRevenue = "200TO400";
        } else if (finYearRevenueIndex == 2) {
            finYearRevenue = "400TO800";
        } else if (finYearRevenueIndex == 3) {
            finYearRevenue = "800TO1200";
        } else if (finYearRevenueIndex == 4) {
            finYearRevenue = "ABOVE1200";
        }
        map.put("finYearRevenue", finYearRevenue);

        // accept (BELOW1, 1TO3, 3TO5, 5TO8, 8TO15, ABOVE15)
        int finNetAssetValueIndex = SecuritiesSharedPUtils.getFinNetAssetValueIndex();
        String finNetAssetValue = "";
        if (finNetAssetValueIndex == 0) {
            finNetAssetValue = "BELOW1";
        } else if (finNetAssetValueIndex == 1) {
            finNetAssetValue = "1TO3";
        } else if (finNetAssetValueIndex == 2) {
            finNetAssetValue = "3TO5";
        } else if (finNetAssetValueIndex == 3) {
            finNetAssetValue = "5TO8";
        } else if (finNetAssetValueIndex == 4) {
            finNetAssetValue = "8TO15";
        } else if (finNetAssetValueIndex == 5) {
            finNetAssetValue = "ABOVE15";
        }
        map.put("finNetAssetValue", finNetAssetValue);

        //  (D,L,H,F,M,S,O)
        int finResidTypeIndex = SecuritiesSharedPUtils.getFinResidTypeIndex();
        String finResidType = "D";
        if (finResidTypeIndex == 0) {
            finResidType = "D";
        } else if (finResidTypeIndex == 1) {
            finResidType = "L";
        } else if (finResidTypeIndex == 2) {
            finResidType = "H";
        } else if (finResidTypeIndex == 3) {
            finResidType = "F";
        } else if (finResidTypeIndex == 4) {
            finResidType = "M";
        } else if (finResidTypeIndex == 5) {
            finResidType = "S";
        } else if (finResidTypeIndex == 6) {
            finResidType = "O";
            String finResidTypeOther = SecuritiesSharedPUtils.getFinResidTypeOther();
            if (!TextUtils.isEmpty(finResidTypeOther)) {
                map.put("finResidTypeOther", finResidTypeOther);
            }
        }
        map.put("finResidType", finResidType);

        //  (S,B,P,IF,II,O)
        int finCapitalSourceIndex = SecuritiesSharedPUtils.getFinCapitalSourceIndex();
        String finCapitalSource = "S";
        if (finCapitalSourceIndex == 0) {
            finCapitalSource = "S";
        } else if (finCapitalSourceIndex == 1) {
            finCapitalSource = "B";
        } else if (finCapitalSourceIndex == 2) {
            finCapitalSource = "P";
        } else if (finCapitalSourceIndex == 3) {
            finCapitalSource = "IF";
        } else if (finCapitalSourceIndex == 4) {
            finCapitalSource = "II";
        } else if (finCapitalSourceIndex == 5) {
            finCapitalSource = "O";
            String finCapitalSourceOther = SecuritiesSharedPUtils.getFinCapitalSourceOther();
            if (!TextUtils.isEmpty(finCapitalSourceOther)) {
                map.put("finCapitalSourceOther", finCapitalSourceOther);
            }
        }
        map.put("finCapitalSource", finCapitalSource);

        // accept(R, D, V, S, B, F, O)
        int finAssetTypeIndex = SecuritiesSharedPUtils.getFinAssetTypeIndex();
        String finAssetType = "S";
        if (finAssetTypeIndex == 0) {
            finAssetType = "R";
        } else if (finAssetTypeIndex == 1) {
            finAssetType = "D";
        } else if (finAssetTypeIndex == 2) {
            finAssetType = "V";
        } else if (finAssetTypeIndex == 3) {
            finAssetType = "S";
        } else if (finAssetTypeIndex == 4) {
            finAssetType = "B";
        } else if (finAssetTypeIndex == 5) {
            finAssetType = "F";
        } else if (finAssetTypeIndex == 6) {
            finAssetType = "O";
            String finAssetTypeOther = SecuritiesSharedPUtils.getFinAssetTypeOther();
            if (!TextUtils.isEmpty(finAssetTypeOther)) {
                map.put("finAssetTypeOther", finAssetTypeOther);
            }
        }
        map.put("finAssetType", finAssetType);

        // accept (H,CG,DI,O,SP,A)
        //H-對沖
        //CG-資本增值
        //DI-股息收入
        //O-其他
        //SP-投機
        //A-套戥
        //• 保本 SP
        //•	資本增值 CG
        //•	股息回報 DI
        //•	對沖  H
        //•	套戥   A
        //•	其他 (如選擇其他, 欄位可容許客戶自行輸入) O
        int finInvestTargetIndex = SecuritiesSharedPUtils.getFinInvestTargetIndex();
        String finInvestTarget = "SP";
        if (finInvestTargetIndex == 0) {
            finInvestTarget = "SP";
        } else if (finInvestTargetIndex == 1) {
            finInvestTarget = "CG";
        } else if (finInvestTargetIndex == 2) {
            finInvestTarget = "DI";
        } else if (finInvestTargetIndex == 3) {
            finInvestTarget = "H";
        } else if (finInvestTargetIndex == 4) {
            finInvestTarget = "A";
        } else if (finInvestTargetIndex == 5) {
            finInvestTarget = "O";
            String finInvestTargetOther = SecuritiesSharedPUtils.getFinInvestTargetOther();
            if (!TextUtils.isEmpty(finInvestTargetOther)) {
                map.put("finInvestTargetOther", finInvestTargetOther);
            }
        }
        map.put("finInvestTarget", finInvestTarget);

        // accept (LOW,MEDIUM,HIGH)
        int finTolerateRiskIndex = SecuritiesSharedPUtils.getFinTolerateRiskIndex();
        String finTolerateRisk = "LOW";
        if (finTolerateRiskIndex == 0) {
            finTolerateRisk = "LOW";
        } else if (finTolerateRiskIndex == 1) {
            finTolerateRisk = "MEDIUM";
        } else if (finTolerateRiskIndex == 2) {
            finTolerateRisk = "HIGH";
        }
        map.put("finTolerateRisk", finTolerateRisk);

        // accept (BELOW1, 1TO3, 3TO5, 5TO8, 8TO10, ABOVE10)
        int finInvestAvgYearValueIndex = SecuritiesSharedPUtils.getFinInvestAvgYearValueIndex();
        String finInvestAvgYearValue = "BELOW1";
        if (finInvestAvgYearValueIndex == 0) {
            finInvestAvgYearValue = "BELOW1";
        } else if (finInvestAvgYearValueIndex == 1) {
            finInvestAvgYearValue = "1TO3";
        } else if (finInvestAvgYearValueIndex == 2) {
            finInvestAvgYearValue = "3TO5";
        } else if (finInvestAvgYearValueIndex == 3) {
            finInvestAvgYearValue = "5TO8";
        } else if (finInvestAvgYearValueIndex == 4) {
            finInvestAvgYearValue = "8TO10";
        } else if (finInvestAvgYearValueIndex == 5) {
            finInvestAvgYearValue = "ABOVE10";
        }
        map.put("finInvestAvgYearValue", finInvestAvgYearValue);

        // accept (NOEXP, BELOW1EXP, 1TO3EXP, 3TO5EXP, ABOVE5EXP)
        int finExpStockYearIndex = SecuritiesSharedPUtils.getFinExpStockYearIndex();
        String finExpStockYear = "NOEXP";
        if (finExpStockYearIndex == 0) {
            finExpStockYear = "NOEXP";
        } else if (finExpStockYearIndex == 1) {
            finExpStockYear = "BELOW1EXP";
        } else if (finExpStockYearIndex == 2) {
            finExpStockYear = "1TO3EXP";
        } else if (finExpStockYearIndex == 3) {
            finExpStockYear = "3TO5EXP";
        } else if (finExpStockYearIndex == 4) {
            finExpStockYear = "ABOVE5EXP";
        }
        map.put("finExpStockYear", finExpStockYear);

        // accept (NOEXP, BELOW1EXP, 1TO3EXP, 3TO5EXP, ABOVE5EXP)
        int finExpDerivativeYearIndex = SecuritiesSharedPUtils.getFinExpDerivativeYearIndex();
        String finExpDerivativeYear = "NOEXP";
        if (finExpDerivativeYearIndex == 0) {
            finExpDerivativeYear = "NOEXP";
        } else if (finExpDerivativeYearIndex == 1) {
            finExpDerivativeYear = "BELOW1EXP";
        } else if (finExpDerivativeYearIndex == 2) {
            finExpDerivativeYear = "1TO3EXP";
        } else if (finExpDerivativeYearIndex == 3) {
            finExpDerivativeYear = "3TO5EXP";
        } else if (finExpDerivativeYearIndex == 4) {
            finExpDerivativeYear = "ABOVE5EXP";
        }
        map.put("finExpDerivativeYear", finExpDerivativeYear);

        // accept (NOEXP, BELOW1EXP, 1TO3EXP, 3TO5EXP, ABOVE5EXP)
        int finExpFutureYearIndex = SecuritiesSharedPUtils.getFinExpFutureYearIndex();
        String finExpFutureYear = "NOEXP";
        if (finExpFutureYearIndex == 0) {
            finExpFutureYear = "NOEXP";
        } else if (finExpFutureYearIndex == 1) {
            finExpFutureYear = "BELOW1EXP";
        } else if (finExpFutureYearIndex == 2) {
            finExpFutureYear = "1TO3EXP";
        } else if (finExpFutureYearIndex == 3) {
            finExpFutureYear = "3TO5EXP";
        } else if (finExpFutureYearIndex == 4) {
            finExpFutureYear = "ABOVE5EXP";
        }
        map.put("finExpFutureYear", finExpFutureYear);

        // accept (NOEXP, BELOW1EXP, 1TO3EXP, 3TO5EXP, ABOVE5EXP)
        int finExpForexYearIndex = SecuritiesSharedPUtils.getFinExpForexYearIndex();
        String finExpForexYear = "NOEXP";
        if (finExpForexYearIndex == 0) {
            finExpForexYear = "NOEXP";
        } else if (finExpForexYearIndex == 1) {
            finExpForexYear = "BELOW1EXP";
        } else if (finExpForexYearIndex == 2) {
            finExpForexYear = "1TO3EXP";
        } else if (finExpForexYearIndex == 3) {
            finExpForexYear = "3TO5EXP";
        } else if (finExpForexYearIndex == 4) {
            finExpForexYear = "ABOVE5EXP";
        }
        map.put("finExpForexYear", finExpForexYear);

        // accept (NOEXP, BELOW1EXP, 1TO3EXP, 3TO5EXP, ABOVE5EXP)
        int finExpBondYearIndex = SecuritiesSharedPUtils.getFinExpBondYearIndex();
        String finExpBondYear = "NOEXP";
        if (finExpBondYearIndex == 0) {
            finExpBondYear = "NOEXP";
        } else if (finExpBondYearIndex == 1) {
            finExpBondYear = "BELOW1EXP";
        } else if (finExpBondYearIndex == 2) {
            finExpBondYear = "1TO3EXP";
        } else if (finExpBondYearIndex == 3) {
            finExpBondYear = "3TO5EXP";
        } else if (finExpBondYearIndex == 4) {
            finExpBondYear = "ABOVE5EXP";
        }
        map.put("finExpBondYear", finExpBondYear);

        // accept (NOEXP, BELOW1EXP, 1TO3EXP, 3TO5EXP, ABOVE5EXP)
        int finExpFoundYearIndex = SecuritiesSharedPUtils.getFinExpFoundYearIndex();
        String finExpFoundYear = "NOEXP";
        if (finExpFoundYearIndex == 0) {
            finExpFoundYear = "NOEXP";
        } else if (finExpFoundYearIndex == 1) {
            finExpFoundYear = "BELOW1EXP";
        } else if (finExpFoundYearIndex == 2) {
            finExpFoundYear = "1TO3EXP";
        } else if (finExpFoundYearIndex == 3) {
            finExpFoundYear = "3TO5EXP";
        } else if (finExpFoundYearIndex == 4) {
            finExpFoundYear = "ABOVE5EXP";
        }
        map.put("finExpFoundYear", finExpFoundYear);

        // accept (NOEXP, BELOW1EXP, 1TO3EXP, 3TO5EXP, ABOVE5EXP)
        int finExpOtherYearIndex = SecuritiesSharedPUtils.getFinExpOtherYearIndex();
        String finExpOtherYear = "NOEXP";
        if (finExpOtherYearIndex == 0) {
            finExpOtherYear = "NOEXP";
        } else if (finExpOtherYearIndex == 1) {
            finExpOtherYear = "BELOW1EXP";
        } else if (finExpOtherYearIndex == 2) {
            finExpOtherYear = "1TO3EXP";
        } else if (finExpOtherYearIndex == 3) {
            finExpOtherYear = "3TO5EXP";
        } else if (finExpOtherYearIndex == 4) {
            finExpOtherYear = "ABOVE5EXP";
        }
        map.put("finExpOtherYear", finExpOtherYear);

        // accept (C,M,H)
        int createAccTypeIndex = SecuritiesSharedPUtils.getCreateAccTypeIndex();
        String createAccType = "M";
        if (createAccTypeIndex == 0) {
            createAccType = "M";
        } else if (createAccTypeIndex == 1) {
            createAccType = "C";
        }
        map.put("createAccType", createAccType);

        // accept (true, false)
        boolean createAccStockHk = SecuritiesSharedPUtils.getCreateAccStockHk();
        map.put("createAccStockHk", String.valueOf(createAccStockHk));

        // accept (true, false)
        boolean createAccStockUs = SecuritiesSharedPUtils.getCreateAccStockUs();
        map.put("createAccStockUs", String.valueOf(createAccStockUs));

        // accept (true, false)
        boolean createAccStockA = SecuritiesSharedPUtils.getCreateAccStockA();
        map.put("createAccStockA", String.valueOf(createAccStockA));

        // accept(SINGLE_COUNTRY, MULTIPLE_COUNTRY )
        int taxInfoIndex = SecuritiesSharedPUtils.getTaxInfoIndex();
        String taxInfo = "SINGLE_COUNTRY";
        if (taxInfoIndex == 0) {
            taxInfo = "SINGLE_COUNTRY";
        } else if (taxInfoIndex == 1) {
            taxInfo = "MULTIPLE_COUNTRY";
        }
        map.put("taxInfo", taxInfo);

        int taxRegionIndex = SecuritiesSharedPUtils.getTaxRegionIndex();
        if (taxRegionIndex != -1) {
            map.put("taxRegion", country_area_alpha3_code[taxRegionIndex]);
        }

        String taxTin = SecuritiesSharedPUtils.getTaxTin();
        if (!TextUtils.isEmpty(taxTin)) {
            map.put("taxTin", taxTin);
        }

        int taxRegionW8benIndex = SecuritiesSharedPUtils.getTaxRegionW8benIndex();
        if (taxRegionW8benIndex != -1) {
            map.put("taxRegionW8ben", country_area_alpha3_code[taxRegionW8benIndex]);
        }

        String applySign = SecuritiesSharedPUtils.getApplySign();
        map.put("applySign", applySign);

        String imgHkIdFrontFileName = SecuritiesSharedPUtils.getImgHkIdFrontFileName();
        if (!TextUtils.isEmpty(imgHkIdFrontFileName)) {
            map.put("imgHkIdFrontFileName", imgHkIdFrontFileName);
        }

        String imgHkIdBackFileName = SecuritiesSharedPUtils.getImgHkIdBackFileName();
        if (!TextUtils.isEmpty(imgHkIdBackFileName)) {
            map.put("imgHkIdBackFileName", imgHkIdBackFileName);
        }

        String imgMainlandIdFrontFileName = SecuritiesSharedPUtils.getImgMainlandIdFrontFileName();
        if (!TextUtils.isEmpty(imgMainlandIdFrontFileName)) {
            map.put("imgMainlandIdFrontFileName", imgMainlandIdFrontFileName);
        }

        String imgMainlandIdBackFileName = SecuritiesSharedPUtils.getImgMainlandIdBackFileName();
        if (!TextUtils.isEmpty(imgMainlandIdBackFileName)) {
            map.put("imgMainlandIdBackFileName", imgMainlandIdBackFileName);
        }

        String imgPassportFileName = SecuritiesSharedPUtils.getImgPassportFileName();
        if (!TextUtils.isEmpty(imgPassportFileName)) {
            map.put("imgPassportFileName", imgPassportFileName);
        }

        String imgHkMacauPassFileName = SecuritiesSharedPUtils.getImgHkMacauPassFileName();
        if (!TextUtils.isEmpty(imgHkMacauPassFileName)) {
            map.put("imgHkMacauPassFileName", imgHkMacauPassFileName);
        }

        String imgProofAddressFileName = SecuritiesSharedPUtils.getImgProofAddressFileName();
        if (!TextUtils.isEmpty(imgProofAddressFileName)) {
            map.put("imgProofAddressFileName", imgProofAddressFileName);
        }

        String vidSelfieFileName = SecuritiesSharedPUtils.getVidSelfieFileName();
        if (!TextUtils.isEmpty(vidSelfieFileName)) {
            map.put("vidSelfieFileName", vidSelfieFileName);
        }

        StringBuilder sb = new StringBuilder();
        // 对key排序
        Set<String> set = map.keySet();
        Object[] arr = set.toArray();
        Arrays.sort(arr);
        for (Object key : arr) {
            String value = map.get(key);
            Log.e(mTAG, "对map排序 key = " + key + ";value = " + value);
            appendData(sb, value);
        }


//        appendData(sb, applySign);
////        appendData(sb, createAccStockA);
////        appendData(sb, createAccStockHk);
////        appendData(sb, createAccStockUs);
////        appendData(sb, createAccType);
////        appendData(sb, finAssetType);
////        appendData(sb, finAssetTypeOther);
////        appendData(sb, finCapitalSource);
////        appendData(sb, finCapitalSourceOther);
////        appendData(sb, finExpBondYear);
////        appendData(sb, finExpDerivativeYear);
////        appendData(sb, finExpForexYear);
////        appendData(sb, finExpFoundYear);
////        appendData(sb, finExpFutureYear);
////        appendData(sb, finExpOtherYear);
////        appendData(sb, finExpStockYear);
////        appendData(sb, finInvestAvgYearValue);
////        appendData(sb, finInvestTarget);
////        appendData(sb, finInvestTargetOther);
////        appendData(sb, finNetAssetValue);
////        appendData(sb, finResidType);
////        appendData(sb, finResidTypeOther);
////        appendData(sb, finTolerateRisk);
////        appendData(sb, finYearRevenue);
////        appendData(sb, imgHkIdBackFileName);
////        appendData(sb, imgHkIdFrontFileName);
////        appendData(sb, imgHkMacauPassFileName);
////        appendData(sb, imgMainlandIdBackFileName);
////        appendData(sb, imgMainlandIdFrontFileName);
////        appendData(sb, imgPassportFileName);
////        appendData(sb, imgProofAddressFileName);
////        appendData(sb, persAddress);
////        appendData(sb, persBandAccRegion);
////        appendData(sb, persBirthDate);
////        appendData(sb, persBirthRegion);
////        appendData(sb, persEducationLevel);
////        appendData(sb, persEducationLevelOther);
////        appendData(sb, persEmail);
////        appendData(sb, persGender);
////        appendData(sb, persIdNo);
////        appendData(sb, persIdType);
////        appendData(sb, persMailAddress);
////        appendData(sb, persMaritalStatus);
////        appendData(sb, persNameChinese);
////        appendData(sb, persNameEnglish);
////        appendData(sb, persNameTitle);
////        appendData(sb, persNationality);
////        appendData(sb, persTelNo);
////        appendData(sb, taxInfo);
////        appendData(sb, taxRegion);
////        appendData(sb, taxRegionW8ben);
////        appendData(sb, taxTin);
////        appendData(sb, vidSelfieFileName);
////        appendData(sb, workAddress);
////        appendData(sb, workEmployerName);
////        appendData(sb, workPos);
////        appendData(sb, workState);
////        appendData(sb, workTel);
////        appendData(sb, workType);
////        appendData(sb, workYear);
        String content = sb.toString();
        Log.e(mTAG, "加密前的值 content = " + content);
        String checksum = DigestUtils.sha256Hex(content);
        Log.e(mTAG, "加密前的后 checksum = " + checksum);
        map.put("checksum", checksum);


        PostRequest<String> postRequest = OkGo.<String>post(URLContents.REGISTER_SECURITY_ACCOUNT).tag(mTAG);
        postRequest.params(map);

        String imgHkIdFront = SecuritiesSharedPUtils.getImgHkIdFront();
        if (!TextUtils.isEmpty(imgHkIdFront)) {
            postRequest.params("imgHkIdFront", new File(imgHkIdFront));
        }

        String imgHkIdBack = SecuritiesSharedPUtils.getImgHkIdBack();
        if (!TextUtils.isEmpty(imgHkIdBack)) {
            postRequest.params("imgHkIdBack", new File(imgHkIdBack));
        }

        String imgMainlandIdFront = SecuritiesSharedPUtils.getImgMainlandIdFront();
        if (!TextUtils.isEmpty(imgMainlandIdFront)) {
            postRequest.params("imgMainlandIdFront", new File(imgMainlandIdFront));
        }

        String imgMainlandIdBack = SecuritiesSharedPUtils.getImgMainlandIdBack();
        if (!TextUtils.isEmpty(imgMainlandIdBack)) {
            postRequest.params("imgMainlandIdBack", new File(imgMainlandIdBack));
        }

        String imgPassport = SecuritiesSharedPUtils.getImgPassport();
        if (!TextUtils.isEmpty(imgPassport)) {
            postRequest.params("imgPassport", new File(imgPassport));
        }

        String imgHkMacauPass = SecuritiesSharedPUtils.getImgHkMacauPass();
        if (!TextUtils.isEmpty(imgHkMacauPass)) {
            postRequest.params("imgHkMacauPass", new File(imgHkMacauPass));
        }

        String imgProofAddress = SecuritiesSharedPUtils.getImgProofAddress();
        if (!TextUtils.isEmpty(imgProofAddress)) {
            postRequest.params("imgProofAddress", new File(imgProofAddress));
        }

        String vidSelfie = SecuritiesSharedPUtils.getVidSelfie();
        if (!TextUtils.isEmpty(vidSelfie)) {
            postRequest.params("vidSelfie", new File(vidSelfie));
        }

//        postRequest.execute(new OkGoBodyToStringCallBack() {
//
//                    @Override
//                    public void onSuccessBody(BaseBean baseBean) {
//                        super.onSuccessBody(baseBean);
//                        baseMvpView.onShowData(baseBean);
//                    }
//
//                    @Override
//                    public void onFail(BaseBean baseBean) {
//                        super.onFail(baseBean);
//                        baseMvpView.onShowError(baseBean.getMessage());
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                        baseMvpView.onHideLoading();
//                    }
//                });
    }


    private void appendData(StringBuilder sb, String input) {
        if (input != null) {
            sb.append(input);
        }
    }
}
