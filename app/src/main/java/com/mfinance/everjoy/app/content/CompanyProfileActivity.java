package com.mfinance.everjoy.app.content;

import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.CompanyProfile;

import android.graphics.Typeface;
import android.os.Message;
import android.webkit.WebView;
import android.widget.TextView;

public class CompanyProfileActivity extends DetailBaseActivity {

	@Override
	public int[] getTitleText() {
		return new int[]{R.id.tvTitle, R.string.db_company_profile};
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
		return ServiceFunction.SRV_MOVE_TO_COMPANY_PROFILE;
	}

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_COMPANY_PROFILE;
	}

	@Override
	public boolean isLoadedData() {
		return (app.data.getCompanyProfile() != null);
	}

	@Override
	public int getLoadingViewId() {
		return R.id.flLoading;
	}

	@Override
	public void loadUIData() {
		if (app.data.getCompanyProfile() != null) {
			CompanyProfile cp = app.data.getCompanyProfile();
			String html = "<body style=\"background-color: "
					+ res.getString(R.string.wv_bg_color)
					+ "; color: "
					+ res.getString(R.string.wv_font_color)
					+ "; font-family: Helvetica; font-size: 12pt; word-wrap: break-word \">";
			if(vHeader!=null)
			((TextView) vHeader.findViewById(R.id.lbH11)).setText(res
					.getString(R.string.db_company_profile));

			if (Utility.isSimplifiedChineses()) {
				html += cp.getAboutGB();
			} else if (Utility.isTraditionalChinese()) {
				html += cp.getAboutBig5();
			} else {
				html += cp.getAboutEN();
			}

			((WebView) findViewById(R.id.wvContent)).loadDataWithBaseURL(null,
					html, mimetype, encoding, null);

			if (MobileTraderApplication.isNeedFontBold&&vHeader!=null) {
				((TextView) vHeader.findViewById(R.id.lbH11))
						.setTextColor(getResources().getColor(
								R.color.detail_title_bold));
				((TextView) vHeader.findViewById(R.id.lbH11)).setTypeface(null,
						Typeface.BOLD);
				((TextView) vHeader.findViewById(R.id.lbH11)).getPaint()
						.setFakeBoldText(true);
			}

			html = null;
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
