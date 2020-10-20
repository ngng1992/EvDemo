package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict=false)
public class Master {
	
	@Element(name="mastername_big5",required=false)
	private String masternameBig5;
	
	@Element(name="mastername_gb",required=false)
	private String masternameGB;
	
	@Element(name="mastername_en",required=false)
	private String masternameEN;
	
	@Element(name="email",required=false)
	private String email;
	
	@Element(name="date",required=false)
	private String date;
	
	@Element(name="post_gb",required=false)
	private String postGB;
	
	@Element(name="post_big5",required=false)
	private String postBig5;
	
	@Element(name="post_en",required=false)
	private String postEN;
	
	@Element(name="title_gb",required=false)
	private String titleGB;
	
	@Element(name="title_big5",required=false)
	private String titleBig5;
	
	@Element(name="title_en", required=false)
	private String titleEN;	
	
	@Element(name="content_gb",required=false)
	private String contentGB;
	
	@Element(name="content_big5",required=false)
	private String contentBig5;
	
	@Element(name="content_en", required=false)
	private String contentEN;

	public String getMasternameBig5() {
		return masternameBig5!=null?masternameBig5:"";
	}

	public String getMasternameGB() {
		return masternameGB!=null?masternameGB:"";
	}

	public String getEmail() {
		return email!=null?email:"";
	}

	public String getPostGB() {
		return postGB!=null?postGB:"";
	}

	public String getPostBig5() {
		return postBig5!=null?postBig5:"";
	}

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

	public String getMasternameEN() {
		return masternameEN!=null?masternameEN:"";
	}

	public String getPostEN() {
		return postEN!=null?postEN:"";
	}

	public String getTitleEN() {
		return titleEN!=null?titleEN:"";
	}

	public String getContentEN() {
		return contentEN!=null?contentEN:"";
	}

	public String getDate() {
		return date!=null?date:"";
	}
	
	public String getKey(){
		return (date!=null?date:"") +"|"+(titleEN!=null?titleEN:"");
	}	
	
}
