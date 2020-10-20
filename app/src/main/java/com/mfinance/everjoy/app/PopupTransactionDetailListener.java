package com.mfinance.everjoy.app;

import android.widget.TextView;

public interface PopupTransactionDetailListener extends BasePopupDetailListener {
	public TextView getRefField();
	public TextView getLotField();
	public TextView getContractField();
	public TextView getAmountField();
	public TextView getActionField();
	public TextView getPriceField();
	
	public TextView getBuySellField();
	public TextView getTimeField();
	
	public TextView getStatusField();
	public TextView getMessageField();
}
