package com.mfinance.everjoy.hungkee.xml;

import java.util.HashMap;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Prices {
	@ElementList(type=Price.class, inline=true)
	private List<Price> realtimeprices;

	public List<Price> getRealTimePriceList(){
		return realtimeprices;
	}
	
	public HashMap<String, Price> getPriceMap(){
		List<Price> lPrices = getRealTimePriceList();
		
		if(lPrices == null){
			return null;
		}else{
			HashMap<String, Price> hmPrice = new HashMap<String, Price>(20);
			for(Price n : lPrices){
				hmPrice.put(String.valueOf(n.getCurr()), n);
			}
			return hmPrice;			
		}
	}
	
	public Prices(){
		
	}

	public void addRealTimePrices(Price p){
		realtimeprices.add(p);
	}
}
