package com.mfinance.everjoy.app.presenter;

import android.content.res.Resources;
import android.os.Messenger;
import android.view.View;
import android.view.View.OnClickListener;

import com.mfinance.everjoy.app.BasePopupDetailListener;
import com.mfinance.everjoy.app.MobileTraderApplication;

public abstract class BaseDetailPresenter {
	protected BasePopupDetailListener view;
	protected Resources res;
	protected MobileTraderApplication app;
	protected Messenger mService = null;
	protected Messenger mServiceMessengerHandler = null;		
	
    public BaseDetailPresenter(BasePopupDetailListener view, Resources res, MobileTraderApplication app, Messenger mService, Messenger mServiceMessengerHandler){
        this.view = view;
        this.res = res;
        this.app = app;
        this.mService = mService;
        this.mServiceMessengerHandler = mServiceMessengerHandler;
    }
    
    public void bindEvent(){
    	view.getCloseBtn().setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				view.dismiss();
			}
    		
    	});
    }
    
    public abstract void updateUI();
}
