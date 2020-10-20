package com.mfinance.everjoy.hungkee.xml.dao;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Advertisements;

public class AdvertisementDao  extends AbstractDao<Advertisements>{
	
	public AdvertisementDao(MobileTraderApplication app) {
		super(app);
	}

	@Override
	public Advertisements getValueFromXML() {
		Advertisements result = null;
		try {
			Serializer serializer = new Persister();
			URL theUrl = new URL(MobileTraderApplication.CONTENT_URL_DIRECT+CompanySettings.Advertisement_XML_Name);
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(2000);
			uc.setReadTimeout(2000);
			result = serializer.read(Advertisements.class, uc.getInputStream());	
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;
	}

	@Override
	public void updateXML() {
		Advertisements adv =  getValueFromXML();
		if(adv != null)
			app.data.setAdvertisements(adv);
	}
}
