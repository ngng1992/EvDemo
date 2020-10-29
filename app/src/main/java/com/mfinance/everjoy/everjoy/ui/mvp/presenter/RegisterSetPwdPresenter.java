package com.mfinance.everjoy.everjoy.ui.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.network.URLContents;
import com.mfinance.everjoy.everjoy.network.okgo.OkGoBodyToStringCallBack;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpPresenter;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpView;

public class RegisterSetPwdPresenter extends BaseMvpPresenter<BaseMvpView<BaseBean>> {

    private BaseMvpView<BaseBean> baseMvpView;

    public RegisterSetPwdPresenter(BaseMvpView<BaseBean> baseMvpView) {
        this.baseMvpView = baseMvpView;
    }

    public void requestRegisterSetPwd(String email, String password) {
        OkGo.<String>get(URLContents.REGISTER)
                .tag(mTAG)
                .params("email", email)
                .params("password", password)
                .execute(new OkGoBodyToStringCallBack() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        baseMvpView.onShowLoading();
                    }

                    @Override
                    public void onSuccessBody(BaseBean baseBean) {
                        super.onSuccessBody(baseBean);
                        baseMvpView.onShowData(baseBean);
                    }

                    @Override
                    public void onFail(BaseBean baseBean) {
                        super.onFail(baseBean);
                        baseMvpView.onShowError(baseBean.getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        baseMvpView.onHideLoading();
                    }
                });
    }
}
