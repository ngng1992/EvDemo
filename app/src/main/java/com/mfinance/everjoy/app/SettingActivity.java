package com.mfinance.everjoy.app;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;


/* -- Facebook
import com.facebook.android.Facebook;
*/

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.facebook.LoginDialogListener;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.json.JSONObject;
import com.mfinance.everjoy.R;

public class SettingActivity extends BaseActivity{	
	
	Button btnRefreshGeneralTime;
	Button btnContract;
	Button btnStartPage;
	Button btnLot;
	Button btnOK;
	Button btnTimeout;
	ArrayList<ContractObj> popupContractList;
	
	PopupContract popContract;
	PopupLOT popLot;
	PopupDashboardItem popStartPage;
    PopupString popTimeout;
    PopupTime popupRefreshGeneral;
    
	Button btnLanguage;
	PopupString popLanguage;
	String sLang;
	
	IPhoneToggle tbOneClick;
	//IPhoneToggle tbFBClick;
	
	@Override
	public void bindEvent() {
		btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				finish();
			}
		});
		
		if(btnLanguage!=null){
			btnLanguage.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					popLanguage.setSelected(btnLanguage.getText().toString());
					popLanguage.showLikeQuickAction();
				}
			});
			
			popLanguage.btnCommit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){	
					popLanguage.dismiss();
					sLang = popLanguage.getValue();				
					btnLanguage.setText(sLang);
	            	setLanguage(sLang);
	            	Intent i=new Intent(SettingActivity.this,DashboardActivity.class);
					i.putExtra(ServiceFunction.REQUIRE_LOGIN, false);
	            	startActivity(i);
	            	finish();
				}
			});
			
			popLanguage.btnClose.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){	
					popLanguage.dismiss();
				}
			});	
		}
		btnRefreshGeneralTime.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				popupRefreshGeneral.showLikeQuickAction();
				GUIUtility.updateSecondsWheel(popupRefreshGeneral.wvTimes, btnRefreshGeneralTime.getText().toString());
			}
		});	

		popupRefreshGeneral.btnCommit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String sSeconds =popupRefreshGeneral.getValue();
				btnRefreshGeneralTime.setText(sSeconds);				
				popupRefreshGeneral.dismiss();
				SettingActivity.this.setDefaultRefreshGeneralInfo(Integer.parseInt(sSeconds));
				goTo(ServiceFunction.SRV_RESTART_GENERAL_XML_TIMER);
			}
			
		});
		
		popupRefreshGeneral.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popupRefreshGeneral.dismiss();
			}			
		});	
		
		btnContract.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popContract.updateSelectedContract(null, btnContract.getText().toString());
				popContract.showLikeQuickAction();
			}
		});
		
		popContract.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popContract.dismiss();
				String sContract = popContract.getValue();
				// do not look up contract by name since it may not be unique!
				ContractObj contract = popupContractList.get(popContract.getSelectedIndex());
				if (contract!=null){
					btnContract.setText(sContract);
					SettingActivity.this.setDefaultContract(contract.strContractCode);
				}
			}
		});
		
		popContract.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popContract.dismiss();
			}
		});	
		
		btnStartPage.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popStartPage.updateSelectedItem(null, btnStartPage.getText().toString());
				popStartPage.showLikeQuickAction();

			}
		});
		
		popStartPage.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popStartPage.dismiss();
				String sStartPage = popStartPage.getValue();				
				btnStartPage.setText(sStartPage);
				DashboardItem item = DashboardItemRespository.getDashboardItemByName(res, sStartPage);
				SettingActivity.this.app.setDefaultPage(item.iService);
				//goTo(ServiceFunction.SRV_LOGOUT);
			}
		});
		
		popStartPage.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popStartPage.dismiss();
			}
		});			
		
		btnLot.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				popLot.showLikeQuickAction();
				popLot.setValue(btnLot.getText().toString());
			}
			
		});	
		
		popLot.btnCommit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String sLot =popLot.getValue();
				
				boolean bContractWiseSetting = false;
				ArrayList<ContractObj> list = app.data.getContractList();
				for( ContractObj obj : list)
				{
					if( obj.dIncLot > 0 || obj.dMinLot > 0 )
					{
						bContractWiseSetting = true;
						break;
					}
				}
				
				if( !bContractWiseSetting && CompanySettings.VALIDATE_MINLOT_AND_INCLOT == true )
				{
					if( Double.valueOf(sLot) < app.data.getBalanceRecord().dMinLotLimit){
	        			Toast.makeText(SettingActivity.this, MessageMapping.getMessageByCode(res, "620" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotLimit))   , Toast.LENGTH_LONG).show();
	                    return;
					}
					
					if  (((1000 * Double.valueOf(sLot)) % (1000 * app.data.getBalanceRecord().dMinLotIncrementUnit)) != 0){
						Toast.makeText(SettingActivity.this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotIncrementUnit))   , Toast.LENGTH_LONG).show();
						return;
					}
				}
				
				btnLot.setText(sLot);				
				popLot.dismiss();
				if (!CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING)
					SettingActivity.this.setDefaultLOT(sLot);
				SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				JSONObject o = new JSONObject();
				app.data.getContractDefaultSettingMap().forEach((key, value) -> {
					try {
						Utility.putContractDefaultSetting(o, key, value);
					} catch (Exception ex) {

					}
				});
				JSONObject defaultContractSetting;
				try {
					defaultContractSetting = new JSONObject(sharedPreferences.getString("DEFAULT_CONTRACT_SETTING", "{}"));
					final String accountName;
					if (CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING) {
						accountName = Optional.ofNullable(app.data.getBalanceRecord()).map(b -> b.strAccount).orElse("");
					} else {
						accountName = "";
					}
					defaultContractSetting.put(accountName, o);
				} catch (Exception ex) {
					defaultContractSetting = new JSONObject();
				}
				edit.putString("DEFAULT_CONTRACT_SETTING", defaultContractSetting.toString());
				edit.apply();
			}
			
		});
		
		popLot.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popLot.dismiss();
			}			
		});	
		
		tbOneClick.tb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {				
				tbOneClick.setChecked(false, isChecked);
				SettingActivity.this.setOneClickTradeEnable(isChecked);
			}
        });
		
		/* -- Facebook
		tbFBClick.tb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {				
				tbFBClick.setChecked(false, isChecked);
				SettingActivity.this.setPostFacebookEnable(isChecked);
				if (isChecked){// && !app.mFacebook.isSessionValid()){
					app.mFacebook.authorize(SettingActivity.this, new String[]{"publish_stream"},Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
				}
			}
        });		
		*/
		btnTimeout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popTimeout.setSelected(btnTimeout.getText().toString());
				popTimeout.showLikeQuickAction();
			}
		});
		
		popTimeout.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popTimeout.dismiss();
				String sTimeout = popTimeout.getValue();
				int iIndex = popTimeout.getIndex(sTimeout);
				int iSize = popTimeout.getSize();

				if((iIndex + 1)== iSize){
					setTimeout(-1);
				}else{
					setTimeout((iIndex + 1) * 60000 * 15);					
				}	
				btnTimeout.setText(sTimeout);
			}
		});
		
		popTimeout.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popTimeout.dismiss();
			}
		});	
				
	}


	@Override
	public void handleByChild(Message msg) {}

	@Override
	public void loadLayout() {
		if (CompanySettings.newinterface)
			setContentView(R.layout.v_setup_new);
		else
			setContentView(R.layout.v_setup);
		
		if(CompanySettings.ENABLE_CONTENT)
			findViewById(R.id.content_function).setVisibility(View.VISIBLE);
		else
			findViewById(R.id.content_function).setVisibility(View.GONE);

		Consumer<View> setGone = v -> v.setVisibility(View.GONE);
		Consumer<View> setVisible = v -> v.setVisibility(View.VISIBLE);
		Optional.<View>ofNullable(findViewById(R.id.viewDefaultLot)).ifPresent(CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING ? setGone : setVisible);
		Optional.<View>ofNullable(findViewById(R.id.viewDefaultLot2)).ifPresent(CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING ? setGone : setVisible);
		btnOK = (Button)findViewById(R.id.btnOK);
        btnContract = (Button)findViewById(R.id.btnContract);
        ContractObj defaultContract=getDefaultContract();
        if(defaultContract!=null)
        	btnContract.setText(defaultContract.getContractName(getLanguage()));
       if( app.bLogon == true )
       {
    	   popupContractList = app.data.getTradableContractList();
       }
       else
       {
    	   popupContractList = app.data.getContractList();
       }
       popContract = new PopupContract(getApplicationContext(), findViewById(R.id.rlTop), popupContractList);
        
        tbOneClick = new IPhoneToggle((ToggleButton)findViewById(R.id.tbOneClick), res, R.string.tb_on, R.string.tb_off);
        
        btnLanguage = (Button)findViewById(R.id.btnLanguage);
        if(btnLanguage!=null){
			ArrayList<String> alString = new ArrayList<String>();
			alString.add("English");
			alString.add("繁體");
			alString.add("简体");
	        popLanguage = new PopupString(getApplicationContext(), findViewById(R.id.rlTop), alString);	
	        btnLanguage.setText( app.isArrivedDashBoard && getNextLocale() != null ? getNextLocale() : getLanguageName());
        }
        //-- Facebook
        /*
        tbFBClick = new IPhoneToggle((ToggleButton)findViewById(R.id.tbFBClick), res, R.string.tb_on, R.string.tb_off);
        */
		btnLot = (Button)findViewById(R.id.btnLot);
		if (btnLot != null) {
			btnLot.setText(getDefaultLOT());
		}
		ContractObj contract = defaultContract;
		popLot = new PopupLOT(getApplicationContext(), findViewById(R.id.rlTop), BigDecimal.valueOf(contract.getFinalMaxLotLimit()));
		
		popStartPage =  new PopupDashboardItem(getApplicationContext(), findViewById(R.id.rlTop), CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN?DashboardItemRespository.getAlDashboardItemFirstPage():DashboardItemRespository.getAlDashboardItem());
		btnStartPage = (Button)findViewById(R.id.btnStartPage);
		btnTimeout = (Button)findViewById(R.id.btnTimeout);
		
		CharSequence[] alTimeout = res.getTextArray(R.array.sp_time_out);
		popTimeout = new PopupString(getApplicationContext(), findViewById(R.id.rlTop), Utility.toArrayList(alTimeout));	
		
		btnRefreshGeneralTime = (Button)findViewById(R.id.btnRefreshGeneralTime);
		
		//This is setting the default value from SharedPreferences
		btnRefreshGeneralTime.setText(String.valueOf(getDefaultRefreshGeneralInfo()));
		popupRefreshGeneral = new PopupTime(getApplicationContext(), findViewById(R.id.rlTop));
		
		long lTimeout = getTimeout();
		if (app.data.sessTimeoutDisconn != -1){
			btnTimeout.setText(String.valueOf(app.data.sessTimeoutDisconn/60) + "mins");
			btnTimeout.setEnabled(false);
			btnTimeout.setAlpha((float)0.5);
		}
		else {
			if (lTimeout == -1) {
				btnTimeout.setText(alTimeout[alTimeout.length - 1]);
			} else {
				int iIndex = (int) ((lTimeout / 15 / 60000) - 1);
				try {
					btnTimeout.setText(alTimeout[iIndex]);
				} catch (Exception e) {
					btnTimeout.setText(alTimeout[alTimeout.length - 1]);
				}
			}
		}
		
		int iService  = app.getDefaultPage();
		DashboardItem dashboardItem=DashboardItemRespository.getDashboardItemByIService(iService);
		
		if(dashboardItem!=null)
			btnStartPage.setText(res.getString(dashboardItem.iNavDesc));
		
		tbOneClick.setChecked(true, isOneClickTradeEnable());
		View vwEnableOneClickTrade1 = findViewById(R.id.vwEnableOneClickTrade1);
		View vwEnableOneClickTrade2 = findViewById(R.id.vwEnableOneClickTrade2);
		View vwEnableOneClickTrade3 = findViewById(R.id.vwEnableOneClickTrade3);
		if (CompanySettings.ENABLE_ONE_CLICK_TRADE) {
			vwEnableOneClickTrade1.setVisibility(View.VISIBLE);
			vwEnableOneClickTrade2.setVisibility(View.VISIBLE);
			vwEnableOneClickTrade3.setVisibility(View.VISIBLE);
		} else {
			vwEnableOneClickTrade1.setVisibility(View.GONE);
			vwEnableOneClickTrade2.setVisibility(View.GONE);
			vwEnableOneClickTrade3.setVisibility(View.GONE);
		}
		
		loginOrLogoutScreenUpdate();
	}

	@Override
	public void updateUI() {
		//tbOneClick.setChecked(true, isOneClickTradeEnable());
		/* -- Facebook
		tbFBClick.setChecked(true, isPostFacebookEnable());
		*/
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
	
	@Override
	public int getServiceId() {
		return ServiceFunction.SRV_SETTING;
	}			
	
	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_SETTING;
	}	
	
	@Override
	public void loginOrLogoutScreenUpdate(){
		if (app.bLogon)
			findViewById(R.id.login_function).setVisibility(View.VISIBLE);
		else
			findViewById(R.id.login_function).setVisibility(View.GONE);
	}
}