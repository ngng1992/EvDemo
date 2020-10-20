package com.mfinance.everjoy.app.service.external;

import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;

@Deprecated // TODO What is IDDictionary.TRADER_SHOW_MARGIN_MSG?
public class MarginMessageHandler extends ServerMessageHandler {

	@Deprecated
	public MarginMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msg) {
		String sAmount = msg.getField("amt");
		boolean bWithdrawal = sAmount.startsWith("-");
		String sMsg = msg.getField("msg");
		String sPopupMsg = "";
		
		if("0".equals(msg.getField("status"))){					
			if(bWithdrawal){
				sPopupMsg = MessageMapping.getMessageByCode(service.getRes(), "998",service.app.locale);
			}else{
				sPopupMsg = MessageMapping.getMessageByCode(service.getRes(), "997",service.app.locale);
			}
			sPopupMsg = sPopupMsg.replaceAll("@@1", sAmount);
		}else{
			if(bWithdrawal){
				sPopupMsg = MessageMapping.getMessageByCode(service.getRes(), "1000",service.app.locale)+"\""+sMsg+"\"";
			}else{
				sPopupMsg = MessageMapping.getMessageByCode(service.getRes(), "999",service.app.locale)+"\""+sMsg+"\"";
			}
		}
	
		service.app.data.addSystemMessage(new SystemMessage(				
				-1,
				sPopupMsg
		));
		
		service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
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
