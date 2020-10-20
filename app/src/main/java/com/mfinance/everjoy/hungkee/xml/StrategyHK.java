package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "strategy", strict = false)
public class StrategyHK{
	@Element(required=false)
	public int id;
	
	@Element(required=false)		
	public String issueDate;
	
	@Element(required=false)
	public int rank;
	
	@Element(required=false)
	public String type_en;
	
	@Element(required=false)
	public String type_zh;
	
	@Element(required=false)
	public String type_cn;
	
	@Element(required=false)
	public String gold_en;
	
	@Element(required=false)
	public String gold_zh;
	
	@Element(required=false)
	public String gold_cn;
	
	@Element(required=false)
	public String caption_en;
	
	@Element(required=false)
	public String caption_zh;
	
	@Element(required=false)
	public String caption_cn;
	
	@Element(required=false)
	public String content_en;
	
	@Element(required=false)
	public String content_zh;
	
	@Element(required=false)
	public String content_cn;
	
	@Element(required=false)
	public String photo1;
	
	@Element(required=false)
	public String photo2;
	
	@Element(required=false)
	public String photo3;
	
	@Element(required=false)
	public String status;
	
	@Element(required=false)
	public String createDate;
	
	@Element(required=false)
	public String lastModDate;
}
