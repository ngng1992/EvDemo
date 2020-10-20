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
import com.mfinance.everjoy.hungkee.xml.Master;
import com.mfinance.everjoy.hungkee.xml.Masters;

public class MasterDao extends AbstractDao<Masters> {

	public MasterDao(MobileTraderApplication app) {
		super(app);
	}

	@Override
	public Masters getValueFromXML() {
		Masters result = null;
		try {
			Serializer serializer = new Persister();
			CommonFunction cf = new CommonFunction(true);
			cf.setKey(Utility.getCustomContentHttpKey());
			//URL theUrl = new URL("http://contentserver.tradingengine.net/sungoldsilver/Master.xml");
			URL theUrl;
			if(CompanySettings.GET_MASTER_FROM_SERVLET)
				theUrl = new URL(MobileTraderApplication.CONTENT_URL+"?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=master");
			else
				theUrl = new URL(MobileTraderApplication.CONTENT_URL_DIRECT+"Master.xml");
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			result = serializer.read(Masters.class, uc.getInputStream());	
		} catch (Exception e) {
			Log.e("MasterDao", "MasterDaoException", e);
			result=new Masters();
			result.masters=new ArrayList<Master>();
			return result;
		}	
		return result;
	}

	@Override
	public void updateXML() {
		Masters master = getValueFromXML();
		if(master != null){
			app.data.setMasters(master);
		}
	}

}
