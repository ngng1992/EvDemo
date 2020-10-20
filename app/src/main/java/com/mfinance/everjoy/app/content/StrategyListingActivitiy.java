package com.mfinance.everjoy.app.content;

import java.util.HashMap;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfinance.everjoy.app.BaseActivity;
import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.hungkee.xml.Strategy;

public class StrategyListingActivitiy extends BaseActivity {
	
	Strategy selectedStrategy =null;
	int selectedIndex=0;
	int totalStrategyCount = 0;
	
	LinearLayout llLeft;
	LinearLayout llRight;
	
	StrategyListAdapter adapter;
	View vHeader;
	ExpandableListView elv;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        
        OnClickListener leftClick = new OnClickListener(){
			@Override
			public void onClick(View v) {
				//System.out.println("Left clicked");
				setPreStrategyIndex();
				onResume();				
			}			
		};
        
        findViewById(R.id.llLeft).setOnClickListener(leftClick);
        findViewById(R.id.btnLeft).setOnClickListener(leftClick);
        
        OnClickListener rightClick = new OnClickListener(){
			@Override
			public void onClick(View v) {
				//System.out.println("Right clicked");
				setNextStrategyIndex();
				onResume();		
			}			
		};
		
		findViewById(R.id.llRight).setOnClickListener(rightClick);
		findViewById(R.id.btnRight).setOnClickListener(rightClick);  
		
		llLeft  = (LinearLayout)findViewById(R.id.llLeft);
		llRight = (LinearLayout)findViewById(R.id.llRight);      
		
		elv = (ExpandableListView) findViewById(R.id.elvContent);	
		elv.setGroupIndicator(getResources().getDrawable(android.R.color.transparent));		
		elv.setOnChildClickListener(new OnChildClickListener(){
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Bundle bundle = new Bundle();
				bundle.putString(ServiceFunction.SEND_SELECTED_KEY, ((Strategy)adapter.getChild(groupPosition, childPosition)).getKey());
				goTo(ServiceFunction.SRV_MOVE_TO_STRATEGY_DETAIL  , bundle);
				return false;
			}
		});
		
        
	}

	@Override
	public void bindEvent() {
	}

	@Override
	public void loadLayout() {
		setContentView(R.layout.l_t4);
		vHeader = inflater.inflate(R.layout.h_t3, null);
		LinearLayout llHeader = (LinearLayout)findViewById(R.id.llHeader);
		LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		llHeader.addView(vHeader, 0, p);
		
	}

	@Override
	public void updateUI() {
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
	public StrategyListAdapter getAdapter() {
		return new StrategyListAdapter(this, getHashMapRecord());
	}	
	
	public HashMap<Integer, Strategy> getHashMapRecord() {
		selectedIndex = ((MobileTraderApplication)app).getSelectedStrategyIndex();
		if (selectedIndex==totalStrategyCount && selectedIndex !=0)
			selectedIndex = totalStrategyCount-1;
		if (selectedIndex<0) 
			selectedIndex = 0;
		((MobileTraderApplication)app).setSelectedStrategyIndex(selectedIndex);
		try {
			selectedStrategy = ((MobileTraderApplication)app).data.getAlDailyStrategy().get(selectedIndex).get(0);
			return ((MobileTraderApplication)app).data.getStrategies().getStrategyMap(((MobileTraderApplication)app).data.getAlDailyStrategy().get(selectedIndex));
		} catch (Exception e){
			//e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int getServiceId(){
		return ServiceFunction.SRV_MOVE_TO_STRATEGY_LIST;
	}

	@Override
	public int[] getTitleText() {
		return new int[]{R.id.tvTitle, R.string.db_day_plan};
	}		
	
	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_STRATEGY_LIST;
	}	
	
	@Override
	public boolean isLoadedData(){
		return    ( ((MobileTraderApplication)app).data.getStrategies()!=null && 
				    ((MobileTraderApplication)app).data.getStrategies().getStrategyList()!=null);
	}	
	
	@Override
	public int getLoadingViewId() {
		return R.id.flLoading;
	}	
	
	
	@Override
	public void loadUIData(){
		try{
		totalStrategyCount = ((MobileTraderApplication)app).data.getAlDailyStrategy().size();
		if(adapter == null){
			adapter = getAdapter();			
			elv.setAdapter(adapter);			
		}else{
			((StrategyListAdapter)adapter).assignRecord(getHashMapRecord());
			if (((StrategyListAdapter)adapter).hmRecord !=null) 
				adapter.notifyDataSetChanged();
		}
		
		for (int i=0; i<= adapter.getGroupCount(); i++){
			elv.expandGroup(i);
		}

		int lVisibility = selectedIndex > 0 && selectedIndex <= totalStrategyCount-1  ? View.VISIBLE : View.INVISIBLE;
		int rVisibility = (selectedIndex == totalStrategyCount-1)  ? View.INVISIBLE : View.VISIBLE;
		
		llLeft.setVisibility(lVisibility);
		llRight.setVisibility(rVisibility);
		
		((TextView)vHeader.findViewById(R.id.lbH11)).setText(selectedStrategy.issueDate.split(" ")[0]);
		
		if (MobileTraderApplication.isNeedFontBold){
			((TextView)vHeader.findViewById(R.id.lbH11)).setTextColor(getResources().getColor(R.color.detail_title_bold));
			((TextView)vHeader.findViewById(R.id.lbH11)).setTypeface(null, Typeface.BOLD);
			((TextView)vHeader.findViewById(R.id.lbH11)).getPaint().setFakeBoldText(true);
		}			
		
		elv.setOnScrollListener(new ImageViewOnScrollListener((ImageView)findViewById(R.id.ivArrowUp),(ImageView)findViewById(R.id.ivArrowDown)));
		}catch(Exception e){}			
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

