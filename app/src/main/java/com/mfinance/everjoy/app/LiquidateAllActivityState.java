package com.mfinance.everjoy.app;

import java.math.BigDecimal;
import java.util.Set;

class LiquidateAllActivityState {
    LiquidateAllActivityState(int slippage, LiquidateAllActivity.LiquidationMethod liquidationMethod, LiquidateAllActivity.BuySellAll buySellAll, String selectedContract, boolean selectedAllContract, String pid, boolean allowSelectMultiple, Set<Integer> selectedPositionIds, BigDecimal selectMultipleAmount, boolean selectMultipleShortcutAll) {
        this.slippage = slippage;
        this.liquidationMethod = liquidationMethod;
        this.buySellAll = buySellAll;
        this.selectedContract = selectedContract;
        this.selectedAllContract = selectedAllContract;
        this.pid = pid;
        this.allowSelectMultiple = allowSelectMultiple;
        this.selectedPositionIds = selectedPositionIds;
        this.selectMultipleAmount = selectMultipleAmount;
        this.selectMultipleShortcutAll = selectMultipleShortcutAll;
    }
    public final int slippage;
    public final LiquidateAllActivity.LiquidationMethod liquidationMethod;
    public final LiquidateAllActivity.BuySellAll buySellAll;
    public final String selectedContract;
    public final boolean selectedAllContract;
    public final String pid;
    public final boolean allowSelectMultiple;
    public final Set<Integer> selectedPositionIds;
    public final BigDecimal selectMultipleAmount;
    public final boolean selectMultipleShortcutAll;
}
