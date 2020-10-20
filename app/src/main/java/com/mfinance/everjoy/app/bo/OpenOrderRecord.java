package com.mfinance.everjoy.app.bo;

public class OpenOrderRecord
{
  public String strGroupCode;
  public String strAccountNumber;
  public String strItem;
  public String strBuySell;
  public String strExecuteDate;
  public String strOrderNumber;
  public String strDecplaces;
  public String strExecutePrice;
  public String strLotSize;
  public String strValueDate;
  public String strInitRatio;
  public String strCommission;
  public String strInterest;
  public String strFloating;
  public String strContractSize;
  public String dNewPosMargin;
  public String dHedgePosMargin;

  public OpenOrderRecord (String strGroupCode, String strAccountNumber, String strItem, String strBuySell, String strExecuteDate, String strOrderNumber, String strDecplaces,
      String strExecutePrice, String strLotSize, String strValueDate, String strInitRatio, String strCommission, String strInterest, String strFloating, String strContractSize)
  {
    this.strGroupCode = strGroupCode;
    this.strAccountNumber = strAccountNumber;
    this.strItem = strItem;
    this.strBuySell = strBuySell;
    this.strExecuteDate = strExecuteDate;
    this.strOrderNumber = strOrderNumber;
    this.strDecplaces = strDecplaces;
    this.strExecutePrice = strExecutePrice;
    this.strLotSize = strLotSize;
    this.strValueDate = strValueDate;
    this.strInitRatio = strInitRatio;
    this.strCommission = strCommission;
    this.strInterest = strInterest;
    this.strFloating = strFloating;
    this.strContractSize = strContractSize;
  }
  
    public OpenOrderRecord (String strGroupCode, String strAccountNumber, String strItem, String strBuySell, String strExecuteDate, String strOrderNumber, String strDecplaces, String strExecutePrice, String strLotSize, String strValueDate, String strInitRatio, String strCommission, String strInterest, String strFloating, String strContractSize, String strNewPosMargin, String strHedgePosMargin)
	{
        this.strGroupCode = strGroupCode;
        this.strAccountNumber = strAccountNumber;
        this.strItem = strItem;
        this.strBuySell = strBuySell;
        this.strExecuteDate = strExecuteDate;
        this.strOrderNumber = strOrderNumber;
        this.strDecplaces = strDecplaces;
        this.strExecutePrice = strExecutePrice;
        this.strLotSize = strLotSize;
        this.strValueDate = strValueDate;
        this.strInitRatio = strInitRatio;
        this.strCommission = strCommission;
        this.strInterest = strInterest;
        this.strFloating = strFloating;
        this.strContractSize = strContractSize;
        this.dNewPosMargin = strNewPosMargin;
        this.dHedgePosMargin = strHedgePosMargin;
	}
}
