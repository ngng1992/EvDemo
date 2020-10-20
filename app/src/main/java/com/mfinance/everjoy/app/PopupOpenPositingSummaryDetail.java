package com.mfinance.everjoy.app;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.presenter.OpenPositingSummaryDetailPresenter;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.R;

public class PopupOpenPositingSummaryDetail implements PopupOpenPositingSummaryDetailListener {
	
	OpenPositingSummaryDetailPresenter presenter;
	Context context;
	public CustomPopupWindow popup;	
	
	public PopupOpenPositingSummaryDetail(Context context, View vParent, MobileTraderApplication app, Messenger mService, Messenger mServiceMessengerHandler, Runnable onClickDetail){
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
		presenter = new OpenPositingSummaryDetailPresenter(this,context.getResources(), app, mService, mServiceMessengerHandler, onClickDetail);

		if (CompanySettings.newinterface) {
			popup.setContentView(R.layout.v_open_position_summ_detail_new);
		} else {
			popup.setContentView(R.layout.v_open_position_summ_detail);
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
	public Button getCloseBtn() {
		return (Button) popup.findViewById(R.id.btnClose);
	}

	@Override
	public Button getDetailBtn() {
		return (Button) popup.findViewById(R.id.btnDetail);
	}	

}
