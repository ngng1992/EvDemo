package com.mfinance.everjoy.hungkee.xml.dao;

import com.mfinance.everjoy.app.MobileTraderApplication;

public abstract class AbstractDao<K> {
	
	public static int CONNECTION_TIMEOUT = 5000;
	
	/**
	 * Application instance
	 */
	protected MobileTraderApplication app;	
	
	public AbstractDao(MobileTraderApplication app){
		this.app = app;
	}
	
	public abstract K getValueFromXML();
	
	public abstract void updateXML();
	
}
