package com.mfinance.everjoy.app.content;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;

public class HourProductListingActivitiy extends ListingBaseActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);  
        ListView list = (ListView) findViewById(getListViewId());  
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bundle = new Bundle();
				bundle.putInt(ServiceFunction.SEND_SELECTED_KEY, Integer.valueOf(arg1.getTag().toString()));
				goTo(ServiceFunction.SRV_MOVE_TO_HOUR_PRODUCT_DETAIL ,bundle);
			}
		});    
    }	
	
	@Override
	public BaseListAdapter<?, ?> getAdapter() {
		return new HourProductListAdapter(this, app.data.getHmHourproducts() );
	}	
	
	@Override
	public int getHeaderId() {
		return -1;
	}

	@Override
	public int getHeaderTemplateId() {
		return -1;
	}

	@Override
	public int getListTemplateId() {
		return R.layout.l_t9;
	}

	@Override
	public int getListViewId() {
		return R.id.lvContent;
	}

	@Override
	public void bindEvent() {
	}

	@Override
	public void updateUI() {
	}
	
	@Override
	public int[][] functionTextMapping() {
		return null;
	}

	@Override
	public HashMap<Integer, OnClickListener> getFunctionClickListener() {
		return null;
	}

	@Override
	public int[] getTitleText() {
		return new int[]{R.id.tvTitle, R.string.db_hour_product};
	}

	@Override
	public int[][] headerTextMapping() {
		return null;
	}

	@Override
	public int getServiceId(){
		return ServiceFunction.SRV_MOVE_TO_HOUR_PRODUCT;
	}	
	
	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_HOUR_PRODUCT;
	}
	
	@Override
	public boolean isLoadedData(){
		return    ( (app).data.getHourproducts()!=null && 
				    (app).data.getHourproducts().getHourProductList()!=null && 
				    (app).data.getHourproducts().getHourProductList().size() > 0);
	}	
	
	@Override
	public int getLoadingViewId() {
		return R.id.flLoading;
	}	
	
	
	@Override
	public void loadUIData(){
		if(adp == null){
			adp = getAdapter();			
			lv.setAdapter(adp);			
		}else{
			((HourProductListAdapter)adp).assignRecord((app).data.getHmHourproducts());
			adp.notifyDataSetChanged();
		}
		((ListView)findViewById(R.id.lvContent)).setOnScrollListener(new ImageViewOnScrollListener((ImageView)findViewById(R.id.ivArrowUp),(ImageView)findViewById(R.id.ivArrowDown)));
	}	
	

	@Override
	public void handleByChild(Message msg) {
		// TODO Auto-generated method stub
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
	
}