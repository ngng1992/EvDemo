package com.mfinance.everjoy.app.bo;

public interface MarginStatusHandler {
	public void Alert(BalanceRecord balanceRecord);
	public void Call(BalanceRecord balanceRecord);
	public void Cut(BalanceRecord balanceRecord);
}
