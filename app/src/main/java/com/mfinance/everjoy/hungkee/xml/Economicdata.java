package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;

public class Economicdata {
	@Element	
	private String date;
	@Element
	private String time;
	
	@Element(name="country_big5")
	private String countryBig5;
	
	@Element(name="country_gb")
	private String countryGB;
	
	@Element(name="country_en")
	private String countryEN;	
	
	@Element(name="name_big5", required=false)
	private String nameBig5 = "";
	
	@Element(name="name_gb", required=false)
	private String nameGB = "";
	
	@Element(name="name_en", required=false)
	private String nameEN = "";	
	
	@Element(name="description_big5", required=false)
	private String descriptionBig5 = "";
	
	@Element(name="description_gb", required=false)
	private String descriptionGB = "";
	
	@Element(name="description_en", required=false)
	private String descriptionEN = "";
	
	@Element(name="prev_value", required=false)
	private String prevValue = "";
	
	@Element(name="forecast_value", required=false)
	private String forecastValue = "";
	
	@Element(name="actual_value", required=false)
	private String actualValue = "";
	
	@Element(name="unit_big5", required=false)
	private String unitBig5 = "";
	
	@Element(name="unit_gb", required=false)
	private String unitGB = "";
	
	@Element(name="unit_en", required=false)
	private String unitEN = "";

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getCountryBig5() {
		return countryBig5;
	}

	public String getCountryGB() {
		return countryGB;
	}

	public String getCountryEN() {
		return countryEN;
	}

	public String getNameBig5() {
		return nameBig5;
	}

	public String getNameGB() {
		return nameGB;
	}

	public String getNameEN() {
		return nameEN;
	}

	public String getDescriptionBig5() {
		return descriptionBig5;
	}

	public String getDescriptionGB() {
		return descriptionGB;
	}

	public String getDescriptionEN() {
		return descriptionEN;
	}

	public String getPrevValue() {
		return prevValue;
	}

	public String getForecastValue() {
		return forecastValue;
	}

	public String getActualValue() {
		return actualValue;
	}

	public String getUnitBig5() {
		return unitBig5;
	}

	public String getUnitGB() {
		return unitGB;
	}

	public String getUnitEN() {
		return unitEN;
	}
	
}
