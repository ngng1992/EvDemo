package com.mfinance.everjoy.app;

import android.content.Context;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.R.id;
import com.mfinance.everjoy.app.bo.ExecutedOrder;
import com.mfinance.everjoy.app.bo.LiquidationRecord;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class LiquidationHistoryListAdapter extends BaseAdapter{
	private final String TAG = this.getClass().getSimpleName();
	
	HashMap<String, LiquidationRecord> hmLiquidationRecord = null;
	HashMap<Integer, ExecutedOrder> hmExecutedOrder = null;
	
	ArrayList<String> alRef = new ArrayList<String>();
	HashMap<String, Boolean> hmSelect = new HashMap<String, Boolean>();
	ArrayList<LiquidationRecord> alLiqList = new ArrayList<LiquidationRecord>();
	Context context;
	LayoutInflater m_inflater;
	MobileTraderApplication app = null;
	boolean bSelectionMode = false;
	protected Messenger mService = null;
	protected Messenger mServiceMessengerHandler = null;
	private final ListViewOnItemListener listener;
	
	public LiquidationHistoryListAdapter(Context context, HashMap<String, LiquidationRecord> hmOrder, HashMap<Integer, ExecutedOrder> hmExecutedOrder ,Messenger mService, Messenger mServiceMessengerHandler, ListViewOnItemListener listener){
		this.context = context;		
		m_inflater = LayoutInflater.from(context);
		app = (MobileTraderApplication)context.getApplicationContext();
		this.mService = mService;
		this.mServiceMessengerHandler = mServiceMessengerHandler;
		this.hmExecutedOrder = hmExecutedOrder;
		this.listener = listener;
		reload(hmOrder,hmExecutedOrder);
	}
	
	@Override
	public int getCount() {		
		return hmLiquidationRecord.size();		
	}

	@Override
	public Object getItem(int position) {
		return hmLiquidationRecord.get(alRef.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vItem;
		if(convertView == null) {
			if (CompanySettings.newinterface)
				convertView = m_inflater.inflate(R.layout.list_item_liquidation_history_new, null);
			else
				convertView = m_inflater.inflate(R.layout.list_item_liquidation_history, null);
			vItem = convertView.findViewById(R.id.llHistory);
			vItem.setOnTouchListener(listener);
		} else {
			vItem = convertView.findViewById(R.id.llHistory);
		}
		synchronized(alRef){
			if(alRef.size() > 0){
			      LiquidationRecord order = hmLiquidationRecord.get(alRef.get(position));
			      
			      if(order != null){
				      String sTmp;    
				      

				      vItem.setTag(order.getKey() );
				      
				      TextView tvTmp = convertView.findViewById(id.lbContract);
				      sTmp = order.contract.getContractName(LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context)));
				      tvTmp.setText(sTmp);
			      
				      tvTmp = convertView.findViewById(id.lbTranDate);
				      sTmp = Utility.displayListingDate(order.sExeDate);				      
				      tvTmp.setText(sTmp);
				      
				      //lbBuy / lbSell
					  if (CompanySettings.newinterface) {
						  if ("B".equals(order.sBorS)) {
							  if (!CompanySettings.SHOW_BS_COLUMN_ON_LISTVIEW) {
								  tvTmp = convertView.findViewById(id.lbBuy);
								  ((TextView) convertView.findViewById(R.id.lbSell)).setText("");
							  } else {
								  tvTmp = convertView.findViewById(id.lbBuy);
								  ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_b", context.getResources()));
								  ((TextView) convertView.findViewById(R.id.lbSell)).setText("");
							  }

							  if (convertView.findViewById(Utility.getIdById("lbOPrice")) != null) {
								  ((TextView) convertView.findViewById(Utility.getIdById("lbOPrice"))).setText(order.sSRate);
							  }

							  ((TextView) convertView.findViewById(R.id.lbExecPrice)).setText(order.sSRate + "/\n" +order.sBRate);

							  if (convertView.findViewById(R.id.lbBuyRate) != null) {
								  ((TextView) convertView.findViewById(R.id.lbBuyRate)).setText(order.sSRate);
							  }
						  } else {
							  if (!CompanySettings.SHOW_BS_COLUMN_ON_LISTVIEW) {
								  tvTmp = convertView.findViewById(id.lbBuy);
								  ((TextView) convertView.findViewById(R.id.lbBuy)).setText("");
								  ((TextView) convertView.findViewById(R.id.lbSell)).setText("");
							  } else {
								  tvTmp = convertView.findViewById(id.lbBuy);
								  ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_s", context.getResources()));
								  ((TextView) convertView.findViewById(R.id.lbSell)).setText("");
							  }

							  if (convertView.findViewById(Utility.getIdById("lbOPrice")) != null) {
								  ((TextView) convertView.findViewById(Utility.getIdById("lbOPrice"))).setText(order.sBRate);
							  }

							  ((TextView) convertView.findViewById(R.id.lbExecPrice)).setText(order.sBRate + "/\n" + order.sSRate);
							  if (convertView.findViewById(R.id.lbBuyRate) != null) {
								  ((TextView) convertView.findViewById(R.id.lbBuyRate)).setText(order.sBRate);
							  }
						  }
					  }
					  else {
						  if("B".equals(order.sBorS)){
							  if(!CompanySettings.SHOW_BS_COLUMN_ON_LISTVIEW){
								  tvTmp = convertView.findViewById(id.lbBuy);
								  ((TextView) convertView.findViewById(R.id.lbSell)).setText("");
							  }else{
								  tvTmp = convertView.findViewById(id.lbSell);
								  ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_b", context.getResources()));
							  }

							  if(convertView.findViewById(Utility.getIdById("lbOPrice"))!=null){
								  ((TextView) convertView.findViewById(Utility.getIdById("lbOPrice"))).setText(order.sSRate);
							  }

							  ((TextView)convertView.findViewById(R.id.lbExecPrice)).setText(order.sBRate);

							  if( convertView.findViewById(R.id.lbBuyRate)!=null){
								  ((TextView)convertView.findViewById(R.id.lbBuyRate)).setText(order.sSRate);
							  }
						  } else {
							  if(!CompanySettings.SHOW_BS_COLUMN_ON_LISTVIEW){
								  tvTmp = convertView.findViewById(id.lbSell);
								  ((TextView) convertView.findViewById(R.id.lbBuy)).setText("");
							  }else{
								  tvTmp = convertView.findViewById(id.lbSell);
								  ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_s", context.getResources()));
							  }

							  if(convertView.findViewById(Utility.getIdById("lbOPrice"))!=null){
								  ((TextView) convertView.findViewById(Utility.getIdById("lbOPrice"))).setText(order.sBRate);
							  }

							  ((TextView)convertView.findViewById(R.id.lbExecPrice)).setText(order.sSRate);
							  if( convertView.findViewById(R.id.lbBuyRate)!=null){
								  ((TextView)convertView.findViewById(R.id.lbBuyRate)).setText(order.sBRate);
							  }
						  }
					  }


				      tvTmp.setText(Utility.formatLot(order.dAmount / order.contract.iContractSize));
				      
				      if( convertView.findViewById(R.id.lbCommission)!=null){
			    		  ((TextView)convertView.findViewById(R.id.lbCommission)).setText(Utility.formatValue(order.dCommission));
			    	  }

				      tvTmp = convertView.findViewById(id.lbFloat);
				      sTmp = Utility.formatValue(order.dPL);
				      tvTmp.setText(sTmp);	
				      
				      ColorController.setNumberColor(context.getResources(), order.dPL >= 0 , tvTmp);

				      vItem.setOnClickListener(new OnClickListener() {
				    	  @Override
				    	  public void onClick(View v) {
					    		String sKey = String.valueOf(v.getTag());				
								app.setSelectedLiquidation(sKey);	
								CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_LIQUIDATE_DETAIL, null);				
				    	  }
				      });    
				      
				      if (position % 2 != 0)
				    	  vItem.setBackgroundResource(R.drawable.list_row_odd);
			    	  else 
			    		  vItem.setBackgroundResource(R.drawable.list_row_even);      
				      

			      }


			}
		}
		return convertView;
	}
	
	public void reload(HashMap<String, LiquidationRecord> hmOrder, HashMap<Integer, ExecutedOrder> hmExecutedOrder){
		synchronized(alRef){
			this.hmLiquidationRecord = (HashMap<String, LiquidationRecord>) hmOrder.clone();
			this.hmExecutedOrder = (HashMap<Integer, ExecutedOrder>) hmExecutedOrder.clone();
			alRef.clear();
			alRef.addAll(hmOrder.keySet());
			
			Collections.sort(alRef, new Comparator<String>(){

				@Override
				public int compare(String i1, String i2) {
					int iResult = i1.compareTo(i2);					
					if(iResult > 0){
						return -1;
					}else if(iResult < 0){
						return 1;
					}else
						return 0;
				}
				
			});
		}
	}
}
