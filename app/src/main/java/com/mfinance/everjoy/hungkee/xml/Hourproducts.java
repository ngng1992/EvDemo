package com.mfinance.everjoy.hungkee.xml;

import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Hourproducts {
	@ElementList(type=Hourproduct.class, inline=true, required = false)
	private List<Hourproduct> hourproducts;

	public List<Hourproduct> getHourProductList(){
		return hourproducts;
	}
	
	public HashMap<Integer, Hourproduct> getHourProductMap(){
		List<Hourproduct> lHourproducts = getHourProductList();
		
		if(lHourproducts == null){
			return null;
		}else{
			HashMap<Integer, Hourproduct> hmHourproduct = new HashMap<Integer, Hourproduct>();
			int count=0;
			for(Hourproduct e : lHourproducts){
				hmHourproduct.put(count++, e);
			}
			return hmHourproduct;			
		}
	}		
}
