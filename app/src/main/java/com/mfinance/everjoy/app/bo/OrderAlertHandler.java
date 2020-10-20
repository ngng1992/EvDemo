package com.mfinance.everjoy.app.bo;


public interface OrderAlertHandler {
	public void orderAlert(OrderRecord order);
	public void updateTargetPrice(OrderRecord order);
}
