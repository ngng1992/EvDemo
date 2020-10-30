package com.mfinance.everjoy.everjoy.ui.mvp.presenter;

import android.app.Activity;
import android.content.res.Resources;
import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.network.URLContents;
import com.mfinance.everjoy.everjoy.network.okgo.OkGoBodyToStringCallBack;
import com.mfinance.everjoy.everjoy.sp.AppSharedPUtils;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpPresenter;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpView;
import com.mfinance.everjoy.everjoy.ui.mvp.view.EmailRegisterView;
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

        String persNameTitle = "Mr";
        int persNameTitleIndex = SecuritiesSharedPUtils.getPersNameTitle();
        if (persNameTitleIndex == 0) {
            persNameTitle = "Mr";
        }else if (persNameTitleIndex == 1) {
            persNameTitle = "Miss";
        }else if (persNameTitleIndex == 2) {
            persNameTitle = "Mrs";
        }
        map.put("persNameTitle", persNameTitle);

        String persNameChinese = SecuritiesSharedPUtils.getPersNameChinese();
        if (!TextUtils.isEmpty(persNameChinese)) {
            map.put("persNameChinese", persNameChinese);
        }

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
        }else if (persMaritalStatusIndex == 1) {
            persMaritalStatus = "married";
        }
        map.put("persMaritalStatus", persMaritalStatus);

        String persGender = "male";
        int persGenderIndex = SecuritiesSharedPUtils.getPersGender();
        if (persGenderIndex == 0) {
            persGender = "male";
        }else if (persGenderIndex == 1) {
            persGender = "female";
        }
        map.put("persGender", persGender);

        int persNationalit = SecuritiesSharedPUtils.getPersNationalit();
        map.put("persNationality", country_area_alpha3_code[persNationalit]);

        String persIdType = "IDHK";
        int persIdTypeIndex = SecuritiesSharedPUtils.getPersIdType();
        if (persIdTypeIndex == 0) {
            persIdType = "IDHK";
        }else if (persIdTypeIndex == 1) {
            persIdType = "IDCN";
        }else if (persIdTypeIndex == 2) {
            persIdType = "PPCN";
        }
        map.put("persIdType", persIdType);

        String persIdNo = SecuritiesSharedPUtils.getPersIdNo();
        map.put("persIdNo", persIdNo);

        int persBandAccRegion = SecuritiesSharedPUtils.getPersBandAccRegion();
        map.put("persBandAccRegion", country_area_alpha3_code[persBandAccRegion]);

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
