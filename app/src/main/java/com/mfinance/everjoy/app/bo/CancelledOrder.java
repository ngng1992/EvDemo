package com.mfinance.everjoy.app.bo;

public class CancelledOrder extends OrderRecord {
	public String sCreateDate = "";
	public String sCancelledDate = "";
	public String sCancelledTime = "";
	
	public CancelledOrder(int iRef) {
		super(iRef);
	}	
}
