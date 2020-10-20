package com.mfinance.everjoy.app.util;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AppLauncherLib {
	public static String getSetting(String License)
	{
		if (License == null) {
			return null;
		}
		
		try
		{
			String result = "";
			String sURL = "http://pfportal.tradingengine.net:2083/metadata/setting.asp?license=";
			URLConnection connection = openConnection(sURL + License);
			if (connection == null)
				connection = openConnection(sURL + License);
			if (connection == null)
				connection = openConnection(sURL + License);
			if (connection == null)
        		return null;

    		BufferedReader in = new BufferedReader(
    					new InputStreamReader(
    					connection.getInputStream(),"windows-1252"));
    					
    		String decodedString;

    		while ((decodedString = in.readLine()) != null) {
    			result += decodedString;
    		}
    		in.close();
    		
    		JSONObject json;
    		JSONArray connInfo;
    		try {
    			json = new JSONObject(result);
    			return json.getString("result");   			
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			return null;
    		}
    	} catch (Exception e) {    		
    		return null;
    	}
		
    	

	}
	
	public static String getMetaData(String License, String Version, String sURL, String sAppUpdateType)
	{
		if (License == null || Version == null) {
			return null;
		}
		
		StringBuffer strQueryString = new StringBuffer();		
        strQueryString.append("license=").append(License).append("&version=").append(Version);
        if( sAppUpdateType != null )
        	strQueryString.append("&urltype=").append(sAppUpdateType);
        
        CommonFunction cf = new CommonFunction();        
        cf.setKey("akxUt4begn62k9gl803u");
        String strEncryptedQueryString = cf.encryptText(strQueryString.toString());        
        String data = "";
        
        //http://applauncher.mfcloud.net/metadata/applauncher.asp?key=eddc129d1e339d21e6c7eba279c935fe3e9ab508f83310c9a23233bb0414cb39c34cba1843c8411182e319333f925bd7f41ee07be971fddb2a93669f4cabedbbcb97f111

        try {
        	
        	URLConnection connection = openConnection(sURL + strEncryptedQueryString);
        	//System.out.println(sURL + strEncryptedQueryString);
        	if (connection == null)
        		connection = openConnection(sURL + strEncryptedQueryString);        	
        	if (connection == null)
        		connection = openConnection(sURL + strEncryptedQueryString);
        	if (connection == null)
        		return null;

    		BufferedReader in = new BufferedReader(
    					new InputStreamReader(
    					connection.getInputStream(),"windows-1252"));
    					
    		String decodedString;

    		while ((decodedString = in.readLine()) != null) {
    		    data += decodedString;
    		}
    		in.close();
    		
    	} catch (Exception e) {    		
    		return null;
    	}
		
    	String json = "";
    	
		try {
			byte[] b =  Base64.decode(data, Base64.DEFAULT);
			json = cf.performOperation(b, false);
			json = json.substring(0,json.indexOf("@END@"));
		} catch (Exception e) {
			json = e.toString();
		}
    	
    	return json;
	}
	
	public static String decode(String s)
	{
		String Temp = "";
		try
		{
			String[] Nums = s.split("(?<=\\G.{3})");
			int k;
			for (int i=0;i<Nums.length;i++){
				k = Integer.parseInt(Nums[i]);
				if (k > 900)
					k -= 900;
				k -= 20;
				char c = (char)k;
				Temp = Temp + c;
			}
		} catch (Exception err) {
			return null;
		}
		return Temp;
	}
	
	private static URLConnection openConnection(String strURL)
	{
		try
		{
			//System.out.println("http://192.168.122.233/uat/metadata/applauncher.asp?key=" + strEncryptedQueryString);
			//http://192.168.122.233/uat/metadata/applauncher.asp?key=eddc129d1e339d21e1c0eba27ac132f9399ebc07f33318c9a33637be0815c234cb4db91949c4431183ef1e3a38925adffe15e17ce874f1dd2a93669f4cabedbbcb92f112cda7
			URL url = new URL(strURL);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(5000);
    		connection.setDoOutput(false);
    		connection.getInputStream();
			return connection;
		} catch (Exception e) {
			//System.out.println(e);
			return null;
		}
	}
}
