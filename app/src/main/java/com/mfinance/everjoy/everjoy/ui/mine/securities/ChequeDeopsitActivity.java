package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;

import butterknife.OnClick;

/**
 * 支票开户
 */
public class ChequeDeopsitActivity extends BaseViewActivity {

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
        return R.layout.activity_cheque_deopsit;
    }

    @Override
    protected void initView(View currentView) {

    }

    @OnClick({R.id.iv_back, R.id.tv_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_start:
                MainActivity.startMainActivity2(this);
                finish();
                break;
            default:
                break;
        }
    }
}
