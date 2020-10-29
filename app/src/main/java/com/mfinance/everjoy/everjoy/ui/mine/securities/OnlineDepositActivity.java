package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

import butterknife.OnClick;

/**
 * 线上入金
 */
public class OnlineDepositActivity extends BaseViewActivity {

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
        return R.layout.activity_online_deposit;
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
                startActivity(new Intent(this, NewAddBankActivity.class));
                break;
            default:
                break;
        }
    }
}
