package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict=false)
public class CompanyProfile {
	
	@Element(name="about_gb",required=false)
	private String aboutGB;
	
	@Element(name="about_en",required=false)
	private String aboutEN;

	@Element(name="about_big5",required=false)
	private String aboutBig5;

	@Element(name="profile_big5",required=false)
	private String profile_big5;
	
	@Element(name="profile_en",required=false)
	private String profile_en;

	@Element(name="profile_gb",required=false)
	private String profile_gb;
	
	public String getAboutGB() {
		if(aboutGB!=null)
			return aboutGB;
		else if(profile_gb!=null)			
			return profile_gb;
		else
			return "";
	}

	public String getAboutEN() {
		if(aboutEN!=null)
			return aboutEN;
		else if(profile_en!=null)			
			return profile_en;
		else
			return "";
	}

	public String getAboutBig5() {
		if(aboutBig5!=null)
			return aboutBig5;
		else if(profile_big5!=null)			
			return profile_big5;
		else
			return "";
	}

}
