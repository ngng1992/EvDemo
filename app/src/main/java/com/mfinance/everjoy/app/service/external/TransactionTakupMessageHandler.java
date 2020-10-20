package com.mfinance.everjoy.app.service.external;

import java.util.Calendar;

import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;

public class TransactionTakupMessageHandler extends ServerMessageHandler {

	public TransactionTakupMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		//System.out.println("===========> TT: " + msgObj.convertToString());
		
		String sID = msgObj.getField(Protocol.TransactionMessage.PEND_ID);
		TransactionObj t = service.app.data.getTransaction(sID);		
		t.sMsgCode = "917";
		t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "917" ,service.app.locale);
		t.iStatusMsg = 917;
		t.sMsg = MessageMapping.getMessageByCode(service.getRes(), "919",service.app.locale);
		t.dateLastUpdate = Calendar.getInstance().getTime();
		service.updateTransactionToDB(t);
		//-- Facebook service.postMessageOnWall(t.sMsg);
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
