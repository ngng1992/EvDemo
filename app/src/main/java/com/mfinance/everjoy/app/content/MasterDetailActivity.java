package com.mfinance.everjoy.app.content;

import java.util.Locale;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Master;

public class MasterDetailActivity extends DetailBaseActivity{
	
	private Bundle extras = null;
	private Master master = null;
	private String html = null;

	@Override
	public int[] getTitleText() {
		return new int[]{R.id.tvTitle, R.string.db_master};
	}

	@Override
	public int getHeaderId() {
		if( CompanySettings.ENABLE_WEBVIEW_MASTER == true )
			return -1;
		else
			return R.id.llHeader;
	}

	@Override
	public int getContentTemplateId() {
		return R.layout.d_t3;
	}
	
	@Override
	public int getHeaderTemplateId() {
		if( CompanySettings.ENABLE_WEBVIEW_MASTER == true )
			return -1;
		else
			return R.layout.h_t11;
	}	
	
	@Override
	public void bindEvent() {
	}

	@Override
	public void loadUIData(){
		if( CompanySettings.ENABLE_WEBVIEW_MASTER == true )
		{
			WebView myWebView = (WebView)findViewById(R.id.wvContent);
			if( getLanguage() == Locale.ENGLISH )
			{
				myWebView.loadUrl(app.hmMasterUrls.get("EN"), null);	
			}
			else if( getLanguage() == Locale.TRADITIONAL_CHINESE )
			{
				myWebView.loadUrl(app.hmMasterUrls.get("TC"), null);	
			}
			else if( getLanguage() == Locale.SIMPLIFIED_CHINESE )
			{
				myWebView.loadUrl(app.hmMasterUrls.get("SC"), null);	
			}
			
			myWebView.setWebViewClient(new WebViewClient());
			WebSettings webSettings = myWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
		}
	}
	
	@Override
	public void updateUI() {
		
		if( CompanySettings.ENABLE_WEBVIEW_MASTER == false )
		{
			extras = getIntent().getExtras();
			
			if (extras != null) {
				if (master == null || !master.equals((app).getSelectedMaster())){
					master = (app).getSelectedMaster();
					
					html = "<body style=\"background-color: "+res.getString(R.string.wv_bg_color)+"; color: "+res.getString(R.string.wv_font_color)+"; font-family: Helvetica; font-size: 12pt; word-wrap: break-word \">";
					((TextView)vHeader.findViewById(R.id.lbH21)).setText(master.getDate());
					if (MobileTraderApplication.isNeedFontBold)
						((TextView)vHeader.findViewById(R.id.lbH21)).setTypeface(null, Typeface.BOLD); 				
					if (Utility.isSimplifiedChineses()) {
						((TextView)vHeader.findViewById(R.id.lbH11)).setText(master.getTitleGB());
						html = html +  Utility.convertHtmlSpecChar(master.getContentGB()) + "</body>";
					} else if (Utility.isTraditionalChinese()) {
						((TextView)vHeader.findViewById(R.id.lbH11)).setText(master.getTitleBig5());
						html = html +  Utility.convertHtmlSpecChar(master.getContentBig5()) + "</body>";
					} else {
						((TextView)vHeader.findViewById(R.id.lbH11)).setText(master.getTitleEN());
						html = html +  Utility.convertHtmlSpecChar(master.getContentEN()) + "</body>";
					}
					
					if (MobileTraderApplication.isNeedFontBold){
						((TextView)vHeader.findViewById(R.id.lbH11)).setTextColor(getResources().getColor(R.color.detail_title_bold));
						((TextView)vHeader.findViewById(R.id.lbH21)).setTextColor(getResources().getColor(R.color.detail_title_bold));
						((TextView)vHeader.findViewById(R.id.lbH11)).setTypeface(null, Typeface.BOLD);
						((TextView)vHeader.findViewById(R.id.lbH21)).setTypeface(null, Typeface.BOLD);
						((TextView)vHeader.findViewById(R.id.lbH11)).getPaint().setFakeBoldText(true);
						((TextView)vHeader.findViewById(R.id.lbH21)).getPaint().setFakeBoldText(true);
					}
					
					((WebView)findViewById(R.id.wvContent)).loadDataWithBaseURL(null, html, mimetype, encoding, null);
				}
			}
		}

		
	}
	
	@Override
	public void onResume(){
		if( CompanySettings.ENABLE_WEBVIEW_MASTER == true )
			loadUIData();
		super.onResume();
	}
	
	@Override
	public int getServiceId(){
		return ServiceFunction.SRV_MOVE_TO_MASTER_LIST;
	}

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_MASTER_DETAIL;
	}
	
	@Override
	public void handleByChild(Message msg) {
		// TODO Auto-generated method stub
	}

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
}
