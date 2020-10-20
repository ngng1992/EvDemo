package com.mfinance.everjoy.app.service.external;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.AccountCreditType;
import com.mfinance.everjoy.app.bo.BalanceRecord;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

import java.math.BigDecimal;
import java.util.Optional;

public class BalanceUpdateMessageHandler extends ServerMessageHandler {

	public BalanceUpdateMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		int iNOC = Integer.parseInt(msgObj.getField(Protocol.AccountInfo.NUMBER_OF_RECORD));
		String sIndex = "1";
		if(iNOC > 0){					
			String strAction = msgObj.getField(Protocol.AccountInfo.ACTION+ sIndex);
			
			if(strAction.equals(FXConstants.COMMON_UPDATE) || strAction.equals(FXConstants.COMMON_ADD) || strAction.equals(FXConstants.COMMON_NEW)){
				BalanceRecord balance = null;
							
				if(service.app.data.getBalanceRecord() == null){
					balance = new BalanceRecord();
					service.app.data.setBalanceRecord(balance);
				}else{
					balance = service.app.data.getBalanceRecord();
				}
				
				assignValueToBalanceRecord(msgObj, balance, sIndex);
				
				service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);				
			}
		}
		
		LoginProgress.setAccountUpdated(service);
	}
	
	public void assignValueToBalanceRecord(MessageObj msgObj, BalanceRecord balanceRecord, String sIndex){
		balanceRecord.strAccount = msgObj.getField(Protocol.AccountInfo.ACCOUNT + sIndex);
		Optional.ofNullable(msgObj.getField(Protocol.AccountInfo.ACCOUNT_NAME + sIndex))
				.ifPresent(balanceRecord::setAccountName);
		balanceRecord.strAEHierarchy = msgObj.getField(Protocol.AccountInfo.AE_HIERARCHY + sIndex);
		balanceRecord.strAccExec = msgObj.getField(Protocol.AccountInfo.AE + sIndex);    						
		balanceRecord.strSettleCurr = msgObj.getField(Protocol.AccountInfo.SETTLEMENT_CURRENCY + sIndex);
		balanceRecord.strGroup = msgObj.getField(Protocol.AccountInfo.GROUP_CODE + sIndex);
		if (msgObj.getField(Protocol.AccountInfo.HEDGED + sIndex) != null) {
			try
			{
				boolean hedged = (Integer.valueOf(msgObj.getField(Protocol.AccountInfo.HEDGED + sIndex)) == 1 ? true : false);
				if( hedged != balanceRecord.hedged)
				{
					balanceRecord.hedged = hedged;
					service.broadcast(ServiceFunction.ACT_RELOAD_DASHBOARD, null);
				}
			}
			catch(Exception e)
			{
				balanceRecord.hedged = false;
			}
		}
		try
		{
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				balanceRecord.dBalance = Utility.roundToDouble(Double.parseDouble(msgObj.getField(Protocol.AccountInfo.BALANCE + sIndex)) / MobileTraderApplication.CONV_RATE, 2, 2);
			else
				balanceRecord.dBalance = Utility.roundToDouble(Double.parseDouble(msgObj.getField(Protocol.AccountInfo.BALANCE + sIndex)) * MobileTraderApplication.CONV_RATE, 2, 2);
		}
		catch(Exception e)
		{
			balanceRecord.dBalance = 0;
		}
		
		try
		{
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				balanceRecord.dPartialEquity = Utility.roundToDouble(Double.parseDouble(msgObj.getField(Protocol.AccountInfo.PARTIAL_EQUITY + sIndex)) / MobileTraderApplication.CONV_RATE, 2, 2);
			else
				balanceRecord.dPartialEquity = Utility.roundToDouble(Double.parseDouble(msgObj.getField(Protocol.AccountInfo.PARTIAL_EQUITY + sIndex)) * MobileTraderApplication.CONV_RATE, 2, 2);
		}
		catch(Exception e)
		{
			balanceRecord.dPartialEquity = 0;
		}
		
		balanceRecord.iNetType = Utility.toInteger(msgObj.getField(Protocol.AccountInfo.NET + sIndex), 0);
		balanceRecord.iUserType = Utility.toInteger(msgObj.getField(Protocol.AccountInfo.USER_TYPE + sIndex), -1);
		balanceRecord.strMarginCurr = msgObj.getField(Protocol.AccountInfo.MARGIN_CURRENCY + sIndex);
		
		try
		{
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				balanceRecord.dNewPosMargin = Utility.roundToDouble(Double.parseDouble(msgObj.getField(Protocol.AccountInfo.NEW_POSITION_MARGIN + sIndex)) / MobileTraderApplication.CONV_RATE, 2, 2);
			else
				balanceRecord.dNewPosMargin = Utility.roundToDouble(Double.parseDouble(msgObj.getField(Protocol.AccountInfo.NEW_POSITION_MARGIN + sIndex)) * MobileTraderApplication.CONV_RATE, 2, 2);
		}
		catch(Exception e)
		{
			balanceRecord.dNewPosMargin = 0;
		}
		try
		{
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				balanceRecord.dHedgePosMargin = Utility.roundToDouble(Double.parseDouble(msgObj.getField(Protocol.AccountInfo.HEDGE_POSITION_MARGIN + sIndex)) / MobileTraderApplication.CONV_RATE, 2, 2);
			else
				balanceRecord.dHedgePosMargin = Utility.roundToDouble(Double.parseDouble(msgObj.getField(Protocol.AccountInfo.HEDGE_POSITION_MARGIN + sIndex)) * MobileTraderApplication.CONV_RATE, 2, 2);
		}
		catch(Exception e)
		{
			
		}
		balanceRecord.iAutoLock = Utility.toInteger(msgObj.getField(Protocol.AccountInfo.IS_AUTO_CUT_LOSS + sIndex), 0);
		
		try
		{
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				balanceRecord.dPrimaryBalance = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.PRIMARY_BALANCE + sIndex)) / MobileTraderApplication.CONV_RATE;
			else
				balanceRecord.dPrimaryBalance = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.PRIMARY_BALANCE + sIndex)) * MobileTraderApplication.CONV_RATE;
		}
		catch(Exception e)
		{
			balanceRecord.dPrimaryBalance = 0.0;
		}
		
		try
		{
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				balanceRecord.dSecondaryBalance = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.SECONDARY_BALANCE + sIndex)) / MobileTraderApplication.CONV_RATE;
			else
				balanceRecord.dSecondaryBalance = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.SECONDARY_BALANCE + sIndex)) * MobileTraderApplication.CONV_RATE;
		}
		catch(Exception e)
		{
			balanceRecord.dSecondaryBalance = 0.0;
		}
		
		try
		{
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				balanceRecord.dLastBalance = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.LAST_BALANCE + sIndex)) / MobileTraderApplication.CONV_RATE;
			else
				balanceRecord.dLastBalance = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.LAST_BALANCE + sIndex)) * MobileTraderApplication.CONV_RATE;
		}
		catch(Exception e)
		{
			balanceRecord.dLastBalance = 0.0;
		}
		
		try
		{
			if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
				balanceRecord.dDayPL = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.PROFIT_AND_LOSS + sIndex)) / MobileTraderApplication.CONV_RATE;
			else
				balanceRecord.dDayPL = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.PROFIT_AND_LOSS + sIndex)) * MobileTraderApplication.CONV_RATE;
		}
		catch(Exception e)
		{
			balanceRecord.dDayPL = 0.0;
		}
		
		try
		{
			balanceRecord.dFixedSettlementExRate = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.FIXED_SETTLEMENT_EXCHANGE_RATE + sIndex));
		}
		catch(Exception e)
		{
			balanceRecord.dFixedSettlementExRate = 1.0;
		}
		
		
		balanceRecord.strSecurityRef = msgObj.getField(Protocol.AccountInfo.SECURITY_REF + sIndex);
		balanceRecord.bWaitBalanceUpdate = false;
		balanceRecord.strSyncNumber = msgObj.getField(Protocol.AccountInfo.SYNC_NUM + sIndex);
		
		try
		{
			balanceRecord.dFixedAmountMarginAlert = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.MARGIN_ALERT_RATIO + sIndex));
		}
		catch(Exception e)
		{
			balanceRecord.dFixedAmountMarginAlert = 0.0;
		}
		try
		{
			balanceRecord.dFixedAmountMarginCall = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.MARGIN_CALL_RATIO + sIndex));
		}
		catch(Exception e)
		{
			balanceRecord.dFixedAmountMarginCall = 0.0;
		}
		try
		{
			balanceRecord.dFixedAmountMarginCut = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.MARGIN_CUT_RATIO + sIndex));
		}
		catch(Exception e)
		{
			balanceRecord.dFixedAmountMarginCut = 0.0;
		}
		
		if (balanceRecord.dFixedAmountMarginAlert > 0)
		{
			balanceRecord.dFixedAmountMarginAlert /= 100.0;
		}		
		
		if (balanceRecord.dFixedAmountMarginCall > 0)
		{
			balanceRecord.dFixedAmountMarginCall /= 100.0;
		}

		
		if (balanceRecord.dFixedAmountMarginCut > 0)
		{
			balanceRecord.dFixedAmountMarginCut /= 100.0;
		}
		
		try
		{
			balanceRecord.dMinLotLimit = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.MIN_LOT_LIMIT + sIndex));
		}
		catch(Exception e)
		{
			balanceRecord.dMinLotLimit = 0.1d;
		}
		
		try
		{
			balanceRecord.dMinLotIncrementUnit = Double.parseDouble(msgObj.getField(Protocol.AccountInfo.MIN_INC_LOT_LIMIT + sIndex));
		}
		catch(Exception e)
		{
			balanceRecord.dMinLotIncrementUnit = 0.1d;
		}
		
		try
		{
			balanceRecord.creditRatio = Utility.roundToDouble(msgObj.getField(Protocol.AccountInfo.CREDIT_RATIO + sIndex), 2, 2) > 0 ? Utility.roundToDouble(msgObj.getField(Protocol.AccountInfo.CREDIT_RATIO + sIndex), 2, 2):1;
		}
		catch(Exception e)
		{
			balanceRecord.creditRatio = 0.0;
		}
		
		try
		{
			balanceRecord.idelay = Integer.valueOf(msgObj.getField(Protocol.AccountInfo.DELAY + sIndex));
		}
		catch(Exception e)
		{
			balanceRecord.idelay = 0;
		}
		
		try
		{
			balanceRecord.dbalin = Utility.toDouble(msgObj.getField(Protocol.AccountInfo.BALANCE_INTEREST + sIndex), 0);
		}
		catch(Exception e)
		{
			balanceRecord.dbalin = 0;
		}
		
		try
		{
			balanceRecord.dabalin = Utility.roundToDouble(msgObj.getField(Protocol.AccountInfo.ACCU_BALANCE_INTEREST + sIndex),2,2);
		}
		catch(Exception e)
		{
			balanceRecord.dabalin = 0;
		}
		
		if(msgObj.getField(Protocol.AccountInfo.CREDIT_LIMIT + sIndex)!=null)
		{
			balanceRecord.dCreditLimit = Utility.roundToDouble(msgObj.getField(Protocol.AccountInfo.CREDIT_LIMIT + sIndex),2,2);
		}
		
		try
		{
			if(msgObj.getField(Protocol.AccountInfo.INIT_MARGIN + sIndex)!=null)
			{
				double dInitialMargin = Utility.roundToDouble(msgObj.getField(Protocol.AccountInfo.INIT_MARGIN + sIndex),2,2);
				if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
					balanceRecord.dInitialMarginFromServer = dInitialMargin / MobileTraderApplication.CONV_RATE;
				else 
					balanceRecord.dInitialMarginFromServer = dInitialMargin * MobileTraderApplication.CONV_RATE;
			}
		}
		catch(Exception e)
		{
			
		}

		try
		{
			if(msgObj.getField(Protocol.AccountInfo.LEVERAGE + sIndex)!=null)
			{
				balanceRecord.dLeverage = Utility.roundToDouble(msgObj.getField(Protocol.AccountInfo.LEVERAGE + sIndex),2,2);
			}
		}
		catch(Exception e)
		{
			balanceRecord.dLeverage = 1;
		}

		{
			String realizedPNL = msgObj.getField(Protocol.AccountInfo.REALIZED_PNL + sIndex);
			if (realizedPNL != null) {
				try {
					balanceRecord.realizedPNL = new BigDecimal(realizedPNL);
				} catch (NumberFormatException e) {

				}
			}
		}
		//public final static String TRADABLE_MARGIN = "tradm";
		//		public final static String NON_TRADABLE_MARGIN = "ntradm";
		//		public final static String NON_TRADABLE_CREDIT = "ntradc";
		{
			String tradableMargin = msgObj.getField(Protocol.AccountInfo.TRADABLE_MARGIN + sIndex);
			if (tradableMargin != null) {
				try {
					balanceRecord.tradableMargin = new BigDecimal(tradableMargin);
				} catch (NumberFormatException e) {

				}
			}
		}
		{
			String nonTradableMargin = msgObj.getField(Protocol.AccountInfo.NON_TRADABLE_MARGIN + sIndex);
			if (nonTradableMargin != null) {
				try {
					balanceRecord.nonTradableMargin = new BigDecimal(nonTradableMargin);
				} catch (NumberFormatException e) {

				}
			}
		}
		{
			String nonTradableCredit = msgObj.getField(Protocol.AccountInfo.NON_TRADABLE_CREDIT + sIndex);
			if (nonTradableCredit != null) {
				try {
					balanceRecord.creditLimitNonTradable = new BigDecimal(nonTradableCredit);
				} catch (NumberFormatException e) {
					
				}
			}
		}
		try {
			Optional.ofNullable(msgObj.getField(Protocol.AccountInfo.FREE_LOT + sIndex))
					.map(BigDecimal::new)
					.ifPresent((v) -> balanceRecord.freeLot = v);
		} catch (NumberFormatException e) {

		}

		try {
			Optional.ofNullable(msgObj.getField(Protocol.AccountInfo.ENABLE_APP_DEPOSIT + sIndex))
					.map(Integer::parseInt)
					.map(i -> i > 0)
					.ifPresent(v -> balanceRecord.allowDeposit = v);
		} catch (Exception ex) {

		}
		try {
			Optional.ofNullable(msgObj.getField(Protocol.AccountInfo.ENABLE_APP_WITHDRAWAL + sIndex))
					.map(Integer::parseInt)
					.map(i -> i > 0)
					.ifPresent(v -> balanceRecord.allowWithdrawal = v);
		} catch (Exception ex) {

		}

		try {
			Optional.ofNullable(msgObj.getField(Protocol.AccountInfo.CREDIT_TYPE + sIndex))
					.map(Integer::parseInt)
					.map(AccountCreditType::fromInteger)
					.ifPresent(balanceRecord::setCreditType);
		} catch (Exception ex) {

		}

		try {
			Optional.ofNullable(msgObj.getField(Protocol.AccountInfo.CALL_MARGIN + sIndex))
					.map(BigDecimal::new)
					.ifPresent(balanceRecord::setCallMargin);
		} catch (Exception ex) {

		}

		//setCallMargin
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
