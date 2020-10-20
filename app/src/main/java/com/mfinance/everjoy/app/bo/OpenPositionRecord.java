package com.mfinance.everjoy.app.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.util.Utility;

public class OpenPositionRecord
{
	public ContractObj contract = null;
	public BalanceRecord accountBalance;
	public int iRef = 0;
	public String viewRef = null;
	public Integer ObjRef = null;
	public String strAccount = null;
	public String strContract = null;
	public String strTradeDate = null;
	public String strValueDate = null;
	public String strBuySell = null;
	public double dAmount = 0;
	public double dDealRate = 0;
	public double dRunningRate = -1;
	public String strDealRate = "";
	public double dealRate = 0;
	public String strDealer = null;
	public double dFloating = -1;
	public double dInterest = 0;
	public double dFloatInterest = 0;
	public double dCommission = 0;
	public double dInitialRatio = 0;
	public double dRevalueRate = 0;
	public int iDp = 0;
	//public double dContractSize = 0;
	public double dSettleToSystemRate = 1.0; // For Open Position Summary
	public String strGroup = null;
	public String strSettleCurr = null;
	public String strCounterConvRate = "1.0000";
	public double dNewPosMargin = 0;
	public double dHedgePosMargin = 0;
	public String strSynNumber = "";
	public boolean isBuyOrder = false; 
	
	public double dLatestMargin = 0;	// updated by YAU 2011 Apr

  public OpenPositionRecord ()
  {
    this(0);
  }

  public OpenPositionRecord (int iRef)
  {
	this.iRef = iRef;
    ObjRef = new Integer(iRef); 
  }

  // modified on 2010/05/02
  public void setContract(ContractObj contract) {
	  this.contract = contract;
  }
  // end
  public String getViewRef(){
	  return viewRef==null?String.valueOf(iRef):viewRef;
  }
  
/*
  public Object clone ()
  {
    OpenPositionRecord cloneObj = new OpenPositionRecord(this.iRef);
    cloneObj.iRef = this.iRef;
    cloneObj.strAccount = this.strAccount;
    cloneObj.strContract = this.strContract;
    //cloneObj.strTradeDate = this.strTradeDate;
    cloneObj.strValueDate = this.strValueDate;
    cloneObj.strBuySell = this.strBuySell;
    cloneObj.dAmount = this.dAmount;
    cloneObj.dDealRate = this.dDealRate;
    cloneObj.strDealer = this.strDealer;
    cloneObj.dFloating = this.dFloating;
    cloneObj.dInterest = this.dInterest;
    cloneObj.dCommission = this.dCommission;
    cloneObj.dInitialRatio = this.dInitialRatio;
    cloneObj.dRevalueRate = this.dRevalueRate;
    cloneObj.iDp = this.iDp;
    //cloneObj.dContractSize = this.dContractSize;
    cloneObj.dSettleToSystemRate = this.dSettleToSystemRate;
    cloneObj.strGroup = this.strGroup;
    cloneObj.strSettleCurr = this.strSettleCurr;
    cloneObj.strCounterConvRate = this.strCounterConvRate;
	cloneObj.dNewPosMargin = this.dNewPosMargin;
	cloneObj.dHedgePosMargin = this.dHedgePosMargin;
	cloneObj.strSynNumber = this.strSynNumber;
	cloneObj.isBuyOrder = this.isBuyOrder;
    return cloneObj;
  }
*/ 
  public void destory(){
	//strAccount = null;
	//strContract = null;
	//strTradeDate = null;
	//strValueDate = null;
	//strBuySell = null;
	//strGroup = null;
	//strSettleCurr = null;
	//strCounterConvRate = null;
	//strDealer = null;
	//strSynNumber = null;
  }

  // New platform should not use this method to calculate PnL!!!
  public double reCalculateFloatingOld(){	  
	  double dPLRate = -1;
	  double dFloating = -1;
	  
	  boolean bIsBuy = this.isBuyOrder;
	  double dCounterRate = contract.dCounterRate;
	  double dBase[] = contract.getBidAsk();
	  
	  if(bIsBuy){
		  dRevalueRate = dBase[0];
		  dPLRate = dRevalueRate - (dRunningRate==-1?dDealRate:dRunningRate);
	  }else{
		  dRevalueRate = dBase[1];
		  dPLRate = (dRunningRate==-1?dDealRate:dRunningRate) - dRevalueRate;
	  }

	  if(contract.bCounterDirectQuto){
		  dFloating = dPLRate * dAmount * 1 / dCounterRate;
	  }else{
		  dFloating = dPLRate * dAmount * dCounterRate;
	  }
	  
	  double dFixedSettlementExRate = accountBalance.dFixedSettlementExRate;
	  if (contract.strContractCode.equals("RKG") && contract.sCurr2.equals("RMB") && accountBalance.strSettleCurr.equals("HKD")) {
		  dFixedSettlementExRate = 1.0;
	  }

	  this.dFloating = dFloating * dFixedSettlementExRate / MobileTraderApplication.CONV_RATE;
	  return this.dFloating;
  }
  
  public double reCalculateFloating(){	  
	  double dPLRate = -1;
	  double dFloating = -1;
	  
	  boolean bIsBuy = this.isBuyOrder;
	  double dBase[] = contract.getBidAsk();
	  
	  double dCounterRate = contract.dCounterRate;
	  boolean bCounterDirectQuto = false;

	  double dFixedSettlementExRate = accountBalance.dFixedSettlementExRate;
	  String strFloatingCurr;
	  
	  if (dFixedSettlementExRate != 1.0)
          strFloatingCurr = "USD";
      else
          strFloatingCurr = strSettleCurr;
	  
	  // Cannot use Crate from FxServer since it does not consider user bid/ask adjust setting
	  if( strFloatingCurr.equals(contract.sCurr2) != true )
	  {
		  double dOutput[] = new double[4];
		  getExchangeRate( contract.sCurr2, strFloatingCurr, dOutput );
		  if(Math.abs(dOutput[0]) < 0.01)
			  bCounterDirectQuto = false;
		  else
			  bCounterDirectQuto = true;
		  dCounterRate = dOutput[1];
	  }
	  
	  if( contract.getBSD() == true )
	  {
		  if(bIsBuy)
		  {
			  dRevalueRate = dBase[1];
			  dPLRate = 1 / dRevalueRate - 1 / dDealRate;
		  }
		  else
		  {
			  dRevalueRate = dBase[0];
			  dPLRate = 1 / dDealRate - 1 / dRevalueRate;
		  }
	  }
	  else
	  {
		  if(bIsBuy){
			  dRevalueRate = dBase[0];
			  dPLRate = dRevalueRate - (dRunningRate==-1?dDealRate:dRunningRate);
		  }else{
			  dRevalueRate = dBase[1];
			  dPLRate = (dRunningRate==-1?dDealRate:dRunningRate) - dRevalueRate;
		  }
	  }

	  if(bCounterDirectQuto == true){
		  dFloating = dPLRate * dAmount * 1 / dCounterRate;
	  }else{
		  dFloating = dPLRate * dAmount * dCounterRate;
	  }
	  
	  /*if (contract.strContractCode.equals("RKG") && contract.sCurr2.equals("RMB") && accountBalance.strSettleCurr.equals("HKD")) {
		  dFixedSettlementExRate = 1.0;
	  }*/
	  if( MobileTraderApplication.CONV_RATE_OP.equals("D") )
		  this.dFloating = Utility.roundToDouble(dFloating * dFixedSettlementExRate / MobileTraderApplication.CONV_RATE, 2, 2);
	  else
		  this.dFloating = Utility.roundToDouble(dFloating * dFixedSettlementExRate * MobileTraderApplication.CONV_RATE, 2, 2);
	  return this.dFloating;
  }
  
  public double reCalculateFloatingWithBSD(){	  
	  double dFloating = -1;
	  
	  boolean bIsBuy = this.isBuyOrder;
	  double dCounterRate = contract.dCounterRate;
	  double dBase[] = contract.getBidAsk();
      if ((bIsBuy && contract.bBSD || (!bIsBuy && !contract.bBSD)))
          dRevalueRate = dBase[1];
      else
          dRevalueRate = dBase[0];
      
      dFloating =(bIsBuy?1:-1) 
              * (!contract.bBSD? dAmount * (dRevalueRate-dDealRate) : dAmount/dRevalueRate - dAmount/dDealRate)
              * (!contract.bCounterDirectQuto? (dCounterRate) : (1/dCounterRate))
              * accountBalance.dFixedSettlementExRate;

	  this.dFloating = dFloating / MobileTraderApplication.CONV_RATE;
	  return this.dFloating;
  }
  
  public static void getExchangeRate(String strCurr1, String strCurr2, double dOutput[])
  {
	  dOutput[0] = 0; dOutput[1] = 1.0; dOutput[2] = 1.0; dOutput[3] = 1.0;
	  HashMap<String, ContractObj> hmContracts = DataRepository.getInstance().getContracts();
	  Iterator<String> contractIter = hmContracts.keySet().iterator();
	  while( contractIter.hasNext() )
	  {
		  String strExchangeContract = (String) contractIter.next();
          ContractObj curContract = (ContractObj) hmContracts.get(strExchangeContract);
          if (strCurr1.equals(curContract.sCurr1) && strCurr2.equals(curContract.sCurr2))
          {
        	  if( curContract.getBSD() == false )
        		  dOutput[0] = 0;
        	  else
        		  dOutput[0] = 1;
        	  dOutput[1] = Utility.roundToDouble((curContract.dBidAsk[0] + curContract.dBidAsk[1]) / 2, curContract.iRateDecPt, curContract.iRateDecPt);
        	  dOutput[2] = curContract.dBidAsk[0];
        	  dOutput[3] = curContract.dBidAsk[1];
              return;
          }
          else if (strCurr2.equals(curContract.sCurr1) && strCurr1.equals(curContract.sCurr2))
          {
        	  if( curContract.getBSD() == false )
        		  dOutput[0] = 1;
        	  else
        		  dOutput[0] = 0;
        	  dOutput[1] = Utility.roundToDouble((curContract.dBidAsk[0] + curContract.dBidAsk[1]) / 2, curContract.iRateDecPt, curContract.iRateDecPt);
        	  dOutput[2] = curContract.dBidAsk[0];
        	  dOutput[3] = curContract.dBidAsk[1];
        	  return;
          }
	  }
  }
}