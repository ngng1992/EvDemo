package com.mfinance.everjoy.app.service.external;


import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.LiquidationRecord;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class LiquidationHistoryMessageHandler extends ServerMessageHandler {

	public LiquidationHistoryMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		
		if(msgObj == null){
			if (BuildConfig.DEBUG)
				Log.d(TAG, "msgObj is empty");
			return;
		}	
		
		//Log.d(TAG, "LiquidationHistoryMessageHandler " + msgObj.convertToString());
		
		String sStatus = msgObj.getField(Protocol.LiquidationHistoryMessage.STATUS);
		int numOfItem = Utility.toInteger(msgObj.getField(Protocol.LiquidationHistoryMessage.NUMBER_OF_RECORD), 0);
		
		if(sStatus.equals(FXConstants.COMMON_NEW)){
			service.app.data.clearLiquidationRecord();
		}
		
		for(int i = 1; i <= numOfItem; i++){
			String sAction = msgObj.getField(Protocol.LiquidationHistoryMessage.ACTION + i);										
			String sBRef = msgObj.getField(Protocol.LiquidationHistoryMessage.BUY_REF + i);
			String sSRef = msgObj.getField(Protocol.LiquidationHistoryMessage.SELL_REF + i);
			
			StringBuilder sb = new StringBuilder();
			sb.append(sBRef).append("|").append(sSRef);
			String sKey = sb.toString();
			
			sb = null;			
			LiquidationRecord record = null; 
		
			try{
				if(sAction == null || sAction.equals(FXConstants.COMMON_ADD)){
					record = new LiquidationRecord(sBRef, sSRef);
					assignValueToLiquidationRecord(msgObj, record, i);
					service.app.data.addLiquidationRecord(record);
				}else if (sAction.equals(FXConstants.COMMON_UPDATE)){
					record = service.app.data.getLiquidationRecord(sKey);
					assignValueToLiquidationRecord(msgObj, record, i);					
				}else if(sAction.equals(FXConstants.COMMON_DELETE)){		
					record = service.app.data.removeLiquidationRecord(sKey);
					record.contract = null;
				}	
	    	}catch(Exception e){
	    		Log.e(TAG, "Ref: "+ sKey, e.fillInStackTrace());
	    	}
		}
    	service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
	}
	
	public void assignValueToLiquidationRecord(MessageObj msgObj, LiquidationRecord record, int iIndex){

		String sAmount = msgObj.getField(Protocol.LiquidationHistoryMessage.AMOUNT + iIndex);
		String sExecDate = msgObj.getField(Protocol.LiquidationHistoryMessage.EXEC_DATE + iIndex);
		String sBDate = msgObj.getField(Protocol.LiquidationHistoryMessage.BUY_DATE + iIndex);
		String sSDate = msgObj.getField(Protocol.LiquidationHistoryMessage.SELL_DATE + iIndex);
		String sContract = msgObj.getField(Protocol.LiquidationHistoryMessage.CONTRACT + iIndex);
		String sBRate = msgObj.getField(Protocol.LiquidationHistoryMessage.BUY_RATE + iIndex);
		String sSRate = msgObj.getField(Protocol.LiquidationHistoryMessage.SELL_RATE + iIndex);
		String sCommission = msgObj.getField(Protocol.LiquidationHistoryMessage.COMMISSION + iIndex);
		String sPL = msgObj.getField(Protocol.LiquidationHistoryMessage.PL + iIndex);
		String sBorS = msgObj.getField(Protocol.LiquidationHistoryMessage.WHICH_IS_LIQ + iIndex);
		String aPL = msgObj.getField(Protocol.LiquidationHistoryMessage.APL + iIndex);
		String sRRate = msgObj.getField(Protocol.LiquidationHistoryMessage.RUNNING_RATE + iIndex);
		String sInterest = msgObj.getField(Protocol.LiquidationHistoryMessage.INTEREST + iIndex);
		String sFloatInterest = msgObj.getField(Protocol.LiquidationHistoryMessage.FLOATINTEREST + iIndex);
		
		if(record.contract == null){	
			
			ContractObj contract = service.app.data.getContract(sContract);
			record.contract = contract;			
		}
		
		record.dAmount = Utility.toDouble(sAmount, 0.0d);
		record.sExeDate = Utility.getDate(sExecDate);
		record.sExeTime = Utility.getDate(sExecDate);
		
		if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
			record.dPL = Utility.toDouble(sPL, 0.0d) / MobileTraderApplication.CONV_RATE;
		else
			record.dPL = Utility.toDouble(sPL, 0.0d) * MobileTraderApplication.CONV_RATE;
		
		if(aPL!=null){
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				record.aPL = Utility.toDouble(aPL, 0.0d) / MobileTraderApplication.CONV_RATE;
			else
				record.aPL = Utility.toDouble(aPL, 0.0d) * MobileTraderApplication.CONV_RATE;
			record.pldiff = record.aPL - record.dPL;
		}
		if(sInterest!=null){
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				record.dInterest = Utility.toDouble(sInterest, 0.0d) / MobileTraderApplication.CONV_RATE;
			else
				record.dInterest = Utility.toDouble(sInterest, 0.0d) * MobileTraderApplication.CONV_RATE;
		}
		if(sFloatInterest!=null){
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				record.dFloatInterest = Utility.toDouble(sFloatInterest, 0.0d) / MobileTraderApplication.CONV_RATE;
			else
				record.dFloatInterest = Utility.toDouble(sFloatInterest, 0.0d) * MobileTraderApplication.CONV_RATE;
		}
		if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
			record.dCommission = Utility.toDouble(sCommission, 0.0d) / MobileTraderApplication.CONV_RATE;
		else
			record.dCommission = Utility.toDouble(sCommission, 0.0d) * MobileTraderApplication.CONV_RATE;
		record.sBDate = Utility.dateLiquidationHistoryFormat(sBDate);
		record.sSDate = Utility.dateLiquidationHistoryFormat(sSDate);
		record.sBRate = Utility.round(sBRate, record.contract.iRateDecPt, record.contract.iRateDecPt);
		record.sSRate = Utility.round(sSRate, record.contract.iRateDecPt, record.contract.iRateDecPt);
		record.sBorS = sBorS;
		if(sRRate!=null)
			record.sRRate = Utility.round(sRRate, record.contract.iRateDecPt, record.contract.iRateDecPt);

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


