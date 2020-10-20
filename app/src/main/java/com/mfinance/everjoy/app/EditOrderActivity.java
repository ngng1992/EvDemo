package com.mfinance.everjoy.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.DefaultCompanySettings.OrderTimeLimit;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.OrderRecord;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

public class EditOrderActivity extends BaseActivity{
	String pid = null;
	Button btnLot;
	Button btnSubmit;
	Button btnRate;
	Button btnOCO;
	
	TextView tvCurRate;
	TextView tvDirection;
	
	IPhoneToggle tbGoodTill;
	Button btnGoodTill;
	PopupGoodTill popGoodTill;
	
	PopupOrder popOrder;
	PopupLOT popLot;
	PopupRate popRate;
	
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
		
		btnSubmit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				/*
				sendEditOrderRequest();
				Toast.makeText(v.getContext(), "Edit order request sent", Toast.LENGTH_LONG).show();
				EditOrderActivity.this.finish();
				*/
				if ( !app.getPriceAgentConnectionStatus() ) {
					Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "307" ,app.locale), Toast.LENGTH_LONG).show();
					return;
				}

				AlertDialog dialog = new AlertDialog.Builder(EditOrderActivity.this, CompanySettings.alertDialogTheme).create();
				dialog.setMessage(res.getText(R.string.are_you_sure));							
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							OpenPositionRecord position = app.getSelectedOpenPosition();
							double  maxLotSize = Double.MAX_VALUE;
							if( position != null)
							{
								maxLotSize = position.dAmount / position.contract.iContractSize;
							}
							if (  ((Button)findViewById(R.id.btnLot)).getText().toString().equals("0.0") || Utility.toDouble(((Button)findViewById(R.id.btnLot)).getText().toString(), 0) > maxLotSize) {
								Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "616" ,app.locale), Toast.LENGTH_LONG).show();
			                    return;
							}
							
							if (((TextView)findViewById(R.id.tvDirection)).getText().toString().equals(">")){
								if (Utility.toDouble(((Button)findViewById(R.id.btnRate)).getText().toString()   , 0) <= Utility.toDouble( ((TextView)findViewById(R.id.tvRate)).getText().toString() , 0)  ) {
									Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
								}
							} else {
								if (Utility.toDouble(((Button)findViewById(R.id.btnRate)).getText().toString()   , 0) >= Utility.toDouble( ((TextView)findViewById(R.id.tvRate)).getText().toString() , 0)  ) {
									Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
								}
							}
							
							double dMinLot = app.data.getContract(app.getSelectedRunningOrder().strContract).dMinLot;
							if( CompanySettings.VALIDATE_MINLOT_AND_INCLOT == true )
							{
								if( dMinLot > 0 )
								{
									if( Double.valueOf(btnLot.getText().toString()) < dMinLot){
			                			Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "620" ,app.locale).replaceFirst("#s", String.valueOf(dMinLot))   , Toast.LENGTH_LONG).show();
					                    return;
									}
								}
								else
								{
									if( Double.valueOf(btnLot.getText().toString()) < app.data.getBalanceRecord().dMinLotLimit){
			                			Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "620" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotLimit))   , Toast.LENGTH_LONG).show();
					                    return;
									}
								}
							}
							
							double dIncLot = app.data.getContract(app.getSelectedRunningOrder().strContract).dIncLot;
							if( CompanySettings.VALIDATE_MINLOT_AND_INCLOT == true )
							{
								if( dIncLot > 0 )
								{
									if  (((1000 * Double.valueOf(btnLot.getText().toString())) % (1000 * dIncLot)) != 0){
										Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(dIncLot))   , Toast.LENGTH_LONG).show();
										return;
									}
								}
								else
								{
									if  (((1000 * Double.valueOf(btnLot.getText().toString())) % (1000 * app.data.getBalanceRecord().dMinLotIncrementUnit)) != 0){
										Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotIncrementUnit))   , Toast.LENGTH_LONG).show();
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
				                    Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
				                } else {
				                	if (((TextView)findViewById(R.id.tvLDirect)).getText().toString().equals(">")){
				                		if (Utility.toDouble(sLPrice, 0) <= Utility.toDouble( ((TextView)findViewById(R.id.tvLimit)).getText().toString() , 0)  ) {
				                			Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	} else {
				                		if (Utility.toDouble(sLPrice, 0) >= Utility.toDouble( ((TextView)findViewById(R.id.tvLimit)).getText().toString() , 0)  ) {
				                			Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	}
				                }
								
								
							}
							
							if(stop){
								sSPrice = btnStop.getText().toString();

								if (Utility.toDouble(sSPrice, 0) <= 0) {
				                    Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
				                } else {
				                	if (((TextView)findViewById(R.id.tvSDirect)).getText().toString().equals(">")){
				                		if ( Utility.toDouble( ((TextView)findViewById(R.id.tvStop)).getText().toString() , 0)  <= Utility.toDouble(sSPrice, 0) ) {
				                			Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	} else {
				                		if ( Utility.toDouble( ((TextView)findViewById(R.id.tvStop)).getText().toString() , 0)  >= Utility.toDouble(sSPrice, 0) ) {
				                			Toast.makeText(EditOrderActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	}
				                }
							}
							
							sendEditOrderRequest();
							Toast.makeText(EditOrderActivity.this, res.getString(R.string.msg_request_sent), Toast.LENGTH_LONG).show();
							//EditOrderActivity.this.finish();
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

			private void sendEditOrderRequest() {
				String sOCO = btnOCO.getText().toString();
				
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
				pid = CommonFunction.getTransactionID(app.data.getBalanceRecord().strAccount);
				String selectedOpenPosition = app.getSelectedOpenPosition() == null?"-1":String.valueOf(app.getSelectedOpenPosition().iRef);
				CommonFunction.editOrder(mService, mServiceMessengerHandler,  
						app.getSelectedRunningOrder(), btnLot.getText().toString(), btnRate.getText().toString(), 
						iGT, selectedOpenPosition, 0.0, "-".equals(sOCO)?"-1":sOCO, -1, (tbLimit!=null&&tbLimit.isChecked())?btnLimit.getText().toString():"-1", (tbStop!=null&&tbStop.isChecked())?btnStop.getText().toString():"-1", pid);
				
				
			}
		});
		
		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				EditOrderActivity.this.finish();
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
				BigDecimal dLot = Utility.toBigDecimal(btnLot.getText().toString(), "0");
			    ((TextView)findViewById(R.id.tvAmount)).setText(
						Utility.formatAmount(dLot.multiply(new BigDecimal(String.valueOf(app.getSelectedRunningOrder().contract.iContractSize))))
			    );
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
				updateOrderRate(false);
				popRate.dismiss();
			}			
		});
		
		btnOCO.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				int iRef = -1;				
				if(!btnOCO.getText().toString().equals("-")){
					iRef = Utility.toInteger(btnOCO.getText().toString(), -1);
				}					
				popOrder.updateSelectedOrderIndex(app.getSelectedRunningOrder().contract.getWorkingOrderInMap(), iRef);
				popOrder.showLikeQuickAction();		
			}
			
		});	
		
		popOrder.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String sValue = popOrder.getValue();
				
				if(sValue.equals(app.getSelectedRunningOrder().getViewRef())){
					btnOCO.setText("-");
				}else{
					btnOCO.setText(sValue);
				}
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
				if (CompanySettings.newinterface && !tbLimit.isChecked()) {
					return;
				}
				popLRate.upateRate(btnLimit.getText().toString(), tvLimit.getText().toString() );
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
				if (CompanySettings.newinterface && !tbStop.isChecked()) {
					return;
				}
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
	}
	
	public void updateSelectedOrderIndex(WheelView wv, OrderWheelAdapter owa, int iRef){
		owa.reload(app.getSelectedRunningOrder().contract.getWorkingOrderInMap());
		
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
		if (CompanySettings.newinterface)
			setContentView(R.layout.v_edit_order_new);
		else
			setContentView(R.layout.v_edit_order);
	
		btnLot = (Button)findViewById(R.id.btnLot);
		btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnRate = (Button)findViewById(R.id.btnRate);
		btnOCO = (Button)findViewById(R.id.btnOCO);
        
		tbGoodTill = new IPhoneToggle((ToggleButton)findViewById(R.id.tbGT), res, R.string.tb_end_of_day, R.string.tb_friday);
		btnGoodTill = (Button)findViewById(R.id.btnGT);
		if( CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel) )
        {
        	tbGoodTill.setVisiblity(View.GONE);
        	if (CompanySettings.newinterface)
				findViewById(R.id.btnTGGT).setVisibility(View.GONE);
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
		
		tvCurRate = (TextView)findViewById(R.id.tvRate);
		tvDirection =  (TextView)findViewById(R.id.tvDirection);

		OrderRecord order = app.getSelectedRunningOrder();
		ContractObj contract = order.contract;
		popLot = new PopupLOT(getApplicationContext(), findViewById(R.id.rlTop), BigDecimal.valueOf(contract.getFinalMaxLotLimit()));
		popRate = new PopupRate(getApplicationContext(), findViewById(R.id.rlTop));
		popOrder = new PopupOrder(getApplicationContext(), findViewById(R.id.rlTop),
					new OrderWheelAdapter(EditOrderActivity.this, app.getSelectedRunningOrder().contract.getWorkingOrderInMap(), mService, mServiceMessengerHandler, order));
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
			tvLimit = (TextView)findViewById(R.id.tvLimit);
			tvStop = (TextView)findViewById(R.id.tvStop);
			tvLDirect =  (TextView)findViewById(R.id.tvLDirect);
			tvSDirect =  (TextView)findViewById(R.id.tvSDirect);
		}

		limitViewHolder = new GUIUtilityUpdateRateViewHolder(tvLimit, tvLDirect, btnLimit, btnRate);
		stopViewHolder = new GUIUtilityUpdateRateViewHolder(tvStop, tvSDirect, btnStop, btnRate);
		currentViewHolder = new GUIUtilityUpdateRateViewHolder(tvCurRate, tvDirection, btnRate, null);
	}

	@Override
	public void updateUI() {	
		Locale l = getLanguage();
		
		OrderRecord order = app.getSelectedRunningOrder();
		OpenPositionRecord position = app.getSelectedOpenPosition();
		
		if(order == null){
			return;
		}
		
		String sRef = order.iLimitStop == 0? res.getString(R.string.tb_limit) : res.getString(R.string.tb_stop);
		sRef += " "+order.getViewRef();
		
		TextView tvTmp = (TextView)findViewById(R.id.tvRef);
		tvTmp.setText(sRef);

		if (!CompanySettings.newinterface) {
			tvTmp = (TextView) findViewById(R.id.tvBuySell);
			tvTmp.setText(
					order.strBuySell.equals("B") ? res.getString(R.string.lb_buy) : res.getString(R.string.lb_sell)
			);
		}
		
		tvTmp = (TextView)findViewById(R.id.tvContract);
		if (CompanySettings.newinterface){
			String bs, color;
			if (order.strBuySell.equals("B")) {
				bs = Utility.getStringById("lb_buy", getResources());
				color = "#379329";
			} else {
				bs = Utility.getStringById("lb_sell", getResources());
				color = "#ff0000";
			}
			String text = "<font color=" + color + ">" + bs + "</font> <font color=#ffffff>" + order.contract.getContractName(getLanguage()) + "</font>";
			tvTmp.setText(Html.fromHtml(text));
		}
		else
			tvTmp.setText(order.contract.getContractName(l));
		ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(order.contract.strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
		GUIUtility.updateRate(false, order.contract, order.strBuySell, order.iLimitStop, currentViewHolder, res, defaultSetting);
		reload();
		
		BigDecimal dLot = Utility.toBigDecimal(btnLot.getText().toString(), "0");
	    tvTmp = (TextView)findViewById(R.id.tvAmount);
		tvTmp.setText(Utility.formatAmount(dLot.multiply(new BigDecimal(String.valueOf(order.contract.iContractSize)))));
	    
	    tvTmp = (TextView)findViewById(R.id.tvDealRef);	    
	    if(position != null){
	    	tvTmp.setText(res.getString(R.string.lb_ref)+ " " + position.getViewRef());

		    tvTmp = (TextView)findViewById(R.id.tvPL);
		    tvTmp.setText(Utility.formatValue(position.dFloating));
		    ColorController.setNumberColor(res, position.dFloating >= 0, tvTmp);		    
		    findViewById(R.id.llDealRef).setVisibility(View.VISIBLE);
	    }else{
	    	tvTmp.setText(res.getString(R.string.lb_ref)+ " -");	    	
	    	findViewById(R.id.llDealRef).setVisibility(View.GONE);
	    }

		if( pid != null && CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST == false) {
			TransactionObj t = app.data.getTransaction(pid);
			if (t != null) {
				if (t.iStatusMsg == 916 ) {
					pid = null;
					EditOrderActivity.this.finish();
					goTo(ServiceFunction.SRV_RUNNING_ORDER);
				}
				else if (t.iStatusMsg != 917 && t.iStatusMsg != -1)
				{
					pid = null;
					if (!CompanySettings.newinterface){
						EditOrderActivity.this.finish();
					}
				}
			}
		}
	}
	
	public boolean bResume = false;
	
	public void reload(){
		if(bResume){
			OrderRecord order = app.getSelectedRunningOrder();
			btnLot.setText(Utility.formatLot(order.dAmount / order.contract.iContractSize));
			((TextView)findViewById(R.id.tvAmount)).setText(Utility.formatAmount(order.dAmount));
			
			if(order.iOCORef != -1){
				btnOCO.setText(String.valueOf(order.getOCOViewRef()));
			}else{
				btnOCO.setText("-");
			}

			btnRate.setText(Utility.round(order.dRequestRate, order.contract.iRateDecPt, order.contract.iRateDecPt));
			bResume = false;

			if( CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel) )
			{
				if(order.iGoodTillType == 1){
					btnGoodTill.setText(R.string.tb_cancel);
				}
				else if(order.iGoodTillType == 2){
					btnGoodTill.setText(R.string.tb_end_of_day);
				}
				else
				{
					btnGoodTill.setText(R.string.tb_friday);
				}
			}
			else
			{
				if(order.iGoodTillType == 2){
					tbGoodTill.setChecked(true, true);
				}else{
					tbGoodTill.setChecked(true, false);
				}	
				
		        if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DailyOnly)||CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.FridayOnly)){
		        	tbGoodTill.tb.setEnabled(false);
		        }
			}
	        
			updateOrderRate(true);
			
			if(tbLimit!=null){
			    if(order.dOCOLimitRate!=0){
			    	tbLimit.setChecked(true, true);
			    	btnLimit.setText(Utility.round(order.dOCOLimitRate, order.contract.iRateDecPt, order.contract.iRateDecPt));
			    }else{
			    	tbLimit.setChecked(true, false);
			    }
		    }
			if(tbStop!=null){
			    if(order.dOCOStopRate!=0){
			    	tbStop.setChecked(true, true);
			    	btnStop.setText(Utility.round(order.dOCOStopRate, order.contract.iRateDecPt, order.contract.iRateDecPt));
			    }else{
			    	tbStop.setChecked(true, false);
			    }
		    }
		}
	}

	public void updateOrderRate(boolean bUpdateButton){
		if(tvSDirect==null)
			return;
		
        String sOrderBuySell = "B";
        try{
            if(app.getSelectedRunningOrder().strBuySell.equals("B")){
            	sOrderBuySell = "S";
            }
			ContractObj contract = app.getSelectedRunningOrder().contract;
			ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(contract.strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
            GUIUtility.updateRate(bUpdateButton, contract, sOrderBuySell, 0, limitViewHolder, res, defaultSetting);
            GUIUtility.updateRate(bUpdateButton, contract, sOrderBuySell, 1, stopViewHolder, res, defaultSetting);
    	
            if(tvSDirect.getText().toString().equals(">")){
            	tvSDirect.setText("<");
            }else{
            	tvSDirect.setText(">");
            }        	
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		bResume = true;
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