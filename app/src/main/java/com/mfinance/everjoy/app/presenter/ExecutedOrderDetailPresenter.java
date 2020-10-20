package com.mfinance.everjoy.app.presenter;

import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Messenger;

import com.mfinance.everjoy.app.CompanySettings;

import com.mfinance.everjoy.app.BasePopupDetailListener;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.PopupExcutedOrderDetailListener;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ExecutedOrder;
import com.mfinance.everjoy.app.util.Utility;

public class ExecutedOrderDetailPresenter extends BaseDetailPresenter{
	
	double dMPrice = 0.0;
	String sTmp = null; 
	double[] dBidAsk = null;
	int iRef ;
	Context context;
	String[] words = null;
	ExecutedOrder order;
	
	public ExecutedOrderDetailPresenter(BasePopupDetailListener view,
			Resources res, MobileTraderApplication app, Messenger mService,
			Messenger mServiceMessengerHandler) {
		super(view, res, app, mService, mServiceMessengerHandler);
	}
	
	
	@Override
    public void updateUI() {
		if (app.getSelectedExcutedOrder()!=null){
			order = (ExecutedOrder) app.getSelectedExcutedOrder();
			((PopupExcutedOrderDetailListener)view).getRefField().setText(order.getViewRef());
			if(((PopupExcutedOrderDetailListener)view).getExcAmountField()!=null)
				((PopupExcutedOrderDetailListener)view).getExcAmountField().setText("("+Utility.formatAmount(order.dExecutedAmount)+")");
		    ((PopupExcutedOrderDetailListener)view).getContractField().setText(order.contract.getContractName(app.locale));
		    ((PopupExcutedOrderDetailListener)view).getExeLotField().setText(  Utility.formatLot(order.dExecutedAmount / order.contract.iContractSize));
		    ((PopupExcutedOrderDetailListener)view).getBuySellField().setText( "B".equals(order.strBuySell) ? R.string.lb_buy : R.string.lb_sell);
		    if ("B".equals(order.strBuySell))
				((PopupExcutedOrderDetailListener)view).getBuySellField().setTextColor(Color.GREEN);
		    else
				((PopupExcutedOrderDetailListener)view).getBuySellField().setTextColor(Color.RED);
		    ((PopupExcutedOrderDetailListener)view).getTPriceField().setText( Utility.round(order.dRequestRate, order.contract.iRateDecPt, order.contract.iRateDecPt));
		    if(CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW){
				String limitStopText = order.iLiquidationMethod!=3?Utility.getStringById("tb_new", res):(order.iLimitStop == 0  ? res.getString(R.string.tb_limit)  : res.getString(R.string.tb_stop) );
		    	((PopupExcutedOrderDetailListener) view).getLimitStopField().setText(limitStopText);
		    }else
		    	((PopupExcutedOrderDetailListener) view).getLimitStopField().setText( order.iLimitStop == 0  ? R.string.tb_limit : R.string.tb_stop );
		    ((PopupExcutedOrderDetailListener)view).getOCOField().setText(order.iOCORef > 0 ? order.getOCOViewRef() : "---");
		    ((PopupExcutedOrderDetailListener)view).getExeDateField().setText(Utility.displayListingDate(order.sExecutedDate));
		    
		    if ("".equals(order.strLiqRef)) {
		    	if(((PopupExcutedOrderDetailListener)view).getLiqField()!=null)
		    		((PopupExcutedOrderDetailListener)view).getLiqField().setText("---");
		    } else {
				words = order.strLiqRef.split("\\|");
				if(((PopupExcutedOrderDetailListener)view).getLiqField()!=null)
					((PopupExcutedOrderDetailListener)view).getLiqField().setText(words[0]);
			}		    
		    
		    ((PopupExcutedOrderDetailListener)view).getExeTimeField().setText( order.sExecutedTime ); 
		}
		
    }
    
}
