package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;

public class MoveToContractDetailProcessor implements MessageProcessor {
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		String sContract = msg.getData().getString(ServiceFunction.CONTRACT_DETAIL_CONTRACT);
		
		if(sContract == null){
			if (service.app.getDefaultContract()!=null)
				service.app.setSelectedContract(service.app.getDefaultContract().strContractCode);
			else {
				service.app.setSelectedContract(sContract);
			}
		}else{
			service.app.setSelectedContract(sContract);
			ContractObj selectedContract = service.app.getSelectedContract();
			if (selectedContract == null || !selectedContract.isViewable()) {
				service.app.setSelectedContract(service.app.getDefaultContract().strContractCode);
			}
		}


		//Log.i(TAG, sContract);
		service.broadcast(ServiceFunction.ACT_GO_TO_CONTRACT_DETAIL, msg.getData());
		return true;
	}

}
