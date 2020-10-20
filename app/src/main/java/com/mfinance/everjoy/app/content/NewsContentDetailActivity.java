package com.mfinance.everjoy.app.content;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Newscontent;

public class NewsContentDetailActivity extends DetailBaseActivity{
	
	private Newscontent newscontent = null;
	private Bundle extras = null;

	@Override
	public int[] getTitleText() {
		return new int[]{R.id.tvTitle, R.string.db_news_content};
	}

	@Override
	public int getHeaderId() {
		return R.id.llHeader;
	}

	@Override
	public int getContentTemplateId() {
		return R.layout.d_t3;
	}
	
	@Override
	public int getHeaderTemplateId() {
		return R.layout.h_t11;
	}	

	@Override
	public void bindEvent() {
	}

	@Override
	public void updateUI() {		
		// 
		extras = getIntent().getExtras();
		if (extras != null) {
			
			if (newscontent == null || !newscontent.equals(((MobileTraderApplication)app).getSelectedNewscontent())){
				newscontent = ((MobileTraderApplication)app).getSelectedNewscontent();
				
				((TextView)vHeader.findViewById(R.id.lbH21)).setText(newscontent.getDate());
				
				String html = "<body style=\"background-color: "+res.getString(R.string.wv_bg_color)+"; color: "+res.getString(R.string.wv_font_color)+"; font-family: Helvetica; font-size: 12pt; word-wrap: break-word \">";
				if (Utility.isSimplifiedChineses()) {
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(Html.fromHtml(Utility.convertHtmlSpecChar(newscontent.getTitleGB())));
					html = html +  Utility.convertHtmlSpecChar(newscontent.getContentGB()) + "</body>";
				} else if  (Utility.isTraditionalChinese()) {
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(Html.fromHtml(Utility.convertHtmlSpecChar(newscontent.getTitleBig5())));
					html = html +  Utility.convertHtmlSpecChar(newscontent.getContentBig5()) + "</body>";
				} else {
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(Html.fromHtml(Utility.convertHtmlSpecChar(newscontent.getTitleEN())));
					html = html +  Utility.convertHtmlSpecChar(newscontent.getContentEN()) + "</body>";
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

	
	@Override
	public int getServiceId(){
		return ServiceFunction.SRV_MOVE_TO_NEWS_CONTENT_LIST;
	}

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_NEWS_CONTENT_DETAIL;
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
