package com.mfinance.everjoy.app;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;

public class PopupForgotPassword {
	public Context context;
	public CustomPopupWindow popup;	
	public Button btnClose;
	public Resources res;
	public String sURL;
	
	public PopupForgotPassword(Context context, View vParent, Resources res, String sURL){
		this.context = context;
		popup = new CustomPopupWindow(vParent);			
		popup.setContentView(R.layout.popup_forgot_password);

		btnClose = (Button)popup.findViewById(R.id.btnClose);		
		this.res = res;
		this.sURL = sURL;
		bindEvent();
	}
	
	public void clearFields(){
		((EditText)popup.findViewById(R.id.etID)).getEditableText().clear();
		((EditText)popup.findViewById(R.id.etEmail)).getEditableText().clear();
	}
	
	public void bindEvent(){

	}
	
	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}
	
	public void dismiss(){
		popup.dismiss();
	}

	public String getURL(){
		String sUser = ((EditText)popup.findViewById(R.id.etID)).getEditableText().toString();
		String sEmail = ((EditText)popup.findViewById(R.id.etEmail)).getEditableText().toString();
		
		if(sUser.equals("")){
			Toast.makeText(context, res.getString(R.string.msg_register_1), Toast.LENGTH_LONG).show();
			return null;	
		}else if(sEmail.equals("")){
			Toast.makeText(context, res.getString(R.string.msg_register_4), Toast.LENGTH_LONG).show();
			return null;
		}else{
			return sURL+"id="+CompanySettings.COMPANY_PREFIX + sUser+"&"+"email="+sEmail;	
		}
	}
	
}
