package com.mfinance.everjoy.app.pojo;

import java.math.BigDecimal;
import java.util.Optional;

public class ContractDefaultSetting {
    private final BigDecimal defaultLotSize;
    private final Integer defaultSlippage;
    private final Optional<Integer> defaultTakeProfitOrderPips;
    private final Optional<Integer> defaultStopLossOrderPips;

    public ContractDefaultSetting(BigDecimal defaultLotSize, Integer defaultSlippage, Optional<Integer> defaultTakeProfitOrderPips, Optional<Integer> defaultStopLossOrderPips) {
        this.defaultLotSize = defaultLotSize;
        this.defaultSlippage = defaultSlippage;
        this.defaultTakeProfitOrderPips = defaultTakeProfitOrderPips;
        this.defaultStopLossOrderPips = defaultStopLossOrderPips;
    }

    public BigDecimal getDefaultLotSize() {
        return defaultLotSize;
    }

    public Integer getDefaultSlippage() {
        return defaultSlippage;
    }

    public Optional<Integer> getDefaultTakeProfitOrderPips() {
        return defaultTakeProfitOrderPips;
    }

    public Optional<Integer> getDefaultStopLossOrderPips() {
        return defaultStopLossOrderPips;
    }
}
