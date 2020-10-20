package com.mfinance.everjoy.hungkee.xml;

import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.ElementList;

public class Advertisements {
	@ElementList(type=Advertisement.class, inline=true)
	private List<Advertisement> advertisements;

	public List<Advertisement> getAdvertisementList(){
		return advertisements;
	}
	
	public HashMap<String, Advertisement> getAdvertisementMap(){
		List<Advertisement> lAdvertisements = getAdvertisementList();
		
		if(lAdvertisements == null){
			return null;
		}else{
			HashMap<String, Advertisement> hmlAdvertisement = new HashMap<String, Advertisement>();
			int count=0;
			for(Advertisement a : lAdvertisements){
				hmlAdvertisement.put(String.valueOf(count++), a);
			}
			return hmlAdvertisement;			
		}
	}			
}
