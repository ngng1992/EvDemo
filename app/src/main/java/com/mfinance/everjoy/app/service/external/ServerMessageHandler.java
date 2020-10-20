package com.mfinance.everjoy.app.service.external;

import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

/**
 * A abstract class for handler message which received from server 
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
public abstract class ServerMessageHandler implements Runnable{
	/**
	 * Tag name for logging
	 */
	public String TAG = this.getClass().getSimpleName();
	/**
	 * Background service instance
	 */
	protected FxMobileTraderService service = null;
	/**
	 * Message object
	 */
	protected MessageObj msgObj = null;
	/**
	 * Constructor 
	 * @param service background service
	 */
	public ServerMessageHandler(FxMobileTraderService service){
		this.service = service;
	}
	/**
	 * Is the handler status less, if it is status less, other handler will running at the same without waiting 
	 * @return Yes = true, No = false
	 */
	public abstract boolean isStatusLess();
	/**
	 * Is balance recalculation require
	 * @return Yes = true, No = false
	 */
	public abstract boolean isBalanceRecalRequire();
	/**
	 * Method for handling message
	 * @param msgObj
	 */
	public abstract void handleMessage(MessageObj msgObj);
	/**
	 * Assign message to handler
	 * @param msgObj
	 */
	public void assignMessageObj(MessageObj msgObj){
		this.msgObj = msgObj;
	}
	
	@Override
	public void run(){
		try{
		if(isStatusLess()){
			synchronized(this){
				notify();
			}	
		}
		
		handleMessage(msgObj);
		
		if(msgObj != null){
			msgObj = null;
		}
		
		if(!isStatusLess()){
			synchronized(this){
				notify();
			}	
		}}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
