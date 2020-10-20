package com.mfinance.everjoy.hungkee.xml;

import java.text.DecimalFormat;

import org.simpleframework.xml.Element;


public class Price {
	
	@Element
	public String curr;
	@Element
	public String curr1;
	@Element
	public String curr2;
	@Element
	private String bid;
	@Element
	private String ask;
	@Element
	private String open;
	@Element
	private String highbid;
	@Element(required=false)
	private String highask;
	@Element
	private String lowbid;
	@Element(required=false)
	private String lowask;
	@Element
	private String close;
	/*
	public Boolean bAskChanged = false;
	public Boolean bBidChanged = false;
	*/
	public int iBidUpDown = 0;
	public int iAskUpDown = 0;
	
	public Boolean bPriceUp = null;
	
	public double dBid = 0.0;
	public double dAsk = 0.0;
	
	
	@Element(name="curr_en", required=false)
	public String sEName=null;
	
	@Element(name="curr_big5", required=false)
	public String sTName=null;
	
	@Element(name="curr_gb", required=false)
	public String sSName=null;
	
	/*
	public String sEName = null;
	public String sTName = null;
	public String sSName = null;
	*/
	
	@Element(name="dp")	
	public int iDP = -1;
	
	public int iBSD = -1;
	public int iBDQ = -1;
	public int iCDQ = -1;
	public double dCounterRate = 0.0;
	public int iContractSize = 0;
	
	public String getCurr() {
		return curr;
	}
	public String getCurr1() {
		return curr1;
	}
	public String getCurr2() {
		return curr2;
	}	
	public String getBid() {
		return Price.formatPrice(bid, iDP);
	}
	public String getAsk() {		
		return Price.formatPrice(ask, iDP);
	}
	public String getOpen() {
		return Price.formatPrice(open, iDP);
	}
	public String getHighbid() {
		return Price.formatPrice(highbid, iDP);
	}
	public String getHighask() {
		return Price.formatPrice(highask, iDP);
	}
	public String getLowbid() {
		return Price.formatPrice(lowbid, iDP);
	}
	public String getLowask() {
		return Price.formatPrice(lowask, iDP);
	}
	public String getClose() {
		return Price.formatPrice(close, iDP);
	}
	/*
	public Boolean getbAskChanged() {
		return bAskChanged;
	}
	public void setbAskChanged(Boolean bAskChanged) {
		this.bAskChanged = bAskChanged;
	}
	public Boolean getbBidChanged() {
		return bBidChanged;
	}	
	public void setbBidChanged(Boolean bBidChanged) {
		this.bBidChanged = bBidChanged;
	}
	*/
	public void setiBidUpDown(int iBidUpDown) {
		this.iBidUpDown = iBidUpDown;
	}
	public int getiBidUpDown() {
		return iBidUpDown;
	}
	public void setiAskUpDown(int iAskUpDown) {
		this.iAskUpDown = iAskUpDown;
	}
	public int getiAskUpDown() {
		return iAskUpDown;
	}
	public void setbPriceUp(Boolean bPriceUp) {
		this.bPriceUp = bPriceUp;
	}
	public Boolean getbPriceUp() {
		return bPriceUp;
	}
	
	
	public String getEName() {
		return sEName;
	}
	public void setEName(String sEName) {
		this.sEName = sEName;
	}
	public String getTName() {
		return sTName;
	}
	public void setTName(String sTName) {
		this.sTName = sTName;
	}
	public String getSName() {
		return sSName;
	}
	public void setSName(String sSName) {
		this.sSName = sSName;
	}
	public int getDP() {
		return iDP;
	}
	public void setDP(int iDP) {
		this.iDP = iDP;
	}
	public int getBSD() {
		return iBSD;
	}
	public void setBSD(int iBSD) {
		this.iBSD = iBSD;
	}
	public int getBDQ() {
		return iBDQ;
	}
	public void setBDQ(int iBDQ) {
		this.iBDQ = iBDQ;
	}
	public int getCDQ() {
		return iCDQ;
	}
	public void setCDQ(int iCDQ) {
		this.iCDQ = iCDQ;
	}
	public double getCounterRate() {
		return dCounterRate;
	}
	public void setCounterRate(double dCounterRate) {
		this.dCounterRate = dCounterRate;
	}
	@Override
	public String toString() {
		return "Price [ask=" + ask + ", bPriceUp=" + bPriceUp + ", bid=" + bid
				+ ", close=" + close + ", curr=" + curr + ", curr1=" + curr1
				+ ", curr2=" + curr2 + ", dCounterRate=" + dCounterRate
				+ ", highask=" + highask + ", highbid=" + highbid
				+ ", iAskUpDown=" + iAskUpDown + ", iBDQ=" + iBDQ + ", iBSD="
				+ iBSD + ", iBidUpDown=" + iBidUpDown + ", iCDQ=" + iCDQ
				+ ", iContractSize=" + iContractSize + ", iDP=" + iDP
				+ ", lowask=" + lowask + ", lowbid=" + lowbid + ", open="
				+ open + ", sEName=" + sEName + ", sSName=" + sSName
				+ ", sTName=" + sTName + "]";
	}
	
	public void updatePrice(Price price){				
		if(dBid > price.dBid){
			dBid = price.dBid;
			iBidUpDown = -1;
		}else if(dBid < price.dBid){
			dBid = price.dBid;	
			iBidUpDown = 1;
		}else{
			iBidUpDown = 0;
		}
		
		if(dAsk > price.dAsk){
			dAsk = price.dAsk;
			iAskUpDown = -1;
		}else if(dAsk < price.dAsk){
			dAsk = price.dAsk;	
			iAskUpDown = 1;
		}else{
			iAskUpDown = 0;
		}
		
		bid = price.bid;
		ask = price.ask;
		highbid = price.highbid;
		highask = price.highask;
		lowbid = price.lowbid;
		lowask = price.lowask;
		open = price.open;
		close = price.close;
	}
	
	public String getContractName(Boolean isSimple, Boolean isTradition ){
		if( isSimple ){
			return sSName;
		}else if(isTradition){
			return sTName;
		}else{
			return sEName;
		}	
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public void setAsk(String ask) {
		this.ask = ask;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public void setHighbid(String highbid) {
		this.highbid = highbid;
	}
	public void setHighask(String highask) {
		this.highask = highask;
	}
	public void setLowbid(String lowbid) {
		this.lowbid = lowbid;
	}
	public void setLowask(String lowask) {
		this.lowask = lowask;
	}
	public void setClose(String close) {
		this.close = close;
	}
	

    public static String formatPrice(String strPrice, int dp)
    {
    	if (strPrice==null)
    		return "";
    	else {
	        double dPrice = 0;
	        DecimalFormat df = new DecimalFormat();
	        String strFillZero = "0";
	        if (dp > 0)
	        {
	            strFillZero += ".";
	            for (int j=0; j<dp; j++)
	                strFillZero += "0";
	        }
	        df.applyPattern(strFillZero);
	        try{
	            strPrice = strPrice.replaceAll(",", "");
	            dPrice = Double.parseDouble(strPrice);
	        }catch(NumberFormatException nfe){
	            return "";
	        }
	        //return df.format(dPrice / Math.pow(10, dp));
	
			return df.format(dPrice);
    	} 
    }
	
}

