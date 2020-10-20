package com.mfinance.everjoy.app;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.R;

public class PopupWeb {
	Context context;
	public CustomPopupWindow popup;	
	public Button btnClose;
	WebView web;
	
	public PopupWeb(Context context, View vParent){
		this.context = context;
		popup = new CustomPopupWindow(vParent);			
		popup.setContentView(R.layout.popup_web);
		web = (WebView) popup.findViewById(R.id.webView);

		btnClose = (Button)popup.findViewById(R.id.btnClose);		
		bindEvent();
	}
	
	public void bindEvent(){
	}
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}
	public void dismiss(){
		popup.dismiss();
	}

	public void updateURL(String sURL){		
		web.loadUrl(sURL);
	}

}
