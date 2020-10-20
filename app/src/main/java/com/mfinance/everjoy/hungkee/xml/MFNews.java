package com.mfinance.everjoy.hungkee.xml;

import java.net.URLEncoder;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.content.res.Resources;
import android.text.Html;
import android.util.Log;


import com.mfinance.everjoy.app.util.Utility;

@Root(name = "news", strict = false)
public class MFNews extends Hourproduct{
	@Element(required = false)
	private String newsid;

	@Element(required = false)
	private String language;

	@Element(required = false)
	private String contract;

	@Element(required = false)
	public String pubdate;

	@Element(required = false)
	private String provider;

	@Element(required = false)
	private String market;

	@Element(required = false)
	private String headline;

	@Element(required = false)
	public String title;

	@Element(required = false)
	private String charts;

	@Element(required = false)
	public String content;

	@Element(required = false)
	private String summary;

	@Element(required = false)
	private String techAnalysis_updatedAt;

	@Element(required = false)
	private String techAnalysis_open;

	@Element(required = false)
	private String techAnalysis_close;

	@Element(required = false)
	private String techAnalysis_high;

	@Element(required = false)
	private String techAnalysis_low;

	@Element(required = false)
	private String techAnalysis_bid;

	@Element(required = false)
	private String techAnalysis_ask;

	@Element(required = false)
	private String techAnalysis_pct;

	@Element(required = false)
	private String trendIndex_Recommendation;

	@Element(required = false)
	private String trendIndex_Strength;

	@Element(required = false)
	private String pivotPoints_s1;

	@Element(required = false)
	private String pivotPoints_s2;

	@Element(required = false)
	private String pivotPoints_s3;

	@Element(required = false)
	private String pivotPoints_r1;

	@Element(required = false)
	private String pivotPoints_r2;

	@Element(required = false)
	private String pivotPoints_r3;

	@Element(required = false)
	private String oBOSIndex_recommendation;
	
	public String getTitle() {
		return title;
	}

	public String getSubTitle() {
		return pubdate;
	}

	public String getHeadline() {
		return title;
	}

	public String getContent(Resources res) {
		String content = this.content;
		if(charts!=null&&charts.length()>0){
			for(String str:charts.split(",")){
				content+=String.format("<br/><img src='%s'></img>", str);
			}
		}
		return content;
	}
	
	protected String getLanguage() {
		return language;
	}
}
