package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;
import com.mfinance.everjoy.app.widget.wheel.adapter.ArrayWheelAdapter;

public class PopupContract {
	public CustomPopupWindow popup;
	public WheelView wvContract;
	public Button btnCommit;
	public Button btnClose;
	List<String> alName;
	
	Context context;
	
	public PopupContract(Context context, View vParent, List<ContractObj> alContract){
		this.context=context;
		
		alName = getContractName(alContract);
		
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_contract);
		
		wvContract = (WheelView) popup.findViewById(R.id.wvContract);
		GUIUtility.initWheel(context, wvContract, alName, 5, 0);
		
		btnCommit = (Button)popup.findViewById(R.id.btnPopCommit);
		btnClose = (Button)popup.findViewById(R.id.btnClose);
	}
	
	public ArrayList<String> getContractName(ArrayList<ContractObj> alContract){
		Locale l = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context));
		ArrayList<String> alName = new ArrayList<String>();
		for(ContractObj contract: alContract){
			alName.add(contract.getContractName(l));
		}
		return alName;
	}
	
	public void showLikeQuickAction(){		
		popup.showLikeQuickAction();	
	}
	public void dismiss(){
		popup.dismiss();
	}
	
	public int getSelectedIndex()
	{
		return wvContract.getCurrentItem();
	}
	
	public String getValue(){
		if (alName.size() > 0)
			return alName.get(wvContract.getCurrentItem());
		else
			return null;		
	}
	
	public void updateSelectedContract(ArrayList<ContractObj> alContract, String sContract){
		if(alContract != null){
			ArrayList<ContractObj> alViewableContract = new ArrayList<ContractObj>();
			Iterator<ContractObj> itC = alContract.iterator();

			while(itC.hasNext()){
				ContractObj c = itC.next();
				if(c.isViewable()){
					alViewableContract.add(c);
				}
			}                    
			alName.clear();
			alName = null;
			alName = getContractName(alViewableContract);                 
			((ArrayWheelAdapter) wvContract.getViewAdapter()).updateItems(alViewableContract.toArray(new String[alViewableContract.size()]));
		}                          
		wvContract.setCurrentItem(alName.indexOf(sContract));         
	}

	public ArrayList<String> getContractName(List<ContractObj> alContract){
		Locale l = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context));
		ArrayList<String> alName = new ArrayList<String>();
		for(ContractObj contract: alContract){
			alName.add(contract.getContractName(l));
		}
		return alName;
	}
	
	public void reload(List<ContractObj> alPrice){
		alName = getContractName(alPrice);
		GUIUtility.initWheel(context, wvContract, alName, 5, 0);
	}
}
