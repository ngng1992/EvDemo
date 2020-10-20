package com.mfinance.everjoy.app.content;

import java.util.Locale;
import java.util.ResourceBundle;

import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Hourproduct;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.webkit.WebView;
import android.widget.TextView;

public class HourProductDetailActivity extends DetailBaseActivity{
	
	private Bundle extras = null;
	private Hourproduct hourproduct = null;
	
	@Override
	public int[] getTitleText() {
		return new int[]{R.id.tvTitle, R.string.db_hour_product};
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
		extras = getIntent().getExtras();
		
		if (extras != null) {
			if (hourproduct == null || !hourproduct.equals((app).getSelectedHourproduct())){
				hourproduct = (app).getSelectedHourproduct();			
				
				((TextView)vHeader.findViewById(R.id.lbH21)).setText(hourproduct.getSubTitle());
				
				
				StringBuffer sbHtml = new StringBuffer();
				
				if ( (app).getSelectedHourproduct()!=null){
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(hourproduct.getTitle());
				}
				
				sbHtml.append(hourproduct.getContent(res));
					
					
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
		return ServiceFunction.SRV_MOVE_TO_HOUR_PRODUCT;
	}	
	
	@Override
	public int getActivityServiceCode() {
		return -1;
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
