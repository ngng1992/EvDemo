package com.mfinance.everjoy.everjoy.ui.mine;

import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

import butterknife.OnClick;

/**
 * 打开指纹识别
 */
public class FingeridOpenActivity extends BaseViewActivity {

    @Override
    protected boolean isFullStatusByView() {
        return true;
    }

    @Override
    protected boolean isRemoveAppBar() {
        return true;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_fingerid_open;
    }

    @Override
    protected void initView(View currentView) {

    }

    @OnClick({R.id.iv_back, R.id.tv_open, R.id.tv_no_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_open:
                break;
            case R.id.tv_no_open:
                break;
            default:
                break;
        }
    }
}
