package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.ActionItem;
import com.mfinance.everjoy.app.widget.quickaction.QuickAction;
import com.mfinance.everjoy.app.widget.wheel.adapter.AbstractWheelTextAdapter;

import android.content.Context;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class OpenPositionWheelAdapter extends AbstractWheelTextAdapter{
	private final String TAG = this.getClass().getSimpleName();
	HashMap<Integer, OpenPositionRecord> hmPosition = null;
	ArrayList<Integer> alRef = new ArrayList<Integer>();
	HashMap<Integer, Boolean> hmSelect = new HashMap<Integer, Boolean>();
	ArrayList<Integer> alLiqList = new ArrayList<Integer>();
	Context context;
	LayoutInflater m_inflater;
	ActionItem aiLiquidate = null;
	MobileTraderApplication app = null;
	QuickAction qa = null;
	boolean bSelectionMode = false;
	protected Messenger mService = null;
	protected Messenger mServiceMessengerHandler = null;	
	
	public OpenPositionWheelAdapter(Context context, HashMap<Integer, OpenPositionRecord> hmPosition, Messenger mService, Messenger mServiceMessengerHandler){
		super(context);
		this.context = context;		
		m_inflater = LayoutInflater.from(context);
		app = (MobileTraderApplication)context.getApplicationContext();
		this.mService = mService;
		this.mServiceMessengerHandler = mServiceMessengerHandler; 
		reload(hmPosition);
	}

    @Override
    public View getItem(int position, View convertView, ViewGroup parent) {
    	synchronized(alRef){
			if(alRef.size() > 0){
			      if(convertView == null)
			          convertView = m_inflater.inflate(R.layout.wheel_position_item, null);
			      
			      OpenPositionRecord positionObj = hmPosition.get(alRef.get(position));
			      
			      if(positionObj != null){
			  		/*
			  		if("B".equals(positionObj.strBuySell)){
			  			sRate = String.valueOf(positionObj.contract.getBidAsk()[1]);
			  		}else{
			  			sRate = String.valueOf(positionObj.contract.getBidAsk()[0]);
			  		}
			  		
			  		TextView tvTmp = (TextView)findViewById(R.id.tvRate);
			  		tvTmp.setText(sRate);
			  		*/
			    	Locale l = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context));
			    	
			  		TextView tvTmp = (TextView)convertView.findViewById(R.id.tvBuySell);
			  		if("B".equals(positionObj.strBuySell)){
			  			tvTmp.setText(R.string.lb_buy);	
			  		}else{
			  			tvTmp.setText(R.string.lb_sell);
			  		}	
			  		
			  		tvTmp = (TextView)convertView.findViewById(R.id.tvOldLot);
			  		tvTmp.setText(Utility.formatLot(positionObj.dAmount / positionObj.contract.iContractSize));
			  		/*
			  		tvTmp = (TextView)convertView.findViewById(R.id.tvPL);
			  		tvTmp.setText(Utility.round(positionObj.dFloating, 2, 2));
			  		*/
			  		tvTmp = (TextView)convertView.findViewById(R.id.tvRef);
			  		tvTmp.setText(positionObj.getViewRef());
			  		
			  		tvTmp = (TextView)convertView.findViewById(R.id.tvFloating);
			  		tvTmp.setText(Utility.round(positionObj.dFloating, 2, 2));		
			  		
			  		tvTmp = (TextView)convertView.findViewById(R.id.tvContract);
			  		tvTmp.setText(positionObj.contract.getContractName(l));			  		
			      }
			      return convertView;
				}else{
					return convertView = m_inflater.inflate(R.layout.wheel_position_item, null);
				}
		}
    }
    
    @Override
    public int getItemsCount() {
        return alRef.size();
    }
    
    @Override
    protected CharSequence getItemText(int index) {
        return String.valueOf(alRef.get(index));
    }	
    
    public int getItemIndex(int iRef){
    	return this.alRef.indexOf(iRef);
    }
	
	public void reload(Map<Integer, OpenPositionRecord> hmPosition){
		synchronized(alRef){
			this.hmPosition = new HashMap<>(hmPosition);
			alRef.clear();
			alRef.addAll(hmPosition.keySet());
			Collections.sort(alRef, Collections.reverseOrder());
			notifyDataChangedEvent();
			notifyDataInvalidatedEvent();
		}
		
	}
	
	public boolean isInSelectionMode(){
		return bSelectionMode;
	}
	
	public HashMap<Integer, Boolean> getSelected(){
		return hmSelect;
	}
	
}
