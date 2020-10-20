package com.mfinance.everjoy.app;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.presenter.ExecutedOrderDetailPresenter;
import com.mfinance.everjoy.app.presenter.LiquidationDetailPresenter;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Messenger;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PopupLiquidationDetail implements PopupLiquidationDetailListener {

	LiquidationDetailPresenter presenter;
	Context context;
	public CustomPopupWindow popup;	
	
	public PopupLiquidationDetail(Context context, View vParent, MobileTraderApplication app, Messenger mService, Messenger mServiceMessengerHandler){
		this.context = context;
		presenter = new LiquidationDetailPresenter(this,context.getResources(), app, mService, mServiceMessengerHandler);
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
			popup.setContentView(R.layout.v_liquidation_detail_new );
		else
			popup.setContentView(R.layout.v_liquidation_detail );

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
	public TextView getContractField()     {
        return (TextView) popup.findViewById(R.id.tvContract);
	}
	@Override
	public Button getCloseBtn() {
		return (Button) popup.findViewById(R.id.btnClose);
	}

	@Override
	public TextView getBuyRefField() {
		return (TextView) popup.findViewById(R.id.tvBuyRef);
	}

	@Override
	public TextView getAmountField() {
		return (TextView) popup.findViewById(R.id.tvAmount);
	}

	@Override
	public TextView getLotField() {
		return (TextView) popup.findViewById(R.id.tvLot);
	}

	@Override
	public TextView getSellRefField() {
		return (TextView) popup.findViewById(R.id.tvSellRef);
	}

	@Override
	public TextView getBuyDateField() {
		return (TextView) popup.findViewById(R.id.tvBuyDate);
	}

	@Override
	public TextView getSellDateField() {
		return (TextView) popup.findViewById(R.id.tvSellDate);
	}

	@Override
	public TextView getCommissionField() {
		return (TextView) popup.findViewById(R.id.tvCommission);
	}

	@Override
	public TextView getPLField() {
		return (TextView) popup.findViewById(R.id.tvPL);
	}

	@Override
	public TextView getBuyRateField() {
		return (TextView) popup.findViewById(R.id.tvBuyRate);
	}

	@Override
	public TextView getSellRateField() {
		return (TextView) popup.findViewById(R.id.tvSellRate);
	}	

	@Override
	public TextView getAPLField() {
		return (TextView) popup.findViewById(R.id.tvAPL);
	}	
	
	@Override
	public TextView getRunningPriceField() {
		return (TextView) popup.findViewById(Utility.getIdById("tvRunningPrice"));
	}

	@Override
	public TextView getBSField() {
		return (TextView) popup.findViewById(Utility.getIdById("tvBuySell"));
	}	
	
	@Override
	public TextView getInterestField() {
		return (TextView) popup.findViewById(Utility.getIdById("tvInterest"));
	}
}
