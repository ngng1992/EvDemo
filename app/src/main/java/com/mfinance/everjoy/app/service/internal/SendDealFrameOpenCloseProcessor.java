package com.mfinance.everjoy.app.service.internal;

import android.os.Bundle;
import android.os.Message;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

public class SendDealFrameOpenCloseProcessor implements MessageProcessor{
	private final String TAG = "SendDealFrameOpenCloseProcessor";

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		Bundle data = msg.getData();

		String Action = data.getString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_ACTION);	
		
		try {
			MessageObj dealOpenCloseMsg;
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
    		
			if( newProtocol == true )
				dealOpenCloseMsg = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, IDDictionary.SERVER_IO_TRADER_SEND_DEAL_INPUT_FRAME_OPEN_CLOSE);
			else
				dealOpenCloseMsg = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, IDDictionary.SERVER_IO_TRADER_SEND_DEAL_INPUT_FRAME_OPEN_CLOSE_OLD);
			
			if( Action.equals("open") || Action.equals("open_pending"))
			{
				dealOpenCloseMsg.addField(Protocol.DealFramOpenClose.SEND_DEAL_FRAME_OPEN_CLOSE_ACTION, Action);
				dealOpenCloseMsg.addField(Protocol.DealFramOpenClose.SEND_DEAL_FRAME_OPEN_CLOSE_ACCOUNT + 1, data.getString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_ACCOUNT));
				dealOpenCloseMsg.addField(Protocol.DealFramOpenClose.SEND_DEAL_FRAME_OPEN_CLOSE_CONTRACT + 1, data.getString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_CONTRACT));
				dealOpenCloseMsg.addField(Protocol.DealFramOpenClose.SEND_DEAL_FRAME_OPEN_CLOSE_BUY_SELL + 1, data.getString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_BUY_SELL));
				dealOpenCloseMsg.addField(Protocol.DealFramOpenClose.SEND_DEAL_FRAME_OPEN_CLOSE_TIME + 1, data.getString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_TIME));
				dealOpenCloseMsg.addField(Protocol.DealFramOpenClose.SEND_DEAL_FRAME_OPEN_CLOSE_NUM_OF_ITEM, "1");
			}
			else
			{
				dealOpenCloseMsg.addField(Protocol.DealFramOpenClose.SEND_DEAL_FRAME_OPEN_CLOSE_ACTION, Action);
				dealOpenCloseMsg.addField(Protocol.DealFramOpenClose.SEND_DEAL_FRAME_OPEN_CLOSE_ACCOUNT, data.getString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_ACCOUNT));
			}
			
			service.connection.sendMessage(dealOpenCloseMsg.convertToString(true));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
}