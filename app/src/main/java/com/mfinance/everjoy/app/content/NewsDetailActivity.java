package com.mfinance.everjoy.app.content;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.webkit.WebView;
import android.widget.TextView;

import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.News;

public class NewsDetailActivity extends DetailBaseActivity{
	
	private Bundle extras = null;
	private News news = null;
	
	@Override
	public int[] getTitleText() {
		return new int[]{R.id.tvTitle, R.string.db_realtime_news};
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
		//if (dayplan == null || !dayplan.equals(((MobileTraderApplication)app).getSelectedDayplan())){
			if (news == null || !news.equals(((MobileTraderApplication)app).getSelectedNews())){
				news = ((MobileTraderApplication)app).getSelectedNews();
				
				((TextView)vHeader.findViewById(R.id.lbH21)).setText(news.getDatetime());
				
				StringBuffer sbHtml = new StringBuffer();
				sbHtml.append("<body style=\"background-color: "+res.getString(R.string.wv_bg_color)+"; color: "+res.getString(R.string.wv_font_color)+"; font-family: Helvetica; font-size: 12pt; word-wrap: break-word \">");
				if (Utility.isSimplifiedChineses()){
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(news.getTitleGB());
					sbHtml.append(Utility.convertHtmlSpecChar(news.getContentGB()));
				}
				else if (Utility.isTraditionalChinese()){
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(news.getTitleBig5());
					sbHtml.append(Utility.convertHtmlSpecChar(news.getContentBig5()));
				}
				else {
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(news.getTitleEN());
					sbHtml.append(Utility.convertHtmlSpecChar(news.getContentEN()));
				}
				
				sbHtml.append("</p>")
					.append("</body>");
				((WebView)findViewById(R.id.wvContent)).loadDataWithBaseURL(null, sbHtml.toString(), mimetype, encoding, null);
				
				if (MobileTraderApplication.isNeedFontBold){
					((TextView)vHeader.findViewById(R.id.lbH11)).setTextColor(getResources().getColor(R.color.detail_title_bold));
					((TextView)vHeader.findViewById(R.id.lbH21)).setTextColor(getResources().getColor(R.color.detail_title_bold));
					((TextView)vHeader.findViewById(R.id.lbH11)).setTypeface(null, Typeface.BOLD);
					((TextView)vHeader.findViewById(R.id.lbH21)).setTypeface(null, Typeface.BOLD);
					((TextView)vHeader.findViewById(R.id.lbH11)).getPaint().setFakeBoldText(true);
					((TextView)vHeader.findViewById(R.id.lbH21)).getPaint().setFakeBoldText(true);
				}				
			
			}
		
	}

	@Override
	public int getHeaderId() {
		return R.id.llHeader;
	}

	@Override
	public void onResume(){
		super.onResume();
		
	}

	@Override
	public int getServiceId(){
		return ServiceFunction.SRV_MOVE_TO_NEWS_LIST;
	}
	
	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_NEWS_DETAIL;
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
