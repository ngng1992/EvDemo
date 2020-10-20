package com.mfinance.everjoy.hungkee.xml;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import android.util.Log;

@Root(name = "dayplans")
public class StrategiesMF extends Strategies{

	@Attribute(required = false)
	public String schemaLocation;

	@ElementList(type=StrategyMF.class, inline = true, required = false)
	public List<StrategyMF> strategymf;

	@Commit
	public void commit() {
		strategy = new ArrayList<Strategy>();
		for(StrategyMF s : strategymf){
			Strategy newStrategy=new Strategy();
			newStrategy.caption_cn=s.caption_cn;
			newStrategy.caption_en=s.caption_en;
			newStrategy.caption_zh=s.caption_zh;
			newStrategy.content_cn=s.content_cn;
			newStrategy.content_en=s.content_en;
			newStrategy.content_zh=s.content_zh;
			newStrategy.gold_cn=s.gold_cn;
			newStrategy.gold_en=s.gold_en;
			newStrategy.gold_zh=s.gold_zh;
			newStrategy.id=s.id;
			newStrategy.issueDate=s.issueDate;
			newStrategy.photo1=s.photo1;
			newStrategy.type_cn=s.type_cn;
			newStrategy.type_en=s.type_en;
			newStrategy.type_zh=s.type_zh;
			newStrategy.issueDateOnly=s.issueDate.split(" ")[0];
			strategy.add(newStrategy);
		}
		int id = 0;
		for (Strategy s : strategy) {
			s.id = id;
			id++;
			/*s.photo1 = s.photo1.replaceAll("/analysis/",
					"http://chart.m-finance.net/plotioapi/chart/commentary/")
					.replaceAll("images", "images/nowatermark");
			*/
		}
	}
}
