package com.mfinance.everjoy.app;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Messenger;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.mfinance.everjoy.app.presenter.TransactionStatusDetailPresenter;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.R;

public class PopupTransactionDetail implements PopupTransactionDetailListener {
	
	TransactionStatusDetailPresenter presenter;
	Context context;
	public CustomPopupWindow popup;	
	
	public PopupTransactionDetail(Context context, View vParent, MobileTraderApplication app, Messenger mService, Messenger mServiceMessengerHandler){
		this.context = context;
		presenter = new TransactionStatusDetailPresenter(this,context.getResources(), app, mService, mServiceMessengerHandler);
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
		if (CompanySettings.newinterface)
			popup.setContentView(R.layout.v_transaction_detail_new );
		else
			popup.setContentView(R.layout.v_transaction_detail );

	}
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
		presenter.bindEvent();
		presenter.updateUI();
	}
	
	public void updateUI() {
		presenter.updateUI();
	}

	@Override
	public void dismiss(){
		popup.dismiss();
	}
	@Override
	public TextView getRefField()     {
		return (TextView) popup.findViewById(R.id.tvRef);
	}
	@Override
	public TextView getLotField()     {
        return (TextView) popup.findViewById(R.id.tvLot);
	}
	@Override
	public TextView getContractField()     {
        return (TextView) popup.findViewById(R.id.tvContract);
	}
	@Override
	public TextView getAmountField()     {
        return (TextView) popup.findViewById(R.id.tvAmount);
	}
	@Override
	public TextView getBuySellField()     {
        return (TextView) popup.findViewById(R.id.tvBuySell);
	}
	@Override
	public TextView getPriceField()     {
        return (TextView) popup.findViewById(R.id.tvPrice);
	}	
	@Override
	public Button getCloseBtn() {
		return (Button) popup.findViewById(R.id.btnClose);
	}

	@Override
	public TextView getActionField() {
		return (TextView) popup.findViewById(R.id.tvAction);
	}

	@Override
	public TextView getTimeField() {
		return (TextView) popup.findViewById(R.id.tvTime);
	}

	@Override
	public TextView getStatusField() {
		return (TextView) popup.findViewById(R.id.tvStatus);
	}

	@Override
	public TextView getMessageField() {
		return (TextView) popup.findViewById(R.id.tvMsg);
	}	

}
