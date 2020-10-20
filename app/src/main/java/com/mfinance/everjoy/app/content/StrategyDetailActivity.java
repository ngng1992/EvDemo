package com.mfinance.everjoy.app.content;

import android.graphics.Typeface;
import android.os.Message;
import android.webkit.WebView;
import android.widget.TextView;

import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Strategy;

public class StrategyDetailActivity extends DetailBaseActivity {

	private Strategy strategy = null;
	private String html = null;

	@Override
	public void loadLayout() {
		super.loadLayout();

		strategy = ((MobileTraderApplication) app).getSelectedStrategy();
		((WebView) findViewById(R.id.wvContent)).setFocusable(false);
		((WebView) findViewById(R.id.wvContent)).setFocusableInTouchMode(false);

		strategy = null;
	}

	@Override
	public void bindEvent() {
	}

	@Override
	public void updateUI() {

		if (strategy == null
				|| !strategy.equals(((MobileTraderApplication) app)
						.getSelectedStrategy())) {
			strategy = ((MobileTraderApplication) app).getSelectedStrategy();

			String content = null;
			if (Utility.isSimplifiedChineses()) {
				((TextView) vHeader.findViewById(R.id.lbH11))
						.setText(strategy.caption_cn);
				content = strategy.content_cn.replaceAll("&lt;br&gt;", "<br>");
			} else if (Utility.isTraditionalChinese()) {
				((TextView) vHeader.findViewById(R.id.lbH11))
						.setText(strategy.caption_zh);
				content = strategy.content_zh.replaceAll("&lt;br&gt;", "<br>");
			} else {
				((TextView) vHeader.findViewById(R.id.lbH11))
						.setText(strategy.caption_en);
				content = strategy.content_en.replaceAll("&lt;br&gt;", "<br>");
			}
			
			content = content.replaceAll("&lt;br&gt;", "<br>");

			content = content == null ? "" : content;
			String phone = strategy.photo1 == null ? "" : strategy.photo1;
			html = String
					.format("<body style='background-color: %s; color: %s; font-family: Helvetica; font-size: 12pt; word-wrap: break-word '>%s<br/><img src='%s'></img></body>",
							res.getString(R.string.wv_bg_color),
							res.getString(R.string.wv_font_color), content,
							phone);
			((TextView) vHeader.findViewById(R.id.lbH21))
					.setText(strategy.issueDate);

			//System.out.println("html:" + html);
			((WebView) findViewById(R.id.wvContent)).loadDataWithBaseURL(null,
					html, mimetype, encoding, null);

			if (MobileTraderApplication.isNeedFontBold) {
				((TextView) vHeader.findViewById(R.id.lbH11))
						.setTextColor(getResources().getColor(
								R.color.detail_title_bold));
				((TextView) vHeader.findViewById(R.id.lbH21))
						.setTextColor(getResources().getColor(
								R.color.detail_title_bold));
				((TextView) vHeader.findViewById(R.id.lbH11)).setTypeface(null,
						Typeface.BOLD);
				((TextView) vHeader.findViewById(R.id.lbH21)).setTypeface(null,
						Typeface.BOLD);
				((TextView) vHeader.findViewById(R.id.lbH11)).getPaint()
						.setFakeBoldText(true);
				((TextView) vHeader.findViewById(R.id.lbH21)).getPaint()
						.setFakeBoldText(true);
			}
		}
	}

	@Override
	public int getHeaderId() {
		return R.id.llHeader;
	}

	@Override
	public int getHeaderTemplateId() {
		return R.layout.h_t10;
	}

	@Override
	public int getContentTemplateId() {
		return R.layout.d_t4;

	}


	@Override
	public int getServiceId() {
		return ServiceFunction.SRV_MOVE_TO_STRATEGY_LIST;
	}

	@Override
	public int[] getTitleText() {
		return new int[] { R.id.tvTitle, R.string.db_day_plan };
	}

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_STRATEGY_DETAIL;
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
