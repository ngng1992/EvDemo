package com.mfinance.everjoy.hungkee.xml.dao;

import java.net.URL;
import java.net.URLConnection;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.hungkee.xml.CompanyProfile;

public class CompanyProfileDao extends AbstractDao<CompanyProfile>{
	
	public CompanyProfileDao(MobileTraderApplication app) {
		super(app);
	}
	
	@Override
	public CompanyProfile getValueFromXML(){
		CompanyProfile result = null;
		try {
			Serializer serializer = new Persister();
			URL theUrl = new URL(MobileTraderApplication.CONTENT_URL_DIRECT+CompanySettings.CompanyProfile_XML_Name);
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			result = serializer.read(CompanyProfile.class, uc.getInputStream());	
		} catch (Exception e) {
			Log.e("CompanyProfileDao", "CompanyProfileDaoException", e);
			return new CompanyProfile();
		}	
		return result;
	}

	@Override
	public void updateXML() {
		CompanyProfile cp = getValueFromXML();
		if(cp != null){
			app.data.setCompanyProfile(cp);
		}
	}
	
	
}
