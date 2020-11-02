package com.mfinance.everjoy.everjoy.ui.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.network.URLContents;
import com.mfinance.everjoy.everjoy.network.okgo.OkGoBodyToStringCallBack;
import com.mfinance.everjoy.everjoy.sp.AppSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mine.ForgetPwdActivity;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpPresenter;
import com.mfinance.everjoy.everjoy.ui.mvp.view.EmailRegisterView;
import com.mfinance.everjoy.everjoy.ui.mvp.view.ForgetPwdView;

public class ForgotPasswordPresenter extends BaseMvpPresenter<ForgetPwdView> {

    private ForgetPwdActivity forgetPwdView;

    public ForgotPasswordPresenter(ForgetPwdActivity forgetPwdView) {
        this.forgetPwdView = forgetPwdView;
    }

    public void requestEmailCode(String email) {
        OkGo.<String>get(URLContents.FORGOTPASSWORD)
                .tag(mTAG)
                .params("type", 1)
                .params("email", email)
                .params("locale", AppSharedPUtils.getUserLanguage())
                .execute(new OkGoBodyToStringCallBack() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        forgetPwdView.onShowLoading();
                    }

                    @Override
                    public void onSuccessBody(BaseBean baseBean) {
                        super.onSuccessBody(baseBean);
                        forgetPwdView.onShowEmailCheckCode(baseBean);
                    }

                    @Override
                    public void onFail(BaseBean baseBean) {
                        super.onFail(baseBean);
                        forgetPwdView.onShowError(baseBean.getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        forgetPwdView.onHideLoading();
                    }
                });
    }

    public void requestForgotPassword(String acc, String email) { //
        OkGo.<String>get(URLContents.FORGOTSECPASSWORD)
                .tag(mTAG)
                .params("acc", acc)
                .params("email", email)
                .execute(new OkGoBodyToStringCallBack() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        forgetPwdView.onShowLoading();
                    }

                    @Override
                    public void onSuccessBody(BaseBean baseBean) {
                        super.onSuccessBody(baseBean);
                        forgetPwdView.toSecurityLogin();
                        //forgetPwdView.onShowEmailCheckCode(baseBean);
                    }

                    @Override
                    public void onFail(BaseBean baseBean) {
                        super.onFail(baseBean);
                        forgetPwdView.onShowError(baseBean.getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        forgetPwdView.onHideLoading();
                    }
                });
    }
}
