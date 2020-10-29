package com.mfinance.everjoy.everjoy.ui.mvp.view;

import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpView;

public interface EmailRegisterView extends BaseMvpView<BaseBean> {

    void onShowEmailCheckCode(BaseBean baseBean);
    void onShowEmailCheckCodeError(String msg);
}
