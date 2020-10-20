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
import com.mfinance.everjoy.hungkee.xml.Strategies;
import com.mfinance.everjoy.hungkee.xml.StrategiesHK;
import com.mfinance.everjoy.hungkee.xml.StrategiesMF;
import com.mfinance.everjoy.hungkee.xml.Strategy;
import com.mfinance.everjoy.hungkee.xml.StrategyMF;

public class StrategyDao  extends AbstractDao<Strategies> {

	public static final String CONTENT_HOST = "contentserver.tradingengine.net";
	public static final int CONTENT_PORT = 80;
	public static final String CONTENT_URL = "http://"+CONTENT_HOST+":"+CONTENT_PORT+"/mf2/ContentServlet";
	
	public StrategyDao(MobileTraderApplication app) {
		super(app);
	}

	@Override
	public Strategies getValueFromXML() {
		return getValueFromXML(0);
	}
	
	@SuppressWarnings("unchecked")
	public Strategies getValueFromXML(int retry) {
		Strategies result = null;
		try {
			Serializer serializer = new Persister();
			CommonFunction cf = new CommonFunction(true);
			
			URL theUrl;
			
			if(CompanySettings.USE_CUSTOM_DAY_PLAN!=null){
				cf.setKey(Utility.getCustomContentHttpKey());
				theUrl = new URL(MobileTraderApplication.CONTENT_URL+"?key=" + cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=dayplan");
			}
			else{
				cf.setKey(Utility.getContentHttpKey());
				theUrl = new URL(CONTENT_URL + "?key=" + cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=dayplan");
			}
			//URL theUrl = new URL("http://contentserver.tradingengine.net/hungkee/ContentServlet?key=" + cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=dayplan");
			
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);

			result = serializer.read(CompanySettings.USE_CUSTOM_DAY_PLAN!=null?CompanySettings.USE_CUSTOM_DAY_PLAN:StrategiesMF.class, uc.getInputStream());

			result.sort();
		} catch (Exception e) {
			Log.e("StrategyDao", "StrategyDaoException", e);
			if (retry == 3) {
				result = new Strategies();
				result.strategy=new ArrayList<Strategy>();
				return result;
			} else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				retry++;
				result = getValueFromXML(retry);
			}
		}	
		return result;
	}

	@Override
	public void updateXML() {
		Strategies strategies = getValueFromXML();
		
		if(strategies != null)
			app.data.setStrategies(strategies);
	}
}
