package com.mfinance.everjoy.app.service.external;


import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.bo.CancelledOrder;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.ExecutedOrder;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class CancelledOrderMessageHandler extends ServerMessageHandler {

	public CancelledOrderMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		
		if(msgObj == null){
			if (BuildConfig.DEBUG)
				Log.d(TAG, "msgObj is empty");
			return;
		}		
		//Log.d(TAG, "CancelledOrderMessageHandler " + msgObj.convertToString());
		
		//int iQueryType = Utility.toInteger(msgObj.getField(Protocol.ExecutedOrderMessage.QUERY_TYPE), 5);		
		int numOfItem = Utility.toInteger(msgObj.getField(Protocol.ExecutedOrderMessage.NUMBER_OF_RECORD), 0);
		String sStatus = msgObj.getField(Protocol.ExecutedOrderMessage.STATUS);
		
		if(sStatus.equals(FXConstants.COMMON_NEW)){
			service.app.data.clearCancelledOrder();
		}

		for(int i = 1; i <= numOfItem; i++){				
			String sAction = msgObj.getField(Protocol.ExecutedOrderMessage.ACTION + i);										
			int iRef = Utility.toInteger(msgObj.getField(Protocol.ExecutedOrderMessage.ORDER_REF + i), -1);
			
			CancelledOrder order = null;
						
			try{
				if(sAction == null || sAction.equals(FXConstants.COMMON_ADD)){
					order = new CancelledOrder(iRef);
					assignValueToCancelledOrder(msgObj, order, i);
					service.app.data.addCancelledOrder(order);
				}else if (sAction.equals(FXConstants.COMMON_UPDATE)){
					order = service.app.data.getCancelledOrder(iRef);
					assignValueToCancelledOrder(msgObj, order, i);					
				}else if(sAction.equals(FXConstants.COMMON_DELETE)){		
					order = service.app.data.removeCancelledOrder(iRef);
					order.contract = null;
				}	
	    	}catch(Exception e){
	    		Log.e(TAG, "Ref: "+ iRef, e.fillInStackTrace());
	    	}	
		}	    		

    	service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
    	LoginProgress.setCancelledOrderUpdated(service);    	
	}
	
	public void assignValueToCancelledOrder(MessageObj msgObj, CancelledOrder order, int iIndex){
		String sAccount = msgObj.getField(Protocol.CancelledOrderMessage.ACCOUNT + iIndex).trim();
		String sCancelledDate = msgObj.getField(Protocol.CancelledOrderMessage.EXECUTED_DATE + iIndex);
		String sContract = msgObj.getField(Protocol.CancelledOrderMessage.ITEM_NAME + iIndex);
		String sBuySell = msgObj.getField(Protocol.CancelledOrderMessage.BUY_SELL + iIndex);
		String sCreateDate = msgObj.getField(Protocol.CancelledOrderMessage.CREATED_DATE + iIndex);
		String sRequestPrice = msgObj.getField(Protocol.CancelledOrderMessage.PRICE + iIndex);
		String sAmount = msgObj.getField(Protocol.CancelledOrderMessage.AMOUNT + iIndex);
		String sLimitOrStop = msgObj.getField(Protocol.CancelledOrderMessage.LIMIT_OR_STOP + iIndex);
		String sGoodTillType = msgObj.getField(Protocol.CancelledOrderMessage.GOOD_TILL_TYPE + iIndex);
		String sGoodTillTime = msgObj.getField(Protocol.CancelledOrderMessage.GOOD_TILL_TIME + iIndex);
		String sDealer = msgObj.getField(Protocol.CancelledOrderMessage.DEALER + iIndex);
		String sLiqMethod = msgObj.getField(Protocol.CancelledOrderMessage.LIQUIDATION_METHOD + iIndex);
		String sOCORef = msgObj.getField(Protocol.CancelledOrderMessage.OCO_REF + iIndex);
		String sLiqRate = msgObj.getField(Protocol.CancelledOrderMessage.LIQUIDATION_RATE + iIndex);
		String sLiqRef = msgObj.getField(Protocol.CancelledOrderMessage.LIQUIDATION_REF + iIndex);		
			
		order.sAccount = sAccount;
		order.strBuySell = sBuySell;
		order.sCancelledDate = Utility.getDate(sCancelledDate);
		order.sCancelledTime = Utility.getTime(sCancelledDate);
		order.sCreateDate = Utility.getDate(sCreateDate);
		order.strContract = sContract;
		order.dRequestRate = Utility.toDouble(sRequestPrice, 0.0d);
		order.dAmount = Utility.toDouble(sAmount, 0.0d);
		order.iLimitStop = Utility.toInteger(sLimitOrStop, -1);
		order.iGoodTillType = Utility.toInteger(sGoodTillType, -1);
		order.strGoodTillDate = Utility.getDate(sGoodTillTime);
		order.strDealer = sDealer;
		order.iLiquidationMethod = Utility.toInteger(sLiqMethod, -1);
		order.dLiqRate = Utility.toDouble(sLiqRate, 0.0d);
		order.strLiqRef = sLiqRef;
		order.iOCORef = Utility.toInteger(sOCORef, -1);
		order.sTargetPosition = sLiqRef.contains("|")?sLiqRef.split("\\|")[0]:null;
	
		if(order.sAccount == null){		
			order.sAccount = sAccount;
		}		
		
		if(order.contract == null){	
			
			ContractObj contract = service.app.data.getContract(sContract);
			order.contract = contract;			
		}
		if(msgObj.getField("vref" + iIndex)!=null)
			order.viewRef = msgObj.getField("vref" + iIndex);	
	}

	@Override
	public boolean isStatusLess() {
		return false;
	}

	@Override
	public boolean isBalanceRecalRequire() {
		return true;
	}

}


