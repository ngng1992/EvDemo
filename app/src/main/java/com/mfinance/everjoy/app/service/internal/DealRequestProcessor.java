package com.mfinance.everjoy.app.service.internal;

import java.math.BigDecimal;
import java.util.HashMap;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.bo.TransactionObjBuilder;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.AESLib;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class DealRequestProcessor implements MessageProcessor {
	private final String TAG = this.getClass().getSimpleName();

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		Bundle data = msg.getData();
		
		boolean newProtocol = false;
		MobileTraderApplication app = service.app;
		if(app.isDemoPlatform == true && CompanySettings.USE_NEW_DEAL_PROTOCOL_DEMO == true){
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 1 && CompanySettings.USE_NEW_DEAL_PROTOCOL == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 2 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD2 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 3 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD3 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 4 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD4 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 5 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD5 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 6 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD6 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 7 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD7 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 8 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD8 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 9 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD9 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 10 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD10 == true )
		{
			newProtocol = true;
		}
		
		String sTransactionID = data.getString(ServiceFunction.SEND_DEAL_REQUEST_ID);
        String sContract = data.getString(ServiceFunction.SEND_DEAL_REQUEST_CONTRACT);
        String sBuySell = data.getString(ServiceFunction.SEND_DEAL_REQUEST_BUY_SELL);
        double dDealRate = data.getDouble(ServiceFunction.SEND_DEAL_REQUEST_REQUEST_RATE);
        String sLot =  data.getString(ServiceFunction.SEND_DEAL_REQUEST_LOT);        
        int iContractSize =  data.getInt(ServiceFunction.SEND_DEAL_REQUEST_CONTRACT_SIZE);
		BigDecimal d1 = Utility.toBigDecimal(sLot, "0");
		BigDecimal d2 = new BigDecimal(String.valueOf(iContractSize));
        double dAmount = d1.multiply(d2).doubleValue();
        String sLPrice = data.getString(ServiceFunction.SEND_DEAL_REQUEST_LIMIT_PRICE);
        String sSPrice = data.getString(ServiceFunction.SEND_DEAL_REQUEST_STOP_PRICE);
        String sLGT = data.getString(ServiceFunction.SEND_DEAL_REQUEST_LIMIT_GT);
        String sSGT = data.getString(ServiceFunction.SEND_DEAL_REQUEST_STOP_GT);
        String sSlippage = data.getString(ServiceFunction.SEND_DEAL_REQUEST_SLIPPAGE);
		String sMarket = data.getString(ServiceFunction.SEND_DEAL_REQUEST_UNRESTRICTEDMARKET);
		String sTag = data.getString(ServiceFunction.SEND_DEAL_REQUEST_TAG);
		String sCTime = data.getString(ServiceFunction.SEND_DEAL_REQUEST_CTIME);
        
        try {			
			MessageObj dealRequestMsg = MessageObj.getMessageObj(IDDictionary.SERVER_DEAL_SERVICE_TYPE, IDDictionary.SERVER_DEAL_REQUEST_DEAL);

		    dealRequestMsg.addField(Protocol.AddDealRequest.GROUP_CODE, service.app.data.getBalanceRecord().strGroup);
		    dealRequestMsg.addField(Protocol.AddDealRequest.ACCOUNT_CODE, service.app.data.getBalanceRecord().strAccount);
		    dealRequestMsg.addField("accountID", service.app.data.getBalanceRecord().strAccount);
		    dealRequestMsg.addField(Protocol.AddDealRequest.BUY_SELL, sBuySell);
		    dealRequestMsg.addField(Protocol.AddDealRequest.REQUEST_PRICE, String.valueOf(dDealRate));
		    dealRequestMsg.addField(Protocol.AddDealRequest.CONTRACT, sContract);
		    dealRequestMsg.addField(Protocol.AddDealRequest.AMOUNT, String.valueOf(dAmount));
			dealRequestMsg.addField(Protocol.AddDealRequest.REQUEST_AMOUNT, String.valueOf(dAmount));
		    dealRequestMsg.addField(Protocol.AddDealRequest.CONTRACT_SIZE, String.valueOf(iContractSize));
		    dealRequestMsg.addField(Protocol.AddDealRequest.LIQUIDATION_METHOD, "-1");
		    dealRequestMsg.addField(Protocol.AddDealRequest.COMMENT, "");
		    dealRequestMsg.addField(Protocol.AddDealRequest.AUTO_GEN, "");
		    dealRequestMsg.addField(Protocol.AddDealRequest.PEND_ID, sTransactionID);

			if (CompanySettings.ENABLE_SLIPPAGE == false){
				dealRequestMsg.addField(Protocol.AddDealRequest.TRUE_MARKET, "true");
			}

		    if( newProtocol == true )
		    	dealRequestMsg.addField(Protocol.AddDealRequest.SLIPPAGE_NEW, sSlippage);
		    else
		    	dealRequestMsg.addField(Protocol.AddDealRequest.SLIPPAGE, sSlippage);
		        		    
		    if(sLPrice != null && !sLPrice.equals("-1")){
		    	dealRequestMsg.addField(Protocol.AddDealRequest.LIMIT_PRICE, sLPrice);
		    	dealRequestMsg.addField(Protocol.AddDealRequest.LIMIT_GT, sLGT);	
		    }else{
		    	dealRequestMsg.addField(Protocol.AddDealRequest.LIMIT_PRICE, "-1");			
		    }
		    
		    		    		    
		    if(sSPrice != null && !sSPrice.equals("-1")){
		    	dealRequestMsg.addField(Protocol.AddDealRequest.STOP_PRICE, sSPrice);
		    	dealRequestMsg.addField(Protocol.AddDealRequest.STOP_GT, sSGT);		    	
		    }else{
		    	dealRequestMsg.addField(Protocol.AddDealRequest.STOP_PRICE, "-1");
		    }


		    if (CompanySettings.AESCrypto) {
				dealRequestMsg.addField("bUnrestrictedMarket", sMarket);
				dealRequestMsg.addField("tag", sTag);
				dealRequestMsg.addField("ctime", sCTime);
			}

			if (CompanySettings.AESCrypto){
				dealRequestMsg.addField("sum", Utility.MD5(dealRequestMsg.getTradeCheckSum()+Utility.getTradeKey()+new String(AESLib.randomKey)));
			}
		    
		    HashMap<String, Double[]> providerQuoteMap = service.app.data.getContract(sContract).providerQuoteMap;
        	if (providerQuoteMap != null && providerQuoteMap.size() > 0) {
            	
        		dealRequestMsg.addField("providerNum", String.valueOf(providerQuoteMap.size()));
            	
            	int j = 1;
            	for (String provider : providerQuoteMap.keySet()) {
            		dealRequestMsg.addField("pname" + j, provider);
            		dealRequestMsg.addField("pbid" + j, String.valueOf(providerQuoteMap.get(provider)[0]));
            		dealRequestMsg.addField("pask" + j, String.valueOf(providerQuoteMap.get(provider)[1]));                        		
                	j++;
                 }
            }
        	
        	String tag = service.app.data.getContract(sContract).tag;
        	if (tag != null){
        		dealRequestMsg.addField("tag", tag);
        	}
        	
			TransactionObj transaction = new TransactionObjBuilder().setsTransactionID(sTransactionID).setsRef("").setsContract(sContract).setsAccount(service.app.data.getBalanceRecord().strAccount).setsBuySell(sBuySell).setdRequestRate(dDealRate).setiStatus(0).setsStatusMsg(MessageMapping.getMessageByCode(service.getRes(), "917", service.app.locale)).setiMsg(923).setiRemarkCode(923).setiType(0).setdAmount(dAmount).setContract(service.app.data.getContract(sContract)).createTransactionObj();
			transaction.sMsgCode = "917";
			transaction.iStatusMsg = 917;
			service.addTransaction(transaction);

		    if(service.app.data.getBalanceRecord().idelay==0){
		    	service.connection.sendMessage(dealRequestMsg.convertToString(true));
		    	//Log.i(TAG, "[Sent deal request.]");
		    }else{
				SendServerThread sendServerThread = new SendServerThread(service,dealRequestMsg.convertToString(true), service.app.data.getBalanceRecord().idelay ); 
				sendServerThread.start();	
		    }

			return true;
		} catch (Exception e) {
			Log.e(TAG, "Exception in processMessage",e);
			return false;
		}	
	}
	
	class SendServerThread extends Thread {
		FxMobileTraderService service;
		String msg;
		int delayTime;
		
		public SendServerThread(FxMobileTraderService service, String msg, int delayTime) {
		    this.service = service;   
		    this.msg = msg;
		    this.delayTime = delayTime;
		}

		public void run() {
			try { 
                sleep(delayTime * 1000);
                service.connection.sendMessage(msg);
                //Log.i(TAG, "[Sent deal request.]");
            } 
            catch(InterruptedException e) { 
    			Log.e(TAG, "Exception in processMessage[SendServerThread]",e);
            } 
		}
	}
}

