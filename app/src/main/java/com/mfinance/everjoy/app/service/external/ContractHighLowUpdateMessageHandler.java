package com.mfinance.everjoy.app.service.external;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class ContractHighLowUpdateMessageHandler extends ServerMessageHandler {

	public ContractHighLowUpdateMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
	
		int iNOC = Integer.parseInt(msgObj.getField(Protocol.ContractHighLowUpdate.NUMBER_OF_RECORD));

		for(int i = 1; i <= iNOC; i++){
			String sContract = msgObj.getField(Protocol.ContractHighLowUpdate.ITEM_NAME+ i);
			ContractObj contract = null;
			
			contract = service.app.data.getContract(sContract);
			
			if(contract != null){
				assignValueToContract(msgObj, contract, i);			
				service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);				
			}				
		}	
		return;
	}
	
	public void assignValueToContract(MessageObj msgObj, ContractObj contract, int iIndex){			
		double dHighBid = Utility.toDouble(msgObj.getField(Protocol.ContractHighLowUpdate.HIGH_BID+iIndex), 0.0d);
		double dHighAsk = Utility.toDouble(msgObj.getField(Protocol.ContractHighLowUpdate.HIGH_ASK+iIndex), 0.0d);
		double dLowBid = Utility.toDouble(msgObj.getField(Protocol.ContractHighLowUpdate.LOW_BID+iIndex), 0.0d);
		double dLowAsk = Utility.toDouble(msgObj.getField(Protocol.ContractHighLowUpdate.LOW_ASK+iIndex), 0.0d);
		contract.setHighLow(dHighBid, dLowBid, dHighAsk, dLowAsk);
	}
	
	@Override
	public boolean isStatusLess() {
		return false;
	}

	@Override
	public boolean isBalanceRecalRequire() {
		return true;
	}

}

