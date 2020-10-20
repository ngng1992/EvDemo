package com.mfinance.everjoy.app;

import android.widget.Button;
import android.widget.TextView;

public interface PopupCashMovementDetailListener extends BasePopupDetailListener {
	TextView getRefField();
	TextView getAmountField();
	TextView getBanknameField();
	TextView getBankaccountField();
	TextView getStatusField();
	TextView getReqdateField();
	TextView getLastUpdateByField();
	Button getCancelCashMovementButton();
}
