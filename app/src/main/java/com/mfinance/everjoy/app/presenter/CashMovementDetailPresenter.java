package com.mfinance.everjoy.app.presenter;

import android.content.res.Resources;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.mfinance.everjoy.app.PopupCashMovementDetailListener;
import com.mfinance.everjoy.app.BasePopupDetailListener;
import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.app.bo.CashMovementRecord;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.internal.CashMovementRequestProcessor;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.util.function.Supplier;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class CashMovementDetailPresenter extends BaseDetailPresenter{

	private CashMovementRecord record ;
	private final Supplier<CashMovementRecord> getSelectedCashMovementRecord;
	private final PopupCashMovementDetailListener view;
	private final Function<String, String> getStatusString;
	private final Runnable onClickCancelCashMovement;
	public CashMovementDetailPresenter(PopupCashMovementDetailListener view,
									   Resources res, MobileTraderApplication app, Messenger mService,
									   Messenger mServiceMessengerHandler,
									   Supplier<CashMovementRecord> getSelectedCashMovementRecord,
									   Function<String, String> getStatusString,
									   Runnable onClickCancelCashMovement) {
		super(view, res, app, mService, mServiceMessengerHandler);
		this.view = view;
		this.getSelectedCashMovementRecord = getSelectedCashMovementRecord;
		this.getStatusString = getStatusString;
		this.onClickCancelCashMovement = onClickCancelCashMovement;
	}
	
	@Override
    public void updateUI() {
		CashMovementRecord oldRecord = record;
		record = getSelectedCashMovementRecord.get();
		if (record != oldRecord) {
			if (record != null) {
				String user = Optional.ofNullable(app.data.getBalanceRecord()).map(b -> b.strAccount).orElse("");
				view.getBanknameField().setText(record.sBankName);
				view.getAmountField().setText(record.sAmount);
				view.getRefField().setText(record.sRef);
				view.getBankaccountField().setText(record.sBankAccount);
				view.getStatusField().setText(getStatusString.apply(record.sStatus));
				view.getReqdateField().setText(Utility.displayListingDate(record.sRequestDate)+" "+Utility.getTime(record.sRequestDate));
				view.getLastUpdateByField().setText(record.lastUpdateBy);
				view.getCancelCashMovementButton().setVisibility("0".equals(record.sStatus) && user.equals(record.lastUpdateBy) ? View.VISIBLE : View.GONE);
			}
		}
    }

	@Override
	public void bindEvent() {
		super.bindEvent();
		view.getCancelCashMovementButton().setOnClickListener((v) -> onClickCancelCashMovement.run());
	}
}
