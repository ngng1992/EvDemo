package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="other")
public class Other {
	@Element(name="privacy_gb", required=false)
	private String privacyBG;
	
	@Element(name="privacy_big5", required=false)
	private String privacyBig5;
	
	@Element(name="privacy_en", required=false)
	private String privacyEN;
	
	@Element(name="risk_gb", required=false)
	private String riskBG;

	@Element(name="risk_big5", required=false)
	private String riskBig5;
	
	@Element(name="risk_en", required=false)
	private String riskEN;
	
	@Element(name="disclaimer_gb", required=false)
	private String disclaimerBG;

	@Element(name="disclaimer_big5", required=false)
	private String disclaimerBig5;
	
	@Element(name="disclaimer_en", required=false)
	private String disclaimerEN;

	public String getPrivacyBG() {
		return privacyBG;
	}

	public String getPrivacyBig5() {
		return privacyBig5;
	}

	public String getRiskBG() {
		return riskBG;
	}

	public String getRiskBig5() {
		return riskBig5;
	}

	public String getDisclaimerBG() {
		return disclaimerBG;
	}

	public String getDisclaimerBig5() {
		return disclaimerBig5;
	}

	public String getPrivacyEN() {
		return privacyEN;
	}

	public String getRiskEN() {
		return riskEN;
	}

	public String getDisclaimerEN() {
		return disclaimerEN;
	}
}
