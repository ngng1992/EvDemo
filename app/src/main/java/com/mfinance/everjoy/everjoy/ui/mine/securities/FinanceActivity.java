package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthplaceDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;
import com.mfinance.everjoy.everjoy.view.AccountEditorInfoView;

import net.mfinance.commonlib.view.StringTextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 财务/投资经验
 */
public class FinanceActivity extends BaseViewActivity {


    @BindArray(R.array.array_derivative)
    String[] array_derivative;

    @BindView(R.id.ll_revenue)
    AccountEditorInfoView ll_revenue;
    @BindView(R.id.ll_asset_value)
    AccountEditorInfoView ll_asset_value;
    @BindView(R.id.ll_af_house)
    AccountEditorInfoView ll_af_house;
    @BindView(R.id.ll_source_funds)
    AccountEditorInfoView ll_source_funds;
    @BindView(R.id.ll_funds_type)
    AccountEditorInfoView ll_funds_type;
    @BindView(R.id.ll_investment_target)
    AccountEditorInfoView ll_investment_target;
    @BindView(R.id.ll_taking_risks)
    AccountEditorInfoView ll_taking_risks;
    @BindView(R.id.ll_investment_quota)
    AccountEditorInfoView ll_investment_quota;
    @BindView(R.id.ll_shares)
    AccountEditorInfoView ll_shares;
    @BindView(R.id.ll_derivative_tools)
    AccountEditorInfoView ll_derivative_tools;
    @BindView(R.id.ll_futures_options)
    AccountEditorInfoView ll_futures_options;
    @BindView(R.id.ll_forex_precious)
    AccountEditorInfoView ll_forex_precious;
    @BindView(R.id.ll_bonds_certificates)
    AccountEditorInfoView ll_bonds_certificates;
    @BindView(R.id.ll_unit_fund)
    AccountEditorInfoView ll_unit_fund;
    @BindView(R.id.ll_other)
    AccountEditorInfoView ll_other;
    @BindView(R.id.ll_derivative)
    AccountEditorInfoView ll_derivative;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.tv_prev)
    TextView tvPrev;


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
        return R.layout.activity_finance;
    }

    @Override
    protected void initView(View currentView) {
        ll_revenue.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showSelectRevenue();
            }
        });
        ll_asset_value.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showAssetValue();
            }
        });
        ll_af_house.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showHouse();
            }
        });
        ll_source_funds.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showSourceFunds();
            }
        });
        ll_funds_type.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showFundsType();
            }
        });
        ll_investment_target.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showInvestmentTarget();
            }
        });
        ll_taking_risks.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showTakingRisks();
            }
        });
        ll_investment_quota.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showInvestmentQuota();
            }
        });
        ll_shares.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showShares();
            }
        });
        ll_derivative_tools.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showDerivativeTools();
            }
        });
        ll_futures_options.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showFuturesOptions();
            }
        });
        ll_forex_precious.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showForexPrecious();
            }
        });
        ll_bonds_certificates.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showBondsCertificates();
            }
        });
        ll_unit_fund.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showUnitFund();
            }
        });
        ll_other.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showOther();
            }
        });
        ll_derivative.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showDerivative();
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
                        ContactActivity.startContactActivity(FinanceActivity.this);
                    }
                })
                .create();
    }

    private int selectDerivativeIndex = 0;

    private void showDerivative() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectDerivativeIndex, Arrays.asList(array_derivative));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectDerivativeIndex = object == null ? 0 : (int) object;
                String content = array_derivative[selectDerivativeIndex];
                ll_derivative.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectOtherIndex = 0;

    /**
     * 其他
     */
    private void showOther() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_investment_experience));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectOtherIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectOtherIndex = object == null ? 0 : (int) object;
                String content = list.get(selectOtherIndex);
                ll_other.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectUnitFundIndex = 0;

    /**
     * 单位信托基金/基金
     */
    private void showUnitFund() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_investment_experience));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectUnitFundIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectUnitFundIndex = object == null ? 0 : (int) object;
                String content = list.get(selectUnitFundIndex);
                ll_unit_fund.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectBondsCertificatesIndex = 0;

    /**
     * 债券/存款证
     */
    private void showBondsCertificates() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_investment_experience));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectBondsCertificatesIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectBondsCertificatesIndex = object == null ? 0 : (int) object;
                String content = list.get(selectBondsCertificatesIndex);
                ll_bonds_certificates.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectForexPreciousIndex = 0;

    /**
     * 外汇/贵金属
     */
    private void showForexPrecious() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_investment_experience));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectForexPreciousIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectForexPreciousIndex = object == null ? 0 : (int) object;
                String content = list.get(selectForexPreciousIndex);
                ll_forex_precious.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectFuturesOptionsIndex = 0;

    /**
     * 期货期权
     */
    private void showFuturesOptions() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_investment_experience));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectFuturesOptionsIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectFuturesOptionsIndex = object == null ? 0 : (int) object;
                String content = list.get(selectFuturesOptionsIndex);
                ll_futures_options.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectDerivativesIndex = 0;

    /**
     * 衍生工具
     */
    private void showDerivativeTools() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_investment_experience));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectDerivativesIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectDerivativesIndex = object == null ? 0 : (int) object;
                String content = list.get(selectDerivativesIndex);
                ll_derivative_tools.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectSharesIndex = 0;

    /**
     * 股票经验
     */
    private void showShares() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_investment_experience));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectSharesIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectSharesIndex = object == null ? 0 : (int) object;
                String content = list.get(selectSharesIndex);
                ll_shares.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectInvestmentQuotaIndex = 0;

    /**
     * 平均年投资额
     */
    private void showInvestmentQuota() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_investment_quota));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectInvestmentQuotaIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectInvestmentQuotaIndex = object == null ? 0 : (int) object;
                String content = list.get(selectInvestmentQuotaIndex);
                ll_investment_quota.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectTakingRisksIndex = 0;

    private void showTakingRisks() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_taking_risks));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectTakingRisksIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectTakingRisksIndex = object == null ? 0 : (int) object;
                String content = list.get(selectTakingRisksIndex);
                ll_taking_risks.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectInvestmentTargetIndex = 0;

    /**
     * 投资目标
     */
    private void showInvestmentTarget() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_investment_target));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectInvestmentTargetIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectInvestmentTargetIndex = object == null ? 0 : (int) object;
                String content = list.get(selectInvestmentTargetIndex);
                ll_investment_target.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectFundsTypeIndex = 0;

    private void showFundsType() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_funds_type));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectFundsTypeIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectFundsTypeIndex = object == null ? 0 : (int) object;
                String content = list.get(selectFundsTypeIndex);
                ll_funds_type.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectSourceFundsIndex = 0;

    /**
     * 资金来源
     */
    private void showSourceFunds() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_source_funds));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectSourceFundsIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectSourceFundsIndex = object == null ? 0 : (int) object;
                String content = list.get(selectSourceFundsIndex);
                ll_source_funds.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int selectHouseIndex = 0;

    /**
     * 住宅类别
     */
    private void showHouse() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_house));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectHouseIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectHouseIndex = object == null ? 0 : (int) object;
                String content = list.get(selectHouseIndex);
                ll_af_house.setEditorContent(content);
            }
        });
        dialog.show();
    }


    private int selectAssetValueIndex = 0;

    /**
     * 资产净值
     */
    private void showAssetValue() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_asset_value));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectAssetValueIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectAssetValueIndex = object == null ? 0 : (int) object;
                String content = list.get(selectAssetValueIndex);
                ll_asset_value.setEditorContent(content);
            }
        });
        dialog.show();
    }


    private int selectRevenueIndex = 0;

    /**
     * 选择年收入
     */
    private void showSelectRevenue() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_revenue));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectRevenueIndex, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectRevenueIndex = object == null ? 0 : (int) object;
                String content = list.get(selectRevenueIndex);
                ll_revenue.setEditorContent(content);
            }
        });
        dialog.show();
    }


    @OnClick({R.id.iv_back, R.id.tv_prev, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_prev:
                onBackPressed();
                break;
            case R.id.tv_next:
                // 选择账户
                saveToSP();
                startActivity(new Intent(this, SelectAccountActivity.class));
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
        // 保存到本地

    }
}
