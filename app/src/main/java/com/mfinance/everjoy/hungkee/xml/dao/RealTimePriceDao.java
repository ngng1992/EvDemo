package com.mfinance.everjoy.hungkee.xml.dao;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Price;
import com.mfinance.everjoy.hungkee.xml.Prices;

public class RealTimePriceDao extends AbstractDao<Prices>  {

	public RealTimePriceDao(MobileTraderApplication app) {
		super(app);
	}
	
	@Override
	public Prices getValueFromXML() {
		Prices result = null;
		URL theUrl = null;
		try {
			Serializer serializer = new Persister();
			CommonFunction cf = new CommonFunction(true);
			
			cf.setKey(Utility.getCustomContentHttpKey());
			//System.out.println("new Date():" + new Date());
			
			theUrl = new URL(MobileTraderApplication.CONTENT_URL+"?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=price");
			
			//System.out.println("price:" + CONTENT_URL+"?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=price");
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(1000);
			uc.setReadTimeout(2000);
			result = serializer.read(Prices.class, uc.getInputStream());	
		} catch (Exception e) {
			e.printStackTrace();
			theUrl = null;
		}	
		return result;
	}

	@Override
	public void updateXML() {
		Prices prices = getValueFromXML();
		
		if(app.bPriceReloadInXML){
			app.data.cleanContract();
			app.bPriceReloadInXML = false; 
		}
		
		if(prices != null){
			for(Price price: prices.getRealTimePriceList()){
				double dBeforeBid = price.dBid; 
				double dBeforeAsk  = price.dAsk;
				
				try {			
					price.dBid = Double.valueOf(price.getBid());
					price.dAsk = Double.valueOf(price.getAsk());
				} catch (Exception e){
					price.dBid = dBeforeBid; 
					price.dAsk = dBeforeAsk;
				}
				if(!app.data.isContractExist(price.curr)){
					addContractObjByPrice(price);
				}else{
					updateContractObjByPrice(app.data.getContract(price.curr), price);
				}
			}				
		}else{
			//Log.e("prices", "null");
		}
	}
	
	public void updateContractObjByPrice(ContractObj contract, Price price){
		double dBid = Utility.toDouble(price.getBid(), -1);
		double dAsk = Utility.toDouble(price.getAsk(), -1);	
		double dHigh = Utility.toDouble(price.getHighbid(), -1);
		double dLow = Utility.toDouble(price.getLowbid(), -1);	
		double dHighAsk = Utility.toDouble(price.getHighask(), -1);
		double dLowAsk = Utility.toDouble(price.getLowask(), -1);	
		
		contract.setBidAsk(dBid, dAsk);
		contract.setHighLow(dHigh, dLow);
		
		if(dHighAsk != -1 && dLowAsk != -1)
			contract.setHighLow(dHigh, dLow, dHighAsk, dLowAsk);

	}
	
	public void addContractObjByPrice(Price price){			
		String sCode = price.curr;
		String sContractName = price.sEName;
		
		String sENName  = price.sEName;
		String sTCName  = price.sTName;
		String sSCName  = price.sSName;
				
		String sCurr1 = price.curr1;
		String sCurr2 = price.curr2;
		
		int iContractSize = price.iContractSize;
		boolean bBaseCurr = price.iBSD == 1;
		boolean bViewable = true;
		boolean bTradable = true;
		
		int iOrderPips = -1;
		int iTailMax = -1;
		int iTailMin = -1;
		
		double dBidInterest = -1;
		double dAskInterest = -1;

		int iDP = price.iDP;
		
		double dBid = Utility.toDouble(price.getBid(), -1);
		double dAsk = Utility.toDouble(price.getAsk(), -1);	
		double dHigh = Utility.toDouble(price.getHighbid(), -1);
		double dLow = Utility.toDouble(price.getLowbid(), -1);			
		double dCounterRate = price.dCounterRate;
		
		boolean bCounterDirectQuto = price.iCDQ == 1;
				
		ContractObj contractObj = new ContractObj(sCode, sContractName, sCurr1, sCurr2,
				iContractSize, bBaseCurr, bViewable, bTradable, iOrderPips,
				iTailMax, iTailMin, dBidInterest, dAskInterest, iDP,  
				dBid, dAsk, dHigh, dLow, dCounterRate, bCounterDirectQuto, sENName, sTCName, sSCName, bBaseCurr, -1, -1, -1, -1
		);	
		
		app.data.addContract(contractObj);
	}
	
	
}
