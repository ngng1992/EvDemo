package com.mfinance.everjoy.app;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.os.IBinder;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.MobileTraderApplication.BuySellStrengthPriceAdjustSetting;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.ContractDefaultSetting;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.wheel.WheelView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LiquidationActivity extends BaseActivity{
	String pid = null;
	Button btnContract;
	Button btnLot;
	Button btnSubmit;
	Button btnSlippage;
	Timer m_BuySellStrengthTimer = null;
	private int selectedPositionId = -1;
	
	PopupLOT popLot;
	PopupOpenPosition popPosition;
	PopupSlippage popSlippage;

	/**
	 *
	 * @return open position from selectedPositionId. return null if open position is not found.
	 */
	private OpenPositionRecord getOpenPositionRecord() {
		return app.data.getOpenPosition(selectedPositionId);
	}
	
	@Override
	public void bindEvent() {
		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				OpenPositionRecord position = app.getSelectedOpenPosition();
				if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(position.strContract) )
				{
					if(m_BuySellStrengthTimer != null)
						m_BuySellStrengthTimer.cancel();
					CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "close");
				}
				finish();
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
				TextView tvTmp = (TextView)findViewById(R.id.tvOldLot);
				double dTmp = Double.parseDouble(tvTmp.getText().toString());
				double dLot = Double.parseDouble(popLot.getValue());
				if( dLot > dTmp )
				{
					btnLot.setText(tvTmp.getText().toString());
				}
				else
				{
					btnLot.setText(popLot.getValue());
				}
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
		
		btnSubmit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				/*
				OpenPositionRecord position = app.getSelectedOpenPosition();
				CommonFunction.sendLiquidationRequest(mService, mServiceMessengerHandler, position, btnLot.getText().toString());
				Toast.makeText(v.getContext(), "Liquidation request sent", Toast.LENGTH_LONG).show();				
				LiquidationActivity.this.finish();
				*/
				if ( !app.getPriceAgentConnectionStatus() ) {
					Toast.makeText(LiquidationActivity.this, MessageMapping.getMessageByCode(res, "307" ,app.locale), Toast.LENGTH_LONG).show();
					return;
				}

				AlertDialog dialog = new AlertDialog.Builder(LiquidationActivity.this, CompanySettings.alertDialogTheme).create();
				dialog.setMessage(res.getText(R.string.are_you_sure));							
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {	
							OpenPositionRecord position = app.getSelectedOpenPosition();
							if (  ((Button)findViewById(R.id.btnLot)).getText().toString().equals("0.0")) {
								Toast.makeText(LiquidationActivity.this, MessageMapping.getMessageByCode(res, "616" ,app.locale), Toast.LENGTH_LONG).show();
			                    return;
							}
							
							double dMinLot = app.data.getContract(position.strContract).dMinLot;
							if( CompanySettings.VALIDATE_MINLOT_AND_INCLOT == true )
							{
								if( dMinLot > 0 )
								{
									if( Double.valueOf(btnLot.getText().toString()) < dMinLot){
			                			Toast.makeText(LiquidationActivity.this, MessageMapping.getMessageByCode(res, "620" ,app.locale).replaceFirst("#s", String.valueOf(dMinLot))   , Toast.LENGTH_LONG).show();
					                    return;
									}
								}
								else
								{
									if( Double.valueOf(btnLot.getText().toString()) < app.data.getBalanceRecord().dMinLotLimit){
			                			Toast.makeText(LiquidationActivity.this, MessageMapping.getMessageByCode(res, "620" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotLimit))   , Toast.LENGTH_LONG).show();
					                    return;
									}
								}
							}
							
							double dIncLot = app.data.getContract(position.strContract).dIncLot;
							if( CompanySettings.VALIDATE_MINLOT_AND_INCLOT == true )
							{
								if( dIncLot > 0 )
								{
									if  (((1000 * Double.valueOf(btnLot.getText().toString())) % (1000 * dIncLot)) != 0){
										Toast.makeText(LiquidationActivity.this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(dIncLot))   , Toast.LENGTH_LONG).show();
										return;
									}
								}
								else
								{
									if  (((1000 * Double.valueOf(btnLot.getText().toString())) % (1000 * app.data.getBalanceRecord().dMinLotIncrementUnit)) != 0){
										Toast.makeText(LiquidationActivity.this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotIncrementUnit))   , Toast.LENGTH_LONG).show();
										return;
									}
								}
							}
							
							pid = CommonFunction.getTransactionID(app.data.getBalanceRecord().strAccount);
							CommonFunction.sendLiquidationRequest(mService, mServiceMessengerHandler, position, btnLot.getText().toString(), btnSlippage.getText().toString(), pid, Long.toString(app.dServerDateTime.getTime()));
							Toast.makeText(LiquidationActivity.this, res.getString(R.string.msg_request_sent), Toast.LENGTH_LONG).show();
							if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(position.strContract))
							{
								if(m_BuySellStrengthTimer != null)
									m_BuySellStrengthTimer.cancel();
								CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "close");
							}
							
							if( CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST == true )
								LiquidationActivity.this.finish();		
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
		
		btnContract.setOnClickListener((v) -> {
			Map<Integer, OpenPositionRecord> openPositions;
			if (getIntent().getBooleanExtra("showAllPosition", true)) {
				openPositions = app.data.getOpenPositions();
			} else {
				Predicate<OpenPositionRecord> filter = position -> Objects.equals(position.strContract, getIntent().getStringExtra("contract")) && Objects.equals(position.isBuyOrder, getIntent().getBooleanExtra("isBuy", false));
				openPositions = app.data.getOpenPositions().values().stream()
						.filter(filter)
						.collect(Collectors.toMap(position -> position.iRef, position -> position));
			}
			if (openPositions.size() <= 0) {
				Toast.makeText(this, res.getString(R.string.msg_no_deal_available), Toast.LENGTH_SHORT).show();
				return;
			}
			popPosition.updateSelectedPositionIndex(openPositions, app.getSelectedOpenPosition().iRef);
			popPosition.showLikeQuickAction();
		});
		
		
		popPosition.btnCommit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				selectedPositionId = Utility.toInteger(popPosition.getValue(), -1);
				app.setSelectedOpenPosition(selectedPositionId);
				OpenPositionRecord position = getOpenPositionRecord();
				
				if(position != null)
					btnLot.setText(Utility.formatLot(position.dAmount / position.contract.iContractSize));
				
				
				if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(position.strContract))
				{
					if(m_BuySellStrengthTimer != null)
						m_BuySellStrengthTimer.cancel();
					CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "close");
					
					BuySellStrengthPriceAdjustSetting setting = MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.get(position.strContract);
					if( setting.liqWaitSec > 0 || setting.liqWaitSec < 0 )
					{
						CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), position.strContract, position.isBuyOrder ? "S" : "B", "open");
					}
					else
					{
						CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), position.strContract, position.isBuyOrder ? "S" : "B", "open_pending");
					}
					
					if( setting.liqWaitSec > 0  )
					{
						m_BuySellStrengthTimer = new Timer();
						m_BuySellStrengthTimer.schedule(new TimerTask()
	            			{
	    						@Override
	    						public void run() {
	    							CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "pending");							
	    						}
	            		
	            			}, setting.liqWaitSec * 1000);
					}
				}
				updateUI();
				popPosition.dismiss();
			}		

		});	
		
		popPosition.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				popPosition.dismiss();
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
	}	
	
	@Override
	public void handleByChild(Message msg) {
		
	}

	@Override
	protected void onDestroy() {
		OpenPositionRecord position = getOpenPositionRecord();
		if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && position != null && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(position.strContract))
		{
			if(m_BuySellStrengthTimer != null)
				m_BuySellStrengthTimer.cancel();
			CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "close");
		}
		super.onDestroy();
	}
	
	@Override
	public void loadLayout() {
		if (CompanySettings.newinterface)
			setContentView(R.layout.v_liquidation_new);
		else
			setContentView(R.layout.v_liquidation);
		
		boolean enableSlippage = false;
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
		
		if(enableSlippage){
			findViewById(R.id.llslippage).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.llslippage).setVisibility(View.GONE);		
		}
		
		btnContract = (Button)findViewById(R.id.btnContract);
		btnLot = (Button)findViewById(R.id.btnLot);
		btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnSlippage = (Button)findViewById(R.id.btnSlippage);

		ContractObj contract = app.getSelectedOpenPosition().contract;
		popLot = new PopupLOT(getApplicationContext(), findViewById(R.id.rlTop), BigDecimal.valueOf(contract.getFinalMaxLotLimit()));
		popPosition = new PopupOpenPosition(getApplicationContext(), findViewById(R.id.rlTop),
				new OpenPositionWheelAdapter(LiquidationActivity.this, app.data.getOpenPositions(), mService, mServiceMessengerHandler));
		popSlippage = new PopupSlippage(getApplicationContext(), findViewById(R.id.rlTop));
	}
	
	public void updateSelectedPositionIndex(WheelView wv, OpenPositionWheelAdapter opa, int iRef){
		wv.setCurrentItem(opa.getItemIndex(iRef));
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		selectedPositionId = getIntent().getIntExtra(ServiceFunction.LIQUIDATE_REF, -1);
		OpenPositionRecord position = getOpenPositionRecord();
		if (position != null) {
			btnLot.setText(Utility.formatLot(position.dAmount / position.contract.iContractSize));
		}
		if(CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && position != null && MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.containsKey(position.strContract) )
		{
			if(m_BuySellStrengthTimer == null)
			{
				BuySellStrengthPriceAdjustSetting setting = MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.get(position.strContract);
				if( setting.liqWaitSec > 0 || setting.liqWaitSec < 0 )
				{
					CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), position.strContract, position.isBuyOrder ? "S" : "B", "open");
				}
				else
				{
					CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), position.strContract, position.isBuyOrder ? "S" : "B", "open_pending");
				}
				
				if( setting.liqWaitSec > 0  )
				{
					m_BuySellStrengthTimer = new Timer();
	            	m_BuySellStrengthTimer.schedule(new TimerTask()
	            			{
	    						@Override
	    						public void run() {
	    							CommonFunction.sendDealInputFrameOpenClose(mService, mServiceMessengerHandler, DataRepository.getInstance().getStrUser(), "pending");							
	    						}
	            		
	            			}, setting.liqWaitSec * 1000);
				}
			}
		}
		updateUI();
	}
	
	@Override
	public void updateUI() {
		Locale l = getLanguage();
		
		OpenPositionRecord position = getOpenPositionRecord();
		
		if(position == null){
			return;
		}

		String sRate = null;
		CharSequence sBuySell = position.strBuySell;
		boolean bChangeBidAsk = position.contract.bChangeBidAsk;

		ContractObj contract = position.contract;

		if (contract.enableDepth.equals("1")){
			double lot = Double.valueOf(btnLot.getText().toString());
			double [] bidLot = contract.bidLotDepth;
			double [] askLot = contract.askLotDepth;

			if (("B".equals(sBuySell) && contract.getBSD() == false) || ("S".equals(sBuySell) && contract.getBSD() == true)) {
				sRate = Utility.round(GUIUtility.getMin(contract.bidPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
				double accLot = 0;
				for (int i = 0 ; i < bidLot.length ; i++){
					accLot += bidLot[i];
					if (lot <= accLot) {
						sRate = Utility.round(contract.bidPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
						break;
					}
				}
				sRate = String.valueOf(Utility.roundToDouble(Double.parseDouble(sRate) + (contract.dOffset + contract.dBidAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt));
				sBuySell = res.getText(R.string.lb_sell);
			} else {
				sRate = Utility.round(GUIUtility.getMax(contract.askPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
				double accLot = 0;
				for (int i = 0 ; i < askLot.length ; i++){
					accLot += askLot[i];
					if (lot <= accLot) {
						sRate = Utility.round(contract.askPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
						break;
					}
				}
				sRate = String.valueOf(Utility.roundToDouble(Double.parseDouble(sRate) + (contract.dOffset + contract.dAskAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt));
				sBuySell = res.getText(R.string.lb_buy);
			}
		}

		else {
			if (("B".equals(sBuySell) && position.contract.getBSD() == false) || ("S".equals(sBuySell) && position.contract.getBSD() == true)) {
				sRate = Utility.round(position.contract.getBidAsk()[0], position.contract.iRateDecPt, position.contract.iRateDecPt);
				sBuySell = res.getText(R.string.lb_sell);
			} else {
				sRate = Utility.round(position.contract.getBidAsk()[1], position.contract.iRateDecPt, position.contract.iRateDecPt);
				sBuySell = res.getText(R.string.lb_buy);
			}
		}
		String color, bs;
		if("B".equals(position.strBuySell)){
			color = "#379329";
			bs=Utility.getStringById("lb_buy", getResources());
		}
		else{
			color = "#ff0000";
			bs=Utility.getStringById("lb_sell", getResources());
		}
		String text = "<font color="+color+">"+bs+"</font> <font color=#ffffff>"+position.strContract+ "@" + position.strDealRate+"</font>";

		
		TextView tvTmp = (TextView)findViewById(R.id.tvRate);
		tvTmp.setText(sRate);
		
	      if(bChangeBidAsk){
	    	  ColorController.setPriceColor(res, position.contract.iBidUpDown, tvTmp);
	      }
	      else{	    	  
	    	  ColorController.setPriceColor(res, 0, tvTmp);
	      }
	      
		BigDecimal dLot = Utility.toBigDecimal(btnLot.getText().toString(), "0");
	    tvTmp = (TextView)findViewById(R.id.tvAmount);
		tvTmp.setText(Utility.formatAmount(dLot.multiply(new BigDecimal(String.valueOf(position.contract.iContractSize)))));
		
		tvTmp = (TextView)findViewById(R.id.tvBuySell);
		if (CompanySettings.newinterface)
			tvTmp.setText(Html.fromHtml(text));
		else
			tvTmp.setText(sBuySell);

		tvTmp = (TextView)findViewById(R.id.tvOldLot);
		tvTmp.setText(Utility.formatLot(position.dAmount / position.contract.iContractSize));

		if (CompanySettings.newinterface) {
			tvTmp = (TextView) findViewById(R.id.label_sm_liq);
			tvTmp.setText(Html.fromHtml("&#8804;"));
		}
		
		tvTmp = (TextView)findViewById(R.id.tvPL);		
		tvTmp.setText(Utility.formatValue(position.dFloating));
		ColorController.setNumberColor(res, position.dFloating >= 0, tvTmp);
	
		btnContract.setText(position.contract.getContractName(l) + " "+ position.getViewRef());
		
		if( pid != null && CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST == false)
		{
			TransactionObj t = app.data.getTransaction(pid);
			if( t != null && t.iStatusMsg == 916 )
			{
				pid = null;
				LiquidationActivity.this.finish();
				goTo(ServiceFunction.SRV_LIQUIDATION_HISTORY);
			}
			else if( t != null && t.iStatusMsg != 917 && t.iMsg == 704 )
	    	{
	    		pid = null;
				LiquidationActivity.this.finish();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		boolean enableSlippage = false;
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
		
		if(enableSlippage)
		{
			ContractDefaultSetting contractDefaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(app.getSelectedOpenPosition().contract, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
			btnSlippage.setText(contractDefaultSetting.getDefaultSlippage().toString());
		}
		OpenPositionRecord position = getOpenPositionRecord();
		if (position != null) {
			btnLot.setText(Utility.formatLot(position.dAmount / position.contract.iContractSize));
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
}