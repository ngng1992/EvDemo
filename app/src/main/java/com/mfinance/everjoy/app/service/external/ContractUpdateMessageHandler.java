package com.mfinance.everjoy.app.service.external;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.pojo.ConnectionStatus;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.InterestRateMethod;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.PriceMessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class ContractUpdateMessageHandler extends ServerMessageHandler {


	private Queue<MessageObj> messageObjQueue = new ConcurrentLinkedDeque<>();
	public ContractUpdateMessageHandler(FxMobileTraderService service) {
		super(service);
		service.getScheduleTaskExecutor().scheduleWithFixedDelay(() -> {
			MessageObj msgObj = messageObjQueue.poll();
			while (msgObj != null) {
				handlePriceMessage(msgObj);
				msgObj = messageObjQueue.poll();
			}
			service.recalAll();
			service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
			LoginProgress.setPriceUpdated(service);
		}, (long)(1000 / CompanySettings.PRICE_QUOTE_REFRESH_PER_SECOND), (long)(1000 / CompanySettings.PRICE_QUOTE_REFRESH_PER_SECOND), TimeUnit.MILLISECONDS);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		if(msgObj == null){
			if (BuildConfig.DEBUG)
				Log.d(TAG, "msgObj is empty");
			return;
		}
		String full = msgObj.getField(Protocol.PriceUpdate.FULL);
		if (service.app.data.getContractList().size() == 0 && (full == null || !full.equals("1"))) {
			Log.d(TAG, "waiting for full contract update");
			return;
		}
		if (service.app.bPriceReload || (service.app.data.getGuestPriceAgentConnectionStatus() != ConnectionStatus.CONNECTED && !LoginProgress.isLoginComplete())){
			MessageObj msgObj1 = messageObjQueue.poll();
			while (msgObj1 != null) {
				handlePriceMessage(msgObj1);
				msgObj1 = messageObjQueue.poll();
			}
			service.recalAll();
			handlePriceMessage(msgObj);
			service.recalAll();
			service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
			LoginProgress.setPriceUpdated(service);
		} else {
			messageObjQueue.add(msgObj);
		}
	}

	private void handlePriceMessage(MessageObj msgObj) {
		if(service.app.bPriceReload){
			service.removePriceLoadingFromXML();
			service.app.data.cleanContract();
		}

		int numOfItem = Utility.toInteger(msgObj.getField(Protocol.PriceUpdate.NUMBER_OF_RECORD), 0);

		ArrayList<String> alTradableContract = (ArrayList<String>) service.app.data.getTradableContract().clone();

		boolean isPsMessage = (IDDictionary.TRADER_LIVE_PRICE_TYPE +"|"+IDDictionary.TRADER_UPDATE_STREAM_PRICE).equals(msgObj.getKey());
		if( isPsMessage == true )
		{
			if(!service.app.usingPriceStreaming)
				service.app.usingPriceStreaming = true;
		}

		if ((IDDictionary.TRADER_LIVE_PRICE_TYPE +"|"+IDDictionary.TRADER_UPDATE_LIVE_PRICE_WITH_DEPTH).equals(msgObj.getKey())){
			updateContract(msgObj, "DEPTH");
		}

		else {
			for (int i = 1; i <= numOfItem; i++) {
				String sAction = msgObj.getField(Protocol.PriceUpdate.ACTION + i);
				String sItem = msgObj.getField(Protocol.PriceUpdate.ITEM_NAME + i);
				String sView = msgObj.getField(Protocol.PriceUpdate.INTERNET_VIEW + i);
				String mkdepth = msgObj.getField("mkdepth" + i);

				if (sAction == null && sView == null && service.app.data.isContractExist(sItem)) {
					try {
						updateContract(msgObj, i);
					} catch (Exception e) {
					}
				} else {
					if (!CompanySettings.SHOW_NON_TRADEABLE_ITEM) {
						if (!alTradableContract.contains(sItem) && alTradableContract.size() > 0) {
							sView = "0";
						}
					}
					try {
					/*if("0".equals(sView)){
						service.app.data.removeContract(sItem);
					}else*/
						{
							if (sAction.equals(FXConstants.COMMON_ADD) || sAction.equals(FXConstants.COMMON_UPDATE) || sAction.equals(FXConstants.COMMON_ADD)) {
								if (!service.app.data.isContractExist(sItem)) {
									addContract(msgObj, i);
								} else {
									updateContract(msgObj, i);
								}
								parseDepth(mkdepth, sItem);
							}
						}
					} catch (Exception e) {
						//e.printStackTrace();
						//Log.e(TAG, sItem, e.fillInStackTrace());
					}
				}
			}
		}

		if(service.app.bPriceReload){
			service.reloadTransactionHistory();
			service.app.bPriceReload = false;
		}
	}

	private Map<ContractObj, ScheduledFuture<?>> clearChangeBitTimeoutMap = new HashMap<>();
	private ScheduledFuture<?> pendingUpdateUI = null;
	private void updateContract(MessageObj msgObj, int iIndex) {
		String sCode = msgObj.getField(Protocol.PriceUpdate.ITEM_NAME + iIndex);
		ContractObj contract = service.app.data.getContract(sCode);
		ScheduledFuture<?> existingScheduledFuture = clearChangeBitTimeoutMap.get(contract);
		if (existingScheduledFuture != null && !existingScheduledFuture.isDone() && !existingScheduledFuture.isCancelled()) {
			existingScheduledFuture.cancel(false);
		}
		clearChangeBitTimeoutMap.put(contract, service.getScheduleTaskExecutor().schedule(() -> {
			contract.bChangeBidAsk = false;
			contract.bChangeHighLow = false;
			service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
		}, 333, TimeUnit.MILLISECONDS));
		// update contract setting
		String sCounterRate = msgObj.getField(Protocol.PriceUpdate.COUNTER_RATE + iIndex);
		if(sCounterRate != null)
			contract.dCounterRate = Utility.toDouble(sCounterRate, -1);

		String sContractName = msgObj.getField(Protocol.PriceUpdate.PRICE_NAME + iIndex);
		if( sContractName != null )
			contract.strContractName = sContractName;

		String sENName  = msgObj.getField(Protocol.PriceUpdate.EN_PRICE_NAME  + iIndex);
		if( sENName != null )
			contract.sENName = sENName;

		String sTCName  = msgObj.getField(Protocol.PriceUpdate.TC_PRICE_NAME  + iIndex);
		if( sTCName != null )
			contract.sTCName = sTCName;

		String sSCName  = msgObj.getField(Protocol.PriceUpdate.SC_PRICE_NAME  + iIndex);
		if( sSCName != null )
			contract.sSCName = sSCName;

		String sCurr1 = msgObj.getField(Protocol.PriceUpdate.BASE_CURRENCY + iIndex);
		if( sCurr1 != null )
			contract.sCurr1 = sCurr1;

		String sCurr2 = msgObj.getField(Protocol.PriceUpdate.COUNTER_CURRENCY + iIndex);
		if( sCurr2 != null )
			contract.sCurr2 = sCurr2;

		try
		{
			if( msgObj.getField(Protocol.PriceUpdate.CONTRACT_SIZE + iIndex) != null )
				contract.iContractSize = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.CONTRACT_SIZE + iIndex));
		}
		catch(Exception e)
		{
		}

		if( msgObj.getField(Protocol.PriceUpdate.DIRECT_QUOTE + iIndex) != null  )
		{
			contract.bBaseCurr = "1".equals(msgObj.getField(Protocol.PriceUpdate.DIRECT_QUOTE+ iIndex));
		}

		if( msgObj.getField(Protocol.PriceUpdate.BSD + iIndex) != null )
			contract.bBSD = "1".equals(msgObj.getField(Protocol.PriceUpdate.BSD + iIndex));

		try{
			if( msgObj.getField(Protocol.PriceUpdate.ORDER_PIPS + iIndex) != null )
				contract.iOrderPips = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.ORDER_PIPS + iIndex));
		}catch(Exception e){}

		try
		{
			if( msgObj.getField(Protocol.PriceUpdate.MAX_TRAIL_STOP + iIndex) != null )
				contract.iTrailingStopMax = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.MAX_TRAIL_STOP + iIndex));
		}catch(Exception e){}

		try
		{
			if( msgObj.getField(Protocol.PriceUpdate.MIN_TRAIL_STOP + iIndex) != null )
				contract.iTrailingStopMin = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.MIN_TRAIL_STOP + iIndex));
		}catch(Exception e){}


		try
		{
			if( msgObj.getField(Protocol.PriceUpdate.BID_INTEREST_RATE + iIndex) != null )
				contract.dBidInterest = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.BID_INTEREST_RATE + iIndex), -1);
		}catch(Exception e){}

		try
		{
			if( msgObj.getField(Protocol.PriceUpdate.ASK_INTEREST_RATE + iIndex) != null )
				contract.dAskInterest = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.ASK_INTEREST_RATE + iIndex), -1);
		}catch(Exception e){}

		try
		{
			if( msgObj.getField(Protocol.PriceUpdate.DEMICAL_PLACE + iIndex) != null )
			{
				contract.iRateDecPt = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.DEMICAL_PLACE + iIndex));
				PriceMessageObj.addDecimalPlace(sCode, contract.iRateDecPt);
			}
		}catch(Exception e){}

		try
		{
			if(msgObj.getField(Protocol.PriceUpdate.COUNTER_DIRECT_QUTO + iIndex) != null )
				contract.bCounterDirectQuto = "1".equals(msgObj.getField(Protocol.PriceUpdate.COUNTER_DIRECT_QUTO + iIndex));
		}catch(Exception e){}

		try {
			Optional.ofNullable(msgObj.getField(Protocol.PriceUpdate.INTEREST_RATE_METHOD + iIndex))
					.map(s -> InterestRateMethod.fromValue(Integer.parseInt(s)))
					.ifPresent(i -> contract.setInterestRateMethod(i));
		} catch (Exception e) {

		}

		boolean isPsMessage = (IDDictionary.TRADER_LIVE_PRICE_TYPE +"|"+IDDictionary.TRADER_UPDATE_STREAM_PRICE).equals(msgObj.getKey());
		if((service.app.usingPriceStreaming&&isPsMessage)||!service.app.usingPriceStreaming)
		{
			String sBid = msgObj.getField(Protocol.PriceUpdate.BID + iIndex);
			if(sBid != null){
				double dBid = Utility.toDouble(sBid, -1);
				double dAsk = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.ASK + iIndex), -1);
				String sHigh = msgObj.getField(Protocol.PriceUpdate.HIGH + iIndex);
                String sLow = msgObj.getField(Protocol.PriceUpdate.LOW + iIndex);
				String sHighAsk = msgObj.getField(Protocol.PriceUpdate.HIGH_ASK_PS + iIndex);
                String sLowAsk = msgObj.getField(Protocol.PriceUpdate.LOW_ASK_PS + iIndex);
				if( Math.abs(dBid + 1) < 0.01 || Math.abs(dAsk + 1) < 0.01)
				{
					// Send error message to FxServer!
					//Bundle data = new Bundle();
					//data.putString(ServiceFunction.MESSAGE, "Invalid Price! Contract: " + sCode + " " + msgObj.getKey() + " " + sBid + "/" + msgObj.getField(Protocol.PriceUpdate.ASK + iIndex) + " " + msgObj.getField("tag" + iIndex));
					//service.broadcast(ServiceFunction.SRV_REPORT_ERROR, data);
				}
				else
				{
					contract.setBidAsk(dBid, dAsk);

					if(service.app.usingPriceStreaming&&isPsMessage){
						double dHigh, dLow, dHighAsk = contract.dHighAsk, dLowAsk = contract.dLowAsk;
						if( CompanySettings.SHOW_HIGHLOW_ASK == true )
						{
							dHigh = contract.dHighBid;
							dLow = contract.dLowBid;
						}
						else
						{
							dHigh = contract.dHigh;
							dLow = contract.dLow;
						}

						//High
                        if (sHigh != null) {
                        	dHigh = Utility.toDouble(sHigh, -1);
                            if (dHigh > 0) {
                            	dHigh = Utility.roundToDouble(dHigh + (contract.dOffset + contract.dBidAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt);
                            }
                        } else {
                        	if( (CompanySettings.SHOW_HIGHLOW_ASK == false && contract.dBidAsk[0]>contract.dHigh) ||
                        		(CompanySettings.SHOW_HIGHLOW_ASK == true && contract.dBidAsk[0]>contract.dHighBid) )
                        	{
                        		dHigh = contract.dBidAsk[0];
    						}
                        }

                        //Low
                        if (sLow != null) {
                        	dLow = Utility.toDouble(sLow, -1);
                        	if (dLow > 0) {
                        		dLow = Utility.roundToDouble(dLow + (contract.dOffset + contract.dBidAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt);
                        	}
                        } else {
                        	if( (CompanySettings.SHOW_HIGHLOW_ASK == false && contract.dBidAsk[0]<contract.dLow) ||
                            		(CompanySettings.SHOW_HIGHLOW_ASK == true && contract.dBidAsk[0]<contract.dLowBid) )
                        	{
                    			dLow = contract.dBidAsk[0];
                    		}
                    	}

                    	//HighAsk
                        if (sHighAsk != null) {
                        	dHighAsk = Utility.toDouble(sHighAsk, -1);
                            if (dHighAsk > 0) {
                            	dHighAsk = Utility.roundToDouble(dHighAsk + (contract.dOffset + contract.dAskAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt);
                            }
                        } else {
                        	if(contract.dBidAsk[1]>contract.dHighAsk){
                        		dHighAsk = contract.dBidAsk[1];
    						}
                        }

                        //LowAsk
                        if (sLowAsk != null) {
                        	dLowAsk = Utility.toDouble(sLowAsk, -1);
                        	if (dLowAsk > 0) {
                        		dLowAsk = Utility.roundToDouble(dLowAsk + (contract.dOffset + contract.dAskAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt);
                        	}

                        } else {
                        	if(contract.dBidAsk[1]<contract.dLowAsk){
                    			dLowAsk = contract.dBidAsk[1];
                    		}
                        }

                        if(CompanySettings.SHOW_HIGHLOW_ASK == true)
                        	contract.setHighLow(dHigh, dLow, dHighAsk, dLowAsk);
                        else
                        	contract.setHighLow(dHigh, dLow);
					}
				}
			}

			if(!service.app.usingPriceStreaming){
				String sHigh = msgObj.getField(Protocol.PriceUpdate.HIGH + iIndex);
				if(sHigh != null){
					double dHigh = Utility.toDouble(sHigh, -1);
					double dLow = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.LOW + iIndex), -1);
					contract.setHighLow(dHigh, dLow);
				}

				String sHighBid = msgObj.getField(Protocol.PriceUpdate.HIGH_BID + iIndex);
				if(sHighBid != null){
					double dHighBid = Utility.toDouble(sHighBid, -1);
					double dLowBid = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.LOW_BID + iIndex), -1);
					double dHighAsk = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.HIGH_ASK + iIndex), -1);
					double dLowAsk = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.LOW_ASK + iIndex), -1);
					contract.setHighLow(dHighBid, dLowBid, dHighAsk, dLowAsk);
				}
			}

			String tag = msgObj.getField("tag" + iIndex);
			if(tag!=null)
				contract.setTag(tag);
		}

		String sProviderQuote = msgObj.getField("prq" + iIndex);
		if(sProviderQuote!=null)
			contract.setProviderQuote(sProviderQuote);
	}

	private void updateContract(MessageObj msgObj, String action) {
		String sCode = msgObj.getField(Protocol.PriceUpdate.ITEM_NAME + "1");
		ContractObj contract = service.app.data.getContract(sCode);

		if (action.equals("DEPTH") && contract != null) {
			String sBid = msgObj.getField(Protocol.PriceUpdate.BID + "1");
			if (sBid != null) {
				double dBid = Utility.toDouble(sBid, -1);
				double dAsk = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.ASK + "1"), -1);
				String sHigh = msgObj.getField(Protocol.PriceUpdate.HIGH + "1");
				String sLow = msgObj.getField(Protocol.PriceUpdate.LOW + "1");
				String sHighAsk = msgObj.getField(Protocol.PriceUpdate.HIGH_ASK_PS + "1");
				String sLowAsk = msgObj.getField(Protocol.PriceUpdate.LOW_ASK_PS + "1");
				if (Math.abs(dBid + 1) < 0.01 || Math.abs(dAsk + 1) < 0.01) {
					// Send error message to FxServer!
					//Bundle data = new Bundle();
					//data.putString(ServiceFunction.MESSAGE, "Invalid Price! Contract: " + sCode + " " + msgObj.getKey() + " " + sBid + "/" + msgObj.getField(Protocol.PriceUpdate.ASK + iIndex) + " " + msgObj.getField("tag" + iIndex));
					//service.broadcast(ServiceFunction.SRV_REPORT_ERROR, data);
				} else {
					contract.setBidAsk(dBid, dAsk);
					for (int i = 1 ; i <= contract.bidPriceDepth.length ; i++){
						double depthBid = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.BID + String.valueOf(i)), 0);
						double depthAsk = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.ASK + String.valueOf(i)), 0);
						double depthBidLot = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.BIDLOT + String.valueOf(i)), 0);
						double depthAskLot = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.ASKLOT + String.valueOf(i)), 0);
						contract.bidPriceDepth[i-1] = String.valueOf(depthBid);
						contract.askPriceDepth[i-1] = String.valueOf(depthAsk);
						contract.bidLotDepth[i-1] = depthBidLot;
						contract.askLotDepth[i-1] = depthAskLot;
					}
				}
			}

			String tag = msgObj.getField("tag" + "1");
			if (tag != null)
				contract.setTag(tag);
		}
	}

	public void addContract(MessageObj msgObj, int iIndex) {			
		String sCode = msgObj.getField(Protocol.PriceUpdate.ITEM_NAME + iIndex);
		String sContractName = msgObj.getField(Protocol.PriceUpdate.PRICE_NAME + iIndex);
		
		String sENName  = msgObj.getField(Protocol.PriceUpdate.EN_PRICE_NAME  + iIndex);
		String sTCName  = msgObj.getField(Protocol.PriceUpdate.TC_PRICE_NAME  + iIndex);
		String sSCName  = msgObj.getField(Protocol.PriceUpdate.SC_PRICE_NAME  + iIndex);
				
		String sCurr1 = msgObj.getField(Protocol.PriceUpdate.BASE_CURRENCY + iIndex);
		String sCurr2 = msgObj.getField(Protocol.PriceUpdate.COUNTER_CURRENCY + iIndex);
		
		int iContractSize = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.CONTRACT_SIZE + iIndex));
		boolean bBaseCurr = "1".equals(msgObj.getField(Protocol.PriceUpdate.DIRECT_QUOTE+ iIndex));
		boolean bViewable = "1".equals(msgObj.getField(Protocol.PriceUpdate.ALLOW_VIEW + iIndex));
		boolean bTradable = "1".equals(msgObj.getField(Protocol.PriceUpdate.ALLOW_TRADE + iIndex));
		boolean bBSD = "1".equals(msgObj.getField(Protocol.PriceUpdate.BSD + iIndex));
		
		int iOrderPips = 0;
		try{
			iOrderPips = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.ORDER_PIPS + iIndex));
		}catch(Exception e){}
		
		int iTailMax = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.MAX_TRAIL_STOP + iIndex));
		int iTailMin = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.MIN_TRAIL_STOP + iIndex));
		
		double dBidInterest = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.BID_INTEREST_RATE + iIndex), -1);
		double dAskInterest = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.ASK_INTEREST_RATE + iIndex), -1);

		int iDP = Integer.parseInt(msgObj.getField(Protocol.PriceUpdate.DEMICAL_PLACE + iIndex));

		
		double dBid = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.BID + iIndex), -1);
		double dAsk = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.ASK + iIndex), -1);	
		double dHigh = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.HIGH + iIndex), -1);
		double dLow = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.LOW + iIndex), -1);		
		double dHighBid = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.HIGH_BID + iIndex), -1);
		double dLowBid = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.LOW_BID + iIndex), -1);
		double dHighAsk = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.HIGH_ASK_PS + iIndex), -1);
		double dLowAsk = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.LOW_ASK_PS + iIndex), -1);		
		double dCounterRate = Utility.toDouble(msgObj.getField(Protocol.PriceUpdate.COUNTER_RATE + iIndex), -1);
		
		boolean bCounterDirectQuto = "1".equals(msgObj.getField(Protocol.PriceUpdate.COUNTER_DIRECT_QUTO + iIndex));
		
		if( CompanySettings.SHOW_HIGHLOW_ASK == true && dLowBid < 0 )
			dLowBid = dLow;
		
		if( CompanySettings.SHOW_HIGHLOW_ASK == true && dHighBid < 0 )
			dHighBid = dHigh;

		ContractObj contractObj = new ContractObj(sCode, sContractName, sCurr1, sCurr2,
				iContractSize, bBaseCurr, bViewable, bTradable, iOrderPips,
				iTailMax, iTailMin, dBidInterest, dAskInterest, iDP,  
				dBid, dAsk, dHigh, dLow, dCounterRate, bCounterDirectQuto, sENName, sTCName, sSCName, bBSD, dHighBid, dHighAsk, dLowBid, dLowAsk
		);
		
		PriceMessageObj.addDecimalPlace(sCode, iDP);
		
		String sProviderQuote = msgObj.getField("prq" + iIndex);
		if(sProviderQuote!=null)
			contractObj.setProviderQuote(sProviderQuote);
		
		String tag = msgObj.getField("tag" + iIndex);
		if(tag!=null)
			contractObj.setTag(tag);

		String enableDepth = msgObj.getField("enabledepth" + iIndex);
		if(enableDepth!=null)
			contractObj.setEnableDepth(enableDepth);
		try {
			Optional.ofNullable(msgObj.getField(Protocol.PriceUpdate.INTEREST_RATE_METHOD + iIndex))
					.map(s -> InterestRateMethod.fromValue(Integer.parseInt(s)))
					.ifPresent(i -> contractObj.setInterestRateMethod(i));
		} catch (Exception e) {

		}
		if( service.app.data.hmContractBidAskAdjTemp.containsKey(sCode) )
		{
			ContractObj temp = service.app.data.hmContractBidAskAdjTemp.remove(sCode);
			contractObj.setMinLot(temp.dMinLot);
			contractObj.setIncLot(temp.dIncLot);
			contractObj.setMaxLotLimitUser(temp.getMaxLotLimitUser());
			contractObj.setBidAskAdjustRpt(temp.dBidAdjustRpt, temp.dAskAdjustRpt);
			contractObj.setBidAskAdjust(temp.dBidAdjust, temp.dAskAdjust);
			
			if( CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX )
				contractObj.setOrderPips(temp.iOrderPips);
		}
		try {
			if( msgObj.getField(Protocol.PriceUpdate.MAX_LOT_LIMIT + iIndex) != null ){
				if (msgObj.getField(Protocol.PriceUpdate.MAX_LOT_LIMIT + iIndex).equals("UNLIMITED"))
					contractObj.setMaxLotLimit(50.0);
				else
					contractObj.setMaxLotLimit(Double.parseDouble(msgObj.getField(Protocol.PriceUpdate.MAX_LOT_LIMIT + iIndex)));
			}

		} catch (Exception ex) {

		}
		service.app.data.addContract(contractObj);
	}

	public void parseDepth(String mkdepth, String sCode) {
		ContractObj contract = service.app.data.getContract(sCode);
		contract.parseDepth(mkdepth);
	}


	@Override
	public boolean isStatusLess() {
		return true;
	}

	@Override
	public boolean isBalanceRecalRequire() {
		return true;
	}

}


