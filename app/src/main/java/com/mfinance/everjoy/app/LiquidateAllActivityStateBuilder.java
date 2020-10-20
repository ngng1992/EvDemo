package com.mfinance.everjoy.app;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

public class LiquidateAllActivityStateBuilder {
    private int slippage;
    private LiquidateAllActivity.LiquidationMethod liquidationMethod;
    private LiquidateAllActivity.BuySellAll buySellAll;
    private String selectedContract;
    private boolean selectedAllContract;
    private String pid;
    private boolean allowSelectMultiple;
    private BigDecimal selectMultipleAmount;
    private Set<Integer> selectedPositionIds;
    private boolean selectMultipleShortcutAll;

    public LiquidateAllActivityStateBuilder() {
        pid = "";
        allowSelectMultiple = false;
        selectedPositionIds = Collections.EMPTY_SET;
        selectMultipleAmount = BigDecimal.ZERO;
        selectMultipleShortcutAll = false;
    }

    public LiquidateAllActivityStateBuilder(LiquidateAllActivityState t) {
        slippage = t.slippage;
        liquidationMethod = t.liquidationMethod;
        buySellAll = t.buySellAll;
        selectedContract = t.selectedContract;
        selectedAllContract = t.selectedAllContract;
        pid = t.pid;
        allowSelectMultiple = t.allowSelectMultiple;
        selectedPositionIds = t.selectedPositionIds;
        selectMultipleAmount = t.selectMultipleAmount;
        selectMultipleShortcutAll = t.selectMultipleShortcutAll;
    }

    public LiquidateAllActivityStateBuilder setSlippage(int slippage) {
        this.slippage = slippage;
        return this;
    }

    public LiquidateAllActivityStateBuilder setLiquidationMethod(LiquidateAllActivity.LiquidationMethod liquidationMethod) {
        this.liquidationMethod = liquidationMethod;
        return this;
    }

    public LiquidateAllActivityStateBuilder setBuySellAll(LiquidateAllActivity.BuySellAll buySellAll) {
        this.buySellAll = buySellAll;
        return this;
    }

    public LiquidateAllActivityStateBuilder setSelectedContract(String selectedContract) {
        this.selectedContract = selectedContract;
        return this;
    }

    public LiquidateAllActivityStateBuilder setSelectedAllContract(boolean selectedAllContract) {
        this.selectedAllContract = selectedAllContract;
        return this;
    }

    public LiquidateAllActivityStateBuilder setPid(String pid) {
        this.pid = pid;
        return this;
    }

    public LiquidateAllActivityStateBuilder setAllowSelectMultiple(boolean allowSelectMultiple) {
        this.allowSelectMultiple = allowSelectMultiple;
        return this;
    }

    public LiquidateAllActivityStateBuilder setSelectedPositionIds(Set<Integer> selectedPositionIds) {
        this.selectedPositionIds = selectedPositionIds;
        return this;
    }

    public LiquidateAllActivityStateBuilder setSelectMultipleAmount(BigDecimal selectMultipleAmount) {
        this.selectMultipleAmount = selectMultipleAmount;
        return this;
    }

    public LiquidateAllActivityStateBuilder setSelectMultipleShortcutAll(boolean selectMultipleShortcutAll) {
        this.selectMultipleShortcutAll = selectMultipleShortcutAll;
        return this;
    }

    public LiquidateAllActivityState build() {
        return new LiquidateAllActivityState(slippage, liquidationMethod, buySellAll, selectedContract, selectedAllContract, pid, allowSelectMultiple, selectedPositionIds, selectMultipleAmount, selectMultipleShortcutAll);
    }
}