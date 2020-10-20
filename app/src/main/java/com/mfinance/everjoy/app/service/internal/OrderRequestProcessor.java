package com.mfinance.everjoy.app.service.internal;

import java.math.BigDecimal;

import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.bo.TransactionObjBuilder;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class OrderRequestProcessor implements MessageProcessor{
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		Bundle data = msg.getData();
		
		String sAction = data.getString(ServiceFunction.SEND_ORDER_REQUEST_ACTION);
		String sBuySell = data.getString(ServiceFunction.SEND_ORDER_REQUEST_BUY_SELL);
		String sRequestRate = data.getString(ServiceFunction.SEND_ORDER_REQUEST_REQUEST_RATE );		
		String sContract = data.getString(ServiceFunction.SEND_ORDER_REQUEST_CONTRACT);
		
		String sProfitRate = data.getString(ServiceFunction.SEND_ORDER_REQUEST_PROFIT_RATE );		
		String sCutRate = data.getString(ServiceFunction.SEND_ORDER_REQUEST_CUT_RATE);
		
		int iContractSize =  data.getInt(ServiceFunction.SEND_ORDER_REQUEST_CONTRACT_SIZE);
		String sLot =  data.getString(ServiceFunction.SEND_ORDER_REQUEST_LOT);
		
		BigDecimal d1 = Utility.toBigDecimal(sLot, "0");
		BigDecimal d2 = new BigDecimal(String.valueOf(iContractSize));
        double dAmount = d1.multiply(d2).doubleValue();

		int iLimitStop = data.getInt(ServiceFunction.SEND_ORDER_REQUEST_LIMIT_STOP);
		int iGT = data.getInt(ServiceFunction.SEND_ORDER_REQUEST_GOOD_TILL);
        String sGoodTillDate = "";
        String sGoodTillTime = "";   
        
		String sLiqMethod = "-1";		
		String sPositionRef = data.getString(ServiceFunction.SEND_ORDER_REQUEST_POSITION_REF);
		//double dPositionAmount = data.getDouble(ServiceFunction.SEND_ORDER_REQUEST_POSITION_AMOUNT);
		
		if(!"-1".equals(sPositionRef)){
			sLiqMethod = "3";			
		}
		
		String sOCORef = data.getString(ServiceFunction.SEND_ORDER_REQUEST_OCO);
		String sLiqRate = "-1";		
		int iTrail = data.getInt(ServiceFunction.SEND_ORDER_REQUEST_TRAILING_STOP_SIZE);		
		double dCurrRate = data.getDouble(ServiceFunction.SEND_ORDER_REQUEST_CURR_RATE);		
		String sTransactionID = data.getString("pid");
        try {			
			MessageObj orderRequestMsg = MessageObj.getMessageObj(IDDictionary.SERVER_ORDER_SERVICE_TYPE, IDDictionary.SERVER_ORDER_REQUEST_ORDER);
			orderRequestMsg.addField(Protocol.AddOrderRequest.ACTION, sAction);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.GROUP_CODE, service.app.data.getBalanceRecord().strGroup);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.ACCOUNT_CODE, service.app.data.getBalanceRecord().strAccount);
		    orderRequestMsg.addField("accountID", service.app.data.getBalanceRecord().strAccount);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.BUY_SELL, sBuySell);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.REQUEST_PRICE, sRequestRate);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.CONTRACT, sContract);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.AMOUNT, String.valueOf(dAmount));
		    orderRequestMsg.addField(Protocol.AddOrderRequest.CONTRACT_SIZE, String.valueOf(iContractSize));
		    orderRequestMsg.addField(Protocol.AddOrderRequest.LIMIT_STOP, String.valueOf(iLimitStop));
		    orderRequestMsg.addField(Protocol.AddOrderRequest.GOOD_TILL, String.valueOf(iGT));
		    orderRequestMsg.addField(Protocol.AddOrderRequest.GOOD_TILL_DATE, sGoodTillDate);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.GOOD_TILL_TIME, sGoodTillTime);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.PROFIT_GOOD_TILL, String.valueOf(iGT));
		    orderRequestMsg.addField(Protocol.AddOrderRequest.CUT_GOOD_TILL, String.valueOf(iGT));
		    orderRequestMsg.addField(Protocol.AddOrderRequest.LIQUIDATION_METHOD, sLiqMethod);
		    
		    if("3".equals(sLiqMethod)){
		    	orderRequestMsg.addField(Protocol.AddOrderRequest.LIQUIDATION_REF, sPositionRef + "|"+String.valueOf(dAmount)+",");	
		    }
		    
		    orderRequestMsg.addField(Protocol.AddOrderRequest.COMMENT, "");
		    orderRequestMsg.addField(Protocol.AddOrderRequest.OCO_REF, sOCORef);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.LIQUIDATION_RATE, sLiqRate);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.TRAILING_STOP, String.valueOf(iTrail));
		    orderRequestMsg.addField(Protocol.AddOrderRequest.CURRENT_PRICE, String.valueOf(dCurrRate));
		    
		    orderRequestMsg.addField(Protocol.AddOrderRequest.PEND_ID, sTransactionID);
		    
		    orderRequestMsg.addField(Protocol.AddOrderRequest.PROFIT_RATE, sProfitRate);
		    orderRequestMsg.addField(Protocol.AddOrderRequest.CUT_RATE, sCutRate);
		    
			TransactionObj transaction = new TransactionObjBuilder().setsTransactionID(sTransactionID).setsRef("").setsContract(sContract).setsAccount(service.app.data.getBalanceRecord().strAccount).setsBuySell(sBuySell).setdRequestRate(Double.valueOf(sRequestRate)).setiStatus(0).setsStatusMsg(MessageMapping.getMessageByCode(service.getRes(), "917", service.app.locale)).setiMsg(923).setiRemarkCode(923).setiType(1).setdAmount(dAmount).setContract(service.app.data.getContract(sContract)).createTransactionObj();
			transaction.sMsgCode = "917";
			service.addTransaction(transaction);

			service.connection.sendMessage(orderRequestMsg.convertToString(true));
			
		    //Log.i(TAG, "[Sent order request]["+orderRequestMsg.convertToString()+"]");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Exception in processMessage",e.fillInStackTrace());
			return false;
		}	
	}
	
}

