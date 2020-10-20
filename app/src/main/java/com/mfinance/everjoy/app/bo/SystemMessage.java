package com.mfinance.everjoy.app.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class SystemMessage {
	public static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	
	public int iID = -1;
	public Date dateCreate = null;
	public int iCode = -1;
	public String sMsg = null;
	
	public SystemMessage(int iCode) {
		this.iCode = iCode;
		dateCreate = Calendar.getInstance().getTime();
		iID = SystemMessage.getID();
	}	
	public SystemMessage(String sMsg) {
		this.sMsg = sMsg;
		dateCreate = Calendar.getInstance().getTime();
		iID = SystemMessage.getID();
	}
	
	public SystemMessage(int iCode, String sMsg) {
		this.iCode = iCode;
		this.sMsg = sMsg;
		dateCreate = Calendar.getInstance().getTime();
		iID = SystemMessage.getID();
	}
	
	public static int getID(){
		return ID_COUNTER.addAndGet(1);
	}
	public void destory() {
		// 
		
	}
}
