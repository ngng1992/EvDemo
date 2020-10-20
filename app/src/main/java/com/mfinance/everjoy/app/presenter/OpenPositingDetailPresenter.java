package com.mfinance.everjoy.app.presenter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import com.mfinance.everjoy.app.PopupOpenPositingDetailListener;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;

import org.w3c.dom.Text;

public class OpenPositingDetailPresenter extends BaseDetailPresenter{

	double dMPrice = 0.0;
	
	public OpenPositingDetailPresenter(BasePopupDetailListener view,
			Resources res, MobileTraderApplication app, Messenger mService,
			Messenger mServiceMessengerHandler) {
		super(view, res, app, mService, mServiceMessengerHandler);
	}

	private OrderRecord FindLimitStopOrderFromPosition(String positionViewRef, int iLimitStop)
	{
		OrderRecord _order = null;
		Map<Integer, OrderRecord> orders = app.data.getRunningOrders();
		for( OrderRecord order : orders.values()) {
			if (positionViewRef.equals(order.getLiqViewRef()) && order.iLimitStop == iLimitStop) {
				_order = order;
				break;
			}
		}
		return _order;
	}
	@Override
    public void bindEvent(){
    	super.bindEvent();
    	
    	((PopupOpenPositingDetailListener)view).getLiqBtn().setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				OpenPositionRecord selectedOpenPosition = app.getSelectedOpenPosition();
				Bundle data = new Bundle();
				data.putInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_REF, selectedOpenPosition.iRef);
				data.putBoolean("isBuy", selectedOpenPosition.isBuyOrder);
				data.putString("contract", selectedOpenPosition.strContract);
				CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_LIQUIDATE, data);
				view.dismiss();
			}
    		
    	});
    	
    	((PopupOpenPositingDetailListener)view).getLimitBtn().setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				Bundle data= new Bundle();
				OpenPositionRecord position = app.getSelectedOpenPosition();
				OrderRecord limitOrder = FindLimitStopOrderFromPosition(position.getViewRef(), 0);

				if (limitOrder!= null) {
					data.putInt(ServiceFunction.EDIT_ORDER_REF, limitOrder.iRef);
					CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_EDIT_ORDER, data);
				}
				else {
					data.putString(ServiceFunction.ORDER_CONTRACT, position.contract.strContractCode);
					data.putString(ServiceFunction.ORDER_BUY_SELL, "B".equals(position.strBuySell) ? "S" : "B");
					data.putInt(ServiceFunction.ORDER_LIMIT_STOP, 0);
					data.putInt(ServiceFunction.ORDER_DEAL_REF, position.iRef);
					CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_ORDER, data);
				}
				view.dismiss();
			}
    		
    	});
    	
    	((PopupOpenPositingDetailListener)view).getStopBtn().setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				Bundle data= new Bundle();
				OpenPositionRecord position = app.getSelectedOpenPosition();
				OrderRecord stopOrder = FindLimitStopOrderFromPosition(position.getViewRef(), 1);

				if (stopOrder!= null) {
					data.putInt(ServiceFunction.EDIT_ORDER_REF, stopOrder.iRef);
					CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_EDIT_ORDER, data);
				}
				else {
					data.putString(ServiceFunction.ORDER_CONTRACT, app.getSelectedOpenPosition().contract.strContractCode);
					data.putString(ServiceFunction.ORDER_BUY_SELL, "B".equals(app.getSelectedOpenPosition().strBuySell) ? "S" : "B");
					data.putInt(ServiceFunction.ORDER_LIMIT_STOP, 1);
					data.putInt(ServiceFunction.ORDER_DEAL_REF, app.getSelectedOpenPosition().iRef);
					CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_ORDER, data);
				}
				view.dismiss();
			}
    		
    	});
    }
    
	@SuppressLint("ResourceAsColor")
	@Override
    public void updateUI() {
    	if (app.getSelectedOpenPosition()!=null){

    		((PopupOpenPositingDetailListener)view).getRefField().setText(app.getSelectedOpenPosition().getViewRef());
    		((PopupOpenPositingDetailListener)view).getLotField().setText(Utility.formatLot(app.getSelectedOpenPosition().dAmount / app.getSelectedOpenPosition().contract.iContractSize));
			
    		((PopupOpenPositingDetailListener)view).getContractField().setText(app.getSelectedOpenPosition().contract.getContractName(app.locale));
    		if(((PopupOpenPositingDetailListener)view).getAmountField()!=null)
    			((PopupOpenPositingDetailListener)view).getAmountField().setText("("+ Utility.formatAmount(app.getSelectedOpenPosition().dAmount) +")");
			
    		((PopupOpenPositingDetailListener)view).getBuySellField().setText("B".equals(app.getSelectedOpenPosition().strBuySell) ?  R.string.lb_buy : R.string.lb_sell );
    		if ("B".equals(app.getSelectedOpenPosition().strBuySell)){
				((PopupOpenPositingDetailListener)view).getBuySellField().setTextColor(Color.GREEN);
			}
			else
				((PopupOpenPositingDetailListener)view).getBuySellField().setTextColor(Color.RED);
    		if( CompanySettings.Inverse_Commission_Sign == true )
    		{
    			((PopupOpenPositingDetailListener)view).getCommissionField().setText(Utility.formatValue(-app.getSelectedOpenPosition().dCommission));
    			ColorController.setNumberColor(res, app.getSelectedOpenPosition().dCommission == (float)0.0 ? null : app.getSelectedOpenPosition().dCommission < 0 , ((PopupOpenPositingDetailListener)view).getCommissionField());
    		}
    		else
    		{
    			((PopupOpenPositingDetailListener)view).getCommissionField().setText(Utility.formatValue(app.getSelectedOpenPosition().dCommission));
    			ColorController.setNumberColor(res, app.getSelectedOpenPosition().dCommission == (float)0.0 ? null : app.getSelectedOpenPosition().dCommission >= 0 , ((PopupOpenPositingDetailListener)view).getCommissionField());
    		}
			
			((PopupOpenPositingDetailListener)view).getTradeDateField().setText(Utility.displayListingDate(app.getSelectedOpenPosition().strTradeDate));
			
			boolean bBSD = app.getSelectedOpenPosition().contract.getBSD();
			double[] dBidAsk = app.getSelectedOpenPosition().contract.getBidAsk();
			dMPrice = 0.0;
		      
		    String sTmp = "";
		    if( ("B".equals(app.getSelectedOpenPosition().strBuySell) && bBSD == false) || ("S".equals(app.getSelectedOpenPosition().strBuySell) && bBSD == true) )
		    	dMPrice = dBidAsk[0];
		    else
		    	dMPrice = dBidAsk[1];
		    		 
		    sTmp =Utility.round(dMPrice, app.getSelectedOpenPosition().contract.iRateDecPt, app.getSelectedOpenPosition().contract.iRateDecPt);
		    
		    ((PopupOpenPositingDetailListener)view).getMPriceField().setText(sTmp);
		    //ColorController.setNumberColor(res, dMPrice >= 0 , ((PopupOpenPositingDetailListener)view).getMPriceField());
		    ColorStateList csl = ((PopupOpenPositingDetailListener)view).getContractField().getTextColors();
		    if( app.getSelectedOpenPosition().contract.bChangeBidAsk){
		    	  ColorController.setPriceColor(res, app.getSelectedOpenPosition().contract.iBidUpDown, ((PopupOpenPositingDetailListener)view).getMPriceField(), csl);
		    }else{
		    	  ColorController.setPriceColor(res, 0, ((PopupOpenPositingDetailListener)view).getMPriceField(), csl);
		    }
		    
		    sTmp =Utility.round(app.getSelectedOpenPosition().dDealRate, app.getSelectedOpenPosition().contract.iRateDecPt, app.getSelectedOpenPosition().contract.iRateDecPt);
		    ((PopupOpenPositingDetailListener)view).getPriceField().setText(sTmp);
		    
		    sTmp =Utility.round(app.getSelectedOpenPosition().dRunningRate, app.getSelectedOpenPosition().contract.iRateDecPt, app.getSelectedOpenPosition().contract.iRateDecPt);
		    if(((PopupOpenPositingDetailListener)view).getRPriceField()!=null)
		    	((PopupOpenPositingDetailListener)view).getRPriceField().setText(sTmp);
		    
		    ((PopupOpenPositingDetailListener)view).getPLField().setText(Utility.formatValue(app.getSelectedOpenPosition().dFloating));
			ColorController.setNumberColor(res, app.getSelectedOpenPosition().dFloating >= 0 , ((PopupOpenPositingDetailListener)view).getPLField());
			
		    if(((PopupOpenPositingDetailListener)view).getInterestField()!=null)
		    	((PopupOpenPositingDetailListener)view).getInterestField().setText(Utility.formatValue(app.getSelectedOpenPosition().dFloatInterest));
		    
		    Map<Integer, OrderRecord> orders = app.data.getRunningOrders();
		    
		    if( ((PopupOpenPositingDetailListener)view).getOCOLimitField() != null && ((PopupOpenPositingDetailListener)view).getOCOStopField() != null )
		    {
		    	((PopupOpenPositingDetailListener)view).getOCOLimitField().setText("-");
		        ((PopupOpenPositingDetailListener)view).getOCOStopField().setText("-");
			    for( OrderRecord order : orders.values())
			    {
			    	if( order.getLiqViewRef().equals(app.getSelectedOpenPosition().getViewRef()) )
			    	{
			    		if( order.iLimitStop == 0 )
			    		{
			    			sTmp =Utility.round(order.dRequestRate, app.getSelectedOpenPosition().contract.iRateDecPt, app.getSelectedOpenPosition().contract.iRateDecPt);
			    			((PopupOpenPositingDetailListener)view).getOCOLimitField().setText(sTmp);
			    		}
			    		else
			    		{
			    			sTmp =Utility.round(order.dRequestRate, app.getSelectedOpenPosition().contract.iRateDecPt, app.getSelectedOpenPosition().contract.iRateDecPt);
			    			((PopupOpenPositingDetailListener)view).getOCOStopField().setText(sTmp);
			    		}
			    	}
			    }
		    }
		    
		    
		    if( !CompanySettings.ENABLE_ORDER || (app.data.getBalanceRecord().hedged == true && CompanySettings.ALLOW_STP_ORDER == false) )
		    {
		    	((PopupOpenPositingDetailListener)view).getLimitBtn().setVisibility(android.view.View.GONE);
		    	((PopupOpenPositingDetailListener)view).getStopBtn().setVisibility(android.view.View.GONE);
		    	
		    }
    	}
		
    }
    
}
