package net.mfinance.commonlib.share.impl;

public interface OnShareListener {

    void onShare();

    void onCancel();

    void onError(String msg);

}
