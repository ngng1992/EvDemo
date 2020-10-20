package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="contact_us",strict=false)
public class ContactUs {
	
	@Element(name="intro_en",required=false)
	private String introEN;

	@Element(name="intro_gb",required=false)
	private String introGB;

	@Element(name="intro_big5",required=false)
	private String introBig5;
	
	@Element(name="company_en",required=false)
	private String companyEN;
	
	@Element(name="company_big5",required=false)
	private String companyBig5;
	
	@Element(name="company_gb",required=false)
	private String companyGB;
	
	@Element(name="address_en",required=false)
	private String addressEN;
	
	@Element(name="address_big5",required=false)
	private String addressBig5;
	
	@Element(name="address_gb",required=false)
	private String addressGB;
	
	@Element(name="disclaim_en",required=false)
	private String disclaimEn;
	
	@Element(name="disclaim_gb",required=false)
	private String disclaimGb;
	
	@Element(name="disclaim_big5",required=false)
	private String disclaimBig5;
	
	@Element(name="disclaimer_en",required=false)
	private String disclaimEn2;
	
	@Element(name="disclaimer_gb",required=false)
	private String disclaimGb2;
	
	@Element(name="disclaimer_big5",required=false)
	private String disclaimBig52;
	
	@Element(required=false)
	private String fax;	
	
	@Element(required=false)
	private String tel;
	
	@Element(required=false)
	private String china;
	
	@Element(required=false)
	private String hotline;
	
	@Element(required=false)
	private String website;
	
	@Element(required=false)
	private String email;
	
	public String getCompanyEN() {
		return companyEN!=null?companyEN:"";
	}
	public String getCompanyBig5() {
		return companyBig5!=null?companyBig5:"";
	}
	public String getAddressEN() {
		return addressEN!=null?addressEN:"";
	}
	public String getAddressBig5() {
		return addressBig5!=null?addressBig5:"";
	}
	public String getFax() {
		return fax!=null?fax:"";
	}	
	public String getTel() {
		return tel!=null?tel:"";
	}
	public String getHotline() {
		return hotline!=null?hotline:"";
	}
	public String getChinaHotline() {
		return china!=null?china:"";
	}
	public String getWebsite() {
		return website!=null?website:"";
	}
	public String getEmail() {
		return email!=null?email:"";
	}
	public String getCompanyGB() {
		return companyGB!=null?companyGB:"";
	}
	public String getAddressGB() {
		return addressGB!=null?addressGB:"";
	}
	public String getIntroEN() {
		return introEN!=null?introEN:"";
	}
	public String getIntroGB() {
		return introGB!=null?introGB:"";
	}
	public String getIntroBig5() {
		return introBig5!=null?introBig5:"";
	}
	public String getDisclaimEn() {
		if(disclaimEn!=null)
			return disclaimEn;
		else if(disclaimEn2!=null)
			return disclaimEn2;
		return "";
	}
	public String getDisclaimGb() {
		if(disclaimGb!=null)
			return disclaimGb;
		else if(disclaimGb2!=null)
			return disclaimGb2;
		return "";
	}
	public String getDisclaimBig5() {
		if(disclaimBig5!=null)
			return disclaimBig5;
		else if(disclaimBig52!=null)
			return disclaimBig52;
		return "";
	}
}
