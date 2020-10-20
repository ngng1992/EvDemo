package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
/**
 * An interface for handling message, which received from activity 
 *  
 *  
 *  @author        : justin.lai
 *  @version       : v1.00
 *  
 *  
 *  @ModificationHistory  
 *  Who			When			Version			What<br>
 *  -------------------------------------------------------------------------------<br> 
 *  justin.lai		yyyymmdd		v1.00			Creation<br>
 *  
 *  
**
 */
public interface MessageProcessor {
	/**
	 * Process message
	 * @param msg message
	 * @param service background service
	 * @return
	 */
	public boolean processMessage(Message msg, FxMobileTraderService service);
}
