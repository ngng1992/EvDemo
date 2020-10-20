package com.mfinance.everjoy.app.presenter;

import java.util.Locale;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Messenger;


import com.mfinance.everjoy.app.BasePopupDetailListener;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.PopupTransactionDetailListener;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

public class TransactionStatusDetailPresenter extends BaseDetailPresenter{
	
	double dMPrice = 0.0;
	String sTmp = null; 
	double[] dBidAsk = null;
	
	public TransactionStatusDetailPresenter(BasePopupDetailListener view,
			Resources res, MobileTraderApplication app, Messenger mService,
			Messenger mServiceMessengerHandler) {
		super(view, res, app, mService, mServiceMessengerHandler);
	}
	
	@Override
    public void updateUI() {
		if (app.getSelectedTransaction()!=null){
			((PopupTransactionDetailListener) view).getRefField().setText(app.getSelectedTransaction().sRef);
			
			((PopupTransactionDetailListener) view).getContractField().setText(app.getSelectedTransaction().contract.getContractName(app.locale));
			if ("610".equals(app.getSelectedTransaction().sMsgCode)){
				((PopupTransactionDetailListener) view).getLotField().setText("-");
				((PopupTransactionDetailListener) view).getAmountField().setText("-");
			}
			else {
				((PopupTransactionDetailListener) view).getLotField().setText(Utility.formatLot((double)app.getSelectedTransaction().dAmount / app.getSelectedTransaction().contract.iContractSize));
				((PopupTransactionDetailListener) view).getAmountField().setText("("+Utility.formatAmount(app.getSelectedTransaction().dAmount)+")");
			}
			((PopupTransactionDetailListener) view).getActionField().setText(app.getSelectedTransaction().iType == 0 ? R.string.lb_deal : R.string.lb_order);
			((PopupTransactionDetailListener) view).getPriceField().setText(Utility.round(app.getSelectedTransaction().dRequestRate, app.getSelectedTransaction().contract.iRateDecPt, app.getSelectedTransaction().contract.iRateDecPt));
			((PopupTransactionDetailListener) view).getBuySellField().setText("B".equals(app.getSelectedTransaction().sBuySell) ? R.string.lb_buy : R.string.lb_sell);
			if ("B".equals(app.getSelectedTransaction().sBuySell))
				((PopupTransactionDetailListener) view).getBuySellField().setTextColor(Color.GREEN);
			else
				((PopupTransactionDetailListener) view).getBuySellField().setTextColor(Color.RED);
			((PopupTransactionDetailListener) view).getTimeField().setText(Utility.getTime(app.getSelectedTransaction().dateLastUpdate));
			((PopupTransactionDetailListener) view).getStatusField().setText(   MessageMapping.getMessageByCode(res, (app.getSelectedTransaction().iStatusMsg) ,app.locale) );
			((PopupTransactionDetailListener) view).getMessageField().setText(String.valueOf(TransactionObj.getMessage(app.getSelectedTransaction(), res, app)));
		}
    }
    
}
