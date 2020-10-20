package com.mfinance.everjoy.app;

public class RunningOrderItem {
    final Integer iRef;
    final String contractName;
    final String tradeDate;
    final String strBuySell;
    final Integer iLimitStop;
    final String lot;
    final String requestPrice;
    final String curPrice;
    final boolean bChangeBidAsk;
    final Integer iBidUpDown;
    final Integer iOCORef;
    final boolean isLiq;
    final boolean selected;
    final boolean showSelection;

    public RunningOrderItem(int iRef, String contractName, String tradeDate, String strBuySell, Integer iLimitStop, String lot, String requestPrice, String curPrice, boolean bChangeBidAsk, Integer iBidUpDown, Integer iOCORef, boolean isLiq, boolean selected, boolean showSelection) {
        this.iRef = iRef;
        this.contractName = contractName;
        this.tradeDate = tradeDate;
        this.strBuySell = strBuySell;
        this.iLimitStop = iLimitStop;
        this.lot = lot;
        this.requestPrice = requestPrice;
        this.curPrice = curPrice;
        this.bChangeBidAsk = bChangeBidAsk;
        this.iBidUpDown = iBidUpDown;
        this.iOCORef = iOCORef;
        this.isLiq = isLiq;
        this.selected = selected;
        this.showSelection = showSelection;
    }
}
