package com.mfinance.everjoy.app;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Messenger;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.presenter.PendingOrderDetailPresenter;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;

public class PopupPendingOrderDetail implements PopupPendingOrderDetailListener {
	
	PendingOrderDetailPresenter presenter;
	Context context;
	public CustomPopupWindow popup;	
	
	public PopupPendingOrderDetail(Context context, View vParent, MobileTraderApplication app, Messenger mService, Messenger mServiceMessengerHandler){
		this.context = context;
		presenter = new PendingOrderDetailPresenter(this,context.getResources(), app, mService, mServiceMessengerHandler, context);
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
			popup.setContentView(R.layout.v_pending_order_detail_new );
		else
			popup.setContentView(R.layout.v_pending_order_detail );
		if(!CompanySettings.ENABLE_EDIT_ORDER)
			popup.findViewById(R.id.btnEdit).setVisibility(View.GONE);
		
		boolean enableOCO = false;
		if(app.isDemoPlatform == true && CompanySettings.ENABLE_ORDER_OCO_DEMO == true)
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 1 && CompanySettings.ENABLE_ORDER_OCO == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 2 && CompanySettings.ENABLE_ORDER_OCO_PROD2 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 3 && CompanySettings.ENABLE_ORDER_OCO_PROD3 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 4 && CompanySettings.ENABLE_ORDER_OCO_PROD4 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 5 && CompanySettings.ENABLE_ORDER_OCO_PROD5 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 6 && CompanySettings.ENABLE_ORDER_OCO_PROD6 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 7 && CompanySettings.ENABLE_ORDER_OCO_PROD7 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 8 && CompanySettings.ENABLE_ORDER_OCO_PROD8 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 9 && CompanySettings.ENABLE_ORDER_OCO_PROD9 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 10 && CompanySettings.ENABLE_ORDER_OCO_PROD10 == true )
			enableOCO = true;
		
		if(enableOCO){
			if(popup.findViewById(Utility.getIdById("ocoLayout"))!=null)
				popup.findViewById(Utility.getIdById("ocoLayout")).setVisibility(View.VISIBLE);
		}else{
			if(popup.findViewById(Utility.getIdById("ocoLayout"))!=null)
				popup.findViewById(Utility.getIdById("ocoLayout")).setVisibility(View.INVISIBLE);
		}
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
	public TextView getTradeDateField()     {
        return (TextView) popup.findViewById(R.id.tvTradeDate);
	}	
	@Override
	public TextView getMPriceField()     {
        return (TextView) popup.findViewById(R.id.tvMPrice);
	}	
	@Override
	public Button getCloseBtn() {
		return (Button) popup.findViewById(R.id.btnClose);
	}

	@Override
	public TextView getLimitStopField() {
		
		return (TextView) popup.findViewById(R.id.tvLimitStop);
	}

	@Override
	public TextView getOCOField() {
		return (TextView) popup.findViewById(R.id.tvOCO);		
	}

	@Override
	public TextView getLiqField() {
		return (TextView) popup.findViewById(R.id.tvLiquid);	
	}

	@Override
	public TextView getTPriceField() {
		return (TextView) popup.findViewById(R.id.tvTPrice);	
	}

	@Override
	public TextView getGoodTillField() {
		return (TextView) popup.findViewById(R.id.tvGdTill);
	}

	@Override
	public Button getEditBtn() {
		return (Button) popup.findViewById(R.id.btnEdit);
	}

	@Override
	public Button getCancelBtn() {
		return (Button) popup.findViewById(R.id.btnCancel);
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
