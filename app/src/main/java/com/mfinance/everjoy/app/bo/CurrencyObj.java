package com.mfinance.everjoy.app.bo;

import java.text.DecimalFormat;
import java.util.Vector;  // modified on 2010/04/26
import java.util.concurrent.CopyOnWriteArrayList;

public class CurrencyObj{
	public String strCode;
	public String strEquivCurr;
	public CurrencyObj equivCurr = null;	
	public int iRateDecPt;
	public int iAmtDecPt;
	public boolean bDirectQuote = false;
	public String strLastTime = null;
	
	public double dBid = -1;
	public double dAsk = -1;
	public double dHigh = -1;
	public double dLow = -1;
	
	public int iBidUpDown = 0;
	public int iAskUpDown = 0;	
	public int iHighUpDown = 0;
	public int iLowUpDown = 0;
	
	public boolean bInitialFinish = false;
	
	private DecimalFormat df = null;
	
	public CopyOnWriteArrayList<CurrencyObj> alEquivCurrOwner = new CopyOnWriteArrayList<CurrencyObj>();
	
	
	public CopyOnWriteArrayList<ContractObj> alDirectRelatedContract = new CopyOnWriteArrayList<ContractObj>();
	public CopyOnWriteArrayList<ContractObj> alRelatedContract = new CopyOnWriteArrayList<ContractObj>();
	
	
	double[] dBidAskHighLow = new double[4];
	String[] strBidAskHighLow = new String[4];	
	double[] dBidAsk = new double[3];
	
	double[] dBidAsk2 = new double[3]; // modified on 2010/05/02
	Vector<Double> vBid = new Vector<Double>(1000);  // modified on 2010/04/26
	Vector<Double> vAsk = new Vector<Double>(1000);  // modified on 2010/04/26
	Vector<Integer> vRDP = new Vector<Integer>(1000);  // modified on 2010/04/26
	Vector<Double> vFBid = new Vector<Double>(1000);  // modified on 2010/05/02
	Vector<Double> vFAsk = new Vector<Double>(1000);  // modified on 2010/05/02
	Vector<Integer> vFRDP = new Vector<Integer>(1000);  // modified on 2010/05/02
	int[] iUpDown = new int[4];
	
	public CurrencyObj(){}
	
	public CurrencyObj(String strCode, int iRateDecPt, int iAmtDecPt, String strEquivCurr, boolean bDirectQuote){
		this.strCode = strCode;
		this.iRateDecPt = iRateDecPt;
		this.iAmtDecPt = iAmtDecPt;
		this.bDirectQuote = bDirectQuote;
		this.strEquivCurr = strEquivCurr;
	}
	
	public void addEquivOwner(CurrencyObj currencyObj){
		if(!alEquivCurrOwner.contains(currencyObj)){
			alEquivCurrOwner.add(currencyObj);	
		}
	}

	public void addDirectRelatedContract(ContractObj contract){
		if(!alDirectRelatedContract.contains(contract)){
			alDirectRelatedContract.add(contract);
		}
	}
	
	public CopyOnWriteArrayList<ContractObj> getDirectRelatedContract(){		
		return alDirectRelatedContract;
	}
	
	public void addRelatedContract(ContractObj contract){
		if(!alRelatedContract.contains(contract)){
			alRelatedContract.add(contract);
		}
	}
	
	public CopyOnWriteArrayList<ContractObj> getRelatedContract(){		
		return alRelatedContract;
	}	
	
	public CopyOnWriteArrayList<CurrencyObj> getRelatedCurrency(){
		return alEquivCurrOwner;
	}
	
	public void finishInitial(){
		bInitialFinish = true;
	}
	
	public boolean bChangeBidAsk = false;
	
	public boolean bChangeHighLow = false;
	
	public void setBidAsk(double dBid, double dAsk){ 
		boolean bNoChange = false;
		bNoChange = (this.dBid == dBid) && (this.dAsk == dAsk);
		
		iBidUpDown = 0;		
		if(this.dBid > dBid){
			iBidUpDown = 1;
		}else{
			iBidUpDown = 2;
		}
		
		iAskUpDown = 0;
		if(this.dAsk > dAsk){
			iAskUpDown = 1;
		}else{
			iAskUpDown = 2;
		}		
		
		if(!bNoChange){
			this.dBid = dBid;
			this.dAsk = dAsk;		

			// modified on 2010/05/02
			//public static final boolean IS_READ_PRICE_QUEUE = IS_CUT_LOSS_DEALER || IS_AUTO_DEALER; //modified on 2010/04/26
			/*
			if (FXConstants.IS_READ_PRICE_QUEUE) {
				vBid.add(this.dBid);
				vAsk.add(this.dAsk);
				vRDP.add(this.iRateDecPt);
			}
			*/
			// end
		}
		
		bChangeBidAsk = !bNoChange;
	}
	
	public boolean setHighLow(double dHigh, double dLow){
		boolean bNoChange = false;
		bNoChange = (this.dHigh == dHigh) && (this.dLow == dLow);
				
		iLowUpDown = 0;		
		if(this.dLow > dLow){
			iLowUpDown = 1;
		}else{
			iLowUpDown = 2;
		}
		
		iHighUpDown = 0;
		if(this.dHigh > dHigh){
			iHighUpDown = 1;
		}else{
			iHighUpDown = 2;
		}		
		
		if(!bNoChange){
			this.dHigh = dHigh;
			this.dLow = dLow;
		}
		else
		{
			iHighUpDown = 0;
			iLowUpDown = 0;
		}
		//System.out.println("no Change : "+this+","+bNoChange);
		bChangeHighLow = !bNoChange;
		return !bNoChange;		
	}
	
	public String[] getBidAskLowHigh(){	
		dBidAskHighLow[0] = dBid;
		dBidAskHighLow[1] = dAsk;
		dBidAskHighLow[2] = dHigh;
		dBidAskHighLow[3] = dLow;		
		
		strBidAskHighLow[0] = formatValue(dBidAskHighLow[0], iRateDecPt);
		strBidAskHighLow[1] = formatValue(dBidAskHighLow[1], iRateDecPt);
		strBidAskHighLow[2] = formatValue(dBidAskHighLow[2], iRateDecPt);
		strBidAskHighLow[3] = formatValue(dBidAskHighLow[3], iRateDecPt);
		
		return strBidAskHighLow;
	}

	// modified on 2010/04/26
	public double[] getBidAskQueue(){
		if (vBid.size() > 0) {
			double[] dBidAsk2 = new double[3];
			dBidAsk2[0] = vBid.remove(0).doubleValue();
			dBidAsk2[1] = vAsk.remove(0).doubleValue();
			dBidAsk2[2] = vRDP.remove(0).doubleValue();
			return dBidAsk2;
		} else {
			return getBidAsk();
		}
	}
	// end

	// modified on 2010/05/02
	public double[] getBidAskFloatQueue(){
		if (vFBid.size() > 0) {
			dBidAsk2[0] = vFBid.remove(0).doubleValue();
			dBidAsk2[1] = vFAsk.remove(0).doubleValue();
			dBidAsk2[2] = vFRDP.remove(0).doubleValue();
			return dBidAsk2;
		} else {
			return getBidAsk();
		}
	}

	public double[] getBidAsk2(){
		return dBidAsk2;
	}
	// end

	public double[] getBidAsk(){
		dBidAsk[0] = dBid;
		dBidAsk[1] = dAsk;
		dBidAsk[2] = iRateDecPt;
		return dBidAsk;
	}

	public int[] getUpDown(){
		iUpDown[0]= iBidUpDown;
		iUpDown[1]= iAskUpDown;
		iUpDown[2]= iHighUpDown;
		iUpDown[3]= iLowUpDown;
		return iUpDown;
	}
/*
	public void writeToMessage(MessageObj msgObj, int iIndex){
		msgObj.addField("mkt"+iIndex, strCode);
		msgObj.addField("emkt"+iIndex, equivCurr.strCode);
		msgObj.addField("dq"+iIndex, String.valueOf(bDirectQuote));
		msgObj.addField("rdp"+iIndex, String.valueOf(iRateDecPt));
		msgObj.addField("adp"+iIndex, String.valueOf(iAmtDecPt));
	}
*/	
	public boolean isSettingUpdated(String strEquivCurr, int iRateDecPt, int iAmtDecPt, boolean bDirectQuote){
		boolean isUpdated = false;		
		isUpdated = this.strEquivCurr.equals(strEquivCurr) && this.iRateDecPt == iRateDecPt && this.iAmtDecPt == iAmtDecPt && this.bDirectQuote == bDirectQuote;		
		return !isUpdated;
	}
	
	public String formatValue (double dValue, int dp)
	{	    
		if(df == null){
			String strFillZero = "0";
			if (dp > 0)
		    {
		      strFillZero += ".";
		      for (int j = 0; j < dp; j++)
		      {
		        strFillZero += "0";
		      }
		    }
			df = new DecimalFormat();
		    df.applyPattern(strFillZero);			
		}
	    return df.format(dValue);
	}
	
	public String toString(){
		return "[Currency]["+strCode+"][Bid]["+dBid+"][Ask]["+dAsk+"]";
	}
}
