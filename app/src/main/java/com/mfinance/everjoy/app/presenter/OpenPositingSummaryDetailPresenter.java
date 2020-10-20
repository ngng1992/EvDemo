package com.mfinance.everjoy.app.presenter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Messenger;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mfinance.everjoy.app.CompanySettings;

import com.mfinance.everjoy.app.BasePopupDetailListener;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.PopupOpenPositingSummaryDetailListener;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.FontFitTextView;

public class OpenPositingSummaryDetailPresenter extends BaseDetailPresenter{

	private final PopupOpenPositingSummaryDetailListener view;
	private final Runnable onClickDetail;
	
	public OpenPositingSummaryDetailPresenter(PopupOpenPositingSummaryDetailListener view,
			Resources res, MobileTraderApplication app, Messenger mService,
			Messenger mServiceMessengerHandler, Runnable onClickDetail) {
		super(view, res, app, mService, mServiceMessengerHandler);
		this.view = view;
		this.onClickDetail = onClickDetail;
	}
	
	@Override
    public void bindEvent(){
    	super.bindEvent();
    	view.getDetailBtn().setOnClickListener(v -> onClickDetail.run());
    }
    
	@SuppressLint("ResourceAsColor")
	@Override
    public void updateUI() {
		if (app.getSelectedContract()!=null && app.getSelectedBuySell() !=null){

			double dFloat = 0.0;
			double dAmount = 0.0;
			double dRate = 0.0;
			double dLot = 0.0;
			double dMPrice = 0.0;

			HashMap<Integer, OpenPositionRecord> hmPosition;
			if ("B".equals(app.getSelectedBuySell())){
		    	hmPosition = (HashMap<Integer, OpenPositionRecord>)app.getSelectedContract().getBuyPositions().clone();
		    	if( app.getSelectedContract().getBSD() == true )
		    		dMPrice = app.getSelectedContract().dAsk;
		    	else
		    		dMPrice = app.getSelectedContract().dBid;
		    }
		    else {
		    	hmPosition = (HashMap<Integer, OpenPositionRecord>)app.getSelectedContract().getSellPositions().clone();
		    	if( app.getSelectedContract().getBSD() == true )
		    		dMPrice = app.getSelectedContract().dBid;
		    	else
		    		dMPrice = app.getSelectedContract().dAsk;
		    }

			Iterator<OpenPositionRecord> itPosition = hmPosition.values().iterator();
		    while(itPosition.hasNext()){
		    	OpenPositionRecord positionRecord = itPosition.next();
		    	dFloat += positionRecord.dFloating;
		    	dAmount += positionRecord.dAmount;
		    	dRate += positionRecord.dDealRate * positionRecord.dAmount / app.getSelectedContract().iContractSize;
		    	dLot +=  positionRecord.dAmount / app.getSelectedContract().iContractSize;
		    }
		    
		    view.getContractField().setText(app.getSelectedContract().getContractName(app.locale));
		    if(view.getAmountField()!=null)
		    	view.getAmountField().setText("("+ Utility.formatAmount(dAmount) +")");
		    view.getBuySellField().setText("B".equals(app.getSelectedBuySell()) ?  R.string.lb_buy : R.string.lb_sell );
		    if ("B".equals(app.getSelectedBuySell()))
				view.getBuySellField().setTextColor(Color.GREEN);
		    else
				view.getBuySellField().setTextColor(Color.RED);
		    view.getLotField().setText( Utility.formatLot(dLot) );
		    
		    view.getPLField().setText( Utility.formatValue(dFloat) );
		    ColorController.setNumberColor(res, dFloat >= 0 , view.getPLField());
		    
		    view.getPriceField().setText(Utility.round(dRate / (dAmount / app.getSelectedContract().iContractSize), app.getSelectedContract().iRateDecPt, app.getSelectedContract().iRateDecPt));
		    
		    view.getMPriceField().setText( Utility.round(dMPrice, app.getSelectedContract().iRateDecPt, app.getSelectedContract().iRateDecPt) );
		    
		    ColorStateList csl = view.getContractField().getTextColors();
		    if( app.getSelectedContract().bChangeBidAsk){
		    	ColorController.setPriceColor(res, app.getSelectedContract().iBidUpDown, view.getMPriceField(), csl);
		    }else{
		    	ColorController.setPriceColor(res, 0, view.getMPriceField(), csl);
		    }
		    
		    ColorController.setNumberColor(res, dFloat >= 0 , view.getPLField());
		    /*
		    ((FontFitTextView) ((PopupOpenPositingSummaryDetailListener)view).getAmountField())..resizeText();
		    ((FontFitTextView) ((PopupOpenPositingSummaryDetailListener)view).getPLField()).resizeText();
		    */
		}
	    
    }
    
}
