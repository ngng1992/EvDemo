package com.mfinance.everjoy.everjoy.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.OpenAccountFileDialog;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;
import com.mfinance.everjoy.everjoy.ui.mine.securities.SecuritiesAccountActivity;

import net.mfinance.commonlib.toast.ToastUtils;
import net.mfinance.commonlib.view.StringTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册成功并选择开通证券账户
 */
public class RegisterSuccessOpenAccountActivity extends BaseViewActivity {

    @BindView(R.id.cbx_desc1)
    CheckBox cbxDesc1;
    @BindView(R.id.cbx_desc2)
    CheckBox cbxDesc2;
    @BindView(R.id.cbx_desc3)
    CheckBox cbxDesc3;
    @BindView(R.id.cbx_desc4)
    CheckBox cbxDesc4;
    @BindView(R.id.cbx_desc5)
    CheckBox cbxDesc5;
    @BindView(R.id.cbx_desc6)
    CheckBox cbxDesc6;
    @BindView(R.id.cbx_desc7)
    CheckBox cbxDesc7;
    @BindView(R.id.cbx_desc8)
    CheckBox cbxDesc8;
    @BindView(R.id.tv_open_account)
    TextView tvOpenAccount;

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
        return R.layout.activity_register_success_open_account;
    }

    @Override
    protected void initView(View currentView) {
        // 在线咨询
        String verifMsg = getString(R.string.str_read_account_file);
        String target = getString(R.string.str_read_account_file_desc);
        new StringTextView(cbxDesc1)
                .setStrText(verifMsg)
                .setColor(getResources().getColor(R.color.blue18))
                .setTextSize(1f)
                .setTargetText(target)
                .setUnderline(false)
                .setClick(true)
                .setOnClickSpannableStringListener(new StringTextView.OnClickSpannableStringListener() {
                    @Override
                    public void onClickSpannableString(View view) {
                        // 开户文件
                        OpenAccountFileDialog openAccountFileDialog = new OpenAccountFileDialog(RegisterSuccessOpenAccountActivity.this);
                        openAccountFileDialog.show();
                    }
                })
                .create();
    }

    @OnClick({R.id.iv_back, R.id.tv_open_account, R.id.tv_jump})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_open_account:
                boolean checked1 = cbxDesc1.isChecked();
                boolean checked2 = cbxDesc2.isChecked();
                boolean checked3 = cbxDesc3.isChecked();
                boolean checked4 = cbxDesc4.isChecked();
                boolean checked5 = cbxDesc5.isChecked();
                boolean checked6 = cbxDesc6.isChecked();
                boolean checked7 = cbxDesc7.isChecked();
                boolean checked8 = cbxDesc8.isChecked();
                if (!checked1 || !checked2 || !checked3 || !checked4 || !checked5 || !checked6 || !checked7 || !checked8) {
                    ToastUtils.showToast(this, R.string.str_checked);
//                    return;
                }
                startActivity(new Intent(this, SecuritiesAccountActivity.class));
                break;
            case R.id.tv_jump:
                MainActivity.startMainActivity2(this);
                finish();
                break;
            default:
                break;
        }
    }
}
