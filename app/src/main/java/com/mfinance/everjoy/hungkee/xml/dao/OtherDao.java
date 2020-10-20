package com.mfinance.everjoy.hungkee.xml.dao;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Other;

public class OtherDao extends AbstractDao<Other>{
	
	public OtherDao(MobileTraderApplication app) {
		super(app);
	}
	
	@Override
	public Other getValueFromXML(){
		Other result = null;
		try {
			Serializer serializer = new Persister();
			CommonFunction cf = new CommonFunction(true);
			cf.setKey(Utility.getCustomContentHttpKey());
			URL theUrl = new URL(MobileTraderApplication.CONTENT_URL +"?key="+cf.encryptText(Utility.dateWithHourMToString(new Date())) + "&con=other");
			URLConnection uc = theUrl.openConnection();
			uc.setConnectTimeout(2000);
			uc.setReadTimeout(2000);
			result = serializer.read(Other.class, uc.getInputStream());	
		} catch (Exception e) {
			e.printStackTrace();
			return new Other();
		}	
		return result;
	}

	@Override
	public void updateXML() {
		Other other = getValueFromXML();
		if(other != null)
			app.data.setOther(other);
	}
	
	
}


