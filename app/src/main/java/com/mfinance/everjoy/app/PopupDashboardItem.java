package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;
import com.mfinance.everjoy.app.widget.wheel.adapter.ArrayWheelAdapter;

public class PopupDashboardItem {
	public Resources res;
	
	public CustomPopupWindow popup;
	public WheelView wvContract;
	public Button btnCommit;
	public Button btnClose;	
	ArrayList<String> alName;
	
	public PopupDashboardItem(Context context, View vParent, ArrayList<DashboardItem> alDashboardItem){
		res = context.getResources();
		
		alName = getItemName(alDashboardItem);
		
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_contract);
		
		wvContract = (WheelView) popup.findViewById(R.id.wvContract);
		GUIUtility.initWheel(context, wvContract, alName, 5, 0);
		
		btnCommit = (Button)popup.findViewById(R.id.btnPopCommit);
		btnCommit.setText(R.string.btn_submit);
		
		btnClose = (Button)popup.findViewById(R.id.btnClose);
	}
	
	public ArrayList<String> getItemName(ArrayList<DashboardItem> alDashboardItem){
		ArrayList<String> alName = new ArrayList<String>();
		for(DashboardItem item: alDashboardItem){
				alName.add((String) (res.getText(item.iNavDesc)));
		}
		return alName;
	}
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}
	public void dismiss(){
		popup.dismiss();
	}
	
	public String getValue(){
		return alName.get(wvContract.getCurrentItem());		
	}
	
	public void updateSelectedItem(ArrayList<DashboardItem> alDashboardItem, String sItem){
		if(alDashboardItem != null){
			alName.clear();
			alName = null;
			alName = getItemName(alDashboardItem);
			((ArrayWheelAdapter) wvContract.getViewAdapter()).updateItems(alDashboardItem.toArray(new String[alDashboardItem.size()]));
		}				
		wvContract.setCurrentItem(alName.indexOf(sItem));		
	}	
}
