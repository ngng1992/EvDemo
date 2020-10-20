package com.mfinance.everjoy.app.service.external;

import java.util.Calendar;

import android.os.Bundle;

import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.bo.TransactionObjBuilder;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class TransactionMessageHandler extends ServerMessageHandler {

	public TransactionMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		if(msgObj == null){
			return;
		}
		
		//Log.e(TAG, "TMH: "  + msgObj.convertToString());
		
		try{
			String sID = msgObj.getField(Protocol.TransactionMessage.PEND_ID);
			String sNewID = msgObj.getField(Protocol.TransactionMessage.NEW_PEND_ID);
			String sCode = msgObj.getField(Protocol.TransactionMessage.ACTION_CODE);	
			int iMsgCode = Utility.toInteger(msgObj.getField(Protocol.TransactionMessage.MESSAGE_CODE + 1), -1);

			String sMsgCode = msgObj.getField(Protocol.TransactionMessage.MESSAGE_CODE + 1);
			String sLRef = msgObj.getField(Protocol.TransactionMessage.LIMIT_REF);
			String sSRef = msgObj.getField(Protocol.TransactionMessage.STOP_REF);
			String sReply = msgObj.getField(Protocol.TransactionMessage.REPLY);
			String sLiqmethod = msgObj.getField(Protocol.TransactionMessage.LIQUIDATION_METHOD);
			
			TransactionObj t = null;
			
			if(sNewID != null){
				t = service.removeTransaction(sID);
				t = new TransactionObjBuilder(t).setsTransactionID(sNewID).createTransactionObj();
				service.addTransaction(t);
			}else{
				t = service.app.data.getTransaction(sID);
			}
			
			// message will be sent in the callAnotherFunction when code = 300
		    if( sCode.equals("300") == false && sCode.equals("200") == false )
		    {
		    	if( msgObj.getField(Protocol.TransactionMessage.LIQUIDATION_REF) != null )
		    	{
		    		Bundle data = new Bundle();
		    		data.putString("liqref", msgObj.getField(Protocol.TransactionMessage.LIQUIDATION_REF).split("|")[0]);
		    		service.broadcast(ServiceFunction.ACT_TRADER_LIQUIDATE_RETURN, data);
		    	}
		    	else if( t.sLiqRef != null && t.sLiqRef.equals("") == false )
		    	{
	    			Bundle data = new Bundle();
	    	    	data.putString("liqref", t.sLiqRef);
	    	        service.broadcast(ServiceFunction.ACT_TRADER_LIQUIDATE_RETURN, data);
		    	}
		    }
		    
			// this is cut loss message
			if( "300".equals(sCode) && t != null )
			{
				t.sMsgCode = sMsgCode;
				if (!"".equals(sLRef)) t.sLRef = sLRef;
				if (!"".equals(sSRef)) t.sSRef = sSRef;
				if (!"".equals(sReply)) t.sReply = sReply;
				if (!"".equals(sLiqmethod)) t.sLiqmethod = sLiqmethod;
			}
			
			//new 0srv5npid1020500004toidcom1u1pidcom1u11301552541555code200
			//hold code200npid1020500005srv5toidcom1u1pid1020500004

			if("200".equals(sCode)){
				t.iStatus = 0;
				t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "917",service.app.locale);
				t.iStatusMsg = 917;
				t.sMsg = MessageMapping.getMessageByCode(service.getRes(), "920",service.app.locale);
				t.sMsgCode = "920";
			}else if("300".equals(sCode)){
				MessageObj cloneMsg = MessageObj.getMessageObj(IDDictionary.TRADER_DEAL_SERVICE_TYPE, IDDictionary.TRADER_RECEIVE_DEAL_MSG);
				cloneMsg.importBody(msgObj.cloneBody());
				service.postMessage(cloneMsg);
				return;
			}else if("403".equals(sCode)){
				t.iStatus = -1;
				/*
				t.sStatusMsg = MessageMapping.getMessageByCode(service.res, "934",service.app.locale);
				t.iStatusMsg = 934;
				t.sMsg = MessageMapping.getMessageByCode(service.res, "934",service.app.locale);
				t.sMsgCode = "934";
				*/
				String sTMsg = MessageMapping.getMessageByCode(service.getRes(), String.valueOf(iMsgCode),service.app.locale);
                if ((iMsgCode == 620)||(iMsgCode == 621)||(iMsgCode == 622))
            	{
                	double iMsgId2 = Utility.toDouble(msgObj.getField(Protocol.TransactionMessage.MESSAGE_CODE + 2), -1.0);
                	if ((iMsgId2 > 0))
                	{
                		t.sMsg1=String.valueOf(iMsgId2);
                		sTMsg=sTMsg.replaceFirst("(#s)", String.valueOf(iMsgId2));
                	}
                	else
                	{
                		// New prototype
                		if( msgObj.getField(Protocol.TransactionMessage.MESSAGE + 1) != null )
                		{
                			String tokens[] = msgObj.getField(Protocol.TransactionMessage.MESSAGE + 1).split(",");
                			if( tokens.length >= 2)
                			{
                				sTMsg = sTMsg.replaceFirst("(#s)", tokens[1]);
                				t.sMsg1 = tokens[1];
                			}
                		}
                	}
                	
                	t.sMsgCode = String.valueOf(iMsgCode);
					t.iStatusMsg = 918;
            	}else if(iMsgCode == 610){
            		String sTmp = msgObj.getField(Protocol.TransactionMessage.MESSAGE + 1);
					t.sOrderRef = sTmp;
					sTMsg = sTMsg.replace("#s", sTmp);
					
					t.iStatus = 1;
					t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "916",service.app.locale);
					t.sMsg = sTMsg;
					t.sRef = sTmp;
					t.sMsgCode = String.valueOf(iMsgCode);
					t.iStatusMsg = 916;
            	}else{
					t.sMsgCode = String.valueOf(iMsgCode);
					t.iStatusMsg = 918;
				}
				service.app.data.addSystemMessage(new SystemMessage(iMsgCode, sTMsg));		
			}

			if (t.sMsgCode.equals("920")){
				String sTMsg = MessageMapping.getMessageByCode(service.getApplicationContext(), "920");
				service.app.data.addSystemMessage(new SystemMessage(923, sTMsg));
			}

			t.dateLastUpdate = Calendar.getInstance().getTime();	
			service.updateTransactionToDB(t);
			//-- Facebook service.postMessageOnWall(t.sMsg);
			service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
		}catch(Exception e){
			e.printStackTrace();
			//System.out.println(msgObj.convertToString());
		}
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
