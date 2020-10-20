package com.mfinance.everjoy.hungkee.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import android.text.Html;

import com.mfinance.everjoy.app.util.Utility;

@Root(name = "streetnews")
public class MFNewsFeed extends Hourproducts {

	@ElementList(type = MFNews.class, inline = true, required = false)
	private List<MFNews> news;

	private List<Hourproduct> newsTc = new ArrayList<Hourproduct>();
	private List<Hourproduct> newsSc = new ArrayList<Hourproduct>();
	private List<Hourproduct> newsEn = new ArrayList<Hourproduct>();

	@Override
	public List<Hourproduct> getHourProductList() {
		if (Utility.isSimplifiedChineses()) {
			return newsSc;
		} else if (Utility.isTraditionalChinese()) {
			return newsTc;
		} else {
			return newsEn;
		}
	}

	@Override
	public HashMap<Integer, Hourproduct> getHourProductMap() {
		List<Hourproduct> lHourproducts = getHourProductList();

		if (lHourproducts == null) {
			return null;
		} else {
			HashMap<Integer, Hourproduct> hmHourproduct = new HashMap<Integer, Hourproduct>();
			int count = 0;
			for (Hourproduct e : lHourproducts) {
				hmHourproduct.put(count++, e);
			}
			return hmHourproduct;
		}
	}

	@Commit
	public void commit() {
		for (MFNews newsItem : news) {
			newsItem.content = Html.fromHtml(newsItem.content).toString();
			newsItem.title = Html.fromHtml(newsItem.title).toString();
			
			SimpleDateFormat sdfto=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			SimpleDateFormat sdffrom=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.ENGLISH);
			sdffrom.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date d = null;
			try {
				d = sdffrom.parse(newsItem.pubdate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			newsItem.pubdate = sdfto.format(d);
			if (newsItem.getLanguage().equals("TraditionalChinese")) {
				newsTc.add(newsItem);
			} else if (newsItem.getLanguage().equals("SimplifiedChinese")) {
				newsSc.add(newsItem);
			} else {
				newsEn.add(newsItem);
			}
		}
	}
}