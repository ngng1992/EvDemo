package com.mfinance.everjoy.app.service.external;


import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class RunningOrderMessageHandler extends ServerMessageHandler {

	public RunningOrderMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		
		if(msgObj == null){
			if (BuildConfig.DEBUG)
				Log.d(TAG, "msgObj is empty");
			return;
		}		
   		    		
		int numOfItem = Utility.toInteger(msgObj.getField(Protocol.RunningOrderMessage.NUMBER_OF_RECORD), 0);

		for(int i = 1; i <= numOfItem; i++){				
			String sAction = msgObj.getField(Protocol.RunningOrderMessage.ACTION + i);										
			int iRef = Utility.toInteger(msgObj.getField(Protocol.RunningOrderMessage.ORDER_REF + i), -1);
			
			OrderRecord order = null;
						
			try{
				if(sAction == null || sAction.equals(FXConstants.COMMON_ADD)){
					order = new OrderRecord(iRef);
					assignValueToPosition(msgObj, order, i);
					service.app.data.addRunningOrder(order);
				}else if (sAction.equals(FXConstants.COMMON_UPDATE)){
					order = service.app.data.getRunningOrder(iRef);
					assignValueToPosition(msgObj, order, i);					
				}else if(sAction.equals(FXConstants.COMMON_DELETE)){		
					order = service.app.data.removeRunningOrder(iRef);
					order.contract.removeRunningOrder(iRef);
				}	
	    	}catch(Exception e){
	    		Log.e(TAG, "Ref: "+ iRef, e);
	    	}	
		}	    		

    	service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
    	LoginProgress.setOrderUpdated(service);
	}
	
	public void assignValueToPosition(MessageObj msgObj, OrderRecord order, int iIndex){
				
		String sContract = msgObj.getField(Protocol.RunningOrderMessage.ITEM_NAME + iIndex);
		String sBuySell = msgObj.getField(Protocol.RunningOrderMessage.BUY_SELL + iIndex);
		String sAccount = msgObj.getField(Protocol.RunningOrderMessage.ACCOUNT + iIndex).trim();
		String sTradeDate = msgObj.getField(Protocol.RunningOrderMessage.TRADE_DATE + iIndex);
		String sRequestPrice = msgObj.getField(Protocol.RunningOrderMessage.PRICE + iIndex);
		String sAmount = msgObj.getField(Protocol.RunningOrderMessage.AMOUNT + iIndex);
		String sLimitOrStop = msgObj.getField(Protocol.RunningOrderMessage.LIMIT_OR_STOP + iIndex);
		String sGoodTillType = msgObj.getField(Protocol.RunningOrderMessage.GOOD_TILL_TYPE + iIndex);
		String sGoodTillTime = msgObj.getField(Protocol.RunningOrderMessage.GOOD_TILL_TIME + iIndex);
		String sDealer = msgObj.getField(Protocol.RunningOrderMessage.DEALER + iIndex);
		//String sDemicalPlace = msgObj.getField(Protocol.RunningOrderMessage.DEMICAL_PLACE + iIndex);
		//String sContractSize = msgObj.getField(Protocol.RunningOrderMessage.CONTRACT_SIZE + iIndex);
		String sLiqMethod = msgObj.getField(Protocol.RunningOrderMessage.LIQUIDATION_METHOD + iIndex);
		String sLiqRate = msgObj.getField(Protocol.RunningOrderMessage.LIQUIDATION_RATE + iIndex);
		String sLiqRef = msgObj.getField(Protocol.RunningOrderMessage.LIQUIDATION_REF + iIndex);
		String sOCORef = msgObj.getField(Protocol.RunningOrderMessage.OCO_REF + iIndex);
		String sTrailSize = msgObj.getField(Protocol.RunningOrderMessage.TRAILING_STOP + iIndex);
		String sProfitRate = msgObj.getField(Protocol.RunningOrderMessage.PROFIT_RATE + iIndex);
		String sCutRate = msgObj.getField(Protocol.RunningOrderMessage.CUT_RATE + iIndex);
		
		
		order.sAccount = sAccount;
		order.strBuySell = sBuySell;
		order.strTradeDate = Utility.getDate(sTradeDate);
		order.strContract = sContract;
		order.dRequestRate = Utility.toDouble(sRequestPrice, 0.0d);
		order.dAmount = Utility.toDouble(sAmount, 0.0d);
		order.iLimitStop = Utility.toInteger(sLimitOrStop, -1);
		order.iGoodTillType = Utility.toInteger(sGoodTillType, -1);
		order.strGoodTillDate = Utility.getDate(sGoodTillTime);
		order.strDealer = sDealer;
		//order.iDp = Utility.toInteger(sDemicalPlace, 0);
		//order.dContractSize = Utility.toDouble(sContractSize, 0);
		order.iLiquidationMethod = Utility.toInteger(sLiqMethod, -1);
		order.dLiqRate = Utility.toDouble(sLiqRate, 0.0d);
		order.strLiqRef = sLiqRef;
		order.iOCORef = Utility.toInteger(sOCORef, -1);
		order.iTrailingStopSize = Utility.toInteger(sTrailSize, -1);
		order.sTargetPosition = sLiqRef.contains("|")?sLiqRef.split("\\|")[0]:null;
		order.dOCOLimitRate = Utility.toDouble(sProfitRate, 0.0d);
		order.dOCOStopRate = Utility.toDouble(sCutRate, 0.0d);
		if(order.sAccount == null){		
			order.sAccount = sAccount;
		}		
		
		if(order.contract == null){	
			
			ContractObj contract = service.app.data.getContract(sContract);
			contract.addRunningOrder(order);			
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


