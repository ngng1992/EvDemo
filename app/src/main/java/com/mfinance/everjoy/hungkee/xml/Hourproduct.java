package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;

import android.content.res.Resources;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.Utility;

public class Hourproduct {
	@Element(required = false)
	private String date;
	@Element(required = false)
	private String time;

	@Element(name = "description_big5", required = false)
	private String descriptionBig5 = "";

	@Element(name = "description_gb", required = false)
	private String descriptionGB = "";

	@Element(name = "description_en", required = false)
	private String descriptionEN = "";

	@Element(name = "curr_big5", required = false)
	private String currBig5 = "";

	@Element(name = "curr_gb", required = false)
	private String currGB = "";

	@Element(name = "curr_en", required = false)
	private String currEN = "";

	private String getDate() {
		return date;
	}

	private String getTime() {
		return time;
	}

	private String getDescriptionBig5() {
		return descriptionBig5;
	}

	private String getDescriptionGB() {
		return descriptionGB;
	}

	private String getDescriptionEN() {
		return descriptionEN;
	}

	private String getCurrBig5() {
		return currBig5;
	}

	private String getCurrGB() {
		return currGB;
	}

	private String getCurrEN() {
		return currEN;
	}

	public String getTitle() {
		if (Utility.isSimplifiedChineses())
			return getCurrGB();
		else if (Utility.isTraditionalChinese())
			return getCurrBig5();
		else
			return getCurrEN();
	}

	public String getSubTitle() {
		return getDate() + " " + getTime();
	}

	public String getHeadline() {
		if (Utility.isSimplifiedChineses())
			return getDescriptionGB();
		else if (Utility.isTraditionalChinese())
			return getDescriptionBig5();
		else
			return getDescriptionEN();
	}

	public String getContent(Resources res) {
		String content = res.getText(R.string.lb_publish_time) + " : "
				+ getTime() + "<br/><br/>" + getHeadline();
		return content;
	}
}
