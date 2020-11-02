package com.mfinance.everjoy.everjoy.ui.mvp.presenter;

import android.app.Activity;
import android.content.res.Resources;
import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpPresenter;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpView;
import com.mfinance.everjoy.everjoy.utils.DateFormatUtils;

import java.util.HashMap;
import java.util.Map;

public class RegisterSuccessOpenAccountPresenter extends BaseMvpPresenter<BaseMvpView<BaseBean>> {

    private BaseMvpView<BaseBean> baseMvpView;

    public RegisterSuccessOpenAccountPresenter(BaseMvpView<BaseBean> baseMvpView) {
        this.baseMvpView = baseMvpView;
    }

    /**
     *
     */
    public void requestEmailCode(Activity activity) {
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
            String workType = "A";
            if (workTypeIndex == 0) {
                workType = "A";
            } else if (workTypeIndex == 1) {
                workType = "F";
            } else if (workTypeIndex == 2) {
                workType = "O";
            }
            map.put("workType", workType);
        }

        // accept (C, M, S, CEO, O)
        int workPosIndex = SecuritiesSharedPUtils.getWorkPos();
        if (workPosIndex != -1) {
            String workPos = "C";
            if (workPosIndex == 0) {
                workPos = "C";
            } else if (workPosIndex == 1) {
                workPos = "M";
            } else if (workPosIndex == 2) {
                workPos = "S";
            }else if (workPosIndex == 3) {
                workPos = "CEO";
            } else if (workPosIndex == 4) {
                workPos = "O";
            }
            map.put("workPos", workPos);
        }


//        OkGo.<String>get(URLContents.REGISTER_SECURITY_ACCOUNT)
//                .tag(mTAG)
//                .params(map)
//                .execute(new OkGoBodyToStringCallBack() {
//
//                    @Override
//                    public void onStart(Request<String, ? extends Request> request) {
//                        super.onStart(request);
//                        baseMvpView.onShowLoading();
//                    }
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
}