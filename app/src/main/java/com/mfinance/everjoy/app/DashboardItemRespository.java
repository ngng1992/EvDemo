package com.mfinance.everjoy.app;

import android.content.res.Resources;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.Menu;

import java.util.ArrayList;


public class DashboardItemRespository{
	public static ArrayList<ArrayList<DashboardItem>> alItem = new ArrayList<ArrayList<DashboardItem>>();
	public static ArrayList<ArrayList<DashboardItem>> menuItem = new ArrayList<ArrayList<DashboardItem>>();
	public static ArrayList<Menu> menuList  = new ArrayList<>();
	static{
			if (!CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN) {
				ArrayList<DashboardItem> alItemTmp = new ArrayList<DashboardItem>();
				alItemTmp.add(new DashboardItem(R.drawable.db_price, R.string.db_price_list, ServiceFunction.SRV_PRICE, R.drawable.nav_price, R.string.nav_price_list, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_price_summary, R.string.db_price_summary, ServiceFunction.SRV_CONTRACT_DETAIL, R.drawable.nav_price_summary, R.string.nav_price_summary, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_chart, R.string.db_chart, ServiceFunction.SRV_CHART, R.drawable.nav_chart, R.string.nav_chart, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_new_position, R.string.db_new_position, ServiceFunction.SRV_DEAL, R.drawable.nav_new_position, R.string.nav_new_position, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_position, R.string.db_open_position_listing, ServiceFunction.SRV_OPEN_POSITION, R.drawable.nav_position, R.string.nav_open_position_listing, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_position_summary, R.string.db_open_position_summary, ServiceFunction.SRV_OPEN_POSITION_SUMMARY, R.drawable.nav_position_summary, R.string.nav_open_position_summary, ""));
				if (CompanySettings.ENABLE_ORDER == true)
					alItemTmp.add(new DashboardItem(R.drawable.db_new_order, R.string.db_new_order, ServiceFunction.SRV_ORDER, R.drawable.nav_new_order, R.string.nav_new_order, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_order, R.string.db_pending_order_listing, ServiceFunction.SRV_RUNNING_ORDER, R.drawable.nav_order, R.string.nav_pending_order_listing, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_ts, R.string.db_transaction_status, ServiceFunction.SRV_TRANSACTION, R.drawable.nav_ts, R.string.nav_transaction_status, ""));
				alItem.add(alItemTmp);
				alItemTmp = new ArrayList<DashboardItem>();
				alItemTmp.add(new DashboardItem(R.drawable.db_liquidation_history, R.string.db_liquidation_history, ServiceFunction.SRV_LIQUIDATION_HISTORY, R.drawable.nav_liquidation_history, R.string.nav_liquidation_history, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_executed_order_history, R.string.db_executed_order_history, ServiceFunction.SRV_EXECUTED_ORDER, R.drawable.nav_executed_order_history, R.string.nav_executed_order_history, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_cancelled_order_history, R.string.db_cancelled_order_history, ServiceFunction.SRV_CANCELLED_ORDER, R.drawable.nav_cancelled_order_history, R.string.nav_cancelled_order_history, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_setting, R.string.db_setting, ServiceFunction.SRV_SETTING, R.drawable.nav_setting, R.string.nav_settting, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_statement, R.string.db_online_statement, ServiceFunction.SRV_ON_LINE_STATEMENT, R.drawable.nav_statement, R.string.nav_online_statement, ""));
				alItem.add(alItemTmp);
			} else {
				ArrayList<DashboardItem> alItemTmp = new ArrayList<DashboardItem>();
				alItemTmp.add(new DashboardItem(R.drawable.db_newscontent, R.string.db_news_content, ServiceFunction.SRV_MOVE_TO_NEWS_CONTENT_LIST, R.drawable.nav_news_content, R.string.nav_news_content, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_aboutus, R.string.db_company_profile, ServiceFunction.SRV_MOVE_TO_COMPANY_PROFILE, R.drawable.nav_aboutus, R.string.nav_company_profile, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_master, R.string.db_master, ServiceFunction.SRV_MOVE_TO_MASTER_LIST, R.drawable.nav_master, R.string.nav_master, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_day_plan, R.string.db_day_plan, ServiceFunction.SRV_MOVE_TO_STRATEGY_LIST, R.drawable.nav_day_plan, R.string.nav_day_plan, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_price, R.string.db_price_list, ServiceFunction.SRV_PRICE, R.drawable.nav_price, R.string.nav_price_list, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_chart, R.string.db_chart, ServiceFunction.SRV_CONTRACT_DETAIL, R.drawable.nav_chart, R.string.nav_chart, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_economic_data, R.string.db_economic_data, ServiceFunction.SRV_MOVE_TO_ECONOMIC_DATA_LIST, R.drawable.nav_economic_data, R.string.nav_economic_data, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_contact_us, R.string.db_contact_us, ServiceFunction.SRV_MOVE_TO_CONTACT_US, R.drawable.nav_contact_us, R.string.nav_contact_us, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_setting, R.string.db_setting, ServiceFunction.SRV_SETTING, R.drawable.nav_setting, R.string.nav_settting, ""));
				alItem.add(alItemTmp);
				alItemTmp = new ArrayList<DashboardItem>();
				alItemTmp.add(new DashboardItem(R.drawable.db_new_position, R.string.db_new_position, ServiceFunction.SRV_DEAL, R.drawable.nav_new_position, R.string.nav_new_position, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_position, R.string.db_open_position_listing, ServiceFunction.SRV_OPEN_POSITION, R.drawable.nav_position, R.string.nav_open_position_listing, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_position_summary, R.string.db_open_position_summary, ServiceFunction.SRV_OPEN_POSITION_SUMMARY, R.drawable.nav_position_summary, R.string.nav_open_position_summary, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_new_order, R.string.db_new_order, ServiceFunction.SRV_ORDER, R.drawable.nav_new_order, R.string.nav_new_order, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_order, R.string.db_pending_order_listing, ServiceFunction.SRV_RUNNING_ORDER, R.drawable.nav_order, R.string.nav_pending_order_listing, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_acc_bal, R.string.db_acc_bal, ServiceFunction.SRV_SHOW_ACC_INFO, R.drawable.nav_acc_bal, R.string.nav_acc_bal, ""));
				alItem.add(alItemTmp);
				alItemTmp = new ArrayList<DashboardItem>();
				alItemTmp.add(new DashboardItem(R.drawable.db_liquidation_history, R.string.db_liquidation_history, ServiceFunction.SRV_LIQUIDATION_HISTORY, R.drawable.nav_liquidation_history, R.string.nav_liquidation_history, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_executed_order_history, R.string.db_executed_order_history, ServiceFunction.SRV_EXECUTED_ORDER, R.drawable.nav_executed_order_history, R.string.nav_executed_order_history, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_cancelled_order_history, R.string.db_cancelled_order_history, ServiceFunction.SRV_CANCELLED_ORDER, R.drawable.nav_cancelled_order_history, R.string.nav_cancelled_order_history, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_statement, R.string.db_online_statement, ServiceFunction.SRV_ON_LINE_STATEMENT, R.drawable.nav_statement, R.string.nav_online_statement, ""));
				alItemTmp.add(new DashboardItem(R.drawable.db_ts, R.string.db_transaction_status, ServiceFunction.SRV_TRANSACTION, R.drawable.nav_ts, R.string.nav_transaction_status, ""));
				alItem.add(alItemTmp);
			}

			//Array for Menu (new layout)
			ArrayList<DashboardItem> alItemTmp = new ArrayList<DashboardItem>();
			alItemTmp.add(new DashboardItem(R.drawable.db_price, R.string.db_price_list, ServiceFunction.SRV_PRICE, R.drawable.nav_price, R.string.nav_price_list, "ContractListActivity"));
			alItemTmp.add(new DashboardItem(R.drawable.db_price_summary, R.string.db_price_summary, ServiceFunction.SRV_CONTRACT_DETAIL, R.drawable.nav_price_summary, R.string.nav_price_summary, "ContractDetailActivity"));
			menuItem.add(alItemTmp);
			alItemTmp = new ArrayList<DashboardItem>();
			alItemTmp.add(new DashboardItem(R.drawable.db_new_position, R.string.db_new_position, ServiceFunction.SRV_DEAL, R.drawable.nav_new_position, R.string.nav_new_position, "DealActivity"));
			alItemTmp.add(new DashboardItem(R.drawable.db_new_order, R.string.db_new_order, ServiceFunction.SRV_ORDER, R.drawable.nav_new_order, R.string.nav_new_order, "OrderActivity"));
			alItemTmp.add(new DashboardItem(R.drawable.db_order, R.string.db_pending_order_listing, ServiceFunction.SRV_RUNNING_ORDER, R.drawable.nav_order, R.string.nav_pending_order_listing, "RunningOrderListActivity"));
			menuItem.add(alItemTmp);
			alItemTmp = new ArrayList<DashboardItem>();
			alItemTmp.add(new DashboardItem(R.drawable.db_position, R.string.db_open_position_listing, ServiceFunction.SRV_OPEN_POSITION, R.drawable.nav_position, R.string.nav_open_position_listing, "OpenPositionListActivity"));
			alItemTmp.add(new DashboardItem(R.drawable.db_position_summary, R.string.db_open_position_summary, ServiceFunction.SRV_OPEN_POSITION_SUMMARY, R.drawable.nav_position_summary, R.string.nav_open_position_summary, "OpenPositionSummaryActivity"));
			menuItem.add(alItemTmp);
			alItemTmp = new ArrayList<DashboardItem>();
			alItemTmp.add(new DashboardItem(R.drawable.db_ts, R.string.db_transaction_status, ServiceFunction.SRV_TRANSACTION, R.drawable.nav_ts, R.string.nav_transaction_status, "TransactionListActivity"));
			alItemTmp.add(new DashboardItem(R.drawable.db_liquidation_history, R.string.db_liquidation_history, ServiceFunction.SRV_LIQUIDATION_HISTORY, R.drawable.nav_liquidation_history, R.string.nav_liquidation_history, "LiquidationHistoryListActivity"));
			alItemTmp.add(new DashboardItem(R.drawable.db_executed_order_history, R.string.db_executed_order_history, ServiceFunction.SRV_EXECUTED_ORDER, R.drawable.nav_executed_order_history, R.string.nav_executed_order_history, "ExecutedOrderListActivity"));
			alItemTmp.add(new DashboardItem(R.drawable.db_cancelled_order_history, R.string.db_cancelled_order_history, ServiceFunction.SRV_CANCELLED_ORDER, R.drawable.nav_cancelled_order_history, R.string.nav_cancelled_order_history, "CancelledOrderListActivity"));
			menuItem.add(alItemTmp);
			alItemTmp = new ArrayList<DashboardItem>();
			alItemTmp.add(new DashboardItem(R.drawable.db_setting, R.string.db_setting, ServiceFunction.SRV_SETTING, R.drawable.nav_setting, R.string.nav_settting, "SettingActivity"));
			alItemTmp.add(new DashboardItem(R.drawable.db_setting, R.string.db_setting_id, ServiceFunction.SRV_SETTING_ID, R.drawable.nav_setting, R.string.nav_settting, "SettingIDActivity"));
			menuItem.add(alItemTmp);

	}
	
	public static ArrayList<DashboardItem> getAlDashboardItem(){
		ArrayList<DashboardItem> alDashboardItem = new ArrayList<DashboardItem>(); 
		for (ArrayList<DashboardItem> al : alItem){
			for (DashboardItem item : al){
				if (item.iService != ServiceFunction.SRV_DEAL && item.iService != ServiceFunction.SRV_ORDER)
					alDashboardItem.add(item);
			}
		}
		//alDashboardItem.add(new DashboardItem(0, R.string.db_main_page, 0, 0, R.string.nav_main_page));
		// alDashboardItem.add(new DashboardItem(0, R.string.db_main_page, ServiceFunction.SRV_DASHBOARD, 0, R.string.nav_main_page));
		return alDashboardItem;
	}

	public static ArrayList<DashboardItem> getAlDashboardItemFirstPage(){
		ArrayList<DashboardItem> alDashboardItem = new ArrayList<DashboardItem>(); 
		ArrayList<DashboardItem> al = alItem.get(0);
			for (DashboardItem item : al){
				if (item.iService != ServiceFunction.SRV_DEAL && item.iService != ServiceFunction.SRV_ORDER)
					alDashboardItem.add(item);
			}
		//alDashboardItem.add(new DashboardItem(0, R.string.db_main_page, 0, 0, R.string.nav_main_page));
		alDashboardItem.add(new DashboardItem(0, R.string.db_main_page, ServiceFunction.SRV_DASHBOARD, 0, R.string.nav_main_page,"test"));
		return alDashboardItem;
	}
	
	public static DashboardItem getDashboardItemByName(Resources res,
			String sStartPage) {
		for (DashboardItem item : getAlDashboardItem()){
			if (sStartPage.equals((String) (res.getText(item.iDesc))) || sStartPage.equals((String) (res.getText(item.iNavDesc)))){
				return item;
			}
		}
		return null;		
	}
	
	public static DashboardItem getDashboardItemByIDesc(int iDesc){
		for (DashboardItem item : getAlDashboardItem()){
			if (iDesc == item.iDesc){
				return item;
			}
		}
		return null;
	}	

	public static DashboardItem getDashboardItemByIService(int iService){
		for (DashboardItem item : getAlDashboardItem()){
			if (iService == item.iService){
				return item;
			}
		}
		return null;
	}	
}