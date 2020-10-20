package com.mfinance.everjoy.app.service.external;

import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class ContractSpreadUpdateMessageHandler extends ServerMessageHandler {

	public ContractSpreadUpdateMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		//System.out.println("Update Spread==============");	
		int iNOC = 0;
		
		try{
		iNOC = Integer.parseInt(msgObj.getField(Protocol.ContractSettingUpdate.NUMBER_OF_RECORD));
		}catch(Exception e){
			try{
				iNOC = Integer.parseInt(msgObj.getField("NOC"));
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}

		for(int i = 1; i <= iNOC; i++){
			String sContract = msgObj.getField(Protocol.ContractSettingUpdate.ITEM_NAME+ i);
			ContractObj contract = null;
			
			contract = service.app.data.getContract(sContract);
			
			if( contract == null )
			{
				// add to the temporary table
				contract = new ContractObj();
			}
			
			if(contract != null){
				assignValueToContract(msgObj, contract, i);
				service.app.data.hmContractBidAskAdjTemp.put(sContract, contract);
				service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);				
			}				
		}
	}
	
	public void assignValueToContract(MessageObj msgObj, ContractObj contract, int iIndex){		
	
		double dBidAdjust;
		double dAskAdjust;
		double dBidAdjustRpt;
		double dAskAdjustRpt;

		if(CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX)
		{
			dBidAdjust = Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.BID_ADJUST_OTX + iIndex), 0.0d);
			dAskAdjust = Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.ASK_ADJUST_OTX + iIndex), 0.0d);			
		}
		else
		{
			dBidAdjust = Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.BID_ADJUST + iIndex), 0.0d);
			dAskAdjust = Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.ASK_ADJUST + iIndex), 0.0d);
			if (msgObj.getField(Protocol.ContractSettingUpdate.BID_ADJUST_RPT + iIndex) != null || msgObj.getField(Protocol.ContractSettingUpdate.ASK_ADJUST_RPT + iIndex) != null) {
				dBidAdjustRpt = Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.BID_ADJUST_RPT + iIndex), 0.0d);
				dAskAdjustRpt = Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.ASK_ADJUST_RPT + iIndex), 0.0d);
				contract.setBidAskAdjustRpt(dBidAdjustRpt, dAskAdjustRpt);
			}
		}
		
		int iOrderPips = Utility.toInteger(msgObj.getField(Protocol.ContractSettingUpdate.ORDER_PIPS + iIndex), 0);
		
		if( msgObj.getField(Protocol.ContractSettingUpdate.MIN_TRADE_LOT + iIndex) != null )
		{
			double dMinLot = Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.MIN_TRADE_LOT + iIndex), 0.0d);
			contract.setMinLot(dMinLot);
		}

		if( msgObj.getField(Protocol.ContractSettingUpdate.INCREMENT_TRADE_LOT + iIndex) != null )
		{
			double dIncLot = Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.INCREMENT_TRADE_LOT + iIndex), 0.0d);
			contract.setIncLot(dIncLot);
		}

		if (msgObj.getField(Protocol.ContractSettingUpdate.BID_ADJUST + iIndex) != null || msgObj.getField(Protocol.ContractSettingUpdate.ASK_ADJUST + iIndex) != null)
			contract.setBidAskAdjust(dBidAdjust, dAskAdjust);
		
		if(CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX && msgObj.getField(Protocol.ContractSettingUpdate.ORDER_PIPS + iIndex) != null )
			contract.setOrderPips(iOrderPips);

		if( msgObj.getField(Protocol.ContractSettingUpdate.MAX_LOT_LIMIT + iIndex) != null )
			contract.setMaxLotLimitUser(Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.MAX_LOT_LIMIT + iIndex), 1000000));

		if( msgObj.getField(Protocol.ContractSettingUpdate.AUTO_DEAL_AMOUT + iIndex) != null )
			contract.setMaxLotLimitUser(Utility.toDouble(msgObj.getField(Protocol.ContractSettingUpdate.AUTO_DEAL_AMOUT + iIndex), 1000000));
		//Log.e("ContractSpreadUpdateMessageHandler", contract.strContractCode +":"+dBidAdjust+" "+dAskAdjust+" "+iOrderPips);
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
