package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;

import net.mfinance.commonlib.toast.ToastUtils;
import net.mfinance.commonlib.view.StringTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 签署协议
 */
public class SigningAgreementActivity extends BaseViewActivity {


    @BindView(R.id.tv_username_sign)
    TextView tv_username_sign;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.tv_contact)
    TextView tvContact;

    private String name;
  
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
        return R.layout.activity_signing_agreement;
    }

    @Override
    protected void initView(View currentView) {
        // 在线咨询，与前面填写的名称一致
        String persNameEnglish = SecuritiesSharedPUtils.getPersNameEnglish();
        String persNameChinese = SecuritiesSharedPUtils.getPersNameChinese();
        if (!TextUtils.isEmpty(persNameEnglish)) {
            name = persNameEnglish;
        } else {
            name = persNameChinese;
        }

        String namesign = String.format(getString(R.string.asa_input_auto_name), name);
        new StringTextView(tv_username_sign)
                .setStrText(namesign)
                .setColor(getResources().getColor(R.color.blue18))
                .setTextSize(1f)
                .setTargetText(name)
                .setUnderline(false)
                .setClick(false)
                .create();

        String applySign = SecuritiesSharedPUtils.getApplySign();
        if (!TextUtils.isEmpty(applySign)) {
            et_username.setText(applySign);
        }

        // 在线咨询
        String verifMsg = getString(R.string.sec_acc_ui_contact);
        String target = verifMsg.substring(verifMsg.length() - 4);
        new StringTextView(tvContact)
                .setStrText(verifMsg)
                .setColor(getResources().getColor(R.color.blue18))
                .setTextSize(1f)
                .setTargetText(target)
                .setUnderline(false)
                .setClick(true)
                .setOnClickSpannableStringListener(new StringTextView.OnClickSpannableStringListener() {
                    @Override
                    public void onClickSpannableString(View view) {
                        ContactActivity.startContactActivity(SigningAgreementActivity.this);
                    }
                })
                .create();

    }

    @OnClick({R.id.iv_back, R.id.tv_prev, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_prev:
                onBackPressed();
                break;
            case R.id.tv_next:
                String inputName = et_username.getText().toString();
                if (!inputName.equals(name)) {
                    ToastUtils.showToast(this, R.string.toast_name_inconsistent);
                    return;
                }
                startActivity(new Intent(this, SubmitDepositActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        saveToSP();
        finish();
    }

    private void saveToSP() {

    }
}
