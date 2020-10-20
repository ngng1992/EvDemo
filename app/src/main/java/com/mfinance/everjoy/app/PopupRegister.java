package com.mfinance.everjoy.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;

public class PopupRegister {
	public Context context;
	public CustomPopupWindow popup;	
	public Button btnClose;
	public Resources res;
	public String sURL;
	private static final Pattern pattern = Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");

	public PopupRegister(Context context, View vParent, Resources res, String sURL){
		this.context = context;
		popup = new CustomPopupWindow(vParent);			
		popup.setContentView(CompanySettings.ENABLE_DEMO_REGISTER? R.layout.popup_acc_register:R.layout.popup_acc_register_msg);

		btnClose = (Button)popup.findViewById(R.id.btnClose);		
		this.res = res;
		this.sURL = sURL;
		bindEvent();
	}
	
	public void bindEvent(){

	}

	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}

	public void dismiss(){
		popup.dismiss();
	}
	
	public String getUserName(){
		String sUser = ((EditText)popup.findViewById(R.id.etID)).getEditableText().toString();
		return sUser;
	}
	
	public String getPassword(){
		String sPassword = ((EditText)popup.findViewById(R.id.etPassword)).getEditableText().toString();
		return sPassword;
	}
	
	public void clearFields(){
		((EditText)popup.findViewById(R.id.etID)).getEditableText().clear();
		((EditText)popup.findViewById(R.id.etPassword)).getEditableText().clear();
		((EditText)popup.findViewById(R.id.etCPassword)).getEditableText().clear();
		((EditText)popup.findViewById(R.id.etEmail)).getEditableText().clear();
	}

	public String getURL(){
		String sUser = getUserName();
		//sUser = MobileTraderApplication.REPORT_GROUP+sUser;
		String sPassword = getPassword();
		String sCPassword = ((EditText)popup.findViewById(R.id.etCPassword)).getEditableText().toString();
		String sEmail = ((EditText)popup.findViewById(R.id.etEmail)).getEditableText().toString();
		
		if(sUser.equals("")){
			Toast.makeText(context, res.getString(R.string.msg_register_1), Toast.LENGTH_LONG).show();
			return null;
		}else if(!sUser.matches("^[a-z0-9]*$"))
		{
			Toast.makeText(context, res.getString(R.string.msg_register_7), Toast.LENGTH_LONG).show();
			return null;
		}
		else if(sPassword.length()<CompanySettings.MINIMUM_PASSWORD_SIZE){
			Toast.makeText(context, res.getString(R.string.msg_register_6).replace("#num#", String.valueOf(CompanySettings.MINIMUM_PASSWORD_SIZE)), Toast.LENGTH_LONG).show();
			return null;
		}else if(sPassword.equals("")){
			Toast.makeText(context, res.getString(R.string.msg_register_2), Toast.LENGTH_LONG).show();
			return null;
		}else if(sCPassword.equals("")){
			Toast.makeText(context, res.getString(R.string.msg_register_2), Toast.LENGTH_LONG).show();
			return null;
		}else if(!sPassword.equals(sCPassword)){
			Toast.makeText(context, res.getString(R.string.msg_register_5), Toast.LENGTH_LONG).show();
			return null;
		}else if(sEmail.equals("")){
			Toast.makeText(context, res.getString(R.string.msg_register_4), Toast.LENGTH_LONG).show();
			return null;
		}else if(!isEmailVaild(sEmail)){
			Toast.makeText(context, res.getString(R.string.msg_register_4), Toast.LENGTH_LONG).show();
			return null;
		}else{
			//Log.e("getRegURL", sURL + "USER=" + sUser + "&PASS=" + sPassword + "&EMAIL=" + sEmail);
			return sURL + "USER=" + CompanySettings.COMPANY_PREFIX + sUser + "&PASS=" + sPassword + "&EMAIL=" + sEmail + "&GROUP=" + CompanySettings.DEMO_REPORT_GROUP_NAME;
		}
	}
	
	private boolean isEmailVaild(String email){
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
