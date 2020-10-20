package com.mfinance.everjoy.app;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;

import java.util.ArrayList;

public class PopupAlertRate {
	private Context context;
	public CustomPopupWindow popup;
	public WheelView[] wvRate = new WheelView[3];
	public Button btnCommit;
	public Button btnClose;
	ArrayList<ArrayList<String>> alRate = new ArrayList<ArrayList<String>>();

	public PopupAlertRate(Context context, View vParent){
		this.context = context;
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_rate);
		
		wvRate[0] = (WheelView) popup.findViewById(R.id.p1);
		wvRate[1] = (WheelView) popup.findViewById(R.id.p2);
		wvRate[2] = (WheelView) popup.findViewById(R.id.p3);	
		
		GUIUtility.initWheel(context, wvRate[0], GUIUtility.LOT.get(0), 5, 0);
		GUIUtility.initWheel(context, wvRate[1], GUIUtility.LOT.get(1), 5, 0);
		GUIUtility.initWheel(context, wvRate[2], GUIUtility.LOT.get(2), 5, 1);
		
		btnCommit = (Button)popup.findViewById(R.id.btnPopCommit);
		btnClose = (Button)popup.findViewById(R.id.btnClose);
	}
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}
	public void dismiss(){
		popup.dismiss();
	}
	
	public void upateRate(String sRate, String sRefRate){
		for(ArrayList<String>alTmp : alRate){
			alTmp.clear();
		}
		alRate.clear();		
		alRate = GUIUtility.generateAlertRate(sRefRate, 3);
		GUIUtility.updateRateWheel(context,wvRate,alRate,sRate);
	}
	
	public String getValue(){
		StringBuilder sb = new StringBuilder();
		sb.append(alRate.get(0).get(wvRate[0].getCurrentItem())).append(alRate.get(1).get(wvRate[1].getCurrentItem())).append(alRate.get(2).get(wvRate[2].getCurrentItem()));
		return sb.toString();
		
		
	}
	
}
