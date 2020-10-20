package com.mfinance.everjoy.app;

import android.content.Context;
import android.view.View;
import android.widget.Button;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;

public class PopupSlippage {
	public CustomPopupWindow popup;
	public WheelView[] wvLot = new WheelView[1];
	public Button btnCommit;
	public Button btnClose;
	
	public PopupSlippage(Context context, View vParent){
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_contract);
		
		wvLot[0] = (WheelView) popup.findViewById(R.id.wvContract);
		
		GUIUtility.initWheel(context, wvLot[0], GUIUtility.SLIPPAGE, 5, 0);
		
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
		StringBuilder sb = new StringBuilder();
		sb.append(GUIUtility.SLIPPAGE.get(wvLot[0].getCurrentItem()));
		return sb.toString();
	}
	
	public void setValue(String value){
		try{
			wvLot[0].setCurrentItem(Integer.parseInt(value));
		}catch(Exception e){}
	}
	
}
