package com.mfinance.everjoy.app.content;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Master;

public class MasterListingActivitiy extends ListingBaseActivity {
	
	Master selectedMaster =null;
	int selectedIndex=0;
	int totalMasterCount = 0;
	
	LinearLayout llLeft;
	LinearLayout llRight;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);  
        
        ListView list = (ListView) findViewById(getListViewId());  
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bundle = new Bundle();
				//bundle.putString(ServiceFunction.SEND_SELECTED_KEY, arg1.getTag().toString());
				bundle.putInt(ServiceFunction.SEND_SELECTED_KEY, Integer.valueOf(arg1.getTag().toString()));
				goTo(ServiceFunction.SRV_MOVE_TO_MASTER_DETAIL ,bundle);
			}
		});
        
        OnClickListener leftClick = new OnClickListener(){
			@Override
			public void onClick(View v) {
				//System.out.println("Left clicked");
				setPreMasterIndex();
				onResume();				
			}			
		};
        
        findViewById(R.id.llLeft).setOnClickListener(leftClick);
        findViewById(R.id.btnLeft).setOnClickListener(leftClick);
        
        OnClickListener rightClick = new OnClickListener(){
			@Override
			public void onClick(View v) {
				//System.out.println("Right clicked");
				setNextMasterIndex();
				onResume();		
			}			
		};
		
		findViewById(R.id.llRight).setOnClickListener(rightClick);        
		findViewById(R.id.btnRight).setOnClickListener(rightClick);  
		
		llLeft  = (LinearLayout)findViewById(R.id.llLeft);
		llRight = (LinearLayout)findViewById(R.id.llRight);
    }	
	
	@Override
	public BaseListAdapter<?, ?> getAdapter() {
		return new MasterListAdapter(this, getHashMapRecord());
	}	
	
	@Override
	public int getHeaderId() {
		return R.id.llHeader;
	}

	@Override
	public int getHeaderTemplateId() {
		return R.layout.h_t5;
	}

	@Override
	public int getListTemplateId() {
		return R.layout.l_t5;
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
	public void onResume(){
		super.onResume();
		
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
		return new int[]{R.id.tvTitle, R.string.db_master};
	}

	@Override
	public int[][] headerTextMapping() {
		return null;
	}
	
	public HashMap<Integer, Master> getHashMapRecord() {
		if (isLoadedData()) {
			selectedIndex = ((MobileTraderApplication)app).getSelectedMasterIndex();
			if (selectedIndex==totalMasterCount && selectedIndex !=0  )
				selectedIndex = totalMasterCount-1;
			if (selectedIndex<0) 
					selectedIndex = 0;
			((MobileTraderApplication)app).setSelectedMasterIndex(selectedIndex);
			try {
				selectedMaster = ((MobileTraderApplication)app).data.getAlEmailMaster().get(selectedIndex).get(0);
				return ((MobileTraderApplication)app).data.getMasters().getMasterMap(((MobileTraderApplication)app).data.getAlEmailMaster().get(selectedIndex));
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int getServiceId(){
		return ServiceFunction.SRV_MOVE_TO_MASTER_LIST;
	}
	
	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_MASTER_LIST;
	}
	
	@Override
	public boolean isLoadedData(){
		return    ( ((MobileTraderApplication)app).data.getMasters()!=null && 
				    ((MobileTraderApplication)app).data.getMasters().getMasterList()!=null);
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
			((MasterListAdapter)adp).assignRecord(getHashMapRecord());
			adp.notifyDataSetChanged();
		}
		
		totalMasterCount = ((MobileTraderApplication)app).data.getAlEmailMaster().size();
		
		int lVisibility = selectedIndex > 0 && selectedIndex <= totalMasterCount-1  ? View.VISIBLE : View.INVISIBLE;
		int rVisibility = (selectedIndex == totalMasterCount-1)  ? View.INVISIBLE : View.VISIBLE;
		
		llLeft.setVisibility(lVisibility);
		llRight.setVisibility(rVisibility);
		
		((ListView)findViewById(R.id.lvContent)).setOnScrollListener(new ImageViewOnScrollListener((ImageView)findViewById(R.id.ivArrowUp),(ImageView)findViewById(R.id.ivArrowDown)));
		
		vHeader.findViewById(R.id.ll1).setVisibility(View.GONE);
		((TextView)vHeader.findViewById(R.id.lbH11)).setText(res.getText(R.string.db_master));
		if (selectedMaster!=null){
			if (Utility.isSimplifiedChineses()) {
				((TextView)vHeader.findViewById(R.id.lbH21)).setText(selectedMaster.getMasternameGB());
				((TextView)vHeader.findViewById(R.id.lbH22)).setText(selectedMaster.getPostGB());
			} else if (Utility.isTraditionalChinese()) {
				((TextView)vHeader.findViewById(R.id.lbH21)).setText(selectedMaster.getMasternameBig5());
				((TextView)vHeader.findViewById(R.id.lbH22)).setText(selectedMaster.getPostBig5());
			} else {
				((TextView)vHeader.findViewById(R.id.lbH21)).setText(selectedMaster.getMasternameEN());
				((TextView)vHeader.findViewById(R.id.lbH22)).setText(selectedMaster.getPostEN());
			}
			((TextView)vHeader.findViewById(R.id.lbH31)).setText(res.getText(R.string.lb_email));
			((TextView)vHeader.findViewById(R.id.lbH32)).setText(selectedMaster.getEmail());
		}	
				
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
