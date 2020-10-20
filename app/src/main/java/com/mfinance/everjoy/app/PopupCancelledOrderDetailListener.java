package com.mfinance.everjoy.app;

import android.widget.TextView;

public interface PopupCancelledOrderDetailListener extends BasePopupDetailListener {
	public TextView getRefField();
	public TextView getLotField();
	public TextView getContractField();
	public TextView getAmountField();
	public TextView getBuySellField();
	public TextView getTPriceField();
	
	public TextView getLimitStopField();
	public TextView getOCOField();
	
	public TextView getCancelDateField();
	public TextView getLiqField();

	public TextView getGoodTillField();
}
