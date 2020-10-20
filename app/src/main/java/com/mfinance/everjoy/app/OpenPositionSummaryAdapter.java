package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.function.BiConsumer;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OpenPositionSummaryAdapter extends BaseAdapter{
	private final String TAG = this.getClass().getSimpleName();
	HashMap<String, ContractObj> hmContract = null;
	ArrayList<String> alRef = new ArrayList<String>();
	ArrayList<OpenPositionRecord> alLiqList = new ArrayList<OpenPositionRecord>();
	Context context;
	LayoutInflater m_inflater;
	MobileTraderApplication app = null;
	Resources res;
	protected Messenger mService = null;
	protected Messenger mServiceMessengerHandler = null;

	String hightLightedKey = "";
	private final BiConsumer<String, String> onClickItem;
	private final ListViewOnItemListener listener;

	public OpenPositionSummaryAdapter(Context context, HashMap<String, ContractObj> hmContract, Messenger mService, Messenger mServiceMessengerHandler, BiConsumer<String, String> onClickItem, ListViewOnItemListener listener) {
		this.context = context;
		res = context.getResources();
		m_inflater = LayoutInflater.from(context);
		app = (MobileTraderApplication)context.getApplicationContext();
		this.mService = mService;
		this.mServiceMessengerHandler = mServiceMessengerHandler;
		this.onClickItem = onClickItem;
		this.listener = listener;
		reload(hmContract);
	}


	@Override
	public int getCount() {
		return hmContract.size();
	}

	@Override
	public Object getItem(int position) {
		return hmContract.get(alRef.get(position));
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
						convertView = m_inflater.inflate(R.layout.list_item_open_summary_new, null);
					else
						convertView = m_inflater.inflate(R.layout.list_item_open_summary, null);
					vItem = convertView.findViewById(R.id.llOpenSummary);
					vItem.setOnTouchListener(listener);
					vItem.setOnClickListener(v -> {
						hightLightedKey = String.valueOf(v.getTag());
						String[] sKey = hightLightedKey.split("\\|");
						String contract = sKey[0];
						String buySell = sKey[1];

						onClickItem.accept(contract, buySell);
						v.setBackgroundResource(R.drawable.list_row_odd);
						hightLightedKey = "";
					});
				} else {
					vItem = convertView.findViewById(R.id.llOpenSummary);
				}
				String sKey = alRef.get(position);
				ContractObj contract = hmContract.get(sKey);
				//System.out.println(sKey);
				if (contract != null) {
					Locale l = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context));

					String sTmp;
					TextView tvTmp;

					HashMap<Integer, OpenPositionRecord> hmPosition;

					tvTmp = (TextView) convertView.findViewById(R.id.lbContract);
					tvTmp.setText(contract.getContractName(l));

					if (sKey.endsWith("B")) {
						sTmp = "B";
						hmPosition = (HashMap<Integer, OpenPositionRecord>) contract.getBuyPositions().clone();

					} else {
						sTmp = "S";
						hmPosition = (HashMap<Integer, OpenPositionRecord>) contract.getSellPositions().clone();
					}

					double dFloat = 0.0;
					double dAmount = 0.0;
					double dRate = 0.0;
					double dLot = 0.0;

					Iterator<OpenPositionRecord> itPosition = hmPosition.values().iterator();
					while (itPosition.hasNext()) {
						OpenPositionRecord positionRecord = itPosition.next();
						dFloat += positionRecord.dFloating;
						dAmount += positionRecord.dAmount;
						dRate += positionRecord.dDealRate * positionRecord.dAmount / contract.iContractSize;
						dLot += positionRecord.dAmount / contract.iContractSize;
					}

					//lbBuy / lbSell
					if (!CompanySettings.SHOW_BS_COLUMN_ON_LISTVIEW) {
						if (CompanySettings.newinterface) {
							if (sKey.endsWith("B")) {
								((ImageView) convertView.findViewById(R.id.ivBuy)).setImageResource(R.drawable.ico_buy);
								tvTmp = (TextView) convertView.findViewById(R.id.lbBuy);
								((TextView) convertView.findViewById(R.id.lbSell)).setText("");
							} else {
								((ImageView) convertView.findViewById(R.id.ivBuy)).setImageResource(R.drawable.ico_sell);
								tvTmp = (TextView) convertView.findViewById(R.id.lbBuy);
								((TextView) convertView.findViewById(R.id.lbSell)).setText("");
							}
						} else {
							if (sKey.endsWith("B")) {
								tvTmp = (TextView) convertView.findViewById(R.id.lbBuy);
								((TextView) convertView.findViewById(R.id.lbSell)).setText("");
							} else {
								tvTmp = (TextView) convertView.findViewById(R.id.lbSell);
								((TextView) convertView.findViewById(R.id.lbBuy)).setText("");
							}
						}
					} else {
						tvTmp = (TextView) convertView.findViewById(R.id.lbSell);
						if (sKey.endsWith("B")) {
							((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_b", context.getResources()));
						} else {
							((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_s", context.getResources()));
						}
					}

					tvTmp.setText(Utility.formatLot(dLot));

					tvTmp = (TextView) convertView.findViewById(R.id.lbFloat);
					tvTmp.setText(Utility.formatValue(dFloat));
					ColorController.setNumberColor(context.getResources(), dFloat >= 0, tvTmp);

					tvTmp = (TextView) convertView.findViewById(R.id.lbAPrice);
					tvTmp.setText(Utility.round(dRate / (dAmount / contract.iContractSize), contract.iRateDecPt, contract.iRateDecPt));

					vItem.setTag(sKey);

					if (position % 2 != 0)
						vItem.setBackgroundResource(R.drawable.list_row_odd);
					else
						vItem.setBackgroundResource(R.drawable.list_row_even);
				}
				return convertView;
			} else {
				return convertView = m_inflater.inflate(R.layout.list_item_open_position, null);
			}
		}
	}

	public void reload(HashMap<String, ContractObj> hmContract){
		HashMap<String, ContractObj> tmp = new HashMap<String, ContractObj>();;

		synchronized(alRef){
			alRef.clear();
			Iterator<ContractObj> itContract = hmContract.values().iterator();
			while(itContract.hasNext()){
				ContractObj contract = itContract.next();
				String sKey;

				if(!contract.isBuyEmpty()){
					sKey = contract.strContractCode + "|B";
					alRef.add(sKey);
					tmp.put(sKey, contract);
				}

				if(!contract.isSellEmpty()){
					sKey = contract.strContractCode + "|S";
					alRef.add(sKey);
					tmp.put(sKey, contract);
				}
			}

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

			this.hmContract = tmp;
		}
	}

}
