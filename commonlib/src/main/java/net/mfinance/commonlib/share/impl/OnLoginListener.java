package net.mfinance.commonlib.share.impl;


import net.mfinance.commonlib.share.bean.LoginBean;

public interface OnLoginListener {

    void onLogin(LoginBean loginBean);

    void onCancel();

    void onError(String msg);

}
