package com.mfinance.everjoy.app.service.external;


import android.os.Bundle;
import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.BalanceRecord;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class OpenPositionMessageHandler extends ServerMessageHandler {

	public OpenPositionMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {

		System.out.println("debug opmsg " + msgObj.convertToString());

		if(msgObj == null){
			if (BuildConfig.DEBUG)
				Log.d(TAG, "msgObj is empty");
			return;
		}
		
   		    		
		int numOfItem = Utility.toInteger(msgObj.getField(Protocol.PriceUpdate.NUMBER_OF_RECORD), 0);

		for(int i = 1; i <= numOfItem; i++){				
			String sAction = msgObj.getField("_is" + i);										
			int iRef = Utility.toInteger(msgObj.getField("ono" + i), -1);				
			OpenPositionRecord position = null;
						
			try{
				if(sAction == null || sAction.equals(FXConstants.COMMON_ADD)){
					position = new OpenPositionRecord(iRef);
					assignValueToPosition(msgObj, position, i);
					service.app.data.addOpenPosition(position);
				}else if (sAction.equals(FXConstants.COMMON_UPDATE)){
					position = service.app.data.getOpenPosition(iRef);
					assignValueToPosition(msgObj, position, i);					
				}else if(sAction.equals(FXConstants.COMMON_DELETE)){		
					position = service.app.data.removeOpenPosition(iRef);
					
					Bundle data = new Bundle();
			    	data.putString("liqref", String.valueOf(iRef));
			        service.broadcast(ServiceFunction.ACT_TRADER_LIQUIDATE_RETURN, data);
			        
					if(position.isBuyOrder){										
						position.contract.removeBuyPosition(iRef);
					}else{
						position.contract.removeSellPosition(iRef);
					}					
				}	
	    	}catch(Exception e){
	    		Log.e(TAG, "Ref: "+ iRef, e);
	    	}	
		}	    		

    	service.recalAll();
    	service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
    	LoginProgress.setPositionUpdated(service);
	}

	
	public void assignValueToPosition(MessageObj msgObj, OpenPositionRecord position, int iIndex){
		BalanceRecord balanceRecord = service.app.data.getBalanceRecord();
		
		String strBuySell = msgObj.getField("b/s" + iIndex);
		String strContract = msgObj.getField("ic" + iIndex);
		String strAccount = msgObj.getField("ac" + iIndex).trim();
		String strSyncNumber = msgObj.getField("sync" + iIndex);
		
		position.strTradeDate = Utility.getDate(msgObj.getField("ed" + iIndex));
		//position.strTradeDate = msgObj.getField("ed" + iIndex);
		position.iDp = Utility.toInteger(msgObj.getField("dp" + iIndex), 4);
        position.dDealRate = Utility.toDouble(msgObj.getField("ep" + iIndex), 0);
        if(msgObj.getField("rrate" + iIndex)!=null)
        	position.dRunningRate = Utility.toDouble(msgObj.getField("rrate" + iIndex), 0);
        if(msgObj.getField("vref" + iIndex)!=null)
        	position.viewRef = msgObj.getField("vref" + iIndex);
        position.strDealRate = msgObj.getField("ep" + iIndex);
        position.dealRate = new Double(position.strDealRate);
        position.dInitialRatio = Utility.toDouble(msgObj.getField("ir" + iIndex), 0);
        position.dAmount = Utility.roundToDouble(msgObj.getField("qty" + iIndex), 2, 2);
        position.strValueDate = msgObj.getField("vd" + iIndex);
        if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
        	position.dCommission = Utility.roundToDouble(msgObj.getField("com" + iIndex), 2, 2) / MobileTraderApplication.CONV_RATE;
        else
        	position.dCommission = Utility.roundToDouble(msgObj.getField("com" + iIndex), 2, 2) * MobileTraderApplication.CONV_RATE;
        if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
        	position.dInterest = Utility.roundToDouble(msgObj.getField("in" + iIndex), 2, 2) / MobileTraderApplication.CONV_RATE;
        else
        	position.dInterest = Utility.roundToDouble(msgObj.getField("in" + iIndex), 2, 2) * MobileTraderApplication.CONV_RATE;
        if(msgObj.getField("fin" + iIndex)!=null)
        {
        	if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
        		position.dFloatInterest = Utility.roundToDouble(msgObj.getField("fin" + iIndex), 2, 2) / MobileTraderApplication.CONV_RATE;
        	else
        		position.dFloatInterest = Utility.roundToDouble(msgObj.getField("fin" + iIndex), 2, 2) * MobileTraderApplication.CONV_RATE;
        }
        //position.dContractSize = Utility.toDouble(msgObj.getField("cs" + iIndex), 0);
        if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
        	position.dNewPosMargin = Utility.roundToDouble(msgObj.getField("nm"+iIndex), 2, 2) / MobileTraderApplication.CONV_RATE;
        else
        	position.dNewPosMargin = Utility.roundToDouble(msgObj.getField("nm"+iIndex), 2, 2) * MobileTraderApplication.CONV_RATE;
        if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
        	position.dHedgePosMargin = Utility.roundToDouble(msgObj.getField("hm"+iIndex), 2, 2) / MobileTraderApplication.CONV_RATE;
        else
        	position.dHedgePosMargin = Utility.roundToDouble(msgObj.getField("hm"+iIndex), 2, 2) * MobileTraderApplication.CONV_RATE;
        if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
        	position.dLatestMargin = Utility.roundToDouble(msgObj.getField("lm"+iIndex), 2, 2) / MobileTraderApplication.CONV_RATE;
        else
        	position.dLatestMargin = Utility.roundToDouble(msgObj.getField("lm"+iIndex), 2, 2) * MobileTraderApplication.CONV_RATE;
        
		position.strBuySell = strBuySell;
		if (strBuySell.equals("B")) position.isBuyOrder = true;
		else position.isBuyOrder = false;
		
		if(position.strAccount == null){		
			position.strAccount = strAccount;						
			position.accountBalance = balanceRecord;
			position.strSettleCurr = balanceRecord.strSettleCurr;
		}		
		
		position.accountBalance.updatePositionSyncNumber(strSyncNumber);
		
		if(position.contract == null){	
			
			ContractObj contract = service.app.data.getContract(strContract);
			
			if(contract == null){
				if (BuildConfig.DEBUG)
					Log.d(TAG, "Contract "+ strContract +"isn't found");
				contract = position.contract;
			}
			position.strContract = contract.strContractCode;		
			
			if(position.isBuyOrder){										
				contract.addBuyContract(position);
			}else{
				contract.addSellContract(position);
			}
			
		}
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


