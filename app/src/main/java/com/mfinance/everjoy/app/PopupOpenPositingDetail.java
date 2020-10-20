package com.mfinance.everjoy.app;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Messenger;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.mfinance.everjoy.R;

import com.mfinance.everjoy.app.presenter.OpenPositingDetailPresenter;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;

import org.w3c.dom.Text;

public class PopupOpenPositingDetail implements PopupOpenPositingDetailListener {
	
	OpenPositingDetailPresenter presenter;
	Context context;
	public CustomPopupWindow popup;
	TextView lbLot;
	
	public PopupOpenPositingDetail(Context context, View vParent, MobileTraderApplication app, Messenger mService, Messenger mServiceMessengerHandler){
		this.context = context;
		presenter = new OpenPositingDetailPresenter(this,context.getResources(), app, mService, mServiceMessengerHandler);
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
			popup.setContentView(R.layout.v_open_position_detail_new );
		else
			popup.setContentView(R.layout.v_open_position_detail );
		lbLot = (TextView) popup.findViewById(R.id.lbLot);
		lbLot.setText(Utility.getStringById("lb_lot", context.getResources()) + "(" + Utility.getStringById("lb_amount_with_quo", context.getResources()));
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
	public TextView getCommissionField()     {
        return (TextView) popup.findViewById(R.id.tvCommission);
	}
	@Override
	public TextView getTradeDateField()     {
        return (TextView) popup.findViewById(R.id.tvTradeDate);
	}	
	@Override
	public TextView getMPriceField()     {
        return (TextView) popup.findViewById(R.id.tvMPrice);
	}	
	@Override
	public TextView getPriceField()     {
        return (TextView) popup.findViewById(R.id.tvPrice);
	}	
	@Override
	public TextView getPLField()     {
        return (TextView) popup.findViewById(R.id.tvPL);
	}

	@Override
	public Button getLiqBtn() {
		return (Button) popup.findViewById(R.id.btnLiq);
	}

	@Override
	public Button getLimitBtn() {
		return (Button) popup.findViewById(R.id.btnLimit);
	}

	@Override
	public Button getStopBtn() {
		return (Button) popup.findViewById(R.id.btnStop);
	}

	@Override
	public Button getCloseBtn() {
		return (Button) popup.findViewById(R.id.btnClose);
	}

	@Override
	public TextView getRPriceField() {
		return (TextView) popup.findViewById(R.id.tvRPrice);
	}	
	
	@Override
	public TextView getInterestField() {
		return (TextView) popup.findViewById(Utility.getIdById("tvInterest"));
	}

	@Override
	public TextView getOCOLimitField() {
		return (TextView) popup.findViewById(R.id.tvLimit);
	}

	@Override
	public TextView getOCOStopField() {
		return (TextView) popup.findViewById(R.id.tvStop);
	}

}
