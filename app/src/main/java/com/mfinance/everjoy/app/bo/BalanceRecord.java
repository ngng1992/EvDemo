package com.mfinance.everjoy.app.bo;

import java.math.BigDecimal;


public class BalanceRecord
{
	public final static int ADD_POSITION = 1;
	public final static int REMOVE_POSITION = -1;
	
  public String strAEHierarchy = null;
  public String strAccExec = null;
  public String strAccount = null;
  private String accountName = "";
  public String strSettleCurr = null;
  public double dBalance = 0;
  public double dFloating = 0;
  public double dInterest = 0;
  public double dPartialEquity = 0;
  public double dExposure = 0;
  public double dInitialMargin = 0;
  public double dInitialMarginFromServer = 0;
  public double dIMMContractValue = 0;
  public double dLeverage = 1;
  public double dFreeMargin = 0;
  public double dMarginRatio = 0;
  public double dEquityRatio = 0;
  public double dEquity = 0; //For cut loss client
  public double dAmount = 0;
  public double dLot = 0;
  public double dBuyLot = 0;
  public double dSellLot = 0;
  public double dCommission = 0;
  public double dCreditLimit = 0;
  public boolean hedged = false;
  
  public int iNetType = -1;
  public int iUserType = 0;
  public String strMarginCurr;
  public double dNewPosMargin;
  public double dHedgePosMargin;
  public double dFixedAmountMarginAlert = -1;
  public double dFixedAmountMarginCall = -1;
  public double dFixedAmountMarginCut = -1;
  public int iAutoLock = 0;
  public int iAutoLockCount = 3;
  public boolean bWaitBalanceUpdate = false;
  public String strGroup = null;

  public double dPrimaryBalance = 0;
  public double dSecondaryBalance = 0;

  public double dLastBalance = 0;

  public int iAlertLevel = 0;
  
  public int positionCount;

  public double dFixedSettlementExRate = 1.0;
  //Added by Sun Kwan 20090424
  public double dHKGSettlementExRate = 1.0;

  public double dDayPL = 0;

  public String strSecurityRef = null;

  public String strSyncNumber = "";
  public String strPositionSyncNumber = "";

  public String strDepositMade = "0";
  
  private MarginStatusHandler marginStatusHandler = null;
  
  public boolean bMarginAlertRange = false;
  public boolean bMarginCallRange = false;
  public boolean bMarginCutRange = false;  
  
  public double dMinLotLimit = 0;
  public double dMinLotIncrementUnit = 0;
  public double creditRatio = 0;

  public int idelay = 0;
  
  public double dbalin = 0;
  public double dabalin = 0;
  public BigDecimal realizedPNL = BigDecimal.ZERO;
  public BigDecimal tradableMargin = BigDecimal.ZERO;
  public BigDecimal nonTradableMargin = BigDecimal.ZERO;
  public BigDecimal creditLimitNonTradable = BigDecimal.ZERO;
  public BigDecimal freeLot = BigDecimal.ZERO;
  public boolean allowDeposit;
  public boolean allowWithdrawal;
  private AccountCreditType creditType = AccountCreditType.MARGIN;
  private BigDecimal callMargin = BigDecimal.ZERO;
  
  public BalanceRecord ()
  {
    this(null);
  }

  public BalanceRecord (String strAccount)
  {
    this.strAccount = strAccount;
  }
  

  public BalanceRecord clone(){
	  BalanceRecord balanceRecord = new BalanceRecord();
	  balanceRecord.strAEHierarchy = strAEHierarchy;
	  balanceRecord.strAccExec = strAccExec;
	  balanceRecord.strAccount = strAccount;
	  balanceRecord.strSettleCurr = strSettleCurr;
	  balanceRecord.dBalance = dBalance;
	  balanceRecord.dFloating = dFloating;
	  balanceRecord.dInterest = dInterest;
	  balanceRecord.dPartialEquity = dPartialEquity;
	  balanceRecord.dExposure = dExposure;
	  balanceRecord.dInitialMargin = dInitialMargin;
	  balanceRecord.dInitialMarginFromServer = dInitialMarginFromServer;
      balanceRecord.dIMMContractValue = dIMMContractValue;
      balanceRecord.dLeverage = dLeverage;
	  balanceRecord.dFreeMargin = dFreeMargin;
	  balanceRecord.dMarginRatio = dMarginRatio;
	  balanceRecord.dEquityRatio = dEquityRatio;
	  balanceRecord.iNetType = iNetType;
	  balanceRecord.iUserType = iUserType;
	  balanceRecord.strMarginCurr = strMarginCurr;
	  balanceRecord.dNewPosMargin = dNewPosMargin;
	  balanceRecord.dHedgePosMargin = dHedgePosMargin;
	  balanceRecord.dFixedAmountMarginAlert = dFixedAmountMarginAlert;
	  balanceRecord.dFixedAmountMarginCall = dFixedAmountMarginCall;
	  balanceRecord.dFixedAmountMarginCut = dFixedAmountMarginCut;
	  balanceRecord.iAutoLock = iAutoLock;
	  balanceRecord.iAutoLockCount = iAutoLockCount;
	  balanceRecord.bWaitBalanceUpdate = bWaitBalanceUpdate;
	  balanceRecord.strGroup = strGroup;
	  balanceRecord.dPrimaryBalance = dPrimaryBalance;
	  balanceRecord.dSecondaryBalance = dSecondaryBalance;
	  balanceRecord.dLastBalance = dLastBalance;
	  balanceRecord.iAlertLevel = iAlertLevel;
	  balanceRecord.dFixedSettlementExRate = dFixedSettlementExRate;
	  balanceRecord.dDayPL = dDayPL;
	  balanceRecord.strSecurityRef = strSecurityRef;
	  balanceRecord.strSyncNumber = strSyncNumber;
	  balanceRecord.bMarginAlertRange = bMarginAlertRange;
	  balanceRecord.bMarginCallRange = bMarginCallRange;
	  balanceRecord.bMarginCutRange = bMarginCutRange;  
	  balanceRecord.dCreditLimit = dCreditLimit;
	  balanceRecord.hedged = hedged;
	  
	  balanceRecord.dMinLotLimit = dMinLotLimit;
	  balanceRecord.dMinLotIncrementUnit = dMinLotIncrementUnit;
	  
	  balanceRecord.creditRatio = creditRatio; 
	  balanceRecord.idelay = idelay;
	  balanceRecord.realizedPNL = realizedPNL;
	  balanceRecord.tradableMargin = tradableMargin;
	  balanceRecord.nonTradableMargin = nonTradableMargin;
	  balanceRecord.creditLimitNonTradable = creditLimitNonTradable;
	  balanceRecord.allowDeposit = allowDeposit;
	  balanceRecord.allowWithdrawal = allowWithdrawal;
	  balanceRecord.setCreditType(getCreditType());
	  balanceRecord.callMargin = callMargin;
	  balanceRecord.accountName = accountName;
	  return balanceRecord;
  }
/*
  public void updateFloatingAndMargin(ContractObj contract, boolean bUpdateTotalValue){
	  String strBuyKey = contract.strBuyCode;
	  String strSellKey = contract.strSellCode;
	  double dFloating = 0;
	  double dBuyAmount = 0;
	  double dBuyLot = 0;
	  double dSellLot = 0;
	  double dSellAmount = 0;
	  double dNewPosMargin = -1;
	  double dHedgePosMargin = -1;
	  double dInitRatio = -1;
	  double dAmount = 0;
	  double dCommission = 0;
	  CopyOnWriteArrayList<OpenPositionRecord> vTmpPositionList = chmPositionByContractGroup.get(strBuyKey);
	  TempResult result = new TempResult();
	  
	  if(vTmpPositionList != null && vTmpPositionList.size() > 0){
		  OpenPositionRecord[] buyPositions = vTmpPositionList.toArray(new OpenPositionRecord[vTmpPositionList.size()]);
		 
		  dNewPosMargin = buyPositions[0].dNewPosMargin;
		  dHedgePosMargin = buyPositions[0].dHedgePosMargin;
		  dInitRatio = buyPositions[0].dInitialRatio;
		  for(int i = 0; i < buyPositions.length; i++){
				dFloating += buyPositions[i].dFloating;
				dBuyAmount += buyPositions[i].dAmount;
				dBuyLot += (buyPositions[i].dAmount/buyPositions[i].dContractSize);
				dCommission += buyPositions[i].dCommission;
		  }
		  result.dPositionCount = buyPositions.length;
		  //chmPositionCount.put(contract.strContractCode+strBuyKey, buyPositions.length);
		  result.buyCount = buyPositions.length;
		  buyPositions = null;
	  }
	  vTmpPositionList = null;
	  //chmContractBuyLot.put(contract.strContractCode, new Double(dBuyLot));
	  result.dBuyLot = dBuyLot;
	  
	  vTmpPositionList = chmPositionByContractGroup.get(strSellKey);
	  
	  if(vTmpPositionList != null && vTmpPositionList.size() > 0){
		  OpenPositionRecord[] sellPositions = vTmpPositionList.toArray(new OpenPositionRecord[vTmpPositionList.size()]);	
		  
		  if(dNewPosMargin == -1){
			  dNewPosMargin = sellPositions[0].dNewPosMargin;
			  dHedgePosMargin = sellPositions[0].dHedgePosMargin;
			  dInitRatio = sellPositions[0].dInitialRatio;
		  }
		  for(int i = 0; i < sellPositions.length; i++){		  
			  dFloating += sellPositions[i].dFloating;
			  dSellAmount += sellPositions[i].dAmount;
			  dSellLot += (sellPositions[i].dAmount/sellPositions[i].dContractSize);
			  dCommission += sellPositions[i].dCommission;
		  }		  
		  result.dPositionCount = sellPositions.length;
		  //chmPositionCount.put(contract.strContractCode+strSellKey, sellPositions.length);
		  result.sellCount = sellPositions.length;
		  sellPositions = null;
	  }
	  
	  vTmpPositionList = null;
	  
	  double dInitMargin = 0;
	  double dCounterRatio = 0;
	  double[] dEquivBidAsk = null;	  
	  
	  dAmount = dBuyAmount + dSellAmount;
	  ////chmContractFloat.put(contract.strContractCode, new Double(dFloating));
	  ///chmContractAmount.put(contract.strContractCode, new Double(dAmount));
	  ///chmContractSellLot.put(contract.strContractCode, new Double(dSellLot));
	  //chmContractCommission.put(contract.strContractCode, new Double(dCommission));
	  result.dAmount = dAmount;
	  result.dFloating = dFloating;
	  result.dCommission = dCommission;
	  result.dSellLot = dSellLot;
/*
Remember rewrite
	  switch(iNetType){
	  	case 0: //Ratio no net
	  		dEquivBidAsk = contract.curr1.equivCurr.getBidAsk();
	  		
	  		if(contract.curr1.equivCurr.bDirectQuote){
	  			dCounterRatio = 1 / ((dEquivBidAsk[0] + dEquivBidAsk[1]) / 2);
	  		}else{
	  			dCounterRatio = ((dEquivBidAsk[0] + dEquivBidAsk[1]) / 2);
	  		}
	  		
	  		dInitMargin = Math.abs(dBuyAmount + dSellAmount) * dCounterRatio * (dInitRatio / 100);
	  		
	  		//chmContractMargin.put(contract.strContractCode, new Double(dInitMargin));
	  		result.dMargin = dInitMargin;
	  		break;
	  	case 1: //Ratio net
	  		dEquivBidAsk = contract.curr1.equivCurr.getBidAsk();
	  		
	  		if(contract.curr1.equivCurr.bDirectQuote){
	  			dCounterRatio = 1 / ((dEquivBidAsk[0] + dEquivBidAsk[1]) / 2);
	  		}else{
	  			dCounterRatio = ((dEquivBidAsk[0] + dEquivBidAsk[1]) / 2);
	  		}
	  		
	  		dInitMargin = Math.abs(dBuyAmount - dSellAmount) * dCounterRatio * (dInitRatio / 100);
	  		
	  		//chmContractMargin.put(contract.strContractCode, new Double(dInitMargin));
	  		result.dMargin = dInitMargin;
	  		break;
	  	case 2: //Fixed	  		
	  		double dNew = (Math.abs(dBuyAmount - dSellAmount) / contract.iContractSize) * dNewPosMargin;
	  		double dHedge = 0;
	  		
	  		if(dBuyAmount <= dSellAmount)
	  			dHedge = (dBuyAmount / contract.iContractSize) * dHedgePosMargin;
	  		else if(dSellAmount < dBuyAmount)
	  			dHedge = (dSellAmount / contract.iContractSize) * dHedgePosMargin;
	  		dInitMargin = dNew + dHedge;	  		
	  		//chmContractMargin.put(contract.strContractCode, new Double(dInitMargin));
	  		result.dMargin = dInitMargin;
	  		break;	  		
	  }	  
*-/	  
	  chmResult.put(contract.strContractCode,result);
	  if(bUpdateTotalValue)
		  updateTotalFloatingAndMargin();
  }
*/

    /**
     * @return Total value of the account. NOT Equity
     */
  public double getCashValue(){
      return dPartialEquity + dFloating + dInterest + tradableMargin.doubleValue() + nonTradableMargin.doubleValue() + creditLimitNonTradable.doubleValue();
  }
  
  public void destory(){
	  strAEHierarchy = null;
	  strAccExec = null;
	  strAccount = null;
	  strSettleCurr = null;
	  strMarginCurr = null;
	  strGroup = null;
	  strSecurityRef = null;
	  strSyncNumber = null;
	  strDepositMade = null;
  }
	
	public void updatePositionSyncNumber(String strSyncNumber){
		if(strPositionSyncNumber == null || strPositionSyncNumber.compareTo(strSyncNumber) < 0)
			strPositionSyncNumber = strSyncNumber;
	}
	
	public boolean isSyncSameAsPosition(){
		return strSyncNumber.equals(strPositionSyncNumber);
	}
	
	public void setMarginStatusHandler(MarginStatusHandler handler){
		marginStatusHandler = handler;
	}

    public AccountCreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(AccountCreditType creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getCallMargin() {
        return callMargin;
    }

    public void setCallMargin(BigDecimal callMargin) {
        this.callMargin = callMargin;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
};
