package com.mfinance.everjoy.everjoy.ui.mine;

import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

/**
 * 用户协议
 */
public class UserAgreementActivity extends BaseViewActivity {

    @Override
    protected boolean isRemoveAppBar() {
        return true;
    }

    @Override
    protected boolean isFullStatusByView() {
        return true;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_user_agreement;
    }

    @Override
    protected void initView(View currentView) {

    }
}
