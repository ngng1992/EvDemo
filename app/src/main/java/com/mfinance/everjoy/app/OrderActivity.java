package com.mfinance.everjoy.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.DefaultCompanySettings.OrderTimeLimit;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.ContractDefaultSetting;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.GUIUtilityUpdateRateViewHolder;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.wheel.WheelView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.simpleframework.xml.Order;

public class OrderActivity extends BaseActivity{
	public enum UpdateType {
		INCOMING_PRICE, USER_CHANGE_INPUT, ON_RESUME
	}
	String pid = null;
	IPhoneToggle tbBuySell;
	IPhoneToggle tbLimitStop;
	IPhoneToggle tbGoodTill;
	Button btnGoodTill;
	PopupGoodTill popGoodTill;
	
	Button btnContract;		
	Button btnLot;
	Button btnSubmit;
	Button btnRate;
	Button btnOCO;
	
	TextView tvCurRate;
	TextView tvDirection;

	PopupOrder popOrder;
	PopupLOT popLot;
	PopupRate popRate;
	PopupContract popContract;
	
	ArrayList<ContractObj> popupContractList; 
	
	String ocoValue="-";
	
	Button btnLimit;
	Button btnStop;
	IPhoneCheckbox tbLimit;
	IPhoneCheckbox tbStop;
	IPhoneToggle tbLimit0;
	IPhoneToggle tbStop0;
	PopupRate popLRate;
	PopupRate popSRate;
	TextView tvLimit;
	TextView tvStop;
	TextView tvLDirect;
	TextView tvSDirect;

	GUIUtilityUpdateRateViewHolder limitViewHolder;
	GUIUtilityUpdateRateViewHolder stopViewHolder;
	GUIUtilityUpdateRateViewHolder currentViewHolder;

	RelativeLayout view_limit;
	RelativeLayout view_stop;
	TextView label_limit;
	TextView label_stop;
	TextView label_contract;
	
	@Override
	public void bindEvent() {
		if( CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel) )
		{
			btnGoodTill.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					popGoodTill.updateSelectedGoodTill(btnGoodTill.getText().toString());
					popGoodTill.showLikeQuickAction();
				}
			});
			
			popGoodTill.btnCommit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					btnGoodTill.setText(popGoodTill.getValue());
					popGoodTill.dismiss();
				}
			});
			
			popGoodTill.btnClose.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					popGoodTill.dismiss();
				}
			});
		}
		
		
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
				// do not look up contract by name since it may not be unique!;
				app.setSelectedContract(popupContractList.get(popContract.getSelectedIndex()));
				btnContract.setText(sContract);
				ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(app.getSelectedContract().strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
                GUIUtility.updateRate(true, app.getSelectedContract(), app.getSelectedBuySell(), app.getSelectedLimitStop(), currentViewHolder, res, defaultSetting);
				updateOrderRate(UpdateType.ON_RESUME);
				OpenPositionRecord position = app.getSelectedOpenPosition();
				if (position == null) {
					btnLot.setText(getDefaultLOT());
				}
				updateDealRef();
				updatePopLot();
			}
		});
		
		popContract.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popContract.dismiss();
			}
		});		
		
		tbBuySell.tb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {				
				tbBuySell.setChecked(false, isChecked);
				if(isChecked)
					app.setSelectedBuySell("B");
				else
					app.setSelectedBuySell("S");
				ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(app.getSelectedContract().strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
                GUIUtility.updateRate(true, app.getSelectedContract(), app.getSelectedBuySell(), app.getSelectedLimitStop(), currentViewHolder, res, defaultSetting);
				updateOrderRate(UpdateType.USER_CHANGE_INPUT);
				updateDealRef();
			}
        });	
		
		tbLimitStop.tb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {	
				
				tbLimitStop.setChecked(false, isChecked);
				
				if(isChecked)
					app.setSelectedLimitStop(0);
				else
					app.setSelectedLimitStop(1);
				ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(app.getSelectedContract().strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
				GUIUtility.updateRate(true, app.getSelectedContract(), app.getSelectedBuySell(), app.getSelectedLimitStop(), currentViewHolder, res, defaultSetting);
				updateOrderRate(UpdateType.USER_CHANGE_INPUT);
			}
        });	

		btnSubmit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				/*
				sendOrderRequest();
				Toast.makeText(v.getContext(), "Order request sent", Toast.LENGTH_LONG).show();
				OrderActivity.this.finish();
				*/
				if ( !app.getPriceAgentConnectionStatus() ) {
					Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "307" ,app.locale), Toast.LENGTH_LONG).show();
					return;
				}

				AlertDialog dialog = new AlertDialog.Builder(OrderActivity.this, CompanySettings.alertDialogTheme).create();
				dialog.setMessage(res.getText(R.string.are_you_sure));
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							OpenPositionRecord position = app.getSelectedOpenPosition();
							double maxLotSize = Double.MAX_VALUE;
							if( position != null )
							{
								maxLotSize = position.dAmount / position.contract.iContractSize; 
							}
							if (  ((Button)findViewById(R.id.btnLot)).getText().toString().equals("0.0") || Utility.toDouble(((Button)findViewById(R.id.btnLot)).getText().toString(), 0) > maxLotSize) {
								Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "616" ,app.locale), Toast.LENGTH_LONG).show();
			                    return;
							}
							
							if (((TextView)findViewById(R.id.tvDirection)).getText().toString().equals(">")){
								if (Utility.toDouble(((Button)findViewById(R.id.btnRate)).getText().toString()   , 0) <= Utility.toDouble( ((TextView)findViewById(R.id.tvRate)).getText().toString() , 0)  ) {
									Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
								}
							} else {
								if (Utility.toDouble(((Button)findViewById(R.id.btnRate)).getText().toString()   , 0) >= Utility.toDouble( ((TextView)findViewById(R.id.tvRate)).getText().toString() , 0)  ) {
									Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
								}
							}
							
							if( CompanySettings.VALIDATE_MINLOT_AND_INCLOT == true )
							{
								if( app.getSelectedContract().dMinLot > 0 )
								{
									if( Double.valueOf(btnLot.getText().toString()) < app.getSelectedContract().dMinLot){
			                			Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "620" ,app.locale).replaceFirst("#s", String.valueOf(app.getSelectedContract().dMinLot))   , Toast.LENGTH_LONG).show();
					                    return;
									}
								}
								else
								{
									if( Double.valueOf(btnLot.getText().toString()) < app.data.getBalanceRecord().dMinLotLimit){
			                			Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "620" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotLimit))   , Toast.LENGTH_LONG).show();
					                    return;
									}
								}
							}
							
							if( CompanySettings.VALIDATE_MINLOT_AND_INCLOT == true )
							{
								if( app.getSelectedContract().dIncLot > 0 )
								{
									if  (((1000 * Double.valueOf(btnLot.getText().toString())) % (1000 * app.getSelectedContract().dIncLot)) != 0){
										Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(app.getSelectedContract().dIncLot))   , Toast.LENGTH_LONG).show();
										return;
									}
								}
								else
								{
									if  (((1000 * Double.valueOf(btnLot.getText().toString())) % (1000 * app.data.getBalanceRecord().dMinLotIncrementUnit)) != 0){
										Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotIncrementUnit))   , Toast.LENGTH_LONG).show();
										return;
									}
								}
							}
							
							String sLPrice = "-1";
							String sSPrice = "-1";

							boolean limit = false;
							boolean stop = false;

							if (CompanySettings.newinterface){
								if (tbLimit != null)
									limit = tbLimit.isChecked();
								if(tbStop != null)
								stop = tbStop.isChecked();
							}
							else{
								if (tbLimit0 != null)
									limit = tbLimit0.isChecked();
								if (tbStop0 != null)
									stop = tbStop0.isChecked();
							}
							
							if(limit){
								sLPrice = btnLimit.getText().toString();

								if (Utility.toDouble(sLPrice, 0) <= 0) {
				                    Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
				                } else {
				                	if (((TextView)findViewById(R.id.tvLDirect)).getText().toString().equals(">")){
				                		if (Utility.toDouble(sLPrice, 0) <= Utility.toDouble( ((TextView)findViewById(R.id.tvLimit)).getText().toString() , 0)  ) {
				                			Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	} else {
				                		if (Utility.toDouble(sLPrice, 0) >= Utility.toDouble( ((TextView)findViewById(R.id.tvLimit)).getText().toString() , 0)  ) {
				                			Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	}
				                }
								
								
							}
							
							if(stop){
								sSPrice = btnStop.getText().toString();

								if (Utility.toDouble(sSPrice, 0) <= 0) {
				                    Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
				                } else {
				                	if (((TextView)findViewById(R.id.tvSDirect)).getText().toString().equals(">")){
				                		if ( Utility.toDouble( ((TextView)findViewById(R.id.tvStop)).getText().toString() , 0)  <= Utility.toDouble(sSPrice, 0) ) {
				                			Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	} else {
				                		if ( Utility.toDouble( ((TextView)findViewById(R.id.tvStop)).getText().toString() , 0)  >= Utility.toDouble(sSPrice, 0) ) {
				                			Toast.makeText(OrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	}
				                }
							}
							
							sendOrderRequest();
							Toast.makeText(OrderActivity.this, res.getString(R.string.msg_request_sent), Toast.LENGTH_LONG).show();
							//OrderActivity.this.finish();
						}
					}
				);

				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					}
				);
				dialog.show();
			}

			private void sendOrderRequest() {
				String sOCO = ocoValue;
				int iGT = 0;
				if( CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel) )
				{
					if( btnGoodTill.getText().equals(getResources().getString(R.string.tb_cancel)))
						iGT = 1;
					else if( btnGoodTill.getText().equals(getResources().getString(R.string.tb_end_of_day)))
						iGT = 2;
					else
						iGT = 0;
				}
				else
				{
					if(tbGoodTill.isChecked())
						iGT = 2;
				}

				boolean limit = false;
				boolean stop = false;

				if (CompanySettings.newinterface){
					if (tbLimit != null)
						limit = tbLimit.isChecked();
					if (tbStop != null)
						stop = tbStop.isChecked();
				}
				else{
					if (tbLimit0 != null)
						limit = tbLimit0.isChecked();
					if (tbStop0 != null)
						stop = tbStop0.isChecked();
				}
				pid = CommonFunction.getTransactionID(app.data.getBalanceRecord().strAccount);
				CommonFunction.addOrder(mService, mServiceMessengerHandler, 
						app.selectedContract, app.getSelectedBuySell(), 
						btnLot.getText().toString(), btnRate.getText().toString(), 
						iGT, tbLimitStop.isChecked()?0:1, 
						app.getSelectedOpenPosition() == null?"-1":String.valueOf(app.getSelectedOpenPosition().iRef), 
						"-".equals(sOCO)?"-1":sOCO, -1, limit?btnLimit.getText().toString():"-1", stop?btnStop.getText().toString():"-1", pid);
				
				OpenPositionRecord position = app.getSelectedOpenPosition();				
				if(position != null){
					app.setSelectedBuySell(position.strBuySell);
				}
			} 
		});

		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				OrderActivity.this.finish();
	
				OpenPositionRecord position = app.getSelectedOpenPosition();				
				if(position != null){
					app.setSelectedBuySell(position.strBuySell);
				}				
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
				btnLot.setText(popLot.getValue());
				updateDealRef();
				popLot.dismiss();
			}
			
		});
		

		
		findViewById(R.id.btnRate).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				popRate.upateRate(btnRate.getText().toString(), tvCurRate.getText().toString());				
				popRate.showLikeQuickAction();
			}
			
		});	
		
		popRate.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btnRate.setText(popRate.getValue());
				updateOrderRate(UpdateType.USER_CHANGE_INPUT);
				popRate.dismiss();
			}			
		});
		
		btnOCO.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				int iRef = -1;				
				if(!ocoValue.equals("-")){
					iRef = Utility.toInteger((String) ocoValue, -1);
				}				
				popOrder.updateSelectedOrderIndex(app.getSelectedContract().getWorkingOrderInMap(), iRef);
				popOrder.showLikeQuickAction();		
			}
			
		});	
		
		popOrder.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btnOCO.setText(popOrder.getValue());
				ocoValue=popOrder.getRealValue();
				popOrder.dismiss();
			}
			
		});
		
		popLot.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popLot.dismiss();
			}			
		});	
		
		popRate.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popRate.dismiss();
			}			
		});	
		
		popOrder.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popOrder.dismiss();
			}			
		});	
		
		if(btnLimit!=null)
		btnLimit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popLRate.upateRate(btnLimit.getText().toString(), tvLimit.getText().toString());				
				popLRate.showLikeQuickAction();
			}
			
		});	
		
		if(popLRate!=null)
		popLRate.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btnLimit.setText(popLRate.getValue());
				popLRate.dismiss();
			}			
		});
		
		if(popLRate!=null)
		popLRate.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popLRate.dismiss();
			}			
		});	
		
		if(btnStop!=null)
		btnStop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popSRate.upateRate(btnStop.getText().toString(), tvStop.getText().toString());				
				popSRate.showLikeQuickAction();
			}
			
		});	
		
		if(popSRate!=null)
		popSRate.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btnStop.setText(popSRate.getValue());
				popSRate.dismiss();
			}			
		});
		
		if(popSRate!=null)
		popSRate.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popSRate.dismiss();
			}			
		});

		if (CompanySettings.newinterface) {
			view_limit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					tbLimit.setChecked(true, !tbLimit.isChecked());
					setLimitLayout();
				}
			});

			view_stop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					tbStop.setChecked(true, !tbStop.isChecked());
					setStopLayout();
				}
			});
		}
	}
	
	/*
    public void bindBackButtonEvent(){
        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View arg0) {
                      OrderActivity.this.finish();                    
                      
                      OpenPositionRecord position = app.getSelectedOpenPosition();                        
                      if(position != null){
                            app.setSelectedBuySell(position.strBuySell);
                      }                 
                }           
        });     
    }
	 */

	public void setLimitLayout(){
		if (tbLimit.isChecked()){
			btnLimit.setEnabled(true);
			btnLimit.setAlpha((float)1);
			tvLimit.setAlpha((float)1);
			tvLDirect.setAlpha((float)1);
			label_limit.setAlpha((float)1);
		}
		else {
			btnLimit.setEnabled(false);
			btnLimit.setAlpha((float)0.5);
			tvLimit.setAlpha((float)0.5);
			tvLDirect.setAlpha((float)0.5);
			label_limit.setAlpha((float)0.5);
		}
	}

	public void setStopLayout(){
		if (tbStop.isChecked()){
			btnStop.setEnabled(true);
			btnStop.setAlpha((float)1);
			tvStop.setAlpha((float)1);
			tvSDirect.setAlpha((float)1);
			label_stop.setAlpha((float)1);
		}
		else {
			btnStop.setEnabled(false);
			btnStop.setAlpha((float)0.5);
			tvStop.setAlpha((float)0.5);
			tvSDirect.setAlpha((float)0.5);
			label_stop.setAlpha((float)0.5);
		}
	}
	
	
	public void updateSelectedOrderIndex(WheelView wv, OrderWheelAdapter owa, int iRef){
		owa.reload(app.getSelectedContract().getWorkingOrderInMap());
		
		if(-1 == iRef){
			wv.setCurrentItem(0);
		}else{
			wv.setCurrentItem(owa.getItemIndex(iRef));	
		}
	}
	
	@Override
	public void handleByChild(Message msg) {
		
	}


	@Override
	public void loadLayout() {		
		try {
			if (CompanySettings.newinterface)
				setContentView(R.layout.v_order_new);
			else
				setContentView(R.layout.v_order);

			TextView tvTitle = (TextView)findViewById(R.id.rlMain).findViewById(R.id.tvTitle);
			
	        btnContract = (Button)findViewById(R.id.btnContract);
	        popupContractList = app.data.getTradableContractList();
	        popContract = new PopupContract(getApplicationContext(), findViewById(R.id.rlTop), popupContractList);
	        btnContract.setText(app.getSelectedContract().getContractName(getLanguage()));
			
			OpenPositionRecord position = app.getSelectedOpenPosition();
			
			boolean enableOCO = false;
			if(app.isDemoPlatform == true && CompanySettings.ENABLE_ORDER_OCO_DEMO == true)
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 1 && CompanySettings.ENABLE_ORDER_OCO == true )
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 2 && CompanySettings.ENABLE_ORDER_OCO_PROD2 == true )
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 3 && CompanySettings.ENABLE_ORDER_OCO_PROD3 == true )
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 4 && CompanySettings.ENABLE_ORDER_OCO_PROD4 == true )
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 5 && CompanySettings.ENABLE_ORDER_OCO_PROD5 == true )
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 6 && CompanySettings.ENABLE_ORDER_OCO_PROD6 == true )
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 7 && CompanySettings.ENABLE_ORDER_OCO_PROD7 == true )
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 8 && CompanySettings.ENABLE_ORDER_OCO_PROD8 == true )
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 9 && CompanySettings.ENABLE_ORDER_OCO_PROD9 == true )
				enableOCO = true;
			else if( CompanySettings.checkProdServer() == 10 && CompanySettings.ENABLE_ORDER_OCO_PROD10 == true )
				enableOCO = true;
			
			if(position == null && enableOCO){
				findViewById(R.id.llDealRef).setVisibility(View.GONE);
				if(findViewById(Utility.getIdById("orderoco"))!=null)
					findViewById(Utility.getIdById("orderoco")).setVisibility(View.VISIBLE);

				if (CompanySettings.newinterface) {
					tbLimit = new IPhoneCheckbox((ToggleButton) findViewById(R.id.tbLimit), res, Utility.getStringIdById("lb_takeprofit"), Utility.getStringIdById("lb_takeprofit"));
					tbLimit.setChecked(true, false);

					tbStop = new IPhoneCheckbox((ToggleButton) findViewById(R.id.tbStop), res, Utility.getStringIdById("lb_stoploss"), Utility.getStringIdById("lb_stoploss"));
					tbStop.setChecked(true, false);
				}
				else{
					tbLimit0 = new IPhoneToggle((ToggleButton) findViewById(R.id.tbLimit), res, Utility.getStringIdById("lb_takeprofit"), Utility.getStringIdById("lb_takeprofit"));
					tbLimit0.setChecked(true, false);

					tbStop0 = new IPhoneToggle((ToggleButton) findViewById(R.id.tbStop), res, Utility.getStringIdById("lb_stoploss"), Utility.getStringIdById("lb_stoploss"));
					tbStop0.setChecked(true, false);
				}
			
		        btnLimit = (Button)findViewById(R.id.btnLimit);
				popLRate = new PopupRate(getApplicationContext(), findViewById(R.id.rlTop));
			
				btnStop = (Button)findViewById(R.id.btnStop);
				popSRate = new PopupRate(getApplicationContext(), findViewById(R.id.rlTop));
			
				tvLimit = (TextView)findViewById(R.id.tvLimit);
			
				tvStop = (TextView)findViewById(R.id.tvStop);
			
				tvLDirect =  (TextView)findViewById(R.id.tvLDirect);
			
				tvSDirect =  (TextView)findViewById(R.id.tvSDirect);
			}else{
				if(findViewById(Utility.getIdById("orderoco"))!=null)
					findViewById(Utility.getIdById("orderoco")).setVisibility(View.GONE);		
			}
			
			if(position!=null){
				if (CompanySettings.newinterface) {
					label_contract = (TextView) findViewById(R.id.label_contract_order);
					String bs, color;
					if ("B".equals(app.getSelectedBuySell())) {
						bs = Utility.getStringById("lb_buy", getResources());
						color = "#379329";
					} else {
						bs = Utility.getStringById("lb_sell", getResources());
						color = "#ff0000";
					}
					String text = "<font color=" + color + ">" + bs + "</font> <font color=#ffffff>" + app.getSelectedContract().getContractName(getLanguage()) + "</font>";
					label_contract.setText(Html.fromHtml(text));
					//label_contract.setText(bs+" "+app.getSelectedContract().getContractName(Locale.getDefault()));
					findViewById(Utility.getIdById("view_contract_order")).setVisibility(View.GONE);
				}
				btnContract.setEnabled(false);
				if(CompanySettings.LOCK_LIQ_ORDER_LIMIT_STOP){
					if(app.getSelectedLimitStop() == 0){
						tvTitle.setText(Utility.getStringById("title_new_limit", getResources()));
					}else{
						tvTitle.setText(Utility.getStringById("title_new_stop", getResources()));
					}
				}
			}
	        tbBuySell = new IPhoneToggle((ToggleButton)findViewById(R.id.tbBuySell), res, R.string.lb_buy, R.string.lb_sell);
	        tbLimitStop = new IPhoneToggle((ToggleButton)findViewById(R.id.tbLS), res, R.string.tb_limit, R.string.tb_stop);
	        tbGoodTill = new IPhoneToggle((ToggleButton)findViewById(R.id.tbGT), res, R.string.tb_end_of_day, R.string.tb_friday);
	        btnGoodTill = (Button)findViewById(R.id.btnGT);

	        if (CompanySettings.newinterface) {
				label_limit = (TextView) findViewById(R.id.label_limit_order);
				label_stop = (TextView) findViewById(R.id.label_stop_order);
				view_limit = (RelativeLayout) findViewById(R.id.view_limit_order);
				view_stop = (RelativeLayout) findViewById(R.id.view_stop_order);

				if (position == null) {
					if (tbLimit != null && tbStop != null) {
						setLimitLayout();
						setStopLayout();
						tbLimit.tb.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								setLimitLayout();
							}
						});
						tbStop.tb.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								setStopLayout();
							}
						});
					}
				}
			}

	        if( CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel) )
	        {
	        	tbGoodTill.setVisiblity(View.GONE);
	        	if (CompanySettings.newinterface)
					findViewById(R.id.btnTBGT).setVisibility(View.GONE);
	        	ArrayList<String> alGoodTill = new ArrayList<String>();
	        	alGoodTill.add(getResources().getString(R.string.tb_cancel));
	        	alGoodTill.add(getResources().getString(R.string.tb_end_of_day));
	        	alGoodTill.add(getResources().getString(R.string.tb_friday));
	        	popGoodTill = new PopupGoodTill(getApplicationContext(), findViewById(R.id.rlTop), alGoodTill);
	        }
	        else
	        {
	        	btnGoodTill.setVisibility(View.GONE);
	        }
	        
	        
	        if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DailyOnly)){
	        	tbGoodTill.setChecked(true, true);
	        	tbGoodTill.tb.setEnabled(false);
	        }else if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.FridayOnly)){
	        	tbGoodTill.setChecked(true, false);
	        	tbGoodTill.tb.setEnabled(false);
	        }else if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel)){
	        	btnGoodTill.setText(getResources().getString(R.string.tb_cancel));
	        }else{
	        	if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultDaily)){
	        		tbGoodTill.setChecked(true, true);
	        	}else{
	        		tbGoodTill.setChecked(true, false);
	        	}
	        }
	        
			btnLot = (Button)findViewById(R.id.btnLot);
			btnSubmit = (Button)findViewById(R.id.btnSubmit);
			btnRate = (Button)findViewById(R.id.btnRate);
			btnOCO = (Button)findViewById(R.id.btnOCO);
					
			tvCurRate = (TextView)findViewById(R.id.tvRate);
			tvDirection =  (TextView)findViewById(R.id.tvDirection);
			ContractObj contract = app.getSelectedContract();
			popLot = new PopupLOT(getApplicationContext(), findViewById(R.id.rlTop), BigDecimal.valueOf(contract.getFinalMaxLotLimit()));
			popRate = new PopupRate(getApplicationContext(), findViewById(R.id.rlTop));
			popOrder = new PopupOrder(
					getApplicationContext(), findViewById(R.id.rlTop),
					new OrderWheelAdapter(OrderActivity.this, app.getSelectedContract().getWorkingOrderInMap(), mService, mServiceMessengerHandler));
			limitViewHolder = new GUIUtilityUpdateRateViewHolder(tvLimit, tvLDirect, btnLimit, btnRate);
			stopViewHolder = new GUIUtilityUpdateRateViewHolder(tvStop, tvSDirect, btnStop, btnRate);
			currentViewHolder = new GUIUtilityUpdateRateViewHolder(tvCurRate, tvDirection, btnRate, null);
			updateBuySell();
			if(position != null){
				btnLot.setText(Utility.formatLot(position.dAmount / position.contract.iContractSize));
			}else{
				btnLot.setText(getDefaultLOT());
			}
			updateDealRef();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void updateBuySell(){	
		boolean bslock = false;
		OpenPositionRecord position = app.getSelectedOpenPosition();
		if(position!=null)
			bslock = true;
		if("B".equals(app.getSelectedBuySell())){
			tbBuySell.setChecked(true, true);
		}else{
			tbBuySell.setChecked(true, false);	
		}
		if(bslock)
			tbBuySell.tb.setEnabled(false);
	}
	



	public void updateDealRef() {
		ContractObj contract = app.getSelectedContract();
		OpenPositionRecord position = app.getSelectedOpenPosition();

		if(contract == null){
			return;
		}

		int iLimitStop = app.getSelectedLimitStop();

		String sBuySell = app.getSelectedBuySell();
		if(sBuySell == null){
			return;
		}

		if(iLimitStop == 0){
			tbLimitStop.setChecked(true, true);
		}else{
			tbLimitStop.setChecked(true, false);
		}

		if(position != null && CompanySettings.LOCK_LIQ_ORDER_LIMIT_STOP){
			tbLimitStop.tb.setEnabled(false);
		}
		ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(app.getSelectedContract().strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
		if (contract.enableDepth.equals("1"))
			GUIUtility.updateRateDepth(false, app, tvCurRate, tvDirection, btnRate, res, btnLot.getText().toString());
		else
			GUIUtility.updateRate(false, app.getSelectedContract(), app.getSelectedBuySell(), app.getSelectedLimitStop(), currentViewHolder, res, defaultSetting);

		BigDecimal dLot = Utility.toBigDecimal(btnLot.getText().toString(), "0");
		TextView tvTmp = (TextView)findViewById(R.id.tvAmount);
		tvTmp.setText(Utility.formatAmount(dLot.multiply(new BigDecimal(String.valueOf(contract.iContractSize)))));

		if (contract.enableDepth.equals("1")) {
			updateOrderRateDepth(false, btnLot.getText().toString());
		}

		tvTmp = (TextView)findViewById(R.id.tvDealRef);
		if(position != null){
			if (CompanySettings.newinterface)
				tvTmp.setText(position.getViewRef());
			else
				tvTmp.setText(res.getString(R.string.lb_ref)+ " " + position.getViewRef());

			tvTmp = (TextView)findViewById(R.id.tvPL);
			tvTmp.setText(Utility.formatValue(position.dFloating));
			ColorController.setNumberColor(res, position.dFloating >= 0, tvTmp);
			findViewById(R.id.llDealRef).setVisibility(View.VISIBLE);
		}else{
			tvTmp.setText(res.getString(R.string.lb_ref)+ " -");
			findViewById(R.id.llDealRef).setVisibility(View.GONE);
		}

	}

	private void updatePopLot() {
		ContractObj contract = app.getSelectedContract();
		popLot = new PopupLOT(getApplicationContext(), findViewById(R.id.rlTop), BigDecimal.valueOf(contract.getFinalMaxLotLimit()));
		popLot.btnCommit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnLot.setText(popLot.getValue());
				updateUI();
				popLot.dismiss();
			}
		});
		popLot.btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popLot.dismiss();
			}
		});
	}

	@Override
	public void updateUI() {
		ContractObj contract = app.getSelectedContract();
		OpenPositionRecord position = app.getSelectedOpenPosition();
		ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(app.getSelectedContract().strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
		if (contract.enableDepth.equals("1"))
			GUIUtility.updateRateDepth(false, app, tvCurRate, tvDirection, btnRate, res, btnLot.getText().toString());
		else {
			GUIUtility.updateRate(false, app.getSelectedContract(), app.getSelectedBuySell(), app.getSelectedLimitStop(), currentViewHolder, res, defaultSetting);
		}

		if (contract.enableDepth.equals("1")) {
			updateOrderRateDepth(false, btnLot.getText().toString());
		} else {
			updateOrderRate(UpdateType.INCOMING_PRICE);
		}

		if(position != null){
			TextView tvTmp = (TextView)findViewById(R.id.tvPL);
			tvTmp.setText(Utility.formatValue(position.dFloating));
			ColorController.setNumberColor(res, position.dFloating >= 0, tvTmp);
			findViewById(R.id.llDealRef).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.llDealRef).setVisibility(View.GONE);
		}

		if( pid != null && CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST == false) {
			TransactionObj t = app.data.getTransaction(pid);
			if (t != null) {
				if (t.iStatusMsg == 916 ) {
					pid = null;
					OrderActivity.this.finish();
					goTo(ServiceFunction.SRV_RUNNING_ORDER);
				}
				else if (t.iStatusMsg != 917 && t.iStatusMsg != -1)
				{
					pid = null;
					if (!CompanySettings.newinterface){
						OrderActivity.this.finish();
					}
				}
			}
		}
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		
		if(app.getSelectedContract() == null){	
			if (app.getDefaultContract()==null)
				return;
			app.setSelectedContract(app.getDefaultContract());
		}		
		
		ContractObj contract = app.getSelectedContract();	
		OpenPositionRecord position = app.getSelectedOpenPosition();
		btnContract.setText(contract.getContractName(getLanguage()));
		
		if(position != null){
			btnLot.setText(Utility.formatLot(position.dAmount / position.contract.iContractSize));
		}else{
			btnLot.setText(getDefaultLOT());
		}
		
		updateBuySell();
		ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(app.getSelectedContract().strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
		GUIUtility.updateRate(true, app.getSelectedContract(), app.getSelectedBuySell(), app.getSelectedLimitStop(), currentViewHolder, res, defaultSetting);

		if (CompanySettings.newinterface) {
			if (tbLimit != null)
				tbLimit.setChecked(true, false);

			if (tbStop != null)
				tbStop.setChecked(true, false);
		}
		else{
			if (tbLimit0 != null)
				tbLimit0.setChecked(true, false);

			if (tbStop0 != null)
				tbStop0.setChecked(true, false);
		}

		updateOrderRate(UpdateType.ON_RESUME);
	}

	public void updateOrderRate(UpdateType updateType){
		if(tvSDirect==null)
			return;
		
        String sOrderBuySell = "B";
        try{
            if(app.getSelectedBuySell().equals("B")){
            	sOrderBuySell = "S";
            }

			ContractObj contract = app.getSelectedContract();
			ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(contract.strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
			switch (updateType) {
				case INCOMING_PRICE:

					break;
				case USER_CHANGE_INPUT:
					GUIUtility.updateRate(true, contract, sOrderBuySell, 0, limitViewHolder, res, defaultSetting);
					GUIUtility.updateRate(true, contract, sOrderBuySell, 1, stopViewHolder, res, defaultSetting);
					if (tvSDirect.getText().toString().equals(">")) {
						tvSDirect.setText("<");
					} else {
						tvSDirect.setText(">");
					}
					break;
				case ON_RESUME:
					String sRate;
					sRate = GUIUtility.updateRate(false, contract, sOrderBuySell, 0, limitViewHolder, res, defaultSetting);
					btnLimit.setText(sRate);
					sRate = GUIUtility.updateRate(false, contract, sOrderBuySell, 1, stopViewHolder, res, defaultSetting);
					btnStop.setText(sRate);
					if (tvSDirect.getText().toString().equals(">")) {
						tvSDirect.setText("<");
					} else {
						tvSDirect.setText(">");
					}
					break;
			}
        }catch(Exception e){
        	e.printStackTrace();
        }
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
	protected void onDestroy() {
        OpenPositionRecord position = app.getSelectedOpenPosition();                        
        if(position != null){
              app.setSelectedBuySell(position.strBuySell);
        }    
		super.onDestroy();
	}

	public void updateOrderRateDepth(boolean bUpdateButton, String lot){
		String sOrderBuySell = "B";
		try{
			if(app.getSelectedBuySell().equals("B")){
				sOrderBuySell = "S";
			}

			GUIUtility.updateRateDepth(bUpdateButton, app.getSelectedContract(), sOrderBuySell, 0, tvLimit, tvLDirect, btnLimit, btnRate, res, lot, tvCurRate.getText().toString());
			GUIUtility.updateRateDepth(bUpdateButton, app.getSelectedContract(), sOrderBuySell, 1, tvStop, tvSDirect, btnStop, btnRate, res, lot, tvCurRate.getText().toString());

			//GUIUtility.updateRate(bUpdateButton, app.getSelectedContract(), sOrderBuySell, 0, tvLimit, tvLDirect, btnLimit, btnRate, res);
			//GUIUtility.updateRate(bUpdateButton, app.getSelectedContract(), sOrderBuySell, 1, tvStop, tvSDirect, btnStop, btnRate, res);

			if(tvSDirect.getText().toString().equals(">")){
				tvSDirect.setText("<");
			}else{
				tvSDirect.setText(">");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}