package com.mfinance.everjoy.hungkee.xml;

import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Newscontents {
	@ElementList(type=Newscontent.class, inline=true)
	public List<Newscontent> newscontents;

	public List<Newscontent> getNewsContentList(){
		return newscontents;
	}
	
	public HashMap<Integer, Newscontent> getNewsContentMap(){
		List<Newscontent> lNewscontents = getNewsContentList();
		
		if(lNewscontents == null){
			return null;
		}else{
			HashMap<Integer, Newscontent> hmNewsContent = new HashMap<Integer, Newscontent>();
			int count=0;
			for(Newscontent n : lNewscontents){
				hmNewsContent.put(count++, n);
			}
			return hmNewsContent;			
		}
	}	
}
