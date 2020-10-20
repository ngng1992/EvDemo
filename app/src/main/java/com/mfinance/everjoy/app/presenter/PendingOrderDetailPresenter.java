package com.mfinance.everjoy.app.presenter;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Messenger;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.MessageMapping;

import com.mfinance.everjoy.app.CompanySettings;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.PopupPendingOrderDetailListener;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;

public class PendingOrderDetailPresenter extends BaseDetailPresenter{
	
	double dMPrice = 0.0;
	String sTmp = null; 
	double[] dBidAsk = null;
	String[] words = null;
	int iRef ;
	Context context;
	PopupPendingOrderDetailListener view;
	public PendingOrderDetailPresenter(PopupPendingOrderDetailListener view,
			Resources res, MobileTraderApplication app, Messenger mService,
			Messenger mServiceMessengerHandler, Context context) {
		super(view, res, app, mService, mServiceMessengerHandler);
		this.view = view;
		this.context = context;
	}
	
	@Override
    public void bindEvent(){
    	super.bindEvent();
    	
    	view.getEditBtn().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Bundle data = new Bundle();
				data.putInt(ServiceFunction.EDIT_ORDER_REF, app.getSelectedRunningOrder().iRef);
				CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_EDIT_ORDER, data);				
				view.dismiss();
			}
    		
    	});
    	
    	
    	view.getCancelBtn().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				if ( !app.getPriceAgentConnectionStatus() ) {
					Toast.makeText(context, MessageMapping.getMessageByCode(res, "307" ,app.locale), Toast.LENGTH_LONG).show();
					return;
				}

				iRef =app.getSelectedRunningOrder().iRef;				
				
				AlertDialog dialog = new AlertDialog.Builder(context, CompanySettings.alertDialogTheme).create();
				dialog.setMessage(res.getText(R.string.are_you_sure));							
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {										
							Bundle data = new Bundle();
							data.putInt(ServiceFunction.SEND_CANCEL_ORDER_REQUEST_ORDER_REF, iRef);
							CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_SEND_CANCEL_ORDER_REQUEST, data);				
							view.dismiss();
							Toast.makeText(context, res.getString(R.string.msg_request_sent), Toast.LENGTH_LONG).show();
						}
					}
				);

				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					}
				);
				dialog.show();
			}
    		
    	});
    	
    }
    
	@SuppressLint("ResourceAsColor")
	@Override
    public void updateUI() {
		if (app.getSelectedRunningOrder()!=null){
			OrderRecord order = app.getSelectedExcutedOrder();
			view.getRefField().setText(String.valueOf(app.getSelectedRunningOrder().getViewRef()));
			view.getLotField().setText( Utility.formatLot(app.getSelectedRunningOrder().dAmount / app.getSelectedRunningOrder().contract.iContractSize));
			view.getContractField().setText( app.getSelectedRunningOrder().contract.getContractName(app.locale));
			if(view.getAmountField()!=null)
				view.getAmountField().setText("("+ Utility.formatAmount(app.getSelectedRunningOrder().dAmount)+")");
			view.getBuySellField().setText( "B".equals(app.getSelectedRunningOrder().strBuySell) ? R.string.lb_buy : R.string.lb_sell );
			if ("B".equals(app.getSelectedRunningOrder().strBuySell))
				view.getBuySellField().setTextColor(Color.GREEN);
			else
				view.getBuySellField().setTextColor(Color.RED);
			dBidAsk = app.getSelectedRunningOrder().contract.getBidAsk();
			dMPrice = 0.0;
		    sTmp = "";
		    boolean bBSD = app.getSelectedRunningOrder().contract.getBSD();
		    
		    if(("B".equals(app.getSelectedRunningOrder().strBuySell) && bBSD == false) || ("S".equals(app.getSelectedRunningOrder().strBuySell) && bBSD == true))
		    	dMPrice = dBidAsk[1];
		    else
		    	dMPrice = dBidAsk[0];
		    		 
		    sTmp =Utility.round(dMPrice, app.getSelectedRunningOrder().contract.iRateDecPt, app.getSelectedRunningOrder().contract.iRateDecPt);
		    view.getMPriceField().setText(sTmp);
		    
		    ColorStateList csl = view.getContractField().getTextColors();
		    if(app.getSelectedRunningOrder().contract.bChangeBidAsk){
		    	  ColorController.setPriceColor(context.getResources(), app.getSelectedRunningOrder().contract.iBidUpDown, view.getMPriceField(), csl);
		    }else{
		    	  ColorController.setPriceColor(context.getResources(), 0, view.getMPriceField(), csl);
		    }		    
		    
		    if(CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW){
		    	String limitStopText = "".equals(order.strLiqRef)?Utility.getStringById("tb_new", res):(order.iLimitStop == 0  ? res.getString(R.string.tb_limit)  : res.getString(R.string.tb_stop) );
		    	view.getLimitStopField().setText(limitStopText);
		    }else
		    	view.getLimitStopField().setText( app.getSelectedRunningOrder().iLimitStop == 0  ? R.string.tb_limit : R.string.tb_stop );
		    view.getOCOField().setText( app.getSelectedRunningOrder().iOCORef > 0  ? app.getSelectedRunningOrder().getOCOViewRef() : "---" );
		    view.getTradeDateField().setText( Utility.displayListingDate(app.getSelectedRunningOrder().strTradeDate));
		    if ("".equals(app.getSelectedRunningOrder().strLiqRef)) 
				view.getLiqField().setText("---");
			else {
				view.getLiqField().setText(app.getSelectedRunningOrder().getLiqViewRef());
			}
		    view.getTPriceField().setText( Utility.round(app.getSelectedRunningOrder().dRequestRate, app.getSelectedRunningOrder().contract.iRateDecPt, app.getSelectedRunningOrder().contract.iRateDecPt));
		    if (app.getSelectedRunningOrder().iGoodTillType == 2)
		    	view.getGoodTillField().setText( R.string.tb_end_of_day );
		    else if (app.getSelectedRunningOrder().iGoodTillType == 1)
		    	view.getGoodTillField().setText( R.string.tb_cancel );
		    else
		    	view.getGoodTillField().setText( Utility.displayListingDate(app.getSelectedRunningOrder().strGoodTillDate) );
		    
		    if(view.getOCOLimitField()!=null){
		    	if(app.getSelectedRunningOrder().dOCOLimitRate!=0)
		    		view.getOCOLimitField().setText( Utility.round(app.getSelectedRunningOrder().dOCOLimitRate, app.getSelectedRunningOrder().contract.iRateDecPt, app.getSelectedRunningOrder().contract.iRateDecPt));
		    	else
		    		view.getOCOLimitField().setText("---");
		    }
		    if(view.getOCOStopField()!=null){
		    	if(app.getSelectedRunningOrder().dOCOStopRate!=0)
		    		view.getOCOStopField().setText( Utility.round(app.getSelectedRunningOrder().dOCOStopRate, app.getSelectedRunningOrder().contract.iRateDecPt, app.getSelectedRunningOrder().contract.iRateDecPt));
		    	else
		    		view.getOCOStopField().setText("---");
		    }
		} else {
			view.dismiss();
		}
		
    }
    
}
