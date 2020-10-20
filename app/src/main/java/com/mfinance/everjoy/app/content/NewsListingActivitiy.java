package com.mfinance.everjoy.app.content;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.hungkee.xml.News;
import com.mfinance.everjoy.hungkee.xml.Newses;

public class NewsListingActivitiy extends ListingBaseActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);  
        ListView list = (ListView) findViewById(getListViewId());  
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bundle = new Bundle();
				//System.out.println("[Set]Key:" + arg1.getTag().toString());
				bundle.putInt(ServiceFunction.SEND_SELECTED_KEY, Integer.valueOf(arg1.getTag().toString()));
				goTo(ServiceFunction.SRV_MOVE_TO_NEWS_DETAIL ,bundle);
			}
		});    
    }		
	@Override
	public BaseListAdapter<?, ?> getAdapter() {
		return new NewsListAdapter(this, ((MobileTraderApplication)app).data.getHmNewses());
	}
	
	public HashMap<String, News> getNewsMap(Newses newses){
		List<News> lNews = newses.getNewsList();
		
		if(lNews == null){
			return null;
		}else{
			HashMap<String, News> hmNews = new HashMap<String, News>();
			for(News n : lNews){
				hmNews.put(String.valueOf(n), n);
			}
			return hmNews;			
		}
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
		//No hightlight
		//return R.layout.l_t7; 
		return R.layout.l_t3;
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
		if(adp == null){
			adp = getAdapter();			
			lv.setAdapter(adp);			
		}else{
			((NewsListAdapter)adp).assignRecord(((MobileTraderApplication)app).data.getHmNewses());
			adp.notifyDataSetChanged();
		}
		((ListView)findViewById(R.id.lvContent)).setOnScrollListener(new ImageViewOnScrollListener((ImageView)findViewById(R.id.ivArrowUp),(ImageView)findViewById(R.id.ivArrowDown)));
	}
	
	@Override
	public void onResume(){
		super.onResume();
		/*
		if(adp == null){
			adp = getAdapter();			
			lv.setAdapter(adp);			
		}else{
			((NewsListAdapter)adp).assignRecord(((MobileTraderApplication)app).data.getHmNewses());
			adp.notifyDataSetChanged();
		}
		((ListView)findViewById(R.id.lvContent)).setOnScrollListener(new ImageViewOnScrollListener((ImageView)findViewById(R.id.ivArrowUp),(ImageView)findViewById(R.id.ivArrowDown)));
		*/		
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
		return new int[]{R.id.tvTitle, R.string.db_realtime_news};
	}

	@Override
	public int[][] headerTextMapping() {
		return null;
	}
	
	@Override
	public int getServiceId(){
		return ServiceFunction.SRV_MOVE_TO_NEWS_LIST;
	}
	
	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_NEWS_LIST;
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
