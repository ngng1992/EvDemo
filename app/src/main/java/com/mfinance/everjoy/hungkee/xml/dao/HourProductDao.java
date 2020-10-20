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
import com.mfinance.everjoy.hungkee.xml.Hourproducts;
import com.mfinance.everjoy.hungkee.xml.StrategiesMF;


public class HourProductDao extends AbstractDao<Hourproducts>  {
	
	public static final String CONTENT_HOST = "contentserver.tradingengine.net";
	public static final int CONTENT_PORT = 80;
	public static final String CONTENT_URL = "http://"+CONTENT_HOST+":"+CONTENT_PORT+"/mf2/ContentServlet";
	
	public HourProductDao(MobileTraderApplication app) {
		super(app);
	}

	@Override
	public Hourproducts getValueFromXML() {
		Hourproducts result = null;
		try {
			Serializer serializer = new Persister();
			CommonFunction cf = new CommonFunction(true);
			
			URL theUrl;
			if(CompanySettings.USE_CUSTOM_HOURPRODUCT!=null){
				if(CompanySettings.USE_CUSTOM_HOURPRODUCT_URL!=null)
					theUrl = new URL(CompanySettings.USE_CUSTOM_HOURPRODUCT_URL);
				else{
					cf.setKey(Utility.getCustomContentHttpKey());
					theUrl = new URL(MobileTraderApplication.CONTENT_URL+"?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=hourproduct");
				}
			}
			else{
				cf.setKey(Utility.getContentHttpKey());
				theUrl = new URL(CONTENT_URL+"?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=hourproduct");
			}

			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(CONNECTION_TIMEOUT);
			uc.setReadTimeout(CONNECTION_TIMEOUT);
			
			result = serializer.read(CompanySettings.USE_CUSTOM_HOURPRODUCT!=null?CompanySettings.USE_CUSTOM_HOURPRODUCT:Hourproducts.class, uc.getInputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;
	}

	@Override
	public void updateXML() {
		Hourproducts data = getValueFromXML();
		if(data != null)
			app.data.setHourproducts(data);
	}
}
