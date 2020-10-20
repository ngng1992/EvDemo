package com.mfinance.everjoy.app;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;

import java.util.Locale;



public class DemoRegistrationActivity extends BaseActivity{
	WebView myWebView;
	Button btnClose;


	@Override
	public void bindEvent() {

		String url = "";
		if( getLanguage() == Locale.ENGLISH && app.hmDemoRegistrationUrls.containsKey("EN"))
		{
			url = app.hmDemoRegistrationUrls.get("EN");
			
		}
		else if( getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmDemoRegistrationUrls.containsKey("SC"))
		{
			url = app.hmDemoRegistrationUrls.get("SC");
		}
		else if( getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmDemoRegistrationUrls.containsKey("TC"))
		{
			url = app.hmDemoRegistrationUrls.get("TC");
		}
	
		if (myWebView == null)
			myWebView = (WebView) findViewById(R.id.wvReport);				
		myWebView.getSettings().setJavaScriptEnabled(true);
		
		myWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(DemoRegistrationActivity.this, CompanySettings.alertDialogTheme);

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
			
			ProgressDialog progressBar = ProgressDialog.show(DemoRegistrationActivity.this, null, res.getText(R.string.lb_loading));
			
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
		return ServiceFunction.SRV_MOVE_TO_DEMO_REGISTRATION;
	}			

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_DEMO_REGISTRATION;
	}	
	
	public boolean isDemoRegistrationActivity(){
		return true;
	}
	
}