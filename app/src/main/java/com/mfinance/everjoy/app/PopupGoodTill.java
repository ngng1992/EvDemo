package com.mfinance.everjoy.app;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;

public class PopupGoodTill {

	public CustomPopupWindow popup;
	public WheelView wvGoodTill;
	public Button btnCommit;
	public boolean isStopOrder; // true - stop, false - limit
	public Button btnClose;	
	ArrayList<String> alName;
	
	Context context;
	
	public PopupGoodTill(Context context, View vParent, ArrayList<String> alGoodTill){
		this.context=context;
		
		alName = alGoodTill;
		
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_contract);
		
		wvGoodTill = (WheelView) popup.findViewById(R.id.wvContract);
		GUIUtility.initWheel(context, wvGoodTill, alName, 5, 0);
		
		btnCommit = (Button)popup.findViewById(R.id.btnPopCommit);
		btnClose = (Button)popup.findViewById(R.id.btnClose);
	}
	
	public void showLikeQuickAction(){		
		popup.showLikeQuickAction();	
	}
	public void dismiss(){
		popup.dismiss();
	}
	
	public int getSelectedIndex()
	{
		return wvGoodTill.getCurrentItem();
	}
	
	public String getValue(){
		if (alName.size() > 0)
			return alName.get(wvGoodTill.getCurrentItem());
		else
			return null;		
	}
	
	public void updateSelectedGoodTill(String sContract){
		wvGoodTill.setCurrentItem(alName.indexOf(sContract));         
	}

	public void reload(ArrayList<String> alGoodTill){
		alName = alGoodTill;
		GUIUtility.initWheel(context, wvGoodTill, alName, 5, 0);
	}
}
