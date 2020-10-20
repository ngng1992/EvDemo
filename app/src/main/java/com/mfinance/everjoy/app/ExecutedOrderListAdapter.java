package com.mfinance.everjoy.app;

import android.content.Context;
import android.content.res.Resources;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ExecutedOrder;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;

public class ExecutedOrderListAdapter extends BaseAdapter {
	private final String TAG = this.getClass().getSimpleName();

	HashMap<Integer, ExecutedOrder> hmExecutedOrder = null;
	ArrayList<Integer> alRef = new ArrayList<Integer>();
	Context context;
	LayoutInflater m_inflater;
	private final MobileTraderApplication app;
	private final Messenger mService;
	private final Messenger mServiceMessengerHandler;
	private final ListViewOnItemListener listener;
	
	int iRef;
	Resources res;

	public ExecutedOrderListAdapter(Context context,
			HashMap<Integer, ExecutedOrder> hmOrder, Messenger mService,
			Messenger mServiceMessengerHandler, ListViewOnItemListener listener) {
		this.context = context;
		m_inflater = LayoutInflater.from(context);
		app = (MobileTraderApplication) context.getApplicationContext();
		res = context.getResources();
		this.mService = mService;
		this.mServiceMessengerHandler = mServiceMessengerHandler;
		this.listener = listener;
		reload(hmOrder);
	}

	@Override
	public int getCount() {
		return hmExecutedOrder.size();
	}

	@Override
	public Object getItem(int position) {
		return hmExecutedOrder.get(alRef.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		synchronized (alRef) {
			if (alRef.size() > 0) {
				View vItem;
				if (convertView == null) {
					if (CompanySettings.newinterface)
						convertView = m_inflater.inflate(R.layout.list_item_executed_order_new, null);
					else
						convertView = m_inflater.inflate(R.layout.list_item_executed_order, null);
					vItem = convertView.findViewById(R.id.llOrder);
					vItem.setOnTouchListener(listener);
				} else {
					vItem = convertView.findViewById(R.id.llOrder);
				}

				ExecutedOrder order = hmExecutedOrder.get(alRef.get(position));
				
			      Iterator<Integer> iterator = hmExecutedOrder.keySet().iterator();
			      while (iterator.hasNext()) {
			    	Integer key = (Integer) iterator.next();
			        //System.out.println("Error " + key + " means " + hmExecutedOrder.get(key));
			      }
				if (order != null) {
					Locale locale = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context));
					String sTmp;
					vItem.setTag(order.iRef);

					TextView tvTmp = (TextView) convertView
							.findViewById(R.id.lbContract);
					tvTmp.setText(order.contract.getContractName(locale));

					tvTmp = (TextView) convertView
							.findViewById(R.id.lbContract);
					tvTmp.setText(order.contract.getContractName(locale));

					tvTmp = (TextView) convertView
							.findViewById(R.id.lbExecutedDate);
					sTmp = order.sExecutedDate;
					tvTmp.setText(Utility.displayListingDate(sTmp));

					if (CompanySettings.newinterface) {
						if ("B".equals(order.strBuySell)) {
							((ImageView) convertView.findViewById(R.id.ivBuy)).setImageResource(R.drawable.ico_buy);
							if (convertView.findViewById(R.id.ivBuy) != null)
								convertView.findViewById(R.id.ivBuy).setVisibility(View.VISIBLE);
							if (convertView.findViewById(R.id.lbBuy) != null)
								((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_b", context.getResources()));
						} else {
							((ImageView) convertView.findViewById(R.id.ivBuy)).setImageResource(R.drawable.ico_sell);
							if (convertView.findViewById(R.id.ivBuy) != null)
								convertView.findViewById(R.id.ivBuy).setVisibility(View.VISIBLE);
							if (convertView.findViewById(R.id.lbBuy) != null)
								((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_s", context.getResources()));
						}

						// lot store at Limit or Stop
						sTmp = Utility.formatLot(order.dAmount
								/ order.contract.iContractSize);
						if(!CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW){
							if(order.iLimitStop == 0){
								((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
							}
							else {
								((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
							}
						}else{
							((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
						}
					}
					else {
						if("B".equals(order.strBuySell)){
							if(convertView.findViewById(R.id.ivBuy)!=null)
								convertView.findViewById(R.id.ivBuy).setVisibility(View.VISIBLE);
							if(convertView.findViewById(R.id.ivSell)!=null)
								convertView.findViewById(R.id.ivSell).setVisibility(View.INVISIBLE);
							if(convertView.findViewById(R.id.lbBuy)!=null)
								((TextView)convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_b", context.getResources()));
						}else{
							if(convertView.findViewById(R.id.ivBuy)!=null)
								convertView.findViewById(R.id.ivBuy).setVisibility(View.INVISIBLE);
							if(convertView.findViewById(R.id.ivSell)!=null)
								convertView.findViewById(R.id.ivSell).setVisibility(View.VISIBLE);
							if(convertView.findViewById(R.id.lbBuy)!=null)
								((TextView)convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_s", context.getResources()));
						}

						// lot store at Limit or Stop
						sTmp = Utility.formatLot(order.dAmount
								/ order.contract.iContractSize);
						if(!CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW){
							if(order.iLimitStop == 0){
								((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
								((TextView) convertView.findViewById(R.id.lbStop)).setText("");
							}
							else {
								((TextView) convertView.findViewById(R.id.lbLimit)).setText("");
								((TextView) convertView.findViewById(R.id.lbStop)).setText(sTmp);
							}
						}else{
							((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
						}
					}



					convertView.findViewById(R.id.ivOco).setVisibility(
							order.iOCORef > 0 ? View.VISIBLE : View.INVISIBLE);
					
					boolean isLiq = order.iLiquidationMethod==3;
					if(convertView.findViewById(R.id.ivLiq)!=null)
						convertView.findViewById(R.id.ivLiq).setVisibility(isLiq ? View.VISIBLE : View.INVISIBLE);
					if (CompanySettings.newinterface) {
						Optional.<TextView>ofNullable(convertView.findViewById(R.id.vtRequestTime))
								.ifPresent(v -> v.setText(order.sExecutedTime));
					}
					if (!CompanySettings.newinterface) {
						if(CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW){
							  if(isLiq){
								  if(order.iLimitStop == 0){
									  ((TextView) convertView.findViewById(R.id.lbStop)).setText(R.string.tb_limit);
								  }
								  else {
									  ((TextView) convertView.findViewById(R.id.lbStop)).setText(R.string.tb_stop);
								  }
							  }else{
								  ((TextView) convertView.findViewById(R.id.lbStop)).setText(Utility.getStringById("tb_new", context.getResources()));
							  }
						}
					}
					tvTmp = (TextView) convertView
							.findViewById(R.id.lbReqPrice);
					sTmp = Utility.round(order.dRequestRate,
							order.contract.iRateDecPt,
							order.contract.iRateDecPt);
					tvTmp.setText(sTmp);

					vItem.setOnClickListener(new OnClickListener() {
				    	  @Override
				    	  public void onClick(View v) {
				    		  iRef = (Integer)v.getTag();				
				    		  app.setSelectedExcutedOrder(iRef);	
				    		  CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_EXECUTED_ORDER_DETAIL, null);
				    	  }
				    });    
					
					if (position % 2 != 0)
						vItem.setBackgroundResource(R.drawable.list_row_odd);
			    	else 
			    		vItem.setBackgroundResource(R.drawable.list_row_even);  
				}

				return convertView;
			} else {
				return convertView = m_inflater.inflate(
						R.layout.list_item_running_order, null);
			}
		}
	}

	public void reload(HashMap<Integer, ExecutedOrder> hmOrder) {
		synchronized (alRef) {
			this.hmExecutedOrder = (HashMap<Integer, ExecutedOrder>) hmOrder
					.clone();
			alRef.clear();
			alRef.addAll(hmOrder.keySet());

			Collections.sort(alRef, new Comparator<Integer>() {

				@Override
				public int compare(Integer i1, Integer i2) {
					if (i1 > i2) {
						return -1;
					} else if (i2 > i1) {
						return 1;
					} else
						return 0;
				}

			});
		}
	}
	
	
}
