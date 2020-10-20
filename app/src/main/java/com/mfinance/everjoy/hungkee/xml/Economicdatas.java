package com.mfinance.everjoy.hungkee.xml;

import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Economicdatas {
	@ElementList(type=Economicdata.class, inline=true)
	public List<Economicdata> economicdatas;

	public List<Economicdata> getEconomicDataList(){
		return economicdatas;
	}
	
	public HashMap<Integer, Economicdata> getEconomicdataMap(){
		List<Economicdata> lEconomicdatas = getEconomicDataList();
		
		if(lEconomicdatas == null){
			return null;
		}else{
			HashMap<Integer, Economicdata> hmEconomicdata = new HashMap<Integer, Economicdata>();
			int count=0;
			for(Economicdata e : lEconomicdatas){
				//System.out.println(e.getDate() + "-"+e.getTime()+"_"+e.getCountryGB()+"-"+e.getNameGB());
				hmEconomicdata.put(count++, e);
			}
			return hmEconomicdata;			
		}
	}		
}
