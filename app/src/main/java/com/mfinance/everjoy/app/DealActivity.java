package com.mfinance.everjoy.app;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.DefaultCompanySettings.OrderTimeLimit;
import com.mfinance.everjoy.app.MobileTraderApplication.BuySellStrengthPriceAdjustSetting;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.ContractDefaultSetting;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.GUIUtilityUpdateRateViewHolder;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DealActivity extends BaseActivity{
	public enum UpdateType {
		INCOMING_PRICE, USER_CHANGE_INPUT, ON_RESUME
	}
	String pid = null;
	Button btnContract;
	Button btnLot;
	Button btnSubmit;
	Button btnLimit;
	Button btnStop;
	Button btnSlippage;
	
	TextView tvLimit;
	TextView tvStop;
	TextView tvLDirect;
	TextView tvSDirect;

	GUIUtilityUpdateRateViewHolder limitViewHolder;
	GUIUtilityUpdateRateViewHolder stopViewHolder;

	IPhoneToggle tbBuySell;
	IPhoneCheckbox tbLimit;
	IPhoneCheckbox tbStop;
	IPhoneToggle tbLimit0;
	IPhoneToggle tbStop0;
	IPhoneToggle tbLimitGT;
	Button btnLimitGT;
	IPhoneToggle tbStopGT;
	Button btnStopGT;

	RelativeLayout view_limit;
	RelativeLayout view_stop;
	TextView label_limit;
	TextView label_stop;
	TextView label_lgt;
	TextView label_sgt;
	
	ArrayList<ContractObj> popupContractList;

	PopupContract popContract;
	PopupLOT popLot;
	PopupRate popLRate;
	PopupRate popSRate;
	PopupSlippage popSlippage;
	PopupGoodTill popGoodTill;	
	
	Timer m_BuySellStrengthTimer = null;
	CountDownTimer timeout = null;
	private boolean enableSlippage = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		app = (MobileTraderApplication)getApplicationContext();
		if(app.isDemoPlatform == true && CompanySettings.ENABLE_SLIPPAGE_DEMO == true)
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 1 && CompanySettings.ENABLE_SLIPPAGE == true )
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 2 && CompanySettings.ENABLE_SLIPPAGE_PROD2 == true )
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 3 && CompanySettings.ENABLE_SLIPPAGE_PROD3 == true )
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 4 && CompanySettings.ENABLE_SLIPPAGE_PROD4 == true )
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 5 && CompanySettings.ENABLE_SLIPPAGE_PROD5 == true )
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 6 && CompanySettings.ENABLE_SLIPPAGE_PROD6 == true )
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 7 && CompanySettings.ENABLE_SLIPPAGE_PROD7 == true )
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 8 && CompanySettings.ENABLE_SLIPPAGE_PROD8 == true )
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 9 && CompanySettings.ENABLE_SLIPPAGE_PROD9 == true )
			enableSlippage = true;
		else if( CompanySettings.checkProdServer() == 10 && CompanySettings.ENABLE_SLIPPAGE_PROD10 == true )
			enableSlippage = true;
		super.onCreate(savedInstanceState);
	}

	@Override
	public void bindEvent() {
		btnContract.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popContract.updateSelectedContract(null, btnContract.getText().toString());
				popContract.showLikeQuickAction();
			}
		});
		
		if( CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel) )
		{
			btnLimitGT.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					popGoodTill.isStopOrder = false;
					popGoodTill.updateSelectedGoodTill(btnLimitGT.getText().toString());
					popGoodTill.showLikeQuickAction();
				}
			});
			
			btnStopGT.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					popGoodTill.isStopOrder = true;
					popGoodTill.updateSelectedGoodTill(btnStopGT.getText().toString());
					popGoodTill.showLikeQuickAction();
				}
			});
			
			popGoodTill.btnCommit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					if( popGoodTill.isStopOrder)
						btnStopGT.setText(popGoodTill.getValue());
					else
						btnLimitGT.setText(popGoodTill.getValue());
					
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
		
		
		
		popContract.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){	
				popContract.dismiss();
				String sContract = popContract.getValue();
				// do not look up contract by name since it may not be unique!
				app.setSelectedContract(popupContractList.get(popContract.getSelectedIndex()));
				btnContract.setText(sContract);
				
				if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(app.getSelectedContract().strContractCode) )
				{
					if(m_BuySellStrengthTimer != null)
						m_BuySellStrengthTimer.cancel();
					CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "close");
					
					BuySellStrengthPriceAdjustSetting setting = MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.get(app.getSelectedContract().strContractCode);
					if( setting.openPosWaitSec > 0 || setting.openPosWaitSec < 0 )
					{
						CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), app.getSelectedContract().strContractCode, app.getSelectedBuySell(), "open");
					}
					else
					{
						CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), app.getSelectedContract().strContractCode, app.getSelectedBuySell(), "open_pending");
					}
					
					if( setting.openPosWaitSec > 0 )
					{
						m_BuySellStrengthTimer = new Timer();
					    m_BuySellStrengthTimer.schedule(new TimerTask()
	            			{
	    						@Override
	    						public void run() {
	    							CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "pending");							
	    						}
	            		
	            			}, setting.openPosWaitSec * 1000);
					}
				}
				
				updateOrderRate(UpdateType.ON_RESUME);
				btnLot.setText(getDefaultLOT());
				ContractDefaultSetting contractDefaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(app.getSelectedContract().strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
				if (enableSlippage) {
					btnSlippage.setText(contractDefaultSetting.getDefaultSlippage().toString());
				}
				if (CompanySettings.newinterface) {
					tbLimit.setChecked(true, contractDefaultSetting.getDefaultTakeProfitOrderPips().isPresent());
					tbStop.setChecked(true, contractDefaultSetting.getDefaultStopLossOrderPips().isPresent());
				}
				else{
					tbLimit0.setChecked(true, contractDefaultSetting.getDefaultTakeProfitOrderPips().isPresent());
					tbStop0.setChecked(true, contractDefaultSetting.getDefaultStopLossOrderPips().isPresent());
				}
				updatePopLot();
				updateUI();
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
				
				
				if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(app.getSelectedContract().strContractCode) )
				{
					if(m_BuySellStrengthTimer != null)
						m_BuySellStrengthTimer.cancel();
					CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "close");
					
					BuySellStrengthPriceAdjustSetting setting = MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.get(app.getSelectedContract().strContractCode);
					if( setting.openPosWaitSec > 0 || setting.openPosWaitSec < 0 )
					{
						CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), app.getSelectedContract().strContractCode, app.getSelectedBuySell(), "open");
					}
					else
					{
						CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), app.getSelectedContract().strContractCode, app.getSelectedBuySell(), "open_pending");
					}
					
					if( setting.openPosWaitSec > 0 )
					{
						m_BuySellStrengthTimer = new Timer();
		            	m_BuySellStrengthTimer.schedule(new TimerTask()
		            			{
		    						@Override
		    						public void run() {
		    							CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "pending");							
		    						}
		            		
		            			}, setting.openPosWaitSec * 1000);
					}
				}
				
				updateOrderRate(UpdateType.ON_RESUME);
				updateUI();
			}
        });
		
		btnSubmit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				/*
				CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, app.getSelectedContract(), app.getSelectedBuySell(), btnLot.getText().toString());
				Toast.makeText(v.getContext(), "Deal request sent", Toast.LENGTH_LONG).show();
				DealActivity.this.finish();
				*/
				if ( !app.getPriceAgentConnectionStatus() ) {
					Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "307" ,app.locale), Toast.LENGTH_LONG).show();
					return;
				}
				
				AlertDialog dialog = new AlertDialog.Builder(DealActivity.this, CompanySettings.alertDialogTheme).create();
				dialog.setMessage(res.getText(R.string.are_you_sure));
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {										
							String sLPrice = "-1";
							String sLGT = "0";
							String sSPrice = "-1";
							String sSGT = "0";
							
							if (  ((Button)findViewById(R.id.btnLot)).getText().toString().equals("0.0")) {
								Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "616" ,app.locale), Toast.LENGTH_LONG).show();
			                    return;
							}

							boolean limit = false;
							boolean stop = false;

							if (CompanySettings.newinterface){
								limit = tbLimit.isChecked();
								stop = tbStop.isChecked();
							}
							else{
								limit = tbLimit0.isChecked();
								stop = tbStop0.isChecked();
							}

							
							if(limit){
								sLPrice = btnLimit.getText().toString();
								
								if( CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel) )
								{
									if( btnLimitGT.getText().equals(getResources().getString(R.string.tb_cancel)))
										sLGT = "1";
									else if( btnLimitGT.getText().equals(getResources().getString(R.string.tb_end_of_day)))
										sLGT = "2";
									else
										sLGT = "0";
								}
								else
								{
									if(tbLimitGT.isChecked())
										sLGT = "2";
								}
								
								if (Utility.toDouble(sLPrice, 0) <= 0) {
				                    Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
				                } else {
									String tv = ">";
									if (((TextView) findViewById(R.id.tvLDirect)).getText().toString().equals(tv)) {
										if (Utility.toDouble(sLPrice, 0) <= Utility.toDouble(((TextView) findViewById(R.id.tvLimit)).getText().toString(), 0)) {
											Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "615", app.locale), Toast.LENGTH_LONG).show();
											return;
										}
									} else {
										if (Utility.toDouble(sLPrice, 0) >= Utility.toDouble(((TextView) findViewById(R.id.tvLimit)).getText().toString(), 0)) {
											Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "615", app.locale), Toast.LENGTH_LONG).show();
											return;
										}
									}
				                }
								
								
							}
							
							if(stop){
								sSPrice = btnStop.getText().toString();
								
								if( CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel) )
								{
									if( btnStopGT.getText().equals(getResources().getString(R.string.tb_cancel)))
										sSGT = "1";
									else if( btnStopGT.getText().equals(getResources().getString(R.string.tb_end_of_day)))
										sSGT = "2";
									else
										sSGT = "0";
								}
								else
								{
									if(tbStopGT.isChecked())
										sSGT = "2";
								}
								
								if (Utility.toDouble(sSPrice, 0) <= 0) {
				                    Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
				                    return;
				                } else {
									String tv = ">";
				                	if (((TextView)findViewById(R.id.tvSDirect)).getText().toString().equals(tv)){
				                		if ( Utility.toDouble( ((TextView)findViewById(R.id.tvStop)).getText().toString() , 0)  <= Utility.toDouble(sSPrice, 0) ) {
				                			Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	} else {
				                		if ( Utility.toDouble( ((TextView)findViewById(R.id.tvStop)).getText().toString() , 0)  >= Utility.toDouble(sSPrice, 0) ) {
				                			Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "615" ,app.locale), Toast.LENGTH_LONG).show();
						                    return;
				                		}
				                	}
				                }
							}	
							
							if( CompanySettings.VALIDATE_MINLOT_AND_INCLOT == true )
							{
								if( app.getSelectedContract().dMinLot > 0 )
								{
									if( Double.valueOf(btnLot.getText().toString()) < app.getSelectedContract().dMinLot){
			                			Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "620" ,app.locale).replaceFirst("#s", String.valueOf(app.getSelectedContract().dMinLot))   , Toast.LENGTH_LONG).show();
					                    return;
									}
								}
								else
								{
									if( Double.valueOf(btnLot.getText().toString()) < app.data.getBalanceRecord().dMinLotLimit){
			                			Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "620" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotLimit))   , Toast.LENGTH_LONG).show();
					                    return;
									}
								}
							}
							
							if( CompanySettings.VALIDATE_MINLOT_AND_INCLOT == true )
							{
								if( app.getSelectedContract().dIncLot > 0 )
								{
									if  (((1000 * Double.valueOf(btnLot.getText().toString())) % (1000 * app.getSelectedContract().dIncLot)) != 0){
										Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(app.getSelectedContract().dIncLot))   , Toast.LENGTH_LONG).show();
										return;
									}
								}
								else
								{
									if  (((1000 * Double.valueOf(btnLot.getText().toString())) % (1000 * app.data.getBalanceRecord().dMinLotIncrementUnit)) != 0){
										Toast.makeText(DealActivity.this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotIncrementUnit))   , Toast.LENGTH_LONG).show();
										return;
									}
								}
							}
							
							pid = CommonFunction.getTransactionID(app.data.getBalanceRecord().strAccount);
							if (app.getSelectedContract().enableDepth.equals("1")){
								TextView tvTmp = (TextView)findViewById(R.id.tvRate);
								CommonFunction.sendDealAndOrderRequestDepth(
										mService, mServiceMessengerHandler, app.getSelectedContract(),
										app.getSelectedBuySell(), btnLot.getText().toString(),
										sLPrice, sLGT, sSPrice, sSGT, btnSlippage.getText().toString(),
										pid, tvTmp.getText().toString(),  Long.toString(app.dServerDateTime.getTime())
								);
							}
							else {
								CommonFunction.sendDealAndOrderRequest(
										mService, mServiceMessengerHandler, app.getSelectedContract(),
										app.getSelectedBuySell(), btnLot.getText().toString(),
										sLPrice, sLGT, sSPrice, sSGT, btnSlippage.getText().toString(),
										pid, Long.toString(app.dServerDateTime.getTime())
								);
							}
							
							
							/*
	                        if(cbLimit.isSelected()){
	                        	requestMsg.addField("lprice", txtLimitPrice.getText().toString());
	                        	
	                        	JLabel tmpSelectedGoodTill = (JLabel) cobLimitGoodTill.getSelectedItem();
	                            if (tmpSelectedGoodTill == lblLimitDayEnd)
	                                requestMsg.addField("lgoodtil", "2");
	                            else if (tmpSelectedGoodTill == lblLimitCancel)
	                                requestMsg.addField("lgoodtil", "1");
	                            else
	                                requestMsg.addField("lgoodtil", "0");                            
	                        }else{
	                        	requestMsg.addField("lprice", "-1");
	                        }
	                        
	                        if(cbStop.isSelected()){
	                        	requestMsg.addField("sprice", txtStopPrice.getText().toString());
	                        	
	                        	JLabel tmpSelectedGoodTill = (JLabel) cobStopGoodTill.getSelectedItem();
	                            if (tmpSelectedGoodTill == lblStopDayEnd)
	                                requestMsg.addField("sgoodtil", "2");
	                            else if (tmpSelectedGoodTill == lblStopCancel)
	                                requestMsg.addField("sgoodtil", "1");
	                            else
	                                requestMsg.addField("sgoodtil", "0");                            
	                        }else{
	                        	requestMsg.addField("sprice", "-1");
	                        } 							
							*/
							
							if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(app.getSelectedContract().strContractCode))
							{
								if(m_BuySellStrengthTimer != null)
									m_BuySellStrengthTimer.cancel();
								CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "close");
							}
							
							Toast.makeText(DealActivity.this, res.getString(R.string.msg_request_sent), Toast.LENGTH_LONG).show();
							if( CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST == true ) 
								DealActivity.this.finish();							
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
		});
		
		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(app.getSelectedContract().strContractCode))
				{
					if(m_BuySellStrengthTimer != null)
						m_BuySellStrengthTimer.cancel();
					CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "close");
				}
				DealActivity.this.finish();
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
				updateUI();
				popLot.dismiss();
			}
			
		});
		
		popLot.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popLot.dismiss();
			}			
		});	
		
		btnSlippage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				popSlippage.showLikeQuickAction();
				popSlippage.setValue(btnSlippage.getText().toString());
			}
			
		});	
		
		popSlippage.btnCommit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				btnSlippage.setText(popSlippage.getValue());
				popSlippage.dismiss();
			}
			
		});
		
		popSlippage.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popSlippage.dismiss();
			}			
		});	
		
		btnLimit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popLRate.upateRate(btnLimit.getText().toString(), tvLimit.getText().toString());				
				popLRate.showLikeQuickAction();
			}
			
		});	
		
		popLRate.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btnLimit.setText(popLRate.getValue());
				popLRate.dismiss();
			}			
		});
		
		popLRate.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popLRate.dismiss();
			}			
		});	
		
		btnStop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popSRate.upateRate(btnStop.getText().toString(), tvStop.getText().toString());				
				popSRate.showLikeQuickAction();
			}
			
		});	
		
		popSRate.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btnStop.setText(popSRate.getValue());
				popSRate.dismiss();
			}			
		});
		
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
	
	@Override
	public void handleByChild(Message msg) {
		
	}

	public void setLimitLayout(){
		if (tbLimit.isChecked()){
			btnLimit.setEnabled(true);
			btnLimit.setAlpha((float)1);
			tbLimitGT.tb.setEnabled(true);
			tbLimitGT.tb.setAlpha((float)1);
			tvLimit.setAlpha((float)1);
			tvLDirect.setAlpha((float)1);
			label_limit.setAlpha((float)1);
			label_lgt.setAlpha((float)1);
		}
		else {
			btnLimit.setEnabled(false);
			btnLimit.setAlpha((float)0.5);
			tbLimitGT.tb.setEnabled(false);
			tbLimitGT.tb.setAlpha((float)0.5);
			tvLimit.setAlpha((float)0.5);
			tvLDirect.setAlpha((float)0.5);
			label_limit.setAlpha((float)0.5);
			label_lgt.setAlpha((float)0.5);
		}
	}

	public void setStopLayout(){
		if (tbStop.isChecked()){
			btnStop.setEnabled(true);
			btnStop.setAlpha((float)1);
			tbStopGT.tb.setEnabled(true);
			tbStopGT.tb.setAlpha((float)1);
			tvStop.setAlpha((float)1);
			tvSDirect.setAlpha((float)1);
			label_stop.setAlpha((float)1);
			label_sgt.setAlpha((float)1);
		}
		else {
			btnStop.setEnabled(false);
			btnStop.setAlpha((float)0.5);
			tbStopGT.tb.setEnabled(false);
			tbStopGT.tb.setAlpha((float)0.5);
			tvStop.setAlpha((float)0.5);
			tvSDirect.setAlpha((float)0.5);
			label_stop.setAlpha((float)0.5);
			label_sgt.setAlpha((float)0.5);
		}
	}

	@Override
	public void loadLayout() {
		if (CompanySettings.newinterface)
			setContentView(R.layout.v_market_deal_new);
		else
			setContentView(R.layout.v_market_deal);
		
		boolean enableOCO = false;
		if(app.isDemoPlatform == true && CompanySettings.ENABLE_DEAL_OCO_DEMO == true)
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 1 && CompanySettings.ENABLE_DEAL_OCO == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 2 && CompanySettings.ENABLE_DEAL_OCO_PROD2 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 3 && CompanySettings.ENABLE_DEAL_OCO_PROD3 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 4 && CompanySettings.ENABLE_DEAL_OCO_PROD4 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 5 && CompanySettings.ENABLE_DEAL_OCO_PROD5 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 6 && CompanySettings.ENABLE_DEAL_OCO_PROD6 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 7 && CompanySettings.ENABLE_DEAL_OCO_PROD7 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 8 && CompanySettings.ENABLE_DEAL_OCO_PROD8 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 9 && CompanySettings.ENABLE_DEAL_OCO_PROD9 == true )
			enableOCO = true;
		else if( CompanySettings.checkProdServer() == 10 && CompanySettings.ENABLE_DEAL_OCO_PROD10 == true )
			enableOCO = true;
		
		if(enableOCO && (CompanySettings.ALLOW_STP_ORDER == true || app.data.getBalanceRecord().hedged == false) ){
			findViewById(R.id.dealocoll1).setVisibility(View.VISIBLE);
			findViewById(R.id.dealocoll2).setVisibility(View.VISIBLE);
			if (!CompanySettings.newinterface)
				findViewById(R.id.dealocoll3).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.dealocoll1).setVisibility(View.GONE);
			findViewById(R.id.dealocoll2).setVisibility(View.GONE);
			if (!CompanySettings.newinterface)
				findViewById(R.id.dealocoll3).setVisibility(View.GONE);
		}

		if(enableSlippage){
			findViewById(R.id.llslippage).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.llslippage).setVisibility(View.GONE);		
		}
		
        btnContract = (Button)findViewById(R.id.btnContract);
        popupContractList = app.data.getTradableContractList();
        popContract = new PopupContract(getApplicationContext(), findViewById(R.id.rlTop), popupContractList);
        btnContract.setText(app.getSelectedContract().getContractName(getLanguage()));

        tbBuySell = new IPhoneToggle((ToggleButton)findViewById(R.id.tbBuySell), res, R.string.lb_buy, R.string.lb_sell);
        if (CompanySettings.newinterface) {
			tbLimit = new IPhoneCheckbox((ToggleButton) findViewById(R.id.tbLimit), res, R.string.tb_limit, R.string.tb_limit);
			tbStop = new IPhoneCheckbox((ToggleButton) findViewById(R.id.tbStop), res, R.string.tb_stop, R.string.tb_stop);
			view_limit = (RelativeLayout)findViewById(R.id.view_limit_deal);
			view_stop = (RelativeLayout)findViewById(R.id.view_stop_deal);
			label_limit = (TextView)findViewById(R.id.label_limit_deal);
			label_stop = (TextView)findViewById(R.id.label_stop_deal);
			label_lgt = (TextView)findViewById(R.id.label_lgt_deal);
			label_sgt = (TextView)findViewById(R.id.label_sgt_deal);
		}
		else{
			tbLimit0 = new IPhoneToggle((ToggleButton)findViewById(R.id.tbLimit), res, R.string.tb_limit, R.string.tb_limit);
			tbStop0 = new IPhoneToggle((ToggleButton)findViewById(R.id.tbStop), res, R.string.tb_stop, R.string.tb_stop);
		}
        btnLimitGT = (Button)findViewById(R.id.btnLimitGT);
    	btnStopGT = (Button)findViewById(R.id.btnStopGT);
    	tbLimitGT = new IPhoneToggle((ToggleButton)findViewById(R.id.tbLimitGT), res, R.string.tb_end_of_day, R.string.tb_friday);
    	tbStopGT = new IPhoneToggle((ToggleButton)findViewById(R.id.tbStopGT), res, R.string.tb_end_of_day, R.string.tb_friday);
        if( CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel) )
        {
        	tbLimitGT.setVisiblity(View.GONE);
        	tbStopGT.setVisiblity(View.GONE);
			if (CompanySettings.newinterface) {
				findViewById(R.id.btnTGGTL).setVisibility(View.GONE);
				findViewById(R.id.btnTGGTS).setVisibility(View.GONE);
			}
        	ArrayList<String> alGoodTill = new ArrayList<String>();
        	alGoodTill.add(getResources().getString(R.string.tb_cancel));
        	alGoodTill.add(getResources().getString(R.string.tb_end_of_day));
        	alGoodTill.add(getResources().getString(R.string.tb_friday));
        	popGoodTill = new PopupGoodTill(getApplicationContext(), findViewById(R.id.rlTop), alGoodTill);
        }
        else
        {
        	btnLimitGT.setVisibility(View.GONE);
        	btnStopGT.setVisibility(View.GONE);
        }
        
        btnLimit = (Button)findViewById(R.id.btnLimit);
        btnStop = (Button)findViewById(R.id.btnStop);
        
        tvLimit = (TextView)findViewById(R.id.tvLimit);
        tvStop = (TextView)findViewById(R.id.tvStop);
        tvLDirect =  (TextView)findViewById(R.id.tvLDirect);
        tvSDirect =  (TextView)findViewById(R.id.tvSDirect);
		limitViewHolder = new GUIUtilityUpdateRateViewHolder(tvLimit, tvLDirect, btnLimit, null);
		stopViewHolder = new GUIUtilityUpdateRateViewHolder(tvStop, tvSDirect, btnStop, null);
        if(CompanySettings.newinterface) {
			tbLimit.setChecked(true, false);
			tbStop.setChecked(true, false);
			setLimitLayout();
			setStopLayout();
		}
		else{
			tbLimit0.setChecked(true, false);
			tbStop0.setChecked(true, false);
		}

        if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DailyOnly)){
        	tbLimitGT.setChecked(true, true);
        	tbLimitGT.tb.setEnabled(false);
        	tbStopGT.setChecked(true, true);
        	tbStopGT.tb.setEnabled(false);
        }else if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.FridayOnly)){
        	tbLimitGT.setChecked(true, false);
        	tbLimitGT.tb.setEnabled(false);
        	tbStopGT.setChecked(true, false);
        	tbStopGT.tb.setEnabled(false);
        }else if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel)){
        	btnLimitGT.setText(getResources().getString(R.string.tb_cancel));
        	btnStopGT.setText(getResources().getString(R.string.tb_cancel));
        	
        }else{
        	if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultDaily)){
	        	tbLimitGT.setChecked(true, true);
	        	tbStopGT.setChecked(true, true);
        	}else{
	        	tbLimitGT.setChecked(true, false);
	        	tbStopGT.setChecked(true, false);
        	}
        }
        
		btnLot = (Button)findViewById(R.id.btnLot);
		btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnSlippage = (Button)findViewById(R.id.btnSlippage);

		final View btnCancel = findViewById(R.id.btnCancel);
		if (CompanySettings.dealTimeOut > 0 && btnCancel != null) {
			timeout = new CountDownTimer(CompanySettings.dealTimeOut * 1000, 1000) {

				public void onTick(long millisUntilFinished) {
					String timer = getResources().getString(R.string.btn_submit) + " (" + millisUntilFinished / 1000 + ")";
					btnSubmit.setText(timer);
				}

				public void onFinish() {
					//checking null to handle exception case for logout case
					btnCancel.callOnClick();
				}
			}.start();
		}

		ContractObj contract = app.getSelectedContract();
		popLot = new PopupLOT(getApplicationContext(), findViewById(R.id.rlTop), BigDecimal.valueOf(contract.getFinalMaxLotLimit()));
		popLRate = new PopupRate(getApplicationContext(), findViewById(R.id.rlTop));
		popSRate = new PopupRate(getApplicationContext(), findViewById(R.id.rlTop));
		popSlippage = new PopupSlippage(getApplicationContext(), findViewById(R.id.rlTop));
		
		updateBuySell();	
	}
	
	public void updateBuySell(){	
		if(app.getSelectedBuySell() == null || "B".equals(app.getSelectedBuySell())){
			tbBuySell.setChecked(true, true);
		}else{
			tbBuySell.setChecked(true, false);	
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

		if(contract == null){
			return;
		}

		String sBuySell = app.getSelectedBuySell();		
		if(sBuySell == null){
			return;
		}
		
		String sTmp = null;

		if (contract.enableDepth.equals("1")){
			double lot = Double.valueOf(btnLot.getText().toString());
			double [] bidLot = contract.bidLotDepth;
			double [] askLot = contract.askLotDepth;

			if (("B".equals(sBuySell) && contract.getBSD() == false) || ("S".equals(sBuySell) && contract.getBSD() == true)) {
				sTmp = Utility.round(GUIUtility.getMax(contract.askPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
				double accLot = 0;
				for (int i = 0 ; i < askLot.length ; i++){
					accLot += askLot[i];
					if (lot <= accLot) {
						sTmp = Utility.round(contract.askPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
						break;
					}
				}
				sTmp = String.valueOf(Utility.roundToDouble(Double.parseDouble(sTmp) + (contract.dOffset + contract.dAskAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt));
			} else {
				sTmp = Utility.round(GUIUtility.getMin(contract.bidPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
				double accLot = 0;
				for (int i = 0 ; i < bidLot.length ; i++){
					accLot += bidLot[i];
					if (lot <= accLot) {
						sTmp = Utility.round(contract.bidPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
						break;
					}
				}
				sTmp = String.valueOf(Utility.roundToDouble(Double.parseDouble(sTmp) + (contract.dOffset + contract.dBidAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt));
			}
		}
		else {
			if (("B".equals(sBuySell) && contract.getBSD() == false) || ("S".equals(sBuySell) && contract.getBSD() == true)) {
				sTmp = Utility.round(contract.getBidAsk()[1], contract.iRateDecPt, contract.iRateDecPt);
			} else {
				sTmp = Utility.round(contract.getBidAsk()[0], contract.iRateDecPt, contract.iRateDecPt);
			}
		}
		
		TextView tvTmp = (TextView)findViewById(R.id.tvRate);
		tvTmp.setText(sTmp);
		
		if(contract.bChangeBidAsk){
			ColorController.setPriceColor(res, contract.iBidUpDown, tvTmp);
		}
		else{
			ColorController.setPriceColor(res, 0, tvTmp);
		}
	      
		BigDecimal dLot = Utility.toBigDecimal(btnLot.getText().toString(), "0");
		tvTmp = (TextView)findViewById(R.id.tvAmount);
		sTmp = Utility.formatAmount(dLot.multiply(new BigDecimal(String.valueOf(contract.iContractSize))));
		tvTmp.setText(sTmp);

		if (contract.enableDepth.equals("1")) {
			double lot = Double.valueOf(btnLot.getText().toString());
			updateOrderRateDepth(false, lot);
		}
		else
			updateOrderRate(UpdateType.INCOMING_PRICE);
	      
	      if( pid != null && CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST == false)
	      {
	    	  TransactionObj t = app.data.getTransaction(pid);
	    	  if( t != null && t.iStatusMsg == 916 )
	    	  {
	    		  pid = null;
	    		  DealActivity.this.finish();
	    		  goTo(ServiceFunction.SRV_OPEN_POSITION);
	    	  }
	    	  else if( t != null && t.iStatusMsg != 917 && t.iStatusMsg != -1)
			  {
				  pid = null;
			  		if (!CompanySettings.newinterface){
						DealActivity.this.finish();
					}
	    	  }
//	    	  else if (t == null){
//				  DealActivity.this.finish();
//				  goTo(ServiceFunction.SRV_TRANSACTION);
//			  }
	      }
	}

	@Override
	protected void onDestroy() {
		if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(app.getSelectedContract().strContractCode))
		{
			if(m_BuySellStrengthTimer != null)
				m_BuySellStrengthTimer.cancel();
			CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "close");
		}
		if (timeout != null) {
			try {
				timeout.cancel();
			} catch (Exception ex) {

			}
		}
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(app.getSelectedContract() == null){
			if (app.getDefaultContract()==null)
				return;
			app.setSelectedContract(app.getDefaultContract());
		}
		ContractDefaultSetting contractDefaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(app.getSelectedContract().strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
		if(enableSlippage)
		{
			btnSlippage.setText(contractDefaultSetting.getDefaultSlippage().toString());
		}
		
		btnContract.setText(app.getSelectedContract().getContractName(getLanguage()));
		btnLot.setText(getDefaultLOT());
		updateBuySell();

		if (CompanySettings.newinterface) {
			tbLimit.setChecked(true, contractDefaultSetting.getDefaultTakeProfitOrderPips().isPresent());
			tbStop.setChecked(true, contractDefaultSetting.getDefaultStopLossOrderPips().isPresent());
		}
		else{
			tbLimit0.setChecked(true, contractDefaultSetting.getDefaultTakeProfitOrderPips().isPresent());
			tbStop0.setChecked(true, contractDefaultSetting.getDefaultStopLossOrderPips().isPresent());
		}
		setLimitLayout();
		setStopLayout();

        if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DailyOnly)){
        	tbLimitGT.setChecked(true, true);
        	tbLimitGT.tb.setEnabled(false);
        	tbStopGT.setChecked(true, true);
        	tbStopGT.tb.setEnabled(false);
        }else if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.FridayOnly)){
        	tbLimitGT.setChecked(true, false);
        	tbLimitGT.tb.setEnabled(false);
        	tbStopGT.setChecked(true, false);
        	tbStopGT.tb.setEnabled(false);
        }else if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultCancel)){
        	
        }else{
        	if(CompanySettings.ORDER_TIME_LIMIT.equals(OrderTimeLimit.DefaultDaily)){
	        	tbLimitGT.setChecked(true, true);
	        	tbStopGT.setChecked(true, true);
        	}else{
	        	tbLimitGT.setChecked(true, false);
	        	tbStopGT.setChecked(true, false);
        	}
        }
        
        updateOrderRate(UpdateType.ON_RESUME);
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(app.getSelectedContract().strContractCode))
		{
			if(m_BuySellStrengthTimer == null)
			{
				BuySellStrengthPriceAdjustSetting setting = MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.get(app.getSelectedContract().strContractCode);
				if( setting.openPosWaitSec > 0 || setting.openPosWaitSec < 0 )
				{
					CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), app.getSelectedContract().strContractCode, app.getSelectedBuySell(), "open");
				}
				else
				{
					CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), app.getSelectedContract().strContractCode, app.getSelectedBuySell(), "open_pending");
				}
				
				if( setting.openPosWaitSec > 0 )
				{
					m_BuySellStrengthTimer = new Timer();
					m_BuySellStrengthTimer.schedule(new TimerTask()
            			{
    						@Override
    						public void run() {
    							CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "pending");							
    						}
            		
            			}, setting.openPosWaitSec * 1000);
				}
			}
		}
	}
	
	public void updateOrderRate(UpdateType updateType){
        String sOrderBuySell = "B";
        try{
            if(app.getSelectedBuySell().equals("B")){
            	sOrderBuySell = "S";
            }

			ContractObj contract = app.getSelectedContract();
			ContractDefaultSetting defaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(contract.strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
			switch (updateType) {
				case INCOMING_PRICE:
					GUIUtility.updateRate(false, contract, sOrderBuySell, 0, limitViewHolder, res, defaultSetting);
					GUIUtility.updateRate(false, contract, sOrderBuySell, 1, stopViewHolder, res, defaultSetting);
					break;
				case USER_CHANGE_INPUT:
					GUIUtility.updateRate(true, contract, sOrderBuySell, 0, limitViewHolder, res, defaultSetting);
					GUIUtility.updateRate(true, contract, sOrderBuySell, 1, stopViewHolder, res, defaultSetting);
					break;
				case ON_RESUME:
					String sRate;
					sRate = GUIUtility.updateRate(false, contract, sOrderBuySell, 0, limitViewHolder, res, defaultSetting);
					btnLimit.setText(sRate);
					sRate = GUIUtility.updateRate(false, contract, sOrderBuySell, 1, stopViewHolder, res, defaultSetting);
					btnStop.setText(sRate);
					break;
			}
            if(tvSDirect.getText().toString().equals(">")){
            	tvSDirect.setText("<");
            }else{
            	tvSDirect.setText(">");
            }        	
        }catch(Exception e){
        	e.printStackTrace();
        }
	}

	public void updateOrderRateDepth(boolean bUpdateButton, double lot){
		String sOrderBuySell = "B";
		try{
			if(app.getSelectedBuySell().equals("B")){
				sOrderBuySell = "S";
			}

			GUIUtility.updateRateDepth(bUpdateButton, app.getSelectedContract(), sOrderBuySell, 0, tvLimit, tvLDirect, btnLimit, res, String.valueOf(lot));
			GUIUtility.updateRateDepth(bUpdateButton, app.getSelectedContract(), sOrderBuySell, 1, tvStop, tvSDirect, btnStop, res, String.valueOf(lot));

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
		return ServiceFunction.SRV_OPEN_POSITION;
	}

}