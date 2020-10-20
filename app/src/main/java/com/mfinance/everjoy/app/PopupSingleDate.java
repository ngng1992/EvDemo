package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.OnWheelChangedListener;
import com.mfinance.everjoy.app.widget.wheel.WheelView;
import com.mfinance.everjoy.app.widget.wheel.adapter.ArrayWheelAdapter;

public class PopupSingleDate {
	Context context;
	public CustomPopupWindow popup;	
	public WheelView[] wvDate = new WheelView[2];
	public WheelView wvDay;
	Button btnDate;
	String[] years = new String[21];
	String[] months = new String[12];
	ArrayList<String> alDay = new ArrayList<String>();	
	public Button btnCommit;
	public Button btnClose;
	
	public PopupSingleDate(Context context, View vParent){
		this.context = context;
		popup = new CustomPopupWindow(vParent);			
		popup.setContentView(R.layout.popup_single_date);
		
		wvDate[0] = (WheelView) popup.findViewById(R.id.p1);
		wvDate[1] = (WheelView) popup.findViewById(R.id.p2);
		wvDay = (WheelView) popup.findViewById(R.id.p3);
		btnDate = (Button)popup.findViewById(R.id.btnDate);
	
		int iYear = Calendar.getInstance().getTime().getYear() + 1900;
		
		for(int i = 0; i <= 10; i++){
			years[i] = String.valueOf(iYear - 10 + i);
		}
		
		for(int i = 11; i <= 20; i++){
			years[i] = String.valueOf(iYear - 10 + i);
		}	

		for(int i = 0; i < 12; i++){
			months[i] = String.format("%02d", i + 1);
		}
		
		updateDateWheel(wvDate, new String[][]{years, months});
		updateDays(wvDate[0], wvDate[1], wvDay);
		
		btnCommit = (Button)popup.findViewById(R.id.btnOK);
		btnClose = (Button)popup.findViewById(R.id.btnClose);
		
		bindEvent();
	}
	
	public void bindEvent(){
		wvDate[0].addChangingListener(new OnWheelChangedListener(){
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays();
				String sValue = getDate();
				if(sValue != null)
					btnDate.setText(sValue);
			}			
		});
		
		wvDate[1].addChangingListener(new OnWheelChangedListener(){
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays();
				String sValue = getDate();
				if(sValue != null)
					btnDate.setText(sValue);
			}
		});
		
		wvDay.addChangingListener(new OnWheelChangedListener(){
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(wvDate[0], wvDate[1], wvDay);
				String sValue = getDate();
				if(sValue != null)
					btnDate.setText(sValue);
			}
		});
		
		btnDate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setCurrDate((btnDate.getText().toString()).split("-"));
			}
			
		});
		
		
	}
	
	void updateDays(WheelView year, WheelView month, WheelView day) {
		int maxDays = Utility.getLastDayOfMonth(Integer.valueOf(years[wvDate[0].getCurrentItem()]) , Integer.valueOf(months[wvDate[1].getCurrentItem()]) - 1 );    
        
        updateDayList(maxDays);
        
        if(day.getViewAdapter() == null)
        	day.setViewAdapter(new ArrayWheelAdapter<String>(context, alDay.toArray(new String[alDay.size()])));
        else
        	((ArrayWheelAdapter)day.getViewAdapter()).updateItems(alDay.toArray(new String[alDay.size()]));
        
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
	
	public void updateDayList(int iMax){
		alDay.clear();
		for(int i = 1; i <= iMax; i++){
			alDay.add(String.valueOf(i));
		}	
	}

	public String getValue() {
		return btnDate.getText().toString();
	}
	
	public String getDate(){
		try{
			return years[wvDate[0].getCurrentItem()]+"-"+months[wvDate[1].getCurrentItem()] +"-"+alDay.get(wvDay.getCurrentItem());
		}catch(Exception e){
			return null;
		}
	}
	
	public void setCurrDate(String[] sValue){	
		int iYear = -1;
		int iMonth = -1;
		int iDay = -1;		
		
		for(int i = 0; i < years.length; i++){			
			if(sValue[0].equals(years[i])){
				iYear = i;
				break;
			}
		}
		
		for(int i = 0; i < months.length; i++){			
			if(sValue[1].equals(months[i])){
				iMonth = i;
				break;
			}
		}	
		
		iDay = alDay.indexOf(String.valueOf(Integer.valueOf(sValue[2])));
		wvDate[0].setCurrentItem(iYear, true);
		wvDate[1].setCurrentItem(iMonth, true);
		wvDay.setCurrentItem(iDay, true);
	}
	
	public void setDateValue(String sDate){
		btnDate.setText(sDate);
	}	
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}
	public void dismiss(){
		popup.dismiss();
	}	
	
	public void updateDays(){
		updateDays(wvDate[0], wvDate[1], wvDay);
	}
	
	private void updateDateWheel(WheelView[] wvDate, String[][] values){		
		for(int i = 0; i < wvDate.length; i++){
			WheelView wv = wvDate[i];
			if(wv.getViewAdapter() == null){
				wv.setViewAdapter(new ArrayWheelAdapter<String>(context, values[i]));		
			}else{
				((ArrayWheelAdapter)wv.getViewAdapter()).updateItems(values[i]);
			}
		}		
	}
	/*
	public View getRoot(){	
		return popup.getRoot();
	}
	*/
}
