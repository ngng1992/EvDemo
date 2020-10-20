package com.mfinance.everjoy.hungkee.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import com.mfinance.everjoy.app.util.Utility;

@Root(name="STRATEGY")
public class StrategiesHK extends Strategies{
	
	@Attribute(required=false) 
	public String schemaLocation; 
	
	@ElementList(type=StrategyHK.class, inline=true, required = false)
	public List<StrategyHK> strategyhk;
	
	@Override
	public void sort(){
		Collections.sort(strategy, new Comparator<Strategy>(){

			@Override
			public int compare(Strategy arg0, Strategy arg1) {
				if(arg0.id < arg1.id)
					return 1;
				else if(arg0.id > arg1.id)
					return -1;
				else
					return 0;
			}
			
		});
	}
	
	@Commit
	public void commit() {
		strategy = new ArrayList<Strategy>();
		for(StrategyHK s : strategyhk){
			Strategy newStrategy=new Strategy();
			newStrategy.caption_cn=s.caption_cn;
			newStrategy.caption_en=s.caption_en;
			newStrategy.caption_zh=s.caption_zh;
			newStrategy.content_cn=Utility.convertHtmlSpecChar(s.content_cn).replace("src=\"", 
					"src=\"http://clients3.web-design.hk");
			newStrategy.content_en=Utility.convertHtmlSpecChar(s.content_en).replace("src=\"", 
					"src=\"http://clients3.web-design.hk");
			newStrategy.content_zh=Utility.convertHtmlSpecChar(s.content_zh).replace("src=\"", 
					"src=\"http://clients3.web-design.hk");
			newStrategy.gold_cn=s.gold_cn;
			newStrategy.gold_en=s.gold_en;
			newStrategy.gold_zh=s.gold_zh;
			newStrategy.id=s.id;
			newStrategy.issueDate=s.issueDate;
			newStrategy.photo1=s.photo1;
			newStrategy.type_cn=s.type_cn;
			newStrategy.type_en=s.type_en;
			newStrategy.type_zh=s.type_zh;
			newStrategy.issueDateOnly=s.issueDate;
			strategy.add(newStrategy);
		}
	}

}
