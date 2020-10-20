package com.mfinance.everjoy.app;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;

import java.util.ArrayList;

import uk.co.jasonfry.android.tools.ui.PageControl;
import uk.co.jasonfry.android.tools.ui.SwipeView;

/* -- Facebook
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
*/

public class DashboardActivity extends BaseActivity{
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 
		super.onCreate(savedInstanceState);
		app.isArrivedDashBoard = true;
	}

	@Override
	public void bindEvent() {
			
	}

	@Override
	public void handleByChild(Message msg) {
		switch(msg.what){
		case ServiceFunction.ACT_RELOAD_DASHBOARD:
			reloadDashboard();
			break;
		}
	}

	@Override
	public void loadLayout() {
		setContentView(R.layout.v_dashboard);	
        
		PageControl mPageControl = (PageControl) findViewById(R.id.page_control);
		((ShapeDrawable)mPageControl.getActiveDrawable()).getPaint().setColor(CompanySettings.PageControlActiveColor);
		((ShapeDrawable)mPageControl.getInactiveDrawable()).getPaint().setColor(CompanySettings.PageControlInactiveColor);
		
        SwipeView mSwipeView = (SwipeView) findViewById(R.id.svMain);
        mSwipeView.setPageControl(mPageControl);
        if(!CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN)
        	reloadDashboard();
		
		/* -- Facebook
		if(isPostFacebookEnable() && !app.mFacebook.isSessionValid()){
			app.mFacebook.authorize(DashboardActivity.this, new String[]{"publish_stream"},Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
		}
		*/
	}
	
    public class ItemAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<DashboardItem>  alItem;
        LayoutInflater m_inflater;
        
        public ItemAdapter(Context c, ArrayList<DashboardItem> alItem){
            mContext = c;
            this.alItem = alItem;
            m_inflater = LayoutInflater.from(c);
        }
 
        public int getCount() {
        	return alItem.size();
        }
 
        public Object getItem(int position) {
            return alItem.get(position);
        }
 
        public long getItemId(int position) {
            return alItem.get(position).iService;
        }
 
        public View getView(int position, View convertView, ViewGroup parent) {
        	if(alItem.size() > 0){
        		if(convertView == null)
        			convertView = m_inflater.inflate(R.layout.db_item, null);
        		
        		DashboardItem item = alItem.get(position);        		
        		Button btn = (Button)convertView.findViewById(R.id.btnItem);
    			btn.setTag(item);
    			btn.setBackgroundResource(item.iBackgroud);
    			TextView tv = (TextView)convertView.findViewById(R.id.tvDesc);
    			
    			//Locale locale = Locale.getDefault();    			
        		//System.out.println(locale.getDisplayCountry() +"-"+locale.getDisplayLanguage() +"-"+locale.getDisplayName() +"-"+locale.getVariant());
    			//System.out.println(res.getText(item.iDesc));
    			
    			tv.setText(item.iDesc);
    			btn.setOnClickListener(new OnClickListener(){
    				@Override
    				public void onClick(View v) {
    					if(!CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN){
	    					if ( (app.bQuit || app.data.getContractList().size() == 0) ){
	    						goTo(ServiceFunction.ACT_DISCONNECT);
	    					} else	    			    	
	    						goTo(((DashboardItem)v.getTag()).iService);
    					}else{
    						goTo(((DashboardItem)v.getTag()).iService);
    					}
    				} 
    			});
    			
    			if(app.bLogon == true && app.data.getBalanceRecord().hedged == true && CompanySettings.ALLOW_STP_ORDER == false)
    			{
    				// if true, need to hide new order button anyway
    				if( item.iDesc == R.string.db_new_order)
    					btn.setVisibility(android.view.View.GONE);
    			}
        	}else{
				return convertView = m_inflater.inflate(R.layout.db_item, null);
			}
    			      
            return convertView;
        }
    }
 
	@Override
	protected void onResume() {
		super.onResume();
		//if(CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN)
		reloadDashboard();
	}
	
    public void reloadDashboard(){
    	if(CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN){
			GridView gv = (GridView)findViewById(R.id.gvItem);
			gv.setAdapter(new ItemAdapter(getApplicationContext(),	DashboardItemRespository.alItem.get(0)));
			gv = (GridView)findViewById(R.id.gvItem2);
			gv.setAdapter(new ItemAdapter(getApplicationContext(), DashboardItemRespository.alItem.get(1)));	
			gv = (GridView)findViewById(R.id.gvItem3);
			gv.setAdapter(new ItemAdapter(getApplicationContext(), DashboardItemRespository.alItem.get(2)));	
			
			if(app.bLogon){
				findViewById(R.id.gvItem).setVisibility(View.VISIBLE);
				findViewById(R.id.gvItem2).setVisibility(View.VISIBLE);
				findViewById(R.id.gvItem3).setVisibility(View.VISIBLE);
				
				PageControl mPageControl = (PageControl) findViewById(R.id.page_control);
				mPageControl.setVisibility(View.VISIBLE);
			}else{
				findViewById(R.id.gvItem).setVisibility(View.VISIBLE);
				findViewById(R.id.gvItem2).setVisibility(View.GONE);
				findViewById(R.id.gvItem3).setVisibility(View.GONE);
				
				PageControl mPageControl = (PageControl) findViewById(R.id.page_control);
				mPageControl.setVisibility(View.INVISIBLE);
			}
		}else{
    		GridView gv = (GridView)findViewById(R.id.gvItem);
    		gv.setAdapter(new ItemAdapter(getApplicationContext(),	DashboardItemRespository.alItem.get(0)));
    	
    		gv = (GridView)findViewById(R.id.gvItem2);
    		gv.setAdapter(new ItemAdapter(getApplicationContext(), DashboardItemRespository.alItem.get(1)));
    	}
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
		return false;
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
	public void onBackPressed() {
		//super.onBackPressed();
				
		new AlertDialog.Builder(DashboardActivity.this, CompanySettings.alertDialogTheme)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(res.getString(R.string.title_information))
        .setMessage(res.getString(R.string.msg_quit))
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if(app.bLogon)
            		goTo(ServiceFunction.SRV_LOGOUT);
            	else{
            		finish();
            		}
            }

        })
        .setNegativeButton(R.string.no, null)
        .show();
	}	
	
	@Override
	public int getActivityServiceCode(){
		return ServiceFunction.SRV_DASHBOARD;
	}
	
}