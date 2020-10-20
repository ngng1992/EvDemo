package com.mfinance.everjoy.hungkee.xml;

import java.util.HashMap;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Newses {
	@ElementList(type=News.class, inline=true)
	private List<News> newses;

	public List<News> getNewsList(){
		return newses;
	}
	
	public HashMap<Integer, News> getNewsMap(){
		List<News> lNewss = getNewsList();
		
		if(lNewss == null){
			return null;
		}else{
			HashMap<Integer, News> hmNews = new HashMap<Integer, News>();
			int count=0;
			for(News n : lNewss){
				hmNews.put(count++, n);
			}
			return hmNews;			
		}
	}		
}
