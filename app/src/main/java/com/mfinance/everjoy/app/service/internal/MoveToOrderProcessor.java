package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Bundle;
import android.os.Message;

public class MoveToOrderProcessor implements MessageProcessor {
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		String sContract = msg.getData().getString(ServiceFunction.ORDER_CONTRACT);
		String sBuySell = msg.getData().getString(ServiceFunction.ORDER_BUY_SELL);
		int iLimitStop = msg.getData().getInt(ServiceFunction.ORDER_LIMIT_STOP);
		int iDealRef = msg.getData().getInt(ServiceFunction.ORDER_DEAL_REF);
		if(sContract == null){
			if (service.app.getDefaultContract()!=null)
				service.app.setSelectedContract(service.app.getDefaultContract().strContractCode);
			else {
				service.app.setSelectedContract(sContract);
			}
			
			service.app.setSelectedBuySell("B");
			service.app.setSelectedLimitStop(0);	
			service.app.setSelectedOpenPosition(-1);
		}else{
			service.app.setSelectedContract(sContract);
			service.app.setSelectedBuySell(sBuySell);
			service.app.setSelectedLimitStop(iLimitStop);
			service.app.setSelectedOpenPosition(iDealRef);
		}
		
		service.broadcast(ServiceFunction.ACT_GO_TO_ORDER, null);
		
		return true;
	}

}
