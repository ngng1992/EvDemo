package com.mfinance.everjoy.app;

import android.widget.Button;
import android.widget.TextView;

public interface PopupOpenPositingSummaryDetailListener extends BasePopupDetailListener {
	public TextView getContractField();	
	public TextView getAmountField();
	public TextView getBuySellField();
	public TextView getLotField();
	public TextView getPriceField();
	public TextView getPLField();
	public TextView getMPriceField();
	public Button getDetailBtn();
}
