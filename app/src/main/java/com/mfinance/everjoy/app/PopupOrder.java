package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Messenger;
import android.view.View;
import android.widget.Button;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;

public class PopupOrder {
	private Context context;
	public CustomPopupWindow popup;
	public WheelView wvOrder;
	public Button btnCommit;
	public Button btnClose;	
	public OrderWheelAdapter orderAdapter = null;
	
	public PopupOrder(Context context, View vParent, OrderWheelAdapter orderAdapter){
		this.context = context;
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_order);
		
		wvOrder = (WheelView) popup.findViewById(R.id.wvOrder);
		
		this.orderAdapter = orderAdapter;		
		wvOrder.setViewAdapter(orderAdapter);
		wvOrder.setVisibleItems(5);
		wvOrder.setCurrentItem(0);
		
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
		return (String) orderAdapter.getItemText(wvOrder.getCurrentItem());
	}
	
	public String getRealValue(){
		return (String) orderAdapter.getOrderRef(wvOrder.getCurrentItem());
	}
	
	public void updateSelectedOrderIndex(HashMap<Integer, OrderRecord> record, int iRef){
		orderAdapter.reload(record);
		
		if(-1 == iRef){
			wvOrder.setCurrentItem(0);
		}else{
			wvOrder.setCurrentItem(orderAdapter.getItemIndex(iRef));	
		}
	}	
}
