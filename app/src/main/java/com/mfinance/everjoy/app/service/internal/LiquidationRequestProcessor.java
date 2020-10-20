package com.mfinance.everjoy.app.service.internal;

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

public class LiquidationRequestProcessor implements MessageProcessor{
	private final String TAG = this.getClass().getSimpleName();
	private int iPending = 1;

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
		
        String sContract = data.getString(ServiceFunction.SEND_LIQUIDATION_REQUEST_CONTRACT);
        String sBuySell = data.getString(ServiceFunction.SEND_LIQUIDATION_REQUEST_BUY_SELL);
        double dRequestRate = data.getDouble(ServiceFunction.SEND_LIQUIDATION_REQUEST_REQUEST_RATE);
        double dAmount =  data.getDouble(ServiceFunction.SEND_LIQUIDATION_REQUEST_AMOUNT);        
        int iContractSize =  data.getInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_CONTRACT_SIZE);
        String sSlippage = data.getString(ServiceFunction.SEND_LIQUIDATION_REQUEST_SLIPPAGE);
        
        String sTransactionID = data.getString(ServiceFunction.SEND_LIQUIDATION_REQUEST_ID);

		String sMarket = data.getString(ServiceFunction.SEND_LIQUIDATION_REQUEST_UNRESTRICTEDMARKET);
		String sTag = data.getString(ServiceFunction.SEND_LIQUIDATION_REQUEST_TAG);
		String sCTime = data.getString(ServiceFunction.SEND_LIQUIDATION_REQUEST_CTIME);
        
        try {			
			MessageObj liquidationRequestMsg = MessageObj.getMessageObj(IDDictionary.SERVER_DEAL_SERVICE_TYPE, IDDictionary.SERVER_DEAL_REQUEST_DEAL);

		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.GROUP_CODE, service.app.data.getBalanceRecord().strGroup);
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.ACCOUNT_CODE, service.app.data.getBalanceRecord().strAccount);
		    liquidationRequestMsg.addField("accountID", service.app.data.getBalanceRecord().strAccount);
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.BUY_SELL, sBuySell);
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.REQUEST_PRICE, String.valueOf(dRequestRate));
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.CONTRACT, sContract);
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.AMOUNT, String.valueOf(dAmount));
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.REQUEST_AMOUNT, String.valueOf(dAmount));
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.CONTRACT_SIZE, String.valueOf(iContractSize));
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.LIQUIDATION_METHOD, "3");
		    if( data.containsKey(ServiceFunction.SEND_LIQUIDATION_REQUEST_REFS))
		    	liquidationRequestMsg.addField(Protocol.LiquidationRequest.LIQUIDATION_REF, data.getString(ServiceFunction.SEND_LIQUIDATION_REQUEST_REFS));
		    else //Backward compatible
		    	liquidationRequestMsg.addField(Protocol.LiquidationRequest.LIQUIDATION_REF, data.getInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_REF) +"|"+dAmount);
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.COMMENT, "");
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.AUTO_GEN, "");
		    liquidationRequestMsg.addField(Protocol.LiquidationRequest.PEND_ID, sTransactionID);
		    if( newProtocol == true )
		    	liquidationRequestMsg.addField(Protocol.LiquidationRequest.SLIPPAGE_NEW, sSlippage);
		    else
		    	liquidationRequestMsg.addField(Protocol.LiquidationRequest.SLIPPAGE, sSlippage);

			if (CompanySettings.ENABLE_SLIPPAGE == false){
				liquidationRequestMsg.addField(Protocol.LiquidationRequest.TRUE_MARKET, "true");
			}
		    
		    HashMap<String, Double[]> providerQuoteMap = service.app.data.getContract(sContract).providerQuoteMap;
        	if (providerQuoteMap != null && providerQuoteMap.size() > 0) {
            	
        		liquidationRequestMsg.addField("providerNum", String.valueOf(providerQuoteMap.size()));
            	
            	int j = 1;
            	for (String provider : providerQuoteMap.keySet()) {
            		liquidationRequestMsg.addField("pname" + j, provider);
            		liquidationRequestMsg.addField("pbid" + j, String.valueOf(providerQuoteMap.get(provider)[0]));
            		liquidationRequestMsg.addField("pask" + j, String.valueOf(providerQuoteMap.get(provider)[1]));                        		
                	j++;
                 }
            }
        	
        	String tag = service.app.data.getContract(sContract).tag;
        	if (tag != null){
        		liquidationRequestMsg.addField("tag", tag);
        	}

			if (CompanySettings.AESCrypto) {
				liquidationRequestMsg.addField("bUnrestrictedMarket", sMarket);
				liquidationRequestMsg.addField("tag", sTag);
				liquidationRequestMsg.addField("ctime", sCTime);
			}

			if (CompanySettings.AESCrypto){
				liquidationRequestMsg.addField("sum", Utility.MD5(liquidationRequestMsg.getTradeCheckSum()+Utility.getTradeKey()+new String(AESLib.randomKey)));
			}
        	
			TransactionObj transaction = new TransactionObjBuilder().setsTransactionID(sTransactionID).setsRef("").setsContract(sContract).setsAccount(service.app.data.getBalanceRecord().strAccount).setsBuySell(sBuySell).setdRequestRate(dRequestRate).setiStatus(0).setsStatusMsg(MessageMapping.getMessageByCode(service.getRes(), "917", service.app.locale)).setiMsg(923).setiRemarkCode(923).setiType(0).setdAmount((int) dAmount).setContract(service.app.data.getContract(sContract)).createTransactionObj();
			transaction.sMsgCode = "917";
			transaction.iStatusMsg = 917;
			transaction.sLiqRef = String.valueOf(data.getInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_REF));
			service.addTransaction(transaction);
			
		    if(service.app.data.getBalanceRecord().idelay==0){
		    	service.connection.sendMessage(liquidationRequestMsg.convertToString(true));
		    	//Log.i(TAG, "[Sent liquidation request.]");
		    }else{
				SendServerThread sendServerThread = new SendServerThread(service,liquidationRequestMsg.convertToString(true), service.app.data.getBalanceRecord().idelay ); 
				sendServerThread.start();	
		    }
		    iPending++;

			return true;
		} catch (Exception e) {
			Log.e(TAG, "Exception in processMessage",e.fillInStackTrace());
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
                //Log.i(TAG, "[Sent liquidation request.]");
            } 
            catch(InterruptedException e) { 
            	e.printStackTrace();
    			Log.e(TAG, "Exception in processMessage[SendServerThread]",e.fillInStackTrace());
            } 
		}
	}
}

