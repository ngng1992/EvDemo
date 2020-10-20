package com.mfinance.everjoy.app;

import android.widget.TextView;

public interface PopupLiquidationDetailListener extends BasePopupDetailListener {
	TextView getBuyRefField();
	TextView getAmountField();
	
	TextView getLotField();
	TextView getContractField();
	
	TextView getSellRefField();
	
	TextView getBuyRateField();
	TextView getSellRateField();
	
	TextView getBuyDateField();
	TextView getSellDateField();
	
	TextView getCommissionField();
	TextView getPLField();
	
	TextView getAPLField();
	TextView getRunningPriceField();
	
	TextView getBSField();
	
	TextView getInterestField();
}
