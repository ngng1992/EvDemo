package com.mfinance.everjoy.app;

public class RunningOrderItemBuilder {
    private int iRef;
    private String contractName;
    private String tradeDate;
    private String strBuySell;
    private Integer iLimitStop;
    private String lot;
    private String requestPrice;
    private String curPrice;
    private boolean bChangeBidAsk;
    private Integer iBidUpDown;
    private Integer iOCORef;
    private boolean isLiq;
    private boolean selected;
    private boolean showSelection;
    public RunningOrderItemBuilder() {

    }
    public RunningOrderItemBuilder(RunningOrderItem t) {
        this.iRef = t.iRef;
        this.contractName = t.contractName;
        this.tradeDate = t.tradeDate;
        this.strBuySell = t.strBuySell;
        this.iLimitStop = t.iLimitStop;
        this.lot = t.lot;
        this.requestPrice = t.requestPrice;
        this.curPrice = t.curPrice;
        this.bChangeBidAsk = t.bChangeBidAsk;
        this.iBidUpDown = t.iBidUpDown;
        this.iOCORef = t.iOCORef;
        this.isLiq = t.isLiq;
        this.selected = t.selected;
        this.showSelection = t.showSelection;
    }

    public RunningOrderItemBuilder setiRef(int iRef) {
        this.iRef = iRef;
        return this;
    }

    public RunningOrderItemBuilder setContractName(String contractName) {
        this.contractName = contractName;
        return this;
    }

    public RunningOrderItemBuilder setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
        return this;
    }

    public RunningOrderItemBuilder setStrBuySell(String strBuySell) {
        this.strBuySell = strBuySell;
        return this;
    }

    public RunningOrderItemBuilder setiLimitStop(Integer iLimitStop) {
        this.iLimitStop = iLimitStop;
        return this;
    }

    public RunningOrderItemBuilder setLot(String lot) {
        this.lot = lot;
        return this;
    }

    public RunningOrderItemBuilder setRequestPrice(String requestPrice) {
        this.requestPrice = requestPrice;
        return this;
    }

    public RunningOrderItemBuilder setCurPrice(String curPrice) {
        this.curPrice = curPrice;
        return this;
    }

    public RunningOrderItemBuilder setbChangeBidAsk(boolean bChangeBidAsk) {
        this.bChangeBidAsk = bChangeBidAsk;
        return this;
    }

    public RunningOrderItemBuilder setiBidUpDown(Integer iBidUpDown) {
        this.iBidUpDown = iBidUpDown;
        return this;
    }

    public RunningOrderItemBuilder setiOCORef(Integer iOCORef) {
        this.iOCORef = iOCORef;
        return this;
    }

    public RunningOrderItemBuilder setIsLiq(boolean isLiq) {
        this.isLiq = isLiq;
        return this;
    }

    public RunningOrderItemBuilder setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public RunningOrderItemBuilder setShowSelection(boolean showSelection) {
        this.showSelection = showSelection;
        return this;
    }

    public RunningOrderItem createRunningOrderItem() {
        return new RunningOrderItem(iRef, contractName, tradeDate, strBuySell, iLimitStop, lot, requestPrice, curPrice, bChangeBidAsk, iBidUpDown, iOCORef, isLiq, selected, showSelection);
    }
}