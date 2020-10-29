package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

/**
 * 存入方式
 */
public class FundsModeActivity extends BaseViewActivity {

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
        return R.layout.activity_funds_mode;
    }

    @Override
    protected void initView(View currentView) {

    }
}
