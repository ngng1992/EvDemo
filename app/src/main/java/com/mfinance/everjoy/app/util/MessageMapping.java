package com.mfinance.everjoy.app.util;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.mfinance.everjoy.R;


public class MessageMapping {

	public static String getMessageByCode(Context context, String sCode) {
		try {
			return context.getString(R.string.class.getField("msg_"+sCode).getInt(null));
		} catch (Exception e) {
			return sCode;
		}

	}
	public static String getMessageByCode(Resources res, String sCode, Locale locale){
		try{
			return res.getString(R.string.class.getField("msg_"+sCode).getInt(null));
		}catch(Exception e){
			return sCode;
		}
	}
	
	public static String getMessageByCode(Resources res, int iCode, Locale locale){
		String sCode = String.valueOf(iCode);
		return getMessageByCode(res, sCode, locale);
	}
	
	public static String getStringById(Resources res, String sCode, Locale locale){
		try{
			//System.out.println(" res.getConfiguration():"+ res.getConfiguration().locale.toString());
			if (!res.getConfiguration().getLocales().get(0).equals(locale)) {
				Configuration conf = res.getConfiguration();
				conf.setLocale(locale);
				res.updateConfiguration(conf, null);
			}
			//System.out.println(" res.getConfiguration():"+ res.getConfiguration().locale.toString());
			return res.getString(R.string.class.getField(sCode).getInt(null));	
		}catch(Exception e){
			return sCode;
		}
	}
}
