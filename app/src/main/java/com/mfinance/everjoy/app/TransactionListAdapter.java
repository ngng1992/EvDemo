package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

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
public class TransactionListAdapter extends BaseAdapter {
	private final String TAG = this.getClass().getSimpleName();
	HashMap<String, TransactionObj> hmTransaction = null;
	ArrayList<String> alTransaction = new ArrayList<String>();
	Context context;
	LayoutInflater m_inflater;

	Resources res;
	MobileTraderApplication app = null;

	protected Messenger mService = null;
	protected Messenger mServiceMessengerHandler = null;
	private ListViewOnItemListener listener;

	public TransactionListAdapter(Context context,
			HashMap<String, TransactionObj> hmTransaction, Messenger mService,
			Messenger mServiceMessengerHandler, ListViewOnItemListener listener) {
		this.context = context;
		this.mService = mService;
		this.mServiceMessengerHandler = mServiceMessengerHandler;
		m_inflater = LayoutInflater.from(context);
		res = context.getResources();
		app = (MobileTraderApplication)context.getApplicationContext();
		this.listener = listener;
		reload(hmTransaction);
	}

	@Override
	public int getCount() {
		return hmTransaction.size();
	}

	@Override
	public Object getItem(int position) {
		return hmTransaction.get(alTransaction.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		synchronized (alTransaction) {
			try {
				if (alTransaction.size() > 0) {
					View vTransaction;
					if (convertView == null) {
						if (CompanySettings.newinterface)
							convertView = m_inflater.inflate(R.layout.list_item_transaction_new, null);
						else
							convertView = m_inflater.inflate(R.layout.list_item_transaction, null);
						vTransaction = convertView.findViewById(R.id.llTransaction);
						vTransaction.setOnTouchListener(listener);
					} else {
						vTransaction = convertView.findViewById(R.id.llTransaction);
					}

					TransactionObj transaction = hmTransaction
							.get(alTransaction.get(position));

					vTransaction.setTag(transaction.sTransactionID);

					TextView tvTmp = null;

					tvTmp = (TextView) convertView
							.findViewById(R.id.tvContract);
					tvTmp.setText(transaction.contract.getContractName(LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context))));

					tvTmp = (TextView) convertView.findViewById(R.id.tvTime);
					tvTmp.setText(Utility.getShortTime(transaction.dateCreate));

					tvTmp = (TextView) convertView.findViewById(R.id.tvDate);
					tvTmp.setText(Utility
							.dateToNormalString(transaction.dateCreate));

					// lot store at Buy or sell
					String sTmp = Utility.formatLot((double)transaction.dAmount
							/ transaction.contract.iContractSize);

					if (CompanySettings.newinterface) {
						if ("B".equals(transaction.sBuySell)) {
							((ImageView) convertView.findViewById(R.id.ivBuy)).setImageResource(R.drawable.ico_buy);
							if ("610".equals(transaction.sMsgCode))
								((TextView) convertView.findViewById(R.id.lbBuy))
										.setText("-");
							else
								((TextView) convertView.findViewById(R.id.lbBuy))
										.setText(sTmp);

							((TextView) convertView.findViewById(R.id.lbSell))
									.setText("");
						} else {
							((ImageView) convertView.findViewById(R.id.ivBuy)).setImageResource(R.drawable.ico_sell);
							((TextView) convertView.findViewById(R.id.lbBuy))
									.setText("");
							if ("610".equals(transaction.sMsgCode))
								((TextView) convertView.findViewById(R.id.lbBuy))
										.setText("-");
							else
								((TextView) convertView.findViewById(R.id.lbBuy))
										.setText(sTmp);

							((TextView) convertView.findViewById(R.id.lbSell))
									.setText("");
						}
					}
					else{
						if ("B".equals(transaction.sBuySell)) {
							if ("610".equals(transaction.sMsgCode))
								((TextView) convertView.findViewById(R.id.lbBuy))
										.setText("-");
							else
								((TextView) convertView.findViewById(R.id.lbBuy))
										.setText(sTmp);

							((TextView) convertView.findViewById(R.id.lbSell))
									.setText("");
						} else {
							((TextView) convertView.findViewById(R.id.lbBuy))
									.setText("");
							if ("610".equals(transaction.sMsgCode))
								((TextView) convertView.findViewById(R.id.lbSell))
										.setText("-");
							else
								((TextView) convertView.findViewById(R.id.lbSell))
										.setText(sTmp);
						}
					}

					tvTmp = (TextView) convertView.findViewById(R.id.tvType);
					tvTmp.setText(transaction.iType == 0 ? R.string.lb_deal
							: R.string.lb_order);

					tvTmp = (TextView) convertView.findViewById(R.id.tvMessage);
					//tvTmp.setText(String.valueOf(transaction.sMsg));
					tvTmp.setText(String.valueOf(TransactionObj.getMessage(transaction, res, app)));

					vTransaction.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							app.setSelectedTransaction(v.getTag().toString());
							CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_TRANSACTION_STATUS_DETAIL, null);
						}

					});
					
					if (position % 2 != 0)
				    	vTransaction.setBackgroundResource(R.drawable.list_row_odd);
			    	else 
			    		vTransaction.setBackgroundResource(R.drawable.list_row_even);
					

					return convertView;

				} else {
					return convertView = m_inflater.inflate(
							R.layout.list_item_transaction, null);
				}
			} catch (Exception e) {
				return convertView = m_inflater.inflate(
						R.layout.list_item_transaction, null);
			}
		}
	}

	public void reload(HashMap<String, TransactionObj> hmTransaction) {
		synchronized (alTransaction) {
			this.hmTransaction = (HashMap<String, TransactionObj>) hmTransaction
					.clone();
			alTransaction.clear();
			alTransaction.addAll(hmTransaction.keySet());

			Collections.sort(alTransaction, new Comparator<String>() {

				@Override
				public int compare(String i1, String i2) {
					Date date1 = Utility.timeToDate(i1.substring(0, i1.indexOf("|")));
					Date date2 = Utility.timeToDate(i1.substring(0, i1.indexOf("|")));
					if (date1.compareTo(date2) ==0){
						return i1.compareTo(i2) * -1;
					} else {
						return date1.compareTo(date2) * -1;
					}
				}

			});
		}
		
		
	}

	

}
