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
import com.mfinance.everjoy.hungkee.xml.Economicdata;

public class EconomicDataDetailActivity extends DetailBaseActivity{
	
	private Bundle extras = null;
	private Economicdata economicdata = null;
	
	@Override
	public int[] getTitleText() {
		return new int[]{R.id.tvTitle, R.string.db_economic_data};
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
			if (economicdata == null || !economicdata.equals((app).getSelectedEconomicdata())){
				economicdata = (app).getSelectedEconomicdata();			
				
				((TextView)vHeader.findViewById(R.id.lbH21)).setText(economicdata.getDate());
				
				StringBuffer sbHtml = new StringBuffer();
				String sCountry = "";
				String sUnit = "";
				String sDesc = "";
				
				if (Utility.isSimplifiedChineses()) {
					sCountry = economicdata.getCountryGB();
					sDesc = economicdata.getDescriptionGB();
					sUnit =  economicdata.getUnitGB();
					String strTemp = sCountry + " " + economicdata.getNameGB();
					strTemp = strTemp.replaceAll("&amp;", "&"); 
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(strTemp);
					
				} else if (Utility.isTraditionalChinese()) {
					sCountry = economicdata.getCountryBig5();
					sDesc = economicdata.getDescriptionBig5();
					sUnit =  economicdata.getUnitBig5();
					String strTemp = sCountry + " " +economicdata.getNameBig5();
					strTemp = strTemp.replaceAll("&amp;", "&"); 
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(strTemp);
				} else {
					sCountry = economicdata.getCountryEN();
					sDesc = economicdata.getDescriptionEN();
					sUnit =  economicdata.getUnitEN();
					String strTemp = sCountry + " " +economicdata.getNameEN();
					strTemp = strTemp.replaceAll("&amp;", "&"); 
					((TextView)vHeader.findViewById(R.id.lbH11)).setText(strTemp);
				}			
				
				String fColor = economicdata.getForecastValue().startsWith("-")? "red":"green";
				String aColor = economicdata.getActualValue().startsWith("-")? "red":"green";
				
				String fValue = "<font color='"+fColor+"'>"+ economicdata.getForecastValue()+ ("".equals(economicdata.getForecastValue())?"" : sUnit) +"</font>";
				String aValue = "<font color='"+aColor+"'>"+ economicdata.getActualValue()+("".equals(economicdata.getActualValue())?"" : sUnit) +"</font>";			
				
				sbHtml.append("<body style=\"background-color: "+res.getString(R.string.wv_bg_color)+"; color: "+res.getString(R.string.wv_font_color)+"; font-family: Helvetica; font-size: 12pt; word-wrap: break-word \">");
				sbHtml.append("<p>")
					.append(res.getText(R.string.lb_release_time)).append(":")
					.append(economicdata.getTime())
					.append("</p>")
					.append("<p>")
					.append(res.getText(R.string.lb_country)).append(":")
					.append(sCountry)
					.append("</p>")
					.append("<p>")
					.append(sDesc)
					.append("</p>")
					.append("<p>")
					.append(res.getText(R.string.lb_before_value)).append(":")
					.append(economicdata.getPrevValue()).append(sUnit)
					.append("</p>")
					.append("<p>")
					.append(res.getText(R.string.lb_forecast_value)).append(":")
					.append(fValue)
					.append("</p>")
					.append("<p>")
					.append(res.getText(R.string.lb_actual_value)).append(":")
					.append(aValue)
					.append("</p>");
					
					
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
		return ServiceFunction.SRV_MOVE_TO_ECONOMIC_DATA_LIST;
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
