package com.mfinance.everjoy.app.pojo;

import java.math.BigDecimal;
import java.util.Optional;

public class ContractDefaultSettingBuilder {
    private BigDecimal defaultLotSize;
    private Integer defaultSlippage;
    private Optional<Integer> defaultTakeProfitOrderPips;
    private Optional<Integer> defaultStopLossOrderPips;

    public ContractDefaultSettingBuilder() {

    }

    public ContractDefaultSettingBuilder(ContractDefaultSetting t) {
        defaultLotSize = t.getDefaultLotSize();
        defaultSlippage = t.getDefaultSlippage();
        defaultTakeProfitOrderPips = t.getDefaultTakeProfitOrderPips();
        defaultStopLossOrderPips = t.getDefaultStopLossOrderPips();
    }

    public ContractDefaultSettingBuilder setDefaultLotSize(BigDecimal defaultLotSize) {
        this.defaultLotSize = defaultLotSize;
        return this;
    }

    public ContractDefaultSettingBuilder setDefaultSlippage(Integer defaultSlippage) {
        this.defaultSlippage = defaultSlippage;
        return this;
    }

    public ContractDefaultSettingBuilder setDefaultTakeProfitOrderPips(Optional<Integer> defaultTakeProfitOrderPips) {
        this.defaultTakeProfitOrderPips = defaultTakeProfitOrderPips;
        return this;
    }

    public ContractDefaultSettingBuilder setDefaultStopLossOrderPips(Optional<Integer> defaultStopLossOrderPips) {
        this.defaultStopLossOrderPips = defaultStopLossOrderPips;
        return this;
    }

    public ContractDefaultSetting createContractDefaultSetting() {
        return new ContractDefaultSetting(defaultLotSize, defaultSlippage, defaultTakeProfitOrderPips, defaultStopLossOrderPips);
    }
}