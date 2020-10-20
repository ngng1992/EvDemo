package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Messenger;
import android.view.View;
import android.widget.Button;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;
import com.mfinance.everjoy.app.widget.wheel.adapter.ArrayWheelAdapter;

public class PopupString {
	public CustomPopupWindow popup;
	public WheelView wvString;
	public Button btnCommit;
	public Button btnClose;	
	List<String> alValue;
	
	private static final int[] SHADOWS_COLORS = new int[] { 0xFF494848,
		0x00AAAAAA, 0x00AAAAAA };	
	
	public PopupString(Context context, View vParent, List<String> alValue){
		this.alValue = alValue;
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_string);
		
		wvString = (WheelView) popup.findViewById(R.id.wvContract);
		GUIUtility.initWheel(context, wvString, alValue, 5, 0, SHADOWS_COLORS);
		
		btnCommit = (Button)popup.findViewById(R.id.btnPopCommit);
		btnClose = (Button)popup.findViewById(R.id.btnClose);
	}
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}
	public void dismiss(){
		popup.dismiss();
	}
	
	public String getValue(){
		return alValue.get(wvString.getCurrentItem());		
	}	
	
	public void setSelected(String sValue){			
		wvString.setCurrentItem(getIndex(sValue));		
	}
	
	public int getIndex(String sValue){
		return alValue.indexOf(sValue);
	}
	
	public int getSize(){
		return alValue.size();
	}
}
