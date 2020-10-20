package com.mfinance.everjoy.app;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;



public class IdentityCheckActivity extends BaseActivity{	
	WebView myWebView;
	Button btnClose;
	private String URL;
	
	@Override
	public void bindEvent() {
		
		if( !app.isDemoPlatform )
		{
			URL = app.hmIdentityCheckUrls.get("IDENTITY_CHECK") + "?";
			
			if( getLanguage() == Locale.ENGLISH )
			{
				URL += "account=" + DataRepository.getInstance().getStrUser() + "&lang=en"; 
			}
			else if( getLanguage() == Locale.SIMPLIFIED_CHINESE)
			{
				URL += "account=" + DataRepository.getInstance().getStrUser() + "&lang=sc";
			}
			else if( getLanguage() == Locale.TRADITIONAL_CHINESE)
			{
				URL += "account=" + DataRepository.getInstance().getStrUser() + "&lang=tw";
			}
			
		}
			
		if (myWebView == null)
			myWebView = (WebView) findViewById(R.id.wvReport);				
		myWebView.getSettings().setJavaScriptEnabled(true);
		
		myWebView.setWebViewClient(new WebViewClient() {
			
			@Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
				String query = url.substring(url.indexOf('?')+1);
				if( query.contains("Reload"))
				{
					myWebView.reload();
				}
				else
				{
					// submit the answer
					try
					{
						String result = "";
						String sURL = app.hmIdentityCheckUrls.get("IDENTITY_CHECK_SUBMIT");
						CommonFunction cf = new CommonFunction(false);
						cf.setKey(Utility.getHttpKey());
						String encryptedQuery = cf.encryptText(query);
						URLConnection connection = openConnection(sURL + "?" + encryptedQuery);
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
			    			result = cf.decryptText(result);
			    			json = new JSONObject(result);
			    			if(json.getString("stat").equals("success") && json.getString("account").equals(DataRepository.getInstance().getStrUser()) )
			    				LoginActivity.identityPassed = true;
			    			else
			    			{
			    				LoginActivity.identityPassed = false;
			    				String code = String.valueOf(json.getInt("code"));
			    				Toast.makeText(IdentityCheckActivity.this, MessageMapping.getMessageByCode(res, code,app.locale), Toast.LENGTH_LONG).show();
			    			}
			    		} catch (JSONException e) {
			    			// TODO Auto-generated catch block
			    			e.printStackTrace();
			    		}
			    	} catch (Exception e) {
			    		e.printStackTrace();
			    	}
					finally
					{
						LoginActivity.backFromIdentityCheck = true;
						finish();
					}
				}
			
				return true;
	        }
			
			ProgressDialog progressBar = ProgressDialog.show(IdentityCheckActivity.this, null, res.getText(R.string.lb_loading));
			
			public   void   onPageStarted(WebView view, String url, Bitmap favicon){  
				progressBar.show();
			}
			
			public void onPageFinished(WebView view, String url) {
	            progressBar.dismiss();
				myWebView.setVisibility(View.VISIBLE);
			    btnClose.setVisibility(View.VISIBLE);
			}

		});
		
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
		
		myWebView.loadUrl(URL);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.getSettings().setSupportZoom(true); 
		myWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		myWebView.getSettings().setUseWideViewPort(true);
		myWebView.setInitialScale(1);				
	}


	@Override
	public void handleByChild(Message msg) {}

	@Override
	public void loadLayout() {
		setContentView(R.layout.v_demo_registration);
		
		if (btnClose == null)
			btnClose = (Button) findViewById(R.id.btnClose);
	}

	@Override
	public void updateUI() {}
	
	
	@Override
	public boolean isBottonBarExist() {
		return true;
	}

	@Override
	public boolean isTopBarExist() {
		return true;
	}
	@Override
	public boolean showLogout() {
		return true;
	}
	
	@Override
	public boolean showTopNav() {
		return true;
	}

	@Override
	public boolean showConnected() {
		return true;
	}

	@Override
	public boolean showPlatformType() {
		return true;
	}
	
	@Override
	public int getServiceId() {
		return ServiceFunction.SRV_MOVE_TO_IDENTITY_CHECK;
	}			

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_IDENTITY_CHECK;
	}	
	
	public boolean isIdentityCheckActivity(){
		return true;
	}
	
	private static URLConnection openConnection(String strURL)
	{
		try
		{
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