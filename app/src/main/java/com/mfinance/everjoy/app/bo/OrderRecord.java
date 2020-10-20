package com.mfinance.everjoy.app.bo;

public class OrderRecord
{
  public int iRef = 0;
  public String viewRef = null;
  public int iStatus = 0; // 0 : Working;   1 : Executed;   2:Cancelled;
  public String sAccount = null;
  public String strContract = null;
  public String strTradeDate = null;  
  public String strBuySell = null;
  public double dAmount;
  public int iGoodTillType = 0; // 0 : Good Till Date;    1 : Good Till Cancel;    2 : Good Till Day End
  public String strGoodTillDate = null;
  public double dRequestRate = 0;
  public String strDealer = null;
  public int iLiquidationMethod = -1; // -1: no Liquidation;  1:LIFO;     2:FIFO;     3:Manual;       //In DB, NULL:no Liquidation  0:LIFO, 1:FIFO, 2: Manual
  public String strLiqRef = null;
  public int iOCORef = -1;
  public double dLiqRate = 0;
  public int iLimitStop = 0; // 0: Limit;    1: Stop
  public int iTrailingStopSize = -1;
  public double dRefRate = 0.0;
  public ContractObj contract = null;
  public String sTargetPosition = null;
  public double dOCOLimitRate = 0;
  public double dOCOStopRate = 0;
  
  public OrderRecord ()
  {
    this(0);
  }

  public OrderRecord (int iRef)
  {
    this.iRef = iRef;
  }

  public String getViewRef(){
	  return viewRef==null?String.valueOf(iRef):viewRef;
  }
  public String getOCOViewRef(){
	  if(viewRef!=null){
		  return String.format("%sO%09d", viewRef.split("X")[0],iOCORef);
	  }else
		  return String.valueOf(iOCORef);
  }
  public String getLiqViewRef(){
	  if(strLiqRef==null||strLiqRef.equals(""))
		  return strLiqRef;
	  if(viewRef!=null){
		  return String.format("%sD%09d", viewRef.split("X")[0],Integer.parseInt(strLiqRef.split("\\|")[0]));
	  }else
		  return strLiqRef.split("\\|")[0];
  }
	public void destory() {
		// 
		
	}

};