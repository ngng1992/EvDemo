package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict=false)
public class Newscontent {

	@Element(name="title_gb",required=false)
	private String titleGB;
	
	@Element(name="title_big5",required=false)
	private String titleBig5;
	
	@Element(name="title_en",required=false)
	private String titleEN;
	
	@Element(name="content_gb",required=false)
	private String contentGB;
	
	@Element(name="content_big5",required=false)
	private String contentBig5;
	
	@Element(name="content_en",required=false)
	private String contentEN;
	
	@Element(name="date",required=false)
	private String date;
	
	public String getTitleGB() {
		return titleGB!=null?titleGB:"";
	}
	public String getTitleBig5() {
		return titleBig5!=null?titleBig5:"";
	}
	public String getContentGB() {
		return contentGB!=null?contentGB:"";
	}
	public String getContentBig5() {
		return contentBig5!=null?contentBig5:"";
	}
	public String getDate() {
		return date!=null?date:"";
	}
	public String getTitleEN() {
		return titleEN!=null?titleEN:"";
	}
	public String getContentEN() {
		return contentEN!=null?contentEN:"";
	}
}
