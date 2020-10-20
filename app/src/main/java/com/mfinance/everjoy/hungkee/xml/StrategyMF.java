package com.mfinance.everjoy.hungkee.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "dayplan", strict = false)
public class StrategyMF{
	@Element(name = "datetime", required = false)
	public String issueDate;

	@Element(name = "type_en", required = false)
	public String type_en;

	@Element(name = "type_big5", required = false)
	public String type_zh;

	@Element(name = "type_gb", required = false)
	public String type_cn;

	@Element(name = "title_en", required = false)
	public String caption_en;

	@Element(name = "title_big5", required = false)
	public String caption_zh;

	@Element(name = "title_gb", required = false)
	public String caption_cn;

	@Element(name = "content_en", required = false)
	public String content_en;

	@Element(name = "content_big5", required = false)
	public String content_zh;

	@Element(name = "content_gb", required = false)
	public String content_cn;

	@Element(name = "curr_en", required = false)
	public String gold_en;

	@Element(name = "curr_big5", required = false)
	public String gold_zh;

	@Element(name = "curr_gb", required = false)
	public String gold_cn;

	public int id;

	@Element(name = "chart_loc", required = false)
	public String photo1;
}
