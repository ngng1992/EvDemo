package com.mfinance.everjoy.app.service.external;


import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.ExecutedOrder;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class ExecutedOrderMessageHandler extends ServerMessageHandler {

	public ExecutedOrderMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		
		if(msgObj == null){
			if (BuildConfig.DEBUG)
				Log.d(TAG, "msgObj is empty");
			return;
		}		
		//Log.d(TAG, "ExecutedOrderMessageHandler " + msgObj.convertToString());
		
		//int iQueryType = Utility.toInteger(msgObj.getField(Protocol.ExecutedOrderMessage.QUERY_TYPE), 5);		
		int numOfItem = Utility.toInteger(msgObj.getField(Protocol.ExecutedOrderMessage.NUMBER_OF_RECORD), 0);
		String sStatus = msgObj.getField(Protocol.ExecutedOrderMessage.STATUS);
		
		if(sStatus.equals(FXConstants.COMMON_NEW)){
			service.app.data.clearExecutedOrder();
		}

		for(int i = 1; i <= numOfItem; i++){				
			String sAction = msgObj.getField(Protocol.ExecutedOrderMessage.ACTION + i);										
			int iRef = Utility.toInteger(msgObj.getField(Protocol.ExecutedOrderMessage.ORDER_REF + i), -1);
			
			ExecutedOrder order = null;
						
			try{
				if(sAction == null || sAction.equals(FXConstants.COMMON_ADD)){
					order = new ExecutedOrder(iRef);
					assignValueToExecutedOrder(msgObj, order, i);
					service.app.data.addExecutedOrder(order);
				}else if (sAction.equals(FXConstants.COMMON_UPDATE)){
					order = service.app.data.getExecutedOrder(iRef);
					assignValueToExecutedOrder(msgObj, order, i);					
				}else if(sAction.equals(FXConstants.COMMON_DELETE)){		
					order = service.app.data.removeExecutedOrder(iRef);
					order.contract = null;
				}	
	    	}catch(Exception e){
	    		Log.e(TAG, "Ref: "+ iRef, e.fillInStackTrace());
	    	}	
		}	    		

    	service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
    	LoginProgress.setExecutedOrderUpdated(service);
	}
	
	public void assignValueToExecutedOrder(MessageObj msgObj, ExecutedOrder order, int iIndex){
		String sAccount = msgObj.getField(Protocol.ExecutedOrderMessage.ACCOUNT + iIndex).trim();
		String sExecutedDate = msgObj.getField(Protocol.ExecutedOrderMessage.EXECUTED_DATE + iIndex);
		String sContract = msgObj.getField(Protocol.ExecutedOrderMessage.ITEM_NAME + iIndex);
		String sBuySell = msgObj.getField(Protocol.ExecutedOrderMessage.BUY_SELL + iIndex);
		String sCreateDate = msgObj.getField(Protocol.ExecutedOrderMessage.CREATED_DATE + iIndex);
		String sRequestPrice = msgObj.getField(Protocol.ExecutedOrderMessage.PRICE + iIndex);
		String sAmount = msgObj.getField(Protocol.ExecutedOrderMessage.AMOUNT + iIndex);
		String sLimitOrStop = msgObj.getField(Protocol.ExecutedOrderMessage.LIMIT_OR_STOP + iIndex);
		String sGoodTillType = msgObj.getField(Protocol.ExecutedOrderMessage.GOOD_TILL_TYPE + iIndex);
		String sGoodTillTime = msgObj.getField(Protocol.ExecutedOrderMessage.GOOD_TILL_TIME + iIndex);
		String sDealer = msgObj.getField(Protocol.ExecutedOrderMessage.DEALER + iIndex);
		//String sDemicalPlace = msgObj.getField(Protocol.ExecutedOrderMessage.DEMICAL_PLACE + iIndex);
		//String sContractSize = msgObj.getField(Protocol.ExecutedOrderMessage.CONTRACT_SIZE + iIndex);
		String sLiqMethod = msgObj.getField(Protocol.ExecutedOrderMessage.LIQUIDATION_METHOD + iIndex);
		String sOCORef = msgObj.getField(Protocol.ExecutedOrderMessage.OCO_REF + iIndex);
		String sLiqRate = msgObj.getField(Protocol.ExecutedOrderMessage.LIQUIDATION_RATE + iIndex);
		String sLiqRef = msgObj.getField(Protocol.ExecutedOrderMessage.LIQUIDATION_REF + iIndex);		
		String sExecutedAmount = msgObj.getField(Protocol.ExecutedOrderMessage.EXECUTED_AMOUNT + iIndex);		
		
		order.sAccount = sAccount;
		order.strBuySell = sBuySell;
		order.sExecutedDate = Utility.getDate(sExecutedDate);
		order.sExecutedTime = Utility.getTime(sExecutedDate);
		order.sCreateDate = Utility.getDate(sCreateDate);
		order.strContract = sContract;
		order.dRequestRate = Utility.toDouble(sRequestPrice, 0.0d);
		order.dAmount = Utility.toDouble(sAmount, 0.0d);
		order.iLimitStop = Utility.toInteger(sLimitOrStop, -1);
		order.iGoodTillType = Utility.toInteger(sGoodTillType, -1);
		order.strGoodTillDate = Utility.getDate(sGoodTillTime);
		order.strDealer = sDealer;
		order.dExecutedAmount = Utility.toDouble(sExecutedAmount, 0);
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
		return false;
	}

}


