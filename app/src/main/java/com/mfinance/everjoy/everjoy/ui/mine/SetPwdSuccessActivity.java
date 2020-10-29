package com.mfinance.everjoy.everjoy.ui.mine;

import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

import butterknife.OnClick;

/**
 * 修改密码成功，去登录
 */
public class SetPwdSuccessActivity extends BaseViewActivity {

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
        return R.layout.activity_set_pwd_success;
    }

    @Override
    protected void initView(View currentView) {

    }

    @OnClick({R.id.iv_back, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_login:
                LoginActivity.loginActivity(this);
                break;
            default:
                break;
        }
    }
}
