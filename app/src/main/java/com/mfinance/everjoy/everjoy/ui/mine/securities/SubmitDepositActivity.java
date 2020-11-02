package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpView;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpViewActivity;
import com.mfinance.everjoy.everjoy.ui.mvp.presenter.RegisterSuccessOpenAccountPresenter;

import net.mfinance.commonlib.toast.ToastUtils;

import butterknife.OnClick;

/**
 * 提交成功，入金
 */

public class SubmitDepositActivity extends BaseMvpViewActivity<BaseMvpView<BaseBean>, RegisterSuccessOpenAccountPresenter> implements BaseMvpView<BaseBean> {
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
        return R.layout.activity_submit_deposit;
    }

    @Override
    protected void initView(View currentView) {
        mPresenter.requestEmailCode(this);
    }

    @OnClick({R.id.iv_back, R.id.cd_online_deposit, R.id.cd_cheque, R.id.cd_arrange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.cd_online_deposit:
                startActivity(new Intent(this, OnlineDepositActivity.class));
                break;
            case R.id.cd_cheque:
                startActivity(new Intent(this, ChequeDeopsitActivity.class));
                break;
            case R.id.cd_arrange:
                startActivity(new Intent(this, AppointmentWitnessActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected RegisterSuccessOpenAccountPresenter createPresenter() {
        return new RegisterSuccessOpenAccountPresenter(this);
    }

    @Override
    public void onShowData(BaseBean data) {

    }

    @Override
    public void onShowError(String msg) {
        ToastUtils.showToast(this, msg);
    }

    @Override
    public void onShowLoading() {
        showLoading();
    }

    @Override
    public void onHideLoading() {
        hideLoading();
    }
}
