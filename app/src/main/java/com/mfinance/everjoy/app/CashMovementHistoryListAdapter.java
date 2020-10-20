package com.mfinance.everjoy.app;

import android.content.Context;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.CashMovementRecord;
import com.mfinance.everjoy.app.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CashMovementHistoryListAdapter extends BaseAdapter {
	public interface OnClickItemHandler {
		void accept(String key);
	}
	private final String TAG = this.getClass().getSimpleName();

	HashMap<String, CashMovementRecord> hmCashMovementRecord = null;

	ArrayList<String> alRef = new ArrayList<String>();
	Context context;
	LayoutInflater m_inflater;
	MobileTraderApplication app = null;
	protected Messenger mService = null;
	protected Messenger mServiceMessengerHandler = null;
	private final OnClickItemHandler onClickHandler;
	private final Function<String, String> getStatusString;

	public CashMovementHistoryListAdapter(Context context,
			Map<String, CashMovementRecord> hmOrder, Messenger mService,
			Messenger mServiceMessengerHandler, OnClickItemHandler onClickHandler, Function<String, String> getStatusString) {
		this.context = context;
		m_inflater = LayoutInflater.from(context);
		app = (MobileTraderApplication) context.getApplicationContext();
		this.mService = mService;
		this.mServiceMessengerHandler = mServiceMessengerHandler;
		this.onClickHandler = onClickHandler;
		this.getStatusString = getStatusString;
		reload(hmOrder);
	}

	@Override
	public int getCount() {
		return hmCashMovementRecord.size();
	}

	@Override
	public Object getItem(int position) {
		return hmCashMovementRecord.get(alRef.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = m_inflater.inflate(R.layout.list_item_cashmovement_history, null);
			convertView.setBackgroundResource(position % 2 != 0 ? R.drawable.list_row_odd_normal : R.drawable.list_row_even_normal);
		}
		synchronized (alRef) {
			if (alRef.size() > 0) {
				CashMovementRecord order = hmCashMovementRecord.get(alRef.get(position));
				if (order != null) {
					String sTmp;
					TextView tvTmp;

					View vItem = convertView.findViewById(R.id.llHistory);
					vItem.setTag(order.getKey());

					tvTmp = convertView.findViewById(R.id.tvRef);
					sTmp = order.sRef;
					tvTmp.setText(sTmp);

					tvTmp = convertView.findViewById(R.id.tvAmount);
					sTmp = order.sAmount;
					tvTmp.setText(sTmp);

					tvTmp = convertView.findViewById(R.id.tvStatus);
					sTmp = getStatusString.apply(order.sStatus);
					tvTmp.setText(sTmp);

					tvTmp = convertView.findViewById(R.id.tvReqdate);
					sTmp = Utility.displayListingDate(order.sRequestDate);
					tvTmp.setText(sTmp);

					vItem.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String sKey = String.valueOf(v.getTag());
							onClickHandler.accept(sKey);
						}
					});
				} else {
					//System.out.println("it is null");
				}
			}
		}
		return convertView;
	}

	public void reload(Map<String, CashMovementRecord> hmOrder) {
		synchronized (alRef) {
			this.hmCashMovementRecord = new HashMap<>(hmOrder);
			alRef.clear();
			alRef.addAll(hmOrder.keySet());

			Collections.sort(alRef, (i1, i2) -> {
				try {
					Integer int1 = Integer.parseInt(i1);
					Integer int2 = Integer.parseInt(i2);
					return int2.compareTo(int1);
				} catch (NumberFormatException ex) {
					return 1;
				}
			});

			notifyDataSetChanged();
		}
	}

	@Override
	public int getItemViewType(int position) {
		return position % 2;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
}
