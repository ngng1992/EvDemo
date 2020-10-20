package com.mfinance.everjoy.app;

import android.widget.TextView;

public interface PopupExcutedOrderDetailListener extends BasePopupDetailListener {
	public TextView getRefField();
	public TextView getExcAmountField();
	public TextView getContractField();
	public TextView getExeLotField();
	public TextView getBuySellField();
	public TextView getTPriceField();
	
	public TextView getLimitStopField();
	public TextView getOCOField();
	
	public TextView getExeDateField();
	public TextView getLiqField();
	
	public TextView getExeTimeField();
}
