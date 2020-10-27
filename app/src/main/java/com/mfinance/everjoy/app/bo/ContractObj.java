package com.mfinance.everjoy.app.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Map;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.util.InterestRateMethod;
import com.mfinance.everjoy.app.util.Utility;

public class ContractObj{
	public String strContractCode;
	public String strContractName;
	public String sENName;
	public String sSCName;
	public String sTCName;
	public String sCurr1;
	public String sCurr2;
	public int iContractSize;
	public boolean bBaseCurr;
	private boolean bViewable;
	private boolean bTradable;
	private boolean bRptViewable;
	private boolean bRptTradable;
	public boolean bCounterDirectQuto;
	public boolean bBSD;
	public String strBuyCode;
	public String strSellCode;
	public double dBidInterest;
	public double dAskInterest;
	public int iOrderPips;
	public int iTrailingStopMax;
	public int iTrailingStopMin;
	public double dCounterRate;
	private double maxLotLimitUser;

	public int iRateDecPt;
	public String strLastTime = null;
	
	public double dBid = -1;
	public double dAsk = -1;
	public double dHigh = -1;
	public double dLow = -1;
	public double dHighBid = -1;
	public double dLowBid = -1;
	public double dHighAsk = -1;
	public double dLowAsk = -1;	
	
	public int iBidUpDown = 0;
	public int iAskUpDown = 0;	
	public int iHighUpDown = 0;
	public int iLowUpDown = 0;
	
	public boolean bChangeBidAsk = false;	
	public boolean bChangeHighLow = false;
	
	public double[] dBidAsk = new double[2];
	
	public double dBidAdjust = 0;
	public double dAskAdjust = 0;

	public double dBidAdjustRpt = 0;
	public double dAskAdjustRpt = 0;
	
	public int iCurr1Image = -1;
	public int iCurr2Image = -1;
	
	public double dSpread;
	public double dOffset;
	
	public double dMinLot = 0;
	public double dIncLot = 0;

	public double[] bidLotDepth = new double[CompanySettings.MARKET_DEPTH_LEVEL];
	public double[] askLotDepth = new double[CompanySettings.MARKET_DEPTH_LEVEL];

	public double[] bidAmountDepth = new double[CompanySettings.MARKET_DEPTH_LEVEL];
	public double[] askAmountDepth = new double[CompanySettings.MARKET_DEPTH_LEVEL];

	public String[] bidPriceDepth = new String[CompanySettings.MARKET_DEPTH_LEVEL];
	public String[] askPriceDepth = new String[CompanySettings.MARKET_DEPTH_LEVEL];

	public double[] dBidDepth = new double[CompanySettings.MARKET_DEPTH_LEVEL];
	public double[] dAskDepth = new double[CompanySettings.MARKET_DEPTH_LEVEL];

	HashMap<Integer, OpenPositionRecord> chmBuyPosition = new HashMap<Integer, OpenPositionRecord>(100);
	HashMap<Integer, OpenPositionRecord> chmSellPosition = new HashMap<Integer, OpenPositionRecord>(100);
	
	ArrayList<OpenPositionRecord> alBuyRef = new ArrayList<OpenPositionRecord>();
	ArrayList<OpenPositionRecord> alSellRef = new ArrayList<OpenPositionRecord>();
	
	HashMap<Integer, OrderRecord> chmRunningOrder = new HashMap<Integer, OrderRecord>(100);	
	ArrayList<OrderRecord> alRunningOrder = new ArrayList<OrderRecord>(100);
	OrderAlertHandler orderAlert = null;
	
	public String providerQuote = null;
	public HashMap<String, Double[]> providerQuoteMap = new HashMap<String, Double[]>();
	
	public String tag;
	public String enableDepth = "0";
	private double maxLotLimit;
	private InterestRateMethod interestRateMethod = InterestRateMethod.FIXED;
	
	public ContractObj(){
		this.maxLotLimit = 1000000000;
		this.maxLotLimitUser = 1000000000;
	}
	
	public ContractObj(String strContractCode, String strContractName, String sCurr1, String sCurr2,
			int iContractSize, boolean bBaseCurr, boolean bViewable,
			boolean bTradable, int iOrderPips, int iTrailingStopMax,
			int iTrailingStopMin, double dBidInterest, double dAskInterest,
			 int iRateDecPt,  
			 double dBid, double dAsk, 
			 double dHigh, double dLow, double dCounterRate, 
			 boolean bCounterDirectQuto,
			 String sENName, String sTCName, String sSCName, boolean bBSD, double dHighBid, double dHighAsk, double dLowBid, double dLowAsk) {
		
		this.strContractCode = strContractCode;
		this.strContractName = strContractName;
		this.sCurr1 = sCurr1;
		this.sCurr2 = sCurr2;
		this.bBaseCurr = bBaseCurr;
		this.bViewable = bViewable;
		this.bTradable = bTradable;
		this.iContractSize = iContractSize;
		this.strBuyCode = strContractCode + "|B";
		this.strSellCode = strContractCode + "|S";
		this.iOrderPips = iOrderPips;
		this.iTrailingStopMax = iTrailingStopMax;
		this.iTrailingStopMin = iTrailingStopMin;
		this.dBidInterest = dBidInterest;
		this.dAskInterest = dAskInterest;
		this.dCounterRate = dCounterRate;	
		this.iRateDecPt = iRateDecPt;
		this.sENName = sENName;
		this.sSCName = sSCName;
		this.sTCName = sTCName;
		this.bCounterDirectQuto = bCounterDirectQuto;
		this.bBSD = bBSD;
		this.setBidAsk(dBid, dAsk);
		if(dHigh!=-1)
			this.setHighLow(dHigh, dLow);
		if(dHighBid!=-1)
			this.setHighLow(dHighBid, dLowBid, dHighAsk, dLowAsk);
		this.maxLotLimit = 1000000000;
		this.maxLotLimitUser = 1000000000;
	}
	

	public void setOrderAlertHandler(OrderAlertHandler orderAlert) {
		this.orderAlert = orderAlert;
	}
/*
	public int getNumberOfPosition() {
		return chmBuyPosition.size() + chmSellPosition.size();
	}

	public OpenPositionRecord[] getBuyPositions() {
		return chmBuyPosition.values().toArray(
				new OpenPositionRecord[chmBuyPosition.size()]);
	}

	public OpenPositionRecord[] getSellPositions() {
		return chmSellPosition.values().toArray(
				new OpenPositionRecord[chmSellPosition.size()]);
	}
*/
	public OrderRecord[] getWorkingOrder() {
		return alRunningOrder.toArray(new OrderRecord[alRunningOrder.size()]);
	}
	
	public HashMap<Integer, OrderRecord> getWorkingOrderInMap() {
		return chmRunningOrder;
	}	

/*
	public void setCurr1(CurrencyObj currencyObj) {
		this.curr1 = currencyObj;
		this.curr1.addDirectRelatedContract(this);
	}

	public void setCurr2(CurrencyObj currencyObj) {
		this.curr2 = currencyObj;
		this.curr2.addDirectRelatedContract(this);
	}
*/
	public boolean getBSD() {
		return bBSD;
	}
	
	public boolean isEmptyPosition(){
		return chmBuyPosition.isEmpty() && chmSellPosition.isEmpty();
	}
	
	/*
	 * public void addContractListener(ContractListener listener){
	 * vUpdateListener.add(listener); listeners = vUpdateListener.toArray(new
	 * ContractListener[vUpdateListener.size()]); }
	 * 
	 * ContractListener[] listeners;
	 * 
	 * public void notifyContractBidAskUpdate(){ if(listeners != null){ for(int
	 * i = 0; i < listeners.length; i++){
	 * listeners[i].contractBidAskUpdated(this); } } }
	 */

	public void addBuyContract(OpenPositionRecord buyPosition) {
		chmBuyPosition.put(buyPosition.iRef, buyPosition);
		alBuyRef.add(buyPosition);
		buyPosition.setContract(this);
	}

	public void addSellContract(OpenPositionRecord sellPosition) {
		chmSellPosition.put(sellPosition.iRef, sellPosition);
		alSellRef.add(sellPosition);
		sellPosition.setContract(this);
	}

	public OpenPositionRecord removeBuyPosition(int iRef) {
		OpenPositionRecord op = chmBuyPosition.remove(new Integer(iRef));
		alBuyRef.remove(op);
		return op;
	}

	public OpenPositionRecord removeSellPosition(int iRef) {
		OpenPositionRecord op = chmSellPosition.remove(new Integer(iRef));
		alSellRef.remove(op);
		return op;
	}

	public void addRunningOrder(OrderRecord order) {
		if (!chmRunningOrder.containsKey(order.iRef)) {
			order.contract = this;
			chmRunningOrder.put(order.iRef, order);
			alRunningOrder.add(order);
		}
	}

	public OrderRecord removeRunningOrder(Integer oRef) {
		OrderRecord removeItem = chmRunningOrder.remove(oRef);
		alRunningOrder.remove(removeItem);
		return removeItem;
	}
	
	public double[] getBidAsk() {
		return dBidAsk.clone();
	}
	
	public Iterator<OpenPositionRecord> toBuyIterator(){
		return chmBuyPosition.values().iterator();
	}
	
	public Iterator<OpenPositionRecord> toSellIterator(){
		return chmSellPosition.values().iterator();
	}	
	
/*
	public String[] getBidAskLowHigh() {
		return this.curr1.getBidAskLowHigh();
	}

	public double[] getBaseBidAsk() {
		return this.curr1.getBidAsk();
	}

	// modified on 2010/05/02
	public double[] getBaseBidAsk2() {
		return this.curr1.getBidAsk2();
	}

	public double[] getBaseBidAskQueue(int type) {
		if (type == 0) { //type = 0 : Alert order queue
			return this.curr1.getBidAskQueue();
		} else { // Cut loss price queue
			return this.curr1.getBidAskFloatQueue();
		}
	}
	// end

	// modified on 2010/04/26
	public double[] getBaseBidAskQueue() {
		return getBaseBidAskQueue(0);
	}
	// end

	public double[] getCounterRate() {
		return this.curr2.getBidAsk();
	}
/*
	public void writeToMessage(MessageObj msgObj, int count) {
		msgObj.addField("ct" + count, strContractCode);
		msgObj.addField("des" + count, strContractName);
		msgObj.addField("curr1" + count, curr1.strCode);
		msgObj.addField("curr2" + count, curr2.strCode);
		msgObj.addField("ics" + count, String.valueOf(iContractSize));
		msgObj.addField("bc" + count, String.valueOf(bBaseCurr));
		msgObj.addField("view" + count, String.valueOf(bViewable));
		msgObj.addField("trade" + count, String.valueOf(bTradable));
		msgObj.addField("ordp" + count, String.valueOf(iOrderPips));
		msgObj.addField("tsmx" + count, String.valueOf(iTrailingStopMax));
		msgObj.addField("tsmn" + count, String.valueOf(iTrailingStopMin));
		msgObj.addField("bi" + count, String.valueOf(dBidInterest));
		msgObj.addField("ai" + count, String.valueOf(dAskInterest));
		msgObj.addField("st" + count, strStopTrade);
		msgObj.addField("sat" + count, strStopAutoDeal);
	}
*/	

	public void destory() {
		//strContractCode = null;
		//strContractName = null;
		//sCurr1 = null;
		//sCurr2 = null;
		chmBuyPosition.clear();
		chmSellPosition.clear();
		chmRunningOrder.clear();
		alRunningOrder.clear();
		alBuyRef.clear();
		alSellRef.clear();
	}
	
	public OpenPositionRecord getFirstBuyPosition(){
		if(alBuyRef.isEmpty()){
			return null;
		}else{
			return alBuyRef.get(0);	
		}
	}

	public OpenPositionRecord getFirstSellPosition(){
		if(alSellRef.isEmpty()){
			return null;
		}else{
			return alSellRef.get(0);	
		}
	}

	public OpenPositionRecord getLastBuyPosition(){
		if(alBuyRef.isEmpty()){
			return null;
		}else{
			return alBuyRef.get(alBuyRef.size()-1);	
		}
	}

	public OpenPositionRecord getLastSellPosition(){
		if(alSellRef.isEmpty()){
			return null;
		}else{
			return alSellRef.get(alSellRef.size()-1);	
		}
	}
	
	
	public ArrayList<OpenPositionRecord> getAllBuyPosition(){
		return alBuyRef;	
	}

	public ArrayList<OpenPositionRecord> getAllSellPosition(){
		return alSellRef;	
	}
	
	public boolean isSettingUpdated(String strContractName, int iContractSize,
			boolean bBaseCurr, boolean bViewable, boolean bTradable,
			double dBidInterest, double dAskInterest) {
		boolean isUpdated = false;

		isUpdated = this.strContractName.equals(strContractName)
				&& this.iContractSize == iContractSize
				&& this.bBaseCurr == bBaseCurr && this.bViewable == bViewable
				&& this.bTradable == bTradable
				&& this.dBidInterest == dBidInterest
				&& this.dAskInterest == dAskInterest;

		return !isUpdated;
	}

	public String toString() {
		return strContractCode + " bid: " +this.dBid+"| ask: "+this.dAsk +
			"| bid a : "+this.dBidAdjust + "| ask a : "+this.dAskAdjust +
			"| bid [] : " +this.dBidAsk[0] + "| ask [] : "+this.dBidAsk[1] + " | dp :"+iRateDecPt;
	}
	
	public void aAlertOrder(OrderRecord order) {
		if(orderAlert != null)
			orderAlert.orderAlert(order);
	}
	
	public void updateTargetPrice(OrderRecord order){
		if(orderAlert != null)
			orderAlert.updateTargetPrice(order);
	}
	
	public boolean setBidAsk(double dBid, double dAsk){ 
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
			
			updateBidAsk();
		}
		bChangeBidAsk = !bNoChange;
		return !bNoChange;
	}	
	
	public boolean setHighLow(double dHigh, double dLow){
		if(CompanySettings.SHOW_HIGHLOW_ASK) return true;
		boolean bNoChange = false;
		bNoChange = (this.dHigh == dHigh || Math.abs(dHigh + 1) < 0.01 ) && (this.dLow == dLow || Math.abs(dLow + 1) < 0.01);
		
		iLowUpDown = 0;
		if( Math.abs(dLow + 1) > 0.01 )
		{
			if(this.dLow > dLow){
				iLowUpDown = 1;
			}else{
				iLowUpDown = 2;
			}
		}
		
		iHighUpDown = 0;
		if(Math.abs(dHigh + 1) > 0.01)
		{
			if(this.dHigh > dHigh){
				iHighUpDown = 1;
			}else{
				iHighUpDown = 2;
			}
		}
		
		if(!bNoChange){
			if(Math.abs(dHigh + 1) > 0.01)
				this.dHigh = dHigh;
			if(Math.abs(dLow + 1) > 0.01)
				this.dLow = dLow;
		}
		else
		{
			iHighUpDown = 0;
			iLowUpDown = 0;
		}
		
		bChangeHighLow = !bNoChange;
		return !bNoChange;		
	}
	
	public boolean setHighLow(double dHighBid, double dLowBid, double dHighAsk, double dLowAsk){
		if(!CompanySettings.SHOW_HIGHLOW_ASK) return true;
		boolean bNoChange = false;
		bNoChange = (this.dHighBid == dHighBid) && (this.dLowBid == dLowBid);
				
		iLowUpDown = 0;		
		if(this.dLowBid > dLowBid){
			iLowUpDown = 1;
		}else{
			iLowUpDown = 2;
		}
		
		iHighUpDown = 0;
		if(this.dHighBid > dHighBid){
			iHighUpDown = 1;
		}else{
			iHighUpDown = 2;
		}		
		
		if(!bNoChange){
			this.dHighBid = dHighBid;
			this.dLowBid = dLowBid;
		}
		else
		{
			iHighUpDown = 0;
			iLowUpDown = 0;
		}
		this.dHighAsk = dHighAsk;
		this.dLowAsk = dLowAsk;
		
		bChangeHighLow = !bNoChange;
		return !bNoChange;		
	}
	
	public boolean setHighLowAsk(double dHigh, double dLow){
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

		bChangeHighLow = !bNoChange;
		return !bNoChange;		
	}
	


	public void update(String sCode, String sContractName, String sCurr12,
			String sCurr22, int iContractSize2, boolean bBaseCurr2,
			boolean bViewable2, boolean bTradable2, int iOrderPips2,
			int iTailMax, int iTailMin, double dBidInterest2,
			double dAskInterest2, int iDP, boolean bDQ, double dBid2,
			double dAsk2, double dHigh2, double dLow2) {
		
	}

	public void setOrderPips(int iOrderPips) {
		this.iOrderPips =  iOrderPips;		
	}

	public void setBidAskAdjustRpt(double dBidAdjustRpt, double dAskAdjustRpt) {
		this.dBidAdjustRpt = dBidAdjustRpt;
		this.dAskAdjustRpt = dAskAdjustRpt;
		updateBidAsk();
	}

	public void setBidAskAdjust(double dBidAdjust, double dAskAdjust) {
		this.dBidAdjust = dBidAdjust;
		this.dAskAdjust = dAskAdjust;
		updateBidAsk();
	}
	
	public void setMinLot(double dMinLot)
	{
		this.dMinLot = dMinLot;
	}

	public void setIncLot(double dIncLot)
	{
		this.dIncLot = dIncLot;
	}
	
	public String getContractName(Locale l) {
		String name = "";
		if (l.equals(Locale.SIMPLIFIED_CHINESE)) {
			name = sSCName;
		} else if (l.equals(Locale.TRADITIONAL_CHINESE)) {
			name = sTCName;
		} else {
			name = sENName;
		}
		if (name == null || name.length() == 0)
			name = strContractCode;
		return name;
	}

	public boolean isBuyEmpty(){
		return chmBuyPosition.isEmpty();
	}
	
	public boolean isSellEmpty(){
		return chmSellPosition.isEmpty();
	}

	public HashMap<Integer, OpenPositionRecord> getBuyPositions() {
		return chmBuyPosition;		
	}
	
	public HashMap<Integer, OpenPositionRecord> getSellPositions() {
		return chmSellPosition;		
	}
	
	public void setProviderQuote(String providerQuote) {
		this.providerQuote = providerQuote;
		if (providerQuote!=null && providerQuote.length() > 0)
			setProviderPriceQuoteMap(providerQuote);
	}

	private void setProviderPriceQuoteMap(String providerQuote) {
		String[] providerFields = providerQuote.split("\\|");		
		
		if(providerFields==null || providerFields.length==0)
			return;
		
    	synchronized (providerQuoteMap) {
    		providerQuoteMap.clear();
    		
			for (int i = 0; i < providerFields.length; i++) {
				String[] providerQuotes = providerFields[i].split("\\,");
				if(providerQuotes==null || providerQuotes.length==0)
					continue;
				
				providerQuoteMap.put(providerQuotes[0],new Double[] { Double.parseDouble(providerQuotes[1]),Double.parseDouble(providerQuotes[2]) });
			}
		}
	}
	
	public void setOffsetAndSpread(double dOffset, double dSpread){
		this.dOffset = dOffset;
		this.dSpread = dSpread;
		updateBidAsk();
	}
	
	public void updateBidAsk(){
		double dNewBid = Utility.roundToDouble(dBid + (dOffset + dBidAdjust + dBidAdjustRpt) * Math.pow(10.0, -iRateDecPt), iRateDecPt, iRateDecPt);
		double dNewAsk;
		dNewAsk = Utility.roundToDouble(dAsk + (dOffset + dAskAdjust + dAskAdjustRpt) * Math.pow(10.0, -iRateDecPt), iRateDecPt, iRateDecPt);
		dBidAsk[0] = dNewBid;
		dBidAsk[1] = dNewAsk;
	}
	
	public boolean isViewable(){
		return bViewable;
	}
	
	public boolean isTradable(){
		return bTradable;
	}
	
	public void setRptViewable(boolean b){
		bRptViewable = b;
	}
	
	public void setRptTradable(boolean b){
		bRptTradable = b;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setEnableDepth(String enableDepth) {
		this.enableDepth = enableDepth;
	}

	public void parseDepth(String sXML){
		if (sXML != null && !sXML.isEmpty())
		{
			try
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(sXML));
				Document doc = dBuilder.parse(is);
				NodeList nodes = doc.getElementsByTagName("row");
				int idepth;
				BigDecimal bid, ask, blot, alot;
				BigDecimal cs = new BigDecimal(iContractSize);
				for( int i = 0; i < nodes.getLength(); i++)
				{
					idepth = Utility.toInteger(getChildNode(nodes.item(i), "Depth").getTextContent(), -1);
					blot = getBigDecimalValueFromNode(getChildNode(nodes.item(i), "BidAmount"), 6);
					alot = getBigDecimalValueFromNode(getChildNode(nodes.item(i), "AskAmount"), 6);
					if (blot != null)
					{
						blot = blot.divide(cs, CompanySettings.NUM_OF_LOT_DP, BigDecimal.ROUND_HALF_UP);
					}
					if (alot != null)
					{
						alot = alot.divide(cs, CompanySettings.NUM_OF_LOT_DP, BigDecimal.ROUND_HALF_UP);
					}
					bidLotDepth[idepth-1] = blot == null ? 0 : blot.doubleValue();
					askLotDepth[idepth-1] = alot == null ? 0 : alot.doubleValue();

					//bidAmountDepth[idepth-1] = blot == null ? 0 : getBigDecimalValueFromNode(getChildNode(nodes.item(i), "BidAmount"), 6).doubleValue();
					//askAmountDepth[idepth-1] = alot == null ? 0 : getBigDecimalValueFromNode(getChildNode(nodes.item(i), "AskAmount"), 6).doubleValue();

					if ((idepth == 1 && (bidPriceDepth[0] == null || askPriceDepth[0] == null)) ||
							(idepth > 1 && idepth <= CompanySettings.MARKET_DEPTH_LEVEL)
							)
					{
						bid = getBigDecimalValueFromNode(getChildNode(nodes.item(i), "Bid"), iRateDecPt);
						ask = getBigDecimalValueFromNode(getChildNode(nodes.item(i), "Ask"), iRateDecPt);
						dBidDepth[idepth-1] = bid == null ? Double.NEGATIVE_INFINITY : bid.doubleValue();
						dAskDepth[idepth-1] = ask == null ? Double.NEGATIVE_INFINITY : ask.doubleValue();
						bidPriceDepth[idepth-1] = bid == null ? null : bid.toString();
						askPriceDepth[idepth-1] = ask == null ? null : ask.toString();
					}
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	private Node getChildNode( Node curNode, String tagName )
	{
		for( int i = 0; i < curNode.getChildNodes().getLength(); i ++)
		{
			if(tagName.equals(curNode.getChildNodes().item(i).getNodeName()))
				return curNode.getChildNodes().item(i);
		}

		return null;
	}
	private BigDecimal getBigDecimalValueFromNode(Node n, int scale)
	{
		if (n == null)
			return null;

		try
		{
			return new BigDecimal(n.getTextContent()).setScale(scale, BigDecimal.ROUND_HALF_UP);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public double getMaxLotLimit() {
		return maxLotLimit;
	}

	public void setMaxLotLimit(double maxLotLimit) {
		this.maxLotLimit = maxLotLimit;
	}

	public double getMaxLotLimitUser() {
		return maxLotLimitUser;
	}

	public void setMaxLotLimitUser(double maxLotLimitUser) {
		this.maxLotLimitUser = maxLotLimitUser;
	}

	public double getFinalMaxLotLimit(){
		if (CompanySettings.getMaxlotFromServer) {
			if (this.maxLotLimitUser == -1)
				return this.maxLotLimit;
			else
				return this.maxLotLimitUser;
		}
		else {
			return 50.0;
		}
	}

	public InterestRateMethod getInterestRateMethod() {
		return interestRateMethod;
	}

	public void setInterestRateMethod(InterestRateMethod interestRateMethod) {
		this.interestRateMethod = interestRateMethod;
	}
}
