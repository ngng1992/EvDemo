package com.mfinance.everjoy.app.bo;

public class LiquidationRecord {
	public ContractObj contract;
	public String sExeDate;
	public String sExeTime;
	public double dAmount;
	public double dCommission;
	public double dPL;
	public double aPL;
	public double pldiff;
	public double dInterest;
	public double dFloatInterest;
	public String sBRef;
	public String sBDate;
	public String sBRate;
	
	public String sSRef;
	public String sSDate;
	public String sSRate;
	public String sRRate;
	
	public String sBorS;
	
	public LiquidationRecord(String sBRef, String sSRef){
		this.sBRef = sBRef;
		this.sSRef = sSRef;
	}
	
	public String getKey(){
		StringBuilder sb = new StringBuilder();
		sb.append(sBRef).append("|").append(sSRef);
		return sb.toString();
	}

	public double getAPL(){
		return (Double.parseDouble(sSRate) - Double.parseDouble(sBRate))*dAmount;
	}
	
	public void destory() {
		// 
		
	}
}
