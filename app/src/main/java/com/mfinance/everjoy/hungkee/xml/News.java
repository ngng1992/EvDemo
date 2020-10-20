package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;

public class News {
	@Element
	private String datetime;
	
	@Element(name="title_en")
	private String titleEN;
	
	@Element(name="title_gb")
	private String titleGB;
	
	@Element(name="title_big5")
	private String titleBig5;
	
	@Element(name="content_en")
	private String contentEN;
	
	@Element(name="content_gb")
	private String contentGB;
	
	@Element(name="content_big5")
	private String contentBig5;

	public String getDatetime() {
		return datetime;
	}

	public String getTitleEN() {
		return titleEN;
	}

	public String getTitleBig5() {
		return titleBig5;
	}
	
	public String getTitleGB() {
		return titleGB;
	}

	public String getContentEN() {
		return contentEN;
	}

	public String getContentGB() {
		return contentGB;
	}

	public String getContentBig5() {
		return contentBig5;
	}
	
}
