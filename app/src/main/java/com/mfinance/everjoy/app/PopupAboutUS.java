package com.mfinance.everjoy.app;

import java.util.Date;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import com.mfinance.everjoy.R;

import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;

public class PopupAboutUS {
	Context context;
	public CustomPopupWindow popup;	
	public Button btnClose;
	
	public PopupAboutUS(Context context, View vParent){
		this.context = context;
		popup = new CustomPopupWindow(vParent);			
		popup.setContentView(R.layout.popup_about_us);

		btnClose = (Button)popup.findViewById(R.id.btnClose);	
		
		try {
			String versionStr = CompanySettings.appVersionStr+ context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName + "." + context.getResources().getString(R.string.release_date) + (CompanySettings.FOR_TEST?"t":"") + (CompanySettings.FOR_UAT?"u":"");
			if(CompanySettings.APP_UPDATE_URL_TYPE != null)
			{
				if(CompanySettings.APP_UPDATE_URL_TYPE.equals("google"))
					versionStr += "go";
				else if(CompanySettings.APP_UPDATE_URL_TYPE.equals("baidu"))
					versionStr += "bd";
				else if(CompanySettings.APP_UPDATE_URL_TYPE.equals("qq"))
					versionStr += "qq";
			}
			
			((TextView)popup.findViewById(R.id.tvVersion)).setText(versionStr);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		bindEvent();
	}
	
	public void bindEvent(){
		btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}
	public void dismiss(){
		popup.dismiss();
	}
}
