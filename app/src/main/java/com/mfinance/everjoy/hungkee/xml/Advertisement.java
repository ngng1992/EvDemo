package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;

public class Advertisement {
	@Element
	private String url_en;
	
	@Element
	private String url_big5;

	@Element
	private String url_gb;
	
	@Element(required=false)
	private String target;
	
	@Element
	private String timer;

	
	public String getUrl_en() {
		return url_en;
	}

	public String getUrl_big5() {
		return url_big5;
	}



	public String getUrl_gb() {
		return url_gb;
	}

	public String getTarget(){
		return target;
	}

	public String getTimer() {
		return timer;
	}

}
