package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
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
 * 财务/投资经验
 */
public class FinanceActivity extends BaseViewActivity {


    @BindArray(R.array.array_fin_year_revenue)
    String[] array_fin_year_revenue;
    @BindArray(R.array.array_fin_net_asset_value)
    String[] array_fin_net_asset_value;
    @BindArray(R.array.array_fin_resid_type)
    String[] array_fin_resid_type;
    @BindArray(R.array.array_fin_capital_source)
    String[] array_fin_capital_source;
    @BindArray(R.array.array_fin_asset_type)
    String[] array_fin_asset_type;
    @BindArray(R.array.array_fin_invest_target)
    String[] array_fin_invest_target;
    @BindArray(R.array.array_fin_tolerate_risk)
    String[] array_fin_tolerate_risk;
    @BindArray(R.array.array_fin_invest_avg_year_value)
    String[] array_fin_invest_avg_year_value;
    @BindArray(R.array.array_investment_experience)
    String[] array_investment_experience;


    @BindArray(R.array.array_derivative)
    String[] array_derivative;

    @BindView(R.id.ll_finYearRevenue)
    AccountEditorInfoView ll_finYearRevenue;
    @BindView(R.id.ll_finNetAssetValue)
    AccountEditorInfoView ll_finNetAssetValue;
    @BindView(R.id.ll_finResidType)
    AccountEditorInfoView ll_finResidType;
    @BindView(R.id.ll_finCapitalSource)
    AccountEditorInfoView ll_finCapitalSource;
    @BindView(R.id.ll_finAssetType)
    AccountEditorInfoView ll_finAssetType;
    @BindView(R.id.ll_finInvestTarget)
    AccountEditorInfoView ll_finInvestTarget;
    @BindView(R.id.ll_finTolerateRisk)
    AccountEditorInfoView ll_finTolerateRisk;
    @BindView(R.id.ll_finInvestAvgYearValue)
    AccountEditorInfoView ll_finInvestAvgYearValue;
    @BindView(R.id.ll_finExpStockYear)
    AccountEditorInfoView ll_finExpStockYear;
    @BindView(R.id.ll_finExpDerivativeYear)
    AccountEditorInfoView ll_finExpDerivativeYear;
    @BindView(R.id.ll_finExpFutureYear)
    AccountEditorInfoView ll_finExpFutureYear;
    @BindView(R.id.ll_finExpForexYear)
    AccountEditorInfoView ll_finExpForexYear;
    @BindView(R.id.ll_finExpBondYear)
    AccountEditorInfoView ll_finExpBondYear;
    @BindView(R.id.ll_finExpFoundYear)
    AccountEditorInfoView ll_finExpFoundYear;
    @BindView(R.id.ll_finExpOtherYear)
    AccountEditorInfoView ll_finExpOtherYear;
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


    private void saveToSP() {
        if (finYearRevenueIndex != -1) {
            SecuritiesSharedPUtils.setFinYearRevenue(finYearRevenueIndex);
        }

        if (finNetAssetValueIndex != -1) {
            SecuritiesSharedPUtils.setFinNetAssetValueIndex(finNetAssetValueIndex);
        }

        if (finResidTypeIndex == array_fin_resid_type.length - 1) {
            String finResidTypeEditorContent = ll_finResidType.getEditorContent();
            if (!TextUtils.isEmpty(finResidTypeEditorContent)) {
                SecuritiesSharedPUtils.setFinResidTypeOther(finResidTypeEditorContent);
            }
        } else {
            if (finResidTypeIndex != -1) {
                SecuritiesSharedPUtils.setFinResidTypeIndex(finResidTypeIndex);
            }
        }

        if (finCapitalSourceIndex == array_fin_capital_source.length - 1) {
            String finCapitalSourceEditorContent = ll_finCapitalSource.getEditorContent();
            if (!TextUtils.isEmpty(finCapitalSourceEditorContent)) {
                SecuritiesSharedPUtils.setFinCapitalSourceOther(finCapitalSourceEditorContent);
            }
        } else {
            if (finCapitalSourceIndex != -1) {
                SecuritiesSharedPUtils.setFinCapitalSourceIndex(finCapitalSourceIndex);
            }
        }

        if (finAssetTypeIndex == array_fin_asset_type.length - 1) {
            String finAssetTypeOtherEditorContent = ll_finAssetType.getEditorContent();
            if (!TextUtils.isEmpty(finAssetTypeOtherEditorContent)) {
                SecuritiesSharedPUtils.setFinAssetTypeOther(finAssetTypeOtherEditorContent);
            }
        } else {
            if (finAssetTypeIndex != -1) {
                SecuritiesSharedPUtils.setFinAssetTypeIndex(finAssetTypeIndex);
            }
        }

        if (finInvestTargetIndex == array_fin_invest_target.length - 1) {
            String finInvestTargetEditorContent = ll_finInvestTarget.getEditorContent();
            if (!TextUtils.isEmpty(finInvestTargetEditorContent)) {
                SecuritiesSharedPUtils.setFinInvestTargetOther(finInvestTargetEditorContent);
            }
        } else {
            if (finInvestTargetIndex != -1) {
                SecuritiesSharedPUtils.setFinInvestTargetIndex(finInvestTargetIndex);
            }
        }

        if (finTolerateRiskIndex != -1) {
            SecuritiesSharedPUtils.setFinTolerateRiskIndex(finTolerateRiskIndex);
        }

        if (finInvestAvgYearValueIndex != -1) {
            SecuritiesSharedPUtils.setFinInvestAvgYearValueIndex(finInvestAvgYearValueIndex);
        }

        if (finExpDerivativeYearIndex != -1) {
            SecuritiesSharedPUtils.setFinExpStockYearIndex(finExpStockYearIndex);
        }

        if (finExpDerivativeYearIndex != -1) {
            SecuritiesSharedPUtils.setFinExpDerivativeYearIndex(finExpDerivativeYearIndex);
        }

        if (finExpFutureYearIndex != -1) {
            SecuritiesSharedPUtils.setFinExpFutureYearIndex(finExpFutureYearIndex);
        }

        if (finExpForexYearIndex != -1) {
            SecuritiesSharedPUtils.setFinExpForexYearIndex(finExpForexYearIndex);
        }

        if (finExpBondYearIndex != -1) {
            SecuritiesSharedPUtils.setFinExpForexYearIndex(finExpBondYearIndex);
        }

        if (finExpFoundYearIndex != -1) {
            SecuritiesSharedPUtils.setFinExpFoundYearIndex(finExpFoundYearIndex);
        }

        if (finExpOtherYearIndex != -1) {
            SecuritiesSharedPUtils.setFinExpFoundYearIndex(finExpOtherYearIndex);
        }
    }

    @Override
    protected void initView(View currentView) {
        finYearRevenueIndex = SecuritiesSharedPUtils.getFinYearRevenue();
        if (finYearRevenueIndex != -1) {
            ll_finYearRevenue.setEditorContent(array_fin_year_revenue[finYearRevenueIndex]);
        }
        ll_finYearRevenue.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showSelectRevenue();
            }
        });

        finNetAssetValueIndex = SecuritiesSharedPUtils.getFinNetAssetValueIndex();
        if (finNetAssetValueIndex != -1) {
            ll_finNetAssetValue.setEditorContent(array_fin_net_asset_value[finNetAssetValueIndex]);
        }
        ll_finNetAssetValue.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showAssetValue();
            }
        });

        finResidTypeIndex = SecuritiesSharedPUtils.getFinResidTypeIndex();
        if (finResidTypeIndex == array_fin_resid_type.length - 1) {
            String finResidTypeOther = SecuritiesSharedPUtils.getFinResidTypeOther();
            if (!TextUtils.isEmpty(finResidTypeOther)) {
                ll_finResidType.setEditorContent(finResidTypeOther);
            }
        } else {
            if (finResidTypeIndex != -1) {
                ll_finResidType.setEditorContent(array_fin_resid_type[finResidTypeIndex]);
            }
        }
        ll_finResidType.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showHouse();
            }
        });

        finCapitalSourceIndex = SecuritiesSharedPUtils.getFinResidTypeIndex();
        if (finCapitalSourceIndex == array_fin_capital_source.length - 1) {
            String finCapitalSourceOther = SecuritiesSharedPUtils.getFinCapitalSourceOther();
            if (!TextUtils.isEmpty(finCapitalSourceOther)) {
                ll_finCapitalSource.setEditorContent(finCapitalSourceOther);
            }
        } else {
            if (finCapitalSourceIndex != -1) {
                ll_finCapitalSource.setEditorContent(array_fin_capital_source[finCapitalSourceIndex]);
            }
        }
        ll_finCapitalSource.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showSourceFunds();
            }
        });

        finAssetTypeIndex = SecuritiesSharedPUtils.getFinAssetTypeIndex();
        if (finAssetTypeIndex == array_fin_asset_type.length - 1) {
            String finAssetTypeEditorContent = ll_finAssetType.getEditorContent();
            if (!TextUtils.isEmpty(finAssetTypeEditorContent)) {
                ll_finAssetType.setEditorContent(finAssetTypeEditorContent);
            }
        } else {
            if (finAssetTypeIndex != -1) {
                ll_finAssetType.setEditorContent(array_fin_asset_type[this.finAssetTypeIndex]);
            }
        }
        ll_finAssetType.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showFundsType();
            }
        });

        finInvestTargetIndex = SecuritiesSharedPUtils.getFinInvestTargetIndex();
        if (finInvestTargetIndex == array_fin_invest_target.length - 1) {
            String finInvestTargetEditorContent = ll_finInvestTarget.getEditorContent();
            if (!TextUtils.isEmpty(finInvestTargetEditorContent)) {
                ll_finAssetType.setEditorContent(finInvestTargetEditorContent);
            }
        } else {
            if (finInvestTargetIndex != -1) {
                ll_finInvestTarget.setEditorContent(array_fin_invest_target[finInvestTargetIndex]);
            }
        }
        ll_finInvestTarget.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showInvestmentTarget();
            }
        });

        finTolerateRiskIndex = SecuritiesSharedPUtils.getFinTolerateRiskIndex();
        if (finTolerateRiskIndex != -1) {
            ll_finTolerateRisk.setEditorContent(array_fin_tolerate_risk[finTolerateRiskIndex]);
        }
        ll_finTolerateRisk.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showTakingRisks();
            }
        });

        finInvestAvgYearValueIndex = SecuritiesSharedPUtils.getFinInvestAvgYearValueIndex();
        if (finInvestAvgYearValueIndex != -1) {
            ll_finInvestAvgYearValue.setEditorContent(array_fin_invest_avg_year_value[finInvestAvgYearValueIndex]);
        }
        ll_finInvestAvgYearValue.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showInvestmentQuota();
            }
        });

        // 职业:•	學生: 一年至三年經驗(没有学生，这里不做判断)
        //•	其他: 三年至五年經驗
//        int workPosIndex = SecuritiesSharedPUtils.getWorkPos();
        finExpStockYearIndex = SecuritiesSharedPUtils.getFinExpStockYearIndex();
        if (finExpStockYearIndex != -1) {
            ll_finExpStockYear.setEditorContent(array_investment_experience[finExpStockYearIndex]);
        }
        ll_finExpStockYear.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showShares();
            }
        });

        finExpDerivativeYearIndex = SecuritiesSharedPUtils.getFinExpStockYearIndex();
        if (finExpDerivativeYearIndex != -1) {
            ll_finExpDerivativeYear.setEditorContent(array_investment_experience[finExpDerivativeYearIndex]);
        }
        ll_finExpDerivativeYear.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showDerivativeTools();
            }
        });

        finExpFutureYearIndex = SecuritiesSharedPUtils.getFinExpFutureYearIndex();
        if (finExpFutureYearIndex != -1) {
            ll_finExpFutureYear.setEditorContent(array_investment_experience[finExpFutureYearIndex]);
        }
        ll_finExpFutureYear.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showFuturesOptions();
            }
        });

        finExpForexYearIndex = SecuritiesSharedPUtils.getFinExpForexYearIndex();
        if (finExpForexYearIndex != -1) {
            ll_finExpForexYear.setEditorContent(array_investment_experience[finExpForexYearIndex]);
        }
        ll_finExpForexYear.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showForexPrecious();
            }
        });

        finExpBondYearIndex = SecuritiesSharedPUtils.getFinExpBondYearIndex();
        if (finExpBondYearIndex != -1) {
            ll_finExpBondYear.setEditorContent(array_investment_experience[finExpBondYearIndex]);
        }
        ll_finExpBondYear.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showBondsCertificates();
            }
        });

        finExpFoundYearIndex = SecuritiesSharedPUtils.getFinExpFoundYearIndex();
        if (finExpFoundYearIndex != -1) {
            ll_finExpFoundYear.setEditorContent(array_investment_experience[finExpFoundYearIndex]);
        }
        ll_finExpFoundYear.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showUnitFund();
            }
        });

        finExpOtherYearIndex = SecuritiesSharedPUtils.getFinExpOtherYearIndex();
        if (finExpOtherYearIndex != -1) {
            ll_finExpOtherYear.setEditorContent(array_investment_experience[finExpOtherYearIndex]);
        }
        ll_finExpOtherYear.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
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

    private int finExpOtherYearIndex = -1;

    /**
     * 其他
     */
    private void showOther() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finExpOtherYearIndex, Arrays.asList(array_investment_experience));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finExpOtherYearIndex = object == null ? 0 : (int) object;
                String content = array_investment_experience[finExpOtherYearIndex];
                ll_finExpOtherYear.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int finExpFoundYearIndex = -1;

    /**
     * 单位信托基金/基金
     */
    private void showUnitFund() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finExpFoundYearIndex, Arrays.asList(array_investment_experience));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finExpFoundYearIndex = object == null ? 0 : (int) object;
                String content = array_investment_experience[finExpFoundYearIndex];
                ll_finExpFoundYear.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int finExpBondYearIndex = -1;

    /**
     * 债券/存款证
     */
    private void showBondsCertificates() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finExpBondYearIndex, Arrays.asList(array_investment_experience));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finExpBondYearIndex = object == null ? 0 : (int) object;
                String content = array_investment_experience[finExpBondYearIndex];
                ll_finExpBondYear.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int finExpForexYearIndex = -1;

    /**
     * 外汇/贵金属
     */
    private void showForexPrecious() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finExpForexYearIndex, Arrays.asList(array_investment_experience));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finExpForexYearIndex = object == null ? 0 : (int) object;
                String content = array_investment_experience[finExpForexYearIndex];
                ll_finExpForexYear.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int finExpFutureYearIndex = 0;

    /**
     * 期货期权
     */
    private void showFuturesOptions() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finExpFutureYearIndex, Arrays.asList(array_investment_experience));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finExpFutureYearIndex = object == null ? 0 : (int) object;
                String content = array_investment_experience[finExpFutureYearIndex];
                ll_finExpFutureYear.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int finExpDerivativeYearIndex = -1;

    /**
     * 衍生工具
     */
    private void showDerivativeTools() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, finExpDerivativeYearIndex, Arrays.asList(array_investment_experience));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finExpDerivativeYearIndex = object == null ? 0 : (int) object;
                String content = array_investment_experience[finExpDerivativeYearIndex];
                ll_finExpDerivativeYear.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int finExpStockYearIndex = -1;

    /**
     * 股票经验
     */
    private void showShares() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finExpStockYearIndex, Arrays.asList(array_investment_experience));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finExpStockYearIndex = object == null ? 0 : (int) object;
                String content = array_investment_experience[finExpStockYearIndex];
                ll_finExpStockYear.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int finInvestAvgYearValueIndex = -1;

    /**
     * 平均年投资额
     */
    private void showInvestmentQuota() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finInvestAvgYearValueIndex, Arrays.asList(array_fin_invest_avg_year_value));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finInvestAvgYearValueIndex = object == null ? 0 : (int) object;
                String content = array_fin_invest_avg_year_value[finInvestAvgYearValueIndex];
                ll_finInvestAvgYearValue.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int finTolerateRiskIndex = 0;

    private void showTakingRisks() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finTolerateRiskIndex, Arrays.asList(array_fin_tolerate_risk));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finTolerateRiskIndex = object == null ? 0 : (int) object;
                String content = array_fin_tolerate_risk[finTolerateRiskIndex];
                ll_finTolerateRisk.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int finInvestTargetIndex = -1;

    /**
     * 投资目标
     */
    private void showInvestmentTarget() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finInvestTargetIndex, Arrays.asList(array_fin_invest_target));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finInvestTargetIndex = object == null ? 0 : (int) object;
                if (finInvestTargetIndex == array_fin_invest_target.length - 1) {
                    ll_finInvestTarget.setEditor(true);
                } else {
                    String content = array_fin_invest_target[finInvestTargetIndex];
                    ll_finInvestTarget.setEditorContent(content);
                }
            }
        });
        dialog.show();
    }

    private int finAssetTypeIndex = -1;

    private void showFundsType() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finAssetTypeIndex, Arrays.asList(array_fin_asset_type));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finAssetTypeIndex = object == null ? 0 : (int) object;
                if (finAssetTypeIndex == array_fin_asset_type.length - 1) {
                    ll_finAssetType.setEditor(true);
                } else {
                    ll_finAssetType.setEditor(false);
                    String content = array_fin_asset_type[finAssetTypeIndex];
                    ll_finAssetType.setEditorContent(content);
                }
            }
        });
        dialog.show();
    }

    private int finCapitalSourceIndex = -1;

    /**
     * 资金来源
     */
    private void showSourceFunds() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finCapitalSourceIndex, Arrays.asList(array_fin_capital_source));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finCapitalSourceIndex = object == null ? 0 : (int) object;
                if (finCapitalSourceIndex == array_fin_capital_source.length - 1) {
                    ll_finCapitalSource.setEditor(true);
                } else {
                    ll_finCapitalSource.setEditor(false);
                    String content = array_fin_capital_source[finCapitalSourceIndex];
                    ll_finCapitalSource.setEditorContent(content);
                }
            }
        });
        dialog.show();
    }

    private int finResidTypeIndex = -1;

    /**
     * 住宅类别
     */
    private void showHouse() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, finResidTypeIndex, Arrays.asList(array_fin_resid_type));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finResidTypeIndex = object == null ? 0 : (int) object;
                if (finResidTypeIndex == array_fin_resid_type.length - 1) {
                    ll_finResidType.setEditor(true);
                } else {
                    ll_finResidType.setEditor(false);
                    String content = array_fin_resid_type[finResidTypeIndex];
                    ll_finResidType.setEditorContent(content);
                }
            }
        });
        dialog.show();
    }


    private int finNetAssetValueIndex = -1;

    /**
     * 资产净值
     */
    private void showAssetValue() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this,
                finNetAssetValueIndex, Arrays.asList(array_fin_net_asset_value));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finNetAssetValueIndex = object == null ? 0 : (int) object;
                String content = array_fin_net_asset_value[finNetAssetValueIndex];
                ll_finNetAssetValue.setEditorContent(content);
            }
        });
        dialog.show();
    }


    private int finYearRevenueIndex = -1;

    /**
     * 选择年收入
     */
    private void showSelectRevenue() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, finYearRevenueIndex, Arrays.asList(array_fin_year_revenue));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                finYearRevenueIndex = object == null ? 0 : (int) object;
                String content = array_fin_year_revenue[finYearRevenueIndex];
                ll_finYearRevenue.setEditorContent(content);
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
                String finYearRevenueEditorContent = ll_finYearRevenue.getEditorContent();
                if (TextUtils.isEmpty(finYearRevenueEditorContent)) {
                    ToastUtils.showToast(this, ll_finYearRevenue.getLeftAndHintContentForTip());
                    return;
                }
                String finNetAssetValueEditorContent = ll_finNetAssetValue.getEditorContent();
                if (TextUtils.isEmpty(finNetAssetValueEditorContent)) {
                    ToastUtils.showToast(this, ll_finNetAssetValue.getLeftAndHintContentForTip());
                    return;
                }
                String finResidTypeEditorContent = ll_finResidType.getEditorContent();
                if (TextUtils.isEmpty(finResidTypeEditorContent)) {
                    ToastUtils.showToast(this, ll_finResidType.getLeftAndHintContentForTip());
                    return;
                }
                String finCapitalSourceEditorContent = ll_finCapitalSource.getEditorContent();
                if (TextUtils.isEmpty(finCapitalSourceEditorContent)) {
                    ToastUtils.showToast(this, ll_finCapitalSource.getLeftAndHintContentForTip());
                    return;
                }
                String finAssetTypeEditorContent = ll_finAssetType.getEditorContent();
                if (TextUtils.isEmpty(finAssetTypeEditorContent)) {
                    ToastUtils.showToast(this, ll_finAssetType.getLeftAndHintContentForTip());
                    return;
                }
                String finInvestTargetEditorContent = ll_finInvestTarget.getEditorContent();
                if (TextUtils.isEmpty(finInvestTargetEditorContent)) {
                    ToastUtils.showToast(this, ll_finInvestTarget.getLeftAndHintContentForTip());
                    return;
                }
                String finTolerateRiskEditorContent = ll_finTolerateRisk.getEditorContent();
                if (TextUtils.isEmpty(finTolerateRiskEditorContent)) {
                    ToastUtils.showToast(this, ll_finTolerateRisk.getLeftAndHintContentForTip());
                    return;
                }
                String finInvestAvgYearValueEditorContent = ll_finInvestAvgYearValue.getEditorContent();
                if (TextUtils.isEmpty(finInvestAvgYearValueEditorContent)) {
                    ToastUtils.showToast(this, ll_finInvestAvgYearValue.getLeftAndHintContentForTip());
                    return;
                }
                String finExpStockYearEditorContent = ll_finExpStockYear.getEditorContent();
                if (TextUtils.isEmpty(finExpStockYearEditorContent)) {
                    ToastUtils.showToast(this, ll_finExpStockYear.getLeftAndHintContentForTip() + getString(R.string.af_investment_experience));
                    return;
                }
                String finExpDerivativeYearEditorContent = ll_finExpDerivativeYear.getEditorContent();
                if (TextUtils.isEmpty(finExpDerivativeYearEditorContent)) {
                    ToastUtils.showToast(this, ll_finExpDerivativeYear.getLeftAndHintContentForTip() + getString(R.string.af_investment_experience));
                    return;
                }
                String finExpFutureYearEditorContent = ll_finExpFutureYear.getEditorContent();
                if (TextUtils.isEmpty(finExpFutureYearEditorContent)) {
                    ToastUtils.showToast(this, ll_finExpFutureYear.getLeftAndHintContentForTip() + getString(R.string.af_investment_experience));
                    return;
                }
                String finExpForexYearEditorContent = ll_finExpForexYear.getEditorContent();
                if (TextUtils.isEmpty(finExpForexYearEditorContent)) {
                    ToastUtils.showToast(this, ll_finExpForexYear.getLeftAndHintContentForTip() + getString(R.string.af_investment_experience));
                    return;
                }
                String finExpBondYearEditorContent = ll_finExpBondYear.getEditorContent();
                if (TextUtils.isEmpty(finExpBondYearEditorContent)) {
                    ToastUtils.showToast(this, ll_finExpBondYear.getLeftAndHintContentForTip() + getString(R.string.af_investment_experience));
                    return;
                }
                String finExpFoundYearEditorContent = ll_finExpFoundYear.getEditorContent();
                if (TextUtils.isEmpty(finExpFoundYearEditorContent)) {
                    ToastUtils.showToast(this, ll_finExpFoundYear.getLeftAndHintContentForTip() + getString(R.string.af_investment_experience));
                    return;
                }
                String finExpOtherYearEditorContent = ll_finExpOtherYear.getEditorContent();
                if (TextUtils.isEmpty(finExpOtherYearEditorContent)) {
                    ToastUtils.showToast(this, ll_finExpOtherYear.getLeftAndHintContentForTip() + getString(R.string.af_investment_experience));
                    return;
                }
                String derivativeEditorContent = ll_derivative.getEditorContent();
                if (TextUtils.isEmpty(derivativeEditorContent)) {
                    ToastUtils.showToast(this, ll_finExpOtherYear.getLeftAndHintContentForTip());
                    return;
                }
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

}
