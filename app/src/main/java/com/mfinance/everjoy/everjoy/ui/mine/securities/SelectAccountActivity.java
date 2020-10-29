package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthplaceDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;
import com.mfinance.everjoy.everjoy.view.AccountEditorInfoView;

import net.mfinance.commonlib.toast.ToastUtils;
import net.mfinance.commonlib.view.StringTextView;

import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择账户
 */
public class SelectAccountActivity extends BaseViewActivity {

    @BindArray(R.array.array_account)
    String[] accounts;
    @BindArray(R.array.array_taxation_info)
    String[] array_taxation_infos;
    @BindArray(R.array.country_area)
    String[] country_areas;

    @BindView(R.id.ll_account)
    AccountEditorInfoView ll_account;
    @BindView(R.id.cbx_ch_stock)
    CheckBox cbx_ch_stock;
    @BindView(R.id.ll_taxation_info)
    AccountEditorInfoView ll_taxation_info;
    @BindView(R.id.ll_country_area)
    AccountEditorInfoView ll_country_area;
    @BindView(R.id.ll_taxation_code)
    AccountEditorInfoView ll_taxation_code;
    @BindView(R.id.ll_w8_country_area)
    AccountEditorInfoView ll_w8_country_area;
    @BindView(R.id.tv_account_desc)
    TextView tvAccountDesc;
    @BindView(R.id.cbx_desc1)
    CheckBox cbx_desc1;
    @BindView(R.id.cbx_desc2)
    CheckBox cbx_desc2;
    @BindView(R.id.cbx_desc3)
    CheckBox cbx_desc3;
    @BindView(R.id.cbx_desc4)
    CheckBox cbx_desc4;
    @BindView(R.id.cbx_desc5)
    CheckBox cbx_desc5;
    @BindView(R.id.tv_contact)
    TextView tvContact;

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
        return R.layout.activity_select_account;
    }

    @Override
    protected void initView(View currentView) {
        ll_account.setEditorContent(accounts[0]);
        ll_account.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showAccount();
            }
        });
        ll_taxation_info.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showTaxationInfo();
            }
        });
        ll_country_area.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showCountryArea();
            }
        });
        String editorContent = ll_taxation_code.getEditorContent();
        ll_w8_country_area.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showW8CountryArea();
            }
        });

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
                        ContactActivity.startContactActivity(SelectAccountActivity.this);
                    }
                })
                .create();
    }

    private int selectW8CountryAreaIndex = 0;

    /**
     * w8税务居民-国家/地区
     */
    private void showW8CountryArea() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectW8CountryAreaIndex, Arrays.asList(country_areas));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectW8CountryAreaIndex = object == null ? 0 : (int) object;
                String content = country_areas[selectW8CountryAreaIndex];
                ll_w8_country_area.setEditorContent(content);
            }
        });
        dialog.show();
    }


    private int selectCountryAreaIndex = 0;

    /**
     * 税务-国家
     */
    private void showCountryArea() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectCountryAreaIndex, Arrays.asList(country_areas));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectCountryAreaIndex = object == null ? 0 : (int) object;
                String content = country_areas[selectCountryAreaIndex];
                ll_country_area.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectTaxationInfoIndex = 0;

    /**
     * 税务信息
     */
    private void showTaxationInfo() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectTaxationInfoIndex, Arrays.asList(array_taxation_infos));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectTaxationInfoIndex = object == null ? 0 : (int) object;
                String content = array_taxation_infos[selectTaxationInfoIndex];
                ll_taxation_info.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectAccountIndex = 0;

    /**
     * 账户
     */
    private void showAccount() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectAccountIndex, Arrays.asList(accounts));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectAccountIndex = object == null ? 0 : (int) object;
                String content = accounts[selectAccountIndex];
                ll_account.setEditorContent(content);
            }
        });
        dialog.show();
    }

    @OnClick({R.id.iv_back, R.id.tv_account_desc, R.id.tv_prev, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
                case R.id.tv_account_desc:
                // TODO 彈出券商預設帳戶說明內文
                break;
            case R.id.tv_prev:
                 onBackPressed();
                break;
            case R.id.tv_next:
                showNextInfo();
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

    /**
     * 保存到本地
     */
    private void saveToSP() {

    }

    private void showNextInfo() {
        if (selectAccountIndex == 0) {
            if (!cbx_desc1.isChecked()) {
                ToastUtils.showToast(this, R.string.asa_error_info);
                return;
            }
            if (!cbx_desc2.isChecked()) {
                ToastUtils.showToast(this, R.string.asa_error_info);
                return;
            }
        }
        if (cbx_ch_stock.isChecked()) {
            if (!cbx_desc2.isChecked()) {
                ToastUtils.showToast(this, R.string.asa_error_info);
                return;
            }
        }
        // TODO 風險披露聲明
        saveToSP();
        startActivity(new Intent(this, RiskDisclosureActivity.class));
    }
}
