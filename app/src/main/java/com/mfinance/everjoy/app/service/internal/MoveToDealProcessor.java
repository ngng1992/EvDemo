package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Bundle;
import android.os.Message;

public class MoveToDealProcessor implements MessageProcessor {
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		String sContract = msg.getData().getString(ServiceFunction.DEAL_CONTRACT);		
		String sBuySell = msg.getData().getString(ServiceFunction.DEAL_BUY_SELL);		
		
		if(sContract == null){
			if (service.app.getDefaultContract()!=null)
				service.app.setSelectedContract(service.app.getDefaultContract().strContractCode);
			else {
				service.app.setSelectedContract(sContract);
			}			
			service.app.setSelectedBuySell("B");
		}else{
			service.app.setSelectedContract(sContract);
			service.app.setSelectedBuySell(sBuySell);
		}
		//System.out.println(service.app.getSelectedContract());
		
		service.broadcast(ServiceFunction.ACT_GO_TO_DEAL, null);		
		return true;
	}

}
