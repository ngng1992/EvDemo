package com.mfinance.everjoy.app;


import android.content.ComponentName;
import android.os.Bundle;
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

public class ExecutedOrderListActivity extends BaseActivity{
	private ListView lvExecutedOrder = null;
	private ListViewAdapterReloader reloader = null;
	private ExecutedOrderListAdapter executedOrderAdapter = null;

	private PopupDate popDate;
	
	private PopupExcutedOrderDetail popupDetail;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		ListView list = (ListView) findViewById(R.id.lvOrder);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					if (popupDetail == null) {
						popupDetail = new PopupExcutedOrderDetail(
								ExecutedOrderListActivity.this,
								findViewById(getTitleLayerId()),
								(SuccessApplication) app, mService,
								mServiceMessengerHandler);
					}
					((SuccessApplication) app)
							.setSelectedLExecutedOrderRecord(((ExecutedOrderListAdapter) adp)
									.getItem(arg2).iRef);
					popupDetail.showLikeQuickAction();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		*/
	}
	
	@Override
	public void bindEvent() {
		findViewById(R.id.btnSelectDate).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				popDate.showLikeQuickAction();
				//System.out.println("Utility.dateToString(app.dtTradeDate):"+Utility.dateToString(app.dtTradeDate));
				popDate.setFromToValue(Utility.dateToString(app.dtTradeDate), Utility.dateToString(app.dtTradeDate));
				popDate.setCurrDate(Utility.dateToString(app.dtTradeDate).split("-"));
			}
		});	
		
		findViewById(R.id.btnToday).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				String sDate = Utility.dateToEngString(app.dtTradeDate);
				app.data.clearExecutedOrder();
				CommonFunction.equeryExecutedOrder(mService, mServiceMessengerHandler,sDate, sDate);
			}
		});	
		

		
		popDate.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String[] values = popDate.getValue();
				String sFrom = Utility.dateToEngString(values[0]);
				String sTo = Utility.dateToEngString(values[1]);				
				popDate.dismiss();
				app.data.clearExecutedOrder();
				CommonFunction.equeryExecutedOrder(mService, mServiceMessengerHandler,sFrom, sTo);
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
		case ServiceFunction.ACT_EXECUTED_ORDER_DETAIL:
			
			if (popupDetail==null){
				popupDetail = new PopupExcutedOrderDetail(ExecutedOrderListActivity.this, findViewById(R.id.rlTop), app,mService, mServiceMessengerHandler);
			}
			
			if(!popupDetail.isShown())
				popupDetail.showLikeQuickAction();			
			
			break;
		}
	}

	@Override
	public void loadLayout() {
		if (CompanySettings.newinterface)
			setContentView(R.layout.v_executed_order_new);
		else
			setContentView(R.layout.v_executed_order);
		lvExecutedOrder = (ListView)findViewById(R.id.lvOrder);
		lvExecutedOrder.setSelected(true);		
		popDate = new PopupDate(getApplicationContext(), findViewById(R.id.rlTop));
	}
	
	
	@Override
	public void updateUI() {
		if (executedOrderAdapter != null) {
			executedOrderAdapter.reload(app.data.getExecutedOrders());
			reloader.reload();
		}

	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		ListViewOnItemListener listener = new ListViewOnItemListener();
		executedOrderAdapter = new ExecutedOrderListAdapter(ExecutedOrderListActivity.this, app.data.getExecutedOrders(), mService, mServiceMessengerHandler, listener);
		lvExecutedOrder.setAdapter(executedOrderAdapter);
		lvExecutedOrder.setOnItemClickListener(listener);
		lvExecutedOrder.setItemsCanFocus(true);
		reloader = new ListViewAdapterReloader(lvExecutedOrder, executedOrderAdapter);
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
		return ServiceFunction.SRV_EXECUTED_ORDER;
	}
	
	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_EXECUTED_ORDER;
	}		
	
}