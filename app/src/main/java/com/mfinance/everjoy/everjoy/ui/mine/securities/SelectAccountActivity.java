package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.AccountDescriptionDialog;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthplaceDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
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

    @BindArray(R.array.array_create_acc_type)
    String[] array_create_acc_type;
    @BindArray(R.array.array_tax_info)
    String[] array_tax_info;
    @BindArray(R.array.country_area)
    String[] array_country_areas;

    @BindView(R.id.ll_createAccType)
    AccountEditorInfoView ll_createAccType;
    @BindView(R.id.cbx_createAccStockHk)
    CheckBox cbx_createAccStockHk;
    @BindView(R.id.cbx_createAccStockUs)
    CheckBox cbx_createAccStockUs;
    @BindView(R.id.cbx_createAccStockA)
    CheckBox cbx_createAccStockA;
    @BindView(R.id.ll_taxInfo)
    AccountEditorInfoView ll_taxInfo;
    @BindView(R.id.ll_taxRegion)
    AccountEditorInfoView ll_taxRegion;
    @BindView(R.id.ll_taxTin)
    AccountEditorInfoView ll_taxTin;
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

    private void saveToSP() {
        SecuritiesSharedPUtils.setCreateAccTypeIndex(createAccTypeIndex);
        SecuritiesSharedPUtils.setCreateAccStockHk(cbx_createAccStockHk.isChecked());
        SecuritiesSharedPUtils.setCreateAccStockUs(cbx_createAccStockUs.isChecked());
        SecuritiesSharedPUtils.setCreateAccStockA(cbx_createAccStockA.isChecked());

        if (taxInfoIndex != -1) {
            SecuritiesSharedPUtils.setTaxInfoIndex(taxInfoIndex);
        }

        if (taxRegionIndex != -1) {
            SecuritiesSharedPUtils.setTaxRegionIndex(taxRegionIndex);
        }

        String taxTinEditorContent = ll_taxTin.getEditorContent();
        if (!TextUtils.isEmpty(taxTinEditorContent)) {
            SecuritiesSharedPUtils.setTaxTin(taxTinEditorContent);
        }

        if (taxRegionW8benIndex != -1) {
            SecuritiesSharedPUtils.setTaxRegionW8benIndex(taxRegionW8benIndex);
        }
    }

    @Override
    protected void initView(View currentView) {
        createAccTypeIndex = SecuritiesSharedPUtils.getCreateAccTypeIndex();
        ll_createAccType.setEditorContent(array_create_acc_type[createAccTypeIndex]);
        ll_createAccType.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showAccount();
            }
        });

        boolean createAccStockHk = SecuritiesSharedPUtils.getCreateAccStockHk();
        cbx_createAccStockHk.setChecked(createAccStockHk);
        boolean createAccStockUs = SecuritiesSharedPUtils.getCreateAccStockUs();
        cbx_createAccStockUs.setChecked(createAccStockUs);
        boolean createAccStockA = SecuritiesSharedPUtils.getCreateAccStockA();
        cbx_createAccStockA.setChecked(createAccStockA);

        taxInfoIndex = SecuritiesSharedPUtils.getTaxInfoIndex();
        if (taxInfoIndex != -1) {
            ll_taxInfo.setEditorContent(array_tax_info[taxInfoIndex]);
        }
        ll_taxInfo.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showTaxationInfo();
            }
        });


        taxRegionIndex = SecuritiesSharedPUtils.getTaxRegionIndex();
        ll_taxRegion.setEditorContent(array_country_areas[taxRegionIndex]);
        ll_taxRegion.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showCountryArea();
            }
        });

        String taxTin = SecuritiesSharedPUtils.getTaxTin();
        if (!TextUtils.isEmpty(taxTin)) {
            ll_taxTin.setEditorContent(taxTin);
        }

        taxRegionW8benIndex = SecuritiesSharedPUtils.getTaxRegionW8benIndex();
        if (taxRegionW8benIndex != -1) {
            ll_w8_country_area.setEditorContent(array_country_areas[taxRegionW8benIndex]);
        }
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

    private int taxRegionW8benIndex = -1;

    /**
     * w8税务居民-国家/地区
     */
    private void showW8CountryArea() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                taxRegionW8benIndex, Arrays.asList(array_country_areas));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                taxRegionW8benIndex = object == null ? 0 : (int) object;
                String content = array_country_areas[taxRegionW8benIndex];
                ll_w8_country_area.setEditorContent(content);
            }
        });
        dialog.show();
    }


    private int taxRegionIndex = -1;
    /**
     * 税务-国家
     */
    private void showCountryArea() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                taxRegionIndex, Arrays.asList(array_country_areas));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                taxRegionIndex = object == null ? 0 : (int) object;
                String content = array_country_areas[taxRegionIndex];
                ll_taxRegion.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int taxInfoIndex = -1;

    /**
     * 税务信息
     */
    private void showTaxationInfo() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                taxInfoIndex, Arrays.asList(array_tax_info));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                taxInfoIndex = object == null ? 0 : (int) object;
                String content = array_tax_info[taxInfoIndex];
                ll_taxInfo.setEditorContent(content);

                // 我不是...，显示国家选择
                ll_taxRegion.setVisibility(taxInfoIndex == 1 ? View.VISIBLE : View.GONE);
            }
        });
        dialog.show();
    }

    private int createAccTypeIndex = 0;

    /**
     * 账户
     */
    private void showAccount() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                createAccTypeIndex, Arrays.asList(array_create_acc_type));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                createAccTypeIndex = object == null ? 0 : (int) object;
                String content = array_create_acc_type[createAccTypeIndex];
                ll_createAccType.setEditorContent(content);
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
                AccountDescriptionDialog accountDescriptionDialog = new AccountDescriptionDialog(this);
                accountDescriptionDialog.show();
                break;
            case R.id.tv_prev:
                onBackPressed();
                break;
            case R.id.tv_next:
                String taxInfoEditorContent = ll_taxInfo.getEditorContent();
                if (TextUtils.isEmpty(taxInfoEditorContent)) {
                    ToastUtils.showToast(this, ll_taxInfo.getLeftAndHintContentForTip());
                    return;
                }

                if (taxInfoIndex == 1) {
                    String taxRegionEditorContent = ll_taxRegion.getEditorContent();
                    if (TextUtils.isEmpty(taxRegionEditorContent)) {
                        ToastUtils.showToast(this, ll_taxRegion.getLeftAndHintContentForTip());
                        return;
                    }
                }

                if (createAccTypeIndex == 0) {
                    if (!cbx_desc1.isChecked()) {
                        ToastUtils.showToast(this, R.string.str_checked);
                        return;
                    }
                    if (!cbx_desc2.isChecked()) {
                        ToastUtils.showToast(this, R.string.str_checked);
                        return;
                    }
                }
                if (cbx_createAccStockA.isChecked()) {
                    if (!cbx_desc2.isChecked()) {
                        ToastUtils.showToast(this, R.string.str_checked);
                        return;
                    }
                }
                if (!cbx_desc3.isChecked()) {
                    ToastUtils.showToast(this, R.string.str_checked);
                    return;
                }
                if (!cbx_desc4.isChecked()) {
                    ToastUtils.showToast(this, R.string.str_checked);
                    return;
                }
                if (!cbx_desc5.isChecked()) {
                    ToastUtils.showToast(this, R.string.str_checked);
                    return;
                }

                // TODO 風險披露聲明
                saveToSP();
                startActivity(new Intent(this, RiskDisclosureActivity.class));
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
}
