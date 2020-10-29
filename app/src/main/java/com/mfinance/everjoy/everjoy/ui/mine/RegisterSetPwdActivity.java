package com.mfinance.everjoy.everjoy.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.config.Constants;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpView;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpViewActivity;
import com.mfinance.everjoy.everjoy.ui.mvp.presenter.RegisterSetPwdPresenter;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;

import net.mfinance.commonlib.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 邮箱注册，设置密码
 */
public class RegisterSetPwdActivity extends BaseMvpViewActivity<BaseMvpView<BaseBean>, RegisterSetPwdPresenter>
        implements BaseMvpView<BaseBean> {

    public static void start(Activity activity, String email) {
        Intent intent = new Intent(activity, RegisterSetPwdActivity.class);
        intent.putExtra(Constants.EMAIL, email);
        activity.startActivity(intent);
    }

    @BindView(R.id.et_newpwd)
    EditText etNewpwd;
    @BindView(R.id.iv_show_newpwd)
    ImageView ivShowNewpwd;
    @BindView(R.id.et_define_pwd)
    EditText etDefinePwd;
    @BindView(R.id.iv_show_define_pwd)
    ImageView ivShowDefinePwd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_go_login)
    TextView tvGoLogin;
    @BindView(R.id.tv_user_agreement)
    TextView tvUserAgreement;

    private String email;

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
        return R.layout.activity_register_set_pwd;
    }

    @Override
    protected void initView(View currentView) {
        Intent intent = getIntent();
        email = intent.getStringExtra(Constants.EMAIL);
    }

    @OnClick({R.id.iv_back, R.id.iv_show_newpwd, R.id.iv_show_define_pwd, R.id.tv_register,
            R.id.tv_go_login, R.id.tv_user_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_show_newpwd:
                ivShowNewpwd.setSelected(!ivShowNewpwd.isSelected());
                if (ivShowNewpwd.isSelected()) {
                    // 明文
                    etNewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 密文
                    etNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.iv_show_define_pwd:
                ivShowDefinePwd.setSelected(!ivShowDefinePwd.isSelected());
                if (ivShowDefinePwd.isSelected()) {
                    // 明文
                    etDefinePwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 密文
                    etDefinePwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.tv_register:
                String newpwd = etNewpwd.getText().toString();
                String definePwd = etDefinePwd.getText().toString();
                if (TextUtils.isEmpty(newpwd)) {
                    ToastUtils.showToast(this, R.string.arsp_input_pwd);
                    return;
                }
                if (!ToolsUtils.verfiPwd(newpwd)) {
                    ToastUtils.showToast(this, R.string.str_pwd_rule_error);
                    return;
                }
                if (TextUtils.isEmpty(definePwd)) {
                    ToastUtils.showToast(this, R.string.arsp_input_define_pwd);
                    return;
                }
                if (!newpwd.equals(definePwd)) {
                    ToastUtils.showToast(this, R.string.arsp_new_define_pwd);
                    return;
                }
                mPresenter.requestRegisterSetPwd(email, newpwd);
                break;
            case R.id.tv_go_login:
                LoginActivity.loginActivity(this);
                break;
            case R.id.tv_user_agreement:
                startActivity(new Intent(this, UserAgreementActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected RegisterSetPwdPresenter createPresenter() {
        return new RegisterSetPwdPresenter(this);
    }

    @Override
    public void onShowData(BaseBean data) {
        startActivity(new Intent(this, RegisterSuccessOpenAccountActivity.class));
    }

    @Override
    public void onShowError(String msg) {
        showLoading(msg);
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
