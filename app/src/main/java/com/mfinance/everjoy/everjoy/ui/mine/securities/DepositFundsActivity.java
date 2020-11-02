package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

import butterknife.OnClick;

/**
 * 存入资金-选择币种
 */
public class DepositFundsActivity extends BaseViewActivity {

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
        return R.layout.activity_deposit_funds;
    }

    @Override
    protected void initView(View currentView) {

    }

    @OnClick({R.id.iv_back, R.id.tv_hkd, R.id.tv_usd, R.id.tv_cny})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_hkd:
                break;
            case R.id.tv_usd:
                break;
            case R.id.tv_cny:
                break;
            default:
                break;
        }
    }
}
