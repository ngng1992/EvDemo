package com.mfinance.everjoy.hungkee.xml.dao;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Economicdata;
import com.mfinance.everjoy.hungkee.xml.Economicdatas;

public class EconomicDataDao extends AbstractDao<Economicdatas>  {
	
	public static final String CONTENT_HOST = "contentserver.tradingengine.net";
	public static final int CONTENT_PORT = 80;
	public static final String CONTENT_URL = "http://"+CONTENT_HOST+":"+CONTENT_PORT+"/mf2/ContentServlet";
	
	public EconomicDataDao(MobileTraderApplication app) {
		super(app);
	}

	@Override
	public Economicdatas getValueFromXML() {
		Economicdatas result = null;
		try {
			Serializer serializer = new Persister();
			CommonFunction cf = new CommonFunction(true);
			cf.setKey(Utility.getContentHttpKey());
			URL theUrl = new URL(CONTENT_URL + "?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=economicdata");
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			result = serializer.read(Economicdatas.class, uc.getInputStream());	
		} catch (Exception e) {
			Log.e("EconomicDataDao", "EconomicDataDaoException", e);
			result = new Economicdatas();
			result.economicdatas=new ArrayList<Economicdata>();
			return result;
		}	
		return result;
	}

	@Override
	public void updateXML() {
		Economicdatas data = getValueFromXML();
		if(data != null)
			app.data.setEconomicdatas(data);
	}
}
