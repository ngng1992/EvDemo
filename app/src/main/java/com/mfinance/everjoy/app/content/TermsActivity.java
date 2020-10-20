package com.mfinance.everjoy.app.content;

import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Other;

import android.graphics.Typeface;
import android.os.Message;
import android.webkit.WebView;
import android.widget.TextView;

public class TermsActivity extends DetailBaseActivity{

	@Override
	public int[] getTitleText() {
		return new int[]{R.id.tvTitle, R.string.db_terms};
	}

	@Override
	public int getHeaderId() {
		return -1;
	}

	@Override
	public int getHeaderTemplateId() {
		return -1;
	}

	@Override
	public int getContentTemplateId() {
		return R.layout.d_t3;
	}

	@Override
	public void bindEvent() {

	}

	@Override
	public void updateUI() {

	}

	@Override
	public void loadLayout() {
		super.loadLayout();
	}

	@Override
	public int getServiceId() {
		return ServiceFunction.SRV_MOVE_TO_TERMS;
	}

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_TERMS;
	}

	@Override
	public boolean isLoadedData() {
		return (app.data.getOther() != null);
	}

	@Override
	public int getLoadingViewId() {
		return R.id.flLoading;
	}
	
	
	@Override
	public void loadUIData(){
		Other other = app.data.getOther();
		if( other != null )
		{
			String html = "<body style=\"background-color: "+res.getString(R.string.wv_bg_color)+"; color: "+res.getString(R.string.wv_font_color)+"; font-family: Helvetica; font-size: 12pt; word-wrap: break-word \">";
			//((TextView)vHeader.findViewById(R.id.lbH11)).setText(res.getString(R.string.db_terms));
			
			if (Utility.isSimplifiedChineses()) {
				html = html + "<p>"+other.getDisclaimerBG()+"</p>";
			} else if  (Utility.isTraditionalChinese()) {
				html = html + "<p>"+other.getDisclaimerBig5()+"</p>";
			} else {
				html = html + "<p>"+other.getDisclaimerEN()+"</p>";
			}
			html = html +"</body>";
			((WebView)findViewById(R.id.wvContent)).loadDataWithBaseURL(null, html, mimetype, encoding, null);
			/*
			if (MobileTraderApplication.isNeedFontBold){
				((TextView)vHeader.findViewById(R.id.lbH11)).setTextColor(getResources().getColor(R.color.detail_title_bold));
				((TextView)vHeader.findViewById(R.id.lbH11)).setTypeface(null, Typeface.BOLD);
				((TextView)vHeader.findViewById(R.id.lbH11)).getPaint().setFakeBoldText(true);
			}
			*/
		}
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
