package com.mfinance.everjoy.app;


import android.os.Message;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.widget.pageswitcher.IndexChangeListener;
import com.mfinance.everjoy.app.widget.pageswitcher.PageSlider;

public class HistoryListActivity extends BaseActivity{
	public int iIndex = 0;
	PageSlider psList;
	
	@Override
	public void bindEvent() {
			
	}
	
	@Override
	public void handleByChild(Message msg) {}

	@Override
	public void loadLayout() {
		setContentView(R.layout.v_history);
		
		PageSlider ps = (PageSlider)findViewById(R.id.psList);
		psList = (PageSlider)findViewById(R.id.psList2);
		ps.setOnIndexChangeListener(new IndexChangeListener(){
			@Override
			public void indexChange(int iIndex) {
				psList.goToPage(iIndex);
				//System.out.println("Move list : "+iIndex);
			}
		});
	}
	
	
	@Override
	public void updateUI() {
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