package com.mfinance.everjoy.app;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.Button;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;

public class PopupOpenPosition {
	private Context context;
	public CustomPopupWindow popup;
	public WheelView wvPosition;
	public Button btnCommit;
	public Button btnClose;	
	public OpenPositionWheelAdapter positionAdapter = null;
	
	public PopupOpenPosition(Context context, View vParent, OpenPositionWheelAdapter positionAdapter){
		this.context = context;
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_open_position);
		
		wvPosition = (WheelView) popup.findViewById(R.id.wvPosition);
		
		this.positionAdapter = positionAdapter;		
		wvPosition.setViewAdapter(positionAdapter);
		wvPosition.setVisibleItems(5);
		wvPosition.setCurrentItem(0);
		
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
		return (String) positionAdapter.getItemText(wvPosition.getCurrentItem());
	}

	public void updateSelectedPositionIndex(Map<Integer, OpenPositionRecord> record, int iRef){
		positionAdapter.reload(record);
		
		if(-1 == iRef){
			wvPosition.setCurrentItem(0);
		}else{
			wvPosition.setCurrentItem(positionAdapter.getItemIndex(iRef));	
		}
	}	
}
