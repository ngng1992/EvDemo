package com.mfinance.everjoy.app.service.external;

import android.util.Log;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class ReportGroupContractSettingUpdateMessageHandler extends ServerMessageHandler {

	public ReportGroupContractSettingUpdateMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
//		System.out.println("ReportGroupContractSettingUpdateMessageHandler: "+msgObj.convertToString());
		if(service.app.data.getContractList().size() == 0){
			service.postMessage(msgObj);
			return;
		}
		
		int iNOC = Integer.parseInt(msgObj.getField(Protocol.ReportGroupContractSettingUpdate.NUMBER_OF_RECORD));

		for(int i = 1; i <= iNOC; i++){
			String sContract = msgObj.getField(Protocol.ReportGroupContractSettingUpdate.ITEM_NAME+ i);
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
		double dOffset = Utility.toDouble(msgObj.getField(Protocol.ReportGroupContractSettingUpdate.OFFSET + iIndex), 0.0d);
		double dSpread = Utility.toDouble(msgObj.getField(Protocol.ReportGroupContractSettingUpdate.SPREAD + iIndex), 0.0d);
        String sStopTrade = msgObj.getField(Protocol.ReportGroupContractSettingUpdate.STOP_TRADE + iIndex);
        String sViewable = msgObj.getField(Protocol.ReportGroupContractSettingUpdate.VIEWABLE + iIndex);        
        int iOrderPips = Utility.toInteger(msgObj.getField(Protocol.ReportGroupContractSettingUpdate.ORDER_PIPS + iIndex), 0);
        
        if(sStopTrade.equals("1")){
        	contract.setRptTradable(false);	
        }else{
        	contract.setRptTradable(true);	
        }
        
        if(sViewable.equals("1")){
        	contract.setRptViewable(true);	
        }else{
        	contract.setRptViewable(false);
        }
        
        contract.setOrderPips(iOrderPips);        
		contract.setOffsetAndSpread(dOffset, dSpread);

		//Log.e("ReportGroupContractSettingUpdateMessageHandler", contract.strContractCode +":"+dOffset+" "+dSpread+" "+iOrderPips);
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

