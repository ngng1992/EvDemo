package com.mfinance.everjoy.app;

import android.widget.Button;
import android.widget.TextView;

public interface PopupPendingOrderDetailListener extends BasePopupDetailListener {
	public TextView getRefField();
	public TextView getLotField();
	public TextView getContractField();
	public TextView getAmountField();
	public TextView getBuySellField();
	public TextView getMPriceField();
	
	public TextView getLimitStopField();
	public TextView getOCOField();
	
	public TextView getTradeDateField();
	public TextView getLiqField();
	
	public TextView getTPriceField();
	public TextView getGoodTillField();
	
	public TextView getOCOLimitField();
	public TextView getOCOStopField();
	
	public Button getEditBtn();
	public Button getCancelBtn();

}
