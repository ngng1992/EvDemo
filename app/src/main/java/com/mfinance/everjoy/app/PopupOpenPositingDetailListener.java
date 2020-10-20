package com.mfinance.everjoy.app;

import android.widget.Button;
import android.widget.TextView;

public interface PopupOpenPositingDetailListener extends BasePopupDetailListener {
	public TextView getRefField();
	public TextView getLotField();
	public TextView getContractField();
	public TextView getAmountField();
	public TextView getBuySellField();
	public TextView getCommissionField();
	public TextView getTradeDateField();
	public TextView getMPriceField();
	public TextView getPriceField();
	public TextView getPLField();
	public TextView getRPriceField();
	public TextView getInterestField();
	public TextView getOCOLimitField();
	public TextView getOCOStopField();
	
	public Button getLiqBtn();
	public Button getLimitBtn();
	public Button getStopBtn();
}
