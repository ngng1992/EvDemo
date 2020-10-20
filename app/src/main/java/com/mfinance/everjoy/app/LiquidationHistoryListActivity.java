package com.mfinance.everjoy.app;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

public class LiquidationHistoryListActivity extends BaseActivity{
	ListView lvLiquidationHistory = null;
	LiquidationHistoryListAdapter liquidationHistorydapter = null;
	ListViewAdapterReloader reloader = null;
	
	PopupDate popDate;
	private PopupLiquidationDetail popupDetail;
	
	@Override
	public void bindEvent() {
		findViewById(R.id.btnSelectDate).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				popDate.showLikeQuickAction();
				popDate.setFromToValue(Utility.dateToString(app.dtTradeDate), Utility.dateToString(app.dtTradeDate));
				popDate.setCurrDate(Utility.dateToString(app.dtTradeDate).split("-"));
			}
		});	
		
		findViewById(R.id.btnToday).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				String sDate = Utility.dateToEngString(app.dtTradeDate);
				app.data.clearLiquidationRecord();
				CommonFunction.equeryLiquidationHistory(mService, mServiceMessengerHandler,sDate, sDate);
			}
		});	
		

		
		popDate.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String[] values = popDate.getValue();
				String sFrom = Utility.dateToEngString(values[0]);
				String sTo = Utility.dateToEngString(values[1]);				
				popDate.dismiss();
				app.data.clearLiquidationRecord();
				CommonFunction.equeryLiquidationHistory(mService, mServiceMessengerHandler,sFrom, sTo);
			}
		});	
		popDate.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popDate.dismiss();
			}
		});	
						
	}
	
	@Override
	public void handleByChild(Message msg) {
		switch(msg.what){
		case ServiceFunction.ACT_LIQUIDATE_DETAIL:
			if (popupDetail==null){
				popupDetail = new PopupLiquidationDetail(LiquidationHistoryListActivity.this, findViewById(R.id.rlTop), app,mService, mServiceMessengerHandler);
			}			
			popupDetail.showLikeQuickAction();
			break;
		}
	}

	@Override
	public void loadLayout() {
		if (CompanySettings.newinterface)
			setContentView(R.layout.v_liquidation_history_new);
		else
			setContentView(R.layout.v_liquidation_history);
		lvLiquidationHistory = (ListView)findViewById(R.id.lvRecord);
		lvLiquidationHistory.setSelected(true);		
		popDate = new PopupDate(getApplicationContext(), findViewById(R.id.rlTop));
	}
	
	@Override
	public void updateUI() {
		if (liquidationHistorydapter != null) {
			liquidationHistorydapter.reload(app.data.getLiquidationRecords(), app.data.getExecutedOrders());
			reloader.reload();
		}

		if (popupDetail!=null)
			popupDetail.updateUI();
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		String sDate = Utility.dateToEngString(app.dtTradeDate);
		CommonFunction.equeryLiquidationHistory(mService, mServiceMessengerHandler,sDate, sDate);

		ListViewOnItemListener listener = new ListViewOnItemListener();
		liquidationHistorydapter = new LiquidationHistoryListAdapter(LiquidationHistoryListActivity.this, app.data.getLiquidationRecords(), app.data.getExecutedOrders(),  mService, mServiceMessengerHandler, listener);
		lvLiquidationHistory.setAdapter(liquidationHistorydapter);
		lvLiquidationHistory.setOnItemClickListener(listener);
		lvLiquidationHistory.setItemsCanFocus(true);
		reloader = new ListViewAdapterReloader(lvLiquidationHistory, liquidationHistorydapter);
		reloader.reload();
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
		return ServiceFunction.SRV_LIQUIDATION_HISTORY;
	}	
	
	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_LIQUIDATION_HISTORY;
	}	
}