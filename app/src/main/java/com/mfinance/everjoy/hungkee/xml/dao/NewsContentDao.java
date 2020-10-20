package com.mfinance.everjoy.hungkee.xml.dao;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Newscontent;
import com.mfinance.everjoy.hungkee.xml.Newscontents;

public class NewsContentDao extends AbstractDao<Newscontents>{

	public NewsContentDao(MobileTraderApplication app) {
		super(app);
	}

	@Override
	public Newscontents getValueFromXML() {
		Newscontents result = null;
		try {
			Serializer serializer = new Persister();
			CommonFunction cf = new CommonFunction(true);
			cf.setKey(Utility.getCustomContentHttpKey());
			//URL theUrl = new URL("http://contentserver.tradingengine.net/sungoldsilver/NewsContent.xml");
			URL theUrl;
			if(CompanySettings.GET_NEWS_CONTENT_FROM_SERVLET)
				theUrl = new URL(MobileTraderApplication.CONTENT_URL+"?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=newscontent");
			else
				theUrl = new URL(MobileTraderApplication.CONTENT_URL_DIRECT+"NewsContent.xml");
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			result = serializer.read(Newscontents.class, uc.getInputStream());				
		} catch (Exception e) {
			Log.e("NewsContentDao", "NewsContentDaoException", e);
			result = new Newscontents();
			result.newscontents=new ArrayList<Newscontent>();
			return result;
		}	
		return result;
	}

	@Override
	public void updateXML() {
		Newscontents news = getValueFromXML();
		if(news != null)
			app.data.setNewscontents(news);
	}


}
