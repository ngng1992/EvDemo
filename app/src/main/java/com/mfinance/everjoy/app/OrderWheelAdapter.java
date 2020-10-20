package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.ActionItem;
import com.mfinance.everjoy.app.widget.quickaction.QuickAction;
import com.mfinance.everjoy.app.widget.wheel.adapter.AbstractWheelTextAdapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class OrderWheelAdapter extends AbstractWheelTextAdapter{
	private final String TAG = this.getClass().getSimpleName();
	HashMap<Integer, OrderRecord> hmOrder = null;
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
	OrderRecord selectedOrderRecord = null;

	public OrderWheelAdapter(Context context, HashMap<Integer, OrderRecord> hmOrder, Messenger mService, Messenger mServiceMessengerHandler, OrderRecord order){
		super(context);
		this.context = context;
		m_inflater = LayoutInflater.from(context);
		app = (MobileTraderApplication)context.getApplicationContext();
		this.mService = mService;
		this.mServiceMessengerHandler = mServiceMessengerHandler;
		this.selectedOrderRecord = order;
		reload(hmOrder);
	}
	
	public OrderWheelAdapter(Context context, HashMap<Integer, OrderRecord> hmOrder, Messenger mService, Messenger mServiceMessengerHandler){
		super(context);
		this.context = context;		
		m_inflater = LayoutInflater.from(context);
		app = (MobileTraderApplication)context.getApplicationContext();
		this.mService = mService;
		this.mServiceMessengerHandler = mServiceMessengerHandler; 
		reload(hmOrder);
	}

    @Override
    public View getItem(int position, View convertView, ViewGroup parent) {
    	if(alRef.isEmpty() || position == 0){    		
    		if(convertView == null)
		          convertView = m_inflater.inflate(R.layout.wheel_order_item, null);
    		
    		convertView.setVisibility(View.INVISIBLE);
    		
			return convertView;			
    	}else{
        	synchronized(alRef){
    			if(alRef.size() > 0){
    			      if(convertView == null)
    			          convertView = m_inflater.inflate(R.layout.wheel_order_item, null);
    			      
    			      OrderRecord order = hmOrder.get(alRef.get(position - 1));
    			      
    			      convertView.setVisibility(View.VISIBLE);
    			      
    			      if(order != null){
    			      		if (this.selectedOrderRecord != null && order.iRef == this.selectedOrderRecord.iRef){
								convertView.setEnabled(false);
								convertView.setAlpha((float)0.3);
							} else {
								convertView.setEnabled(true);
								convertView.setAlpha(1);
							}
    			    	  	Locale l = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context));
    			    	  	
    				  		TextView tvTmp = (TextView)convertView.findViewById(R.id.tvBuySell);
    				  		if("B".equals(order.strBuySell)){
    				  			tvTmp.setText(R.string.lb_buy);	
    				  		}else{
    				  			tvTmp.setText(R.string.lb_sell);
    				  		}
    				  			
    				  		
    				  		tvTmp = (TextView)convertView.findViewById(R.id.tvOldLot);
    				  		tvTmp.setText(Utility.formatLot(order.dAmount / order.contract.iContractSize));

    				  		tvTmp = (TextView)convertView.findViewById(R.id.tvRef);
    				  		tvTmp.setText(order.getViewRef());
    				  		
    				  		tvTmp = (TextView)convertView.findViewById(R.id.tvReqRate);
    				  		tvTmp.setText(Utility.round(order.dRequestRate, order.contract.iRateDecPt, order.contract.iRateDecPt));		    				  		
    			      }
    			      return convertView;
    				}else{
    					  if(convertView == null)
        			          convertView = m_inflater.inflate(R.layout.wheel_order_item, null);
    					  
    					  convertView.setVisibility(View.INVISIBLE);
    					return convertView;
    				}
    		}
    	}
    }
    
    @Override
    public int getItemsCount() {
        return alRef.size() + 1;
    }
    
    @Override
    protected CharSequence getItemText(int index) {
    	if(index == 0){
    		return "-";
    	}else{
    		return hmOrder.get(alRef.get(index - 1)).getViewRef();
    	}
    }	
    
    protected CharSequence getOrderRef(int index) {
    	if(index == 0){
    		return "-";
    	}else{
    		return String.valueOf(hmOrder.get(alRef.get(index - 1)).iRef);
    	}
    }	
    
    public int getItemIndex(int iRef){
    	if(alRef.contains(iRef))
    		return this.alRef.indexOf(iRef) + 1;
    	else{
    		return 0;
    	}
    }
	
	public void reload(HashMap<Integer, OrderRecord> hmOrder){
		synchronized(alRef){
			this.hmOrder = (HashMap<Integer, OrderRecord>) hmOrder.clone();
			alRef.clear();
			alRef.addAll(hmOrder.keySet());
			Collections.sort(alRef,Collections.reverseOrder());
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
