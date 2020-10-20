package com.mfinance.everjoy.hungkee.xml;

public class Strategy {
	public String issueDate;

	public String issueDateOnly;
	
	public String type_en;

	public String type_zh;

	public String type_cn;

	public String caption_en;

	public String caption_zh;

	public String caption_cn;

	public String content_en;

	public String content_zh;

	public String content_cn;

	public String gold_en;

	public String gold_zh;

	public String gold_cn;

	public int id;

	public String photo1;

	public String getKey() {
		return String.valueOf(id);
	}
}
