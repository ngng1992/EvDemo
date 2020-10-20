package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;

public class MoveToChartProcessor implements MessageProcessor {
	private final String TAG = "MoveToChartProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		String sContract = msg.getData().getString(ServiceFunction.CHART_CONTRACT);	
		
		if(sContract == null){
			if (service.app.getDefaultContract()!=null)
				service.app.setSelectedContract(service.app.getDefaultContract().strContractCode);
			else {
				service.app.setSelectedContract(sContract);
			}
			service.app.setSelectedBuySell("B");
		}else{
			service.app.setSelectedContract(sContract);
		}
		
		service.broadcast(ServiceFunction.ACT_GO_TO_CHART, msg.getData());
		return true;
	}

}
