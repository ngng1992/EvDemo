package com.mfinance.everjoy.app.bo;

public class ExecutedOrder extends OrderRecord {
	public String sCreateDate = "";
	public String sExecutedDate = "";
	public String sExecutedTime = "";
	public double dExecutedAmount= 0.0;
	
	public ExecutedOrder(int iRef) {
		super(iRef);
	}	
}
