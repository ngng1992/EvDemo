package com.mfinance.everjoy.app.service;

/**
 * An interface for monitor received message
 *  
 *  
 *  @author        : justin.lai
 *  @version       : v1.00
 *  
 *  
 *  @ModificationHistory  
 *  Who			When			Version			What<br>
 *  -------------------------------------------------------------------------------<br> 
 *  justin.lai	20110413		v1.00			Creation<br>
 *  
 *  
**
 */
public interface MessageListener {
	/**
	 * Notify message recevied
	 */
	public void notifyMessageReceived();
}
