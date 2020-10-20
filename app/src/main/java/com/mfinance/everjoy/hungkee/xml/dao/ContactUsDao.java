package com.mfinance.everjoy.hungkee.xml.dao;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.ContactUs;

public class ContactUsDao extends AbstractDao<ContactUs>{
	
	public ContactUsDao(MobileTraderApplication app) {
		super(app);
	}
	
	@Override
	public ContactUs getValueFromXML(){
		ContactUs result = null;
		try {
			Serializer serializer = new Persister();
			URL theUrl = null;
			if(CompanySettings.GET_COMPANY_INFO_CONTENT_FROM_SERVLET){
				CommonFunction cf = new CommonFunction(true);
				cf.setKey(Utility.getCustomContentHttpKey());
				theUrl = new URL(MobileTraderApplication.CONTENT_URL+"?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=contactus");
			}else{
				theUrl = new URL(MobileTraderApplication.CONTENT_URL_DIRECT+CompanySettings.ContactUs_XML_Name);
			}
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			result = serializer.read(ContactUs.class, uc.getInputStream());	
		} catch (Exception e) {
			Log.e("ContactUsDao", "ContactUsDaoException", e);
			return new ContactUs();
		}	
		return result;
	}

	@Override
	public void updateXML() {
		ContactUs contactUs = getValueFromXML();
		if(contactUs != null)
			app.data.setContactUs(contactUs);
	}
	
	
}
