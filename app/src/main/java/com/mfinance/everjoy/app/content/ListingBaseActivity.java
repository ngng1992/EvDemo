package com.mfinance.everjoy.app.content;

import java.util.HashMap;

import com.mfinance.everjoy.app.BaseActivity;

import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public abstract class ListingBaseActivity extends BaseActivity {
	BaseListAdapter<?, ?> adp;
	ListView lv;
	View vHeader;
	
	@Override
	public void loadLayout() {
		setContentView(getListTemplateId());
		
		if (getHeaderTemplateId() > 0 && getHeaderId() > 0 ){
			vHeader = inflater.inflate(getHeaderTemplateId(), null);
			LinearLayout llHeader = (LinearLayout)findViewById(getHeaderId());
			LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			llHeader.addView(vHeader,p);
		}
		
		adp = getAdapter();
		lv = (ListView)findViewById(getListViewId());
		lv.setAdapter(adp);
		
		lv.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				//System.out.println("[firstVisibleItem "+firstVisibleItem+"][visibleItemCount "+visibleItemCount+"][totalItemCount "+totalItemCount+"]");
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
		});
		int[][] mapping = headerTextMapping();
		if (mapping!=null){
			for(int i = 0; i < mapping.length; i++){
				((TextView)vHeader.findViewById(mapping[i][0])).setText(res.getText(mapping[i][1]));
			}
		}
		
		HashMap<Integer, OnClickListener> hmHandler = getFunctionClickListener();
		mapping = functionTextMapping();
		if (mapping!=null){
			for(int i = 0; i < mapping.length; i++){
				int iID = mapping[i][0];
				Button btn = (Button)findViewById(iID);
				OnClickListener handler = hmHandler.get(iID);
				
				if(handler == null){
					btn.setVisibility(View.GONE);
				}else{
					btn.setText(res.getText(mapping[i][1]));
					btn.setOnClickListener(handler);
				}
			}
		}
	}

	public abstract int getListTemplateId();
	public abstract int getHeaderTemplateId();
	public abstract int getHeaderId();
	public abstract int getListViewId();
	public abstract BaseListAdapter<?, ?> getAdapter();
	
	public abstract int[][] headerTextMapping();
	public abstract int[][] functionTextMapping();
	public abstract HashMap<Integer, OnClickListener> getFunctionClickListener();
	
}
