package com.mfinance.everjoy.app;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Messenger;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.CashMovementRecord;
import com.mfinance.everjoy.app.presenter.CashMovementDetailPresenter;
import com.mfinance.everjoy.app.util.function.Supplier;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;

import java.util.function.Function;

public class PopupCashMovementDetail {

	private CashMovementDetailPresenter presenter;
	private Context context;
	private CustomPopupWindow popup;
	private PopupCashMovementDetailListener viewHolder;
	
	public PopupCashMovementDetail(Context context, View vParent, MobileTraderApplication app, Messenger mService, Messenger mServiceMessengerHandler, Supplier<CashMovementRecord> getSelectedCashMovementRecord, Function<String, String> getStatusString, Runnable onClickCancelCashMovement){
		this.context = context;
		popup = new CustomPopupWindow(vParent) {
			@Override
			protected void preShow() {
				window.setBackgroundDrawable(new BitmapDrawable());
				window.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
				window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
				window.setTouchable(true);
				window.setFocusable(true);
				window.setOutsideTouchable(true);
			}
		};
		popup.setContentView(R.layout.v_cashmovement_history_detail);
		viewHolder = new PopupCashMovementDetailListener() {
			@Override
			public void dismiss(){
				popup.dismiss();
			}

			@Override
			public Button getCloseBtn() {
				return popup.findViewById(R.id.btnClose);
			}

			@Override
			public TextView getAmountField() {
				return popup.findViewById(R.id.tvAmount);
			}

			@Override
			public TextView getRefField() {
				return  popup.findViewById(R.id.tvRef);
			}

			@Override
			public TextView getBanknameField() {
				return popup.findViewById(R.id.tvBankname);
			}

			@Override
			public TextView getBankaccountField() {
				return popup.findViewById(R.id.tvBankaccount);
			}

			@Override
			public TextView getStatusField() {
				return popup.findViewById(R.id.tvStatus);
			}

			@Override
			public TextView getReqdateField() {
				return  popup.findViewById(R.id.tvReqdate);
			}

			@Override
			public TextView getLastUpdateByField() {
				return popup.findViewById(R.id.tvCashMovementHistoryLastUpdateBy);
			}

			@Override
			public Button getCancelCashMovementButton() {
				return popup.findViewById(R.id.btnCancelCashMovement);
			}
		};
		presenter = new CashMovementDetailPresenter(viewHolder, context.getResources(), app, mService, mServiceMessengerHandler, getSelectedCashMovementRecord, getStatusString, onClickCancelCashMovement);
	}
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
		presenter.bindEvent();
		presenter.updateUI();
	}
	
	public void updateUI() {
		presenter.updateUI();
	}
	public void dismiss(){
		popup.dismiss();
	}
}
