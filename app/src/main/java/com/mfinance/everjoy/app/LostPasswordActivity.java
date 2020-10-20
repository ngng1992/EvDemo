package com.mfinance.everjoy.app;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;

import java.util.Locale;



public class LostPasswordActivity extends BaseActivity{	
	WebView myWebView;
	Button btnClose;
	
	@Override
	public void bindEvent() {
		
		String url = "";
		if( !app.isDemoPlatform )
		{
			if( CompanySettings.ENABLE_FATCH_PLATFORM_ID_FROM_MOBILE_SERVICE == true )
			{
				try
				{
					String userId = DataRepository.getInstance().getStrUser();
					String URL = app.getReportGroupURL(false)
									+ "id=" + userId + "&isDemo=0";
					String result = LoginActivity.fetch(URL);
							
					if (BuildConfig.DEBUG)
						Log.e("REPORT_GROUP", result);
							
					String[] results = result.split("\\|");
					if(results.length > 1)
					{
						CompanySettings.PLATFORM_ID_FROM_MOBILE_SERVICE = Integer.parseInt(results[0]);
					}
				} catch (Exception e) {
					
				}
			}
			
			if( CompanySettings.checkProdServer() == 1 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("PROD_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD_TC");
				}
			}
			else if( CompanySettings.checkProdServer() == 2 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD2_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD2_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("PROD2_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD2_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD2_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD2_TC");
				}
			}
			else if( CompanySettings.checkProdServer() == 3 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD3_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD3_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE &&  app.hmLostPwdUrls.containsKey("PROD3_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD3_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD3_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD3_TC");
				}
			}
			else if( CompanySettings.checkProdServer() == 4 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD4_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD4_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("PROD4_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD4_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD4_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD4_TC");
				}
			}
			else if( CompanySettings.checkProdServer() == 5 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD5_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD5_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("PROD5_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD5_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD5_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD5_TC");
				}
			}
			else if( CompanySettings.checkProdServer() == 6 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD6_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD6_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("PROD6_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD6_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD6_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD6_TC");
				}
			}
			else if( CompanySettings.checkProdServer() == 7 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD7_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD7_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("PROD7_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD7_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD7_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD7_TC");
				}
			}
			else if( CompanySettings.checkProdServer() == 8 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD8_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD8_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("PROD8_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD8_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD8_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD8_TC");
				}
			}
			else if( CompanySettings.checkProdServer() == 9 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD9_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD9_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("PROD9_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD9_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD9_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD9_TC");
				}
			}
			else if( CompanySettings.checkProdServer() == 10 )
			{
				if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("PROD10_EN"))
				{
					url = app.hmLostPwdUrls.get("PROD10_EN");
				}
				else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("PROD10_SC"))
				{
					url = app.hmLostPwdUrls.get("PROD10_SC");
				}
				else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("PROD10_TC"))
				{
					url = app.hmLostPwdUrls.get("PROD10_TC");
				}
			}
		}
		else
		{
			if( getLanguage() == Locale.ENGLISH && app.hmLostPwdUrls.containsKey("DEMO_EN"))
			{
				url = app.hmLostPwdUrls.get("DEMO_EN");
			}
			else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmLostPwdUrls.containsKey("DEMO_SC"))
			{
				url = app.hmLostPwdUrls.get("DEMO_SC");
			}
			else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmLostPwdUrls.containsKey("DEMO_TC"))
			{
				url = app.hmLostPwdUrls.get("DEMO_TC");
			}
		}
	
		if (myWebView == null)
			myWebView = (WebView) findViewById(R.id.wvReport);				
		myWebView.getSettings().setJavaScriptEnabled(true);
		
		myWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(LostPasswordActivity.this, CompanySettings.alertDialogTheme);

				builder.setTitle(res.getText(R.string.ssl));
				builder.setMessage(res.getText(R.string.ssl_continue));
				builder.setPositiveButton(res.getText(R.string.btn_ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.proceed();
					}
				});
				builder.setNegativeButton(res.getText(R.string.btn_cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.cancel();
					}
				});
				final AlertDialog dialog = builder.create();
				dialog.show();
			}
			
			@Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
	        }
			
			ProgressDialog progressBar = ProgressDialog.show(LostPasswordActivity.this, null, res.getText(R.string.lb_loading));
			
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
		
		myWebView.loadUrl(url);
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
		return ServiceFunction.SRV_MOVE_TO_LOST_PASSWORD;
	}			

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_LOST_PASSWORD;
	}	
	
	public boolean isLostPasswordActivity(){
		return true;
	}
	
}