package com.mfinance.everjoy.app;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;
import com.mfinance.everjoy.R;

public class PopupTime {
	//private Context context;
	public CustomPopupWindow popup;
	public WheelView[] wvTimes = new WheelView[2];
	public Button btnCommit;
	public Button btnClose;
	ArrayList<ArrayList<String>> alSeconds = new ArrayList<ArrayList<String>>();	
	
	public PopupTime(Context context, View vParent){
		//this.context = context;
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_seconds);
		
		wvTimes[0] = (WheelView) popup.findViewById(R.id.p1);
		wvTimes[1] = (WheelView) popup.findViewById(R.id.p2);
		
		GUIUtility.initWheel(context, wvTimes[0], GUIUtility.SECONDS.get(0), 5, 0);
		GUIUtility.initWheel(context, wvTimes[1], GUIUtility.SECONDS.get(1), 5, 0);
		
		btnCommit = (Button)popup.findViewById(R.id.btnPopCommit);
		btnCommit.setText(R.string.btn_submit);
		btnClose = (Button)popup.findViewById(R.id.btnClose);
	}
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}
	public void dismiss(){
		popup.dismiss();
	}
	
	public String getValue(){
		StringBuilder sb = new StringBuilder();
		
		if(wvTimes[0].getCurrentItem() != 0)
			sb.append(GUIUtility.SECONDS.get(0).get(wvTimes[0].getCurrentItem()));
		if(wvTimes[0].getCurrentItem() == 6)
			sb.append("0");
		else
			sb.append(GUIUtility.SECONDS.get(1).get(wvTimes[1].getCurrentItem()));
		if ("0".equals(sb.toString()))
			return "1";
		else
			return sb.toString();
	}

}
