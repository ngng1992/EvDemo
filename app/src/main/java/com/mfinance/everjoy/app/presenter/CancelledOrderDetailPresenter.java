package com.mfinance.everjoy.app.presenter;

import java.util.Locale;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Messenger;

import com.mfinance.everjoy.app.CompanySettings;

import com.mfinance.everjoy.app.BasePopupDetailListener;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.PopupCancelledOrderDetailListener;
import com.mfinance.everjoy.app.bo.CancelledOrder;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.R;
public class CancelledOrderDetailPresenter extends BaseDetailPresenter{
	
	CancelledOrder order = null;
	String[] words = null;
	
	public CancelledOrderDetailPresenter(BasePopupDetailListener view,
			Resources res, MobileTraderApplication app, Messenger mService,
			Messenger mServiceMessengerHandler) {
		super(view, res, app, mService, mServiceMessengerHandler);
	}
	
	@Override
    public void updateUI() {
		if (app.getSelectedCancelledOrder()!=null){
			order = (CancelledOrder) app.getSelectedCancelledOrder();
			((PopupCancelledOrderDetailListener)view).getRefField().setText(order.getViewRef());
			((PopupCancelledOrderDetailListener)view).getLotField().setText(Utility.formatLot(order.dAmount / order.contract.iContractSize));
			((PopupCancelledOrderDetailListener)view).getContractField().setText(order.contract.getContractName(app.locale));
			if(((PopupCancelledOrderDetailListener)view).getAmountField()!=null)
				((PopupCancelledOrderDetailListener)view).getAmountField().setText("("+Utility.formatAmount(order.dAmount)+")");
			((PopupCancelledOrderDetailListener)view).getBuySellField().setText("B".equals(order.strBuySell)? R.string.lb_buy : R.string.lb_sell);
			if ("B".equals(order.strBuySell))
				((PopupCancelledOrderDetailListener)view).getBuySellField().setTextColor(Color.GREEN);
			else
				((PopupCancelledOrderDetailListener)view).getBuySellField().setTextColor(Color.RED);
			((PopupCancelledOrderDetailListener)view).getTPriceField().setText(Utility.round(order.dRequestRate, order.contract.iRateDecPt, order.contract.iRateDecPt));
			if(CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW){
				String limitStopText = order.iLiquidationMethod!=3?Utility.getStringById("tb_new", res):(order.iLimitStop == 0  ? res.getString(R.string.tb_limit)  : res.getString(R.string.tb_stop) );
		    	((PopupCancelledOrderDetailListener) view).getLimitStopField().setText(limitStopText);
			}else
		    	((PopupCancelledOrderDetailListener) view).getLimitStopField().setText( order.iLimitStop == 0  ? R.string.tb_limit : R.string.tb_stop );
		    
			((PopupCancelledOrderDetailListener)view).getOCOField().setText(order.iOCORef > 0 ? order.getOCOViewRef() : "---");
			((PopupCancelledOrderDetailListener)view).getCancelDateField().setText(Utility.displayListingDate(order.sCancelledDate) + " " + order.sCancelledTime);
			if ("".equals(order.strLiqRef)) {
				if(((PopupCancelledOrderDetailListener)view).getLiqField()!=null)
					((PopupCancelledOrderDetailListener)view).getLiqField().setText("---");
			} else {
				words = order.strLiqRef.split("\\|");
				if(((PopupCancelledOrderDetailListener)view).getLiqField()!=null)
					((PopupCancelledOrderDetailListener)view).getLiqField().setText(words[0]);
			}
			if(order.iGoodTillType == 2){
				((PopupCancelledOrderDetailListener)view).getGoodTillField().setText(R.string.tb_end_of_day);
		    }
			else if(order.iGoodTillType == 1)
			{
				((PopupCancelledOrderDetailListener)view).getGoodTillField().setText( R.string.tb_cancel );
			}
			else{
		    	((PopupCancelledOrderDetailListener)view).getGoodTillField().setText(Utility.displayListingDate(order.strGoodTillDate));
		    } 
			
			
		}
    }
    
}
