package com.mfinance.everjoy.hungkee.xml.dao;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Newses;

public class NewsDao extends AbstractDao<Newses>{
	
	public NewsDao(MobileTraderApplication app) {
		super(app);
	}
	
	@Override
	public Newses getValueFromXML() {
		Newses result = null;
		try {
			Serializer serializer = new Persister();
			CommonFunction cf = new CommonFunction(true);
			cf.setKey(Utility.getCustomContentHttpKey());
			URL theUrl = new URL(MobileTraderApplication.CONTENT_URL+"?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=news");
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(2000);
			uc.setReadTimeout(2000);
			result = serializer.read(Newses.class, uc.getInputStream());	
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;
	}
	
	@Override
	public void updateXML() {
		Newses news = getValueFromXML();
		if(news != null)
			app.data.setNewses(news);
	}	
}
