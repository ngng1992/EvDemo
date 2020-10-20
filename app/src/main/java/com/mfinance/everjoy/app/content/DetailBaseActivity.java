package com.mfinance.everjoy.app.content;


import com.mfinance.everjoy.app.BaseActivity;

import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class DetailBaseActivity extends BaseActivity{
	
	protected View vHeader = null;
	
	@Override
	public void loadLayout() {
		setContentView(getContentTemplateId());
		if(getHeaderTemplateId()!=-1){
		vHeader = inflater.inflate(getHeaderTemplateId(), null);
		
		LinearLayout llHeader = (LinearLayout)findViewById(getHeaderId());
		LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		llHeader.addView(vHeader, 0, p);	
		}
	}
	
	public abstract int getHeaderId();
	public abstract int getHeaderTemplateId();
	public abstract int getContentTemplateId();

}
