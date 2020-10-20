package com.mfinance.everjoy.app;

import java.util.HashMap;
import java.util.Map.Entry;


import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

import android.os.Message;
import android.widget.ListView;
import com.mfinance.everjoy.R;
public class TransactionListActivity extends BaseActivity{
	private final String TAG = this.getClass().getSimpleName();
	
	ListView lvTransaction = null;
	TransactionListAdapter transactionAdapter = null;
	
	private PopupTransactionDetail popupDetail;
	
	@Override
	public void bindEvent() {}

	@Override
	public void handleByChild(Message msg) {
		switch(msg.what){
		case ServiceFunction.ACT_TRANSACTION_STATUS_DETAIL:
			if (popupDetail==null){
				popupDetail = new PopupTransactionDetail(TransactionListActivity.this, findViewById(R.id.rlTop), app,mService, mServiceMessengerHandler);
			}
			popupDetail.showLikeQuickAction();
			break;
		}
	}

	@Override
	public void loadLayout() {
		if (CompanySettings.newinterface)
			setContentView(R.layout.v_transaction_new);
		else
			setContentView(R.layout.v_transaction);
		lvTransaction = (ListView)findViewById(R.id.lvTransaction);	
	}

	@Override
	public void updateUI() {
		if(transactionAdapter == null){
			ListViewOnItemListener listener = new ListViewOnItemListener();
			transactionAdapter = new TransactionListAdapter(TransactionListActivity.this, getTransactionsOrderByDate(app.data.getTransactions()), mService, mServiceMessengerHandler, listener);
			lvTransaction.setAdapter(transactionAdapter);				
		}else{
			transactionAdapter.reload(getTransactionsOrderByDate(app.data.getTransactions()));
			transactionAdapter.notifyDataSetChanged();		
		}
		
		if (popupDetail!=null)
			popupDetail.updateUI();
	}
	

	@Override
	public boolean isBottonBarExist() {
		return true;
	}

	@Override
	public boolean isTopBarExist() {
		return true;
	}    
	
	@Override
	public boolean showLogout() {
		return true;
	}
	
	@Override
	public boolean showTopNav() {
		return true;
	}

	@Override
	public boolean showConnected() {
		return true;
	}

	@Override
	public boolean showPlatformType() {
		return true;
	}

	@Override
	public int getServiceId() {
		return ServiceFunction.SRV_TRANSACTION;
	}
	
	@Override
	public int getActivityServiceCode(){
		return ServiceFunction.SRV_TRANSACTION;
	}	
	
	public HashMap<String, TransactionObj> getTransactionsOrderByDate(HashMap<String, TransactionObj> hmTransactionObj){
		
		HashMap<String, TransactionObj> hm = new HashMap<String, TransactionObj> (20); 
		
		for(Entry<String, TransactionObj> entry : hmTransactionObj.entrySet() ) {        
			hm.put( Utility.getTime( ((TransactionObj)entry.getValue()).dateLastUpdate!=null? ((TransactionObj)entry.getValue()).dateLastUpdate : ((TransactionObj)entry.getValue()).dateCreate )  +"|" + ((TransactionObj)entry.getValue()).sTransactionID , ((TransactionObj)entry.getValue()) );
	    }  
		//System.out.println("hm:" + hm.size());
		return hm;
	}
}