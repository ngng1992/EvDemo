package com.mfinance.everjoy.everjoy.ui.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.network.URLContents;
import com.mfinance.everjoy.everjoy.network.okgo.OkGoBodyToStringCallBack;
import com.mfinance.everjoy.everjoy.sp.AppSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpPresenter;
import com.mfinance.everjoy.everjoy.ui.mvp.view.EmailRegisterView;

public class EmailRegisterPresenter extends BaseMvpPresenter<EmailRegisterView> {

    private EmailRegisterView emailRegisterView;

    public EmailRegisterPresenter(EmailRegisterView emailRegisterView) {
        this.emailRegisterView = emailRegisterView;
    }

    public void requestEmailCode(String email) {
        OkGo.<String>get(URLContents.REGISTER)
                .tag(mTAG)
                .params("type", 1)
                .params("email", email)
                .params("locale", AppSharedPUtils.getUserLanguage())
                .execute(new OkGoBodyToStringCallBack() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        emailRegisterView.onShowLoading();
                    }

                    @Override
                    public void onSuccessBody(BaseBean baseBean) {
                        super.onSuccessBody(baseBean);
                        emailRegisterView.onShowData(baseBean);
                    }

                    @Override
                    public void onFail(BaseBean baseBean) {
                        super.onFail(baseBean);
                        // 0 - OK, -1 - Internal Error, -2 - Email already registered
                        if (baseBean.getCode() == -2) {
                            emailRegisterView.onShowEmailCodeError(baseBean);
                        }else {
                            emailRegisterView.onShowError(baseBean.getMessage());
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        emailRegisterView.onHideLoading();
                    }
                });
    }


    public void requestEmailCheckCode(String email, String code) {
        OkGo.<String>get(URLContents.REGISTER)
                .tag(mTAG)
                .params("type", 2)
                .params("email", email)
                .params("otp", code)
                .execute(new OkGoBodyToStringCallBack() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        emailRegisterView.onShowLoading();
                    }

                    @Override
                    public void onSuccessBody(BaseBean baseBean) {
                        super.onSuccessBody(baseBean);
                        emailRegisterView.onShowEmailCheckCode(baseBean);
                    }

                    @Override
                    public void onFail(BaseBean baseBean) {
                        super.onFail(baseBean);
                        // -3 错误验证码，-2未请求
                        emailRegisterView.onShowEmailCheckCodeError(baseBean);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        emailRegisterView.onHideLoading();
                    }
                });
    }
}
