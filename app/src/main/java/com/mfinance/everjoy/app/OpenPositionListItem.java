package com.mfinance.everjoy.app;

import com.mfinance.everjoy.app.bo.OpenPositionRecord;

public class OpenPositionListItem {
    public OpenPositionListItem(OpenPositionRecord position, boolean showAmountSelection, double amountSelected, boolean showCurrentPrice) {
        this.position = position;
        this.showAmountSelection = showAmountSelection;
        this.amountSelected = amountSelected;
        this.showCurrentPrice = showCurrentPrice;
    }
    private final OpenPositionRecord position;
    private final boolean showAmountSelection;
    private final double amountSelected;
    private final boolean showCurrentPrice;

    public OpenPositionRecord getPosition() {
        return position;
    }

    public boolean isShowAmountSelection() {
        return showAmountSelection;
    }

    public double getAmountSelected() {
        return amountSelected;
    }

    public boolean isShowCurrentPrice() {
        return showCurrentPrice;
    }
}
